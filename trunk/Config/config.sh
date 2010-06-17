#!/bin/bash

###########################################################
#      Sample configuration file to build hdf-java        #
###########################################################
EXTRA_FLAGS=

############################################################
#                     linux32 (jam)                        #
############################################################
#INSTDIR=/mnt/scr1/xcao/hdf_java/src/hdf-java/build
#JAVAINC=/usr/java/jdk-106/jdk/include
#JAVALIB=/usr/java/jdk-106/jdk/jre/lib
#HDF4=/mnt/hdf/packages/hdf4/v425/Linux_2.6/hdf-4.2.5-linux
#HDF5=/mnt/hdf/packages/hdf5/v185/Linux_2.6/hdf5-1.8.5-linux-static
#JPEG=/mnt/scr1/xcao/hdf_java/lib/linux32/jpeg
#SZIP=/mnt/scr1/xcao/hdf_java/lib/linux32/szip
#ZLIB=/mnt/scr1/xcao/hdf_java/lib/linux32/zlib

############################################################
#                     solaris32 (linew)                    #
############################################################
#INSTDIR=/mnt/scr1/xcao/hdf_java/bin/solaris32
#JAVAINC=/usr/java/jdk1.6.0_12/include
#JAVALIB=/usr/java/jdk1.6.0_12/jre/lib
#HDF4=/mnt/hdf/packages/hdf4/v425/SunOS_5.10/hdf-4.2.5-solaris
#HDF5=/mnt/hdf/packages/hdf5/v185/SunOS_5.10/hdf5-1.8.5-solaris-static
#JPEG=/mnt/scr1/xcao/hdf_java/lib/solaris32/jpeg
#SZIP=/mnt/scr1/xcao/hdf_java/lib/solaris32/szip
#ZLIB=/mnt/scr1/xcao/hdf_java/lib/solaris32/zlib
#export CC="/opt/SUNWspro/bin/cc"

############################################################
#                     solaris64 (linew)                    #
############################################################
#INSTDIR=/mnt/scr1/xcao/hdf_java/bin/solaris64
#JAVAINC=/usr/java/jdk1.6.0_12/include
#JAVALIB=/usr/java/jdk1.6.0_12/jre/lib
#JAVABIN=/usr/java/jdk1.6.0_12/bin/sparcv9
#HDF4=/mnt/scr1/xcao/hdf_java/lib/solaris64/hdf4
#HDF5=/mnt/scr1/xcao/hdf_java/lib/solaris64/hdf5
#JPEG=/mnt/scr1/xcao/hdf_java/lib/solaris64/jpeg
#SZIP=/mnt/scr1/xcao/hdf_java/lib/solaris64/szip
#ZLIB=/mnt/scr1/xcao/hdf_java/lib/solaris64/zlib
#export CC="/opt/SUNWspro/bin/cc -KPIC -m64"
#EXTRA_FLAGS=--with-javabin=$JAVABIN 

############################################################
#                     linux64 (amani)                     #
############################################################
#INSTDIR=/mnt/scr1/xcao/hdf_java/bin/linux64
#JAVAINC=/mnt/scr1/xcao/jdk/linux64/jdk1.6.0_11/include
#JAVALIB=/mnt/scr1/xcao/jdk/linux64/jdk1.6.0_11/jre/lib
#HDF4=/mnt/scr1/xcao/hdf_java/lib/linux64/hdf4
#HDF5=/mnt/scr1/xcao/hdf_java/lib/linux64/hdf5
#JPEG=/mnt/scr1/xcao/hdf_java/lib/linux64/jpeg
#SZIP=/mnt/scr1/xcao/hdf_java/lib/linux64/szip
#ZLIB=/mnt/scr1/xcao/hdf_java/lib/linux64/zlib
#export CC="gcc -fPIC"

############################################################
#                     macintel (tejeda)                    #
############################################################
#INSTDIR=/Users/xcao/work/build/hdf-java
#JAVAINC=/System/Library/Frameworks/JavaVM.framework/Headers
#JAVALIB=/System/Library/Frameworks/JavaVM.framework/Libraries
#HDF4=/Users/xcao/work/build/hdf4
#HDF5=/Users/xcao/work/build/hdf5
#JPEG=/Users/xcao/work/lib-external/jpeg-6b
#SZIP=/Users/xcao/work/lib-external/szip-2.1
#ZLIB=/Users/xcao/work/lib-external/zlib-1.2.1
#EXTRA_FLAGS="-build=powerpc-apple --with-jdkclasses=/System/Library/Frameworks/JavaVM.framework/Classes --with-javabin=/System/Library/Frameworks/JavaVM.framework/Commands"


############################################################
#               Do not change below this line              #
############################################################
rm -rf $INSTDIR; mkdir $INSTDIR
./configure $EXTRA_FLAGS --prefix=$INSTDIR \
--with-jdk=$JAVAINC,$JAVALIB \
--with-hdf5=$HDF5/include,$HDF5/lib \
--with-hdf4=$HDF4/include,$HDF4/lib \
--with-libjpeg=$JPEG/include,$JPEG/lib \
--with-libsz=$SZIP/include,$SZIP/lib \
--with-libz=$ZLIB/include,$ZLIB/lib \

