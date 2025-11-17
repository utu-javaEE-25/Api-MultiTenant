// RUTA: src/main/java/uy/tse/periferico/service/ProfesionalService.java
package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.tse.periferico.dto.ProfesionalCreateDTO;
import uy.tse.periferico.dto.ProfesionalDTO;
import uy.tse.periferico.dto.ProfesionalUpdateDTO;
import uy.tse.periferico.exception.ResourceAlreadyExistsException;
import uy.tse.periferico.exception.ResourceNotFoundException;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.ProfesionalRepository;
import uy.tse.periferico.dto.ProfesionalProfileUpdateDTO; // Asegúrate de importar el nuevo DTO

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    // --- ALTA (CREATE) ---
    @Transactional
    public ProfesionalDTO createProfesional(ProfesionalCreateDTO createDTO) {
        // Verifica si el username ya existe para evitar duplicados
        profesionalRepository.findByUsername(createDTO.getUsername()).ifPresent(p -> {
            throw new ResourceAlreadyExistsException("El username '" + createDTO.getUsername() + "' ya existe.");
        });

        Profesional nuevoProfesional = new Profesional();
        nuevoProfesional.setUsername(createDTO.getUsername());
        nuevoProfesional.setNombre(createDTO.getNombre());
        nuevoProfesional.setApellido(createDTO.getApellido());
        nuevoProfesional.setEspecializacion(createDTO.getEspecializacion());
        nuevoProfesional.setEmail(createDTO.getEmail()); // Se asigna el email desde el DTO

        // Hashear la contraseña antes de guardarla en la base de datos
        nuevoProfesional.setPasswordHash(passwordEncoder.encode(createDTO.getPassword()));

        // El estado se establece por defecto a "ACTIVO" en el modelo Profesional.java

        Profesional profesionalGuardado = profesionalRepository.save(nuevoProfesional);
        return mapToDTO(profesionalGuardado);
    }

    // --- MODIFICACIÓN (UPDATE) ---
    @Transactional
    public ProfesionalDTO updateProfesional(Long id, ProfesionalUpdateDTO updateDTO) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));

        // Actualiza los campos proporcionados en el DTO
        profesional.setNombre(updateDTO.getNombre());
        profesional.setApellido(updateDTO.getApellido());
        profesional.setEspecializacion(updateDTO.getEspecializacion());

        // Si se provee una nueva contraseña en el DTO, se actualiza el hash
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            profesional.setPasswordHash(passwordEncoder.encode(updateDTO.getPassword()));
        }

        Profesional profesionalActualizado = profesionalRepository.save(profesional);
        return mapToDTO(profesionalActualizado);
    }

    // --- BAJA (DELETE FÍSICO / HARD DELETE) ---
    @Transactional
    public void deleteProfesional(Long id) {
        // 1. Primero, verificamos si el profesional existe para lanzar una excepción clara si no.
        if (!profesionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional no encontrado con id: " + id);
        }
        // 2. Si existe, lo borra permanentemente de la base de datos.
        // ADVERTENCIA: Esto puede fallar si el profesional tiene registros asociados (ej. Documentos Clínicos)
        // y no hay una estrategia de borrado en cascada configurada.
        profesionalRepository.deleteById(id);
    }

    // --- CONSULTAS (READ) ---
    @Transactional(readOnly = true)
    public ProfesionalDTO findProfesionalById(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));
        return mapToDTO(profesional);
    }

    @Transactional(readOnly = true)
    public List<ProfesionalDTO> findAllProfesionales() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    // --- Mapeador privado de Entidad a DTO ---
    // Convierte un objeto de la base de datos (Profesional) a un objeto de transferencia de datos (ProfesionalDTO)
    // que se enviará al frontend.
    private ProfesionalDTO mapToDTO(Profesional profesional) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getId());
        dto.setUsername(profesional.getUsername());
        dto.setNombre(profesional.getNombre());
        dto.setApellido(profesional.getApellido());
        dto.setEspecializacion(profesional.getEspecializacion());
        dto.setEmail(profesional.getEmail());
        dto.setEstado(profesional.getEstado());
        dto.setRol("PROFESIONAL"); // El rol es fijo para esta entidad en este contexto
        return dto;
    }

    @Transactional(readOnly = true)
    public ProfesionalDTO findProfesionalByEmail(String email) {
        Profesional profesional = profesionalRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un profesional con el email: " + email));
        return mapToDTO(profesional);
    }

    @Transactional
    public ProfesionalDTO updateOwnProfile(String username, ProfesionalProfileUpdateDTO updateDTO) {
        // Busca al profesional por el username obtenido del token JWT
        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado: " + username));

        // Actualiza los campos permitidos
        profesional.setNombre(updateDTO.getNombre());
        profesional.setApellido(updateDTO.getApellido());
        profesional.setEspecializacion(updateDTO.getEspecializacion());
        profesional.setEmail(updateDTO.getEmail());

        // Si se provee una nueva contraseña, se actualiza
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            profesional.setPasswordHash(passwordEncoder.encode(updateDTO.getPassword()));
        }

        Profesional profesionalActualizado = profesionalRepository.save(profesional);
        return mapToDTO(profesionalActualizado);
    }

    @Transactional(readOnly = true)
    public ProfesionalDTO getProfileByUsername(String username) {
        // 1. Busca la entidad Profesional en la base de datos por su username.
        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de profesional no encontrado para el usuario: " + username));

        // 2. Reutiliza tu método mapToDTO para convertir la entidad al DTO que necesita el frontend.
        return mapToDTO(profesional);
    }

}