package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaNacimiento;
    private String fechaFallecimiento;
    @OneToMany (mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosLibros datosLibro) {
        this.nombre = datosLibro.autor().get(0).nombre();
        this.fechaNacimiento = datosLibro.autor().get(0).fechaNacimiento();
        this.fechaFallecimiento = datosLibro.autor().get(0).fechaFallecimiento();

    }

    public Autor(String nombre, String fechaNacimiento, String fechaFallecimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;

    }


    @Override
    public String toString() {
        return "Autor{" +
                "autor='" + nombre + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", fechaFallecimiento='" + fechaFallecimiento + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(String fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }


    public List<Libro> getLibro() {
        return libros;
    }

    public void setLibro(List<Libro> libro) {
        this.libros = libro;
    }
}
