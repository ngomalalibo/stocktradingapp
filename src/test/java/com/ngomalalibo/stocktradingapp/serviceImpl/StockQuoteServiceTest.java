package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.StockQuote;
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

public class StockQuoteServiceTest
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
    void getStock()
    {
        StockQuote stockQuote = new StockQuote("nflx", 500D);
        Map<String, Object> map = new HashMap<>();
        map.put("companyname", "nflx");
        Mockito.when(service.service(map)).thenReturn(stockQuote);
        
        StockQuote run = (StockQuote) service.service(map);
        
        Assertions.assertEquals(run.getSecurityName(), "nflx");
        Assertions.assertEquals(run.getUnitSharePrice(), 500D);
        
        Mockito.verify(service, Mockito.times(1)).service(ArgumentMatchers.anyMap());
    }
    
    
}