package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import uy.tse.periferico.dto.DocumentoClinicoDTO;
import uy.tse.periferico.exception.ResourceNotFoundException;
import uy.tse.periferico.model.DocumentoClinico;
import uy.tse.periferico.repository.DocumentoClinicoRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;



@Service
@RequiredArgsConstructor
public class DocumentoClinicoService {

    private final DocumentoClinicoRepository repository;
    
    @Transactional 
    public DocumentoClinicoDTO findDocumentoById(Long id) {
        DocumentoClinico doc = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento clínico no encontrado con id: " + id));
        
        return mapToDTO(doc);
    }

    private DocumentoClinicoDTO mapToDTO(DocumentoClinico doc) {
        DocumentoClinicoDTO dto = new DocumentoClinicoDTO();
        dto.setDocId(doc.getDocId());
        dto.setTipo(doc.getTipo());
        dto.setContenido(doc.getContenido());
        dto.setFechaCreacion(doc.getFechaCreacion());

        // Mapeo de Paciente
        DocumentoClinicoDTO.PacienteInfo pacienteInfo = new DocumentoClinicoDTO.PacienteInfo();
        pacienteInfo.setPacienteId(doc.getPaciente().getPacienteId());
        pacienteInfo.setNombreCompleto(doc.getPaciente().getNombre() + " " + doc.getPaciente().getApellido());
        dto.setPaciente(pacienteInfo);

        // Mapeo de Profesional
        DocumentoClinicoDTO.ProfesionalInfo profesionalInfo = new DocumentoClinicoDTO.ProfesionalInfo();
        profesionalInfo.setProfesionalId(doc.getProfesional().getId());
        profesionalInfo.setNombreCompleto(doc.getProfesional().getNombre() + " " + doc.getProfesional().getApellido());
        profesionalInfo.setEspecializacion(doc.getProfesional().getEspecializacion());
        dto.setProfesional(profesionalInfo);

        return dto;
    }
}