package com.sim.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 * Multi server multi queue implementaion as a part of our project for CSI4124
 * Simulation
 * and modelisation Created by:
 * 
 * @author
 * @version 1.0
 */
public class Multiqueue {

    /**
     * List of customers to go through simulation
     */
    private ArrayList<Customer> customers = null;

    /**
     * The total number of servers of the simulation
     */
    private int numServer = 0;

    /**
     * The max number of execution of the simulation
     */
    private int numMaxLoop = 0;

    /**
     * The current loop execution
     */
    private int currentLoop = 1;

    /**
     * The total number of customers who have been processed as departure
     */
    private int numCustomersServed = 0;
    /**
     * The total number of customers who have been processed as arrival
     */
    private int numCustomersArrived = 0;

    /**
     * The event clock for the current simulation
     */
    private double clock = 0;

    /**
     * The total event clock for the all simulations
     */
    private double totalClock = 0;

    /**
     * The max waiting time for all customers and simulations
     */
    private double maxWaitingTime = 0;

    /**
     * The desired primary server service time mean for the simulations
     */
    private double meanPrimaryS = 0;

    /**
     * The desired primary server service time mean standart variation for the
     * simulations
     */
    private double sigmaPrimaryS = 0;

    /**
     * The desired experienced server service time mean for the simulations
     */
    private double meanExperiencedS = 0;

    /**
     * The desired experienced server service time mean standart variation for the
     * simulations
     */
    private double sigmaExperiencedS = 0;

    /**
     * The daily pay (8 hours) for a primary server
     */
    private double dailyPayPrimary = 0;

    /**
     * The daily pay (8 hours) for an experienced server
     */
    private double dailyPayExperienced = 0;

    /**
     * The number of primary servers in the simulation
     */
    private int numPrimary = 0;

    /**
     * The number of experienced servers in the simulation
     */
    private int numExperienced = 0;

    /**
     * The customers waiting in line for service during a simulation
     */
    private CustomerQueue[] customerQueues = null;

    /**
     * The servers that serve the customers during a simulation
     */
    private Server[] servers = null;

    /**
     * The current event being processed
     */
    private Event event = null;

    /**
     * The list of all future events for a simulation auto-sorted based on
     * event time
     */
    private LinkedList<Event> eventList = null;

    /**
     * The random generator for the servers choice
     */
    private Random rdm = new Random();
    /**
     * The random generator for the service time for all simulations
     */
    private Random rdmS = null;

    /**
     * The max execution time (workday)
     */
    private double maxClock = 0;

    /**
     * The mean divider which is used to produce diffrent values from reference
     * document
     * with 1.0 value it will simulate using the same datas as the
     * reference document
     */
    private double meanDivider = 1.0; // default value

    /**
     * The simulation executions results
     */
    private Results results = null;

    /**
     * The Z value use to compute confidence interval
     */
    private final double Z = 1.96;// from normal table

    /**
     * Run simulation main method,
     * 
     * 
     * @param args args[0]=numMaxLoop;[1]=maxClock;[2]=meanDivider;[3]=numPrimary;[4]=numExperienced;[5]=meanPrimaryS;[6]=sigmaPrimaryS;[7]=meanExperiencedS;[8]=sigmaExperiencedS;[9]=dailyPayPrimary;[10]=dailyPayExperienced;
     * @return results
     */
    public Results runSim(String[] args) {
        // retrieve config and create customers
        initialize(args);

        // Starts simulation
        System.out.println();
        System.out.print(
                App.GREEN + "Running " + App.TEXT_RESET + numMaxLoop + App.GREEN + " multiqueue simulations with "
                        + App.TEXT_RESET + args[3] + App.GREEN + " primary servers and " + App.TEXT_RESET + args[4]
                        + App.GREEN + " experienced servers... ");

        // Loops to run multiple simulations
        while (currentLoop <= numMaxLoop) {

            // reinitialize all variables
            reInitialize();

            // loop through events in chronological order and process the right event type
            // stops when the workday is over ( 8hr)
            while (clock < maxClock) {

                // retrieve the next event
                event = eventList.removeFirst();
                // update the clock to current event
                clock = event.getTime();

                // Calls the appropriate method depending on event type
                switch (event.getType()) {
                    case DEPARTURE:
                        processDeparture();
                        break;
                    case ARRIVAL:
                        processArrival();
                        break;

                }
            }

            // record stats for customers who did not get served during work day
            for (int i = 0; i < customerQueues.length; i++) {
                while (!customerQueues[i].isEmpty()) {
                    var c = customerQueues[i].dequeue().getCustomer();
                    c.setWaitingTime(clock);
                    c.setTotalSystemTime(clock);
                }
            }
            // compute avg results for this execution
            storeExecutionResults();

            // updates clocks and loops variables
            totalClock += clock;
            currentLoop++;
        }

        System.out.println("Done" + App.TEXT_RESET);

        // Generate the outputs

        return storeResults();
    }

    /**
     * Retrieves the configs initialyze objects
     * 
     * @param args args[0]=numMaxLoop;[1]=maxClock;[2]=meanDivider;[3]=numPrimary;[4]=numExperienced;[5]=meanPrimaryS;[6]=sigmaPrimaryS;[7]=meanExperiencedS;[8]=sigmaExperiencedS;[9]=dailyPayPrimary;[10]=dailyPayExperienced;
     */
    private void initialize(String[] args) {

        // initalize parameters variables
        numMaxLoop = Integer.parseInt(args[0]);
        maxClock = Double.parseDouble(args[1]);
        meanDivider = Integer.parseInt(args[2]);
        numPrimary = Integer.parseInt(args[3]);
        numExperienced = Integer.parseInt(args[4]);
        meanPrimaryS = Integer.parseInt(args[5]);
        sigmaPrimaryS = Integer.parseInt(args[6]);
        meanExperiencedS = Integer.parseInt(args[7]);
        sigmaExperiencedS = Integer.parseInt(args[8]);
        dailyPayPrimary = Integer.parseInt(args[9]);
        dailyPayExperienced = Integer.parseInt(args[10]);
        var trial = Integer.parseInt(args[11]);

        numCustomersServed = 0;
        rdmS = new Random();
        numServer = numPrimary + numExperienced;
        customerQueues = new CustomerQueue[numServer];
        servers = new Server[numServer];
        for (int i = 0; i < customerQueues.length; i++) {
            customerQueues[i] = new CustomerQueue();
        }
        for (int i = 0; i < numPrimary; i++) {

            servers[i] = new Server(meanPrimaryS, sigmaPrimaryS, dailyPayPrimary, ServerType.PRIMARY);
        }
        for (int i = numPrimary; i < (numPrimary + numExperienced); i++) {
            servers[i] = new Server(meanExperiencedS, sigmaExperiencedS, dailyPayExperienced, ServerType.EXPERIENCED);
        }
        var name = App.GREEN + "Results for multiqueue simulations trial # " + App.TEXT_RESET + trial + App.GREEN
                + " with "
                + App.TEXT_RESET + args[3] + App.GREEN + " primary servers and " + App.TEXT_RESET + args[4]
                + App.GREEN + " experienced servers... ";

        results = new Results(name);
    }

    /**
     * Reset all the variable before starting a new simulation
     */
    private void reInitialize() {

        // reinitializing variables
        clock = 0;
        eventList = new LinkedList<Event>();
        customers = new ArrayList<Customer>();

        for (int i = 0; i < servers.length; i++) {
            servers[i].setStatus(Status.IDLE);
        }

        // generating InterArrival Events depending max time (8hr)
        double nextIA = 0;
        double currentIA = 0;

        while (currentIA < maxClock) {
            // generate next random value based on poisson distribution with mean based on
            // quadratic equation

            nextIA = generateNextIA(currentIA);

            currentIA += nextIA;
            Customer c = new Customer();

            customers.add(c);

            c.setInterArrivalValue(nextIA);
            // creates arrival event
            eventList.add(new Event(EventType.ARRIVAL, currentIA, c));

        }

        // sort event list
        sort();
    }

    /**
     * generate next interarrival time based on poisson distribution and time of day
     * function
     * 
     * @param time current time
     * @return a new ramdomly generated value
     */
    private double generateNextIA(double time) {

        // compute mean according to quadractic equation based on time of day
        var mean = (Math.pow(time, 2) * 0.000003657) - (0.1262 * time) + 1200;

        var pd = new PoissonDistribution(mean / meanDivider);
        var ret = Math.abs(pd.sample());
        // System.out.println("Random value " + ret);

        return ret;
    }

    /**
     * Select the queue with the shortest line
     * 
     * @param queues The queue of each server
     * @return The queue with the shortest line
     */
    private CustomerQueue getShortestQueue(CustomerQueue[] queues) {
        CustomerQueue queueWithShortestLine = queues[0];
        for (int i = 1; i < queues.length; i++) {
            if (queues[i].numCustomers() < queueWithShortestLine.numCustomers()) {
                queueWithShortestLine = queues[i];
            }
        }
        return queueWithShortestLine;
    }

    /**
     * If the server is IDLE then it will create an DEPARTURE event, otherwise it
     * will enqueue the event in the customers queue
     */
    private void processArrival() {
        numCustomersArrived++;
        var c = event.getCustomer();
        // record customer arrival time
        c.setArrivalTime(clock);
        // check if any server idle
        // Pick the queue with an idle server, which means empty line. This means the
        // customer is not added to a waiting queue but is served immediately.
        Map<Integer, Integer> idleServers = new HashMap<Integer, Integer>();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getStatus() == Status.IDLE) {
                idleServers.put(i, i);
            }
        }
        // If no servers are idle, pick the shortest line.
        if (idleServers.isEmpty()) {
            CustomerQueue shortestQueue = getShortestQueue(customerQueues);
            shortestQueue.enqueue(event);
        } else {
            int index = 0;

            if (idleServers.containsKey(servers.length - 1) && idleServers.size() > 1) {
                idleServers.remove(servers.length - 1);
            }
            var indexpick = 0;
            if (idleServers.size() > 1) {

                indexpick = rdm.nextInt(idleServers.size());

            }
            var keyset = idleServers.keySet();
            var arr = keyset.toArray();
            index = (Integer) arr[indexpick];
            Server currentServer = servers[index];
            // set server busy
            currentServer.setStatus(Status.BUSY);
            c.setServerIndex(index);
            // schedule departure event
            double nextS = Math.abs(
                    rdmS.nextGaussian() * currentServer.getMeanServiceTime() + currentServer.getSigmaServiceTime());
            // compile server busy time
            currentServer.addToTotalServiceTime(nextS);
            // add departure event
            eventList.add(new Event(EventType.DEPARTURE, nextS + event.getTime(), c));
            sort();

        }
    }

    /**
     * Records departure time, if any customer is waiting then create new departure
     * event and record waiting time, otherwise set the server status to IDLE
     */
    private void processDeparture() {

        numCustomersServed++;
        var c = event.getCustomer();
        int serverIndex = c.getServerIndex();
        Server currentServer = servers[serverIndex];

        // record customer system time
        c.setTotalSystemTime(clock);

        // check if any customer are waiting in line
        if (customerQueues[serverIndex].isEmpty()) {
            // update server status
            currentServer.setStatus(Status.IDLE);

        } else {

            // process customer waiting in line
            event = customerQueues[serverIndex].dequeue();
            c = event.getCustomer();

            // record customer waiting time
            c.setWaitingTime(clock);

            c.setServerIndex(serverIndex);
            // record the max waiting time
            if (c.getWaitingTime() > maxWaitingTime) {
                maxWaitingTime = c.getWaitingTime();
            }

            // schedule departure event

            double nextS = Math.abs(
                    rdmS.nextGaussian() * currentServer.getMeanServiceTime() + currentServer.getSigmaServiceTime());
            currentServer.addToTotalServiceTime(nextS);
            eventList.add(new Event(EventType.DEPARTURE, nextS + clock, c));
            sort();

        }

    }

    /**
     * Sorts the event list in order of event time
     */
    private void sort() {

        // Create new comparator to compare event time
        Comparator<Event> compareByTime = new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getTime().compareTo(e2.getTime());
            }

        };

        Collections.sort(eventList, compareByTime); // uses TimSort
    }

    /**
     * Compute and store simulation results for a single execution in results Object
     */
    private void storeExecutionResults() {
        // compute avg waiting and system time
        var waitingAcc = 0.0;
        var systemAcc = 0.0;

        for (Customer customer : customers) {
            waitingAcc += customer.getWaitingTime();
            systemAcc += customer.getTotalSystemTime();
        }
        var waitingTimeAvg = waitingAcc / (double) customers.size();
        var systemTimeAvg = systemAcc / (double) customers.size();

        // compute variance
        var waitingVarAcc = 0.0;
        var systemVarAcc = 0.0;
        for (Customer customer : customers) {
            waitingVarAcc += Math.pow(customer.getWaitingTime() - waitingTimeAvg, 2);
            systemVarAcc += Math.pow(customer.getTotalSystemTime() - systemTimeAvg, 2);
        }
        var waitingTimeAvgVar = waitingVarAcc / (double) customers.size();
        var systemTimeAvgVar = systemVarAcc / (double) customers.size();

        // store in results

        results.addWaitingTime(waitingAcc);
        results.addSystemTime(systemAcc);
        results.addWaitingTimeAvgVar(waitingTimeAvgVar);
        results.addSystemTimeAvgVar(systemTimeAvgVar);
    }

    /**
     * Computes and the simulation executions results in Results object
     * 
     * @return results
     */
    private Results storeResults() {

        double numPrimaryServers = (double) numPrimary;
        double numExperiencedServers = (double) numExperienced;
        double maxloop = (double) numMaxLoop;
        double numcust = (double) numCustomersServed / maxloop;

        // compute the results
        var avgStats = getExecutionsStats(); // [0]=waitingTime;[1]=waitingTimeVar;[2]avgWaitingTime;[3]avgWaitingTimeVar;[4]=systemTime;[5]=systemTimeVar;[6]avgSystemTime;[7]avgSystemTimeVar;[8]waitingconfidenceInterval;[9]systemconfidenceInterval

        // store the results
        results.addResult("custArrived", (double) numCustomersArrived / maxloop);
        int maxQueueSize = 0;
        int totalCost = 0;
        for (int i = 0; i < customerQueues.length; i++) {
            int currentSize = customerQueues[i].getMaxQS();
            if (currentSize > maxQueueSize) {
                maxQueueSize = currentSize;
            }
        }
        for (int i = 0; i < servers.length; i++) {
            totalCost += servers[i].getDailyPay();
        }
        results.addResult("maxQueue", (double) maxQueueSize);
        results.addResult("custServed", numcust);
        results.addResult("numPrimaryServers", numPrimaryServers);
        results.addResult("numExperiencedServers", numExperiencedServers);
        results.addResult("loopDone", maxloop);
        results.addResult("typeServers", 1.0);
        results.addResult("totalCost", (double) totalCost);
        results.addResult("costPerCustomer", totalCost / numcust);
        results.addResult("finalClock", totalClock / maxloop);
        results.addResult("waitingTime", avgStats[0]); // avg per executions
        results.addResult("waitingTimeVar", avgStats[1]); // avg variance per executions
        results.addResult("avgWaitingTime", avgStats[2]); // avg per execution per customers
        results.addResult("avgWaitingTimeVar", avgStats[3]);// avg variance per executions per cutomers
        results.addResult("maxWaitingTime", maxWaitingTime);
        results.addResult("systemTime", avgStats[4]);
        results.addResult("systemTimeVar", avgStats[5]);
        results.addResult("avgSystemTime", avgStats[6]);
        results.addResult("avgSystemTimeVar", avgStats[7]);
        results.addResult("waitingTimeH", avgStats[8]);
        results.addResult("systemTimeH", avgStats[9]);
        results.addResult("meanDivider", meanDivider);

        // compute server busy time and percentage
        for (int i = 0; i < numServer; i++) {

            var key = "timeServer" + i;
            var totalTime = servers[i].getTotalTime();
            var value = totalTime / maxloop;
            results.addResult(key, value);
            value = 100.0 * totalTime / totalClock;
            results.addResult(key + "%", value);

        }
        // adds the results to output class so its available for comparing
        return results;

    }

    /**
     * computes all the results from simulation executions
     * 
     * @return avgStats
     */
    private double[] getExecutionsStats() {
        var maxloop = (double) numMaxLoop;
        var cust = (double) numCustomersArrived / maxloop;
        // ret[0]=waitingTime;[1]=waitingTimeVar;[2]avgWaitingTime;[3]avgWaitingTimeVar;[4]=systemTime;[5]=systemTimeVar;[6]avgSystemTime;[7]avgSystemTimeVar;[8]waitingconfidenceInterval;[9]systemconfidenceInterval
        double[] ret = new double[10];
        // init array
        for (int i = 0; i < ret.length; i++) {
            ret[i] = 0.0;
        }

        // compute total waiting time
        for (double avgwait : results.getWaitingTime()) {
            ret[0] += avgwait;
        }
        ret[0] = ret[0] / maxloop;

        // compute total waiting time variance
        for (double avgwait : results.getWaitingTime()) {
            ret[1] += Math.pow(avgwait - ret[0], 2);
        }
        ret[1] = ret[1] / (maxloop - 1);

        // compute avg waiting time and variance per customers
        ret[2] = ret[0] / cust;
        ret[3] = ret[1] / cust;

        // compute total system time
        for (double avgsyst : results.getSystemTime()) {
            ret[4] += avgsyst;
        }
        ret[4] = ret[4] / maxloop;

        // compute total system time variance
        for (double avgsyst : results.getSystemTime()) {

            ret[5] += Math.pow(avgsyst - ret[4], 2);
        }
        ret[5] = ret[5] / (maxloop - 1);

        // compute avg system time and variance per customers
        ret[6] = ret[4] / cust;
        ret[7] = ret[5] / cust;

        // compute confidence interval
        ret[8] = Z * Math.sqrt(ret[1]) / Math.sqrt(maxloop);
        ret[9] = Z * Math.sqrt(ret[5]) / Math.sqrt(maxloop);

        return ret;
    }

}
