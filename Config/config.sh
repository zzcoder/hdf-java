#!/bin/sh
set -x

# ===================== setting for heping
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/linux
#JAVAINC=/usr/java/j2sdk1.4.2_06/include
#JAVALIB=/usr/java/j2sdk1.4.2_06/jre/lib
#HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-linux
#HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-linux-gcc
#SZIP=/afs/ncsa.uiuc.edu/projects/hdf/packages/szip/Linux2.4
#GZIP=/afs/ncsa.uiuc.edu/projects/hdf/packages/zlib/Linux2.4
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/jpeg/build
#export CC=/usr/sdt/bin/gcc-3.3

# ===================== setting for copper 
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/aix
#JAVAINC=/usr/java14/include
#JAVALIB=/usr/java14/jre/lib
#HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-aix5.1-32
#HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-aix
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/szip/AIX5.1
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/zlib/AIX5.1
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/packages/jpeg/AIX5.1
#export CC=/usr/vacpp/bin/xlc

# ===================== setting for shanti 
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/solaris
#JAVAINC=/usr/j2se/include
#JAVALIB=/usr/j2se/jre/lib
#HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-solaris2.8-32
#HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-solaris
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/szip/SunOS-5.9
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/zlib/SunOS-5.9
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/packages/jpeg/SunOS-5.9
#CC=/opt/SUNWspro/bin/cc
#export CC

# ====================== setting for tcs.psc.edu -l pcao
#INSTDIR=/usr/users/1/pcao/osf1
#JAVAINC=/usr/opt/java142/include
#JAVALIB=/usr/opt/java142/jre/lib
#HDF4=/usr/users/1/pcao/hdf4.2r1
#HDF5=/usr/users/1/pcao/hdf5.164
#SZIP=/usr/users/1/pcao/lib_external/szip2.0-osf1-enc
#GZIP=/usr/users/1/pcao/lib_external/zlib-1.2.1
#JPEG=/usr/users/1/pcao/lib_external/jpeg-6b
#CC=/bin/cc
#export CC

# ===================== setting for premium 
#INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/irix
#JAVAINC=/usr/java/include
#JAVALIB=/usr/java/jre/lib
#HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-irix64-n32
#HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-irix64-n32
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/szip/IRIX64-6.5-32bit
#GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/zlib/IRIX64-6.5-32bit
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/packages/jpeg/IRIX64-6.5-32bit
#export CC=/opt/MIPSpro/bin/cc

# ===================== setting for pommier
INSTDIR=/afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/java/macosx
JAVAINC=/System/Library/Frameworks/JavaVM.framework/Headers
JAVALIB=/System/Library/Frameworks/JavaVM.framework/Libraries
HDF4=/afs/ncsa.uiuc.edu/packages/hdf/4.2r1-macosx
HDF5=/afs/ncsa.uiuc.edu/packages/hdf5/5-1.6.4-macosx
SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/packages/szip/Darwin-7.7
JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/packages/jpeg/MacOSX
#SZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
GZIP=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
#JPEG=/afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/macosx/ext
export CC=/usr/bin/cc


./configure --prefix=$INSTDIR \
-build=powerpc-apple \
--with-jdkclasses=/System/Library/Frameworks/JavaVM.framework/Classes \
--with-javabin=/System/Library/Frameworks/JavaVM.framework/Commands \
--with-jdk=$JAVAINC,$JAVALIB \
--with-hdf5=$HDF5/include,$HDF5/lib \
--with-hdf4=$HDF4/include,$HDF4/lib \
--with-libsz=$SZIP/include,$SZIP/lib \
--with-libz=$GZIP/include,$GZIP/lib \
--with-libjpeg=$JPEG/include,$JPEG/lib
 
