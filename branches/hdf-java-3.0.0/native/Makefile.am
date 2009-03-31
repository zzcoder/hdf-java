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
# Top-level HDFJava Makefile(.in)

# pmake has issues if variables are undefined.  Solve this problem in
# top-level Makefile by defining .MAKEFLAGS target to -V before pmake can
# encounter any undefined variables.
# Automake resists putting anything but variable definitions first in
# a Makefile.in, so we'll put a placebo comment here and use sed in
# bin/reconfigure to turn it into the .MAKEFLAGS target.  Sigh.  -JL 2005
# Configure should set AM_MAKEFLAGS to -V to solve this problem in
# subdirectories.
# NOTE: This means that invoking pmake in a subdirectory will not work.
#xxx.MAKEFLAGS:@AM_MAKEFLAGS@
#xxx    $(MAKE) all
#xxx

include $(top_srcdir)/config/commence.am

SUBDIRS = h4 h5
DIST_SUBDIRS = h4 h5

# Automake wants to make config.status depend on configure.  This
# makes sense, but config.status can't always be regenerated
# properly, which can cause builds to fail.
# This is a problem for our Daily Tests, which need to be able to
# 'make distclean' reliably before running configure.
# The simple solution is to override the dependency Automake supplies
# for config.status so that it will never be regenerated.
$(top_builddir)/config.status:

# Don't include conclude.am in root Makefile; tests target needs to
# recurse into perform directory as well as reguar subdirs.
