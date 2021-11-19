package com.sim.proj;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
    private double arrivalTime = 0;

    /**
     * Waiting time for one simulation, averaged from all simulations ran
     */
    private double waitingTime = 0;
    /**
     * Total system time for one simulation, averaged from all simulations ran
     */
    private double totalSystemTime = 0;

    /**
     * Total Waiting time for all simulations
     */
    private double waitingTimeMultiple = 0;

    /**
     * Total System time for all simulations
     */
    private double totalSystemTimeMultiple = 0;

    /**
     * Name of customer
     */
    private String name = "";

    /**
     * Stores the current number of simulation executed to compute average waiting
     * and system time
     */
    private static int numCurrentLoop = 0;

    /**
     * Constructor will generate random name
     */
    public Customer() {

        id = initId++;

    }

    /**
     * Constructor will set name from argument
     * 
     * @param name
     */
    public Customer(String name) {

        this.name = name;
        id = initId++;

    }

    // setters

    /**
     * To compute the waiting time based on the current clock when a customer leaves
     * the waiting line
     * 
     * @param clock
     */
    public void setWaitingTime(double clock) {
        waitingTime = clock - arrivalTime;
        waitingTimeMultiple += waitingTime;
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
    public void setTotalSystemTime(double clock) {
        totalSystemTime = clock - arrivalTime;
        totalSystemTimeMultiple += totalSystemTime;

    }

    /**
     * Stores the customer arrival time in the system for the current simulation
     * 
     * @param currentLoop
     * @param clock
     */
    public void setArrivalTime(int currentLoop, double clock) {
        this.arrivalTime = clock;
        numCurrentLoop = currentLoop;
    }

    // getters

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
     * Customer name
     * 
     * @return
     */
    public String getName() {
        return name;
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
        totalSystemTime = totalSystemTimeMultiple / numCurrentLoop;
        waitingTime = waitingTimeMultiple / numCurrentLoop;
        NumberFormat formatter = new DecimalFormat("#0.00");
        String s = "";

        s += "Customer id: " + id;

        s += "; Served by server #: " + serverIndex;

        s += "\nWaiting time: " + formatter.format(waitingTime) + " sec";

        s += "; Total System time: " + formatter.format(totalSystemTime) + " sec";

        return s;
    }

}