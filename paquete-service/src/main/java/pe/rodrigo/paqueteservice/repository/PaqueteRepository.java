package pe.rodrigo.paqueteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.rodrigo.paqueteservice.entity.EstadoPaquete;
import pe.rodrigo.paqueteservice.entity.Paquete;

import java.util.List;
import java.util.UUID;

public interface PaqueteRepository extends JpaRepository<Paquete, UUID> {

    @Query("SELECT p FROM Paquete p WHERE (p.sucursalOrigen = :sucursal OR p.sucursalDestino = :sucursal) AND p.estado = :estado")
    List<Paquete> findBySucursalAndEstado(@Param("sucursal") String sucursal, @Param("estado") EstadoPaquete estado);

    @Query("SELECT p FROM Paquete p WHERE LOWER(p.codigoRastreo) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "OR LOWER(p.nombreRemitente) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "OR LOWER(p.nombreDestinatario) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Paquete> buscarPorTextoParcial(@Param("texto") String texto);
}