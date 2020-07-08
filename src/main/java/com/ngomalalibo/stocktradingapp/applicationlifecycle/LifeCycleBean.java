package com.ngomalalibo.stocktradingapp.applicationlifecycle;

import com.ngomalalibo.stocktradingapp.database.DatabaseConnection;
import com.ngomalalibo.stocktradingapp.database.MongoConnectionImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Slf4j
@Component
public class LifeCycleBean implements InitializingBean, DisposableBean, BeanNameAware,
        BeanFactoryAware
{
    private MongoConnectionImpl database;
    
    public LifeCycleBean()
    {
        database = new MongoConnectionImpl();
        database.startDB();
        log.info("## I'm in the LifeCycleBean Constructor");
    }
    
    @Override
    public void destroy() throws Exception
    {
        log.info("## The Lifecycle bean has been terminated");
        database.stopDB();
        
    }
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        log.info("## The LifeCycleBean has its properties set!");
        
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        log.info("## Bean Factory has been set");
    }
    
    @Override
    public void setBeanName(String name)
    {
        log.info("## My Bean Name is: " + name);
        
    }
    
    //Bean specific
    @PostConstruct
    public void postConstruct()
    {
        log.info("## Bean Post Construct method has been called");
    }
    
    //Bean specific
    @PreDestroy
    public void preDestroy()
    {
        log.info("## Bean Pre destroy method has been called");
    }
    
    public void beforeInit()
    {
        
        log.info("## - Before Init - Called by Bean Post Processor");
    }
    
    public void afterInit()
    {
        log.info("## - After init called by Bean Post Processor");
        
    }
    
    @EventListener(classes = {ContextStartedEvent.class, ContextStoppedEvent.class/*, ContextClosedEvent.class, ContextRefreshedEvent.class*/})
    public void handleMultipleEvents()
    {
        log.info("Multi-event listener invoked");
    }
}

