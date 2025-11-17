package uy.tse.periferico.dto;

import lombok.Data;
import java.util.List;

@Data
public class DocumentoDetalleDTO {
    // Datos del Paciente
    private String pacienteNombre;
    private String pacienteNroDocumento;
    private String pacienteFechaNacimiento;
    private String pacienteSexo;

    // Datos de la Instancia Médica
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String autor;
    private String documentoId;
    private String fechaGeneracion;
    private String custodio;

    private List<String> motivosDeConsulta;
    private List<DiagnosticoDTO> diagnosticos; 
    private List<String> instruccionesSeguimiento;
}