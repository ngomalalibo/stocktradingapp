package com.ngomalalibo.stocktradingapp.aspect;

import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class BusinessAopSpringBootTest
{
    
    @Autowired
    PersistingBaseEntity bean;
    
    @Test
    public void invokeAOPStuff()
    {
        bean.save(new ClientTransaction(TransactionType.BUY, 1, new StockQuote("nfix", 440D), 440D, "john.snow@got.com", ClientTransactionStatus.BOUGHT));
    }
    
}