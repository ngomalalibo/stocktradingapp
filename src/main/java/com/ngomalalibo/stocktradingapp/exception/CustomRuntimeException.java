package com.ngomalalibo.stocktradingapp.exception;

// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomRuntimeException extends RuntimeException
{
    public CustomRuntimeException()
    {
        super();
    }
    
    public CustomRuntimeException(String message)
    {
        super(message);
    }
    
    public CustomRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
