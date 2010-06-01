// HdfObjH5ImageDS.cpp: implementation of the CH5ImageDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH5ImageDS.h"
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

CH5ImageDS::CH5ImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CImageDS(fileformat, name, path)
{
}

CH5ImageDS::~CH5ImageDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
}

CObArray* CH5ImageDS::ReadAttributes()
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

void CH5ImageDS::Init()
{
	hid_t did=-1, sid=-1, tid = -1, aid=-1, atid=-1;
	hsize_t* dims = NULL;

	// TODO: Add your own code here.

	hid_t fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);
	sid = H5Dget_space(did );
	tid = H5Dget_type(did );
	m_nNT = CH5AtomicDS::GetNT(tid);

	m_nRank = H5Sget_simple_extent_ndims(sid);
	if (m_nRank>0)
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
	}

	// default image settings
	m_nInterlaceMode = IMAGE_INTERLACE_PIXEL;
	m_nImageSubclass = IMAGE_SUBCLASS_INDEXED;
	// get informaton for Attribute name="IMAGE_SUBCLASS" 
    aid = H5Aopen_name(did, "IMAGE_SUBCLASS");
	if (aid > 0)
	{
		atid = H5Aget_type(aid);
		char* subclassValue = (char*)malloc(sizeof(char) * 17);
		H5Aread(aid, atid, subclassValue);
		H5Tclose(atid);
		H5Aclose(aid);

		if (strncmp(subclassValue, "IMAGE_TRUECOLOR", 15) == 0)
		{
			m_nImageSubclass = IMAGE_SUBCLASS_TRUECOLOR;
			if (m_nRank>2)
			{
				m_pCount[2] = m_pDims[2];
				m_nComponents = m_pCount[2];
				m_nHeight = m_pCount[0];
				m_nHeightIdx = 0;
				m_nWidth = m_pCount[1];
				m_nWidthIdx = 1;
			}
		}
		free(subclassValue);
    }

	// get informaton for Attribute name="INTERLACE_MODE" 
	aid = H5Aopen_name(did, "INTERLACE_MODE");
	if (aid > 0)
	{
		atid = H5Aget_type(aid);
		char* interlaceValue = (char*)malloc(sizeof(char) * 17);
		H5Aread(aid, atid, interlaceValue);
		H5Aclose(aid);
		H5Tclose(atid);

		if (strncmp(interlaceValue, "INTERLACE_PLANE", 15) == 0)
		{
			m_nInterlaceMode = IMAGE_INTERLACE_PLANE;
			if (m_nRank>2 && m_pDims[0]>=3) 
			{
				m_nImageSubclass = IMAGE_SUBCLASS_TRUECOLOR;
				// storage layout [pixel components][height][width]
				m_pCount[2] = m_pDims[2];
				m_nComponents = m_pCount[0];
				m_nHeight = m_pCount[1];
				m_nHeightIdx = 1;
				m_nWidth = m_pCount[2];
				m_nWidthIdx = 2;
			}
		}
		else
		{
			m_nInterlaceMode = IMAGE_INTERLACE_PIXEL;
			if (m_nRank>2 && m_pDims[2]>=3) 
			{
				m_nImageSubclass = IMAGE_SUBCLASS_TRUECOLOR;
				m_pCount[2] = m_pDims[2];
				m_nComponents = m_pCount[2];
				m_nHeight = m_pCount[0];
				m_nHeightIdx = 0;
				m_nWidth = m_pCount[1];
				m_nWidthIdx = 1;
			}
		}

		free(interlaceValue);
	}

    aid = H5Aopen_name(did, "IMAGE_MINMAXRANGE");
	if (aid > 0)
	{
		atid = H5Aget_type(aid);
		int tsize = H5Tget_size(atid);
		void* attrValue = malloc(2*tsize*sizeof(char));
		if (H5Aread(aid, atid, attrValue)>=0)
		{
			m_pDataRange = (double *)malloc(2*sizeof(double));
			unsigned char *vp = (unsigned char *)attrValue;
			memcpy(&m_pDataRange, vp, tsize);
			memcpy(&m_pDataRange+1, vp+1, tsize);
		}
		free(attrValue);
		H5Aclose(aid);
		H5Tclose(atid);
     }

	m_pPaletteRefs = GetPaletteRef(did);

	if (dims) free(dims);
	H5Sclose(sid);
	H5Tclose(tid);
	H5Dclose(did);
}

void* CH5ImageDS::ReadData()
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
	m_nDataPoints = m_nWidth*m_nHeight*m_nComponents;

	if (m_nRank > 0)
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

    m_pData = malloc(m_nDataPoints*H5Tget_size(tid));
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

unsigned char* CH5ImageDS::ReadPalette()
{
	if (m_pPalette || m_nImageSubclass == IMAGE_SUBCLASS_TRUECOLOR)
		return m_pPalette;


    if (!m_pPaletteRefs) // no palete attached to the image
        return NULL;

    hid_t fid=-1, pal_id=-1, tid=-1;
	fid = (hid_t)m_pFile->GetFileID();

	// use the fist palette
    pal_id =  H5Rdereference(fid, H5R_OBJECT, m_pPaletteRefs);
    tid = H5Dget_type(pal_id);

    // support up to 3*256 byte palette data
    if (H5Dget_storage_size(pal_id) <= 768) 
	{
		m_pPalette = (unsigned char *)malloc(3*256); // support 256 8-bit colors
        herr_t status = H5Dread( pal_id, tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, m_pPalette);
		if (status < 0)
		{
			free (m_pPalette);
			m_pPalette = NULL;
		}
    }

	H5Tclose(tid);
    H5Dclose(pal_id);

	return m_pPalette;
}

/* reads the references of palettes into an array
 * Each reference requires  eight bytes storage. Therefore, the array length
 * is 8*numberOfPalettes.
*/
hobj_ref_t* CH5ImageDS::GetPaletteRef(hid_t did)
{
    hid_t aid=-1, sid=-1, atid=-1;
    hobj_ref_t *ref_buf;
	int rank;
	size_t size;
	hsize_t *dims;

 
    aid = H5Aopen_name(did, "PALETTE");
	if (aid < 0) return NULL;
	dims = NULL;
	ref_buf = NULL;

    sid = H5Aget_space(aid);
    rank = H5Sget_simple_extent_ndims(sid);
	size = 1;

	if (rank > 0)
	{
		dims = (hsize_t *)malloc(rank * sizeof(hsize_t));
        H5Sget_simple_extent_dims(sid, dims, NULL);
        for (int i=0; i<rank; i++)
            size *= (size_t)dims[i];
	}

    ref_buf = (hobj_ref_t *)malloc(size*sizeof(hobj_ref_t));
    atid = H5Aget_type(aid);

    if (H5Aread( aid, atid, ref_buf) < 0)
    {
		free(ref_buf);
        ref_buf = NULL;
    }

	H5Tclose(atid);
	H5Sclose(sid);
	H5Aclose(aid);

    return ref_buf;
}
