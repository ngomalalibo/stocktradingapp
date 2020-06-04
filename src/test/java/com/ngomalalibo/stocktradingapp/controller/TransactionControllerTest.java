package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
class TransactionControllerTest
{
    private static String buyURL;
    private static String sellURL;
    private static String fundaccountURL;
    
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRRM0lVVjlRRVJWWE5JNXgueVVIaDRlOURHVi9YRVBUUFM0bTVCVUJIcWd0VkNRL0g3VWZTbSIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTI2NTkyNywiZXhwIjoxNTkxMzAxOTI3fQ.T7ckYLfHT1Uzk2OGMNF7KSkmHOpg1HPw1PsnPyCTNCE";
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup()
    {
    
    }
    
    @Test
    void fundAccount() throws Exception
    {
        String template = "/transaction";
        String username = "john.snow@got.com";
        double deposit = 50000;
        String transactionType = "fundaccount";
        
        HashMap<String, Object> request = new HashMap<String, Object>()
        {{
            put("user", username);
            put("deposit", deposit);
            put("transactiontype", transactionType);
        }};
        fundaccountURL = template;
        log.info("fundaccountURL -> " + fundaccountURL);
        
        mockMvc.perform(MockMvcRequestBuilders.post(fundaccountURL)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    
    @Test
    void buyStock() throws Exception
    {
        String template = "/transaction";
        String company = "nflx";
        String username = "john.snow@got.com";
        String transactionType = "buy";
        int units = 2;
        HashMap<String, Object> request = new HashMap<String, Object>()
        {{
            put("companyname", company);
            put("username", username);
            put("units", units);
            put("transactiontype", transactionType);
        }};
        
        buyURL = template;
        log.info("buyURL -> " + buyURL);
        
        mockMvc.perform(MockMvcRequestBuilders.post(buyURL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void sell() throws Exception
    {
        //test data
        String template = "/transaction";
        String company = "nflx";
        String username = "john.snow@got.com";
        int units = 1;
        String transactionType = "sell";
        HashMap<String, Object> request = new HashMap<String, Object>()
        {{
            put("companyname", company);
            put("username", username);
            put("units", units);
            put("transactiontype", transactionType);
        }};
        
        sellURL = template;
        
        mockMvc.perform(MockMvcRequestBuilders.post(sellURL)
                                              .contentType("application/json")
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}