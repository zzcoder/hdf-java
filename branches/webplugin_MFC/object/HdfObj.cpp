// HdfObj.cpp: implementation of the CHObject class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObj.h"
#include "HdfObjFileFormat.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CHObject::CHObject(CFileFormat* file, LPCTSTR name, LPCTSTR path)
{
	m_pFile = file;
	strcpy(m_pName, name);
	strcpy(m_pPath, path);
	m_nObjectID = 0;
	m_pAttrList = NULL;

	if (m_pPath)
	{
		strcpy(m_pFullName, m_pPath);
		strcat(m_pFullName, name);
	}
}

CHObject::~CHObject()
{

	if (m_pFile)
	{
		delete m_pFile;
		m_pFile = 0;
	}

}

CFileFormat* CHObject::GetFile()
{
	return m_pFile;
}

LPCTSTR CHObject::GetName()
{
	return m_pName;
}

LPCTSTR CHObject::GetFullName()
{
	return m_pFullName;
}

LPCTSTR CHObject::GetPath()
{
	return m_pPath;
}

void CHObject::SetName(LPCTSTR name)
{
	strcpy(m_pName, name);
}

void CHObject::SetPath(LPCTSTR path)
{
	strcpy(m_pPath, path);
}

int CHObject::GetType()
{
	return m_nType;
}
