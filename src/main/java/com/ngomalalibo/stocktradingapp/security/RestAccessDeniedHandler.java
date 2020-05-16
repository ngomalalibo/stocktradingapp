package com.ngomalalibo.stocktradingapp.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler, ApplicationContextAware
{
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
    }
    
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException
    {
        log.info("AccessDeniedHandler handler called.......");
        
        HandlerMethod handlerMethod = null;
        try
        {
            ServletContext servletContext = httpServletRequest.getSession().getServletContext();
            context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            log.info("Application context = " + context);
            RequestMappingHandlerMapping req2HandlerMapping = (RequestMappingHandlerMapping) context.getBean("requestMappingHandlerMapping");
            log.info("req2HandlerMapping = " + req2HandlerMapping);
            HandlerExecutionChain handlerExeChain = req2HandlerMapping.getHandler(httpServletRequest);
            log.info("handlerExeChain = " + handlerExeChain);
            if (Objects.nonNull(handlerExeChain))
            {
                handlerMethod = (HandlerMethod) handlerExeChain.getHandler();
                
            }
        }
        catch (Exception ex)
        {
            log.warn("Lookup the handler method", ex);
        }
        
        authenticateNavigation(httpServletRequest.getRequestURI(), e, handlerMethod);
        /*ApiResponse response = new ApiResponse(403, "Access Denied");
        response.setMessage("Access Denied");
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();*/
    }
    
    private void authenticateNavigation(String request, AccessDeniedException e, HandlerMethod h/*, HttpServletResponse httpServletResponse*/)
    {
        Class<?> beanType = h.getBeanType();
        log.info("Controller class Name = " + beanType);
        log.info("Controller method" + h.toString());
        if (!SecurityUtils.isAccessGranted(h.getBeanType()))//going somewhere without access
        {
            if (!"login".equals(request) && !"register".equals(request) && !"emailvalidation".equals(request)) //exclude these classes from check
            {
                if (SecurityUtils.isUserLoggedIn())
                {
                    log.info("Spring Sec Forbidden -> Access Control");
                    
                    throw new AccessDeniedException("Stock Trading Inc. - Forbidden -> Access Control");
                }
            }
        }
    }
}

