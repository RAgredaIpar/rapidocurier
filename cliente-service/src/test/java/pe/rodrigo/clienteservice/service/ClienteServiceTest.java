package pe.rodrigo.clienteservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pe.rodrigo.clienteservice.dto.request.ClienteCreateDto;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.dto.response.ReniecResponse;
import pe.rodrigo.clienteservice.entity.Cliente;
import pe.rodrigo.clienteservice.feignClient.ReniecClient;
import pe.rodrigo.clienteservice.repository.ClienteRepository;
import pe.rodrigo.common.exception.DuplicateResourceException;
import pe.rodrigo.common.security.UserContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReniecClient reniecClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        UserContext.setRole("ADMIN");
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void registrarCliente_EmailYaExiste_LanzaExcepcion() {
        ClienteCreateDto dto = new ClienteCreateDto();
        dto.setEmail("rodrigo@gmail.com");

        when(clienteRepository.existsByEmail("rodrigo@gmail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            clienteService.registrarCliente(dto);
        });

        verify(reniecClient, never()).getData(anyString(), anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void registrarCliente_Exitoso() throws Exception {
        ClienteCreateDto dto = new ClienteCreateDto();
        dto.setEmail("nuevo@gmail.com");
        dto.setDni("12345678");

        ReniecResponse reniecMock = new ReniecResponse();
        reniecMock.setFirstName("Juan");
        reniecMock.setFirstLastName("Perez");
        reniecMock.setSecondLastName("Gomez");

        Cliente clienteMapeado = new Cliente();
        Cliente clienteGuardado = new Cliente();
        ClienteResponseDto respuestaFinal = new ClienteResponseDto();

        when(clienteRepository.existsByEmail("nuevo@gmail.com")).thenReturn(false);
        when(reniecClient.getData(eq("12345678"), anyString())).thenReturn(reniecMock);
        when(modelMapper.map(dto, Cliente.class)).thenReturn(clienteMapeado);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(modelMapper.map(clienteGuardado, ClienteResponseDto.class)).thenReturn(respuestaFinal);

        ClienteResponseDto resultado = clienteService.registrarCliente(dto);

        assertNotNull(resultado);
        assertEquals("Juan", clienteMapeado.getNombres());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void eliminar_RolAdmin_Exitoso() {
        UUID idPrueba = UUID.randomUUID();
        UserContext.setRole("ADMIN");

        when(clienteRepository.existsById(idPrueba)).thenReturn(true);

        clienteService.eliminar(idPrueba);

        verify(clienteRepository, times(1)).deleteById(idPrueba);
    }

    @Test
    void eliminar_RolCliente_LanzaExcepcionSeguridad() {
        UUID idPrueba = UUID.randomUUID();
        UserContext.setRole("CLIENTE");

        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            clienteService.eliminar(idPrueba);
        });

        assertEquals("Acceso denegado: Solo el administrador puede eliminar clientes.", excepcion.getMessage());

        verify(clienteRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void listarTodos_RetornaListaVacia() {
        when(clienteRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        java.util.List<ClienteResponseDto> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }
}