// HDFPluginTreeView.cpp : implementation of the CHDFPluginTreeView class
//

#include "stdafx.h"
#include "afxpriv.h" //WM_INITIALUPDATE
#include "HDFPlugin.h"

#include "HDFPluginDoc.h"
#include "HDFPluginTreeView.h"
#include "HDFPluginMainFrm.h"

#include "HdfObj.h"
#include "HdfObjDataset.h"
#include "HdfObjGroup.h"

#include "TableView.h"
#include "ImageView.h"
#include "TextView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView

IMPLEMENT_DYNCREATE(CHDFPluginTreeView, CTreeView)

BEGIN_MESSAGE_MAP(CHDFPluginTreeView, CTreeView)
	//{{AFX_MSG_MAP(CHDFPluginTreeView)
	ON_WM_LBUTTONDBLCLK()
	ON_WM_RBUTTONDOWN()
	//}}AFX_MSG_MAP
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, CTreeView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, CTreeView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, CTreeView::OnFilePrintPreview)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView construction/destruction

CHDFPluginTreeView::CHDFPluginTreeView()
{
	// TODO: add construction code here
	imgList = 0;
	m_pCurrentFile = NULL;
	m_pTableView = NULL;
	m_pImageView = NULL;
	m_pTextView = NULL;
}

CHDFPluginTreeView::~CHDFPluginTreeView()
{
	if (imgList)
	{
		delete imgList;
		imgList = 0;
	}
}

BOOL CHDFPluginTreeView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs
	return CTreeView::PreCreateWindow(cs);
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView drawing

void CHDFPluginTreeView::OnDraw(CDC* pDC)
{
	CHDFPluginDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);

	// TODO: add draw code for native data here
}


/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView printing

BOOL CHDFPluginTreeView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void CHDFPluginTreeView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void CHDFPluginTreeView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}

/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView diagnostics

#ifdef _DEBUG
void CHDFPluginTreeView::AssertValid() const
{
	CTreeView::AssertValid();
}

void CHDFPluginTreeView::Dump(CDumpContext& dc) const
{
	CTreeView::Dump(dc);
}

CHDFPluginDoc* CHDFPluginTreeView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CHDFPluginDoc)));
	return (CHDFPluginDoc*)m_pDocument;
}
#endif //_DEBUG


/////////////////////////////////////////////////////////////////////////////
// CHDFPluginTreeView message handlers

void CHDFPluginTreeView::LoadBitmaps()
{
	// Create the CImageList. It's destroyed in the destructor.
	imgList = new CImageList();
	imgList->Create(BITMAP_WIDTH, BITMAP_HEIGHT, ILC_COLOR8, BITMAP_COUNT, 0);

	CBitmap bitmap;

	// Load the bitmaps and add them to the image list.
	for (int i = 0; i < BITMAP_COUNT; i++)
	{
		bitmap.LoadBitmap(IDB_BITMAP_HDF+i);
		imgList->Add(&bitmap, (COLORREF)0xFFFFFF);
		bitmap.DeleteObject();
	}
	// Associate the image list with the tree control.
	GetTreeCtrl().SetImageList(imgList, LVSIL_NORMAL);
}

void CHDFPluginTreeView::OnInitialUpdate() 
{
	CTreeView::OnInitialUpdate();
	
	// TODO: Add your specialized code here and/or call the base class
	GetTreeCtrl().ModifyStyle(NULL, TVS_HASBUTTONS | TVS_HASLINES | TVS_LINESATROOT);
	
	LoadBitmaps();
	

	CDocument* pDoc = GetDocument();
	if (pDoc != NULL) 
	{
		CString strFile = pDoc->GetPathName();
		if (strFile.GetLength()>0)
		{
			// close the old file
			if (m_pCurrentFile)
				m_pCurrentFile->Close();

			CFileFormat* currentFile = CFileFormat::CreateInstance(strFile);
		    ShowFileTree(currentFile);
			m_pCurrentFile = currentFile;
		}
	}
}

void CHDFPluginTreeView::TraverseInsertNodes(HTREEITEM parentNode, CGroup* pParentGroup)
{
	CTreeCtrl & ctc = GetTreeCtrl();
	HTREEITEM node;
	CHObject* pObj;
	oid_t* pOID = NULL;

	if (!pParentGroup)
		return;

	int n = pParentGroup->GetSize();
	for (int i=0; i<n; i++)
	{
		pOID = (oid_t*)malloc(sizeof(oid_t));
		pObj =  pParentGroup->Get(i);
		*pOID = pObj->GetObjectID();

		switch(pObj->GetType())
		{
			case CHObject::HDF_GROUP:
				node = ctc.InsertItem(pObj->GetName(), BITMAP_INDEX_HDF_NODE_OPEN, BITMAP_INDEX_HDF_NODE_OPEN, parentNode);
				TraverseInsertNodes(node, (CGroup *)pObj);
				ctc.SetItemData(node, (DWORD)pOID);
				break;
			case CHObject::HDF_DATASET_ATOMIC:
				node = ctc.InsertItem(pObj->GetName(), BITMAP_INDEX_HDF_DATASET, BITMAP_INDEX_HDF_DATASET, parentNode);
				ctc.SetItemData(node, (DWORD)pOID);
				break;
			case CHObject::HDF_DATASET_COMPOUND:
				node = ctc.InsertItem(pObj->GetName(), BITMAP_INDEX_HDF_TABLE, BITMAP_INDEX_HDF_TABLE, parentNode);
				ctc.SetItemData(node, (DWORD)pOID);
				break;
			case CHObject::HDF_DATASET_IMAGE:
				node = ctc.InsertItem(pObj->GetName(), BITMAP_INDEX_HDF_IMAGE, BITMAP_INDEX_HDF_IMAGE, parentNode);
				ctc.SetItemData(node, (DWORD)pOID);
				break;
		}
	}
	ctc.Expand(parentNode, TVE_EXPAND); // expand all nodes
}

void CHDFPluginTreeView::ShowFileTree(CFileFormat* pFile)
{
	if (!pFile)
		return;

	CTreeCtrl & ctc = GetTreeCtrl();

	ctc.DeleteAllItems(); // delete all the old items. prepare for the new tree

	HTREEITEM hSubTree = NULL;
	CGroup* pRootGroup = pFile->GetRootGroup();
	oid_t* pOID = (oid_t*)malloc(sizeof(oid_t));

	if (pFile->GetFileType() == CFileFormat::SUPPORTED_FILE_TYPE_NCSA_HDF5)
		hSubTree = ctc.InsertItem(pRootGroup->GetName(), BITMAP_INDEX_HDF_5, BITMAP_INDEX_HDF_5);
	else
		hSubTree = ctc.InsertItem(pRootGroup->GetName(), BITMAP_INDEX_HDF_4, BITMAP_INDEX_HDF_4);

	*pOID = pRootGroup->GetObjectID();
	ctc.SetItemData(hSubTree, (DWORD)pOID);
	TraverseInsertNodes(hSubTree, pFile->GetRootGroup());

	ctc.UpdateWindow();
}

void CHDFPluginTreeView::OnLButtonDblClk(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	if (!m_pCurrentFile)
		return;

	CGroup* pRootGroup = m_pCurrentFile->GetRootGroup();
	if (!pRootGroup)
		return;

 	CTreeCtrl &ctc = GetTreeCtrl();
	HTREEITEM hitem = ctc.HitTest( point, &nFlags );;
	if (hitem == NULL)
		return;

	oid_t* pOID = (oid_t*) ctc.GetItemData(hitem);
	CHObject* pObj =  pRootGroup->FindDown(*pOID);
	if (pObj)
	{
		BeginWaitCursor();  
		switch (pObj->GetType())
		{
			case CHObject::HDF_DATASET_ATOMIC:
			case CHObject::HDF_DATASET_COMPOUND:
				ShowTableView((CDataset*)pObj);
				break;
			case CHObject::HDF_DATASET_IMAGE:
				ShowImageView((CImageDS*)pObj);
				break;
		}
		EndWaitCursor(); 
	}
	
	CTreeView::OnLButtonDblClk(nFlags, point);
}

void CHDFPluginTreeView::OnRButtonDown(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	// TODO: Add your message handler code here and/or call default
	if (!m_pCurrentFile)
		return;

	CGroup* pRootGroup = m_pCurrentFile->GetRootGroup();
	if (!pRootGroup)
		return;

	UINT hitflags;

 	CTreeCtrl &ctc = GetTreeCtrl();


	HTREEITEM hitem = ctc.HitTest( point, &hitflags ) ;
	if (hitem == NULL)
		return;

	if ( hitflags & (TVHT_ONITEM | TVHT_ONITEMBUTTON  )) 
		ctc.Select( hitem, TVGN_CARET );

	oid_t* pOID = (oid_t*) ctc.GetItemData(hitem);
	CHObject* pObj = pRootGroup->FindDown(*pOID);
	if (pObj)
	{
		BeginWaitCursor(); 
		ShowMetaData(pObj);
		EndWaitCursor(); 
	}
	
	CTreeView::OnRButtonDown(nFlags, point);
}

void CHDFPluginTreeView::ShowTableView(CDataset* pDataset)
{
	CSplitterWnd* pSplitter = (CSplitterWnd *)GetParent();
	CHDFPluginDoc* pDoc = GetDocument();

    if (!m_pTableView)
    {
		// create the new view
		m_pTableView = new CTableView;
		m_pTableView->Create(NULL, "Table View", AFX_WS_DEFAULT_VIEW, 
               CRect(0, 0, 0, 0),  pSplitter, 0, NULL);
	}

	CHDFPluginMainFrame* pFrame = (CHDFPluginMainFrame *)((CHDFPluginApp*)AfxGetApp())->m_pMainWnd;
	CView* pActiveView = (CView *)pFrame->GetRightPane();
	if (pActiveView != m_pTableView)
	{
		SwitchRightPane(m_pTableView);
	}

	CDataset* pOldDataset = m_pTableView->GetDataset();
	if (pOldDataset != pDataset)
	{
		m_pTableView->SetDataset(pDataset);
	}

}

void CHDFPluginTreeView::ShowImageView(CImageDS* pDataset)
{
	CSplitterWnd* pSplitter = (CSplitterWnd *)GetParent();
	CHDFPluginDoc* pDoc = GetDocument();

    if (!m_pImageView)
    {
		// create the new view
		m_pImageView = new CImageView;
		m_pImageView->Create(NULL, "Image View", AFX_WS_DEFAULT_VIEW, 
               CRect(0, 0, 0, 0),  pSplitter, 0, NULL);
	}

	CHDFPluginMainFrame* pFrame = (CHDFPluginMainFrame *)((CHDFPluginApp*)AfxGetApp())->m_pMainWnd;
	CView* pActiveView = (CView *)pFrame->GetRightPane();
	if (pActiveView != m_pImageView)
	{
		SwitchRightPane(m_pImageView);
	}

	CImageDS* pOldDataset = m_pImageView->GetImageDS();
	if (pOldDataset != pDataset)
	{
		m_pImageView->SetImageDS(pDataset);
	}

}

void CHDFPluginTreeView::ShowMetaData(CHObject* pObj)
{
	if (!pObj)
		return;

	CSplitterWnd* pSplitter = (CSplitterWnd *)GetParent();
	CHDFPluginDoc* pDoc = GetDocument();

    if (!m_pTextView)
    {
		// create the new view
		m_pTextView = new CTextView;
		m_pTextView->Create(NULL, "Text View", AFX_WS_DEFAULT_VIEW, 
               CRect(0, 0, 0, 0),  pSplitter, 0, NULL);
	}

	CHDFPluginMainFrame* pFrame = (CHDFPluginMainFrame *)((CHDFPluginApp*)AfxGetApp())->m_pMainWnd;
	CView* pActiveView = (CView *)pFrame->GetRightPane();

	if (pActiveView != m_pTextView)
	{
		SwitchRightPane(m_pTextView);
	}

	CHObject* pOldObj = m_pTextView->GetObj();
	if (pOldObj != pObj)
	{
		m_pTextView->SetObj(pObj);
	}
}

CView* CHDFPluginTreeView::SwitchRightPane(CView *pNewView)
{
    // modified from http://www.codeproject.com/docview/switchingviews.asp
	CHDFPluginMainFrame* pFrame = (CHDFPluginMainFrame *)((CHDFPluginApp*)AfxGetApp())->m_pMainWnd;
	CView* pOldActiveView = (CView *)pFrame->GetRightPane();

	if (pOldActiveView == pNewView)
		return  pOldActiveView;

    CSplitterWnd* pSplitter = (CSplitterWnd *)pOldActiveView->GetParent();

	CDocument* pDoc = GetDocument();

	// set flag so that document will not be deleted when view is destroyed
	pDoc->m_bAutoDelete = FALSE;    

	// Dettach existing view
	pDoc->RemoveView(pOldActiveView);

	// set flag back to default 
	pDoc->m_bAutoDelete = TRUE;
 
	/* Set the child window ID of the active view to the ID of the corresponding
		pane. Set the child ID of the previously active view to some other ID.
	*/
	::SetWindowLong(pOldActiveView->m_hWnd, GWL_ID, 0);
	::SetWindowLong(pNewView->m_hWnd, GWL_ID, pSplitter->IdFromRowCol(0, 1));

	// Show the newly active view and hide the inactive view.
	pNewView->ShowWindow(SW_SHOW);
	pOldActiveView->ShowWindow(SW_HIDE);

	// Attach new view
	pDoc->AddView(pNewView);

	// Set active 
	pSplitter->GetParentFrame()->SetActiveView(pNewView);

    pSplitter->RecalcLayout();
	pNewView->SendMessage(WM_INITIALUPDATE);	 
	pNewView->SendMessage(WM_PAINT); 

	return pOldActiveView;

}


