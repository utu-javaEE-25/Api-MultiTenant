package uy.tse.periferico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uy.tse.periferico.model.DocumentoClinico;


@Repository
public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {

    /**
     * Busca un documento por su identificador de negocio único (String) y carga
     * ansiosamente las relaciones con Paciente y Profesional para evitar LazyInitializationException.
     * Este será el ÚNICO método de búsqueda que expondremos en la API.
     */
    @Query("SELECT d FROM DocumentoClinico d JOIN FETCH d.paciente JOIN FETCH d.profesional WHERE d.idExternaDoc = :idExternaDoc")
    Optional<DocumentoClinico> findByIdExternaDocWithDetails(String idExternaDoc);

    // Mantenemos este método simple para usos internos que no necesitan los detalles,
    // como verificar si un ID ya existe antes de insertar (ej. en el DataLoader).
    Optional<DocumentoClinico> findByIdExternaDoc(String idExternaDoc);
}
