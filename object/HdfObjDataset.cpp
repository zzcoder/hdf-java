// HdfObjDataset.cpp: implementation of the CDataset class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjDataset.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CDataset::CDataset(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
	:CHObject(fileformat, name, path)
{
	m_nType = HDF_DATASET;
	m_pData = NULL;
	m_nRank = -1;
	m_nWidth = 1;
	m_nHeight = 1;
	m_nWidthIdx = -1;
	m_nHeightIdx = -1;
	m_nNT = HDF_NT_UNKNOWN;
	m_nTypeSize = 0;
	m_nDataPoints = 0;
	m_pDims = NULL;
	m_pCount = NULL;
	m_pStart = NULL;
	m_pStride = NULL;

}

CDataset::~CDataset()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free(m_pDims);
	if (m_pCount) free(m_pCount);
	if (m_pStart) free(m_pStart);
	if (m_pStride) free(m_pStride);
}

int CDataset::GetRank()
{
	return m_nRank;
}

unsigned long* CDataset::GetDims()
{
	return m_pDims;
}

unsigned long* CDataset::GetCount()
{
	return m_pCount;
}

unsigned long* CDataset::GetStart()
{
	return m_pStart;
}

unsigned long* CDataset::GetStride()
{
	return m_pStride;
}

void CDataset::FreeData()
{
	if (m_pData)
		free(m_pData);
	m_pData = NULL;
}

int CDataset::GetWidth()
{
	if (m_nRank <= 1)
		return 1;

	return m_pCount[m_nWidthIdx];
}

int CDataset::GetHeight()
{
	if (m_nRank <= 0)
		return 1;

	return m_pCount[m_nHeightIdx];
}

void CDataset::SetWidthIdx(int wIdx)
{
	if (wIdx<0 || wIdx>=m_nRank)
		return;

	if (m_nRank <=0 )
		m_nWidth = 1;
	else
	{
		m_pCount[m_nWidthIdx] = 1;
		m_pCount[wIdx] = m_pDims[wIdx];
		m_nWidth = (int)m_pCount[wIdx];
	}
	m_nWidthIdx = wIdx;
}

void CDataset::SetHeightIdx(int hIdx)
{
	if (hIdx<0 || hIdx>=m_nRank)
		return;


	if (m_nRank <=0 )
		m_nHeight = 1;
	else 
	{
		m_pCount[m_nHeightIdx] = 1;
		m_pCount[hIdx] = m_pDims[hIdx];
		m_nHeight = (int)m_pCount[hIdx];
	}
	m_nHeightIdx = hIdx;
}

