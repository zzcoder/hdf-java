// HdfObjH5File.cpp: implementation of the CH5File class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH5File.h"
#include "HdfObjH5AtomicDS.h"
#include "HdfObjH5ImageDS.h"
#include "HdfObjH5CompoundDS.h"
#include <string.h>

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata)
{
	H5G_stat_t statbuf;
	info_all_t* info = (info_all_t*)opdata;

	if (H5Gget_objinfo(loc_id, name, FALSE, &statbuf) < 0)
	{
		*(info->type+info->count) = -1;
		*(info->objname+info->count) = NULL;
	} else {
		*(info->type+info->count) = statbuf.type;
		*(info->objname+info->count) = (char *)strdup(name);
	}
	info->count++;

	return 0;
}

herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type )
{
	info_all_t info;
	info.objname = objname; 
	info.type = type;
	info.count = 0;

	if(H5Giterate(loc_id, group_name, NULL, obj_info_all, (void *)&info)<0)
		return -1;

	return 0;
}


//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH5File::CH5File(LPCTSTR pFileName, UINT nFlag)
:CFileFormat(pFileName, nFlag)
{
	m_nFileType = SUPPORTED_FILE_TYPE_NCSA_HDF5;
	if (m_pFileName && IsThisType(m_pFileName))
		m_nFileID = Open(pFileName, m_nAccessFlag);

	if (m_nFileID > 0)
		m_pRootGroup = GetFileStructure();
}

CH5File::~CH5File()
{
	Close();
}

BOOL CH5File::IsThisType(LPCTSTR filename)
{
	BOOL isthistype = TRUE;

	// TODO: Add your own code here.
	if (H5Fis_hdf5(filename) <=0)
		isthistype = FALSE;

	return isthistype;
}

oid_t CH5File::Open(LPCTSTR filename, UINT nFlag)
{
	// TODO: Add your own code here.
	UINT flag = H5F_ACC_RDONLY;
	if (nFlag == FILE_ACCESS_WRITE || 
		nFlag == FILE_ACCESS_CREATE)
	{
		flag = H5F_ACC_RDWR;
	}

	return (oid_t)H5Fopen(filename, flag, H5P_DEFAULT);
}

void CH5File::Close()
{
	// TODO: Add your own code here.

	if (m_nFileID<=0)
		return;

     // close all open datasets
	int i = 0;
	hid_t fid = (hid_t)m_nFileID;
	int n = H5Fget_obj_count(fid, H5F_OBJ_DATASET);
	hid_t* ids = (hid_t *) malloc(sizeof(hid_t)*n);
	H5Fget_obj_ids(fid, H5F_OBJ_DATASET, n, ids);
	for (i=0; i<n; i++) 
	{
		try { H5Dclose(ids[i]); }
		catch (char *str) { AfxMessageBox(str); }
	}
 
	free(ids);
	n = H5Fget_obj_count(fid, H5F_OBJ_GROUP );
	ids = ids = (hid_t *) malloc(sizeof(hid_t)*n);
	H5Fget_obj_ids(fid, H5F_OBJ_GROUP , n, ids);
	for (i=0; i<n; i++) 
	{
		try { H5Gclose(ids[i]); } 
		catch (char *str) { AfxMessageBox(str); }
	}

	H5Fflush(fid, H5F_SCOPE_GLOBAL);
	H5Fclose(fid);

	//if (m_pRootGroup)
	//	m_pRootGroup->ClearDown();

	m_nFileID = -1;
}


CH5Group* CH5File::GetFileStructure()
{
	CH5Group* root = NULL;

	char *rootName = NULL;
	rootName = strrchr(m_pFileName, '\\');
	if (!rootName)
		rootName = strrchr(m_pFileName, '/');

	if (rootName)
		root = new CH5Group(this, rootName+1, "/");
	else
		root = new CH5Group(this, m_pFileName, "/");

	DepthFirst(root);

	return root;
}

/**
 * Retrieves the file structure by depth-first order, recursively.
 * The current implementation retrieves group and dataset only. It does
 * not include named datatype and soft links.
 * <p>
 * It also detects and stops loops. A loop is detected if there exists
 * object with the same object ID by tracing path back up to the root.
 * <p>
 * @param pParent the pointer to the parent group.
 */
void CH5File::DepthFirst(CH5Group* pParent)
{
	int i=0, objType=-1;
	hsize_t nMembers;
	hid_t gid, fid;
	LPTSTR objName, parentGroupFullName;
	CHObject* pHObject=NULL;

	if (!pParent)
		return;
	fid = (hid_t)m_nFileID;
	nMembers = 0;
	parentGroupFullName = NULL;


	if (pParent->IsRoot())
		parentGroupFullName = "/";
	else
	{
		i = strlen(pParent->GetPath())+strlen(pParent->GetName())+1;
		parentGroupFullName = (LPTSTR)malloc(sizeof(char)*i);
		strcpy(parentGroupFullName, pParent->GetPath()); 
		strcat(parentGroupFullName, pParent->GetName()); 
		strcat(parentGroupFullName, "/"); 
	}

	gid = H5Gopen(fid, parentGroupFullName);

	if (gid < 0)
		return;

	if (H5Gget_num_objs(gid, &nMembers) < 0)
	{
		H5Gclose(gid);
		return;
	}

	if (nMembers<=0)
	{
		H5Gclose(gid);
		return;
	}

	// since each call of H5Gget_objname_by_idx() takes about one second.
	// 1,000,000 calls take 12 days. Instead of calling it in a loop,
	// we use only one call to get all the information, which takes about
	// two seconds
	int n = (unsigned int)nMembers;
	int *objTypes = (int *)malloc(n*sizeof(int));
    char **objNames = (char **)malloc(n * sizeof (*objNames)); 
    H5Gget_obj_info_all(fid, parentGroupFullName, objNames, objTypes );

	for (i=0; i<nMembers; i++)
	{
		/* comment out for bad performance, see notes above
		objName = (LPTSTR)malloc(sizeof(char)*80);
		H5Gget_objname_by_idx(gid, i, objName, 80);
		objType = H5Gget_objtype_by_idx(gid, i);
		*/
		objName = *(objNames+i);
		objType = *(objTypes+i);

		if(objType == H5G_GROUP)
		{
			CH5Group* pGroup = new CH5Group(this, objName, parentGroupFullName);
			pGroup->SetParent(pParent);
			DepthFirst(pGroup);
			pHObject = pGroup;
		}
		else if(objType == H5G_DATASET)
		{
			// TODO to seperate differnet types of dataset

			hid_t did = H5Dopen(gid, objName);
			hid_t tid = H5Dget_type(did);
			H5T_class_t cid = H5Tget_class(tid);

			if (cid == H5T_COMPOUND)
			{
				CH5CompoundDS* pCompoundDS = new CH5CompoundDS(this, objName, parentGroupFullName);
				pHObject = pCompoundDS;
			}
			else
			{
				BOOL isImage = FALSE;

				hid_t aid = H5Aopen_name(did, "CLASS");
				if (aid >=0)
				{
					hid_t atid = H5Aget_type(aid);
					if (H5Tget_class(atid) == H5T_STRING)
					{
						int tsize = H5Tget_size(atid);
						char* buf = (char*)malloc(sizeof(char) * tsize);
						if (H5Aread(aid, atid, buf) >=0)
						{
							if (strncmp(buf, "IMAGE", 5) == 0)
								isImage = TRUE;
						}
						free(buf);
					}
				}

				H5Aclose(aid);
				if (isImage)
				{
					CH5ImageDS* pImageDS = new CH5ImageDS(this, objName, parentGroupFullName);
					pHObject = pImageDS;
				}
				else
				{
					CH5AtomicDS* pAtomicDS = new CH5AtomicDS(this, objName, parentGroupFullName);
					pHObject = pAtomicDS;
				}
			}

			H5Tclose(tid);
			H5Dclose(did);
		} //else if(objType == H5G_DATASET)

		if (pHObject)
		{
			pParent->Add(pHObject);
			oid_t oid = 0;
			H5Rcreate(&oid, gid, objName, H5R_OBJECT, -1);
			pHObject->SetObjectID(oid);
		}

	} //for (i=0; i<nMembers; i++)

	if (objTypes) free(objTypes);
	if (objNames) free(objNames);
	H5Gclose(gid);
}


CObArray* CH5File::GetAttribute(hid_t oid)
{
	CObArray* pAttributes = new CObArray;

	// TODO: Add your own code here.
	hid_t aid=-1, sid=-1, tid=-1, n=0;
	hsize_t* dims = NULL;
	int* idims = NULL;
	//char name[256];
	LPTSTR name = NULL;

	n = H5Aget_num_attrs(oid);
	for (int i=0; i<n; i++)
	{
		dims = NULL;
		idims = NULL;
		aid = H5Aopen_idx(oid, i);
		sid = H5Aget_space(aid);
		int rank = H5Sget_simple_extent_ndims(sid);
		name = (LPTSTR)malloc(256);

		if (rank <= 0)
		{
			// for scalar data, rank=0
			rank = 1;
			dims = (hsize_t*)malloc(sizeof(hsize_t));
			dims[0] = 1;
		}
		else
		{
			dims = (hsize_t*)malloc(rank*sizeof(hsize_t));
			H5Sget_simple_extent_dims(sid, dims, NULL);
		}

		H5Aget_name(aid, 256, name);

		tid = H5Aget_type(aid);
		int typeSize = H5Tget_size(tid);
		int nativeType = H5Tget_native_type(tid,H5T_DIR_ASCEND);
		CAttribute* attr = new CAttribute(name);
		attr->SetValueType(CH5AtomicDS::GetNT(nativeType));
        pAttributes->Add(attr);

		idims = (int *)malloc(rank * sizeof(int));

		for (int i=0; i<rank; i++) idims[i] = (int)dims[i];
		attr->SetDims(idims);
		attr->SetRank(rank);
		attr->SetTypeSize(typeSize);

		// retrieve the attribute value
		unsigned int lsize = 1;
		for (int j=0; j<rank; j++) lsize *= (unsigned int)dims[j];

		void* attrValue = malloc(lsize*typeSize);

		if ( H5Aread(aid,nativeType,attrValue) < 0 )
		{
			free(attrValue);
		} else
			attr->SetValue(attrValue);

		if (dims)
			free(dims);

		H5Tclose(nativeType);
		H5Tclose(tid);
		H5Sclose(sid);
		H5Aclose(aid);
	}

	return pAttributes;
}
