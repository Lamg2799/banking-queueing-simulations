package com.sim.proj;


import java.util.LinkedList;

/**
 * Class Customer to record waiting and total system time during the simulation
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
public class Customer {

    /**
     * static id to record last id
     */
    private static int initId = 0;

    /**
     * Customer id
     */
    private int id;

    private int serverIndex;

    /**
     * Stores customer arrival time in system
     */
    private double arrivalTime = 0.0;

    /**
     * Waiting time for one simulation,
     */
    private double waitingTime = 0.0;
    /**
     * Total system time for one simulation,
     */
    private double totalSystemTime = 0.0;
    /**
     * Random inter-arrival values for each execution
     */
    private LinkedList<Double> interArrivalValues = null;

    /**
     * Constructor will generate random name
     */
    public Customer() {

        id = initId++;
        interArrivalValues = new LinkedList<Double>();

    }

    // setters

    /**
     * To compute the waiting time based on the current clock when a customer leaves
     * the waiting line
     * 
     * @param clock
     */
    public void setWaitingTime(Double clock) {
        waitingTime = clock - arrivalTime;

    }

    public void setServerIndex(int i) {
        serverIndex = i;
    }

    /**
     * To compute the waiting and total system time based on the current clock when
     * a customer is leaving the system
     * 
     * @param clock
     */
    public void setTotalSystemTime(Double clock) {

        totalSystemTime = clock - arrivalTime;

    }

    /**
     * Stores the customer arrival time in the system for the current simulation
     * 
     * @param currentLoop
     * @param clock
     */
    public void setArrivalTime(double clock) {
        this.arrivalTime = clock;

    }

    public void setInterArrivalValue(Double ia) {
        interArrivalValues.add(ia);
    }

    // getters
    public LinkedList<Double> getInterArrivalValues(Double ia) {
        return interArrivalValues;
    }

    public int getServerIndex() {
        return serverIndex;
    }

    /**
     * Customer ID
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * The average waiting time for all simulations
     * 
     * @return
     */
    public double getWaitingTime() {
        return waitingTime;
    }

    /**
     * The average total system time for all simulations
     * 
     * @return
     */
    public double getTotalSystemTime() {
        return totalSystemTime;
    }

    @Override
    /**
     * String representation of customer
     */
    public String toString() {
        /*
         * totalSystemTime = totalSystemTimeMultiple / numCurrentLoop; waitingTime =
         * waitingTimeMultiple / numCurrentLoop; NumberFormat formatter = new
         * DecimalFormat("#0.00"); String s = "";
         * 
         * s += "Customer id: " + id;
         * 
         * s += "; Served by server #: " + serverIndex;
         * 
         * s += "\nWaiting time: " + formatter.format(waitingTime) + " sec";
         * 
         * s += "; Total System time: " + formatter.format(totalSystemTime) + " sec";
         */
        return "Customer.toString() needs to be reimplemented";
    }

}