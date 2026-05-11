package pe.rodrigo.clienteservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "direcciones")
@Getter
@Setter
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String calle;
    private String ciudad;
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}