package com.ngomalalibo.stocktradingapp.serviceImpl;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PortfolioServiceTest
{
    TestDataInitialization data;
    
    @Mock
    ClientService service;
    
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
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        params.put("allClientTransactions", clientTransactions);
        
        Mockito.when(service.service(params)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = (ClientPortfolio) service.service(params);
        
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
    
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        params.put("allClientTransactions", clientTransactions);
        params.put("from", from);
        params.put("to", to);
        
        Mockito.when(service.service(params)).thenReturn(portfolio);
        
        ClientPortfolio clientPortfolio = (ClientPortfolio) service.service(params);
        
        Assertions.assertEquals(clientPortfolio.getUsername(), portfolio.getUsername());
        Assertions.assertEquals(clientPortfolio.getEvaluation(), portfolio.getEvaluation());
        Assertions.assertEquals(clientPortfolio.getDateOfAcquisition(), portfolio.getDateOfAcquisition());
        
        
    }
}