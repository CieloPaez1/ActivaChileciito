package input;

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
public class RegistrarCanchaRequest {
    private Long complejoId;
    private String nombre;
    private String deporte;
    private String tipoSuelo;
    private BigDecimal precioTurno;
    private List<HorarioRequest> horarios;
}
