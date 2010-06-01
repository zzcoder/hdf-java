// HDFPlugin.h : main header file for the HDFPLUGIN application
//

#if !defined(AFX_HDFPLUGIN_H__1AFAED68_FEF6_4370_A486_260729669871__INCLUDED_)
#define AFX_HDFPLUGIN_H__1AFAED68_FEF6_4370_A486_260729669871__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

#define SWEEP_BUFFER_SIZE			10000


/////////////////////////////////////////////////////////////////////////////
// CHDFPluginApp:
// See HDFPlugin.cpp for the implementation of this class
//

class CHDFPluginApp : public CWinApp
{
public:
	CHDFPluginApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginApp)
	public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();
	//}}AFX_VIRTUAL

// Implementation
	COleTemplateServer m_server;
		// Server object for document creation
	//{{AFX_MSG(CHDFPluginApp)
	afx_msg void OnAppAbout();
	afx_msg void OnFileOpen();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
protected:
	BOOL EmptyDirectory(LPCTSTR szPath);
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HDFPLUGIN_H__1AFAED68_FEF6_4370_A486_260729669871__INCLUDED_)
