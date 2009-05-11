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

#ifndef H5UTIL_H__
#define H5UTIL_H__

#ifndef SUCCEED
#define SUCCEED     0
#endif

#ifndef FAIL
#define FAIL        (-1)
#endif

#define HGOTO_DONE(ret_val) {ret_value = ret_val; goto done;}

#ifdef H5_USE_16_API
#define HGOTO_ERROR(maj_id, min_id, ret_val, str) { \
    H5Eclear1(); \
   H5Epush1(__FILE__, __FUNCTION__, __LINE__, maj_id, min_id, str); \
   HGOTO_DONE(ret_val) \
}
#else
#define HGOTO_ERROR(maj_id, min_id, ret_val, str) { \
    H5Eclear2(H5E_DEFAULT); \
   H5Epush2(NULL, __FILE__, __FUNCTION__, __LINE__, H5E_ERR_CLS_g, maj_id, min_id, str); \
   HGOTO_DONE(ret_val) \
}
#endif

typedef struct h5str_t {
    char    *s;
    size_t    max;  /* the allocated size of the string */
} h5str_t;

void    h5str_new (h5str_t *str, size_t len);
void    h5str_free (h5str_t *str);
void    h5str_resize (h5str_t *str, size_t new_len);
char*    h5str_append (h5str_t *str, const char* cstr);
int        h5str_sprintf(h5str_t *str, hid_t container, hid_t tid, void *buf);
void    h5str_array_free(char **strs, size_t len);

#endif  /* H5UTIL_H__ */
