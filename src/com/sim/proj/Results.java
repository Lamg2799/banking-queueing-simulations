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
    private LinkedList<LinkedList<Double>> resultsIaTime;
    /**
     * List containing the average waiting time for each execution
     */
    private LinkedList<Double> waitingTimeAvg;
    /**
     * List containing the average system time for each execution
     */
    private LinkedList<Double> systemTimeAvg;
    /**
     * List containing the average waiting time variance for each execution
     */
    private LinkedList<Double> waitingTimeAvgAvgVar;
    /**
     * List containing the average system time variance for each execution
     */
    private LinkedList<Double> systemTimeAvgAvgVar;
    /**
     * List containing the average interarrival time for each execution
     */
    private LinkedList<Double> iaAvg;
    /**
     * List containing the average interarrival time variance for each execution
     */
    private LinkedList<Double> iaAvgVar;
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
        resultsIaTime = new LinkedList<>();
        waitingTimeAvg = new LinkedList<>();
        systemTimeAvg = new LinkedList<>();
        waitingTimeAvgAvgVar = new LinkedList<>();
        systemTimeAvgAvgVar = new LinkedList<>();
        iaAvg = new LinkedList<>();
        iaAvgVar = new LinkedList<>();
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
     * *
     * 
     * @param value the list of random generated values
     */
    public void addIaTimeResult(LinkedList<Double> value) {
        resultsIaTime.add(value);
    }

    /**
     * to store inter-arrival random generated values ( for possible reference in
     * the report)
     * *
     * 
     * @param value the list of random generated values
     */
    public void addIaTimeAvg(double value) {
        iaAvg.add(value);
    }

    /**
     * to store inter-arrival random generated values ( for possible reference in
     * the report)
     * *
     * 
     * @param value the list of random generated values
     */
    public void addIaTimeAvgVar(double value) {
        iaAvgVar.add(value);
    }

    /**
     * to store avg waiting time at each execution for all customers
     * 
     * @param value the average waiting time for this execution
     */
    public void addWaitingTimeAvg(double value) {
        waitingTimeAvg.add(value);
    }

    /**
     * to store avg waiting time variance at each execution for all customers
     * 
     * @param value the average waiting time variance for this execution
     */
    public void addWaitingTimeAvgVar(double value) {
        waitingTimeAvgAvgVar.add(value);
    }

    /**
     * to store avg system time at each execution for all customers
     * 
     * @param value the average system time for this execution
     */
    public void addSystemTimeAvg(double value) {
        systemTimeAvg.add(value);
    }

    /**
     * to store avg system time variance at each execution for all customers
     * 
     * @param value the average system time variance for this execution
     */
    public void addSystemTimeAvgVar(double value) {
        systemTimeAvgAvgVar.add(value);
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
    public LinkedList<LinkedList<Double>> getResultsIaTime() {
        return resultsIaTime;
    }

    /**
     * Returns the hashmap containing all ramdomly generated interarrival values
     * 
     * @return resultsIaTimeAvg
     */
    public LinkedList<Double> getResultsIaTimeAvg() {
        return iaAvg;
    }

    /**
     * Returns the hashmap containing all ramdomly generated interarrival values
     * 
     * @return resultsIaTimeAvgVar
     */
    public LinkedList<Double> getResultsIaTimeAvgVar() {
        return iaAvgVar;
    }

    /**
     * Returns the waiting time average for each execution
     * 
     * @return waitingTimeAvg
     */
    public LinkedList<Double> getWaitingTimeAvg() {
        return waitingTimeAvg;
    }

    /**
     * Returns the system time average for each execution
     * 
     * @return systemTimeAvg
     */
    public LinkedList<Double> getSystemTimeAvg() {
        return systemTimeAvg;
    }

    /**
     * Returns the waiting time average variance for each execution
     * 
     * @return waitingTimeAvgAvgVar
     */
    public LinkedList<Double> getWaitingTimeAvgVar() {
        return waitingTimeAvgAvgVar;
    }

    /**
     * Returns the system time average variancee for each execution
     * 
     * @return systemTimeAvgAvgVar
     */
    public LinkedList<Double> getSystemTimeAvgVar() {
        return systemTimeAvgAvgVar;
    }

}
