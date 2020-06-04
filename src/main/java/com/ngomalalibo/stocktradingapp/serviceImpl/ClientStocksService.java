package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.repository.GenericDataService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientStocksService
{
    private static GenericDataService transactionsGDS = new GenericDataService(new ClientTransaction());
    
    // get unique set of stocks owned by client
    public Set<String> getClientStocks(String username)
    {
        // get all transactions by user
        List<ClientTransaction> allUserTransactions = transactionsGDS.getRecordsByEntityKey
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
        
        return allUserTransactions.stream().map(trans -> trans.getStockQuote().getSecurityName()).collect(Collectors.toSet()); // collect a non-duplicate list of clients securities
    }
}
