package pe.rodrigo.paqueteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.common.dto.ApiResponse;
import pe.rodrigo.paqueteservice.dto.request.PaqueteRequestDto;
import pe.rodrigo.paqueteservice.dto.response.PaqueteResponseDto;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import pe.rodrigo.paqueteservice.service.PaqueteService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/paquetes")
@RequiredArgsConstructor
public class PaqueteController {

    private final PaqueteService paqueteService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaqueteResponseDto>> registrar(@Valid @RequestBody PaqueteRequestDto dto) {
        PaqueteResponseDto data = paqueteService.crearPaquete(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Paquete registrado y tarifa calculada", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaqueteResponseDto>>> listar() {
        List<PaqueteResponseDto> data = paqueteService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Lista de paquetes obtenida", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaqueteResponseDto>> buscar(@PathVariable UUID id) {
        PaqueteResponseDto data = paqueteService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Paquete encontrado", data));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Void>> actualizarEstado(@PathVariable UUID id, @RequestParam EstadoPaquete estado) {
        paqueteService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.success("Estado del paquete actualizado", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable UUID id) {
        paqueteService.eliminarPaquete(id);
        return ResponseEntity.ok(ApiResponse.success("Paquete eliminado correctamente", null));
    }
}