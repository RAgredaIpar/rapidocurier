package pe.rodrigo.trackingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.rodrigo.common.entity.BaseEntity;
import java.util.UUID;

@Entity
@Table(name = "eventos_tracking")
@Getter
@Setter
public class EventoTracking extends BaseEntity {

    @Column(nullable = false)
    private UUID paqueteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTracking estado;

    private String ubicacionActual;

    private String observacion;
}