#!/bin/sh
set -x

# 1 ===================== setting for heping
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/linux
#JAVAINC=/usr/java/j2sdk1.4.2_06/include
#JAVALIB=/usr/java/j2sdk1.4.2_06/jre/lib
#HDF4=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/linux/hdf4
#HDF5=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/linux/hdf5
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/linux/szip
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/linux/zlib
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/linux/jpeg

# 2 ===================== setting for copper 
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/aix
#JAVAINC=/usr/java14/include
#JAVALIB=/usr/java14/jre/lib
#HDF4=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/aix/hdf4
#HDF5=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/aix/hdf5
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/aix/szip
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/aix/zlib
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/aix/jpeg
#export CC=/usr/vacpp/bin/xlc

# 3 ===================== setting for shanti (solaris)
#   must use short path, otherwise, expr error
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/solaris
#JAVAINC=/usr/j2se/include
#JAVALIB=/usr/j2se/jre/lib
#HDF4=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/solaris/hdf4
#HDF5=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/solaris/hdf5
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/solaris/szip
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/solaris/zlib
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/solaris/jpeg
#HDF4=/tmp/solaris/hdf4;HDF5=/tmp/solaris/hdf5;SZIP=/tmp/solaris/szip;GZIP=/tmp/solaris/zlib;JPEG=/tmp/solaris/jpeg
#CC=/opt/SUNWspro/bin/cc
#export CC

# 4 ====================== setting for tcs.psc.edu -l pcao
#INSTDIR=/usr/users/1/pcao/osf1
#JAVAINC=/usr/opt/java142/include
#JAVALIB=/usr/opt/java142/jre/lib
#HDF4=/usr/users/1/pcao/hdf4.2r1
#HDF5=/usr/users/1/pcao/hdf5.165
#SZIP=/usr/users/1/pcao/lib_external/szip2.0-osf1-enc
#GZIP=/usr/users/1/pcao/lib_external/zlib-1.2.1
#JPEG=/usr/users/1/pcao/lib_external/jpeg-6b
#CC=/bin/cc
#export CC

# 5 ===================== setting for modi4 (irix)
#   need build jpeg with "cc -n32" for 32-bit jpeg
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/irix
#JAVAINC=/usr/java2/include
#JAVALIB=/usr/java2/jre/lib
#HDF4=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/irix/hdf4
#HDF5=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/irix/hdf5
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/irix/szip
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/irix/zlib
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib/irix/jpeg
#export CC=/opt/MIPSpro/bin/cc

# 6 ===================== setting for pommier
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/macosx
#JAVAINC=/System/Library/Frameworks/JavaVM.framework/Headers
#JAVALIB=/System/Library/Frameworks/JavaVM.framework/Libraries
#HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-macosx
#HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-macosx
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/szip/Darwin-7.7
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/packages/jpeg/MacOSX
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
#export CC=/usr/bin/cc
#-build=powerpc-apple \
#--with-jdkclasses=/System/Library/Frameworks/JavaVM.framework/Classes \
#--with-javabin=/System/Library/Frameworks/JavaVM.framework/Commands \

# 7 ===================== setting for mir -- AMD 64-bit linux
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/linux64
#JAVAINC=/usr/java/jdk1.5.0_05/include
#JAVALIB=/usr/java/jdk1.5.0_05/jre/lib
#HDF4=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib64/linux/hdf4
#HDF5=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib64/linux/hdf5
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib64/linux/szip
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib64/linux/zlib
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/lib64/linux/jpeg

# 8 ===================== setting for cobalt (sgi altix ia64-pc-linux)
#INSTDIR=/u/ncsa/xcao/java/linux
#JAVAINC=/usr/local/java/j2sdk1.4.2_05/includue
#JAVALIB=/usr/local/java/j2sdk1.4.2_05/jre/lib
#HDF5=/u/ncsa/xcao/_o_direct/build
#SZIP=/usr
#GZIP=/usr
#JPEG=/usr

./configure --prefix=$INSTDIR \
--with-jdk=$JAVAINC,$JAVALIB \
--with-hdf5=$HDF5/include,$HDF5/lib \
--with-hdf4=$HDF4/include,$HDF4/lib \
--with-libsz=$SZIP/include,$SZIP/lib \
--with-libz=$GZIP/include,$GZIP/lib \
--with-libjpeg=$JPEG/include,$JPEG/lib
 
