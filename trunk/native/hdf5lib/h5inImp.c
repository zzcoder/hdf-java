/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

/*
 *  This code is the C-interface called by Java programs to access the
 *  Reference API Functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */
#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include <jni.h>
#include <stdlib.h>

#define GOTO_H5IN_ERROR(error) {h5JNIFatalError(env, error); ret_value=-1; goto done;}

extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5INcreate
  (JNIEnv *env, jclass clss,
   jstring grp_name, jint grp_loc_id, jint property_list, jint data_loc_id, 
   jstring data_loc_name, jstring field_name, jlong max_mem_size)
{
    hid_t ret_value;
    char *gname, *dname, *fldname;

    gname = dname = fldname = NULL;

    if (grp_name == NULL)
        GOTO_H5IN_ERROR ("H5INcreate: name of the index group is NULL");

    if ( NULL == (gname = (char *)(*env)->GetStringUTFChars(env, grp_name, NULL)))
        GOTO_H5IN_ERROR ("H5INcreate: name of the index group not pinned");

    if (data_loc_name == NULL)
        GOTO_H5IN_ERROR ("H5INcreate: name of the index dataset is NULL");

    if ( NULL == (dname = (char *)(*env)->GetStringUTFChars(env, data_loc_name, NULL)))
        GOTO_H5IN_ERROR ("H5INcreate: name of the index dataset not pinned");

    if (field_name != NULL)
    {
        if ( NULL == (fldname = (char *)(*env)->GetStringUTFChars(env, field_name, NULL)))
            GOTO_H5IN_ERROR ("H5INcreate: name of the index field not pinned");
    }

    ret_value = H5INcreate(gname, (hid_t)grp_loc_id, (hid_t) property_list, 
        (hid_t)data_loc_id, dname, fldname, (hsize_t)max_mem_size);

done:

    if (gname)
        (*env)->ReleaseStringUTFChars(env, grp_name, gname);

    if (dname)
        (*env)->ReleaseStringUTFChars(env, data_loc_name, dname);

    if (fldname)
        (*env)->ReleaseStringUTFChars(env, field_name, fldname);

    return (jint)ret_value;

}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5INquery
   (JNIEnv *env, jclass clss,
    jint dset_id, jobjectArray keys, jobject ubounds, jobject lbounds, jint nkeys)
{
    hid_t ret_value;
    char **cKeys;
    int i;
    void *ub, *lb;
    char *upstr, *lowstr;

    cKeys = NULL;
    upstr = lowstr = NULL;

    if (NULL == keys || nkeys <=0)
        GOTO_H5IN_ERROR ("H5INquery: no key to query");

    if (NULL == ubounds || NULL == lbounds)
        GOTO_H5IN_ERROR ("H5INquery: query bound is NULL");

    cKeys = (char **)malloc(nkeys*sizeof (char *));
    memset(cKeys, 0, nkeys*sizeof (char *));
    for (i=0; i<nkeys; i++) {
        jstring theKey;
        char *cstr;
        theKey = (jstring)(*env)->GetObjectArrayElement(env, keys, i);
        cstr = (char *)(*env)->GetStringUTFChars(env, theKey, 0);
        cKeys[i] = (char *)malloc(strlen(cstr)+1);
        strcpy(cKeys[i], cstr);
        (*env)->ReleaseStringUTFChars(env, theKey, cstr);
    }

    if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[Z")) ) {
        jboolean up, low, *ptr;
        jbooleanArray ja;
        ja =(jbooleanArray)ubounds;
        ptr = (*env)->GetBooleanArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseBooleanArrayElements(env, ja, ptr, 0);
        ja = (jbooleanArray)lbounds;
        ptr = (*env)->GetBooleanArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseBooleanArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[B")) ) {
        jbyte up, low, *ptr;
        jbyteArray ja;
        ja =(jbyteArray)ubounds;
        ptr = (*env)->GetByteArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseByteArrayElements(env, ja, ptr, 0);
        ja = (jbyteArray)lbounds;
        ptr = (*env)->GetByteArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseByteArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[S")) ) {
        jshort up, low, *ptr;
        jshortArray ja;
        ja =(jshortArray)ubounds;
        ptr = (*env)->GetShortArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseShortArrayElements(env, ja, ptr, 0);
        ja = (jshortArray)lbounds;
        ptr = (*env)->GetShortArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseShortArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[I")) ) {
        jint up, low, *ptr;
        jintArray ja;
        ja =(jintArray)ubounds;
        ptr = (*env)->GetIntArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseIntArrayElements(env, ja, ptr, 0);
        ja = (jintArray)lbounds;
        ptr = (*env)->GetIntArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseIntArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[J")) ) {
        jlong up, low, *ptr;
        jlongArray ja;
        ja =(jlongArray)ubounds;
        ptr = (*env)->GetLongArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseLongArrayElements(env, ja, ptr, 0);
        ja = (jlongArray)lbounds;
        ptr = (*env)->GetLongArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseLongArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[F")) ) {
        jfloat up, low, *ptr;
        jfloatArray ja;
        ja =(jfloatArray)ubounds;
        ptr = (*env)->GetFloatArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseFloatArrayElements(env, ja, ptr, 0);
        ja = (jfloatArray)lbounds;
        ptr = (*env)->GetFloatArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseFloatArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "[D")) ) {
        jdouble up, low, *ptr;
        jdoubleArray ja;
        ja =(jdoubleArray)ubounds;
        ptr = (*env)->GetDoubleArrayElements(env, ja, 0);
        up = ptr[0];
        ub = &up;
        (*env)->ReleaseDoubleArrayElements(env, ja, ptr, 0);
        ja = (jdoubleArray)lbounds;
        ptr = (*env)->GetDoubleArrayElements(env, ja, 0);
        low = ptr[0];
        lb = &low;
        (*env)->ReleaseDoubleArrayElements(env, ja, ptr, 0);
    }
    else if ( (*env)->IsInstanceOf(env, ubounds, (*env)->FindClass(env, "java/lang/String;")) ) {
        jstring jstr;
        char *cstr;
        jstr = (jstring)ubounds;
        cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
        upstr = (char *)malloc(strlen(cstr)+1);
        strcpy(upstr, cstr);
        (*env)->ReleaseStringUTFChars(env, jstr, cstr);
        jstr = (jstring)lbounds;
        cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
        lowstr = (char *)malloc(strlen(cstr)+1);
        strcpy(lowstr, cstr);
        (*env)->ReleaseStringUTFChars(env, jstr, cstr);

        ub = upstr;
        lb = lowstr;
    }

    ret_value = H5INquery( (hid_t)dset_id, cKeys, ub, lb, (int)nkeys);

done:

    if (cKeys) {
        for (i=0; i<nkeys; i++) {
            if (cKeys[i])
                free(cKeys[i]);
        }
        free(cKeys);
    }

    if (upstr)
        free( upstr);

    if (lowstr)
        free(lowstr);

    return (jint)ret_value;
}


#ifdef __cplusplus
}
#endif
