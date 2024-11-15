/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license/default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package servidor.classes;

 /**
  *
  * @autor giozar
  */
 import java.time.LocalDateTime;
 
 public class MovieFunction {
     private String id;
     private LocalDateTime dateTime;
     private Movie movie;
     private int[] seats;
 
     public MovieFunction(String id, LocalDateTime dateTime, Movie movie) {
         this.id = id;
         this.dateTime = dateTime;
         this.movie = movie;
         this.seats = new int[10]; // Array para los asientos (0 = disponible, 1 = ocupado)
     }
 
     public String getId() { return id; }
     public LocalDateTime getDateTime() { return dateTime; }
     public Movie getMovie() { return movie; }
 
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
         return "ID de Función: " + id + ", Fecha y Hora: " + dateTime +
                ", Película: " + movie.getName() +
                ", Sala: " + movie.getRoom() +
                ", Disponibilidad: " + (hasAvailableSeats() ? "Sí" : "No");
     }
 }
 