#!/usr/bin/env bash
#Multiserver v1.0

echo "Starting Multiserver"
echo "Compiling..."
echo
cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/src/com/sim/proj && javac -classpath .:/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:. -d /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin ./*.java
echo
echo "Compiling...Done"
echo
echo "Starting Simulation..."
cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin && java com.sim.proj.App

echo
echo "Simulation completed"
echo
