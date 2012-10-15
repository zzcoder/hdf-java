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

#ifdef __cplusplus
extern "C" {
#endif

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "hdf5.h"
#include "h5util.h"
#include "jni.h"

    JavaVM *jvm;
    jobject visit_callback;

int h5str_dump_region(h5str_t *str, hid_t region);
static hbool_t h5tools_is_zero(const void *_mem, size_t size);

/** frees memory held by aray of strings */
void h5str_array_free(char **strs, size_t len) {
    size_t i;

    if (!strs || len <= 0)
        return;

    for (i = 0; i < len; i++) {
        if (*(strs + i))
            free(*(strs + i));
    } /* for (i=0; i<n; i++)*/
    free(strs);
}

/** allocate a new str with given length */
void h5str_new(h5str_t *str, size_t len) {
    if (str && len > 0) {
        str->s = (char *) malloc(len);
        str->max = len;
        str->s[0] = '\0';
    }
}

/** free string memory */
void h5str_free(h5str_t *str) {
    if (str && str->max > 0) {
        free(str->s);
        memset(str, 0, sizeof(h5str_t));
    }
}

/** reset the max size of the string */
void h5str_resize(h5str_t *str, size_t new_len) {
    char *new_str;

    if (!str || new_len <= 0 || str->max == new_len)
        return;

    new_str = (char *) malloc(new_len);
    if (new_len > str->max) /* increase memory */
        strcpy(new_str, str->s);
    else
        strncpy(new_str, str->s, new_len - 1);

    free(str->s);
    str->s = new_str;
    str->max = new_len;
}

/* appends a copy of the string pointed to by cstr to the h5str.
 Return Value:
 the char string point to str->s
 */
char* h5str_append(h5str_t *str, const char* cstr) {
    size_t len;

    if (!str)
        return NULL;
    else if (!cstr)
        return str->s;

    len = strlen(str->s) + strlen(cstr);
    while (len >= str->max) /* not enough to hold the new string, double the space */
    {
        h5str_resize(str, str->max * 2);
    }

    return strcat(str->s, cstr);
}

/** print value of a data point into string.
 Return Value:
 On success, the total number of characters printed is returned.
 On error, a negative number is returned.
 */
int h5str_sprintf(h5str_t *str, hid_t container, hid_t tid, void *ptr) {
    unsigned char tmp_uchar = 0;
    char tmp_char = 0;
    unsigned short tmp_ushort = 0;
    short tmp_short = 0;
    unsigned int tmp_uint = 0;
    int tmp_int = 0;
    unsigned long tmp_ulong = 0;
    long tmp_long = 0;
    unsigned long long tmp_ullong = 0;
    long long tmp_llong = 0;
    float tmp_float = 0.0;
    double tmp_double = 0.0;
    long double tmp_ldouble = 0.0;
    static char    fmt_llong[8], fmt_ullong[8];

    size_t offset, size, nll;
    char *cptr = (char*) ptr;
    unsigned char *ucptr = (unsigned char*) ptr;
    char *this_str;
    int this_strlen, i, n, len;
    hid_t mtid = -1;
    H5T_class_t tclass = H5Tget_class(tid);
    hvl_t *vlptr;

    if (!str || !ptr)
        return -1;

    /* Build default formats for long long types */
    if (!fmt_llong[0]) {
        sprintf(fmt_llong, "%%%sd", H5_PRINTF_LL_WIDTH);
        sprintf(fmt_ullong, "%%%su", H5_PRINTF_LL_WIDTH);
    }

    this_str = NULL;
    this_strlen = 0;

	switch (tclass) {
		case H5T_FLOAT:
			if (H5Tequal(tid, H5T_NATIVE_FLOAT)) {
				memcpy(&tmp_float, ptr, sizeof(float));
				this_str = (char*) malloc(25);
				sprintf(this_str, "%g", tmp_float);
			}
			else if (H5Tequal(tid, H5T_NATIVE_DOUBLE)) {
				memcpy(&tmp_double, ptr, sizeof(double));
				this_str = (char*) malloc(25);
				sprintf(this_str, "%g", tmp_double);
			}
#if H5_SIZEOF_LONG_DOUBLE !=0
            else if (H5Tequal(tid, H5T_NATIVE_LDOUBLE)) {
				memcpy(&tmp_ldouble, ptr, sizeof(long double));
				this_str = (char*) malloc(27);
				sprintf(this_str, "%Lf", tmp_ldouble);
			}
#endif
			break;
		case H5T_STRING:
			{
				char *tmp_str;
				size = 0;

				if (H5Tis_variable_str(tid)) {
					tmp_str = *(char**) ptr;
					if (tmp_str != NULL)
						size = strlen(tmp_str);
				}
				else {
					tmp_str = cptr;
					size = H5Tget_size(tid);
				}

				/* Check for NULL pointer for string */
				if (tmp_str == NULL) {
					strncpy(this_str, "NULL", 4);
				}
				else {
					if (size > 0) {
						this_str = (char *) malloc(size+1);
						strncpy(this_str, tmp_str, size);
					}
				}
			}
			break;
		case H5T_INTEGER:
			if (H5Tequal(tid, H5T_NATIVE_INT)) {
				memcpy(&tmp_int, ptr, sizeof(int));
				this_str = (char*) malloc(14);
				sprintf(this_str, "%d", tmp_int);
			}
			else if (H5Tequal(tid, H5T_NATIVE_UINT)) {
				memcpy(&tmp_uint, ptr, sizeof(unsigned int));
				this_str = (char*) malloc(14);
				sprintf(this_str, "%u", tmp_uint);
			}
			else if (H5Tequal(tid, H5T_NATIVE_SCHAR)) {
				memcpy(&tmp_char, ptr, sizeof(char));
				this_str = (char*) malloc(7);
				sprintf(this_str, "%hhd", tmp_char);
			}
			else if (H5Tequal(tid, H5T_NATIVE_UCHAR)) {
				memcpy(&tmp_uchar, ptr, sizeof(unsigned char));
				this_str = (char*) malloc(7);
				sprintf(this_str, "%u", tmp_uchar);
			}
			else if (H5Tequal(tid, H5T_NATIVE_SHORT)) {
				memcpy(&tmp_short, ptr, sizeof(short));
				this_str = (char*) malloc(9);
				sprintf(this_str, "%d", tmp_short);
			}
			else if (H5Tequal(tid, H5T_NATIVE_USHORT)) {
				memcpy(&tmp_ushort, ptr, sizeof(unsigned short));
				this_str = (char*) malloc(9);
				sprintf(this_str, "%u", tmp_ushort);
			}
			else if (H5Tequal(tid, H5T_NATIVE_LONG)) {
				memcpy(&tmp_long, ptr, sizeof(long));
				this_str = (char*) malloc(23);
				sprintf(this_str, "%ld", tmp_long);
			}
			else if (H5Tequal(tid, H5T_NATIVE_ULONG)) {
				memcpy(&tmp_ulong, ptr, sizeof(unsigned long));
				this_str = (char*) malloc(23);
				sprintf(this_str, "%lu", tmp_ulong);
			}
			else if (H5Tequal(tid, H5T_NATIVE_LLONG)) {
				memcpy(&tmp_llong, ptr, sizeof(long long));
				this_str = (char*) malloc(25);
				sprintf(this_str, fmt_llong, tmp_llong);
			}
			else if (H5Tequal(tid, H5T_NATIVE_ULLONG)) {
				memcpy(&tmp_ullong, ptr, sizeof(unsigned long long));
				this_str = (char*) malloc(25);
				sprintf(this_str, fmt_ullong, tmp_ullong);
			}
			break;
		case H5T_COMPOUND:
			{
                unsigned i;
				n = H5Tget_nmembers(tid);
				h5str_append(str, " {");

				for (i = 0; i < n; i++) {
					offset = H5Tget_member_offset(tid, i);
					mtid = H5Tget_member_type(tid, i);
					h5str_sprintf(str, container, mtid, cptr + offset);
					if (i < n - 1)
						h5str_append(str, ", ");
					H5Tclose(mtid);
				}
				h5str_append(str, "} ");
			}
			break;
		case H5T_ENUM:
			{
				char enum_name[1024];
				if (H5Tenum_nameof(tid, ptr, enum_name, sizeof enum_name) >= 0) {
					h5str_append(str, enum_name);
				}
				else {
					size_t i;
					nll = H5Tget_size(tid);
					this_str = (char*) malloc(4 * (nll + 1));

					if (1 == nll) {
						sprintf(this_str, "0x%02x", ucptr[0]);
					}
					else {
						for (i = 0; i < (int)nll; i++)
							sprintf(this_str, "%s%02x", i ? ":" : "", ucptr[i]);
					}
				}
			}
			break;
		case H5T_REFERENCE:
			if (H5Tequal(tid, H5T_STD_REF_DSETREG)) {
				/*
				 * Dataset region reference -- show the type and OID of the referenced
				 * object, but we are unable to show the region yet because there
				 * isn't enough support in the data space layer.  - rpm 19990604
				 */
				if (h5tools_is_zero(ptr, H5Tget_size(tid))) {
					h5str_append(str, "NULL");
				}
				else {
					char obj_info[128];
					hid_t obj, region;
					H5G_stat_t sb;

					/* get name of the dataset the region reference points to using H5Rget_name */
					obj = H5Rdereference(container, H5R_DATASET_REGION, ptr);
					H5Gget_objinfo(obj, ".", 0, &sb);
					sprintf(obj_info, "%lu:%lu ", sb.objno[1], sb.objno[0]);
					h5str_append(str, obj_info);

					region = H5Rget_region(container, H5R_DATASET_REGION, ptr);
					h5str_dump_region(str, region);
					H5Sclose(region);
					H5Dclose(obj);
				}
			}
			else if (H5Tequal(tid, H5T_STD_REF_OBJ)) {
				memcpy(&tmp_ulong, ptr, 8);
				this_str = (char*) malloc(23);
				sprintf(this_str, "%u", tmp_ulong);
			}
			break;
		case H5T_ARRAY:
			{
				int rank = 0;
				hsize_t i, dims[H5S_MAX_RANK], total_elmts;
				h5str_append(str, "[ ");

				mtid = H5Tget_super(tid);
				size = H5Tget_size(mtid);
				rank = H5Tget_array_ndims(tid);

				H5Tget_array_dims2(tid, dims);

				total_elmts = 1;
				for (i = 0; i < rank; i++)
					total_elmts *= dims[i];

				for (i = 0; i < total_elmts; i++) {
					h5str_sprintf(str, container, mtid, cptr + i * size);
					if (i < total_elmts - 1)
						h5str_append(str, ", ");
				}
				H5Tclose(mtid);
				h5str_append(str, "] ");
			}
			break;
		case H5T_VLEN:
			{
				unsigned int i;
				mtid = H5Tget_super(tid);
				size = H5Tget_size(mtid);

				vlptr = (hvl_t *) cptr;

				nll = vlptr->len;
				for (i = 0; i < (int)nll; i++) {
					h5str_sprintf(str, container, mtid, ((char *) (vlptr->p)) + i
							* size);
					if (i < (int)nll - 1)
						h5str_append(str, ", ");
				}
				H5Tclose(mtid);
			}
			break;

		default:
			{
				/* All other types get printed as hexadecimal */
				size_t i;
				nll = H5Tget_size(tid);
				this_str = (char*) malloc(4 * (nll + 1));

				if (1 == nll) {
					sprintf(this_str, "0x%02x", ucptr[0]);
				}
				else {
					for (i = 0; i < (int)nll; i++)
						sprintf(this_str, "%s%02x", i ? ":" : "", ucptr[i]);
				}
			}
            break;
	} /* end switch */

    if (this_str) {
        h5str_append(str, this_str);
        this_strlen = strlen(this_str);
        free(this_str);
    }

    return this_strlen;
}

/* dumps region reference information into a string */
int h5str_dump_region(h5str_t *str, hid_t region) {
    hssize_t nblocks, npoints;
    hsize_t alloc_size;
    hsize_t *ptdata;
    int ndims = H5Sget_simple_extent_ndims(region);
    char tmp_str[256];

    /*
     * These two functions fail if the region does not have blocks or points,
     * respectively. They do not currently know how to translate from one to
     * the other.
     */
    H5E_BEGIN_TRY {
        nblocks = H5Sget_select_hyper_nblocks(region);
        npoints = H5Sget_select_elem_npoints(region);
    }
    H5E_END_TRY;

    h5str_append(str, "{");

    /* Print block information */
    if (nblocks > 0) {
        int i;

        alloc_size = nblocks * ndims * 2 * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_hyper_blocklist(region, (hsize_t) 0,
                    (hsize_t) nblocks, ptdata);

            for (i = 0; i < nblocks; i++) {
                int j;

                h5str_append(str, " ");

                /* Start coordinates and opposite corner */
                for (j = 0; j < ndims; j++) {
                    tmp_str[0] = '\0';
                    sprintf(tmp_str, "%s%lu", j ? "," : "(",
                            (unsigned long) ptdata[i * 2 * ndims + j]);
                    h5str_append(str, tmp_str);
                }

                for (j = 0; j < ndims; j++) {
                    tmp_str[0] = '\0';
                    sprintf(tmp_str, "%s%lu", j ? "," : ")-(",
                            (unsigned long) ptdata[i * 2 * ndims + j + ndims]);
                    h5str_append(str, tmp_str);
                }
                h5str_append(str, ") ");
                tmp_str[0] = '\0';
            }

            free(ptdata);
        } /* if (alloc_size == (hsize_t)((size_t)alloc_size)) */
    } /* if (nblocks > 0) */

    /* Print point information */
    if (npoints > 0) {
        int i;

        alloc_size = npoints * ndims * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_elem_pointlist(region, (hsize_t) 0,
                    (hsize_t) npoints, ptdata);

            for (i = 0; i < npoints; i++) {
                int j;

                h5str_append(str, " ");

                for (j = 0; j < ndims; j++) {
                    tmp_str[0] = '\0';
                    sprintf(tmp_str, "%s%lu", j ? "," : "(",
                            (unsigned long) (ptdata[i * ndims + j]));
                    h5str_append(str, tmp_str);
                }

                h5str_append(str, ") ");
            }

            free(ptdata);
        }
    }

    h5str_append(str, "}");

    return 0;
}

static hbool_t h5tools_is_zero(const void *_mem, size_t size) {
    const unsigned char *mem = (const unsigned char *) _mem;

    while (size-- > 0)
        if (mem[size])
            return 0;

    return 1;
}

#ifdef __cplusplus
}
#endif
