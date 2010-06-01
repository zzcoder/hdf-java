#if !defined(AFX_IMAGEVIEW_H__8C6C5275_1A93_4DEB_B188_DB9ED3E1620F__INCLUDED_)
#define AFX_IMAGEVIEW_H__8C6C5275_1A93_4DEB_B188_DB9ED3E1620F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// ImageView.h : header file
//

#include "BaseView.h"
#include "HdfObjImageDS.h"

/////////////////////////////////////////////////////////////////////////////
// CImageView view

class CImageView : public CBaseView
{
public:
	CImageView();           // protected constructor used by dynamic creation
	DECLARE_DYNCREATE(CImageView)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CImageView)
	protected:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
	virtual void OnInitialUpdate();     // first time after construct
	//}}AFX_VIRTUAL

// Implementation
public:
	CImageDS* GetImageDS();
	void SetImageDS(CImageDS* pImageDS);
	virtual ~CImageView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	// Generated message map functions
	//{{AFX_MSG(CImageView)
	afx_msg void OnSize(UINT nType, int cx, int cy);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
protected:
	void CreateIndexedImage(CDC* pDC);
	void CreateTrueColorImage(CDC* pDC);
	CSize m_sizeImage;
	int m_nPixels;
	int m_nInterlace;
	CBitmap m_bitmap;
	BOOL m_bIsTrueColor;
	CImageDS* m_pImageDS;
	void CreateImage();
	BYTE* m_pImageData;
	BYTE m_pR[256];
	BYTE m_pG[256];
	BYTE m_pB[256];
	CPalette* m_pPalette;
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_IMAGEVIEW_H__8C6C5275_1A93_4DEB_B188_DB9ED3E1620F__INCLUDED_)
