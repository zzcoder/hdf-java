# Generated automatically from Makefile.in by configure.

#/****************************************************************************
 #* NCSA HDF                                                                 *
 #* National Comptational Science Alliance                                   *
 #* University of Illinois at Urbana-Champaign                               *
 #* 605 E. Springfield, Champaign IL 61820                                   *
 #*                                                                          *
 #* For conditions of distribution and use, see the accompanying             *
 #* hdf/COPYING file.                                                        *
 #*                                                                          *
 #****************************************************************************/


CC = gcc -shared -DUNIX386

HDFINC = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/include
HDFLIB = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib
JAVAINC1=/usr/local/jdk1.3.1/include
JAVAINC2=/usr/local/jdk1.3.1/include/linux

LIBDIR=../../lib/linux

# Flags passed to the C compiler.  Leave these alone.

LD=gcc
LDOPT=-shared
SLEXT=so

COPT=-shared -DUNIX386

CFLAGS = $(DEFS) -I. -I$(JAVAINC1) -I$(JAVAINC2) -I$(HDFINC)

# Be sure to add -lbsd if compiling in a SYSV environment
LIBS =  $(HDFLIB)/libmfhdf.a $(HDFLIB)/libdf.a $(HDFLIB)/libjpeg.a $(HDFLIB)/libz.a -lm 

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

libjhdf: $(OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjhdf.$(SLEXT) $(LDOPT2) $(OBJECTS) $(LIBS)

clean:
	rm -f *.o so_locations $(LIBDIR)/libjhdf.$(SLEXT)
