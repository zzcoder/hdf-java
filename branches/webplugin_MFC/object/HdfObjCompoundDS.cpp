// HdfObjCompoundDS.cpp: implementation of the CCompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjCompoundDS.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CCompoundDS::CCompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CDataset(fileformat, name, path)
{
	m_nType = HDF_DATASET_COMPOUND;
	m_pMemberNames = NULL;
	m_pMemberTypes = NULL;
	m_pMemberOrders = NULL;

}

CCompoundDS::~CCompoundDS()
{
	if (m_pMemberNames) free(m_pMemberNames);
	if (m_pMemberTypes) free(m_pMemberTypes);
	if (m_pMemberOrders) free(m_pMemberOrders);
}

LPCTSTR CCompoundDS::GetMemberName(int index)
{
	if (m_pMemberNames && index < m_nMembers)
		return (LPCSTR)m_pMemberNames[index];

	return NULL;
}

CString CCompoundDS::ValueToStr(int row, int col, CString strValue)
{
	return strValue;
}

CString CCompoundDS::GetObjInfo()
{
	if (!m_ObjInfo.IsEmpty())
		return m_ObjInfo;

	if (m_nRank < 0) Init();

	char str[10];


	m_ObjInfo += "\"";
	m_ObjInfo += m_pName;
	m_ObjInfo += "\"";

	// data space information
	if (m_nRank <=0)
		m_ObjInfo += "\nDataspace:\t0";
	else
	{
		m_ObjInfo += "\n";
		sprintf(str, "%d", m_pDims[0]);
		m_ObjInfo += str;

		for (int i=1; i<m_nRank; i++)
		{
			m_ObjInfo += "x";
			sprintf(str, "%d", m_pDims[i]);
			m_ObjInfo += str;
		}
	}

	// data type information
	m_ObjInfo += ",\tCompound\n";

	m_ObjInfo += GetDatatypeInfo();


	return m_ObjInfo;
}
