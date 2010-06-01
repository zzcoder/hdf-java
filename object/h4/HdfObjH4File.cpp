// HdfObjH4File.cpp: implementation of the CH4File class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH4File.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH4File::CH4File(LPCTSTR pFileName, UINT nFlag)
:CFileFormat(pFileName, nFlag)
{
	m_nFileType = SUPPORTED_FILE_TYPE_NCSA_HDF4;
	m_nFileID = -1;
    m_grid = -1;
    m_sdid = -1;

	if (m_pFileName && IsThisType(m_pFileName))
		m_nFileID = Open(pFileName, m_nAccessFlag);

	if (m_nFileID > 0)
		m_pRootGroup = GetFileStructure();
}

CH4File::~CH4File()
{
	Close();
}

BOOL CH4File::IsThisType(LPCTSTR filename)
{
	BOOL isthistype = TRUE;

	// TODO: Add your own code here.
	isthistype = Hishdf(filename);

	return isthistype;
}

oid_t CH4File::Open(LPCTSTR filename, UINT nFlag)
{
	int fid = -1;

	// TODO: Add your own code here.
	m_nFlag = DFACC_READ;
	if (nFlag == FILE_ACCESS_WRITE || 
		nFlag == FILE_ACCESS_CREATE)
	{
		m_nFlag = DFACC_WRITE;
	}

	fid = Hopen(filename, m_nFlag, 0); 
	if (fid >=0)
	{
		Vstart(fid);
        m_grid = GRstart(fid);
        m_sdid = SDstart(filename, m_nFlag);
	}

	return (oid_t)fid;
}

void CH4File::Close()
{
	if (m_nFileID<=0)
		return;

    GRend(m_grid);
    SDend(m_sdid);
    Vend((int32)m_nFileID);
    Hclose((int32)m_nFileID);

	//if (m_pRootGroup)
	//	m_pRootGroup->ClearDown();

    m_nFileID = -1;
}

CH4Group* CH4File::GetFileStructure()
{
	CH4Group* root = NULL;
	int32 fid=-1, ref=-1;
	int32* refs = NULL;
	int32 n=-1, i=0;

	if (m_nFileID < 0)
		return NULL;
	else
		fid = (int32)m_nFileID;

	char *rootName = NULL;
	rootName = strrchr(m_pFileName, '\\');
	if (!rootName)
		rootName = strrchr(m_pFileName, '/');

	if (rootName)
		root = new CH4Group(this, rootName+1, "/");
	else
		root = new CH4Group(this, m_pFileName, "/");

	// get top level VGroup
	n = Vlone(fid, NULL, 0);

	if (n>0)
	{
		refs = (int32*) malloc(n*sizeof (int32));
		Vlone(fid, refs, n);
		for (i=0; i < n; i++)
		{
			ref = refs[i];
			CH4Group* g = GetVGroup(ref, "/", root);

			if (g)
			{
				root->Add(g);
				DepthFirst(g);
			}
		}
		free(refs);
	}

	// get the top level GR images
	int32 n_attrs;
	if ( GRfileinfo( m_grid, &n, &n_attrs ) != FAIL )
	{
		for (int i=0; i<n; i++)
		{
			CH4ImageDS* gr = GetGRImage(i, "/");
			if (gr) root->Add(gr);
		}
	} 

	// get top level SDS
	if (SDfileinfo( m_sdid, &n, &n_attrs) != FAIL )
	{
		for (int i=0; i<n; i++)
		{
			CH4AtomicDS* sds = GetSDS(i, "/");
			if (sds) root->Add(sds);
		}
	}

	// get top level VData
	n = VSlone( fid, NULL, 0 );
	if (n > 0)
	{
		refs = (int32*) malloc(n*sizeof(int32));
		VSlone(fid, refs, n);
  
        for (int i=0; i<n; i++)
        {
			ref = refs[i];
			CH4CompoundDS* vdata = GetVData(ref, "/");
			if (vdata) root->Add(vdata);
        }
		free(refs);
	}


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
void CH4File::DepthFirst(CH4Group* pParent)
{
	if (pParent == NULL)
		return;

	int32 fid = (int32)m_nFileID;
	int32 vgroup_ref = (int32)pParent->GetObjectID();
    int32 vgroup_id = Vattach( fid, vgroup_ref, "r" );

	if (vgroup_id == FAIL)
	{
		Vdetach(vgroup_id);
		return;
	}

	int32 n = Vntagrefs(vgroup_id);
	if (n<=0)
	{
		Vdetach(vgroup_id);
		return;
	}

	LPTSTR fullPath;
	CHObject* pHObject=NULL;
	fullPath = NULL;
	if (pParent->IsRoot())
		fullPath = "/";
	else
	{
		int len = strlen(pParent->GetPath())+strlen(pParent->GetName())+1;
		fullPath = (LPTSTR)malloc(sizeof(char)*len);
		strcpy(fullPath, pParent->GetPath()); 
		strcat(fullPath, pParent->GetName()); 
		strcat(fullPath, "/"); 
	}

	int32* tags = new int32[n];
	int32* refs = new int32[n];
	int npairs = Vgettagrefs(vgroup_id, tags, refs, n);
	int32 tag=-1, ref=-1, index=-1;

	for ( int i = 0; i < n; i++)
	{
		tag = tags[i];
		ref = refs[i];
		CH4ImageDS* gr = NULL;
		CH4AtomicDS* sds = NULL;
		CH4CompoundDS* vdata = NULL;
		CH4Group* vgroup = NULL;

		switch (tag)
		{
			case DFTAG_RIG:
			case DFTAG_RI:
			case DFTAG_RI8:
				index = GRreftoindex(m_grid, (uint16)ref);
				gr = GetGRImage(index, fullPath);
				if (gr) pParent->Add(gr);
				break;
			case DFTAG_SD:
			case DFTAG_SDG:
			case DFTAG_NDG:
				index = SDreftoindex(m_sdid, ref);
				sds = GetSDS(index, fullPath);
				if (sds) pParent->Add(sds);
				break;
			case DFTAG_VH:
			case DFTAG_VS:
				vdata = GetVData(ref, fullPath);
				if (vdata) pParent->Add(vdata);
				break;
			case DFTAG_VG:
				vgroup = GetVGroup(ref, fullPath, pParent);
				if (vgroup)
				{
					pParent->Add(vgroup);
					// check if there is a loop 
					BOOL isLooped = FALSE;
					CGroup* theGroup = pParent;
					while (theGroup && !isLooped)
					{
						if ((int32)(theGroup->GetObjectID()) == ref)
							isLooped = TRUE;
						else
							theGroup = theGroup->GetParent();
					}
					if (!isLooped)
						DepthFirst(vgroup);
				}
				break;
			default:
				break;
		} //switch (tag)

	} //for (int i=0; i<nelms; i++)

	delete tags;
	delete refs;
	Vdetach(vgroup_id);
}

CH4Group* CH4File::GetVGroup(int32 vgroup_ref, const char* path, CH4Group* pParent)
{
	CH4Group* vgroup  = NULL;

	if (FindVGroup(vgroup_ref))
		return NULL;

    int32 vgroup_id = Vattach( (int32)m_nFileID, vgroup_ref, "r" );
    
	if ( vgroup_id == FAIL )
	{
		Vdetach(vgroup_id);
		return NULL;
	}

	LPTSTR vg_name = (LPTSTR)malloc(MAX_NC_NAME);
	LPTSTR vg_class = (LPTSTR)malloc(MAX_NC_NAME);
    Vgetname( vgroup_id, vg_name );
	Vgetclass(vgroup_id, vg_class);


	// ignore the Vgroups created by the GR interface
	if ( IsReservedName(vg_class) || IsReservedName(vg_name) )
	{
		vgroup  = NULL;
	}
	else
	{
		vgroup = new CH4Group( this, vg_name, path);
		vgroup->SetParent(pParent);
		vgroup->SetObjectID(vgroup_ref);
		m_arrayVGroup.Add(vgroup_ref);
	}

	free(vg_class);
	Vdetach(vgroup_id);

	return vgroup;
}

CH4CompoundDS* CH4File::GetVData(int32 ref, const char* path)
{
	CH4CompoundDS* vdata  = NULL;

	if (FindVData(ref))
		return NULL;

    int32 vdata_id = VSattach( (int32)m_nFileID, ref, "r" );
    
	if ( vdata_id == FAIL )
	{
		VSdetach(vdata_id);
		return NULL;
	}

	LPTSTR vs_name = (LPTSTR)malloc(MAX_NC_NAME);
	LPTSTR vg_class = (LPTSTR)malloc(MAX_NC_NAME);

    VSgetname( vdata_id, vs_name );
	VSgetclass(vdata_id, vg_class);


	// ignore the Vgroups created by the GR interface
	if ( IsReservedName(vg_class) || IsReservedName(vs_name) )
	{
		vdata  = NULL;;
	}
	else
	{
		vdata = new CH4CompoundDS( this, vs_name, path);
		vdata->SetObjectID(ref);
		m_arrayVData.Add(ref);
	}

	VSdetach(vdata_id);

	return vdata;
}


CH4ImageDS* CH4File::GetGRImage(int index, const char* path)
{
	CH4ImageDS* gr = NULL;

	int32 ri_id = GRselect( m_grid, index );
	if (ri_id == FAIL) return NULL;

	uint16 ref = GRidtoref(ri_id);
	if (FindGRImage(ref))
		return NULL;

	LPTSTR gr_name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 ncomp;
	int32 data_type;
	int32 interlace_mode;
	int32 dim_sizes[] = {0, 0};
	int32 num_attrs;
	intn ret = GRgetiminfo(ri_id, gr_name, &ncomp, &data_type, &interlace_mode, dim_sizes, &num_attrs); 

	if (ret == FAIL)
		return NULL;

	gr = new CH4ImageDS(this, gr_name, path);
	gr->SetObjectID(ref);
	m_arrayGRImage.Add(ref);

	return gr;
}

CH4AtomicDS* CH4File::GetSDS(int index, const char* path)
{
	CH4AtomicDS* sds = NULL;

	int32 sd_id = SDselect( m_sdid, index );
	if (sd_id == FAIL) return NULL;

	int32 ref = SDidtoref(sd_id);
	if (FindSDS(ref))
		return NULL;

	LPTSTR sd_name = (LPTSTR)malloc(MAX_NC_NAME);
	int32 rank;
	int32 data_type;
	int32 dim_sizes[20];
	int32 num_attrs;
	intn ret = SDgetinfo(sd_id, sd_name, &rank, dim_sizes, &data_type, &num_attrs); 
	SDendaccess(sd_id);

	if (ret == FAIL)
		return NULL;

	sds = new CH4AtomicDS(this, sd_name, path);
	sds->SetObjectID(ref);
	m_arraySDS.Add(ref);

	return sds;
}

BOOL CH4File::FindVGroup(int ref)
{
	int n = m_arrayVGroup.GetSize();
	for (int i=0; i<n; i++)
	{
		if (m_arrayVGroup.GetAt(i)==(UINT)ref)
			return TRUE;
	}

	return FALSE;
}

BOOL CH4File::FindGRImage(int ref)
{
	int n = m_arrayGRImage.GetSize();
	for (int i=0; i<n; i++)
	{
		if (m_arrayGRImage.GetAt(i)==(UINT)ref)
			return TRUE;
	}

	return FALSE;
}

BOOL CH4File::FindSDS(int ref)
{
	int n = m_arraySDS.GetSize();
	for (int i=0; i<n; i++)
	{
		if (m_arraySDS.GetAt(i)==(UINT)ref)
			return TRUE;
	}

	return FALSE;
}

BOOL CH4File::FindVData(int ref)
{
	int n = m_arrayVData.GetSize();
	for (int i=0; i<n; i++)
	{
		if (m_arrayVData.GetAt(i)==(UINT)ref)
			return TRUE;
	}

	return FALSE;
}

BOOL CH4File::IsReservedName( CString name ) 
{
	BOOL ret = FALSE;
 
	if ( 
		(name.Find(GR_NAME) !=-1)  ||
		(name.Find(RI_NAME) !=-1)  ||
		(name.Find(RIGATTRNAME) !=-1)  ||
		(name.Find(RIGATTRCLASS) !=-1)  ||
		(name.Find(_HDF_ATTRIBUTE) !=-1)  ||
		(name.Find(_HDF_VARIABLE) !=-1)  ||
		(name.Find(_HDF_DIMENSION) !=-1)  ||
		(name.Find(_HDF_UDIMENSION) !=-1)  ||
		(name.Find(DIM_VALS) !=-1)  ||
		(name.Find(DIM_VALS01) !=-1)  ||
		(name.Find(_HDF_CDF) !=-1)  ||
		(name.Find(DATA0) !=-1)  ||
		(name.Find(ATTR_FIELD_NAME) !=-1)  ||
		(name.Find("_HDF_CHK_TBL_") !=-1)  
	   )
	{
 		ret =  TRUE;
	}
 
	return ret;
} 