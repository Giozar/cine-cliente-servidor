package servidor.classes;

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

            // Validar edad del cliente
            if (clientAge < 18) {
                out.println("Lo sentimos, debe ser mayor de edad para realizar reservas.");
                return;
            }

            // Mostrar lista de funciones al cliente
            out.println("Aquí están las funciones disponibles. Escriba 'END' cuando haya terminado:");
            functionService.listAllFunctions().forEach(function -> out.println(function));
            out.println("END"); // Indica al cliente que terminó la lista

            // Recibir selección de función
            out.println("Ingrese el ID de la función que desea reservar:");
            int functionId = Integer.parseInt(in.readLine());

            // Validar si la función existe
            MovieFunction function = functionService.getFunctionById(functionId);
            if (function == null) {
                out.println("Función no encontrada. Verifique el ID e intente de nuevo.");
                return;
            }

            // Recibir número de asiento
            out.println("Ingrese el número de asiento que desea reservar (1-10):");
            int seatNumber = Integer.parseInt(in.readLine());

            // Intentar reservar el asiento
            if (functionService.reserveSeat(functionId, seatNumber)) {
                out.println("Asiento reservado con éxito.");

                // Generar detalles del boleto
                String ticketInfo = "-------------------\n" +
                                    "BOLETO DE RESERVA\n" +
                                    "-------------------\n" +
                                    "Cliente: " + clientName + "\n" +
                                    "Edad: " + clientAge + "\n" +
                                    "Película: " + function.getMovie().getTitle() + "\n" +
                                    "ID de Función: " + function.getId() + "\n" +
                                    "Fecha y Hora: " + function.getDateTime() + "\n" +
                                    "Sala: " + function.getMovie().getId() + "\n" +
                                    "Asiento: " + seatNumber + "\n" +
                                    "-------------------";
                out.println(ticketInfo);
            } else {
                out.println("No se pudo reservar el asiento. Puede que ya esté ocupado.");
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}
