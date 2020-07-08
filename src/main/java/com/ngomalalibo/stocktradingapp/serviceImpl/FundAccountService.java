package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ClientTransactionStatus;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class FundAccountService implements TransactionService
{
    private GenericDataRepository userDataRepository;
    private GenericDataRepository clientDataRepository;
    private GenericDataRepository clientAccountDataRepository;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    
    public FundAccountService(GenericDataRepository userDataRepository,
                              GenericDataRepository clientDataRepository,
                              GenericDataRepository clientAccountDataRepository)
    {
        this.userDataRepository = userDataRepository;
        this.clientDataRepository = clientDataRepository;
        this.clientAccountDataRepository = clientAccountDataRepository;
    }
    
    /*public FundAccountService()
    {
        userDataRepository = new UserDataRepository(new User());
        clientDataRepository = new ClientDataRepository(new Client());
        clientAccountDataRepository = new ClientAccountDataRepository(new ClientAccount());
    }*/
    
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
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            throw new CustomNullPointerException("This user does not exist. No account to fund");
        }
        Client client = (Client) clientDataRepository.getRecordByEntityProperty("email", user.getClientID());
        if (client != null)
        {
            ClientAccount clientAccount = (ClientAccount) clientAccountDataRepository.getRecordByEntityProperty("clientID", client.getClientAccountID()); // get balance
            if (clientAccount != null)
            {
                Double balance = clientAccount.getBalance();
                newBalance = balance + deposit; // calculate new balance after funding account
                clientAccount.setBalance(newBalance);
                clientAccount.setPreviousBalance(balance);
                
                ClientAccount returnedClientAccount = persistingBaseEntity.replaceEntity(clientAccount, clientAccount);// persist client account funding with update
                
                if (returnedClientAccount != null)
                {
                    if (returnedClientAccount.getPreviousBalance().equals(balance)) // confirm funding was successful
                    {
                        ClientTransaction fundAccount = new ClientTransaction(); // new client transaction: FUNDACCOUNT
                        fundAccount.setTransactionType(TransactionType.FUNDACCOUNT); // set type of transaction
                        fundAccount.setTransactionAmount(deposit);
                        fundAccount.setUsername(username); // client making purchase
                        fundAccount.setTransactionStatus(ClientTransactionStatus.COMPLETED);
                        
                        persistingBaseEntity.save(fundAccount);
                        return fundAccount; // previous balance in database record is current balance is user record before update
                    }
                }
            }
        }
        return null;
    }
}
