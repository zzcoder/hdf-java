#if !defined(AFX_TABLEVIEW_H__2EC40C59_6AEC_4638_9658_A24DCE3364D6__INCLUDED_)
#define AFX_TABLEVIEW_H__2EC40C59_6AEC_4638_9658_A24DCE3364D6__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// TableView.h : header file
//

#include "BaseView.h"
#include "GridCtrl.h"
#include "HdfObjDataset.h"

/////////////////////////////////////////////////////////////////////////////
// CTableView view

class CTableView : public CBaseView
{
public:
	CTableView();           // protected constructor used by dynamic creation
	DECLARE_DYNCREATE(CTableView)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CTableView)
	protected:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
	virtual void OnInitialUpdate();     // first time after construct
	//}}AFX_VIRTUAL

// Implementation
public:
	void SetDataset(CDataset* pDataset);
	CDataset* GetDataset();
	virtual ~CTableView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	// Generated message map functions
	//{{AFX_MSG(CTableView)
	afx_msg void OnSize(UINT nType, int cx, int cy);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
protected:
	CGridCtrl* m_pGridCtrl;
	CDataset* m_pDataset;
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_TABLEVIEW_H__2EC40C59_6AEC_4638_9658_A24DCE3364D6__INCLUDED_)
