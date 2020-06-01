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
//               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)));
        
        //SUCCEED TEST
        mockMvc.perform(MockMvcRequestBuilders.post(registerURL)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(objectMapper.writeValueAsBytes(passRequest))
                                              .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void fundAccount() throws Exception
    {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRHZk8wblI4Y055NDhVVGVTZ013NUN1clVONGl0bXRHUTMyazIvdk1TaEozMENzc3NCd2cvLiIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTAxODQ1OCwiZXhwIjoxNTkxMDU0NDU4fQ.1C_3hL0Cmt9pWSirQMCuq0G7LC36D6VFmXr3FnaYVBc";
        
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
    void getStockPrice() throws Exception
    {
        StockRequest stockRequest = new StockRequest();
        stockRequest.setCompanyname("Netflix");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRHZk8wblI4Y055NDhVVGVTZ013NUN1clVONGl0bXRHUTMyazIvdk1TaEozMENzc3NCd2cvLiIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTAxODQ1OCwiZXhwIjoxNTkxMDU0NDU4fQ.1C_3hL0Cmt9pWSirQMCuq0G7LC36D6VFmXr3FnaYVBc";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/stockprice/{companyname}", "nflx")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(stockRequest)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void buyStock() throws Exception
    {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRHZk8wblI4Y055NDhVVGVTZ013NUN1clVONGl0bXRHUTMyazIvdk1TaEozMENzc3NCd2cvLiIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTAxODQ1OCwiZXhwIjoxNTkxMDU0NDU4fQ.1C_3hL0Cmt9pWSirQMCuq0G7LC36D6VFmXr3FnaYVBc";
        
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
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRHZk8wblI4Y055NDhVVGVTZ013NUN1clVONGl0bXRHUTMyazIvdk1TaEozMENzc3NCd2cvLiIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTAxODQ1OCwiZXhwIjoxNTkxMDU0NDU4fQ.1C_3hL0Cmt9pWSirQMCuq0G7LC36D6VFmXr3FnaYVBc";
        
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
    
    @Test
    void viewStocks_returnsClientPortfolio() throws Exception
    {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29tYWxhbGlib0B5YWhvby5jb20iLCJwYXNzd29yZCI6IiQyYSQxMSRHZk8wblI4Y055NDhVVGVTZ013NUN1clVONGl0bXRHUTMyazIvdk1TaEozMENzc3NCd2cvLiIsInJvbGVzIjoiVVNFUiIsImlhdCI6MTU5MTAxODQ1OCwiZXhwIjoxNTkxMDU0NDU4fQ.1C_3hL0Cmt9pWSirQMCuq0G7LC36D6VFmXr3FnaYVBc";
        
        String template = "/portfolio";
        String username = "john.snow@got.com";
        HashMap<String, Object> request = new HashMap<String, Object>()
        {{
            put("username", username);
        }};
        portfolioURL = template;
        log.info("portfolioURL -> " + portfolioURL);
        mockMvc.perform(MockMvcRequestBuilders.post(portfolioURL).contentType(MediaType.APPLICATION_JSON)
                                              .param("token", token)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(16)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("john.snow@got.com")));
        
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
    
    private static String loginURL;
    private static String registerURL;
    private static String buyURL;
    private static String sellURL;
    private static String portfolioURL;
    private static String stockpriceURL;
    private static String fundaccountURL;
    
    
}