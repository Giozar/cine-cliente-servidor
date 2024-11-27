package servidor.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
    void connect() throws SQLException;
    void disconnect() throws SQLException;
    Connection getConnection();
}