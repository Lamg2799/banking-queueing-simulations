#!/usr/bin/env bash
#Multiserver v1.0

localpath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
#arguments
meandivider=2
maxqueuesize=2 
maxtrial=6

echo "Starting Multiserver"
echo "Compiling..."
echo
cd "$localpath"/src/com/sim/proj && javac -classpath .:"$localpath"/lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:. -d "$localpath"/bin ./*.java
echo
echo "Compiling...Done"
echo
echo "Starting Simulation..."
cd "$localpath"/bin && java com.sim.proj.App $meandivider $maxqueuesize $maxtrial 

echo
echo "Simulation completed"
echo
