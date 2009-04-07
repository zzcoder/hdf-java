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

/*
 *  This code is the C-interface called by Java programs to access the
 *  general library functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://www.hdfgroup.org/HDF5/doc/
 *
 */

#include <stdlib.h>
#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5F.h"

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fis_hdf5
     * Signature: (Ljava/lang/String;)Z
     */
    JNIEXPORT jboolean JNICALL Java_hdf_h5_H5F_H5Fis_1hdf5
      (JNIEnv *env, jclass cls, jstring filename)
    {
        htri_t ret_val = 0;
        char * file;
        jboolean isCopy;

        if (filename == NULL) {
            h5nullArgument( env, "H5Fis_hdf5:  name is NULL");
            return JNI_FALSE;
        }

        file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
        if (file == NULL) {
            h5JNIFatalError( env, "H5Fis_hdf5:  file name is not pinned");
            return JNI_FALSE;
        }

        ret_val = H5Fis_hdf5(file);

        ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);

        if (ret_val < 0)
            h5libraryError(env);

        if (ret_val > 0)
            return JNI_TRUE;
        else
            return JNI_FALSE;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fcreate
     * Signature: (Ljava/lang/String;III)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fcreate
      (JNIEnv *env, jclass cls, jstring filename, jint flags, jint create_id,
              jint access_id)
    {
        hid_t ret_val;
        char* file;
        jboolean isCopy;

        if (filename == NULL) {
            h5nullArgument( env, "H5Fcreate:  filename is NULL");
            return -1;
        }

        file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
        if (file == NULL) {
            h5JNIFatalError( env, "H5Fcreate:  file filename is not pinned");
            return -1;
        }

        ret_val = H5Fcreate(file, flags, create_id, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);
        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fclose
     * Signature: (I)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Fclose
      (JNIEnv *env, jclass cls, jint file_id)
    {
        herr_t ret_val = 0;

        if (file_id > 0)
            ret_val = H5Fclose((hid_t) file_id );

        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fopen
     * Signature: (Ljava/lang/String;II)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fopen
      (JNIEnv *env, jclass cls, jstring filename, jint flags, jint access_id)
    {
        hid_t ret_val;
        char* file;
        jboolean isCopy;

        if (filename == NULL) {
            h5nullArgument( env, "H5Fopen: file name is NULL");
            return -1;
        }

        file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
        if (file == NULL) {
            h5JNIFatalError( env, "H5Fopen: file name not pinned");
            return -1;
        }

        ret_val = H5Fopen(file, (unsigned) flags, (hid_t) access_id );

        ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Freopen
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Freopen
      (JNIEnv *env, jclass cls, jint file_id)
    {
        herr_t ret_val = -1;
        ret_val =  H5Freopen((hid_t)file_id);
        if (ret_val < 0) {
            /* throw exception */
            h5libraryError(env);
        }
        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fflush
     * Signature: (ILhdf/h5/enums/H5F_SCOPE;)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Fflush
      (JNIEnv *env, jclass cls, jint object_id, jobject scope)
    {
        herr_t ret_val = -1;
        jint enum_val = H5F_SCOPE_LOCAL;

        GET_ENUM_VALUE(scope, enum_val);

        ret_val =  H5Fflush((hid_t) object_id, (H5F_scope_t) enum_val);

        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_name
     * Signature: (I)Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_hdf_h5_H5F_H5Fget_1name
      (JNIEnv *env, jclass cls, jint file_id)
    {
        char *namePtr;
        jstring str;
        ssize_t buf_size;

        /* get the length of the name */
        buf_size = H5Fget_name(file_id, NULL, 0);

        if (buf_size <= 0) {
            h5badArgument( env, "H5Fget_name:  buf_size <= 0");
            return NULL;
        }

        buf_size++; /* add extra space for the null terminator */
        namePtr = (char*)malloc(sizeof(char)*buf_size);
        if (namePtr == NULL) {
            h5outOfMemory( env, "H5Fget_name:  malloc failed");
            return NULL;
        }

        buf_size = H5Fget_name ((hid_t) file_id, (char *)namePtr, (size_t)buf_size);

        if (buf_size < 0) {
            free(namePtr);
            h5libraryError(env);
            return NULL;
        }

        str = ENVPTR->NewStringUTF(ENVPAR namePtr);
        free(namePtr);

        return str;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_create_plist
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fget_1create_1plist
      (JNIEnv *env, jclass cls, jint file_id)
    {
        hid_t ret_val = -1;
        ret_val =  H5Fget_create_plist((hid_t) file_id );

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_access_plist
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fget_1access_1plist
      (JNIEnv *env, jclass cls, jint file_id)
    {
        hid_t ret_val = -1;
        ret_val =  H5Fget_access_plist((hid_t) file_id);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_intent
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fget_1intent
      (JNIEnv *env, jclass cls, jint file_id)
    {
        herr_t ret_val = -1;
        unsigned intent = 0;

        ret_val =  H5Fget_intent((hid_t) file_id, &intent);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jint)intent;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_obj_count
     * Signature: (II)J
     */
    JNIEXPORT jlong JNICALL Java_hdf_h5_H5F_H5Fget_1obj_1count
      (JNIEnv *env, jclass cls, jint file_id, jint types)
    {
        ssize_t ret_val = -1;

        ret_val = H5Fget_obj_count((hid_t)file_id, (unsigned int)types );

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jlong)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_obj_ids
     * Signature: (IIJ[I)J
     */
    JNIEXPORT jlong JNICALL Java_hdf_h5_H5F_H5Fget_1obj_1ids
      (JNIEnv *env, jclass cls, jint file_id, jint types, jlong maxObjs,
              jintArray obj_id_list)
    {
        ssize_t ret_val;
        jint *obj_id_listP;
        jboolean isCopy;
        hid_t *id_list;
        int rank;
        int i;

        ret_val = -1;

        if ( obj_id_list == NULL ) {
            h5nullArgument( env, "H5Fget_obj_ids:  obj_id_list is NULL");
            return -1;
        }

        obj_id_listP = ENVPTR->GetIntArrayElements(ENVPAR obj_id_list,&isCopy);
        if (obj_id_listP == NULL) {
            h5JNIFatalError( env, "H5Fget_obj_ids:  obj_id_list not pinned");
            return -1;
        }
        rank = (int)ENVPTR->GetArrayLength(ENVPAR obj_id_list);

        id_list = (hid_t *)malloc( rank * sizeof(hid_t));

        if (id_list == NULL) {
            ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,JNI_ABORT);
            h5JNIFatalError(env,  "H5Fget_obj_ids:  obj_id_list not converted to hid_t");
            return -1;
        }

        ret_val = H5Fget_obj_ids((hid_t)file_id, (unsigned int)types, (size_t)maxObjs, id_list);

        if (ret_val < 0) {
            ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,JNI_ABORT);
            free(id_list);
            h5libraryError(env);
            return -1;
        }

        for (i = 0; i < rank; i++) {
            obj_id_listP[i] = id_list[i];
        }
        free(id_list);
        ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,0);

        return (jlong)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fmount
     * Signature: (ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Fmount
      (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint child_id, jint plist_id)
    {
        herr_t ret_val;
        char* file;
        jboolean isCopy;

        if (name == NULL) {
            h5nullArgument( env, "H5Fmount:  name is NULL");
            return;
        }

        file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (file == NULL) {
            h5JNIFatalError( env, "H5Fmount:  file name is not pinned");
            return;
        }

        ret_val = H5Fmount((hid_t) loc_id, file, (hid_t) child_id, (hid_t) plist_id );
        ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);

        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Funmount
     * Signature: (ILjava/lang/String;)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Funmount
      (JNIEnv *env, jclass cls, jint loc_id, jstring name)
    {
        herr_t ret_val;
        char* file;
        jboolean isCopy;

        if (name == NULL) {
            h5nullArgument( env, "H5Funmount:  name is NULL");
            return;
        }

        file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (file == NULL) {
            h5JNIFatalError( env, "H5Funmount:  file name is not pinned");
            return;
        }

        ret_val = H5Funmount((hid_t) loc_id, file );

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);
        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_freespace
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_hdf_h5_H5F_H5Fget_1freespace
      (JNIEnv *env, jclass cls, jint file_id)
    {
        hssize_t ret_val = -1;

        ret_val = H5Fget_freespace((hid_t)file_id);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jlong)ret_val;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_filesize
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_hdf_h5_H5F_H5Fget_1filesize
      (JNIEnv *env, jclass cls, jint file_id)
    {
        herr_t ret_val = -1;
        hsize_t size = 0;

        ret_val = H5Fget_filesize((hid_t)file_id, &size);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jlong)size;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_mdc_hit_rate
     * Signature: (I)D
     */
    JNIEXPORT jdouble JNICALL Java_hdf_h5_H5F_H5Fget_1mdc_1hit_1rate
      (JNIEnv *env, jclass cls, jint file_id)
    {
        double rate = 0.0;
        herr_t ret_val = -1;

        ret_val = H5Fget_mdc_hit_rate((hid_t)file_id, &rate);

        if (ret_val < 0) {
            h5libraryError(env);
        }

        return (jdouble)rate;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Fget_mdc_size
     * Signature: (I[J)V
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fget_1mdc_1size
      (JNIEnv *env, jclass cls, jint file_id, jlongArray metadata_cache)
    {
        herr_t ret_val = -1;
        jint size = 0;
        jlong *metadata_cache_ptr;
        size_t max_size=0, min_clean_size=0, cur_size=0;
        int cur_num_entries=0;
        jboolean isCopy;

        if ( metadata_cache == NULL ) {
            h5nullArgument( env, "H5Fget_mdc_size:  metadata_cache is NULL");
            return -1;
        }

        size = (int)ENVPTR->GetArrayLength(ENVPAR metadata_cache);
        if (size < 3) {
            h5badArgument(env, "H5Fget_mdc_size:  length of metadata_cache < 3.");
            return -1;
        }

        ret_val = H5Fget_mdc_size((hid_t)file_id, &max_size, &min_clean_size,
        		&cur_size, &cur_num_entries);

        if (ret_val < 0) {
            h5libraryError(env);
            return -1;
        }

        metadata_cache_ptr = ENVPTR->GetLongArrayElements(ENVPAR metadata_cache,&isCopy);
        metadata_cache_ptr[0] = max_size;
        metadata_cache_ptr[1] = min_clean_size;
        metadata_cache_ptr[2] = cur_size;
        ENVPTR->ReleaseLongArrayElements(ENVPAR metadata_cache, metadata_cache_ptr, 0);

        return (jint)cur_num_entries;
    }

    /*
     * Class:     hdf_h5_H5F
     * Method:    H5Freset_mdc_hit_rate_stats
     * Signature: (I)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Freset_1mdc_1hit_1rate_1stats
      (JNIEnv *env, jclass cls, jint file_id)
    {
        herr_t ret_val = -1;

        ret_val = H5Freset_mdc_hit_rate_stats((hid_t)file_id);

        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

#ifdef __cplusplus
}
#endif
