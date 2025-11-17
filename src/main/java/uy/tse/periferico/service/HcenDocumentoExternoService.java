package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uy.tse.periferico.dto.DocumentoDetalleDTO;
import uy.tse.periferico.exception.AccesoNoAutorizadoException;
import uy.tse.periferico.dto.DocumentoClinicoDTO;

@Service
public class HcenDocumentoExternoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.api.url.documento-externo}")
    private String hcenApiUrl;

    public DocumentoClinicoDTO obtenerDocumento(String schemaSolicitante, String cedulaPaciente, String docId,Long idProfesional) {
        String url = UriComponentsBuilder.fromHttpUrl(hcenApiUrl)
                .queryParam("schemaSolicitante", schemaSolicitante)
                .queryParam("cedulaPaciente", cedulaPaciente)
                .queryParam("docId", docId)
                .queryParam("idProfesional", idProfesional)
                .toUriString();
        try {
            return restTemplate.getForObject(url, DocumentoClinicoDTO.class);
        } catch (HttpClientErrorException.Forbidden e) {
            // Capturamos el 403 y lo relanzamos como nuestra propia excepción
            throw new AccesoNoAutorizadoException(e.getResponseBodyAsString());
        }
    }
}
