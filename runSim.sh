#!/usr/bin/env bash
#Multiserver v1.0

localPath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcPath="/com/sim/proj"
mainClass="com.sim.proj.App"
jarName="CSI4124_group-4th.jar"
libSrcPath="/lib/commons-math3-3.6.1-sources.jar"

#arguments
meanDivider=1
maxQueueSize=5
maxTrial=1
resultLevel=1

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
file="../results_$time"
if ((resultLevel > 4)); then
    file="../results_$time.csv"
fi
cd "$localPath"/bin && java "$mainClass" $meanDivider $maxQueueSize $maxTrial $resultLevel >"$file"
echo
timeend=$(date +"%T")
echo "Simulation completed $timeend"
 difference=$(( $(date -d "$timeend" "+%s") - $(date -d "$time" "+%s") ))

echo "Execution time = $difference seconds"

echo
echo "Displaying output from results_$time"
echo
less -R "$file"
