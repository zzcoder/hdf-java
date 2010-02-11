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
#define ENVPTR (env)
#define ENVPAR 
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lcreate_external
     * Signature: (Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1external
    (JNIEnv *env, jclass clss, jstring file_name, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
  {
      char    *lFileName;
      char    *lCurName;
      char    *lDstName;
      jboolean isCopy;
      herr_t   status = -1;
      
      if (file_name == NULL) {
          h5nullArgument( env, "H5Lcreate_external:  file_name is NULL");
          return;
      }
      
      lFileName = (char*)ENVPTR->GetStringUTFChars(ENVPAR file_name, &isCopy);
      if (lFileName == NULL) {
          h5JNIFatalError( env, "H5Lcreate_external:  file_name not pinned");
          return;
      }
     
      if (cur_name == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          h5nullArgument( env, "H5Lcreate_external:  cur_name is NULL");
          return;
      }
      
      lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name,&isCopy);
      if (lCurName == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          h5JNIFatalError( env, "H5Lcreate_external:  cur_name not pinned");
          return;
      }
      
      if (dst_name == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
          h5nullArgument( env, "H5Lcreate_external:  dst_name is NULL");
          return;
      }
      
      lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
      if (lDstName == NULL) {
          ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, lFileName);
          ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
          h5JNIFatalError( env, "H5Lcreate_external:  dst_name not pinned");
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
     * Signature: (ILjava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1hard
      (JNIEnv *env, jclass clss, jint cur_loc_id, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument( env, "H5Lcreate_hard:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError( env, "H5Lcreate_hard:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument( env, "H5Lcreate_hard:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError( env, "H5Lcreate_hard:  dst_name not pinned");
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
     * Signature: (Ljava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lcreate_1soft
      (JNIEnv *env, jclass clss, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument( env, "H5Lcreate_soft:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError( env, "H5Lcreate_soft:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument( env, "H5Lcreate_soft:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name,&isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError( env, "H5Lcreate_soft:  dst_name not pinned");
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
     * Signature: (ILjava/lang/String;I)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ldelete
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char    *lName;
        jboolean isCopy;
        herr_t   status = -1;

        if (name == NULL) {
            h5nullArgument( env, "H5Ldelete:  name is NULL");
            return;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Ldelete:  name not pinned");
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
     * Method:    H5Lexists
     * Signature: (ILjava/lang/String;I)Z
     */
    JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lexists
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char    *lName;
        jboolean isCopy;
        htri_t   bval = 0;

        if (name == NULL) {
            h5nullArgument( env, "H5Lexists:  name is NULL");
            return JNI_FALSE;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lexists:  name not pinned");
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
     * Signature: (ILjava/lang/String;I)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
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
            h5nullArgument( env, "H5Lget_info:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_info:  name not pinned");
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
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IZJIJ)V");
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
     * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5L_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1info_1by_1idx
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint index_field, jint order, jlong link_n, jint access_id)
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
            h5nullArgument( env, "H5Lget_info_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_info_by_idx:  name not pinned");
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
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IZJIJ)V");
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
     * Signature: (ILjava/lang/String;IIJI)Ljava/lang/String;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1name_1by_1idx
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint index_field, jint order, jlong link_n, jint access_id)
    {
        size_t   buf_size;
        char    *lName;
        char    *lValue;
        jboolean isCopy;
        jlong    status_size;
        jstring  str = NULL;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_name_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_name_by_idx:  name not pinned");
            return NULL;
        }

        /* get the length of the link name */
        status_size = H5Lget_name_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (char*)NULL, (size_t)0, (hid_t)H5P_DEFAULT);
        if(status_size < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = status_size + 1;/* add extra space for the null terminator */
        
        lValue = (char*)malloc(sizeof(char) * buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory( env, "H5Lget_name_by_idx:  malloc failed ");
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
            h5nullArgument( env, "H5Lget_val:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_val:  name not pinned");
            return NULL;
        }

        /* get the length of the link val */
        status = H5Lget_info((hid_t)loc_id, (const char*)lName, (H5L_info_t*)&infobuf, (hid_t)H5P_DEFAULT);
        if(status < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = infobuf.u.val_size + 1;/* add extra space for the null terminator */
        
        if(infobuf.type == H5L_TYPE_HARD) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5JNIFatalError( env, "H5Lget_val:  link is hard type");
            return NULL;
        }
        
        lValue = (char*)malloc(sizeof(char) * buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory( env, "H5Lget_val:  malloc failed");
            return NULL;
        }

        status = H5Lget_val((hid_t)loc_id, (const char*)lName, (void*)lValue, (size_t)buf_size, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        if (status < 0) {
           free(lValue);
           h5libraryError(env);
           return NULL;
        }
        /* may throw OutOfMemoryError */
        if(infobuf.type == H5L_TYPE_EXTERNAL) {
            status = H5Lunpack_elink_val((char*)lValue, (size_t)infobuf.u.val_size, (unsigned*)NULL, (const char**)&file_name, (const char**)&obj_name);
            if (status < 0) {
               free(lValue);
               h5libraryError(env);
               return NULL;
            }
            
            str = ENVPTR->NewStringUTF(ENVPAR obj_name);
        }
        else
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
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_val_external
     * Signature: (ILjava/lang/String;I)Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1val_1external
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
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
            h5nullArgument( env, "H5Lget_val_external:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_val_external:  name not pinned");
            return NULL;
        }

        /* get the length of the link val */
        status = H5Lget_info((hid_t)loc_id, (const char*)lName, (H5L_info_t*)&infobuf, (hid_t)H5P_DEFAULT);
        if(status < 0) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5libraryError(env);
            return NULL;
        }
        buf_size = infobuf.u.val_size + 1;/* add extra space for the null terminator */
        
        if(infobuf.type == H5L_TYPE_HARD) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5JNIFatalError( env, "H5Lget_val_external:  link is hard type");
            return NULL;
        }
        
        lValue = (char*)malloc(sizeof(char)*buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
            h5outOfMemory( env, "H5Lget_val_external:  malloc failed ");
            return NULL;
        }

        status = H5Lget_val((hid_t)loc_id, (const char*)lName, (void*)lValue, (size_t)buf_size, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
        if (status < 0) {
           free(lValue);
           h5libraryError(env);
           return NULL;
        }
        /* may throw OutOfMemoryError */
        if(infobuf.type == H5L_TYPE_EXTERNAL) {
            status =  H5Lunpack_elink_val((char*)lValue, (size_t)infobuf.u.val_size, (unsigned*)NULL, (const char**)&file_name, (const char**)&obj_name);
            if (status < 0) {
               free(lValue);
               h5libraryError(env);
               return NULL;
            }
            
            str = ENVPTR->NewStringUTF(ENVPAR file_name);
        }
        else
            str = ENVPTR->NewStringUTF(ENVPAR lValue);
        
        if (str == NULL) {
            /* exception -- fatal JNI error */
            free(lValue);
            h5JNIFatalError( env, "H5Lget_val_external:  return string not created");
            return NULL;
        }

        free(lValue);

        return str;
    }


#ifdef __cplusplus
}
#endif
