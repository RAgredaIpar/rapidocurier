package pe.rodrigo.paqueteservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class PaqueteRequestDto {
    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @Positive(message = "El peso debe ser mayor a 0")
    private Double pesoKg;

    @PositiveOrZero(message = "El valor declarado no puede ser negativo")
    private Double valorDeclarado;

    @NotBlank(message = "La sucursal de origen es obligatoria")
    private String sucursalOrigen;

    @NotBlank(message = "La sucursal de destino es obligatoria")
    private String sucursalDestino;

    @NotNull(message = "El ID del remitente es obligatorio")
    private UUID remitenteId;

    @NotNull(message = "El ID del destinatario es obligatorio")
    private UUID destinatarioId;

    private Set<UUID> categoriaIds;
}