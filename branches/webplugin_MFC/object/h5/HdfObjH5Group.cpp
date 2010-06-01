// HdfObjH5Group.cpp: implementation of the CH5Group class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH5Group.h"
#include "HdfObjH5File.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH5Group::CH5Group(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CGroup(fileformat, name, path)
{

}

CH5Group::~CH5Group()
{

}

CObArray* CH5Group::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	hid_t gid, fid;
	fid = (hid_t)m_pFile->GetFileID();
	if (IsRoot())
		gid = H5Gopen(fid, "/");
	else
		gid = H5Gopen(fid, m_pFullName);

	m_pAttrList = CH5File::GetAttribute(gid);

	H5Gclose(gid);

	return m_pAttrList;
}

void CH5Group::Init()
{
}
