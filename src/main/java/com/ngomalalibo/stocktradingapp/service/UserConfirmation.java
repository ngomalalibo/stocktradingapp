package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.repository.GenericDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConfirmation
{
    private static GenericDataService userGDS = new GenericDataService(new User());
    
    
    /**
     * Utility methods
     */
    
    public static boolean exists(String username)
    {
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            return false; // user does not exist
        }
        else
        {
            return true; // user already exists
        }
    }
    
    
}
