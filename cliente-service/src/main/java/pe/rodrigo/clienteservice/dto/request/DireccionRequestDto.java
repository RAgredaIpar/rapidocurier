package pe.rodrigo.clienteservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DireccionRequestDto {
    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 5, message = "La calle debe tener al menos 5 caracteres")
    private String calle;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    private String referencia;
}