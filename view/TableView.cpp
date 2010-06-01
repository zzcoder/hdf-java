// TableView.cpp : implementation file
//

#include "stdafx.h"
#include "..\HDFPlugin.h"
#include "TableView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CTableView

IMPLEMENT_DYNCREATE(CTableView, CBaseView)

CTableView::CTableView()
{
	m_pGridCtrl = NULL;
	m_pDataset = NULL;
}

CTableView::~CTableView()
{
	if (m_pGridCtrl)
	{
		delete m_pGridCtrl;
		m_pGridCtrl = NULL;
	}
}


BEGIN_MESSAGE_MAP(CTableView, CBaseView)
	//{{AFX_MSG_MAP(CTableView)
	ON_WM_SIZE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CTableView drawing

void CTableView::OnInitialUpdate()
{
	CBaseView::OnInitialUpdate();

	// TODO: calculate the total size of this view

	if (!m_pGridCtrl)
	{
		RECT	rect;
		GetClientRect(&rect);
		m_pGridCtrl = new CGridCtrl;
		m_pGridCtrl->CreateGrid(WS_CHILD|WS_VISIBLE|WS_CLIPCHILDREN|WS_CLIPSIBLINGS, rect,this,1024);
	}

	CSize sizeTotal;
	sizeTotal.cx = sizeTotal.cy = 100;
	SetScrollSizes(MM_TEXT, sizeTotal);
}

void CTableView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	// TODO: add draw code here
}

/////////////////////////////////////////////////////////////////////////////
// CTableView diagnostics

#ifdef _DEBUG
void CTableView::AssertValid() const
{
	CBaseView::AssertValid();
}

void CTableView::Dump(CDumpContext& dc) const
{
	CBaseView::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CTableView message handlers

CDataset* CTableView::GetDataset()
{
	return m_pDataset;
}

void CTableView::SetDataset(CDataset* pDataset)
{
	if (pDataset==NULL || 
		pDataset == m_pDataset)
		return;

	if(!pDataset->ReadData())
	{
		char msg[80];
		strcpy(msg, "Failed to read data from \n");
		strcat(msg, pDataset->GetFullName());
		AfxMessageBox(msg, MB_ICONEXCLAMATION);
		return; 
	}

	m_pDataset = pDataset;
	m_pGridCtrl->m_pDataset = pDataset;
	m_pGridCtrl->OnSetup();
}


void CTableView::OnSize(UINT nType, int cx, int cy) 
{
	CBaseView::OnSize(nType, cx, cy);
	
	// TODO: Add your message handler code here
	if (m_pGridCtrl)
	{
		RECT rect;
		GetClientRect(&rect);
		m_pGridCtrl->MoveWindow(&rect);
	}
}

