package com.alura.literalura.principal;

import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu(){
        //var json = consumoAPI.obtenerDatos(URL_BASE);
        //System.out.println(json);
        //var datos = conversor.obtenerDatos(json, Datos.class);
        //System.out.println(datos);

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
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibro();
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
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados()
                .stream().filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado");
            DatosLibros libro = libroBuscado.get();
            // Imprimir los detalles del libro de manera ordenada
            System.out.println("Título: " + libro.titulo());
            System.out.println("Autor: " + libro.autores().get(0).autor());
            System.out.println("Idioma: " + libro.idioma());
            System.out.println("Número de Descargas: " + libro.numeroDescargas());
        }else{
            System.out.println("Libro no encontrado");
        }
    }



}
