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
public class FundAccountService
{
    private static GenericDataService userGDS = new GenericDataService(new User());
    private static GenericDataService clientGDS = new GenericDataService(new Client());
    private static GenericDataService clientAccountGDS = new GenericDataService(new ClientAccount());
    
    // method to fund client account
    public boolean fundAccount(String username, Double deposit) throws CustomNullPointerException
    {
        double newBalance;
        if (Strings.isNullOrEmpty(username))
        {
            throw new CustomNullPointerException("Please provide a valid username to fund account");
        }
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist. No account to fund");
        }
        Client client = (Client) clientGDS.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            // ClientAccount clientAccount = GetObjectByID.getObjectById(client.getClientAccountID(), Connection.clientAccount); // get balance
            ClientAccount clientAccount = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID());
            if (clientAccount != null)
            {
                Double balance = clientAccount.getBalance();
                newBalance = balance + deposit; // calculate new balance after funding account
                clientAccount.setBalance(newBalance);
                clientAccount.setPreviousBalance(balance);
                
                ClientAccount returnedClientAccount = clientAccount.replaceEntity(clientAccount, clientAccount);// persist client account funding with update
                
                if (returnedClientAccount != null)
                {
                    if (returnedClientAccount.getPreviousBalance().equals(balance)) // confirm funding was successful
                    {
                        return true; // previous balance in database record is current balance is user record before update
                    }
                }
            }
            
            
        }
        return false;
    }
}
