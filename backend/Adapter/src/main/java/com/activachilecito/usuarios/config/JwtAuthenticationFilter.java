package com.activachilecito.usuarios.config;

import com.activachilecito.usuarios.adapter.output.JwtProviderAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProviderAdapter jwtProviderAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            
            try {
                String email = jwtProviderAdapter.extraerEmail(jwt);
                
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtProviderAdapter.esTokenValido(jwt, email)) {
                        Long id = jwtProviderAdapter.extraerId(jwt);
                        String rol = jwtProviderAdapter.extraerRol(jwt);

                        CustomUserDetails userDetails = new CustomUserDetails(id, email, "", rol);

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Token invÃ¡lido o expirado
            }
        }
        filterChain.doFilter(request, response);
    }
}
