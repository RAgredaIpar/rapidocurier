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

    @Column(unique = true, nullable = false, updatable = false)
    private String codigoRastreo;

    private String descripcion;
    private Double peso;
    private Double valorDeclarado;
    private Double costoEnvio;

    private String sucursalOrigen;
    private String sucursalDestino;

    @Column(nullable = false)
    private UUID remitenteId;
    private String nombreRemitente;
    private String remitenteEmail;

    @Column(nullable = false)
    private UUID destinatarioId;
    private String nombreDestinatario;
    private String destinatarioEmail;

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