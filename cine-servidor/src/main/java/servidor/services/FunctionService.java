package servidor.services;

import servidor.classes.MovieFunction;
import servidor.interfaces.FunctionRepository;
import java.util.List;

public class FunctionService {
    private FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public List<MovieFunction> listAllFunctions() {
        return functionRepository.getAllFunctions();
    }

    public MovieFunction getFunctionById(int id) {
        return functionRepository.findFunctionById(id);
    }

    public boolean reserveSeat(int functionId, int seatNumber, String clientName, int clientAge) {
        System.out.println("entro aquí");
        MovieFunction function = functionRepository.findFunctionById(functionId);
        if (function == null) {
            System.out.println("Función no encontrada.");
            return false;
        }
        System.out.println("llego aquí");
        return functionRepository.reserveSeat(functionId, seatNumber, clientName, clientAge);
    }
    
}
