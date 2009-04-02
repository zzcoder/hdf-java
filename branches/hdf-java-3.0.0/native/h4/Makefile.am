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

CFLAGS = $(DEFS) -I. -I$(JAVAINC) -I$(JAVAINC2) -I$(HDF4INC)

# Be sure to add -lbsd if compiling in a SYSV environment
LIBS =  $(HDF4LIB)/libmfhdf.la $(HDF4LIB)/libdf.la $(SZLIB) $(ZLIB) $(JPEGLIB4)

LIBDIR=$(JHVINST)

lib_LTLIBRARIES = libjhdf4.la
libjhdf4_la_LIBADD = $(LIBS) -lm

libjhdf4_la_SOURCES =  hdfstructsutil.c \
	hdfImp.c \
	hdfvgroupImp.c \
	hdfvdataImp.c \
	hdfsdsImp.c \
	hdfanImp.c \
	hdfdfanImp.c \
	hdfdfsdImp.c \
	hdfr24Imp.c \
	hdfdfpalImp.c \
	hdfr8Imp.c \
	hdfgrImp.c \
	hdfheImp.c \
	hdfdfuImp.c \
	hdfhxImp.c \
	hdfvfImp.c \
	hdfvhImp.c \
	hdfvqImp.c \
	hdfvsqImp.c \
	hdfexceptionImp.c \
	hdfdfcompImp.c \
	hdfnativeImp.c 

include_HEADERS =  h4jni.h


include $(top_srcdir)/config/conclude.am
