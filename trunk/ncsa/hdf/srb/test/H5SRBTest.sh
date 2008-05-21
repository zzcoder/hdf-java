#!/bin/sh

HDFJAVA=/home/srb/hdf-java/build

export CLASSPATH=$HDFJAVA"/lib/jhdfobj.jar:"$HDFJAVA"/lib/h5srb.jar:."
export LD_LIBRARY_PATH=$HDFJAVA"/lib/linux:"$LD_LIBRARY_PATH

javac H5SRBTest.java 
java -Djava.library.path=$LD_LIBRARY_PATH H5SRBTest /tempZone/home/rods/test.h5 $1

