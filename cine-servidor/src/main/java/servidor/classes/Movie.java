/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package servidor.classes;

 /**
  *
  * @author giozar
  */
 public class Movie {
     private String id;
     private String name;
     private int room;
     private int[] seats;
 
     public Movie(String id, String name, int room) {
         this.id = id;
         this.name = name;
         this.room = room;
         this.seats = new int[10]; // Arreglo de asientos (0 = disponible, 1 = ocupado)
     }
 
     public String getId() { return id; }
     public String getName() { return name; }
     public int getRoom() { return room; }
 
     public boolean reserveSeat(int seatNumber) {
         if (seatNumber < 1 || seatNumber > 10 || seats[seatNumber - 1] == 1) {
             return false; // Asiento no disponible
         }
         seats[seatNumber - 1] = 1;
         return true; // Asiento reservado
     }
 
     public boolean hasAvailableSeats() {
         for (int seat : seats) {
             if (seat == 0) return true;
         }
         return false;
     }
 
     @Override
     public String toString() {
         return "ID: " + id + ", Nombre: " + name + ", Sala: " + room + ", Disponibilidad: " + (hasAvailableSeats() ? "Sí" : "No");
     }
 }
 