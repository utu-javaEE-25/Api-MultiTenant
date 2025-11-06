package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.tse.periferico.model.AdminTenant;
import java.util.Optional;

public interface AdminTenantRepository extends JpaRepository<AdminTenant, Long> {
   
    Optional<AdminTenant> findByNombreUsuario(String nombreUsuario);
}