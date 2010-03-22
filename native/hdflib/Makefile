
#/****************************************************************************
#* Copyright by The HDF Group.
#* Copyright by the Board of Trustees of the University of Illinois.
#* All rights reserved.
#*
#* This file is part of HDFJava.  The full HDFJava copyright notice, including
#* terms governing use, modification, and redistribution, is contained in
#* the files COPYING and Copyright.html.  COPYING can be found at the root
#* of the source code distribution tree; Copyright.html can be found at the
#* root level of an installed copy of the electronic HDF5 document set and
#* is linked from the top-level documents page.  It can also be found at
#* http://hdfgroup.org/hdf-java/doc/Copyright.html.  If you do not have
#* access to either file, you may request a copy from help@hdfgroup.org.
#****************************************************************************/


CC = gcc -shared -Dx86_64 -fPIC
RM = /bin/rm

HDFINC = /usr/local/bin/hdf4/include
HDFLIB = /usr/local/bin/hdf4/lib
JAVAINC1=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/include
JAVAINC2=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/include/linux

LIBDIR=../../lib/linux
ZLIB=/home/abyrne/HDF_Projects/ZLIB/zlib-1.2.3/libz.a
JPEGLIB=/home/abyrne/HDF_Projects/JPEG/jpeg-6b/libjpeg.a
SZLIB=/home/abyrne/HDF_Projects/SZIP/szip/lib/libsz.a

# Flags passed to the C compiler.  Leave these alone.

LD=gcc
LDOPT=-shared
LLEXT=a
SLEXT=so
JSLEXT=so

COPT=-shared -Dx86_64 -fPIC

CFLAGS = $(DEFS) -I. -I$(JAVAINC1) -I$(JAVAINC2) -I$(HDFINC)

# Be sure to add -lbsd if compiling in a SYSV environment
LIBS =  $(HDFLIB)/libmfhdf.$(LLEXT) $(HDFLIB)/libdf.$(LLEXT) $(SZLIB) $(ZLIB) $(JPEGLIB) -lm

# Macro def: object filename used
OBJECTS =  hdfstructsutil.o \
	hdfImp.o hdfvgroupImp.o hdfvdataImp.o hdfsdsImp.o hdfanImp.o \
	hdfdfanImp.o hdfdfsdImp.o hdfr24Imp.o hdfdfpalImp.o hdfr8Imp.o \
	hdfgrImp.o hdfheImp.o hdfdfuImp.o \
	hdfhxImp.o hdfvfImp.o hdfvhImp.o hdfvqImp.o hdfvsqImp.o \
	hdfexceptionImp.o \
	hdfdfcompImp.o \
	hdfnativeImp.o 

# make depends on the files specified in the objects macro defination
all:  libjhdf

hdflib: libjhdf

libjhdf: $(OBJECTS) 
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjhdf.$(JSLEXT) $(LDOPT2) $(OBJECTS) $(LIBS)

clean:
	$(RM) -f *.o so_locations $(LIBDIR)/libjhdf.$(JSLEXT)
