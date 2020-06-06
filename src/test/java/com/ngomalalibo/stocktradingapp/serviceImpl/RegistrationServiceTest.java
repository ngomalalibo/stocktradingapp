package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

class RegistrationServiceTest
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
    
    @Test
    void register()
    {
        Client client = new Client("John", "Snow", "john.snow@got.com");
        Map<String, Object> params = new HashMap<>();
        params.put("username", "john.snow@got.com");
        params.put("password", "1234567890");
        params.put("client", client);
        Mockito.when(service.service(params)).thenReturn("successful");
        
        String successful = (String) service.service(params);
        
        Assertions.assertNotNull(successful);
    }
}