package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.tse.periferico.model.Notificacion;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByProfesionalIdOrderByFechaCreacionDesc(Long profesionalId);
}
