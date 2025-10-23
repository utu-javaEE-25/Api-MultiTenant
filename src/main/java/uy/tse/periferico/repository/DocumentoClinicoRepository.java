package uy.tse.periferico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uy.tse.periferico.model.DocumentoClinico;


@Repository
public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {
}
