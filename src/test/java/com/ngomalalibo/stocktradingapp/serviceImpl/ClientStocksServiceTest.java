package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ClientStocksServiceTest
{
    @Mock
    ClientService service;
    
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
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        
        Mockito.when(service.service(params)).thenReturn(stocks);
        
        Set<String> securities = (Set<String>) service.service(params);
        
        Assertions.assertTrue(securities.contains("dangote"));
        Assertions.assertTrue(securities.contains("nbc"));
        Assertions.assertTrue(securities.contains("fbn"));
        Assertions.assertTrue(securities.contains("access"));
        
        Mockito.verify(service, Mockito.atLeast(1)).service(ArgumentMatchers.anyMap());
        
        
    }
}