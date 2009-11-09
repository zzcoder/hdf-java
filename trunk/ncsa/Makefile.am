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

# All subdirectories
SUBDIRS=hdf

#############################break##########################################
H45INC=@H45INC@
H4INC=@HDF4INC@
H5INC=@HDF5INC@

all: hdflib hdf5lib h4toh5lib

hdflib: FORCE
	@if test -n "$(H4INC)" ; then $(MAKE) do-hdflib ; fi

do-hdflib: 
	cd hdflib; \
	$(MAKE) ; 

hdf5lib: FORCE
	@if test -n "$(H5INC)" ; then $(MAKE) do-hdf5lib ; fi

do-hdf5lib: 
	cd hdf5lib; \
	$(MAKE) ; 

h4toh5lib: FORCE
	@if test -n "$(H45INC)" ; then $(MAKE) doh4toh5lib ; fi

doh4toh5lib: 
	cd h4toh5lib; \
	$(MAKE) ; 

clean: clean-hdflib clean-hdf5lib clean-h4toh5lib

clean-hdflib:
	cd hdflib; \
	$(MAKE) clean ; 

clean-hdf5lib:
	cd hdf5lib; \
	$(MAKE) clean ; 

clean-h4toh5lib:
	@if test -n "$(H45INC)" ; then $(MAKE) docleanh4toh5lib ; fi

docleanh4toh5lib:
	cd h4toh5lib; \
	$(MAKE) clean ; 

FORCE:
