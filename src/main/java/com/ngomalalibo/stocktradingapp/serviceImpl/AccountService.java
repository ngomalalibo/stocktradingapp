package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements ClientService
{
    private final GenericDataRepository userGDS = new GenericDataRepository(new User());
    private static GenericDataRepository clientGDS = new GenericDataRepository(new Client());
    private static GenericDataRepository clientAccountGDS = new GenericDataRepository(new ClientAccount());
    
    @Override
    public Object service(Map<String, Object> params) // getAccountBalance
    {
        if (Strings.isNullOrEmpty(params.get("username").toString()))
        {
            throw new CustomNullPointerException("Please provide a valid username to get account balance");
        }
        User user = (User) userGDS.getRecordByEntityProperty("username", params.get("username").toString());
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist.");
        }
        Client client = (Client) clientGDS.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            ClientAccount ca = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (ca != null)
            {
                return ca.getBalance();
            }
        }
        return null;
    }
}
