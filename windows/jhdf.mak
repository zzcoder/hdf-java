#============================================================================
#
#              Makefile to compile HDF Java Native C Source
#              Usage: nmake /f jhdf.mak
#
#============================================================================

# Visual C++ directory, for example
VCPPDIR=C:\Program Files\Microsoft Visual Studio\VC98
#VCPPDIR=

# directory where JDK is installed (need JDK 1.3.1 or above), for example
JAVADIR=D:\java\jdk1.3.1\
#JAVADIR=

# directory of the HDF Java Products, for example
HDFJAVADIR=D:\work\hdf-java\
#HDFJAVADIR=

# the directory where HDF library is installed, for example
HDFDIR=E:\Work\MyHDFstuff\hdf-42r0\
#HDFDIR=

# the JPEG library, for example
JPEGLIB=E:\Work\MyHDFstuff\lib-external\jpeg\libjpeg.lib

# the GZIP library, for example
GZIPLIB=E:\Work\MyHDFstuff\lib-external\zlib\bin\windows\zlib114\lib\zlib.lib

# SZIP library, for example
SZIPLIB=E:\Work\MyHDFstuff\lib-external\szip\bin\windows\szip-msvc++\lib\szlib.lib


#===========================================================================
#   Do not make any change below this line unless you know what you do
#===========================================================================
PATH=$(PATH);$(VCPPDIR)\BIN
SRCDIR=$(HDFJAVADIR)\native\hdflib\

VALID_PATH_SET=YES
#-------------------------------------------------------
# Test if all path is valid

!IF EXISTS("$(VCPPDIR)")
!ELSE
!MESSAGE ERROR: Visual C++ directory does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(JAVADIR)")
!ELSE
!MESSAGE ERROR: JDK directory does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(SRCDIR)")
!ELSE
!MESSAGE ERROR: C source directory does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(HDFDIR)")
!ELSE
!MESSAGE ERROR: HDF library directory does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(JPEGLIB)")
!ELSE
!MESSAGE ERROR: JPEG library does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(GZIPLIB)")
!ELSE
!MESSAGE ERROR: GZIP library does not exist
VALID_PATH_SET=NO 
!ENDIF

!IF EXISTS("$(SZIPLIB)")
!ELSE
!MESSAGE ERROR: SZIP library does not exist
VALID_PATH_SET=NO 
!ENDIF

#-------------------------------------------------------



!IF "$(VALID_PATH_SET)" == "YES"

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

INTDIR=.\jhdf\Release
OUTDIR=$(HDFJAVADIR)\lib\win

INCLUDES =  \
	"$(VCPPDIR)\include\basetsd.h" \
	"$(JAVADIR)\include\jni.h" \
	"$(JAVADIR)\include\win32\jni_md.h" \
	"$(HDFDIR)\include\atom.h" \
	"$(HDFDIR)\include\bitvect.h" \
	"$(HDFDIR)\include\dynarray.h" \
	"$(HDFDIR)\include\h4config.h" \
	"$(HDFDIR)\include\hbitio.h" \
	"$(HDFDIR)\include\hchunks.h" \
	"$(HDFDIR)\include\hcomp.h" \
	"$(HDFDIR)\include\hdf.h" \
	"$(HDFDIR)\include\hdf2netcdf.h" \
	"$(HDFDIR)\include\hdfi.h" \
	"$(HDFDIR)\include\herr.h" \
	"$(HDFDIR)\include\hfile.h" \
	"$(HDFDIR)\include\hlimits.h" \
	"$(HDFDIR)\include\hntdefs.h" \
	"$(HDFDIR)\include\hproto.h" \
	"$(HDFDIR)\include\htags.h" \
	"$(HDFDIR)\include\linklist.h" \
	"$(HDFDIR)\include\mfgr.h" \
	"$(HDFDIR)\include\mfhdf.h" \
	"$(HDFDIR)\include\netcdf.h" \
	"$(HDFDIR)\include\tbbt.h" \
	"$(HDFDIR)\include\vg.h"

ALL : "$(OUTDIR)\jhdf.dll"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /GX /O2 /I "$(HDFDIR)\include" /I "$(JAVADIR)\include" /I "$(JAVADIR)\include\win32" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /Fp"$(INTDIR)\jhdf.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /win32 
RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(INTDIR)\jhdf.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=$(HDFDIR)\lib\hd420.lib $(HDFDIR)\lib\hm420.lib ws2_32.lib $(JPEGLIB) $(SZIPLIB) $(GZIPLIB) /nologo /dll /incremental:no /pdb:"$(INTDIR)\jhdf.pdb" /machine:I386 /out:"$(OUTDIR)\jhdf.dll" /implib:"$(INTDIR)\jhdf.lib" 
LINK32_OBJS= \
	"$(INTDIR)\hdfanImp.obj" \
	"$(INTDIR)\hdfdfanImp.obj" \
	"$(INTDIR)\hdfdfcompImp.obj" \
	"$(INTDIR)\hdfdfpalImp.obj" \
	"$(INTDIR)\hdfdfsdImp.obj" \
	"$(INTDIR)\hdfdfuImp.obj" \
	"$(INTDIR)\hdfexceptionImp.obj" \
	"$(INTDIR)\hdfgrImp.obj" \
	"$(INTDIR)\hdfheImp.obj" \
	"$(INTDIR)\hdfhxImp.obj" \
	"$(INTDIR)\hdfImp.obj" \
	"$(INTDIR)\hdfnativeImp.obj" \
	"$(INTDIR)\hdfr24Imp.obj" \
	"$(INTDIR)\hdfr8Imp.obj" \
	"$(INTDIR)\hdfsdsImp.obj" \
	"$(INTDIR)\hdfstructsutil.obj" \
	"$(INTDIR)\hdfvdataImp.obj" \
	"$(INTDIR)\hdfvfImp.obj" \
	"$(INTDIR)\hdfvgroupImp.obj" \
	"$(INTDIR)\hdfvhImp.obj" \
	"$(INTDIR)\hdfvqImp.obj" \
	"$(INTDIR)\hdfvsqImp.obj"

"$(OUTDIR)\jhdf.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

SOURCE=$(SRCDIR)\hdfanImp.c

"$(INTDIR)\hdfanImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfdfanImp.c

"$(INTDIR)\hdfdfanImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfdfcompImp.c

"$(INTDIR)\hdfdfcompImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfdfpalImp.c

"$(INTDIR)\hdfdfpalImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfdfsdImp.c

"$(INTDIR)\hdfdfsdImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfdfuImp.c

"$(INTDIR)\hdfdfuImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfexceptionImp.c

"$(INTDIR)\hdfexceptionImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfgrImp.c

"$(INTDIR)\hdfgrImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfheImp.c

"$(INTDIR)\hdfheImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfhxImp.c

"$(INTDIR)\hdfhxImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfImp.c

"$(INTDIR)\hdfImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfnativeImp.c

"$(INTDIR)\hdfnativeImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfr24Imp.c

"$(INTDIR)\hdfr24Imp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfr8Imp.c

"$(INTDIR)\hdfr8Imp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfsdsImp.c

"$(INTDIR)\hdfsdsImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfstructsutil.c

"$(INTDIR)\hdfstructsutil.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvdataImp.c

"$(INTDIR)\hdfvdataImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvfImp.c

"$(INTDIR)\hdfvfImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvgroupImp.c

"$(INTDIR)\hdfvgroupImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvhImp.c

"$(INTDIR)\hdfvhImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvqImp.c

"$(INTDIR)\hdfvqImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\hdfvsqImp.c

"$(INTDIR)\hdfvsqImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)

# clean obj files
CLEAN :
	-@erase "$(INTDIR)\hdfanImp.obj"
	-@erase "$(INTDIR)\hdfdfanImp.obj"
	-@erase "$(INTDIR)\hdfdfcompImp.obj"
	-@erase "$(INTDIR)\hdfdfpalImp.obj"
	-@erase "$(INTDIR)\hdfdfsdImp.obj"
	-@erase "$(INTDIR)\hdfdfuImp.obj"
	-@erase "$(INTDIR)\hdfexceptionImp.obj"
	-@erase "$(INTDIR)\hdfgrImp.obj"
	-@erase "$(INTDIR)\hdfheImp.obj"
	-@erase "$(INTDIR)\hdfhxImp.obj"
	-@erase "$(INTDIR)\hdfImp.obj"
	-@erase "$(INTDIR)\hdfnativeImp.obj"
	-@erase "$(INTDIR)\hdfr24Imp.obj"
	-@erase "$(INTDIR)\hdfr8Imp.obj"
	-@erase "$(INTDIR)\hdfsdsImp.obj"
	-@erase "$(INTDIR)\hdfstructsutil.obj"
	-@erase "$(INTDIR)\hdfvdataImp.obj"
	-@erase "$(INTDIR)\hdfvfImp.obj"
	-@erase "$(INTDIR)\hdfvgroupImp.obj"
	-@erase "$(INTDIR)\hdfvhImp.obj"
	-@erase "$(INTDIR)\hdfvqImp.obj"
	-@erase "$(INTDIR)\hdfvsqImp.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\jhdf.exp"
	-@erase "$(INTDIR)\jhdf.lib"
	-@erase "$(OUTDIR)\jhdf.dll"

!ENDIF
