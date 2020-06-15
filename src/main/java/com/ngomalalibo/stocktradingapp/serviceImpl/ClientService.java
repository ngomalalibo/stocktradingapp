package com.ngomalalibo.stocktradingapp.serviceImpl;

import java.util.Map;

public interface ClientService
{
    Object service(Map<String, Object> params);
    
    static ClientService createService(Map<String, Object> request)
    {
        ClientService service;
        
        String transactionType = request.get("transactiontype").toString();
        
        
        if (transactionType != null)
        {
            if (transactionType.equalsIgnoreCase("buy"))
            {
                service = new BuyService();
            }
            else if (transactionType.equalsIgnoreCase("sell"))
            {
                service = new SellService();
            }
            else if (transactionType.equalsIgnoreCase("fundaccount"))
            {
                service = new FundAccountService();
            }
            else
            {
//                response.replace("Please provide a valid transaction type. Transaction types Supported are buy or sell.", null);
                return null;
            }
            return service;
        }
        else
        {
//            response.replace("Please provide a valid transaction type. Transaction types Supported are buy or sell", null);
            return null;
        }
    }
}
