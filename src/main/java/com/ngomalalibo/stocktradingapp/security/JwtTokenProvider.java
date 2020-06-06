package com.ngomalalibo.stocktradingapp.security;

import com.ngomalalibo.stocktradingapp.dataprovider.UsersDP;
import com.ngomalalibo.stocktradingapp.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider
{
    @Value("${security.jwt.token.secret-key:secret}")
    private static String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private static long validityInMilliseconds = 36000000; // 10hrs
    
    @Autowired
    private UsersDP userDetailsService;
    
    @PostConstruct
    protected void init()
    {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    public String createToken(User user)
    {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("password", user.getPassword());
        claims.put("roles", user.getRole());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()//
                   .setClaims(claims)//
                   .setIssuedAt(now)//
                   .setExpiration(validity)//
                   .signWith(SignatureAlgorithm.HS256, secretKey)//
                   .compact();
    }
    
    public Authentication getAuthentication(String token)
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public static String getUsername(String token)
    {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    /*public String resolveToken(HttpServletRequest req)
    {
        String token = req.getParameter("token");
        if (token != null)
        {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }*/
    
    public boolean validateToken(String token) throws JwtException
    {
        
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        // remove token expiry.
        /*if (claims.getBody().getExpiration().before(new Date()))
        {
            return false;
        }*/
        return true;
    }
}