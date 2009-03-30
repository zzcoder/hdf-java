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


JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VHmakegroup
( JNIEnv *env,
jclass oclass,
jint file_id,
jintArray tag_array, /* IN: int[] */
jintArray ref_array, /* IN: int[] */
jint n_objects,
jstring vgroup_name,
jstring vgroup_class)
{
int32 rval;
jint *tags;
jint *refs;
char *name;
char *cls;
    jboolean bb;

    tags = ENVPTR->GetIntArrayElements(ENVPAR tag_array,&bb);

    refs = ENVPTR->GetIntArrayElements(ENVPAR ref_array,&bb);

    name = (char *)ENVPTR->GetStringUTFChars(ENVPAR vgroup_name,0);

    cls = (char *)ENVPTR->GetStringUTFChars(ENVPAR vgroup_class,0);

    rval = VHmakegroup((int32) file_id, (int32 *) tags, (int32 *)refs,
        (int32) n_objects, (char *)name, (char *)cls);

    ENVPTR->ReleaseIntArrayElements(ENVPAR tag_array,tags,JNI_ABORT);
    ENVPTR->ReleaseIntArrayElements(ENVPAR ref_array,refs,JNI_ABORT);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vgroup_name,name);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vgroup_class,cls);

    return rval;
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VHstoredata
( JNIEnv *env,
jclass oclass,
jint file_id,
jstring fieldname,
jbyteArray buf, /* IN: byte[] */
jint n_records,
jint data_type,
jstring vdata_name,
jstring vdata_class)
{
int32 rval;
jbyte *buffer;
char *fldname;
char *name;
char *cls;
    jboolean bb;

    buffer = ENVPTR->GetByteArrayElements(ENVPAR buf,&bb);

    fldname = (char *)ENVPTR->GetStringUTFChars(ENVPAR fieldname,0);

    name = (char *)ENVPTR->GetStringUTFChars(ENVPAR vdata_name,0);

    cls = (char *)ENVPTR->GetStringUTFChars(ENVPAR vdata_class,0);


    rval = VHstoredata((int32) file_id, (char *)fldname,
        (uint8 *) buffer, (int32) n_records, (int32) data_type,
        (char *)name, (char *)cls);

    ENVPTR->ReleaseByteArrayElements(ENVPAR buf,buffer,JNI_ABORT);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vdata_name,name);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vdata_class,cls);
    ENVPTR->ReleaseStringUTFChars(ENVPAR fieldname,fldname);

    return rval;
}

JNIEXPORT jint JNICALL Java_hdf_h4_HDFLibrary_VHstoredatam
( JNIEnv *env,
jclass oclass,
jint file_id,
jstring fieldname,
jbyteArray buf, /* IN: byte[] */
jint n_records,
jint data_type,
jstring vdata_name,
jstring vdata_class,
jint order)
{
int32 rval;
jbyte *buffer;
char *fldname;
char *name;
char *cls;
jboolean bb;

    buffer = ENVPTR->GetByteArrayElements(ENVPAR buf,&bb);

    fldname = (char *)ENVPTR->GetStringUTFChars(ENVPAR fieldname,0);

    name = (char *)ENVPTR->GetStringUTFChars(ENVPAR vdata_name,0);

    cls = (char *)ENVPTR->GetStringUTFChars(ENVPAR vdata_class,0);

    rval = VHstoredatam((int32) file_id, (char *)fldname,
        (uint8 *) buffer, (int32) n_records, (int32) data_type,
        (char *)name, (char *)cls, (int32) order);

    ENVPTR->ReleaseByteArrayElements(ENVPAR buf,buffer,JNI_ABORT);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vdata_name,name);
    ENVPTR->ReleaseStringUTFChars(ENVPAR vdata_class,cls);
    ENVPTR->ReleaseStringUTFChars(ENVPAR fieldname,fldname);

    return rval;
}

#ifdef __cplusplus
}
#endif
