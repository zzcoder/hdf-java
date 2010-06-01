// HdfObjAtomicDS.h: interface for the CAtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJATOMICDS_H__ABF18E86_6518_4268_8FDE_C7C2602CBB45__INCLUDED_)
#define AFX_HDFOBJATOMICDS_H__ABF18E86_6518_4268_8FDE_C7C2602CBB45__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjDataset.h"

class CAtomicDS : public CDataset  
{
public:
	int GetDatasetType() { return HDF_DATASET_ATOMIC; }
	CString GetObjInfo();
	static char* GetDatatypeInfo(int NT);

	CAtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CAtomicDS();
	virtual CString ValueToStr(int row, int col, CString strValue);

protected:
	double* m_pDataRange;
};

#endif // !defined(AFX_HDFOBJATOMICDS_H__ABF18E86_6518_4268_8FDE_C7C2602CBB45__INCLUDED_)
