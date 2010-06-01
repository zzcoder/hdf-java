# Microsoft Developer Studio Project File - Name="HDFPlugin" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

CFG=HDFPlugin - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "HDFPlugin.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "HDFPlugin.mak" CFG="HDFPlugin - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "HDFPlugin - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "HDFPlugin - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "HDFPlugin - Win32 Release"

# PROP BASE Use_MFC 5
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 5
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /Yu"stdafx.h" /FD /c
# ADD CPP /nologo /MT /W3 /GX /O2 /I "." /I ".." /I "../.." /I "view" /I "view/ugrid" /I "object" /I "object/h4" /I "object/h5" /I "hdf5/include" /I "hdf4/include" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /Yu"stdafx.h" /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /machine:I386
# ADD LINK32 .\hdf4\lib\hd420.lib .\hdf4\lib\hm420.lib .\hdf5\lib\hdf5.lib .\szip\lib\szlib.lib .\zlib\lib\zlib.lib .\jpeg\lib\libjpeg.lib ws2_32.lib /nologo /subsystem:windows /machine:I386 /nodefaultlib:"LIBC"

!ELSEIF  "$(CFG)" == "HDFPlugin - Win32 Debug"

# PROP BASE Use_MFC 5
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 5
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /Yu"stdafx.h" /FD /GZ /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /I "." /I ".." /I "../.." /I "view" /I "view/ugrid" /I "object" /I "object/h4" /I "object/h5" /I "hdf5/include" /I "hdf4/include" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /FR /Yu"stdafx.h" /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# ADD LINK32 .\hdf4\lib\hd420d.lib .\hdf4\lib\hm420d.lib .\hdf4\lib\hm420d.lib .\hdf5\lib\hdf5.lib .\szip\lib\szlib.lib .\zlib\lib\zlib.lib .\jpeg\lib\libjpeg.lib ws2_32.lib /nologo /subsystem:windows /debug /machine:I386 /nodefaultlib:"LIBCD.lib" /pdbtype:sept

!ENDIF 

# Begin Target

# Name "HDFPlugin - Win32 Release"
# Name "HDFPlugin - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Group "view"

# PROP Default_Filter ""
# Begin Group "ugrid"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\view\ugrid\UGCBType.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCBType.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCell.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCell.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCelTyp.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCelTyp.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCnrBtn.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCnrBtn.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCTarrw.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCTarrw.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCtrl.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGCtrl.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDEFINE.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDLType.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDLType.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDrgDrp.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDrgDrp.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDrwHnt.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDrwHnt.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDtaSrc.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGDtaSrc.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGEdit.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGEdit.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGEXCEL.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGEXCEL.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGFORMAT.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGFORMAT.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGGDINFO.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGGDINFO.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGGrid.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGGrid.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGHint.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGHint.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGHSCROL.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGHSCROL.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\ugLstBox.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGLSTBOX.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGMemMan.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGMemMan.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGMultiS.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGMultiS.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGPRINT.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGPRINT.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGPTRLST.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGPTRLST.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGSIDEHD.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGSIDEHD.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGSTRUCT.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGTAB.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGTAB.H
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGTopHdg.cpp
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGTopHdg.h
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGVSCROL.CPP
# End Source File
# Begin Source File

SOURCE=.\view\ugrid\UGVSCROL.H
# End Source File
# End Group
# Begin Source File

SOURCE=.\view\BaseView.cpp
# End Source File
# Begin Source File

SOURCE=.\view\BaseView.h
# End Source File
# Begin Source File

SOURCE=.\view\GridCtrl.cpp
# End Source File
# Begin Source File

SOURCE=.\view\GridCtrl.h
# End Source File
# Begin Source File

SOURCE=.\view\HDFPluginDoc.cpp
# End Source File
# Begin Source File

SOURCE=.\view\HDFPluginDoc.h
# End Source File
# Begin Source File

SOURCE=.\view\HDFPluginTreeView.cpp
# End Source File
# Begin Source File

SOURCE=.\view\HDFPluginTreeView.h
# End Source File
# Begin Source File

SOURCE=.\view\ImageView.cpp

!IF  "$(CFG)" == "HDFPlugin - Win32 Release"

# ADD CPP /I ".."

!ELSEIF  "$(CFG)" == "HDFPlugin - Win32 Debug"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\view\ImageView.h
# End Source File
# Begin Source File

SOURCE=.\view\TableView.cpp
# End Source File
# Begin Source File

SOURCE=.\view\TableView.h
# End Source File
# Begin Source File

SOURCE=.\view\TextView.cpp

!IF  "$(CFG)" == "HDFPlugin - Win32 Release"

# ADD CPP /I ".."

!ELSEIF  "$(CFG)" == "HDFPlugin - Win32 Debug"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\view\TextView.h
# End Source File
# End Group
# Begin Group "object"

# PROP Default_Filter ""
# Begin Group "h4"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\object\h4\HdfObjH4AtomicDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4AtomicDS.h
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4CompoundDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4CompoundDS.h
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4File.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4File.h
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4Group.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4Group.h
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4ImageDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h4\HdfObjH4ImageDS.h
# End Source File
# End Group
# Begin Group "h5"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\object\h5\HdfObjH5AtomicDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5AtomicDS.h
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5CompoundDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5CompoundDS.h
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5File.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5File.h
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5Group.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5Group.h
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5ImageDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\h5\HdfObjH5ImageDS.h
# End Source File
# End Group
# Begin Source File

SOURCE=.\object\HdfObj.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObj.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjAtomicDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjAtomicDS.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjAttribute.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjAttribute.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjCompoundDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjCompoundDS.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjDataset.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjDataset.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjFileFormat.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjFileFormat.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjGroup.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjGroup.h
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjImageDS.cpp
# End Source File
# Begin Source File

SOURCE=.\object\HdfObjImageDS.h
# End Source File
# End Group
# Begin Source File

SOURCE=.\HDFPlugin.cpp
# End Source File
# Begin Source File

SOURCE=.\HDFPlugin.rc
# End Source File
# Begin Source File

SOURCE=.\HDFPluginIpFrame.cpp
# End Source File
# Begin Source File

SOURCE=.\HDFPluginMainFrm.cpp
# End Source File
# Begin Source File

SOURCE=.\SrvrItem.cpp
# End Source File
# Begin Source File

SOURCE=.\StdAfx.cpp
# ADD CPP /Yc"stdafx.h"
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\HDFPlugin.h
# End Source File
# Begin Source File

SOURCE=.\HDFPluginIpFrame.h
# End Source File
# Begin Source File

SOURCE=.\HDFPluginMainFrm.h
# End Source File
# Begin Source File

SOURCE=.\Resource.h
# End Source File
# Begin Source File

SOURCE=.\SrvrItem.h
# End Source File
# Begin Source File

SOURCE=.\StdAfx.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# Begin Source File

SOURCE=.\res\HDFPlugin.ico
# End Source File
# Begin Source File

SOURCE=.\res\HDFPlugin.rc2
# End Source File
# Begin Source File

SOURCE=.\res\HDFPluginDoc.ico
# End Source File
# Begin Source File

SOURCE=.\icons\icon01_hdf.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon02_hdf4.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon03_hdf5.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon04_animation.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon05_blank.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon06_chart.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon07_copy.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon08_cut.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon09_dataset.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon10_fileclose.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon11_filenew.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon12_fileopen.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon13_filesave.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon14_first.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon15_help.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon16_image.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon17_last.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon18_next.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon19_folderclose.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon20_folderopen.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon21_palette.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon22_paste.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon23_previous.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon24_table.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon25_text.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon26_zoomin.bmp
# End Source File
# Begin Source File

SOURCE=.\icons\icon27_zoomout.bmp
# End Source File
# Begin Source File

SOURCE=.\res\IToolbar.bmp
# End Source File
# Begin Source File

SOURCE=.\res\Toolbar.bmp
# End Source File
# End Group
# Begin Source File

SOURCE=.\HDFPlugin.reg
# End Source File
# Begin Source File

SOURCE=.\ReadMe.txt
# End Source File
# End Target
# End Project
