package com.activachilecito.usuarios.adapter.output;

import com.activachilecito.usuarios.config.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import output.JwtProviderPort;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProviderAdapter implements JwtProviderPort {

    private final UserDetailsService userDetailsService;
    
    // DeberÃ­a venir de properties, pero hardcodeamos uno seguro por simplicidad.
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public String generarToken(String email) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("rol", userDetails.getRole());
        claims.put("activo", userDetails.isEnabled());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key)
                .compact();
    }
    
    public String extraerEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
    
    public String extraerRol(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("rol");
    }
    
    public boolean extraerActivo(String token) {
        Object obj = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("activo");
        return obj != null && (Boolean) obj;
    }
    
    public Long extraerId(String token) {
        return ((Number) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("id")).longValue();
    }
    
    public boolean esTokenValido(String token, String username) {
        final String email = extraerEmail(token);
        return (email.equals(username) && !esTokenExpirado(token));
    }
    
    private boolean esTokenExpirado(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
