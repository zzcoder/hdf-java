# Generated automatically from Makefile.in by configure.
#/****************************************************************************
#* NCSA HDF                                                                 *
#* National Comptational Science Alliance                                   *
#* University of Illinois at Urbana-Champaign                               *
#* 605 E. Springfield, Champaign IL 61820                                   *
#*                                                                          *
#* For conditions of distribution and use, see the accompanying             *
#* java-hdf5/COPYING file.                                                  *
#*                                                                          *
#****************************************************************************/

CC = gcc -shared -DUNIX386

HDF5INC = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/include
HDF5LIB = /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib
JAVAINC1=/usr/local/jdk1.3.1/include
JAVAINC2=/usr/local/jdk1.3.1/include/linux
ZLIB=/afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib/libz.a

LIBDIR=../../lib/linux

# Flags passed to the C compiler.  Leave these alone.

LD=gcc
LDOPT=-shared
SLEXT=so

COPT=-shared -DUNIX386

CFLAGS = $(DEFS) -I. -I$(HDF5INC) -I$(JAVAINC1) -I$(JAVAINC2)

#LIBS =  $(HDF5LIB)/libhdf5.$(SLEXT) 
LIBS =  $(HDF5LIB)/libhdf5.a /afs/ncsa.uiuc.edu/projects/hdf/java/java6/mcgrath/eirene/New/lib/libz.a -lm

# Macro def: object filename used
OBJECTS =  exceptionImp.o h5Constants.o h5Imp.o h5aImp.o h5dImp.o h5fImp.o h5gImp.o h5iImp.o h5pImp.o h5rImp.o h5sImp.o h5tImp.o nativeData.o H5Git.o

# make depends on the files specified in the objects macro defination
all:  libjhdf5

libjhdf5: $(OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjhdf5.$(SLEXT) $(LDOPT2) $(OBJECTS) $(LIBS)

clean:
	rm -f *.o so_locations $(LIBDIR)/libjhdf5.$(SLEXT)
