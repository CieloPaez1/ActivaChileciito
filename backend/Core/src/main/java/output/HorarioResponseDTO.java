package output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioResponseDTO {
    private Long id;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
