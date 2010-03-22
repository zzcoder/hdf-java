#/****************************************************************************
#* Copyright by The HDF Group.                                               *
#* Copyright by the Board of Trustees of the University of Illinois.         *
#* All rights reserved.                                                      *
#*                                                                           *
#* This file is part of HDF Java Products. The full HDF Java copyright       *
#* notice, including terms governing use, modification, and redistribution,  *
#* is contained in the file, COPYING.  COPYING can be found at the root of   *
#* the source code distribution tree. You can also access it online  at      *
#* http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
#* access to the file, you may request a copy from help@hdfgroup.org.        *
#****************************************************************************/

CC = gcc -shared -Dx86_64 -fPIC
RM = /bin/rm

HDF5INC = /usr/local/bin/hdf5.1.8/include
HDF5LIB = /usr/local/bin/hdf5.1.8/lib
JAVAINC1=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/include
JAVAINC2=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/include/linux
SZLIB=/home/abyrne/HDF_Projects/SZIP/szip/lib/libsz.a
ZLIB=/home/abyrne/HDF_Projects/ZLIB/zlib-1.2.3/libz.a

LIBDIR=../../lib/linux

# Flags passed to the C compiler.  Leave these alone.

LD=gcc
LDOPT=-shared
LLEXT=a
SLEXT=so
JSLEXT=so

COPT=-shared -Dx86_64 -fPIC

CFLAGS = -D_FILE_OFFSET_BITS=64 $(DEFS) -I. -I$(HDF5INC) -I$(JAVAINC1) -I$(JAVAINC2)

LIBS =  $(HDF5LIB)/libhdf5.$(LLEXT) $(ZLIB) $(SZLIB) -lm

# Macro def: object filename used
OBJECTS =  exceptionImp.o h5Constants.o h5Imp.o h5aImp.o h5dImp.o h5eImp.o h5fImp.o h5gImp.o h5iImp.o h5lImp.o h5oImp.o h5pImp.o h5rImp.o h5sImp.o h5tImp.o nativeData.o h5util.o h5zImp.o

# make depends on the files specified in the objects macro defination
all:  libjhdf5

hdf5lib: libjhdf5

libjhdf5: $(OBJECTS)
	-mkdir -p $(LIBDIR);
	$(LD) $(LDOPT) -o $(LIBDIR)/libjhdf5.$(JSLEXT) $(LDOPT2) $(OBJECTS) $(LIBS)

clean:
	$(RM) -f *.o so_locations $(LIBDIR)/libjhdf5.$(JSLEXT)
