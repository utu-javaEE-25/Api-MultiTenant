package uy.tse.periferico.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentoMetadataHcenDTO {
    private String idExternaDoc;
    private Long idCustodio;
    private String tipoDocumento;
    private LocalDateTime fechaCreacion;
    private String nombrePrestador;
    private String schemaCustodio;
}
