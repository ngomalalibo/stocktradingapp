package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConfirmation
{
    private static GenericDataRepository userGDS = new GenericDataRepository(new User());
    
    
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
