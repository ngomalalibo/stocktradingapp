package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellService
{
    private static GenericDataService userGDS = new GenericDataService(new User());
    private static GenericDataService clientGDS = new GenericDataService(new Client());
    private static GenericDataService clientAccountGDS = new GenericDataService(new ClientAccount());
    
    
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
        List<ClientTransaction> clientTransactions = new ClientTransactionsService().getAllClientTransactions(username);
        
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
                    StockQuote stockQuote = new StockQuoteService().getStock(company);
                    
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
}