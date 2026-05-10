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
    private Double peso;

    @PositiveOrZero(message = "El valor declarado no puede ser negativo")
    private Double valorDeclarado;

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;

    private Set<UUID> categoriaIds;
}