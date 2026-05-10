package pe.rodrigo.trackingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.common.dto.ApiResponse;
import pe.rodrigo.trackingservice.dto.request.TrackingRequestDto;
import pe.rodrigo.trackingservice.dto.response.TrackingResponseDto;
import pe.rodrigo.trackingservice.service.TrackingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrackingResponseDto>> registrarEvento(@Valid @RequestBody TrackingRequestDto dto) {
        TrackingResponseDto data = trackingService.registrarEvento(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Evento de tracking registrado correctamente", data));
    }

    @GetMapping("/paquete/{paqueteId}")
    public ResponseEntity<ApiResponse<List<TrackingResponseDto>>> obtenerHistorial(@PathVariable UUID paqueteId) {
        List<TrackingResponseDto> data = trackingService.obtenerHistorialPorPaquete(paqueteId);
        return ResponseEntity.ok(ApiResponse.success("Historial de tracking obtenido", data));
    }
}
