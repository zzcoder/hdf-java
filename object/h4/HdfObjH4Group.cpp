// HdfObjH4Group.cpp: implementation of the CH4Group class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH4Group.h"
#include "HdfObjAttribute.h"
#include "HdfObjH4AtomicDS.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH4Group::CH4Group(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CGroup(fileformat, name, path)
{
	m_tag = DFTAG_VG;
}

CH4Group::~CH4Group()
{

}

int CH4Group::Open()
{
    int vgroup_id = Vattach( (int32)m_pFile->GetFileID(), (int32)m_nObjectID, "r" );
    
	if ( vgroup_id == FAIL )
	{
		Vdetach(vgroup_id);
		return -1;
	}

	return vgroup_id;
}

void CH4Group::Close(int vgroup_id)
{
	Vdetach(vgroup_id);
}

CObArray* CH4Group::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	int vgid = Open();
	if (vgid < 0) return NULL;

	int32 attrType, attrCount, attrSize;
	int n = Vnattrs(vgid);
	m_pAttrList = new CObArray;
	LPTSTR attrName = (LPTSTR)malloc(MAX_NC_NAME);
	int* idims = NULL;

	for (int i=0; i<n; i++)
	{
		attrName = (LPTSTR)malloc(MAX_NC_NAME);

		if (Vattrinfo( vgid, i, attrName, &attrType, &attrCount, &attrSize) < 0)
			continue;

		CAttribute* attr = new CAttribute(attrName);
		attr->SetValueType(CH4AtomicDS::GetNT(attrType));
        m_pAttrList->Add(attr);
		idims = (int*) malloc(sizeof(int));
		idims[0] = attrCount;
		attr->SetDims(idims);

		void* attrValue = malloc(attrCount*attrSize);
		if (Vgetattr( vgid, i, attrValue) == FAIL)
		{
			free(attrValue);
			continue;
		} else
			attr->SetValue(attrValue);
		attr->SetRank(1);
		attr->SetTypeSize(attrSize);

	}
	Close(vgid);

	return m_pAttrList;
}

void CH4Group::Init()
{

	// TODO: Add your own code here.
}
