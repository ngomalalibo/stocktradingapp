package com.ngomalalibo.stocktradingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StocktradingappApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder)
    {
        return builder.sources(StocktradingappApplication.class);
    }
    public static void main(String[] args)
    {
        SpringApplication.run(StocktradingappApplication.class, args);
    }
    
    /*public static void main(String[] args)
    {
        SpringApplication sa = new SpringApplication(
                StocktradingappApplication.class);
        sa.run(args);
    }*/
    
    
}
