package pe.rodrigo.clienteservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClienteCreateDto {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "El teléfono debe tener entre 7 y 9 dígitos")
    private String telefono;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotEmpty(message = "El cliente debe registrar al menos una dirección")
    @Valid
    private List<DireccionRequestDto> direcciones;
}