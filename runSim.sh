#!/usr/bin/env bash
#Multiserver v1.0

localPath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcPath="/com/sim/proj"
mainClass="com.sim.proj.App"
jarName="multiserver.jar"
libSrcPath="/lib/commons-math3-3.6.1-sources.jar"

#arguments
meanDivider=1
maxQueueSize=2 
maxTrial=6



echo "Starting Multiserver"
echo "Compiling..."

cd "$localPath"/src"$srcPath" && javac -cp .:"$localPath$libSrcPath":. -d "$localPath"/bin ./*.java

echo "Compiling...Done"
echo "Exporting jar..."

cd "$localPath"/bin && jar cfe ../"$jarName"  "$mainClass" ./*

echo "Exporting jar...Done"
echo "Starting Simulation..."
time=$(date +"%T")
echo "Results stored in file: results_$time"
cd "$localPath" && java -jar ./"$jarName" $meanDivider $maxQueueSize $maxTrial  > ./results_"$time"
echo
echo "Simulation completed"
echo
echo "Displaying output from results_$time"
echo
less -R ./results_"$time"
