// HDFPluginDoc.h : interface of the CHDFPluginDoc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFPLUGINDOC_H__0579C84E_4F57_40CF_BC62_034B308C1339__INCLUDED_)
#define AFX_HDFPLUGINDOC_H__0579C84E_4F57_40CF_BC62_034B308C1339__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CHDFPluginSrvrItem;

class CHDFPluginDoc : public COleServerDoc
{
protected: // create from serialization only
	CHDFPluginDoc();
	DECLARE_DYNCREATE(CHDFPluginDoc)

// Attributes
public:
	CHDFPluginSrvrItem* GetEmbeddedItem()
		{ return (CHDFPluginSrvrItem*)COleServerDoc::GetEmbeddedItem(); }

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHDFPluginDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual BOOL IsModified();
	virtual void Serialize(CArchive& ar);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void OnCloseDocument();
	protected:
	virtual COleServerItem* OnGetEmbeddedItem();
	virtual COleIPFrameWnd* CreateInPlaceFrame(CWnd* pParentWnd);
	virtual void DestroyInPlaceFrame(COleIPFrameWnd* pFrameWnd);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CHDFPluginDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:
	virtual CDocObjectServer* GetDocObjectServer(LPOLEDOCUMENTSITE pDocSite);

// Generated message map functions
protected:
	//{{AFX_MSG(CHDFPluginDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HDFPLUGINDOC_H__0579C84E_4F57_40CF_BC62_034B308C1339__INCLUDED_)
