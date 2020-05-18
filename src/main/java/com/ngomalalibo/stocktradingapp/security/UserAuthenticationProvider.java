package com.ngomalalibo.stocktradingapp.security;

import com.google.common.base.Strings;
import com.ngomalalibo.stocktradingapp.dataService.GenericDataService;
import com.ngomalalibo.stocktradingapp.dataproviders.UsersDP;
import com.ngomalalibo.stocktradingapp.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserAuthenticationProvider extends DaoAuthenticationProvider
{
    public UserAuthenticationProvider()
    {
        super();
    }
    
    private static GenericDataService gds = new GenericDataService(new User());
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        log.info("authenticating......");
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // log.info("authenticate -> " + username);
        // log.info("credentials -> " + password);
        
        if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(username))
        {
            throw new BadCredentialsException("Invalid username or password.");
        }
        
        User usere = (User) gds.getRecordByEntityProperty("username", username);
        
        if (usere == null)
        {
            throw new BadCredentialsException("User does not exist");
        }
        
        if (PasswordEncoder.getPasswordEncoder().matches(password, usere.getPassword()))
        {
            Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
            
            String[] availableRoles = {"USER", "ADMIN", "SUPER_ADMIN"};
            // List.of(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
            for (String role : availableRoles)
            {
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            log.info("authorities -> " + grantedAuthorities.toString());
            
            usere.replaceEntity(usere, usere);
            
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        }
        else
        {
            usere.replaceEntity(usere, usere);
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    @Override
    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper)
    {
        super.setAuthoritiesMapper(SecurityConfig.authoritiesMapper());
    }
    
    
    @Override
    public void setPasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder)
    {
        super.setPasswordEncoder(PasswordEncoder.getPasswordEncoder());
    }
    
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        super.setUserDetailsService(new UsersDP());
    }
}
