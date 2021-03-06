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
 * Multi server implementaion as a part of our project for CSI4124 Simulation
 * and modelisation Created by:
 * 
 * @author CSI 4124 Group 4th
 * @version 1.0
 */
public class Singlequeue {

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
    private CustomerQueue customersQ = null;

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
     * The interarrival the random values
     */
    private LinkedList<Double> randomValues = null;
    /**
     * The current trial
     */
    private int trial = 0;
    /**
     * The current result level
     */
    private int resultLevel = 0;

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
        if (resultLevel > 1 && resultLevel < 5) {
            System.out.println();
            System.out.print(
                    App.GREEN + "Running " + App.TEXT_RESET + numMaxLoop + App.GREEN
                            + " singlequeue  simulations with "
                            + App.TEXT_RESET + args[3] + App.GREEN + " primary servers and " + App.TEXT_RESET + args[4]
                            + App.GREEN + " experienced servers... ");
        }
        // Loops to run multiple simulations
        while (currentLoop <= numMaxLoop) {

            // reinitialize all variables
            reInitialize();

            // loop through events in chronological order and process the right event type
            // stops when the workday is over ( 8hr)
            // must keep processing events untill all departure are processed
            while (clock < maxClock || !eventList.isEmpty()) {

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
                        // Stop letting people in at least 3 min before the end of workday. But will add 3 min for each person waiting in  line
                        //to ensure that all customers already in the ststem are getting served within the 8 hours workday 
                        if (clock < maxClock - (180.0 * (1.0+ (double) customersQ.numCustomers()))) {
                            processArrival();
                        }
                        break;

                }

            }

            // record stats for customers who did not get served during work day
            while (!customersQ.isEmpty()) {
                var c = customersQ.dequeue().getCustomer();
                c.setWaitingTime(clock);
                c.setTotalSystemTime(clock);
            }

            // compute avg results for this execution
            storeExecutionResults();

            // updates clocks and loops variables
            totalClock += clock;
            currentLoop++;
        }
        if (resultLevel > 1 && resultLevel < 5) {
            System.out.println("Done" + App.TEXT_RESET);
        }
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
        trial = Integer.parseInt(args[11]);
        resultLevel = Integer.parseInt(args[12]);

        numCustomersArrived = 0;
        numCustomersServed = 0;
        rdmS = new Random();
        numServer = numPrimary + numExperienced;
        customersQ = new CustomerQueue();
        servers = new Server[numServer];
        for (int i = 0; i < numPrimary; i++) {
            servers[i] = new Server(meanPrimaryS, sigmaPrimaryS, dailyPayPrimary, ServerType.PRIMARY);
        }
        for (int i = numPrimary; i < (numPrimary + numExperienced); i++) {
            servers[i] = new Server(meanExperiencedS, sigmaExperiencedS, dailyPayExperienced, ServerType.EXPERIENCED);
        }
        var name = App.GREEN + "Results for singlequeue  simulations trial # " + App.TEXT_RESET + trial
                + App.GREEN
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
        randomValues = new LinkedList<>();
        System.gc();
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

        // compute mean according to equation based on time of day
        var mean = (Math.pow(time, 2) * 0.000003657) - (0.1262 * time) + 1200;

        var pd = new PoissonDistribution(mean / meanDivider);
        var ret = Math.abs((double) pd.sample());
        randomValues.add(ret);

        return ret;
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
        Map<Integer, Integer> idleServers = new HashMap<Integer, Integer>();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getStatus() == Status.IDLE) {
                idleServers.put(i, i);
            }
        }
        // System.out.println("Processing arrival - number of servers at idle " +
        // idleServers.size());

        // if any idle server pick one else wait in line
        if (idleServers.isEmpty()) {
            // System.out.println("Processing arrival customer id " + c.getId() + " at " +
            // clock + " enqueued");
            // add to waiting line
            if (!isCustomerTurning()) {
                customersQ.enqueue(event);
            }
        } else {
            // System.out.println("Processing arrival idleServers.contains(s.length - 1) "
            // + idleServers.containsKey(s.length - 1) + " " + idleServers.size());
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
            scheduleDeparture(currentServer, event.getTime(), event.getCustomer());
        }

    }

    /**
     * Schedule a new departure event in the event list
     * 
     * @param currentServer the server where the customer is being served
     * @param time          the time of the departure
     * @param customer      the customer being served
     */
    private void scheduleDeparture(Server currentServer, double time, Customer customer) {

        double nextS = Math.abs(
                rdmS.nextGaussian() * currentServer.getMeanServiceTime() + currentServer.getSigmaServiceTime());
        // compile server busy time
        currentServer.addToTotalServiceTime(nextS);
        // add departure event
        eventList.add(new Event(EventType.DEPARTURE, nextS + time, customer));
        sort();

    }

    /**
     * compute the probablity of a customer turning away based on waiting line size
     * the probabily start at 0, will start growing fallowing this equation:
     * 1.097^x -2 ( x = waiting line size)
     * The probability of customers turning away will reach 100% at a size around
     * 100
     * 
     * @return if a customer is turning away
     */
    private boolean isCustomerTurning() {
        var s = (double) customersQ.numCustomers();

        var prob = Math.pow(1.097, s) - 2;
        var rg = rdm.nextInt(100);
        if (prob < 0) {
            prob = 0;
        }
        if (rg < prob) {
            return true;
        }

        return false;
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
        if (customersQ.isEmpty()) {
            // update server status
            currentServer.setStatus(Status.IDLE);

        } else {

            // process customer waiting in line
            event = customersQ.dequeue();
            c = event.getCustomer();

            // record customer waiting time
            c.setWaitingTime(clock);

            c.setServerIndex(serverIndex);
            // record the max waiting time
            if (c.getWaitingTime() > maxWaitingTime) {
                maxWaitingTime = c.getWaitingTime();
            }

            // schedule departure event
            scheduleDeparture(currentServer, clock, c);

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
        var arrivalAcc = 0.0;

        for (Customer customer : customers) {
            waitingAcc += customer.getWaitingTime();
            systemAcc += customer.getTotalSystemTime();
            arrivalAcc += customer.getInterArrivalValues();
        }
        var waitingTimeAvg = waitingAcc / (double) customers.size();
        var systemTimeAvg = systemAcc / (double) customers.size();
        var interarrivalTimeAvg = arrivalAcc / (double) customers.size();

        // compute variance
        var waitingVarAcc = 0.0;
        var systemVarAcc = 0.0;
        var interarrivalVarAcc = 0.0;
        for (Customer customer : customers) {
            waitingVarAcc += Math.pow(customer.getWaitingTime() - waitingTimeAvg, 2);
            systemVarAcc += Math.pow(customer.getTotalSystemTime() - systemTimeAvg, 2);
            interarrivalVarAcc += Math.pow(customer.getInterArrivalValues() - interarrivalTimeAvg, 2);
        }
        var waitingTimeAvgVar = waitingVarAcc / (double) customers.size();
        var systemTimeAvgVar = systemVarAcc / (double) customers.size();
        var interarrivalAvgVar = interarrivalVarAcc / (double) customers.size();
        // store in results
        results.addIaTimeResult(randomValues);
        results.addWaitingTimeAvg(waitingAcc);
        results.addSystemTimeAvg(systemAcc);
        results.addWaitingTimeAvgVar(waitingTimeAvgVar);
        results.addSystemTimeAvgVar(systemTimeAvgVar);
        results.addIaTimeAvg(interarrivalTimeAvg);
        results.addIaTimeAvgVar(interarrivalAvgVar);
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

        int totalCost = 0;
        for (int i = 0; i < servers.length; i++) {
            totalCost += servers[i].getDailyPay();
        }

        // store the results
        results.addResult("custArrived", (double) numCustomersArrived / maxloop);
        results.addResult("maxQueue", (double) customersQ.getMaxQS());
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

        // compute server busy time and pourcentage
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
        for (double avgwait : results.getWaitingTimeAvg()) {
            ret[0] += avgwait;
        }
        ret[0] = ret[0] / maxloop;

        // compute total waiting time variance
        for (double avgwait : results.getWaitingTimeAvg()) {
            ret[1] += Math.pow(avgwait - ret[0], 2);
        }
        ret[1] = ret[1] / (maxloop - 1);

        // compute avg waiting time and variance per customers
        ret[2] = ret[0] / cust;
        ret[3] = ret[1] / cust;

        // compute total system time
        for (double avgsyst : results.getSystemTimeAvg()) {
            ret[4] += avgsyst;
        }
        ret[4] = ret[4] / maxloop;

        // compute total system time variance
        for (double avgsyst : results.getSystemTimeAvg()) {

            ret[5] += Math.pow(avgsyst - ret[4], 2);
        }
        ret[5] = ret[5] / (maxloop - 1);

        // compute avg system time and variance per customers
        ret[6] = ret[4] / cust;
        ret[7] = ret[5] / cust;

        // compute confidence interval
        ret[8] = (Z * Math.sqrt(ret[1])) / Math.sqrt(maxloop);
        ret[9] = (Z * Math.sqrt(ret[5])) / Math.sqrt(maxloop);

        if (ret[8] > 100) {
            ret[8] = 100;
        }

        if (ret[9] > 100) {
            ret[9] = 100;
        }

        return ret;
    }

}
