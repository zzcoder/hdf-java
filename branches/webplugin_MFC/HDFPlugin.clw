; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CTextView
LastTemplate=CScrollView
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "HDFPlugin.h"
LastPage=0

ClassCount=11
Class1=CHDFPluginApp
Class2=CHDFPluginDoc
Class3=CHDFPluginDataView
Class4=CHDFPluginMainFrame
Class5=CHDFPluginInPlaceFrame

ResourceCount=4
Resource1=IDR_MAINFRAME
Resource2=IDR_SRVR_INPLACE
Class6=CHDFPluginTreeView
Class7=CAboutDlg
Resource3=IDR_SRVR_EMBEDDED
Class8=CBaseView
Class9=CImageView
Class10=CTextView
Class11=CTableView
Resource4=IDD_ABOUTBOX

[CLS:CHDFPluginApp]
Type=0
HeaderFile=HDFPlugin.h
ImplementationFile=HDFPlugin.cpp
Filter=N
BaseClass=CWinApp
VirtualFilter=AC
LastObject=CHDFPluginApp

[CLS:CHDFPluginDoc]
Type=0
HeaderFile=view\hdfplugindoc.h
ImplementationFile=view\hdfplugindoc.cpp
BaseClass=COleServerDoc
LastObject=CHDFPluginDoc
Filter=N
VirtualFilter=vLODC

[CLS:CHDFPluginDataView]
Type=0
HeaderFile=view\hdfplugindataview.h
ImplementationFile=view\hdfplugindataview.cpp
BaseClass=CListView
LastObject=CHDFPluginDataView

[CLS:CHDFPluginMainFrame]
Type=0
HeaderFile=HDFPluginMainFrm.h
ImplementationFile=HDFPluginMainFrm.cpp
Filter=T
LastObject=CHDFPluginMainFrame
BaseClass=CFrameWnd
VirtualFilter=fWC

[CLS:CHDFPluginInPlaceFrame]
Type=0
HeaderFile=HDFPluginIpFrame.h
ImplementationFile=HDFPluginIpFrame.cpp
Filter=T
LastObject=CHDFPluginInPlaceFrame


[CLS:CHDFPluginTreeView]
Type=0
HeaderFile=view\hdfplugintreeview.h
ImplementationFile=view\hdfplugintreeview.cpp
BaseClass=CTreeView
LastObject=CHDFPluginTreeView
Filter=C
VirtualFilter=VWC

[CLS:CAboutDlg]
Type=0
HeaderFile=HDFPlugin.cpp
ImplementationFile=HDFPlugin.cpp
Filter=D
LastObject=CAboutDlg

[DLG:IDD_ABOUTBOX]
Type=1
Class=CAboutDlg
ControlCount=4
Control1=IDC_STATIC,static,1342177283
Control2=IDOK,button,1342373889
Control3=IDC_STATIC,static,1342312588
Control4=IDC_STATIC,static,1342308492

[MNU:IDR_MAINFRAME]
Type=1
Class=CHDFPluginMainFrame
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_FILE_PRINT
Command4=ID_FILE_PRINT_PREVIEW
Command5=ID_FILE_PRINT_SETUP
Command6=ID_FILE_MRU_FILE1
Command7=ID_APP_EXIT
Command8=ID_EDIT_UNDO
Command9=ID_EDIT_CUT
Command10=ID_EDIT_COPY
Command11=ID_EDIT_PASTE
Command12=ID_VIEW_TOOLBAR
Command13=ID_VIEW_STATUS_BAR
Command14=ID_APP_ABOUT
CommandCount=14

[MNU:IDR_SRVR_INPLACE]
Type=1
Class=CHDFPluginDataView
Command1=ID_EDIT_UNDO
Command2=ID_EDIT_CUT
Command3=ID_EDIT_COPY
Command4=ID_EDIT_PASTE
Command5=ID_VIEW_TOOLBAR
Command6=ID_APP_ABOUT
CommandCount=6

[TB:IDR_SRVR_INPLACE]
Type=1
Class=CHDFPluginDataView
Command1=ID_EDIT_CUT
Command2=ID_EDIT_COPY
Command3=ID_EDIT_PASTE
Command4=ID_APP_ABOUT
CommandCount=4

[MNU:IDR_SRVR_EMBEDDED]
Type=1
Class=CHDFPluginDataView
Command1=ID_FILE_UPDATE
Command2=ID_FILE_PRINT
Command3=ID_FILE_PRINT_PREVIEW
Command4=ID_FILE_PRINT_SETUP
Command5=ID_APP_EXIT
Command6=ID_EDIT_UNDO
Command7=ID_EDIT_CUT
Command8=ID_EDIT_COPY
Command9=ID_EDIT_PASTE
Command10=ID_VIEW_TOOLBAR
Command11=ID_VIEW_STATUS_BAR
Command12=ID_APP_ABOUT
CommandCount=12

[ACL:IDR_MAINFRAME]
Type=1
Class=CHDFPluginMainFrame
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_FILE_SAVE
Command4=ID_FILE_PRINT
Command5=ID_EDIT_UNDO
Command6=ID_EDIT_CUT
Command7=ID_EDIT_COPY
Command8=ID_EDIT_PASTE
Command9=ID_EDIT_UNDO
Command10=ID_EDIT_CUT
Command11=ID_EDIT_COPY
Command12=ID_EDIT_PASTE
Command13=ID_NEXT_PANE
Command14=ID_PREV_PANE
CommandCount=14

[ACL:IDR_SRVR_INPLACE]
Type=1
Class=CHDFPluginDataView
Command1=ID_EDIT_UNDO
Command2=ID_EDIT_CUT
Command3=ID_EDIT_COPY
Command4=ID_EDIT_PASTE
Command5=ID_EDIT_UNDO
Command6=ID_EDIT_CUT
Command7=ID_EDIT_COPY
Command8=ID_EDIT_PASTE
Command9=ID_CANCEL_EDIT_SRVR
CommandCount=9

[ACL:IDR_SRVR_EMBEDDED]
Type=1
Class=CHDFPluginDataView
Command1=ID_FILE_UPDATE
Command2=ID_FILE_PRINT
Command3=ID_EDIT_UNDO
Command4=ID_EDIT_CUT
Command5=ID_EDIT_COPY
Command6=ID_EDIT_PASTE
Command7=ID_EDIT_UNDO
Command8=ID_EDIT_CUT
Command9=ID_EDIT_COPY
Command10=ID_EDIT_PASTE
Command11=ID_NEXT_PANE
Command12=ID_PREV_PANE
CommandCount=12

[TB:IDR_MAINFRAME]
Type=1
Class=?
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_EDIT_CUT
Command4=ID_EDIT_COPY
Command5=ID_EDIT_PASTE
Command6=ID_FILE_PRINT
Command7=ID_VIEW_LARGEICON
Command8=ID_VIEW_SMALLICON
Command9=ID_VIEW_LIST
Command10=ID_VIEW_DETAILS
Command11=ID_APP_ABOUT
CommandCount=11

[CLS:CBaseView]
Type=0
HeaderFile=view\BaseView.h
ImplementationFile=view\BaseView.cpp
BaseClass=CScrollView
Filter=C
LastObject=CBaseView

[CLS:CImageView]
Type=0
HeaderFile=view\ImageView.h
ImplementationFile=view\ImageView.cpp
BaseClass=CBaseView
Filter=C
LastObject=CImageView
VirtualFilter=VWC

[CLS:CTextView]
Type=0
HeaderFile=view\TextView.h
ImplementationFile=view\TextView.cpp
BaseClass=CScrollView
Filter=C
LastObject=CTextView

[CLS:CTableView]
Type=0
HeaderFile=view\TableView.h
ImplementationFile=view\TableView.cpp
BaseClass=CBaseView
Filter=C
LastObject=CTableView
VirtualFilter=VWC

