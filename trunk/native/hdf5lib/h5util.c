/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF5.  The full HDF5 copyright notice, including     *
 * terms governing use, modification, and redistribution, is contained in    *
 * the files COPYING and Copyright.html.  COPYING can be found at the root   *
 * of the source code distribution tree; Copyright.html can be found at the  *
 * root level of an installed copy of the electronic HDF5 document set and   *
 * is linked from the top-level documents page.  It can also be found at     *
 * http://hdf.ncsa.uiuc.edu/HDF5/doc/Copyright.html.  If you do not have     *
 * access to either file, you may request a copy from hdfhelp@ncsa.uiuc.edu. *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "hdf5.h"
#include "h5util.h"

/** allocate a new str with given length */
void h5str_new(h5str_t *str, size_t len)
{
	if (str && len > 0)
	{
		str->s = (char *) malloc(len);
		str->max = len;
		str->s[0] = '\0';
	}
}

/** free string memory */
void h5str_free(h5str_t *str)
{
	if (str && str->max>0)
	{
		free(str->s);
		memset(str, 0, sizeof(h5str_t));
	}
}

/** reset the max size of the string */
void h5str_resize (h5str_t *str, size_t new_len)
{
	char *new_str;

	if (!str || new_len<=0 || str->max == new_len)
		return;

	new_str = (char *)malloc(new_len);
	if (new_len > str->max) /* increase memory */
		strcpy(new_str, str->s);
	else 
		strncpy(new_str, str->s, new_len-1);

	free(str->s);
	str->s = new_str;
	str->max = new_len;
}

/* appends a copy of the string pointed to by cstr to the h5str.
	Return Value:
	the char string point to str->s
*/
char* h5str_append (h5str_t *str, const char* cstr)
{
	size_t len;

	if (!str)
		return NULL;
	else if (!cstr)
		return str->s;

	len = strlen(str->s) + strlen(cstr);
	while (len >= str->max) /* not enough to hold the new string, double the space */
	{
		h5str_resize(str, str->max*2);
	}

	return strcat(str->s, cstr);
}

/** print value of a data point into string.
	Return Value:
		On success, the total number of characters printed is returned.
		On error, a negative number is returned.
*/
int h5str_sprintf(h5str_t *str, hid_t tid, void *ptr)
{
	unsigned char		tmp_uchar = 0;
	char				tmp_char = 0;
	unsigned short		tmp_ushort = 0;
	short				tmp_short = 0;
	unsigned int		tmp_uint = 0;
	int					tmp_int = 0;
	unsigned long		tmp_ulong = 0;
	long				tmp_long = 0;
	float				tmp_float = 0;
	double				tmp_double = 0.0;

	size_t offset, size;
	char *cptr = (char*)ptr;
	unsigned char *ucptr = (unsigned char*)ptr;
	char *this_str;
	int	this_strlen, i, n;
	hid_t mtid = -1;
	H5T_class_t tclass = H5Tget_class(tid);
	hvl_t *vlptr;

	if (!str || !ptr)
		return -1;

	this_str = NULL;
	this_strlen = 0;

    if (H5Tequal(tid, H5T_NATIVE_SCHAR))
	{
		this_str = (char*)malloc(7);
        memcpy(&tmp_char, ptr, 1);
		sprintf(this_str, "%d", tmp_char);
    } else if (H5Tequal(tid, H5T_NATIVE_UCHAR))
	{
		this_str = (char*)malloc(7);
        memcpy(&tmp_uchar, ptr, 1);
		sprintf(this_str, "%u", tmp_uchar);
    } else if (H5Tequal(tid, H5T_NATIVE_SHORT))
	{
		this_str = (char*)malloc(9);
        memcpy(&tmp_short, ptr, 2);
		sprintf(this_str, "%d", tmp_short);
    } else if (H5Tequal(tid, H5T_NATIVE_USHORT))
	{
		this_str = (char*)malloc(9);
        memcpy(&tmp_ushort, ptr, 2);
		sprintf(this_str, "%u", tmp_ushort);
	} else if (H5Tequal(tid, H5T_NATIVE_INT))
	{
		this_str = (char*)malloc(14);
        memcpy(&tmp_int, ptr, 4);
		sprintf(this_str, "%d", tmp_int);
    } else if (H5Tequal(tid, H5T_NATIVE_UINT))
	{
		this_str = (char*)malloc(14);
        memcpy(&tmp_uint, ptr, 4);
		sprintf(this_str, "%u", tmp_uint);
    } else if (H5Tequal(tid, H5T_NATIVE_LONG)) {
		this_str = (char*)malloc(23);
        memcpy(&tmp_long, ptr, sizeof(long));
		sprintf(this_str, "%d", tmp_long);
    } else if (H5Tequal(tid, H5T_NATIVE_ULONG))
	{
		this_str = (char*)malloc(23);
        memcpy(&tmp_ulong, ptr, sizeof(unsigned long));
		sprintf(this_str, "%u", tmp_ulong);
    } else if (H5Tequal(tid, H5T_STD_REF_OBJ))
	{
		this_str = (char*)malloc(23);
        memcpy(&tmp_ulong, ptr, 8);
		sprintf(this_str, "%u", tmp_ulong);
	} else 	if (H5Tequal(tid, H5T_NATIVE_FLOAT))
	{
		this_str = (char*)malloc(25);
        memcpy(&tmp_float, ptr, sizeof(float));
		sprintf(this_str, "%f", tmp_float);
    } else if (H5Tequal(tid, H5T_NATIVE_DOUBLE)) {
		this_str = (char*)malloc(25);
        memcpy(&tmp_double, ptr, sizeof(double));
		sprintf(this_str, "%f", tmp_double);
    } else if (tclass == H5T_STRING)
	{
        char *tmp_str;
		size = 0;

        if(H5Tis_variable_str(tid)) 
		{
            tmp_str = *(char**)ptr;
            if(tmp_str) size = strlen(tmp_str);
        } else 
		{
            tmp_str = cptr;
            size = H5Tget_size(tid);
        }

		if (size > 0)
		{
			this_str = (char *)malloc(size);
			strcpy(this_str, tmp_str);
		}
    } else if (tclass == H5T_COMPOUND)
	{
        n = H5Tget_nmembers(tid);
		h5str_append(str, " {");

        for (i = 0; i < n; i++)
		{
			offset = H5Tget_member_offset(tid, i);
            mtid = H5Tget_member_type(tid ,i);
            h5str_sprintf(str, mtid, cptr+offset);
            if (i<n-1) strcat(str->s, ", ");
            H5Tclose(mtid);
        }
		h5str_append(str, "} ");
    } else if (tclass == H5T_ARRAY)
	{
        int rank=0;
        hsize_t dims[H5S_MAX_RANK], total_elmts;

		h5str_append(str, "[ ");

        mtid = H5Tget_super(tid);
        size = H5Tget_size(mtid);
        rank = H5Tget_array_ndims(tid);
        H5Tget_array_dims(tid, dims, NULL);

		total_elmts = 1;
        for (i=0; i<rank; i++)
			total_elmts *= dims[i];

        for (i = 0; i < total_elmts; i++)
		{
            h5str_sprintf(str, mtid, cptr + i * size);
			if (i<total_elmts-1) strcat(str->s, ", ");
        }
        H5Tclose(mtid);
		h5str_append(str, "] ");
    } else if (tclass == H5T_VLEN)
	{
        mtid = H5Tget_super(tid);
        size = H5Tget_size(mtid);

		vlptr = (hvl_t *)cptr;

        n = vlptr->len;
        for (i = 0; i < n; i++)
		{
			h5str_sprintf(str, mtid, ((char *)(vlptr->p)) + i * size);
        	if (i<n-1) strcat(str->s, ", ");
		}
        H5Tclose(mtid);
    } else /* All other types get printed as hexadecimal */
	{
        n = H5Tget_size(tid);
		this_str = (char*)malloc(4*(n+1));

        if (1==n)
		{
			sprintf(this_str, "0x%02x", ucptr[0]);
        } else
		{
            for (i = 0; i < n; i++)
				sprintf(this_str, "%s%02x", i?":":"", ucptr[i]);
        }

    }

    if (this_str)
	{
		h5str_append(str, this_str);
		this_strlen = strlen(this_str);
		free (this_str);
	}

	return this_strlen;
}