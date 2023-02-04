package com.product.productreviewanalysis.service.Jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {



    public String extractUsername(String token);

    public String generateToken(String email);

    public boolean validateToken(String token , UserDetails userDetails);
}
