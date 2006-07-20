# Microsoft Developer Studio Generated NMAKE File, Based on jhdf5.dsp
!IF "$(CFG)" == ""
CFG=jhdf5 - Win32 Debug
!MESSAGE No configuration specified. Defaulting to jhdf5 - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "jhdf5 - Win32 Release" && "$(CFG)" != "jhdf5 - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "jhdf5.mak" CFG="jhdf5 - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "jhdf5 - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "jhdf5 - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "jhdf5 - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\jhdf5.dll"


CLEAN :
	-@erase "$(INTDIR)\exceptionImp.obj"
	-@erase "$(INTDIR)\h5aImp.obj"
	-@erase "$(INTDIR)\h5Constants.obj"
	-@erase "$(INTDIR)\h5dImp.obj"
	-@erase "$(INTDIR)\h5fImp.obj"
	-@erase "$(INTDIR)\h5gImp.obj"
	-@erase "$(INTDIR)\h5iImp.obj"
	-@erase "$(INTDIR)\h5Imp.obj"
	-@erase "$(INTDIR)\H5IN.obj"
	-@erase "$(INTDIR)\h5inImp.obj"
	-@erase "$(INTDIR)\h5pImp.obj"
	-@erase "$(INTDIR)\h5rImp.obj"
	-@erase "$(INTDIR)\h5sImp.obj"
	-@erase "$(INTDIR)\h5tImp.obj"
	-@erase "$(INTDIR)\h5util.obj"
	-@erase "$(INTDIR)\h5zImp.obj"
	-@erase "$(INTDIR)\nativeData.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(OUTDIR)\jhdf5.dll"
	-@erase "$(OUTDIR)\jhdf5.exp"
	-@erase "$(OUTDIR)\jhdf5.lib"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /GX /O2 /I "I:\work\hdf5-165-win\include" /I "I:\work\lib_external\szip20-win-xp-noenc\include" /I "I:\work\lib_external\zlib-121-windows\include" /I "I:\work\java\jdk142\include" /I "I:\work\java\jdk142\include\win32" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /Fp"$(INTDIR)\jhdf5.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

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
BSC32_FLAGS=/nologo /o"$(OUTDIR)\jhdf5.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=I:\work\hdf5-165-win\lib\hdf5.lib I:\work\lib_external\szip20-win-xp-noenc\lib\szlib.lib I:\work\lib_external\zlib-121-windows\lib\zlib.lib /nologo /dll /incremental:no /pdb:"$(OUTDIR)\jhdf5.pdb" /machine:I386 /nodefaultlib:"MSVCRT" /out:"$(OUTDIR)\jhdf5.dll" /implib:"$(OUTDIR)\jhdf5.lib" 
LINK32_OBJS= \
	"$(INTDIR)\exceptionImp.obj" \
	"$(INTDIR)\h5aImp.obj" \
	"$(INTDIR)\h5Constants.obj" \
	"$(INTDIR)\h5dImp.obj" \
	"$(INTDIR)\h5fImp.obj" \
	"$(INTDIR)\h5gImp.obj" \
	"$(INTDIR)\h5iImp.obj" \
	"$(INTDIR)\h5Imp.obj" \
	"$(INTDIR)\H5IN.obj" \
	"$(INTDIR)\h5inImp.obj" \
	"$(INTDIR)\h5pImp.obj" \
	"$(INTDIR)\h5rImp.obj" \
	"$(INTDIR)\h5sImp.obj" \
	"$(INTDIR)\h5tImp.obj" \
	"$(INTDIR)\h5util.obj" \
	"$(INTDIR)\h5zImp.obj" \
	"$(INTDIR)\nativeData.obj"

"$(OUTDIR)\jhdf5.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "jhdf5 - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug
# Begin Custom Macros
OutDir=.\Debug
# End Custom Macros

ALL : "$(OUTDIR)\jhdf5.dll"


CLEAN :
	-@erase "$(INTDIR)\exceptionImp.obj"
	-@erase "$(INTDIR)\h5aImp.obj"
	-@erase "$(INTDIR)\h5Constants.obj"
	-@erase "$(INTDIR)\h5dImp.obj"
	-@erase "$(INTDIR)\h5fImp.obj"
	-@erase "$(INTDIR)\h5gImp.obj"
	-@erase "$(INTDIR)\h5iImp.obj"
	-@erase "$(INTDIR)\h5Imp.obj"
	-@erase "$(INTDIR)\H5IN.obj"
	-@erase "$(INTDIR)\h5inImp.obj"
	-@erase "$(INTDIR)\h5pImp.obj"
	-@erase "$(INTDIR)\h5rImp.obj"
	-@erase "$(INTDIR)\h5sImp.obj"
	-@erase "$(INTDIR)\h5tImp.obj"
	-@erase "$(INTDIR)\h5util.obj"
	-@erase "$(INTDIR)\h5zImp.obj"
	-@erase "$(INTDIR)\nativeData.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\jhdf5.dll"
	-@erase "$(OUTDIR)\jhdf5.exp"
	-@erase "$(OUTDIR)\jhdf5.ilk"
	-@erase "$(OUTDIR)\jhdf5.lib"
	-@erase "$(OUTDIR)\jhdf5.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /Gm /GX /ZI /Od /I "E:\work\MyHDFstuff\HDF5143\c\release\include" /I "D:\java\jdk1.3.1\include" /I "D:\java\jdk1.3.1\include\win32" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /Fp"$(INTDIR)\jhdf5.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

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
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /win32 
RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\jhdf5.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /incremental:yes /pdb:"$(OUTDIR)\jhdf5.pdb" /debug /machine:I386 /out:"$(OUTDIR)\jhdf5.dll" /implib:"$(OUTDIR)\jhdf5.lib" /pdbtype:sept 
LINK32_OBJS= \
	"$(INTDIR)\exceptionImp.obj" \
	"$(INTDIR)\h5aImp.obj" \
	"$(INTDIR)\h5Constants.obj" \
	"$(INTDIR)\h5dImp.obj" \
	"$(INTDIR)\h5fImp.obj" \
	"$(INTDIR)\h5gImp.obj" \
	"$(INTDIR)\h5iImp.obj" \
	"$(INTDIR)\h5Imp.obj" \
	"$(INTDIR)\H5IN.obj" \
	"$(INTDIR)\h5inImp.obj" \
	"$(INTDIR)\h5pImp.obj" \
	"$(INTDIR)\h5rImp.obj" \
	"$(INTDIR)\h5sImp.obj" \
	"$(INTDIR)\h5tImp.obj" \
	"$(INTDIR)\h5util.obj" \
	"$(INTDIR)\h5zImp.obj" \
	"$(INTDIR)\nativeData.obj"

"$(OUTDIR)\jhdf5.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("jhdf5.dep")
!INCLUDE "jhdf5.dep"
!ELSE 
!MESSAGE Warning: cannot find "jhdf5.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "jhdf5 - Win32 Release" || "$(CFG)" == "jhdf5 - Win32 Debug"
SOURCE=..\..\..\src\native\hdf5lib\exceptionImp.c

"$(INTDIR)\exceptionImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5aImp.c

"$(INTDIR)\h5aImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5Constants.c

"$(INTDIR)\h5Constants.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5dImp.c

"$(INTDIR)\h5dImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5fImp.c

"$(INTDIR)\h5fImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5gImp.c

"$(INTDIR)\h5gImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5iImp.c

"$(INTDIR)\h5iImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5Imp.c

"$(INTDIR)\h5Imp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\..\work\hdf5index\H5IN.c

"$(INTDIR)\H5IN.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5inImp.c

"$(INTDIR)\h5inImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5pImp.c

"$(INTDIR)\h5pImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5rImp.c

"$(INTDIR)\h5rImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5sImp.c

"$(INTDIR)\h5sImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5tImp.c

"$(INTDIR)\h5tImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5util.c

"$(INTDIR)\h5util.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\h5zImp.c

"$(INTDIR)\h5zImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdf5lib\nativeData.c

"$(INTDIR)\nativeData.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)



!ENDIF 

