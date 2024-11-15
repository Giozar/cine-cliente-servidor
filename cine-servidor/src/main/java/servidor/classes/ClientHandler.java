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

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Welcome to the Movie Function System. Enter 'list' to see all functions or a function ID to check availability:");
            String input = in.readLine();

            if ("list".equalsIgnoreCase(input)) {
                for (MovieFunction function : functionService.listAllFunctions()) {
                    out.println(function);
                }
            } else {
                MovieFunction function = functionService.getFunctionById(input);
                if (function != null) {
                    out.println("Function Info: " + function);
                    out.println("Enter seat number to reserve (1-10):");
                    int seatNumber = Integer.parseInt(in.readLine());

                    if (functionService.reserveSeat(input, seatNumber)) {
                        out.println("Seat reserved successfully.");
                    } else {
                        out.println("Seat not available or out of range.");
                    }
                } else {
                    out.println("Function not found.");
                }
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
