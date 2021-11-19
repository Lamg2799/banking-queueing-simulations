#!/usr/bin/env bash
#singleserver v1.0

read -p"Enter parameters nex, maxclock, mia, sia, ms, ss, ns md or nothing for default: " n nc ms ss ns md
while true; do
    if n=""; then
        n=1     # num execution should be set to 500
        nc=28800 #max clock
        ms=150   # mean service
        ss=80 # sigma service
        ns=3 # number of server
        md=2.0 # mean divider

    fi
    echo "Starting Simulation- with parameters ->  nex: $n, nc: $nc, ms $ms, ss: $ss, ns: $ns, md: $md"
    echo
    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/src/com/sim/proj && javac -classpath .:/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/lib/commons-math3-3.6.1/commons-math3-3.6.1-sources.jar:. -d /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin ./*.java

    cd /home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver/bin && java com.sim.proj.App "$n" "$nc" "$ms" "$ss" "$ns" "$md"

    echo
    echo "Simulation completed"
    echo

    read -p"Do you want to try again? " -n 1 -r
    echo

    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1

    fi

done