// HdfObjGroup.h: interface for the CGroup class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJGROUP_H__F764A442_7D9C_4DF5_9C0F_DED178A463E0__INCLUDED_)
#define AFX_HDFOBJGROUP_H__F764A442_7D9C_4DF5_9C0F_DED178A463E0__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <afxcoll.h>
#include "HdfObj.h"

class CGroup : public CHObject 
{
public:
	CString GetObjInfo();
	CHObject* FindDown(oid_t oid);
	CHObject* Get(int nIndex);
	int GetSize() const;
	int Add(CHObject* newMember);
	BOOL IsRoot();
	CObArray* GetMemebers();
	void SetParent(CGroup* parent);
	CGroup* GetParent();
	CGroup(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CGroup();

protected:
	CObArray* m_pMembers;
	CGroup* m_pParent;
};

#endif // !defined(AFX_HDFOBJGROUP_H__F764A442_7D9C_4DF5_9C0F_DED178A463E0__INCLUDED_)
