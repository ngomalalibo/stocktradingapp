package com.ngomalalibo.stocktradingapp.dataprovider;

import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import com.ngomalalibo.stocktradingapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDP implements UserDetailsService
{
    @Qualifier("userDataRepository")
    @Autowired
    private final GenericDataRepository userDataRepository;
    
    public UsersDP(GenericDataRepository userDataRepository)
    {
        this.userDataRepository = userDataRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        User user = (User) userDataRepository.getRecordByEntityProperty("username", s);
        
        if (user == null)
        {
            throw new UsernameNotFoundException("user not found: " + s);
        }
        
        return new UserPrincipal(user);
    }
}
