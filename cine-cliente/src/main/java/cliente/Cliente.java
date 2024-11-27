/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 package cliente;

 /**
  *
  * @author giozar
  */
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
             System.out.println("Ingrese su nombre:");
             clientName = getClientName(scanner);
             System.out.println("Ingrese su edad:");
             clientAge = getClientAge(scanner);
             //clientAge = Integer.parseInt(scanner.nextLine());
 
             // Enviar datos del cliente al servidor
             out.println(clientName + "," + clientAge);
 
             // Leer y mostrar la lista de funciones
             System.out.println("Funciones disponibles:");
             String response;
             while (!(response = in.readLine()).equals("END")) {
                 System.out.println(response);
             }
 
             // Solicitar al cliente la selección de función y asiento
             System.out.println("Ingrese el ID de la función que desea reservar:");
             String functionId = scanner.nextLine();
             out.println(functionId);
 
             System.out.println("Ingrese el número de asiento (1-10):");
             int seatNumber = Integer.parseInt(scanner.nextLine());
             out.println(seatNumber);
 
             // Recibir confirmación de la reserva y generar boleto
             String confirmation = in.readLine();
             if (confirmation.equals("Asiento reservado con éxito.")) {
                 System.out.println("¡Reserva confirmada!");
 
                 // Recibir información del boleto y generarlo
                 String ticketInfo = in.readLine();
                 generateTicket(ticketInfo, functionId, seatNumber);
             } else {
                 System.out.println(confirmation);
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
            name = scanner.nextLine();
            // Validar que el nombre solo contenga letras
            if (Pattern.matches("[a-zA-Z]+", name)) { // Uso de expresion Regular
                break;
            }
        }
        return name;
    }
     
     private int getClientAge(Scanner scanner) {
        int age;
        while (true) {
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age >= 18 && age <= 66) {
                    break;
                } else {
                    System.out.println(" Debe ser mayor de edad.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un número válido.");
            }
        }
        return age;
    }
 
     private void generateTicket(String ticketInfo, String functionId, int seatNumber) {
         String ticketId = clientName + "-" + functionId + "-" + seatNumber;
         String ticketContent = "BOLETO\n" +
                                "ID: " + ticketId + "\n" +
                                "Nombre: " + clientName + "\n" +
                                ticketInfo + "\n" +
                                "Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
 
        File databaseDir = new File("databases" + File.separator + "tickets");
        if (!databaseDir.exists() && !databaseDir.mkdirs()) {
            System.err.println("No se pudo crear el directorio para guardar los boletos.");
            return;
        }
 
         // Guardar el boleto en la carpeta "database"
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(databaseDir, ticketId + ".txt")))) {
             writer.write(ticketContent);
              System.out.println("Boleto generado: " + databaseDir.getAbsolutePath() + File.separator + ticketId + ".txt");
             //System.out.println("Boleto generado: databases/tickets/" + ticketId + ".txt");
         } catch (IOException e) {
             System.err.println("Error al guardar el boleto: " + e.getMessage());
             e.printStackTrace();
         }
     }
 }
 