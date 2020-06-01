package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

class LoginServiceTest
{
    
    @Mock
    LoginService service;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @AfterEach
    public void tearDown()
    {
    
    }
    
    @Test
    void login()
    {
        AuthenticationManager mgr = new AuthenticationManager()
        {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException
            {
                return null;
            }
        };
        
        
        Mockito.when(service.login("john.snow@got.com", "1234567890", mgr)).thenReturn(true);
        
        boolean successful = service.login("john.snow@got.com", "1234567890", mgr);
        
        Assertions.assertTrue(successful);
    }
}