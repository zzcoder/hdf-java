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

/*
 *  This code is the C-interface called by Java programs to access the
 *  Dataset Object API Functions of HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *    http://hdfdfgroup.org/HDF5/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hdf5.h"
#include "h5util.h"
#include "h5jni.h"
#include "h5dImp.h"

#ifdef __cplusplus
  #ifdef _WINDOWS
    #include <direct.h>
  #endif
#endif

#ifdef _WINDOWS
  #define CHDIR _chdir
  #define GETCWD _getcwd
#else
  #define CHDIR chdir
  #define GETCWD getcwd
#endif

#ifdef __cplusplus
  #define CBENVPTR (cbenv)
  #define CBENVPAR 
  #define JVMPTR (jvm)
  #define JVMPAR 
  #define JVMPAR2 
#else
  #define CBENVPTR (*cbenv)
  #define CBENVPAR cbenv,
  #define JVMPTR (*jvm)
  #define JVMPAR jvm
  #define JVMPAR2 jvm,
#endif

herr_t H5DreadVL_str (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf);
herr_t H5DreadVL_notstr (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf);

#define PIN_BYTE_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jbyte*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetByteArrayElements(ENVPAR buf, &isCopy); \
}

#define UNPIN_BYTE_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf, buffP, mode); \
}

#define PIN_SHORT_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jshort*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetShortArrayElements(ENVPAR buf, &isCopy); \
}

#define UNPIN_SHORT_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseShortArrayElements(ENVPAR buf, buffP, mode); \
}

#define PIN_INT_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jint*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetIntArrayElements(ENVPAR buf, &isCopy); \
}

#define UNPIN_INT_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseIntArrayElements(ENVPAR buf, buffP, mode); \
}

#define PIN_LONG_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jlong*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetLongArrayElements(ENVPAR buf,&isCopy); \
}

#define UNPIN_LONG_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseLongArrayElements(ENVPAR buf, buffP, mode); \
}

#define PIN_FLOAT_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jfloat*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetFloatArrayElements(ENVPAR buf, &isCopy); \
}

#define UNPIN_FLOAT_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseFloatArrayElements(ENVPAR buf, buffP, mode); \
}

#define PIN_DOUBLE_ARRAY() { \
    if (isCriticalPinning) \
        buffP = (jdouble*)ENVPTR->GetPrimitiveArrayCritical(ENVPAR buf, &isCopy); \
    else \
        buffP = ENVPTR->GetDoubleArrayElements(ENVPAR buf, &isCopy); \
}

#define UNPIN_DOUBLE_ARRAY(mode) { \
    if (isCriticalPinning) \
        ENVPTR->ReleasePrimitiveArrayCritical(ENVPAR buf, buffP, mode); \
    else \
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR buf, buffP, mode); \
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dcreate
 * Signature: (JLjava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dcreate
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong type_id,
          jlong space_id, jlong create_plist_id)
{
    hid_t    status;
    char    *file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Dcreate:  name is NULL");
        return -1;
    }
    file = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dcreate:  file name not pinned");
        return -1;
    }

    status = H5Dcreate2((hid_t)loc_id, file, (hid_t)type_id, (hid_t)space_id, (hid_t)H5P_DEFAULT, (hid_t)create_plist_id, (hid_t)H5P_DEFAULT);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, file);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dchdir_ext
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dchdir_1ext
  (JNIEnv *env, jclass clss, jstring dir_name)
{
    hid_t    status;
    char    *file;
    jboolean isCopy;

    if (dir_name == NULL) {
        h5nullArgument( env, "H5Dchdir_ext:  dir_name is NULL");
        return -1;
    }
    file = (char*)ENVPTR->GetStringUTFChars(ENVPAR dir_name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dchdir_ext:  file dir not pinned");
        return -1;
    }

    status = CHDIR( file );

    ENVPTR->ReleaseStringUTFChars(ENVPAR dir_name, file);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dgetdir_1ext
 * Signature: ([Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dgetdir_1ext
  (JNIEnv *env, jclass clss, jobjectArray dir_name, jint buf_size)
{
    char   *aName;
    jstring str;

    if (buf_size <= 0) {
        h5badArgument( env, "H5Dgetcwd:  buf_size <= 0");
        return -1;
    }
    aName = (char*)malloc(sizeof(char) * buf_size);
    if (aName == NULL) {
        h5outOfMemory( env, "H5Dgetcwd:  malloc failed");
        return -1;
    }
    GETCWD( (char*)aName, (size_t)buf_size);

    str = ENVPTR->NewStringUTF(ENVPAR aName);

    free(aName);

    if (str == NULL) {
         h5JNIFatalError( env,"H5Dgetcwd:  return string failed");
         return -1;
    }

    ENVPTR->SetObjectArrayElement(ENVPAR dir_name, 0, str);

    return 0;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dopen
 * Signature: (JLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dopen
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name)
{
    hid_t    status;
    char    *file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Dopen:  name is NULL");
        return -1;
    }

    file = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dopen:  file name not pinned");
        return -1;
    }

    status = H5Dopen2((hid_t)loc_id, (const char*)file, (hid_t)H5P_DEFAULT);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, file);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dget_space
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dget_1space
  (JNIEnv *env, jclass clss, jlong dataset_id)
{
    hid_t retVal = -1;

    retVal = H5Dget_space((hid_t)dataset_id);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dget_type
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dget_1type
  (JNIEnv *env, jclass clss, jlong dataset_id)
{
    hid_t retVal = -1;

    retVal = H5Dget_type((hid_t)dataset_id);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dget_create_plist
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dget_1create_1plist
  (JNIEnv *env, jclass clss, jlong dataset_id)
{
    hid_t retVal = -1;

    retVal = H5Dget_create_plist((hid_t)dataset_id);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong)retVal;
}

htri_t H5Tdetect_variable_str(hid_t tid) {
    htri_t ret_val = 0;

    if (H5Tget_class(tid) == H5T_COMPOUND) {
        hid_t mtid = H5Tget_member_type(tid, 0);
        ret_val = H5Tdetect_variable_str(mtid);
        H5Tclose (mtid);
    }
    else
        ret_val = H5Tis_variable_str(tid);

    return ret_val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread
 * Signature: (JJJJJ[BZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jbyteArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jbyte   *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread:  buf does not support variable length string type");
        return -1;
    }

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }

    PIN_BYTE_ARRAY();

    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_BYTE_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_BYTE_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite
 * Signature: (JJJJJ[BZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jbyteArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jbyte   *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }

    PIN_BYTE_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_BYTE_ARRAY(JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dextend
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dextend
  (JNIEnv *env, jclass clss, jlong dataset_id, jbyteArray size)
{
    herr_t   status;
    int      i;
    int      rank;
    hsize_t *sa;
    hsize_t *lp;
    jbyte   *P;
    jboolean isCopy;
    jlong   *jlp;

    if (size == NULL) {
        h5nullArgument( env, "H5Dextend:  array of sizes is NULL");
        return -1;
    }
    /*
     *  Future:  check that the array has correct
     *           rank (same as dataset dataset_id)
     */
    P = ENVPTR->GetByteArrayElements(ENVPAR size, &isCopy);
    if (P == NULL) {
        h5JNIFatalError( env, "H5Dextend:  array not pinned");
        return -1;
    }
    i = ENVPTR->GetArrayLength(ENVPAR size);
    rank = i / sizeof(jlong);
    sa = lp = (hsize_t*)malloc(rank * sizeof(hsize_t));
    if (sa == NULL)  {
        ENVPTR->ReleaseByteArrayElements(ENVPAR size, P, JNI_ABORT);
        h5JNIFatalError(env,  "H5Dextend:  size not converted to hsize_t");
        return -1;
    }
    jlp = (jlong*)P;
    for (i = 0; i < rank; i++) {
        *lp = (hsize_t) * jlp;
        lp++;
        jlp++;
    }

    status = H5Dextend((hid_t)dataset_id, (hsize_t*)sa);

    ENVPTR->ReleaseByteArrayElements(ENVPAR size, P, JNI_ABORT);
    free(sa);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dclose
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dclose
  (JNIEnv *env, jclass clss, jlong dataset_id)
{
    herr_t retVal = 0;

    retVal = H5Dclose((hid_t)dataset_id);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_storage_size
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1storage_1size
  (JNIEnv *env, jclass clss, jlong dataset_id)
{
    hsize_t retVal = (hsize_t)-1;
    if (dataset_id < 0) {
        h5badArgument(env, "H5Dget_storage_size: not a dataset");
    }
    retVal = H5Dget_storage_size((hid_t)dataset_id );
    return (jlong)retVal;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dcopy
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dcopy
  (JNIEnv *env, jclass clss, jlong src_id, jlong dst_id)
{
    jbyte  *buf;
    herr_t  retVal = -1;
    hid_t   src_did = (hid_t)src_id;
    hid_t   dst_did = (hid_t)dst_id;
    hid_t   tid = -1;
    hid_t   sid = -1;
    hsize_t total_size = 0, total_allocated_size;

    total_allocated_size = H5Dget_storage_size(src_did);
    if (total_allocated_size <=0)
      return 0; // nothing to write;

    sid = H5Dget_space(src_did);
    if (sid < 0) {
        h5libraryError(env);
        return -1;
    }

    tid = H5Dget_type(src_did);
    if (tid < 0) {
        H5Sclose(sid);
        h5libraryError(env);
        return -1;
    }

    total_size = H5Sget_simple_extent_npoints(sid) * H5Tget_size(tid);

    H5Sclose(sid);

    buf = (jbyte*)malloc((int)(total_size * sizeof(jbyte)));
    if (buf == NULL) {
        H5Tclose(tid);
        h5outOfMemory(env, "H5Dcopy:  malloc failed");
        return -1;
    }

    retVal = H5Dread((hid_t)src_did, (hid_t)tid, (hid_t)H5S_ALL, (hid_t)H5S_ALL, (hid_t)H5P_DEFAULT, buf);
    H5Tclose(tid);

    if (retVal < 0) {
        free(buf);
        h5libraryError(env);
        return (jint)retVal;
    }

    tid = H5Dget_type(dst_did);
    if (tid < 0) {
        free(buf);
        h5libraryError(env);
        return -1;
    }
    retVal = H5Dwrite((hid_t)dst_did, (hid_t)tid, (hid_t)H5S_ALL, (hid_t)H5S_ALL, (hid_t)H5P_DEFAULT, buf);
    H5Tclose(tid);
    free(buf);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_get_buf_size
 * Signature: (JJJ[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1get_1buf_1size
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong type_id, jlong space_id,
          jintArray size)
{
    jint    *P;
    jboolean isCopy;
    herr_t   status;
    hsize_t  sz;

    if ( size == NULL ) {
        h5nullArgument(env, "H5Dvlen_get_buf_size:  size is NULL");
        return -1;
    }

    P = ENVPTR->GetIntArrayElements(ENVPAR size, &isCopy);
    if (P == NULL) {
        h5JNIFatalError(env, "H5Dvlen_get_buf_size:  array not pinned");
        return -1;
    }

    status = (jint)H5Dvlen_get_buf_size((hid_t)dataset_id, (hid_t)type_id, (hid_t)space_id, (hsize_t*)&sz);

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR size, P, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    P[0] = (jint)sz;
    ENVPTR->ReleaseIntArrayElements(ENVPAR size, P, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_reclaim
 * Signature: (JJJ[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1reclaim
  (JNIEnv *env, jclass clss, jlong type_id, jlong space_id,
          jlong xfer_plist_id, jbyteArray buf)
{
    herr_t   status;
    jbyte   *byteP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dvlen_reclaim:  buf is NULL");
        return -1;
    }
    byteP = ENVPTR->GetByteArrayElements(ENVPAR buf, &isCopy);
    if (byteP == NULL) {
        h5JNIFatalError( env, "H5Dvlen_reclaim:  buf not pinned");
        return -1;
    }

    status = H5Dvlen_reclaim((hid_t)type_id,
        (hid_t)space_id, (hid_t)xfer_plist_id, byteP);

    ENVPTR->ReleaseByteArrayElements(ENVPAR buf, byteP, JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/***************************************************************
 *                   New APIs for HDF5.1.6                     *
 ***************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dget_space_status
 * Signature: (J[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dget_1space_1status
  (JNIEnv *env, jclass clss, jlong dset_id, jintArray status)
{
    jint    *theArray;
    jboolean isCopy;
    H5D_space_status_t space_status;
    herr_t   retVal = -1;

    if (status == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Dget_space_status:  status is NULL");
        return -1;
    }
    theArray = (jint*)ENVPTR->GetIntArrayElements(ENVPAR status, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Dget_space_status:  status not pinned");
        return -1;
    }

    retVal = H5Dget_space_status((hid_t)dset_id, (H5D_space_status_t*)&space_status );

    if (retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR status, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    theArray[0] = space_status;
    ENVPTR->ReleaseIntArrayElements(ENVPAR status, theArray, 0);

    return (jint)retVal;
}


/*
    ////////////////////////////////////////////////////////////////////
    //                                                                //
    //         New APIs for read data from library                    //
    //  Using H5Dread(..., Object buf) requires function calls        //
    //  theArray.emptyBytes() and theArray.arrayify( buf), which      //
    //  triples the actual memory needed by the data set.             //
    //  Using the following APIs solves the problem.                  //
    //                                                                //
    ////////////////////////////////////////////////////////////////////
*/
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_short
 * Signature: (JJJJJ[SZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1short
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jshortArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jshort  *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_short:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_short:  buf does not support variable length string type");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env, "H5Dread_short:  buf is NULL");
        return -1;
    }

    PIN_SHORT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dread_short:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_SHORT_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_SHORT_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_int
 * Signature: (JJJJJ[IZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1int
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jintArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jint    *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_int:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_int:  buf does not support variable length string type");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env, "H5Dread_int:  buf is NULL");
        return -1;
    }

    PIN_INT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dread_int:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_INT_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_INT_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_long
 * Signature: (JJJJJ[JZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1long
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jlongArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jlong   *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_long:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_long:  buf does not support variable length string type");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env, "H5Dread_long:  buf is NULL");
        return -1;
    }

    PIN_LONG_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dread_long:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_LONG_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_LONG_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_float
 * Signature: (JJJJJ[FZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1float
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jfloatArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jfloat  *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_float:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_float:  buf does not support variable length string type");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env, "H5Dread_float:  buf is NULL");
        return -1;
    }

    PIN_FLOAT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dread_float:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_FLOAT_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_FLOAT_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_double
 * Signature: (JJJJJ[DZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1double
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jdoubleArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jdouble *buffP;
    jboolean isCopy;
    htri_t data_class;

    /* recursive detect any vlen data values in type (compound, array ...) */
    data_class = H5Tdetect_class(mem_type_id, H5T_VLEN);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_double:  buf does not support variable length type");
        return -1;
    }
    /* recursive detect any vlen string in type (compound, array ...) */
    data_class = H5Tdetect_variable_str(mem_type_id);
    if((data_class == 1) || (data_class < 0)) {
        h5badArgument( env, "H5Dread_double:  buf does not support variable length string type");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env, "H5Dread_double:  buf is NULL");
        return -1;
    }

    PIN_DOUBLE_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dread_double:  buf not pinned");
        return -1;
    }
    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
        UNPIN_DOUBLE_ARRAY(JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    UNPIN_DOUBLE_ARRAY(0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_string
 * Signature: (JJJJJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1string
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jobjectArray j_buf)
{
    herr_t  status;
    char   *c_buf;
    char   *cstr;
    size_t  str_len;
    size_t  i;
    size_t  n;
    size_t  pos;
    jstring jstr;

    c_buf = cstr = NULL;
    if (j_buf == NULL) {
        h5nullArgument(env, "H5Dread_string:  buf is NULL");
        return -1;
    }

    n = ENVPTR->GetArrayLength(ENVPAR j_buf);
    if (n <= 0) {
        h5nullArgument(env, "H5Dread_string:  buf length <=0");
        return -1;
    }

    if ((str_len = H5Tget_size((hid_t)mem_type_id)) <=0) {
        h5libraryError(env);
        return -1;
    }

    if ((cstr = (char*)malloc(str_len + 1)) == NULL) {
        h5JNIFatalError(env, "H5Dread_string: memory allocation failed.");
        return -1;
    }

    if ((c_buf = (char*)malloc(n * str_len)) == NULL) {
        if (cstr)
            free (cstr);
        cstr = NULL;
        h5JNIFatalError(env, "H5Dread_string: memory allocation failed.");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                     (hid_t)file_space_id, (hid_t)xfer_plist_id, c_buf);

    if (status < 0) {
        if (cstr)
            free (cstr);
        cstr = NULL;
        if (c_buf)
            free (c_buf);
        c_buf = NULL;
        h5libraryError(env);
        return -1;
    }

    pos = 0;
    for (i=0; i<n; i++) {
        memcpy(cstr, c_buf+pos, str_len);
        cstr[str_len] = '\0';
        jstr = ENVPTR->NewStringUTF(ENVPAR cstr);
        ENVPTR->SetObjectArrayElement(ENVPAR j_buf, i, jstr);
        pos += str_len;
    }

    if (c_buf)
        free(c_buf);

    if (cstr)
        free (cstr);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite_short
 * Signature: (JJJJJ[SZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1short
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jshortArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jshort  *buffP;
    jboolean isCopy;

    if (buf == NULL ) {
        h5nullArgument(env, "H5Dwrite_short:  buf is NULL");
        return -1;
    }

    PIN_SHORT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dwrite_short:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_SHORT_ARRAY(JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite_int
 * Signature: (JJJJJ[IZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1int
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jintArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jint    *buffP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument(env, "H5Dwrite_int:  buf is NULL");
        return -1;
    }

    PIN_INT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dwrite_int:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_INT_ARRAY(JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite_long
 * Signature: (JJJJJ[JZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1long
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jlongArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jlong   *buffP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument(env, "H5Dwrite_long:  buf is NULL");
        return -1;
    }

    PIN_LONG_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dwrite_long:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_LONG_ARRAY(JNI_ABORT);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite_float
 * Signature: (JJJJJ[FZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1float
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jfloatArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jfloat  *buffP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument(env, "H5Dwrite_float:  buf is NULL");
        return -1;
    }

    PIN_FLOAT_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dwrite_float:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_FLOAT_ARRAY(JNI_ABORT);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite_double
 * Signature: (JJJJJ[DZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1double
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jdoubleArray buf, jboolean isCriticalPinning)
{
    herr_t   status;
    jdouble *buffP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument(env, "H5Dwrite_double:  buf is NULL");
        return -1;
    }

    PIN_DOUBLE_ARRAY();
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dwrite_double:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    UNPIN_DOUBLE_ARRAY(JNI_ABORT);
    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

// Rosetta Biosoftware
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5DwriteString
 * Signature: (JJJJJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DwriteString
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jobjectArray buf)
{
    herr_t  status;
    char  **wdata;
    jsize   size;
    jsize    i;

    if (buf == NULL) {
        h5nullArgument(env, "H5DwriteString:  buf is NULL");
        return -1;
    }

    size = ENVPTR->GetArrayLength(ENVPAR (jarray) buf);
    wdata = (char**)malloc(size * sizeof (char*));

    if (!wdata) {
        h5JNIFatalError(env, "H5DwriteString:  cannot allocate buffer");
        return -1;
    }

    memset(wdata, 0, size * sizeof(char*));
    for (i = 0; i < size; ++i) {
        jstring obj = (jstring) ENVPTR->GetObjectArrayElement(ENVPAR (jobjectArray) buf, i);
        if (obj != 0) {
            jsize length = ENVPTR->GetStringUTFLength(ENVPAR obj);
            const char *utf8 = ENVPTR->GetStringUTFChars(ENVPAR obj, 0);

            if (utf8) {
                wdata[i] = (char*)malloc(length + 1);
                if (wdata[i]) {
                  memset(wdata[i], 0, (length + 1));
                  strncpy(wdata[i], utf8, length);
                }
           }

           ENVPTR->ReleaseStringUTFChars(ENVPAR obj, utf8);
           ENVPTR->DeleteLocalRef(ENVPAR obj);
        }
    } /*for (i = 0; i < size; ++i) */

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, wdata);

    // now free memory
    for (i = 0; i < size; i++) {
       if(wdata[i]) {
           free(wdata[i]);
       }
    }
    free(wdata);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5DwriteNotString
 * Signature: (JJJJJ[BZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DwriteNotString
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jbyteArray buf, jboolean isCriticalPinning)
{
    herr_t  status;
    hvl_t  *wdata;
    size_t  size;
    jsize   n;
    jbyte   *buffP;
    jboolean isCopy;
    jint    i;
    jint    j;

    if (buf == NULL) {
        h5nullArgument(env, "H5DwriteNotString:  buf is NULL");
        return -1;
    }

    PIN_BYTE_ARRAY();

    if (buffP == NULL) {
        h5JNIFatalError( env, "H5DwriteNotString:  buf not pinned");
        return -1;
    }

    /* rebuild VL structure */
    n = ENVPTR->GetArrayLength(ENVPAR (jarray) buf);
    wdata = (hvl_t*)calloc(n, sizeof(hvl_t));

    if (!wdata) {
        h5JNIFatalError(env, "H5DwriteNotString:  cannot allocate buffer");
        return -1;
    }

    size = H5Tget_size(mem_type_id);
    memset(wdata, 0, n * sizeof(hvl_t));
    /* Allocate and initialize VL data to write */
//    for (i = 0; i < n; i++) {
//        jbyte *obj = (jbyte *) ENVPTR->GetByteArrayElement(ENVPAR (jbyteArray) buf, i);
//        if (obj != 0) {
//            jsize length = ENVPTR->GetStringUTFLength(ENVPAR obj);
//            const char *utf8 = ENVPTR->GetStringUTFChars(ENVPAR obj, 0);
//
//            if (utf8) {
//                wdata[i].p = malloc(length * size);
//                if (wdata[i].p == NULL) {
//                    h5JNIFatalError(env, "H5DwriteNotString:  cannot allocate memory for VL data!");
//                    return -1;
//                } /* end if */
//                wdata[i].len = length;
//                for(j = 0; j < length; j++)
//                    switch(mem_type_id) {
//                    case float:
//                        ((float *)wdata[i].p)[j] = (float)(utf8);
//                        break;
//                    }
//            }
//
//            ENVPTR->ReleaseStringUTFChars(ENVPAR obj, utf8);
//            ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);
//        }
//    } /*for (i = 0; i < n; ++i) */

    UNPIN_BYTE_ARRAY(0);

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
                      (hid_t)file_space_id, (hid_t)xfer_plist_id, wdata);

    // now free memory
    for (i = 0; i < n; i++) {
       if(wdata[i].p) {
           free(wdata[i].p);
       }
    }
    free(wdata);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/**
 *  Read VLEN data into array of arrays.
 *  Object[] buf contains VL arrays of data points
 *  Currently only deal with variable length of atomic data types
 */
/* old version */

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5DreadVL
 * Signature: (JJJJJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DreadVL
  (JNIEnv *env, jclass clss, jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
          jlong file_space_id, jlong xfer_plist_id, jobjectArray buf)
{
    htri_t isStr=0, isComplex=0, isVlenStr=0;

    if (buf == NULL) {
        h5nullArgument(env, "H5DreadVL:  buf is NULL");
        return -1;
    }

    /* fixed bug 951
    if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        hid_t nested_tid = H5Tget_member_type((hid_t)mem_type_id, 0);
        isStr = H5Tis_variable_str(nested_tid);
        H5Tclose(nested_tid);
    }
    else
        isStr = H5Tis_variable_str((hid_t)mem_type_id);
    */

    /* fixed bug 2105, the following line does not detect array of vlen strings
    isStr = H5Tdetect_variable_str((hid_t)mem_type_id);
    */

    isStr = H5Tdetect_class((hid_t)mem_type_id, H5T_STRING);


    /* fixed compound of vlen of compound */
    if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        hid_t nested_tid = H5Tget_member_type((hid_t)mem_type_id, 0);
        isComplex = H5Tdetect_class((hid_t)nested_tid, H5T_COMPOUND) ||
                    H5Tdetect_class((hid_t)nested_tid, H5T_VLEN);
        H5Tclose(nested_tid);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_VLEN) {
      isVlenStr = 1; /* strings created by H5Tvlen_create( H5T_C_S1) */
    }

    if (isStr == 0 || isComplex>0 || isVlenStr) {
        return (jint) H5DreadVL_notstr (env, (hid_t)dataset_id, (hid_t)mem_type_id,
                                     (hid_t)mem_space_id, (hid_t)file_space_id,
                                     (hid_t)xfer_plist_id, buf);
    }

    if (isStr > 0) {
        return (jint) H5DreadVL_str (env, (hid_t)dataset_id, (hid_t)mem_type_id,
                                     (hid_t)mem_space_id, (hid_t)file_space_id,
                                     (hid_t)xfer_plist_id, buf);
    }


    return -1;
}

herr_t H5DreadVL_notstr (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid,
    hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf)
{
    jint    i;
    jint    n;
    jstring jstr;
    herr_t  status;
    h5str_t h5str;
    hvl_t  *rdata;
    size_t  size;
    size_t  max_len = 0;


    n = ENVPTR->GetArrayLength(ENVPAR buf);

    rdata = (hvl_t*)calloc(n, sizeof(hvl_t));
    if (rdata == NULL) {
        h5JNIFatalError(env, "H5DreadVL_notstr:  failed to allocate buff for read");
        return -1;
    }

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, rdata);

    if (status < 0) {
        H5Dvlen_reclaim(tid, mem_sid, xfer_plist_id, rdata);
        free(rdata);
        h5JNIFatalError(env, "H5DreadVL_notstr: failed to read data");
        return -1;
    }

    max_len = 1;
    for (i=0; i<n; i++) {
        if ((rdata + i)->len > max_len)
            max_len = (rdata + i)->len;
    }

    size = H5Tget_size(tid) * max_len;
    memset(&h5str, 0, sizeof(h5str_t));
    h5str_new(&h5str, 4 * size);

    if (h5str.s == NULL) {
        H5Dvlen_reclaim(tid, mem_sid, xfer_plist_id, rdata);
        free(rdata);
        h5JNIFatalError(env, "H5DreadVL_notstr:  failed to allocate strng buf");
        return -1;
    }

    for (i=0; i<n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, did, tid, rdata+i, 0);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }
    h5str_free(&h5str);

    H5Dvlen_reclaim(tid, mem_sid, xfer_plist_id, rdata);
    free(rdata);

    return status;
}

herr_t H5DreadVL_str (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t
    file_sid, hid_t xfer_plist_id, jobjectArray buf)
{
    char  **strs;
    jstring jstr;
    jint    i;
    jint    n;
    herr_t  status = -1;

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    strs =(char**)calloc(n, sizeof(char*));

    if (strs == NULL) {
        h5JNIFatalError(env, "H5DreadVL_str:  failed to allocate buff for read variable length strings");
        return -1;
    }

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, strs);

    if (status < 0) {
        H5Dvlen_reclaim(tid, mem_sid, xfer_plist_id, strs);
        free(strs);
        h5JNIFatalError(env, "H5DreadVL_str: failed to read variable length strings");
        return -1;
    }

    for (i=0; i<n; i++) {
        jstr = ENVPTR->NewStringUTF(ENVPAR strs[i]);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
        free (strs[i]);
    }

    /*
    for repeatedly reading a dataset with a large number of strs (e.g., 1,000,000 strings,
    H5Dvlen_reclaim() may crash on Windows because the Java GC will not be able to collect
    free space in time. Instead, use "free(strs[i])" above to free individual strings
    after it is done.
    H5Dvlen_reclaim(tid, mem_sid, xfer_plist_id, strs);
    */

    free(strs);

    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_reg_ref
 * Signature: (JJJJJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1reg_1ref (JNIEnv *env, jclass clss,
        jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
        jlong file_space_id, jlong xfer_plist_id, jobjectArray buf)
{
    herr_t    status;
    h5str_t   h5str;
    size_t    size;
    hdset_reg_ref_t *ref_data;
    jint      i;
    jint      n;
    jstring   jstr;

    hid_t region = -1;
    hid_t did = (hid_t) dataset_id;
    hid_t tid = (hid_t) mem_type_id;
    hid_t mem_sid = (hid_t) mem_space_id;
    hid_t file_sid = (hid_t) file_space_id;

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    size = sizeof(hdset_reg_ref_t); /*H5Tget_size(tid);*/
    ref_data = (hdset_reg_ref_t*)malloc(size * n);

    if (ref_data == NULL) {
        h5JNIFatalError(env, "H5Dread_reg_ref:  failed to allocate buff for read");
        return -1;
    }

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, ref_data);

    if (status < 0) {
        free(ref_data);
        h5JNIFatalError(env, "H5Dread_reg_ref: failed to read data");
        return -1;
    }

    memset(&h5str, 0, sizeof(h5str_t));
    h5str_new(&h5str, 1024);
    for (i=0; i<n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, did, tid, ref_data[i], 0);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);

        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);
    free(ref_data);

    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread_reg_ref_data
 * Signature: (JJJJJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1reg_1ref_1data (JNIEnv *env, jclass clss,
        jlong dataset_id, jlong mem_type_id, jlong mem_space_id,
        jlong file_space_id, jlong xfer_plist_id, jobjectArray buf)
{
    herr_t    status;
    h5str_t   h5str;
    size_t    size;
    hdset_reg_ref_t *ref_data;
    jint      i;
    jint      n;
    jstring   jstr;

    hid_t        region_obj;
    H5S_sel_type region_type;

    hid_t region = -1;
    hid_t did = (hid_t) dataset_id;
    hid_t tid = (hid_t) mem_type_id;
    hid_t mem_sid = (hid_t) mem_space_id;
    hid_t file_sid = (hid_t) file_space_id;

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    size = sizeof(hdset_reg_ref_t); /*H5Tget_size(tid);*/
    ref_data = (hdset_reg_ref_t*)malloc(size * n);

    if (ref_data == NULL) {
        h5JNIFatalError(env, "H5Dread_reg_ref_data:  failed to allocate buff for read");
        return -1;
    }

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, ref_data);

    if (status < 0) {
        free(ref_data);
        h5JNIFatalError(env, "H5Dread_reg_ref_data: failed to read data");
        return -1;
    }

    memset(&h5str, 0, sizeof(h5str_t));
    h5str_new(&h5str, 1024);
    for (i=0; i<n; i++) {
        h5str.s[0] = '\0';

        /* get name of the dataset the region reference points to using H5Rget_name */
        region_obj = H5Rdereference2(did, H5P_DEFAULT, H5R_DATASET_REGION, ref_data[i]);
        if (region_obj >= 0) {
            region = H5Rget_region(did, H5R_DATASET_REGION, ref_data[i]);
            if (region >= 0) {
				region_type = H5Sget_select_type(region);
				if(region_type==H5S_SEL_POINTS) {
					h5str_dump_region_points_data(&h5str, region, region_obj);
				}
				else {
					h5str_dump_region_blocks_data(&h5str, region, region_obj);
				}

                H5Sclose(region);
            }
            H5Dclose(region_obj);
        }
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);

        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);
    free(ref_data);

    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dcreate2
 * Signature: (JLjava/lang/String;JJJJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dcreate2
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong type_id,
          jlong space_id, jlong link_plist_id, jlong create_plist_id, jlong access_plist_id)
{
    hid_t    status;
    char    *file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument(env, "_H5Dcreate2:  name is NULL");
        return -1;
    }
    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError(env, "_H5Dcreate2:  file name not pinned");
        return -1;
    }

    status = H5Dcreate2((hid_t)loc_id, (const char*)file, (hid_t)type_id, (hid_t)space_id, (hid_t)link_plist_id, (hid_t)create_plist_id, (hid_t)access_plist_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, file);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dopen2
 * Signature: (JLjava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dopen2
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong access_plist)
{
    hid_t    status;
    char    *file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument(env, "_H5Dopen2:  name is NULL");
        return -1;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError(env, "_H5Dopen2:  file name not pinned");
        return -1;
    }

    status = H5Dopen2((hid_t)loc_id, file, (hid_t)access_plist );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, file);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Dcreate_anon
 * Signature: (JJJJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Dcreate_1anon
  (JNIEnv *env, jclass clss, jlong loc_id, jlong type_id, jlong space_id, jlong dcpl_id, jlong dapl_id)
{
    hid_t status;

    status = H5Dcreate_anon((hid_t)loc_id, (hid_t)type_id, (hid_t)space_id, (hid_t)dcpl_id, (hid_t)dapl_id);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_space_status
 * Signature: (J)I;
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1space_1status
  (JNIEnv *env, jclass clss, jlong loc_id)
{
    herr_t             status;
    H5D_space_status_t space_status;

    status = H5Dget_space_status((hid_t)loc_id, (H5D_space_status_t*)&space_status);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)space_status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_access_plist
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1access_1plist
  (JNIEnv *env, jclass clss, jlong loc_id)
{
    hid_t status;

    status = H5Dget_access_plist((hid_t)loc_id);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_offset
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1offset
  (JNIEnv *env, jclass clss, jlong loc_id)
{
    haddr_t offset;

    offset = H5Dget_offset((hid_t)loc_id);
    if (offset == HADDR_UNDEF) {
        h5libraryError(env);
    }
    return (jlong)offset;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_get_buf_size_long
 * Signature: (JJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1get_1buf_1size_1long
(JNIEnv *env, jclass clss, jlong dataset_id, jlong type_id, jlong space_id)
{
  herr_t  status;
  hsize_t sz;

  status = H5Dvlen_get_buf_size((hid_t)dataset_id, (hid_t)type_id,
                                      (hid_t)space_id, (hsize_t*)&sz);

  if (status < 0) {
      h5libraryError(env);
      return -1;
  }

  return (jlong)sz;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dfill
 * Signature: ([BJ[BJJ)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dfill
  (JNIEnv *env, jclass clss, jbyteArray fill, jlong fill_type_id, jbyteArray buf, jlong buf_type_id, jlong space_id)
{
    herr_t    status;
    jbyte    *fillP;
    jbyte    *buffP;
    jboolean  isCopy1;
    jboolean  isCopy2;
    
    if(fill) {
        fillP = ENVPTR->GetByteArrayElements(ENVPAR fill, &isCopy1);
        if (fillP == NULL) {
            h5JNIFatalError( env, "H5Dfill:  fill not pinned");
            return;
        }
    }
    else
        fillP = NULL;
    
    if (buf == NULL) {
        h5nullArgument(env, "H5Dfill:  buf is NULL");
        return;
    }

    buffP = ENVPTR->GetByteArrayElements(ENVPAR buf, &isCopy2);
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Dfill:  buf not pinned");
        return;
    }
    
    status = H5Dfill((const void*)fillP, (hid_t)fill_type_id, (void*)buffP, (hid_t)buf_type_id, (hid_t)space_id);
    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf, buffP, JNI_ABORT);
        if(fillP) {
            ENVPTR->ReleaseByteArrayElements(ENVPAR fill, fillP, JNI_ABORT);
        }
        h5libraryError(env);
        return;
    }
    
    if (isCopy2 == JNI_TRUE) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf, buffP, 0);
    }
    if(fillP) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR fill, fillP, JNI_ABORT);
    }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dset_extent
 * Signature: (J[J)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dset_1extent
  (JNIEnv *env, jclass clss, jlong loc_id, jlongArray buf)
{
    herr_t    status;
    hsize_t  *dims;
    jlong    *buffP;
    jsize     rank;
    jboolean  isCopy;
    int       i = 0;

    if (buf == NULL) {
        h5nullArgument(env, "H5Dset_extent:  buf is NULL");
        return;
    }

    rank = ENVPTR->GetArrayLength(ENVPAR buf);
    if (rank <= 0) {
        h5JNIFatalError(env, "H5Dset_extent:  rank <=0");
        return;
    }

    buffP = ENVPTR->GetLongArrayElements(ENVPAR buf, &isCopy);
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dset_extent:  buf not pinned");
        return;
    }

    dims = (hsize_t*) malloc(rank * sizeof(hsize_t));
    for (i = 0; i< rank; i++)
        dims[i] = (hsize_t)buffP[i];

    status = H5Dset_extent((hid_t)loc_id, (hsize_t*)dims);

    free (dims);

    ENVPTR->ReleaseLongArrayElements(ENVPAR buf, buffP, JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
}

herr_t H5D_iterate_cb(void* elem, hid_t elem_id, unsigned ndim, const hsize_t *point, void *op_data) {
    JNIEnv    *cbenv;
    jint       status;
    jclass     cls;
    jmethodID  mid;
    jbyteArray elemArray;
    jlongArray pointArray;
    jsize      size;

    if(JVMPTR->AttachCurrentThread(JVMPAR2 (void**)&cbenv, NULL) != 0) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }
    cls = CBENVPTR->GetObjectClass(CBENVPAR visit_callback);
    if (cls == 0) {
       JVMPTR->DetachCurrentThread(JVMPAR);
       return -1;
    }
    mid = CBENVPTR->GetMethodID(CBENVPAR cls, "callback", "([BJI[JLncsa/hdf/hdf5lib/callbacks/H5D_iterate_t;)I");
    if (mid == 0) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }
    
    if (elem == NULL) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }
    if (point == NULL) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }

    size = H5Tget_size(elem_id);
    elemArray = CBENVPTR->NewByteArray(CBENVPAR size);
    if (elemArray == NULL) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }
    CBENVPTR->SetByteArrayRegion(CBENVPAR elemArray, 0, size, (jbyte *)elem);
    
    pointArray = CBENVPTR->NewLongArray(CBENVPAR 2);
    if (pointArray == NULL) {
        JVMPTR->DetachCurrentThread(JVMPAR);
        return -1;
    }
    CBENVPTR->SetLongArrayRegion(CBENVPAR pointArray, 0, 2, (const jlong *)point);

    status = CBENVPTR->CallIntMethod(CBENVPAR visit_callback, mid, (void*)elemArray, elem_id, ndim, pointArray, op_data);

    CBENVPTR->GetByteArrayRegion(CBENVPAR elemArray, 0, size, (jbyte *)elem);

    JVMPTR->DetachCurrentThread(JVMPAR);
    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Diterate
 * Signature: ([BJJLjava/lang/Object;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Diterate
  (JNIEnv *env, jclass clss, jbyteArray buf, jlong buf_type, jlong space,
          jobject callback_op, jobject op_data)
{
    jboolean      isCopy;
    jbyte        *buffP;
    herr_t        status = -1;
    
    ENVPTR->GetJavaVM(ENVPAR &jvm);
    visit_callback = callback_op;

    if (op_data == NULL) {
        h5nullArgument(env,  "H5Diterate:  op_data is NULL");
        return -1;
    }
    if (callback_op == NULL) {
        h5nullArgument(env,  "H5Diterate:  callback_op is NULL");
        return -1;
    }

    if (buf == NULL) {
        h5nullArgument(env,  "H5Diterate:  buf is NULL");
        return -1;
    }
    buffP = ENVPTR->GetByteArrayElements(ENVPAR buf, &isCopy);
    if (buffP == NULL) {
        h5JNIFatalError(env, "H5Diterate:  buf not pinned");
        return -1;
    }
    
    status = H5Diterate((void*)buffP, (hid_t)buf_type, (hid_t)space, (H5D_operator_t)H5D_iterate_cb, (void*)op_data);
    
    if (status < 0) {
       ENVPTR->ReleaseByteArrayElements(ENVPAR buf, buffP, JNI_ABORT);
       h5libraryError(env);
       return status;
    }
    
    if (isCopy == JNI_TRUE) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf, buffP, 0);
    }
    
    return status;
}


#ifdef __cplusplus
}
#endif
