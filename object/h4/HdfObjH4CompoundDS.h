// HdfObjH4CompoundDS.h: interface for the CH4CompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH4COMPOUNDDS_H__31A16A6F_9044_4072_A48B_B2A08A852888__INCLUDED_)
#define AFX_HDFOBJH4COMPOUNDDS_H__31A16A6F_9044_4072_A48B_B2A08A852888__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjCompoundDS.h"
#include "hdf.h"

class CH4CompoundDS : public CCompoundDS  
{
public:
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();
	CString GetDatatypeInfo();
	CH4CompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH4CompoundDS();
	int GetTag() { return m_tag; }
	virtual CString ValueToStr(int row, int col, CString strValue);
	void Close(int id);
	int Open();

private:
	int m_tag;
	int32* m_pFieldSizes;
};

#endif // !defined(AFX_HDFOBJH4COMPOUNDDS_H__31A16A6F_9044_4072_A48B_B2A08A852888__INCLUDED_)
