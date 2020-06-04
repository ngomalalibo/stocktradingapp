package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class RegistrationServiceTest
{
    @Mock
    RegistrationService service;
    
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
        Mockito.when(service.register("john.snow@got.com", "1234567890", client)).thenReturn("successful");
        
        String successful = service.register("john.snow@got.com", "1234567890", client);
        
        Assertions.assertNotNull(successful);
    }
}