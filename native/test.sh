#!/bin/sh

export CLASSPATH="jhdfobj.jar:h5srb.jar:."
export LD_LIBRARY_PATH=".:"$LD_LIBRARY_PATH

javac H5SRBTest.java 
java -Djava.library.path=$LD_LIBRARY_PATH H5SRBTest /tempZone/home/rods/test.h5 $1

