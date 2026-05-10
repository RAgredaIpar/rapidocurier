package pe.rodrigo.paqueteservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pe.rodrigo.common.entity.BaseEntity;

@Entity
@Table(name = "categorias")
@Getter
@Setter
public class Categoria extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String nombre;

    private String descripcion;

    private Double recargo;
}