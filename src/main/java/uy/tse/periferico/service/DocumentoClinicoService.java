package uy.tse.periferico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.tse.periferico.dto.DocumentoMetadataMensajeDTO; 
import uy.tse.periferico.dto.DocumentoClinicoCreateDTO; // Importar el nuevo DTO
import uy.tse.periferico.dto.DocumentoClinicoDTO;
import uy.tse.periferico.exception.ResourceNotFoundException;
import uy.tse.periferico.model.DocumentoClinico;
import uy.tse.periferico.model.Paciente; // Importar Paciente
import uy.tse.periferico.model.Profesional; // Importar Profesional
import uy.tse.periferico.repository.DocumentoClinicoRepository;
import uy.tse.periferico.repository.PacienteRepository; // Importar PacienteRepository
import uy.tse.periferico.repository.ProfesionalRepository; // Importar ProfesionalRepository
import uy.tse.periferico.repository.TenantConfiguracionRepository;
import uy.tse.periferico.model.TenantConfiguracion;

import java.time.LocalDateTime; // Importar LocalDateTime
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoClinicoService {

    private final DocumentoClinicoRepository repository;
    private final PacienteRepository pacienteRepository; // Añadir dependencia
    private final ProfesionalRepository profesionalRepository;
    private final TenantConfiguracionRepository configRepository;
    private final ObjectMapper objectMapper; // Spring Boot inyecta un ObjectMapper por defecto
    private final HcenRestNotifierService notifierService;



    @Transactional
    public DocumentoClinicoDTO createDocumento(DocumentoClinicoCreateDTO createDTO, String profesionalUsername, String tenantId) {
        // 1. Buscar las entidades relacionadas
        Paciente paciente = pacienteRepository.findById(createDTO.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + createDTO.getPacienteId()));
        Profesional profesional = profesionalRepository.findByUsername(profesionalUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado: " + profesionalUsername));

        // 2. Crear la nueva entidad DocumentoClinico
        DocumentoClinico nuevoDoc = new DocumentoClinico();
        nuevoDoc.setPaciente(paciente);
        nuevoDoc.setProfesional(profesional);

        // Generar un ID externo único
        nuevoDoc.setIdExternaDoc("DOC-" + tenantId.toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8));

        // Mapear datos directos
        nuevoDoc.setInstanciaMedica(createDTO.getInstanciaMedica());
        nuevoDoc.setLugar(createDTO.getLugar());
        nuevoDoc.setFechaAtencionInicio(createDTO.getFechaAtencionInicio());
        nuevoDoc.setFechaAtencionFin(createDTO.getFechaAtencionFin());
        nuevoDoc.setFechaCreacion(LocalDateTime.now());

        // 3. Serializar los campos JSONB
        try {
            nuevoDoc.setMotivos(objectMapper.writeValueAsString(createDTO.getMotivos()));
            nuevoDoc.setDiagnosticos(objectMapper.writeValueAsString(createDTO.getDiagnosticos()));
            nuevoDoc.setInstrucciones(objectMapper.writeValueAsString(createDTO.getInstrucciones()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar los datos del documento a JSON", e);
        }

        // 4. Guardar y devolver el DTO
        DocumentoClinico docGuardado = repository.save(nuevoDoc);

        try {
            DocumentoMetadataMensajeDTO mensajeDTO = new DocumentoMetadataMensajeDTO();
            mensajeDTO.setPacienteGlobalId(docGuardado.getPaciente().getGlobalUserId());
            mensajeDTO.setTenantSchemaName(tenantId);
            mensajeDTO.setIdExternaDoc(docGuardado.getIdExternaDoc());
            mensajeDTO.setTipoDocumento(docGuardado.getInstanciaMedica());

            // Llamada síncrona al servicio de notificación
            notifierService.notificarNuevoDocumento(mensajeDTO);

        } catch (Exception e) {
            // Si la notificación falla, simplemente imprimimos una advertencia en la consola
            // pero no detenemos la operación. El documento ya se guardó localmente.
            System.err.println("ADVERTENCIA: El documento se creó localmente, pero la notificación REST a HCEN falló. " +
                               "El metadato no se registrará en el sistema central. Causa: " + e.getMessage());
        }
        
        TenantConfiguracion config = configRepository.findById(1).orElse(new TenantConfiguracion());

        try {
            return mapToDTO(docGuardado, config.getTituloPrincipal());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear el documento guardado a DTO", e);
        }
    }

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