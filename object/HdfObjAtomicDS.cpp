// HdfObjAtomicDS.cpp: implementation of the CAtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjAtomicDS.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CAtomicDS::CAtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CDataset(fileformat, name, path)
{
	m_nType = HDF_DATASET_ATOMIC;
	m_pDataRange = NULL;
}

CAtomicDS::~CAtomicDS()
{
	if (m_pDataRange) free(m_pDataRange);
}

CString CAtomicDS::ValueToStr(int row, int col, CString strValue)
{
	strValue.Empty();

	int idx = row*m_nWidth+col;
	unsigned long offset;

	char            tmp_char;
	unsigned char   tmp_uchar;
	_int8			tmp_int8;
	_int16			tmp_int16;
	_int32			tmp_int32;
	_int64			tmp_int64;
	unsigned _int8	tmp_uint8;
	unsigned _int16 tmp_uint16;
	unsigned _int32 tmp_uint32;
	unsigned _int64 tmp_uint64;
	float			tmp_float;
	double			tmp_double;
	char*			tmp_str;

	unsigned char *vp = (unsigned char *)m_pData;
	switch (m_nNT)
	{
	case HDF_NT_CHAR:
		offset = idx;
		memcpy(&tmp_char, vp+offset, 1);
		strValue.Format("%c",tmp_char); 
		break;
	case HDF_NT_UCHAR:
		offset = idx;
		memcpy(&tmp_uchar, vp+offset, 1);
		strValue.Format("%c",tmp_uchar); 
		break;
	case HDF_NT_INT8:
		offset = idx;
		memcpy(&tmp_int8, vp+offset, 1);
		strValue.Format("%d",tmp_int8); 
		break;
	case HDF_NT_INT16:
		offset = idx*2;
		memcpy(&tmp_int16, vp+offset, 2);
		strValue.Format("%d",tmp_int16);
		break;
	case HDF_NT_INT32:
		offset = idx*4;
		memcpy(&tmp_int32, vp+offset, 4);
		strValue.Format("%d",tmp_int32);
		break;
	case HDF_NT_INT64:
		offset = idx*8;
		memcpy(&tmp_int64, vp+offset, 8);
		strValue.Format("%d",tmp_int64);
		break;
	case HDF_NT_UINT8:
		offset = idx;
		memcpy(&tmp_uint8, vp+offset, 1);
		strValue.Format("%u",tmp_uint8); 
		break;
	case HDF_NT_UINT16:
		offset = idx*2;
		memcpy(&tmp_uint16, vp+offset, 2);
		strValue.Format("%u",tmp_uint16);
		break;
	case HDF_NT_UINT32:
		offset = idx*4;
		memcpy(&tmp_uint32, vp+offset, 4);
		strValue.Format("%u",tmp_uint32);
		break;
	case HDF_NT_UINT64:
		offset = idx*8;
		memcpy(&tmp_uint64, vp+offset, 8);
		strValue.Format("%u",tmp_uint64);
		break;
	case HDF_NT_FLOAT:
		offset = idx*4;
		memcpy(&tmp_float, vp+offset, 4);
		strValue.Format("%g",tmp_float);
		break;
	case HDF_NT_DOUBLE:
		offset = idx*8;
		memcpy(&tmp_double, vp+offset, 8);
		strValue.Format("%g",tmp_double);
		break;
	case HDF_NT_STRING:
		offset = idx*m_nTypeSize;
		tmp_str = (char *)malloc(m_nTypeSize);
		memcpy(tmp_str, vp+offset, m_nTypeSize);
		strValue += tmp_str;
		free(tmp_str);
		break;
	}

	return strValue;
}

char* CAtomicDS::GetDatatypeInfo(int NT)
{
	char* desc = NULL;

	switch (NT)
	{
	case HDF_NT_CHAR:
		desc = "8-bit char"; 
		break;
	case HDF_NT_UCHAR:
		desc = "8-bit unsigned char"; 
		break;
	case HDF_NT_INT8:
		desc = "8-bit int"; 
		break;
	case HDF_NT_INT16:
		desc = "16-bit int"; 
		break;
	case HDF_NT_INT32:
		desc = "32-bit int"; 
		break;
	case HDF_NT_INT64:
		desc = "64-bit int"; 
		break;
	case HDF_NT_UINT8:
		desc = "8-bit unsigned int"; 
		break;
	case HDF_NT_UINT16:
		desc = "16-bit unsigned int"; 
		break;
	case HDF_NT_UINT32:
		desc = "32-bit unsigned int"; 
		break;
	case HDF_NT_UINT64:
		desc = "64-bit unsigned int"; 
		break;
	case HDF_NT_FLOAT:
		desc = "32-bit float"; 
		break;
	case HDF_NT_DOUBLE:
		desc = "64-bit float"; 
		break;
	case HDF_NT_STRING:
		desc = "String"; 
		break;
	}

	return desc;
}

CString CAtomicDS::GetObjInfo()
{
	if (!m_ObjInfo.IsEmpty())
		return m_ObjInfo;

	if (m_nRank < 0) Init();

	char str[10];

	m_ObjInfo += "\"";
	m_ObjInfo += m_pName;
	m_ObjInfo += "\"";

	if (m_nRank <=0)
		m_ObjInfo += "\n0,\t";
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
		m_ObjInfo += ",\t";
	}
	m_ObjInfo += GetDatatypeInfo(m_nNT);

	return m_ObjInfo;
}
