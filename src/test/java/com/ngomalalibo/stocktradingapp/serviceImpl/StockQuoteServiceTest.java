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

public class StockQuoteServiceTest
{
    @Mock
    StockQuoteService service;
    
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
        
        Mockito.when(service.getStock("nflx")).thenReturn(stockQuote);
        
        StockQuote run = service.getStock("nflx");
        
        Assertions.assertEquals(run.getSecurityName(), "nflx");
        Assertions.assertEquals(run.getUnitSharePrice(), 500D);
        
        Mockito.verify(service, Mockito.times(1)).getStock(ArgumentMatchers.anyString());
    }
    
    
}