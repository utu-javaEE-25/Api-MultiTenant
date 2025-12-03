package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.tse.periferico.model.MobileDeviceToken;

import java.util.List;

public interface MobileDeviceTokenRepository extends JpaRepository<MobileDeviceToken, Long> {

    List<MobileDeviceToken> findByProfesionalId(Long profesionalId);

    void deleteByDeviceId(String deviceId);

    boolean existsByDeviceId(String deviceId);
}
