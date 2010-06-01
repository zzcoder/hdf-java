// HdfObjH5ImageDS.h: interface for the CH5ImageDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH5IMAGEDS_H__6D71492A_5C1A_4856_8ECB_A25ADEC0B5F0__INCLUDED_)
#define AFX_HDFOBJH5IMAGEDS_H__6D71492A_5C1A_4856_8ECB_A25ADEC0B5F0__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjImageDS.h"
#include "hdf5.h"

class CH5ImageDS : public CImageDS  
{
public:
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();
	unsigned char* ReadPalette();

	CH5ImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH5ImageDS();

private:
	hobj_ref_t* GetPaletteRef(hid_t did);
	hobj_ref_t* m_pPaletteRefs;
};

#endif // !defined(AFX_HDFOBJH5IMAGEDS_H__6D71492A_5C1A_4856_8ECB_A25ADEC0B5F0__INCLUDED_)
