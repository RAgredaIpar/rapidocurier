package pe.rodrigo.paqueteservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingRequestDto {
    private UUID paqueteId;
    private String estado;
    private String ubicacionActual;
    private String observacion;
}