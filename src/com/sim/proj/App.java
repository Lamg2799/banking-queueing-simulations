package com.sim.proj;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";

    private Multiserver multiserver = null;
    private Multiqueue multiqueue = null;
    // data structure to store result
    private ArrayList<HashMap<String, Double>> multiServerResults;
    private ArrayList<HashMap<String, Double>> multiQueueResults;

    private int maxExecution = 5; // default value
    private int maxQueueSize = 2; // default value
    private double meanDivider = 1; // default value
    private final int meanService = 150; // constant
    private final int sigmaService = 80; // constant
    private final double maxClock = 28800; // constant
    private final int maxLoop = 500; // constant
    private final int serverStartIndex = 1; // constant
    private String[] arguments;
    private NumberFormat formatter = new DecimalFormat("#0.00");

    /**
     * Main static function 3 arguments [0]= max queue size; [1] = mean divider ;
     * [2] maxExecution trials will work with default value 2,1,5
     * 
     * @param args
     */
    public static void main(String[] args) {

        new App(args);

    }

    // constructor
    private App(String[] args) {
        arguments = args;
        multiQueueResults = new ArrayList<HashMap<String, Double>>();
        multiServerResults = new ArrayList<HashMap<String, Double>>();
        // run simulations
        runSims();

    }

    private void runSims() {

        var args = new String[6];
        // check if arguments are received or else use default values
        if (arguments != null && arguments.length == 3) {
            maxQueueSize = Integer.parseInt(arguments[0]);
            meanDivider = Double.parseDouble(arguments[1]);
            maxExecution = Integer.parseInt(arguments[2]);
        }
        args[0] = String.valueOf(maxLoop);// # num execution should be set to 500
        args[1] = String.valueOf(maxClock); // #max clock
        args[2] = String.valueOf(meanService); // # mean service
        args[3] = String.valueOf(sigmaService); // # sigma service

        args[5] = String.valueOf(meanDivider); // #mean divider

        // loops through both kind of simulation and trying different number of servers
        for (int i = 0; i < maxExecution; i++) {
            multiserver = new Multiserver();
            multiqueue = new Multiqueue();
            args[4] = String.valueOf(i + serverStartIndex); // # number of server
            var sim = multiserver.runSim(args);
            if (sim != null) {
                multiServerResults.add(sim);
            }
            sim = multiqueue.runSim(args);
            if (sim != null) {
                multiQueueResults.add(sim);
            }
        }

        // printing results for all trials
        printMultiServerResults();
        printMultiQueueResults();

        // finding optimal parameters based on maxQueueSize
        computeMultiserverOptimal();
        computeMultiqueueOptimal();
    }

    private void computeMultiqueueOptimal() {

        // find lowest cost within restriction
        System.out.println(TEXT_GREEN+"Finding optimal result for multiqueue..."+TEXT_RESET);
        double mincost = Double.MAX_VALUE;
        double minserver = 0;
        double cost = 0;
        if (multiQueueResults.size() > 0) {
            for (HashMap<String, Double> hashMap : multiQueueResults) {
                if (hashMap.size() > 0) {
                    var h = hashMap.get("costPerCustomer");
                    var qsize = hashMap.get("maxQueue");
                    if (h < mincost && qsize <= maxQueueSize) {
                        mincost = h;
                        minserver = hashMap.get("numServers");
                        cost = hashMap.get("totalCost");
                    }
                }

            }
            if (cost > 0) {
                System.out.println("The optimal number of server with a mean divider of " + meanDivider
                        + " and a maximum queue size of " + maxQueueSize + " for the multiqueue system is "
                        + (int) minserver + " with a total cost of " + formatter.format(cost)
                        + "$ and a cost per customer of " + formatter.format(mincost) + "$");
            }
        }
    }

    private void computeMultiserverOptimal() {

        // find lowest cost within restriction
        System.out.println(TEXT_GREEN+"Finding optimal result for multiserver..."+TEXT_RESET);
        double mincost = Double.MAX_VALUE;
        double minserver = 0;
        double cost = 0;
        if (multiServerResults.size() > 0) {
            for (HashMap<String, Double> hashMap : multiServerResults) {
                if (hashMap.size() > 0) {
                    var h = hashMap.get("costPerCustomer");
                    var qsize = hashMap.get("maxQueue");
                    if (h < mincost && qsize <= maxQueueSize) {
                        mincost = h;
                        minserver = hashMap.get("numServers");
                        cost = hashMap.get("totalCost");
                    }
                }
            }
            if (cost > 0) {
                System.out.println("The optimal number of server with a mean divider of " + meanDivider
                        + " and a maximum queue size of " + maxQueueSize + " for the multiserver system is "
                        + (int) minserver + " with a total cost of " + formatter.format(cost)
                        + "$ and a cost per customer of " + formatter.format(mincost) + "$");
            }
        }
    }

    private void printMultiServerResults() {
        System.out.println();
        System.out.println(TEXT_GREEN+"Printing multiserver results..."+TEXT_RESET);
        if (multiServerResults.size() > 0) {
            for (HashMap<String, Double> hashMap : multiServerResults) {
                System.out.println();
                if (hashMap.size() > 0) {
                    printReport(hashMap);
                }
            }
        } 
        System.out.println();

    }

    private void printMultiQueueResults() {
        System.out.println();
        System.out.println(TEXT_GREEN+"Printing multiqueue results..."+TEXT_RESET);
        if (multiQueueResults.size() > 0) {
            for (HashMap<String, Double> hashMap : multiQueueResults) {
                System.out.println();
                if (hashMap.size() > 0) {
                    printReport(hashMap);
                }
            }
        } 
        System.out.println();
    }

    /**
     * Computes, saves and displays the results obtained from the simulations
     */
    private void printReport(HashMap<String, Double> results) {

        NumberFormat formatter = new DecimalFormat("#0.00");
        NumberFormat formattershort = new DecimalFormat("#0");

        System.out.println();

        // Displaying result
        System.out.println("Server types " + results.get("typeServers"));
        System.out.println("Results with " + formattershort.format(results.get("numServers")) + " servers");
        System.out.println("Customers arrived = " + Math.round(results.get("custArrived")));

        System.out.println(
                "Customers served = " + Math.round(results.get("custServed")) + " Customers served % " + formatter
                        .format(100 * Math.round(results.get("custServed")) / Math.round(results.get("custArrived"))));
        System.out.println("Mean divider = " + results.get("meanDivider"));
        System.out.println("Total cost of server = " + formatter.format(results.get("totalCost")) + " $ per day");
        System.out.println("Total cost of server per customer served= "
                + formatter.format(results.get("costPerCustomer")) + " $ per customer served");
        System.out.println("Num loop done: " + formattershort.format(results.get("loopDone")));
        System.out.println("Final clock (min): " + formatter.format(results.get("finalClock") / 60));
        System.out.println("Total Waiting Time (min): " + formatter.format(results.get("waitingTime") / 60)
                + "; avg (min): " + formatter.format(results.get("avgWaitingTime") / 60) + "; Max waiting time (min): "
                + formatter.format(results.get("maxWaitingTime") / 60));
        System.out.println("Total System Time (min): " + formatter.format(results.get("systemTime") / 60)
                + "; avg (min): " + formatter.format(results.get("avgSystemTime") / 60));
        for (int i = 0; i < results.get("numServers"); i++) {

            System.out.println("Total time Server #: " + i + " was Busy (min): "
                    + formatter.format(results.get("timeServer" + i) / 60) + "; (%):  "
                    + formatter.format(results.get("timeServer" + i + "%")));
        }
        System.out.println("Max customers waiting in line: " + formattershort.format(results.get("maxQueue")));

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