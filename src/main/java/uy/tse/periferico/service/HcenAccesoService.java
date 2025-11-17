package uy.tse.periferico.service;

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

    public String solicitarAcceso(SolicitudAccesoRequestDTO dto) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(hcenApiUrl, dto, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("HCEN rechazó la solicitud: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo conectar con HCEN para solicitar acceso.", e);
        }
    }
}
