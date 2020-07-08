package com.ngomalalibo.stocktradingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

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
        ConfigurableApplicationContext run = SpringApplication.run(StocktradingappApplication.class, args);
        /*String[] beanDefinitionNames = run.getBeanDefinitionNames();
        System.out.println("Beans: " + Arrays.stream(beanDefinitionNames).collect(Collectors.joining(", ", "[", "]")));*/
        
    }
    
}
