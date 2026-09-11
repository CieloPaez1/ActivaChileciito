package input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.RolUsuario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModificarUsuarioAdminRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private RolUsuario rol;
    private Boolean activo;
}
