/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package servidor.classes;

 /**
  * Clase Movie representa una película en el sistema.
  * Incluye información de la base de datos como id, título, duración, género, calificación, y descripción.
  * 
  * @author giozar
  */
 public class Movie {
     private int id;           // ID único de la película
     private String title;     // Título de la película
     private int duration;     // Duración en minutos
     private String genre;     // Género de la película
     private String rating;    // Clasificación (PG, PG-13, etc.)
     private String description; // Breve descripción
 
     // Constructor
     public Movie(int id, String title, int duration, String genre, String rating, String description) {
         this.id = id;
         this.title = title;
         this.duration = duration;
         this.genre = genre;
         this.rating = rating;
         this.description = description;
     }
 
     // Getters
     public int getId() {
         return id;
     }
 
     public String getTitle() {
         return title;
     }
 
     public int getDuration() {
         return duration;
     }
 
     public String getGenre() {
         return genre;
     }
 
     public String getRating() {
         return rating;
     }
 
     public String getDescription() {
         return description;
     }
 
     // Método toString para mostrar información de la película
     @Override
     public String toString() {
         return "ID: " + id + ", Título: " + title + ", Duración: " + duration + " min, Género: " + genre +
                ", Clasificación: " + rating + ", Descripción: " + description;
     }
 }
 