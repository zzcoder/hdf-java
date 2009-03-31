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

include $(top_srcdir)/config/commence.am


CFLAGS = -D_FILE_OFFSET_BITS=64 $(DEFS) -I. -I$(HDF5INC) -I$(JAVAINC1) -I$(JAVAINC2)

LIBS =  $(HDF5LIB)/libh5.$(LLEXT) $(ZLIB) $(SZLIB) -lm

# Macro def: object filename used
libjh5_OBJECTS =  exceptionImp.o \
	h5Constants.o \
	h5Imp.o \
	h5util.o


.c.o:
	$(CC) $(CFLAGS) -c $< -o $@

# make depends on the files specified in the objects macro defination
all:  libjh5

h5lib: libjh5

libjh5: $(libjh5_OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjh5.$(JSLEXT) $(LDOPT2) $(libjh5_OBJECTS) $(LIBS)

clean:
	$(RM) -f *.o so_locations $(LIBDIR)/libjh5.$(JSLEXT)

include $(top_srcdir)/config/conclude.am
