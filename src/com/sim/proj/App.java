package com.sim.proj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class for a Java implementation of the Multi server problem as a
 * practice for the CSI4124 Simulation and modelisation Created by:
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
class App {

    private Multiserver multiserver = null;
    private Multiqueue multiqueue = null;

    private ArrayList<HashMap<String, Double>> multiServerResults;
    private ArrayList<HashMap<String, Double>> multiQueueResults;
    // set the number of trial we do
    private final int maxExecution = 6;
    private final int maxQueueSize = 3;
    private final int meanService = 150;
    private final int sigmaService = 80;
    private final double maxClock = 28800;
    private final double meanDivider = 1;
    private final int maxLoop = 500;
    private final int serverStartIndex = 1;

    /**
     * Main static function
     * 
     * @param args
     */
    public static void main(String[] args) {

        new App();

    }

    private App() {

        multiQueueResults = new ArrayList<HashMap<String, Double>>();
        multiServerResults = new ArrayList<HashMap<String, Double>>();
        for (int j = 0; j < 1; j++) {
            runSims();
        }
    }

    private void runSims() {

        var args = new String[6];
        args[0] = String.valueOf(maxLoop);// # num execution should be set to 500
        args[1] = String.valueOf(maxClock); // #max clock
        args[2] = String.valueOf(meanService); // # mean service
        args[3] = String.valueOf(sigmaService); // # sigma service

        args[5] = String.valueOf(meanDivider); // #mean divider

        for (int i = 0; i < maxExecution; i++) {
            multiserver = new Multiserver();
            multiqueue = new Multiqueue();
            args[4] = String.valueOf(i + serverStartIndex); // # number of server
            var sim = multiserver.runSim(args);
            multiServerResults.add(sim);
            // sim=multiqueue.runSim(args); not done yet
            // multiQueueResults.add(sim); not done yet
        }
        /*
         * printMultiServerResults(); printMultiQueueResults();
         */
        // finding optimal parameters based on maxQueueSize
        computeMultiserverOptimal();
        computeMultiqueueOptimal();
    }

    private void computeMultiqueueOptimal() {
    }

    private void computeMultiserverOptimal() {
    }

    private void printMultiServerResults() {
        System.out.println("Printing multiserver results...");
        for (HashMap<String, Double> hashMap : multiServerResults) {
            System.out.println();

            for (String s : hashMap.keySet()) {
                System.out.println(s + ": " + hashMap.get(s));
            }
        }
        System.out.println();

    }

    private void printMultiQueueResults() {
        System.out.println("Printing multiqueue results...");
        for (HashMap<String, Double> hashMap : multiQueueResults) {
            System.out.println();

            for (String s : hashMap.keySet()) {
                System.out.println(s + ": " + hashMap.get(s));
            }
        }
        System.out.println();
    }

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