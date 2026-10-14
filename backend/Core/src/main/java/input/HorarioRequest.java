package input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRequest {
    private Integer diaSemana; // 1 = Lunes, 7 = Domingo
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
