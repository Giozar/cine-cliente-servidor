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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import servidor.interfaces.FunctionRepository;
import servidor.interfaces.MovieRepository;

public class FunctionRepositoryTxt implements FunctionRepository {
    private List<MovieFunction> functions;
    private MovieRepository movieRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FunctionRepositoryTxt(String filePath, MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.functions = new ArrayList<>();
        loadFunctions(filePath);
    }

    private void loadFunctions(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String functionId = data[0];
                String movieId = data[1];
                LocalDateTime dateTime = LocalDateTime.parse(data[2], formatter);
                
                // Buscar la película en el repositorio de películas
                Movie movie = movieRepository.findMovieById(movieId);
                if (movie != null) {
                    functions.add(new MovieFunction(functionId, dateTime, movie));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MovieFunction> getAllFunctions() {
        return functions;
    }

    @Override
    public MovieFunction findFunctionById(String id) {
        return functions.stream().filter(function -> function.getId().equals(id)).findFirst().orElse(null);
    }
}
