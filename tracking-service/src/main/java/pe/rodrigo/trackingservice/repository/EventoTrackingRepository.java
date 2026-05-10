package pe.rodrigo.trackingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.rodrigo.trackingservice.entity.EventoTracking;

import java.util.List;
import java.util.UUID;

public interface EventoTrackingRepository extends JpaRepository<EventoTracking, UUID> {
    List<EventoTracking> findByPaqueteIdOrderByCreatedAtDesc(UUID paqueteId);
}
