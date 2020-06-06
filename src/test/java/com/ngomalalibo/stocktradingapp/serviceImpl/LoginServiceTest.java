package com.ngomalalibo.stocktradingapp.serviceImpl;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

class LoginServiceTest
{
    
    @Mock
    ClientService service;
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @AfterEach
    public void tearDown()
    {
    
    }
    
    @Disabled
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
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        params.put("password", "1234567890");
        
        Mockito.when(service.service(params)).thenReturn(true);
        
        boolean successful = (boolean) service.service(params);
        
        Assertions.assertTrue(successful);
    }
}