#!/bin/sh

TESTS="apitest imagetest nametest sdstest vdatatest vgrouptest"

JH45INSTALLDIR=/afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/

LIBDIR=$JH45INSTALLDIR"/lib"

CLASSPATH=".:"$LIBDIR"/jh4toh5.jar:"$LIBDIR"/jhdf.jar:"$LIBDIR"/jhdf5.jar"

LD_LIBRARY_PATH=$LIBDIR"/linux"

export CLASSPATH
export LD_LIBRARY_PATH

cp testfiles/*.hdf .

for i in $TESTS
do

/usr/local/jdk1.3.1/bin/java -Djava.library.path=$LD_LIBRARY_PATH -Dncsa.hdf.hdf5lib.H5.hdf5lib=$LIBDIR"/linux/libjh4toh5.so" -Dncsa.hdf.h4toh5lib.h4toh5.h45lib=$LIBDIR"/linux/libjh4toh5.so" -Dncsa.hdf.hdflib.HDFLibrary.hdflib=$LIBDIR"/linux/libjh4toh5.so" -Dncsa.hdf.libh4toh5.h4toh5.h45lib=$LIBDIR"/linux/libjh4toh5.so" $i

# check against correct output....

done

