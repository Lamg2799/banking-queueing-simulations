package com.sim.proj;



/**
 * Main class for a Java implementation of the Multi server problem as a
 * practice for the CSI4124 Simulation and modelisation Created by:
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
class App {

    Multiserver multiserver = null;

    /**
     * Main static function
     * 
     * @param args
     */
    public static void main(String[] args) {

        new App(args);
       

    }

    private App(String[] args){
        multiserver = new Multiserver(args);
    }
    /**
     * Constructor
     */

   

}

/**
 * Enum for server status either IDLE or BUSY
 */
enum State {

    IDLE, BUSY;

};

/**
 * Enum for event type either ARRIVAL or DEPARTURE
 */
enum EventType {

    ARRIVAL, DEPARTURE
};