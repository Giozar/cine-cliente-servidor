package servidor.classes;

import servidor.interfaces.FunctionRepository;
import servidor.interfaces.MovieRepository;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FunctionRepositoryTxt implements FunctionRepository {
    private List<MovieFunction> functions;
    private MovieRepository movieRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Map<Integer, Set<Integer>> reservations; // Mapa para almacenar las reservas
    private String reservationsFilePath = "databases/reservations.txt"; // Ruta del archivo de reservas

    public FunctionRepositoryTxt(String filePath, MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.functions = new ArrayList<>();
        this.reservations = new HashMap<>();
        loadFunctions(filePath);
        loadReservations(reservationsFilePath);
    }

    private void loadFunctions(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Suponiendo que el formato es: functionId,movieId,function_datetime
                String[] data = line.split(",");
                if (data.length >= 3) {
                    int functionId = Integer.parseInt(data[0]);
                    int movieId = Integer.parseInt(data[1]);
                    LocalDateTime dateTime = LocalDateTime.parse(data[2], formatter);

                    // Buscar la película en el repositorio de películas
                    Movie movie = movieRepository.findMovieById(movieId);
                    if (movie != null) {
                        functions.add(new MovieFunction(functionId, dateTime, movie));
                    } else {
                        System.err.println("Película no encontrada con ID: " + movieId);
                    }
                } else {
                    System.err.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            System.err.println("Error al parsear datos: " + e.getMessage());
        }
    }

    private void loadReservations(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            // Si el archivo no existe, no hay reservas previas
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Formato: functionId,seatNumber,clientName,clientAge
                String[] data = line.split(",");
                if (data.length >= 4) {
                    int functionId = Integer.parseInt(data[0]);
                    int seatNumber = Integer.parseInt(data[1]);
                    String clientName = data[2];
                    int clientAge = Integer.parseInt(data[3]);

                    // Agregar el asiento reservado al mapa
                    reservations.computeIfAbsent(functionId, k -> new HashSet<>()).add(seatNumber);
                } else {
                    System.err.println("Formato incorrecto en la línea de reserva: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear datos de reserva: " + e.getMessage());
        }
    }

    private void saveReservation(int functionId, int seatNumber, String clientName, int clientAge) {
        // Guardar la reserva en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationsFilePath, true))) {
            String line = functionId + "," + seatNumber + "," + clientName + "," + clientAge;
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar la reserva: " + e.getMessage());
        }
    }

    @Override
    public List<MovieFunction> getAllFunctions() {
        return functions;
    }

    @Override
    public MovieFunction findFunctionById(int id) {
        Optional<MovieFunction> function = functions.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
        return function.orElse(null);
    }

    @Override
    public boolean reserveSeat(int functionId, int seatNumber, String clientName, int clientAge) {
        // Verificar si el asiento ya está reservado
        Set<Integer> reservedSeats = reservations.computeIfAbsent(functionId, k -> new HashSet<>());
        if (reservedSeats.contains(seatNumber)) {
            return false; // El asiento ya está reservado
        }

        // Reservar el asiento
        reservedSeats.add(seatNumber);

        // Guardar la reserva en el archivo
        saveReservation(functionId, seatNumber, clientName, clientAge);

        return true;
    }
}
