package pe.rodrigo.trackingservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.rodrigo.trackingservice.client.PaqueteClient;
import pe.rodrigo.trackingservice.dto.request.TrackingRequestDto;
import pe.rodrigo.trackingservice.dto.response.TrackingResponseDto;
import pe.rodrigo.trackingservice.entity.EventoTracking;
import pe.rodrigo.trackingservice.repository.EventoTrackingRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingService {
    private final EventoTrackingRepository trackingRepository;
    private final PaqueteClient paqueteClient;
    private final ModelMapper modelMapper;

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "paqueteCB", fallbackMethod = "fallbackRegistrarEvento")
    public TrackingResponseDto registrarEvento(TrackingRequestDto dto) {
        paqueteClient.buscarPorId(dto.getPaqueteId());

        EventoTracking evento = modelMapper.map(dto, EventoTracking.class);
        EventoTracking guardado = trackingRepository.save(evento);

        return modelMapper.map(guardado, TrackingResponseDto.class);
    }

    public TrackingResponseDto fallbackRegistrarEvento(TrackingRequestDto dto, Throwable t) {
        throw new RuntimeException("El servicio de paquetes no responde. No se puede registrar el tracking en este momento.");
    }

    public List<TrackingResponseDto> obtenerHistorialPorPaquete(UUID paqueteId) {
        return trackingRepository.findByPaqueteIdOrderByCreatedAtDesc(paqueteId)
                .stream()
                .map(evento -> modelMapper.map(evento, TrackingResponseDto.class))
                .collect(Collectors.toList());
    }
}
