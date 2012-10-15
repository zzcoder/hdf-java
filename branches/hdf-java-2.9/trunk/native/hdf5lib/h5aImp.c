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
 *  Attribute API Functions of the HDF5 library.
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
#include "h5util.h"
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include "h5jni.h"

herr_t H5AreadVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AreadVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AreadVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acreate
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id,
  jint space_id, jint create_plist)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Acreate:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Acreate: aName is not pinned");
        return -1;
    }

    status = H5Acreate2((hid_t)loc_id, aName, (hid_t)type_id,
        (hid_t)space_id, (hid_t)create_plist, (hid_t)H5P_DEFAULT );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_name
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env,"H5Aopen_name:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env,"H5Aopen_name: name is not pinned");
        return -1;
    }

    status = H5Aopen_name((hid_t)loc_id, aName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_idx
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jint idx)
{
    hid_t retVal = -1;
    retVal =  H5Aopen_idx((hid_t)loc_id, (unsigned int) idx );
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Awrite
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Awrite
  (JNIEnv *env, jclass clss, jint attr_id, jint mem_type_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument( env,"H5Awrite:  buf is NULL");
        return -1;
    }

    byteP = ENVPTR->GetByteArrayElements(ENVPAR buf,&isCopy);

    if (byteP == NULL) {
        h5JNIFatalError( env,"H5Awrite: buf is not pinned");
        return -1;
    }
    status = H5Awrite((hid_t)attr_id, (hid_t)mem_type_id, byteP);

    ENVPTR->ReleaseByteArrayElements(ENVPAR buf, byteP,JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aread
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aread
  (JNIEnv *env, jclass clss, jint attr_id, jint mem_type_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument( env,"H5Aread:  buf is NULL");
        return -1;
    }

    byteP = ENVPTR->GetByteArrayElements(ENVPAR buf,&isCopy);

    if (byteP == NULL) {
        h5JNIFatalError( env,"H5Aread: buf is not pinned");
        return -1;
    }

    status = H5Aread((hid_t)attr_id, (hid_t)mem_type_id, byteP);

    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf,byteP,JNI_ABORT);
        h5libraryError(env);
    } else  {

        ENVPTR->ReleaseByteArrayElements(ENVPAR buf,byteP,0);
    }

    return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_space
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aget_1space
  (JNIEnv *env, jclass clss, jint attr_id)
{
    hid_t retVal = -1;
    retVal =  H5Aget_space((hid_t)attr_id);
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aget_1type
  (JNIEnv *env, jclass clss, jint attr_id)
{
    hid_t retVal = -1;
    retVal =  H5Aget_type((hid_t)attr_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_name
 * Signature: (IJ[Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1name
  (JNIEnv *env, jclass clss, jint attr_id, jlong buf_size, jobjectArray name)
{
    char *aName;
    jstring str;
    hssize_t size;
    long bs;

    if (buf_size==0 && name == NULL)
      return (jlong) H5Aget_name((hid_t)attr_id, 0, NULL);

    bs = (long)buf_size;
    if (bs <= 0) {
        h5badArgument( env, "H5Aget_name:  buf_size <= 0");
        return -1;
    }
    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        h5outOfMemory( env, "H5Aget_name:  malloc failed");
        return -1;
    }
    size = H5Aget_name((hid_t)attr_id, (size_t)buf_size, aName);
    if (size < 0) {
        free(aName);
        h5libraryError(env);
        return -1;
        /*  exception, returns immediately */
    }
    /* successful return -- save the string; */

    str = ENVPTR->NewStringUTF(ENVPAR aName);

    if (str == NULL) {
        free(aName);
        h5JNIFatalError( env,"H5Aget_name:  return string failed");
        return -1;
    }
    free(aName);
    /*  Note: throws ArrayIndexOutOfBoundsException,
        ArrayStoreException */

    ENVPTR->SetObjectArrayElement(ENVPAR name,0,str);

    return (jlong)size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_num_attrs
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1num_1attrs
  (JNIEnv *env, jclass clss, jint loc_id)
{
    int retVal = -1;
    retVal =  H5Aget_num_attrs((hid_t)loc_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
    herr_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env,"H5Adelete:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env,"H5Adelete: name is not pinned");
        return -1;
    }

    status = H5Adelete((hid_t)loc_id, aName );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aclose
  (JNIEnv *env, jclass clss, jint attr_id)
{
    herr_t retVal = 0;

    if (attr_id > 0)
        retVal =  H5Aclose((hid_t)attr_id);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aread
 * Signature: (II[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5AreadVL
  (JNIEnv *env, jclass clss, jint attr_id, jint mem_type_id, jobjectArray buf)
{
    htri_t isStr;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5AreadVL:  buf is NULL");
        return -1;
    }

    isStr = H5Tis_variable_str((hid_t)mem_type_id);

    if (H5Tis_variable_str((hid_t)mem_type_id) > 0) {
        return (jint) H5AreadVL_str (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        return (jint) H5AreadVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_ARRAY) {
        return (jint) H5AreadVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else {
        return (jint) H5AreadVL_num (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
}

herr_t H5AreadVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t  status;
    int     i;
    int     n;
    size_t  max_len = 0;
    h5str_t h5str;
    jstring jstr;
    hvl_t  *rdata = NULL;
    size_t  size;
    hid_t   sid;
    hsize_t dims[H5S_MAX_RANK];

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    rdata = (hvl_t *)calloc(n+1, sizeof(hvl_t));
    if (rdata == NULL) {
        h5JNIFatalError( env, "H5AreadVL:  failed to allocate buff for read");
        return -1;
    }

    status = H5Aread(aid, tid, rdata);
    dims[0] = n;
    sid = H5Screate_simple(1, dims, NULL);
    if (status < 0) {
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
        H5Sclose(sid);
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL: failed to read data");
        return -1;
    }

    for (i = 0; i < n; i++) {
        if ((rdata +i)->len > max_len)
            max_len = (rdata + i)->len;
    }

    size = H5Tget_size(tid);
    memset((void *)&h5str, (int)0, (size_t)sizeof(h5str_t));
    h5str_new(&h5str, 4*size);

    if (h5str.s == NULL) {
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
        H5Sclose(sid);
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL:  failed to allocate strng buf");
        return -1;
    }

    for (i = 0; i < n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, aid, tid, rdata + i);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);
    H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
    H5Sclose(sid);

    free(rdata);

    return status;
}

herr_t H5AreadVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t      status;
    int         i;
    int         n;
    size_t      max_len = 0;
    h5str_t     h5str;
    jstring     jstr;
    char       *rdata;
    size_t      size;
    hid_t       p_type;

    p_type = H5Tget_native_type(tid, H5T_DIR_DEFAULT);
    size = (((H5Tget_size(tid))>(H5Tget_size(p_type))) ? (H5Tget_size(tid)) : (H5Tget_size(p_type)));
    H5Tclose(p_type);
    n = ENVPTR->GetArrayLength(ENVPAR buf);
    rdata = (char *)malloc(n * size);

    if (rdata == NULL) {
        h5JNIFatalError(env, "H5AreadVL:  failed to allocate buff for read");
        return -1;
    }

    status = H5Aread(aid, tid, rdata);

    if (status < 0) {
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL: failed to read data");
        return -1;
    }

    memset(&h5str, 0, sizeof(h5str_t));
    h5str_new(&h5str, 4 * size);

    if (h5str.s == NULL) {
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL:  failed to allocate strng buf");
        return -1;
    }

    for (i = 0; i < n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, aid, tid, rdata + i * size);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);

    free(rdata);

    return status;
}

herr_t H5AreadVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t status=-1;
    jstring jstr;
    char **strs;
    int i, n;
    hid_t sid;
    hsize_t dims[H5S_MAX_RANK];

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    strs =(char **)malloc(n*sizeof(char *));

    if (strs == NULL)
    {
        h5JNIFatalError( env, "H5AreadVL:  failed to allocate buff for read variable length strings");
        return -1;
    }

    status = H5Aread(aid, tid, strs);
    dims[0] = n;
    sid = H5Screate_simple(1, dims, NULL);

    if (status < 0) {
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, strs);
        H5Sclose(sid);
        free(strs);
        h5JNIFatalError(env, "H5AreadVL: failed to read variable length strings");
        return -1;
    }

    for (i=0; i<n; i++)
    {
        jstr = ENVPTR->NewStringUTF(ENVPAR strs[i]);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    /*H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, strs);*/
    H5Sclose(sid);

    for (i=0; i<n; i++) {
        if (strs[i])
            free(strs[i]);
    }

    if (strs)
        free(strs);

    return status;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Acopy
  (JNIEnv *env, jclass clss, jint src_id, jint dst_id)
{
    jbyte *buf;
    herr_t retVal = -1;
    hid_t src_did = (hid_t)src_id;
    hid_t dst_did = (hid_t)dst_id;
    hid_t tid=-1;
    hid_t sid=-1;
    hsize_t total_size = 0;


    sid = H5Aget_space(src_did);
    if (sid < 0) {
        h5libraryError(env);
        return -1;
    }

    tid = H5Aget_type(src_did);
    if (tid < 0) {
        H5Sclose(sid);
        h5libraryError(env);
        return -1;
    }

    total_size = H5Sget_simple_extent_npoints(sid) * H5Tget_size(tid);

    H5Sclose(sid);

    buf = (jbyte *)malloc( (int) (total_size * sizeof(jbyte)));
    if (buf == NULL) {
    H5Tclose(tid);
        h5outOfMemory( env, "H5Acopy:  malloc failed");
        return -1;
    }

    retVal = H5Aread(src_did, tid, buf);
    H5Tclose(tid);

    if (retVal < 0) {
        free(buf);
        h5libraryError(env);
        return (jint)retVal;
    }

    tid = H5Aget_type(dst_did);
    if (tid < 0) {
        free(buf);
        h5libraryError(env);
        return -1;
    }
    retVal = H5Awrite(dst_did, tid, buf);
    H5Tclose(tid);
    free(buf);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acreate2
 * Signature: (ILjava/lang/String;IIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Acreate2
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id,
  jint space_id, jint create_plist, jint access_plist)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Acreate2:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Acreate2: aName is not pinned");
        return -1;
    }

  status = H5Acreate2((hid_t)loc_id, aName, (hid_t)type_id,
        (hid_t)space_id, (hid_t)create_plist, (hid_t)access_plist );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}


/**********************************************************************
 *                                                                    *
 *          New functions release 1.8.0                               *
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Acreate2
 * Signature: (ILjava/lang/String;IIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate2
(JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id,
  jint space_id, jint create_plist, jint access_plist)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Acreate2:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Acreate2: aName is not pinned");
        return -1;
    }

  status = H5Acreate2((hid_t)loc_id, aName, (hid_t)type_id,
        (hid_t)space_id, (hid_t)create_plist, (hid_t)access_plist );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen
  (JNIEnv *env, jclass clss, jint obj_id, jstring name, jint access_plist)

{
   hid_t retVal;
   char* aName;
   jboolean isCopy;

   if (name == NULL) {
        h5nullArgument( env, "H5Aopen:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aopen: aName is not pinned");
        return -1;
    }

  retVal = H5Aopen((hid_t)obj_id, aName, (hid_t)access_plist);

  ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (retVal< 0) {
        h5libraryError(env);
    }
    return (jint)retVal;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen_by_idx
 * Signature: (ILjava/lang/String;IIIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1by_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint idx_type, jint order, jlong n, jint aapl_id, jint lapl_id)
{
  hid_t retVal;
  char* aName;
  jboolean isCopy;

  if (name == NULL) {
    h5nullArgument( env, "H5Aopen_by_idx:  name is NULL");
    return -1;
  }

  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

  if (aName == NULL) {
    h5JNIFatalError( env, "H5Aopen_by_idx: aName is not pinned");
    return -1;
  }

  retVal = H5Aopen_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (hid_t)aapl_id, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jint)retVal;

}

/*
* Class:     ncsa_hdf_hdf5lib_H5
* Method:    _H5Acreate_by_name
* Signature: (ILjava/lang/String;Ljava/lang/String;IIIII)I
*/
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate_1by_1name
(JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring attr_name, jint type_id, jint space_id, jint acpl_id, jint aapl_id, jint lapl_id)
{
  hid_t retVal;
  char *aName, *attrName;
  jboolean isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Acreate_by_name:  object name is NULL");
    return -1;
  }
  if (attr_name == NULL) {
    h5nullArgument( env, "H5Acreate_by_name:  attribute name is NULL");
    return -1;
  }
  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Acreate_by_name: aName is not pinned");
    return -1;
  }
  attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
  if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    h5JNIFatalError( env, "H5Acreate_by_name: attrName is not pinned");
    return -1;
  }

  retVal = H5Acreate_by_name((hid_t)loc_id, aName, attrName, (hid_t)type_id,
    (hid_t)space_id, (hid_t)acpl_id, (hid_t)aapl_id, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aexists_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aexists_1by_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring attr_name, jint lapl_id)
{
   htri_t retVal;
   char *aName, *attrName;
   jboolean isCopy;

  if (obj_name == NULL) {
        h5nullArgument( env, "H5Aexists_by_name:  object name is NULL");
        return -1;
  }
  if (attr_name == NULL) {
        h5nullArgument( env, "H5Aexists_by_name:  attribute name is NULL");
        return -1;
  }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aexists_by_name: aName is not pinned");
        return -1;
    }
    attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
        h5JNIFatalError( env, "H5Aexists_by_name: attrName is not pinned");
        return -1;
    }

  retVal = H5Aexists_by_name((hid_t)loc_id, aName, attrName, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

  if (retVal< 0) {
        h5libraryError(env);
    }
    return (jboolean)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Arename
 * Signature: (ILjava/lang/String;Ljava/lang/String)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Arename
  (JNIEnv *env, jclass clss, jint loc_id, jstring old_attr_name, jstring new_attr_name)
{
    herr_t retVal;
    char *oName, *nName;
    jboolean isCopy;

    if (old_attr_name == NULL) {
        h5nullArgument( env, "H5Arename:  old_attr_name is NULL");
        return -1;
    }
    if (new_attr_name == NULL) {
        h5nullArgument( env, "H5Arename:  new_attr_name is NULL");
        return -1;
    }

    oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR old_attr_name,&isCopy);
    if (oName == NULL) {
        h5JNIFatalError( env, "H5Arename:  old_attr_name not pinned");
        return -1;
    }
    nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_attr_name,&isCopy);
    if (nName == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
        h5JNIFatalError( env, "H5Arename:  new_attr_name not pinned");
        return -1;
    }

    retVal = H5Arename((hid_t)loc_id, oName, nName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR new_attr_name,nName);

    if (retVal< 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Arename_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Arename_1by_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring old_attr_name, jstring new_attr_name, jint lapl_id)
{
  herr_t retVal;
  char *aName, *oName, *nName;
  jboolean isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  object name is NULL");
    return -1;
  }
  if (old_attr_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  old_attr_name is NULL");
    return -1;
  }
  if (new_attr_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  new_attr_name is NULL");
    return -1;
  }

  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Arename_by_name: object name is not pinned");
    return -1;
  }
  oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR old_attr_name,&isCopy);
  if (oName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    h5JNIFatalError( env, "H5Arename_by_name:  old_attr_name not pinned");
    return -1;
  }
  nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_attr_name,&isCopy);
  if (nName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
    h5JNIFatalError( env, "H5Arename_by_name:  new_attr_name not pinned");
    return -1;
  }

  retVal = H5Arename_by_name((hid_t)loc_id, aName, oName, nName, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR new_attr_name,nName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_name_by_idx
 * Signature: (ILjava/lang/String;IIJI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1name_1by_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jint lapl_id)
{
  size_t   buf_size;
  char    *aName;
  char    *aValue;
  jboolean isCopy;
  jlong    status_size;
  jstring  str = NULL;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Aget_name_by_idx:  object name is NULL");
    return NULL;
  }
  aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Aget_name_by_idx:  name not pinned");
    return NULL;
  }

  /* get the length of the attribute name */
  status_size = H5Aget_name_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t) order, (hsize_t) n, (char*)NULL, (size_t)0, (hid_t)lapl_id);

  if(status_size < 0) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
    h5libraryError(env);
    return NULL;
  }
  buf_size = (size_t)status_size + 1;/* add extra space for the null terminator */

  aValue = (char*)malloc(sizeof(char) * buf_size);
  if (aValue == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
    h5outOfMemory( env, "H5Aget_name_by_idx:  malloc failed ");
    return NULL;
  }

  status_size = H5Aget_name_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t) order, (hsize_t) n, (char*)aValue, (size_t)buf_size, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

  if (status_size < 0) {
    free(aValue);
    h5libraryError(env);
    return NULL;
  }
  /* may throw OutOfMemoryError */
  str = ENVPTR->NewStringUTF(ENVPAR aValue);
  if (str == NULL) {
    /* exception -- fatal JNI error */
    free(aValue);
    h5JNIFatalError( env, "H5Aget_name_by_idx:  return string not created");
    return NULL;
  }

  free(aValue);

  return str;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_storage_size
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1storage_1size
  (JNIEnv *env, jclass clss, jint attr_id)
{
  hsize_t retVal = (hsize_t)-1;

    retVal = H5Aget_storage_size((hid_t)attr_id);
/* probably returns '0' if fails--don't do an exception
    if (retVal < 0) {
        h5libraryError(env);
    }
*/
    return (jlong)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info
 * Signature: (I)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info
  (JNIEnv *env, jclass clss, jint attr_id)
{
    herr_t     status;
    H5A_info_t ainfo;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    status = H5Aget_info((hid_t)attr_id, (H5A_info_t*)&ainfo);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
    constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info_by_idx
 * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info_1by_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jint lapl_id)
{

    char      *aName;
    herr_t     status;
    H5A_info_t ainfo;
    jboolean   isCopy;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    if (obj_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_idx: obj_name is NULL");
        return NULL;
    }

    aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aget_info_by_idx: object name not pinned");
        return NULL;
    }

  status = H5Aget_info_by_idx((hid_t)loc_id, (const char*)aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (H5A_info_t*)&ainfo, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
  constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info_1by_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring attr_name, jint lapl_id)
{
  char      *aName;
  char    *attrName;
    herr_t     status;
    H5A_info_t ainfo;
    jboolean   isCopy;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    if (obj_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_name: obj_name is NULL");
        return NULL;
    }
  if (attr_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_name: attr_name is NULL");
        return NULL;
    }
    aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aget_info_by_name: object name not pinned");
        return NULL;
    }
    attrName = (char*)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
        h5JNIFatalError( env, "H5Aget_info_by_name: Attribute name not pinned");
        return NULL;
    }
  status = H5Aget_info_by_name((hid_t)loc_id, (const char*)aName, (const char*)attrName,
    (H5A_info_t*)&ainfo,(hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name, attrName);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
  constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete_1by_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring attr_name, jint lapl_id)
{
   herr_t retVal;
   char *aName, *attrName;
   jboolean isCopy;

  if (obj_name == NULL) {
        h5nullArgument( env, "H5Adelete_by_name:  object name is NULL");
        return -1;
  }
  if (attr_name == NULL) {
        h5nullArgument( env, "H5Adelete_by_name:  attribute name is NULL");
        return -1;
  }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Adelete_by_name: aName is not pinned");
        return -1;
    }
    attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
        h5JNIFatalError( env, "H5Adelete_by_name: attrName is not pinned");
        return -1;
    }
    retVal = H5Adelete_by_name((hid_t)loc_id, (const char*)aName, (const char*)attrName, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

  if (retVal< 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aexists
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aexists
  (JNIEnv *env, jclass clss, jint obj_id, jstring attr_name)
{
  char    *aName;
  jboolean isCopy;
  htri_t   bval = 0;

  if (attr_name == NULL) {
    h5nullArgument( env, "H5Aexists: attr_name is NULL");
    return JNI_FALSE;
  }
  aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Aexists: attr_name not pinned");
    return JNI_FALSE;
  }

  bval = H5Aexists((hid_t)obj_id, (const char*)aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name, aName);

  if (bval > 0) {
    return JNI_TRUE;
  }
  else if (bval == 0) {
    return JNI_FALSE;
  }
  else {
    h5libraryError(env);
    return JNI_FALSE;
  }

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete_by_idx
 * Signature: (ILjava/lang/String;IIJI)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete_1by_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jint lapl_id)
{
  char      *aName;
  herr_t     status;
  jboolean   isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Adelete_by_idx: obj_name is NULL");
    return;
  }

  aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Adelete_by_idx: obj_name not pinned");
    return;
  }

  status = H5Adelete_by_idx((hid_t)loc_id, (const char*)aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (hid_t)lapl_id);
  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

  if (status < 0) {
    h5libraryError(env);
    return;
  }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1by_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring obj_name, jstring attr_name, jint aapl_id, jint lapl_id)

{
    hid_t status;
    char *aName, *oName;
    jboolean isCopy;

    if (obj_name == NULL) {
        h5nullArgument( env,"_H5Aopen_by_name:  obj_name is NULL");
        return -1;
    }
  if (attr_name == NULL) {
        h5nullArgument( env,"_H5Aopen_by_name:  attr_name is NULL");
        return -1;
    }

  oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name,&isCopy);
    if (oName == NULL) {
        h5JNIFatalError( env,"_H5Aopen_by_name: obj_name is not pinned");
        return -1;
    }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name,&isCopy);
    if (aName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,oName);
        h5JNIFatalError( env,"_H5Aopen_by_name: attr_name is not pinned");
        return -1;
    }

    status = H5Aopen_by_name((hid_t)loc_id, oName, aName, (hid_t)aapl_id, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,oName);
   ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;

}

#ifdef __cplusplus
}
#endif
