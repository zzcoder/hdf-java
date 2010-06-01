// HdfObjH5AtomicDS.cpp: implementation of the CH5AtomicDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH5AtomicDS.h"
#include "HdfObjH5File.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
CH5AtomicDS::CH5AtomicDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CAtomicDS(fileformat, name, path)
{

}

CH5AtomicDS::~CH5AtomicDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
}

CObArray* CH5AtomicDS::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	hid_t did, fid;
	fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);

	m_pAttrList = CH5File::GetAttribute(did);

	H5Dclose(did);

	return m_pAttrList;
}

void CH5AtomicDS::Init()
{
	hid_t did=-1, sid=-1, tid = -1;
	hsize_t* dims = NULL;

	// TODO: Add your own code here.

	hid_t fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);
	sid = H5Dget_space(did );
	tid = H5Dget_type(did );
	m_nNT = GetNT(tid);
	m_nTypeSize = H5Tget_size(tid);

	m_nRank = H5Sget_simple_extent_ndims(sid);

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
		dims = (hsize_t*)malloc(m_nRank*sizeof(hsize_t));
		H5Sget_simple_extent_dims(sid, dims,NULL ) ;

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

		free(dims);
	}

	H5Sclose(sid);
	H5Tclose(tid);
	H5Dclose(did);
}

void* CH5AtomicDS::ReadData()
{
	hid_t did=-1, msid=-1, fsid=-1, tid=-1, mem_type_id=-1, fid=-1;

	if (m_pData)
		return m_pData; // data is loaded

	if (m_nRank < 0)
		Init();

	fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);
	tid = H5Dget_type(did );
	msid = fsid = H5S_ALL;
	m_nDataPoints = m_nWidth*m_nHeight;

	if (m_nRank > 0 && m_nRank*m_pDims[0]>1)
	{
		hsize_t mdims[1];
		mdims[0] = m_nDataPoints;
		msid = H5Screate_simple(1, mdims, NULL);
		hssize_t *start;
		hsize_t *count;

		start = (hssize_t *)malloc(m_nRank*sizeof(hssize_t));
		count = (hsize_t *)malloc(m_nRank*sizeof(hsize_t));

		for (int i=0; i<m_nRank; i++)
		{
			start[i] = m_pStart[i];
			count[i] = m_pCount[i];
		}

		fsid = H5Dget_space(did);
		H5Sselect_hyperslab(fsid, H5S_SELECT_SET, start, NULL, count, NULL);

		free(start);
		free(count);
	}

    m_pData = malloc(m_nDataPoints*m_nTypeSize*sizeof(char));
	if (m_pData)
	{
		mem_type_id = H5Tget_native_type(tid,H5T_DIR_ASCEND);
		herr_t status =  H5Dread (did, mem_type_id, msid, fsid, H5P_DEFAULT, m_pData);
		if (status < 0)
		{
			free(m_pData);
			m_pData = NULL;
		}
	}

	H5Sclose(msid);
	H5Sclose(fsid);
	H5Tclose(mem_type_id);
	H5Tclose(tid);
	H5Dclose(did);

	return m_pData;
}

int CH5AtomicDS::GetNT(int tid)
{
	int nt = HDF_NT_UNKNOWN;
	int tclass = H5Tget_class(tid);
    int tsize = H5Tget_size(tid);
    int tsign = H5Tget_sign(tid);
	int base_tid = -1;

	switch (tclass)
	{
	case H5T_INTEGER:
		switch (tsize)
		{
		case 1:
			if (tsign == H5T_SGN_NONE)
				nt = HDF_NT_UINT8;
			else
				nt = HDF_NT_INT8;
			break;
		case 2:
			if (tsign == H5T_SGN_NONE)
				nt = HDF_NT_UINT16;
			else
				nt = HDF_NT_INT16;
			break;
		case 4:
			if (tsign == H5T_SGN_NONE)
				nt = HDF_NT_UINT32;
			else
				nt = HDF_NT_INT32;
			break;
		case 8:
			if (tsign == H5T_SGN_NONE)
				nt = HDF_NT_UINT64;
			else
				nt = HDF_NT_INT64;
			break;
		}
		break;
	case H5T_FLOAT:
        if (tsize == 4)
			nt = HDF_NT_FLOAT;
        else if (tsize == 8)
			nt = HDF_NT_DOUBLE;
		break;
	case H5T_STRING:
		nt = HDF_NT_STRING;
		break;
	case H5T_ARRAY:
		base_tid = H5Tget_super(tid);
		nt = GetNT(base_tid);
		H5Tclose(base_tid);
		break;
	case H5T_REFERENCE:
		nt = HDF_NT_UINT64;
		break;
 	default:
		nt = HDF_NT_UNKNOWN;
		break;
	}

	return nt;
}


