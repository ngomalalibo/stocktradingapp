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

class SellServiceTest
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
    void sell()
    {
        String companyName = "nflx";
        String username = "rob.stark@got.com";
        Integer units = 500;
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("companyname", companyName);
        params.put("units", units);
        
        Mockito.when(service.service(params)).thenReturn(true);
        
        boolean successful = (boolean) service.service(params);
        Assertions.assertTrue(successful);
        
        Mockito.verify(service, Mockito.times(1)).service(ArgumentMatchers.anyMap());
    }
}