package pe.rodrigo.clienteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.clienteservice.dto.request.ClienteCreateDto;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.service.ClienteService;
import pe.rodrigo.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDto>> registrar(@Valid @RequestBody ClienteCreateDto dto) {
        ClienteResponseDto data = clienteService.registrarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente registrado exitosamente", data));
    }
}