// HDFPluginIpFrame.h : interface of the CHDFPluginInPlaceFrame class
//

#if !defined(AFX_HDFPLUGINIPFRAME_H__A04C9859_0744_44BE_AD0E_A9B7085BF53E__INCLUDED_)
#define AFX_HDFPLUGINIPFRAME_H__A04C9859_0744_44BE_AD0E_A9B7085BF53E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CHDFPluginInPlaceFrame : public COleDocIPFrameWnd
{
	DECLARE_DYNCREATE(CHDFPluginInPlaceFrame)
public:
	CHDFPluginInPlaceFrame();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginInPlaceFrame)
	public:
	virtual BOOL OnCreateControlBars(CFrameWnd* pWndFrame, CFrameWnd* pWndDoc);
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CHDFPluginInPlaceFrame();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:
	CToolBar    m_wndToolBar;
	COleDropTarget	m_dropTarget;
	COleResizeBar   m_wndResizeBar;

// Generated message map functions
protected:
	//{{AFX_MSG(CHDFPluginInPlaceFrame)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HDFPLUGINIPFRAME_H__A04C9859_0744_44BE_AD0E_A9B7085BF53E__INCLUDED_)
