#!/bin/sh
#****************************************************************************
#* NCSA HDF                                                                 *
#* National Comptational Science Alliance                                   *
#* University of Illinois at Urbana-Champaign                               *
#* 605 E. Springfield, Champaign IL 61820                                   *
#*                                                                          *
#* For conditions of distribution and use, see the accompanying             *
#* java-hdf5/COPYING file.                                                  *
#*                                                                          *
#****************************************************************************/

##  These paths are taken from the configure for the Make
##  This program checks that the libraries are installed
##  in these paths.
##
##  Edit these paths to debug
##

JH5INSTALLDIR=/home/abyrne/HDF_Projects/HDFJava-J-Trunk/bin
RM=/bin/rm

LIBDIR=$JH5INSTALLDIR"/lib"

CLASSPATH="..:"$LIBDIR"/jhdf5.jar"

LD_LIBRARY_PATH=$LIBDIR"/linux:"$LIBDIR
DYLD_LIBRARY_PATH=$LIBDIR"/linux:"$LIBDIR

export CLASSPATH
export LD_LIBRARY_PATH
export DYLD_LIBRARY_PATH

/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/bin/java -Djava.library.path=$LD_LIBRARY_PATH "$1" > "$1".out
if diff "$1".out testfiles/"$1".txt > /dev/null; 
then exit 0
else exit 1
fi
