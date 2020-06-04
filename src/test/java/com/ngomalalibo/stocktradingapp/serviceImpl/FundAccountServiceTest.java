package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class FundAccountServiceTest
{
    @Mock
    FundAccountService service;
    
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
        
        Mockito.when(service.fundAccount(username, deposit)).thenReturn(true);
        
        Assertions.assertTrue(service.fundAccount(username, deposit));
    }
}