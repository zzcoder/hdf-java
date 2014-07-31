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
 *  Link Object API Functions of the HDF5 library.
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
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcopy
     * Signature: (JLjava/lang/String;JLjava/lang/String;JJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcopy
      (JNIEnv *env, jclass clss, jlong cur_loc_id, jstring cur_name, jlong dst_loc_id, jstring dst_name, jlong create_id, jlong access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument(env, "H5Lcopy:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError(env, "H5Lcopy:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument(env, "H5Lcopy:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError(env, "H5Lcopy:  dst_name not pinned");
            return;
        }

        status = H5Lcopy((hid_t)cur_loc_id, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_external
     * Signature: (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;JJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1external
    (JNIEnv *env, jclass clss, jstring file_name, jstring cur_name, jlong dst_loc_id, jstring dst_name, jlong create_id, jlong access_id)
  {
      char    *lFileName;
      char    *lCurName;
      char    *lDstName;
      jboolean isCopy;
      herr_t   status = -1;
      
      if (file_name == NULL) {
          h5nullArgument(env, "H5Lcreate_external:  file_name is NULL");
          return;
      }
      
      lFileName = (char*)ENVPTR->GetStringUTFChars(ENVPAR file_name, &isCopy);
      if (lFileName == NULL) {
          h5JNIFatalError(env, "H5Lcreate_external:  file_name not pinned");
          return;
      }
     
      if (cur_name == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          h5nullArgument(env, "H5Lcreate_external:  cur_name is NULL");
          return;
      }
      
      lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name,&isCopy);
      if (lCurName == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          h5JNIFatalError(env, "H5Lcreate_external:  cur_name not pinned");
          return;
      }
      
      if (dst_name == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
          h5nullArgument(env, "H5Lcreate_external:  dst_name is NULL");
          return;
      }
      
      lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
      if (lDstName == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
          h5JNIFatalError(env, "H5Lcreate_external:  dst_name not pinned");
          return;
      }

      status = H5Lcreate_external((const char*)lFileName, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

      ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
      ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
      ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
      
      if (status < 0) {
         h5libraryError(env);
         return;
      }
      
      return;
  }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_hard
     * Signature: (JLjava/lang/String;JLjava/lang/String;JJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1hard
      (JNIEnv *env, jclass clss, jlong cur_loc_id, jstring cur_name, jlong dst_loc_id, jstring dst_name, jlong create_id, jlong access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument(env, "H5Lcreate_hard:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError(env, "H5Lcreate_hard:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument(env, "H5Lcreate_hard:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError(env, "H5Lcreate_hard:  dst_name not pinned");
            return;
        }

        status = H5Lcreate_hard((hid_t)cur_loc_id, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_soft
     * Signature: (Ljava/lang/String;JLjava/lang/String;JJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1soft
      (JNIEnv *env, jclass clss, jstring cur_name, jlong dst_loc_id, jstring dst_name, jlong create_id, jlong access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument(env, "H5Lcreate_soft:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError(env, "H5Lcreate_soft:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument(env, "H5Lcreate_soft:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name,&isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError(env, "H5Lcreate_soft:  dst_name not pinned");
            return;
        }

        status = H5Lcreate_soft((const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Ldelete
     * Signature: (JLjava/lang/String;J)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ldelete
      (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong access_id)
    {
        char    *lName;
        jboolean isCopy;
        herr_t   status = -1;

        if (name == NULL) {
            h5nullArgument(env, "H5Ldelete:  name is NULL");
            return;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Ldelete:  name not pinned");
            return;
        }
        
        status = H5Ldelete((hid_t)loc_id, (const char*)lName, (hid_t)access_id);
        
        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        
        if (status < 0) {
            h5libraryError(env);
            return;
        }
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Ldelete_by_idx
     * Signature: (JLjava/lang/String;IIJJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ldelete_1by_1idx
    (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jint index_field, jint order, jlong link_n, jlong access_id)
    {
        char      *lName;
        herr_t     status;
        jboolean   isCopy;
        hsize_t    n = (hsize_t)link_n;

        if (name == NULL) {
            h5nullArgument(env, "H5Ldelete_by_idx:  name is NULL");
            return;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Ldelete_by_idx:  name not pinned");
            return;
        }

        status = H5Ldelete_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, n, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

        if (status < 0) {
           h5libraryError(env);
           return;
        }
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lexists
     * Signature: (JLjava/lang/String;J)Z
     */
    JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lexists
      (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong access_id)
    {
        char    *lName;
        jboolean isCopy;
        htri_t   bval = 0;

        if (name == NULL) {
            h5nullArgument(env, "H5Lexists:  name is NULL");
            return JNI_FALSE;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lexists:  name not pinned");
            return JNI_FALSE;
        }
        
        bval = H5Lexists((hid_t)loc_id, (const char*)lName, (hid_t)access_id);
        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        
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
     * Signature: (JLjava/lang/String;J)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info
    (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong access_id)
    {
        char      *lName;
        herr_t     status;
        H5L_info_t infobuf;
        jboolean   isCopy;
        jclass     cls;
        jmethodID  constructor;
        jvalue     args[5];
        jobject    ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument(env, "H5Lget_info:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lget_info:  name not pinned");
            return NULL;
        }

        status = H5Lget_info((hid_t)loc_id, (const char*)lName, (H5L_info_t*)&infobuf, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to your class if you don't have it already
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5L_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IZJIJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID failed\n");
           return NULL;
        }
        args[0].i = infobuf.type;
        args[1].z = infobuf.corder_valid;
        args[2].j = infobuf.corder;
        args[3].i = infobuf.cset;
        if(infobuf.type==0)
            args[4].j = infobuf.u.address;
        else
            args[4].j = infobuf.u.val_size;
        ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_info_by_idx
     * Signature: (JLjava/lang/String;IIJJ)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info_1by_1idx
    (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jint index_field, jint order, jlong link_n, jlong access_id)
    {
        char      *lName;
        herr_t     status;
        H5L_info_t infobuf;
        jboolean   isCopy;
        jclass     cls;
        jmethodID  constructor;
        jvalue     args[5];
        jobject    ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument(env, "H5Lget_info_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lget_info_by_idx:  name not pinned");
            return NULL;
        }

        status = H5Lget_info_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (H5L_info_t*)&infobuf, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to your class if you don't have it already
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5L_info_t");
        if (cls == 0) {
            h5JNIFatalError(env, "JNI error: GetObjectClass failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IZJIJ)V");
        if (constructor == 0) {
            h5JNIFatalError(env, "JNI error: GetMethodID failed\n");
           return NULL;
        }
        args[0].i = infobuf.type;
        args[1].z = infobuf.corder_valid;
        args[2].j = infobuf.corder;
        args[3].i = infobuf.cset;
        if(infobuf.type==0)
            args[4].j = infobuf.u.address;
        else
            args[4].j = infobuf.u.val_size;
        ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_name_by_idx
     * Signature: (JLjava/lang/String;IIJJ)Ljava/lang/String;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1name_1by_1idx
    (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jint index_field, jint order, jlong link_n, jlong access_id)
    {
        size_t   buf_size;
        char    *lName;
        char    *lValue;
        jboolean isCopy;
        jlong    status_size;
        jstring  str = NULL;

        if (name == NULL) {
            h5nullArgument(env, "H5Lget_name_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lget_name_by_idx:  name not pinned");
            return NULL;
        }

        /* get the length of the link name */
        status_size = H5Lget_name_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (char*)NULL, (size_t)0, (hid_t)H5P_DEFAULT);
        if(status_size < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = (size_t)status_size + 1;/* add extra space for the null terminator */
        
        lValue = (char*)malloc(sizeof(char) * buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory(env, "H5Lget_name_by_idx:  malloc failed ");
            return NULL;
        }

        status_size = H5Lget_name_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (char*)lValue, (size_t)buf_size, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

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
            h5JNIFatalError(env, "H5Lget_name_by_idx:  return string not created");
            return NULL;
        }

        free(lValue);

        return str;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_val
     * Signature: (JLjava/lang/String;[Ljava/lang/String;J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1val
      (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jobjectArray link_value, jlong access_id)
    {
        size_t      buf_size;
        herr_t      status;
        H5L_info_t  infobuf;
        char       *lName;
        char       *lValue;
        const char *file_name;
        const char *obj_name;
        jboolean    isCopy;
        jstring     str;
        
        if (name == NULL) {
            h5nullArgument(env, "H5Lget_val:  name is NULL");
            return -1;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lget_val:  name not pinned");
            return -1;
        }

        /* get the length of the link val */
        status = H5Lget_info((hid_t)loc_id, (const char*)lName, (H5L_info_t*)&infobuf, (hid_t)H5P_DEFAULT);
        if(status < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return -1;
        }
        buf_size = infobuf.u.val_size + 1;/* add extra space for the null terminator */
        
        if(infobuf.type == H5L_TYPE_HARD) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5JNIFatalError(env, "H5Lget_val:  link is hard type");
            return -1;
        }
        
        lValue = (char*)malloc(sizeof(char) * buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory(env, "H5Lget_val:  malloc failed");
            return -1;
        }

        status = H5Lget_val((hid_t)loc_id, (const char*)lName, (void*)lValue, (size_t)buf_size, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        if (status < 0) {
           free(lValue);
           h5libraryError(env);
           return -1;
        }
        /* may throw OutOfMemoryError */
        if(infobuf.type == H5L_TYPE_EXTERNAL) {
            status = H5Lunpack_elink_val((char*)lValue, (size_t)infobuf.u.val_size, (unsigned*)NULL, (const char**)&file_name, (const char**)&obj_name);
            if (status < 0) {
               free(lValue);
               h5libraryError(env);
               return -1;
            }
            
            str = ENVPTR->NewStringUTF(ENVPAR obj_name);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 0, str);
            
            str = ENVPTR->NewStringUTF(ENVPAR file_name);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 1, str);
        }
        else {
            str = ENVPTR->NewStringUTF(ENVPAR lValue);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 0, str);
        }

        free(lValue);

        return infobuf.type;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_val_by_idx
     * Signature: (JLjava/lang/String;IIJ[Ljava/lang/String;J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1val_1by_1idx
    (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jint index_field, jint order,
            jlong link_n, jobjectArray link_value, jlong access_id)
    {
        herr_t      status;
        size_t      buf_size;
        H5L_info_t  infobuf;
        char       *lName;
        void       *lValue;
        const char *file_name;
        const char *obj_name;
        jboolean    isCopy;
        jstring     str;

        if (name == NULL) {
            h5nullArgument(env, "H5Lget_val_by_idx:  name is NULL");
            return -1;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lget_val_by_idx:  name not pinned");
            return -1;
        }

        /* get the length of the link valuee */
        status = H5Lget_info_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (H5L_info_t*)&infobuf, (hid_t)access_id);
        if(status < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return -1;
        }
        buf_size = infobuf.u.val_size;
        if(buf_size < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return -1;
        }
        lValue = (void*)malloc(buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory(env, "H5Lget_val_by_idx:  malloc failed ");
            return -1;
        }

        status = H5Lget_val_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (void*)lValue, (size_t)buf_size, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        if (status < 0) {
           free(lValue);
           h5libraryError(env);
           return -1;
        }
        /* may throw OutOfMemoryError */
        if(infobuf.type == H5L_TYPE_EXTERNAL) {
            status = H5Lunpack_elink_val((char*)lValue, (size_t)infobuf.u.val_size, (unsigned*)NULL, (const char**)&file_name, (const char**)&obj_name);
            if (status < 0) {
               free(lValue);
               h5libraryError(env);
               return -1;
            }
            
            str = ENVPTR->NewStringUTF(ENVPAR obj_name);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val_by_idx:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 0, str);
            
            str = ENVPTR->NewStringUTF(ENVPAR file_name);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val_by_idx:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 1, str);
        }
        else {
            str = ENVPTR->NewStringUTF(ENVPAR (char *)lValue);
            if (str == NULL) {
                /* exception -- fatal JNI error */
                free(lValue);
                h5JNIFatalError(env, "H5Lget_val_by_idx:  return string not created");
                return -1;
            }
            ENVPTR->SetObjectArrayElement(ENVPAR link_value, 0, str);
        }

        free(lValue);

        return infobuf.type;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lmove
     * Signature: (JLjava/lang/String;JLjava/lang/String;JJ)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lmove
      (JNIEnv *env, jclass clss, jlong cur_loc_id, jstring cur_name, jlong dst_loc_id, jstring dst_name, jlong create_id, jlong access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument(env, "H5Lcreate_hard:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError(env, "H5Lcreate_hard:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument(env, "H5Lcreate_hard:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError( env, "H5Lcreate_hard:  dst_name not pinned");
            return;
        }

        status = H5Lmove((hid_t)cur_loc_id, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }

    herr_t H5L_iterate_cb(hid_t g_id, const char *name, const H5L_info_t *info, void *op_data) {
        JNIEnv    *cbenv;
        jint       status;
        jclass     cls;
        jmethodID  mid;
        jstring    str;
        jmethodID  constructor;
        jvalue     args[5];
        jobject    cb_info_t = NULL;

        if(JVMPTR->AttachCurrentThread(JVMPAR2 (void**)&cbenv, NULL) != 0) {
            /* printf("JNI H5L_iterate_cb error: AttachCurrentThread failed\n"); */
            JVMPTR->DetachCurrentThread(JVMPAR);
            return -1;
        }
        cls = CBENVPTR->GetObjectClass(CBENVPAR visit_callback);
        if (cls == 0) {
            /* printf("JNI H5L_iterate_cb error: GetObjectClass failed\n"); */
           JVMPTR->DetachCurrentThread(JVMPAR);
           return -1;
        }
        mid = CBENVPTR->GetMethodID(CBENVPAR cls, "callback", "(JLjava/lang/String;Lncsa/hdf/hdf5lib/structs/H5L_info_t;Lncsa/hdf/hdf5lib/callbacks/H5L_iterate_t;)I");
        if (mid == 0) {
            /* printf("JNI H5L_iterate_cb error: GetMethodID failed\n"); */
            JVMPTR->DetachCurrentThread(JVMPAR);
            return -1;
        }
        str = CBENVPTR->NewStringUTF(CBENVPAR name);

        // get a reference to your class if you don't have it already
        cls = CBENVPTR->FindClass(CBENVPAR "ncsa/hdf/hdf5lib/structs/H5L_info_t");
        if (cls == 0) {
            /* printf("JNI H5L_iterate_cb error: GetObjectClass info failed\n"); */
           JVMPTR->DetachCurrentThread(JVMPAR);
           return -1;
        }
        // get a reference to the constructor; the name is <init>
        constructor = CBENVPTR->GetMethodID(CBENVPAR cls, "<init>", "(IZJIJ)V");
        if (constructor == 0) {
            /* printf("JNI H5L_iterate_cb error: GetMethodID constructor failed\n"); */
            JVMPTR->DetachCurrentThread(JVMPAR);
            return -1;
        }
        args[0].i = info->type;
        args[1].z = info->corder_valid;
        args[2].j = info->corder;
        args[3].i = info->cset;
        if(info->type==0)
            args[4].j = info->u.address;
        else
            args[4].j = info->u.val_size;
        cb_info_t = CBENVPTR->NewObjectA(CBENVPAR cls, constructor, args);

        status = CBENVPTR->CallIntMethod(CBENVPAR visit_callback, mid, g_id, str, cb_info_t, op_data);

        JVMPTR->DetachCurrentThread(JVMPAR);
        return status;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lvisit
     * Signature: (JIILjava/lang/Object;Ljava/lang/Object;)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lvisit
      (JNIEnv *env, jclass clss, jlong grp_id, jint idx_type, jint order,
              jobject callback_op, jobject op_data)
    {
        herr_t        status = -1;
        
        ENVPTR->GetJavaVM(ENVPAR &jvm);
        visit_callback = callback_op;

        if (op_data == NULL) {
            h5nullArgument(env, "H5Lvisit:  op_data is NULL");
            return -1;
        }
        if (callback_op == NULL) {
            h5nullArgument(env, "H5Lvisit:  callback_op is NULL");
            return -1;
        }
        
        status = H5Lvisit((hid_t)grp_id, (H5_index_t)idx_type, (H5_iter_order_t)order, (H5L_iterate_t)H5L_iterate_cb, (void*)op_data);
        
        if (status < 0) {
           h5libraryError(env);
           return status;
        }
        
        return status;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lvisit_by_name
     * Signature: (JLjava/lang/String;IILjava/lang/Object;Ljava/lang/Object;J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lvisit_1by_1name
      (JNIEnv *env, jclass clss, jlong grp_id, jstring name, jint idx_type, jint order,
              jobject callback_op, jobject op_data, jlong access_id)
    {
        jboolean      isCopy;
        char         *lName;
        herr_t        status = -1;
        
        ENVPTR->GetJavaVM(ENVPAR &jvm);
        visit_callback = callback_op;
        
        if (name == NULL) {
            h5nullArgument(env, "H5Lvisit_by_name:  name is NULL");
            return -1;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Lvisit_by_name:  name not pinned");
            return -1;
        }

        if (op_data == NULL) {
            h5nullArgument(env, "H5Lvisit_by_name:  op_data is NULL");
            return -1;
        }
        if (callback_op == NULL) {
            h5nullArgument(env, "H5Lvisit_by_name:  callback_op is NULL");
            return -1;
        }
        
        status = H5Lvisit_by_name((hid_t)grp_id, (const char*)lName, (H5_index_t)idx_type, (H5_iter_order_t)order, (H5L_iterate_t)H5L_iterate_cb, (void*)op_data, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        
        if (status < 0) {
           h5libraryError(env);
           return status;
        }
        
        return status;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Literate
     * Signature: (JIIJLjava/lang/Object;Ljava/lang/Object;)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Literate
      (JNIEnv *env, jclass clss, jlong grp_id, jint idx_type, jint order,
              jlong idx, jobject callback_op, jobject op_data)
    {
        hsize_t       start_idx = (hsize_t)idx;
        herr_t        status = -1;
        
        ENVPTR->GetJavaVM(ENVPAR &jvm);
        visit_callback = callback_op;

        if (op_data == NULL) {
            h5nullArgument(env,  "H5Literate:  op_data is NULL");
            return -1;
        }
        if (callback_op == NULL) {
            h5nullArgument(env,  "H5Literate:  callback_op is NULL");
            return -1;
        }
        
        status = H5Literate((hid_t)grp_id, (H5_index_t)idx_type, (H5_iter_order_t)order, (hsize_t*)&start_idx, (H5L_iterate_t)H5L_iterate_cb, (void*)op_data);
        
        if (status < 0) {
           h5libraryError(env);
           return status;
        }
        
        return status;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Literate_by_name
     * Signature: (JLjava/lang/String;IIJLjava/lang/Object;Ljava/lang/Object;J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Literate_1by_1name
      (JNIEnv *env, jclass clss, jlong grp_id, jstring name, jint idx_type, jint order,
              jlong idx, jobject callback_op, jobject op_data, jlong access_id)
    {
        jboolean      isCopy;
        char         *lName;
        hsize_t       start_idx = (hsize_t)idx;
        herr_t        status = -1;
        
        ENVPTR->GetJavaVM(ENVPAR &jvm);
        visit_callback = callback_op;
        
        if (name == NULL) {
            h5nullArgument(env, "H5Literate_by_name:  name is NULL");
            return -1;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Literate_by_name:  name not pinned");
            return -1;
        }

        if (op_data == NULL) {
            h5nullArgument(env,  "H5Literate_by_name:  op_data is NULL");
            return -1;
        }
        if (callback_op == NULL) {
            h5nullArgument(env,  "H5Literate_by_name:  callback_op is NULL");
            return -1;
        }
        
        status = H5Literate_by_name((hid_t)grp_id, (const char*)lName, (H5_index_t)idx_type, (H5_iter_order_t)order, (hsize_t*)&start_idx, (H5L_iterate_t)H5L_iterate_cb, (void*)op_data, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        
        if (status < 0) {
           h5libraryError(env);
           return status;
        }
        
        return status;
    }


#ifdef __cplusplus
}
#endif
