package com.sim.proj;

/**
 * Class Event, entities to be proccessed in the simulations
 * 
 * @author Samuel Garneau, Oct 16, 2021, Ottawa, On
 * @version 1.0
 */
class Event {

    /**
     * The event scheduled start time
     */
    private Double time;
    /**
     * Event type, either ARRIVAL or DEPARTURE
     */
    private EventType type;

    /**
     * Customer subject of the event
     */
    private Customer customer;

    /**
     * Constructor
     * 
     * @param ty departure or arrival
     * @param ti scheduled time of event
     * @param c the customer subject of the event 
     */
    public Event(EventType ty, double ti, Customer c) {
        // the time the event will happen
        time = ti;
        // type of event either ARRIVE or DEPART
        type = ty;
        customer = c;

    }

    /**
     * The event time
     * 
     * @return time of event
     */
    public Double getTime() {
        return time;
    }

    /**
     * The event type (ARRIVAL or DEPARTURE)
     * 
     * @return ARRIVAL or DEPARTURE
     */
    public EventType getType() {
        return type;
    }

    /**
     * The customer subject of the event
     * 
     * @return the customer subject of the event 
     */
    public Customer getCustomer() {
        return customer;
    }

}