package servidor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import servidor.classes.DataInitializer;
import servidor.classes.FunctionRepositoryMySql;
import servidor.classes.FunctionRepositoryTxt;
import servidor.classes.MovieRepositoryTxt;
import servidor.classes.MovieServer;
import servidor.interfaces.FunctionRepository;
import servidor.interfaces.MovieRepository;
import servidor.services.FunctionService;

/**
 *
 * @autor giozar
 */
public class Servidor {
    public static void main(String[] args) {
        initSQL();
    }


    public static void initSQL(){
        // Configuración de la base de datos
        String url = "jdbc:mysql://localhost:3306/cinema?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "root";

        

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexión a la base de datos MySQL establecida.");

            // Inicializamos el repositorio con MySQL
            FunctionRepository functionRepository = new FunctionRepositoryMySql(connection);

            // Inicializamos el servicio de funciones
            FunctionService functionService = new FunctionService(functionRepository);

            // Iniciamos el servidor de funciones
            MovieServer server = new MovieServer(functionService);
            server.start();

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void initTxt(){
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
