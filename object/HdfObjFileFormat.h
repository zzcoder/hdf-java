// HdfObjFileFormat.h: interface for the CFileFormat class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJFILEFORMAT_H__59133B51_2228_4EC3_9B8F_CD7CA4265AB0__INCLUDED_)
#define AFX_HDFOBJFILEFORMAT_H__59133B51_2228_4EC3_9B8F_CD7CA4265AB0__INCLUDED_

#include "StdAfx.h"	// Added by ClassView
#include "HdfObj.h"

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CGroup;

class CFileFormat : public CObject  
{
public:
	static CFileFormat* CreateInstance(LPCTSTR  pFilename);
	virtual BOOL IsThisType(LPCTSTR filename) = 0;
	virtual oid_t Open(LPCTSTR filename, UINT nFlag) = 0;
	virtual void Close() = 0;
	oid_t GetFileID() { return m_nFileID; }
	CGroup* GetRootGroup() { return m_pRootGroup; }
	LPCTSTR GetFileName() { return m_pFileName; }
	UINT GetFileType() { return m_nFileType; }

	CFileFormat(LPCTSTR pFileName, UINT nFlag);
	virtual ~CFileFormat();

	enum FILE_ACCESS_FLAG //File access flags
	{
		FILE_ACCESS_READ,
		FILE_ACCESS_WRITE,
		FILE_ACCESS_CREATE
	};

	enum SUPPORTED_FILE_TYPE //File access flags
	{
		SUPPORTED_FILE_TYPE_UNKNOWN,
		SUPPORTED_FILE_TYPE_NCSA_HDF5,
		SUPPORTED_FILE_TYPE_NCSA_HDF4
	};


protected:
	UINT m_nAccessFlag;
	oid_t m_nFileID;
	CGroup* m_pRootGroup;
	LPCTSTR m_pFileName;
	UINT m_nFileType;
};

#endif // !defined(AFX_HDFOBJFILEFORMAT_H__59133B51_2228_4EC3_9B8F_CD7CA4265AB0__INCLUDED_)
