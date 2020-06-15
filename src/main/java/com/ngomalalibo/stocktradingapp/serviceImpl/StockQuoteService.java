package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.apiclient.StockQuoteApiClient;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Slf4j
@RequiredArgsConstructor
@Service
public class StockQuoteService implements ClientService
{
    @Override
    public Object service(Map<String, Object> params)
    {
        String company = params.get("companyname").toString();
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        return new StockQuoteApiClient().getStock(company);
    }
}
