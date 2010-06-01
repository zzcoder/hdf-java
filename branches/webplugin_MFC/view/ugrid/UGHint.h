// UGHint.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CUGHint window

class CUGHint : public CWnd
{
// Construction
public:
	CUGHint();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CUGHint)
	protected:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CUGHint();

	// Generated message map functions
protected:
	//{{AFX_MSG(CUGHint)
	afx_msg void OnPaint();
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()


public:

	CUGCtrl *		m_ctrl;		//pointer to the main class
	CString			m_text;
	COLORREF		m_textColor;
	COLORREF		m_backColor;
	int				m_windowAlign;
	int				m_textAlign;
	CFont *			m_hFont;
	int				m_fontHeight;

	int	SetWindowAlign(int align);
	int SetTextAlign(int align);
	int SetTextColor(COLORREF color);
	int SetBackColor(COLORREF color);
	int SetFont(CFont *font);

	int SetText(LPCTSTR string,int update);
	int MoveHintWindow(int x,int y,int width);

	int Hide();
	int Show();

};

/////////////////////////////////////////////////////////////////////////////
