// HDFPlugin.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include <afxpriv.h>  //mfc private
#include <shlobj.h>
#include "HDFPlugin.h"

#include "HDFPluginMainFrm.h"
#include "HDFPluginIpFrame.h"
#include "HDFPluginDoc.h"
#include "HDFPluginTreeView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginApp

BEGIN_MESSAGE_MAP(CHDFPluginApp, CWinApp)
	//{{AFX_MSG_MAP(CHDFPluginApp)
	ON_COMMAND(ID_APP_ABOUT, OnAppAbout)
	ON_COMMAND(ID_FILE_OPEN, OnFileOpen)
	//}}AFX_MSG_MAP
	// Standard file based document commands
	ON_COMMAND(ID_FILE_NEW, CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, CWinApp::OnFileOpen)
	// Standard print setup command
	ON_COMMAND(ID_FILE_PRINT_SETUP, CWinApp::OnFilePrintSetup)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginApp construction

CHDFPluginApp::CHDFPluginApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance

/*
	// clean IE cache
	TCHAR szPath[MAX_PATH];
	if (SHGetSpecialFolderPath(NULL, szPath, CSIDL_INTERNET_CACHE, FALSE))
	{
		strcat(szPath, "\\Content.IE5");
		EmptyDirectory(szPath);
	}
*/

}

/////////////////////////////////////////////////////////////////////////////
// The one and only CHDFPluginApp object

CHDFPluginApp theApp;

// This identifier was generated to be statistically unique for your app.
// You may change it if you prefer to choose a specific identifier.

// {3CFCA545-709C-4540-A242-4A6E01D6868A}
static const CLSID clsid =
{ 0x3cfca545, 0x709c, 0x4540, { 0xa2, 0x42, 0x4a, 0x6e, 0x1, 0xd6, 0x86, 0x8a } };

static BOOL NEAR PASCAL SetRegKey(LPCSTR lpszKey, LPCSTR lpszValue)
{ 
	DWORD   err = ::RegSetValue(HKEY_CLASSES_ROOT, lpszKey, REG_SZ, lpszValue, lstrlen(lpszValue));

	if (err != ERROR_SUCCESS)
    {
		TRACE1("Warning: registration database update failed for key %Fs\n", lpszKey);
		return FALSE;
	}

	return TRUE;
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginApp initialization

BOOL CHDFPluginApp::InitInstance()
{
	// Initialize OLE libraries
	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}

	AfxEnableControlContainer();

	// Standard initialization
	// If you are not using these features and wish to reduce the size
	//  of your final executable, you should remove from the following
	//  the specific initialization routines you do not need.

#ifdef _AFXDLL
	Enable3dControls();			// Call this when using MFC in a shared DLL
#else
	Enable3dControlsStatic();	// Call this when linking to MFC statically
#endif

	// Change the registry key under which our settings are stored.
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization.
	SetRegistryKey(_T("Local AppWizard-Generated Applications"));

	LoadStdProfileSettings(16);  // Load standard INI file options (including MRU)

	// Register the application's document templates.  Document templates
	//  serve as the connection between documents, frame windows and views.

	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CHDFPluginDoc),
		RUNTIME_CLASS(CHDFPluginMainFrame),       // main SDI frame window
		RUNTIME_CLASS(CHDFPluginTreeView));
	pDocTemplate->SetServerInfo(
		IDR_SRVR_EMBEDDED, IDR_SRVR_INPLACE,
		RUNTIME_CLASS(CHDFPluginInPlaceFrame));

	AddDocTemplate(pDocTemplate);

	// Connect the COleTemplateServer to the document template.
	//  The COleTemplateServer creates new documents on behalf
	//  of requesting OLE containers by using information
	//  specified in the document template.
	m_server.ConnectTemplate(clsid, pDocTemplate, FALSE);
		// Note: SDI applications register server objects only if /Embedding
		//   or /Automation is present on the command line.

	// When a server application is launched stand-alone, it is a good idea
	//  to update the system registry in case it has been damaged.
	m_server.UpdateRegistry(OAT_DOC_OBJECT_SERVER);
	COleObjectFactory::UpdateRegistryAll();

	// SetRegKey adds a file extension to the Registration Database
	(void)SetRegKey(".h5", "HDFPlugin.Document");
	(void)SetRegKey(".hdf", "HDFPlugin.Document");
	(void)SetRegKey(".he5", "HDFPlugin.Document");
	(void)SetRegKey(".hdf5", "HDFPlugin.Document");
	(void)SetRegKey(".he", "HDFPlugin.Document");

	// Enable DDE Execute open
	EnableShellOpen();
	RegisterShellFileTypes(TRUE);

	// Parse command line for standard shell commands, DDE, file open
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	// Check to see if launched as OLE server
	if (cmdInfo.m_bRunEmbedded || cmdInfo.m_bRunAutomated)
	{
		// Register all OLE server (factories) as running.  This enables the
		//  OLE libraries to create objects from other applications.
		//COleTemplateServer::RegisterAll();
		m_server.RegisterAll();

		// Application was run with /Embedding or /Automation.  Don't show the
		//  main window in this case.
		return TRUE;
	}

	// Dispatch commands specified on the command line
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	m_pMainWnd->SetIcon(LoadIcon(IDR_HDFTYPE), TRUE);
	CString app_path, ext_key;
	AfxGetModuleShortFileName(AfxGetInstanceHandle(), app_path);
	ext_key.Format("%s\\DefaultIcon", "HDFPlugin.Document" );
	(void)SetRegKey(ext_key, app_path+",1");

	// The one and only window has been initialized, so show and update it.
	m_pMainWnd->ShowWindow(SW_SHOW);
	m_pMainWnd->UpdateWindow();

	// Enable drag/drop open
	m_pMainWnd->DragAcceptFiles();

	if (strcmp(cmdInfo.m_strFileName, "Initilization_SetRegistry_For_HDFPlugin")== 0)
	{
		this->CloseAllDocuments(TRUE);
		this->ExitInstance();
	}

	return TRUE;
}


/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
		// No message handlers
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

// App command to run the dialog
void CHDFPluginApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginApp message handlers


int CHDFPluginApp::ExitInstance() 
{
	// TODO: Add your specialized code here and/or call the base class
	return CWinApp::ExitInstance();
}

void CHDFPluginApp::OnFileOpen() 
{
	// TODO: Add your command handler code here
	static TCHAR szFilter[] = 
		_T("HDF Files (*.hdf;*.h5;*.he;*.he5;*.hdf5)|*.hdf;*.h5;*.he;*.he5;*.hdf5|AllFiles(*.*)|*.*|") ;
	CFileDialog dlg( TRUE, _T("*.hdf;*.hdf4;*.h5;*.he;*.he5;*.hdf5"), NULL,
		OFN_FILEMUSTEXIST | OFN_HIDEREADONLY | OFN_PATHMUSTEXIST
		,szFilter, NULL )  ;
	
	if (IDOK != dlg.DoModal())
		return;

	OpenDocumentFile(dlg.GetPathName());	
}

BOOL CHDFPluginApp::EmptyDirectory(LPCTSTR szPath)
{
	WIN32_FIND_DATA wfd;
	HANDLE hFind;
	CString sFullPath;
	CString sFindFilter;
	DWORD dwAttributes = 0;

	sFindFilter = szPath;
	sFindFilter += _T("\\*.*");
	if ((hFind = FindFirstFile(sFindFilter, &wfd)) == INVALID_HANDLE_VALUE)
	{
		return FALSE;
	}

	do
	{
		if (_tcscmp(wfd.cFileName, _T(".")) == 0 || 
			_tcscmp(wfd.cFileName, _T("..")) == 0)
		{
			continue;
		}

		sFullPath = szPath;
		sFullPath += _T('\\');
		sFullPath += wfd.cFileName;

		//remove readonly attributes
		dwAttributes = GetFileAttributes(sFullPath);
		if (dwAttributes & FILE_ATTRIBUTE_READONLY)
		{
			dwAttributes &= ~FILE_ATTRIBUTE_READONLY;
			SetFileAttributes(sFullPath, dwAttributes);
		}

		if (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		{
			EmptyDirectory(sFullPath);
			RemoveDirectory(sFullPath);
		}
		else
		{
			DeleteFile(sFullPath);
		}
	}
	while (FindNextFile(hFind, &wfd));
	FindClose(hFind);

	return TRUE;
}

