package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

class BuyServiceTest
{
    @Mock
    TransactionService service;
    
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
    void buy()
    {
        String companyName = "nflx";
        String username = "rob.stark@got.com";
        Integer units = 500;
        Map<String, Object> params = new HashMap<String, Object>()
        {{
            put("username", username);
            put("companyName", companyName);
            put("units", units);
        }};
        
        Mockito.when(service.service(params)).thenReturn(true);
        
        Boolean successful = (Boolean) service.service(params);
        Assertions.assertTrue(successful);
    }
    
}