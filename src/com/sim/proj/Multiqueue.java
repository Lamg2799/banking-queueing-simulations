package com.sim.proj;

import java.util.HashMap;

public class Multiqueue {

    public HashMap<String, Double> runSim(String[] args) {
        // Do your thing

        // ...your code...

        // return results
        return storeResults();
    }

    private HashMap<String, Double> storeResults() {

        
        var results = new HashMap<String, Double>();
        /*
          double servers = (double) numServer; double maxloop = (double) numMaxLoop;
          double numcust = (double) numCustomersServed / numMaxLoop;
          
          results.put("custArrived", (double) numCustomersArrived / maxloop);
          results.put("custServed", numcust); results.put("numServers", servers);
          results.put("totalCost", servers * 320); results.put("costPerCustomer",
          servers * 320.0 / numcust); results.put("loopDone", maxloop);
          results.put("finalClock", totalClock / maxloop); results.put("waitingTime",
          totalWaitingTime / maxloop); results.put("avgWaitingTime", (totalWaitingTime
          / maxloop) / numcust); results.put("maxWaitingTime", maxWaitingTime);
          results.put("systemTime", (totalSystemTime / maxloop));
          results.put("avgSystemTime", (totalSystemTime / maxloop) / numcust);
          results.put("maxQueue", (double) customersQ.getMaxQS());
          results.put("meanDivider", meanDivider); results.put("typeServers", 1.0);
          
          for (int i = 0; i < results.get("numServers"); i++) { // server busy
          poucentage var key = "timeServer" + i; var value = totalServerTime[i] /
          maxloop; results.put(key, value); value = 100 * totalServerTime[i] /
          totalClock; results.put(key + "%", value);
          
          }
         */
        return results;
        // adds the results to output class so its available for comparing

    }

}
