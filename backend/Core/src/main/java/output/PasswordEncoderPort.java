package output;

public interface PasswordEncoderPort {
    String encriptar(String password);
    boolean coincide(String passwordPura, String passwordEncriptada);
}
