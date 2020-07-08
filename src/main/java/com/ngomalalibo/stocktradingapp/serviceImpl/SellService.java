package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SellService implements TransactionService
{
    @Autowired
    StockQuoteService stockQuoteService;
    
    @Autowired
    ClientTransactionsService clientTransactionsService;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    private static GenericDataRepository userDataRepository;
    private static GenericDataRepository clientDataRepository;
    private static GenericDataRepository clientAccountDataRepository;
    private static GenericDataRepository clientTransactionDataRepository;
    
    
    public SellService(GenericDataRepository userDataRepository,
                       GenericDataRepository clientDataRepository,
                       GenericDataRepository clientAccountDataRepository,
                       GenericDataRepository clientTransactionDataRepository)
    {
        this.userDataRepository = userDataRepository;
        this.clientDataRepository = clientDataRepository;
        this.clientAccountDataRepository = clientAccountDataRepository;
        this.clientTransactionDataRepository = clientTransactionDataRepository;
    }
    
    // sell stock from portfolio
    @Override
    public Object service(Map<String, Object> params)
    {
        String username = params.get("username").toString();
        String company = params.get("companyname").toString();
        int units = Integer.parseInt(params.get("units").toString());
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
        Map<String, Object> req = new HashMap<>();
        req.put("username", username);
        List<ClientTransaction> clientTransactions = (List<ClientTransaction>) clientTransactionsService.service(req);
        
        boolean userHasUnits = false;
        if (clientTransactions.size() > 0)
        {
            // check if user already has units of interested stock
            userHasUnits = clientTransactions.stream().map(ClientTransaction::getStockQuote).map(StockQuote::getSecurityName).anyMatch(stockName -> stockName.equalsIgnoreCase(company));
            
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
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);  // get object by username
        
        if (user != null)
        {
            // retrieve client details from clientID and store details in map embedded inside user object
            // user.getAdditionalProperties().put("client", GetObjectByID.getObjectById(user.getClientID(), Connection.client));
            user.getAdditionalProperties().put("client", clientDataRepository.getRecordByEntityProperty("email", user.getClientID()));
            client = (Client) user.getAdditionalProperties().get("client");
            
            if (client != null)
            {
                // ClientAccount ca = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount);
                ClientAccount ca = (ClientAccount) clientAccountDataRepository.getRecordByEntityProperty("clientID", client.getClientAccountID());
                if (ca != null)
                {
                    
                    balance = ca.getBalance();
                    
                    // get stock price
                    Map<String, Object> request = new HashMap<String, Object>();
                    request.put("companyname", company);
                    StockQuote stockQuote = (StockQuote) stockQuoteService.service(request);
                    
                    if (stockQuote != null)
                    {
                        stockPrice = stockQuote.getUnitSharePrice(); // get current stock price of company equity
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
                    sell.setStockQuote(new StockQuote(company, stockPrice));
                    sell.setTransactionAmount(profit);
                    sell.setUsername(username);
                    sell.setTransactionStatus(ClientTransactionStatus.SOLD);
                    
                    balance += profit;
                    
                    
                    ca.setPreviousBalance(ca.getBalance());
                    ca.setBalance(balance);
                    
                    // update account balance
                    ClientAccount returnedClientAccount = persistingBaseEntity.replaceEntity(ca, ca);
                    
                    // Client returned = (Client) client.save(client); // client collection is not impacted by sell transaction. only transaction and account collections
                    if (returnedClientAccount != null)
                    {
                        // create new transaction
                        persistingBaseEntity.save(sell);
                        return sell;
                    }
                }
            }
        }
        
        return null;
    }
}
