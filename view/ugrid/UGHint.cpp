// UGHint.cpp : implementation file
//


#include "stdafx.h"

#include "UGCtrl.h"
//#include "UGHint.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif


/********************************************
*********************************************/
CUGHint::CUGHint()
{
	//alloc memory
}

/********************************************
*********************************************/
CUGHint::~CUGHint()
{
	//perform clean-up
}


/********************************************
*********************************************/
BEGIN_MESSAGE_MAP(CUGHint, CWnd)
	//{{AFX_MSG_MAP(CUGHint)
	ON_WM_PAINT()
	ON_WM_CREATE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


/********************************************
*********************************************/
void CUGHint::OnPaint() 
{
	
	CDC* dc =GetDC();

	if(m_hFont != NULL)
		dc->SelectObject(m_hFont);
	
	RECT rect;
	GetClientRect(&rect);

	dc->SetTextColor(m_textColor);
	dc->SetBkColor(m_backColor);
	
	dc->ExtTextOut(2,1,ETO_OPAQUE|ETO_CLIPPED,&rect,m_text,m_text.GetLength(),NULL);

	dc->MoveTo(0,0);
	dc->LineTo(rect.right-1,0);
	dc->LineTo(rect.right-1,rect.bottom-1);
	dc->LineTo(0,rect.bottom-1);
	dc->LineTo(0,0);

	ReleaseDC(dc);
	ValidateRect(NULL);
}

/********************************************
*********************************************/
BOOL CUGHint::PreCreateWindow(CREATESTRUCT& cs) 
{
	// TODO: Add your specialized code here and/or call the base class
	cs.dwExStyle = cs.dwExStyle | WS_EX_TOPMOST;

	return CWnd::PreCreateWindow(cs);
}


/********************************************
*********************************************/
int CUGHint::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	if (CWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	//init the variables
	m_text=_T("");						//display text
	m_textColor = RGB(0,0,0);		//text color
	m_backColor = RGB(255,255,224);	//background color
	m_windowAlign	= UG_ALIGNLEFT;	//UG_ALIGNLEFT,UG_ALIGNRIGHT,UG_ALIGNCENTER
									//UG_ALIGNTOP,UG_ALIGNBOTTOM,UG_ALIGNVCENTER
	m_textAlign		= UG_ALIGNLEFT;	//UG_ALIGNLEFT,UG_ALIGNRIGHT,UG_ALIGNCENTER
	m_hFont	= NULL;					//font handle
	
	//get the font height
	CDC * dc =GetDC();
	CSize s = dc->GetTextExtent(_T("X"),1);
	m_fontHeight = s.cy;
	ReleaseDC(dc);

	return 0;
}
/********************************************
*********************************************/
int CUGHint::SetFont(CFont * font){

	m_hFont	= font;
	
	//get the font height
	CDC * dc =GetDC();
	if(m_hFont != NULL)
		dc->SelectObject(m_hFont);
	CSize s = dc->GetTextExtent(_T("Xy"),2);
	m_fontHeight = s.cy + 3;
	ReleaseDC(dc);
	return UG_SUCCESS;
}
/********************************************
align = UG_ALIGNLEFT or UG_ALIGNRIGHT or UG_ALIGNCENTER
		+ UG_ALIGNTOP or UG_ALIGNBOTTOM or UG_ALIGNVCENTER
*********************************************/
int	CUGHint::SetWindowAlign(int align){
	m_windowAlign = align;
	return UG_SUCCESS;
}
/********************************************
align = UG_ALIGNLEFT or UG_ALIGNRIGHT or UG_ALIGNCENTER
*********************************************/
int CUGHint::SetTextAlign(int align){
	m_textAlign = align;
	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::SetTextColor(COLORREF color){
	m_textColor	= color;
	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::SetBackColor(COLORREF color){
	m_backColor	= color;
	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::SetText(LPCTSTR string,int update){
	m_text = string;
	if(update)
		Invalidate();
	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::MoveHintWindow(int x,int y,int width){
	
	RECT rect;

	//get the width of the string and reset the
	//specified width if needed
	CDC * dc =GetDC();
	// TD first select font...
	if(m_hFont != NULL)
		dc->SelectObject(m_hFont);
	CSize s = dc->GetTextExtent(m_text,m_text.GetLength());
	if((s.cx+4) > width)
		width = s.cx+4;
	ReleaseDC(dc);
	
	//set up the horizontal pos
	if(m_windowAlign&UG_ALIGNCENTER){		//center
		rect.left = x-(width/2);
		rect.right = x+width;
	}
	else if(m_windowAlign&UG_ALIGNRIGHT){	//right
		rect.left = x-width;
		rect.right = x;
	}
	else{									//left
		rect.left = x;
		rect.right = x+width;
	}

	//set up the vertical pos
	if(m_windowAlign&UG_ALIGNVCENTER){		//center
		rect.top	= y-(m_fontHeight/2);
		rect.bottom = rect.top+m_fontHeight;
	}
	else if(m_windowAlign&UG_ALIGNBOTTOM){  //bottom
		rect.top	= y-m_fontHeight;
		rect.bottom = y;
	}
	else{									//top
		rect.top = y;
		rect.bottom = y+m_fontHeight;
	}

	//make sure the position is within the parent
	RECT parentRect;
	int dif;
	GetParent()->GetClientRect(&parentRect);

	if(rect.left < 0){
		dif = 0 - rect.left;
		rect.left+=dif;
		rect.right +=dif;
	}
	if(rect.top <0){
		dif = 0 - rect.top;
		rect.top +=dif;
		rect.bottom +=dif;
	}
	if(rect.right > parentRect.right){
		dif = rect.right - parentRect.right;
		rect.right -=dif;
		rect.left -=dif;
	}
	if(rect.bottom > parentRect.bottom){
		dif = rect.bottom - parentRect.bottom;
		rect.top -= dif;
		rect.bottom -= dif;
	}

	MoveWindow(&rect,TRUE);
	SendMessage(WM_PAINT,0,0);

	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::Hide(){
	ShowWindow(SW_HIDE);
	// TD solves (workaround) grid update problem.
	m_ctrl->m_CUGGrid->Invalidate();
	return UG_SUCCESS;
}
/********************************************
*********************************************/
int CUGHint::Show(){
	if(IsWindowVisible() == FALSE)
		ShowWindow(SW_SHOW);			
	return UG_SUCCESS;
}
