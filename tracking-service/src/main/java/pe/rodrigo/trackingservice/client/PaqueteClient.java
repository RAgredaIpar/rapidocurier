package pe.rodrigo.trackingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.rodrigo.common.dto.ApiResponse;

import java.util.UUID;

@FeignClient(name = "PAQUETE-SERVICE")
public interface PaqueteClient {
    @GetMapping("/api/v1/paquetes/{id}")
    ApiResponse<Object> buscarPorId(@PathVariable("id") UUID id);
}
