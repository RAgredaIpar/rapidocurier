package pe.rodrigo.paqueteservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.rodrigo.common.dto.ApiResponse;
import pe.rodrigo.paqueteservice.dto.request.TrackingRequestDto;

@FeignClient(name = "TRACKING-SERVICE")
public interface TrackingClient {

    @PostMapping("/api/v1/tracking")
    ApiResponse<Object> registrarEventoInicial(@RequestBody TrackingRequestDto dto);
}