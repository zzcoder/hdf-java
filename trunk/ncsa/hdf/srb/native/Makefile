# Makefile - This is the makefile for libjh5srb.so, jh5srb.class. 
SHELL=/bin/csh

include ../../../mk/mk.config
include ../../../mk/mk.common

buildDir = ../../..
LIBDIR = .

SLIB = $(LIBDIR)/libjh5srb.so
OBJ  = $(LIBDIR)/jh5srb.o

srbObjDir = $(buildDir)/obj
hdf5srbObjDir = $(buildDir)/proxy/hdf5/obj
utilObjDir = $(buildDir)/utilities/obj
OBJS = \
	$(srbObjDir)/clAuth.o \
	$(srbObjDir)/clConnect.o \
	$(srbObjDir)/clExec.o \
	$(srbObjDir)/clMisc.o \
	$(srbObjDir)/clStub.o \
        $(srbObjDir)/packMsg.o \
	$(srbObjDir)/clGlobal.o	\
	$(srbObjDir)/clChksum.o	\
	$(srbObjDir)/mcatgeneral.o	\
	$(srbObjDir)/srb_perror.o $(srbObjDir)/crypt1.o	\
	$(srbObjDir)/srbSecureComm.o \
	$(srbObjDir)/obf.o \
	$(hdf5srbObjDir)/clH5Dataset.o $(hdf5srbObjDir)/clH5File.o 	\
	$(hdf5srbObjDir)/clH5Group.o $(hdf5srbObjDir)/h5ClHandler.o	\
	$(hdf5srbObjDir)/h5Ctor.o $(hdf5srbObjDir)/h5Dtor.o	\
	$(LIB_GSI_AUTH) $(KRB_LIBS)

#        $(utilObjDir)/srbClientUtil.o   \
#        $(utilObjDir)/bloadLib.o        \
#        $(utilObjDir)/bunloadLib.o      \

ifdef GSI_AUTH
OBJS+=$(srbObjDir)/aid.o
endif

# CC = /opt/SUNWspro/bin/cc -DPORTNAME_solaris -DDEBUG
# CC = /opt/SUNWspro/bin/cc -DPORTNAME_solaris
# ifeq ($(PORTNAME), PORTNAME_solaris)
# CC=gcc
# endif

CFLAGS=$(MY_CFLAG)

ifeq ($(PORTNAME), PORTNAME_solaris)
PLATFORM=solaris
endif 

ifeq ($(PORTNAME), PORTNAME_linux)
PLATFORM=linux
endif

ifeq ($(PORTNAME), PORTNAME_aix)
PLATFORM=aix
CFLAGS+=-c -M -I.
endif

ifeq ($(PORTNAME), PORTNAME_sgi)
PLATFORM=irix
endif

INC =	-I$(buildDir)/src/include \
		-I$(buildDir)/src/catalog/include \
		-I$(buildDir)/utilities/include \
		-I$(buildDir)/proxy/hdf5/include \
		-I$(javaDir)/include \
		-I$(javaDir)/include/$(PLATFORM)

.SUFFIXES: .c .o .so .java .class .h


all: $(SLIB)
	@ echo $(SLIB) is to date.

$(SLIB): $(OBJ)
ifeq ($(PORTNAME), PORTNAME_aix)
	grep " JNICALL " *.c|sed "s/.* JNICALL //g">$@.exp
	$(LDR) -lm -bnoentry -bM:SRE -bE:$@.exp -lc_r -blibpath:/usr/lib/threads:/usr/lib:/lib -L/usr/local/apps/Java/lib/aix/native_threads -o $(SLIB) $(OBJS) $(OBJ)
	rm $@.exp
else
ifeq ($(PORTNAME), PORTNAME_sgi)
	$(LDR) -shared -o $(SLIB) $(OBJS) $(OBJ)
else
ifeq ($(PORTNAME), PORTNAME_linux)
	$(LDR) -shared -lm -lpthread -o $(SLIB) $(OBJS) $(OBJ)
else
	$(LDR) -G -o $(SLIB) $(OBJS) $(OBJ)
endif
endif
endif

$(LIBDIR)/jh5srb.o: jh5srb.c 
	$(CC) -c $(CFLAGS) $(INC) -o $@ $<
clean:
	rm -f $(OBJ) $(SLIB) $(SLIB).exp

