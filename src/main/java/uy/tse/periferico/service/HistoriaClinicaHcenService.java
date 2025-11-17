package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.tse.periferico.dto.DocumentoMetadataHcenDTO;

import java.util.List;

@Service
public class HistoriaClinicaHcenService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.api.url.historia-clinica}")
    private String hcenApiUrl; // La URL base, ej: http://.../api/historia-clinica

    public List<DocumentoMetadataHcenDTO> obtenerHistoriaPorCedula(String cedula) {
        try {
            String url = hcenApiUrl + "/" + cedula;
            System.out.println("Consultando a HCEN en: " + url);

            // Hacemos la llamada GET y esperamos una lista de objetos DTO
            ResponseEntity<List<DocumentoMetadataHcenDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DocumentoMetadataHcenDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al consultar la historia clínica de HCEN: " + e.getMessage());
            // En un caso real, podríamos lanzar una excepción más específica
            throw new RuntimeException("No se pudo obtener la historia clínica desde HCEN.", e);
        }
    }
}