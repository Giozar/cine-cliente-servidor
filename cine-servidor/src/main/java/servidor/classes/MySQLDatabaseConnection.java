package servidor.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import servidor.interfaces.DatabaseConnection;

public class MySQLDatabaseConnection implements DatabaseConnection {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public MySQLDatabaseConnection(String host, int port, String databaseName, String username, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false&serverTimezone=UTC";
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión a MySQL establecida.");
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión a MySQL cerrada.");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
