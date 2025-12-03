package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.tse.periferico.model.PreferenciasNotificacion;

import java.util.Optional;

public interface PreferenciasNotificacionRepository
        extends JpaRepository<PreferenciasNotificacion, Long> {

    Optional<PreferenciasNotificacion> findByProfesionalId(Long profesionalId);
}
