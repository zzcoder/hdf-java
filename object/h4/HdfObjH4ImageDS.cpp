// HdfObjH4ImageDS.cpp: implementation of the CH4ImageDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH4ImageDS.h"
#include "HdfObjH4File.h"
#include "HdfObjH4AtomicDS.h"
#include "HdfObjAttribute.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH4ImageDS::CH4ImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CImageDS(fileformat, name, path)
{
	m_nType = HDF_DATASET_IMAGE;
	m_tag = DFTAG_RIG;
	m_nComponents = 1;
	CH4File* h4file = (CH4File *)fileformat;
	m_grid = h4file->GetGR_id();
}

CH4ImageDS::~CH4ImageDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
}

int CH4ImageDS::Open()
{

	int index = GRreftoindex(m_grid, (short)m_nObjectID);
	int id = GRselect(m_grid, index);

	return id;
}

void CH4ImageDS::Close(int id)
{
	GRendaccess(id);
}

CObArray* CH4ImageDS::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	int id = Open();
	if (id < 0) return NULL;

	LPTSTR name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 ncomp, count;
	int32 data_type;
	int32 interlace_mode;
	int32 dims[] = {0, 0};
	int32 num_attrs;
	intn ret = GRgetiminfo(id, name, &ncomp, &data_type, &interlace_mode, dims, &num_attrs); 
	int* idims = NULL;

	if (ret == FAIL)
	{
		Close(id);
		return NULL;
	}

	m_pAttrList = new CObArray;
	for (int i=0; i<num_attrs; i++)
	{
		name = (LPTSTR)malloc(MAX_NC_NAME);

		if ( GRattrinfo(id, i, name, &data_type, &count) == FAIL ) 
			continue;

		CAttribute* attr = new CAttribute(name);
		attr->SetValueType(CH4AtomicDS::GetNT(data_type));
        m_pAttrList->Add(attr);

		idims = (int*) malloc(sizeof(int));
		idims[0] = count;
		attr->SetDims(idims);

		int typeSize = DFKNTsize(data_type);
		void* attrValue = malloc(count*typeSize);
		if (GRgetattr(id, i, attrValue) == FAIL)
		{
			free(attrValue);
			continue;
		} else
			attr->SetValue(attrValue);
		attr->SetRank(1);
		attr->SetTypeSize(typeSize);
	}
	Close(id);

	return m_pAttrList;
}

void CH4ImageDS::Init()
{
	// TODO: Add your own code here.
	int32 ref = (int32)m_nObjectID;
	int32 index = GRreftoindex(m_grid, (uint16)ref);
	int32 gr_id = GRselect( m_grid, index );

	if (gr_id == FAIL) 
		return;


	LPTSTR gr_name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 ncomp;
	int32 data_type;
	int32 interlace_mode;
	int32 dims[] = {0, 0};
	int32 num_attrs;
	intn ret = GRgetiminfo(gr_id, gr_name, &ncomp, &data_type, &interlace_mode, dims, &num_attrs); 

	m_nInterlaceMode = IMAGE_INTERLACE_PIXEL;
	m_nImageSubclass = IMAGE_SUBCLASS_INDEXED;

	if (interlace_mode == MFGR_INTERLACE_COMPONENT)
		m_nInterlaceMode = IMAGE_INTERLACE_PLANE;

	if (ncomp ==3 )
		m_nImageSubclass = IMAGE_SUBCLASS_TRUECOLOR;

	if (m_nImageSubclass == IMAGE_SUBCLASS_INDEXED)
	{
		m_pPalette = (unsigned char *)malloc(3*256);
		int32 lutid = GRgetlutid(gr_id, 0); // // find the first palette.
		GRreqlutil(gr_id, interlace_mode);
		GRreadlut(lutid, m_pPalette);
	}

	m_tid = (int)data_type;
	m_nNT = GetNT(m_tid);
	m_nTypeSize = DFKNTsize(m_tid); 

	m_nRank = 2;
	m_nComponents = (int)ncomp;
	m_pDims = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pCount = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pStart = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pStride = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));

	for (int i=0; i<m_nRank; i++)
	{
		m_pDims[i] = (unsigned long)dims[i];
		m_pCount[i] = (unsigned long)dims[i];
		m_pStart[i] = 0;
		m_pStride[i] = 1;
	}

	// data in HDF4 GR image is arranged as dim[0]=width, dim[1]=height.
	// other image data is arranged as dim[0]=height, dim[1]=width.
	m_nHeight = m_pCount[1];
	m_nHeightIdx = 1;
	m_nWidth = m_pCount[0];
	m_nWidthIdx = 0;

	GRendaccess(gr_id);
}

unsigned char* CH4ImageDS::ReadPalette()
{
	if (m_pPalette || m_nImageSubclass == IMAGE_SUBCLASS_TRUECOLOR)
		return m_pPalette;

	if (m_nRank < 0) Init();

	return m_pPalette;
}

void* CH4ImageDS::ReadData()
{
	if (m_pData)
		return m_pData; // data is loaded

	if (m_nRank<0)
		Init();

	int32 ref = (int32)m_nObjectID;
	int32 index = GRreftoindex(m_grid, (uint16)ref);
	int32 gr_id = GRselect( m_grid, index );

	if (gr_id == FAIL || m_nRank<0) 
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

	m_pData = malloc(m_nDataPoints*m_nTypeSize*m_nComponents);

	if (m_pData)
	{
		intn ret = GRreadimage(gr_id, start, stride, count, (VOIDP) m_pData);
		if (ret == FAIL)
		{
			free(m_pData);
			m_pData = NULL;
		}
	}

	free(start);
	free(stride);
	free(count);
	GRendaccess(gr_id);

	return m_pData;
}

int CH4ImageDS::GetNT(int tid)
{
	int nt = HDF_NT_UNKNOWN;

	switch(tid)
	{
		case DFNT_CHAR:
		case DFNT_INT8:
			nt = HDF_NT_INT8;
			break;
		case DFNT_UCHAR8:
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