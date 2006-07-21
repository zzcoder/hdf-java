#!/bin/bash

PRO_NAME=$1
export PRO_NAME

rm $PRO_NAME.class

# heping (32bit linux)
#export OS_NAME=linux
#export JAVA_HOME=/usr/java/jdk1.5.0_05
#export HDF_HOME=/afs/ncsa.uiuc.edu/projects/hdf/packages/hdfview/linux
#export JAVA_EXE=$JAVA_HOME/bin/java
#export JAVA_CC=$JAVA_HOME/bin/javac
#export LIB_DIR=$HDF_HOME/lib

# mir (64bit Linux)
#export OS_NAME=linux
#export JAVA_HOME=/usr/java/jdk1.5.0_05
#export HDF_HOME=/afs/ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/linux64amd
#export JAVA_EXE=$JAVA_HOME/bin/java
#export JAVA_CC=$JAVA_HOME/bin/javac
#export LIB_DIR=$HDF_HOME/lib

# shanti (64bit solaris)
export OS_NAME=solaris
export JAVA_HOME=/usr/jdk1.5.0_06
export HDF_HOME=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/solaris64
export JAVA_EXE=$JAVA_HOME/bin/sparcv9/java
export JAVA_CC=$JAVA_HOME/bin/sparcv9/javac
export LIB_DIR=$HDF_HOME/lib

export CLASSPATH=.:$LIB_DIR/jhdf5.jar:$LIB_DIR/jhdfobj.jar:$LIB_DIR/jhdf5obj.jar
$JAVA_CC -classpath "$CLASSPATH" $PRO_NAME".java"
$JAVA_EXE -Xmx1024M -Dncsa.hdf.hdf5lib.H5.hdf5lib=$LIB_DIR/$OS_NAME/libjhdf5.so -classpath "$CLASSPATH" $PRO_NAME 


