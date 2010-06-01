// HDFPluginTreeView.h : interface of the CHDFPluginTreeView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFPLUGINTREEVIEW_H__D814187C_D941_4FDB_8C89_8836010B50EC__INCLUDED_)
#define AFX_HDFPLUGINTREEVIEW_H__D814187C_D941_4FDB_8C89_8836010B50EC__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjGroup.h"
#include "HdfObjImageDS.h"
#include "HdfObjAtomicDS.h"
#include "HdfObjCompoundDS.h"
#include "TableView.h"
#include "ImageView.h"
#include "TextView.h"


class CHDFPluginDoc;

class CHDFPluginTreeView : public CTreeView
{
protected: // create from serialization only
	CHDFPluginTreeView();
	DECLARE_DYNCREATE(CHDFPluginTreeView)

// Attributes
public:
	CHDFPluginDoc* GetDocument();
	CFileFormat* GetCurrentFile() { return m_pCurrentFile; }

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginTreeView)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnInitialUpdate(); // called first time after construct
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CHDFPluginTreeView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	CTableView* m_pTableView;
	CImageView* m_pImageView;
	CTextView* m_pTextView;
	CFileFormat* m_pCurrentFile;

	//{{AFX_MSG(CHDFPluginTreeView)
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()

private:
	CView* SwitchRightPane(CView* pNewView);
	void ShowTableView(CDataset* pDataset);
	void ShowImageView(CImageDS* pDataset);
	void ShowMetaData(CHObject* pObj);
	CImageList * imgList;
	void LoadBitmaps();
	void TraverseInsertNodes(HTREEITEM parentNode, CGroup* pParentGroup);
	void ShowFileTree(CFileFormat* pFile);
};

#ifndef _DEBUG  // debug version in HDFPluginTreeView.cpp
inline CHDFPluginDoc* CHDFPluginTreeView::GetDocument()
   { return (CHDFPluginDoc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HDFPLUGINTREEVIEW_H__D814187C_D941_4FDB_8C89_8836010B50EC__INCLUDED_)
