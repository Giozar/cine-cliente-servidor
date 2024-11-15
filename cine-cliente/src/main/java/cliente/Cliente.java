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
              System.out.println("Enter your name:");
              clientName = scanner.nextLine();
              System.out.println("Enter your age:");
              clientAge = Integer.parseInt(scanner.nextLine());
  
              // Enviar datos del cliente al servidor
              out.println(clientName + "," + clientAge);
  
              // Leer y mostrar la lista de funciones
              System.out.println("Available functions:");
              String response;
              while (!(response = in.readLine()).equals("END")) {
                  System.out.println(response);
              }
  
              // Solicitar al cliente la selección de función y asiento
              System.out.println("Enter the ID of the function you want to book:");
              String functionId = scanner.nextLine();
              out.println(functionId);
  
              System.out.println("Enter seat number (1-10):");
              int seatNumber = Integer.parseInt(scanner.nextLine());
              out.println(seatNumber);
  
              // Recibir confirmación de la reserva y generar boleto
              String confirmation = in.readLine();
              if (confirmation.equals("Seat reserved successfully.")) {
                  System.out.println("Booking confirmed!");
  
                  // Recibir información del boleto y generarlo
                  String ticketInfo = in.readLine();
                  generateTicket(ticketInfo, functionId, seatNumber);
              } else {
                  System.out.println(confirmation);
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  
      private void generateTicket(String ticketInfo, String functionId, int seatNumber) {
          String ticketId = clientName + "-" + functionId + "-" + seatNumber;
          String ticketContent = "TICKET\n" +
                                 "ID: " + ticketId + "\n" +
                                 "Name: " + clientName + "\n" +
                                 ticketInfo + "\n" +
                                 "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  
          // Crear la carpeta "database" si no existe
          File databaseDir = new File("database");
          if (!databaseDir.exists()) {
              databaseDir.mkdir();
          }
  
          // Guardar el boleto en la carpeta "database"
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(databaseDir, ticketId + ".txt")))) {
              writer.write(ticketContent);
              System.out.println("Ticket generated: database/" + ticketId + ".txt");
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
  