package com.ngomalalibo.stocktradingapp.dataprovider;

import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDP implements UserDetailsService
{
    private final GenericDataRepository gds;
    
    // private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    public UsersDP()
    {
        super();
        gds = new GenericDataRepository(new User());
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
