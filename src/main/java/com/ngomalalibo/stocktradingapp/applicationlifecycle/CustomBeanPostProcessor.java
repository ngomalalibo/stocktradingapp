package com.ngomalalibo.stocktradingapp.applicationlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor
{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        //put code here that will be run before bean initialization
        if (bean instanceof LifeCycleBean)
        {
            ((LifeCycleBean) bean).beforeInit();
        }
        
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        //put code here that will be run after bean initialization
        if (bean instanceof LifeCycleBean)
        {
            ((LifeCycleBean) bean).afterInit();
        }
        
        return bean;
    }
}
