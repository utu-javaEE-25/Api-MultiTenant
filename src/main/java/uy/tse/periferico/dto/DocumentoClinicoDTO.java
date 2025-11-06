package uy.tse.periferico.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentoClinicoDTO {
    // --- DATOS DEL ENCABEZADO ---
    private PacienteInfo paciente;
    private ProfesionalInfo profesional;
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String idExternaDoc;
    private LocalDateTime fechaGeneracion;
    private String custodio;

    // --- DATOS ESTRUCTURADOS DEL CUERPO ---
    private List<MotivoConsultaDTO> motivosDeConsulta;
    private List<DiagnosticoDTO> diagnosticos;
    private List<InstruccionSeguimientoDTO> instruccionesDeSeguimiento;

    // --- CLASES ANIDADAS ---
    @Data @NoArgsConstructor public static class PacienteInfo {
        private String nombreCompleto;
        private String nroDocumento;
        private String fechaNacimiento;
        private String sexo;
    }
    @Data @NoArgsConstructor public static class ProfesionalInfo {
        private String nombreCompleto;
    }
    @Data @NoArgsConstructor public static class MotivoConsultaDTO {
        private String descripcion;
        public MotivoConsultaDTO(String d) { this.descripcion = d; } // Constructor útil
    }
    @Data @NoArgsConstructor public static class DiagnosticoDTO {
        private String descripcion;
        private String fechaInicio;
        private String estadoProblema;
        private String gradoCerteza;
    }
    @Data @NoArgsConstructor public static class InstruccionSeguimientoDTO {
        private String tipo;
        private String descripcion;
    }
}