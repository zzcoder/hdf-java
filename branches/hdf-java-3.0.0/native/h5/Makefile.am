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

LIBS =  $(HDF5LIB)/libhdf5.$(LLEXT) $(ZLIB) $(SZLIB) -lm

LIBDIR=$(JH5INST)

# Macro def: object filename used
jdfh5_OBJECTS =  exceptionImp.o \
	H5.o 


.c.o:
	$(CC) $(COPT) $(CFLAGS) -c $< -o $@

# make depends on the files specified in the objects macro defination
all:  jhdf5

jhdf5: $(jhdf5_OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/jhdf5.$(JSLEXT) $(LDOPT2) $(jhdf5_OBJECTS) $(LIBS)

clean:
	$(RM) -f *.o so_locations $(LIBDIR)/jhdf5.$(JSLEXT)

include $(top_srcdir)/config/conclude.am
