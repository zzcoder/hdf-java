// HdfObjH5File.h: interface for the CH5File class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH5FILE_H__D63E5904_4C13_43FD_AF9A_432F46813FF3__INCLUDED_)
#define AFX_HDFOBJH5FILE_H__D63E5904_4C13_43FD_AF9A_432F46813FF3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjFileFormat.h"
#include "HdfObjH5Group.h"
#include <hdf5.h>
#include "HdfObjAttribute.h"	// Added by ClassView

#ifdef __cplusplus
herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata);
herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type );
#else
static herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata);
static herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type );
#endif

typedef struct info_all 
{
	char **objname; 
	int *type;
	int count;
} info_all_t;



class CH5File : public CFileFormat  
{
public:
	static CObArray* GetAttribute(hid_t oid);
	BOOL IsThisType(LPCTSTR filename);			// override CHObject::IsThisType(LPCTSTR)

	CH5File(LPCTSTR pFileName, UINT nFlag);
	virtual ~CH5File();
	oid_t Open(LPCTSTR filename, UINT nFlag);
	void Close();

private:
	CH5Group* GetFileStructure();
	void DepthFirst(CH5Group* pParent);
};

#endif // !defined(AFX_HDFOBJH5FILE_H__D63E5904_4C13_43FD_AF9A_432F46813FF3__INCLUDED_)
