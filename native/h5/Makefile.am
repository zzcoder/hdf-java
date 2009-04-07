#
# Copyright by The HDF Group.
# Copyright by the Board of Trustees of the University of Illinois.
# All rights reserved.
#
# This file is part of HDF Java Products. The full HDF Java copyright       *
# notice, including terms governing use, modification, and redistribution,  *
# is contained in the file, COPYING.  COPYING can be found at the root of   *
# the source code distribution tree. You can also access it online  at      *
# http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
# access to the file, you may request a copy from help@hdfgroup.org.        *
##
## Makefile.am
## Run automake to generate a Makefile.in from this file.
##
# 
# This makefile mostly just reinvokes make in the various subdirectories
# but does so in the correct order.  You can alternatively invoke make from
# each subdirectory manually.
#

include $(top_srcdir)/config/commence.am


CFLAGS = -D_FILE_OFFSET_BITS=64 $(DEFS) -I. -I$(HDF5INC) -I$(JAVAINC1) -I$(JAVAINC2)

LIBS =  $(HDF5LIB)/libhdf5.la $(ZLIB) $(SZLIB)

LIBDIR=$(JH5INST)

lib_LTLIBRARIES = libjhdf5.la
libjhdf5_la_LIBADD = $(LIBS) -lm

libjhdf5_la_SOURCES = H5constant.c \
	H5Dconstant.c \
	H5Econstant.c \
	H5Fconstant.c \
	H5Gconstant.c \
	H5Iconstant.c \
	H5Pconstant.c \
	H5Rconstant.c \
	H5Sconstant.c \
	H5Tconstant.c \
	H5Zconstant.c \
	exceptionImp.c \
	H5.c \
	H5E.c \
	H5F.c 
	
include_HEADERS = h5jni.h \
	H5constant.h \
	H5Dconstant.h \
	H5Econstant.h \
	H5Fconstant.h \
	H5Gconstant.h \
	H5Iconstant.h \
	H5Pconstant.h \
	H5Rconstant.h \
	H5Sconstant.h \
	H5Tconstant.h \
	H5Zconstant.h \
	H5.h \
	H5E.h \
	H5F.h 

include $(top_srcdir)/config/conclude.am
