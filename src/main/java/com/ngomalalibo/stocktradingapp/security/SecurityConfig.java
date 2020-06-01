package com.ngomalalibo.stocktradingapp.security;

import com.ngomalalibo.stocktradingapp.dataprovider.UsersDP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        /*http.csrf().disable().authorizeRequests()
            .antMatchers("/", "/login", "/test").permitAll()
            .antMatchers("/**").hasAnyAuthority("USER", "ADMIN")
            .anyRequest()
            .authenticated()
            .and().httpBasic();*/
        
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/stocktradingapp/**", "/test", "/registration", "/me").permitAll()
//                .antMatchers(/*"/stockprice/**",*/  "/transatcion/**", "/portfolio/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        
       /* http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic().disable();*/
        
       /* http.csrf().disable().headers().frameOptions().sameOrigin().and().
                authorizeRequests().anyRequest().anonymous().and().httpBasic().disable();*/
        
        // http.csrf().disable().authorizeRequests().anyRequest().anonymous().and().httpBasic().disable();
    }
    
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    { //
        return super.authenticationManagerBean();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        UserAuthenticationProvider provider = new UserAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(PasswordEncoder.getPasswordEncoder());
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
    }
    
    
    @Bean
    public static GrantedAuthoritiesMapper authoritiesMapper()
    {
        SimpleAuthorityMapper authMapper = new SimpleAuthorityMapper();
        authMapper.setConvertToUpperCase(true);
        authMapper.setDefaultAuthority("USER");
        authMapper.setPrefix("");
        return authMapper;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(new UserAuthenticationProvider());
        // auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
       /* List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder().username("admin").password("password").roles("USER", "ADMIN").build());
        users.add(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        {
        }
        ;
        return new InMemoryUserDetailsManager(users);*/
        return new UsersDP();
        
    }
    
    
    
   /* @Override
    public void configure(WebSecurity web)
    {
        // web.ignoring().antMatchers("/**");
        // web.ignoring().antMatchers("/resources/**");
        
        web.ignoring().antMatchers(
        );
    }*/
    
    
}