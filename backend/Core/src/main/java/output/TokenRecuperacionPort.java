package output;

public interface TokenRecuperacionPort {
    String generarToken(Long usuarioId);
    boolean esTokenValido(String token);
    Long obtenerUsuarioIdPorToken(String token);
    void invalidarToken(String token);
}
