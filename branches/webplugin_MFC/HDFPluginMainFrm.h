// HDFPluginMainFrm.h : interface of the CHDFPluginMainFrame class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFPLUGINMAINFRM_H__17AC400C_D3BB_44A1_92AE_EC28F69E009E__INCLUDED_)
#define AFX_HDFPLUGINMAINFRM_H__17AC400C_D3BB_44A1_92AE_EC28F69E009E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HDFPluginTreeView.h"

class CHDFPluginMainFrame : public CFrameWnd
{
public: // create from serialization only
	CHDFPluginMainFrame();
	DECLARE_DYNCREATE(CHDFPluginMainFrame)

// Attributes
protected:
	CSplitterWnd m_wndSplitter;

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginMainFrame)
	public:
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CHDFPluginMainFrame();
	CView* GetRightPane();
	CHDFPluginTreeView* GetTreeView();

#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:  // control bar embedded members
	CStatusBar  m_wndStatusBar;
	CToolBar    m_wndToolBar;

// Generated message map functions
protected:
	//{{AFX_MSG(CHDFPluginMainFrame)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	//}}AFX_MSG
	afx_msg void OnUpdateViewStyles(CCmdUI* pCmdUI);
	afx_msg void OnViewStyle(UINT nCommandID);
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HDFPLUGINMAINFRM_H__17AC400C_D3BB_44A1_92AE_EC28F69E009E__INCLUDED_)
