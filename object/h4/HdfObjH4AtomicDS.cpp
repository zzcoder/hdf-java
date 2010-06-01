// HdfObjH4AtomicDS.cpp: implementation of the CH4AtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH4AtomicDS.h"
#include "HdfObjH4File.h"
#include "HdfObjAttribute.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH4AtomicDS::CH4AtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CAtomicDS(fileformat, name, path)
{
	m_tag = DFTAG_NDG;
	CH4File* h4file = (CH4File *)fileformat;
	m_sdid = h4file->GetSD_id();
}

CH4AtomicDS::~CH4AtomicDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
}

int CH4AtomicDS::Open()
{

	int index = SDreftoindex(m_sdid, (short)m_nObjectID);
	int id = SDselect(m_sdid, index);

	return id;
}

void CH4AtomicDS::Close(int id)
{
	SDendaccess(id);
}

CObArray* CH4AtomicDS::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	m_pAttrList = new CObArray;
	int id = Open();
	if (id < 0) return NULL;
	LPTSTR name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 rank;
	int32 data_type, count;
	int32 dim_sizes[20];
	int32 num_attrs;
	intn ret = SDgetinfo(id, name, &rank, dim_sizes, &data_type, &num_attrs); 
	int* idims = NULL;

	if (ret == FAIL)
	{
		Close(id);
		return m_pAttrList;
	}

	for (int i=0; i<num_attrs; i++)
	{
		name = (LPTSTR)malloc(MAX_NC_NAME);

		if ( SDattrinfo(id, i, name, &data_type, &count) == FAIL ) 
			continue;

		CAttribute* attr = new CAttribute(name);
		attr->SetValueType(CH4AtomicDS::GetNT(data_type));

        m_pAttrList->Add(attr);

		idims = (int*) malloc(sizeof(int));
		idims[0] = count;
		attr->SetDims(idims);

		int attrSize = DFKNTsize(data_type);
		void* attrValue = malloc(count*attrSize);
		if (SDreadattr(id, i, attrValue) == FAIL)
		{
			free(attrValue);
			continue;
		} else
			attr->SetValue(attrValue);
		attr->SetRank(1);
		attr->SetTypeSize(attrSize);
	}

	Close(id);

	return m_pAttrList;
}

void CH4AtomicDS::Init()
{
	// TODO: Add your own code here.
	int32 ref = (int32)m_nObjectID;
	int32 index = SDreftoindex(m_sdid, ref);
	int32 sd_id = SDselect( m_sdid, index );

	if (sd_id == FAIL) 
		return;


	LPTSTR sd_name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 rank;
	int32 data_type;
	int32 dims[MAX_VAR_DIMS];
	int32 num_attrs;
	intn ret = SDgetinfo(sd_id, sd_name, &rank, dims, &data_type, &num_attrs); 
	SDendaccess(sd_id);

	if (ret == FAIL)
		return;

	m_tid = (int)data_type;
	m_nNT = GetNT(m_tid);
	m_nTypeSize = DFKNTsize(m_tid);

	m_nRank = rank;
	if (m_nRank == 0) // scalar point
	{
		m_pDims = (unsigned long *)malloc(sizeof(unsigned long));
		m_pCount = (unsigned long *)malloc(sizeof(unsigned long));
		m_pStart = (unsigned long *)malloc(sizeof(unsigned long));
		m_pStride = (unsigned long *)malloc(sizeof(unsigned long));

		m_pDims[0] = 1;
		m_pCount[0] = 1;
		m_pStart[0] = 0;
		m_pStride[0] = 1;
		m_nWidth = m_nHeight = 1;
		m_nHeightIdx = m_nWidthIdx = 0;
	}
	else if (m_nRank>0)
	{
		m_pDims = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pCount = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pStart = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pStride = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));

		for (int i=0; i<m_nRank; i++)
		{
			m_pDims[i] = (unsigned long)dims[i];
			m_pCount[i] = 1;
			m_pStart[i] = 0;
			m_pStride[i] = 1;
		}

		if (m_nRank == 1)
		{
			m_pCount[0] = m_pDims[0];
			m_nHeight = m_pCount[0];
			m_nHeightIdx = 0;
		}
		else 
		{
			m_pCount[0] = m_pDims[0];
			m_pCount[1] = m_pDims[1];
			m_nHeight = m_pCount[0];
			m_nHeightIdx = 0;
			m_nWidth = m_pCount[1];
			m_nWidthIdx = 1;
		}
	}
}

void* CH4AtomicDS::ReadData()
{
	if (m_pData)
		return m_pData; // data is loaded

	if (m_nRank<0)
		Init();

	int32 ref = (int32)m_nObjectID;
	int32 index = SDreftoindex(m_sdid, ref);
	int32 sd_id = SDselect( m_sdid, index );

	if (sd_id == FAIL || m_nRank<0) 
		return NULL;

	int32* start = (int32 *)malloc(m_nRank*sizeof(int32));
	int32* stride = (int32 *)malloc(m_nRank*sizeof(int32));
	int32* count = (int32 *)malloc(m_nRank*sizeof(int32));

	m_nDataPoints = 1;
	for (int i=0; i<m_nRank; i++)
	{
		count[i] = (int32)m_pCount[i];
		start[i] = (int32)m_pStart[i];
		stride[i] = (int32)m_pStride[i];
		m_nDataPoints *= count[i];
	}

	m_pData = malloc(m_nDataPoints*m_nTypeSize);

	if (m_pData)
	{
		intn ret = SDreaddata(sd_id, start, stride, count, (VOIDP) m_pData);
		if (ret == FAIL)
		{
			free(m_pData);
			m_pData = NULL;
		}
	}

	free(start);
	free(stride);
	free(count);
	SDendaccess(sd_id);

	return m_pData;
}

int CH4AtomicDS::GetNT(int tid)
{
	int nt = HDF_NT_UNKNOWN;

	switch(tid)
	{
		case DFNT_CHAR:
			nt = HDF_NT_CHAR;
			break;
		case DFNT_INT8:
			nt = HDF_NT_INT8;
			break;
		case DFNT_UCHAR8:
			nt = HDF_NT_UCHAR;
			break;
		case DFNT_UINT8:
			nt = HDF_NT_UINT8;
			break;
		case DFNT_INT16:
			nt = HDF_NT_INT16;
			break;
		case DFNT_UINT16:
			nt = HDF_NT_UINT16;
			break;
		case DFNT_INT32:
			nt = HDF_NT_INT32;
			break;
		case DFNT_UINT32:
			nt = HDF_NT_UINT32;
			break;
		case DFNT_INT64:
			nt = HDF_NT_INT64;
			break;
		case DFNT_UINT64:
			nt = HDF_NT_UINT64;
			break;
		case DFNT_FLOAT32:
			nt = HDF_NT_FLOAT;
			break;
		case DFNT_FLOAT64:
			nt = HDF_NT_DOUBLE;
			break;
		default:
			nt = HDF_NT_UNKNOWN;
			break;
	}

	return nt;
}


