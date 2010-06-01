// HdfObjH4AtomicDS.h: interface for the CH4AtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH4ATOMICDS_H__5303DAEA_A61A_4AC0_9AD2_4EF184CBD554__INCLUDED_)
#define AFX_HDFOBJH4ATOMICDS_H__5303DAEA_A61A_4AC0_9AD2_4EF184CBD554__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjAtomicDS.h"
#include "hdf.h"

class CH4AtomicDS : public CAtomicDS  
{
public:
	static int GetNT(int tid);
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();
	void Close(int id);
	int Open();

	CH4AtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH4AtomicDS();
	int GetTag() { return m_tag; }

private:
	int m_tag;
    int m_sdid;
	int m_tid;

};

#endif // !defined(AFX_HDFOBJH4ATOMICDS_H__5303DAEA_A61A_4AC0_9AD2_4EF184CBD554__INCLUDED_)
