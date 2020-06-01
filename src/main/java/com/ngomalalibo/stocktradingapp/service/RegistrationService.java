package com.ngomalalibo.stocktradingapp.service;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.security.JwtTokenProvider;
import com.ngomalalibo.stocktradingapp.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;

import static com.ngomalalibo.stocktradingapp.service.UserConfirmation.exists;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService
{
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    //register new client users
    public String register(String username, String password, Client client) throws NonUniqueResultException, CustomNullPointerException
    {
        String token;
        if (client == null)
        {
            throw new CustomNullPointerException("Please provide an existing client in order to create an account");
        }
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password))
        {
            throw new CustomNullPointerException("Please provide a valid username or password");
        }
        else if (exists(username))
        {
            throw new NonUniqueResultException("This user exists in the system. Kindly reset your password of choose another username");
        }
        User user = new User(username, PasswordEncoder.getPasswordEncoder().encode(password), "USER", username);
        token = new JwtTokenProvider().createToken(user);
        user.setToken(token);
        client.setEmail(username);
        
        //  confirm registration was successful by retrieving for user and client from database
        client = (Client) client.save(client);
        user = (User) user.save(user);
        
        if (user != null && client != null && user.getUsername().equalsIgnoreCase(client.getEmail()))
        {
            return token; // user and client creation confirmed
        }
        return null;
    }
}
