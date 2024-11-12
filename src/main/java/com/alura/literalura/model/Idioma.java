package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "idiomas")
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idiomaNombre;
    @ManyToMany(mappedBy = "idiomas")
    private List<Libro> libros;

    public Idioma() {}

    public Idioma(DatosLibros datosLibro) {
        this.idiomaNombre = datosLibro.idiomas().get(0).idiomaNombre;
    }

    public Idioma(String idioma) {
        this.idiomaNombre = idioma;
    }

    @Override
    public String toString() {
        return "Idioma: "  + idiomaNombre ;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdioma() {
        return idiomaNombre;
    }

    public void setIdioma(String idioma) {
        this.idiomaNombre = idioma;
    }
}

