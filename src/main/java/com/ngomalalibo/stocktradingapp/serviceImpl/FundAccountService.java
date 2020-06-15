package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundAccountService implements ClientService
{
    private static GenericDataRepository userGDS = new GenericDataRepository(new User());
    private static GenericDataRepository clientGDS = new GenericDataRepository(new Client());
    private static GenericDataRepository clientAccountGDS = new GenericDataRepository(new ClientAccount());
    
    // method to fund client account
    @Override
    public Object service(Map<String, Object> params)
    {
        String username = params.get("username").toString();
        double deposit = Double.parseDouble(params.get("deposit").toString());
        
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
            ClientAccount clientAccount = (ClientAccount) clientAccountGDS.getRecordByEntityProperty("clientID", client.getClientAccountID()); // get balance
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
                        ClientTransaction fundAccount = new ClientTransaction(); // new client transaction: FUNDACCOUNT
                        fundAccount.setTransactionType(TransactionType.FUNDACCOUNT); // set type of transaction
                        fundAccount.setTransactionAmount(deposit);
                        fundAccount.setUsername(username); // client making purchase
                        fundAccount.setTransactionStatus(ClientTransactionStatus.COMPLETED);
                        
                        fundAccount.save(fundAccount);
                        return fundAccount; // previous balance in database record is current balance is user record before update
                    }
                }
            }
        }
        return null;
    }
}
