package com.ngomalalibo.stocktradingapp.controllers;

import com.ngomalalibo.stocktradingapp.services.Services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class UserController extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        log.info("logout controller in LogoutHandler");
       /* VaadinSession session = VaadinSession.getCurrent();
        System.out.println("session = " + session);
        if (session != null)
        {
            session.setAttribute("libraryuser", null);
            session.setAttribute("userrole", null);
            session.setAttribute("username", null);
            session.setAttribute("password", null);
            
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getSession().close();
            UI.getCurrent().setPollInterval(3000);
        }*/
        
        Services.loginStatus = false;
        
        super.onLogoutSuccess(request, response, authentication);
    }
   /* public static void logOut()
    {
        log.info("logOut()");
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("userrole", null);
        session.setAttribute("username", null);
        UI.getCurrent().navigate(LoginPage.class);
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getSession().close();
        UI.getCurrent().setPollInterval(3000);
//		SecurityContextHolder.clearContext();
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
//		new SecurityContextLogoutHandler().logout(request, null, auth);
    }*/
    
}
