package com.ngomalalibo.stocktradingapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.dataService.TestDataInitialization;
import com.ngomalalibo.stocktradingapp.entities.User;
import com.ngomalalibo.stocktradingapp.models.StockRequest;
import com.ngomalalibo.stocktradingapp.services.Services;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
// @WebMvcTest(controllers = StockController.class) //If we leave away the controllers parameter, Spring Boot will include all controllers in the application context.
// @ExtendWith(SpringExtension.class)
class StockControllerTest
{
    private RequestPostProcessor requestPostProcessor;
    private RequestPostProcessor requestPostProcessor_approver;
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    Services services;
    
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
    void register() throws Exception
    {
        String template = "/register?name=%s&pass=%s";
        String username = "ngomalalibo@yahoo.com";
        String password = "1234567890";
        registerURL = String.format(template, username, password);
        log.info("Url -> " + registerURL);
        
        String failEmail = "";
        String failPassword = "password";
        
        // FAIL test
        
        mockMvc.perform(MockMvcRequestBuilders.post(String.format(template, failEmail, failPassword))
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)));
    }
    
    @Test
    void login() throws Exception
    {
        String template = "/login?user=%s&pass=%s";
        String username = "john.snow@got.com";
        String password = "1234567890";
        String role = "USER";
        
        User user = new User(username, password, role, username);
        
        boolean passResult = true;
        
        loginURL = String.format(template, username, password);
        log.info("LoginURL -> " + loginURL);
        
        AuthenticationManager mgr = new AuthenticationManager()
        {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException
            {
                return null;
            }
        };
        
        mockMvc.perform(MockMvcRequestBuilders.post(template, username, password).content(objectMapper.writeValueAsString(user))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void fundAccount() throws Exception
    {
        String template = "/fundaccount?user=%s&deposit=%f";
        String username = "john.snow@got.com";
        double deposit = 50000;
        
        fundaccountURL = String.format(template, username, deposit);
        log.info("fundaccountURL -> " + fundaccountURL);
        
        mockMvc.perform(MockMvcRequestBuilders.post(fundaccountURL)
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void getStockPrice() throws Exception
    {
        StockRequest stockRequest = new StockRequest();
        stockRequest.setCompanyname("Netflix");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/stockprice/{companyname}", "nflx")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(stockRequest)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void checkStockPrice() throws Exception
    {
        // String companyName = "Netflix, Inc.";
        //
        // Stock stock = new Stock(companyName, 500D);
        //
        String template = "/stockprice/{companyname}";
        mockMvc.perform(MockMvcRequestBuilders.post(template, "nflx"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void buyStock() throws Exception
    {
        String template = "/buy?companyname=%s&username=%s&units=%d";
        String company = "nflx";
        String username = "john.snow@got.com";
        int units = 2;
        
        boolean result = true;
        
        buyURL = String.format(template, company, username, units);
        log.info("buyURL -> " + buyURL);
        
        mockMvc.perform(MockMvcRequestBuilders.post(buyURL)
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void sell() throws Exception
    {
        String template = "/sell?companyname=%s&username=%s&units=%d";
        
        //test data
        String username = "john.snow@got.com";
        String company = "nflx";
        int units = 1;
        
        mockMvc.perform(MockMvcRequestBuilders.post(String.format(template, company, username, units))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void viewStocks_returnsClientPortfolio() throws Exception
    {
        String template = "/portfolio?username=%s";
        String username = "john.snow@got.com";
        portfolioURL = String.format(template, username);
        log.info("portfolioURL -> " + portfolioURL);
        mockMvc.perform(MockMvcRequestBuilders.post(portfolioURL))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(16)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("john.snow@got.com")));
        
    }
    
    @Test
    void testingTemplate() throws Exception
    {
        String template = "/test";
        mockMvc.perform(MockMvcRequestBuilders.post("/test")
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