#!/usr/bin/env bash


localPath="/home/sam/Documents/UOttawa_21-22/Live_code/Java_Projects/multiserver"
srcPath="/src/com/sim/proj"

javadoc -private -cp .:"$localPath"/lib/*:. -d "$localPath"/javadocs  "$localPath$srcPath"/* 

