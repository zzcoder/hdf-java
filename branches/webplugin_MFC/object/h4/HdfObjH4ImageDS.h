// HdfObjH4ImageDS.h: interface for the CH4ImageDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH4IMAGEDS_H__40D7B846_1998_483D_AD98_A0B4BB164386__INCLUDED_)
#define AFX_HDFOBJH4IMAGEDS_H__40D7B846_1998_483D_AD98_A0B4BB164386__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjImageDS.h"
#include "hdf.h"

class CH4ImageDS : public CImageDS  
{
public:
	static int GetNT(int tid);
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();
	unsigned char* ReadPalette();
	void Close(int id);
	int Open();

	CH4ImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH4ImageDS();
	int GetTag() { return m_tag; }

private:
	int m_tag;
    int m_grid;
	int m_tid;
	int m_nComponents;
};

#endif // !defined(AFX_HDFOBJH4IMAGEDS_H__40D7B846_1998_483D_AD98_A0B4BB164386__INCLUDED_)
