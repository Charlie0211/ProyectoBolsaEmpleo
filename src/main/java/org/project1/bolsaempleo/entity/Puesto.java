package org.project1.bolsaempleo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "puestos")
public class Puesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Double salario;
    private Boolean activo = Boolean.TRUE;

    /** true = público (visible para todos), false = privado (solo oferentes registrados) */
    private Boolean esPublico = Boolean.TRUE;

    @OneToMany(mappedBy = "puesto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PuestoCaracteristica> caracteristicas = new ArrayList<>();

    public Puesto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Boolean getEsPublico() { return esPublico; }
    public void setEsPublico(Boolean esPublico) { this.esPublico = esPublico; }
    public List<PuestoCaracteristica> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<PuestoCaracteristica> caracteristicas) { this.caracteristicas = caracteristicas; }
}
