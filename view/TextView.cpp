// TextView.cpp : implementation file
//

#include "stdafx.h"
#include "..\HDFPlugin.h"
#include "TextView.h"
#include "HdfObjAttribute.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CTextView

IMPLEMENT_DYNCREATE(CTextView, CRichEditView)

CTextView::CTextView()
{
	m_pObj = NULL;
}

CTextView::~CTextView()
{
}


BEGIN_MESSAGE_MAP(CTextView, CRichEditView)
	//{{AFX_MSG_MAP(CTextView)
		// NOTE - the ClassWizard will add and remove mapping macros here.
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CTextView drawing

void CTextView::OnInitialUpdate()
{
	CRichEditView::OnInitialUpdate();

	CRichEditCtrl& txtCtrl = GetRichEditCtrl();
	txtCtrl.SetReadOnly();

	//CString test;
	//test="{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang2057{\\fonttbl{\\f0\froman\\fprq2\\fcharset0 Times New Roman;}{\\f1\\fswiss\\fprq2\\fcharset0 System;}}{\\colortbl ;\\red255\\green0\\blue0;\\red51\\green153\\blue102;\\red0\\green0\\blue255;}\\viewkind4\\uc1\\pard\\cf1\\i\\f0\\fs24 Inputted\\cf0\\i0  \\cf2\\b rich\\cf0\\b0  \\cf3 text\\cf0 !\\b\\f1\\fs20 \\par }";
	//test = "\ntest of attribute view\nline 1\nline2";
	//Readin(test);
}

void CTextView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	// TODO: add draw code here
}


void CTextView::SetObj(CHObject* pObj) 
{
	if (pObj==NULL || 
		pObj == m_pObj)
		return;

	CString info = pObj->GetObjInfo();

	CObArray* attrList = pObj->ReadAttributes();
	CString tmpStr;
	if (attrList && attrList->GetSize() > 0)
	{
		int n = attrList->GetSize();
		tmpStr.Format("%d", n);
		info += "\n\nNumber of attributes = ";
		info += tmpStr;
		info +="\n";
		info += TABLE_LINE;

		CAttribute* attr;
		CString attrValue;
		for(int i=0; i<n; i++)
		{
			attr = (CAttribute*)attrList->GetAt(i);
			attrValue = attr->ValueToStr();
			if (attrValue.GetLength() <=0)
				continue;

			info +="\n";

			info += attr->GetName();
			info += " = ";
			info += attrValue;
		}
	}
	else
		info += "\n\nNumber of attributes = 0";

	Readin(info);
}

void CTextView::Readout(CString sReadText) 
{
	EDITSTREAM es;

	es.dwCookie = (DWORD)&sReadText; // Pass a pointer to the CString to the callback function 
	es.pfnCallback = MEditStreamOutCallback; // Specify the pointer to the callback function.

	CRichEditCtrl& txtCtrl = GetRichEditCtrl();
	//txtCtrl.StreamOut(SF_RTF,es); // Perform the streaming
	txtCtrl.StreamOut(SF_TEXT,es); // Perform the streaming
}

void CTextView::Readin(CString sWriteText) 
{
	DeleteContents(); // clean the old content

	// This is hard-coded for example purposes. It is likely this would
	// be read from file or another source.
	EDITSTREAM es;
	es.dwCookie = (DWORD)&sWriteText; // Pass a pointer to the CString to the callback function
	es.pfnCallback = MEditStreamInCallback; // Specify the pointer to the callback function

	CRichEditCtrl& txtCtrl = GetRichEditCtrl();
	//txtCtrl.StreamIn(SF_RTF,es); // Perform the streaming
	txtCtrl.StreamIn(SF_TEXT,es); // Perform the streaming

}

/////////////////////////////////////////////////////////////////////////////
// CTextView diagnostics

#ifdef _DEBUG
void CTextView::AssertValid() const
{
	CRichEditView::AssertValid();
}

void CTextView::Dump(CDumpContext& dc) const
{
	CRichEditView::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CTextView message handlers

DWORD __stdcall MEditStreamInCallback(DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb)
{
	CString *psBuffer = (CString *)dwCookie;

	if (cb > psBuffer->GetLength()) cb = psBuffer->GetLength();

	for (int i=0;i<cb;i++) {
		*(pbBuff+i) = psBuffer->GetAt(i);
	}

	*pcb = cb;

	*psBuffer = psBuffer->Mid(cb);

	return 0;
}

DWORD __stdcall MEditStreamOutCallback(DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb)
{
	CString sThisWrite;
	sThisWrite.GetBufferSetLength(cb);

	CString *psBuffer = (CString *)dwCookie;
	
	for (int i=0;i<cb;i++) {
		sThisWrite.SetAt(i,*(pbBuff+i));
	}

	*psBuffer += sThisWrite;

	*pcb = sThisWrite.GetLength();
	sThisWrite.ReleaseBuffer();
	return 0;
}


