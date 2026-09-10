package output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerfilResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private boolean activo;
}
