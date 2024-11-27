package servidor.classes;

import servidor.interfaces.FunctionRepository;
import servidor.interfaces.MovieRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionRepositoryTxt implements FunctionRepository {
    private List<MovieFunction> functions;
    private MovieRepository movieRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FunctionRepositoryTxt(String filePath, MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.functions = new ArrayList<>();
        loadFunctions(filePath);
    }

    private void loadFunctions(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Suponiendo que el formato es: functionId,movieId,function_datetime
                String[] data = line.split(",");
                if (data.length >= 3) {
                    int functionId = Integer.parseInt(data[0]);
                    int movieId = Integer.parseInt(data[1]);
                    LocalDateTime dateTime = LocalDateTime.parse(data[2], formatter);

                    // Buscar la película en el repositorio de películas
                    Movie movie = movieRepository.findMovieById(movieId);
                    if (movie != null) {
                        functions.add(new MovieFunction(functionId, dateTime, movie));
                    } else {
                        System.err.println("Película no encontrada con ID: " + movieId);
                    }
                } else {
                    System.err.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            System.err.println("Error al parsear datos: " + e.getMessage());
        }
    }

    @Override
    public List<MovieFunction> getAllFunctions() {
        return functions;
    }

    @Override
    public MovieFunction findFunctionById(int id) {
        Optional<MovieFunction> function = functions.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
        return function.orElse(null);
    }

    @Override
    public boolean reserveSeat(int functionId, int seatNumber) {
        // Implementación vacía o lógica según sea necesario
        return false;
    }
}
