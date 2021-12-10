package com.sim.proj;

/**
 * Class Server to keep track of the multiple servers in simulations
 */
public class Server {
    /**
     * Static id to record last id
     */
    private static int initId = 0;

    /**
     * Server id
     */
    private int id;

    /**
     * Server type, either EXPERIENCED or PRIMARY
     */
    private ServerType type;

    /**
     * The total time the server was busy for all customers and simulations
     */
    private double totalTime;

    /**
     * The server status, either IDLE or BUSY
     */
    private Status status;

    /**
     * The server daily unit cost in $/day
     */
    private double dailyPay = 320.0;

    /**
     * The mean service time
     */
    private double meanServiceTime = 150;

    /**
     * The sigma service time
     */
    private double sigmaServiceTime = 80;

   /**
    * Constructor
    * @param meanServiceTime avg service time for this server
    * @param sigmaServiceTime avg service time sigma for this server
    * @param dailyPay the server daily pay rate
    * @param type the server type
    */
    public Server(double meanServiceTime, double sigmaServiceTime, double dailyPay, ServerType type) {

        this.meanServiceTime = meanServiceTime;
        this.sigmaServiceTime = sigmaServiceTime;
        this.dailyPay = dailyPay;
        this.type = type;
        this.id = initId++;

    }

    /**
     * Set the status as IDLE or BUSY
     * 
     * @param status the desired status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Set the total time that the server has served customers
     * 
     * @param serviceTime The time spent serving the current customer to add to the
     *                    total
     */
    public void addToTotalServiceTime(double serviceTime) {
        this.totalTime += serviceTime;
    }

    /**
     * get the status as IDLE or BUSY
     * 
     * @return status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * get the mean service time
     * 
     * @return mean service time
     */
    public double getMeanServiceTime() {
        return this.meanServiceTime;
    }

    /**
     * get the sigma service time
     * 
     * @return sigma service time
     */
    public double getSigmaServiceTime() {
        return this.sigmaServiceTime;
    }

    /**
     * get the daily pay
     * 
     * @return daily pay
     */
    public double getDailyPay() {
        return this.dailyPay;
    }

    /**
     * get the server type as PRIMARY or EXPERIENCED
     * 
     * @return type
     */
    public ServerType getType() {
        return this.type;
    }

    /**
     * get the total time serving customers
     * 
     * @return total time
     */
    public double getTotalTime() {
        return this.totalTime;
    }

}
