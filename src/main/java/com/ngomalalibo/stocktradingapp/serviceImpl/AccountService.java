package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService
{
    private final GenericDataService userGDS = new GenericDataService(new User());
    private static GenericDataService clientGDS = new GenericDataService(new Client());
    private static GenericDataService clientAccountGDS = new GenericDataService(new ClientAccount());
    
    public Double getAccountBalance(String username) throws CustomNullPointerException
    {
        if (Strings.isNullOrEmpty(username))
        {
            throw new CustomNullPointerException("Please provide a valid username to get account balance");
        }
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
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
