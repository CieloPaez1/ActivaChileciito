package usecase;

import input.IniciarSesionInput;
import output.JwtProviderPort;
import output.ValidarCredencialesPort;

public class IniciarSesionUseCase implements IniciarSesionInput {

    private final ValidarCredencialesPort validarCredencialesPort;
    private final JwtProviderPort jwtProviderPort;

    public IniciarSesionUseCase(ValidarCredencialesPort validarCredencialesPort, JwtProviderPort jwtProviderPort) {
        this.validarCredencialesPort = validarCredencialesPort;
        this.jwtProviderPort = jwtProviderPort;
    }

    @Override
    public String iniciarSesion(String email, String password) {
        validarCredencialesPort.validar(email, password);
        return jwtProviderPort.generarToken(email);
    }
}
