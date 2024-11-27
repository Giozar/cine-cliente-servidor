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
      private int id;
      private LocalDateTime dateTime;
      private Movie movie;
  
      public MovieFunction(int id, LocalDateTime dateTime, Movie movie) {
          this.id = id;
          this.dateTime = dateTime;
          this.movie = movie;
      }
  
      // Getters
      public int getId() {
          return id;
      }
  
      public LocalDateTime getDateTime() {
          return dateTime;
      }
  
      public Movie getMovie() {
          return movie;
      }
  
      @Override
      public String toString() {
          return "Function ID: " + id + ", DateTime: " + dateTime +
                 ", Movie: [" + movie.toString() + "]";
      }
  }
  