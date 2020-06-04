package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.model.StockRequest;
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

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class StockPriceControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup()
    {
    }
    
    @Test
    void getStockPrice() throws Exception
    {
        StockRequest stockRequest = new StockRequest();
        stockRequest.setCompanyname("Netflix");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRRM0lVVjlRRVJWWE5JNXgueVVIaDRlOURHVi9YRVBUUFM0bTVCVUJIcWd0VkNRL0g3VWZTbSIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTI2NTkyNywiZXhwIjoxNTkxMzAxOTI3fQ.T7ckYLfHT1Uzk2OGMNF7KSkmHOpg1HPw1PsnPyCTNCE";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/stockprice/{companyname}", "nflx")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(stockRequest)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}