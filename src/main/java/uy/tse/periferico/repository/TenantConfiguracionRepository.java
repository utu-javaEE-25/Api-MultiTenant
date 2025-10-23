package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.tse.periferico.model.TenantConfiguracion;

public interface TenantConfiguracionRepository extends JpaRepository<TenantConfiguracion, Integer> {}
