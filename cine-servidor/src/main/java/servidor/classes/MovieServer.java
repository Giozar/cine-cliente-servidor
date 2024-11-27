package servidor.classes;

import servidor.services.FunctionService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MovieServer {
    private static final int PORT = 1234;
    private FunctionService functionService;

    public MovieServer(FunctionService functionService) {
        this.functionService = functionService;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Pasar el socket y el servicio a un nuevo manejador de cliente
                new Thread(new ClientHandler(clientSocket, functionService)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            throw e; // Propaga la excepción para que sea manejada en el main
        }
    }
}
