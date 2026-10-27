package output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplejoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String direccion;
    private String telefono;
    private List<String> prestaciones;
}
