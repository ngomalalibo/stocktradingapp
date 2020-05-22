package com.ngomalalibo.stocktradingapp.controllers;

import com.ngomalalibo.stocktradingapp.entities.*;
import com.ngomalalibo.stocktradingapp.exceptions.ApiResponse;
import com.ngomalalibo.stocktradingapp.exceptions.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exceptions.InsufficientCaseException;
import com.ngomalalibo.stocktradingapp.security.JwtTokenProvider;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import com.ngomalalibo.stocktradingapp.services.Services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NonUniqueResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

// import org.springframework.security.access.annotation.Secured;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.AuthenticationException;

@Slf4j
@RestController
// @RequestMapping("/stocktradingapp")
public class StockController// implements ApplicationContextAware
{
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    Services services;
    
    ApplicationContext applicationContext = null;
    
    public StockController(Services services)
    {
        this.services = services;
    }
    
    /*@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }*/
    
    @PostMapping(value = "/register")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> register(@RequestParam("user") String user, @RequestParam("pass") String pass)
    {
        try
        {
            boolean successful = services.register(user, pass, new Client());
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User has been registered successfully", HttpStatus.OK.getReasonPhrase());
                return ok(apiResponse);
            }
            else
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "User creation failed", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                // return ResponseEntity.of(Optional.of(apiResponse));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User creation failed");
            }
        }
        catch (NonUniqueResultException | CustomNullPointerException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User NOT created " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User data)
    {
        try
        {
            String username = data.getUsername();
            String password = data.getPassword();
            String role = data.getRole();
            services.login(username, password, authenticationManager);
            // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, data.getRole());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        }
        catch (AuthenticationException e)
        {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
    
    @GetMapping("/me")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserPrincipal userDetails)
    {
        if (userDetails != null)
        {
            Map<Object, Object> model = new HashMap<>();
            model.put("username", userDetails.getUsername());
            model.put("roles", userDetails.getAuthorities()
                                          .stream()
                                          .map(GrantedAuthority::getAuthority));
            return ok(model);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user principal");
        
        
    }
    
    /*@PostMapping({"/login", "/"})
    public ResponseEntity<Object> login(@RequestParam("user") String user, @RequestParam("pass") String pass, AuthenticationManager authenticationManager)
    {
        try
        {
            boolean successful = services.login(user, pass, authenticationManager);
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User has been logged in successfully", HttpStatus.OK.getReasonPhrase());
                return ResponseEntity.ok(apiResponse);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User login failed");
            }
        }
        catch (AuthenticationException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User NOT created " + e.getMessage());
        }
        
    }*/
    
    // @Secured(value = {"USER"})
    @PostMapping(value = "/fundaccount")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> fundAccount(@RequestParam("user") String user, @RequestParam("deposit") Double deposit)
    {
        try
        {
            boolean successful = services.fundAccount(user, deposit);
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Account funded successfully", HttpStatus.OK.getReasonPhrase());
                return ok(apiResponse);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Account funding failed");
            }
            
            
        }
        catch (CustomNullPointerException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account funding failed " + e.getMessage());
        }
    }
    
    // @Secured(value = {"USER"})
    // @PostMapping("/stockprice")
    @GetMapping("/stockprice/{companyname}")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> checkStockPrice(@PathVariable(name = "companyname") String companyname)
    {
        try
        {
            Stock stock = services.getStock(companyname);
            if (stock != null)
            {
                return ok(stock);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
        }
        catch (CustomNullPointerException e)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Could not retrieve stock price", e.getMessage());
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
    
    
    // @Secured(value = {"ADMIN"})
    @PostMapping("/buy")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> buyStock(@RequestParam("companyname") String companyname,
                                           @RequestParam("username") String username,
                                           @RequestParam("units") Integer units)
    {
        try
        {
            boolean successful = services.buy(companyname, username, units);
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Purchase completed successfully", HttpStatus.OK.getReasonPhrase());
                return ok(apiResponse);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Purchase failed");
            }
        }
        catch (CustomNullPointerException | InsufficientCaseException e)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Purchase failed", e.getMessage());
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
    
    // @Secured(value = {"USER"})
    @PostMapping("/sell")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> sell(@RequestParam("companyname") String companyname,
                                       @RequestParam("username") String username,
                                       @RequestParam("units") Integer units)
    {
        try
        {
            boolean successful = services.sell(companyname, username, units);
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Sale transaction completed successfully", HttpStatus.OK.getReasonPhrase());
                return ok(apiResponse);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sell transaction failed.");
            }
        }
        catch (InsufficientCaseException e)
        {
            
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Sell transaction failed", e.getMessage());
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
    
    // @Secured(value = {"USER"})
    // @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/portfolio", method = RequestMethod.POST)
    public ResponseEntity<Object> viewStocks(@RequestParam("username") String username)
    {
        try
        {
            List<ClientTransaction> allClientTransactions = services.getAllClientTransactions(username);
            if (allClientTransactions != null && allClientTransactions.size() > 0)
            {
                ClientPortfolio portfolio = services.getPortfolio(allClientTransactions, username);
                return ok(portfolio);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error retrieving client portfolio. Bad Request");
            }
        }
        catch (CustomNullPointerException | InsufficientCaseException e)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Error retrieving client portfolio. Bad Request", e.getMessage());
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
    
    
    // @Secured(value = {"ADMIN"})
    // @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = {"/test", "/"})
    public ResponseEntity<Object> testing()
    {
        
        return new ResponseEntity<>(new Stock("nflx", 444D), HttpStatus.OK);
    }
}
