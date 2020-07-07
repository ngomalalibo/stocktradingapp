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
import java.util.Map;


class AccountServiceTest
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
    void getAccountBalance()
    {
        double balance = 11_100_000D;
        String username = "john.snow@got.com";
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        Mockito.when(service.service(params)).thenReturn(balance);
        
        Double accountBalance = (Double) service.service(params);
        Assertions.assertEquals(accountBalance, balance);
        Mockito.verify(service, Mockito.times(1)).service(ArgumentMatchers.anyMap());
    }
}