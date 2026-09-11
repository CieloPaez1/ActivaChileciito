package exception;

public class ExcepcionTokenInvalido extends RuntimeException {
    public ExcepcionTokenInvalido(String mensaje) {
        super(mensaje);
    }
}
