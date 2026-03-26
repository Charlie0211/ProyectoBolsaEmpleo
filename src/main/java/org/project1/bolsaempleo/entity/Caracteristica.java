package org.project1.bolsaempleo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caracteristicas")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    /**
     * Referencia al padre (null = categoría raíz).
     * Ejemplo: "Lenguajes de programación" → padre = null
     *          "Java"                      → padre = "Lenguajes de programación"
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Caracteristica padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caracteristica> hijos = new ArrayList<>();

    public Caracteristica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Caracteristica getPadre() { return padre; }
    public void setPadre(Caracteristica padre) { this.padre = padre; }

    public List<Caracteristica> getHijos() { return hijos; }
    public void setHijos(List<Caracteristica> hijos) { this.hijos = hijos; }

    /** Retorna si esta característica es una categoría raíz (sin padre) */
    public boolean esRaiz() { return padre == null; }
}

