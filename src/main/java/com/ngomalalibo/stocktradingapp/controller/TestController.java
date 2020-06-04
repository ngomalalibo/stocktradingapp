package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping(value = {"/test", "/"})
    public ResponseEntity<Object> testing()
    {
        
        return new ResponseEntity<>(new StockQuote("nflx", 444D), HttpStatus.OK);
    }
    
}
