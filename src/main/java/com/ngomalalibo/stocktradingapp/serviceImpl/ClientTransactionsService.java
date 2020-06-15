package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.dataprovider.SortProperties;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientTransactionsService implements ClientService
{
    private GenericDataRepository transactionsGDS = new GenericDataRepository(new ClientTransaction());
    
    @Override
    public Object service(Map<String, Object> params)
    {
        String username = params.get("username").toString();
        if (username == null)
        {
            throw new CustomNullPointerException("Provide username to view transaction details");
        }
    
        return transactionsGDS.getRecordsByEntityKey // get all transaction for current user with an ascending sort on name
                ("username", username, Collections.singletonList(new SortProperties("username", true)));
    }
}
