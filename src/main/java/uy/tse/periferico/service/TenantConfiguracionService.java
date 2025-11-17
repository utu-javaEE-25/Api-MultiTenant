package uy.tse.periferico.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uy.tse.periferico.dto.TenantConfigUpdateDTO;
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

    @Transactional // Asegura que la operación sea atómica (o todo se guarda, o nada)
    public TenantConfiguracion updateConfiguration(TenantConfigUpdateDTO updateDTO) {
        // 1. Busca la única fila de configuración que existe (con ID 1)
        TenantConfiguracion config = repository.findById(1)
                .orElseThrow(() -> new RuntimeException("Configuración de tenant no encontrada."));

        // 2. Actualiza los campos de la entidad con los datos que vienen del DTO
        config.setTituloPrincipal(updateDTO.getTituloPrincipal());
        config.setColorPrimario(updateDTO.getColorPrimario());
        config.setColorFondo(updateDTO.getColorFondo());
        config.setLogoUrl(updateDTO.getLogoUrl()); // Guarda el string Base64 del logo

        // 3. Guarda la entidad actualizada en la base de datos y la retorna
        return repository.save(config);
    }
}