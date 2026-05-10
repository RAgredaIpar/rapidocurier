package pe.rodrigo.trackingservice.dto.response;

import lombok.Data;
import pe.rodrigo.trackingservice.entity.EstadoTracking;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TrackingResponseDto {
    private UUID id;
    private UUID paqueteId;
    private EstadoTracking estado;
    private String ubicacionActual;
    private String observacion;
    private LocalDateTime createdAt;
}
