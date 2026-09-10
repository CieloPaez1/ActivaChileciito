package usecase;

import exception.ExcepcionTokenInvalido;
import lombok.RequiredArgsConstructor;
import output.TokenBlacklistPort;

@RequiredArgsConstructor
public class CerrarSesionUseCase {

    private final TokenBlacklistPort tokenBlacklistPort;

    public void cerrarSesion(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ExcepcionTokenInvalido("El token provisto está malformado o es nulo.");
        }
        
        String jwt = token.substring(7);
        if (tokenBlacklistPort.esTokenInvalido(jwt)) {
            throw new ExcepcionTokenInvalido("El token ya está invalidado o expirado.");
        }
        
        tokenBlacklistPort.invalidarToken(jwt);
    }
}
