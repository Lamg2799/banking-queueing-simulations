package com.sim.proj;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

class App {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";

    private Multiserver multiserver = null;
    private Multiqueue multiqueue = null;
    // data structure to store result
    private ArrayList<Results> multiServerResults;
    private ArrayList<Results> multiQueueResults;

    private int maxExecution = 5; // default value
    private int maxQueueSize = 2; // default value
    private double meanDivider = 1; // default value
    private final int MEAN_SERVICE_TIME = 150; // constant
    private final int SIGMA_SERVICE_TIME = 80; // constant
    private final double MAX_CLOCK = 28800; // constant
    private final int MAX_LOOP = 1000; // constant
    private final int SERVER_START_NUM = 1; // constant
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
        multiQueueResults = new ArrayList<Results>();
        multiServerResults = new ArrayList<Results>();
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
        args[0] = String.valueOf(MAX_LOOP);// # num execution should be set to 500
        args[1] = String.valueOf(MAX_CLOCK); // #max clock
        args[2] = String.valueOf(MEAN_SERVICE_TIME); // # mean service
        args[3] = String.valueOf(SIGMA_SERVICE_TIME); // # sigma service

        args[5] = String.valueOf(meanDivider); // #mean divider

        // loops through both kind of simulation and trying different number of servers
        for (int serverNum = SERVER_START_NUM; serverNum < maxExecution + SERVER_START_NUM; serverNum++) {
            multiserver = new Multiserver();
            multiqueue = new Multiqueue();
            args[4] = String.valueOf(serverNum); // # number of server

            // run multiserver sim
            multiServerResults.add(multiserver.runSim(args));

            // run multiqueue sim
            multiQueueResults.add(multiqueue.runSim(args));

        }

        // printing results for all trials
        try {
            printMultiServerResults();
            computeMultiserverOptimal();
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            printMultiQueueResults();
            computeMultiqueueOptimal();
        } catch (Exception e) {
            // TODO: handle exception
        }

        // finding optimal parameters based on maxQueueSize

    }

    private void computeMultiqueueOptimal() {

        // find lowest cost within restriction
        System.out.println(TEXT_GREEN + "Finding optimal result for multiqueue..." + TEXT_RESET);
        double mincost = Double.MAX_VALUE;
        double minserver = 0;
        double cost = 0;
        if (multiQueueResults.size() > 0) {
            for (Results rst : multiQueueResults) {
                // retrieve result hashmap
                var r = rst.getResults();

                var cc = r.get("costPerCustomer");
                var qsize = r.get("maxQueue");
                if (cc < mincost && qsize <= maxQueueSize) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
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
        System.out.println(TEXT_GREEN + "Finding optimal result for multiserver..." + TEXT_RESET);
        double mincost = Double.MAX_VALUE;
        double minserver = 0;
        double cost = 0;
        if (multiServerResults.size() > 0) {
            for (Results rst : multiServerResults) {
                // retrieve result hashmap
                var r = rst.getResults();

                var cc = r.get("costPerCustomer");
                var qsize = r.get("maxQueue");
                if (cc < mincost && qsize <= maxQueueSize) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
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
        System.out.println(TEXT_GREEN + "Printing multiserver results..." + TEXT_RESET);
        if (multiServerResults.size() > 0) {
            for (Results rst : multiServerResults) {
                System.out.println();

                printReport(rst.getResults());

            }
        }
        System.out.println();

    }

    private void printMultiQueueResults() {
        System.out.println();
        System.out.println(TEXT_GREEN + "Printing multiqueue results..." + TEXT_RESET);
        if (multiQueueResults.size() > 0) {
            for (Results rst : multiQueueResults) {
                System.out.println();

                printReport(rst.getResults());

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
        // waiting time stats
        System.out.println("Total waiting time average per executions (Ȳ) (min): "
                + formatter.format(results.get("waitingTime") / 60)
                + "; Total waiting time average variance per executions (S^2) (min): "
                + formatter.format(results.get("waitingTimeVar") / 60)
                + "; average per executions per customers (Ȳ) (min): "
                + formatter.format(results.get("avgWaitingTime") / 60)
                + "; average variance per executions per customers (S^2) (min): "
                + formatter.format(results.get("avgWaitingTimeVar") / 60)
                + "; Max waiting time (min): "
                + formatter.format(results.get("maxWaitingTime") / 60));
                System.out.println("Wainting time confidence interval (min): " + formatter.format(results.get("waitingTimeH") / 60));
        // system time stats
        System.out.println("Total system time average per executions (Ȳ) (min): "
                + formatter.format(results.get("systemTime") / 60)
                + "; Total system time average variance per executions (S^2) (min): "
                + formatter.format(results.get("systemTimeVar") / 60)
                + "; average per executions per customers (Ȳ) (min): "
                + formatter.format(results.get("avgSystemTime") / 60)
                + "; average variance per executions per customers (S^2) (min): "
                + formatter.format(results.get("avgSystemTimeVar") / 60));

        System.out.println("System time confidence interval (min): " + formatter.format(results.get("systemTimeH") / 60));

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