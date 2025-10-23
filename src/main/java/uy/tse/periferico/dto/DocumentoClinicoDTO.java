package uy.tse.periferico.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class DocumentoClinicoDTO {
    private Long docId;
    private String tipo;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private PacienteInfo paciente;
    private ProfesionalInfo profesional;

    @Data
    @NoArgsConstructor
    public static class PacienteInfo {
        private Long pacienteId;
        private String nombreCompleto;
    }

    @Data
    @NoArgsConstructor
    public static class ProfesionalInfo {
        private Long profesionalId;
        private String nombreCompleto;
        private String especializacion;
    }
}
