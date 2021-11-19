#!/usr/bin/env bash
#singleserver v1.0

read -p"Enter parameters nex, maxclock, mia, sia, ms, ss, ns or nothing for default: " n nc ms ss ns
while true; do
    if n=""; then
        n=1     # num execution should be set to 500
        nc=28800 #max clock
        ms=150   #
        ss=80
        ns=4

    fi
    echo "Starting Simulation- with parameters ->  nex: $n, nc: $nc, ms $ms, ss: $ss, ns: $ns"
    echo
    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/src/com/sim/proj && javac -classpath .:/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:. -d /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin ./*.java

    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin && java com.sim.proj.App "$n" "$nc" "$ms" "$ss" "$ns"

    echo
    echo "Simulation completed"
    echo

    read -p"Do you want to try again? " -n 1 -r
    echo

    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1

    fi

done
