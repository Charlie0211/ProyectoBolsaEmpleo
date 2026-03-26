package org.project1.bolsaempleo.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "oferente_habilidades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"oferente_id", "caracteristica_id"})
)
public class OferenteHabilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oferente_id", nullable = false)
    private Oferente oferente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @Column(nullable = false)
    private Integer nivel = 1;

    public OferenteHabilidad() {
    }

    public OferenteHabilidad(Oferente oferente, Caracteristica caracteristica, Integer nivel) {
        this.oferente = oferente;
        this.caracteristica = caracteristica;
        this.nivel = nivel;
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

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}

