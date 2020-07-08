package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
public class AccountService implements TransactionService
{
    @Qualifier("userDataRepository")
    @Autowired
    private GenericDataRepository userDataRepository;
    
    @Qualifier("clientDataRepository")
    @Autowired
    private GenericDataRepository clientDataRepository;
    
    @Qualifier("clientAccountDataRepository")
    @Autowired
    private GenericDataRepository clientAccountDataRepository;
    
    @Override
    public Object service(Map<String, Object> params) // getAccountBalance
    {
        if (Strings.isNullOrEmpty(params.get("username").toString()))
        {
            throw new CustomNullPointerException("Please provide a valid username to get account balance");
        }
        User user = (User) userDataRepository.getRecordByEntityProperty("username", params.get("username").toString());
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist.");
        }
        Client client = (Client) clientDataRepository.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            ClientAccount ca = (ClientAccount) clientAccountDataRepository.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (ca != null)
            {
                return ca.getBalance();
            }
        }
        return null;
    }
}
