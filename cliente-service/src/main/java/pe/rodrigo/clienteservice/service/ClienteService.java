package pe.rodrigo.clienteservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.rodrigo.clienteservice.dto.request.ClienteCreateDto;
import pe.rodrigo.clienteservice.dto.request.ClienteUpdateDto;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.dto.response.ReniecResponse;
import pe.rodrigo.clienteservice.entity.Cliente;
import pe.rodrigo.clienteservice.feignClient.ReniecClient;
import pe.rodrigo.clienteservice.repository.ClienteRepository;
import pe.rodrigo.common.exception.DuplicateResourceException;
import pe.rodrigo.common.exception.EntityNotFoundException;
import pe.rodrigo.common.exception.ExternalServiceException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ReniecClient reniecClient;
    private final ModelMapper modelMapper;

    @Value("${reniec.api.token}")
    private String apiToken;

    public ClienteResponseDto registrarCliente(ClienteCreateDto dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        ReniecResponse reniecData;
        try {
            reniecData = reniecClient.getData(dto.getDni(), "Bearer " + apiToken);
            if (reniecData == null || reniecData.getFirstName() == null) {
                throw new Exception("Datos nulos desde RENIEC");
            }
        } catch (Exception e) {
            throw new ExternalServiceException("Fallo al comunicarse con RENIEC para el DNI: " + dto.getDni());
        }

        Cliente cliente = modelMapper.map(dto, Cliente.class);

        cliente.setNombres(reniecData.getFirstName());
        cliente.setApellidos(reniecData.getFirstLastName() + " " + reniecData.getSecondLastName());

        if (cliente.getDirecciones() != null) {
            cliente.getDirecciones().forEach(direccion -> direccion.setCliente(cliente));
        }

        Cliente guardado = clienteRepository.save(cliente);

        return modelMapper.map(guardado, ClienteResponseDto.class);
    }

    public ClienteResponseDto actualizar(UUID id, ClienteUpdateDto dto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar, cliente no encontrado"));

        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setTelefono(dto.getTelefono());

        if (dto.getDirecciones() != null) {
            clienteExistente.getDirecciones().clear();

            List<pe.rodrigo.clienteservice.entity.Direccion> nuevasDirecciones = dto.getDirecciones().stream()
                    .map(d -> {
                        pe.rodrigo.clienteservice.entity.Direccion dir = modelMapper.map(d, pe.rodrigo.clienteservice.entity.Direccion.class);
                        dir.setCliente(clienteExistente);
                        return dir;
                    }).toList();

            clienteExistente.getDirecciones().addAll(nuevasDirecciones);
        }

        Cliente actualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(actualizado, ClienteResponseDto.class);
    }

    public List<ClienteResponseDto> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDto.class))
                .collect(Collectors.toList());
    }

    public ClienteResponseDto buscarPorId(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        return modelMapper.map(cliente, ClienteResponseDto.class);
    }


    public void eliminar(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar, cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
}