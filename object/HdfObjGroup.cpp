// HdfObjGroup.cpp: implementation of the CGroup class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjGroup.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CGroup::CGroup(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
	:CHObject(fileformat, name, path)
{
	m_pParent = NULL;
	m_nType = HDF_GROUP;
	m_pMembers = new CObArray();
}

CGroup::~CGroup()
{
	if (m_pMembers)
	{
		delete m_pMembers;
		m_pMembers = 0;
	}
}

CGroup* CGroup::GetParent()
{
	return m_pParent;
}

void CGroup::SetParent(CGroup *parent)
{
	m_pParent = parent;
}

CObArray* CGroup::GetMemebers()
{
	return m_pMembers;
}

BOOL CGroup::IsRoot()
{
	return (m_pParent == NULL);
}

int CGroup::Add(CHObject *newMember)
{
	if (!m_pMembers)
		m_pMembers = new CObArray();

	return m_pMembers->Add(newMember);
}

int CGroup::GetSize() const
{
	if (!m_pMembers)
		return -1;

	return m_pMembers->GetSize();
}

CHObject* CGroup::Get(int nIndex)
{
	if (!m_pMembers)
		return NULL;

	return (CHObject*)m_pMembers->ElementAt(nIndex);

}

/**
 *  Find object blow this group by object id
 */
CHObject* CGroup::FindDown(oid_t oid)
{
	CHObject* pObjFound = NULL;

	if (m_nObjectID == oid)
		pObjFound = this;
	else if (m_pMembers)
	{
		int n = m_pMembers->GetSize();
		for (int i=0; i<n; i++)
		{
			CHObject* pObj = (CHObject*)m_pMembers->ElementAt(i);

			if (oid == pObj->GetObjectID())
			{
				pObjFound = pObj;
				break;
			}
			else if (pObj->GetType() == CHObject::HDF_GROUP)
			{
				pObjFound = ((CGroup *)pObj)->FindDown(oid);
				if (pObjFound)
					break;
			}
		}
	}
	return pObjFound;
}

CString CGroup::GetObjInfo()
{
	if (!m_ObjInfo.IsEmpty())
		return m_ObjInfo;

	char str[80];
	int n = 0;
	if (m_pMembers) n = m_pMembers->GetSize();

	m_ObjInfo += "\"";
	m_ObjInfo += m_pName;
	m_ObjInfo += "\"";

	m_ObjInfo += "\nNumber of members = ";
	sprintf(str, "%d", n); 
	m_ObjInfo += str;
	m_ObjInfo += "\n\tMember Type\t\t\tMember Name\n";
	m_ObjInfo += TABLE_LINE;
	CHObject* obj = NULL;

	for (int i=0; i<n; i++)
	{
		obj = (CHObject*)m_pMembers->GetAt(i);
		if (!obj) continue;

		if (obj->GetType() == CHObject::HDF_GROUP)
			m_ObjInfo += "\n\tGroup\t\t\t\t";
		else if (obj->GetType() == CHObject::HDF_DATASET_ATOMIC)
			m_ObjInfo += "\n\tScalar dataset\t\t\t";
		else if (obj->GetType() == CHObject::HDF_DATASET_COMPOUND)
			m_ObjInfo += "\n\tCompound dataset\t\t";
		else if (obj->GetType() == CHObject::HDF_DATASET_IMAGE)
			m_ObjInfo += "\n\tImage\t\t\t\t";
		m_ObjInfo += obj->GetName();
	}

	return m_ObjInfo;
}