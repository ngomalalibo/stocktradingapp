package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class AccountServiceTest
{
    @Mock
    AccountService service;
    
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
        
        Mockito.when(service.getAccountBalance(username)).thenReturn(balance);
        
        Double accountBalance = service.getAccountBalance(username);
        Assertions.assertEquals(accountBalance, balance);
        Mockito.verify(service, Mockito.times(1)).getAccountBalance(ArgumentMatchers.anyString());
    }
}