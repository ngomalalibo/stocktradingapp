package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientTransactionsService
{
    private GenericDataService transactionsGDS = new GenericDataService(new ClientTransaction());
    
    public List<ClientTransaction> getAllClientTransactions(String username)
    {
        if (username == null)
        {
            throw new CustomNullPointerException("Provide username to view transaction details");
        }
        
        return transactionsGDS.getRecordsByEntityKey // get all transaction for current user with an ascending sort on name
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
    }
}
