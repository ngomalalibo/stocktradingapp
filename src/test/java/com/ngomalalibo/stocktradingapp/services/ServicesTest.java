package com.ngomalalibo.stocktradingapp.services;

import com.ngomalalibo.stocktradingapp.dataService.GenericDataService;
import com.ngomalalibo.stocktradingapp.dataService.TestDataInitialization;
import com.ngomalalibo.stocktradingapp.entities.Client;
import com.ngomalalibo.stocktradingapp.entities.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entities.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entities.Stock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ServicesTest
{
    
    GenericDataService genericDataService;
    TestDataInitialization data;
    
    @Mock
    Services services;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        genericDataService = new GenericDataService(new Stock());
        data = new TestDataInitialization();
    }
    
    @AfterEach
    public void tearDown()
    {
    
    }
    
    @Test
    void getStock()
    {
        Stock stock = new Stock("nflx", 500D);
        
        Mockito.when(services.getStock("nflx")).thenReturn(stock);
        
        Stock run = services.getStock("nflx");
        
        Assertions.assertEquals(run.getSecurityName(), "nflx");
        Assertions.assertEquals(run.getUnitSharePrice(), 500D);
        
        Mockito.verify(services, Mockito.times(1)).getStock(ArgumentMatchers.anyString());
    }
    
    @Test
    void register()
    {
        Client client = new Client("John", "Snow", "john.snow@got.com");
        Mockito.when(services.register("john.snow@got.com", "1234567890", client)).thenReturn(true);
        
        boolean successful = services.register("john.snow@got.com", "1234567890", client);
        
        Assertions.assertTrue(successful);
    }
    
    @Test
    void login()
    {
        AuthenticationManager mgr = new AuthenticationManager()
        {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException
            {
                return null;
            }
        };
        
        
        Mockito.when(services.login("john.snow@got.com", "1234567890", mgr)).thenReturn(true);
        
        boolean successful = services.login("john.snow@got.com", "1234567890", mgr);
        
        Assertions.assertTrue(successful);
    }
    
    @Test
    void fundAccount()
    {
        String username = "john.snow@got.com";
        double deposit = 5_000_000D;
        
        Mockito.when(services.fundAccount(username, deposit)).thenReturn(true);
        
        Assertions.assertTrue(services.fundAccount(username, deposit));
    }
    
    
    @Test
    void buy()
    {
        String companyName = "nflx";
        String username = "rob.stark@got.com";
        Integer units = 500;
        
        Mockito.when(services.buy(companyName, username, units)).thenReturn(true);
        
        boolean successful = services.buy(companyName, username, units);
        Assertions.assertTrue(successful);
    }
    
    @Test
    void getClientStocks()
    {
        Set<String> stocks = new HashSet<String>()
        {{
            add("dangote");
            add("nbc");
            add("fbn");
            add("access");
        }};
        
        Mockito.when(services.getClientStocks("john.snow@got.com")).thenReturn(stocks);
        
        Set<String> securities = services.getClientStocks("john.snow@got.com");
        
        Assertions.assertTrue(securities.contains("dangote"));
        Assertions.assertTrue(securities.contains("nbc"));
        Assertions.assertTrue(securities.contains("fbn"));
        Assertions.assertTrue(securities.contains("access"));
        
        Mockito.verify(services, Mockito.atLeast(1)).getClientStocks(ArgumentMatchers.anyString());
        
        
    }
    
    @Test
    void sell()
    {
        String companyName = "nflx";
        String username = "rob.stark@got.com";
        Integer units = 500;
        
        Mockito.when(services.sell(companyName, username, units)).thenReturn(true);
        
        boolean successful = services.sell(companyName, username, units);
        Assertions.assertTrue(successful);
        
        Mockito.verify(services, Mockito.times(1)).sell(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
    }
    
    @Test
    void getPortfolio()
    {
        List<ClientTransaction> clientTransactions = Collections.singletonList(data.initializeClientTransaction());
        String username = "john.snow@got.com";
        
        ClientPortfolio portfolio = data.initializePortfolio();
        
        Mockito.when(services.getPortfolio(clientTransactions, username)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = services.getPortfolio(clientTransactions, username);
        
        Assertions.assertEquals(clientPortfolio.getEvaluation(), portfolio.getEvaluation());
        Assertions.assertEquals(clientPortfolio.getUsername(), portfolio.getUsername());
        
    }
    
    @Test
    void getPortfolioForPeriod()
    {
        LocalDateTime from = LocalDateTime.of(2020, Month.FEBRUARY, 27, 19, 30, 40);
        LocalDateTime to = LocalDateTime.of(2020, Month.MAY, 15, 00, 00, 00);
        
        List<ClientTransaction> clientTransactions = Collections.singletonList(data.initializeClientTransaction());
        String username = "john.snow@got.com";
        
        ClientPortfolio portfolio = data.initializePortfolio();
        
        Mockito.when(services.getPortfolioForPeriod(clientTransactions, username, from, to)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = services.getPortfolioForPeriod(clientTransactions, username, from, to);
        
        Assertions.assertEquals(clientPortfolio.getUsername(), portfolio.getUsername());
        Assertions.assertEquals(clientPortfolio.getEvaluation(), portfolio.getEvaluation());
        Assertions.assertEquals(clientPortfolio.getDateOfAcquisition(), portfolio.getDateOfAcquisition());
        
        
    }
    
    @Test
    void getAccountBalance()
    {
        double balance = 11_100_000D;
        String username = "john.snow@got.com";
        
        Mockito.when(services.getAccountBalance(username)).thenReturn(balance);
        
        Double accountBalance = services.getAccountBalance(username);
        Assertions.assertEquals(accountBalance, balance);
        Mockito.verify(services, Mockito.times(1)).getAccountBalance(ArgumentMatchers.anyString());
    }
    
}