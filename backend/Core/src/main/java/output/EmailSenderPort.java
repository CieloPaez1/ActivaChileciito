package output;

public interface EmailSenderPort {
    void enviarEmailRecuperacion(String email, String token);
}
