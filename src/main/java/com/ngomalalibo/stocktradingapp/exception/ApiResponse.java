package com.ngomalalibo.stocktradingapp.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class ApiResponse
{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;
    
    public ApiResponse(HttpStatus status, String message, List<String> errors)
    {
        super();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
    
    public ApiResponse(HttpStatus status, String message, String error)
    {
        super();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}