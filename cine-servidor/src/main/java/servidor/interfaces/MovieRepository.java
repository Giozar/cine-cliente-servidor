/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package servidor.interfaces;

/**
 *
 * @author giozar
 */
import java.util.List;

import servidor.classes.Movie;

public interface MovieRepository {
    List<Movie> getAllMovies();
    Movie findMovieById(String id);
}
