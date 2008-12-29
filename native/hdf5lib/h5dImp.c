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
 *  Dataset Object API Functions of HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/&isCopy/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include "h5util.h"
#include <jni.h>
#include <stdlib.h>
#include <string.h>

extern jboolean h5outOfMemory( JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

herr_t H5DreadVL_str (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf);
herr_t H5DreadVL_num (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dcreate
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dcreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id,
  jint space_id, jint create_plist_id)
{
    herr_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Dcreate:  name is NULL");
        return -1;
    }
#ifdef __cplusplus
    file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
    file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dcreate:  file name not pinned");
        return -1;
    }

    status = H5Dcreate(loc_id, file, type_id, space_id, create_plist_id);

#ifdef __cplusplus
    env->ReleaseStringUTFChars(name,file);
#else
    (*env)->ReleaseStringUTFChars(env,name,file);
#endif
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dchdir_ext
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dchdir_1ext
  (JNIEnv *env, jclass clss, jstring dir_name)
{
    hid_t status;
    char* file;
    jboolean isCopy;

    if (dir_name == NULL) {
        h5nullArgument( env, "H5Dchdir_ext:  dir_name is NULL");
        return -1;
    }
#ifdef __cplusplus
    file = (char *)env->GetStringUTFChars(dir_name,&isCopy);
#else
    file = (char *)(*env)->GetStringUTFChars(env,dir_name,&isCopy);
#endif
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dchdir_ext:  file dir not pinned");
        return -1;
    }
    status = chdir ( file );

#ifdef __cplusplus
    env->ReleaseStringUTFChars(dir_name,file);
#else
    (*env)->ReleaseStringUTFChars(env,dir_name,file);
#endif
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dgetdir_1ext
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dgetdir_1ext
  (JNIEnv *env, jclass clss, jstring dir_name, jint buf_size)
{
    char *aName;
    jstring str;

    if (buf_size <= 0) {
        h5badArgument( env, "H5Dgetcwd:  buf_size <= 0");
        return -1;
    }
    aName = (char*)malloc(sizeof(char)*buf_size);
    if (aName == NULL) {
        h5outOfMemory( env, "H5Dgetcwd:  malloc failed");
        return -1;
    }
    getcwd( (char *)aName, (size_t)buf_size);

    str = (*env)->NewStringUTF(env,aName);

    if (str == NULL) {
        free(aName);
        h5JNIFatalError( env,"H5Dgetcwd:  return string failed");
        return -1;
    }
    free(aName);

    (*env)->SetObjectArrayElement(env,dir_name,0,str);

    return 0;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dopen
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dopen
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
    hid_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Dopen:  name is NULL");
        return -1;
    }
#ifdef __cplusplus
    file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
    file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
    if (file == NULL) {
        h5JNIFatalError( env, "H5Dopen:  file name not pinned");
        return -1;
    }
    status = H5Dopen((hid_t)loc_id, file );

#ifdef __cplusplus
    env->ReleaseStringUTFChars(name,file);
#else
    (*env)->ReleaseStringUTFChars(env,name,file);
#endif
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_space
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1space
  (JNIEnv *env, jclass clss, jint dataset_id)
{
    hid_t retVal = -1;
    retVal =  H5Dget_space((hid_t)dataset_id );
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1type
  (JNIEnv *env, jclass clss, jint dataset_id)
{
    hid_t retVal = -1;
    retVal = H5Dget_type((hid_t)dataset_id );
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1create_1plist
  (JNIEnv *env, jclass clss, jint dataset_id)
{
    hid_t retVal = -1;
    retVal =  H5Dget_create_plist((hid_t)dataset_id );
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;


    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    byteP = env->GetByteArrayElements(buf,&isCopy);
#else
    byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
    if (byteP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, byteP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(buf,byteP,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,buf,byteP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(buf,byteP,0);
#else
        (*env)->ReleaseByteArrayElements(env,buf,byteP,0);
#endif
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DreadNIO
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jobject buf)
{
    herr_t status;
    jbyte *byteP;

	byteP = (*env)->GetDirectBufferAddress(env, buf);
    if (byteP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, byteP);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dwrite
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }

#ifdef __cplusplus
    byteP = env->GetByteArrayElements(buf,&isCopy);
#else
    byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
    if (byteP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, byteP);

#ifdef __cplusplus
    env->ReleaseByteArrayElements(buf,byteP,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,buf,byteP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dextend
 * Signature: (IB)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dextend
  (JNIEnv *env, jclass clss, jint dataset_id, jbyteArray size)
{
    herr_t status;
    jbyte *P;
    jboolean isCopy;
    hsize_t *sa;
    int i;
    int rank;
    hsize_t *lp;
    jlong *jlp;

    if ( size == NULL ) {
        h5nullArgument( env, "H5Dextend:  array of sizes is NULL");
        return -1;
    }
    /*
     *  Future:  check that the array has correct
     *           rank (same as dataset dataset_id)
     */
#ifdef __cplusplus
    P = env->GetByteArrayElements(size,&isCopy);
#else
    P = (*env)->GetByteArrayElements(env,size,&isCopy);
#endif
    if (P == NULL) {
        h5JNIFatalError( env, "H5Dextend:  array not pinned");
        return -1;
    }
#ifdef __cplusplus
    i = env->GetArrayLength(size);
#else
    i = (*env)->GetArrayLength(env,size);
#endif
    rank = i / sizeof(jlong);
    sa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
    if (sa == NULL)  {
#ifdef __cplusplus
        env->ReleaseByteArrayElements(size,P,JNI_ABORT);
#else
        (*env)->ReleaseByteArrayElements(env,size,P,JNI_ABORT);
#endif
        h5JNIFatalError(env,  "H5Dextend:  size not converted to hsize_t");
        return -1;
    }
    jlp = (jlong *)P;
    for (i = 0; i < rank; i++) {
        *lp = (hsize_t)*jlp;
        lp++;
        jlp++;
    }

    status = H5Dextend((hid_t)dataset_id, (hsize_t *)sa);

#ifdef __cplusplus
    env->ReleaseByteArrayElements(size,P,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,size,P,JNI_ABORT);
#endif
    free(sa);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dclose
  (JNIEnv *env, jclass clss, jint dataset_id)
{
    hid_t retVal = 0;

	if (dataset_id > 0)
	    retVal =  H5Dclose((hid_t)dataset_id );

    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_storage_size
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1storage_1size
  (JNIEnv *env, jclass clss, jint dataset_id)
{
    hsize_t retVal = (hsize_t)-1;
    retVal =  H5Dget_storage_size((hid_t)dataset_id );
/* probably returns '0' if fails--don't do an exception
    if (retVal < 0) {
        h5libraryError(env);
    }
*/
    return (jlong)retVal;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dcopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dcopy
  (JNIEnv *env, jclass clss, jint src_id, jint dst_id)
{
    jbyte *buf;
    herr_t retVal = -1;
    hid_t src_did = (hid_t)src_id;
    hid_t dst_did = (hid_t)dst_id;
    hid_t tid=-1;
    hid_t sid=-1;
    hsize_t total_size = 0;


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

    total_size = H5Sget_simple_extent_npoints(sid) *
            H5Tget_size(tid);

    H5Sclose(sid);

    buf = (jbyte *)malloc( (int) (total_size * sizeof(jbyte)));
        if (buf == NULL) {
        H5Tclose(tid);
                h5outOfMemory( env, "H5Dcopy:  malloc failed");
                return -1;
        }

    retVal = H5Dread(src_did, tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);
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
    retVal = H5Dwrite(dst_did, tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);
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
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1get_1buf_1size
  (JNIEnv *env, jclass clss,
jint dataset_id, jint type_id, jint space_id, jintArray size )
{
    herr_t status;
    jint *P;
    jboolean isCopy;
    hsize_t sz;

    if ( size == NULL ) {
        h5nullArgument( env, "H5Dvlen_get_buf_size:  size is NULL");
        return -1;
    }

#ifdef __cplusplus
    P = env->GetIntArrayElements(size,&isCopy);
#else
    P = (*env)->GetIntArrayElements(env,size,&isCopy);
#endif
    if (P == NULL) {
        h5JNIFatalError( env, "H5Dvlen_get_buf_size:  array not pinned");
        return -1;
    }

    status = (jint)H5Dvlen_get_buf_size((hid_t) dataset_id,
        (hid_t) type_id, (hid_t) space_id, (hsize_t *)&sz);



    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(size,P,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,size,P,JNI_ABORT);
#endif
        h5libraryError(env);
    } else {
        P[0] = (jint)sz;
#ifdef __cplusplus
        env->ReleaseIntArrayElements(size,P,0);
#else
        (*env)->ReleaseIntArrayElements(env,size,P,0);
#endif
    }
    return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_reclaim
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1reclaim
  (JNIEnv *env, jclass clss, jint type_id, jint space_id,
   jint xfer_plist_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    byteP = env->GetByteArrayElements(buf,&isCopy);
#else
    byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
    if (byteP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dvlen_reclaim((hid_t)type_id,
        (hid_t)space_id, (hid_t)xfer_plist_id, byteP);

#ifdef __cplusplus
    env->ReleaseByteArrayElements(buf,byteP,JNI_ABORT);
#else
    (*env)->ReleaseByteArrayElements(env,buf,byteP,JNI_ABORT);
#endif

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
 * Method:    H5Dget_space_status(hid_t dset_id, H5D_space_status_t *status)
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1space_1status
  (JNIEnv *env, jclass clss, jint dset_id, jintArray status)
{
    herr_t retVal = -1;
    jint *theArray;
    jboolean isCopy;
    H5D_space_status_t space_status;


    if (status == NULL) {
        /* exception ? */
        h5nullArgument( env, "H5Dget_space_status:  status is NULL");
        return -1;
    }
#ifdef __cplusplus
    theArray = (jint *)env->GetIntArrayElements(status,&isCopy);
#else
    theArray = (jint *)(*env)->GetIntArrayElements(env,status,&isCopy);
#endif
    if (theArray == NULL) {
        h5JNIFatalError( env, "H5Dget_space_status:  status not pinned");
        return -1;
    }

    retVal =  H5Dget_space_status((hid_t)dset_id, &space_status );

    if (retVal < 0) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(status,theArray,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,status,theArray,JNI_ABORT);
#endif
        h5libraryError(env);
    } else {
        theArray[0] = space_status;
#ifdef __cplusplus
        env->ReleaseIntArrayElements(status,theArray,0);
#else
        (*env)->ReleaseIntArrayElements(env,status,theArray,0);
#endif
    }

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
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1short
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jshortArray buf)
{
    herr_t status;
    jshort *buffP;
    jboolean isCopy;

	if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }

#ifdef __cplusplus
    buffP = env->GetShortArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetShortArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseShortArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseShortArrayElements(env,buf,buffP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseShortArrayElements(buf,buffP,0);
#else
        (*env)->ReleaseShortArrayElements(env,buf,buffP,0);
#endif
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1int
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jintArray buf)
{
    herr_t status;
    jint *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetIntArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetIntArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseIntArrayElements(env,buf,buffP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseIntArrayElements(buf,buffP,0);
#else
        (*env)->ReleaseIntArrayElements(env,buf,buffP,0);
#endif
    }

    return (jint)status;
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1long
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jlongArray buf)
{
    herr_t status;
    jlong *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetLongArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetLongArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseLongArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseLongArrayElements(env,buf,buffP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseLongArrayElements(buf,buffP,0);
#else
        (*env)->ReleaseLongArrayElements(env,buf,buffP,0);
#endif
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1float
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jfloatArray buf)
{
    herr_t status;
    jfloat *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetFloatArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetFloatArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseFloatArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseFloatArrayElements(env,buf,buffP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseFloatArrayElements(buf,buffP,0);
#else
        (*env)->ReleaseFloatArrayElements(env,buf,buffP,0);
#endif
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1double
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jdoubleArray buf)
{
    herr_t status;
    jdouble *buffP;
    jboolean isCopy;

	if ( buf == NULL ) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }

#ifdef __cplusplus
    buffP = env->GetDoubleArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetDoubleArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dread:  buf not pinned");
        return -1;
    }
    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

    if (status < 0) {
#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseDoubleArrayElements(env,buf,buffP,JNI_ABORT);
#endif
        h5libraryError(env);
    } else  {
#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(buf,buffP,0);
#else
        (*env)->ReleaseDoubleArrayElements(env,buf,buffP,0);
#endif
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1string
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jobjectArray j_buf)
{
    herr_t status;
    jstring jstr;
    char *c_buf;
    char *cstr;
    size_t str_len, i, n, pos;

    c_buf = cstr = NULL;
    if ( j_buf == NULL) {
        h5nullArgument( env, "H5Dread:  buf is NULL");
        return -1;
    }

    n = (*env)->GetArrayLength(env, j_buf);
    if ( n<=0) {
        h5nullArgument( env, "H5Dread:  buf length <=0");
        return -1;
    }

    if ( (str_len = H5Tget_size((hid_t)mem_type_id)) <=0 ) {
        h5libraryError(env);
    }

    if ( (cstr = (char *)malloc(str_len+1)) == NULL) {
        h5JNIFatalError(env,  "H5Dread_string: memory allocation failed.");
        return -1;
    }

    if ( (c_buf = (char *)malloc(n*str_len)) == NULL) {
        if (cstr) free (cstr); cstr = NULL;
        h5JNIFatalError(env,  "H5Dread_string: memory allocation failed.");
        return -1;
    }

    status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, c_buf);

    if (status < 0) {
        if (cstr) free (cstr); cstr = NULL;
        if (c_buf) free (c_buf); c_buf = NULL;
        h5libraryError(env);
        return -1;
    }

    pos = 0;
    for (i=0; i<n; i++) {
        memcpy(cstr, c_buf+pos, str_len);
        cstr[str_len] = '\0';
        jstr = (*env)->NewStringUTF(env, cstr);
        (*env)->SetObjectArrayElement(env, j_buf, i, jstr);
        pos += str_len;
    }

    if (c_buf)
        free(c_buf);

    if (cstr)
        free (cstr);


    return (jint)status;
}


/**
 *  Read VLEN data into array of arrays.
 *  Object[] buf contains VL arrays of data points
 *  Currently only deal with variable length of atomic data types
 */
/* old versin */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DreadVL
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jobjectArray buf)
{
	htri_t isStr=0;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5DreadVL:  buf is NULL");
        return -1;
    }

	if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        hid_t nested_tid = H5Tget_member_type((hid_t)mem_type_id, 0);
		isStr = H5Tis_variable_str(nested_tid);
		H5Tclose(nested_tid);
    }
	else
		isStr = H5Tis_variable_str((hid_t)mem_type_id);


	if (isStr > 0)
	{
		return (jint) H5DreadVL_str (env, (hid_t)dataset_id, (hid_t)mem_type_id,
			(hid_t) mem_space_id,(hid_t) file_space_id, (hid_t)xfer_plist_id, buf);
	}
	else if (isStr == 0)
	{
		return (jint) H5DreadVL_num (env, (hid_t)dataset_id, (hid_t)mem_type_id,
			(hid_t) mem_space_id,(hid_t) file_space_id, (hid_t)xfer_plist_id, buf);
	}
	else
		return -1;
}

herr_t H5DreadVL_num (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf)
{
    herr_t status;
	int i, n;
	size_t max_len=0;
	h5str_t h5str;
	jstring jstr;
	hvl_t *rdata;
	size_t size;

	n = (*env)->GetArrayLength(env, buf);

	rdata = (hvl_t *)calloc(n, sizeof(hvl_t));
	if (rdata == NULL) {
        h5JNIFatalError( env, "H5DreadVL:  failed to allocate buff for read");
        return -1;
    }

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, rdata);

    if (status < 0) {
		H5Dvlen_reclaim(tid, mem_sid, H5P_DEFAULT, rdata);
		free(rdata);
        h5JNIFatalError(env, "H5DreadVL: failed to read data");
		return -1;
	}

	max_len = 1;
	for (i=0; i<n; i++)
	{
		if ((rdata+i)->len > max_len)
			max_len = (rdata+i)->len;
	}

	size = H5Tget_size(tid)*max_len;
	memset(&h5str, 0, sizeof(h5str_t));
	h5str_new(&h5str, 4*size);

	if (h5str.s == NULL)
	{
		H5Dvlen_reclaim(tid, mem_sid, H5P_DEFAULT, rdata);
		free(rdata);
        h5JNIFatalError( env, "H5DreadVL:  failed to allocate strng buf");
		return -1;
	}

	for (i=0; i<n; i++)
	{
		h5str.s[0] = '\0';
		h5str_sprintf(&h5str, did, tid, rdata+i);
		jstr = (*env)->NewStringUTF(env, h5str.s);
		(*env)->SetObjectArrayElement(env, buf, i, jstr);
	}

	h5str_free(&h5str);
	H5Dvlen_reclaim(tid, mem_sid, H5P_DEFAULT, rdata);
	free(rdata);

	return status;
}

herr_t H5DreadVL_str (JNIEnv *env, hid_t did, hid_t tid, hid_t mem_sid, hid_t file_sid, hid_t xfer_plist_id, jobjectArray buf)
{
    herr_t status=-1;
	jstring jstr;
	char **strs;
	int i, n;

	n = (*env)->GetArrayLength(env, buf);
	strs =(char **)malloc(n*sizeof(char *));

	if (strs == NULL)
	{
        h5JNIFatalError( env, "H5DreadVL:  failed to allocate buff for read variable length strings");
		return -1;
	}

    status = H5Dread(did, tid, mem_sid, file_sid, xfer_plist_id, strs);

    if (status < 0) {
		H5Dvlen_reclaim(tid, mem_sid, H5P_DEFAULT, strs);
		free(strs);
        h5JNIFatalError(env, "H5DreadVL: failed to read variable length strings");
		return -1;
	}

	for (i=0; i<n; i++)
	{
		jstr = (*env)->NewStringUTF(env, strs[i]);
		(*env)->SetObjectArrayElement(env, buf, i, jstr);
		free (strs[i]);
	}

	/*
	for repeatly reading a dataset with a large number of strs (e.g., 1,000,000 strings,
	H5Dvlen_reclaim() may crash on Windows because the Java GC will not be able to collect
	free space in time. Instead, use "free(strs[i])" above to free individual strings
	after it is done.
	H5Dvlen_reclaim(tid, mem_sid, H5P_DEFAULT, strs);
	*/

	free(strs);

	return status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread_1reg_1ref (JNIEnv *env, jclass clss,
    jint dataset_id, jint mem_type_id, jint mem_space_id,
    jint file_space_id, jint xfer_plist_id, jobjectArray buf)
{
    herr_t status;
	int i, n;
	h5str_t h5str;
	jstring jstr;

	hdset_reg_ref_t *ref_data;
	size_t size;

    hid_t region = -1;
    hid_t did = (hid_t) dataset_id;
    hid_t tid = (hid_t) mem_type_id;
    hid_t mem_sid = (hid_t) mem_space_id;
    hid_t file_sid = (hid_t) file_space_id;

	n = (*env)->GetArrayLength(env, buf);
	size = sizeof(hdset_reg_ref_t); /*H5Tget_size(tid);*/
	ref_data = malloc(size*n);

	if (ref_data == NULL) {
        h5JNIFatalError( env, "H5Dread_reg_ref:  failed to allocate buff for read");
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
	for (i=0; i<n; i++)
	{
		h5str.s[0] = '\0';
		h5str_sprintf(&h5str, did, tid, ref_data[i]);
		jstr = (*env)->NewStringUTF(env, h5str.s);

		(*env)->SetObjectArrayElement(env, buf, i, jstr);
	}

	h5str_free(&h5str);
	free(ref_data);

	return status;
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1short
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jshortArray buf)
{
    herr_t status;
    jshort *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetShortArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetShortArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

#ifdef __cplusplus
    env->ReleaseShortArrayElements(buf,buffP,JNI_ABORT);
#else
    (*env)->ReleaseShortArrayElements(env,buf,buffP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1int
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jintArray buf)
{
    herr_t status;
    jint *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetIntArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetIntArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

#ifdef __cplusplus
    env->ReleaseIntArrayElements(buf,buffP,JNI_ABORT);
#else
    (*env)->ReleaseIntArrayElements(env,buf,buffP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1long
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jlongArray buf)
{
    herr_t status;
    jlong *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetLongArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetLongArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

#ifdef __cplusplus
    env->ReleaseLongArrayElements(buf,buffP,JNI_ABORT);
#else
    (*env)->ReleaseLongArrayElements(env,buf,buffP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1float
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jfloatArray buf)
{
    herr_t status;
    jfloat *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }
#ifdef __cplusplus
    buffP = env->GetFloatArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetFloatArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

#ifdef __cplusplus
        env->ReleaseFloatArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseFloatArrayElements(env,buf,buffP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite_1double
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jdoubleArray buf)
{
    herr_t status;
    jdouble *buffP;
    jboolean isCopy;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }

#ifdef __cplusplus
    buffP = env->GetDoubleArrayElements(buf,&isCopy);
#else
    buffP = (*env)->GetDoubleArrayElements(env,buf,&isCopy);
#endif
    if (buffP == NULL) {
        h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
        return -1;
    }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, buffP);

#ifdef __cplusplus
        env->ReleaseDoubleArrayElements(buf,buffP,JNI_ABORT);
#else
        (*env)->ReleaseDoubleArrayElements(env,buf,buffP,JNI_ABORT);
#endif

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

// Rosetta Biosoftware
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5DwriteString
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5DwriteString
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id,
  jint file_space_id, jint xfer_plist_id, jobjectArray buf)
{
    herr_t status;
    jboolean isCopy;
        char* * wdata;
        jsize size;
        jint i, j;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5Dwrite:  buf is NULL");
        return -1;
    }

        size = (*env)->GetArrayLength(env, (jarray) buf);
        wdata = malloc(size * sizeof (char *));

        if (!wdata) {
        h5JNIFatalError( env, "H5DwriteString:  cannot allocate buffer");
        return -1;
        }

        memset(wdata, 0, size * sizeof(char *));

        for (i = 0; i < size; ++i) {
                jstring obj = (jstring) (*env)->GetObjectArrayElement(env, (jobjectArray) buf, i);
                if (obj != 0) {
                        jsize length = (*env)->GetStringUTFLength(env, obj);
                        const char * utf8 = (*env)->GetStringUTFChars(env, obj, 0);

                        if (utf8) {
                                wdata[i] = malloc(strlen(utf8)+1);
                                if (!wdata[i]) {
                                        status = -1;
                                        // can't allocate memory, cleanup
                                        for (j = 0; j < i; ++i) {
                                                if(wdata[j]) {
                                                        free(wdata[j]);
                                                }
                                        }
                                        free(wdata);

                        (*env)->ReleaseStringUTFChars(env, obj, utf8);
                        (*env)->DeleteLocalRef(env, obj);

                                        h5JNIFatalError( env, "H5DwriteString:  cannot allocate buffer");
                                        return -1;
                                }

                                strcpy(wdata[i], utf8);
                        }

                        (*env)->ReleaseStringUTFChars(env, obj, utf8);
                        (*env)->DeleteLocalRef(env, obj);
                }
        }

    status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
        (hid_t)file_space_id, (hid_t)xfer_plist_id, wdata);

        // now free memory
        for (i = 0; i < size; ++i) {
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


#ifdef __cplusplus
}
#endif
