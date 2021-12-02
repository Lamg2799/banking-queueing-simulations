package com.sim.proj;


/**
 * Class Customer to record waiting and total system time during the simulation
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
public class Customer {

    /**
     * Static id to record last id
     */
    private static int initId = 0;

    /**
     * Customer id
     */
    private int id;

    /**
     * The server number that is processing the service
     */
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
     * Random inter-arrival value
     */
    private double interArrivalValues = 0.0;

    /**
     * Constructor will generate random name
     */
    public Customer() {

        id = initId++;

    }

    // setters

    /**
     * To compute the waiting time based on the current clock when a customer leaves
     * the waiting line
     * 
     * @param clock the current clock
     */
    public void setWaitingTime(Double clock) {
        waitingTime = clock - arrivalTime;

    }

    /**
     * Set server number that is processing the service
     * 
     * @param i the server index
     */
    public void setServerIndex(int i) {
        serverIndex = i;
    }

    /**
     * To compute the waiting and total system time based on the current clock when
     * a customer is leaving the system
     * 
     * @param clock the current clock
     */
    public void setTotalSystemTime(Double clock) {

        totalSystemTime = clock - arrivalTime;

    }

    /**
     * Stores the customer arrival time in the system for the current simulation
     * 
     * @param clock the current clock
     */
    public void setArrivalTime(double clock) {
        this.arrivalTime = clock;

    }

    /**
     * Store the random generated interarrival value
     * 
     * @param ia the interarrival value
     */
    public void setInterArrivalValue(double ia) {
        interArrivalValues = ia;
    }

    // getters
    /**
     * Get inter arrival value for this event
     * 
     * @return the interarrival value for this event
     */
    public double getInterArrivalValues() {
        return interArrivalValues;
    }

    /**
     * Get the server number that is processing the service
     * 
     * @return server number processing the service
     */
    public int getServerIndex() {
        return serverIndex;
    }

    /**
     * Customer ID
     * 
     * @return Customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * The waiting time for this simulation
     * 
     * @return waitingTime
     */
    public double getWaitingTime() {
        return waitingTime;
    }

    /**
     * The total system time for this simulation
     * 
     * @return totalSystemTime
     */
    public double getTotalSystemTime() {
        return totalSystemTime;
    }

    @Override
   /**
    * @return customer string
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