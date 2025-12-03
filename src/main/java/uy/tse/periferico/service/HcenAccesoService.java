package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uy.tse.periferico.dto.SolicitudAccesoRequestDTO;

@Service
public class HcenAccesoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.api.url.solicitud-acceso}")
    private String hcenApiUrl;

    @Autowired
    private NotificationService notificationService;

    public String solicitarAcceso(SolicitudAccesoRequestDTO dto, String profesionalUsername) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(hcenApiUrl, dto, String.class);

            // Notificación al profesional solicitante
            notificationService.crearYEnviarNotificacion(
                    profesionalUsername,
                    "Solicitud de acceso enviada",
                    "Se envió una solicitud de acceso para la historia clínica del paciente: "
                            + dto.getCedulaPaciente(),
                    "SOLICITUD_ACCESO",
                    null);

            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("HCEN rechazó la solicitud: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo conectar con HCEN para solicitar acceso.", e);
        }
    }
}
