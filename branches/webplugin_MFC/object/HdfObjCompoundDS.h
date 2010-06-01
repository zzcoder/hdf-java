// HdfObjCompoundDS.h: interface for the CCompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJCOMPOUNDDS_H__75C2E3BA_BD6F_4553_9BB7_8DD465A85E96__INCLUDED_)
#define AFX_HDFOBJCOMPOUNDDS_H__75C2E3BA_BD6F_4553_9BB7_8DD465A85E96__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjDataset.h"

class CCompoundDS : public CDataset  
{
public:
	int GetDatasetType() { return HDF_DATASET_COMPOUND; }
	CString GetObjInfo();
	virtual CString GetDatatypeInfo()=0;
	LPCTSTR GetMemberName( int index);
	int GetMemberSize() { return m_nMembers; }
	int GetRecordSize() { return m_nRecords; }
	int* GetMemberTypes() { return m_pMemberTypes; }
	int* GetMemberOrders() { return m_pMemberOrders; }

	CCompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CCompoundDS();
	virtual CString ValueToStr(int row, int col, CString strValue);

protected:
	int m_nMembers;
	int m_nRecords;
	char** m_pMemberNames;
	int* m_pMemberTypes;
	int* m_pMemberOrders;
};

#endif // !defined(AFX_HDFOBJCOMPOUNDDS_H__75C2E3BA_BD6F_4553_9BB7_8DD465A85E96__INCLUDED_)
