package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
import com.ngomalalibo.stocktradingapp.serviceImpl.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class RegistrationControllerTest
{
    private static String registerURL;
    
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
    }
    
    @Test
    void registration() throws Exception
    {
        String template = "/registration";
        String username = "ngomalalibo@yahoo.com";
        String password = "1234567890";
        HashMap<String, Object> passRequest = new HashMap<String, Object>()
        {{
            put("user", username);
            put("pass", password);
        }};
        
        registerURL = template;
        log.info("Url -> " + registerURL);
        
        String failEmail = "";
        String failPassword = "password";
        HashMap<String, Object> failRequest = new HashMap<String, Object>()
        {{
            put("user", failEmail);
            put("pass", failPassword);
        }};
        
        // FAIL test
        mockMvc.perform(MockMvcRequestBuilders.post(template)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(objectMapper.writeValueAsBytes(failRequest))
                                              .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)));
        
        //SUCCEED TEST
        mockMvc.perform(MockMvcRequestBuilders.post(registerURL)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(objectMapper.writeValueAsBytes(passRequest))
                                              .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void currentUser() throws Exception
    {
        String template = "/currentuser";
        
        mockMvc.perform(MockMvcRequestBuilders.post(template)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("token", RegistrationService.token)
                                              .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo("ngomalalibo@yahoo.com")))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}