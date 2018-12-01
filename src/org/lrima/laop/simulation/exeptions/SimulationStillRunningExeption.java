package org.lrima.laop.simulation.exeptions;

public class SimulationStillRunningExeption extends RuntimeException {
    public SimulationStillRunningExeption() { super(); }
    public SimulationStillRunningExeption(String message) { super(message); }
    public SimulationStillRunningExeption(String message, Throwable cause) { super(message, cause); }
    public SimulationStillRunningExeption(Throwable cause) { super(cause); }

}
