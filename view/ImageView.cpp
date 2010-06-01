// ImageView.cpp : implementation file
//

#include "stdafx.h"
#include "..\HDFPlugin.h"
#include "ImageView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CImageView

IMPLEMENT_DYNCREATE(CImageView, CBaseView)

CImageView::CImageView()
{
	m_pImageDS = NULL;
	m_pPalette = NULL;
	m_pImageData = NULL;
	m_sizeImage.cx = m_sizeImage.cy = 100;
}

CImageView::~CImageView()
{
}


BEGIN_MESSAGE_MAP(CImageView, CBaseView)
	//{{AFX_MSG_MAP(CImageView)
	ON_WM_SIZE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CImageView drawing

void CImageView::OnInitialUpdate()
{
	CBaseView::OnInitialUpdate();

	// TODO: calculate the total size of this view
	SetScrollSizes(MM_TEXT, m_sizeImage);
}

void CImageView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	// TODO: add draw code here

	if (!m_pPalette)
		m_pPalette = new CPalette();

	CDC dcMem;
	dcMem.CreateCompatibleDC(pDC);
	CBitmap* oldBitmap = dcMem.SelectObject(&m_bitmap);
	CPalette* oldPalette = pDC->SelectPalette( m_pPalette, FALSE );
	pDC->RealizePalette();

	pDC->BitBlt( 0, 0, m_sizeImage.cx, m_sizeImage.cy, &dcMem, 0, 0, SRCCOPY);
	pDC->SelectPalette ( oldPalette, FALSE );

	dcMem.SelectObject(oldBitmap);
	dcMem.DeleteDC();
}

/////////////////////////////////////////////////////////////////////////////
// CImageView diagnostics

#ifdef _DEBUG
void CImageView::AssertValid() const
{
	CBaseView::AssertValid();
}

void CImageView::Dump(CDumpContext& dc) const
{
	CBaseView::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CImageView message handlers

void CImageView::CreateImage()
{
	Invalidate(); // erase previous display

	// clear the old bitmap
	if (m_bitmap.m_hObject)
		m_bitmap.DeleteObject();

	CClientDC cdc(this);

    CDC memDC;
	memDC.CreateCompatibleDC(&cdc);
    m_bitmap.CreateCompatibleBitmap(&cdc, m_sizeImage.cx, m_sizeImage.cy);
    CBitmap* pOldBitmap = memDC.SelectObject(&m_bitmap);

	if (m_bIsTrueColor)
		CreateTrueColorImage(&memDC);
	else
		CreateIndexedImage(&memDC);

	memDC.SelectObject(pOldBitmap);
	memDC.DeleteDC();
}

void CImageView::SetImageDS(CImageDS *pImageDS)
{
	if (!pImageDS)
		return;

	m_pImageDS = pImageDS;
	 
	if (!m_pImageDS->ReadData())
		return;

	m_nPixels = m_pImageDS->GetNumberOfComponents();
	m_sizeImage.cx = m_pImageDS->GetWidth();
	m_sizeImage.cy = m_pImageDS->GetHeight();
	SetScrollSizes(MM_TEXT, m_sizeImage);

	if (m_sizeImage.cx<=0 || m_sizeImage.cy<=0 || m_nPixels<=0)
		return;

	m_pImageData = m_pImageDS->GetByteData();
	if (m_pImageData)
	{
		m_bIsTrueColor = m_pImageDS->IsTrueColor();
		if(m_bIsTrueColor)
		{
			m_nInterlace = m_pImageDS->GetInterlace();
		}
		else
		{
			// indexed image
			unsigned char* pal = m_pImageDS->GetPalette();
			if (!pal)
			{
				// use default gray palette
				for ( int i = 0; i < 256; i++)
					m_pR[i] = m_pG[i] = m_pB[i] = i;	
			}
			else
			{
				// use default gray palette
				for ( int i = 0; i < 256; i++)
				{
					m_pR[i] = *(pal + 3*i);
					m_pG[i] = *(pal + 3*i + 1);
					m_pB[i] = *(pal + 3*i + 2);	
				}
			}
		}
	
		CreateImage();
	} // if (m_pImageData)
}


void CImageView::CreateTrueColorImage(CDC *pDC)
{
	int i, j;

	int idx =  0;
	int size = m_sizeImage.cx*m_sizeImage.cy;
	BYTE r = 0;
	BYTE g = 0;
	BYTE b = 0;

	if (m_nInterlace == CImageDS::IMAGE_INTERLACE_PIXEL)
	{
		for (i = 0; i < m_sizeImage.cy; i++ ) 
		{
			for ( j = 0; j < m_sizeImage.cx; j++ ) 
			{
				r = m_pImageData[idx];
				g = m_pImageData[idx+1];
				b = m_pImageData[idx+2];
				pDC->SetPixel(j, i, RGB( r, g, b ) );
				idx +=3;
			}
		}
	} else
	{
		for (i = 0; i < m_sizeImage.cy; i++ ) 
		{
			for ( j = 0; j < m_sizeImage.cx; j++ ) 
			{
				r = m_pImageData[idx];
				g = m_pImageData[idx+size];
				b = m_pImageData[idx+2*size];
				pDC->SetPixel(j, i, RGB( r, g, b ) );
				idx++;
			}
		}
	}
}

void CImageView::CreateIndexedImage(CDC *pDC)
{
	int i, j;
	int nColors = 256;
	UINT nSize = sizeof(LOGPALETTE) + (sizeof(PALETTEENTRY) * nColors);
	LOGPALETTE *pLP = (LOGPALETTE *) new BYTE[nSize];
	pLP->palVersion = 0x300;
	pLP->palNumEntries = nColors;

	for(i=0; i < nColors; i++)
	{
		pLP->palPalEntry[i].peRed = m_pR[i];
		pLP->palPalEntry[i].peGreen = m_pG[i];
		pLP->palPalEntry[i].peBlue = m_pB[i];
		pLP->palPalEntry[i].peFlags = 0;
	}

	m_pPalette = new CPalette();
	VERIFY(m_pPalette->CreatePalette( pLP ));
	delete[] pLP;

	// Select and realize the palette
	CPalette* pOldPal = pDC->SelectPalette(m_pPalette, TRUE);
	pDC->RealizePalette();

	int idx_data =  0;
	int idx_pixel = 0;
	BYTE r = 0;
	BYTE g = 0;
	BYTE b = 0;
	for (i = 0; i < m_sizeImage.cy; i++ ) 
	{
		for ( j = 0; j < m_sizeImage.cx; j++ ) 
		{
			idx_pixel = m_pImageData[idx_data];
			r = m_pR[idx_pixel];
			g = m_pG[idx_pixel];
			b = m_pB[idx_pixel];
			pDC->SetPixel(j, i, RGB( r, g, b ) );
			idx_data++;
		}
	}

	// Back to old palette
	pDC->SelectPalette(pOldPal, TRUE);
	pDC->RealizePalette();

}

CImageDS* CImageView::GetImageDS()
{
	return m_pImageDS;
}

void CImageView::OnSize(UINT nType, int cx, int cy) 
{
	CBaseView::OnSize(nType, cx, cy);
	
	// TODO: Add your message handler code here
	
}

