package com.sim.proj;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * used to store the outputs results for each execution
 */
public class Results {

    private HashMap<String, Double> results;
   
    private HashMap<String, LinkedList<Double>> resultsIaTime;
    private LinkedList<Double> waitingTime;
    private LinkedList<Double> systemTime;
    private LinkedList<Double> waitingTimeAvgVar;
    private LinkedList<Double> systemTimeAvgVar;

    public Results() {
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
     * @param key
     * @param value
     */
    public void addResult(String key, Double value) {
        results.put(key, value);
    }

    /**
     * to store inter-arrival random generated values ( for possible reference in
     * the report)
     * 
     * @param key
     * @param value
     */
    public void addIaTimeResult(String key, LinkedList<Double> value) {
        resultsIaTime.put(key, value);
    }

    /**
     * to store avg waiting time at each execution for all customers
     * 
     * @param value
     */
    public void addWaitingTime(double value) {
        waitingTime.add(value);
    }

    /**
     * to store avg waiting time variance at each execution for all customers
     * 
     * @param value
     */
    public void addWaitingTimeAvgVar(double value) {
        waitingTimeAvgVar.add(value);
    }

    /**
     * to store avg system time at each execution for all customers
     * 
     * @param value
     */
    public void addSystemTime(double value) {
        systemTime.add(value);
    }

    /**
     * to store avg system time variance at each execution for all customers
     * 
     * @param value
     */
    public void addSystemTimeAvgVar(double value) {
        systemTimeAvgVar.add(value);
    }

    public HashMap<String, Double> getResults() {
        return results;
    }

 

    public HashMap<String, LinkedList<Double>> getResultsIaTime() {
        return resultsIaTime;
    }

    public LinkedList<Double> getWaitingTime() {
        return waitingTime;
    }

    public LinkedList<Double> getSystemTime() {
        return systemTime;
    }

    public LinkedList<Double> getWaitingTimeAvgVar() {
        return waitingTimeAvgVar;
    }

    public LinkedList<Double> getSystemTimeAvgVar() {
        return systemTimeAvgVar;
    }

}
