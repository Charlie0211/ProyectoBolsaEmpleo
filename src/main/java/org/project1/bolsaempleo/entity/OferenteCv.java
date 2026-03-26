package org.project1.bolsaempleo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferente_cv")
public class OferenteCv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oferente_id", nullable = false)
    private Oferente oferente;

    /** Nombre del archivo PDF guardado */
    @Column(nullable = false)
    private String nombreArchivo;

    /** Ruta o identificador del archivo en el sistema */
    @Column(nullable = false)
    private String rutaArchivo;

    @Column(nullable = false)
    private LocalDateTime fechaCarga = LocalDateTime.now();

    public OferenteCv() {
    }

    public OferenteCv(Oferente oferente, String nombreArchivo, String rutaArchivo) {
        this.oferente = oferente;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.fechaCarga = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Oferente getOferente() {
        return oferente;
    }

    public void setOferente(Oferente oferente) {
        this.oferente = oferente;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}

