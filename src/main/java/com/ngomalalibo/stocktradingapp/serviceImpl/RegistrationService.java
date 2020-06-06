package com.ngomalalibo.stocktradingapp.serviceImpl;

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
import java.util.Map;

import static com.ngomalalibo.stocktradingapp.serviceImpl.UserConfirmation.exists;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService implements ClientService
{
    public static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRJbEhFZC5iVkJCNTNyMXlXdnowc0VlYk8weWRrLjk4U0QxYzZGTFJtNTVrOTZTMUJOcHhKZSIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTQzNTYyMywiZXhwIjoxNTkxNDcxNjIzfQ.7QAPcf8eo7uicZYBT_hgwVi1m6Mvg8LM31Iqqfgz8eM";
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    @Override
    public Object service(Map<String, Object> params) throws NonUniqueResultException, CustomNullPointerException
    {
        Client client = new Client();
        String username = params.get("user").toString();
        String password = params.get("pass").toString();
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
    
    //register new client users
    /*public String register(String username, String password, Client client) throws NonUniqueResultException, CustomNullPointerException
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
    }*/
    
    
}
