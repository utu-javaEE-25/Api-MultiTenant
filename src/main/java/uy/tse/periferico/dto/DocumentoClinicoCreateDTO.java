package uy.tse.periferico.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentoClinicoCreateDTO {

    private Long pacienteId; 
    private String instanciaMedica;
    private String lugar;
    private LocalDateTime fechaAtencionInicio;
    private LocalDateTime fechaAtencionFin;

    private List<DocumentoClinicoDTO.MotivoConsultaDTO> motivos;
    private List<DocumentoClinicoDTO.DiagnosticoDTO> diagnosticos;
    private List<DocumentoClinicoDTO.InstruccionSeguimientoDTO> instrucciones;
}