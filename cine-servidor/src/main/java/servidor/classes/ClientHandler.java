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
    
            // Recibir el nombre y la edad del cliente
            String clientInfo = in.readLine();
            String[] clientData = clientInfo.split(",");
            String clientName = clientData[0];
            int clientAge = Integer.parseInt(clientData[1]);
    
            // Enviar las funciones disponibles al cliente
            out.println("Aquí están las funciones disponibles. Escriba 'END' cuando haya terminado:");
            functionService.listAllFunctions().forEach(function -> out.println(function));
            out.println("END");
    
            // Leer el ID de la función seleccionada
            String functionIdInput = in.readLine();
    
            if (functionIdInput == null || functionIdInput.isEmpty()) {
                out.println("Error: El ID de la función no puede estar vacío.");
                return;
            }
    
            int functionId;
            try {
                functionId = Integer.parseInt(functionIdInput);
            } catch (NumberFormatException e) {
                out.println("Error: El ID de la función debe ser un número válido.");
                return;
            }
    
            // Verificar si la función existe
            MovieFunction function = functionService.getFunctionById(functionId);
            if (function == null) {
                out.println("Error: No se encontró ninguna función con el ID proporcionado.");
                return;
            }
    
            // Confirmar que la función es válida
            out.println("OK");
    
            // Solicitar el número de asiento
            out.println("Ingrese el número de asiento (1-100):");
            String seatNumberInput = in.readLine();
    
            if (seatNumberInput == null || seatNumberInput.isEmpty()) {
                out.println("Error: El número de asiento no puede estar vacío.");
                return;
            }
    
            int seatNumber;
            try {
                seatNumber = Integer.parseInt(seatNumberInput);
            } catch (NumberFormatException e) {
                out.println("Error: El número de asiento debe ser un número válido.");
                return;
            }
    
            // Intentar reservar el asiento
            boolean seatReserved = functionService.reserveSeat(functionId, seatNumber, clientName, clientAge);
            if (seatReserved) {
                out.println("Asiento reservado con éxito.");
                // Enviar detalles del boleto
                String ticketInfo = "Cliente: " + clientName + ", Edad: " + clientAge + "\n" +
                                    "Función: " + function + "\n" +
                                    "Asiento: " + seatNumber;
                out.println(ticketInfo);
            } else {
                out.println("El asiento no está disponible.");
            }
    
        } catch (IOException e) {
            System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
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
