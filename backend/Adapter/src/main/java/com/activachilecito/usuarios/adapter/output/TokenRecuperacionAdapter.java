package com.activachilecito.usuarios.adapter.output;

import com.activachilecito.usuarios.entity.data.TokenRecuperacionData;
import com.activachilecito.usuarios.persistence.TokenRecuperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import output.TokenRecuperacionPort;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenRecuperacionAdapter implements TokenRecuperacionPort {

    private final TokenRecuperacionRepository repository;

    @Override
    @Transactional
    public String generarToken(Long usuarioId) {
        String token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TokenRecuperacionData data = new TokenRecuperacionData(
                usuarioId,
                token,
                LocalDateTime.now().plusHours(2)
        );
        repository.save(data);
        return token;
    }

    @Override
    public boolean esTokenValido(String token) {
        Optional<TokenRecuperacionData> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return false;
        }
        return tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    @Override
    public Long obtenerUsuarioIdPorToken(String token) {
        return repository.findByToken(token)
                .map(TokenRecuperacionData::getUsuarioId)
                .orElse(null);
    }

    @Override
    @Transactional
    public void invalidarToken(String token) {
        repository.deleteByToken(token);
    }
}
