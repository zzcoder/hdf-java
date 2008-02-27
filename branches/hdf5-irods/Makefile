#
# modules/hdf5/Makefile
#
# Build the iRODS hdf5 module
#

# MAKE_LOCAL_TEST specifies that build the test program using local hdf5
# library routine instead of the irods client library. Normal it is off
MAKE_LOCAL_TEST = 1

#
# Source files
#
# Set hdf5Dir to the top-level directory of the
# hdf5 installation.
# hdf5Dir = /data/mwan/hdf5/hdf5-1.6.4
hdf5Dir=/mnt/scr1/xcao/hdf_java/lib/linux32/hdf5
szlibDir=/home/srb/ext_lib/szip
zlibDir=/home/srb/ext_lib/zlib


ifndef buildDir
buildDir = $(CURDIR)/../..
endif

include $(buildDir)/config/config.mk
include $(buildDir)/config/platform.mk
include $(buildDir)/config/directories.mk
include $(buildDir)/config/common.mk

#
# Directories
#
hdf5MSObjDir =      $(modulesDir)/hdf5/microservices/obj
hdf5MSSrcDir =      $(modulesDir)/hdf5/microservices/src
hdf5MSIncDir =      $(modulesDir)/hdf5/microservices/include

hdf5TestDir =	$(modulesDir)/hdf5/test
hdf5LibIncDir =	$(modulesDir)/hdf5/lib/include
hdf5LibObjDir =	$(modulesDir)/hdf5/lib/obj
hdf5LibSrcDir =	$(modulesDir)/hdf5/lib/src

ifdef hdf5Dir
OBJECTS	=
TEST_OBJECT = $(hdf5TestDir)/test_h5File.o
INCLUDE_FLAGS = -I$(zlibDir)/include -I$(szlibDir)/include -I$(hdf5TestDir) -I$(hdf5Dir)/include -I$(hdf5LibIncDir)
HDF_LIB_OBJECT = $(hdf5LibObjDir)/h5Ctor.o $(hdf5LibObjDir)/h5Dtor.o
HDF_LIB_OBJECT+=$(hdf5LibObjDir)/h5File.o \
$(hdf5LibObjDir)/h5Dataset.o $(hdf5LibObjDir)/h5String.o        \
$(hdf5LibObjDir)/h5Group.o $(hdf5LibObjDir)/h5Object.o  \
$(hdf5LibObjDir)/h5Attribute.o

ifdef MAKE_LOCAL_TEST
TEST_PROG = $(hdf5TestDir)/tl5
INCLUDE_FLAGS+=-DHDF5_LOCAL
HDF_LIB_OBJECT+=$(hdf5LibObjDir)/h5LocalHandler.o
else
TEST_PROG = $(hdf5TestDir)/t5
HDF_LIB_OBJECT+=$(hdf5LibObjDir)/h5ClHandler.o
endif
LIBS = -L$(zlibDir)/lib -L$(szlibDir)/lib -L$(hdf5Dir)/lib -lhdf5 -lm -lz -lsz
else
OBJECTS =
INCLUDE_FLAGS =
LIBS =
endif

#
# Compile and link flags
#
INCLUDES +=	$(INCLUDE_FLAGS) $(LIB_INCLUDES) $(SVR_INCLUDES)
CFLAGS_OPTIONS := $(CFLAGS) $(MY_CFLAG)
CFLAGS =        $(CFLAGS_OPTIONS) $(INCLUDES) $(MODULE_CFLAGS)

.PHONY: all server client microservices clean
.PHONY: server_ldflags client_ldflags server_cflags client_cflags
.PHONY: print_cflags

# Build everything
all:	test
	@true

# List module's objects and needed libs for inclusion in clients
client_ldflags:
	@true

# List module's includes for inclusion in the clients
client_cflags:
	@true

# List module's objects and needed libs for inclusion in the server
server_ldflags:
	@echo $(OBJECTS) $(LIBS)

# List module's includes for inclusion in the server
server_cflags:
	@echo $(INCLUDE_FLAGS)

# Build microservices
ifdef hdf5Dir
microservices:	print_cflags $(OBJECTS)
	@true
test:	$(TEST_PROG)
else
microservices:
	@echo "The hdf5Dir variable is not set in the hdf5 module Makefile."
	@echo "Either set this to the path to ImageMagick, or disable the hdf5"
	@echo "module before building iRODS."
endif

# Build client additions
client:
	@true

# Build server additions
server:
	@true

# Build rules
rules:
	@true

# Clean
ifdef hdf5Dir
clean:
	@echo "Clean hdf5 module..."
	@rm -f $(OBJECTS) $(HDF_LIB_OBJECT) $(TEST_OBJECT) $(TEST_PROG)
else
clean:
	@echo "Clean hdf5 module..."
endif


# Show compile flags
print_cflags:
	@echo "Compile flags:"
	@echo "    $(CFLAGS_OPTIONS)"

#
# Compile targets
#
$(OBJECTS): $(hdf5MSObjDir)/%.o: $(hdf5MSSrcDir)/%.c $(DEPEND)
	@echo "Compile hdf5 module `basename $@`..."
	@$(CC) -c $(CFLAGS) -o $@ $<

$(TEST_OBJECT): $(hdf5TestDir)/%.o: $(hdf5TestDir)/%.c $(DEPEND)
	@echo "Compile  $(TEST_OBJECT) `basename $@`..."
	@$(CC) -c $(CFLAGS) -o $@ $<

$(HDF_LIB_OBJECT): $(hdf5LibObjDir)/%.o: $(hdf5LibSrcDir)/%.c $(DEPEND)
	@echo "Compile  $(HDF_LIB_OBJECT) `basename $@`..."
	@$(CC) -c $(CFLAGS) -o $@ $<

# rule to link the program
$(TEST_PROG): $(OBJS) $(TEST_OBJECT) $(HDF_LIB_OBJECT)
	$(CC) $(TEST_OBJECT) $(HDF_LIB_OBJECT) $(LIBS) -o $(TEST_PROG)

