package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.aspect.Loggable;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@RequiredArgsConstructor
public class LoginService
{
    //@Qualifier("userDataRepository")
    @Autowired
    private GenericDataRepository userDataRepository;
    
    public static boolean loginStatus = false;
    
    // login to application with spring security providing form-based authentication and authorization. Logged via pointcut @Loggable
    @Loggable
    public boolean login(String username, String password, AuthenticationManager authenticationManager) throws AuthenticationException
    {
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);
        
        // try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
        log.info("authenticating -> login " + username);
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password)); //
        // if authentication was successful we will update the security context and redirect to the page requested first
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        if (user != null)
        {
            log.info("user roles -> " + user.getRole());
        }
        
        loginStatus = true; // set login status to true
        
        log.info("Logged in successfully.......");
        
        return loginStatus;
    }
    
}
