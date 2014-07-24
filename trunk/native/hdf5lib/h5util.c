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

    /* size of hyperslab buffer when a dataset is bigger than H5TOOLS_MALLOCSIZE */
    hsize_t H5TOOLS_BUFSIZE = (32 * 1024 * 1024);  /* 32 MB */
    int     H5TOOLS_TEXT_BLOCK = 16;  /* Number of elements on a line in a text export file */

    JavaVM *jvm;
    jobject visit_callback;

int     h5str_dump_region_blocks(h5str_t *str, hid_t region, hid_t region_obj);
int     h5str_dump_region_points(h5str_t *str, hid_t region, hid_t region_obj);
int     h5str_is_zero(const void *_mem, size_t size);
hid_t   h5str_get_native_type(hid_t type);
hid_t   h5str_get_little_endian_type(hid_t type);
hid_t   h5str_get_big_endian_type(hid_t type);
htri_t  h5str_detect_vlen(hid_t tid);
htri_t  h5str_detect_vlen_str(hid_t tid);
int     h5tools_dump_simple_data(FILE *stream, hid_t container, hid_t type, void *_mem, hsize_t nelmts);
int     h5str_render_bin_output(FILE *stream, hid_t container, hid_t tid, void *_mem, hsize_t block_nelmts);
int     render_bin_output_region_data_blocks(FILE *stream, hid_t region_id,
            hid_t container, int ndims, hid_t type_id, hssize_t nblocks, hsize_t *ptdata);
int     render_bin_output_region_blocks(FILE *stream, hid_t region_space,
            hid_t region_id, hid_t container);
int     render_bin_output_region_data_points(FILE *stream, hid_t region_space, hid_t region_id,
            hid_t container, int ndims, hid_t type_id, hssize_t npoints, hsize_t *ptdata);
int     render_bin_output_region_points(FILE *stream, hid_t region_space,
            hid_t region_id, hid_t container);
/** frees memory held by array of strings */
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
int h5str_sprintf(h5str_t *str, hid_t container, hid_t tid, void *ptr, int expand_data) {
    unsigned char   tmp_uchar = 0;
    char            tmp_char = 0;
    unsigned short  tmp_ushort = 0;
    short           tmp_short = 0;
    unsigned int    tmp_uint = 0;
    int             tmp_int = 0;
    unsigned long   tmp_ulong = 0;
    long            tmp_long = 0;
    unsigned long long tmp_ullong = 0;
    long long       tmp_llong = 0;
    float           tmp_float = 0.0;
    double          tmp_double = 0.0;
    long double     tmp_ldouble = 0.0;
    static char     fmt_llong[8], fmt_ullong[8];

    hid_t           mtid = -1;
    size_t          offset;
    size_t          nll;
    char           *this_str;
    int             this_strlen;
    int             i;
    int             n;
    int             len;
    hvl_t          *vlptr;
    char           *cptr = (char*) ptr;
    unsigned char  *ucptr = (unsigned char*) ptr;
    H5T_class_t     tclass = H5Tget_class(tid);
    size_t          size = H5Tget_size(tid);
    H5T_sign_t      nsign = H5Tget_sign(tid);
    int bdata_print = 0;

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
		    if (sizeof(float) == size) {
		        /* if (H5Tequal(tid, H5T_NATIVE_FLOAT)) */
				memcpy(&tmp_float, ptr, sizeof(float));
				this_str = (char*) malloc(25);
				sprintf(this_str, "%g", tmp_float);
			}
			else if (sizeof(double) == size) {
                /* if (H5Tequal(tid, H5T_NATIVE_DOUBLE)) */
				memcpy(&tmp_double, ptr, sizeof(double));
				this_str = (char*) malloc(25);
				sprintf(this_str, "%g", tmp_double);
			}
#if H5_SIZEOF_LONG_DOUBLE !=0
            else if (sizeof(long double) == size) {
                /* if (H5Tequal(tid, H5T_NATIVE_LDOUBLE)) */
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
				}

				/* Check for NULL pointer for string */
				if (tmp_str == NULL) {
					this_str = (char *) malloc(5);
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
		    if (sizeof(char) == size) {
		        if(H5T_SGN_NONE == nsign) {
		            /* if (H5Tequal(tid, H5T_NATIVE_UCHAR)) */
		            memcpy(&tmp_uchar, ptr, sizeof(unsigned char));
		            this_str = (char*) malloc(7);
		            sprintf(this_str, "%u", tmp_uchar);
		        }
		        else {
		            /* if (H5Tequal(tid, H5T_NATIVE_SCHAR)) */
		            memcpy(&tmp_char, ptr, sizeof(char));
		            this_str = (char*) malloc(7);
		            sprintf(this_str, "%hhd", tmp_char);
		        }
		    }
		    else if (sizeof(int) == size) {
		        if(H5T_SGN_NONE == nsign) {
		            /* if (H5Tequal(tid, H5T_NATIVE_UINT)) */
		            memcpy(&tmp_uint, ptr, sizeof(unsigned int));
		            this_str = (char*) malloc(14);
		            sprintf(this_str, "%u", tmp_uint);
		        }
		        else {
		            /* if (H5Tequal(tid, H5T_NATIVE_INT)) */
		            memcpy(&tmp_int, ptr, sizeof(int));
		            this_str = (char*) malloc(14);
		            sprintf(this_str, "%d", tmp_int);
		        }
		    }
			else if (sizeof(short) == size) {
                if(H5T_SGN_NONE == nsign) {
                    /* if (H5Tequal(tid, H5T_NATIVE_USHORT)) */
                    memcpy(&tmp_ushort, ptr, sizeof(unsigned short));
                    this_str = (char*) malloc(9);
                    sprintf(this_str, "%u", tmp_ushort);
                }
                else {
                    /* if (H5Tequal(tid, H5T_NATIVE_SHORT)) */
                    memcpy(&tmp_short, ptr, sizeof(short));
                    this_str = (char*) malloc(9);
                    sprintf(this_str, "%d", tmp_short);
                }
			}
			else if (sizeof(long) == size) {
                if(H5T_SGN_NONE == nsign) {
                    /* if (H5Tequal(tid, H5T_NATIVE_ULONG)) */
                    memcpy(&tmp_ulong, ptr, sizeof(unsigned long));
                    this_str = (char*) malloc(23);
                    sprintf(this_str, "%lu", tmp_ulong);
                }
                else {
                    /* if (H5Tequal(tid, H5T_NATIVE_LONG)) */
                    memcpy(&tmp_long, ptr, sizeof(long));
                    this_str = (char*) malloc(23);
                    sprintf(this_str, "%ld", tmp_long);
                }
			}
			else if (sizeof(long long) == size) {
                if(H5T_SGN_NONE == nsign) {
                    /* if (H5Tequal(tid, H5T_NATIVE_ULLONG)) */
                    memcpy(&tmp_ullong, ptr, sizeof(unsigned long long));
                    this_str = (char*) malloc(25);
                    sprintf(this_str, fmt_ullong, tmp_ullong);
                }
                else {
                    /* if (H5Tequal(tid, H5T_NATIVE_LLONG)) */
                    memcpy(&tmp_llong, ptr, sizeof(long long));
                    this_str = (char*) malloc(25);
                    sprintf(this_str, fmt_llong, tmp_llong);
                }
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
					h5str_sprintf(str, container, mtid, cptr + offset, expand_data);
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
		    if (h5str_is_zero(ptr, size)) {
		        h5str_append(str, "NULL");
		    }
		    else {
		        if (H5R_DSET_REG_REF_BUF_SIZE == size) {
		            /* if (H5Tequal(tid, H5T_STD_REF_DSETREG)) */
		            /*
		             * Dataset region reference --
		             * show the type and the referenced object
		             */
		            char         ref_name[1024];
                    hid_t        region_obj;
                    hid_t        region;
		            H5S_sel_type region_type;

		            /* get name of the dataset the region reference points to using H5Rget_name */
		            region_obj = H5Rdereference2(container, H5P_DEFAULT, H5R_DATASET_REGION, ptr);
		            if (region_obj >= 0) {
		                region = H5Rget_region(container, H5R_DATASET_REGION, ptr);
		                if (region >= 0) {
		                	if(expand_data) {
								region_type = H5Sget_select_type(region);
								if(region_type==H5S_SEL_POINTS) {
									h5str_dump_region_points_data(str, region, region_obj);
								}
								else {
									h5str_dump_region_blocks_data(str, region, region_obj);
								}
		                	}
		                	else {
								if(H5Rget_name(region_obj, H5R_DATASET_REGION, ptr, (char*)ref_name, 1024) >= 0) {
									h5str_append(str, ref_name);
								}

								region_type = H5Sget_select_type(region);

								if(region_type==H5S_SEL_POINTS) {
									h5str_append(str, " REGION_TYPE POINT");
									h5str_dump_region_points(str, region, region_obj);
								}
								else {
									h5str_append(str, " REGION_TYPE BLOCK");
									h5str_dump_region_blocks(str, region, region_obj);
								}
		                	}

		                    H5Sclose(region);
		                }
		                H5Dclose(region_obj);
		            }
		        }
		        else if (H5R_OBJ_REF_BUF_SIZE == size) {
		            /* if (H5Tequal(tid, H5T_STD_REF_OBJ)) */
                    /*
                     * Object references -- show the type and OID of the referenced
                     * object.
                     */
                    H5O_info_t  oi;
                    hid_t       obj;

		            this_str = (char*) malloc(64);
                    obj = H5Rdereference2(container, H5P_DEFAULT, H5R_OBJECT, ptr);
                    H5Oget_info(obj, &oi);

                    /* Print object data and close object */
                    sprintf(this_str, "%u-%lu", (unsigned) oi.type, oi.addr);
                    H5Oclose(obj);
		        }
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
					h5str_sprintf(str, container, mtid, cptr + i * size, expand_data);
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
					h5str_sprintf(str, container, mtid, ((char *) (vlptr->p)) + i * size, expand_data);
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
        this_strlen = strlen(str->s);
        free(this_str);
    }

    return this_strlen;
}

/*-------------------------------------------------------------------------
 * Purpose: Print the data values from a dataset referenced by region blocks.
 *
 * Description:
 *      This is a special case subfunction to print the data in a region reference of type blocks.
 *
 * Return:
 *      The function returns FAIL if there was an error, otherwise SUCEED
 *-------------------------------------------------------------------------
 */
int h5str_print_region_data_blocks(hid_t region_id,
        h5str_t *str, int ndims, hid_t type_id, hssize_t nblocks, hsize_t *ptdata)
{
    hsize_t     *dims1 = NULL;
    hsize_t     *start = NULL;
    hsize_t     *count = NULL;
    hsize_t      blkndx;
    hsize_t      total_size[H5S_MAX_RANK];
    unsigned int region_flags; /* buffer extent flags */
    hsize_t      numelem;
    hsize_t      numindex;
    size_t       jndx;
    unsigned     indx;
    int          type_size;
    int          ret_value = SUCCEED;
    hid_t        mem_space = -1;
    hid_t        sid1 = -1;
    void        *region_buf = NULL;

    /* Get the dataspace of the dataset */
    if((sid1 = H5Dget_space(region_id)) >= 0) {

        /* Allocate space for the dimension array */
        if((dims1 = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {

            /* find the dimensions of each data space from the block coordinates */
            numelem = 1;
            for (jndx = 0; jndx < ndims; jndx++) {
                dims1[jndx] = ptdata[jndx + ndims] - ptdata[jndx] + 1;
                numelem = dims1[jndx] * numelem;
            }

            /* Create dataspace for reading buffer */
            if((mem_space = H5Screate_simple(ndims, dims1, NULL)) >= 0) {
                if((type_size = H5Tget_size(type_id)) > 0) {
                    if((region_buf = malloc(type_size * (size_t)numelem)) != NULL) {
                        /* Select (x , x , ..., x ) x (y , y , ..., y ) hyperslab for reading memory dataset */
                        /*          1   2        n      1   2        n                                       */
                        if((start = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
                            if((count = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
                                for (blkndx = 0; blkndx < nblocks; blkndx++) {
                                    for (indx = 0; indx < ndims; indx++) {
                                        start[indx] = ptdata[indx + blkndx * ndims * 2];
                                        count[indx] = dims1[indx];
                                    }

                                    if(H5Sselect_hyperslab(sid1, H5S_SELECT_SET, start, NULL, count, NULL) >= 0) {
                                        if(H5Dread(region_id, type_id, mem_space, sid1, H5P_DEFAULT, region_buf) >= 0) {
                                            if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) {
                                                for (numindex = 0; numindex < numelem; numindex++) {
                                                    h5str_sprintf(str, region_id, type_id, ((char*)region_buf + numindex * type_size), 1);

                                                    if (numindex + 1 < numelem)
                                                        h5str_append(str, ", ");
                                                } /* end for (jndx = 0; jndx < numelem; jndx++, region_elmtno++, ctx.cur_elmt++) */
                                            } /* end if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) */
                                        } /* end if(H5Dread(region_id, type_id, mem_space, sid1, H5P_DEFAULT, region_buf) >= 0) */
                                    } /* end if(H5Sselect_hyperslab(sid1, H5S_SELECT_SET, start, NULL, count, NULL) >= 0) */
                                } /* end for (blkndx = 0; blkndx < nblocks; blkndx++) */

                                free(count);
                            } /* end if((count = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
                            else
                                ret_value = -1;

                            free(start);
                        } /* end if((start = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
                        else
                            ret_value = -1;

                        free(region_buf);
                    } /* end if((region_buf = HDmalloc(type_size * (size_t)numelem)) != NULL) */
                    else
                        ret_value = -1;
                } /* end if((type_size = H5Tget_size(type_id)) > 0) */
                else
                    ret_value = -1;

                if(H5Sclose(mem_space) < 0)
                    ret_value = -1;
            } /* end if((mem_space = H5Screate_simple(ndims, dims1, NULL)) >= 0) */
            else
                ret_value = -1;

            free(dims1);
        } /* end if((dims1 = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
        else
            ret_value = -1;

        if(H5Sclose(sid1) < 0)
            ret_value = -1;
    } /* end if((sid1 = H5Dget_space(region_id)) >= 0) */
    else
        ret_value = -1;

    return ret_value;
}

int h5str_dump_region_blocks_data(h5str_t *str, hid_t region, hid_t region_id)
{
    int        ret_value = 0;
    hssize_t   nblocks;
    hsize_t    alloc_size;
    hsize_t   *ptdata;
    hid_t      dtype = -1;
    hid_t      type_id = -1;
    char       tmp_str[256];
    int        ndims = H5Sget_simple_extent_ndims(region);

    /*
     * This function fails if the region does not have blocks.
     */
    H5E_BEGIN_TRY {
        nblocks = H5Sget_select_hyper_nblocks(region);
    } H5E_END_TRY;

    /* Print block information */
    if (nblocks > 0) {
        int i;

        alloc_size = nblocks * ndims * 2 * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_hyper_blocklist(region, (hsize_t) 0,
                    (hsize_t) nblocks, ptdata);


            if((dtype = H5Dget_type(region_id)) >= 0) {
                if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) {

                    h5str_print_region_data_blocks(region_id, str, ndims, type_id, nblocks, ptdata);

                    if(H5Tclose(type_id) < 0)
                        ret_value = -1;
                } /* end if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) */
                else
                    ret_value = -1;

                if(H5Tclose(dtype) < 0)
                    ret_value = -1;
            } /* end if((dtype = H5Dget_type(region_id)) >= 0) */
            else
                ret_value = -1;
            free(ptdata);
        } /* if (alloc_size == (hsize_t)((size_t)alloc_size)) */
    } /* if (nblocks > 0) */

    return ret_value;
}

int h5str_dump_region_blocks(h5str_t *str, hid_t region, hid_t region_id)
{
    int        ret_value = 0;
    hssize_t   nblocks;
    hsize_t    alloc_size;
    hsize_t   *ptdata;
    hid_t      dtype = -1;
    hid_t      type_id = -1;
    char       tmp_str[256];
    int        ndims = H5Sget_simple_extent_ndims(region);

    /*
     * This function fails if the region does not have blocks.
     */
    H5E_BEGIN_TRY {
        nblocks = H5Sget_select_hyper_nblocks(region);
    } H5E_END_TRY;

    /* Print block information */
    if (nblocks > 0) {
        int i;

        alloc_size = nblocks * ndims * 2 * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_hyper_blocklist(region, (hsize_t) 0,
                    (hsize_t) nblocks, ptdata);

            h5str_append(str, " {");
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
            h5str_append(str, " }");

            free(ptdata);
        } /* if (alloc_size == (hsize_t)((size_t)alloc_size)) */
    } /* if (nblocks > 0) */

    return ret_value;
}

/*-------------------------------------------------------------------------
 * Purpose: Print the data values from a dataset referenced by region points.
 *
 * Description:
 *      This is a special case subfunction to print the data in a region reference of type points.
 *
 * Return:
 *      The function returns FAIL on error, otherwise SUCCEED
 *-------------------------------------------------------------------------
 */
int
h5str_print_region_data_points(hid_t region_space, hid_t region_id,
        h5str_t *str, int ndims, hid_t type_id, hssize_t npoints, hsize_t *ptdata)
{
    hsize_t        *dims1 = NULL;
    hsize_t         total_size[H5S_MAX_RANK];
    size_t          jndx;
    unsigned        indx;
    int             type_size;
    int             ret_value = SUCCEED;
    unsigned int    region_flags; /* buffer extent flags */
    hid_t           mem_space = -1;
    void           *region_buf = NULL;
    char            tmp_str[256];

    /* Allocate space for the dimension array */
    if((dims1 = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {

        dims1[0] = npoints;

        /* Create dataspace for reading buffer */
        if((mem_space = H5Screate_simple(1, dims1, NULL)) >= 0) {

            if((type_size = H5Tget_size(type_id)) > 0) {

                if((region_buf = malloc(type_size * (size_t)npoints)) != NULL) {

                    if(H5Dread(region_id, type_id, mem_space, region_space, H5P_DEFAULT, region_buf) >= 0) {

                        for (jndx = 0; jndx < npoints; jndx++) {
                            if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) {

                                h5str_sprintf(str, region_id, type_id, ((char*)region_buf + jndx * type_size), 1);

                                if (jndx + 1 < npoints)
                                    h5str_append(str, ", ");

                            } /* end if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) */
                        } /* end for (jndx = 0; jndx < npoints; jndx++, elmtno++) */
                    } /* end if(H5Dread(region_id, type_id, mem_space, region_space, H5P_DEFAULT, region_buf) >= 0) */
                    else
                        ret_value = -1;

                    free(region_buf);
                } /* end if((region_buf = HDmalloc(type_size * (size_t)npoints)) != NULL) */
                else
                    ret_value = -1;
            } /* end if((type_size = H5Tget_size(type_id)) > 0) */
            else
                ret_value = -1;

            if(H5Sclose(mem_space) < 0)
                ret_value = -1;
        } /* end if((mem_space = H5Screate_simple(1, dims1, NULL)) >= 0) */
        else
            ret_value = -1;
        free(dims1);
    } /* end if((dims1 = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
    else
        ret_value = -1;

    return ret_value;
}

int h5str_dump_region_points_data(h5str_t *str, hid_t region, hid_t region_id)
{
    int        ret_value = 0;
    hssize_t   npoints;
    hsize_t    alloc_size;
    hsize_t   *ptdata;
    char       tmp_str[256];
    hid_t      dtype = -1;
    hid_t      type_id = -1;
    int        ndims = H5Sget_simple_extent_ndims(region);

    /*
     * This function fails if the region does not have points.
     */
    H5E_BEGIN_TRY {
        npoints = H5Sget_select_elem_npoints(region);
    } H5E_END_TRY;

    /* Print point information */
    if (npoints > 0) {
        int i;

        alloc_size = npoints * ndims * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_elem_pointlist(region, (hsize_t) 0,
                    (hsize_t) npoints, ptdata);

            if((dtype = H5Dget_type(region_id)) >= 0) {
                if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) {

                    h5str_print_region_data_points(region, region_id,
                            str, ndims, type_id, npoints, ptdata);

                    if(H5Tclose(type_id) < 0)
                        ret_value = -1;
                } /* end if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) */
                else
                    ret_value = -1;

                if(H5Tclose(dtype) < 0)
                    ret_value = -1;
            } /* end if((dtype = H5Dget_type(region_id)) >= 0) */
            else
                ret_value = -1;
            free(ptdata);
        }
    }

    return ret_value;
}

int h5str_dump_region_points(h5str_t *str, hid_t region, hid_t region_id)
{
    int        ret_value = 0;
    hssize_t   npoints;
    hsize_t    alloc_size;
    hsize_t   *ptdata;
    char       tmp_str[256];
    hid_t      dtype = -1;
    hid_t      type_id = -1;
    int        ndims = H5Sget_simple_extent_ndims(region);

    /*
     * This function fails if the region does not have points.
     */
    H5E_BEGIN_TRY {
        npoints = H5Sget_select_elem_npoints(region);
    } H5E_END_TRY;

    /* Print point information */
    if (npoints > 0) {
        int i;

        alloc_size = npoints * ndims * sizeof(ptdata[0]);
        if (alloc_size == (hsize_t)((size_t) alloc_size)) {
            ptdata = (hsize_t *) malloc((size_t) alloc_size);
            H5Sget_select_elem_pointlist(region, (hsize_t) 0,
                    (hsize_t) npoints, ptdata);

            h5str_append(str, " {");
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
            h5str_append(str, " }");

            free(ptdata);
        }
    }

    return ret_value;
}

int h5str_is_zero(const void *_mem, size_t size) {
    const unsigned char *mem = (const unsigned char *) _mem;

    while (size-- > 0)
        if (mem[size])
            return 0;

    return 1;
}

/*-------------------------------------------------------------------------
 * Function: h5str_detect_vlen_str
 *
 * Purpose: Recursive check for variable length string of a datatype.
 *
 * Return:
 *    TRUE : type conatains any variable length string
 *    FALSE : type doesn't contain any variable length string
 *    Negative value: error occur
 *
 *-------------------------------------------------------------------------
 */
htri_t
h5str_detect_vlen_str(hid_t tid)
{
    H5T_class_t tclass = H5T_NO_CLASS;
    htri_t ret = 0;

    ret = H5Tis_variable_str(tid);
    if((ret == 1) || (ret < 0))
        goto done;

    tclass = H5Tget_class(tid);
    if(tclass == H5T_ARRAY || tclass == H5T_VLEN) {
        hid_t btid = H5Tget_super(tid);

        if(btid < 0) {
            ret = (htri_t)btid;
            goto done;
        }
        ret = h5str_detect_vlen_str(btid);
        if((ret == 1) || (ret < 0)) {
            H5Tclose(btid);
            goto done;
        }
    }
    else if(tclass == H5T_COMPOUND) {
        int i = 0;
        int n = H5Tget_nmembers(tid);

        if(n < 0) {
            n = ret;
            goto done;
        }

        for(i = 0; i < n; i++) {
            hid_t mtid = H5Tget_member_type(tid, i);

            ret = h5str_detect_vlen_str(mtid);
            if((ret == 1) || (ret < 0)) {
                H5Tclose(mtid);
                goto done;
            }
            H5Tclose(mtid);
        }
    }

done:
    return ret;
}

/*-------------------------------------------------------------------------
 * Function: h5str_get_native_type
 *
 * Purpose: Wrapper around H5Tget_native_type() to work around
 *          Problems with bitfields.
 *
 * Return: Success:    datatype ID
 *         Failure:    FAIL
 *-------------------------------------------------------------------------
 */
hid_t h5str_get_native_type(hid_t type)
{
    hid_t p_type;
    H5T_class_t type_class;

    type_class = H5Tget_class(type);
    if(type_class==H5T_BITFIELD)
        p_type=H5Tcopy(type);
    else
        p_type = H5Tget_native_type(type,H5T_DIR_DEFAULT);

    return(p_type);
}


/*-------------------------------------------------------------------------
 * Function: h5str_get_little_endian_type
 *
 * Purpose: Get a little endian type from a file type
 *
 * Return: Success:    datatype ID
 *         Failure:    FAIL
 *-------------------------------------------------------------------------
 */
hid_t h5str_get_little_endian_type(hid_t tid)
{
    hid_t       p_type=-1;
    H5T_class_t type_class;
    size_t      size;
    H5T_sign_t  sign;

    type_class = H5Tget_class(tid);
    size       = H5Tget_size(tid);
    sign       = H5Tget_sign(tid);

    switch( type_class )
    {
    case H5T_INTEGER:
    {
        if ( size == 1 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I8LE);
        else if ( size == 2 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I16LE);
        else if ( size == 4 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I32LE);
        else if ( size == 8 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I64LE);
        else if ( size == 1 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U8LE);
        else if ( size == 2 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U16LE);
        else if ( size == 4 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U32LE);
        else if ( size == 8 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U64LE);
    }
    break;

    case H5T_FLOAT:
        if ( size == 4)
            p_type=H5Tcopy(H5T_IEEE_F32LE);
        else if ( size == 8)
            p_type=H5Tcopy(H5T_IEEE_F64LE);
        break;

    case H5T_TIME:
    case H5T_BITFIELD:
    case H5T_OPAQUE:
    case H5T_STRING:
    case H5T_COMPOUND:
    case H5T_REFERENCE:
    case H5T_ENUM:
    case H5T_VLEN:
    case H5T_ARRAY:
        break;

    default:
        break;

    }

    return(p_type);
}

/*-------------------------------------------------------------------------
 * Function: h5str_get_big_endian_type
 *
 * Purpose: Get a big endian type from a file type
 *
 * Return: Success:    datatype ID
 *         Failure:    FAIL
 *-------------------------------------------------------------------------
 */
hid_t h5str_get_big_endian_type(hid_t tid)
{
    hid_t       p_type=-1;
    H5T_class_t type_class;
    size_t      size;
    H5T_sign_t  sign;

    type_class = H5Tget_class(tid);
    size       = H5Tget_size(tid);
    sign       = H5Tget_sign(tid);

    switch( type_class )
    {
    case H5T_INTEGER:
    {
        if ( size == 1 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I8BE);
        else if ( size == 2 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I16BE);
        else if ( size == 4 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I32BE);
        else if ( size == 8 && sign == H5T_SGN_2)
            p_type=H5Tcopy(H5T_STD_I64BE);
        else if ( size == 1 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U8BE);
        else if ( size == 2 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U16BE);
        else if ( size == 4 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U32BE);
        else if ( size == 8 && sign == H5T_SGN_NONE)
            p_type=H5Tcopy(H5T_STD_U64BE);
    }
    break;

    case H5T_FLOAT:
        if ( size == 4)
            p_type=H5Tcopy(H5T_IEEE_F32BE);
        else if ( size == 8)
            p_type=H5Tcopy(H5T_IEEE_F64BE);
        break;

    case H5T_TIME:
    case H5T_BITFIELD:
    case H5T_OPAQUE:
    case H5T_STRING:
    case H5T_COMPOUND:
    case H5T_REFERENCE:
    case H5T_ENUM:
    case H5T_VLEN:
    case H5T_ARRAY:
        break;

    default:
        break;

    }

    return(p_type);
}

/*-------------------------------------------------------------------------
 * Function: h5str_detect_vlen
 *
 * Purpose: Recursive check for any variable length data in given type.
 *
 * Return:
 *    1 : type conatains any variable length data
 *    0 : type doesn't contain any variable length data
 *    Negative value: error occur
 *-------------------------------------------------------------------------
 */
htri_t
h5str_detect_vlen(hid_t tid)
{
    htri_t ret;

    /* recursive detect any vlen data values in type (compound, array ...) */
    ret = H5Tdetect_class(tid, H5T_VLEN);
    if((ret == 1) || (ret < 0))
        goto done;

    /* recursive detect any vlen string in type (compound, array ...) */
    ret = h5str_detect_vlen_str(tid);
    if((ret == 1) || (ret < 0))
        goto done;

done:
    return ret;
}

/*-------------------------------------------------------------------------
 * Function: render_bin_output
 *
 * Purpose: Write one element of memory buffer to a binary file stream
 *
 * Return: Success:    SUCCEED
 *         Failure:    FAIL
 *-------------------------------------------------------------------------
 */
int h5str_render_bin_output(FILE *stream, hid_t container, hid_t tid, void *_mem, hsize_t block_nelmts)
{
    int                ret_value = 0;
    unsigned char     *mem  = (unsigned char*)_mem;
    size_t             size;   /* datum size */
    hsize_t            block_index;
    H5T_class_t        type_class;

    if((size = H5Tget_size(tid)) > 0) {

        if((type_class = H5Tget_class(tid)) >= 0) {

            switch (type_class) {
                case H5T_INTEGER:
                case H5T_FLOAT:
                case H5T_ENUM:
                    block_index = block_nelmts * size;
                    while(block_index > 0) {
                        size_t bytes_in        = 0;    /* # of bytes to write  */
                        size_t bytes_wrote     = 0;    /* # of bytes written   */
                        size_t item_size       = size;    /* size of items in bytes */

                        if(block_index > sizeof(size_t))
                            bytes_in = sizeof(size_t);
                        else
                            bytes_in = (size_t)block_index;

                        bytes_wrote = fwrite(mem, 1, bytes_in, stream);

                        if(bytes_wrote != bytes_in || (0 == bytes_wrote && ferror(stream))) {
                            ret_value = -1;
                            break;
                        }

                        block_index -= (hsize_t)bytes_wrote;
                        mem = mem + bytes_wrote;
                    }
                    break;
                case H5T_STRING:
                {
                    unsigned int    i;
                    H5T_str_t       pad;
                    char           *s;
                    unsigned char   tempuchar;

                    pad = H5Tget_strpad(tid);

                    for (block_index = 0; block_index < block_nelmts; block_index++) {
                        mem = ((unsigned char*)_mem) + block_index * size;

                        if (H5Tis_variable_str(tid)) {
                            s = *(char**) mem;
                            if (s != NULL)
                                size = strlen(s);
                        }
                        else {
                            s = (char *) mem;
                        }
                        for (i = 0; i < size && (s[i] || pad != H5T_STR_NULLTERM); i++) {
                            memcpy(&tempuchar, &s[i], sizeof(unsigned char));
                            if (1 != fwrite(&tempuchar, sizeof(unsigned char), 1, stream)) {
                                ret_value = -1;
                                break;
                            }
                        } /* i */
                        if(ret_value < 0)
                            break;
                    } /* for (block_index = 0; block_index < block_nelmts; block_index++) */
                }
                break;
                case H5T_COMPOUND:
                {
                    unsigned j;
                    hid_t    memb;
                    unsigned nmembs;
                    size_t   offset;

                    nmembs = H5Tget_nmembers(tid);

                    for (block_index = 0; block_index < block_nelmts; block_index++) {
                        mem = ((unsigned char*)_mem) + block_index * size;
                        for (j = 0; j < nmembs; j++) {
                            offset = H5Tget_member_offset(tid, j);
                            memb   = H5Tget_member_type(tid, j);

                            if (h5str_render_bin_output(stream, container, memb, mem + offset, 1) < 0) {
                                H5Tclose(memb);
                                ret_value = -1;
                                break;
                            }

                            H5Tclose(memb);
                        }
                        if(ret_value < 0)
                            break;
                    }
                }
                break;
                case H5T_ARRAY:
                {
                    int     k, ndims;
                    hsize_t i, dims[H5S_MAX_RANK], temp_nelmts, nelmts;
                    hid_t   memb;

                    /* get the array's base datatype for each element */
                    memb = H5Tget_super(tid);
                    ndims = H5Tget_array_ndims(tid);
                    H5Tget_array_dims2(tid, dims);

                    /* calculate the number of array elements */
                    for (k = 0, nelmts = 1; k < ndims; k++) {
                        temp_nelmts = nelmts;
                        temp_nelmts *= dims[k];
                        nelmts = (size_t) temp_nelmts;
                    }

                    for (block_index = 0; block_index < block_nelmts; block_index++) {
                        mem = ((unsigned char*)_mem) + block_index * size;
                        /* dump the array element */
                        if (h5str_render_bin_output(stream, container, memb, mem, nelmts) < 0) {
                            ret_value = -1;
                            break;
                        }
                    }
                    H5Tclose(memb);
                }
                break;
                case H5T_VLEN:
                {
                    unsigned int i;
                    hsize_t      nelmts;
                    hid_t        memb;

                    /* get the VL sequences's base datatype for each element */
                    memb = H5Tget_super(tid);

                    for (block_index = 0; block_index < block_nelmts; block_index++) {
                        mem = ((unsigned char*)_mem) + block_index * size;
                        /* Get the number of sequence elements */
                        nelmts = ((hvl_t *) mem)->len;

                        /* dump the array element */
                        if (h5str_render_bin_output(stream, container, memb, ((char *) (((hvl_t *) mem)->p)), nelmts) < 0) {
                            ret_value = -1;
                            break;
                        }
                    }
                    H5Tclose(memb);
                }
                break;
                case H5T_REFERENCE:
                {
                    if (H5Tequal(tid, H5T_STD_REF_DSETREG)) {
                        /* region data */
                        hid_t   region_id, region_space;
                        H5S_sel_type region_type;

                        for (block_index = 0; block_index < block_nelmts; block_index++) {
                            mem = ((unsigned char*)_mem) + block_index * size;
                            region_id = H5Rdereference2(container, H5P_DEFAULT, H5R_DATASET_REGION, mem);
                            if (region_id >= 0) {
                                region_space = H5Rget_region(container, H5R_DATASET_REGION, mem);
                                if (region_space >= 0) {
                                    region_type = H5Sget_select_type(region_space);
                                    if(region_type == H5S_SEL_POINTS)
                                        ret_value = render_bin_output_region_points(stream, region_space, region_id, container);
                                    else
                                        ret_value = render_bin_output_region_blocks(stream, region_space, region_id, container);
                                    H5Sclose(region_space);
                                } /* end if (region_space >= 0) */
                                H5Dclose(region_id);
                            } /* end if (region_id >= 0) */
                            if(ret_value < 0)
                                break;
                        }
                    }
                    else if (H5Tequal(tid, H5T_STD_REF_OBJ)) {
                        ;
                    }
                }
                break;
                default:
                    for (block_index = 0; block_index < block_nelmts; block_index++) {
                        mem = ((unsigned char*)_mem) + block_index * size;
                        if (size != fwrite(mem, sizeof(char), size, stream)) {
                            ret_value = -1;
                            break;
                        }
                    }
                    break;
            }
        } /* end if((type_class = H5Tget_class(tid)) >= 0) */
        else
            ret_value = -1;
    } /* end if((size = H5Tget_size(tid)) > 0) */
    else
        ret_value = -1;

    return ret_value;
}

/*-------------------------------------------------------------------------
 * Purpose: Print the data values from a dataset referenced by region blocks.
 *
 * Description:
 *      This is a special case subfunction to print the data in a region reference of type blocks.
 *
 * Return:
 *      The function returns FAIL if there was an error, otherwise SUCEED
 *
 *-------------------------------------------------------------------------
 */
int render_bin_output_region_data_blocks(FILE *stream, hid_t region_id,
    hid_t container, int ndims, hid_t type_id, hssize_t nblocks, hsize_t *ptdata)
{
    hsize_t     *dims1 = NULL;
    hsize_t     *start = NULL;
    hsize_t     *count = NULL;
    hsize_t      numelem;
    hsize_t      numindex;
    hsize_t      total_size[H5S_MAX_RANK];
    int          jndx;
    int          type_size;
    hid_t        mem_space = -1;
    void        *region_buf = NULL;
    int          blkndx;
    hid_t        sid1 = -1;
    int          ret_value = SUCCEED;

    /* Get the dataspace of the dataset */
    if((sid1 = H5Dget_space(region_id)) >= 0) {
        /* Allocate space for the dimension array */
        if((dims1 = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
            /* find the dimensions of each data space from the block coordinates */
            numelem = 1;
            for (jndx = 0; jndx < ndims; jndx++) {
                dims1[jndx] = ptdata[jndx + ndims] - ptdata[jndx] + 1;
                numelem = dims1[jndx] * numelem;
            }

            /* Create dataspace for reading buffer */
            if((mem_space = H5Screate_simple(ndims, dims1, NULL)) >= 0) {
                if((type_size = H5Tget_size(type_id)) > 0) {
                    if((region_buf = malloc(type_size * (size_t)numelem)) != NULL) {
                        /* Select (x , x , ..., x ) x (y , y , ..., y ) hyperslab for reading memory dataset */
                        /*          1   2        n      1   2        n                                       */
                        if((start = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
                            if((count = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
                                for (blkndx = 0; blkndx < nblocks; blkndx++) {
                                    for (jndx = 0; jndx < ndims; jndx++) {
                                        start[jndx] = ptdata[jndx + blkndx * ndims * 2];
                                        count[jndx] = dims1[jndx];
                                    }

                                    if(H5Sselect_hyperslab(sid1, H5S_SELECT_SET, start, NULL, count, NULL) >= 0) {
                                        if(H5Dread(region_id, type_id, mem_space, sid1, H5P_DEFAULT, region_buf) >= 0) {
                                            if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) {
                                                ret_value = h5str_render_bin_output(stream, container, type_id, (char*)region_buf, numelem);
                                            } /* end if(H5Sget_simple_extent_dims(mem_space, total_size, NULL) >= 0) */
                                            else {
                                                ret_value = -1;
                                                break;
                                            }
                                        } /* end if(H5Dread(region_id, type_id, mem_space, sid1, H5P_DEFAULT, region_buf) >= 0) */
                                        else {
                                            ret_value = -1;
                                            break;
                                        }
                                    } /* end if(H5Sselect_hyperslab(sid1, H5S_SELECT_SET, start, NULL, count, NULL) >= 0) */
                                    else {
                                        ret_value = -1;
                                        break;
                                    }
                                    /* Render the region data element end */
                                } /* end for (blkndx = 0; blkndx < nblocks; blkndx++) */

                                free(count);
                            } /* end if((count = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
                            else
                                ret_value = -1;
                            free(start);
                        } /* end if((start = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
                        else
                            ret_value = -1;
                        free(region_buf);
                    } /* end if((region_buf = HDmalloc(type_size * (size_t)numelem)) != NULL) */
                    else
                        ret_value = -1;
                } /* end if((type_size = H5Tget_size(type_id)) > 0) */
                else
                    ret_value = -1;

                if(H5Sclose(mem_space) < 0)
                    ret_value = -1;
            } /* end if((mem_space = H5Screate_simple(ndims, dims1, NULL)) >= 0) */
            else
                ret_value = -1;
            free(dims1);
        } /* end if((dims1 = (hsize_t *) HDmalloc(sizeof(hsize_t) * ndims)) != NULL) */
        else
            ret_value = -1;
        if(H5Sclose(sid1) < 0)
            ret_value = -1;
    } /* end if((sid1 = H5Dget_space(region_id)) >= 0) */
    else
        ret_value = -1;

    return ret_value;
}

/*-------------------------------------------------------------------------
 * Purpose: Print some values from a dataset referenced by region blocks.
 *
 * Description:
 *      This is a special case subfunction to dump a region reference using blocks.
 *
 * Return:
 *      The function returns False if ERROR, otherwise True
 *
 *-------------------------------------------------------------------------
 */
int render_bin_output_region_blocks(FILE *stream, hid_t region_space, hid_t region_id, hid_t container)
{
    int          ret_value = SUCCEED;
    hssize_t     nblocks;
    hsize_t      alloc_size;
    hsize_t     *ptdata = NULL;
    int          ndims;
    hid_t        dtype;
    hid_t        type_id;

    if((nblocks = H5Sget_select_hyper_nblocks(region_space)) > 0) {
        /* Print block information */
        if((ndims = H5Sget_simple_extent_ndims(region_space)) >= 0) {
            alloc_size = nblocks * ndims * 2 * sizeof(ptdata[0]);
            if((ptdata = (hsize_t*) malloc((size_t) alloc_size)) != NULL) {
                if(H5Sget_select_hyper_blocklist(region_space, (hsize_t) 0, (hsize_t) nblocks, ptdata) >= 0) {
                    if((dtype = H5Dget_type(region_id)) >= 0) {
                        if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) {
                            ret_value = render_bin_output_region_data_blocks(stream, region_id, container, ndims,
                                    type_id, nblocks, ptdata);

                            if(H5Tclose(type_id) < 0)
                                ret_value = -1;
                        } /* end if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) */
                        else
                            ret_value = -1;

                        if(H5Tclose(dtype) < 0)
                            ret_value = -1;
                    } /* end if((dtype = H5Dget_type(region_id)) >= 0) */
                    else
                        ret_value = -1;
                } /* end if(H5Sget_select_hyper_blocklist(region_space, (hsize_t) 0, (hsize_t) nblocks, ptdata) >= 0) */
                else
                    ret_value = -1;

                free(ptdata);
            } /* end if((ptdata = (hsize_t*) HDmalloc((size_t) alloc_size)) != NULL) */
            else
                ret_value = -1;
        } /* end if((ndims = H5Sget_simple_extent_ndims(region_space)) >= 0) */
        else
            ret_value = -1;
    } /* end if((nblocks = H5Sget_select_hyper_nblocks(region_space)) > 0) */
    else
        ret_value = -1;

    return ret_value;
}

/*-------------------------------------------------------------------------
 * Purpose: Print the data values from a dataset referenced by region points.
 *
 * Description:
 *      This is a special case subfunction to print the data in a region reference of type points.
 *
 * Return:
 *      The function returns FAIL on error, otherwise SUCCEED
 *
 *-------------------------------------------------------------------------
 */
int render_bin_output_region_data_points(FILE *stream, hid_t region_space, hid_t region_id,
        hid_t container, int ndims, hid_t type_id, hssize_t npoints, hsize_t *ptdata)
{
    hsize_t *dims1 = NULL;
    int      jndx;
    int      type_size;
    hid_t    mem_space = -1;
    void    *region_buf = NULL;
    int      ret_value = SUCCEED;

    if((type_size = H5Tget_size(type_id)) > 0) {
        if((region_buf = malloc(type_size * (size_t)npoints)) != NULL) {
            /* Allocate space for the dimension array */
            if((dims1 = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) {
                dims1[0] = npoints;
                if((mem_space = H5Screate_simple(1, dims1, NULL)) >= 0) {
                    if(H5Dread(region_id, type_id, mem_space, region_space, H5P_DEFAULT, region_buf) >= 0) {
                        if(H5Sget_simple_extent_dims(region_space, dims1, NULL) >= 0) {
                            ret_value = h5str_render_bin_output(stream, container, type_id, (char*)region_buf, npoints);
                        } /* end if(H5Sget_simple_extent_dims(region_space, dims1, NULL) >= 0) */
                        else
                            ret_value = -1;
                    } /* end if(H5Dread(region_id, type_id, mem_space, region_space, H5P_DEFAULT, region_buf) >= 0) */
                    else
                        ret_value = -1;
                } /* end if((mem_space = H5Screate_simple(1, dims1, NULL)) >= 0) */
                else
                    ret_value = -1;

                free(dims1);
            } /* end if((dims1 = (hsize_t *) malloc(sizeof(hsize_t) * ndims)) != NULL) */
            else
                ret_value = -1;
            free(region_buf);
        } /* end if((region_buf = malloc(type_size * (size_t)npoints)) != NULL) */
        else
            ret_value = -1;

        if(H5Sclose(mem_space) < 0)
            ret_value = -1;
    } /* end if((type_size = H5Tget_size(type_id)) > 0) */
    else
        ret_value = -1;

    return ret_value;
}

/*-------------------------------------------------------------------------
 * Purpose: Print some values from a dataset referenced by region points.
 *
 * Description:
 *      This is a special case subfunction to dump a region reference using points.
 *
 * Return:
 *      The function returns False if the last dimension has been reached, otherwise True
 *
 *-------------------------------------------------------------------------
 */
int render_bin_output_region_points(FILE *stream, hid_t region_space, hid_t region_id, hid_t container)
{
    int      ret_value = SUCCEED;
    hssize_t npoints;
    hsize_t  alloc_size;
    hsize_t *ptdata;
    int      ndims;
    hid_t    dtype;
    hid_t    type_id;

    if((npoints = H5Sget_select_elem_npoints(region_space)) > 0) {
        /* Allocate space for the dimension array */
        if((ndims = H5Sget_simple_extent_ndims(region_space)) >= 0) {
            alloc_size = npoints * ndims * sizeof(ptdata[0]);
            if(NULL != (ptdata = (hsize_t *)malloc((size_t) alloc_size))) {
                if(H5Sget_select_elem_pointlist(region_space, (hsize_t) 0, (hsize_t) npoints, ptdata) >= 0) {
                    if((dtype = H5Dget_type(region_id)) >= 0) {
                        if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) {
                            ret_value = render_bin_output_region_data_points(stream, region_space, region_id,
                                    container, ndims, type_id, npoints, ptdata);

                            if(H5Tclose(type_id) < 0)
                                ret_value = -1;
                        } /* end if((type_id = H5Tget_native_type(dtype, H5T_DIR_DEFAULT)) >= 0) */
                        else
                            ret_value = -1;

                        if(H5Tclose(dtype) < 0)
                            ret_value = -1;
                    } /* end if((dtype = H5Dget_type(region_id)) >= 0) */
                    else
                        ret_value = -1;
                } /* end if(H5Sget_select_elem_pointlist(region_space, (hsize_t) 0, (hsize_t) npoints, ptdata) >= 0) */
                else
                    ret_value = -1;

                free(ptdata);
            } /* end if(NULL != (ptdata = (hsize_t *)malloc((size_t) alloc_size))) */
            else
                ret_value = -1;
        } /* end if((ndims = H5Sget_simple_extent_ndims(region_space)) >= 0) */
        else
            ret_value = -1;

    } /* end if((npoints = H5Sget_select_elem_npoints(region_space)) > 0) */
    else
        ret_value = -1;

    return ret_value;
}

int h5str_dump_simple_dset(FILE *stream, hid_t dset, int binary_order)
{
    int      ret_value = SUCCEED;
    hid_t               f_space = -1;                  /* file data space */
    hsize_t             elmtno;                   /* counter  */
    size_t              i;                        /* counter  */
    int                 ndims;
    int                 carry;                    /* counter carry value */
    hsize_t             zero[8];                  /* vector of zeros */
    unsigned int        flags;                    /* buffer extent flags */
    hsize_t             total_size[H5S_MAX_RANK]; /* total size of dataset*/

    /* Print info */
    size_t              p_type_nbytes;            /* size of memory type */
    hsize_t             p_nelmts;                 /* total selected elmts */

    /* Stripmine info */
    hsize_t             sm_size[H5S_MAX_RANK];    /* stripmine size */
    hsize_t             sm_nbytes;                /* bytes per stripmine */
    hsize_t             sm_nelmts;                /* elements per stripmine*/
    unsigned char      *sm_buf = NULL;            /* buffer for raw data */
    hid_t               sm_space = -1;                 /* stripmine data space */

    /* Hyperslab info */
    hsize_t             hs_offset[H5S_MAX_RANK];  /* starting offset */
    hsize_t             hs_size[H5S_MAX_RANK];    /* size this pass */
    hsize_t             hs_nelmts;                /* elements in request */

    /* VL data special information */
    unsigned int        vl_data = 0; /* contains VL datatypes */
    hid_t               p_type = -1;
    hid_t               f_type = -1;

    if(dset < 0) return -1;
    f_type = H5Dget_type(dset);
    if (binary_order == 1)
        p_type = h5str_get_native_type(f_type);
    else if (binary_order == 2)
        p_type = h5str_get_little_endian_type(f_type);
    else if (binary_order == 3)
        p_type = h5str_get_big_endian_type(f_type);
    else
        p_type = H5Tcopy(f_type);

    H5Tclose(f_type);

    if (p_type >= 0) {
        if((f_space = H5Dget_space(dset)) >= 0) {
            ndims = H5Sget_simple_extent_ndims(f_space);

            if ((size_t)ndims <= (sizeof(sm_size)/sizeof(sm_size[0]))) {
                H5Sget_simple_extent_dims(f_space, total_size, NULL);

                /* calculate the number of elements we're going to print */
                p_nelmts = 1;

                if (ndims > 0) {
                    for (i = 0; i < ndims; i++)
                        p_nelmts *= total_size[i];
                } /* end if */

                if (p_nelmts > 0) {
                    /* Check if we have VL data in the dataset's datatype */
                    if (h5str_detect_vlen(p_type) != 0)
                        vl_data = 1;

                    /*
                     * Determine the strip mine size and allocate a buffer. The strip mine is
                     * a hyperslab whose size is manageable.
                     */
                    sm_nbytes = p_type_nbytes = H5Tget_size(p_type);

                    if (ndims > 0) {
                        for (i = ndims; i > 0; --i) {
                            hsize_t size = H5TOOLS_BUFSIZE / sm_nbytes;
                            if ( size == 0) /* datum size > H5TOOLS_BUFSIZE */
                                size = 1;
                            sm_size[i - 1] = (((total_size[i - 1]) < (size)) ? (total_size[i - 1]) : (size));
                            sm_nbytes *= sm_size[i - 1];
                        }
                    }

                    if(sm_nbytes > 0) {
                        sm_buf = (unsigned char *)malloc((size_t)sm_nbytes);

                        sm_nelmts = sm_nbytes / p_type_nbytes;
                        sm_space = H5Screate_simple(1, &sm_nelmts, NULL);

                        /* The stripmine loop */
                        memset(hs_offset, 0, sizeof hs_offset);
                        memset(zero, 0, sizeof zero);

                        for (elmtno = 0; elmtno < p_nelmts; elmtno += hs_nelmts) {
                            /* Calculate the hyperslab size */
                            if (ndims > 0) {
                                for (i = 0, hs_nelmts = 1; i < ndims; i++) {
                                    hs_size[i] = (((total_size[i] - hs_offset[i]) < (sm_size[i])) ? (total_size[i] - hs_offset[i]) : (sm_size[i]));
                                    hs_nelmts *= hs_size[i];
                                }

                                H5Sselect_hyperslab(f_space, H5S_SELECT_SET, hs_offset, NULL, hs_size, NULL);
                                H5Sselect_hyperslab(sm_space, H5S_SELECT_SET, zero, NULL, &hs_nelmts, NULL);
                            }
                            else {
                                H5Sselect_all(f_space);
                                H5Sselect_all(sm_space);
                                hs_nelmts = 1;
                            }

                            /* Read the data */
                            if (H5Dread(dset, p_type, sm_space, f_space, H5P_DEFAULT, sm_buf) >= 0) {

                                if (binary_order == 99)
                                    ret_value = h5tools_dump_simple_data(stream, dset, p_type, sm_buf, hs_nelmts);
                                else
                                    ret_value = h5str_render_bin_output(stream, dset, p_type, sm_buf, hs_nelmts);

                                /* Reclaim any VL memory, if necessary */
                                if (vl_data)
                                    H5Dvlen_reclaim(p_type, sm_space, H5P_DEFAULT, sm_buf);
                            }
                            else {
                                ret_value = -1;
                                break;
                            }

                            if(ret_value < 0) break;

                            /* Calculate the next hyperslab offset */
                            for (i = ndims, carry = 1; i > 0 && carry; --i) {
                                hs_offset[i - 1] += hs_size[i - 1];

                                if (hs_offset[i - 1] == total_size[i - 1])
                                    hs_offset[i - 1] = 0;
                                else
                                    carry = 0;
                            }
                        }

                        if(sm_buf)
                            free(sm_buf);
                    }
                    if(sm_space >= 0 && H5Sclose(sm_space) < 0)
                        ret_value = -1;
                }
            }
            if(f_space >= 0 && H5Sclose(f_space) < 0)
                ret_value = -1;
        } /* end if((f_space = H5Dget_space(dset)) >= 0) */
        else
            ret_value = -1;

        if (p_type >= 0)
            H5Tclose(p_type);
    }
    return ret_value;
}

int h5tools_dump_simple_data(FILE *stream, hid_t container, hid_t type, void *_mem, hsize_t nelmts)
{
    int                ret_value = 0;
    int                line_count;
    unsigned char     *mem  = (unsigned char*)_mem;
    size_t             size;   /* datum size */
    H5T_class_t        type_class;
    hsize_t            i;         /*element counter  */
    h5str_t            buffer;    /*string into which to render */

    if((size = H5Tget_size(type)) > 0) {
        for (i = 0, line_count = 0; i < nelmts; i++, line_count++) {
            size_t bytes_in        = 0;    /* # of bytes to write  */
            size_t bytes_wrote     = 0;    /* # of bytes written   */
            void* memref = mem + i * size;

            /* Render the data element*/
            h5str_new(&buffer, 32 * size);
            bytes_in = h5str_sprintf(&buffer, container, type, memref, 1);
            if(i > 0) {
                fprintf(stream, ", ");
                if (line_count >= H5TOOLS_TEXT_BLOCK) {
                    line_count = 0;
                    fprintf(stream, "\n");
                }
            }
            fprintf(stream, "%s", buffer.s);
            h5str_free(&buffer);
        } /* end for (i = 0; i < nelmts... */
        fprintf(stream, "\n");
    } /* end if((size = H5Tget_size(tid)) > 0) */
    else
        ret_value = -1;

    return ret_value;
}

#ifdef __cplusplus
}
#endif
