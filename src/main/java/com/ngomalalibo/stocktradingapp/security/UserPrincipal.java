package com.ngomalalibo.stocktradingapp.security;

import com.ngomalalibo.stocktradingapp.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserPrincipal implements UserDetails
{
    
    private User user;
    
    public UserPrincipal(User user)
    {
        super();
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        
        String[] availableRoles = {"USER", "ADMIN", "SUPER_ADMIN"};
        // List.of(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
        for (String role : availableRoles)
        {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
        // return SetUtils.singletonSet(new SimpleGrantedAuthority(this.user.getPersonRoleTypes().displayText()));
        // this.user.getPersonRoleTypes().forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(d.displayText())));
    }
    
    @Override
    public String getPassword()
    {
        // return this.user.getPassword();
        return this.user.getPassword();
    }
    
    @Override
    public String getUsername()
    {
        return this.user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked()
    {
        return false;
    }
    
    @Override
    public boolean isCredentialsNonExpired()
    {
        return false;
    }
    
    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
