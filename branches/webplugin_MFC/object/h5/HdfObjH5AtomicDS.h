// HdfObjH5AtomicDS.h: interface for the CH5AtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH5ATOMICDS_H__9ECDB815_F25E_4103_BC1B_2BC0C63FCE32__INCLUDED_)
#define AFX_HDFOBJH5ATOMICDS_H__9ECDB815_F25E_4103_BC1B_2BC0C63FCE32__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjAtomicDS.h"
#include "hdf5.h"

class CH5AtomicDS : public CAtomicDS  
{
public:
	static int GetNT(int tid);
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();

	CH5AtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH5AtomicDS();

};

#endif // !defined(AFX_HDFOBJH5ATOMICDS_H__9ECDB815_F25E_4103_BC1B_2BC0C63FCE32__INCLUDED_)
