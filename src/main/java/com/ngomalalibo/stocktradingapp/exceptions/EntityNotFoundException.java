package com.ngomalalibo.stocktradingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // This exception is mapped to the HttpStatus.NOT_FOUND status so it will be caught with the appropriate view
public class EntityNotFoundException extends RuntimeException
{
    
    public EntityNotFoundException()
    {
        super();
    }
    
    public EntityNotFoundException(String message)
    {
        super(message);
    }
}
