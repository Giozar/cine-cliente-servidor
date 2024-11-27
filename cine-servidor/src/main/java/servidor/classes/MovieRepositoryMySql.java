package servidor.classes;

import servidor.interfaces.MovieRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryMySql implements MovieRepository {
    private Connection connection;

    public MovieRepositoryMySql(Connection connection) {
        this.connection = connection;
    }

    // Obtener todas las películas desde la base de datos
    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getInt("duration"),
                    rs.getString("genre"),
                    rs.getString("rating"),
                    rs.getString("description")
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    // Buscar una película por ID
    @Override
    public Movie findMovieById(int id) {
        Movie movie = null;
        String query = "SELECT * FROM movies WHERE movie_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    movie = new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genre"),
                        rs.getString("rating"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movie;
    }
}
