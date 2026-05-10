package pe.rodrigo.paqueteservice.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoriaResponseDto {
    private UUID id;
    private String nombre;
    private Double recargo;
}