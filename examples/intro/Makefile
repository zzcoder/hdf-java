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

all: H5_CreateAttribute H5_CreateDataset H5_CreateFile H5_CreateGroup H5_CreateGroupAbsoluteRelative H5_CreateGroupDataset H5_ReadWrite

H5_CreateAttribute: H5_CreateAttribute.class

H5_CreateDataset: H5_CreateDataset.class

H5_CreateFile: H5_CreateFile.class

H5_CreateGroup: H5_CreateGroup.class

H5_CreateGroupAbsoluteRelative: H5_CreateGroupAbsoluteRelative.class

H5_CreateGroupDataset: H5_CreateGroupDataset.class

H5_ReadWrite: H5_ReadWrite.class

check: all

clean:
	$(RM) -f *.class
	$(RM) -f *.hdf
