/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package servidor;

import java.io.IOException;

import servidor.classes.DataInitializer;
import servidor.classes.FunctionRepositoryTxt;
import servidor.classes.MovieRepositoryTxt;
import servidor.classes.MovieServer;
import servidor.interfaces.FunctionRepository;
import servidor.interfaces.MovieRepository;
import servidor.services.FunctionService;

/**
 *
 * @author giozar
 */
public class Servidor {
    public static void main(String[] args) {
        try {
            // Inicializamos los datos
            DataInitializer.initializeData();

            // Inicializamos el repositorio de películas y funciones
            MovieRepository movieRepository = new MovieRepositoryTxt("databases/movies.txt");
            FunctionRepository functionRepository = new FunctionRepositoryTxt("databases/functions.txt", movieRepository);
            FunctionService functionService = new FunctionService(functionRepository);

            // Iniciamos el servidor de funciones
            MovieServer server = new MovieServer(functionService);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
