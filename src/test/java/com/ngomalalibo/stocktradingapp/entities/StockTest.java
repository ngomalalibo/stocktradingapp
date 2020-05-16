package com.ngomalalibo.stocktradingapp.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockTest
{
    Stock stock;
    
    @BeforeEach
    public void setup()
    {
        stock = new Stock();
    }
    
    @Test
    void getSecurityName()
    {
        String securityName = "nfix";
        
        stock.setSecurityName(securityName);
        Assertions.assertEquals(securityName, stock.getSecurityName());
    }
    
    @Test
    void getUnitSharePrice()
    {
        Double unitPrice = 5D;
        stock.setUnitSharePrice(unitPrice);
        
        Assertions.assertEquals(unitPrice, stock.getUnitSharePrice());
    }
}