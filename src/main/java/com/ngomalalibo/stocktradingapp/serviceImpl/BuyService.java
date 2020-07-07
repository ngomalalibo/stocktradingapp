package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BuyService implements TransactionService
{
    @Autowired
    StockQuoteService stockQuoteService;
    
    private final GenericDataRepository userDataRepository;
    private final GenericDataRepository clientDataRepository;
    private final GenericDataRepository clientAccountDataRepository;
    private final GenericDataRepository clientTransactionDataRepository;
    
    public BuyService(GenericDataRepository userDataRepository,
                      GenericDataRepository clientDataRepository,
                      GenericDataRepository clientAccountDataRepository,
                      GenericDataRepository clientTransactionDataRepository)
    {
        this.userDataRepository = userDataRepository;
        this.clientDataRepository = clientDataRepository;
        this.clientAccountDataRepository = clientAccountDataRepository;
        this.clientTransactionDataRepository = clientTransactionDataRepository;
    }
    
    // buy stock
    @Override
    public Object service(Map<String, Object> params)
    {
        
        String username = params.get("username").toString();
        String company = params.get("companyname").toString();
        int units = Integer.parseInt(params.get("units").toString());
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
        List<ClientTransaction> clientTransactions = clientTransactionDataRepository.getRecordsByEntityKey
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
        
        boolean userHasUnits = false;
        if (clientTransactions.size() > 0)
        {
            // check if user already has units of interested stock
            userHasUnits = clientTransactions.stream().map(ClientTransaction::getStockQuote).map(StockQuote::getSecurityName).allMatch(stock -> stock.equalsIgnoreCase(company));
            
            // get amount of units owned by adding all transactions
            unitsOwned = clientTransactions.stream().map(ClientTransaction::getNoOfUnits).reduce(unitsOwned, Integer::sum);
        }
        
        // get user account object
        // User user = GetObjectByID.getObjectById(username, Connection.user);
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);
        
        if (user != null)
        {
            // store user client details in map
            // user.getAdditionalProperties().put("client", GetObjectByID.getObjectById(user.getClientID(), Connection.client));
            user.getAdditionalProperties().put("client", (Client) clientDataRepository.getRecordByEntityProperty("email", user.getClientID()));
            
        }
        
        client = (Client) user.getAdditionalProperties().get("client");
        if (client != null)
        {
            // ClientAccount clientAccount = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount);
            ClientAccount clientAccount = (ClientAccount) clientAccountDataRepository.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (clientAccount != null)
            {
                balance = clientAccount.getBalance();
                Map<String, Object> req = new HashMap<String, Object>();
                req.put("companyname", company);
                StockQuote stockQuote = (StockQuote) stockQuoteService.service(req); // get company stock
                if (stockQuote != null)
                {
                    stockPrice = stockQuote.getUnitSharePrice(); // get current stock price of company equity
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
                    buy.setStockQuote(new StockQuote(company, stockPrice)); // store stock purchase detail. price and company
                    buy.setTransactionAmount(cost);
                    buy.setUsername(username); // client making purchase
                    buy.setTransactionStatus(ClientTransactionStatus.BOUGHT);
                    balance -= cost;
                    
                    
                    clientAccount.setPreviousBalance(clientAccount.getBalance());
                    clientAccount.setBalance(balance);
                    
                    clientAccount = clientAccount.replaceEntity(clientAccount, clientAccount);// update balance. replaceEntity takes a records with the same ID and replaces the one in the database one with the other
                    if (clientAccount != null)
                    {
                        buy.save(buy); // persist new transaction
                        return buy;
                    }
                }
                else
                {
                    throw new InsufficientCaseException
                            (new RuntimeException("Not enough funds in your account to make this purchase. Kindly fund your account with " + (cost - balance) + " to proceed with acquisition."));
                }
            }
        }
        return null;
    }
}
