package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.tse.periferico.dto.DocumentoMetadataMensajeDTO;

@Service
public class HcenRestNotifierService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Inyectamos la URL desde application.properties
    @Value("${central.api.url.metadata-receiver}")
    private String hcenEndpointUrl;

    public void notificarNuevoDocumento(DocumentoMetadataMensajeDTO dto) {
        System.out.println("Intentando notificar a HCEN sobre el documento: " + dto.getIdExternaDoc());
        try {
            // Realiza la llamada HTTP POST. Spring convierte 'dto' a JSON automáticamente.
            restTemplate.postForEntity(hcenEndpointUrl, dto, String.class);
            System.out.println("Notificación para el documento " + dto.getIdExternaDoc() + " enviada a HCEN con éxito.");
        } catch (Exception e) {
            System.err.println("¡FALLO LA NOTIFICACIÓN A HCEN! El registro de metadatos se ha perdido.");
            System.err.println("Causa del error: " + e.getMessage());
        
            throw new RuntimeException("No se pudo notificar a HCEN.", e);
        }
    }
}