// HdfObjAttribute.cpp: implementation of the CAttribute class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjAttribute.h"
#include "HdfObjDataset.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CAttribute::CAttribute(LPCTSTR name)
{
	m_pName = name;
	m_pValue = NULL;
	m_pDims = NULL;
	m_nRank = 1;
}

CAttribute::~CAttribute()
{
}

void CAttribute::SetName(LPCTSTR name)
{
	m_pName = name;
}

LPCTSTR CAttribute::GetName()
{
	return m_pName;
}

void CAttribute::SetValue(void* value)
{
	m_pValue = value;
}

void* CAttribute::GetValue()
{
	return m_pValue;
}

CString CAttribute::ValueToStr()
{
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
	int i=0;

	unsigned char *vp = (unsigned char *)m_pValue;
	int n = 1;

	if (m_pDims)
	{
		for (int i=0; i<m_nRank; i++)
		{
			n *= m_pDims[i];
		}
	}

	CString allValue;
	CString oneValue;
	switch (m_nNT)
	{
		case CDataset::HDF_NT_CHAR:
 			memcpy(&tmp_char, vp, 1);
			oneValue.Format("%c",tmp_char);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_char, vp+i, 1);
				oneValue.Format("%c",tmp_char); 
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_UCHAR:
 			memcpy(&tmp_uchar, vp, 1);
			oneValue.Format("%c",tmp_uchar);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uchar, vp+i, 1);
				oneValue.Format("%c",tmp_uchar); 
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_INT8:
			memcpy(&tmp_int8, vp, 1);
			oneValue.Format("%d",tmp_int8);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int8, vp+i, 1);
				oneValue.Format(", %d",tmp_int8); 
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_INT16:
			memcpy(&tmp_int16, vp, 2);
			oneValue.Format("%d",tmp_int16);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int16, vp+i*2, 2);
				oneValue.Format(", %d",tmp_int16);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_INT32:
			memcpy(&tmp_int32, vp, 4);
			oneValue.Format("%d",tmp_int32);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int32, vp+i*4, 4);
				oneValue.Format(", %d",tmp_int32);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_INT64:
			memcpy(&tmp_int64, vp, 8);
			oneValue.Format("%d",tmp_int64);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int64, vp+i*8, 8);
				oneValue.Format(", %d",tmp_int64);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_UINT8:
			memcpy(&tmp_uint8, vp, 1);
			oneValue.Format("%u", tmp_uint8);

			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint8, vp+i, 1);
				oneValue.Format(", %u", tmp_uint8); 
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_UINT16:
			memcpy(&tmp_uint16, vp, 2);
			oneValue.Format("%u", tmp_uint16);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint16, vp+i*2, 2);
				oneValue.Format(", %u", tmp_uint16);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_UINT32:
			memcpy(&tmp_uint32, vp, 4);
			oneValue.Format("%u", tmp_uint32);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint32, vp+i*4, 4);
				oneValue.Format(", %u", tmp_uint32);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_UINT64:
			memcpy(&tmp_uint64, vp, 8);
			oneValue.Format("%u", tmp_uint64);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint64, vp+i*8, 8);
				oneValue.Format(", %u", tmp_uint64);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_FLOAT:
			memcpy(&tmp_float, vp, 4);
			oneValue.Format("%g",tmp_float);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_float, vp+i*4, 4);
				oneValue.Format(", %g",tmp_float);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_DOUBLE:
			memcpy(&tmp_double, vp, 8);
			oneValue.Format("%g",tmp_double);
			allValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_double, vp+i*8, 8);
				oneValue.Format(", %g",tmp_double);
				allValue += oneValue;
			}
			break;
		case CDataset::HDF_NT_STRING:
			allValue += (char *)vp;
			break;		
		default:
			break;
	}

	return allValue;
}

void CAttribute::SetDims(int* dims) 
{ 
	m_pDims = dims;
}