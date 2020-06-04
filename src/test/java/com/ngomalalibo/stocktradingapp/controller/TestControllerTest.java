package com.ngomalalibo.stocktradingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.repository.TestDataInitialization;
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
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class TestControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup()
    {
    }
    
    @Test
    void testingTemplate() throws Exception
    {
        String template = "/test";
        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk());
        
        
        //Create resource location
        /*URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand("employee.getId()")
                                                  .toUri();*/
        //Send location in response
        /*return ResponseEntity.created(location).build();*/
    }
}