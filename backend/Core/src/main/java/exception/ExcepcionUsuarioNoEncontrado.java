package exception;

public class ExcepcionUsuarioNoEncontrado extends RuntimeException {
    public ExcepcionUsuarioNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
