package com.ngomalalibo.stocktradingapp.exception;

import org.springframework.security.access.AccessDeniedException;

// @ResponseStatus(HttpStatus.FORBIDDEN) //map exception to HTTP status so it can be caught in a view
public class CustomAccessDeniedException extends AccessDeniedException
{
    public CustomAccessDeniedException(String msg)
    {
        super(msg);
    }
    
    public CustomAccessDeniedException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
