package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private String hcenApiUrl;

    @Autowired
    private NotificationService notificationService;

    public List<DocumentoMetadataHcenDTO> obtenerHistoriaPorCedula(String cedula, String profesionalUsername) {
        try {
            String url = hcenApiUrl + "/" + cedula;
            System.out.println("Consultando a HCEN en: " + url);

            ResponseEntity<List<DocumentoMetadataHcenDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DocumentoMetadataHcenDTO>>() {
                    });

            // Notificación por acceso a historia clínica
            notificationService.crearYEnviarNotificacion(
                    profesionalUsername,
                    "Acceso a Historia Clínica",
                    "Consultaste la historia clínica del paciente con cédula: " + cedula,
                    "ACCESO_HC",
                    null);

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al consultar la historia clínica de HCEN: " + e.getMessage());
            throw new RuntimeException("No se pudo obtener la historia clínica desde HCEN.", e);
        }
    }
}
