package output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanchaResponseDTO {
    private Long id;
    private Long complejoId;
    private String nombre;
    private String deporte;
    private String tipoSuelo;
    private BigDecimal precioTurno;
    private Boolean activa;
    private List<HorarioResponseDTO> horarios;
}
