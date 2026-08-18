package exception;

public class ExcepcionUsuario extends RuntimeException {
  public ExcepcionUsuario(String mensaje) {
    super(mensaje);
  }
}
