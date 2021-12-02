package com.sim.proj;

import java.util.LinkedList;

/**
 * Customer's waiting line when server is busy
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
class CustomerQueue {

    /**
     * the linked list to store the customers
     */
    private LinkedList<Event> queue;
    /**
     * Store the maximum number of customer waiting in line for all simulations
     */
    private int maxQueueSize = 0;

    /**
     * Constructor
     */
    public CustomerQueue() {

        queue = new LinkedList<Event>();

    }

    /**
     * Adds a customer in the waiting line
     * 
     * @param e the arrival event to add in waiting line
     */
    public void enqueue(Event e) {

        queue.add(e);
        if (queue.size() > maxQueueSize) {
            maxQueueSize = queue.size();
        }

    }

    /**
     * Retrieve customer in waiting line
     * 
     * @return the customer in waiting line
     */
    public Event dequeue() {

        return queue.removeFirst();

    }

    /**
     * Check whether a customer is waiting in line
     * 
     * @return true if the waiting line is empty
     */
    public boolean isEmpty() {
        return queue.size() == 0;
    }

    /**
     * Return the max number of customer waiting in line
     * 
     * @return the max number of customer waiting in line
     */
    public int getMaxQS() {
        return maxQueueSize;
    }
}