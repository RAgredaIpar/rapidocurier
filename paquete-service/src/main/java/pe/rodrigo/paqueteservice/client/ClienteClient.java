package pe.rodrigo.paqueteservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.rodrigo.common.dto.ApiResponse;
import java.util.UUID;

@FeignClient(name = "CLIENTE-SERVICE")
public interface ClienteClient {

    @GetMapping("/api/v1/clientes/{id}")
    ApiResponse<Object> buscarPorId(@PathVariable("id") UUID id);
}