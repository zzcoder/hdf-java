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
#include "h4jni.h"
#include <jni.h>


JNIEXPORT jboolean JNICALL Java_hdf_h4_HDFLibrary_HXsetcreatedir
( JNIEnv *env,
jclass clss,
jstring dir)
{
    intn rval;
    char *str;

    if (dir != NULL) {
        str =(char *) ENVPTR->GetStringUTFChars(ENVPAR dir,0);
    } else {
        str = NULL;
    }

    rval = HXsetcreatedir((char *)str);

    if (str != NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR dir,str);
    }

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_hdf_h4_HDFLibrary_HXsetdir
( JNIEnv *env,
jclass clss,
jstring dir)
{
    intn rval;
    char *str;

    if (dir != NULL) {
        str =(char *) ENVPTR->GetStringUTFChars(ENVPAR dir,0);
    } else {
        str = NULL;
    }

    rval = HXsetdir(str);

    if (str != NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR dir,str);
    }

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

#ifdef __cplusplus
}
#endif
