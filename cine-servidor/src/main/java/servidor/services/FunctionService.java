/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servidor.services;

/**
 *
 * @author giozar
 */
import java.util.List;

import servidor.classes.MovieFunction;
import servidor.interfaces.FunctionRepository;

public class FunctionService {
    private FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public List<MovieFunction> listAllFunctions() {
        return functionRepository.getAllFunctions();
    }

    public MovieFunction getFunctionById(String id) {
        return functionRepository.findFunctionById(id);
    }

    public boolean reserveSeat(String functionId, int seatNumber) {
        MovieFunction function = functionRepository.findFunctionById(functionId);
        return function != null && function.reserveSeat(seatNumber);
    }
}
