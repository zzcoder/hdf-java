// HdfObjH5CompoundDS.h: interface for the CH5CompoundDS class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HDFOBJH5COMPOUNDDS_H__80781684_F776_452D_AF17_3F4F012E0D1F__INCLUDED_)
#define AFX_HDFOBJH5COMPOUNDDS_H__80781684_F776_452D_AF17_3F4F012E0D1F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "HdfObjCompoundDS.h"
#include "hdf5.h"

/*
 * A macro for detecting over/under-flow when casting between types
 */
#ifndef NDEBUG
#define H5_CHECK_OVERFLOW(var,vartype,casttype) \
{                                               \
    casttype _tmp_overflow=(casttype)(var);     \
    ASSERT((var)==(vartype)_tmp_overflow);      \
}
#else /* NDEBUG */
#define H5_CHECK_OVERFLOW(var,vartype,casttype)
#endif /* NDEBUG */

#define ESCAPE_HTML             1
#define OPT(X,S)                ((X) ? (X) : (S))
#define OPTIONAL_LINE_BREAK     "\001"  /* Special strings embedded in the output */
#define long_long	__int64	/*Win32*/
#define ALIGN(A,Z)		((((A) + (Z) - 1) / (Z)) * (Z))

/*
 * Information about how to format output.
 */
typedef struct h5dump_t {
    /*
     * Fields associated with formatting numeric data.  If a datatype matches
     * multiple formats based on its size, then the first applicable format
     * from this list is used. However, if `raw' is non-zero then dump all
     * data in hexadecimal format without translating from what appears on
     * disk.
     *
     *   raw:        If set then print all data as hexadecimal without
     *               performing any conversion from disk.
     *
     *   fmt_raw:    The printf() format for each byte of raw data. The
     *               default is `%02x'.
     *
     *   fmt_int:    The printf() format to use when rendering data which is
     *               typed `int'. The default is `%d'.
     *
     *   fmt_uint:   The printf() format to use when rendering data which is
     *               typed `unsigned'. The default is `%u'.
     *
     *   fmt_schar:  The printf() format to use when rendering data which is
     *               typed `signed char'. The default is `%d'. This format is
     *               used ony if the `ascii' field is zero.
     *
     *   fmt_uchar:  The printf() format to use when rendering data which is
     *               typed `unsigned char'. The default is `%u'. This format
     *               is used only if the `ascii' field is zero.
     *   
     *   fmt_short:  The printf() format to use when rendering data which is
     *               typed `short'. The default is `%d'.
     *
     *   fmt_ushort: The printf() format to use when rendering data which is
     *               typed `unsigned short'. The default is `%u'.
     *
     *   fmt_long:   The printf() format to use when rendering data which is
     *               typed `long'. The default is `%ld'.
     *
     *   fmt_ulong:  The printf() format to use when rendering data which is
     *               typed `unsigned long'. The default is `%lu'.
     *
     *   fmt_llong:  The printf() format to use when rendering data which is
     *               typed `long long'. The default depends on what printf()
     *               format is available to print this datatype.
     *
     *   fmt_ullong: The printf() format to use when rendering data which is
     *               typed `unsigned long long'. The default depends on what
     *               printf() format is available to print this datatype.
     *
     *   fmt_double: The printf() format to use when rendering data which is
     *               typed `double'. The default is `%g'.
     * 
     *   fmt_float:  The printf() format to use when rendering data which is
     *               typed `float'. The default is `%g'.
     *
     *   ascii:      If set then print 1-byte integer values as an ASCII
     *               character (no quotes).  If the character is one of the
     *               standard C escapes then print the escaped version.  If
     *               the character is unprintable then print a 3-digit octal
     *               escape.  If `ascii' is zero then then 1-byte integers are
     *               printed as numeric values.  The default is zero.
     *
     *   str_locale: Determines how strings are printed. If zero then strings
     *               are printed like in C except. If set to ESCAPE_HTML then
     *               strings are printed using HTML encoding where each
     *               character not in the class [a-zA-Z0-9] is substituted
     *               with `%XX' where `X' is a hexadecimal digit.
     *
     *   str_repeat: If set to non-zero then any character value repeated N
     *               or more times is printed as 'C'*N
     *
     * Numeric data is also subject to the formats for individual elements.
     */
    hbool_t     raw;
    const char  *fmt_raw;
    const char  *fmt_int;
    const char  *fmt_uint;
    const char  *fmt_schar;
    const char  *fmt_uchar;
    const char  *fmt_short;
    const char  *fmt_ushort;
    const char  *fmt_long;
    const char  *fmt_ulong;
    const char  *fmt_llong;
    const char  *fmt_ullong;
    const char  *fmt_double;
    const char  *fmt_float;
    int         ascii;
    int         str_locale;
    int         str_repeat;

    /*
     * Fields associated with compound array members.
     *
     *   pre:       A string to print at the beginning of each array. The
     *              default value is the left square bracket `['.
     *
     *   sep:       A string to print between array values.  The default
     *              value is a ",\001" ("\001" indicates an optional line
     *              break).
     *
     *   suf:       A string to print at the end of each array.  The default
     *              value is a right square bracket `]'.
     *
     *   linebreaks: a boolean value to determine if we want to break the line
     *               after each row of an array.
     */
    const char  *arr_pre;
    const char  *arr_sep;
    const char  *arr_suf;
    int         arr_linebreak;
    
    /*
     * Fields associated with compound data types.
     *
     *   name:      How the name of the struct member is printed in the
     *              values. By default the name is not printed, but a
     *              reasonable setting might be "%s=" which prints the name
     *              followed by an equal sign and then the value.
     *
     *   sep:       A string that separates one member from another.  The
     *              default is ", \001" (the \001 indicates an optional
     *              line break to allow structs to span multiple lines of
     *              output).
     *
     *   pre:       A string to print at the beginning of a compound type.
     *              The default is a left curly brace.
     *
     *   suf:       A string to print at the end of each compound type.  The
     *              default is  right curly brace.
     *              
     *   end:       a string to print after we reach the last element of 
     *              each compound type. prints out before the suf.
     */
    const char  *cmpd_name;
    const char  *cmpd_sep;
    const char  *cmpd_pre;
    const char  *cmpd_suf;
    const char  *cmpd_end;

    /*
     * Fields associated with vlen data types.
     *
     *   sep:       A string that separates one member from another.  The
     *              default is ", \001" (the \001 indicates an optional
     *              line break to allow structs to span multiple lines of
     *              output).
     *
     *   pre:       A string to print at the beginning of a vlen type.
     *              The default is a left parentheses.
     *
     *   suf:       A string to print at the end of each vlen type.  The
     *              default is a right parentheses.
     *              
     *   end:       a string to print after we reach the last element of 
     *              each compound type. prints out before the suf.
     */
    const char  *vlen_sep;
    const char  *vlen_pre;
    const char  *vlen_suf;
    const char  *vlen_end;

    /*
     * Fields associated with the individual elements.
     *
     *   fmt:       A printf(3c) format to use to print the value string
     *              after it has been rendered.  The default is "%s".
     *
     *   suf1:      This string is appended to elements which are followed by
     *              another element whether the following element is on the
     *              same line or the next line.  The default is a comma.
     *
     *   suf2:      This string is appended (after `suf1') to elements which
     *              are followed on the same line by another element.  The
     *              default is a single space.
     */
    const char  *elmt_fmt;
    const char  *elmt_suf1;
    const char  *elmt_suf2;
    
    /*
     * Fields associated with the index values printed at the left edge of
     * each line of output.
     *
     *   n_fmt:     Each index value is printed according to this printf(3c)
     *              format string which should include a format for a long
     *              integer.  The default is "%lu".
     *
     *   sep:       Each integer in the index list will be separated from the
     *              others by this string, which defaults to a comma.
     *
     *   fmt:       After the index values are formated individually and
     *              separated from one another by some string, the entire
     *              resulting string will be formated according to this
     *              printf(3c) format which should include a format for a
     *              character string.  The default is "%s".
     */
    const char  *idx_n_fmt;             /*index number format           */
    const char  *idx_sep;               /*separator between numbers     */
    const char  *idx_fmt;               /*entire index format           */
    
    /*
     * Fields associated with entire lines.
     *
     *   ncols:     Number of columns per line defaults to 80.
     *
     *   per_line:  If this field has a positive value then every Nth element
     *              will be printed at the beginning of a line.
     *
     *   pre:       Each line of output contains an optional prefix area
     *              before the data. This area can contain the index for the
     *              first datum (represented by `%s') as well as other
     *              constant text.  The default value is `%s'.
     *
     *   1st:       This is the format to print at the beginning of the first
     *              line of output. The default value is the current value of
     *              `pre' described above.
     *
     *   cont:      This is the format to print at the beginning of each line
     *              which was continued because the line was split onto
     *              multiple lines. This often happens with compound
     *              data which is longer than one line of output. The default
     *              value is the current value of the `pre' field
     *              described above.
     *
     *   suf:       This character string will be appended to each line of
     *              output.  It should not contain line feeds.  The default
     *              is the empty string.
     *   
     *   sep:       A character string to be printed after every line feed
     *              defaulting to the empty string.  It should end with a
     *              line feed.
     *
     *   multi_new: Indicates the algorithm to use when data elements tend to
     *              occupy more than one line of output. The possible values
     *              are (zero is the default):
     *
     *              0:  No consideration. Each new element is printed
     *                  beginning where the previous element ended.
     *
     *              1:  Print the current element beginning where the
     *                  previous element left off. But if that would result
     *                  in the element occupying more than one line and it
     *                  would only occupy one line if it started at the
     *                  beginning of a line, then it is printed at the
     *                  beginning of the next line.
     *
     *   multi_new: If an element is continued onto additional lines then
     *              should the following element begin on the next line? The
     *              default is to start the next element on the same line
     *              unless it wouldn't fit.
     *              
     * indentlevel: a string that shows how far to indent if extra spacing
     *              is needed. dumper uses it.
     */
    int         line_ncols;             /*columns of output             */
    size_t      line_per_line;          /*max elements per line         */
    const char  *line_pre;              /*prefix at front of each line  */
    const char  *line_1st;              /*alternate pre. on first line  */
    const char  *line_cont;             /*alternate pre. on continuation*/
    const char  *line_suf;              /*string to append to each line */
    const char  *line_sep;              /*separates lines               */
    int         line_multi_new;         /*split multi-line outputs?     */
    const char  *line_indent;           /*for extra identation if we need it*/

    /*used to skip the first set of checks for line length*/
    int skip_first;

    /*flag used to hide or show the file number for obj refs*/
    int obj_hidefileno;

    /*string used to format the output for the obje refs*/
    const char *obj_format;

    /*flag used to hide or show the file number for dataset regions*/
    int dset_hidefileno;

    /*string used to format the output for the dataset regions*/
    const char *dset_format;

    const char *dset_blockformat_pre;
    const char *dset_ptformat_pre;
    const char *dset_ptformat;

} h5dump_t;

typedef struct h5tools_context_t {
    size_t	cur_column;	/*current column for output	*/
    size_t	cur_elmt;	/*current element/output line	*/
    int		need_prefix;	/*is line prefix needed?	*/
    int		ndims;		/*dimensionality		*/
    hsize_t	p_min_idx[H5S_MAX_RANK]; /*min selected index	*/
    hsize_t	p_max_idx[H5S_MAX_RANK]; /*max selected index	*/
    int		prev_multiline;	/*was prev datum multiline?	*/
    size_t	prev_prefix_len;/*length of previous prefix	*/
    int		continuation;	/*continuation of previous data?*/
    hsize_t	size_last_dim;  /*the size of the last dimension,
                                 *needed so we can break after each
                                 *row */
    int		indent_level;   /*the number of times we need some
                                 *extra indentation */
    int		default_indent_level; /*this is used when the indent
                                       *level gets changed */
} h5tools_context_t;




/*
 * If REPEAT_VERBOSE is defined then character strings will be printed so
 * that repeated character sequences like "AAAAAAAAAA" are displayed as
 *
 * 	'A' repeates 9 times
 *
 * Otherwise the format is more Perl-like
 *
 * 	'A'*10
 * 
 */
#define REPEAT_VERBOSE

/* Variable length string datatype */
#define STR_INIT_LEN    4096    /*initial length            */


typedef struct h5tools_str_t {
	char	*s;		/*allocate string		*/
	size_t	len;		/*length of actual value	*/
	size_t	nalloc;		/*allocated size of string	*/
} h5tools_str_t;

void     h5tools_str_close(h5tools_str_t *str);
size_t   h5tools_str_len(h5tools_str_t *str);
char    *h5tools_str_append(h5tools_str_t *str, const char *fmt, ...);
char    *h5tools_str_reset(h5tools_str_t *str);
char    *h5tools_str_trunc(h5tools_str_t *str, size_t size);
char    *h5tools_str_fmt(h5tools_str_t *str, size_t start, const char *fmt);
char    *h5tools_str_prefix(h5tools_str_t *str, const h5dump_t *info, hsize_t max_idx[]);
int      h5tools_str_dump_region(h5tools_str_t *, hid_t, const h5dump_t *);
void     h5tools_print_char(h5tools_str_t *str, const h5dump_t *info, unsigned char ch);
char    *h5tools_str_prefix(h5tools_str_t *str, const h5dump_t *info, hsize_t elmtno, int ndims, hsize_t min_idx[], hsize_t max_idx[]);
int      h5tools_str_dump_region(h5tools_str_t *, hid_t, const h5dump_t *);
void     h5tools_print_char(h5tools_str_t *str, const h5dump_t *info, unsigned char ch);
char    *h5tools_str_sprint(h5tools_str_t *str, const h5dump_t *info, hid_t container, hid_t type, void *vp, h5tools_context_t *ctx, int column /*of grid in 1D compound data */);
char    *h5tools_escape(char *s, size_t size, int escape_spaces);
hbool_t  h5tools_is_zero(const void *_mem, size_t size);
hid_t	 h5tools_fixtype( hid_t f_type );

class CH5CompoundDS : public CCompoundDS  
{
public:
	CObArray* ReadAttributes();	// override CHObject::ReadAttributes()
	void Init();				// override CHObject::oid_t Init()
	void* ReadData();
	CString GetDatatypeInfo();
	CString GetDatatypeInfo(int tid);
	CH5CompoundDS(CFileFormat* fileformat, LPCTSTR name, LPCTSTR path);
	virtual ~CH5CompoundDS();
	virtual CString ValueToStr(int row, int col, CString strValue);
private:
	size_t* m_pMemberOffset;
	hid_t m_nType;
};

#endif // !defined(AFX_HDFOBJH5COMPOUNDDS_H__80781684_F776_452D_AF17_3F4F012E0D1F__INCLUDED_)
