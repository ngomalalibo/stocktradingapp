package com.ngomalalibo.stocktradingapp.dataService;

import com.ngomalalibo.stocktradingapp.entities.*;
import com.ngomalalibo.stocktradingapp.enums.ActivityLogType;
import com.ngomalalibo.stocktradingapp.enums.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TestDataInitialization
{
    
    private ActivityLog activityLog;
    private Client client;
    private ClientAccount clientAccount;
    private ClientTransaction clientTransaction;
    private ClientPortfolio clientPortfolio;
    private Stock stock;
    private User user;
    
    public static void main(String[] args)
    {
        Connection.startDB();
        log.info("initializing database");
        TestDataInitialization init = new TestDataInitialization();
        log.info("Initialization complete");
        Connection.stopDB();
        
    }
    
    public TestDataInitialization()
    {
        
        //initData();
    }
    
    
    public boolean initData()
    {
        /**
         * Load Test Data in correct sequence
         * */
        activityLog = initializeActivityLog();
        clientAccount = initializeClientAccount();
        client = initializeClient();
        clientTransaction = initializeClientTransaction();
        stock = initializeStock();
        user = initializeUser();
        clientPortfolio = initializePortfolio();
        initializeDB(); // persist test data
        
        return true;
    }
    
    private void initializeDB()
    {
        ActivityLog activityLog = initializeActivityLog();
        activityLog.save(activityLog);
        
        Client client = initializeClient();
        client.save(client);
        
        ClientAccount clientAccount = initializeClientAccount();
        clientAccount.save(clientAccount);
        
        ClientPortfolio clientPortFolio = initializePortfolio();
        clientPortFolio.save(clientPortFolio);
        
        ClientTransaction clientTransaction = initializeClientTransaction();
        clientTransaction.save(clientTransaction);
        
        Stock stock = initializeStock();
        stock.save(stock);
        
        User user = initializeUser();
        user.save(user);
    }
    
    public User initializeUser()
    {
        return new User("john.snow@got.com", "1234567890", "USER", "john.snow@got.com");
    }
    
    public Stock initializeStock()
    {
        return new Stock("nflx", 50D);
    }
    
    public ClientTransaction initializeClientTransaction()
    {
        return new ClientTransaction(TransactionType.BUY, 50, new Stock("nfix", 500D), 25_000D, "john.snow@got.com", ClientTransactionStatus.BOUGHT);
    }
    
    public ClientPortfolio initializePortfolio()
    {
        Set<String> companies = new HashSet<>(2);
        companies.add("winterfeld");
        companies.add("nightwatch");
        companies.add("nflx");
        
        return new ClientPortfolio("john.snow@got.com",
                                   companies
                , Collections.singletonList(initializeClientTransaction()),
                                   5_000_000D,
                                   8_000_000D,
                                   500_000D,
                                   LocalDateTime.now(),
                                   "Profitable");
    }
    
    public ClientAccount initializeClientAccount()
    {
        return new ClientAccount(90000000D, 6000D, "john.snow@got.com");
    }
    
    public Client initializeClient()
    {
        Client client = new Client("John", "Snow", "john.snow@got.com");
        client.setClientAccountID(clientAccount.getClientID());
        return client;
    }
    
    public ActivityLog initializeActivityLog()
    {
        
        return new ActivityLog("SYSTEM", "Access", "login", ActivityLogType.INFO, "ActivityLog");
    }
}
