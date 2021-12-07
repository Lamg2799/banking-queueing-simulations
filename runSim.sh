#!/usr/bin/env bash
#Multiserver v1.0

localPath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcPath="/com/sim/proj"
mainClass="com.sim.proj.App"
jarName="multiserver.jar"
libSrcPath="/lib/commons-math3-3.6.1-sources.jar"

#arguments
meanDivider=1
<<<<<<< HEAD
maxQueueSize=2
maxTrial=6
resultLevel=1
=======
maxQueueSize=4
maxTrial=6
resultLevel=0
>>>>>>> show_random_values

echo "Starting Multiserver"
echo "Compiling..."

cd "$localPath"/src"$srcPath" && javac -cp .:"$localPath$libSrcPath":. -d "$localPath"/bin ./*.java

echo "Compiling...Done"
echo "Exporting jar..."

cd "$localPath"/bin && jar cfe ../"$jarName" "$mainClass" ./*

echo "Exporting jar...Done"
echo "Starting Simulation..."
time=$(date +"%T")
echo "Results stored in file: results_$time"
file="./results_$time"
if ((resultLevel > 4)); then
    file="./results_$time.csv"
fi
<<<<<<< HEAD
cd "$localPath"/bin && java "$mainClass" $meanDivider $maxQueueSize $maxTrial $resultLevel >"$file"
=======
cd "$localPath" && java -jar ./"$jarName" $meanDivider $maxQueueSize $maxTrial $resultLevel >"$file"
>>>>>>> show_random_values
echo
echo "Simulation completed"
echo
echo "Displaying output from results_$time"
echo
less -R "$file"
