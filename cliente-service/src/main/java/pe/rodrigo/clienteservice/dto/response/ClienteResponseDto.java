package pe.rodrigo.clienteservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClienteResponseDto {
    private UUID id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String dni;
    private LocalDateTime createdAt;
    private List<DireccionResponseDto> direcciones;
}