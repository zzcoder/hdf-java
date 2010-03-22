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

all: testh4file testh5file
	chmod u+x runtests.sh

testh4file: TestH4File.class
	chmod u+x testh4file.sh

testh5file: TestH5File.class
	chmod u+x testh5file.sh

runtests: all
	chmod u+x runtests.sh

check: all
#	./runtests.sh
	./TestH5Object.sh

check-memory-leak:
#	./TestH5MemoryLeak.sh

clean:
	$(RM) -f *.class
	$(RM) -f *.hdf
	$(RM) -f *.h5
