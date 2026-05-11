package pe.rodrigo.clienteservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class DireccionResponseDto {
    private UUID id;
    private String calle;
    private String ciudad;
    private String referencia;
}