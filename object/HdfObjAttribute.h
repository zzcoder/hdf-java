// HdfObjAttribute.h: interface for the CAttribute class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJATTRIBUTE_H__7B1F617D_109C_4F12_87BF_9CB5CA7E2BEB__INCLUDED_)
#define AFX_HDFOBJATTRIBUTE_H__7B1F617D_109C_4F12_87BF_9CB5CA7E2BEB__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CAttribute : public CObject  
{
public:
	void* GetValue();
	int* GetDims() { return m_pDims; }
	void SetDims(int* dims);
	void SetRank(int rank) { m_nRank = rank; }
	int GetRank() { return m_nRank; }
	int GetValueType() { return m_nNT; }
	void SetTypeSize(int size) { m_nTypeSize = size; }
	void SetValueType(int nt) { m_nNT = nt; }
	virtual CString ValueToStr();
	void SetValue(void* value);
	LPCTSTR GetName();
	void SetName(LPCTSTR name);
	CAttribute(LPCTSTR name);
	virtual ~CAttribute();

protected:
	void* m_pValue;
	LPCTSTR m_pName;
	int m_nNT;
	int *m_pDims;
	int m_nRank;
	int m_nTypeSize;
};

#endif // !defined(AFX_HDFOBJATTRIBUTE_H__7B1F617D_109C_4F12_87BF_9CB5CA7E2BEB__INCLUDED_)
