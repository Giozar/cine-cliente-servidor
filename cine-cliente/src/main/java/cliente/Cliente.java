package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ConnectException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cliente {
    private static final String PRIMARY_SERVER_ADDRESS = "localhost";
    private static final int PRIMARY_SERVER_PORT = 1234;
    private static final String SECONDARY_SERVER_ADDRESS = "localhost"; // Cambia esto si el secundario está en otro host
    private static final int SECONDARY_SERVER_PORT = 1235; // Puerto diferente para el servidor secundario
    private static final int MAX_RETRIES = 4; // Número máximo de intentos de reconexión

    private String clientName;
    private int clientAge;

    public static void main(String[] args) {
        new Cliente().start();
    }

    public void start() {
        boolean success = false;
        int retries = 0;
        String currentServer = "primary"; // Comenzamos con el servidor primario

        while (!success && retries < MAX_RETRIES) {
            try {
                // Intentar realizar la reserva
                performReservation(currentServer);
                success = true;
            } catch (IOException e) {
                System.err.println("Se perdió la conexión con el servidor (" + currentServer + "): " + e.getMessage());
                retries++;
                // Alternar entre servidores
                currentServer = currentServer.equals("primary") ? "secondary" : "primary";
                System.err.println("Intentando reconectar al servidor " + currentServer + "...");
            }
        }

        if (!success) {
            System.out.println("No se pudo establecer conexión con ningún servidor. Por favor, intente más tarde.");
        }
    }

    private void performReservation(String server) throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = connectToServer(server);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Solicitar datos del cliente
            clientName = getClientName(scanner);
            clientAge = getClientAge(scanner);

            // Enviar datos del cliente al servidor
            out.println(clientName + "," + clientAge);

            // Leer y mostrar la lista de funciones
            System.out.println("Funciones disponibles:");
            String response;
            while ((response = in.readLine()) != null && !response.equals("END")) {
                System.out.println(response);
            }

            // Verificar si la conexión se perdió
            if (response == null) {
                throw new SocketException("Conexión cerrada por el servidor.");
            }

            // Solicitar al cliente la selección de función
            int functionId = getFunctionId(scanner);
            out.println(functionId);

            // Leer respuesta de validación del servidor
            String functionValidation = in.readLine();
            if (functionValidation == null) {
                throw new SocketException("Conexión cerrada por el servidor.");
            }
            if (!functionValidation.equals("OK")) {
                System.out.println(functionValidation);
                return; // Terminar si la función no es válida
            }

            // Leer el mensaje del servidor solicitando el número de asiento
            String seatPrompt = in.readLine();
            
            if (seatPrompt == null) {
                throw new SocketException("Conexión cerrada por el servidor.");
            }
            

            // Solicitar al cliente el número de asiento
            int seatNumber = getSeatNumber(scanner);
            out.println(seatNumber);

            // Leer respuesta de confirmación del servidor
            String confirmation = in.readLine();
            if (confirmation == null) {
                throw new SocketException("Conexión cerrada por el servidor.");
            }
            System.out.println(confirmation);
            if (confirmation.equals("Asiento reservado con éxito.")) {
                // Leer información del boleto
                String ticketInfo = in.readLine();
                if (ticketInfo == null) {
                    throw new SocketException("Conexión cerrada por el servidor.");
                }
                generateTicket(ticketInfo, functionId, seatNumber);
            }

        } finally {
            // Cerrar recursos
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        }
    }

    private Socket connectToServer(String server) throws IOException {
        Socket socket = null;
        String address;
        int port;

        if (server.equals("primary")) {
            address = PRIMARY_SERVER_ADDRESS;
            port = PRIMARY_SERVER_PORT;
        } else {
            address = SECONDARY_SERVER_ADDRESS;
            port = SECONDARY_SERVER_PORT;
        }

        try {
            // Intentar conectar al servidor especificado
            socket = new Socket(address, port);
            System.out.println("Conectado al servidor " + server + ".");
        } catch (IOException e) {
            System.err.println("Servidor " + server + " no disponible.");
            throw e; // Propagar la excepción para manejarla en el método start()
        }

        return socket;
    }

    private String getClientName(Scanner scanner) {
        String name;
        while (true) {
            System.out.println("Ingrese su nombre (solo letras):");
            name = scanner.nextLine().trim();
            // Validar que el nombre solo contenga letras y espacios
            if (Pattern.matches("[a-zA-Z ]+", name)) {
                break;
            } else {
                System.out.println("El nombre solo debe contener letras y espacios. Inténtelo de nuevo.");
            }
        }
        return name;
    }

    private int getClientAge(Scanner scanner) {
        int age;
        while (true) {
            try {
                System.out.println("Ingrese su edad:");
                age = Integer.parseInt(scanner.nextLine());
                if (age >= 18 && age <= 120) {
                    break;
                } else {
                    System.out.println("Debe ser mayor de edad. Inténtelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido para la edad. Inténtelo de nuevo.");
            }
        }
        return age;
    }

    private int getFunctionId(Scanner scanner) {
        int functionId;
        while (true) {
            try {
                System.out.println("Ingrese el ID de la función que desea reservar:");
                functionId = Integer.parseInt(scanner.nextLine().trim());
                if (functionId > 0) {
                    break;
                } else {
                    System.out.println("El ID de la función debe ser un número positivo. Inténtelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido para el ID de la función. Inténtelo de nuevo.");
            }
        }
        return functionId;
    }

    private int getSeatNumber(Scanner scanner) {
        int seatNumber;
        while (true) {
            try {
                System.out.println("Ingrese el número de asiento (1-100):");
                seatNumber = Integer.parseInt(scanner.nextLine());
                if (seatNumber >= 1 && seatNumber <= 100) {
                    break;
                } else {
                    System.out.println("El número de asiento debe estar entre 1 y 100. Inténtelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido para el asiento. Inténtelo de nuevo.");
            }
        }
        return seatNumber;
    }

    private void generateTicket(String ticketInfo, int functionId, int seatNumber) {
        String ticketId = clientName.replaceAll(" ", "") + "-" + functionId + "-" + seatNumber;
        String ticketContent = "-------------------\n" +
                "BOLETO DE RESERVA\n" +
                "-------------------\n" +
                "ID: " + ticketId + "\n" +
                "Nombre: " + clientName + "\n" +
                ticketInfo + "\n" +
                "Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Crear la carpeta "databases/tickets" si no existe
        File databaseDir = new File("databases" + File.separator + "tickets");
        if (!databaseDir.exists() && !databaseDir.mkdirs()) {
            System.err.println("No se pudo crear el directorio para guardar los boletos.");
            return;
        }

        // Guardar el boleto en la carpeta "databases/tickets"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(databaseDir, ticketId + ".txt")))) {
            writer.write(ticketContent);
            System.out.println("Boleto generado: " + databaseDir.getAbsolutePath() + File.separator + ticketId + ".txt");
        } catch (IOException e) {
            System.err.println("Error al guardar el boleto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
