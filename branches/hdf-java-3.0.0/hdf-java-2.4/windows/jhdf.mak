# Microsoft Developer Studio Generated NMAKE File, Based on jhdf4.dsp
!IF "$(CFG)" == ""
CFG=jhdf4 - Win32 Debug
!MESSAGE No configuration specified. Defaulting to jhdf4 - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "jhdf4 - Win32 Release" && "$(CFG)" != "jhdf4 - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "jhdf4.mak" CFG="jhdf4 - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "jhdf4 - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "jhdf4 - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "jhdf4 - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\jhdf.dll"


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
	-@erase "$(OUTDIR)\jhdf.dll"
	-@erase "$(OUTDIR)\jhdf.exp"
	-@erase "$(OUTDIR)\jhdf.lib"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /GX /O2 /I "D:\work\hdf-42r1\release\include" /I "D:\java\jdk1.4.2\include" /I "D:\java\jdk1.4.2\include\win32" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /Fp"$(INTDIR)\jhdf4.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

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
BSC32_FLAGS=/nologo /o"$(OUTDIR)\jhdf4.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=D:\work\hdf-42r1\release\lib\hd421.lib D:\work\hdf-42r1\release\lib\hm421.lib d:\work\lib-external\jpeg-6b\libjpeg.lib D:\work\lib-external\szip20\encoder\lib\szlib.lib D:\work\lib-external\zlib122\lib\libz.lib Ws2_32.lib /nologo /dll /incremental:no /pdb:"$(OUTDIR)\jhdf.pdb" /machine:I386 /out:"$(OUTDIR)\jhdf.dll" /implib:"$(OUTDIR)\jhdf.lib" 
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

!ELSEIF  "$(CFG)" == "jhdf4 - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug
# Begin Custom Macros
OutDir=.\Debug
# End Custom Macros

ALL : "$(OUTDIR)\jhdf4.dll"


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
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\jhdf4.dll"
	-@erase "$(OUTDIR)\jhdf4.exp"
	-@erase "$(OUTDIR)\jhdf4.ilk"
	-@erase "$(OUTDIR)\jhdf4.lib"
	-@erase "$(OUTDIR)\jhdf4.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /ZI /Od /I "D:\work\hdf-42r0\include" /I "D:\java\jdk1.4.2\include" /I "D:\java\jdk1.4.2\include\win32" /Fp"$(INTDIR)\jhdf4.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

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
BSC32_FLAGS=/nologo /o"$(OUTDIR)\jhdf4.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=d:\work\hdf-42r0\lib\hd420.lib d:\work\hdf-42r0\lib\hm420.lib d:\work\lib-external\jpeg\libjpeg.lib d:\work\lib-external\szip\bin\windows\szip-msvc++\lib\szlib.lib d:\work\lib-external\zlib\bin\windows\zlib114\lib\zlib.lib /nologo /dll /incremental:yes /pdb:"$(OUTDIR)\jhdf4.pdb" /debug /machine:I386 /out:"$(OUTDIR)\jhdf4.dll" /implib:"$(OUTDIR)\jhdf4.lib" /pdbtype:sept 
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

"$(OUTDIR)\jhdf4.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("jhdf4.dep")
!INCLUDE "jhdf4.dep"
!ELSE 
!MESSAGE Warning: cannot find "jhdf4.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "jhdf4 - Win32 Release" || "$(CFG)" == "jhdf4 - Win32 Debug"
SOURCE=..\..\..\src\native\hdflib\hdfanImp.c

"$(INTDIR)\hdfanImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfdfanImp.c

"$(INTDIR)\hdfdfanImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfdfcompImp.c

"$(INTDIR)\hdfdfcompImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfdfpalImp.c

"$(INTDIR)\hdfdfpalImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfdfsdImp.c

"$(INTDIR)\hdfdfsdImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfdfuImp.c

"$(INTDIR)\hdfdfuImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfexceptionImp.c

"$(INTDIR)\hdfexceptionImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfgrImp.c

"$(INTDIR)\hdfgrImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfheImp.c

"$(INTDIR)\hdfheImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfhxImp.c

"$(INTDIR)\hdfhxImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfImp.c

"$(INTDIR)\hdfImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfnativeImp.c

"$(INTDIR)\hdfnativeImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfr24Imp.c

"$(INTDIR)\hdfr24Imp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfr8Imp.c

"$(INTDIR)\hdfr8Imp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfsdsImp.c

"$(INTDIR)\hdfsdsImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfstructsutil.c

"$(INTDIR)\hdfstructsutil.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvdataImp.c

"$(INTDIR)\hdfvdataImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvfImp.c

"$(INTDIR)\hdfvfImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvgroupImp.c

"$(INTDIR)\hdfvgroupImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvhImp.c

"$(INTDIR)\hdfvhImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvqImp.c

"$(INTDIR)\hdfvqImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


SOURCE=..\..\..\src\native\hdflib\hdfvsqImp.c

"$(INTDIR)\hdfvsqImp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)



!ENDIF 

