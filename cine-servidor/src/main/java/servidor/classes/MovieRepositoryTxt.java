package servidor.classes;

import servidor.interfaces.MovieRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepositoryTxt implements MovieRepository {
    private List<Movie> movies;

    public MovieRepositoryTxt(String filePath) {
        movies = new ArrayList<>();
        loadMovies(filePath);
    }

    // Método para cargar las películas desde un archivo de texto
    private void loadMovies(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Suponiendo que el formato es: id,title,duration,genre,rating,description
                String[] data = line.split(",");
                if (data.length >= 6) {
                    int id = Integer.parseInt(data[0]);
                    String title = data[1];
                    int duration = Integer.parseInt(data[2]);
                    String genre = data[3];
                    String rating = data[4];
                    String description = data[5];
                    movies.add(new Movie(id, title, duration, genre, rating, description));
                } else {
                    System.err.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear un número: " + e.getMessage());
        }
    }

    // Implementación de getAllMovies()
    @Override
    public List<Movie> getAllMovies() {
        return movies;
    }

    // Implementación de findMovieById()
    @Override
    public Movie findMovieById(int id) {
        Optional<Movie> movie = movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
        return movie.orElse(null);
    }
}
