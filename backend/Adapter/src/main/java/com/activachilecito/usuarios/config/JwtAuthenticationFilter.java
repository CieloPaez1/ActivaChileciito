package com.activachilecito.usuarios.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            
            // Simulación de la extracción del JWT
            Long extractedId = simularExtraccionId(jwt);
            String extractedEmail = "usuario@test.com";
            String extractedRole = "CLIENTE";

            if (extractedId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = new CustomUserDetails(extractedId, extractedEmail, "", extractedRole);
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Long simularExtraccionId(String jwt) {
        try {
            if (jwt.contains("id-")) {
                String idStr = jwt.substring(jwt.indexOf("id-") + 3);
                if(idStr.contains("-")) idStr = idStr.substring(0, idStr.indexOf("-"));
                return Long.parseLong(idStr);
            }
        } catch (Exception ignored) {}
        return 1L;
    }
}
