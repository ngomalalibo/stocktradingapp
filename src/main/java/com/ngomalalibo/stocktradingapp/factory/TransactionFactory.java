package com.ngomalalibo.stocktradingapp.factory;

import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.serviceImpl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionFactory
{
    @Qualifier("buyService")
    @Autowired
    private TransactionService buyService;
    
    @Qualifier("sellService")
    @Autowired
    private TransactionService sellService;
    
    @Qualifier("fundAccountService")
    @Autowired
    private TransactionService fundAccountService;
    
    private TransactionService service;
    
    public TransactionFactory()
    {
    }
    
    public ClientTransaction createTransaction(Map<String, Object> request)
    {
        String transactionType = request.get("transactiontype").toString();
        
        if (transactionType != null)
        {
            if (transactionType.equalsIgnoreCase("buy"))
            {
                service = buyService;
            }
            else if (transactionType.equalsIgnoreCase("sell"))
            {
                service = sellService;
            }
            else if (transactionType.equalsIgnoreCase("fundaccount"))
            {
                service = fundAccountService;
            }
            else
            {
                return null;
            }
            return (ClientTransaction) service.service(request);
        }
        else
        {
            return null;
        }
    }
}
