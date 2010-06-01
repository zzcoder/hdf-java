// HdfObj.h: interface for the CHObject class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJ_H__729F2FB9_62B7_42D7_B13C_FFBE463D4958__INCLUDED_)
#define AFX_HDFOBJ_H__729F2FB9_62B7_42D7_B13C_FFBE463D4958__INCLUDED_

typedef __int64 oid_t;
#define MAX_NAME_LEN 256
#define TABLE_LINE "--------------------------------------------------------------------------------------------------------------------------------------------------------------"

#include "HdfObjFileFormat.h"	// Added by ClassView
#include "StdAfx.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CFileFormat;

class CHObject : public CObject  
{
public:
	int GetType();
	virtual CObArray* ReadAttributes() =0;
	virtual CString GetObjInfo() = 0;
	virtual void Init() = 0;
	CHObject(CFileFormat* file, LPCTSTR name, LPCTSTR path);
	virtual ~CHObject();
	CFileFormat* GetFile();
	LPCTSTR GetName();
	LPCTSTR GetFullName();
	LPCTSTR GetPath();
	void SetName(LPCTSTR name);
	void SetPath(LPCTSTR path);
	oid_t GetObjectID() { return m_nObjectID; }
	void SetObjectID(oid_t oid) { m_nObjectID = oid; }

	enum hdf_obj_t //File access flags
	{
		HDF_LINK=0,				// Object is a symbolic link.  
		HDF_GROUP,				// Object is a group.  
		HDF_DATASET,			// Object is a dataset. 
		HDF_DATASET_ATOMIC,		// Object is an image dataset. 
		HDF_DATASET_COMPOUND,	// Object is an image dataset. 
		HDF_DATASET_IMAGE,		// Object is an image dataset. 
		HDF_TYPE				// Object is a named datatype.  
	};

protected:
	oid_t m_nObjectID; // unique identifier to the object
	int m_nType;
	char m_pPath[MAX_NAME_LEN];
	char m_pName[MAX_NAME_LEN];
	char m_pFullName[MAX_NAME_LEN];
	char m_pFilename[MAX_NAME_LEN];
	CFileFormat* m_pFile;
	CString m_ObjInfo;
	CObArray* m_pAttrList;
};

#endif // !defined(AFX_HDFOBJ_H__729F2FB9_62B7_42D7_B13C_FFBE463D4958__INCLUDED_)
