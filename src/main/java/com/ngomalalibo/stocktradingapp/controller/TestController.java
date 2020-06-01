package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.*;
import com.ngomalalibo.stocktradingapp.security.JwtTokenProvider;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import com.ngomalalibo.stocktradingapp.service.UserConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

// import org.springframework.security.access.annotation.Secured;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.AuthenticationException;

@Slf4j
@RestController
public class TestController// implements ApplicationContextAware
{
    @Autowired
    AuthenticationManager authenticationManager;
    
    // @Secured(value = {"ADMIN"})
    // @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = {"/test", "/"})
    public ResponseEntity<Object> testing()
    {
        
        return new ResponseEntity<>(new StockQuote("nflx", 444D), HttpStatus.OK);
    }
    
    
    
    
   
}
