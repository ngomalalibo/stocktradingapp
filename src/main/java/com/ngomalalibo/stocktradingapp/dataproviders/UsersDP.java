package com.ngomalalibo.stocktradingapp.dataproviders;

import com.google.common.base.Strings;

import com.ngomalalibo.stocktradingapp.dataService.GenericDataService;
import com.ngomalalibo.stocktradingapp.entities.User;
import com.ngomalalibo.stocktradingapp.security.PasswordEncoder;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsersDP implements UserDetailsService
{
    private final GenericDataService gds = new GenericDataService(new User());
    
    private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    public UsersDP()
    {
        super();
    }
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        User user = (User) gds.getRecordByEntityProperty("username", s);
        
        if (user == null)
        {
            throw new UsernameNotFoundException("user not found: " + s);
        }
        
        return new UserPrincipal(user);
    }
}
