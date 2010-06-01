// HdfObjH4File.h: interface for the CH4File class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH4FILE_H__30A54B78_01BE_45D4_A2F2_9364E3BC00B3__INCLUDED_)
#define AFX_HDFOBJH4FILE_H__30A54B78_01BE_45D4_A2F2_9364E3BC00B3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjFileFormat.h"
#include "HdfObjH4Group.h"
#include "HdfObjH4AtomicDS.h"
#include "HdfObjH4ImageDS.h"
#include "HdfObjH4CompoundDS.h"
#include <mfhdf.h>

class CH4File : public CFileFormat  
{
public:
	BOOL IsThisType(LPCTSTR filename);			// override CHObject::IsThisType(LPCTSTR)
	int GetSD_id() { return m_sdid; }
	int GetGR_id() { return m_grid; }

	CH4File(LPCTSTR pFileName, UINT nFlag);
	virtual ~CH4File();
	oid_t Open(LPCTSTR filename, UINT nFlag);
	void Close();

private:
	CH4Group* GetFileStructure();
	void DepthFirst(CH4Group* pParent);
	CH4Group* GetVGroup(int32, const char* path, CH4Group* pParent);
	CH4ImageDS* GetGRImage(int index, const char* path);
	CH4AtomicDS* GetSDS(int index, const char* path);
	CH4CompoundDS* GetVData(int32 ref, const char* path);
	BOOL FindVGroup(int ref);
	BOOL FindGRImage(int ref);
	BOOL FindSDS(int ref);
	BOOL FindVData(int ref);
	BOOL IsReservedName( CString name );

private:
    int m_grid;
    int m_sdid;
	UINT m_nFlag;
	CUIntArray m_arrayVGroup;
	CUIntArray m_arrayGRImage;
	CUIntArray m_arraySDS;
	CUIntArray m_arrayVData;
};

#endif // !defined(AFX_HDFOBJH4FILE_H__30A54B78_01BE_45D4_A2F2_9364E3BC00B3__INCLUDED_)
