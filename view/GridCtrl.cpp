// GridCtrl.cpp: implementation of the CGridCtrl class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "GridCtrl.h"
#include "TableView.h"
#include "HdfObjCompoundDS.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CGridCtrl::CGridCtrl()
{
	m_pDataset = NULL;
}

CGridCtrl::~CGridCtrl()
{

}

void CGridCtrl::OnSetup()
{
	if (!m_pDataset)
		return;

	Invalidate(); // erase previous display

	unsigned long* count = m_pDataset->GetCount();
	int rows=1, cols=1;

	if (m_pDataset->GetType() == CHObject::HDF_DATASET_COMPOUND)
	{
		rows = ((CCompoundDS*)m_pDataset)->GetRecordSize();
		cols = ((CCompoundDS*)m_pDataset)->GetMemberSize();
	} else
	{
		rows= m_pDataset->GetHeight();
		cols= m_pDataset->GetWidth();
	}

	SetDefColWidth(COLUMN_WIDTH);
	SetNumberRows(rows);
	SetNumberCols(cols);

	CUGCell  cell;

	// set the header cell
	COLORREF oldColor = cell.GetBackColor();
	GetCell(-1,-1,&cell);
	cell.SetBackColor(RGB(250, 230, 10));
	SetCell(-1,-1,&cell);
	cell.SetBackColor(oldColor);
  
	//set row header
	GetCell(0,1,&cell);
	CString strBuf;
	for (int i = 0; i < rows; i++)
	{
		strBuf.Format("%d", i+1);
		cell.SetText(strBuf);
		SetCell(-1,i,&cell);
	}

	//set col header
	if (m_pDataset->GetType() == CHObject::HDF_DATASET_COMPOUND)
	{
		CCompoundDS* pDataset = (CCompoundDS*)m_pDataset;
		for (i = 0; i < cols; i++)
		{
			//strcpy(strBuf, pDataset->GetMemberName(i));
			cell.SetText(pDataset->GetMemberName(i));
			SetCell(i,-1,&cell);
		}
	}
	else
	{
		for (i = 0; i < cols; i++)
		{
			strBuf.Format("%d", i+1);
			cell.SetText(strBuf);
			SetCell(i,-1,&cell);
		}
	}

	// set data values
 	for (i = 0; i < rows; i++)
	{
		for (int j = 0; j < cols; j++)
		{
			cell.SetText(m_pDataset->ValueToStr(i, j, strBuf));
			SetCell(j,i,&cell);
		}
	}
}
