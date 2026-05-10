package pe.rodrigo.paqueteservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.rodrigo.paqueteservice.dto.request.PaqueteRequestDto;
import pe.rodrigo.paqueteservice.dto.response.PaqueteResponseDto;
import pe.rodrigo.paqueteservice.entity.Categoria;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import pe.rodrigo.paqueteservice.entity.Paquete;
import pe.rodrigo.paqueteservice.repository.CategoriaRepository;
import pe.rodrigo.paqueteservice.repository.PaqueteRepository;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import pe.rodrigo.paqueteservice.client.ClienteClient;

@Service
@RequiredArgsConstructor
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ClienteClient clienteClient;
    private final ModelMapper modelMapper;

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "clienteCB", fallbackMethod = "fallbackCrearPaquete")
    public PaqueteResponseDto crearPaquete(PaqueteRequestDto dto) {

        clienteClient.buscarPorId(dto.getClienteId());

        Paquete paquete = modelMapper.map(dto, Paquete.class);
        List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
        paquete.setCategorias(new HashSet<>(categorias));
        paquete.setEstado(EstadoPaquete.REGISTRADO);

        paquete.setCostoEnvio(calcularTarifa(paquete));

        Paquete guardado = paqueteRepository.save(paquete);
        return modelMapper.map(guardado, PaqueteResponseDto.class);
    }

    private Double calcularTarifa(Paquete p) {
        double base = 10.0;
        double porPeso = p.getPeso() * 2.5;
        double porValor = p.getValorDeclarado() * 0.05;
        double recargos = p.getCategorias().stream()
                .mapToDouble(Categoria::getRecargo)
                .sum();
        return base + porPeso + porValor + recargos;
    }

    public PaqueteResponseDto fallbackCrearPaquete(PaqueteRequestDto dto, Throwable e) {
        throw new RuntimeException("El servicio de clientes no responde. Intente más tarde.");
    }

    public List<PaqueteResponseDto> listarTodos() {
        return paqueteRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PaqueteResponseDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    public PaqueteResponseDto buscarPorId(UUID id) {
        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() -> new pe.rodrigo.common.exception.EntityNotFoundException("Paquete no encontrado"));
        return modelMapper.map(paquete, PaqueteResponseDto.class);
    }

    public void eliminarPaquete(UUID id) {
        if (!paqueteRepository.existsById(id)) {
            throw new pe.rodrigo.common.exception.EntityNotFoundException("No se puede eliminar, paquete no existe");
        }
        paqueteRepository.deleteById(id);
    }
}