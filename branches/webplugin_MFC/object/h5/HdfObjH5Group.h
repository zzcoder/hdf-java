// HdfObjH5Group.h: interface for the CH5Group class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH5GROUP_H__F4FE6438_3FD6_4E89_8E4F_FE7C1AF5443E__INCLUDED_)
#define AFX_HDFOBJH5GROUP_H__F4FE6438_3FD6_4E89_8E4F_FE7C1AF5443E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjGroup.h"

class CH5Group : public CGroup  
{
public:
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()

	CH5Group(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH5Group();

};

#endif // !defined(AFX_HDFOBJH5GROUP_H__F4FE6438_3FD6_4E89_8E4F_FE7C1AF5443E__INCLUDED_)
