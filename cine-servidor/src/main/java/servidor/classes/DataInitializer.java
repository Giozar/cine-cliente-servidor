package servidor.classes;

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
        // Crear la carpeta databases si no existe
        Files.createDirectories(Paths.get("databases"));

        // Crear y rellenar el archivo de películas si no existe
        if (!Files.exists(Paths.get(MOVIE_FILE_PATH))) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(MOVIE_FILE_PATH))) {
                // Escribir datos de películas en el formato:
                // id,title,duration,genre,rating,description
                writer.write("1,The Great Adventure,120,Action,PG-13,An epic action adventure.");
                writer.newLine();
                writer.write("2,Romantic Escape,95,Romance,PG,A tale of love and discovery.");
                writer.newLine();
                writer.write("3,Mystery Manor,110,Mystery,PG-13,Unravel the secrets of the manor.");
                writer.newLine();
                writer.write("4,Comedy Nights,100,Comedy,PG,Laughs guaranteed.");
                writer.newLine();
                writer.write("5,Space Odyssey,130,Sci-Fi,PG-13,Journey through the stars.");
                writer.newLine();
                writer.write("6,Horror House,105,Horror,R,Not for the faint-hearted.");
                writer.newLine();
                writer.write("7,Animated Dreams,90,Animation,G,Fun for the whole family.");
                writer.newLine();
                writer.write("8,Historical Battles,150,History,PG-13,Epic historical reenactments.");
                writer.newLine();
                writer.write("9,Documentary World,80,Documentary,G,Learn about our planet.");
                writer.newLine();
                writer.write("10,Fantasy Realms,115,Fantasy,PG,Explore magical worlds.");
                writer.newLine();
            }
        }

        // Crear y rellenar el archivo de funciones si no existe
        if (!Files.exists(Paths.get(FUNCTION_FILE_PATH))) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FUNCTION_FILE_PATH))) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                // Escribir datos de funciones en el formato:
                // functionId,movieId,function_datetime
                writer.write("1,1," + now.plusDays(1).withHour(18).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("2,2," + now.plusDays(1).withHour(20).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("3,3," + now.plusDays(2).withHour(16).withMinute(30).format(formatter));
                writer.newLine();
                writer.write("4,4," + now.plusDays(2).withHour(19).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("5,5," + now.plusDays(3).withHour(21).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("6,6," + now.plusDays(3).withHour(22).withMinute(30).format(formatter));
                writer.newLine();
                writer.write("7,7," + now.plusDays(4).withHour(14).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("8,8," + now.plusDays(4).withHour(17).withMinute(0).format(formatter));
                writer.newLine();
                writer.write("9,9," + now.plusDays(5).withHour(19).withMinute(30).format(formatter));
                writer.newLine();
                writer.write("10,10," + now.plusDays(5).withHour(20).withMinute(45).format(formatter));
                writer.newLine();
            }
        }
    }
}
