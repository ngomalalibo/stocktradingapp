package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.apiclient.StockQuoteApiClient;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@RequiredArgsConstructor
@Service
public class StockQuoteService
{
    public StockQuote getStock(String company) throws CustomNullPointerException
    {
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        return new StockQuoteApiClient().getStock(company);
    }
    
    
    
}
