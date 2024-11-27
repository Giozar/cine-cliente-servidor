package servidor.interfaces;

import servidor.classes.MovieFunction;
import java.util.List;

public interface FunctionRepository {
    List<MovieFunction> getAllFunctions();
    MovieFunction findFunctionById(int id);
    // Agregar los nuevos parámetros al método
    boolean reserveSeat(int functionId, int seatNumber, String clientName, int clientAge);

}
