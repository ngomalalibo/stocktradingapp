package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.model.StockRequest;
import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
// @WebMvcTest(controllers = StockController.class) //If we leave away the controllers parameter, Spring Boot will include all controllers in the application context.
// @ExtendWith(SpringExtension.class)
class StockQuoteControllerTest
{
    private RequestPostProcessor requestPostProcessor;
    private RequestPostProcessor requestPostProcessor_approver;
    
    private static String buyURL;
    private static String sellURL;
    private static String portfolioURL;
    private static String stockpriceURL;
    private static String fundaccountURL;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    TestDataInitialization testData;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @BeforeEach
    public void setup()
    {
        // Mockito.reset(services);
        // mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //testData = new TestDataInitialization();
    }
    
    
    
   
    
    
    
    
    
   
    
    
}