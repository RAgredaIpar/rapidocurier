package pe.rodrigo.clienteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.clienteservice.dto.request.ClienteCreateDto;
import pe.rodrigo.clienteservice.dto.request.ClienteUpdateDto;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.service.ClienteService;
import pe.rodrigo.common.dto.ApiResponse;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponseDto>>> listar() {
        List<ClienteResponseDto> data = clienteService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Lista de clientes obtenida", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> buscar(@PathVariable UUID id) {
        ClienteResponseDto data = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente encontrado", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDto>> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteUpdateDto dto) {
        ClienteResponseDto data = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizado correctamente", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable UUID id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente eliminado correctamente", null));
    }
}