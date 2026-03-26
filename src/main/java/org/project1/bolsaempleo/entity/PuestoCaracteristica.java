package org.project1.bolsaempleo.entity;

import jakarta.persistence.*;

/**
 * Tabla intermedia que liga un Puesto con una Caracteristica
 * y almacena el nivel requerido (1=Básico … 5=Experto).
 *
 * Tabla en MySQL: puesto_caracteristicas
 */
@Entity
@Table(name = "puesto_caracteristicas")
public class PuestoCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    /**
     * Nivel requerido para este puesto.
     * 1 = Básico, 2 = Elemental, 3 = Intermedio, 4 = Avanzado, 5 = Experto
     */
    @Column(nullable = false)
    private Integer nivelRequerido = 1;

    public PuestoCaracteristica() {}

    public PuestoCaracteristica(Puesto puesto, Caracteristica caracteristica, Integer nivelRequerido) {
        this.puesto = puesto;
        this.caracteristica = caracteristica;
        this.nivelRequerido = nivelRequerido;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Puesto getPuesto() { return puesto; }
    public void setPuesto(Puesto puesto) { this.puesto = puesto; }

    public Caracteristica getCaracteristica() { return caracteristica; }
    public void setCaracteristica(Caracteristica caracteristica) { this.caracteristica = caracteristica; }

    public Integer getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(Integer nivelRequerido) { this.nivelRequerido = nivelRequerido; }

    /** Devuelve la etiqueta legible del nivel */
    public String getNivelLabel() {
        return switch (nivelRequerido) {
            case 1 -> "Básico";
            case 2 -> "Elemental";
            case 3 -> "Intermedio";
            case 4 -> "Avanzado";
            case 5 -> "Experto";
            default -> "Desconocido";
        };
    }
}

