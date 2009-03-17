# Generated automatically from Makefile.in by configure.
#/****************************************************************************
#* NCSA HDF                                                                 *
#* National Comptational Science Alliance                                   *
#* University of Illinois at Urbana-Champaign                               *
#* 605 E. Springfield, Champaign IL 61820                                   *
#*                                                                          *
#* For conditions of distribution and use, see the accompanying             *
#* java-h4toh5/COPYING file.                                                  *
#*                                                                          *
#****************************************************************************/

CC = gcc -shared -DUNIX386

HDF5INC = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/include
HDF5LIB = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib
HDF4INC = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/include
HDF4LIB = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib
H45INC = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/include
H45LIB = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib

JAVAINC1=/usr/local/jdk1.3.1/include
JAVAINC2=/usr/local/jdk1.3.1/include/linux

LIBDIR=../../lib/linux

# Flags passed to the C compiler.  Leave these alone.

LD=gcc
LDOPT=-shared
SLEXT=so

COPT=-shared -DUNIX386

CFLAGS = $(DEFS) -I. -I$(HDF4INC) -I$(HDF5INC) -I$(H45INC) -I$(JAVAINC1) -I$(JAVAINC2)

HDFL=$(HDF4LIB)/libdf.a $(HDF4LIB)/libjpeg.a $(HDF4LIB)/libmfhdf.a $(HDF4LIB)/libz.a

LIBS =  $(H45LIB)/libh4toh5.a $(HDF5LIB)/libhdf5.a $(HDFL) -lm

H4OBJECTS =  ../hdflib/hdfstructsutil.o \
	../hdflib/hdfImp.o \
	../hdflib/hdfvgroupImp.o \
	../hdflib/hdfvdataImp.o \
	../hdflib/hdfsdsImp.o \
	../hdflib/hdfanImp.o \
	../hdflib/hdfdfanImp.o \
	../hdflib/hdfdfsdImp.o \
	../hdflib/hdfr24Imp.o \
	../hdflib/hdfdfpalImp.o \
	../hdflib/hdfr8Imp.o \
	../hdflib/hdfgrImp.o \
	../hdflib/hdfheImp.o \
	../hdflib/hdfdfuImp.o \
	../hdflib/hdfhxImp.o \
	../hdflib/hdfvfImp.o \
	../hdflib/hdfvhImp.o \
	../hdflib/hdfvqImp.o \
	../hdflib/hdfvsqImp.o \
	../hdflib/hdfexceptionImp.o \
	../hdflib/hdfdfcompImp.o \
	../hdflib/hdfnativeImp.o 

H5OBJECTS =  ../hdf5lib/exceptionImp.o \
	../hdf5lib/h5Constants.o \
	../hdf5lib/h5Imp.o \
	../hdf5lib/h5aImp.o \
	../hdf5lib/h5dImp.o \
	../hdf5lib/h5fImp.o \
	../hdf5lib/h5gImp.o \
	../hdf5lib/h5iImp.o \
	../hdf5lib/h5pImp.o \
	../hdf5lib/h5rImp.o \
	../hdf5lib/h5sImp.o \
	../hdf5lib/h5tImp.o \
	../hdf5lib/nativeData.o \
	../hdf5lib/H5Git.o

H45OBJECTS =  h45exceptionImp.o h4toh5Imp.o 

OBJECTS =  $(H45OBJECTS) $(H5OBJECTS) $(H4OBJECTS)

all:  libjh4toh5

libjh4toh5: $(OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjh4toh5.$(SLEXT) $(LDOPT2) $(OBJECTS) $(LIBS)

clean:
	rm -f *.o so_locations $(LIBDIR)/libjh4toh5.$(SLEXT)

