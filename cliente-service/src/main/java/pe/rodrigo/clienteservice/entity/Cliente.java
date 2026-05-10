package pe.rodrigo.clienteservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.rodrigo.common.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente extends BaseEntity {

    private String nombres;
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String email;

    private String telefono;

    @Column(unique = true, nullable = false)
    private String dni;
}