#if !defined(AFX_TEXTVIEW_H__953EA395_E31D_4E18_ADD0_1C195D586CEC__INCLUDED_)
#define AFX_TEXTVIEW_H__953EA395_E31D_4E18_ADD0_1C195D586CEC__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// TextView.h : header file
//

#include "BaseView.h"
#include "HdfObj.h"
#include <AFXRICH.H>

DWORD __stdcall MEditStreamInCallback(DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb);
DWORD __stdcall MEditStreamOutCallback(DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb);

/////////////////////////////////////////////////////////////////////////////
// CTextView view

class CTextView : public CRichEditView
{
//protected:
public:
	CTextView();           // protected constructor used by dynamic creation
	DECLARE_DYNCREATE(CTextView)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CTextView)
	protected:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
	virtual void OnInitialUpdate();     // first time after construct
	//}}AFX_VIRTUAL

// Implementation
//protected:
public:
	virtual ~CTextView();
	void Readout(CString sReadText); 
	void Readin(CString sWriteText); 
	CHObject* GetObj() { return m_pObj; }
	void SetObj(CHObject* obj);

#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	// Generated message map functions
	//{{AFX_MSG(CTextView)
		// NOTE - the ClassWizard will add and remove member functions here.
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()

protected:
	CHObject* m_pObj;
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.


#endif // !defined(AFX_TEXTVIEW_H__953EA395_E31D_4E18_ADD0_1C195D586CEC__INCLUDED_)
