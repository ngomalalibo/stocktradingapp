package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.exception.ApiResponse;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.service.ClientTransactionsService;
import com.ngomalalibo.stocktradingapp.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    
    // @Secured(value = {"USER"})
    // @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/portfolio", method = RequestMethod.POST)
    public ResponseEntity<Object> viewStocks(@RequestBody HashMap<String, Object> request)
    {
        try
        {
            List<ClientTransaction> allClientTransactions = new ClientTransactionsService().getAllClientTransactions(request.get("username").toString());
            if (allClientTransactions != null && allClientTransactions.size() > 0)
            {
                ClientPortfolio portfolio = services.getPortfolio(allClientTransactions, request.get("username").toString());
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
