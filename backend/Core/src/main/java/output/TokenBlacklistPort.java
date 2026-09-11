package output;

public interface TokenBlacklistPort {
    void invalidarToken(String token);
    boolean esTokenInvalido(String token);
}
