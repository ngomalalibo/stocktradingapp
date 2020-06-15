package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.enumeration.ActivityLogType;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService
{
    private static GenericDataRepository userGDS = new GenericDataRepository(new User());
    private static GenericDataRepository clientGDS = new GenericDataRepository(new Client());
    private static GenericDataRepository clientAccountGDS = new GenericDataRepository(new ClientAccount());
    private static GenericDataRepository transactionsGDS = new GenericDataRepository(new ClientTransaction());
    public static boolean loginStatus = false;
    
    // login to application with spring security providing form-based authentication and authorization
    public boolean login(String username, String password, AuthenticationManager authenticationManager) throws AuthenticationException
    {
        User user = (User) userGDS.getRecordByEntityProperty("username", username);
        
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
        
        // log access activity
        ActivityLog alog = new ActivityLog();
        alog.setUser(username);
        alog.setActivityLogType(ActivityLogType.INFO);
        alog.setEventName("LOGIN");
        alog.setEventDescription("logged in at "
                                         + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy 'at' h:mm a")));
        alog.save(alog); // persist
        
        loginStatus = true; // set login status to true
        
        log.info("Logged in successfully.......");
        
        return loginStatus;
    }
    
}
