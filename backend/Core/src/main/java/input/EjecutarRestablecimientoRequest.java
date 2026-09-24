package input;

import lombok.Data;

@Data
public class EjecutarRestablecimientoRequest {
    private String token;
    private String nuevaClave;
}
