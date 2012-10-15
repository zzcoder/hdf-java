# A line that starts with a # is a comment
SRCDIR=.

JAVADOC_FLAGS   = -version -author

#make this relative to the source root...
LIBDIR=$(JH45INSTALLDIR)/lib
BINDIR=$(JH45INSTALLDIR)/bin
DOCDIR= $(JH45INSTALLDIR)/docs
UGDIR= $(JH45INSTALLDIR)/UsersGuide

CLASS_DIR=$(TOP)/lib
CP="$(CLASSPATH):$(CLASS_DIR):. "

RM = rm -f

# New suffixes
.SUFFIXES: .java .class .h

# Temp file for list of files to compile
COMPILEME = .compileme$(USER)
COPYME = .copyme$(USER)

CURRENTDIR = .
JAR_DIR=$(CLASS_DIR)
OUT_DIR=$(CLASS_DIR)/$(DIR)

JFLAGS= -g -deprecation -d $(CLASS_DIR)	-classpath $(CP)
RMICFLAGS= -g -d $(CLASS_DIR)
JARFLAGS = cfm

JAVAH	= $(JAVAH) -jni
RMIC	= rmic	$(RMICFLAGS)
JC	= $(JAVAC) $(JFLAGS)

PACKAGE		= $(subst /,.,$(DIR))

JAVA_OBJS	= $(JAVA_SRCS:%.java=$(OUT_DIR)/%.class)
RMI_OBJS	= $(RMI_SRCS:%.java=$(OUT_DIR)/%.class)
STUB_OBJS	= $(RMI_OBJS:%.class=%_Stub.class)
SKEL_OBJS	= $(RMI_OBJS:%.class=%_Skel.class)

H_FILES		= $(JNI_SRCS:%.java=$(JNI_DIR)/%.h)

# Notice that each line that starts with an @ is ONE LONG LINE
# It may not show up or print out like that in the FAQ

# Walk down the SUBDIRS first
all::
	@echo "subdirs is " $(SUBDIRS); \
	 if test "$(SUBDIRS)" != "NULL" ; then \
		for i in $(SUBDIRS) ; do \
		    (cd $$i ; echo "making" all "in $(CURRENTDIR)/$$i"; \
		    $(MAKE) CURRENTDIR=$(CURRENTDIR)/$$i all); \
		done \
	 fi

# Then compile each file in each subdir
all:: $(JAVA_OBJS) $(RMI_OBJS) $(JNI_OBJS)
	@if test -r ${COMPILEME}; then CFF=`cat ${COMPILEME}`; fi; \
          $(RM) ${COMPILEME}; if test "$${CFF}" != ""; then  \
          echo $(JC) $${CFF}; fi; if test "$${CFF}" != ""; then \
          $(JC) $${CFF}; fi
 
	@$(RM) ${COMPILEME}



# "make clean" will delete all your class files to start fresh
clean::
	$(RM) $(OUT_DIR)/*.class *~ $(COMPILEME)
	$(RM) $(OUT_DIR)/*.gif *~ $(COMPILEME)
	$(RM) $(OUT_DIR)/*.jpg *~ $(COMPILEME)
	$(RM) $(OUT_DIR)/*.py *~ $(COMPILEME)

# SUBDIRS
clean::
	@echo "2nd check: subdirs is " $(SUBDIRS); \
	 if test  "$(SUBDIRS)" != "NULL"; then \
		echo "Past the 2nd if then"; \
		for i in $(SUBDIRS) ;\
		    do \
		    (cd $$i ; echo "making" clean "in $(CURRENTDIR)/$$i"; \
		    $(MAKE) CURRENTDIR=$(CURRENTDIR)/$$i clean); \
		    done \
	 fi

clean::
	@if [ "$(H_FILES)" != "/" ] && [ "$(H_FILES)" != "" ]; then \
		echo $(RM) $(H_FILES); \
		$(RM) $(H_FILES); \
	fi
	@if [ "$(RMI_OBJS)" != "/" ] && [ "$(RMI_OBJS)" != "" ]; then \
		echo $(RM) $(RMI_OBJS); \
		$(RM) $(RMI_OBJS); \
	fi
	@if [ "$(STUB_OBJS)" != "/" ] && [ "$(STUB_OBJS)" != "" ]; then \
		echo $(RM) $(STUB_OBJS); \
		$(RM) $(STUB_OBJS); \
	fi
	@if [ "$(SKEL_OBJS)" != "/" ] && [ "$(SKEL_OBJS)" != "" ]; then \
		echo $(RM) $(SKEL_OBJS); \
		$(RM) $(SKEL_OBJS); \
	fi


all::  $(STUB_OBJS) $(SKEL_OBJS) $(H_FILES)


### Rules

# .class.java rule, add file to list
$(OUT_DIR)/%.class : %.java
	@echo $< >> $(COMPILEME)

# Rule for compiling  a Stub/Skel files
$(OUT_DIR)/%_Skel.class :: $(OUT_DIR)/%_Stub.class

$(OUT_DIR)/%_Stub.class:: %.java
	$(RMIC) $(PACKAGE).$(notdir $(basename $(<F)))	


# Rule for compiling a .h file
$(JNI_DIR)/%.h : %.java
	$(JAVAH) -o $@ $(PACKAGE).$(notdir $(basename $< )) 

