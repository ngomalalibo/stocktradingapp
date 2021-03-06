package com.ngomalalibo.stocktradingapp.repository;

import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;
import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ActivityLogType;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
    private StockQuote stockQuote;
    private User user;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    @Autowired
    private static MongoConnectionImpl database;
    
    public static void main(String[] args)
    {
        database.startDB();
        log.info("initializing database");
        new TestDataInitialization();
        log.info("Initialization complete");
        database.stopDB();
        
    }
    
    public TestDataInitialization()
    {

//        initData();
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
        stockQuote = initializeStock();
        user = initializeUser();
        clientPortfolio = initializePortfolio();
        initializeDB(); // persist test data
        
        return true;
    }
    
    private void initializeDB()
    {
        ActivityLog activityLog = initializeActivityLog();
        persistingBaseEntity.save(activityLog);
        
        Client client = initializeClient();
        persistingBaseEntity.save(client);
        
        ClientAccount clientAccount = initializeClientAccount();
        persistingBaseEntity.save(clientAccount);
        
        ClientPortfolio clientPortFolio = initializePortfolio();
        persistingBaseEntity.save(clientPortFolio);
        
        ClientTransaction clientTransaction = initializeClientTransaction();
        persistingBaseEntity.save(clientTransaction);
        
        StockQuote stockQuote = initializeStock();
        persistingBaseEntity.save(stockQuote);
        
        User user = initializeUser();
        persistingBaseEntity.save(user);
    }
    
    public User initializeUser()
    {
        return new User("john.snow@got.com", "1234567890", "USER", "john.snow@got.com");
    }
    
    public StockQuote initializeStock()
    {
        return new StockQuote("nflx", 50D);
    }
    
    public ClientTransaction initializeClientTransaction()
    {
        return new ClientTransaction(TransactionType.BUY, 50, new StockQuote("nfix", 500D), 25_000D, "john.snow@got.com", ClientTransactionStatus.BOUGHT);
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
