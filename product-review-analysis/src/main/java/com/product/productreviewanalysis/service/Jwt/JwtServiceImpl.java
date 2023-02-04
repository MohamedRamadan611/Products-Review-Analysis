package com.product.productreviewanalysis.service.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

    private final static String SECRET = "2A472D4B614E645267556B58703273357638792F423F4528482B4D6251655368";

    private <T> T extractClaim(String token , Function<Claims ,T> claimsFunction)
    {
        final Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    public String extractUsername(String token)
    {
        String email = extractClaim(token, Claims::getSubject);
        //System.out.println("email is : " + email);
        return email;
    }

    private Date extractExpiration (String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    private boolean isTokenValid (String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username)
    {
        Map<String , Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public boolean validateToken(String token , UserDetails userDetails)
    {
        final String email = extractUsername(token);
        return (email.equalsIgnoreCase(userDetails.getUsername()) && !isTokenValid(token));
    }
    private String createToken(Map<String , Object> claims , String username)
    {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignKey() {
        byte [] keys = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keys);
    }
}
