/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF Java Products. The full HDF Java copyright       *
 * notice, including terms governing use, modification, and redistribution,  *
 * is contained in the file, COPYING.  COPYING can be found at the root of   *
 * the source code distribution tree. You can also access it online  at      *
 * http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
 * access to the file, you may request a copy from help@hdfgroup.org.        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


#ifndef H5UTIL_H__
#define H5UTIL_H__

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
