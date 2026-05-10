package pe.rodrigo.paqueteservice.dto.response;

import lombok.Data;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import java.util.Set;
import java.util.UUID;

@Data
public class PaqueteResponseDto {
    private UUID id;
    private String descripcion;
    private Double peso;
    private Double valorDeclarado;
    private Double costoEnvio;
    private UUID clienteId;
    private EstadoPaquete estado;
    private Set<CategoriaResponseDto> categorias;
}