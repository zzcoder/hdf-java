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
#include <stdlib.h>
#include "hdf5.h"
#include "h5jni.h"
#include "h5lImp.h"

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR 
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_hard
     * Signature: (ILjava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1hard
      (JNIEnv *env, jclass clss, jint cur_loc_id, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
    {
        herr_t status = -1;
        char* lCurName;
        char* lDstName;
        jboolean isCopy;
        
        if (cur_name == NULL) {
            h5nullArgument( env, "H5Lcreate_hard:  cur_name is NULL");
            return;
        }
        
        lCurName = (char *)ENVPTR->GetStringUTFChars(ENVPAR cur_name,&isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError( env, "H5Lcreate_hard:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            h5nullArgument( env, "H5Lcreate_hard:  dst_name is NULL");
            return;
        }
        
        lDstName = (char *)ENVPTR->GetStringUTFChars(ENVPAR dst_name,&isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name,lCurName);
            h5JNIFatalError( env, "H5Lcreate_hard:  dst_name not pinned");
            return;
        }

        status = H5Lcreate_hard(cur_loc_id, lCurName, dst_loc_id, lDstName, create_id, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name,lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name,lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_soft
     * Signature: (Ljava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1soft
      (JNIEnv *env, jclass clss, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
    {
        herr_t status = -1;
        char* lCurName;
        char* lDstName;
        jboolean isCopy;
        
        if (cur_name == NULL) {
            h5nullArgument( env, "H5Lcreate_soft:  cur_name is NULL");
            return;
        }
        
        lCurName = (char *)ENVPTR->GetStringUTFChars(ENVPAR cur_name,&isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError( env, "H5Lcreate_soft:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            h5nullArgument( env, "H5Lcreate_soft:  dst_name is NULL");
            return;
        }
        
        lDstName = (char *)ENVPTR->GetStringUTFChars(ENVPAR dst_name,&isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name,lCurName);
            h5JNIFatalError( env, "H5Lcreate_soft:  dst_name not pinned");
            return;
        }

        status = H5Lcreate_soft(lCurName, dst_loc_id, lDstName, create_id, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name,lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name,lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Ldelete
     * Signature: (ILjava/lang/String;I)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ldelete
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        herr_t status = -1;

        if (name == NULL) {
            h5nullArgument( env, "H5Ldelete:  name is NULL");
            return;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Ldelete:  name not pinned");
            return;
        }
        
        status = H5Ldelete(loc_id, lName, access_id);
        
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
        
        if (status < 0) {
            h5libraryError(env);
            return;
        }
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lexists
     * Signature: (ILjava/lang/String;I)Z
     */
    JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lexists
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        htri_t bval = 0;

        if (name == NULL) {
            h5nullArgument( env, "H5Lexists:  name is NULL");
            return JNI_FALSE;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lexists:  name not pinned");
            return JNI_FALSE;
        }
        
        bval = H5Lexists(loc_id, lName, access_id);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
        
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
     * Method:    H5Lget_info
     * Signature: (ILjava/lang/String;I)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        herr_t status;
        H5L_info_t infobuf;
        jclass cls;
        jmethodID constructor;
        jvalue args[5];
        jobject ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_info:  name is NULL");
            return NULL;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_info:  name not pinned");
            return NULL;
        }

        status = H5Lget_info(loc_id, lName, &infobuf, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to your class if you don't have it already
        cls = (*env)->FindClass(env, "ncsa/hdf/hdf5lib/structs/H5L_info_t");
        // get a reference to the constructor; the name is <init>
        constructor = (*env)->GetMethodID(env, cls, "<init>", "(IZJIJ)V");
        args[0].i = infobuf.type;
        args[1].z = infobuf.corder_valid;
        args[2].j = infobuf.corder;
        args[3].i = infobuf.cset;
        if(infobuf.type==0)
            args[4].j = infobuf.u.address;
        else
            args[4].j = infobuf.u.val_size;
        ret_info_t = (*env)->NewObjectA(env, cls, constructor, args);
        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_info_by_idx
     * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info_1by_1idx
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint index_field, jint order, jlong link_n, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        herr_t status;
        H5L_info_t infobuf;
        jclass cls;
        jmethodID constructor;
        jvalue args[5];
        jobject ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_info_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_info_by_idx:  name not pinned");
            return NULL;
        }

        status = H5Lget_info_by_idx(loc_id, lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, &infobuf, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to your class if you don't have it already
        cls = (*env)->FindClass(env, "ncsa/hdf/hdf5lib/structs/H5L_info_t");
        // get a reference to the constructor; the name is <init>
        constructor = (*env)->GetMethodID(env, cls, "<init>", "(IZJIJ)V");
        args[0].i = infobuf.type;
        args[1].z = infobuf.corder_valid;
        args[2].j = infobuf.corder;
        args[3].i = infobuf.cset;
        if(infobuf.type==0)
            args[4].j = infobuf.u.address;
        else
            args[4].j = infobuf.u.val_size;
        ret_info_t = (*env)->NewObjectA(env, cls, constructor, args);
        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_name_by_idx
     * Signature: (ILjava/lang/String;IIJI)Ljava/lang/String;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1name_1by_1idx
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint index_field, jint order, jlong link_n, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        jlong status_size;
        size_t buf_size;
        char *lValue;
        jstring str = NULL;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_name_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_name_by_idx:  name not pinned");
            return NULL;
        }

        /* get the length of the link name */
        status_size = H5Lget_name_by_idx(loc_id, lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, NULL, 0, H5P_DEFAULT);
        if(status_size < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = status_size + 1;/* add extra space for the null terminator */
        
        lValue = (char *) malloc(sizeof(char)*buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5outOfMemory( env, "H5Lget_name_by_idx:  malloc failed ");
            return NULL;
        }

        status_size = H5Lget_name_by_idx(loc_id, lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, lValue, buf_size, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);

        if (status_size < 0) {
            free(lValue);
            h5libraryError(env);
            return NULL;
        }
        /* may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR lValue);
        if (str == NULL) {
            /* exception -- fatal JNI error */
            free(lValue);
            h5JNIFatalError( env, "H5Lget_name_by_idx:  return string not created");
            return NULL;
        }

        free(lValue);

        return str;
    }
   
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_val
     * Signature: (ILjava/lang/String;I)Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1val
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        char *lValue;
        size_t buf_size;
        jstring str;
        herr_t status;
        H5L_info_t infobuf;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_val:  name is NULL");
            return NULL;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_val:  name not pinned");
            return NULL;
        }

        /* get the length of the link val */
        status = H5Lget_info(loc_id, lName, &infobuf, H5P_DEFAULT);
        if(status < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = infobuf.u.val_size + 1;/* add extra space for the null terminator */
        
        if(infobuf.type != H5L_TYPE_SOFT) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5JNIFatalError( env, "H5Lget_val:  link not symbolic");
            return NULL;
        }
        
        lValue = (char *) malloc(sizeof(char)*buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5outOfMemory( env, "H5Lget_val:  malloc failed ");
            return NULL;
        }

        status = H5Lget_val((hid_t)loc_id, lName, lValue, (size_t)buf_size, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
        if (status < 0) {
           free(lValue);
           h5libraryError(env);
           return NULL;
        }
        /* may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR lValue);
        if (str == NULL) {
            /* exception -- fatal JNI error */
            free(lValue);
            h5JNIFatalError( env, "H5Lget_val:  return string not created");
            return NULL;
        }

        free(lValue);

        return str;
    }


#ifdef __cplusplus
}
#endif
