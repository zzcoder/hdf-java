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
LIBS =  $(HDF4LIB)/libmfhdf.$(LLEXT) $(HDF4LIB)/libdf.$(LLEXT) $(SZLIB) $(ZLIB) $(JPEGLIB) -lm

LIBDIR=$(JHVINST)

jhdf4_OBJECTS =  hdfstructsutil.o \
	hdfImp.o \
	hdfvgroupImp.o \
	hdfvdataImp.o \
	hdfsdsImp.o \
	hdfanImp.o \
	hdfdfanImp.o \
	hdfdfsdImp.o \
	hdfr24Imp.o \
	hdfdfpalImp.o \
	hdfr8Imp.o \
	hdfgrImp.o \
	hdfheImp.o \
	hdfdfuImp.o \
	hdfhxImp.o \
	hdfvfImp.o \
	hdfvhImp.o \
	hdfvqImp.o \
	hdfvsqImp.o \
	hdfexceptionImp.o \
	hdfdfcompImp.o \
	hdfnativeImp.o 

.c.o:
	$(CC) $(COPT) $(CFLAGS) -c $< -o $@


# make depends on the files specified in the objects macro defination
all:  jhdf4

jhdf4: $(jhdf4_OBJECTS) 
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/jhdf4.$(JSLEXT) $(LDOPT2) $(jhdf4_OBJECTS) $(LIBS)

clean:
	$(RM) -f *.o so_locations $(LIBDIR)/jhdf4.$(JSLEXT)


include $(top_srcdir)/config/conclude.am
