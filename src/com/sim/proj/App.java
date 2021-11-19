package com.sim.proj;

/**
 * Main class for a Java implementation of the Multi server problem as a
 * practice for the CSI4124 Simulation and modelisation Created by:
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
class App {

    private Multiserver multiserver = null;
    private Output output = null;

    /**
     * Main static function
     * 
     * @param args
     */
    public static void main(String[] args) {

        if(args.length==0){args = new String[6];}
        args[0] = String.valueOf(1);//     # num execution should be set to 500
        args[1] = String.valueOf(28800); //#max clock
        args[2] = String.valueOf(150);   //# mean service
        args[3] = String.valueOf(80); //# sigma service
        args[4] = String.valueOf(3); //# number of server
        args[5] = String.valueOf(2.0); //#mean divider
       

        new App(args);

    }

    private App(String[] args) {

        output = Output.getOutputInstance();
        multiserver = new Multiserver(args);
        runSims();
    }

    private void runSims() {



        multiserver.runSim();
        output.printMultiServerResults();

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