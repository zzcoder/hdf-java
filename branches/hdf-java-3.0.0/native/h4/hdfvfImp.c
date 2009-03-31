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


JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VFfieldesize
( JNIEnv *env,
jclass clss,
jint vdata_id,
int field_index)
{
    return (VFfieldesize((int32) vdata_id,  (int32) field_index));
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VFfieldisize
( JNIEnv *env,
jclass clss,
jint vdata_id,
int field_index)
{

    return (VFfieldisize((int32) vdata_id,  (int32) field_index));
}

JNIEXPORT jstring JNICALL Java_hdf_h4_HDFLibrary_VFfieldname
( JNIEnv *env,
jclass clss,
jint vdata_id,
int field_index)
{
    jstring rstring;
    char * str;

    str = VFfieldname((int32) vdata_id,  (int32) field_index);

    /* check for error */

    /* convert it to java string */
    rstring = ENVPTR->NewStringUTF(ENVPAR str);

    return rstring;
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VFfieldorder
( JNIEnv *env,
jclass clss,
jint vdata_id,
int field_index)
{

        return (VFfieldorder((int32) vdata_id,  (int32) field_index));
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VFfieldtype
( JNIEnv *env,
jclass clss,
jint vdata_id,
int field_index)
{

        return (VFfieldtype((int32) vdata_id,  (int32) field_index));
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VFnfields
( JNIEnv *env,
jclass clss,
jint key)
{
    return (VFnfields((int32) key));
}

#ifdef __cplusplus
}
#endif
