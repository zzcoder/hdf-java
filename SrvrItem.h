// SrvrItem.h : interface of the CHDFPluginSrvrItem class
//

#if !defined(AFX_SRVRITEM_H__F5FEB648_1FFD_4A05_A57C_7738E5DFE340__INCLUDED_)
#define AFX_SRVRITEM_H__F5FEB648_1FFD_4A05_A57C_7738E5DFE340__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CHDFPluginSrvrItem : public CDocObjectServerItem
{
	DECLARE_DYNAMIC(CHDFPluginSrvrItem)

// Constructors
public:
	CHDFPluginSrvrItem(CHDFPluginDoc* pContainerDoc);

// Attributes
	CHDFPluginDoc* GetDocument() const
		{ return (CHDFPluginDoc*)CDocObjectServerItem::GetDocument(); }

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginSrvrItem)
	public:
	virtual BOOL OnDraw(CDC* pDC, CSize& rSize);
	virtual BOOL OnGetExtent(DVASPECT dwDrawAspect, CSize& rSize);
	//}}AFX_VIRTUAL

// Implementation
public:
	~CHDFPluginSrvrItem();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:
	virtual void Serialize(CArchive& ar);   // overridden for document i/o
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SRVRITEM_H__F5FEB648_1FFD_4A05_A57C_7738E5DFE340__INCLUDED_)
