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

class FundAccountServiceTest
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
    void fundAccount()
    {
        String username = "john.snow@got.com";
        double deposit = 5_000_000D;
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        params.put("deposit", 5_000_000D);
        
        Mockito.when(service.service(params)).thenReturn(true);
        
        Assertions.assertTrue((Boolean) service.service(params));
    }
}