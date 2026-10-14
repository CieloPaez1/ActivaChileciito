package com.activachilecito.usuarios.config;

import model.Usuario;
import model.RolUsuario;
import output.UsuarioOutput;
import com.activachilecito.usuarios.adapter.output.JwtProviderAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProviderAdapter jwtProviderAdapter;
    private final UsuarioOutput usuarioOutput;

    public OAuth2AuthenticationSuccessHandler(JwtProviderAdapter jwtProviderAdapter, UsuarioOutput usuarioOutput) {
        this.jwtProviderAdapter = jwtProviderAdapter;
        this.usuarioOutput = usuarioOutput;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        if (firstName == null) firstName = name != null ? name : "GoogleUser";
        if (lastName == null) lastName = "";

        Optional<Usuario> userOpt = usuarioOutput.buscarPorEmail(email);
        Usuario user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new Usuario();
            user.setEmail(email);
            user.setNombre(firstName);
            user.setApellido(lastName);
            user.setRol(RolUsuario.DEPORTISTA);
            user.setTelefono("");
            user.setPassword(UUID.randomUUID().toString()); 
            user = usuarioOutput.guardar(user);
        }

        String token = jwtProviderAdapter.generarToken(user.getEmail());

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:4200/oauth2/redirect")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}