package pe.rodrigo.clienteservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import pe.rodrigo.clienteservice.dto.response.ReniecResponse;

@FeignClient(name = "reniec-client", url = "https://api.decolecta.com/v1/reniec/dni")
public interface ReniecClient {
    @GetMapping
    ReniecResponse getData(@RequestParam String numero, @RequestHeader("Authorization") String token);
}