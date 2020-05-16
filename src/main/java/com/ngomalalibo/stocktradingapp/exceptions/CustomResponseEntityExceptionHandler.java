package com.ngomalalibo.stocktradingapp.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors())
        {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
        {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return buildResponseEntity(apiResponse);
        
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiResponse)
    {
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    
    @ExceptionHandler({CustomNullPointerException.class, CustomRuntimeException.class, InsufficientCaseException.class})
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return buildResponseEntity(apiResponse);
        /*return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);*/
    }
    
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String template = "Missing parameter:  %s. Missing parameter: %s";
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), String.format(template, ex.getMessage(), ex.getParameter()));
        return handleExceptionInternal(
                ex, apiResponse, headers, apiResponse.getStatus(), request);
    }
    
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request)
    {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations())
        {
            errors.add(violation.getRootBeanClass().getName() + " " +
                               violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(
                apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
    
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request)
    {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
    
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request)
    {
        String error = ex.getParameterName() + " parameter is missing";
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
    
    @ExceptionHandler({Exception.class, CustomAccessDeniedException.class})  //Default
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request)
    {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(
                apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
}
