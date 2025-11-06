package uy.tse.periferico.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.tse.periferico.dto.DocumentoClinicoDTO;
import uy.tse.periferico.exception.ResourceNotFoundException;
import uy.tse.periferico.model.DocumentoClinico;
import uy.tse.periferico.repository.DocumentoClinicoRepository;
import uy.tse.periferico.repository.TenantConfiguracionRepository;
import uy.tse.periferico.model.TenantConfiguracion;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DocumentoClinicoService {

    private final DocumentoClinicoRepository repository;
    private final TenantConfiguracionRepository configRepository;
    private final ObjectMapper objectMapper; // Spring Boot inyecta un ObjectMapper por defecto

    @Transactional(readOnly = true)
    public DocumentoClinicoDTO findDocumentoByIdExterna(String idExterna) {
        DocumentoClinico doc = repository.findByIdExternaDocWithDetails(idExterna)
                .orElseThrow(() -> new ResourceNotFoundException("Documento clínico no encontrado con id externo: " + idExterna));
        
        TenantConfiguracion config = configRepository.findById(1).orElse(new TenantConfiguracion());
        
        try {
            return mapToDTO(doc, config.getTituloPrincipal());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear el documento a DTO", e);
        }
    }

    private DocumentoClinicoDTO mapToDTO(DocumentoClinico doc, String nombreCustodio) throws Exception {
        DocumentoClinicoDTO dto = new DocumentoClinicoDTO();
        
        // --- Mapeo de Encabezado ---
        dto.setInstanciaMedica(doc.getInstanciaMedica());
        dto.setLugar(doc.getLugar());
        dto.setIdExternaDoc(doc.getIdExternaDoc());
        dto.setFechaGeneracion(doc.getFechaCreacion());
        dto.setCustodio(nombreCustodio);
        
        if (doc.getFechaAtencionInicio() != null && doc.getFechaAtencionFin() != null) {
            DateTimeFormatter atencionFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy, HH:mm:ss", new Locale("es", "ES"));
            dto.setFechaAtencion("Desde " + doc.getFechaAtencionInicio().format(atencionFormatter) + 
                               " hasta " + doc.getFechaAtencionFin().format(atencionFormatter));
        }

        // --- Mapeo de Paciente ---
        DocumentoClinicoDTO.PacienteInfo pacienteInfo = new DocumentoClinicoDTO.PacienteInfo();
        if (doc.getPaciente() != null) {
            pacienteInfo.setNombreCompleto(doc.getPaciente().getNombre() + " " + doc.getPaciente().getApellido());
            pacienteInfo.setNroDocumento(doc.getPaciente().getNroDocumento());
            if (doc.getPaciente().getFechaNacimiento() != null) {
                pacienteInfo.setFechaNacimiento(doc.getPaciente().getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy", new Locale("es", "ES"))));
            }
            pacienteInfo.setSexo(doc.getPaciente().getSexo());
        }
        dto.setPaciente(pacienteInfo);

        // --- Mapeo de Profesional ---
        DocumentoClinicoDTO.ProfesionalInfo profesionalInfo = new DocumentoClinicoDTO.ProfesionalInfo();
        if (doc.getProfesional() != null) {
            profesionalInfo.setNombreCompleto(doc.getProfesional().getNombre() + " " + doc.getProfesional().getApellido());
        }
        dto.setProfesional(profesionalInfo);

        // --- DESERIALIZACIÓN DE CAMPOS JSON ---
        if (doc.getMotivos() != null) {
            dto.setMotivosDeConsulta(objectMapper.readValue(doc.getMotivos(), new TypeReference<>() {}));
        } else {
            dto.setMotivosDeConsulta(Collections.emptyList());
        }

        if (doc.getDiagnosticos() != null) {
            dto.setDiagnosticos(objectMapper.readValue(doc.getDiagnosticos(), new TypeReference<>() {}));
        } else {
            dto.setDiagnosticos(Collections.emptyList());
        }
        
        if (doc.getInstrucciones() != null) {
            dto.setInstruccionesDeSeguimiento(objectMapper.readValue(doc.getInstrucciones(), new TypeReference<>() {}));
        } else {
            dto.setInstruccionesDeSeguimiento(Collections.emptyList());
        }

        return dto;
    }
}