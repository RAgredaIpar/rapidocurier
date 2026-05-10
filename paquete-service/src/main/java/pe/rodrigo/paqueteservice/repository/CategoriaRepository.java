package pe.rodrigo.paqueteservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.rodrigo.paqueteservice.entity.Categoria;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {}