// HdfObjH4Group.h: interface for the CH4Group class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH4GROUP_H__95639D4D_CC3C_4047_AB55_09531423D267__INCLUDED_)
#define AFX_HDFOBJH4GROUP_H__95639D4D_CC3C_4047_AB55_09531423D267__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjGroup.h"
#include "hdf.h"

class CH4Group : public CGroup  
{
public:
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void Close(int id);
	int Open();

	CH4Group(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH4Group();
	int GetTag() { return m_tag; }

private:
	int m_tag;
};

#endif // !defined(AFX_HDFOBJH4GROUP_H__95639D4D_CC3C_4047_AB55_09531423D267__INCLUDED_)
