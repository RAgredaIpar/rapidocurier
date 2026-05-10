package pe.rodrigo.paqueteservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.rodrigo.common.dto.ApiResponse;
import pe.rodrigo.paqueteservice.client.ClienteClient;
import pe.rodrigo.paqueteservice.client.TrackingClient;
import pe.rodrigo.paqueteservice.dto.request.PaqueteRequestDto;
import pe.rodrigo.paqueteservice.dto.request.TrackingRequestDto;
import pe.rodrigo.paqueteservice.dto.response.PaqueteResponseDto;
import pe.rodrigo.paqueteservice.entity.Categoria;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import pe.rodrigo.paqueteservice.entity.Paquete;
import pe.rodrigo.paqueteservice.repository.CategoriaRepository;
import pe.rodrigo.paqueteservice.repository.PaqueteRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ClienteClient clienteClient;
    private final TrackingClient trackingClient;
    private final ModelMapper modelMapper;

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "clienteCB")
    public PaqueteResponseDto crearPaquete(PaqueteRequestDto dto) {

        String nombreRemitente = obtenerNombreCliente(dto.getRemitenteId());
        String nombreDestinatario = obtenerNombreCliente(dto.getDestinatarioId());

        Paquete paquete = modelMapper.map(dto, Paquete.class);
        paquete.setPeso(dto.getPesoKg());
        paquete.setNombreRemitente(nombreRemitente);
        paquete.setNombreDestinatario(nombreDestinatario);

        paquete.setCodigoRastreo(generarCodigoRastreo());
        paquete.setEstado(EstadoPaquete.REGISTRADO);

        List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
        paquete.setCategorias(new HashSet<>(categorias));
        paquete.setCostoEnvio(calcularTarifa(paquete));

        Paquete guardado = paqueteRepository.save(paquete);

        try {
            trackingClient.registrarEventoInicial(new TrackingRequestDto(
                    guardado.getId(), guardado.getEstado().name(),
                    guardado.getSucursalOrigen(), "Ingresado a sistema"
            ));
        } catch (Exception e) {
            System.err.println("No se pudo iniciar el tracking: " + e.getMessage());
        }

        return modelMapper.map(guardado, PaqueteResponseDto.class);
    }

    public void actualizarEstado(UUID id, EstadoPaquete nuevoEstado) {
        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() -> new pe.rodrigo.common.exception.EntityNotFoundException("Paquete no encontrado"));

        validarTransicion(paquete.getEstado(), nuevoEstado);

        paquete.setEstado(nuevoEstado);
        paqueteRepository.save(paquete);
    }

    // --- METODOS DE BÚSQUEDA Y FILTRO ---

    public List<PaqueteResponseDto> buscarPorTexto(String texto) {
        return paqueteRepository.buscarPorTextoParcial(texto).stream()
                .map(p -> modelMapper.map(p, PaqueteResponseDto.class)).collect(Collectors.toList());
    }

    public List<PaqueteResponseDto> filtrarPorSucursalYEstado(String sucursal, EstadoPaquete estado) {
        return paqueteRepository.findBySucursalAndEstado(sucursal, estado).stream()
                .map(p -> modelMapper.map(p, PaqueteResponseDto.class)).collect(Collectors.toList());
    }

    // --- METODOS AUXILIARES ---

    private String generarCodigoRastreo() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder("RC-");
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }

    private String obtenerNombreCliente(UUID id) {
        try {
            ApiResponse<pe.rodrigo.paqueteservice.dto.response.ClienteInfoResponseDto> response = clienteClient.buscarPorId(id);

            if (response.getData() != null) {
                var cliente = response.getData();
                return (cliente.getNombres() + " " + cliente.getApellidos()).trim();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener datos del cliente: " + e.getMessage());
        }
        return "Cliente No Identificado";
    }

    private void validarTransicion(EstadoPaquete actual, EstadoPaquete nuevo) {
        if (actual == nuevo) return;
        boolean valida = switch (actual) {
            case REGISTRADO -> (nuevo == EstadoPaquete.EN_ALMACEN || nuevo == EstadoPaquete.CANCELADO);
            case EN_ALMACEN -> (nuevo == EstadoPaquete.EN_RUTA);
            case EN_RUTA -> (nuevo == EstadoPaquete.ENTREGADO);
            case ENTREGADO, CANCELADO -> false;
        };
        if (!valida) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transición inválida de " + actual + " a " + nuevo);
        }
    }

    private Double calcularTarifa(Paquete p) {
        double base = 10.0;
        double porPeso = p.getPeso() * 2.5;
        double porValor = p.getValorDeclarado() * 0.05;
        double recargos = p.getCategorias().stream().mapToDouble(Categoria::getRecargo).sum();
        return base + porPeso + porValor + recargos;
    }

    public List<PaqueteResponseDto> listarTodos() {
        return paqueteRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PaqueteResponseDto.class)).collect(Collectors.toList());
    }

    public PaqueteResponseDto buscarPorId(UUID id) {
        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() -> new pe.rodrigo.common.exception.EntityNotFoundException("Paquete no encontrado"));
        return modelMapper.map(paquete, PaqueteResponseDto.class);
    }

    public void eliminarPaquete(UUID id) {
        paqueteRepository.deleteById(id);
    }
}