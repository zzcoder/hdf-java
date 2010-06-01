// HdfObjH5CompoundDS.cpp: implementation of the CH5CompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "HDFPlugin.h"
#include "HdfObjH5CompoundDS.h"
#include "HdfObjH5AtomicDS.h"
#include "HdfObjH5File.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CH5CompoundDS::CH5CompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path)
:CCompoundDS(fileformat, name, path)
{

}

CH5CompoundDS::~CH5CompoundDS()
{
	if (m_pData) free ( m_pData );
	if (m_pDims) free (m_pDims);
	if (m_pCount) free (m_pCount);
	if (m_pStart) free (m_pStart);
	if (m_pStride) free (m_pStride);
	H5Tclose(m_nType);
}

CObArray* CH5CompoundDS::ReadAttributes()
{
	if (m_pAttrList)
		return m_pAttrList;

	hid_t did, fid;
	fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);

	m_pAttrList = CH5File::GetAttribute(did);

	H5Dclose(did);

	return m_pAttrList;
}

void CH5CompoundDS::Init()
{
	hid_t did=-1, sid=-1, tid;
	hsize_t* dims;
	int i;

	// TODO: Add your own code here.

	hid_t fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);
	sid = H5Dget_space(did );
	tid = H5Dget_type(did );
	m_nType = h5tools_fixtype(tid );

	m_nRecords = 1;
	m_nRank = H5Sget_simple_extent_ndims(sid);
	if (m_nRank>0)
	{
		dims = (hsize_t*)malloc(m_nRank*sizeof(hsize_t));
		H5Sget_simple_extent_dims(sid, dims,NULL ) ;

		m_pDims = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pCount = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pStart = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));
		m_pStride = (unsigned long *)malloc(m_nRank*sizeof(unsigned long));

		for (i=0; i<m_nRank; i++)
		{
			m_pDims[i] = (unsigned long)dims[i];
			m_pCount[i] = 1;
			m_pStart[i] = 0;
			m_pStride[i] = 1;
		}

		// only dsiplay 1D
		m_pCount[0] = m_pDims[0];
		m_nHeight = m_pCount[0];
		m_nHeightIdx = 0;
		m_nRecords = m_nHeight;
		m_nWidth = H5Tget_nmembers(m_nType);
		m_nMembers = m_nWidth;
		m_nTypeSize = H5Tget_size(m_nType);

		m_pMemberOffset = (size_t *)malloc(m_nMembers*sizeof(size_t));
		m_pMemberNames = (char**)malloc(m_nMembers*sizeof(*m_pMemberNames));
		m_pMemberTypes = (int *)malloc(m_nMembers*sizeof(int));
		for (i=0; i<m_nMembers; i++)
		{
			m_pMemberNames[i] = H5Tget_member_name(m_nType, i);
			m_pMemberTypes[i]= H5Tget_member_type(m_nType, i);
			m_pMemberOffset[i] = H5Tget_member_offset(m_nType, i);
		}
	}


	free(dims);
	H5Tclose(tid);
	H5Sclose(sid);
	H5Dclose(did);

}

void* CH5CompoundDS::ReadData()
{
	hid_t did=-1, msid=-1, fsid=-1, fid=-1;

	if (m_pData)
		return m_pData; // data is loaded

	if (m_nRank < 0)
		Init();

	fid = (hid_t)m_pFile->GetFileID();
	did = H5Dopen(fid, m_pFullName);
	msid = fsid = H5S_ALL;

	m_nDataPoints = m_nRecords;
	if (m_nRank > 0)
	{
		hsize_t mdims[1];
		mdims[0] = m_nDataPoints;
		msid = H5Screate_simple(1, mdims, NULL);
		hssize_t *start;
		hsize_t *count;

		start = (hssize_t *)malloc(m_nRank*sizeof(hssize_t));
		count = (hsize_t *)malloc(m_nRank*sizeof(hsize_t));

		for (int i=0; i<m_nRank; i++)
		{
			start[i] = m_pStart[i];
			count[i] = m_pCount[i];
		}

		fsid = H5Dget_space(did);
		H5Sselect_hyperslab(fsid, H5S_SELECT_SET, start, NULL, count, NULL);

		free(start);
		free(count);
	}

    m_pData = malloc(m_nDataPoints*m_nTypeSize);
	if (m_pData)
	{
		herr_t status =  H5Dread (did, m_nType, msid, fsid, H5P_DEFAULT, m_pData);
		if (status < 0)
		{
			free(m_pData);
			m_pData = NULL;
		}
	}

	H5Sclose(msid);
	H5Sclose(fsid);
	H5Dclose(did);

	return m_pData;

}

CString CH5CompoundDS::ValueToStr(int row, int col, CString strValue)
{
	h5tools_str_t buffer;
	unsigned char *vp=NULL;
	h5dump_t info;
	h5tools_context_t ctx;
	strValue.Empty();

	memset(&buffer, 0, sizeof(h5tools_str_t));
	memset(&info, 0, sizeof info);
	memset(&ctx, 0, sizeof(ctx));
	vp = (unsigned char*)m_pData;

	size_t offset=0;
	offset += row*m_nTypeSize;
	offset += m_pMemberOffset[col];

	h5tools_str_reset(&buffer);
	h5tools_str_sprint(&buffer, &info, 0, m_nType, vp + offset, &ctx, col);

	strValue += h5tools_str_fmt(&buffer, 0, "%s");
	h5tools_str_close(&buffer);

	return strValue;
}

CString CH5CompoundDS::GetDatatypeInfo()
{
	CString desc;

	desc += TABLE_LINE;
	for (int i=0; i<m_nMembers; i++)
	{
		desc += "\n\t";
		desc += m_pMemberNames[i];
		desc += ",\t";
		desc += GetDatatypeInfo(m_pMemberTypes[i]);
	}

	return desc;
}

CString CH5CompoundDS::GetDatatypeInfo(int tid)
{
	CString desc;
	int tclass = H5Tget_class(tid);
	
	switch (tclass)
	{
	case H5T_INTEGER:
	case H5T_FLOAT:
		desc += CH5AtomicDS::GetDatatypeInfo(CH5AtomicDS::GetNT(tid));
		break;
	case H5T_COMPOUND:
		desc += "Compound";
		break;
	case H5T_TIME:
		desc += "Time";
		break;
	case H5T_STRING:
		desc += "String";
		break;
	case H5T_BITFIELD:
		desc += "Bitfield";
		break;
	case H5T_OPAQUE:
		desc += "Opaque";
		break;
	case H5T_REFERENCE:
		desc += "Reference";
		break;
	case H5T_ENUM:
		desc += "Enum";
		break;
	case H5T_VLEN:
		desc += "Variable length";
		break;
	case H5T_ARRAY:
		desc += "Array of ";
		desc += GetDatatypeInfo(H5Tget_super(tid));
		break;
	default:
		desc = "Unknown";
	}
	return desc;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_sprint
*
* Purpose: Renders the value pointed to by VP of type TYPE into variable
*  length string STR.
*
* Return: A pointer to memory containing the result or NULL on error.
*
* Programmer: Robb Matzke
*              Thursday, July 23, 1998
*
* Modifications:
*   Robb Matzke, 1999-04-26
*  Made this function safe from overflow problems by allowing it
*  to reallocate the output string.
*
*   Robb Matzke, 1999-06-04
*  Added support for object references. The new `container'
*  argument is the dataset where the reference came from.
*
*   Robb Matzke, 1999-06-07
*  Added support for printing raw data. If info->raw is non-zero
*  then data is printed in hexadecimal format.
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_sprint(h5tools_str_t *str, const h5dump_t *info, hid_t container,
                   hid_t type, void *vp, h5tools_context_t *ctx,
																			int column )
{
 size_t         n, offset, size, nelmts, start;
 char           quote = '\0';
 unsigned char *ucp_vp = (unsigned char *)vp;
 char          *cp_vp = (char *)vp;
 hid_t          memb;
 static char    fmt_llong[8], fmt_ullong[8];
 H5T_str_t      pad;
	char           *name;
	int            nmembs;
#if 0
	int            otype;
 hid_t          obj, region;
 H5G_stat_t     sb;
#endif

	/*display 1D compound data in columns */
	int display_cols=(column==-1)? 0 : 1;
 
 /*
 * some tempvars to store the value before we append it to the string to
 * get rid of the memory alignment problem
 */
 double             tempdouble;
 float              tempfloat;
 unsigned long_long tempullong;
 long_long          templlong;
 unsigned long      tempulong;
 long               templong;
 unsigned int       tempuint;
 int                tempint;
 unsigned short     tempushort;
 short              tempshort;
 
 /* Build default formats for long long types */
 if (!fmt_llong[0]) {
  sprintf(fmt_llong, "%%%sd", H5_PRINTF_LL_WIDTH);
  sprintf(fmt_ullong, "%%%su", H5_PRINTF_LL_WIDTH);
 }
 
 /* Append value depending on data type */
 start = h5tools_str_len(str);
 
 if (info->raw) {
  size_t i;
  
  h5tools_str_append(str, "0x");
  n = H5Tget_size(type);
  
  for (i = 0; i < n; i++)
   h5tools_str_append(str, OPT(info->fmt_raw, "%02x"), ucp_vp[i]);
 } else if (H5Tequal(type, H5T_NATIVE_FLOAT)) {
  memcpy(&tempfloat, vp, sizeof(float)); 
  h5tools_str_append(str, OPT(info->fmt_float, "%g"), tempfloat);
 } else if (H5Tequal(type, H5T_NATIVE_DOUBLE)) {
  memcpy(&tempdouble, vp, sizeof(double)); 
  h5tools_str_append(str, OPT(info->fmt_double, "%f"), tempdouble);
 } else if (info->ascii && (H5Tequal(type, H5T_NATIVE_SCHAR) ||
  H5Tequal(type, H5T_NATIVE_UCHAR))) {
  h5tools_print_char(str, info, (unsigned char)(*ucp_vp));
 } else if (H5T_STRING == H5Tget_class(type)) {
  unsigned int i;
  
  quote = '\0';
  if(H5Tis_variable_str(type)) {
   size = strlen(cp_vp);//size = HDstrlen(cp_vp);
  } else {
   size = H5Tget_size(type);
  }
  pad = H5Tget_strpad(type);
  
  for (i = 0; i < size && (cp_vp[i] != '\0' || pad != H5T_STR_NULLTERM); i++) {
   int j = 1;
   
   /*
   * Count how many times the next character repeats. If the
   * threshold is zero then that means it can repeat any number
   * of times.
   */
   if (info->str_repeat > 0)
    while (i + j < size && cp_vp[i] == cp_vp[i + j])
     j++;
    
     /*
     * Print the opening quote.  If the repeat count is high enough to
     * warrant printing the number of repeats instead of enumerating
     * the characters, then make sure the character to be repeated is
     * in it's own quote.
    */
#if defined (QUOTE)
    if (info->str_repeat > 0 && j > info->str_repeat) {
     if (quote)
      h5tools_str_append(str, "%c", quote);
     
     quote = '\'';
     h5tools_str_append(str, "%s%c", i ? " " : "", quote);
    } else if (!quote) {
     quote = '"';
     h5tools_str_append(str, "%s%c", i ? " " : "", quote);
    }
#endif
    
    /* Print the character */
    h5tools_print_char(str, info, (unsigned char)(ucp_vp[i]));
    
    /* Print the repeat count */
    if (info->str_repeat && j > info->str_repeat) {
#ifdef REPEAT_VERBOSE
     h5tools_str_append(str, "%c repeats %d times", quote, j - 1);
#else
     h5tools_str_append(str, "%c*%d", quote, j - 1);
#endif  /* REPEAT_VERBOSE */
     quote = '\0';
     i += j - 1;
    }
    
  }
  
  if (quote)
   h5tools_str_append(str, "%c", quote);
  
  if (i == 0)
   /*empty string*/
   h5tools_str_append(str, "\"\"");
 } else if (H5Tequal(type, H5T_NATIVE_INT)) {
  memcpy(&tempint, vp, sizeof(int));
  h5tools_str_append(str, OPT(info->fmt_int, "%d"), tempint);
 } else if (H5Tequal(type, H5T_NATIVE_UINT)) {
  memcpy(&tempuint, vp, sizeof(unsigned int));
  h5tools_str_append(str, OPT(info->fmt_uint, "%u"), tempuint);
 } else if (H5Tequal(type, H5T_NATIVE_SCHAR)) {
  h5tools_str_append(str, OPT(info->fmt_schar, "%d"), *cp_vp);
 } else if (H5Tequal(type, H5T_NATIVE_UCHAR)) {
  h5tools_str_append(str, OPT(info->fmt_uchar, "%u"), *ucp_vp);
 } else if (H5Tequal(type, H5T_NATIVE_SHORT)) {
  memcpy(&tempshort, vp, sizeof(short));
  h5tools_str_append(str, OPT(info->fmt_short, "%d"), tempshort);
 } else if (H5Tequal(type, H5T_NATIVE_USHORT)) {
  memcpy(&tempushort, vp, sizeof(unsigned short));
  h5tools_str_append(str, OPT(info->fmt_ushort, "%u"), tempushort);
 } else if (H5Tequal(type, H5T_NATIVE_LONG)) {
  memcpy(&templong, vp, sizeof(long));
  h5tools_str_append(str, OPT(info->fmt_long, "%ld"), templong);
 } else if (H5Tequal(type, H5T_NATIVE_ULONG)) {
  memcpy(&tempulong, vp, sizeof(unsigned long));
  h5tools_str_append(str, OPT(info->fmt_ulong, "%lu"), tempulong);
 } else if (H5Tequal(type, H5T_NATIVE_LLONG)) {
  memcpy(&templlong, vp, sizeof(long_long));
  h5tools_str_append(str, OPT(info->fmt_llong, fmt_llong), templlong);
 } else if (H5Tequal(type, H5T_NATIVE_ULLONG)) {
  memcpy(&tempullong, vp, sizeof(unsigned long_long));
  h5tools_str_append(str, OPT(info->fmt_ullong, fmt_ullong), tempullong);
 } else if (H5Tequal(type, H5T_NATIVE_HSSIZE)) {
  if (sizeof(hssize_t) == sizeof(int)) {
   memcpy(&tempint, vp, sizeof(int));   
   h5tools_str_append(str, OPT(info->fmt_int, "%d"), tempint);
  } else if (sizeof(hssize_t) == sizeof(long)) {
   memcpy(&templong, vp, sizeof(long));
   h5tools_str_append(str, OPT(info->fmt_long, "%ld"), templong);
  } else {
   memcpy(&templlong, vp, sizeof(long_long));
   h5tools_str_append(str, OPT(info->fmt_llong, fmt_llong), templlong);
  }
 } else if (H5Tequal(type, H5T_NATIVE_HSIZE)) {
  if (sizeof(hsize_t) == sizeof(int)) {
   memcpy(&tempuint, vp, sizeof(unsigned int));
   h5tools_str_append(str, OPT(info->fmt_uint, "%u"), tempuint);
  } else if (sizeof(hsize_t) == sizeof(long)) {
   memcpy(&tempulong, vp, sizeof(long));
   h5tools_str_append(str, OPT(info->fmt_ulong, "%lu"), tempulong);
  } else {
   memcpy(&tempullong, vp, sizeof(unsigned long_long));
   h5tools_str_append(str, OPT(info->fmt_ullong, fmt_ullong), tempullong);
  }
 } else if (H5Tget_class(type) == H5T_COMPOUND) {

		if (display_cols) /* for 1D data, separate members in columns */
		{
			ASSERT(column>=0 && column<H5Tget_nmembers(type)); 
			/* The value */
			offset = H5Tget_member_offset(type, column);
			memb = H5Tget_member_type(type, column);
			ctx->indent_level++;
			h5tools_str_sprint(str, info, container, memb, cp_vp /*+ offset*/, ctx, column /*nested */);
			ctx->indent_level--;
			H5Tclose(memb);
		}

  else /* all compound data in one cell */
		{
			int j;
			nmembs = H5Tget_nmembers(type);
#if defined (BRACKET)
			h5tools_str_append(str, "%s", OPT(info->cmpd_pre, "{"));
#endif
			for (j = 0; j < nmembs; j++) {
				if (j)
					h5tools_str_append(str, "%s", OPT(info->cmpd_sep, ", " /*OPTIONAL_LINE_BREAK*/));
					/* RPM 2000-10-31
					* If the previous character is a line-feed (which is true when
					* h5dump is running) then insert some white space for
					* indentation. Be warned that column number calculations will be
					* incorrect and that object indices at the beginning of the line
				* will be missing (h5dump doesn't display them anyway).  */
				if (ctx->indent_level >= 0 && str->len && str->s[str->len - 1] == '\n') {
					int x;
					h5tools_str_append(str, OPT(info->line_pre, ""), "");
					for (x = 0; x < ctx->indent_level + 1; x++)
						h5tools_str_append(str, "%s", OPT(info->line_indent, ""));
				}
				/* The name */
				name = H5Tget_member_name(type, j);
				h5tools_str_append(str, OPT(info->cmpd_name, ""), name);
				free(name);
				/* The value */
				offset = H5Tget_member_offset(type, j);
				memb = H5Tget_member_type(type, j);
				ctx->indent_level++;
				h5tools_str_sprint(str, info, container, memb, cp_vp + offset , ctx, column);
				ctx->indent_level--;
				H5Tclose(memb);
			}
			/* RPM 2000-10-31
			* If the previous character is a line feed (which is true when
			* h5dump is running) then insert some white space for indentation.
			* Be warned that column number calculations will be incorrect and
			* that object indices at the beginning of the line will be missing
			* (h5dump doesn't display them anyway). */
			h5tools_str_append(str, "%s", OPT(info->cmpd_end, ""));
			if (ctx->indent_level >= 0 && str->len && str->s[str->len - 1] == '\n') {
				int x;
				h5tools_str_append(str, OPT(info->line_pre, ""), "");
				for (x = 0; x < ctx->indent_level; x++)
					h5tools_str_append(str, "%s", OPT(info->line_indent, ""));
			}
#if defined (BRACKET)
			h5tools_str_append(str, "%s", OPT(info->cmpd_suf, "}"));
#endif
		} /*one cell*/


 } else if (H5Tget_class(type) == H5T_ENUM) {
  char enum_name[1024];
  
  if (H5Tenum_nameof(type, vp, enum_name, sizeof enum_name) >= 0) {
   h5tools_str_append(str, h5tools_escape(enum_name, sizeof(enum_name), TRUE));
  } else {
   size_t i;
   
   h5tools_str_append(str, "0x");
   n = H5Tget_size(type);
   
   for (i = 0; i < n; i++)
    h5tools_str_append(str, "%02x", ucp_vp[i]);
  }
 } else if (H5Tequal(type, H5T_STD_REF_DSETREG)) {
 /*
 * Dataset region reference -- show the type and OID of the referenced
 * object, but we are unable to show the region yet because there
 * isn't enough support in the data space layer.  - rpm 19990604
  */
  if (h5tools_is_zero(vp, H5Tget_size(type))) {
   h5tools_str_append(str, "NULL");
  } else {

#if defined (SHOW_OID)
   obj = H5Rdereference(container, H5R_DATASET_REGION, vp);
   region = H5Rget_region(container, H5R_DATASET_REGION, vp);
   H5Gget_objinfo(obj, ".", FALSE, &sb);
   
   if (info->dset_hidefileno)
    h5tools_str_append(str, info->dset_format, sb.objno[1], sb.objno[0]);
   else
    h5tools_str_append(str, info->dset_format,
    sb.fileno[1], sb.fileno[0], sb.objno[1], sb.objno[0]);
   
   h5tools_str_dump_region(str, region, info);
   H5Sclose(region);
   H5Dclose(obj);
#else
			{
			hdset_reg_ref_t* objno = (hdset_reg_ref_t*)vp;
   h5tools_str_append(str, "%lu", objno[0]);
			}
#endif

  }
 } else if (H5Tequal(type, H5T_STD_REF_OBJ)) {
 /*
 * Object references -- show the type and OID of the referenced
 * object.
  */
  if (h5tools_is_zero(vp, H5Tget_size(type))) {
   h5tools_str_append(str, "NULL");
  } else {

#if defined (SHOW_OID)
   otype = H5Rget_obj_type(container, H5R_OBJECT, vp);
   obj = H5Rdereference(container, H5R_OBJECT, vp);
   H5Gget_objinfo(obj, ".", FALSE, &sb);
   
   /* Print object type and close object */
   switch (otype) {
   case H5G_GROUP:
    h5tools_str_append(str, GROUPNAME);
    H5Gclose(obj);
    break;
   case H5G_DATASET:
    h5tools_str_append(str, DATASET);
    H5Dclose(obj);
    break;
   case H5G_TYPE:
    h5tools_str_append(str, DATATYPE);
    H5Tclose(obj);
    break;
   default:
    h5tools_str_append(str, "%u-", otype);
    break;
   }
   
   /* Print OID */
   if (info->obj_hidefileno) 
			{
    h5tools_str_append(str, info->obj_format, sb.objno[1], sb.objno[0]);
   } else 
			{
    h5tools_str_append(str, info->obj_format,
     sb.fileno[1], sb.fileno[0], sb.objno[1], sb.objno[0]);
   }
#else
			{
			hobj_ref_t* objno = (hobj_ref_t*)vp;
   h5tools_str_append(str, "%lu", objno[0]);
			}
#endif

  } /* h5tools_is_zero*/

 } else if (H5Tget_class(type) == H5T_ARRAY) {
  int k, ndims;
  hsize_t i, dims[H5S_MAX_RANK],temp_nelmts;
  
  /* Get the array's base datatype for each element */
  memb = H5Tget_super(type);
  size = H5Tget_size(memb);
  ndims = H5Tget_array_ndims(type);
  H5Tget_array_dims(type, dims, NULL);
  ASSERT(ndims >= 1 && ndims <= H5S_MAX_RANK);
  
  /* Calculate the number of array elements */
  for (k = 0, nelmts = 1; k < ndims; k++){
   temp_nelmts = nelmts;
   temp_nelmts *= dims[k];
   ASSERT(temp_nelmts==(hsize_t)((size_t)temp_nelmts));
   nelmts = (size_t)temp_nelmts;
  } 
#if defined (BRACKET)
  /* Print the opening bracket */
  h5tools_str_append(str, "%s", OPT(info->arr_pre, "["));
#endif
  for (i = 0; i < nelmts; i++) {
   if (i)
    h5tools_str_append(str, "%s",
    OPT(info->arr_sep, "," /*OPTIONAL_LINE_BREAK*/));
   
   if (info->arr_linebreak && i && i % dims[ndims - 1] == 0) {
    int x;
    
    h5tools_str_append(str, "%s", "\n");
    
    /*need to indent some more here*/
    if (ctx->indent_level >= 0)
     h5tools_str_append(str, "%s", OPT(info->line_pre, ""));
    
    for (x = 0; x < ctx->indent_level + 1; x++)
     h5tools_str_append(str,"%s",OPT(info->line_indent,""));
   } /* end if */
   
   ctx->indent_level++;
   
   /* Dump the array element */
   h5tools_str_sprint(str, info, container, memb, cp_vp + i * size, ctx, column);
   
   ctx->indent_level--;
  } /* end for */
  
  /* Print the closing bracket */
#if defined (BRACKET)
  h5tools_str_append(str, "%s", OPT(info->arr_suf, "]"));
#endif
  H5Tclose(memb);
 } else if (H5Tget_class(type) == H5T_VLEN) {
  unsigned int i;
  
  /* Get the VL sequences's base datatype for each element */
  memb = H5Tget_super(type);
  size = H5Tget_size(memb);
  
#if defined (BRACKET)
  /* Print the opening bracket */
  h5tools_str_append(str, "%s", OPT(info->vlen_pre, "("));
#endif
  
  /* Get the number of sequence elements */
  nelmts = ((hvl_t *)cp_vp)->len;
  
  for (i = 0; i < nelmts; i++) {
   if (i)
    h5tools_str_append(str, "%s",
    OPT(info->arr_sep, "," /*OPTIONAL_LINE_BREAK*/));
   
#ifdef LATER
   /* Need to fix so VL data breaks at correct location on end of line -QAK */
   if (info->arr_linebreak && h5tools_str_len(str)>=info->line_ncols) {
    int x;
    
    h5tools_str_append(str, "%s", "\n");
    
    /* need to indent some more here */
    if (ctx->indent_level >= 0)
     h5tools_str_append(str, "%s", OPT(info->line_pre, ""));
    
    for (x = 0; x < ctx->indent_level + 1; x++)
     h5tools_str_append(str,"%s",OPT(info->line_indent,""));
   } /* end if */
#endif /* LATER */
   
   ctx->indent_level++;
   
   /* Dump the array element */
   h5tools_str_sprint(str, info, container, memb,
    ((char *)(((hvl_t *)cp_vp)->p)) + i * size, ctx, column);
   
   ctx->indent_level--;
  } /* end for */
  
#if defined (BRACKET)
  h5tools_str_append(str, "%s", OPT(info->vlen_suf, ")"));
#endif
  H5Tclose(memb);
 } else {
  /* All other types get printed as hexadecimal */
  size_t i;
  
  h5tools_str_append(str, "0x");
  n = H5Tget_size(type);
  
  for (i = 0; i < n; i++)
   h5tools_str_append(str, "%02x", ucp_vp[i]);
 }
 
 return h5tools_str_fmt(str, start, OPT(info->elmt_fmt, "%s"));
}




/*-------------------------------------------------------------------------
* Function: h5tools_str_close
*
* Purpose: Closes a string by releasing it's memory and setting the size
*  information to zero.
*
* Return: void
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
void
h5tools_str_close(h5tools_str_t *str)
{
 if (str && str->nalloc) {
  free(str->s);
  memset(str, 0, sizeof(h5tools_str_t));
 }
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_len
*
* Purpose: Returns the length of the string, not counting the null
*  terminator.
*
* Return: Success: Length of string
*
*  Failure: 0
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
size_t
h5tools_str_len(h5tools_str_t *str)
{
 return str->len;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_append
*
* Purpose: Formats variable arguments according to printf() format
*  string and appends the result to variable length string STR.
*
* Return: Success: Pointer to buffer containing result.
*
*  Failure: NULL
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_append(h5tools_str_t *str/*in,out*/, const char *fmt, ...)
{
 va_list ap;
 
 va_start(ap, fmt);
 
 /* Make sure we have some memory into which to print */
 if (!str->s || str->nalloc <= 0) {
  str->nalloc = STR_INIT_LEN;
  str->s = (char*) malloc(str->nalloc);
  ASSERT(str->s);
  str->s[0] = '\0';
  str->len = 0;
 }
 
 while (1) {
  size_t avail = str->nalloc - str->len;
  size_t nchars = (size_t)_vsnprintf(str->s + str->len, avail, fmt, ap);
  
  if (nchars < avail) {
   /* success */
   str->len += nchars;
   break;
  }
  
  /* Try again with twice as much space */
  str->nalloc *= 2;
  str->s = (char*)realloc(str->s, str->nalloc);
  ASSERT(str->s);
 }
 
 va_end(ap);
 return str->s;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_reset
*
* Purpose: Reset the string to the empty value. If no memory is
*  allocated yet then initialize the h5tools_str_t struct.
*
* Return: Success: Ptr to the buffer which contains a null
*    character as the first element.
*
*  Failure: NULL
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_reset(h5tools_str_t *str/*in,out*/)
{
 if (!str->s || str->nalloc <= 0) {
  str->nalloc = STR_INIT_LEN;
  str->s = (char*)malloc(str->nalloc);
  ASSERT(str->s);
 }
 
 str->s[0] = '\0';
 str->len = 0;
 return str->s;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_trunc
*
* Purpose: Truncate a string to be at most SIZE characters.
*
* Return: Success: Pointer to the string
*
*  Failure: NULL
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_trunc(h5tools_str_t *str/*in,out*/, size_t size)
{
 if (size < str->len) {
  str->len = size;
  str->s[size] = '\0';
 }
 
 return str->s;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_fmt
*
* Purpose: Reformat a string contents beginning at character START
*  according to printf format FMT. FMT should contain no format
*  specifiers except possibly the `%s' variety. For example, if
*  the input string is `hello' and the format is "<<%s>>" then
*  the output value will be "<<hello>>".
*
* Return: Success: A pointer to the resulting string.
*
*  Failure: NULL
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_fmt(h5tools_str_t *str/*in,out*/, size_t start, const char *fmt)
{
 char _temp[1024], *temp = _temp;
 
 /* If the format string is simply "%s" then don't bother doing anything */
 if (!strcmp(fmt, "%s"))
  return str->s;
 
  /*
  * Save the input value if there is a `%' anywhere in FMT.  Otherwise
  * don't bother because we don't need a temporary copy.
 */
 if (strchr(fmt, '%')) {
  if (str->len - start + 1 > sizeof(_temp)) {
   temp = (char*)malloc(str->len-start + 1);
   ASSERT(temp);
  }
  
  strcpy(temp, str->s + start);
 }
 
 /* Reset the output string and append a formatted version */
 h5tools_str_trunc(str, start);
 h5tools_str_append(str, fmt, temp);
 
 /* Free the temp buffer if we allocated one */
 if (temp != _temp)
  free(temp);
 
 return str->s;
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_prefix
*
* Purpose: Renders the line prefix value into string STR.
*
* Return: Success: Pointer to the prefix.
*
*   Failure: NULL
*
* Programmer: Robb Matzke
*              Thursday, July 23, 1998
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
char *
h5tools_str_prefix(h5tools_str_t *str/*in,out*/, const h5dump_t *info,
                   hsize_t elmtno, int ndims, hsize_t min_idx[],
                   hsize_t max_idx[])
{
 hsize_t p_prod[H5S_MAX_RANK], p_idx[H5S_MAX_RANK];
 hsize_t n, i = 0;
 
 h5tools_str_reset(str);
 
 if (ndims > 0) {
 /*
 * Calculate the number of elements represented by a unit change in a
 * certain index position.
  */
  for (i = ndims - 1, p_prod[ndims - 1] = 1; i > 0; --i)
   p_prod[i - 1] = (max_idx[i] - min_idx[i]) * p_prod[i];
  
  /* Calculate the index values from the element number. */
  for (i = 0, n = elmtno; i < (hsize_t)ndims; i++) {
   p_idx[i] = n / p_prod[i] + min_idx[i];
   n %= p_prod[i];
  }
  
  /* Print the index values */
  for (i = 0; i < (hsize_t)ndims; i++) {
   if (i)
    h5tools_str_append(str, "%s", OPT(info->idx_sep, ","));
   
   h5tools_str_append(str, OPT(info->idx_n_fmt, "%lu"),
    (unsigned long)p_idx[i]);
  }
 } else {
  /* Scalar */
  h5tools_str_append(str, OPT(info->idx_n_fmt, "%lu"), (unsigned long)0);
 }
 
 /* Add prefix and suffix to the index */
 return h5tools_str_fmt(str, 0, OPT(info->idx_fmt, "%s: "));
}

/*-------------------------------------------------------------------------
* Function: h5tools_str_dump_region
*
* Purpose: Prints information about a dataspace region by appending
*  the information to the specified string.
*
* Return: Success: 0
*
*  Failure: NULL
*
* Programmer: Robb Matzke
*              Monday, June  7, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
int
h5tools_str_dump_region(h5tools_str_t *str, hid_t region, const h5dump_t *info)
{
 hssize_t nblocks, npoints;
 hsize_t     alloc_size;
 hsize_t    *ptdata;
 int  ndims = H5Sget_simple_extent_ndims(region);
 
 /*
 * These two functions fail if the region does not have blocks or points,
 * respectively. They do not currently know how to translate from one to
 * the other.
 */
 H5E_BEGIN_TRY {
  nblocks = H5Sget_select_hyper_nblocks(region);
  npoints = H5Sget_select_elem_npoints(region);
 } H5E_END_TRY;
 
 h5tools_str_append(str, "{");
 
 /* Print block information */
 if (nblocks > 0) {
  int i;
  
  alloc_size = nblocks * ndims * 2 * sizeof(ptdata[0]);
  ASSERT(alloc_size == (hsize_t)((size_t)alloc_size)); /*check for overflow*/
  ptdata = (hsize_t*)malloc((size_t)alloc_size);
  H5_CHECK_OVERFLOW(nblocks, hssize_t, hsize_t);
  H5Sget_select_hyper_blocklist(region, (hsize_t)0, (hsize_t)nblocks, ptdata);
  
  for (i = 0; i < nblocks; i++) {
   int j;
   
   h5tools_str_append(str, info->dset_blockformat_pre,
    i ? "," OPTIONAL_LINE_BREAK " " : "",
    (unsigned long)i);
   
   /* Start coordinates and opposite corner */
   for (j = 0; j < ndims; j++)
    h5tools_str_append(str, "%s%lu", j ? "," : "(",
    (unsigned long)ptdata[i * 2 * ndims + j]);
   
   for (j = 0; j < ndims; j++)
    h5tools_str_append(str, "%s%lu", j ? "," : ")-(",
    (unsigned long)ptdata[i * 2 * ndims + j + ndims]);
   
   h5tools_str_append(str, ")");
  }
  
  free(ptdata);
 }
 
 /* Print point information */
 if (npoints > 0) {
  int i;
  
  alloc_size = npoints * ndims * sizeof(ptdata[0]);
  ASSERT(alloc_size == (hsize_t)((size_t)alloc_size)); /*check for overflow*/
  ptdata = (hsize_t*)malloc((size_t)alloc_size);
  H5_CHECK_OVERFLOW(npoints,hssize_t,hsize_t);
  H5Sget_select_elem_pointlist(region, (hsize_t)0, (hsize_t)npoints, ptdata);
  
  for (i = 0; i < npoints; i++) {
   int j;
   
   h5tools_str_append(str, info->dset_ptformat_pre ,
    i ? "," OPTIONAL_LINE_BREAK " " : "",
    (unsigned long)i);
   
   for (j = 0; j < ndims; j++)
    h5tools_str_append(str, "%s%lu", j ? "," : "(",
    (unsigned long)(ptdata[i * ndims + j]));
   
   h5tools_str_append(str, ")");
  }
  
  free(ptdata);
 }
 
 h5tools_str_append(str, "}");
 return 0;
}

/*-------------------------------------------------------------------------
* Function: h5tools_print_char
*
* Purpose: Shove a character into the STR.
*
* Return: Nothing
*
* Programmer: Bill Wendling
*              Tuesday, 20. February 2001
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
void
h5tools_print_char(h5tools_str_t *str, const h5dump_t *info, unsigned char ch)
{
 if (info->str_locale == ESCAPE_HTML) {
  if (ch <= ' ' || ch > '~')
   h5tools_str_append(str, "%%%02x", ch);
  else
   h5tools_str_append(str, "%c", (char)ch);
 } else {
  switch (ch) {
  case '"':
   h5tools_str_append(str, "\\\"");
   break;
  case '\\':
   h5tools_str_append(str, "\\\\");
   break;
  case '\b':
   h5tools_str_append(str, "\\b");
   break;
  case '\f':
   h5tools_str_append(str, "\\f");
   break;
  case '\n':
   h5tools_str_append(str, "\\n");
   break;
  case '\r':
   h5tools_str_append(str, "\\r");
   break;
  case '\t':
   h5tools_str_append(str, "\\t");
   break;
  default:
   if (isprint(ch))
    h5tools_str_append(str, "%c", (char)ch);
   else 
    h5tools_str_append(str, "\\%03o", ch);
   
   break;
  }
 }
}



/*-------------------------------------------------------------------------
* Function: h5tools_escape
*
* Purpose: Changes all "funny" characters in S into standard C escape
*  sequences. If ESCAPE_SPACES is non-zero then spaces are
*  escaped by prepending a backslash.
*
* Return: Success: S
*
*  Failure: NULL if the buffer would overflow. The
*    buffer has as many left-to-right escapes as
*    possible before overflow would have happened.
*
* Programmer: Robb Matzke
*              Monday, April 26, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
static char *
h5tools_escape(char *s/*in,out*/, size_t size, int escape_spaces)
{
 register size_t i;
 size_t n = strlen(s);
 const char *escape;
 char octal[8];
 
 for (i = 0; i < n; i++) {
  switch (s[i]) {
  case '"':
   escape = "\\\"";
   break;
  case '\\':
   escape = "\\\\";
   break;
  case '\b':
   escape = "\\b";
   break;
  case '\f':
   escape = "\\f";
   break;
  case '\n':
   escape = "\\n";
   break;
  case '\r':
   escape = "\\r";
   break;
  case '\t':
   escape = "\\t";
   break;
  case ' ':
   escape = escape_spaces ? "\\ " : NULL;
   break;
  default:
   if (!isprint((int)*s)) {
    sprintf(octal, "\\%03o", (unsigned char)s[i]);
    escape = octal;
   } else {
    escape = NULL;
   }
   
   break;
  }
  
  if (escape) {
   size_t esc_size = strlen(escape);
   
   if (n + esc_size + 1 > size)
    /*would overflow*/
    return NULL;
   
   memmove(s + i + esc_size, s + i, (n - i) + 1); /*make room*/
   memcpy(s + i, escape, esc_size); /*insert*/
   n += esc_size;
   i += esc_size - 1;
  }
 }
 
 return s;
}

/*-------------------------------------------------------------------------
* Function: h5tools_is_zero
*
* Purpose: Determines if memory is initialized to all zero bytes.
*
* Return: TRUE if all bytes are zero; FALSE otherwise
*
* Programmer: Robb Matzke
*              Monday, June  7, 1999
*
* Modifications:
*
*-------------------------------------------------------------------------
*/
static hbool_t
h5tools_is_zero(const void *_mem, size_t size)
{
 const unsigned char *mem = (const unsigned char *)_mem;
 
 while (size-- > 0)
  if (mem[size])
   return FALSE;
  
  return TRUE;
}

/*-------------------------------------------------------------------------
* Function:	h5tools_fixtype
*
* Purpose:	Given a file data type choose a memory data type which is
*		appropriate for printing the data.
*
* Return:	Success:	Memory data type
*
*		Failure:	FAIL
*
* Programmer:	Robb Matzke
*              Thursday, July 23, 1998
*
* Modifications:
* 		Robb Matzke, 1999-06-04
*		Added support for references.
*
*-------------------------------------------------------------------------
*/
hid_t h5tools_fixtype( hid_t f_type )
{
	hid_t   m_type = -1, f_memb;
	hid_t   *memb = NULL;
	char    **name = NULL;
	int     nmembs = 0, i;
	int     ndims;
	hsize_t dim[H5S_MAX_RANK];
	size_t  size, offset;
	hid_t   array_base;
	
	size = H5Tget_size(f_type);
	
	switch (H5Tget_class(f_type)) 
	{
	case H5T_INTEGER:
		
	/*
	* Use the smallest native integer type of the same sign as the file
	* such that the memory type is at least as large as the file type.
	* If there is no memory type large enough then use the largest
	* memory type available.
		*/
		
		if (size <= sizeof(char)) 
			m_type = H5Tcopy(H5T_NATIVE_SCHAR);
		else if (size <= sizeof(short)) 
			m_type = H5Tcopy(H5T_NATIVE_SHORT);
		else if (size <= sizeof(int)) 
			m_type = H5Tcopy(H5T_NATIVE_INT);
		else if (size <= sizeof(long)) 
			m_type = H5Tcopy(H5T_NATIVE_LONG);
		else 
			m_type = H5Tcopy(H5T_NATIVE_LLONG);
		
		H5Tset_sign(m_type, H5Tget_sign(f_type));
		break;
		
	case H5T_FLOAT:
		
	/*
	* Use the smallest native floating point type available such that
	* its size is at least as large as the file type.  If there is not
	* native type large enough then use the largest native type.
		*/
		
		if (size <= sizeof(float)) 
			m_type = H5Tcopy(H5T_NATIVE_FLOAT);
		else if (size <= sizeof(double)) 
			m_type = H5Tcopy(H5T_NATIVE_DOUBLE);
		else 
			m_type = H5Tcopy(H5T_NATIVE_LDOUBLE);
		
		break;
		
	case H5T_STRING:
		
	/*
	* This is needed because the function in dumputil.c is the case where
	* strDUAction == TRUE. if it is false we will do the original action
	* here.
		*/
		
		m_type = H5Tcopy(f_type);
		H5Tset_cset(m_type, H5T_CSET_ASCII);
		break;
		
	case H5T_COMPOUND:
		
	/*
	* We have to do this in two steps.  The first step scans the file
	* type and converts the members to native types and remembers all
	* their names and sizes, computing the size of the memory compound
	* type at the same time.  Then we create the memory compound type
	* and add the members.
		*/
		
		nmembs = H5Tget_nmembers(f_type);
		ASSERT(nmembs > 0);
		memb = (int*) calloc((size_t)nmembs, sizeof(hid_t));
		name = (char**)calloc((size_t)nmembs, sizeof(char *));
		
		for (i = 0, size = 0; i < nmembs; i++) 
		{
			/* Get the member type and fix it */
			f_memb = H5Tget_member_type(f_type, i);
			memb[i] = h5tools_fixtype(f_memb);
			H5Tclose(f_memb);
			
			if (memb[i] < 0)
				goto done;
			
			/* Get the member name */
			name[i] = H5Tget_member_name(f_type, i);
			
			if (name[i] == NULL)
				goto done;
			
	   /*
				* Compute the new offset so each member is aligned on a byte
				* boundary which is the same as the member size.
			*/
			size = ALIGN(size, H5Tget_size(memb[i])) + H5Tget_size(memb[i]);
		}
		
		m_type = H5Tcreate(H5T_COMPOUND, size);
		
		for (i = 0, offset = 0; i < nmembs; i++) 
		{
			if (offset)
				offset = ALIGN(offset, H5Tget_size(memb[i]));
			
			H5Tinsert(m_type, name[i], offset, memb[i]);
			offset += H5Tget_size(memb[i]);
		}
		
		break;
		
	case H5T_ARRAY:
		/* Get the array information */
		ndims = H5Tget_array_ndims(f_type);
		H5Tget_array_dims(f_type, dim, NULL);
		
		/* Get the array's base type and convert it to the printable version */
		f_memb = H5Tget_super(f_type);
		array_base = h5tools_fixtype(f_memb);
		
		/* Copy the array */
		m_type = H5Tarray_create(array_base, ndims, dim, NULL);
		
		/* Close the temporary datatypes */
		H5Tclose(array_base);
		H5Tclose(f_memb);
		break;
		
	case H5T_VLEN:
		/* Get the VL sequence's base type and convert it to the printable version */
		f_memb = H5Tget_super(f_type);
		array_base = h5tools_fixtype(f_memb);
		
		/* Copy the VL type */
		m_type = H5Tvlen_create(array_base);
		
		/* Close the temporary datatypes */
		H5Tclose(array_base);
		H5Tclose(f_memb);
		break;
		
	case H5T_ENUM:
	case H5T_REFERENCE:
	case H5T_OPAQUE:
		/* Same as file type */
		m_type = H5Tcopy(f_type);
		break;
		
	case H5T_BITFIELD:
		
	/*
	* Same as the file except the offset is set to zero and the byte
	* order is set to little endian.
		*/
		m_type = H5Tcopy(f_type);
		H5Tset_offset(m_type, 0);
		H5Tset_order(m_type, H5T_ORDER_LE);
		break;
		
	case H5T_TIME:
	/*
	* These type classes are not implemented yet.
		*/
		break;
		
	default:
		break;
		
}

done:
/* Clean up temp buffers */
if (memb && name) 
{
	register int j;
	
	for (j = 0; j < nmembs; j++) 
	{
		if (memb[j] >= 0)
			H5Tclose(memb[j]);
		
		if (name[j])
			free(name[j]);
	}
	
	free(memb);
	free(name);
}

return m_type;
}
