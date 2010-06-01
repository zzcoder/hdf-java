// HdfObjDataset.h: interface for the CDataset class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJDATASET_H__F86A2BD8_BA19_4D17_9B24_4BF61D83BF92__INCLUDED_)
#define AFX_HDFOBJDATASET_H__F86A2BD8_BA19_4D17_9B24_4BF61D83BF92__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObj.h"

class CDataset : public CHObject  
{
public:
	void FreeData();
	virtual void* ReadData() = 0;
	virtual CString ValueToStr(int row, int col, CString strValue) = 0;
	unsigned long* GetStride();
	unsigned long* GetStart();
	unsigned long* GetCount();
	unsigned long* GetDims();

	virtual int GetWidth();
	virtual int GetHeight();
	void SetWidthIdx(int wIdx);
	void SetHeightIdx(int hIdx);

	int GetRank();

	CDataset(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CDataset();

	enum hdf_datatype_t //File access flags
	{
		HDF_NT_UNKNOWN=-1,
		HDF_NT_CHAR=0,
		HDF_NT_INT8,
		HDF_NT_INT16,
		HDF_NT_INT32,
		HDF_NT_INT64,
		HDF_NT_UCHAR,
		HDF_NT_UINT8,
		HDF_NT_UINT16,
		HDF_NT_UINT32,
		HDF_NT_UINT64,
		HDF_NT_FLOAT,
		HDF_NT_DOUBLE,
		HDF_NT_STRING
	};

protected:
	int m_nRank;

	// for display data in 2D table
	int m_nWidth;
	int m_nHeight;
	int m_nWidthIdx;
	int m_nHeightIdx;
	int m_nNT;
	int m_nTypeSize;
	unsigned long m_nDataPoints;

	void* m_pData;

	unsigned long* m_pDims;
	unsigned long* m_pCount;
	unsigned long* m_pStart;
	unsigned long* m_pStride;
};

#endif // !defined(AFX_HDFOBJDATASET_H__F86A2BD8_BA19_4D17_9B24_4BF61D83BF92__INCLUDED_)
