#============================================================================
#
#              Makefile to compile HDF Java Native C Source
#              Usage: nmake /f jhdf5.mak
#
#============================================================================

# Visual C++ directory, for example
#VCPPDIR=C:\Program Files\Microsoft Visual Studio\VC98
VCPPDIR=

# directory where JDK is installed (need JDK 1.3.1 or above), for example
#JAVADIR=D:\java\jdk1.3.1\
JAVADIR=

# directory of the HDF Java Products, for example
#HDFJAVADIR=D:\work\hdf-java\
HDFJAVADIR=

# the directory where HDF library is installed, for example
#HDFDIR=D:\work\5-162-win2k\release\
HDFDIR=

# the JPEG library, for example
#JPEGLIB=E:\Work\MyHDFstuff\lib-external\jpeg\libjpeg.lib
JPEGLIB=

# the GZIP library, for example
#GZIPLIB=E:\Work\MyHDFstuff\lib-external\zlib\bin\windows\zlib114\lib\zlib.lib
GZIPLIB=

# SZIP library, for example
#SZIPLIB=E:\Work\MyHDFstuff\lib-external\szip\bin\windows\szip-msvc++\lib\szlib.lib
SZIPLIB=


#===========================================================================
#   Do not make any change below this line unless you know what you do
#===========================================================================
PATH=$(PATH);$(VCPPDIR)\BIN
SRCDIR=$(HDFJAVADIR)\native\hdf5lib\

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

INTDIR=.\jhdf5\Release
OUTDIR=$(HDFJAVADIR)\lib\win

INCLUDES =  \
	"$(JAVADIR)\include\jni.h" \
	"$(JAVADIR)\include\win32\jni_md.h" \
	"$(SRCDIR)\h5Constants.h" \
	"$(HDFDIR)\include\H5ACpublic.h" \
	"$(HDFDIR)\include\H5api_adpt.h" \
	"$(HDFDIR)\include\H5Apkg.h" \
	"$(HDFDIR)\include\H5Apublic.h" \
	"$(HDFDIR)\include\H5Bpkg.h" \
	"$(HDFDIR)\include\H5Bpublic.h" \
	"$(HDFDIR)\include\H5config.h" \
	"$(HDFDIR)\include\H5Dpkg.h" \
	"$(HDFDIR)\include\H5Dpublic.h" \
	"$(HDFDIR)\include\H5Epublic.h" \
	"$(HDFDIR)\include\H5FDcore.h" \
	"$(HDFDIR)\include\H5FDfamily.h" \
	"$(HDFDIR)\include\H5FDfphdf5.h" \
	"$(HDFDIR)\include\H5FDgass.h" \
	"$(HDFDIR)\include\H5FDlog.h" \
	"$(HDFDIR)\include\H5FDmpio.h" \
	"$(HDFDIR)\include\H5FDmpiposix.h" \
	"$(HDFDIR)\include\H5FDmulti.h" \
	"$(HDFDIR)\include\H5FDpublic.h" \
	"$(HDFDIR)\include\H5FDsec2.h" \
	"$(HDFDIR)\include\H5FDsrb.h" \
	"$(HDFDIR)\include\H5FDstdio.h" \
	"$(HDFDIR)\include\H5FDstream.h" \
	"$(HDFDIR)\include\H5Fpkg.h" \
	"$(HDFDIR)\include\H5FPpublic.h" \
	"$(HDFDIR)\include\H5Fpublic.h" \
	"$(HDFDIR)\include\H5Gpkg.h" \
	"$(HDFDIR)\include\H5Gpublic.h" \
	"$(HDFDIR)\include\H5HGpublic.h" \
	"$(HDFDIR)\include\H5HLpublic.h" \
	"$(HDFDIR)\include\H5Ipkg.h" \
	"$(HDFDIR)\include\H5Ipublic.h" \
	"$(HDFDIR)\include\H5MMpublic.h" \
	"$(HDFDIR)\include\H5Opkg.h" \
	"$(HDFDIR)\include\H5Opublic.h" \
	"$(HDFDIR)\include\H5Ppkg.h" \
	"$(HDFDIR)\include\H5Ppublic.h" \
	"$(HDFDIR)\include\H5pubconf.h" \
	"$(HDFDIR)\include\H5public.h" \
	"$(HDFDIR)\include\H5Rpublic.h" \
	"$(HDFDIR)\include\H5Spkg.h" \
	"$(HDFDIR)\include\H5Spublic.h" \
	"$(HDFDIR)\include\H5Tpkg.h" \
	"$(HDFDIR)\include\H5Tpublic.h" \
	"$(HDFDIR)\include\H5Zpkg.h" \
	"$(HDFDIR)\include\H5Zpublic.h" \
 	"$(HDFDIR)\include\hdf5.h"


ALL : "$(OUTDIR)\jhdf5.dll"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /GX /O2 /I "$(HDFDIR)\include" /I "$(JAVADIR)\include" /I "$(JAVADIR)\include\win32" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /Fp"$(INTDIR)\jhdf5.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

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
BSC32_FLAGS=/nologo /o"$(INTDIR)\jhdf5.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=$(HDFDIR)\lib\hdf5.lib $(SZIPLIB) $(GZIPLIB) /nologo /dll /incremental:no /pdb:"$(INTDIR)\jhdf5.pdb" /machine:I386 /out:"$(OUTDIR)\jhdf5.dll" /implib:"$(INTDIR)\jhdf5.lib" 
LINK32_OBJS= \
	"$(INTDIR)\exceptionImp.obj" \
	"$(INTDIR)\h5aImp.obj" \
	"$(INTDIR)\h5Constants.obj" \
	"$(INTDIR)\h5dImp.obj" \
	"$(INTDIR)\h5fImp.obj" \
	"$(INTDIR)\h5gImp.obj" \
	"$(INTDIR)\h5iImp.obj" \
	"$(INTDIR)\h5Imp.obj" \
	"$(INTDIR)\h5pImp.obj" \
	"$(INTDIR)\h5rImp.obj" \
	"$(INTDIR)\h5sImp.obj" \
	"$(INTDIR)\h5tImp.obj" \
	"$(INTDIR)\h5zImp.obj" \
	"$(INTDIR)\nativeData.obj"

"$(OUTDIR)\jhdf5.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<


SOURCE=$(SRCDIR)\exceptionImp.c

"$(INTDIR)\exceptionImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5aImp.c

"$(INTDIR)\h5aImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5Constants.c

"$(INTDIR)\h5Constants.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5dImp.c

"$(INTDIR)\h5dImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5fImp.c

"$(INTDIR)\h5fImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5gImp.c

"$(INTDIR)\h5gImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5iImp.c

"$(INTDIR)\h5iImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5Imp.c

"$(INTDIR)\h5Imp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5pImp.c

"$(INTDIR)\h5pImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5rImp.c

"$(INTDIR)\h5rImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5sImp.c

"$(INTDIR)\h5sImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5tImp.c

"$(INTDIR)\h5tImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\h5zImp.c

"$(INTDIR)\h5zImp.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=$(SRCDIR)\nativeData.c

"$(INTDIR)\nativeData.obj" : $(SOURCE) $(INCLUDES) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)

CLEAN :
	-@erase "$(INTDIR)\exceptionImp.obj"
	-@erase "$(INTDIR)\h5aImp.obj"
	-@erase "$(INTDIR)\h5Constants.obj"
	-@erase "$(INTDIR)\h5dImp.obj"
	-@erase "$(INTDIR)\h5fImp.obj"
	-@erase "$(INTDIR)\h5gImp.obj"
	-@erase "$(INTDIR)\h5iImp.obj"
	-@erase "$(INTDIR)\h5Imp.obj"
	-@erase "$(INTDIR)\h5pImp.obj"
	-@erase "$(INTDIR)\h5rImp.obj"
	-@erase "$(INTDIR)\h5sImp.obj"
	-@erase "$(INTDIR)\h5tImp.obj"
	-@erase "$(INTDIR)\h5zImp.obj"
	-@erase "$(INTDIR)\nativeData.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\jhdf5.exp"
	-@erase "$(INTDIR)\jhdf5.lib"
	-@erase "$(OUTDIR)\jhdf5.dll"

!ENDIF
