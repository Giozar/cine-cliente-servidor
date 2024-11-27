package servidor.classes;

import servidor.classes.Room;
import servidor.interfaces.RoomRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomRepositoryMySql implements RoomRepository {
    private Connection connection;

    public RoomRepositoryMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Room findRoomById(int roomId) {
        String query = "SELECT room_id, room_number, total_seats FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("room_id");
                    int number = rs.getInt("room_number");
                    int totalSeats = rs.getInt("total_seats");
                    return new Room(id, number, totalSeats);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la sala: " + e.getMessage());
        }
        return null;
    }
}
