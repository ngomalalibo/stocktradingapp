package com.ngomalalibo.stocktradingapp.applicationlifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationContextProvider implements ApplicationContextAware
{
    private static ApplicationContext ctx = null;
    
    @Bean
    public static ApplicationContext getApplicationContext()
    {
        return ctx;
    }
    
    
    public void setApplicationContext(ApplicationContext ctx) throws BeansException
    {
        log.info("## Application context has been set");
        this.ctx = ctx;
    }
}