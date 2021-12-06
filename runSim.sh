#!/usr/bin/env bash
#Multiserver v1.0

localPath="C:\Users\Legia\Documents\Uni\Year4\Term3\CSI4124-Foundation Modelling & Simulation\CSI4124_group-4th"
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

cd "$localPath" && java -jar ./"$jarName" $meanDivider $maxQueueSize $maxTrial 
echo
echo "Simulation completed"
echo