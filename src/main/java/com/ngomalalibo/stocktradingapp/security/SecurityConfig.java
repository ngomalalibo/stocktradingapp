package com.ngomalalibo.stocktradingapp.security;

import com.ngomalalibo.stocktradingapp.dataproviders.UsersDP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    { //
        return super.authenticationManagerBean();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(PasswordEncoder.getPasswordEncoder());
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(new UserAuthenticationProvider());
        // auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        return new UsersDP();
        
    }
    
    @Bean
    public static GrantedAuthoritiesMapper authoritiesMapper()
    {
        SimpleAuthorityMapper authMapper = new SimpleAuthorityMapper();
        authMapper.setConvertToUpperCase(true);
        authMapper.setDefaultAuthority("ADMIN");
        return authMapper;
    }
    
    @Override
    public void configure(WebSecurity web)
    {
        // web.ignoring().antMatchers("/**");
        // web.ignoring().antMatchers("/resources/**");
        
        web.ignoring().antMatchers(
        );
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
       /* http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic().disable();
        
        http.csrf().disable().headers().frameOptions().sameOrigin().and().
                authorizeRequests().anyRequest().anonymous().and().httpBasic().disable();*/
        
        http.csrf().disable().authorizeRequests().anyRequest().anonymous().and().httpBasic().disable();
    }
}