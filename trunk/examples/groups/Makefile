# Generated automatically from Makefile.in by configure.
#/****************************************************************************
# * NCSA HDF                                                                 *
# * National Comptational Science Alliance                                   *
# * University of Illinois at Urbana-Champaign                               *
# * 605 E. Springfield, Champaign IL 61820                                   *
# *                                                                          *
# * For conditions of distribution and use, see the accompanying             *
# * hdf-java/COPYING file.                                                   *
# *                                                                          *
# ****************************************************************************/


JAVAC=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/bin/javac
RM=/bin/rm

CLASSPATH=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/jre/lib/rt.jar:/home/abyrne/HDF_Projects/HDFJava-J-Trunk   

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) -classpath $(CLASSPATH) $<

all: H5Ex_G_Create H5Ex_G_Iterate

H5Ex_G_Create: H5Ex_G_Create.class

H5Ex_G_Iterate: H5Ex_G_Iterate.class

check: all

clean:
	$(RM) -f *.class
	$(RM) -f *.hdf
