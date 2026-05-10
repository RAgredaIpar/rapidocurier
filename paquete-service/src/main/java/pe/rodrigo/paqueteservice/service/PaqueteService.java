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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    public PaqueteResponseDto crearPaquete(PaqueteRequestDto dto) {
        Paquete paquete = modelMapper.map(dto, Paquete.class);

        // 1. Cargar categorías
        List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
        paquete.setCategorias(new HashSet<>(categorias));
        paquete.setEstado(EstadoPaquete.REGISTRADO);

        // 2. Lógica de cálculo de tarifa
        Double tarifaTotal = calcularTarifa(paquete);
        paquete.setCostoEnvio(tarifaTotal);

        Paquete guardado = paqueteRepository.save(paquete);
        return modelMapper.map(guardado, PaqueteResponseDto.class);
    }

    private Double calcularTarifa(Paquete p) {
        double base = 10.0; // Costo base de envío
        double porPeso = p.getPeso() * 2.5; // 2.5 por kilo
        double porValor = p.getValorDeclarado() * 0.05; // 5% de seguro

        // Sumar recargos de categorías (Lógica Many-to-Many)
        double recargos = p.getCategorias().stream()
                .mapToDouble(Categoria::getRecargo)
                .sum();

        return base + porPeso + porValor + recargos;
    }
}