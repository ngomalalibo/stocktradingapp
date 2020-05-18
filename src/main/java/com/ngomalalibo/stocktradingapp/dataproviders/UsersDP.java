package com.ngomalalibo.stocktradingapp.dataproviders;

import com.ngomalalibo.stocktradingapp.dataService.GenericDataService;
import com.ngomalalibo.stocktradingapp.entities.User;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDP implements UserDetailsService
{
    private final GenericDataService gds;
    
    // private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    public UsersDP()
    {
        super();
        gds = new GenericDataService(new User());
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
