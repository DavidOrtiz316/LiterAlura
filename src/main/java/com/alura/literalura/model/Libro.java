package com.alura.literalura.model;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "libro_idioma",joinColumns = @JoinColumn(name = "libro_id"),inverseJoinColumns = @JoinColumn(name = "idioma_id"))
    private List<Idioma> idiomas;
    private Double numeroDescargas;

    public Libro() {}

    public Libro(String titulo, Autor autor, List<Idioma> idiomas, Double numeroDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
    }

    public Libro(DatosLibros datosLibro){
        this.titulo = datosLibro.titulo(); // Asigna el título
        this.autor = datosLibro.autor().get(0).toAutor();
        this.idiomas = datosLibro.idiomas(); // Asigna el idioma
        this.numeroDescargas = datosLibro.numeroDescargas(); // Asigna el número de descargas

    }

    @Override
    public String toString() {
        return  "Libro{" +
                "titulo=" + titulo + '\'' +
                ", autor=" + autor +
                ", idiomas=" + idiomas +
                ", numeroDescargas=" + numeroDescargas +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Idioma> getIdioma() {
        return idiomas;
    }

    public void setIdioma(List<Idioma> idioma) {
        this.idiomas = idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
