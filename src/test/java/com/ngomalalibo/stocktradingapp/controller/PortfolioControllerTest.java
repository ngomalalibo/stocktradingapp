package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.serviceImpl.RegistrationService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class PortfolioControllerTest
{
    private static String portfolioURL;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup()
    {
    }
    
    
    @Test
    void viewStocks_returnsClientPortfolio() throws Exception
    {
        String template = "/portfolio";
        String username = "john.snow@got.com";
        HashMap<String, Object> request = new HashMap<String, Object>()
        {{
            put("username", username);
        }};
        portfolioURL = template;
        log.info("portfolioURL -> " + portfolioURL);
        mockMvc.perform(MockMvcRequestBuilders.post(portfolioURL)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("token", RegistrationService.token)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(16)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("john.snow@got.com")));
        
    }
}