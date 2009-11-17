#
# Copyright by The HDF Group.
# Copyright by the Board of Trustees of the University of Illinois.
# All rights reserved.
#
# This file is part of HDFJava.  The full HDFJava copyright notice, including
# terms governing use, modification, and redistribution, is contained in
# the files COPYING and Copyright.html.  COPYING can be found at the root
# of the source code distribution tree; Copyright.html can be found at the
# root level of an installed copy of the electronic HDF5 document set and
# is linked from the top-level documents page.  It can also be found at
# http://hdfgroup.org/hdf-java/doc/Copyright.html.  If you do not have
# access to either file, you may request a copy from help@hdfgroup.org.
##
## Makefile.am
## Run automake to generate a Makefile.in from this file.
##
# 
# This makefile mostly just reinvokes make in the various subdirectories
# but does so in the correct order.  You can alternatively invoke make from
# each subdirectory manually.
#

# Our main target, the HDF5 library
lib_LIBRARIES=libjhdf5.a

# Add libtool numbers to the HDF5 library (from config/lt_vers.am)
libjhdf5_a_LDFLAGS= -version-info $(LT_VERS_INTERFACE):$(LT_VERS_REVISION):$(LT_VERS_AGE)

# library sources
libjhdf5_a_SOURCES= exceptionImp.c h5Constants.c h5Imp.c h5aImp.c h5dImp.c h5eImp.c \
                    h5fImp.c h5gImp.c h5iImp.c h5lImp.c h5pImp.c h5rImp.c h5sImp.c \
                    h5tImp.c nativeData.c h5util.c h5zImp.c

# Public headers
include_HEADERS = h5Constants.h h5util.h 

