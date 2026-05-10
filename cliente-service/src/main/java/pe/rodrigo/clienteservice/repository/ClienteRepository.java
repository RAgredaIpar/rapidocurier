package pe.rodrigo.clienteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.rodrigo.clienteservice.entity.Cliente;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByEmail(String email);
}