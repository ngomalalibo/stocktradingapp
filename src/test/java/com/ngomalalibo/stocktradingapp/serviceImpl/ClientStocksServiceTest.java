package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

class ClientStocksServiceTest
{
    @Mock
    ClientStocksService service;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @AfterEach
    public void tearDown()
    {
    
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
        
        Mockito.when(service.getClientStocks("john.snow@got.com")).thenReturn(stocks);
        
        Set<String> securities = service.getClientStocks("john.snow@got.com");
        
        Assertions.assertTrue(securities.contains("dangote"));
        Assertions.assertTrue(securities.contains("nbc"));
        Assertions.assertTrue(securities.contains("fbn"));
        Assertions.assertTrue(securities.contains("access"));
        
        Mockito.verify(service, Mockito.atLeast(1)).getClientStocks(ArgumentMatchers.anyString());
        
        
    }
}