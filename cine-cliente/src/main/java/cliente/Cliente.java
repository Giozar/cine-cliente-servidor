package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cliente {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private String clientName;
    private int clientAge;

    public static void main(String[] args) {
        new Cliente().start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)) {

            // Solicitar datos del cliente
            clientName = getClientName(scanner);
            clientAge = getClientAge(scanner);

            // Enviar datos del cliente al servidor
            out.println(clientName + "," + clientAge);

            // Leer y mostrar la lista de funciones
            System.out.println("Funciones disponibles:");
            String response;
            while (!(response = in.readLine()).equals("END")) {
                System.out.println(response);
            }

            // Solicitar al cliente la selección de función
            int functionId = getFunctionId(scanner);
            out.println(functionId);

            // Leer respuesta de validación del servidor
            String functionValidation = in.readLine();
            if (!functionValidation.equals("OK")) {
                System.out.println(functionValidation);
                return; // Terminar si la función no es válida
            }

            // Leer el mensaje del servidor solicitando el número de asiento
            in.readLine();


            // Solicitar al cliente el número de asiento
            int seatNumber = getSeatNumber(scanner);
            out.println(seatNumber);

            // Leer respuesta de confirmación del servidor
            String confirmation = in.readLine();
            System.out.println(confirmation);
            if (confirmation.equals("Asiento reservado con éxito.")) {
                // Leer información del boleto
                String ticketInfo = in.readLine();
                generateTicket(ticketInfo, functionId, seatNumber);
            }

        } catch (IOException e) {
            System.err.println("Error al conectarse al servidor: " + e.getMessage());
            e.printStackTrace();
        }
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
            System.out
                    .println("Boleto generado: " + databaseDir.getAbsolutePath() + File.separator + ticketId + ".txt");
        } catch (IOException e) {
            System.err.println("Error al guardar el boleto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
