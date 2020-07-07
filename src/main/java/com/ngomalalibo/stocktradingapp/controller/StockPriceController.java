package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.serviceImpl.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class StockPriceController
{
    @Autowired
    @Qualifier("stockQuoteService")
    TransactionService services;
    
    @GetMapping("/stockprice/{companyname}")
    public ResponseEntity<Object> checkStockPrice(@PathVariable(name = "companyname") String companyname,
                                                  @RequestParam("token") String token)
    {
        Map<String, Object> request = new HashMap<>();
        request.put("companyname", companyname);
        StockQuote stockQuote = (StockQuote) services.service(request);
        if (stockQuote != null)
        {
            return ok(stockQuote);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
}
