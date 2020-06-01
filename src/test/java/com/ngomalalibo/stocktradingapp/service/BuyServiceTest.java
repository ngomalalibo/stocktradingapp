package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class BuyServiceTest
{
    @Mock
    BuyService service;
    
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
        
        Mockito.when(service.buy(companyName, username, units)).thenReturn(true);
        
        boolean successful = service.buy(companyName, username, units);
        Assertions.assertTrue(successful);
    }
    
}