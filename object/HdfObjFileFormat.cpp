// HdfObjFileFormat.cpp: implementation of the CFileFormat class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HdfObjFileFormat.h"

#include "HdfObjH5File.h"
#include "HdfObjH4File.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CFileFormat::CFileFormat(LPCTSTR pFileName, UINT nFlag)
{
	m_pFileName = pFileName;
	m_nAccessFlag = nFlag;
	m_nFileID = -1;
	m_pRootGroup = NULL;
	m_nFileType = SUPPORTED_FILE_TYPE_UNKNOWN;
}

CFileFormat::~CFileFormat()
{

}


CFileFormat* CFileFormat::CreateInstance(LPCTSTR pFilename)
{
	CFileFormat* pFileformat;
	CH5File* h5File;
	CH4File* h4File;

	if (pFilename == NULL || strstr(pFilename, "Initilization_SetRegistry_For_HDFPlugin"))
		return NULL;

	h5File = new CH5File(NULL, -1);
	h4File = new CH4File(NULL, -1);
	if (h5File->IsThisType(pFilename))
	{
		pFileformat = new CH5File(pFilename, CH5File::FILE_ACCESS_READ);
	}
	else if (h4File->IsThisType(pFilename))
	{
		pFileformat = new CH4File(pFilename, CH5File::FILE_ACCESS_READ);
	}
	else
	{
		// unsupported file format
		AfxMessageBox("File format is not supported.", MB_ICONEXCLAMATION); 
		pFileformat = NULL;
	}

	delete h5File;
	delete h4File;

	return pFileformat;
}
