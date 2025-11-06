package uy.tse.periferico.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_clinico")
@Data
@NoArgsConstructor
public class DocumentoClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Column(name = "id_externa_doc", unique = true)
    private String idExternaDoc;

    // --- CAMPOS NUEVOS Y MODIFICADOS ---
    @Column(name = "instancia_medica")
    private String instanciaMedica;

    private String lugar;

    @Column(name = "fecha_atencion_inicio")
    private LocalDateTime fechaAtencionInicio;
    
    @Column(name = "fecha_atencion_fin")
    private LocalDateTime fechaAtencionFin;

    // Usaremos JSONB para almacenar datos estructurados de forma flexible
    @Column(columnDefinition = "jsonb")
    private String motivos; // Contendrá un JSON array de motivos

    @Column(columnDefinition = "jsonb")
    private String diagnosticos; // Contendrá un JSON array de diagnósticos

    @Column(columnDefinition = "jsonb")
    private String instrucciones; // Contendrá un JSON array de instrucciones
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
}