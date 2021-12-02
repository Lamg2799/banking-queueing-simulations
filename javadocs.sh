#!/usr/bin/env bash


localpath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcpath="/src/com/sim/proj"

javadoc -private -classpath .:"$localpath"/lib/*:. -d "$localpath"/javadocs  "$localpath$srcpath"/* 

