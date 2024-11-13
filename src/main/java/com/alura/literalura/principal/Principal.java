package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.IdiomaRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private List<DatosLibros> datosLibros = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private IdiomaRepository idiomaRepository;
    //Constructor de la inyección de dependencia
    public Principal(LibroRepository librorepository, AutorRepository autorRepository, IdiomaRepository idiomaRepository) {
        this.libroRepository = librorepository;
        this.autorRepository = autorRepository;
        this.idiomaRepository = idiomaRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0 ){
            var menu = """
                    Elige una opción a través del numero
                    1 - Búscar Libro por Titulo
                    2 - Listar Libros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores vivos en determinado Año
                    5 - Listar libros por Idiomas
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor, ingresa un número válido.");
                continue;
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");

            }
        }

    }

    public void buscarLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ", "+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados()
                .stream().filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("******** Libro Encontrado ***********");
            DatosLibros datosLibro = libroBuscado.get();
            // Imprimir los detalles del libro de manera ordenada
            System.out.println("Título: " + datosLibro.titulo());
            System.out.println("Autor: " + datosLibro.autor().get(0).nombre());
            System.out.println("Idioma: " + datosLibro.idiomas());
            System.out.println("Número de Descargas: " + datosLibro.numeroDescargas());
            System.out.println("*************************************");

            // Verificar si el libro ya existe en la base de datos
            if (libroRepository.existsByTitulo(datosLibro.titulo())) {
                System.out.println("Este libro ya está registrado en la base de datos.");
            } else {
                Libro libro = new Libro(datosLibro);
                libroRepository.save(libro);
                System.out.println("El libro ha sido guardado exitosamente.");
            }

        }else{
            System.out.println("Libro no encontrado");
        }

    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(libro -> {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                System.out.println("Idiomas: " + libro.getIdioma());
                System.out.println("Número de Descargas: " + libro.getNumeroDescargas());
                System.out.println("-----------------------------------");
            });
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        // Iterar sobre cada autor sin validar si la lista está vacía
        for (Autor autor : autores) {
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fecha de Fallecimiento: " + autor.getFechaFallecimiento());
            List<Libro> libros = autor.getLibro();
            System.out.println("Libros:");
            //Iterar sobre los libros del autor
            for (Libro libro : libros) {
               System.out.println("- Título: " + libro.getTitulo());
            }
            System.out.println("-----------------------------------");
        }
    }

    private void listarAutoresVivosAño() {
        listarAutores();
        System.out.println("Ingrese el año entre nacimiento y fallecimiento del autor a buscar:");
        var buscarAño = Integer.parseInt(teclado.nextLine());

        List<Autor> autores = autorRepository.findAll();

        List<Autor> autoresVivos = autores.stream()
                .filter(a -> {int añoNacimiento = Integer.parseInt(a.getFechaNacimiento());
                              int añoFallecimiento = Integer.parseInt(a.getFechaFallecimiento());
                              return añoNacimiento <= buscarAño && añoFallecimiento > buscarAño; })
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("******** No se encontraron autores vivos en el año especificado *********");
        } else {
            System.out.println("******** Autores vivos en " + buscarAño + " *********");
            autoresVivos.forEach(autor -> System.out.println(autor.getNombre()));
        }
    }

    private void listarLibrosPorIdioma() {
        while (true) {
            System.out.println("""
            Ingrese el idioma para buscar libros
            es - Español
            en - Ingles
            fr - Frances
            pt - Portuguese""");

            var idiomaBuscado = teclado.nextLine().toLowerCase(); // Convertimos a minúsculas para facilitar la comparación

            if ("es".equals(idiomaBuscado) || "en".equals(idiomaBuscado) || "fr".equals(idiomaBuscado) || "pt".equals(idiomaBuscado)) {
                // Obtener los libros del repositorio
                List<Libro> libros = libroRepository.findByIdiomas_IdiomaNombre(idiomaBuscado);

                if (libros.isEmpty()) {
                    System.out.println("***** No se encontraron libros en el idioma especificado. *********");
                } else {
                    System.out.println("Libros en " + idiomaBuscado + ":");
                    for (Libro libro : libros) {
                        System.out.println("- " + libro.getTitulo());
                    }
                }
                break;
            } else {
                System.out.println("Idioma no válido. Por favor, ingrese un idioma válido (es/en/fr/pt).");
            }
        }
    }

}
