package com.ngomalalibo.stocktradingapp.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils extends HandlerInterceptorAdapter
{
    
    public SecurityUtils()
    {
        // Util methods only
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        log.info("Interceptor preHandle interceptor called.......");
    
        /*SpringViewProvider
        com.vaadin.navigator.Navigator navigator = new Navigator(UI.getCurrent(), this);
        // Without an AccessDeniedView, the view provider would act like the restricted views did not exist at all.
        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(springViewProvider);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(navigator.getState());
        
        */
        
        Class aClass = getClassFromURIObject(handler.toString());
        if (!SecurityUtils.isAccessGranted(aClass))//going somewhere without access
        {
            String endpoint = aClass.getEnclosingMethod().getName();
            // if (!"login".equals(target) && !"register".equals(target) && !"emailvalidation".equals(target))
            if (!"login".equals(endpoint) && !"register".equals(endpoint) && !"uservalidation".equals(endpoint))
            {
                if (SecurityUtils.isUserLoggedIn())
                {
                    log.info("Interceptor preHandle Forbidden -> Access Control");
                    log.info("Interceptor preHandle Throwing access denied exception");
                }
                else
                {
                    response.sendRedirect("login");
                    // rerouteTo(LoginPage.class);
                }
            }
        }
        
        return true;
    }
    
    public static boolean isAccessGranted(Class<?> securedClass)
    {
        String endpoint = securedClass.getEnclosingMethod().getName();
        List<String> viewRoles = new ArrayList<>();
        if ("login".equals(endpoint) || "register".equals(endpoint) || "uservalidation".equals(endpoint))
        {
            return true;
        }
        
        if (!isUserLoggedIn())
        { //
            return false;
        }
        // Allow if no roles are required.
        Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(securedClass, PreAuthorize.class);
        SecuredByRole securedByRole = AnnotationUtils.findAnnotation(securedClass, SecuredByRole.class);
        if (secured == null && preAuthorize == null && securedByRole == null)
        {
            return true; // grant access
        }
        if (secured != null)
        {
            viewRoles = Arrays.asList(secured.value());
        }
        if (preAuthorize != null)
        {
            String values = preAuthorize.value();
            log.info("values -> " + values);
            List<String> dd;
            if (values.contains(","))
            {
                // dd = List.of(values.split(","));
                dd = Arrays.asList(values.split(","));
            }
            else
            {
                // dd = List.of(values.split(" "));
                dd = Arrays.asList(values.split(" "));
            }
            
            viewRoles = dd.stream().map(String::trim).collect(Collectors.toList());
            
        }
        if (securedByRole != null)
        {
            viewRoles = Arrays.asList(securedByRole.value());
        }
        
        // lookup needed role in user roles
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> cleanRoles = new ArrayList<>();
        viewRoles.forEach(d ->
                          {
                              if (d.contains("ADMIN"))
                              {
                                  cleanRoles.add("ADMIN");
                              }
                              if (d.contains("USER"))
                              {
                                  cleanRoles.add("USER");
                              }
                              if (d.contains("SUPER_ADMIN"))
                              {
                                  cleanRoles.add("SUPER_ADMIN");
                              }
                          });
        log.info(userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ", "User has ", " role(s)")));
        log.info(cleanRoles.stream().collect(Collectors.joining(", ", securedClass.getSimpleName() + " view requires ", " role(s) for access")));
        return userAuthentication.getAuthorities().stream().map(d -> Strings.toRootUpperCase(d.toString())).collect(Collectors.toList()).containsAll(cleanRoles);
        // return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(allowedRoles::contains);
    }
    
    public static boolean isUserLoggedIn()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
    
    public static void main(String[] args)
    {
    
    }
    
    public static Class<?> getClassFromURIObject(String packageFile)
    {
        String[] ee = packageFile.split("#");
        packageFile = ee[0];
        // log.info("packageFile -> " + packageFile);
        try
        {
            return Class.forName(packageFile);
            
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    
}