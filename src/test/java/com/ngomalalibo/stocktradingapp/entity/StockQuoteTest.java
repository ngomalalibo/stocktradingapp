package com.ngomalalibo.stocktradingapp.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StockQuoteTest
{
    StockQuote stockQuote;
    
    @BeforeEach
    public void setup()
    {
       stockQuote = new StockQuote();
    }
    
    @Test
    void getSecurityName()
    {
        String securityName = "nfix";
        
        stockQuote.setSecurityName(securityName);
        Assertions.assertEquals(securityName, stockQuote.getSecurityName());
    }
    
    @Test
    void getUnitSharePrice()
    {
        Double unitPrice = 5D;
        stockQuote.setUnitSharePrice(unitPrice);
        
        Assertions.assertEquals(unitPrice, stockQuote.getUnitSharePrice());
    }
}