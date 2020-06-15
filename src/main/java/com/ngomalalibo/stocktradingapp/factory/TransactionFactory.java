package com.ngomalalibo.stocktradingapp.factory;

import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.serviceImpl.ClientService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionFactory
{
    public ClientTransaction createTransaction(Map<String, Object> request)
    {
        ClientService clientService = ClientService.createService(request);
        if (clientService != null)
        {
            return (ClientTransaction) clientService.service(request);
        }
        else
        {
            return null;
        }
    }
}
