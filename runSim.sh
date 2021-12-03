#!/usr/bin/env bash
#Multiserver v1.0

localpath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcpath="/com/sim/proj"
mainclass=com.sim.proj.App
libpath=/org/apache/commons/math3

#arguments
meandivider=2
maxqueuesize=2 
maxtrial=6



echo "Starting Multiserver"
echo "Compiling..."
echo "Compiling...Done"

cd "$localpath/src$srcpath" && javac -classpath .:"$localpath"/lib/commons-math3-3.6.1-sources.jar:. -d "$localpath"/bin ./*.java

echo "Exporting jar..."

cd "$localpath"/bin && jar cfe ../multiserver.jar  $mainclass ."$srcpath" ."$libpath"

echo "Exporting jar...Done"
echo "Starting Simulation..."

cd "$localpath"/bin && java $mainclass $meandivider $maxqueuesize $maxtrial 
echo
echo "Simulation completed"
echo
