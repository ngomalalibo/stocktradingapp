package com.ngomalalibo.stocktradingapp.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfiguration //extends AuthorizationServerConfigurerAdapter
{
    /*private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    private final AuthenticationManager authenticationManager;
    
    private final UsersDP userService;
    
    @Value("${jwt.clientId:stocktradingapp}")
    private String clientId;
    
    @Value("${jwt.client-secret:secret}")
    private String clientSecret;
    
    @Value("${jwt.signing-key:123}")
    private String jwtSigningKey;
    
    @Value("${jwt.accessTokenValidititySeconds:43200}") // 12 hours
    private int accessTokenValiditySeconds;
    
    @Value("${jwt.authorizedGrantTypes:password,authorization_code,refresh_token}")
    private String[] authorizedGrantTypes;
    
    @Value("${jwt.refreshTokenValiditySeconds:2592000}") // 30 days
    private int refreshTokenValiditySeconds;
    
    public OAuthConfiguration(AuthenticationManager authenticationManager, BCryptPasswordEncoder bcryptPassEncoder, UsersDP userService) {
        this.authenticationManager = authenticationManager;
        this.bcryptPassEncoder = bcryptPassEncoder;
        this.userService = userService;
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
               .withClient(clientId)
               .secret(bcryptPassEncoder.encode(clientSecret))
               .accessTokenValiditySeconds(accessTokenValiditySeconds)
               .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
               .authorizedGrantTypes(authorizedGrantTypes)
               .scopes("read", "write")
               .resourceIds("api");
    }
    
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userService)
                .authenticationManager(authenticationManager);
    }
    
    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        return converter;
    }
    */
}


