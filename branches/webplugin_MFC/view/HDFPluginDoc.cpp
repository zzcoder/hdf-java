// HDFPluginDoc.cpp : implementation of the CHDFPluginDoc class
//

#include "stdafx.h"
#include "afxpriv.h" // for WM_INITIALUPDATE
#include "HDFPlugin.h"

#include "HDFPluginDoc.h"
#include "SrvrItem.h"
#include "HDFPluginMainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc

IMPLEMENT_DYNCREATE(CHDFPluginDoc, COleServerDoc)

BEGIN_MESSAGE_MAP(CHDFPluginDoc, COleServerDoc)
	//{{AFX_MSG_MAP(CHDFPluginDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc construction/destruction

CHDFPluginDoc::CHDFPluginDoc()
{
	// Use OLE compound files
	EnableCompoundFile();

	// TODO: add one-time construction code here

}

CHDFPluginDoc::~CHDFPluginDoc()
{
}

BOOL CHDFPluginDoc::OnNewDocument()
{
	if (!COleServerDoc::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc server implementation

COleServerItem* CHDFPluginDoc::OnGetEmbeddedItem()
{
	// OnGetEmbeddedItem is called by the framework to get the COleServerItem
	//  that is associated with the document.  It is only called when necessary.

	CHDFPluginSrvrItem* pItem = new CHDFPluginSrvrItem(this);
	ASSERT_VALID(pItem);
	return pItem;
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc Active Document server implementation

CDocObjectServer *CHDFPluginDoc::GetDocObjectServer(LPOLEDOCUMENTSITE pDocSite)
{
	return new CDocObjectServer(this, pDocSite);
}



/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc serialization

void CHDFPluginDoc::Serialize(CArchive& ar)
{
	/*
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
	*/
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc diagnostics

#ifdef _DEBUG
void CHDFPluginDoc::AssertValid() const
{
	COleServerDoc::AssertValid();
}

void CHDFPluginDoc::Dump(CDumpContext& dc) const
{
	COleServerDoc::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginDoc commands

//Microsoft Knowledge Base Article - 195031 
COleIPFrameWnd* CHDFPluginDoc::CreateInPlaceFrame(CWnd* pParentWnd) 
{
     ASSERT_VALID(this);
     ASSERT_VALID(pParentWnd);
 
     // Get run-time class from the doc template.
     CDocTemplate* pTemplate = GetDocTemplate();
     ASSERT_VALID(pTemplate);
   
     // Use existing view if possible.
     CView* pView = NULL;
     CFrameWnd* pFrame = GetFirstFrame();
     if (pFrame != NULL)
     {
        pView = (CView*)pFrame->GetDescendantWindow(AFX_IDW_PANE_FIRST,TRUE);
        if (pView != NULL)
        {
           ASSERT_KINDOF(CView, pView);
           m_dwOrigStyle = pView->GetStyle();
           m_dwOrigStyleEx = pView->GetExStyle();
        }
     }
   
     // Create the frame from the template, ALWAYS use the CView of the
     // CSplitterWnd.
     COleIPFrameWnd* pFrameWnd = (COleIPFrameWnd*) pTemplate->CreateOleFrame(pParentWnd, this, FALSE);
     if (pFrameWnd == NULL)
        return NULL;
   
     // Connect the splitter window (CSplitterWnd class) to the
     // COleDocIPFrameWnd.
     CWnd* split = pFrame->GetWindow(GW_CHILD);
     VERIFY(pFrame == split->SetParent(pFrameWnd));
   
     // Remember the original parent window for deactivate--uses the
     // CFrameWnd-derived class (that is, CMDIChildWnd for MDI).
     m_pOrigParent = pFrame;
   
     // Set the active view of COleDocIPFrameWnd.
     pFrameWnd->SetActiveView(pView, FALSE);
     pFrameWnd->RecalcLayout();
     pView->ModifyStyleEx(WS_EX_CLIENTEDGE, 0, SWP_DRAWFRAME);
   
     // Verify the type.
     ASSERT_VALID(pFrameWnd);
     ASSERT_KINDOF(COleIPFrameWnd, pFrameWnd);
     return pFrameWnd;
}


//Microsoft Knowledge Base Article - 195031 
void CHDFPluginDoc::DestroyInPlaceFrame(COleIPFrameWnd* pFrameWnd) 
{
     ASSERT_VALID(this);
     ASSERT_VALID(pFrameWnd);
   
     // connect view to original, if existing view was used
     if (m_pOrigParent != NULL)
     {
        CView* pView = (CView*)pFrameWnd->GetDescendantWindow(AFX_IDW_PANE_FIRST, TRUE);
        ASSERT_VALID(pView);
   
        // Leaving the focus on an MDI child or one of its child
        // windows causes Windows to get confused when the child window
        // is destroyed, not to mention the fact that the focus will be
        // out of sync with activation.
        if (::GetFocus() == pView->m_hWnd)
        {
           // Move focus to somewhere safe.
           HWND hWnd = ::GetParent(pFrameWnd->m_hWnd);
           if (hWnd != NULL)
              ::SetFocus(hWnd);
   
           // Check again.
           if (::GetFocus() == pView->m_hWnd)
              SetFocus(NULL); // last ditch effort
        }
   
        // Set parent of the splitter window (CSplitterWnd class) to be
        // the CFrameWnd-derived class (that is, CMDIChildWnd for MDI
        // application).
        ASSERT_KINDOF(CFrameWnd, m_pOrigParent);
        CFrameWnd* frame = (CFrameWnd*) m_pOrigParent;
        CWnd* split = pFrameWnd->GetWindow(GW_CHILD);
        VERIFY(pFrameWnd == split->SetParent(frame));
   
        // Set the active view of CFrameWnd-derived class (that is,
        // CMDIChildWnd for MDI application).
        frame->SetActiveView(pView, FALSE);
        frame->RecalcLayout();
   
        m_pOrigParent = NULL;
   
        // Remove any scrollbars added because of in-place activation.
        if ((m_dwOrigStyle & (WS_HSCROLL|WS_VSCROLL)) == 0 &&
           (pView->GetStyle() & (WS_HSCROLL|WS_VSCROLL)) != 0)
        {
           ::SetScrollRange(pView->m_hWnd, SB_HORZ, 0, 0, TRUE);
           ::SetScrollRange(pView->m_hWnd, SB_VERT, 0, 0, TRUE);
        }
   
        // Restore old 3D style.
        pView->ModifyStyleEx(0, m_dwOrigStyleEx & WS_EX_CLIENTEDGE,
           SWP_DRAWFRAME);
   
        // Force recalc layout on splitter window.
        CSplitterWnd* pSplitter = CView::GetParentSplitter(pView,
           TRUE);
        if (pSplitter != NULL)
           pSplitter->RecalcLayout();
     }
   
     // No active view or document during destroy.
     pFrameWnd->SetActiveView(NULL);
   
     // Destroy in-place frame window.
     pFrameWnd->DestroyWindow();
}


BOOL CHDFPluginDoc::OnOpenDocument(LPCTSTR lpszPathName) 
{
	// bug, failed to open file from URL, always load as standalone.
//	if (!COleServerDoc::OnOpenDocument(lpszPathName))
//		return FALSE;
	
	// TODO: Add your specialized creation code here
	this->SetPathName(lpszPathName);

	return TRUE;
}

void CHDFPluginDoc::OnCloseDocument() 
{
	// TODO: Add your specialized code here and/or call the base class

	COleServerDoc::OnCloseDocument();
}

BOOL CHDFPluginDoc::IsModified()
{
	return FALSE;
}

