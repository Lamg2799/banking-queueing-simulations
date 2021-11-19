package com.sim.proj;

import java.util.ArrayList;
import java.util.HashMap;

public class Output {

    // object variables

    private ArrayList<HashMap<String, Double>> multiServerResults;
    private ArrayList<HashMap<String, Double>> multiQueueResults;

    // singleton initialyze
    private static Output output;

    private Output() {

        multiQueueResults = new ArrayList<HashMap<String, Double>>(4);
        multiServerResults = new ArrayList<HashMap<String, Double>>(4);

    }

    public static Output getOutputInstance() {
        if (output == null) {
            output = new Output();

        }

        return output;
    }

    public void addMultiServerResult(HashMap<String, Double> h) {

        multiServerResults.add(h);
    }

    public void addMultiQueueResult(HashMap<String, Double> h) {

        multiQueueResults.add(h);
    }

    public int getMultiServerOptimalNumberOfServer(int maxQueueSize) {
        return -1;
    }

    public int getMultiQueueOptimalNumberOfServer(int maxQueueSize) {
        return -1;
    }

    public String getMultiQueueResults(int index) {

        return "Not done yet";
    }

    public String getMultiServerResults(int index) {

        return "Not done yet";
    }

    public void printMultiServerResults() {
        System.out.println("Printing multiserver results...");
        for (HashMap<String, Double> hashMap : multiServerResults) {
            System.out.println();

            for (String s : hashMap.keySet()) {
                System.out.println(s + ": " + hashMap.get(s));
            }
        }

    }

    public void printMultiQueueResults() {
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
