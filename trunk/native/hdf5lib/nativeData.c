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
 *  This module contains the implementation of all the native methods
 *  used for number conversion.  This is represented by the Java
 *  class HDFNativeData.
 *
 *  These routines convert one dimensional arrays of bytes into
 *  one-D arrays of other types (int, float, etc) and vice versa.
 *
 *  These routines are called from the Java parts of the Java-C
 *  interface.
 *
 *  ***Important notes:
 *
 *     1.  These routines are designed to be portable--they use the
 *         C compiler to do the required native data manipulation.
 *     2.  These routines copy the data at least once -- a serious
 *         but unavoidable performance hit.
 */

#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include <jni.h>

extern jboolean h5outOfMemory( JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);

/* returns int [] */
JNIEXPORT jintArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToInt___3B
( JNIEnv *env,
jclass clss,
jbyteArray bdata)  /* IN: array of bytes */
{
    jbyte *barr;
    jintArray rarray;
    int blen;
    jint *iarray;
    jboolean bb;
    char *bp;
    jint *iap;
    int ii;
    int len;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToInt: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToInt: pin failed");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif

    len = blen/sizeof(jint);
#ifdef __cplusplus
    rarray = env->NewIntArray(len);
#else
    rarray = (*env)->NewIntArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToInt" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetIntArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetIntArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToInt: pin iarray failed");
        return NULL;
    }

    bp = (char *)barr;
    iap = iarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jint *)bp;
        iap++;
        bp += sizeof(jint);
    }

#ifdef __cplusplus
    env->ReleaseIntArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseIntArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;

}

/* returns float [] */
JNIEXPORT jfloatArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToFloat___3B
( JNIEnv *env,
jclass clss,
jbyteArray bdata)  /* IN: array of bytes */
{
    jbyte *barr;
    jfloatArray rarray;
    int blen;
    jfloat *farray;
    jboolean bb;
    char *bp;
    jfloat *iap;
    int ii;
    int len;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToFloat: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToFloat: pin failed");
        return NULL;
    }
#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif

    len = blen/sizeof(jfloat);
#ifdef __cplusplus
    rarray = env->NewFloatArray(len);
#else
    rarray = (*env)->NewFloatArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToFloat" );
        return NULL;
    }
#ifdef __cplusplus
    farray = env->GetFloatArrayElements(rarray,&bb);
#else
    farray = (*env)->GetFloatArrayElements(env,rarray,&bb);
#endif
    if (farray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToFloat: pin farray failed");
        return NULL;
    }

    bp = (char *)barr;
    iap = farray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jfloat *)bp;
        iap++;
        bp += sizeof(jfloat);
    }

#ifdef __cplusplus
    env->ReleaseFloatArrayElements(rarray,farray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseFloatArrayElements(env,rarray,farray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;

}

/* returns short [] */
JNIEXPORT jshortArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToShort___3B
( JNIEnv *env,
jclass clss,
jbyteArray bdata)  /* IN: array of bytes */
{
    jbyte *barr;
    jshortArray rarray;
    int blen;
    jshort *sarray;
    jboolean bb;
    char *bp;
    jshort *iap;
    int ii;
    int len;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToShort: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToShort: pin failed");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif

    len = blen/sizeof(jshort);
#ifdef __cplusplus
    rarray = env->NewShortArray(len);
#else
    rarray = (*env)->NewShortArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToShort" );
        return NULL;
    }

#ifdef __cplusplus
    sarray = env->GetShortArrayElements(rarray,&bb);
#else
    sarray = (*env)->GetShortArrayElements(env,rarray,&bb);
#endif
    if (sarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToShort: pin sarray failed");
        return NULL;
    }

    bp = (char *)barr;
    iap = sarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jshort *)bp;
        iap++;
        bp += sizeof(jshort);
    }

#ifdef __cplusplus
    env->ReleaseShortArrayElements(rarray,sarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseShortArrayElements(env,rarray,sarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;

}


/* returns long [] */
JNIEXPORT jlongArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToLong___3B
( JNIEnv *env,
jclass clss,
jbyteArray bdata)  /* IN: array of bytes */
{
    jbyte *barr;
    jlongArray rarray;
    int blen;
    jlong *larray;
    jboolean bb;
    char *bp;
    jlong *iap;
    int ii;
    int len;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToLong: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToLong: pin failed");
        return NULL;
    }
#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif

    len = blen/sizeof(jlong);
#ifdef __cplusplus
    rarray = env->NewLongArray(len);
#else
    rarray = (*env)->NewLongArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToLong" );
        return NULL;
    }

#ifdef __cplusplus
    larray = env->GetLongArrayElements(rarray,&bb);
#else
    larray = (*env)->GetLongArrayElements(env,rarray,&bb);
#endif
    if (larray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToLong: pin larray failed");
        return NULL;
    }

    bp = (char *)barr;
    iap = larray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jlong *)bp;
        iap++;
        bp += sizeof(jlong);
    }
#ifdef __cplusplus
    env->ReleaseLongArrayElements(rarray,larray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseLongArrayElements(env,rarray,larray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;
}


/* returns double [] */
JNIEXPORT jdoubleArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToDouble___3B
( JNIEnv *env,
jclass clss,
jbyteArray bdata)  /* IN: array of bytes */
{
    jbyte *barr;
    jdoubleArray rarray;
    int blen;
    jdouble *darray;
    jboolean bb;
    char *bp;
    jdouble *iap;
    int ii;
    int len;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToDouble: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToDouble: pin failed");
        return NULL;
    }
#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif

    len = blen/sizeof(jdouble);
#ifdef __cplusplus
    rarray = env->NewDoubleArray(len);
#else
    rarray = (*env)->NewDoubleArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToDouble" );
        return NULL;
    }

#ifdef __cplusplus
    darray = env->GetDoubleArrayElements(rarray,&bb);
#else
    darray = (*env)->GetDoubleArrayElements(env,rarray,&bb);
#endif
    if (darray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToDouble: pin darray failed");
        return NULL;
    }

    bp = (char *)barr;
    iap = darray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jdouble *)bp;
        iap++;
        bp += sizeof(jdouble);
    }

#ifdef __cplusplus
    env->ReleaseDoubleArrayElements(rarray,darray,0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseDoubleArrayElements(env,rarray,darray,0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;
}


/* returns int [] */
JNIEXPORT jintArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToInt__II_3B
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jbyteArray bdata)  /* IN: array of bytes */
{
    char *bp;
    jbyte *barr;
    jintArray rarray;
    int blen;
    jint *iarray;
    jint *iap;
    int ii;
    jboolean bb;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToInt: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError(env,  "byteToInt: pin failed");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif
    if ((start < 0) || ((int)(start + (len*sizeof(jint))) > blen)) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToInt: getLen failed");
        return NULL;
    }

    bp = (char *)barr + start;

#ifdef __cplusplus
    rarray = env->NewIntArray(len);
#else
    rarray = (*env)->NewIntArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToInt" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetIntArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetIntArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "byteToInt: pin iarray failed");
        return NULL;
    }

    iap = iarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jint *)bp;
        iap++;
        bp += sizeof(jint);
    }

#ifdef __cplusplus
    env->ReleaseIntArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseIntArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;
}

/* returns short [] */
JNIEXPORT jshortArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToShort__II_3B
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jbyteArray bdata)  /* IN: array of bytes */
{
    char *bp;
    jbyte *barr;
    jshortArray rarray;
    int blen;
    jshort *iarray;
    jshort *iap;
    int ii;
    jboolean bb;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToShort: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError( env,  "byteToShort: getByte failed?");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif
    if ((start < 0) || ((int)(start + (len*(sizeof(jshort)))) > blen)) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5badArgument( env,  "byteToShort: start or len is out of bounds");
        return NULL;
    }

    bp = (char *)barr + start;

#ifdef __cplusplus
    rarray = env->NewShortArray(len);
#else
    rarray = (*env)->NewShortArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToShort" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetShortArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetShortArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "byteToShort: getShort failed?");
        return NULL;
    }

    iap = iarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jshort *)bp;
        iap++;
        bp += sizeof(jshort);
    }

#ifdef __cplusplus
    env->ReleaseShortArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseShortArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;

}

/* returns float [] */
JNIEXPORT jfloatArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToFloat__II_3B
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jbyteArray bdata)  /* IN: array of bytes */
{
    char *bp;
    jbyte *barr;
    jfloatArray rarray;
    int blen;
    jfloat *iarray;
    jfloat *iap;
    int ii;
    jboolean bb;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToFloat: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError( env,  "byteToFloat: getByte failed?");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif
    if ((start < 0) || ((int)(start + (len*(sizeof(jfloat)))) > blen)) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5badArgument( env,  "byteToFloat: start or len is out of bounds");
        return NULL;
    }

    bp = (char *)barr + start;

#ifdef __cplusplus
    rarray = env->NewFloatArray(len);
#else
    rarray = (*env)->NewFloatArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToFloat" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetFloatArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetFloatArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "byteToFloat: getFloat failed?");
        return NULL;
    }

    iap = iarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jfloat *)bp;
        iap++;
        bp += sizeof(jfloat);
    }

#ifdef __cplusplus
    env->ReleaseFloatArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseFloatArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;
}

/* returns long [] */
JNIEXPORT jlongArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToLong__II_3B
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jbyteArray bdata)  /* IN: array of bytes */
{
    char *bp;
    jbyte *barr;
    jlongArray rarray;
    int blen;
    jlong *iarray;
    jlong *iap;
    int ii;
    jboolean bb;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToLong: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError( env,  "byteToLong: getByte failed?");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif
    if ((start < 0) || ((int)(start + (len*(sizeof(jlong)))) > blen)) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5badArgument( env,  "byteToLong: start or len is out of bounds");
        return NULL;
    }

    bp = (char *)barr + start;

#ifdef __cplusplus
    rarray = env->NewLongArray(len);
#else
    rarray = (*env)->NewLongArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToLong" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetLongArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetLongArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "byteToLong: getLong failed?");
        return NULL;
    }

    iap = iarray;
    for (ii = 0; ii < len; ii++) {

        *iap = *(jlong *)bp;
        iap++;
        bp += sizeof(jlong);
    }

#ifdef __cplusplus
    env->ReleaseLongArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseLongArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;

}

/* returns double [] */
JNIEXPORT jdoubleArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToDouble__II_3B
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jbyteArray bdata)  /* IN: array of bytes */
{
    char *bp;
    jbyte *barr;
    jdoubleArray rarray;
    int blen;
    jdouble *iarray;
    jdouble *iap;
    int ii;
    jboolean bb;

    if (bdata == NULL) {
        h5nullArgument( env,  "byteToDouble: bdata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    barr = env->GetByteArrayElements(bdata,&bb);
#else
    barr = (*env)->GetByteArrayElements(env,bdata,&bb);
#endif
    if (barr == NULL) {
        h5JNIFatalError( env,  "byteToDouble: getByte failed?");
        return NULL;
    }

#ifdef __cplusplus
    blen = env->GetArrayLength(bdata);
#else
    blen = (*env)->GetArrayLength(env,bdata);
#endif
    if ((start < 0) || ((int)(start + (len*(sizeof(jdouble)))) > blen)) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5badArgument( env,  "byteToDouble: start or len is out of bounds");
        return NULL;
    }

    bp = (char *)barr + start;

#ifdef __cplusplus
    rarray = env->NewDoubleArray(len);
#else
    rarray = (*env)->NewDoubleArray(env,len);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "byteToDouble" );
        return NULL;
    }

#ifdef __cplusplus
    iarray = env->GetDoubleArrayElements(rarray,&bb);
#else
    iarray = (*env)->GetDoubleArrayElements(env,rarray,&bb);
#endif
    if (iarray == NULL) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "byteToDouble: getDouble failed?");
        return NULL;
    }

    iap = iarray;
    for (ii = 0; ii < len; ii++) {
        *iap = *(jdouble *)bp;
        iap++;
        bp += sizeof(jdouble);
    }

#ifdef __cplusplus
    env->ReleaseDoubleArrayElements(rarray,iarray, 0);
    env->ReleaseByteArrayElements(bdata,barr,JNI_ABORT);
#else
    (*env)->ReleaseDoubleArrayElements(env,rarray,iarray, 0);
    (*env)->ReleaseByteArrayElements(env,bdata,barr,JNI_ABORT);
#endif

    return rarray;
}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_intToByte__II_3I
(JNIEnv *env,
jclass clss,
jint start,
jint len,
jintArray idata)  /* IN: array of int */
{
    jint *ip;
    jint *iarr;
    int ilen;
    jbyteArray rarray;
    int blen;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ii;
    int ij;
    union things {
        int ival;
        char bytes[4];
    } u;

    if (idata == NULL) {
        h5nullArgument( env,  "intToByte: idata is NULL?");
        return NULL;
    }

#ifdef __cplusplus
    iarr = env->GetIntArrayElements(idata,&bb);
#else
    iarr = (*env)->GetIntArrayElements(env,idata,&bb);
#endif
    if (iarr == NULL) {
        h5JNIFatalError( env,  "intToByte: getInt failed?");
        return NULL;
    }

#ifdef __cplusplus
    ilen = env->GetArrayLength(idata);
#else
    ilen = (*env)->GetArrayLength(env,idata);
#endif
    if ((start < 0) || (((start + len)) > ilen)) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5badArgument( env,  "intToByte: start or len is out of bounds");
        return NULL;
    }

    ip = iarr + start;

    blen = ilen * sizeof(jint);
#ifdef __cplusplus
    rarray = env->NewByteArray(blen);
#else
    rarray = (*env)->NewByteArray(env,blen);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "intToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "intToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    for (ii = 0; ii < len; ii++) {
        u.ival = *ip++;
        for (ij = 0; ij < sizeof(jint); ij++) {
            *bap = u.bytes[ij];
            bap++;
        }
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
    env->ReleaseIntArrayElements(idata,iarr,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
    (*env)->ReleaseIntArrayElements(env,idata,iarr,JNI_ABORT);
#endif

    return rarray;
}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_shortToByte__II_3S
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jshortArray idata)  /* IN: array of short */
{
    jshort *ip;
    jshort *iarr;
    int ilen;
    jbyteArray rarray;
    int blen;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ii;
    int ij;
    union things {
        short ival;
        char bytes[4];
    } u;

    if (idata == NULL) {
        h5nullArgument( env,  "shortToByte: idata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    iarr = env->GetShortArrayElements(idata,&bb);
#else
    iarr = (*env)->GetShortArrayElements(env,idata,&bb);
#endif
    if (iarr == NULL) {
        h5JNIFatalError( env,  "shortToByte: getShort failed?");
        return NULL;
    }

#ifdef __cplusplus
    ilen = env->GetArrayLength(idata);
#else
    ilen = (*env)->GetArrayLength(env,idata);
#endif
    if ((start < 0) || (((start + len)) > ilen)) {
#ifdef __cplusplus
        env->ReleaseShortArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseShortArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5badArgument( env,  "shortToByte: start or len is out of bounds");
        return NULL;
    }

    ip = iarr + start;

    blen = ilen * sizeof(jshort);
#ifdef __cplusplus
    rarray = env->NewByteArray(blen);
#else
    rarray = (*env)->NewByteArray(env,blen);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseShortArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseShortArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "shortToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
#ifdef __cplusplus
        env->ReleaseShortArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseShortArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "shortToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    for (ii = 0; ii < len; ii++) {
        u.ival = *ip++;
        for (ij = 0; ij < sizeof(jshort); ij++) {
            *bap = u.bytes[ij];
            bap++;
        }
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
    env->ReleaseShortArrayElements(idata,iarr,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
    (*env)->ReleaseShortArrayElements(env,idata,iarr,JNI_ABORT);
#endif

    return rarray;

}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_floatToByte__II_3F
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jfloatArray idata)  /* IN: array of float */
{
    jfloat *ip;
    jfloat *iarr;
    int ilen;
    jbyteArray rarray;
    int blen;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ii;
    int ij;
    union things {
        float ival;
        char bytes[4];
    } u;

    if (idata == NULL) {
        h5nullArgument( env,  "floatToByte: idata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    iarr = env->GetFloatArrayElements(idata,&bb);
#else
    iarr = (*env)->GetFloatArrayElements(env,idata,&bb);
#endif
    if (iarr == NULL) {
        h5JNIFatalError( env,  "floatToByte: getFloat failed?");
        return NULL;
    }

#ifdef __cplusplus
    ilen = env->GetArrayLength(idata);
#else
    ilen = (*env)->GetArrayLength(env,idata);
#endif
    if ((start < 0) || (((start + len)) > ilen)) {
#ifdef __cplusplus
        env->ReleaseFloatArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseFloatArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5badArgument( env,  "floatToByte: start or len is out of bounds");
        return NULL;
    }

    ip = iarr + start;

    blen = ilen * sizeof(jfloat);
#ifdef __cplusplus
    rarray = env->NewByteArray(blen);
#else
    rarray = (*env)->NewByteArray(env,blen);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseFloatArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseFloatArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "floatToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
#ifdef __cplusplus
        env->ReleaseFloatArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseFloatArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "floatToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    for (ii = 0; ii < len; ii++) {
        u.ival = *ip++;
        for (ij = 0; ij < sizeof(jfloat); ij++) {
            *bap = u.bytes[ij];
            bap++;
        }
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
    env->ReleaseFloatArrayElements(idata,iarr,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
    (*env)->ReleaseFloatArrayElements(env,idata,iarr,JNI_ABORT);
#endif

    return rarray;
}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_doubleToByte__II_3D
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jdoubleArray idata)  /* IN: array of double */
{
    jdouble *ip;
    jdouble *iarr;
    int ilen;
    jbyteArray rarray;
    int blen;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ii;
    int ij;
    union things {
        double ival;
        char bytes[8];
    } u;

    if (idata == NULL) {
        h5nullArgument( env,  "doubleToByte: idata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    iarr = env->GetDoubleArrayElements(idata,&bb);
#else
    iarr = (*env)->GetDoubleArrayElements(env,idata,&bb);
#endif
    if (iarr == NULL) {
        h5JNIFatalError( env,  "doubleToByte: getDouble failed?");
        return NULL;
    }

#ifdef __cplusplus
    ilen = env->GetArrayLength(idata);
#else
    ilen = (*env)->GetArrayLength(env,idata);
#endif
    if ((start < 0) || (((start + len)) > ilen)) {
#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseDoubleArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5badArgument( env,  "doubleToByte: start or len is out of bounds");
        return NULL;
    }

    ip = iarr + start;

    blen = ilen * sizeof(jdouble);
#ifdef __cplusplus
    rarray = env->NewByteArray(blen);
#else
    rarray = (*env)->NewByteArray(env,blen);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseDoubleArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "doubleToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseDoubleArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "doubleToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    for (ii = 0; ii < len; ii++) {
        u.ival = *ip++;
        for (ij = 0; ij < sizeof(jdouble); ij++) {
            *bap = u.bytes[ij];
            bap++;
        }
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
    env->ReleaseDoubleArrayElements(idata,iarr,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
    (*env)->ReleaseDoubleArrayElements(env,idata,iarr,JNI_ABORT);
#endif

    return rarray;
}


/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_longToByte__II_3J
( JNIEnv *env,
jclass clss,
jint start,
jint len,
jlongArray idata)  /* IN: array of long */
{
    jlong *ip;
    jlong *iarr;
    int ilen;
    jbyteArray rarray;
    int blen;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ii;
    int ij;
    union things {
        jlong ival;
        char bytes[8];
    } u;

    if (idata == NULL) {
        h5nullArgument( env,  "longToByte: idata is NULL?");
        return NULL;
    }
#ifdef __cplusplus
    iarr = env->GetLongArrayElements(idata,&bb);
#else
    iarr = (*env)->GetLongArrayElements(env,idata,&bb);
#endif
    if (iarr == NULL) {
        h5JNIFatalError( env,  "longToByte: getLong failed?");
        return NULL;
    }

#ifdef __cplusplus
    ilen = env->GetArrayLength(idata);
#else
    ilen = (*env)->GetArrayLength(env,idata);
#endif
    if ((start < 0) || (((start + len)) > ilen)) {
#ifdef __cplusplus
        env->ReleaseLongArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseLongArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5badArgument( env,  "longToByte: start or len is out of bounds?\n");
        return NULL;
    }

    ip = iarr + start;

    blen = ilen * sizeof(jlong);
#ifdef __cplusplus
    rarray = env->NewByteArray(blen);
#else
    rarray = (*env)->NewByteArray(env,blen);
#endif
    if (rarray == NULL) {
#ifdef __cplusplus
        env->ReleaseLongArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseLongArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5outOfMemory( env,  "longToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
#ifdef __cplusplus
        env->ReleaseLongArrayElements(idata,iarr,JNI_ABORT);
#else
        (*env)->ReleaseLongArrayElements(env,idata,iarr,JNI_ABORT);
#endif
        h5JNIFatalError( env,  "longToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    for (ii = 0; ii < len; ii++) {
        u.ival = *ip++;
        for (ij = 0; ij < sizeof(jlong); ij++) {
            *bap = u.bytes[ij];
            bap++;
        }
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
    env->ReleaseLongArrayElements(idata,iarr,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
    (*env)->ReleaseLongArrayElements(env,idata,iarr,JNI_ABORT);
#endif

    return rarray;

}
 /******/


/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_intToByte__I
( JNIEnv *env,
jclass clss,
jint idata)  /* IN: int */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    int ij;
    jboolean bb;
    union things {
        int ival;
        char bytes[sizeof(int)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jint));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jint));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "intToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "intToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jint); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,barray, 0);
#endif
    return rarray;

}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_floatToByte__F
( JNIEnv *env,
jclass clss,
jfloat idata)  /* IN: int */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ij;
    union things {
        float ival;
        char bytes[sizeof(float)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jfloat));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jfloat));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "floatToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "floatToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jfloat); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,(jbyte *)barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,(jbyte *)barray, 0);
#endif
    return rarray;

}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_shortToByte__S
( JNIEnv *env,
jclass clss,
jshort idata)  /* IN: short */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ij;
    union things {
        short ival;
        char bytes[sizeof(short)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jshort));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jshort));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "shortToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "shortToByte: getShort failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jshort); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,(jbyte *)barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,(jbyte *)barray, 0);
#endif

    return rarray;
}


/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_doubleToByte__D
( JNIEnv *env,
jclass clss,
jdouble idata)  /* IN: double */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ij;
    union things {
        double ival;
        char bytes[sizeof(double)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jdouble));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jdouble));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "doubleToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "doubleToByte: getDouble failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jdouble); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,(jbyte *)barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,(jbyte *)barray, 0);
#endif

    return rarray;
}


/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_longToByte__J
( JNIEnv *env,
jclass clss,
jlong idata)  /* IN: array of long */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ij;
    union things {
        jlong ival;
        char bytes[sizeof(jlong)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jlong));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jlong));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "longToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "longToByte: getLong failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jlong); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,(jbyte *)barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,(jbyte *)barray, 0);
#endif

    return rarray;
}

/* returns byte [] */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_HDFNativeData_byteToByte__B
( JNIEnv *env,
jclass clss,
jbyte idata)  /* IN: array of long */
{
    jbyteArray rarray;
    jbyte *barray;
    jbyte *bap;
    jboolean bb;
    int ij;
    union things {
        jbyte ival;
        char bytes[sizeof(jbyte)];
    } u;

#ifdef __cplusplus
    rarray = env->NewByteArray(sizeof(jbyte));
#else
    rarray = (*env)->NewByteArray(env,sizeof(jbyte));
#endif
    if (rarray == NULL) {
        h5outOfMemory( env,  "byteToByte" );
        return NULL;
    }

#ifdef __cplusplus
    barray = env->GetByteArrayElements(rarray,&bb);
#else
    barray = (*env)->GetByteArrayElements(env,rarray,&bb);
#endif
    if (barray == NULL) {
        h5JNIFatalError( env,  "byteToByte: getByte failed?");
        return NULL;
    }

    bap = barray;
    u.ival = idata;
    for (ij = 0; ij < sizeof(jbyte); ij++) {
        *bap = u.bytes[ij];
        bap++;
    }

#ifdef __cplusplus
    env->ReleaseByteArrayElements(rarray,(jbyte *)barray, 0);
#else
    (*env)->ReleaseByteArrayElements(env,rarray,(jbyte *)barray, 0);
#endif

    return rarray;
}
#ifdef __cplusplus
}
#endif
