#if !defined(AFX_BASEVIEW_H__45795658_53AC_4EEF_9945_4961C680C67B__INCLUDED_)
#define AFX_BASEVIEW_H__45795658_53AC_4EEF_9945_4961C680C67B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// BaseView.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CBaseView view

class CBaseView : public CScrollView
{
protected:
	CBaseView();           // protected constructor used by dynamic creation
	DECLARE_DYNCREATE(CBaseView)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CBaseView)
	protected:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
	virtual void OnInitialUpdate();     // first time after construct
	//}}AFX_VIRTUAL

// Implementation
protected:
	virtual ~CBaseView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	// Generated message map functions
	//{{AFX_MSG(CBaseView)
		// NOTE - the ClassWizard will add and remove member functions here.
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_BASEVIEW_H__45795658_53AC_4EEF_9945_4961C680C67B__INCLUDED_)
