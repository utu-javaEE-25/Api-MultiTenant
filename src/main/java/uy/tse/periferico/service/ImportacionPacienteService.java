package uy.tse.periferico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uy.tse.periferico.dto.UsuarioCentralDTO;
import uy.tse.periferico.model.Paciente;
import uy.tse.periferico.repository.PacienteRepository;

import java.time.LocalDateTime;

@Service
public class ImportacionPacienteService {

    private final PacienteRepository pacienteRepository;
    private final RestTemplate restTemplate;

    // URL del API del central.
    private static final String CENTRAL_API_URL = "http://hcenuy.web.elasticloud.uy/Laboratorio/api/usuarios/ci/";

    @Autowired
    public ImportacionPacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
        this.restTemplate = new RestTemplate();
    }

    public Paciente importarPacientePorCI(String cedula) throws Exception {
        if (pacienteRepository.findByNroDocumento(cedula).isPresent()) {
            throw new Exception("El paciente con CI " + cedula + " ya existe en este sistema.");
        }

        UsuarioCentralDTO usuarioDTO;
        try {
            usuarioDTO = restTemplate.getForObject(CENTRAL_API_URL + cedula, UsuarioCentralDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new Exception("No se encontró ningún paciente con la CI " + cedula + " en el sistema central.");
        } catch (Exception e) {
            throw new Exception("Error de comunicación con el sistema central: " + e.getMessage());
        }

        if (usuarioDTO == null) {
            throw new Exception("La respuesta del sistema central fue inválida o vacía.");
        }

        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setGlobalUserId(usuarioDTO.getId());
        nuevoPaciente.setNroDocumento(usuarioDTO.getCedulaIdentidad());
        nuevoPaciente.setNombre(usuarioDTO.getNombre());
        nuevoPaciente.setApellido(usuarioDTO.getApellido());
        nuevoPaciente.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        nuevoPaciente.setEmail(usuarioDTO.getEmail());

        LocalDateTime ahora = LocalDateTime.now();
        nuevoPaciente.setFechaCreacion(ahora);
        nuevoPaciente.setFechaModificacion(ahora);

        return pacienteRepository.save(nuevoPaciente);
    }
}