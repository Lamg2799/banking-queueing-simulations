package com.sim.proj;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class for Bank Queuing Simulation with New Realities project
 * University of Ottawa CSI 4124 group 4 fall 2021.
 * Group Members and Student IDs:
 * ● Samuel Garneau (2380248)
 * ● Chengen Lyu (300028734)
 * ● Jahesh Davodra (300018359)
 * ● Le Nguyen (300013304)
 * ● Luke Germond (300014113)
 * 
 */
class App {

    /**
     * text color reset white
     */
    private static final String TEXT_RESET = "\u001B[0m";
    /**
     * text color green
     */
    private static final String TEXT_GREEN = "\u001B[32m";
    /**
     * Output formatter
     */
    private NumberFormat formatter = new DecimalFormat("#0.00");
    /**
     * multiserver simulation instance
     */
    private Multiserver multiserver = null;
    /**
     * multiqueue simulation instance
     */
    private Multiqueue multiqueue = null;
    /**
     * List to store the multiserver simulation results at each execution
     */
    private ArrayList<Results> multiServerResults;
    /**
     * List to store the multiqueue simulation results at each execution
     */
    private ArrayList<Results> multiQueueResults;

    /**
     * The max number of execution with different server numbers
     */
    private int NUM_SERVERS_TO_TEST = 5; // default value
    /**
     * The max number of customer allowed in the waiting line at the same time (New
     * Realities)
     */
    private int MAX_QUEUE_SIZE = 2; // default value 2 - *negative numbers represents infinite 
    /**
     * The mean divider which is used to produce diffrent values from reference
     * document
     * with 1.0 value it will simulate using the same datas as the
     * reference document
     */
    private double MEAN_DIVIDER = 1; // default value
    /**
     * Constant for average service time
     */
    private final int MEAN_SERVICE_TIME = 150;
    /**
     * Constant for average service time sigma
     */
    private final int SIGMA_SERVICE_TIME = 80;
    /**
     * Constant for workday duration in seconds
     */
    private final double MAX_CLOCK = 28800;
    /**
     * Constant for max number of execution (Replications)
     */
    private final int MAX_LOOP = 2000;
    /**
     * Constant for number of server to start trial with
     */
    private final int SERVER_START_NUM = 1;
    /**
     * Startup input arguments array
     */
    private String[] arguments;

    /**
     * Main static function for project start
     * 
     * @param args args[0]=numMaxLoop;[1]=maxClock;[2]=meanS;[3]=sigmaS;[4]=numServer;[5]=MEAN_DIVIDER;
     */
    public static void main(String[] args) {

        new App(args);

    }

    /**
     * Constructor
     * 
     * @param args args[0]=numMaxLoop;[1]=maxClock;[2]=meanS;[3]=sigmaS;[4]=numServer;[5]=MEAN_DIVIDER;
     */
    private App(String[] args) {
        arguments = args;
        multiQueueResults = new ArrayList<Results>();
        multiServerResults = new ArrayList<Results>();
        // run simulations
        runSims();

    }

    /**
     * Main execution function for the project,
     * will execute all the simulations trials with different parameters
     * stops when it reachs NUM_SERVERS_TO_TEST
     */
    private void runSims() {

        var args = new String[6];
        // check if arguments are received or else use default values
        if (arguments != null && arguments.length == 3) {
            MAX_QUEUE_SIZE = Integer.parseInt(arguments[0]);
            MEAN_DIVIDER = Double.parseDouble(arguments[1]);
            NUM_SERVERS_TO_TEST = Integer.parseInt(arguments[2]);
        }
        args[0] = String.valueOf(MAX_LOOP);// # num execution should be set to 500
        args[1] = String.valueOf(MAX_CLOCK); // #max clock
        args[2] = String.valueOf(MEAN_SERVICE_TIME); // # mean service
        args[3] = String.valueOf(SIGMA_SERVICE_TIME); // # sigma service
        args[5] = String.valueOf(MEAN_DIVIDER); // #mean divider

        // loops through both kind of simulation and trying different number of servers
        for (int serverNum = SERVER_START_NUM; serverNum < NUM_SERVERS_TO_TEST + SERVER_START_NUM; serverNum++) {
            multiserver = new Multiserver();
            multiqueue = new Multiqueue();
            args[4] = String.valueOf(serverNum); // # number of server

            // run multiserver sim
            multiServerResults.add(multiserver.runSim(args));

            // run multiqueue sim
            multiQueueResults.add(multiqueue.runSim(args));

        }

        // printing results for all trials
        // finding optimal parameters based on MAX_QUEUE_SIZE
        try {
            printMultiServerResults();
            computeMultiserverOptimal();
            printMultiQueueResults();
            computeMultiqueueOptimal();
        } catch (Exception e) {

        }

    }

    /**
     * Find the optimal number of server based on max customer waiting in line
     * parameter
     */
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
                if (cc < mincost && qsize <= MAX_QUEUE_SIZE) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
                } else if(MAX_QUEUE_SIZE < 0) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
                }

            }
            if (cost > 0) {
                System.out.println("The optimal number of server with a mean divider of " + MEAN_DIVIDER
                        + " and a maximum queue size of " + (MAX_QUEUE_SIZE < 0 ? "infinite" : MAX_QUEUE_SIZE) + " for the multiqueue system is "
                        + (int) minserver + " with a total cost of " + formatter.format(cost)
                        + "$ and a cost per customer of " + formatter.format(mincost) + "$");
            }
        }
    }

    /**
     * Find the optimal number of server based on max customer waiting in line
     * parameter
     */
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
                if (cc < mincost && qsize <= MAX_QUEUE_SIZE) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
                } else if(MAX_QUEUE_SIZE < 0) {
                    mincost = cc;
                    minserver = r.get("numServers");
                    cost = r.get("totalCost");
                }

            }
            if (cost > 0) {
                System.out.println("The optimal number of server with a mean divider of " + MEAN_DIVIDER
                        + " and a maximum queue size of " + (MAX_QUEUE_SIZE < 0 ? "infinite" : MAX_QUEUE_SIZE) + " for the multiserver system is "
                        + (int) minserver + " with a total cost of " + formatter.format(cost)
                        + "$ and a cost per customer of " + formatter.format(mincost) + "$");
            }
        }
    }

    /**
     * loops through all the results and call print function
     */
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

    /**
     * loops through all the results and call print function
     */
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
     * 
     * @param results the hashmap to print
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
        System.out.println("Mean divider = " + results.get("MEAN_DIVIDER"));
        System.out.println("Total cost of server = " + formatter.format(results.get("totalCost")) + " $ per day");
        System.out.println("Total cost of server per customer served= "
                + formatter.format(results.get("costPerCustomer")) + " $ per customer served");
        System.out.println("Num loop done: " + formattershort.format(results.get("loopDone")));
        System.out.println("Final clock (min): " + formatter.format(results.get("finalClock") / 60));
        // waiting time stats
        System.out.println("Total waiting time average per executions (Y) (min): "
                + formatter.format(results.get("waitingTime") / 60)
                + "; Total waiting time average variance per executions (S^2) : "
                + formatter.format(results.get("waitingTimeVar") / 3600)
                + "; average per executions per customers (Y) (min): "
                + formatter.format(results.get("avgWaitingTime") / 60)
                + "; average variance per executions per customers (S^2) : "
                + formatter.format(results.get("avgWaitingTimeVar") / 3600)
                + "; Max waiting time (min): "
                + formatter.format(results.get("maxWaitingTime") / 60));
        System.out.println(
                "Wainting time confidence interval (min): " + formatter.format(results.get("waitingTimeH") / 60));
        // system time stats
        System.out.println("Total system time average per executions (Y) (min): "
                + formatter.format(results.get("systemTime") / 60)
                + "; Total system time average variance per executions (S^2): "
                + formatter.format(results.get("systemTimeVar") / 3600)
                + "; average per executions per customers (Y) (min): "
                + formatter.format(results.get("avgSystemTime") / 60)
                + "; average variance per executions per customers (S^2) : "
                + formatter.format(results.get("avgSystemTimeVar") / 3600));

        System.out
                .println("System time confidence interval (min): " + formatter.format(results.get("systemTimeH") / 60));

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

    /**
     * Server state idle
     */
    IDLE,
    /**
     * Server state busy
     */
    BUSY;

};

/**
 * Enum for event type either ARRIVAL or DEPARTURE
 */
enum EventType {

    /**
     * Event type Arrival
     */
    ARRIVAL,
    /**
     * Event type Departure
     */
    DEPARTURE;
};