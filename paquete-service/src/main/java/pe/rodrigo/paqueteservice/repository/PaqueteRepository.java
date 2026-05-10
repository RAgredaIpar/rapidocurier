package pe.rodrigo.paqueteservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.rodrigo.paqueteservice.entity.Paquete;
import java.util.UUID;

public interface PaqueteRepository extends JpaRepository<Paquete, UUID> {}