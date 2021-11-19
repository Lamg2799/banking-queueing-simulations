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
     * 
     * @param name
     */
    public CustomerQueue() {

        queue = new LinkedList<Event>();

    }

    /**
     * Adds a customer in the waiting line
     * 
     * @param c
     */
    public void enqueue(Event c) {

        queue.add(c);
        if (queue.size() > maxQueueSize) {
            maxQueueSize = queue.size();
        }

    }

    /**
     * Retrieve customer in waiting line
     * 
     * @return event
     */
    public Event dequeue() {

        return queue.removeFirst();

    }

    /**
     * Check whether a customer is waiting in line
     * 
     * @return bool
     */
    public boolean isEmpty() {
        return queue.size() == 0;
    }

    /**
     * Return the max number of customer waiting in line
     * 
     * @return maxQueueSize
     */
    public int getMaxQS() {
        return maxQueueSize;
    }
}