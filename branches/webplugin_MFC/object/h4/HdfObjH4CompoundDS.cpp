// HdfObjH4CompoundDS.cpp: implementation of the CH4CompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH4CompoundDS.h"
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

CH4CompoundDS::CH4CompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CCompoundDS(fileformat, name, path)
{
	m_tag = DFTAG_VS;
}

CH4CompoundDS::~CH4CompoundDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
}

int CH4CompoundDS::Open()
{
    int id = VSattach( (int32)m_pFile->GetFileID(), (int32)m_nObjectID, "r" );
    
	if ( id == FAIL )
	{
		VSdetach(id);
		return -1;
	}

	return id;
}

void CH4CompoundDS::Close(int id)
{
	VSdetach(id);
}

CObArray* CH4CompoundDS::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	int id = Open();
	if (id < 0) return NULL;

	int32 attrType, attrCount, attrSize;
	int n = VSnattrs(id);
	m_pAttrList = new CObArray;
	LPTSTR attrName = (LPTSTR)malloc(MAX_NC_NAME);
	int* idims = NULL;

	for (int i=0; i<n; i++)
	{
		attrName = (LPTSTR)malloc(MAX_NC_NAME);

		if (VSattrinfo( id, -1, i, attrName, &attrType, &attrCount, &attrSize) < 0)
			continue;

		CAttribute* attr = new CAttribute(attrName);
		attr->SetValueType(CH4AtomicDS::GetNT(attrType));
        m_pAttrList->Add(attr);
		idims = (int*) malloc(sizeof(int));
		idims[0] = attrCount;
		attr->SetDims(idims);

		void* attrValue = malloc(attrCount*attrSize);
		if (VSgetattr( id, -1, i, attrValue) == FAIL)
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

void CH4CompoundDS::Init()
{
	// TODO: Add your own code here.
	int32 fid = (int32)m_pFile->GetFileID();
	int32 vs_id = VSattach(fid, (int32)m_nObjectID, "r");

	m_nMembers = VFnfields(vs_id);
	m_nRecords = VSelts(vs_id);

	m_nRank = 1;
	m_pDims = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pCount = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pStart = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
	m_pStride = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));

	m_pDims[0] = m_nRecords;
	m_pStart[0] = 0;
	m_pStride[0] = 1;
	m_pCount[0] = m_nRecords;
	m_nHeight = m_pCount[0];
	m_nHeightIdx = 0;
	m_nWidth = m_nMembers;
	m_nDataPoints = m_nRecords;

	m_pMemberNames = (char**)malloc(m_nMembers*sizeof(*m_pMemberNames));
	m_pMemberTypes = (int *)malloc(m_nMembers*sizeof(int));
	m_pMemberOrders = (int *)malloc(m_nMembers*sizeof(int));
	m_pFieldSizes = (int32 *)malloc(m_nMembers*sizeof(int32));

	for (int i=0; i<m_nMembers; i++)
	{
		m_pMemberNames[i] = VFfieldname(vs_id, i);
		m_pMemberTypes[i] = VFfieldtype(vs_id, i);
		// mask off the litend bit
		m_pMemberTypes[i] = m_pMemberTypes[i] & (~DFNT_LITEND);
		m_pMemberOrders[i] = VFfieldorder(vs_id, i);
		m_pFieldSizes[i] = VSsizeof(vs_id, m_pMemberNames[i]);
		m_nTypeSize += m_pFieldSizes[i];
	} // for (int i=0; i<numberOfMembers; i++)

	VSdetach(vs_id);
}

void* CH4CompoundDS::ReadData()
{
	if (m_pData)
		return m_pData; // data is loaded

	if (m_nRank < 0)
		Init();

	int32 fid = (int32)m_pFile->GetFileID();
	int32 vs_id = VSattach(fid, (int32)m_nObjectID, "r");

	char fields[VSFIELDMAX*FIELDNAMELENMAX];
	int32  interlace;
	char   vsname[VSNAMELENMAX];
	int32  vsize, nr;
	VSinquire( vs_id, &nr, &interlace, fields, &vsize, vsname );
	VSseek(vs_id, 0);
	VSsetfields( vs_id, fields );

 	m_pData =malloc(m_nDataPoints*m_nTypeSize);

	if (m_pData)
	{
		int32 nRecords = VSread(vs_id, (uint8 *)m_pData, m_nRecords, interlace);
		if (nRecords <= 0)
		{
			free(m_pData);
			m_pData = NULL;
		}
	}

	VSdetach(vs_id);
	return m_pData;
}

CString CH4CompoundDS::ValueToStr(int row, int col, CString strValue)
{
	unsigned long offset=0;
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

	unsigned char *vp = (unsigned char *)m_pData;
	strValue.Empty();

	offset = row*m_nTypeSize;
	for (i=0; i<col; i++)
		offset += m_pFieldSizes[i];

	int n = m_pMemberOrders[col];

	CString oneValue;
	switch (m_pMemberTypes[col])
	{
		case DFNT_CHAR:
 			memcpy(&tmp_char, vp+offset, 1);
			oneValue.Format("%c",tmp_char);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_char, vp+offset+i, 1);
				oneValue.Format("%c",tmp_char); 
				strValue += oneValue;
			}
			break;
		case DFNT_UCHAR:
 			memcpy(&tmp_uchar, vp+offset, 1);
			oneValue.Format("%c",tmp_uchar);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uchar, vp+offset+i, 1);
				oneValue.Format("%c",tmp_uchar); 
				strValue += oneValue;
			}
			break;
		case DFNT_INT8:
			memcpy(&tmp_int8, vp+offset, 1);
			oneValue.Format("%d",tmp_int8);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int8, vp+offset+i, 1);
				oneValue.Format(", %d",tmp_int8); 
				strValue += oneValue;
			}
			break;
		case DFNT_INT16:
			memcpy(&tmp_int16, vp+offset, 2);
			oneValue.Format("%d",tmp_int16);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int16, vp+offset+i*2, 2);
				oneValue.Format(", %d",tmp_int16);
				strValue += oneValue;
			}
			break;
		case DFNT_INT32:
			memcpy(&tmp_int32, vp+offset, 4);
			oneValue.Format("%d",tmp_int32);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int32, vp+offset+i*4, 4);
				oneValue.Format(", %d",tmp_int32);
				strValue += oneValue;
			}
			break;
		case DFNT_INT64:
			memcpy(&tmp_int64, vp+offset, 8);
			oneValue.Format("%d",tmp_int64);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_int64, vp+offset+i*8, 8);
				oneValue.Format(", %d",tmp_int64);
				strValue += oneValue;
			}
			break;
		case DFNT_UINT8:
			memcpy(&tmp_uint8, vp+offset, 1);
			oneValue.Format("%u", tmp_uint8);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint8, vp+offset+i, 1);
				oneValue.Format(", %u", tmp_uint8); 
				strValue += oneValue;
			}
			break;
		case DFNT_UINT16:
			memcpy(&tmp_uint16, vp+offset, 2);
			oneValue.Format("%u", tmp_uint16);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint16, vp+offset+i*2, 2);
				oneValue.Format(", %u", tmp_uint16);
				strValue += oneValue;
			}
			break;
		case DFNT_UINT32:
			memcpy(&tmp_uint32, vp+offset, 4);
			oneValue.Format("%u", tmp_uint32);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint32, vp+offset+i*4, 4);
				oneValue.Format(", %u", tmp_uint32);
				strValue += oneValue;
			}
			break;
		case DFNT_UINT64:
			memcpy(&tmp_uint64, vp+offset, 8);
			oneValue.Format("%u", tmp_uint64);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_uint64, vp+offset+i*8, 8);
				oneValue.Format(", %u", tmp_uint64);
				strValue += oneValue;
			}
			break;
		case DFNT_FLOAT:
			memcpy(&tmp_float, vp+offset, 4);
			oneValue.Format("%g",tmp_float);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_float, vp+offset+i*4, 4);
				oneValue.Format(", %g",tmp_float);
				strValue += oneValue;
			}
			break;
		case DFNT_DOUBLE:
			memcpy(&tmp_double, vp+offset, 8);
			oneValue.Format("%g",tmp_double);
			strValue += oneValue;
			for (i=1; i<n; i++)
			{
				memcpy(&tmp_double, vp+offset+i*8, 8);
				oneValue.Format(", %g",tmp_double);
				strValue += oneValue;
			}
			break;
		default:
			break;

	}

	return strValue;
}

CString CH4CompoundDS::GetDatatypeInfo()
{
	CString desc;

	desc += TABLE_LINE;
	for (int i=0; i<m_nMembers; i++)
	{
		desc += "\n\t";
		desc += m_pMemberNames[i];
		desc += ",\t";
		desc += CH4AtomicDS::GetDatatypeInfo(CH4AtomicDS::GetNT(m_pMemberTypes[i]));
	}

	return desc;
}