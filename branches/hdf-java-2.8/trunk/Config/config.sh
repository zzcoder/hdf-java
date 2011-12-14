#!/bin/bash

###########################################################
#      Sample configuration file to build hdf-java        #
###########################################################
BUILDDIR=/mnt/scr1/xcao/hdf_java/build
LIBDIR=/mnt/scr1/xcao/hdf_java/lib

############################################################
#                     linux32 (jam)                        #
############################################################
#MACHINE=linux32
#JAVAINC=/usr/lib/jvm/java/include
#JAVALIB=/usr/lib/jvm/java/jre/lib
#EXTRA_FLAGS=--with-javax="-Xmx1024m"

############################################################
#                     linux64 (koala)                      #
############################################################
#MACHINE=linux64
#JAVAINC=/usr/lib/jvm/java/include
#JAVALIB=/usr/lib/jvm/java/jre/lib
#export CC="gcc -fPIC"

############################################################
#                     solaris32 (linew)                    #
############################################################
#MACHINE=solaris32
#JAVAINC=/usr/java/jdk1.6.0_12/include
#JAVALIB=/usr/java/jdk1.6.0_12/jre/lib
#export CC="/opt/SUNWspro/bin/cc"
#EXTRA_FLAGS=--with-javax="-Xmx1024m"

############################################################
#                     solaris64 (linew)                    #
############################################################
#MACHINE=solaris64
#JAVAINC=/usr/java/jdk1.6.0_12/include
#JAVALIB=/usr/java/jdk1.6.0_12/jre/lib
#JAVABIN=/usr/java/jdk1.6.0_12/bin/sparcv9
#export CC="/opt/SUNWspro/bin/cc -KPIC -m64"
#EXTRA_FLAGS=--with-javabin=$JAVABIN 

############################################################
#                     macintel32 (tejeda)                  #
############################################################
#MACHINE=macintel32
#BUILDDIR=/Users/xcao/hdf_java/build
#LIBDIR=/Users/xcao/hdf_java/lib
#JAVAINC=/System/Library/Frameworks/JavaVM.framework/Headers
#JAVALIB=/System/Library/Frameworks/JavaVM.framework/Libraries
#EXTRA_FLAGS="--with-javax=-Xmx1024m -build=powerpc-apple --with-jdkclasses=/System/Library/Frameworks/JavaVM.framework/Classes --with-javabin=/System/Library/Frameworks/JavaVM.framework/Commands"

############################################################
#                     macintel64 (fred)                    #
############################################################
#MACHINE=macintel64
#BUILDDIR=/Users/xcao/hdf_java/build
#LIBDIR=/Users/xcao/hdf_java/lib
#JAVAINC=/System/Library/Frameworks/JavaVM.framework/Headers
#JAVALIB=/System/Library/Frameworks/JavaVM.framework/Libraries
#EXTRA_FLAGS="-build=powerpc-apple --with-jdkclasses=/System/Library/Frameworks/JavaVM.framework/Classes --with-javabin=/System/Library/Frameworks/JavaVM.framework/Commands"
#export CC="gcc -mmacosx-version-min=10.5 -isysroot /Developer/SDKs/MacOSX10.5.sdk -fPIC"


############################################################
#               Do not change below this line              #
############################################################
INSTDIR=$BUILDDIR/$MACHINE
HDF4=$LIBDIR/$MACHINE/hdf4
HDF5=$LIBDIR/$MACHINE/hdf5
JPEG=$LIBDIR/$MACHINE/jpeg
SZIP=$LIBDIR/$MACHINE/szip
ZLIB=$LIBDIR/$MACHINE/zlib
rm -rf $INSTDIR; mkdir $INSTDIR
./configure $EXTRA_FLAGS --prefix=$INSTDIR \
--with-jdk=$JAVAINC,$JAVALIB \
--with-hdf5=$HDF5/include,$HDF5/lib \
--with-hdf4=$HDF4/include,$HDF4/lib \
--with-libjpeg=$JPEG/include,$JPEG/lib \
--with-libsz=$SZIP/include,$SZIP/lib \
--with-libz=$ZLIB/include,$ZLIB/lib \

