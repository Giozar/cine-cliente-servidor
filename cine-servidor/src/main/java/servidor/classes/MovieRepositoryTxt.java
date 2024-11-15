/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servidor.classes;

/**
 *
 * @author giozar
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import servidor.interfaces.MovieRepository;

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
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                int room = Integer.parseInt(data[2]);
                movies.add(new Movie(id, name, room));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implementación de getAllMovies()
    @Override
    public List<Movie> getAllMovies() {
        return movies;
    }

    // Implementación de findMovieById()
    @Override
    public Movie findMovieById(String id) {
        return movies.stream().filter(movie -> movie.getId().equals(id)).findFirst().orElse(null);
    }
}
