package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.exception.ApiResponse;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import com.ngomalalibo.stocktradingapp.serviceImpl.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class RegistrationController
{
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Qualifier("registrationService")
    @Autowired
    TransactionService services;
    
    @PostMapping(value = "/registration")
    public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> request)
    {
        String token = (String) services.service(request);
        if (token != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User has been registered successfully with token " +
                    token, HttpStatus.OK.getReasonPhrase());
            return ResponseEntity.ok()
                                 .header("API_TOKEN", token)
                                 .body((Object) apiResponse);
        }
        else
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "User creation failed", HttpStatus.BAD_REQUEST.getReasonPhrase());
            // return ResponseEntity.of(Optional.of(apiResponse));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed");
        }
    }
/*public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> request)
    {
        
        return Optional.ofNullable(services.register(request.get("user").toString(), request.get("pass").toString(), new Client())).filter(Objects::nonNull)
                       .map(token -> ResponseEntity.ok()
                                                   .header("API_TOKEN", "User has been registered successfully with token \n" + token)
                                                   .body((Object) new ApiResponse(HttpStatus.OK, "User has been registered successfully with token " +
                                                           token, HttpStatus.OK.getReasonPhrase())))
                       .orElse(ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST, "User creation failed", HttpStatus.BAD_REQUEST.getReasonPhrase())));
    }*/
    
    @PostMapping("/currentuser")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserPrincipal userDetails)
    {
        if (userDetails != null)
        {
            Map<Object, Object> model = new HashMap<>();
            model.put("username", userDetails.getUsername());
            model.put("roles", userDetails.getAuthorities());
            return ok(model);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user principal");
    }
}
