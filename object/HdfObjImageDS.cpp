// HdfObjImageDS.cpp: implementation of the CImageDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjImageDS.h"
#include <float.h>

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CImageDS::CImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CAtomicDS(fileformat, name, path)
{
	m_nType = HDF_DATASET_IMAGE;
	m_pPalette = NULL;
	m_nComponents = 1;
	m_pByteData = NULL;
}

CImageDS::~CImageDS()
{
	if (m_pByteData)
	{
		free(m_pByteData);
		m_pByteData = NULL;
	}

	if (m_pPalette) free(m_pPalette);
}

CString CImageDS::ValueToStr(int row, int col, CString strValue)
{
	return strValue;
}

unsigned char* CImageDS::GetPalette() 
{ 
	if (m_pPalette)
		return m_pPalette;
	
	return (m_pPalette = ReadPalette());
}

BYTE* CImageDS::GetByteData()
{
	if (!m_pByteData)
		ConvertToByte();

	return m_pByteData;
}

BYTE* CImageDS::ConvertToByte()
{
	_int8			tmp_int8;
	_int16			tmp_int16;
	_int32			tmp_int32;
	_int64			tmp_int64;
	unsigned _int16 tmp_uint16;
	unsigned _int32 tmp_uint32;
	unsigned _int64 tmp_uint64;
	float			tmp_float;
	double			tmp_double;
	unsigned long	i;
	double			min=0.0, max=0.0, ratio=1.0;

	// no need to convert for unsigned 8-bit
	if (m_nNT == HDF_NT_UINT8)
		return (m_pByteData = (BYTE*)m_pData);

	m_pByteData = (BYTE*)malloc(m_nDataPoints*sizeof(BYTE));

	if (!m_pByteData || m_nDataPoints<=0)
		return NULL;

	GetDataRange();
	min = *m_pDataRange;
	max = *(m_pDataRange+1);
	ratio = (min == max) ? 1.0 : (double)(255.0/(max-min));
	unsigned char *vp = (unsigned char *)m_pData;
	switch (m_nNT)
	{
	case HDF_NT_INT8:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int8, vp+i, 1);
			*(m_pByteData+i) = (BYTE) ((tmp_int8-min)*ratio);
		}
		break;
	case HDF_NT_INT16:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int16, vp+i, 2);
			*(m_pByteData+i) = (BYTE) ((tmp_int16-min)*ratio);
		}
		break;
	case HDF_NT_INT32:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int32, vp+i, 4);
			*(m_pByteData+i) = (BYTE) ((tmp_int32-min)*ratio);
		}
		break;
	case HDF_NT_INT64:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int64, vp+i, 8);
			*(m_pByteData+i) = (BYTE) ((tmp_int64-min)*ratio);
		}
		break;
	case HDF_NT_UINT16:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint16, vp+i, 2);
			*(m_pByteData+i) = (BYTE) ((tmp_uint16-min)*ratio);
		}
		break;
	case HDF_NT_UINT32:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint32, vp+i, 4);
			*(m_pByteData+i) = (BYTE) ((tmp_uint32-min)*ratio);
		}
		break;
	case HDF_NT_UINT64:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint64, vp+i, 8);
			*(m_pByteData+i) = (BYTE) ((tmp_uint64-min)*ratio);
		}
		break;
	case HDF_NT_FLOAT:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_float, vp+i, 4);
			*(m_pByteData+i) = (BYTE) ((tmp_float-min)*ratio);
		}
		break;
	case HDF_NT_DOUBLE:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_double, vp+i, 8);
			*(m_pByteData+i) = (BYTE) ((tmp_double-min)*ratio);
		}
		break;
	}


	return m_pByteData;
}

double* CImageDS::GetDataRange()
{
	if (m_pDataRange && (*m_pDataRange > *(m_pDataRange+1)))
		return m_pDataRange;

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
	unsigned long	i;
	double			min = DBL_MAX;
	double			max = -DBL_MAX;

	unsigned char *vp = (unsigned char *)m_pData;
	switch (m_nNT)
	{
	case HDF_NT_INT8:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int8, vp+i, 1);
			min = __min(min, tmp_int8);
			max = __max(max, tmp_int8);
		}
		break;
	case HDF_NT_INT16:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int16, vp+i, 2);
			min = __min(min, tmp_int16);
			max = __max(max, tmp_int16);
		}
		break;
	case HDF_NT_INT32:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int32, vp+i, 4);
			min = __min(min, tmp_int32);
			max = __max(max, tmp_int32);
		}
		break;
	case HDF_NT_INT64:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_int64, vp+i, 8);
			min = __min(min, tmp_int64);
			max = __max(max, tmp_int64);
		}
		break;
	case HDF_NT_UINT8:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint8, vp+i, 1);
			min = __min(min, tmp_uint8);
			max = __max(max, tmp_uint8);
		}
		break;
	case HDF_NT_UINT16:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint16, vp+i, 2);
			min = __min(min, tmp_uint16);
			max = __max(max, tmp_uint16);
		}
		break;
	case HDF_NT_UINT32:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint32, vp+i, 4);
			min = __min(min, tmp_uint32);
			max = __max(max, tmp_uint32);
		}
		break;
	case HDF_NT_UINT64:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_uint64, vp+i, 8);
			min = __min(min, tmp_uint64);
			max = __max(max, tmp_uint64);
		}
		break;
	case HDF_NT_FLOAT:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_float, vp+i, 4);
			min = __min(min, tmp_float);
			max = __max(max, tmp_float);
		}
		break;
	case HDF_NT_DOUBLE:
		for (i=0; i<m_nDataPoints; i++)
		{
			memcpy(&tmp_double, vp+i, 8);
			min = __min(min, tmp_double);
			max = __max(max, tmp_double);
		}
		break;
	}

	m_pDataRange = (double *)malloc(2*sizeof(double));
	*m_pDataRange = min;
	*(m_pDataRange+1) = max;

	return m_pDataRange;
}