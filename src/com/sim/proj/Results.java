package com.sim.proj;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * used to store the outputs results for each execution
 */
public class Results {

    /**
     * Hashmap containing all results
     */
    private HashMap<String, Double> results;
    /**
     * Hashmap containing all generated poisson random values
     */
    private HashMap<String, LinkedList<Double>> resultsIaTime;
    /**
     * List containing the average waiting time for each execution
     */
    private LinkedList<Double> waitingTime;
    /**
     * List containing the average system time for each execution
     */
    private LinkedList<Double> systemTime;
    /**
     * List containing the average waiting variance time for each execution
     */
    private LinkedList<Double> waitingTimeAvgVar;
    /**
     * List containing the average system time variance for each execution
     */
    private LinkedList<Double> systemTimeAvgVar;
    /**
     * name of the results
     */
    private String name;

    /**
     * Constructor
     */
    public Results(String name) {
        this.name = name;
        results = new HashMap<>();
        resultsIaTime = new HashMap<>();
        waitingTime = new LinkedList<>();
        systemTime = new LinkedList<>();
        waitingTimeAvgVar = new LinkedList<>();
        systemTimeAvgVar = new LinkedList<>();
    }

    /**
     * to store results with type double
     * 
     * @param key   the result key index
     * @param value the result value
     */
    public void addResult(String key, Double value) {
        results.put(key, value);
    }

    /**
     * to store inter-arrival random generated values ( for possible reference in
     * the report)
     * 
     * @param key   the result key index
     * @param value the list of random generated values
     */
    public void addIaTimeResult(String key, LinkedList<Double> value) {
        resultsIaTime.put(key, value);
    }

    /**
     * to store avg waiting time at each execution for all customers
     * 
     * @param value the average waiting time for this execution
     */
    public void addWaitingTime(double value) {
        waitingTime.add(value);
    }

    /**
     * to store avg waiting time variance at each execution for all customers
     * 
     * @param value the average waiting time variance for this execution
     */
    public void addWaitingTimeAvgVar(double value) {
        waitingTimeAvgVar.add(value);
    }

    /**
     * to store avg system time at each execution for all customers
     * 
     * @param value the average system time for this execution
     */
    public void addSystemTime(double value) {
        systemTime.add(value);
    }

    /**
     * to store avg system time variance at each execution for all customers
     * 
     * @param value the average system time variance for this execution
     */
    public void addSystemTimeAvgVar(double value) {
        systemTimeAvgVar.add(value);
    }

    /**
     * Get results object name
     * 
     * @return name of results
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hashmap containing all the results
     * 
     * @return results
     */
    public HashMap<String, Double> getResults() {
        return results;
    }

    /**
     * Returns the hashmap containing all ramdomly generated interarrival values
     * 
     * @return resultsIaTime
     */
    public HashMap<String, LinkedList<Double>> getResultsIaTime() {
        return resultsIaTime;
    }

    /**
     * Returns the waiting time average for each execution
     * 
     * @return waitingTime
     */
    public LinkedList<Double> getWaitingTime() {
        return waitingTime;
    }

    /**
     * Returns the system time average for each execution
     * 
     * @return systemTime
     */
    public LinkedList<Double> getSystemTime() {
        return systemTime;
    }

    /**
     * Returns the waiting time average variance for each execution
     * 
     * @return waitingTimeAvgVar
     */
    public LinkedList<Double> getWaitingTimeAvgVar() {
        return waitingTimeAvgVar;
    }

    /**
     * Returns the system time average variancee for each execution
     * 
     * @return systemTimeAvgVar
     */
    public LinkedList<Double> getSystemTimeAvgVar() {
        return systemTimeAvgVar;
    }

}
