// HdfObjImageDS.h: interface for the CImageDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJIMAGEDS_H__055336CB_388A_4F5B_9AE1_9AA0568E97AA__INCLUDED_)
#define AFX_HDFOBJIMAGEDS_H__055336CB_388A_4F5B_9AE1_9AA0568E97AA__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjAtomicDS.h"

class CImageDS : public CAtomicDS  
{
public:
	BYTE* GetByteData();
	int GetDatasetType() { return HDF_DATASET_IMAGE; }
	BOOL IsTrueColor() { return (m_nImageSubclass==IMAGE_SUBCLASS_TRUECOLOR); }
	int GetInterlace() { return m_nInterlaceMode; }
	int GetImageSubclass() { return m_nImageSubclass; }
	int GetNumberOfComponents() { return m_nComponents; }
	virtual unsigned char* ReadPalette() = 0;
	unsigned char* GetPalette();
	double* CImageDS::GetDataRange();

	CImageDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CImageDS();
	virtual CString ValueToStr(int row, int col, CString strValue);

    enum IMAGE_SUBCLASS
    {
        IMAGE_SUBCLASS_INDEXED,
        IMAGE_SUBCLASS_TRUECOLOR,
        IMAGE_SUBCLASS_BITMAP,
        IMAGE_SUBCLASS_GRAYSCALE
    };

    enum INTERLACE_MODE
    {
        IMAGE_INTERLACE_PIXEL,
        IMAGE_INTERLACE_PLANE
    };

protected:
	BYTE* ConvertToByte();
	unsigned char* m_pPalette;
	int m_nInterlaceMode;
	int m_nImageSubclass;
	int m_nComponents;
	BYTE* m_pByteData;
};

#endif // !defined(AFX_HDFOBJIMAGEDS_H__055336CB_388A_4F5B_9AE1_9AA0568E97AA__INCLUDED_)
