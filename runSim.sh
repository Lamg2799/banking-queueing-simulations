#!/usr/bin/env bash
#Multiserver v1.0

localpath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcpath="/src/com/sim/proj"
#arguments
meandivider=2
maxqueuesize=2 
maxtrial=6

echo "Starting Multiserver"
echo "Compiling..."
echo
cd "$localpath$srcpath" && javac -classpath .:"$localpath"/lib/*:. -d "$localpath"/bin ./*.java
echo
echo "Compiling...Done"
echo
echo "Starting Simulation..."
cd "$localpath"/bin && java com.sim.proj.App $meandivider $maxqueuesize $maxtrial 

echo
echo "Simulation completed"
echo
