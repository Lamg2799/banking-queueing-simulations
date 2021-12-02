package com.sim.proj;


/**
 * Multi server multi queue implementaion as a part of our project for CSI4124 Simulation
 * and modelisation Created by:
 * 
 * @author ...
 * @version 1.0
 */
public class Multiqueue {


    /**
     * Run simulation main method,
     * 
     * 
     * @param args args[0]=numMaxLoop;[1]=maxClock;[2]=meanS;[3]=sigmaS;[4]=numServer;[5]=meanDivider;
     * @return results
     */
    public Results runSim(String[] args) {
        // Do your thing

        // ...your code...

        // return results
        return storeResults();
    }

    /**
     * Computes and the simulation executions results in Results object
     * 
     * @return results
     */
    private Results storeResults() {

        var results = new Results();
        /*
         * double servers = (double) numServer; double maxloop = (double) numMaxLoop;
         * double numcust = (double) numCustomersServed / numMaxLoop;
         * 
         * results.put("custArrived", (double) numCustomersArrived / maxloop);
         * results.put("custServed", numcust); results.put("numServers", servers);
         * results.put("totalCost", servers * 320); results.put("costPerCustomer",
         * servers * 320.0 / numcust); results.put("loopDone", maxloop);
         * results.put("finalClock", totalClock / maxloop); results.put("waitingTime",
         * totalWaitingTime / maxloop); results.put("avgWaitingTime", (totalWaitingTime
         * / maxloop) / numcust); results.put("maxWaitingTime", maxWaitingTime);
         * results.put("systemTime", (totalSystemTime / maxloop));
         * results.put("avgSystemTime", (totalSystemTime / maxloop) / numcust);
         * results.put("maxQueue", (double) customersQ.getMaxQS());
         * results.put("meanDivider", meanDivider); results.put("typeServers", 1.0);
         * 
         * for (int i = 0; i < results.get("numServers"); i++) { // server busy
         * poucentage var key = "timeServer" + i; var value = totalServerTime[i] /
         * maxloop; results.put(key, value); value = 100 * totalServerTime[i] /
         * totalClock; results.put(key + "%", value);
         * 
         * }
         */
        return results;
        // adds the results to output class so its available for comparing

    }

}
