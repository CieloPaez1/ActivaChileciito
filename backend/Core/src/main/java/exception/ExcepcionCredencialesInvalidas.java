package exception;

public class ExcepcionCredencialesInvalidas extends RuntimeException {
    public ExcepcionCredencialesInvalidas(String mensaje) {
        super(mensaje);
    }
}
