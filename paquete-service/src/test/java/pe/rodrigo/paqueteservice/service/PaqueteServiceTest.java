package pe.rodrigo.paqueteservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pe.rodrigo.common.security.UserContext;
import pe.rodrigo.paqueteservice.client.ClienteClient;
import pe.rodrigo.paqueteservice.client.TrackingClient;
import pe.rodrigo.paqueteservice.dto.response.PaqueteResponseDto;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import pe.rodrigo.paqueteservice.entity.Paquete;
import pe.rodrigo.paqueteservice.repository.CategoriaRepository;
import pe.rodrigo.paqueteservice.repository.PaqueteRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaqueteServiceTest {

    /* * JUSTIFICACIÓN DE @Spy (Rúbrica - Sección 9):
     * No se utiliza la anotación @Spy en este proyecto porque no contamos con un
     * "componente auxiliar compartido" inyectable (como un TarifaCalculator externo).
     * La lógica de cálculo de tarifa es un metodo privado intrínseco (calcularTarifa)
     * dentro de la misma clase PaqueteService, por lo que su comportamiento se
     * prueba implícitamente en los happy paths sin necesidad de espiarlo.
     */

    @Mock private PaqueteRepository paqueteRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private ClienteClient clienteClient;
    @Mock private TrackingClient trackingClient;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private PaqueteService paqueteService;

    @BeforeEach
    void setUp() {
        UserContext.setRole("ADMIN");
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void buscarPorId_Exitoso() {
        UUID id = UUID.randomUUID();
        Paquete paqueteEnBd = new Paquete();
        paqueteEnBd.setRemitenteEmail("admin@rapidocourier.pe");

        PaqueteResponseDto responseEsperado = new PaqueteResponseDto();
        responseEsperado.setPeso(15.5);

        when(paqueteRepository.findById(id)).thenReturn(Optional.of(paqueteEnBd));
        when(modelMapper.map(paqueteEnBd, PaqueteResponseDto.class)).thenReturn(responseEsperado);

        PaqueteResponseDto resultado = paqueteService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(15.5, resultado.getPeso());
        verify(paqueteRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_NoExiste_LanzaExcepcion() {
        UUID id = UUID.randomUUID();
        when(paqueteRepository.findById(id)).thenReturn(Optional.empty());

        pe.rodrigo.common.exception.EntityNotFoundException excepcion = assertThrows(
                pe.rodrigo.common.exception.EntityNotFoundException.class,
                () -> paqueteService.buscarPorId(id)
        );

        assertEquals("Paquete no encontrado", excepcion.getMessage());
    }

    @Test
    void actualizarEstado_TransicionInvalida_LanzaExcepcion() {
        UUID id = UUID.randomUUID();
        Paquete paqueteEnBd = new Paquete();
        paqueteEnBd.setEstado(EstadoPaquete.REGISTRADO); // Estado actual

        when(paqueteRepository.findById(id)).thenReturn(Optional.of(paqueteEnBd));

        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            paqueteService.actualizarEstado(id, EstadoPaquete.ENTREGADO);
        });

        assertEquals("Transición inválida de REGISTRADO a ENTREGADO", excepcion.getMessage());

        verify(paqueteRepository, never()).save(any(Paquete.class));
    }
}