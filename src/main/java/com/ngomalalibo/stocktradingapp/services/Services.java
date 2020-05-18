package com.ngomalalibo.stocktradingapp.services;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.dataService.GenericDataService;
import com.ngomalalibo.stocktradingapp.dataService.StockService;
import com.ngomalalibo.stocktradingapp.dataproviders.SortProperties;
import com.ngomalalibo.stocktradingapp.entities.*;
import com.ngomalalibo.stocktradingapp.enums.ActivityLogType;
import com.ngomalalibo.stocktradingapp.enums.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enums.TransactionType;
import com.ngomalalibo.stocktradingapp.exceptions.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exceptions.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Services
{
    private static GenericDataService userGDS = new GenericDataService(new User());
    private static GenericDataService clientGDS = new GenericDataService(new Client());
    private static GenericDataService clientAccountGDS = new GenericDataService(new ClientAccount());
    private static GenericDataService transactionsGDS = new GenericDataService(new ClientTransaction());
    public static boolean loginStatus = false;
    
    //register new client users
    public boolean register(String username, String password, Client client) throws NonUniqueResultException, CustomNullPointerException
    {
        if (client == null)
        {
            throw new CustomNullPointerException("Please provide an existing client in order to create an account");
        }
        if (username == null || password == null)
        {
            throw new CustomNullPointerException("Please provide a valid username or password");
        }
        else if (exists(username))
        {
            throw new NonUniqueResultException("This user exists in the system. Kindly reset your password of choose another username");
        }
        User user = new User(username, PasswordEncoder.getPasswordEncoder().encode(password), "ROLE_USER", username);
        
        client.setEmail(username);
        
        //  confirm registration was successful by retrieving for user and client from database
        client = (Client) client.save(client);
        user = (User) user.save(user);
        
        if (user != null && client != null)
        {
            return true; // user and client creation confirmed
        }
        return false;
    }
    
    
    // login to application with spring security providing form-based authentication and authorization
    public boolean login(String username, String password, AuthenticationManager authenticationManager) throws AuthenticationException
    {
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        
        // try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
        log.info("authenticating -> " + username);
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password)); //
        // if authentication was successful we will update the security context and redirect to the page requested first
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        if (user != null)
        {
            log.info("user roles -> " + user.getRole());
        }
        
        // log access activity
        ActivityLog alog = new ActivityLog();
        alog.setUser(username);
        alog.setActivityLogType(ActivityLogType.INFO);
        alog.setEventName("LOGIN");
        alog.setEventDescription("logged in at "
                                         + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy 'at' h:mm a")));
        alog.save(alog); // persist
        
        loginStatus = true; // set login status to true
        
        return true;
    }
    
    
    // method to fund client account
    public boolean fundAccount(String username, Double deposit) throws CustomNullPointerException
    {
        double newBalance;
        if (Strings.isNullOrEmpty(username))
        {
            throw new CustomNullPointerException("Please provide a valid username to fund account");
        }
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist. No account to fund");
        }
        Client client = (Client) clientGDS.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            // ClientAccount clientAccount = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount); // get balance
            ClientAccount clientAccount = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (clientAccount != null)
            {
                Double balance = clientAccount.getBalance();
                newBalance = balance + deposit; // calculate new balance after funding account
                clientAccount.setBalance(newBalance);
                clientAccount.setPreviousBalance(balance);
                
                ClientAccount returnedClientAccount = clientAccount.replaceEntity(clientAccount, clientAccount);// persist client account funding with update
                
                if (returnedClientAccount != null)
                {
                    if (returnedClientAccount.getPreviousBalance().equals(balance)) // confirm funding was successful
                    {
                        return true; // previous balance in database record is current balance is user record before update
                    }
                }
            }
            
            
        }
        return false;
    }
    
    public Stock getStock(String company) throws CustomNullPointerException
    {
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        return StockService.getStock(company);
    }
    
    // buy stock
    public boolean buy(String company, String username, Integer units) throws CustomNullPointerException, InsufficientCaseException
    {
        if (username == null)
        {
            throw new CustomNullPointerException("Please provide a client to proceed with buying stock");
        }
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to buy stock");
        }
        
        ClientTransaction buy = new ClientTransaction(); // new client transaction: BUY
        Integer unitsOwned = 0;
        
        Client client = new Client();
        Double balance = 0D;
        Double stockPrice = 0D;
        Double cost = 0D;
        
        // get all transactions for this client
        List<ClientTransaction> clientTransactions = transactionsGDS.getRecordsByEntityKey
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
        
        boolean userHasUnits = false;
        if (clientTransactions.size() > 0)
        {
            // check if user already has units of interested stock
            userHasUnits = clientTransactions.stream().map(ClientTransaction::getStock).map(Stock::getSecurityName).allMatch(stock -> stock.equalsIgnoreCase(company));
            
            // get amount of units owned by adding all transactions
            unitsOwned = clientTransactions.stream().map(ClientTransaction::getNoOfUnits).reduce(unitsOwned, Integer::sum);
        }
        
        // get user account object
        // User user = GetObjectByID.getObjectById(username, Connection.user);
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        
        if (user != null)
        {
            // store user client details in map
            // user.getAdditionalProperties().put("client", GetObjectByID.getObjectById(user.getClientID(), Connection.client));
            user.getAdditionalProperties().put("client", (Client) clientGDS.getRecordByEntityProperty("email", user.getClientID()));
            
        }
        
        client = (Client) user.getAdditionalProperties().get("client");
        if (client != null)
        {
            // ClientAccount clientAccount = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount);
            ClientAccount clientAccount = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (clientAccount != null)
            {
                balance = clientAccount.getBalance();
                
                Stock stock = getStock(company); // get company stock
                if (stock != null)
                {
                    stockPrice = stock.getUnitSharePrice(); // get current stock price of company equity
                }
                
                cost = stockPrice * units; // cost of current transaction
                
                if (userHasUnits) // if user already has units of interested company stock add new stock to old stock
                {
                    buy.setNoOfUnits(unitsOwned + units);
                }
                else
                {
                    buy.setNoOfUnits(units);
                }
                // ensure there is enough funds to execute stock purchase
                if (balance >= cost)
                {
                    buy.setTransactionType(TransactionType.BUY); // set type of transaction
                    buy.setStock(new Stock(company, stockPrice)); // store stock purchase detail. price and company
                    buy.setTransactionAmount(cost);
                    buy.setUsername(username); // client making purchase
                    buy.setTransactionStatus(ClientTransactionStatus.BOUGHT);
                    balance -= cost;
                    
                    
                    clientAccount.setPreviousBalance(clientAccount.getBalance());
                    clientAccount.setBalance(balance);
                    
                    ClientAccount returnedClientAccount = clientAccount.replaceEntity(clientAccount, clientAccount);// update balance. replaceEntity takes a records with the same ID and replaces the one in the database one with the other
                    if (returnedClientAccount != null)
                    {
                        buy.save(buy); // persist new transaction
                        return true;
                    }
                }
                else
                {
                    throw new InsufficientCaseException
                            (new RuntimeException("Not enough funds in your account to make this purchase. Kindly fund your account with " + (cost - balance) + " to proceed with acquisition."));
                }
            }
        }
        
        
        return false;
    }
    
    // get unique set of stocks owned by client
    public Set<String> getClientStocks(String username)
    {
        // get all transactions by user
        List<ClientTransaction> allUserTransactions = transactionsGDS.getRecordsByEntityKey
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
        
        return allUserTransactions.stream().map(trans -> trans.getStock().getSecurityName()).collect(Collectors.toSet()); // collect a non-duplicate list of clients securities
    }
    
    // sell stock from portfolio
    public boolean sell(String company, String username, Integer units) throws InsufficientCaseException
    {
        if (username == null)
        {
            throw new CustomNullPointerException("Please provide existing client to proceed with selling stock");
        }
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to selling stock");
        }
        
        ClientTransaction sell = new ClientTransaction(); // new client transaction: SELL
        Integer unitsOwned = 0;
        
        Client client;
        double balance = 0D;
        double stockPrice = 0D;
        double profit = 0D;
        
        // get all transactions for this client
        List<ClientTransaction> clientTransactions = getAllClientTransactions(username);
        
        boolean userHasUnits = false;
        if (clientTransactions.size() > 0)
        {
            // check if user already has units of interested stock
            userHasUnits = clientTransactions.stream().map(ClientTransaction::getStock).map(Stock::getSecurityName).anyMatch(stockName -> stockName.equalsIgnoreCase(company));
            
            // get amount of units owned by adding all transactions
            unitsOwned = clientTransactions.stream().map(ClientTransaction::getNoOfUnits).reduce(unitsOwned, Integer::sum);
        }
        else
        {
            // no transactions to execute sell
            throw new InsufficientCaseException(new RuntimeException("User has no previous transactions. Cannot sell"));
        }
        
        // get user account object
        // User user = GetObjectByID.getObjectById(username, Connection.user);
        User user = (User) userGDS.getRecordByEntityProperty("username", username);  // get object by username
        
        if (user != null)
        {
            // retrieve client details from clientID and store details in map embedded inside user object
            // user.getAdditionalProperties().put("client", GetObjectByID.getObjectById(user.getClientID(), Connection.client));
            user.getAdditionalProperties().put("client", clientGDS.getRecordByEntityProperty("email", user.getClientID()));
            client = (Client) user.getAdditionalProperties().get("client");
            
            if (client != null)
            {
                // ClientAccount ca = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount);
                ClientAccount ca = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
                if (ca != null)
                {
                    
                    balance = ca.getBalance();
                    
                    // get stock price
                    Stock stock = getStock(company);
                    
                    if (stock != null)
                    {
                        stockPrice = stock.getUnitSharePrice(); // get current stock price of company equity
                        profit = stockPrice * units; // profit gained from current sell transaction
                    }
                    
                    
                    if (userHasUnits) // if user already has units of interested company stock add new stock to old stock
                    {
                        sell.setNoOfUnits(unitsOwned - units);
                    }
                    else
                    {
                        throw new InsufficientCaseException(new RuntimeException("User does not own company equity. Cannot sell"));
                    }
                    
                    sell.setTransactionType(TransactionType.SELL);
                    sell.setStock(new Stock(company, stockPrice));
                    sell.setTransactionAmount(profit);
                    sell.setUsername(username);
                    sell.setTransactionStatus(ClientTransactionStatus.SOLD);
                    
                    balance += profit;
                    
                    
                    ca.setPreviousBalance(ca.getBalance());
                    ca.setBalance(balance);
                    
                    // update account balance
                    ClientAccount returnedClientAccount = ca.replaceEntity(ca, ca);
                    
                    // Client returned = (Client) client.save(client); // client collection is not impacted by sell transaction. only transaction and account collections
                    if (returnedClientAccount != null)
                    {
                        // create new transaction
                        sell.save(sell);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public ClientPortfolio getPortfolio(List<ClientTransaction> allClientTransactions, String username) throws CustomNullPointerException, InsufficientCaseException
    {
        ClientPortfolio portfolio = new ClientPortfolio();
        Double totalAmountInvested = 0D;
        AtomicReference<Double> totalValueOfPortfolio = new AtomicReference<>(0D);
        Double profitFromSales = 0D;
        LocalDateTime firstTransactionDate;
        
        String evaluation;
        
        //if no transactions, return with message of no transactions for current user
        if (allClientTransactions == null || allClientTransactions.size() == 0)
        {
            throw new InsufficientCaseException(new RuntimeException("No transactions for current user"));
        }
        
        //total amount invested in buying stocks
        totalAmountInvested = allClientTransactions.stream().filter(trans -> trans.getTransactionType().equals(TransactionType.BUY))
                                                   .map(ClientTransaction::getTransactionAmount).reduce(totalAmountInvested, Double::sum);
        portfolio.setTotalAmountInvested(totalAmountInvested); // add total amount invested to portfolio
        
        // get all unique client stocks and add to portfolio summary by calling
        portfolio.setStocks(getClientStocks(username));
        
        //get no of units owned per stock, get current stock price. Multiply both value for each stock to get value of portfolio
        if (portfolio.getStocks() != null)
        {
            portfolio.getStocks().forEach(stock ->
                                          {
                                              Integer noOfUnits = allClientTransactions.stream() // stream all client transactions
                                                                                       .filter(trans -> stock.equals(trans.getStock().getSecurityName())) // get transactions for current stock
                                                                                       .map(ClientTransaction::getNoOfUnits).reduce(Integer::sum).orElse(0); // get no of units
                                              Stock stock1 = getStock(stock);// get current price of stock
                                              if (stock1 != null)
                                              {
                                                  Double currentPrice = stock1.getUnitSharePrice();
                                                  // calculate value of stock and accumulate value of all stocks through each repetition of the foreach loop.
                                                  totalValueOfPortfolio.updateAndGet(accumulatedTotal -> accumulatedTotal + (noOfUnits * currentPrice));
                                              }
                                              /*else
                                              {
                                                  throw new CustomNullPointerException("Error while calculating polio value. Issue with stock: " + stock);
                                              }*/
                                          });
            portfolio.setCurrentValueOfPortfolio(totalValueOfPortfolio.get());
        }
        
        
        // get all transactions of type SELL and add them together to get profit from sales
        allClientTransactions.stream().filter(trans -> trans.getTransactionType().equals(TransactionType.SELL)).map(ClientTransaction::getTransactionAmount).reduce(profitFromSales, Double::sum);
        log.info("profit from sales -> " + profitFromSales);
        portfolio.setProfitFromSales(profitFromSales);
        
        // get date of first Transaction or todays date if unavailable
        firstTransactionDate = allClientTransactions.stream().map(PersistingBaseEntity::getCreatedDate).min(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        portfolio.setDateOfAcquisition(firstTransactionDate);
        portfolio.setUsername(username);
        portfolio.setEvaluation("To be evaluated");
        
        ClientPortfolio savedPortfolio = (ClientPortfolio) portfolio.save(portfolio);// persist portfolio to database
        
        if (savedPortfolio != null)
        {
            return savedPortfolio;
        }
        return null;
    }
    
    
    public ClientPortfolio getPortfolioForPeriod(List<ClientTransaction> allClientTransactions, String username, LocalDateTime from, LocalDateTime to) throws InsufficientCaseException
    {
        if (from.isAfter(to))
        {
            throw new InsufficientCaseException(new RuntimeException("Please provide a correct date range to get portfolio for period"));
        }
        List<ClientTransaction> periodicListOfClientTrans
                = allClientTransactions.stream().filter(trans -> trans.getCreatedDate().isAfter(from) && trans.getCreatedDate().isBefore(to)).collect(Collectors.toList());
        
        ClientPortfolio portfolio = getPortfolio(periodicListOfClientTrans, username);
        ClientPortfolio savedPortfolio = (ClientPortfolio) portfolio.save(portfolio);
        if (savedPortfolio != null)
        {
            return savedPortfolio;
        }
        return null;
    }
    
    
    public Double getAccountBalance(String username) throws CustomNullPointerException
    {
        if (Strings.isNullOrEmpty(username))
        {
            throw new CustomNullPointerException("Please provide a valid username to get account balance");
        }
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist.");
        }
        Client client = (Client) clientGDS.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            ClientAccount ca = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (ca != null)
            {
                return ca.getBalance();
            }
            
        }
        return null;
    }
    
    /**
     * Utility methods
     */
    
    private boolean exists(String username)
    {
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            return false; // user does not exist
        }
        else
        {
            return true; // user already exists
        }
    }
    
    public List<ClientTransaction> getAllClientTransactions(String username)
    {
        if (username == null)
        {
            throw new CustomNullPointerException("Provide username to view transaction details");
        }
        
        return transactionsGDS.getRecordsByEntityKey // get all transaction for current user with an ascending sort on name
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
    }
}
