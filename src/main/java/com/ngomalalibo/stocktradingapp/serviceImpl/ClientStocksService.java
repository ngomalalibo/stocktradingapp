package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientStocksService implements TransactionService
{
    @Qualifier("clientTransactionsDataRepository")
    @Autowired
    private GenericDataRepository clientTransactionDataRepository;
    
    /*public ClientStocksService()
    {
        clientTransactionDataRepository = new GenericDataRepository(new ClientTransaction());
    }*/
    
    // get unique set of stocks owned by client
    @Override
    public Object service(Map<String, Object> params)
    {
        String username = params.get("username").toString();
        // get all transactions by user
        List<ClientTransaction> allUserTransactions = clientTransactionDataRepository.getRecordsByEntityKey
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
    
        // return allUserTransactions.stream().filter(Objects::nonNull).map(trans -> trans.getStockQuote().getSecurityName()).filter(Objects::nonNull).collect(Collectors.toSet());// collect a non-duplicate list of clients securities
        return allUserTransactions.stream().filter(Objects::nonNull).map(ClientTransaction::getStockQuote).filter(Objects::nonNull).map(StockQuote::getSecurityName).filter(Objects::nonNull).collect(Collectors.toSet());// collect a non-duplicate list of clients securities
    }
}
