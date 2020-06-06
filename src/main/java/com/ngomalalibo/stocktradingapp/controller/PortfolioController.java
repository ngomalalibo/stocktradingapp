package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.exception.ApiResponse;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.serviceImpl.ClientTransactionsService;
import com.ngomalalibo.stocktradingapp.serviceImpl.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class PortfolioController
{
    @Autowired
    PortfolioService services;
    
    @PostMapping("/portfolio")
    public ResponseEntity<Object> viewStocks(@RequestBody HashMap<String, Object> request)
    {
        try
        {
            List<ClientTransaction> allClientTransactions = (List<ClientTransaction>) new ClientTransactionsService().service(request);
            if (allClientTransactions != null && allClientTransactions.size() > 0)
            {
                request.put("allClientTransactions", allClientTransactions);
                
                ClientPortfolio portfolio = (ClientPortfolio) new PortfolioService().service(request);
                return ok(portfolio);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error retrieving client portfolio. Bad Request");
            }
        }
        catch (CustomNullPointerException | InsufficientCaseException e)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Error retrieving client portfolio. Bad Request", e.getMessage());
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
}
