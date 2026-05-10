package pe.rodrigo.trackingservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.rodrigo.trackingservice.entity.EstadoTracking;

import java.util.UUID;

@Data
public class TrackingRequestDto {
    @NotNull(message = "El ID del paquete es obligatorio")
    private UUID paqueteId;

    @NotNull(message = "El estado es obligatorio")
    private EstadoTracking estado;

    @NotNull(message = "La ubicación actual es obligatoria")
    private String ubicacionActual;

    private String observacion;
}
