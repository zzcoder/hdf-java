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


#ifdef __cplusplus
extern "C" {
#endif

#include "hdf.h"
#include <jni.h>
#include "h4jni.h"


JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_HEvalue
( JNIEnv *env,
jclass clss,
jint level)
{
    return HEvalue((int32) level);
}

JNIEXPORT void JNICALL Java_hdf_h4_HDFDeprecated_HEprint
( JNIEnv *env,
jclass clss,
jobject stream,  /* FILE * to output to? */
jint level)
{
printf("HEprint called:\n");fflush(stdout);
    /* HEprint is not implemented
        because I don't know HOW to implement it !*/
    /* Exception....*/
    h4NotImplemented( env, "HEprint");
}

JNIEXPORT jstring JNICALL Java_hdf_h4_HDFLibrary_HEstring
( JNIEnv *env,
jclass clss,
jshort error_code)
{
    char * str;
    jstring rstring;

    str = (char *)HEstring((hdf_err_code_t)error_code);

    rstring = ENVPTR->NewStringUTF(ENVPAR str);

    return rstring;
}

#ifdef __cplusplus
}
#endif
