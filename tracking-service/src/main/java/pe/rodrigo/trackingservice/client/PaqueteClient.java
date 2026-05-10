package pe.rodrigo.trackingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.common.dto.ApiResponse;

import java.util.UUID;

@FeignClient(name = "PAQUETE-SERVICE")
public interface PaqueteClient {
    @GetMapping("/api/v1/paquetes/{id}")
    ApiResponse<Object> buscarPorId(@PathVariable("id") UUID id);

    @PutMapping("/api/v1/paquetes/{id}/estado")
    void actualizarEstadoPaquete(@PathVariable("id") UUID id, @RequestParam("estado") String estado);
}
