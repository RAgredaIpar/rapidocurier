package pe.rodrigo.clienteservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.rodrigo.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 9)
    private String telefono;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();
}