package com.sim.proj;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.distribution.PoissonDistribution;

public class Multiserver {
    /**
     * List of customers to go through simulation
     */
    private ArrayList<Customer> customers = null;

    private int numServer = 0;

    /**
     * Stores the max number of execution of the simulation
     */
    private int numMaxLoop = 0;

    /**
     * Stores the current loop execution
     */
    private int currentLoop = 1;

    private int numCustomersServed = 0;
    private int numCustomersArrived = 0;

    /**
     * Stores the event clock for the current simulation
     */
    private double clock = 0;
    private double totalClock = 0;

    /**
     * Stores the total time spent inside the simulation for all customers and
     * simulations
     */
    private double totalSystemTime;

    /**
     * Stores the total waiting time spent inside the simulation for all customers
     * and simulations
     */
    private double totalWaitingTime;

    /**
     * Stores the max waiting time for all customers and simulations
     */
    private double maxWaitingTime = 0;

    /**
     * Stores the total time the server was busy for all customers and simulations
     */
    private double[] totalServerTime;

    /**
     * Stores the desired service time mean for the simulations
     */
    private double meanS = 0;

    /**
     * Stores the desired service time mean standart variation for the simulations
     */
    private double sigmaS = 0;

    /**
     * Stores the single server status either IDLE or BUSY
     */
    private State[] serverStatus;

    /**
     * Stores the customers waiting in line for service during a simulation
     */
    private CustomerQueue customersQ = null;

    /**
     * Stores the current event being processed
     */
    private Event event = null;

    /**
     * Stores the list of all future events for a simulation auto-sorted based on
     * event time
     */
    private LinkedList<Event> eventList = null;

    private Random rdm = new Random();
    /**
     * Stores the random generator for the service time for all simulations
     */
    private Random rdmS = null;

    /**
     * Stores the start time clock to compute execution time in millis
     */
    private long startTime = 0;
    private double maxClock = 0;

    private double meanDivider = 2.0;

    public Multiserver() {

    }

    /**
     * Run simulation method
     */
    public HashMap<String, Double> runSim(String[] args) {
        // retrieve config and create customers
        initialize(args);

        // start running time clock
        startTime = System.currentTimeMillis();

        // Starts simulation
        System.out.println();
        System.out.println("Running simulation with " + args[4] + " servers");

        // Loops to run multiple simulations
        while (currentLoop <= numMaxLoop) {

            // reinitialize all variables
            reInitialize();

            // loop through events in chronological order and process the right event type
            // stops when all the customers left the server
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
            // updates clocks and loops variables

            emptyWaitingLine();
            totalClock += clock;
            currentLoop++;
        }

        System.out.println("Simulation done... Generating report");

        // Generate the outputs

        return storeResults();
    }

    /**
     * Retrieves the configs and create a list of customer before starting the first
     * simulation
     */
    private void initialize(String[] args) {
        System.out.print("Initializing... ");

        // initalize parameters variables
        numMaxLoop = Integer.parseInt(args[0]);
        maxClock = Double.parseDouble(args[1]);
        meanS = Integer.parseInt(args[2]);
        sigmaS = Integer.parseInt(args[3]);
        numServer = Integer.parseInt(args[4]);
        meanDivider = Double.parseDouble(args[5]);
        numCustomersServed = 0;

        rdmS = new Random();
        totalServerTime = new double[numServer];
        serverStatus = new State[numServer];
        customersQ = new CustomerQueue();
        eventList = new LinkedList<Event>();
        customers = new ArrayList<Customer>();

        System.out.println("Initializing done");
    }

    /**
     * Reset all the variable before starting a new simulation
     */
    private void reInitialize() {
        clock = 0;
        // reinitializing variables

        for (int i = 0; i < serverStatus.length; i++) {

            serverStatus[i] = State.IDLE;
        }

        double nextIA = 0;
        double currentIA = 0;

        // generating InterArrival Events depending on customers number
        for (int i = 0; currentIA < maxClock; i++) {
            // generate next random value based on normal distribution with the required
            // mean and sigma

            // nextIA = Math.abs(rdmIA.nextGaussian() * sigmaIA + meanIA);

            nextIA = generateNextIA(currentIA);
            currentIA += nextIA;
            Customer c;
            if (currentLoop <= 1 || i >= customers.size()) {
                c = new Customer();
                customers.add(c);
            } else {

                c = customers.get(i);
            }
            // creates arrival event
            eventList.add(new Event(EventType.ARRIVAL, currentIA, c));

        }
        // sort event list
        sort();
    }

    private double generateNextIA(double time) {

        var mean = (Math.pow(time, 2) * 0.000003657) - (0.1262 * time) + 1200;

        // System.out.println("Poisson Distribution");

        var pd = new PoissonDistribution(mean / meanDivider);
        var ret = pd.sample();
        // System.out.println("Random value " + ret);

        return ret;
    }

    private void emptyWaitingLine() {

        while (!customersQ.isEmpty()) {
            var c = customersQ.dequeue();
            c.getCustomer().setWaitingTime(clock);
            c.getCustomer().setTotalSystemTime(clock);
        }
    }

    /**
     * If the server is IDLE then it will create an DEPARTURE event, otherwise it
     * will enqueue the event in the customers queue
     */
    private void processArrival() {
        numCustomersArrived++;
        var c = event.getCustomer();
        // record customer arrival time
        c.setArrivalTime(currentLoop, clock);
        // check if any server idle
        Map<Integer, Integer> idleServers = new HashMap<Integer, Integer>();
        var s = serverStatus;
        for (int i = 0; i < s.length; i++) {

            if (s[i] == State.IDLE) {

                idleServers.put(i, i);
            }

        }
        // System.out.println("Processing arrival - number of servers at idle " +
        // idleServers.size());

        if (!idleServers.isEmpty()) {
            // System.out.println("Processing arrival idleServers.contains(s.length - 1) "
            // + idleServers.containsKey(s.length - 1) + " " + idleServers.size());
            int index = 0;

            if (idleServers.containsKey(s.length - 1) && idleServers.size() > 1) {
                idleServers.remove(s.length - 1);
            }
            var indexpick = 0;
            if (idleServers.size() > 1) {

                indexpick = rdm.nextInt(idleServers.size());

            }
            var keyset = idleServers.keySet();
            var arr = keyset.toArray();
            index = (Integer) arr[indexpick];
            // set server busy
            serverStatus[index] = State.BUSY;
            // System.out.println("Processing arrival customer id " + c.getId() + " at " +
            // clock[currentLoop] + " server #: " + index);
            c.setServerIndex(index);
            // schedule departure event

            double nextS = Math.abs(rdmS.nextGaussian() * sigmaS + meanS);
            totalServerTime[index] += (nextS);
            eventList.add(new Event(EventType.DEPARTURE, nextS + event.getTime(), c));
            sort();

        } else {
            // System.out.println("Processing arrival customer id " + c.getId() + " at " +
            // clock + " enqueued");
            customersQ.enqueue(event);
        }

    }

    /**
     * Records departure time, if any customer is waiting then create new departure
     * event and record waiting time, otherwise set the server status to IDLE and
     * record server busy time
     */
    private void processDeparture() {

        numCustomersServed++;
        var c = event.getCustomer();
        int serverIndex = c.getServerIndex();
        // System.out.println("Processing depart customer id " + c.getId() + " at " +
        // clock + " server # " + serverIndex);
        // record customer system time
        c.setTotalSystemTime(clock);
        totalSystemTime += c.getTotalSystemTime();

        // check if any customer are waiting in line
        if (customersQ.isEmpty()) {
            // update server status and record server busy time
            serverStatus[serverIndex] = State.IDLE;

        } else {

            // process customer waiting in line
            event = customersQ.dequeue();
            c = event.getCustomer();

            // record customer waiting time
            c.setWaitingTime(clock);
            totalWaitingTime += c.getWaitingTime();
            c.setServerIndex(serverIndex);
            // record the max waiting time
            if (c.getWaitingTime() > maxWaitingTime) {
                maxWaitingTime = c.getWaitingTime();
            }

            // schedule departure event

            double nextS = Math.abs(rdmS.nextGaussian() * sigmaS + meanS);
            totalServerTime[serverIndex] += (nextS);
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

        Collections.sort(eventList, compareByTime);
    }

    private HashMap<String, Double> storeResults() {

        var results = new HashMap<String, Double>();

        double servers = (double) numServer;
        double maxloop = (double) numMaxLoop;
        double numcust = (double) numCustomersServed / numMaxLoop;
     
        

        results.put("numServers", servers);
        results.put("totalCost", servers * 320);
        results.put("costPerCustomer", servers * 320.0 / numcust);
        results.put("loopDone", maxloop);
        results.put("finalClock", totalClock / maxloop);
        results.put("waitingTime", totalWaitingTime / maxloop);
        results.put("avgWaitingTime", (totalWaitingTime / (maxloop / numcust)));
        results.put("maxWaitingTime", maxWaitingTime);
        results.put("systemTime", (totalSystemTime / (maxloop / numcust)));
        results.put("avgSystemTime", (totalSystemTime / (maxloop)) / numcust);
        results.put("maxQueue", (double) customersQ.getMaxQS());
        results.put("meanDivider", meanDivider);

        for (int i = 0; i < totalServerTime.length; i++) {
            // server busy poucentage
            var key = "timeServer" + i;
            var value = totalServerTime[i] / maxloop;
            results.put(key, value);
            value = 100 * totalServerTime[i] / totalClock;
            results.put(key + "%", value);

        }
        printReport();
        return results;
        // adds the results to output class so its available for comparing

    }

    /**
     * Computes, saves and displays the results obtained from the simulations
     */
    private void printReport() {

        // record stop time
        long executionTime = System.currentTimeMillis() - startTime;

        NumberFormat formatter = new DecimalFormat("#0.00");

        System.out.println();
        System.out.println();

        /*
         * loop through all customers display and save the result for (int i = 0; i <
         * customers.size(); i++) { var c = customers.get(i);
         * 
         * System.out.println(c); System.out.println();
         * 
         * }
         */
        // Displaying result
        System.out.println("Customers arrived = " + numCustomersArrived / numMaxLoop);
        var m =  numCustomersServed / numMaxLoop;
        System.out.println("Customers served = " + m +" Customers served % "+Math.round(100*(double)numCustomersServed/(double)numCustomersArrived));
        System.out.println("Mean divider = " + meanDivider);
        System.out.println("Total cost of server = " + formatter.format(numServer * 320) + " $ per day");
        System.out.println("Total cost of server per customer served= "
                + formatter.format((double) numServer * 320.0 / ((double) numCustomersServed / numMaxLoop))
                + " $ per customer served");
        System.out.println("Total execution Time (millis): " + String.valueOf(executionTime));
        System.out.println("Num loop done: " + numMaxLoop);
        System.out.println("Final clock (min): " + formatter.format((totalClock / numMaxLoop)/60));
        System.out.println("Total Waiting Time (min): " + formatter.format((totalWaitingTime / numMaxLoop)/60)
                + "; avg (min): " + formatter.format((totalWaitingTime / numCustomersServed)/60)
                + "; Max waiting time (min): " + formatter.format(maxWaitingTime/60));
        System.out.println("Total System Time (min): " + formatter.format((totalSystemTime / numMaxLoop)/60)
                + "; avg (min): " + formatter.format((totalSystemTime / numCustomersServed)/60));
        for (int i = 0; i < totalServerTime.length; i++) {

            System.out.println("Total time Server #: " + i + " was Busy (min): "
                    + formatter.format((totalServerTime[i] / numMaxLoop)/60) + "; (%):  "
                    + formatter.format(100 * totalServerTime[i] / totalClock));
        }
        System.out.println("Max customers waiting in line: " + customersQ.getMaxQS());

    }

}
