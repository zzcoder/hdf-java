# Microsoft Developer Studio Project File - Name="lib" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Dynamic-Link Library" 0x0102

CFG=lib - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "lib.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "lib.mak" CFG="lib - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "lib - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "lib - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "lib - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "LIB_EXPORTS" /YX /FD /c
# ADD CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "LIB_EXPORTS" /YX /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /machine:I386

!ELSEIF  "$(CFG)" == "lib - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "LIB_EXPORTS" /YX /FD /GZ /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /I "\\Eirene\sdt\mcgrath\rudbeckia\Hdf41r5\HDF41r5\include" /I "\\Eirene\sdt\mcgrath\rudbeckia\hdf5-1.4.2-patch1\src" /I "\\Eirene\sdt\mcgrath\rudbeckia\hdf5\src" /I "\\Eirene\sdt\mcgrath\rudbeckia\h4toh5lib\src" /I "D:\Apps\jdk1.3\include" /I "D:\Apps\jdk1.3\include\win32" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "LIB_EXPORTS" /D "_HDFDLL_" /D "_HDFLIB_" /D "_HDF5USEDLL_" /D "_H4TOH5USEDLL_" /YX /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /debug /machine:I386 /out:"..\lib\win32\jh4toh5.dll" /pdbtype:sept

!ENDIF 

# Begin Target

# Name "lib - Win32 Release"
# Name "lib - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=..\native\hdf5lib\exceptionImp.c
# End Source File
# Begin Source File

SOURCE=..\native\h4toh5lib\h45exceptionImp.c
# End Source File
# Begin Source File

SOURCE=..\native\h4toh5lib\h4toh5Imp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5aImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5Constants.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5dImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5fImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5gImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\H5Git.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5iImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5Imp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5pImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5rImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5sImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\h5tImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfanImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfdfanImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfdfcompImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfdfpalImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfdfsdImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfdfuImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfexceptionImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfgrImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfheImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfhxImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfnativeImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfr24Imp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfr8Imp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfsdsImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfstructsutil.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvdataImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvfImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvgroupImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvhImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvqImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdflib\hdfvsqImp.c
# End Source File
# Begin Source File

SOURCE=..\native\hdf5lib\nativeData.c
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# Begin Source File

SOURCE=..\..\test\lib\win32\hdf5ddll.lib
# End Source File
# Begin Source File

SOURCE=..\..\test\lib\win32\hm415md.lib
# End Source File
# Begin Source File

SOURCE=..\..\test\lib\win32\hd415md.lib
# End Source File
# Begin Source File

SOURCE=..\..\test\lib\win32\h425libddlldbg.lib
# End Source File
# End Target
# End Project
