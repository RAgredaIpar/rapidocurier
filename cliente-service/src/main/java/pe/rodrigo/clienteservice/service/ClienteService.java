package pe.rodrigo.clienteservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.rodrigo.clienteservice.dto.request.ClienteCreateDto;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.dto.response.ReniecResponse;
import pe.rodrigo.clienteservice.entity.Cliente;
import pe.rodrigo.clienteservice.feignClient.ReniecClient;
import pe.rodrigo.clienteservice.repository.ClienteRepository;
import pe.rodrigo.common.exception.DuplicateResourceException;
import pe.rodrigo.common.exception.ExternalServiceException;

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

        Cliente guardado = clienteRepository.save(cliente);

        return modelMapper.map(guardado, ClienteResponseDto.class);
    }
}