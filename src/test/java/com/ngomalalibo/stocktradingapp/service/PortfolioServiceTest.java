package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

class PortfolioServiceTest
{
    TestDataInitialization data;
    
    @Mock
    PortfolioService service;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        data = new TestDataInitialization();
    }
    
    @AfterEach
    public void tearDown()
    {
    
    }
    
    @Test
    void getPortfolio()
    {
        List<ClientTransaction> clientTransactions = Collections.singletonList(data.initializeClientTransaction());
        String username = "john.snow@got.com";
        
        ClientPortfolio portfolio = data.initializePortfolio();
        
        Mockito.when(service.getPortfolio(clientTransactions, username)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = service.getPortfolio(clientTransactions, username);
        
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
        
        Mockito.when(service.getPortfolioForPeriod(clientTransactions, username, from, to)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = service.getPortfolioForPeriod(clientTransactions, username, from, to);
        
        Assertions.assertEquals(clientPortfolio.getUsername(), portfolio.getUsername());
        Assertions.assertEquals(clientPortfolio.getEvaluation(), portfolio.getEvaluation());
        Assertions.assertEquals(clientPortfolio.getDateOfAcquisition(), portfolio.getDateOfAcquisition());
        
        
    }
}