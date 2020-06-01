package com.ngomalalibo.stocktradingapp.exception;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver
{
    public CustomSimpleMappingExceptionResolver()
    {
        super();
    }
    
    @Override
    public void setDefaultErrorView(String defaultErrorView)
    {
        super.setDefaultErrorView(defaultErrorView);
    }
    
    @Override
    public void setStatusCodes(Properties statusCodes)
    {
        super.setStatusCodes(statusCodes);
    }
    
    @Override
    public void addStatusCode(String viewName, int statusCode)
    {
        super.addStatusCode(viewName, statusCode);
    }
    
    @Override
    public void setDefaultStatusCode(int defaultStatusCode)
    {
        super.setDefaultStatusCode(defaultStatusCode);
    }
    
    @Override
    protected ModelAndView getModelAndView(String viewName, Exception ex)
    {
        return super.getModelAndView(viewName, ex);
    }
}
