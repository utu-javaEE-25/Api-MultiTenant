package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.tse.periferico.model.Paciente;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Spring Data JPA crea la consulta automáticamente a partir del nombre del método.
    Optional<Paciente> findByNroDocumento(String nroDocumento);
    
    List<Paciente> findByNroDocumentoContainingIgnoreCase(String nroDocumento);
}