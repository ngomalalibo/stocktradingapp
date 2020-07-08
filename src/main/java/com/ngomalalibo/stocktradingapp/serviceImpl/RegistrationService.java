package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import com.ngomalalibo.stocktradingapp.security.JwtTokenProvider;
import com.ngomalalibo.stocktradingapp.security.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.Map;

@Slf4j
@Service
//@RequiredArgsConstructor
public class RegistrationService implements TransactionService
{
    //    public static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRWdHNaVDl1Qi5TUlVncWYuU3A0bEh1NFUxbzRDZDdFei9hOElnbi9zNGlvMXMxVGJoSnpvVyIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5Mjg2ODI5MX0.gkezGEBJERsbbyjJPA9-2SVgCGimnt7rb7WmRRMTaos";
    public static final String token = System.getenv().get("STOCK_API_TOKEN");
    
    @Qualifier("userDataRepository")
    @Autowired
    private GenericDataRepository userDataRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    //register new client users
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
        else if ((token = exists(username)) != null)
        {
            log.info("This user exists in the system. Kindly reset your password of choose another username");
            return token;
            //throw new NonUniqueResultException("This user exists in the system. Kindly reset your password of choose another username");
        }
        User user = new User(username, PasswordEncoder.getPasswordEncoder().encode(password), "USER", username);
        token = jwtTokenProvider.createToken(user);
        user.setToken(token);
        client.setEmail(username);
        
        //  confirm registration was successful by retrieving for user and client from database
        client = (Client) persistingBaseEntity.save(client);
        user = (User) persistingBaseEntity.save(user);
        
        if (user != null && client != null && user.getUsername().equalsIgnoreCase(client.getEmail()))
        {
            return token; // user and client creation confirmed
        }
        return null;
    }
    
    public String exists(String username)
    {
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            return null; // user does not exist
        }
        else
        {
            return user.getToken(); // user already exists
        }
    }
}
