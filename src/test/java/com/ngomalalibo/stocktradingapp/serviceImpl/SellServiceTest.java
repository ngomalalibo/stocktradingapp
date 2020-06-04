package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class SellServiceTest
{
    @Mock
    SellService service;
    
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
    void sell()
    {
        String companyName = "nflx";
        String username = "rob.stark@got.com";
        Integer units = 500;
        
        Mockito.when(service.sell(companyName, username, units)).thenReturn(true);
        
        boolean successful = service.sell(companyName, username, units);
        Assertions.assertTrue(successful);
        
        Mockito.verify(service, Mockito.times(1)).sell(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
    }
}