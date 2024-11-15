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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import servidor.services.FunctionService;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private FunctionService functionService;

    public ClientHandler(Socket clientSocket, FunctionService functionService) {
        this.clientSocket = clientSocket;
        this.functionService = functionService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Recibir nombre y edad del cliente
            String clientInfo = in.readLine();
            String[] clientData = clientInfo.split(",");
            String clientName = clientData[0];
            int clientAge = Integer.parseInt(clientData[1]);

            // Mostrar lista de funciones al cliente
            out.println("Here are the available functions. Enter 'END' when done:");
            for (MovieFunction function : functionService.listAllFunctions()) {
                out.println(function);
            }
            out.println("END");  // Indica al cliente que terminó la lista

            // Recibir selección de función y asiento
            String functionId = in.readLine();
            int seatNumber = Integer.parseInt(in.readLine());

            // Intentar reservar el asiento y enviar confirmación o error
            if (functionService.reserveSeat(functionId, seatNumber)) {
                MovieFunction function = functionService.getFunctionById(functionId);
                out.println("Seat reserved successfully.");

                // Enviar detalles para el boleto al cliente
                String ticketInfo = "Client: " + clientName + ", Age: " + clientAge + "\n" +
                                    "Movie: " + function.getMovie().getName() + "\n" +
                                    "Function ID: " + function.getId() + "\n" +
                                    "DateTime: " + function.getDateTime() + "\n" +
                                    "Room: " + function.getMovie().getRoom() + "\n" +
                                    "Seat: " + seatNumber;
                out.println(ticketInfo);  // Enviar información para generar el boleto
            } else {
                out.println("Failed to reserve seat. It may be unavailable.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
