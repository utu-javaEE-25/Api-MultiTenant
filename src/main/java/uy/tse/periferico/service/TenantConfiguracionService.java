package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uy.tse.periferico.model.TenantConfiguracion;
import uy.tse.periferico.repository.TenantConfiguracionRepository;

@Service
@RequiredArgsConstructor
public class TenantConfiguracionService {
    private final TenantConfiguracionRepository repository;

    public TenantConfiguracion getConfiguration() {
        // Asumimos que siempre hay una fila con ID 1
        return repository.findById(1)
                .orElseThrow(() -> new RuntimeException("Configuración de tenant no encontrada."));
    }
}