package com.ngomalalibo.stocktradingapp.factory;

import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.serviceImpl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private TransactionService selectedService;
    
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
                selectedService = buyService;
            }
            else if (transactionType.equalsIgnoreCase("sell"))
            {
                selectedService = sellService;
            }
            else if (transactionType.equalsIgnoreCase("fundaccount"))
            {
                selectedService = fundAccountService;
            }
            else
            {
                return null;
            }
            return (ClientTransaction) selectedService.service(request);
        }
        else
        {
            return null;
        }
    }
}
