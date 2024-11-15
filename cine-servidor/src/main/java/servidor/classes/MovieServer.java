/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servidor.classes;

/**
 *
 * @author giozar
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import servidor.services.FunctionService;

public class MovieServer {
    private static final int PORT = 1234;  // Puerto de escucha del servidor
    private FunctionService functionService;

    public MovieServer(FunctionService functionService) {
        this.functionService = functionService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Movie Server started on port " + PORT);

            // Escuchar constantemente conexiones entrantes
            while (true) {
                Socket clientSocket = serverSocket.accept();  // Acepta conexión de cliente
                System.out.println("New client connected");

                // Crea un nuevo hilo para manejar la conexión del cliente
                new ClientHandler(clientSocket, functionService).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
