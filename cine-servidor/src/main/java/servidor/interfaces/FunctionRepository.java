package servidor.interfaces;

import servidor.classes.MovieFunction;
import java.util.List;

public interface FunctionRepository {
    List<MovieFunction> getAllFunctions();
    MovieFunction findFunctionById(int id);
    boolean reserveSeat(int functionId, int seatNumber); // Método para reservar asientos
}
