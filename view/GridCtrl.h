// GridCtrl.h: interface for the CGridCtrl class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_GRIDCTRL_H__78F4421A_A3E8_4455_BDD5_5118EFE32635__INCLUDED_)
#define AFX_GRIDCTRL_H__78F4421A_A3E8_4455_BDD5_5118EFE32635__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "UGRID\UGCtrl.h"
#include "HdfObjDataset.h"

#define COLUMN_WIDTH 80

class CGridCtrl : public CUGCtrl  
{
public:
	CDataset* m_pDataset;
	CGridCtrl();
	virtual ~CGridCtrl();
	virtual void OnSetup();
};

#endif // !defined(AFX_GRIDCTRL_H__78F4421A_A3E8_4455_BDD5_5118EFE32635__INCLUDED_)
