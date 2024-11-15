/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servidor.classes;

/**
 *
 * @author giozar
 */
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataInitializer {
    private static final String MOVIE_FILE_PATH = "databases/movies.txt";
    private static final String FUNCTION_FILE_PATH = "databases/functions.txt";

    public static void initializeData() throws IOException {
        // Crea la carpeta databases si no existe
        Files.createDirectories(Paths.get("databases"));

        // Crear y rellenar el archivo de películas si no existe
        if (!Files.exists(Paths.get(MOVIE_FILE_PATH))) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(MOVIE_FILE_PATH))) {
                for (int i = 1; i <= 10; i++) {
                    writer.write(i + ",Movie " + i + "," + (i % 5 + 1));
                    writer.newLine();
                }
            }
        }

        // Crear y rellenar el archivo de funciones si no existe
        if (!Files.exists(Paths.get(FUNCTION_FILE_PATH))) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FUNCTION_FILE_PATH))) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime now = LocalDateTime.now();
                for (int i = 1; i <= 10; i++) {
                    String dateTime = now.plusDays(i).format(formatter);
                    writer.write("f" + i + "," + i + "," + dateTime);
                    writer.newLine();
                }
            }
        }
    }
}
