#!/usr/bin/env bash
#singleserver v1.0


    echo "Starting Simulation.."
    echo
    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/src/com/sim/proj && javac -classpath .:/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:. -d /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin ./*.java

    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin && java com.sim.proj.App 

    echo
    echo "Simulation completed"
    echo

    