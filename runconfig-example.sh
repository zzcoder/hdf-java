#!/bin/sh

## This is an example script to set the configure parameters for
## the HDF Java products.
##
## The paths need to be set according to the local configuration
##
## May need to adjust the arguments to configure
##
##

##
## IMPORTANT NOTE:  The make files require 'gmake'
##
##  Be sure to 'setenv MAKE gmake' if necessary
##

INSTDIR= #where to install the hdfview.

JAVAINC=  #path to java includes (jni.h, etc.)
JAVALIB=  #path to java lib (the rt.jar, etc.)

HDF5= # path to HDF5 installation
HDF4= # path to HDF4 installation (if used)
HDF45= # path to HDF4 to HDF5 installation (if used)

SZIP= # path to SZIP installation (normally not needed, should be in HDF5)

####

# recommend using 'h5cc' to build
CC=$HDF5/bin/h5cc
export CC

./configure --prefix=$INSTDIR --with-jdk=$JAVAINC,$JAVALIB --with-hdf5=$HDF5/include,$HDF5/lib --with-hdf4=$HDF4/include,$HDF4/lib

# other options
#  --with-libsz=$SZIP"/include,"$SZIP"/lib" # if need to tell where SZIP is
#  --with-h5toh5=$HDF45"/include,"$HDF45"/lib" # if want h4toh5lib configured
#  --with-libz= # path to gzip lib, if needed  (typically found by h5cc)
#  
#  --without-hdf4  -- omit HDF4
#  --without-libsz  -- omit SZIP

#
#  Some options used for macOSX
# 
#  -host=powerpc-apple 
#  --with-jdkclasses=   # path to classes if not in 'jdk/lib'
#  --with-javabin=  # path to java bin, if not in 'jdk/bin'
