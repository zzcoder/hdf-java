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
 *  Group Object API Functions of the HDF5 library.
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
#include "h5jni.h"
#include "h5gImp.h"
#include "h5util.h"

/* missing definitions from hdf5.h */
#ifndef FALSE
#define FALSE 0
#endif

#ifndef TRUE
#define TRUE (!FALSE)
#endif

#ifdef __cplusplus
    herr_t obj_info_all(hid_t g_id, const char *name, const H5L_info_t *linfo, void *op_data);
    herr_t obj_info_max(hid_t g_id, const char *name, const H5L_info_t *linfo, void *op_data);
    int H5Gget_obj_info_max(hid_t, char **, int *, int *, unsigned long *, int);
    int H5Gget_obj_info_full( hid_t loc_id, char **objname, int *otype, int *ltype, unsigned long *fno, unsigned long *objno, int indexType, int indexOrder);
#else
    static herr_t obj_info_all(hid_t g_id, const char *name, const H5L_info_t *linfo, void *op_data);
    static herr_t obj_info_max(hid_t g_id, const char *name, const H5L_info_t *linfo, void *op_data);
    static int H5Gget_obj_info_max(hid_t, char **, int *, int *, unsigned long *, int);
    static int H5Gget_obj_info_full( hid_t loc_id, char **objname, int *otype, int *ltype, unsigned long *fno, unsigned long *objno, int indexType, int indexOrder);
#endif

typedef struct info_all
{
    char **objname;
    int *otype;
    int *ltype;
    unsigned long *objno;
    unsigned long *fno;
    unsigned long idxnum;
    int count;
} info_all_t;

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gcreate
 * Signature: (ILjava/lang/String;J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gcreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jlong size_hint)
{
    hid_t status;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gcreate:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gcreate:  file name not pinned");
        return -1;
    }

    status = H5Gcreate2((hid_t)loc_id, gName, (hid_t)H5P_DEFAULT, (hid_t)H5P_DEFAULT, (hid_t)H5P_DEFAULT );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gopen
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gopen
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
    hid_t status;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gopen:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gopen:  file name not pinned");
        return -1;
    }

    status = H5Gopen2((hid_t)loc_id, gName, (hid_t)H5P_DEFAULT );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gclose
  (JNIEnv *env, jclass clss, jint group_id)
{
    herr_t retVal =  H5Gclose((hid_t)group_id) ;

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Glink
 * Signature: (IILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Glink
  (JNIEnv *env, jclass clss, jint loc_id, jint link_type, jstring
    current_name, jstring new_name)
{
    herr_t status;
    char *cName, *nName;
    jboolean isCopy;

    if (current_name == NULL) {
        h5nullArgument( env, "H5Glink:  current_name is NULL");
        return -1;
    }
    if (new_name == NULL) {
        h5nullArgument( env, "H5Glink:  new_name is NULL");
        return -1;
    }
    cName = (char *)ENVPTR->GetStringUTFChars(ENVPAR current_name,&isCopy);
    if (cName == NULL) {
        h5JNIFatalError( env, "H5Glink:  current_name not pinned");
        return -1;
    }
    nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_name,&isCopy);
    if (nName == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR current_name,cName);
        h5JNIFatalError( env, "H5Glink:  new_name not pinned");
        return -1;
    }

    status = H5Glink((hid_t)loc_id, (H5G_link_t)link_type, cName, nName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR new_name,nName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR current_name,cName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Glink
 * Signature: (ILjava/lang/String;IILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Glink2
  (JNIEnv *env, jclass clss,
    jint current_loc_id, jstring current_name, jint link_type,
    jint new_loc_id, jstring new_name)
{
    herr_t status;
    char *cName, *nName;
    jboolean isCopy;

    if (current_name == NULL) {
        h5nullArgument( env, "H5Glink2:  current_name is NULL");
        return -1;
    }
    if (new_name == NULL) {
        h5nullArgument( env, "H5Glink2:  new_name is NULL");
        return -1;
    }
    cName = (char *)ENVPTR->GetStringUTFChars(ENVPAR current_name,&isCopy);
    if (cName == NULL) {
        h5JNIFatalError( env, "H5Glink2:  current_name not pinned");
        return -1;
    }
    nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_name,&isCopy);
    if (nName == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR current_name,cName);
        h5JNIFatalError( env, "H5Glink2:  new_name not pinned");
        return -1;
    }

    status = H5Glink2((hid_t)current_loc_id, cName, (H5G_link_t)link_type, (hid_t)new_loc_id, nName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR new_name,nName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR current_name,cName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gunlink
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gunlink
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
    herr_t status;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gunlink:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gunlink:  name not pinned");
        return -1;
    }

    status = H5Gunlink((hid_t)loc_id, gName );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gmove
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gmove
  (JNIEnv *env, jclass clss, jint loc_id, jstring src, jstring dst)
{
    herr_t status;
    char *sName, *dName;
    jboolean isCopy;

    if (src == NULL) {
        h5nullArgument( env, "H5Gmove:  src is NULL");
        return -1;
    }
    if (dst == NULL) {
        h5nullArgument( env, "H5Gmove:  dst is NULL");
        return -1;
    }
    sName = (char *)ENVPTR->GetStringUTFChars(ENVPAR src,&isCopy);
    if (sName == NULL) {
        h5JNIFatalError( env, "H5Gmove:  src not pinned");
        return -1;
    }
    dName = (char *)ENVPTR->GetStringUTFChars(ENVPAR dst,&isCopy);
    if (dName == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR src,sName);
        h5JNIFatalError( env, "H5Gmove:  dst not pinned");
        return -1;
    }

    status = H5Gmove((hid_t)loc_id, sName, dName );

    ENVPTR->ReleaseStringUTFChars(ENVPAR dst,dName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR src,sName);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_objinfo
 * Signature: (ILjava/lang/String;Z[J[J[I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objinfo
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jboolean follow_link,
  jlongArray fileno, jlongArray objno, jintArray link_info, jlongArray mtime)
{
    char* gName;
    jboolean isCopy;
    herr_t retVal;
    jint *linkInfo;
    jlong *fileInfo, *objInfo, *timeInfo;
    hbool_t follow;
    H5G_stat_t h5gInfo;

    if (name == NULL) {
        h5nullArgument( env, "H5Gget_objinfo:  name is NULL");
        return -1;
    }
    if (follow_link == JNI_TRUE) {
        follow = TRUE;  /*  HDF5 'TRUE' */
    }
    else if (follow_link == JNI_FALSE) {
        follow = FALSE;  /*  HDF5 'FALSE' */
    }
    else {
        h5badArgument( env, "H5Gget_objinfo:  follow_link is invalid");
        return -1;
    }
    if (fileno == NULL) {
        h5nullArgument( env, "H5Gget_objinfo:  fileno is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR fileno) < 2) {
        h5badArgument( env, "H5Gget_objinfo:  fileno input array < 2");
        return -1;
    }
    if (objno == NULL) {
        h5nullArgument( env, "H5Gget_objinfo:  objno is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR objno) < 2) {
        h5badArgument( env, "H5Gget_objinfo:  objno input array < 2");
        return -1;
    }
    if (link_info == NULL) {
        h5nullArgument( env, "H5Gget_objinfo:  link_info is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR link_info) < 3) {
        h5badArgument( env, "H5Gget_objinfo:  link_info input array < 3");
        return -1;
    }
    if (mtime == NULL) {
        h5nullArgument( env, "H5Gget_objinfo:  mtime is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gget_object:  name not pinned");
        return -1;
    }
    fileInfo = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR fileno,&isCopy);
    if (fileInfo == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5JNIFatalError( env, "H5Gget_object:  fileno not pinned");
        return -1;
    }
    objInfo = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR objno,&isCopy);
    if (objInfo == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR fileno,fileInfo,JNI_ABORT);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5JNIFatalError( env, "H5Gget_object:  objno not pinned");
        return -1;
    }
    linkInfo = (jint *)ENVPTR->GetIntArrayElements(ENVPAR link_info,&isCopy);
    if (linkInfo == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR objno,objInfo,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR fileno,fileInfo,JNI_ABORT);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5JNIFatalError( env, "H5Gget_object:  link_info not pinned");
        return -1;
    }
    timeInfo = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR mtime,&isCopy);
    if (timeInfo == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR link_info,linkInfo,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR objno,objInfo,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR fileno,fileInfo,JNI_ABORT);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5JNIFatalError( env, "H5Gget_object:  mtime not pinned");
        return -1;
    }

    retVal = H5Gget_objinfo((hid_t)loc_id, gName, follow, &h5gInfo);

    if (retVal < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR mtime,timeInfo,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR objno,objInfo,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR fileno,fileInfo,JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR link_info,linkInfo,JNI_ABORT);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5libraryError(env);
        return -1;
    }
    else {
        fileInfo[0] = (jlong)h5gInfo.fileno[0];
        fileInfo[1] = (jlong)h5gInfo.fileno[1];
        objInfo[0] = (jlong)h5gInfo.objno[0];
        objInfo[1] = (jlong)h5gInfo.objno[1];
        timeInfo[0] = (jlong)h5gInfo.mtime;
        linkInfo[0] = (jint)h5gInfo.nlink;
        linkInfo[1] = (jint)h5gInfo.type;
        linkInfo[2] = (jint)h5gInfo.linklen;
        ENVPTR->ReleaseLongArrayElements(ENVPAR mtime,timeInfo,0);
        ENVPTR->ReleaseLongArrayElements(ENVPAR objno,objInfo,0);
        ENVPTR->ReleaseLongArrayElements(ENVPAR fileno,fileInfo,0);
        ENVPTR->ReleaseIntArrayElements(ENVPAR link_info,linkInfo,0);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_linkval
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1linkval
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint size,
          jobjectArray value)
{
    char* gName;
    jboolean isCopy;
    char *lValue;
    jstring str;
    herr_t status;

    if (size < 0) {
        h5badArgument( env, "H5Gget_linkval:  size < 0");
        return -1;
    }
    if (name == NULL) {
        h5nullArgument( env, "H5Gget_linkval:  name is NULL");
        return -1;
    }
    lValue = (char *) malloc(sizeof(char)*size);
    if (lValue == NULL) {
        h5outOfMemory( env, "H5Gget_linkval:  malloc failed ");
        return -1;
    }
    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (gName == NULL) {
        free(lValue);
        h5JNIFatalError( env, "H5Gget_linkval:  name not pinned");
        return -1;
    }

    status = H5Gget_linkval((hid_t)loc_id, gName, (size_t)size, lValue);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status >= 0)
    {
        /* may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR lValue);
        if (str == NULL) {
            /* exception -- fatal JNI error */
            free(lValue);
            h5JNIFatalError( env, "H5Gget_linkval:  return string not created");
            return -1;
        }
        /*  the SetObjectArrayElement may raise exceptions... */
        ENVPTR->SetObjectArrayElement(ENVPAR value,0,(jobject)str);
        free(lValue);
    }
    else {
        free(lValue);
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gset_comment
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gset_1comment
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jstring comment)
{
    herr_t status;
    char *gName, *gComment;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gset_comment:  name is NULL");
        return -1;
    }
    if (comment == NULL) {
        h5nullArgument( env, "H5Gset_comment:  comment is NULL");
        return -1;
    }
    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gset_comment:  name not pinned");
        return -1;
    }
    gComment = (char *)ENVPTR->GetStringUTFChars(ENVPAR comment,&isCopy);
    if (gComment == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
        h5JNIFatalError( env, "H5Gset_comment:  comment not pinned");
        return -1;
    }

    status = H5Gset_comment((hid_t)loc_id, gName, gComment);

    ENVPTR->ReleaseStringUTFChars(ENVPAR comment,gComment);
    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_comment
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1comment
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint bufsize,
  jobjectArray comment)
{
    char* gName;
    jboolean isCopy;
    char *gComment;
    jstring str;
    jint status;

    if (bufsize <= 0) {
        h5badArgument( env, "H5Gget_comment:  bufsize <= 0");
        return -1;
    }
    if (name == NULL) {
        h5nullArgument( env, "H5Gget_comment:  name is NULL");
        return -1;
    }
    if (comment == NULL) {
        h5nullArgument( env, "H5Gget_comment:  comment is NULL");
        return -1;
    }
    gComment = (char *)malloc(sizeof(char)*bufsize);
    if (gComment == NULL) {
        /* exception -- out of memory */
        h5outOfMemory( env, "H5Gget_comment:  malloc failed");
        return -1;
    }
    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (gName == NULL) {
        free(gComment);
        h5JNIFatalError( env, "H5Gget_comment:  name not pinned");
        return -1;
    }
    status = H5Gget_comment((hid_t)loc_id, gName, (size_t)bufsize, gComment);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status >= 0)
    {
        /*  may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR gComment);
        if (str == NULL) {
            free(gComment);
            h5JNIFatalError( env, "H5Gget_comment:  return string not allocated");
            return -1;
        }
        /*  The SetObjectArrayElement may raise exceptions */
        ENVPTR->SetObjectArrayElement(ENVPAR comment,0,(jobject)str);
        free(gComment);
    }
    else {
        free(gComment);
        h5libraryError(env);
    }

    return (jint)status;
}


/***************************************************************
 *                   New APIs for HDF5.1.8                    *
 ***************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_num_objs
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1num_1objs
  (JNIEnv *env, jclass clss, jint loc_id, jlongArray num_obj)
{
    int status;
    jlong *num_objP;
    jboolean isCopy;
    hsize_t *num_obja;
    int i;
    int rank;

    if (num_obj == NULL) {
        h5nullArgument( env, "H5Gget_num_objs:  num_obj is NULL");
        return -1;
    }

    num_objP = ENVPTR->GetLongArrayElements(ENVPAR num_obj,&isCopy);
    if (num_objP == NULL) {
        h5JNIFatalError(env,  "H5Gget_num_objs:  num_obj not pinned");
        return -1;
    }
    rank = (int) ENVPTR->GetArrayLength(ENVPAR num_obj);
    num_obja = (hsize_t *)malloc( rank * sizeof(hsize_t));
    if (num_obja == NULL)  {
        ENVPTR->ReleaseLongArrayElements(ENVPAR num_obj,num_objP,JNI_ABORT);
        h5JNIFatalError(env,  "H5Gget_num_objs:  num_obj not converted to hsize_t");
        return -1;
    }

    status = H5Gget_num_objs(loc_id, (hsize_t *)num_obja);

    if (status < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR num_obj,num_objP,JNI_ABORT);
        free(num_obja);
        h5libraryError(env);
        return -1;
    }
    for (i = 0; i < rank; i++) {
            num_objP[i] = num_obja[i];
        }
        ENVPTR->ReleaseLongArrayElements(ENVPAR num_obj,num_objP,0);

    free(num_obja);
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_objname_by_idx
 * Signature: (IJ[Ljava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objname_1by_1idx
  (JNIEnv *env, jclass clss, jint group_id, jlong idx,
          jobjectArray name, jlong buf_size)
{
    char *aName;
    jstring str;
    hssize_t size;
    long bs;

    bs = (long)buf_size;
    if (bs <= 0) {
        h5badArgument( env, "H5Gget_objname_by_idx:  buf_size <= 0");
        return -1;
    }
    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        h5outOfMemory(env, "H5Gget_objname_by_idx:  malloc failed");
        return -1;
    }
    size = H5Gget_objname_by_idx((hid_t)group_id, (hsize_t)idx, aName, (size_t)buf_size);
    if (size < 0) {
        free(aName);
        h5libraryError(env);
        /*  exception, returns immediately */
        return -1;
    }
    /* successful return -- save the string; */
    str = ENVPTR->NewStringUTF(ENVPAR aName);
    if (str == NULL) {
        free(aName);
        h5JNIFatalError( env,"H5Gget_objname_by_idx:  return string failed");
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
 * Method:    H5Gget_objtype_by_idx
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objtype_1by_1idx
  (JNIEnv *env, jclass clss, jint group_id, jlong idx)
{
    int type;

    type = H5Gget_objtype_by_idx((hid_t)group_id, (hsize_t)idx );
    if (type < 0) {
        h5libraryError(env);
        /*  exception, returns immediately */
        return -1;
    }

    return (jint)type;
}

/*
/////////////////////////////////////////////////////////////////////////////////
//
//
// Add these methods so that we don't need to call H5Gget_objtype_by_idx
// in a loop to get information for all the object in a group, which takes
// a lot of time to finish if the number of objects is more than 10,000
//
/////////////////////////////////////////////////////////////////////////////////
*/
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_obj_info_full
 * Signature: (ILjava/lang/String;[Ljava/lang/String;[I[I[J[JIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1obj_1info_1full
  (JNIEnv *env, jclass clss, jint loc_id, jstring group_name,
  jobjectArray objName, jintArray oType, jintArray lType, jlongArray fNo,
  jlongArray oRef, jint n, jint indx_type, jint indx_order)
{
    herr_t ret_val = -1;
    char *gName=NULL;
    char **oName=NULL;
    jboolean isCopy;
    jstring str;
    jint *otarr;
    jint *ltarr;
    jlong *refP;
    jlong *fnoP;
    unsigned long *refs=NULL;
    unsigned long *fnos=NULL;
    int i;
    int gid = loc_id;
    int indexType = indx_type;
    int indexOrder = indx_order;

    if (group_name != NULL) {
        gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR group_name,&isCopy);
        if (gName == NULL) {
            h5JNIFatalError( env, "H5Gget_obj_info_full:  name not pinned");
            return -1;
        }
        gid = H5Gopen2(loc_id, gName, H5P_DEFAULT);

        ENVPTR->ReleaseStringUTFChars(ENVPAR group_name,gName);

        if(gid < 0) {
            h5JNIFatalError( env, "H5Gget_obj_info_full: could not get group identifier");
            return -1;
        }
    }

    if (oType == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_full:  oType is NULL");
        return -1;
    }

    if (lType == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_full:  lType is NULL");
        return -1;
    }

    if (oRef == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_full:  oRef is NULL");
        return -1;
    }

    otarr = ENVPTR->GetIntArrayElements(ENVPAR oType,&isCopy);
    if (otarr == NULL) {
        h5JNIFatalError( env, "H5Gget_obj_info_full:  otype not pinned");
        return -1;
    }

    ltarr = ENVPTR->GetIntArrayElements(ENVPAR lType,&isCopy);
    if (ltarr == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
        h5JNIFatalError( env, "H5Gget_obj_info_full:  ltype not pinned");
        return -1;
    }

    refP = ENVPTR->GetLongArrayElements(ENVPAR oRef,&isCopy);
    fnoP = ENVPTR->GetLongArrayElements(ENVPAR fNo,&isCopy);
    if (refP == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
        h5JNIFatalError( env, "H5Gget_obj_info_full:  type not pinned");
        return -1;
    }

    oName = (char **)calloc(n, sizeof (*oName));
    if (!oName)
      goto error;

    refs = (unsigned long *)calloc(n, sizeof (unsigned long));
    fnos = (unsigned long *)calloc(n, sizeof (unsigned long));
    if (!refs || !fnos)
      goto error;

    ret_val = H5Gget_obj_info_full( (hid_t) gid, oName, (int *)otarr, (int *)ltarr, fnos, refs, indexType, indexOrder);

    if (ret_val < 0)
        goto error;

    if (refs) {
        for (i=0; i<n; i++) {
            refP[i] = (jlong) refs[i];
        }
    }

    if (fnos) {
        for (i=0; i<n; i++) {
            fnoP[i] = (jlong) fnos[i];
        }
    }

    if (oName) {
        for (i=0; i<n; i++) {
            if (*(oName+i)) {
                str = ENVPTR->NewStringUTF(ENVPAR *(oName+i));
                ENVPTR->SetObjectArrayElement(ENVPAR objName,i,(jobject)str);
            }
        } /* for (i=0; i<n; i++)*/
    }

    if (group_name != NULL) H5Gclose(gid);
  ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,0);
  ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,0);
  ENVPTR->ReleaseLongArrayElements(ENVPAR oRef,refP,0);
  ENVPTR->ReleaseLongArrayElements(ENVPAR fNo,fnoP,0);
  if (oName) h5str_array_free(oName, n);
  if (refs) free(refs);
  if (fnos) free(fnos);

   return ret_val;

error:
  if (group_name != NULL) H5Gclose(gid);
  ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,JNI_ABORT);
  ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
  ENVPTR->ReleaseLongArrayElements(ENVPAR oRef,refP,JNI_ABORT);
  ENVPTR->ReleaseLongArrayElements(ENVPAR fNo,fnoP,JNI_ABORT);
  if (oName) h5str_array_free(oName, n);
  if (refs) free(refs);
  if (fnos) free(fnos);
  h5libraryError(env);

  return -1;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_obj_info_max
 * Signature: (I[Ljava/lang/String;[I[I[JII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1obj_1info_1max
  (JNIEnv *env, jclass clss, jint loc_id, jobjectArray objName,
          jintArray oType, jintArray lType, jlongArray oRef,
          int maxnum, int n)
{
    herr_t ret_val = -1;
    char **oName=NULL;
    jboolean isCopy;
    jstring str;
    jint *otarr;
    jint *ltarr;
    jlong *refP;
    unsigned long *refs;
    int i;

    if (oType == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_max:  oType is NULL");
        return -1;
    }

    if (lType == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_max:  lType is NULL");
        return -1;
    }

    if (oRef == NULL) {
        h5nullArgument( env, "H5Gget_obj_info_all:  oRef is NULL");
        return -1;
    }

    otarr = ENVPTR->GetIntArrayElements(ENVPAR oType,&isCopy);
    if (otarr == NULL) {
        h5JNIFatalError( env, "H5Gget_obj_info_max:  otype not pinned");
        return -1;
    }

    ltarr = ENVPTR->GetIntArrayElements(ENVPAR lType,&isCopy);
    if (ltarr == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
        h5JNIFatalError( env, "H5Gget_obj_info_max:  ltype not pinned");
        return -1;
    }

    refP = ENVPTR->GetLongArrayElements(ENVPAR oRef,&isCopy);
    if (refP == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,JNI_ABORT);
        h5JNIFatalError( env, "H5Gget_obj_info_all:  type not pinned");
        return -1;
    }

    oName = (char **)calloc(n, sizeof (*oName));
    refs = (unsigned long *)calloc(n, sizeof (unsigned long));

    ret_val = H5Gget_obj_info_max( (hid_t) loc_id, oName, (int *)otarr, (int *)ltarr, refs, maxnum );

    if (ret_val < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR oRef,refP,JNI_ABORT);
        h5str_array_free(oName, n);
        free(refs);
        h5libraryError(env);
        return -1;
    }

    ENVPTR->ReleaseIntArrayElements(ENVPAR lType,ltarr,0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR oType,otarr,0);

    if (refs) {
        for (i=0; i<n; i++) {
            refP[i] = (jlong) refs[i];
        }
    }
    free(refs);
    ENVPTR->ReleaseLongArrayElements(ENVPAR oRef,refP,0);

    if (oName) {
        for (i=0; i<n; i++) {
            if (*(oName+i)) {
                str = ENVPTR->NewStringUTF(ENVPAR *(oName+i));
                ENVPTR->SetObjectArrayElement(ENVPAR objName,i,(jobject)str);
            }
        } /* for (i=0; i<n; i++)*/
    }

    h5str_array_free(oName, n);

    return ret_val;
}

int H5Gget_obj_info_full( hid_t loc_id, char **objname, int *otype, int *ltype, unsigned long *fno, unsigned long *objno, int indexType, int indexOrder)
{
    info_all_t info;
    info.objname = objname;
    info.otype = otype;
    info.ltype = ltype;
    info.idxnum = 0;
    info.fno = fno;
    info.objno = objno;
    info.count = 0;

    if(H5Literate(loc_id, (H5_index_t)indexType, (H5_iter_order_t)indexOrder, NULL, obj_info_all, (void *)&info) < 0){

        /* iterate failed, try normal alphabetical order */
        if(H5Literate(loc_id, H5_INDEX_NAME, H5_ITER_INC, NULL, obj_info_all, (void *)&info) < 0)
            return -1;
    }

    return info.count;
}

int H5Gget_obj_info_max( hid_t loc_id, char **objname, int *otype, int *ltype, unsigned long *objno, int maxnum )
{
    info_all_t info;
    info.objname = objname;
    info.otype = otype;
    info.ltype = ltype;
    info.idxnum = maxnum;
    info.objno = objno;
    info.count = 0;

    if(H5Lvisit(loc_id, H5_INDEX_NAME, H5_ITER_NATIVE, obj_info_max, (void *)&info) < 0)
        return -1;

    return info.count;
}

herr_t obj_info_all(hid_t loc_id, const char *name, const H5L_info_t *info, void *op_data)
{
    int type = -1;
    hid_t oid=-1;
    herr_t retVal = -1;
    info_all_t* datainfo = (info_all_t*)op_data;
    H5O_info_t object_info;

    retVal = H5Oget_info_by_name(loc_id, name, &object_info, H5P_DEFAULT);

    if ( retVal < 0) {
        *(datainfo->otype+datainfo->count) = -1;
        *(datainfo->ltype+datainfo->count) = -1;
        *(datainfo->objname+datainfo->count) = (char *) malloc(strlen(name)+1);
        strcpy(*(datainfo->objname+datainfo->count), name);
        *(datainfo->objno+datainfo->count) = -1;
    }
    else {
        *(datainfo->otype+datainfo->count) = object_info.type;
        *(datainfo->ltype+datainfo->count) = info->type;
        *(datainfo->objname+datainfo->count) = (char *) malloc(strlen(name)+1);
        strcpy(*(datainfo->objname+datainfo->count), name);

		*(datainfo->fno+datainfo->count) = object_info.fileno;
		*(datainfo->objno+datainfo->count) = (unsigned long)object_info.addr;
		/*
        if(info->type==H5L_TYPE_HARD)
            *(datainfo->objno+datainfo->count) = (unsigned long)info->u.address;
        else
            *(datainfo->objno+datainfo->count) = info->u.val_size;
        */
    }

    datainfo->count++;

    return 0;
}

herr_t obj_info_max(hid_t loc_id, const char *name, const H5L_info_t *info, void *op_data)
{
    int type = -1;
    herr_t retVal = 0;
    info_all_t* datainfo = (info_all_t*)op_data;
    H5O_info_t object_info;

    retVal = H5Oget_info(loc_id, &object_info);
    if ( retVal < 0) {
        *(datainfo->otype+datainfo->count) = -1;
        *(datainfo->ltype+datainfo->count) = -1;
        *(datainfo->objname+datainfo->count) = NULL;
        *(datainfo->objno+datainfo->count) = -1;
        return 1;
    }
    else {
        *(datainfo->otype+datainfo->count) = object_info.type;
        *(datainfo->ltype+datainfo->count) = info->type;
        /* this will be freed by h5str_array_free(oName, n)*/
        *(datainfo->objname+datainfo->count) = (char *) malloc(strlen(name)+1);
        strcpy(*(datainfo->objname+datainfo->count), name);
    if(info->type==H5L_TYPE_HARD)
            *(datainfo->objno+datainfo->count) = (unsigned long)info->u.address;
        else
            *(datainfo->objno+datainfo->count) = info->u.val_size;
    }
    datainfo->count++;
    if(datainfo->count < (int)datainfo->idxnum)
        return 0;
    else
        return 1;
}

/*
 * Create a java object of hdf.h5.structs.H5G_info_t
 * public class H5G_info_t {
 *   public H5G_STORAGE_TYPE  storage_type; // Type of storage for links in group
 *   public long     nlinks;       // Number of links in group
 *   public long     max_corder;   // Current max. creation order value for group
 *   public int      mounted;      // Whether group has a file mounted on it
 * }
 *
 */
jobject create_H5G_info_t(JNIEnv *env, H5G_info_t group_info)
{
    jclass cls;
    jboolean jmounted;
    jint storage_type;
    jobject obj;
    jfieldID fid_storage_type, fid_nlinks, fid_max_corder, fid_mounted;

    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5G_info_t");
    if (cls == NULL) return NULL;

    obj = ENVPTR->AllocObject(ENVPAR cls);
    if (obj == NULL) return NULL;

    fid_storage_type = ENVPTR->GetFieldID(ENVPAR cls, "storage_type", "I");
    fid_nlinks = ENVPTR->GetFieldID(ENVPAR cls, "nlinks", "J");
    fid_max_corder = ENVPTR->GetFieldID(ENVPAR cls, "max_corder", "J");
    fid_mounted = ENVPTR->GetFieldID(ENVPAR cls, "mounted", "Z");

    if (fid_storage_type==NULL || fid_nlinks==NULL || fid_max_corder==NULL ||
            fid_mounted == NULL)
        return NULL;

    jmounted = (group_info.mounted==0) ? JNI_FALSE : JNI_TRUE;
    storage_type = (jint)group_info.storage_type;

    ENVPTR->SetIntField(ENVPAR obj, fid_storage_type, (jint)storage_type);
    ENVPTR->SetLongField(ENVPAR obj, fid_nlinks, (jlong)group_info.nlinks);
    ENVPTR->SetLongField(ENVPAR obj, fid_max_corder, (jlong)group_info.max_corder);
    ENVPTR->SetBooleanField(ENVPAR obj, fid_mounted, jmounted);

    return obj;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gcreate2
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gcreate2
  (JNIEnv *env, jclass clss, jint loc_id, jstring name,
          jint link_plist_id, jint create_plist_id, jint access_plist_id)
{
    hid_t status;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gcreate:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gcreate:  file name not pinned");
        return -1;
    }

    status = H5Gcreate2((hid_t)loc_id, gName, link_plist_id, create_plist_id, access_plist_id );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gcreate_anon
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gcreate_1anon
  (JNIEnv *env, jclass cls, jint loc_id, jint gcpl_id, jint gapl_id)
{
    hid_t ret_val;

    ret_val = H5Gcreate_anon((hid_t)loc_id, (hid_t)gcpl_id, (hid_t)gapl_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }
    return (jint)ret_val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Gopen2
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Gopen2
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_plist_id)
{
    hid_t status;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gopen:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gopen:  file name not pinned");
        return -1;
    }

    status = H5Gopen2((hid_t)loc_id, gName, (hid_t)access_plist_id );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1create_1plist
(JNIEnv *env, jclass cls, jint loc_id)
{
  hid_t ret_val;

  ret_val = H5Gget_create_plist((hid_t)loc_id);

  if (ret_val < 0) {
      h5libraryError(env);
  }

  return (jint)ret_val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_info
 * Signature: (I)Lncsa/hdf/hdf5lib/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1info
  (JNIEnv *env, jclass cls, jint loc_id)
{
    H5G_info_t group_info;
    herr_t ret_val = -1;

    ret_val = H5Gget_info( (hid_t) loc_id, &group_info);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

    return create_H5G_info_t(env, group_info);
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_info_by_name
 * Signature: (ILjava/lang/String;I)Lncsa/hdf/hdf5lib/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1info_1by_1name
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint lapl_id)
{
    H5G_info_t group_info;
    herr_t ret_val = -1;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gget_info_by_name:  name is NULL");
        return NULL;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gget_info_by_name:  file name not pinned");
        return NULL;
    }

    ret_val = H5Gget_info_by_name((hid_t)loc_id, gName, &group_info, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

    return create_H5G_info_t(env, group_info);
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_info_by_idx
 * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1info_1by_1idx
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint index_type,
          jint order, jlong n, jint lapl_id)
{
    H5G_info_t group_info;
    herr_t ret_val = -1;
    char* gName;
    jboolean isCopy;
    H5_index_t cindex_type = (H5_index_t)index_type;
    H5_iter_order_t corder = (H5_iter_order_t)order;

    if (name == NULL) {
        h5nullArgument( env, "H5Gget_info_by_idx:  name is NULL");
        return NULL;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gget_info_by_idx:  file name not pinned");
        return NULL;
    }

    ret_val = H5Gget_info_by_idx((hid_t)loc_id, gName, cindex_type,
            corder, (hsize_t)n, &group_info, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

    return create_H5G_info_t(env, group_info);
}


#ifdef __cplusplus
}
#endif
