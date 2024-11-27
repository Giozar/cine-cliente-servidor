package servidor.classes;

import servidor.interfaces.FunctionRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FunctionRepositoryMySql implements FunctionRepository {
    private Connection connection;

    // Constructor solo con Connection
    public FunctionRepositoryMySql(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no puede ser null.");
        }
        this.connection = connection;
    }

    @Override
    public List<MovieFunction> getAllFunctions() {
        List<MovieFunction> functions = new ArrayList<>();
        String query = "SELECT f.function_id, f.function_datetime, f.movie_id, m.title, m.duration, m.genre, m.rating, m.description "
                +
                "FROM functions f " +
                "JOIN movies m ON f.movie_id = m.movie_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int functionId = rs.getInt("function_id");
                LocalDateTime dateTime = rs.getTimestamp("function_datetime").toLocalDateTime();

                // Crear el objeto Movie desde los datos de la base de datos
                Movie movie = new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genre"),
                        rs.getString("rating"),
                        rs.getString("description"));

                // Crear el objeto MovieFunction y agregarlo a la lista
                functions.add(new MovieFunction(functionId, dateTime, movie));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al interactuar con la base de datos.", e);
        }

        return functions;
    }

    @Override
    public MovieFunction findFunctionById(int id) {
        MovieFunction function = null;
        String query = "SELECT f.function_id, f.function_datetime, f.movie_id, m.title, m.duration, m.genre, m.rating, m.description "
                +
                "FROM functions f " +
                "JOIN movies m ON f.movie_id = m.movie_id " +
                "WHERE f.function_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime dateTime = rs.getTimestamp("function_datetime").toLocalDateTime();

                    // Crear el objeto Movie desde los datos de la base de datos
                    Movie movie = new Movie(
                            rs.getInt("movie_id"),
                            rs.getString("title"),
                            rs.getInt("duration"),
                            rs.getString("genre"),
                            rs.getString("rating"),
                            rs.getString("description"));

                    // Crear el objeto MovieFunction
                    function = new MovieFunction(id, dateTime, movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al interactuar con la base de datos.", e);
        }

        return function;
    }

    @Override
    public boolean reserveSeat(int functionId, int seatNumber, String clientName, int clientAge) {
        String checkQuery = "SELECT COUNT(*) FROM reservations WHERE function_id = ? AND seat_number = ?";
        String insertQuery = "INSERT INTO reservations (function_id, seat_number, client_name, client_age) VALUES (?, ?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, functionId);
            checkStmt.setInt(2, seatNumber);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // El asiento ya está reservado
                }
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, functionId);
                insertStmt.setInt(2, seatNumber);
                insertStmt.setString(3, clientName);
                insertStmt.setInt(4, clientAge);
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al reservar el asiento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
