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
 *  file interface functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *    http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "hdf5.h"
#include "h5jni.h"
#include "h5fImp.h"
#include "h5util.h"

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fopen
 * Signature: (Ljava/lang/String;IJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Fopen
  (JNIEnv *env, jclass clss, jstring name, jint flags, jlong access_id)
{
    hid_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        /* exception -- bad argument? */
        h5nullArgument( env, "H5Fopen:  name is NULL");
        return -1;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (file == NULL) {
        /* exception -- out of memory? */
        h5JNIFatalError( env, "H5Fopen:  file name not pinned");
        return -1;
    }
    status = H5Fopen(file, (unsigned) flags, (hid_t) access_id );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);
    if (status < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)status;


}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fcreate
 * Signature: (Ljava/lang/String;IJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Fcreate
  (JNIEnv *env, jclass clss, jstring name, jint flags, jlong create_id, jlong access_id)
{
    hid_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        /* exception -- bad argument? */
        h5nullArgument( env, "H5Fcreate:  name is NULL");
        return -1;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (file == NULL) {
        /* exception -- out of memory? */
        h5JNIFatalError( env, "H5Fcreate:  file name is not pinned");
        return -1;
    }

    status = H5Fcreate(file, flags, create_id, access_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);
    if (status < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fflush
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fflush
  (JNIEnv *env, jclass clss, jlong object_id, jint scope)
{
    herr_t retVal = -1;
    retVal =  H5Fflush((hid_t) object_id, (H5F_scope_t) scope );
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_name
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1name
  (JNIEnv *env, jclass cls, jlong file_id)
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
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fis_hdf5
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fis_1hdf5
  (JNIEnv *env, jclass clss, jstring name)
{
    htri_t retVal = 0;
    char * file;
    jboolean isCopy;

    if (name == NULL) {
        /* exception -- bad argument? */
        h5nullArgument( env, "H5Fis_hdf5:  name is NULL");
        return JNI_FALSE;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (file == NULL) {
        /* exception -- out of memory? */
        h5JNIFatalError( env, "H5Fis_hdf5:  file name is not pinned");
        return JNI_FALSE;
    }

    retVal = H5Fis_hdf5(file);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);

    if (retVal > 0) {
        return JNI_TRUE;
    } 
    else if (retVal == 0) {
        return JNI_FALSE;
    } 
    else {
        /*  raise exception here -- return value is irrelevant */
        h5libraryError(env);
        return JNI_FALSE;
    }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_create_plist
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Fget_1create_1plist
  (JNIEnv *env, jclass clss, jlong file_id)
{
    hid_t retVal = -1;
    retVal =  H5Fget_create_plist((hid_t) file_id );
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_access_plist
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Fget_1access_1plist
  (JNIEnv *env, jclass clss, jlong file_id)
{
    hid_t retVal = -1;
    retVal =  H5Fget_access_plist((hid_t) file_id);
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_intent
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1intent
  (JNIEnv *env, jclass cls, jlong file_id)
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
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fclose
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Fclose
  (JNIEnv *env, jclass clss, jlong file_id)
{
    herr_t status = -1;

    if (file_id > 0)
        status = H5Fclose((hid_t) file_id );

    if (status < 0) {
        h5libraryError(env);
        return -1;
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fmount
 * Signature: (JLjava/lang/String;JJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fmount
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong child_id, jlong plist_id)
{
    herr_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        /* exception -- bad argument? */
        h5nullArgument( env, "H5Fmount:  name is NULL");
        return -1;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (file == NULL) {
        /* exception -- out of memory? */
        h5JNIFatalError( env, "H5Fmount:  file name is not pinned");
        return -1;
    }

    status = H5Fmount((hid_t) loc_id, file, (hid_t) child_id, (hid_t) plist_id );
    ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);
    if (status < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Funmount
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Funmount
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name)
{
    herr_t status;
    char* file;
    jboolean isCopy;

    if (name == NULL) {
        /* exception -- bad argument? */
        h5nullArgument( env, "H5Funmount:  name is NULL");
        return -1;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
    if (file == NULL) {
        h5JNIFatalError( env, "H5Funmount:  file name is not pinned");
        /* exception -- out of memory? */
        return -1;
    }

    status = H5Funmount((hid_t) loc_id, file );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,file);
    if (status < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_freespace
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1freespace
  (JNIEnv *env, jclass cls, jlong file_id)
{
    hssize_t ret_val = -1;

    ret_val = H5Fget_freespace((hid_t)file_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jlong)ret_val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Freopen
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Freopen
  (JNIEnv *env, jclass clss, jlong file_id)
{
    hid_t retVal = -1;
    retVal =  H5Freopen((hid_t)file_id);
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_obj_ids_long
 * Signature: (JIJ[J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1obj_1ids_1long
  (JNIEnv *env, jclass cls, jlong file_id, jint types, jlong maxObjs,
          jlongArray obj_id_list)
{
    ssize_t ret_val;
    jint *obj_id_listP;
    jboolean isCopy;
    hid_t *id_list;
    int rank;
    int i;

    ret_val = -1;

    if ( obj_id_list == NULL ) {
        h5nullArgument( env, "H5Fget_obj_ids_long:  obj_id_list is NULL");
        return -1;
    }

    obj_id_listP = ENVPTR->GetIntArrayElements(ENVPAR obj_id_list,&isCopy);
    if (obj_id_listP == NULL) {
        h5JNIFatalError( env, "H5Fget_obj_ids_long:  obj_id_list not pinned");
        return -1;
    }
    rank = (int)ENVPTR->GetArrayLength(ENVPAR obj_id_list);

    id_list = (hid_t *)malloc( rank * sizeof(hid_t));

    if (id_list == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,JNI_ABORT);
        h5JNIFatalError(env,  "H5Fget_obj_ids_long:  obj_id_list not converted to hid_t");
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
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_obj_ids
 * Signature: (JII[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1obj_1ids
  (JNIEnv *env, jclass clss, jlong file_id, jint types, jint obj_count, jlongArray obj_id_list)
{
    ssize_t status=-1;
    jint *obj_id_listP;
    jboolean isCopy;

    if ( obj_id_list == NULL ) {
        h5nullArgument( env, "H5Fget_obj_ids:  obj_id_list is NULL");
        return -1;
    }

    obj_id_listP = ENVPTR->GetIntArrayElements(ENVPAR obj_id_list,&isCopy);
    if (obj_id_listP == NULL) {
        h5JNIFatalError( env, "H5Fget_obj_ids:  obj_id_list not pinned");
        return -1;
    }

    status = H5Fget_obj_ids((hid_t)file_id, (unsigned int)types, (int)obj_count, (hid_t*)obj_id_listP);

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,JNI_ABORT);
        h5libraryError(env);
    } 
    else  {
        ENVPTR->ReleaseIntArrayElements(ENVPAR obj_id_list,obj_id_listP,0);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_obj_count(hid_t file_id, unsigned int types )
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1obj_1count
  (JNIEnv *env, jclass clss, jlong file_id, jint types)
{
    ssize_t status = -1;

    status = H5Fget_obj_count((hid_t)file_id, (unsigned int)types );

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_obj_count_long
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1obj_1count_1long
  (JNIEnv *env, jclass cls, jlong file_id, jint types)
{
    ssize_t ret_val = -1;

    ret_val = H5Fget_obj_count((hid_t)file_id, (unsigned int)types );

    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jlong)ret_val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_name
 * Signature: (JLjava/lang/String;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_2name
  (JNIEnv *env, jclass clss, jlong obj_id, jstring name, jint buf_size)
{
    char *aName;
    jstring str;
    ssize_t size;

    if (buf_size <= 0) {
        h5badArgument( env, "H5Fget_name:  buf_size <= 0");
        return NULL;
    }
    aName = (char*)malloc(sizeof(char)*buf_size);
    if (aName == NULL) {
        h5outOfMemory( env, "H5Fget_name:  malloc failed");
        return NULL;
    }
    size = H5Fget_name ((hid_t) obj_id, (char *)aName, (size_t)buf_size);
    if (size < 0) {
        free(aName);
        h5libraryError(env);
        return NULL; 
    }

    str = ENVPTR->NewStringUTF(ENVPAR aName);
    free(aName);

    return str;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_filesize
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1filesize
  (JNIEnv *env, jclass clss, jlong file_id)
{
    herr_t status;
    hsize_t size = 0;

    status = H5Fget_filesize ((hid_t) file_id, (hsize_t *) &size);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jlong) size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_mdc_hit_rate
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1mdc_1hit_1rate
  (JNIEnv *env, jclass cls, jlong file_id)
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
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_mdc_size
 * Signature: (J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1mdc_1size
  (JNIEnv *env, jclass cls, jlong file_id, jlongArray metadata_cache)
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
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Freset_mdc_hit_rate_stats
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Freset_1mdc_1hit_1rate_1stats
  (JNIEnv *env, jclass cls, jlong file_id)
{
    herr_t ret_val = -1;

    ret_val = H5Freset_mdc_hit_rate_stats((hid_t)file_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5export_dataset
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5export_1dataset
  (JNIEnv *env, jclass cls, jstring file_export_name, jstring file_name, jstring object_path, jint binary_order)
{
    herr_t status = -1;
    herr_t ret_val = -1;
    hid_t file_id = -1;
    hid_t dataset_id = -1;
    FILE *stream;
    char *file_export;
    char *file;
    char *object_name;
    jboolean isCopy;

    if (file_export_name == NULL) {
        h5nullArgument(env, "HDF5Library_export_data:  file_export_name is NULL");
        return;
    }
    if (file_name == NULL) {
        h5nullArgument(env, "HDF5Library_export_data:  file_name is NULL");
        return;
    }
    if (object_path == NULL) {
        h5nullArgument(env, "HDF5Library_export_data:  object_path is NULL");
        return;
    }

    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR file_name, &isCopy);
    if (file == NULL) {
        /* exception -- out of memory? */
        h5JNIFatalError( env, "H5Fopen:  file name not pinned");
        return;
    }

    file_id = H5Fopen(file, (unsigned)H5F_ACC_RDWR, (hid_t)H5P_DEFAULT);

    ENVPTR->ReleaseStringUTFChars(ENVPAR file_name, file);
    if (file_id < 0) {
        /* throw exception */
        h5libraryError(env);
        return;
    }

    object_name = (char*)ENVPTR->GetStringUTFChars(ENVPAR object_path, &isCopy);
    if (object_name == NULL) {
        h5JNIFatalError( env, "H5Dopen:  object name not pinned");
        return;
    }

    dataset_id = H5Dopen2(file_id, (const char*)object_name, (hid_t)H5P_DEFAULT);

    ENVPTR->ReleaseStringUTFChars(ENVPAR object_path, object_name);
    if (dataset_id < 0) {
        H5Fclose(file_id);
        h5libraryError(env);
        return;
    }

    file_export = (char *)ENVPTR->GetStringUTFChars(ENVPAR file_export_name, 0);
    stream = fopen(file_export, "w+");
    ENVPTR->ReleaseStringUTFChars(ENVPAR file_export_name, file_export);

    ret_val = h5str_dump_simple_dset(stream, dataset_id, binary_order);

    if (stream)
        fclose(stream);

    H5Dclose(dataset_id);

    H5Fclose(file_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }
}


#ifdef __cplusplus
}
#endif
