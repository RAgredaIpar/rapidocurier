package pe.rodrigo.paqueteservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.rodrigo.common.entity.BaseEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "paquetes")
@Getter
@Setter
public class Paquete extends BaseEntity {

    private String descripcion;
    private Double peso;
    private Double valorDeclarado;
    private Double costoEnvio;

    @Column(nullable = false)
    private UUID clienteId;

    @Enumerated(EnumType.STRING)
    private EstadoPaquete estado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "paquetes_categorias",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
}