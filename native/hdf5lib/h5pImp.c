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
 *  Property List API Functions of the HDF5 library.
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
#include <jni.h>
#include <stdlib.h>

#ifndef FALSE
#define FALSE 0
#endif

#ifndef TRUE
#define TRUE (!FALSE)
#endif

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR 
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif

extern jboolean h5outOfMemory(JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError(JNIEnv *env, char *functName);
extern jboolean h5nullArgument(JNIEnv *env, char *functName);
extern jboolean h5libraryError(JNIEnv *env );
extern jboolean h5badArgument(JNIEnv *env, char *functName);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pcreate
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Pcreate
  (JNIEnv *env, jclass clss, jint type)
{
    hid_t retVal = -1;
    
    retVal = H5Pcreate((hid_t)type);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Pclose
  (JNIEnv *env, jclass clss, jint plist)
{
    herr_t retVal = 0;

    if (plist > 0)
        retVal = H5Pclose((hid_t)plist);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_class
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1class
  (JNIEnv *env, jclass clss, jint plist)
{
    hid_t retVal = H5P_NO_CLASS;
    
    retVal = H5Pget_class((hid_t) plist);
    if (retVal == H5P_NO_CLASS) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pcopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Pcopy
  (JNIEnv *env, jclass clss, jint plist)
{
    hid_t retVal = -1;
    
    retVal = H5Pcopy((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_version
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1version
  (JNIEnv *env, jclass clss, jint plist, jintArray version_info)
{
    herr_t   status;
    jint    *theArray;
    jboolean isCopy;

    if (version_info == NULL) {
        h5nullArgument(env, "H5Pget_version:  version_info input array is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR version_info) < 4) {
        h5badArgument(env, "H5Pget_version:  version_info input array < 4");
        return -1;
    }
    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR version_info,&isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_version:  version_info not pinned");
        return -1;
    }

    status = H5Pget_version((hid_t)plist, (unsigned *)&(theArray[0]),
            (unsigned *)&(theArray[1]), (unsigned *)&(theArray[2]), (unsigned *)&(theArray[3]));
    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR version_info, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    ENVPTR->ReleaseIntArrayElements(ENVPAR version_info, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_userblock
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1userblock
  (JNIEnv *env, jclass clss, jint plist, jlong size)
{
    long   sz;
    herr_t retVal = -1;
    
    sz = (long)size;
    retVal = H5Pset_userblock((hid_t)plist, (hsize_t)sz);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_userblock
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1userblock
  (JNIEnv *env, jclass clss, jint plist, jlongArray size)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    hsize_t  s;

    if (size == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pget_userblock:  size is NULL");
        return -1;
    }
    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_userblock:  size not pinned");
        return -1;
    }

    status = H5Pget_userblock((hid_t)plist, &s);
    if (status < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    theArray[0] = s;
    ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_sizes
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1sizes
  (JNIEnv *env, jclass clss, jint plist, jint sizeof_addr, jint sizeof_size)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_sizes((hid_t)plist, (size_t)sizeof_addr, (size_t)sizeof_size);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_sizes
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1sizes
  (JNIEnv *env, jclass clss, jint plist, jlongArray size)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    size_t   ss;
    size_t   sa;

    if (size == NULL) {
        h5nullArgument(env, "H5Pget_sizes:  size is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR size) < 2) {
        h5badArgument(env, "H5Pget_sizes:  size input array < 2 elements");
        return -1;
    }
    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_sizes:  size not pinned");
        return -1;
    }

    status = H5Pget_sizes((hid_t)plist, &sa, &ss);
    if (status < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    theArray[0] = sa;
    theArray[1] = ss;
    ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, 0);

    return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_sym_k
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1sym_1k
  (JNIEnv *env, jclass clss, jint plist, jint ik, jint lk)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_sym_k((hid_t)plist, (int)ik, (int)lk);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_sym_k
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1sym_1k
  (JNIEnv *env, jclass clss, jint plist, jintArray size)
{
    herr_t   status;
    jint    *theArray;
    jboolean isCopy;

    if (size == NULL) {
        h5nullArgument(env, "H5Pget_sym_k:  size is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR size) < 2) {
        h5badArgument(env, "H5Pget_sym_k:  size < 2 elements");
        return -1;
    }

    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_sym_k:  size not pinned");
        return -1;
    }

    status = H5Pget_sym_k((hid_t)plist, (unsigned *)&(theArray[0]), (unsigned *)&(theArray[1]));
    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR size, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    ENVPTR->ReleaseIntArrayElements(ENVPAR size, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_istore_k
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1istore_1k
  (JNIEnv *env, jclass clss, jint plist, jint ik)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_istore_k((hid_t)plist, (int)ik);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_istore_k
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1istore_1k
  (JNIEnv *env, jclass clss, jint plist, jintArray ik)
{
    herr_t   status;
    jint    *theArray;
    jboolean isCopy;

    if (ik == NULL) {
        h5nullArgument(env, "H5Pget_store_k:  ik is NULL");
        return -1;
    }
    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR ik, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_store_k:  size not pinned");
        return -1;
    }

    status = H5Pget_istore_k((hid_t)plist, (unsigned *)&(theArray[0]));
    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR ik, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    ENVPTR->ReleaseIntArrayElements(ENVPAR ik, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_layout
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1layout
  (JNIEnv *env, jclass clss, jint plist, jint layout)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_layout((hid_t)plist, (H5D_layout_t)layout);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_layout
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1layout
  (JNIEnv *env, jclass clss, jint plist)
{
    H5D_layout_t retVal = H5D_LAYOUT_ERROR;
    
    retVal = H5Pget_layout((hid_t)plist);
    if (retVal == H5D_LAYOUT_ERROR) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_chunk
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1chunk
  (JNIEnv *env, jclass clss, jint plist, jint ndims, jbyteArray dim)
{
    herr_t   status;
    jbyte   *theArray;
    jboolean isCopy;
    hsize_t *da;
    hsize_t *lp;
    jlong   *jlp;
    int      i;
    int      rank;

    if (dim == NULL) {
        h5nullArgument(env, "H5Pset_chunk:  dim array is NULL");
        return -1;
    }
    i = ENVPTR->GetArrayLength(ENVPAR dim);
    rank = i / sizeof(jlong);
    if (rank < ndims) {
        h5badArgument(env, "H5Pset_chunk:  dims array < ndims");
        return -1;
    }
    theArray = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR dim, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pset_chunk:  dim array not pinned");
        return -1;
    }
    da = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
    if (da == NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR dim, theArray, JNI_ABORT);
        h5JNIFatalError(env,  "H5Pset_chunk:  dims not converted to hsize_t");
        return -1;
    }
    jlp = (jlong *)theArray;
    for (i = 0; i < rank; i++) {
        *lp = (hsize_t)*jlp;
        lp++;
        jlp++;
    }

    status = H5Pset_chunk((hid_t)plist, (int)ndims, da);

    ENVPTR->ReleaseByteArrayElements(ENVPAR dim, theArray, 0);
    free(da);

    if (status < 0) {
        h5libraryError(env);
    }
    
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_chunk
 * Signature: (II[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1chunk
  (JNIEnv *env, jclass clss, jint plist, jint max_ndims, jlongArray dims)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    hsize_t *da;
    int      i;

    if (dims == NULL) {
        h5nullArgument(env, "H5Pget_chunk:  dims is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR dims) < max_ndims) {
        h5badArgument(env, "H5Pget_chunk:  dims array < max_ndims");
        return -1;
    }
    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR dims, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_chunk:  input dims not pinned");
        return -1;
    }
    da = (hsize_t *)malloc(max_ndims * sizeof(hsize_t));
    if (da == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR dims, theArray, JNI_ABORT);
        h5JNIFatalError(env, "H5Pget_chunk:  dims not converted to hsize_t");
        return -1;
    }

    status = H5Pget_chunk((hid_t)plist, (int)max_ndims, da);

    if (status < 0)  {
        ENVPTR->ReleaseLongArrayElements(ENVPAR dims, theArray, JNI_ABORT);
        free (da);
        h5libraryError(env);
        return -1;
    } 
    for (i = 0; i < max_ndims; i++) {
        theArray[i] = da[i];
    }
    free (da);
    ENVPTR->ReleaseLongArrayElements(ENVPAR dims, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_alignment
 * Signature: (IJJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1alignment
  (JNIEnv *env, jclass clss, jint plist, jlong threshold, jlong alignment)
{
    long   thr;
    long   align;
    herr_t retVal = -1;
    
    thr = (long)threshold;
    align = (long)alignment;
    
    retVal = H5Pset_alignment((hid_t)plist, (hsize_t)thr, (hsize_t)align);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_alignment
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1alignment
  (JNIEnv *env, jclass clss, jint plist, jlongArray alignment)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    hsize_t  t;
    hsize_t  a;

    if (alignment == NULL) {
        h5nullArgument(env, "H5Pget_alignment:  input alignment is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR alignment) < 2) {
        h5badArgument(env, "H5Pget_alignment:  allingment input array < 2");
        return -1;
    }
    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR alignment, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_alignment:  input array not pinned");
        return -1;
    }

    status = H5Pget_alignment((hid_t)plist, &t, &a);
    if (status < 0)  {
        ENVPTR->ReleaseLongArrayElements(ENVPAR alignment, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    theArray[0] = t;
    theArray[1] = a;
    ENVPTR->ReleaseLongArrayElements(ENVPAR alignment, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_external
 * Signature: (ILjava/lang/String;JJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1external
  (JNIEnv *env, jclass clss, jint plist, jstring name, jlong offset, jlong size)
{
    herr_t   status;
    char    *file;
    jboolean isCopy;
    long     off;
    long     sz;

    off = (long)offset;
    sz = (long)size;
    if (name == NULL) {
        h5nullArgument(env, "H5Pset_external:  name is NULL");
        return -1;
    }
    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (file == NULL) {
        h5JNIFatalError(env, "H5Pset_external:  name not pinned");
        return -1;
    }

    status = H5Pset_external((hid_t)plist, file, (off_t)off, (hsize_t)sz);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, file);
    
    if (status < 0) {
        h5libraryError(env);
    }
    
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_external_count
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1external_1count
  (JNIEnv *env, jclass clss, jint plist)
{
    int retVal = -1;
    
    retVal = H5Pget_external_count((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_external
 * Signature: (IIJ[Ljava/lang/String;[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1external
  (JNIEnv *env, jclass clss, jint plist, jint idx, jlong name_size,
  jobjectArray name, jlongArray size)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    char    *file;
    jstring  str;
    off_t    o;
    hsize_t  s;

    if (name_size < 0) {
        h5badArgument(env, "H5Pget_external:  name_size < 0");
        return -1;
    }
    else if (name_size == 0) {
        file = NULL;
    }
    else {
        file = (char *)malloc(sizeof(char)*name_size);
    }

    if (size != NULL) {
        if (ENVPTR->GetArrayLength(ENVPAR size) < 2) {
            free(file);
            h5badArgument(env, "H5Pget_external:  size input array < 2");
            return -1;
        }
        theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR size, &isCopy);
        if (theArray == NULL) {
            free(file);
            h5JNIFatalError( env, "H5Pget_external:  size array not pinned");
            return -1;
        }
    }

    status = H5Pget_external((hid_t) plist, (int)idx, (size_t)name_size,
            file, (off_t *)&o, (hsize_t *)&s);
    if (status < 0) {
        if (size != NULL) {
            ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, JNI_ABORT);
        }
        free(file);
        h5libraryError(env);
        return -1;
    }

    if (size != NULL) {
        theArray[0] = o;
        theArray[1] = s;
        ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, 0);
    }

    if (file != NULL) {
        /*  NewStringUTF may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR file);
        if (str == NULL) {
            free(file);
            h5JNIFatalError(env, "H5Pget_external:  return array not created");
            return -1;
        }
        /*  SetObjectArrayElement may raise exceptions */
        ENVPTR->SetObjectArrayElement(ENVPAR name, 0, (jobject)str);
        free(file);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fill_value
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fill_1value
  (JNIEnv *env, jclass clss, jint plist_id, jint type_id, jbyteArray value)
{
    jint     status = -1;
    jbyte   *byteP;
    jboolean isCopy;

    if (value != NULL) {
        byteP = ENVPTR->GetByteArrayElements(ENVPAR value, &isCopy);
        if (byteP == NULL) {
            h5JNIFatalError(env, "H5Pget_fill_value:  value array not pinned");
            return -1;
        }
    }

    status = H5Pset_fill_value((hid_t)plist_id, (hid_t)type_id, byteP);
    if (status < 0) {
        if (value != NULL) {
            ENVPTR->ReleaseByteArrayElements(ENVPAR value, byteP, JNI_ABORT);
        }
        h5libraryError(env);
        return -1;
    }
    
    if (value != NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR value, byteP, 0);
    }

    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fill_value
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fill_1value
  (JNIEnv *env, jclass clss, jint plist_id, jint type_id, jbyteArray value)
{
    jint     status;
    jbyte   *byteP;
    jboolean isCopy;

    if (value == NULL) {
        h5badArgument(env, "H5Pget_fill_value:  value is NULL");
        return -1;
    }
    byteP = ENVPTR->GetByteArrayElements(ENVPAR value, &isCopy);
    if (byteP == NULL) {
        h5JNIFatalError(env, "H5Pget_fill_value:  value array not pinned");
        return -1;
    }
    
    status = H5Pget_fill_value((hid_t)plist_id, (hid_t)type_id, byteP);
    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR value, byteP, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseByteArrayElements(ENVPAR value, byteP, 0);

    return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_filter
 * Signature: (IIIJ[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter, jint flags,
  jlong cd_nelmts, jintArray cd_values)
{
    herr_t status;
    jint *theArray;
    jboolean isCopy;

    if (cd_values == NULL)
        status = H5Pset_filter((hid_t)plist, (H5Z_filter_t)filter,
                (unsigned int)flags, (size_t)cd_nelmts, NULL);
    else {
        theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR cd_values, &isCopy);
        if (theArray == NULL) {
            h5JNIFatalError(env, "H5Pset_filter:  input array  not pinned");
            return -1;
        }
        status = H5Pset_filter((hid_t)plist, (H5Z_filter_t)filter,
                (unsigned int)flags, (size_t)cd_nelmts, (const unsigned int *)theArray);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, theArray, 0);
    }

    if (status < 0) {
        h5libraryError(env);
    }
    
    return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_nfilters
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1nfilters
  (JNIEnv *env, jclass clss, jint plist)
{
    int retVal = -1;
    
    retVal = H5Pget_nfilters((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter
 * Signature: (II[I[J[IJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter_number, jintArray flags,
  jlongArray cd_nelmts, jintArray cd_values, jlong namelen, jobjectArray name)
{
    herr_t   status;
    jint    *flagsArray;
    jlong   *cd_nelmtsArray;
    jint    *cd_valuesArray;
    jboolean isCopy;
    jstring  str;
    char    *filter;

    if (namelen <= 0) {
        h5badArgument(env, "H5Pget_filter:  namelen <= 0");
        return -1;
    }
    if (flags == NULL) {
        h5badArgument(env, "H5Pget_filter:  flags is NULL");
        return -1;
    }
    if (cd_nelmts == NULL) {
        h5badArgument(env, "H5Pget_filter:  cd_nelmts is NULL");
        return -1;
    }
    if (cd_values == NULL) {
        h5badArgument(env, "H5Pget_filter:  cd_values is NULL");
        return -1;
    }
    filter = (char *)malloc(sizeof(char)*namelen);
    if (filter == NULL) {
        h5outOfMemory(env, "H5Pget_filter:  namelent malloc failed");
        return -1;
    }
    flagsArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR flags, &isCopy);
    if (flagsArray == NULL) {
        free(filter);
        h5JNIFatalError(env, "H5Pget_filter:  flags array not pinned");
        return -1;
    }
    cd_nelmtsArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR cd_nelmts, &isCopy);
    if (cd_nelmtsArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5JNIFatalError(env, "H5Pget_filter:  nelmts array not pinned");
        return -1;
    }
    cd_valuesArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR cd_values, &isCopy);
    if (cd_valuesArray == NULL)  {
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5JNIFatalError(env, "H5Pget_filter:  elmts array not pinned");
        return -1;
    }

    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long cd_nelmts_temp = *(cd_nelmtsArray);
        size_t cd_nelmts_t = cd_nelmts_temp;
        unsigned int filter_config;
        status = H5Pget_filter2((hid_t)plist, (int)filter_number, 
            (unsigned int *)flagsArray, &cd_nelmts_t, (unsigned int *)cd_valuesArray,
            (size_t)namelen, filter, &filter_config);
        
        *cd_nelmtsArray = cd_nelmts_t;
    }

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, 0);
    ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, 0);
    /*  NewStringUTF may throw OutOfMemoryError */
    str = ENVPTR->NewStringUTF(ENVPAR filter);
    if (str == NULL) {
        free(filter);
        h5JNIFatalError(env, "H5Pget_filter:  return string not pinned");
        return -1;
    }
    /*  SetObjectArrayElement may throw exceptiosn */
    ENVPTR->SetObjectArrayElement(ENVPAR name, 0, (jobject)str);
    free(filter);
    
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_driver
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1driver
  (JNIEnv *env, jclass clss, jint plist)
{
    hid_t retVal =  -1;
    
    retVal = H5Pget_driver((hid_t) plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_cache
 * Signature: (IIJJD)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1cache
  (JNIEnv *env, jclass clss, jint plist, jint mdc_nelmts, jlong rdcc_nelmts,
  jlong rdcc_nbytes, jdouble rdcc_w0)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_cache((hid_t)plist, (int)mdc_nelmts, (size_t)rdcc_nelmts,
                (size_t)rdcc_nbytes, (double) rdcc_w0);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_cache
 * Signature: (I[I[J[J[D)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1cache
  (JNIEnv *env, jclass clss, jint plist, jintArray mdc_nelmts,
  jlongArray rdcc_nelmts, jlongArray rdcc_nbytes, jdoubleArray rdcc_w0)
{
    herr_t   status;
    jint     mode;
    jdouble *w0Array;
    jint    *mdc_nelmtsArray;
    jlong   *rdcc_nelmtsArray;
    jlong   *nbytesArray;
    jboolean isCopy;

    if (mdc_nelmts == NULL) {
        h5nullArgument(env, "H5Pget_gache:  mdc_nelmts is NULL");
        return -1;
    }
    mdc_nelmtsArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR mdc_nelmts, &isCopy);
    if (mdc_nelmtsArray == NULL) {
        h5JNIFatalError(env, "H5Pget_cache:  mdc_nelmts array not pinned");
        return -1;
    }

    if (rdcc_w0 == NULL) {
        w0Array = (jdouble *)NULL;
    } 
    else {
        w0Array = (jdouble *)ENVPTR->GetDoubleArrayElements(ENVPAR rdcc_w0, &isCopy);
        if (w0Array == NULL) {
            ENVPTR->ReleaseIntArrayElements(ENVPAR mdc_nelmts, mdc_nelmtsArray, JNI_ABORT);
            h5JNIFatalError(env, "H5Pget_cache:  w0_array array not pinned");
            return -1;
        }
    }

    if (rdcc_nelmts == NULL) {
        rdcc_nelmtsArray = (jlong *) NULL;
    } 
    else {
        rdcc_nelmtsArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR rdcc_nelmts, &isCopy);
        if (rdcc_nelmtsArray == NULL) {
            ENVPTR->ReleaseIntArrayElements(ENVPAR mdc_nelmts, mdc_nelmtsArray, JNI_ABORT);
            /* exception -- out of memory */
            if (w0Array != NULL) {
                ENVPTR->ReleaseDoubleArrayElements(ENVPAR rdcc_w0, w0Array, JNI_ABORT);
            }
            h5JNIFatalError(env, "H5Pget_cache:  rdcc_nelmts array not pinned");
            return -1;
        }
    }

    if (rdcc_nbytes == NULL) {
        nbytesArray = (jlong *) NULL;
    } 
    else {
        nbytesArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR rdcc_nbytes, &isCopy);
        if (nbytesArray == NULL) {
            ENVPTR->ReleaseIntArrayElements(ENVPAR mdc_nelmts, mdc_nelmtsArray, JNI_ABORT);
            if (w0Array != NULL) {
                ENVPTR->ReleaseDoubleArrayElements(ENVPAR rdcc_w0, w0Array, JNI_ABORT);
            }
            if (rdcc_nelmtsArray != NULL) {
                ENVPTR->ReleaseLongArrayElements(ENVPAR rdcc_nelmts, rdcc_nelmtsArray, JNI_ABORT);
            }
            h5JNIFatalError(env, "H5Pget_cache:  nbytesArray array not pinned");
            return -1;
        }
    }
    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long rdcc_nelmts_temp = *(rdcc_nelmtsArray);
        size_t rdcc_nelmts_t = rdcc_nelmts_temp;
        long nbytes_temp = *(nbytesArray);
        size_t nbytes_t = nbytes_temp;

        status = H5Pget_cache((hid_t)plist, (int *)mdc_nelmtsArray, &rdcc_nelmts_t, 
                &nbytes_t, (double *)w0Array);
        
        *rdcc_nelmtsArray = rdcc_nelmts_t;
        *nbytesArray = nbytes_t;
    }


    if (status < 0) {
        mode = JNI_ABORT;
    } 
    else {
        mode = 0; /* commit and free */
    }

    ENVPTR->ReleaseIntArrayElements(ENVPAR mdc_nelmts, mdc_nelmtsArray, mode);

    if (rdcc_nelmtsArray != NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR rdcc_nelmts, rdcc_nelmtsArray, mode);
    }

    if (nbytesArray != NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR rdcc_nbytes, nbytesArray, mode);
    }

    if (w0Array != NULL) {
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR rdcc_w0, w0Array, mode);
    }

    if (status < 0) {
        h5libraryError(env);
    }
    
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_buffer
 * Signature: (IJ[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1buffer
  (JNIEnv *env, jclass clss, jint plist, jlong size, jbyteArray tconv, jbyteArray bkg)
{
    h5JNIFatalError(env, "H5Pset_buffer:  not implemented");
    return -1;
#ifdef notdef

/* DON'T IMPLEMENT THIS!!! */
    jint     status = -1;
    jbyte   *tconvP;
    jbyte   *bkgP;
    jboolean isCopy;

    if (tconv == NULL)
        tconvP = (jbyte *)NULL;
    else {
        tconvP = ENVPTR->GetByteArrayElements(ENVPAR tconv, &isCopy);
        if (tconvP == NULL) {
            h5JNIFatalError(env, "H5Pset_buffer:  tconv not pinned");
            return -1;
        }
    }
    if (bkg == NULL)
        bkgP = (jbyte *)NULL;
    else {
        bkgP = ENVPTR->GetByteArrayElements(ENVPAR bkg, &isCopy);
        if (bkgP == NULL) {
            h5JNIFatalError(env, "H5Pset_buffer:  bkg not pinned");
            return -1;
        }
    }

    status = H5Pset_buffer((hid_t)plist, (size_t)size, tconvP, bkgP);
    if (status < 0) {
        if (tconv != NULL)
            ENVPTR->ReleaseByteArrayElements(ENVPAR tconv, tconvP, JNI_ABORT);
        if (bkg != NULL)
            ENVPTR->ReleaseByteArrayElements(ENVPAR bkg, bkgP, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }

    if (tconv != NULL)
        ENVPTR->ReleaseByteArrayElements(ENVPAR tconv, tconvP, 0);
    if (bkg != NULL)
        ENVPTR->ReleaseByteArrayElements(ENVPAR bkg, bkgP, 0);

    return status;
#endif
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_buffer
 * Signature: (I[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1buffer
  (JNIEnv *env, jclass clss, jint plist, jbyteArray tconv, jbyteArray bkg)
{
    h5JNIFatalError(env, "H5Pset_buffer:  not implemented");
    return -1;
#ifdef notdef

/* DON'T IMPLEMENT THIS!!! */
    jint     status = -1;
    jbyte   *tconvP;
    jbyte   *bkgP;
    jboolean isCopy;

    if (tconv == NULL) {
        h5nullArgument(env, "H5Pget_buffer:  tconv input array is NULL");
        return -1;
    }
    tconvP = ENVPTR->GetByteArrayElements(ENVPAR tconv, &isCopy);
    if (tconvP == NULL) {
        h5JNIFatalError(env, "H5Pget_buffer:  tconv not pinned");
        return -1;
    }
    if (bkg == NULL) {
        h5nullArgument(env, "H5Pget_buffer:  bkg array is NULL");
        return -1;
    }
    bkgP = ENVPTR->GetByteArrayElements(ENVPAR bkg, &isCopy);
    if (bkgP == NULL) {
        h5JNIFatalError(env, "H5Pget_buffer:  bkg not pinned");
        return -1;
    }
    
    status = H5Pget_buffer((hid_t)plist, tconvP, bkgP);
    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR tconv, tconvP, JNI_ABORT);
        ENVPTR->ReleaseByteArrayElements(ENVPAR bkg, bkgP, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseByteArrayElements(ENVPAR tconv, tconvP, 0);
    ENVPTR->ReleaseByteArrayElements(ENVPAR bkg, bkgP, 0);

    return status;
#endif
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_preserve
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1preserve
  (JNIEnv *env, jclass clss, jint plist, jboolean status)
{
    hbool_t st;
    herr_t  retVal = -1;

    if (status == JNI_TRUE) {
        st = TRUE;
    } 
    else if (status == JNI_FALSE) {
        st = FALSE;
    }
    else {
        /* exception -- bad argument */
        h5badArgument(env, "H5Pset_preserve:  status not TRUE or FALSE");
        return -1;
    }
    
    retVal = H5Pset_preserve((hid_t)plist, (hbool_t)st);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_preserve
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1preserve
  (JNIEnv *env, jclass clss, jint plist)
{
    herr_t retValue = -1;
    
    retValue = H5Pget_preserve((hid_t)plist);
    if (retValue < 0) {
        h5libraryError(env);
    }

    return (jint)retValue;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_deflate
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1deflate
  (JNIEnv *env, jclass clss, jint plist, jint level)
{
    herr_t retValue = -1;
    
    retValue = H5Pset_deflate((hid_t)plist, (int)level);
    if (retValue < 0) {
        h5libraryError(env);
    }

    return (jint)retValue;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_gc_references
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1gc_1references
  (JNIEnv *env, jclass clss, jint fapl_id, jboolean gc_ref)
  {
    herr_t   retVal = -1;
    unsigned gc_ref_val;
    
    if (gc_ref == JNI_TRUE) {
        gc_ref_val = 1;
    } 
    else {
        gc_ref_val = 0;
    }
    
    retVal = H5Pset_gc_references((hid_t)fapl_id, gc_ref_val);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_gc_references
 * Signature: (I[Z)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1gc_1references
  (JNIEnv *env, jclass clss, jint fapl_id, jbooleanArray gc_ref)
{
    herr_t    status;
    jboolean *theArray;
    jboolean  isCopy;
    unsigned  gc_ref_val = 0;

    if (gc_ref == NULL) {
        h5nullArgument(env, "H5Pget_gc_references:  gc_ref input array is NULL");
        return -1;
    }
    theArray = (jboolean *)ENVPTR->GetBooleanArrayElements(ENVPAR gc_ref, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_gc_references:  gc_ref not pinned");
        return -1;
    }

    status = H5Pget_gc_references((hid_t)fapl_id, (unsigned *)&gc_ref_val);
    if (status < 0) {
        ENVPTR->ReleaseBooleanArrayElements(ENVPAR gc_ref, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    if (gc_ref_val == 1) {
        theArray[0] = JNI_TRUE;
    } 
    else {
        theArray[0] = JNI_FALSE;
    }
    ENVPTR->ReleaseBooleanArrayElements(ENVPAR gc_ref, theArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_btree_ratios
 * Signature: (IDDD)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1btree_1ratios
  (JNIEnv *env, jclass clss, jint plist_id, jdouble left, jdouble middle, jdouble right)
{
    herr_t status;

    status = H5Pset_btree_ratios((hid_t)plist_id, (double)left,(double)middle, (double)right);
    if (status < 0) {
        h5libraryError(env);
        return -1;
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_btree_ratios
 * Signature: (I[D[D[D)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1btree_1ratios
  (JNIEnv *env, jclass clss, jint plist_id, jdoubleArray left, jdoubleArray middle, jdoubleArray right)
{
    herr_t   status;
    jdouble *leftP;
    jdouble *middleP;
    jdouble *rightP;
    jboolean isCopy;

    if (left == NULL) {
        h5nullArgument(env, "H5Pget_btree_ratios:  left input array is NULL");
        return -1;
    }
    if (middle == NULL) {
        h5nullArgument(env, "H5Pget_btree_ratios:  middle input array is NULL");
        return -1;
    }
    if (right == NULL) {
        h5nullArgument(env, "H5Pget_btree_ratios:  right input array is NULL");
        return -1;
    }
    
    leftP = (jdouble *)ENVPTR->GetDoubleArrayElements(ENVPAR left, &isCopy);
    if (leftP == NULL) {
        h5JNIFatalError(env, "H5Pget_btree_ratios:  left not pinned");
        return -1;
    }
    middleP = (jdouble *)ENVPTR->GetDoubleArrayElements(ENVPAR middle, &isCopy);
    if (middleP == NULL) {
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR left, leftP, JNI_ABORT);
        h5JNIFatalError(env, "H5Pget_btree_ratios:  middle not pinned");
        return -1;
    }
    rightP = (jdouble *)ENVPTR->GetDoubleArrayElements(ENVPAR right, &isCopy);
    if (rightP == NULL) {
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR left, leftP, JNI_ABORT);
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR middle, middleP, JNI_ABORT);
        h5JNIFatalError(env, "H5Pget_btree_ratios:  middle not pinned");
        return -1;
    }

    status = H5Pget_btree_ratios((hid_t)plist_id, (double *)leftP,
             (double *)middleP, (double *)rightP);
    if (status < 0) {
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR left, leftP, JNI_ABORT);
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR middle, middleP, JNI_ABORT);
        ENVPTR->ReleaseDoubleArrayElements(ENVPAR right, rightP, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    ENVPTR->ReleaseDoubleArrayElements(ENVPAR left, leftP, 0);
    ENVPTR->ReleaseDoubleArrayElements(ENVPAR middle, middleP, 0);
    ENVPTR->ReleaseDoubleArrayElements(ENVPAR right, rightP, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_small_data_block_size
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1small_1data_1block_1size
  (JNIEnv *env, jclass clss, jint plist, jlong size)
{
    long   sz;
    herr_t retVal = -1;
    
    sz = (long)size;
    
    retVal = H5Pset_small_data_block_size((hid_t)plist, (hsize_t)sz);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_small_data_block_size
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1small_1data_1block_1size
  (JNIEnv *env, jclass clss, jint plist, jlongArray size)
{
    herr_t   status;
    jlong   *theArray;
    jboolean isCopy;
    hsize_t  s;

    if (size == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pget_small_user_block_size:  size is NULL");
        return -1;
    }
    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_userblock:  size not pinned");
        return -1;
    }

    status = H5Pget_small_data_block_size((hid_t)plist, &s);
    if (status < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    theArray[0] = s;
    ENVPTR->ReleaseLongArrayElements(ENVPAR size, theArray, 0);

    return (jint)status;
}


/***************************************************************
 *                   New APIs for HDF5.1.6                     *
 ***************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_alloc_time
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1alloc_1time
  (JNIEnv *env, jclass clss, jint plist, jint alloc_time)
{
    herr_t retVal = -1;

    retVal = H5Pset_alloc_time((hid_t)plist, (H5D_alloc_time_t)alloc_time);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_alloc_time
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1alloc_1time
  (JNIEnv *env, jclass clss, jint plist, jintArray alloc_time)
{
    herr_t           retVal = -1;
    jint            *theArray;
    jboolean         isCopy;
    H5D_alloc_time_t time;

    if (alloc_time == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pget_alloc_time:  alloc_time is NULL");
        return -1;
    }
    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR alloc_time, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_alloc_time:  alloc_time not pinned");
        return -1;
    }

    retVal =  H5Pget_alloc_time((hid_t)plist, &time );
    if (retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR alloc_time, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    theArray[0] = time;
    ENVPTR->ReleaseIntArrayElements(ENVPAR alloc_time, theArray, 0);

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fill_time
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fill_1time
  (JNIEnv *env, jclass clss, jint plist, jint fill_time)
{
    herr_t retVal = -1;

    retVal = H5Pset_fill_time((hid_t)plist, (H5D_fill_time_t)fill_time);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fill_time
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fill_1time
  (JNIEnv *env, jclass clss, jint plist, jintArray fill_time)
{
    herr_t          retVal = -1;
    jint           *theArray;
    jboolean        isCopy;
    H5D_fill_time_t time;

    if (fill_time == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pget_fill_time:  fill_time is NULL");
        return -1;
    }
    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR fill_time, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_fill_time:  fill_time not pinned");
        return -1;
    }

    retVal = H5Pget_fill_time((hid_t)plist, &time);
    if (retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR fill_time, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    theArray[0] = time;
    ENVPTR->ReleaseIntArrayElements(ENVPAR fill_time, theArray, 0);

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pfill_value_defined
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pfill_1value_1defined
  (JNIEnv *env, jclass clss, jint plist, jintArray status)
{
    herr_t retVal = -1;
    jint *theArray;
    jboolean isCopy;
    H5D_fill_value_t value;


    if (status == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pfill_value_defined:  status is NULL");
        return -1;
    }
    theArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR status, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pfill_value_defined:  status not pinned");
        return -1;
    }

    retVal = H5Pfill_value_defined((hid_t)plist, &value );
    if (retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR status, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    theArray[0] = value;
    ENVPTR->ReleaseIntArrayElements(ENVPAR status, theArray, 0);

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fletcher32
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fletcher32
  (JNIEnv *env, jclass clss, jint plist)
{
    herr_t retVal = -1;

    retVal = H5Pset_fletcher32((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_edc_check
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1edc_1check
  (JNIEnv *env, jclass clss, jint plist, jint check)
{
    herr_t retVal = -1;

    retVal = H5Pset_edc_check((hid_t)plist, (H5Z_EDC_t)check);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_edc_check
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1edc_1check
  (JNIEnv *env, jclass clss, jint plist)
{
    H5Z_EDC_t retVal = (H5Z_EDC_t)-1;

    retVal = H5Pget_edc_check((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_shuffle
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1shuffle
  (JNIEnv *env, jclass clss, jint plist)
{
    herr_t retVal = -1;

    retVal = H5Pset_shuffle((hid_t)plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_szip
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1szip
  (JNIEnv *env, jclass clss, jint plist, jint options_mask, jint pixels_per_block)
{
    herr_t retVal = -1;

    retVal = H5Pset_szip((hid_t)plist, (unsigned int)options_mask, (unsigned int)pixels_per_block);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_hyper_vector_size
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1hyper_1vector_1size
  (JNIEnv *env, jclass clss, jint plist, jlong vector_size)
{
    herr_t retVal = -1;

    retVal = H5Pset_hyper_vector_size((hid_t)plist, (size_t)vector_size);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_hyper_vector_size
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1hyper_1vector_1size
  (JNIEnv *env, jclass clss, jint plist, jlongArray vector_size)
{
    herr_t   retVal = -1;
    jlong   *theArray;
    size_t   size;
    jboolean isCopy;

    if (vector_size == NULL) {
        /* exception ? */
        h5nullArgument(env, "H5Pget_hyper_vector_size:  vector_size is NULL");
        return -1;
    }

    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR vector_size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_hyper_vector_size:  vector_size not pinned");
        return -1;
    }

    retVal =  H5Pget_hyper_vector_size((hid_t)plist, &size);
    if (retVal < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR vector_size, theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    } 
    theArray[0] = size;
    ENVPTR->ReleaseLongArrayElements(ENVPAR vector_size, theArray, 0);

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pall_filters_avail
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pall_1filters_1avail
  (JNIEnv *env, jclass clss, jint dcpl_id)
{
    htri_t bval;

    bval = H5Pall_filters_avail((hid_t)dcpl_id);
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
 * Method:    H5Pmodify_filter
 * Signature: (IIIJ[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pmodify_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter, jint flags,
  jlong cd_nelmts, jintArray cd_values)
{
    herr_t   status;
    jint    *cd_valuesP;
    jboolean isCopy;

    if (cd_values == NULL) {
        h5nullArgument(env, "H5Pmodify_filter:  cd_values is NULL");
        return -1;
    }

    cd_valuesP = ENVPTR->GetIntArrayElements(ENVPAR cd_values,&isCopy);
    if (cd_valuesP == NULL) {
        h5JNIFatalError(env, "H5Pmodify_filter:  cd_values not pinned");
        return -1;
    }

    status = H5Pmodify_filter((hid_t)plist, (H5Z_filter_t)filter,(const unsigned int)flags,
              (size_t)cd_nelmts, (unsigned int *)cd_valuesP);

    ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesP, 0);

    if (status < 0) {
        h5libraryError(env);
    }
    
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter_by_id
 * Signature: (II[I[J[IJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter_1by_1id
  (JNIEnv *env, jclass clss, jint plist, jint filter, jintArray flags,
  jlongArray cd_nelmts, jintArray cd_values, jlong namelen, jobjectArray name)
{
    herr_t       status;
    jint        *cd_valuesArray;
    jint        *flagsArray;
    jlong       *cd_nelmtsArray;
    jboolean     isCopy;
    jstring      str;
    char        *aName;
    int          i = 0;
    int          rank;
    long         bs;

    bs = (long)namelen;
    if (bs <= 0) {
        h5badArgument(env, "H5Pget_filter_by_id:  namelen <= 0");
        return -1;
    }
    if (flags == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  flags is NULL");
        return -1;
    }
    if (cd_nelmts == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  cd_nelms is NULL");
        return -1;
    }
    if (cd_values == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  cd_values is NULL");
        return -1;
    }
    if (name == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  name is NULL");
        return -1;
    }

    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        h5outOfMemory(env, "H5Pget_filter_by_id:  malloc failed");
        return -1;
    }

    flagsArray = ENVPTR->GetIntArrayElements(ENVPAR flags, &isCopy);
    if (flagsArray == NULL) {
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  flags not pinned");
        return -1;
    }

    cd_nelmtsArray = ENVPTR->GetLongArrayElements(ENVPAR cd_nelmts, &isCopy);
    if (cd_nelmtsArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  cd_nelms not pinned");
        return -1;
    }

    cd_valuesArray = ENVPTR->GetIntArrayElements(ENVPAR cd_values, &isCopy);
    rank = ENVPTR->GetArrayLength(ENVPAR cd_values);
    if (cd_valuesArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  cd_values array not converted to unsigned int.");
        return -1;
    }

    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long cd_nelmts_temp = *(cd_nelmtsArray);
        size_t cd_nelmts_t = cd_nelmts_temp;
        unsigned int filter_config;

        status = H5Pget_filter_by_id2( (hid_t)plist, (H5Z_filter_t)filter,
                (unsigned int *)flagsArray, &cd_nelmts_t, (unsigned int *)cd_valuesArray,
                (size_t)namelen, (char *)aName, &filter_config);
        
        *cd_nelmtsArray = cd_nelmts_t;
    }

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        free(aName);
        h5libraryError(env);
        return -1;
    } 

    str = ENVPTR->NewStringUTF(ENVPAR aName);
    ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, 0);
    ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, 0);

    free(aName);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fclose_degree
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fclose_1degree
  (JNIEnv *env, jclass clss, jint plist, jint fc_degree)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_fclose_degree((hid_t)plist, (H5F_close_degree_t)fc_degree);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fclose_degree
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fclose_1degree
  (JNIEnv *env, jclass clss, jint plist)
{
    H5F_close_degree_t degree;
    herr_t             retVal = -1;
    
    retVal = H5Pget_fclose_degree((hid_t)plist, &degree);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)degree;
}


/**********************************************************************
 *                                                                    *
 *                    File access properties                          *
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_family
 * Signature: (IJI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1family
  (JNIEnv *env, jclass clss, jint plist, jlong memb_size, jint memb_plist)
{
    long   ms;
    herr_t retVal = -1;
    
    ms = (long)memb_size;
    retVal = H5Pset_fapl_family((hid_t)plist, (hsize_t)ms, (hid_t)memb_plist);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fapl_family
 * Signature: (I[J[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fapl_1family
  (JNIEnv *env, jclass clss, jint tid, jlongArray memb_size, jintArray memb_plist)
{
    herr_t   status;
    jlong   *sizeArray;
    jint    *plistArray;
    jboolean isCopy;
    hsize_t *sa;
    int      i;
    int      rank;

    if (memb_size == NULL) {
        h5nullArgument(env, "H5Pget_family:  memb_size is NULL");
        return -1;
    }
    if (memb_plist == NULL) {
        h5nullArgument(env, "H5Pget_family:  memb_plist is NULL");
        return -1;
    }
    
    sizeArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR memb_size, &isCopy);
    if (sizeArray == NULL) {
        h5JNIFatalError(env,  "H5Pget_family:  sizeArray not pinned");
        return -1;
    }
    rank  = ENVPTR->GetArrayLength(ENVPAR  memb_size);
    sa = (hsize_t *)malloc( rank * sizeof(hsize_t));
    if (sa == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR memb_size, sizeArray, JNI_ABORT);
        h5JNIFatalError(env,  "H5Screate-simple:  dims not converted to hsize_t");
        return -1;
    }
    plistArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR memb_plist, &isCopy);
    if (plistArray == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR memb_size, sizeArray, JNI_ABORT);
        h5JNIFatalError(env,  "H5Pget_family:  plistArray not pinned");
        return -1;
    }
    
    status = H5Pget_fapl_family ((hid_t)tid, sa, (hid_t *)plistArray);

    if (status < 0) {
        free(sa);
        ENVPTR->ReleaseLongArrayElements(ENVPAR memb_size, sizeArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR memb_plist, plistArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    for (i = 0; i < rank; i++) {
        sizeArray[i] = sa[i];
    }
    free(sa);
    ENVPTR->ReleaseLongArrayElements(ENVPAR memb_size, sizeArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR memb_plist, plistArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_core
 * Signature: (IJZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1core
  (JNIEnv *env, jclass clss, jint fapl_id, jlong increment, jboolean backing_store)
{
    herr_t retVal = -1;
    
    retVal =  H5Pset_fapl_core((hid_t)fapl_id, (size_t)increment, (hbool_t)backing_store);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fapl_core
 * Signature: (I[J[Z)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fapl_1core
  (JNIEnv *env, jclass clss, jint fapl_id, jlongArray increment, jbooleanArray backing_store)
{
    herr_t    status;
    jlong    *incArray;
    jboolean *backArray;
    jboolean  isCopy;

    if (increment == NULL) {
        h5nullArgument(env, "H5Pget_fapl_core:  increment is NULL");
        return -1;
    }
    if (backing_store == NULL) {
        h5nullArgument(env, "H5Pget_fapl_core:  backing_store is NULL");
        return -1;
    }

    incArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR increment, &isCopy);
    if (incArray == NULL) {
        h5JNIFatalError(env,  "H5Pget_fapl_core:  incArray not pinned");
        return -1;
    }

    backArray = (jboolean *)ENVPTR->GetBooleanArrayElements(ENVPAR backing_store, &isCopy);
    if (backArray == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR increment, incArray, JNI_ABORT);
        h5JNIFatalError(env, "H5Pget_fapl_core:  backArray not pinned");
        return -1;
    }

    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long inc_temp = *(incArray);
        size_t inc_t = inc_temp;

        status = H5Pget_fapl_core((hid_t)fapl_id, &inc_t, (hbool_t *)backArray);
        
        *incArray = inc_t;
    }
    
    if (status < 0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR increment, incArray, JNI_ABORT);
        ENVPTR->ReleaseBooleanArrayElements(ENVPAR backing_store, backArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseLongArrayElements(ENVPAR increment, incArray, 0);
    ENVPTR->ReleaseBooleanArrayElements(ENVPAR backing_store, backArray, 0);

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_family_offset
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1family_1offset
  (JNIEnv *env, jclass clss, jint fapl_id, jlong offset)
{
    herr_t retVal = -1;
    
    retVal =  H5Pset_family_offset ((hid_t)fapl_id, (hsize_t)offset);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_family_offset
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1family_1offset
  (JNIEnv *env, jclass clss, jint fapl_id)
{
    hsize_t offset = -1;
    herr_t  retVal = -1;
    
    retVal = H5Pget_family_offset ((hid_t)fapl_id, &offset);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jlong)offset;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_log
 * Signature: (ILjava/lang/String;JJ)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1log
  (JNIEnv *env, jclass clss, jint fapl_id, jstring logfile, jlong flags, jlong buf_size)
{
    herr_t   retVal = -1;
    char    *pLogfile;
    jboolean isCopy;

    if (logfile == NULL) {
        h5nullArgument(env, "H5Pset_fapl_log:  logfile is NULL");
        return;
    }

    pLogfile = (char *)ENVPTR->GetStringUTFChars(ENVPAR logfile, &isCopy);

    if (pLogfile == NULL) {
        h5JNIFatalError(env, "H5Pset_fapl_log:  logfile not pinned");
        return;
    }

#if (H5_VERS_RELEASE > 6) /* H5_VERSION_GE(1,8,7) */
    retVal = H5Pset_fapl_log( (hid_t)fapl_id, (const char *)pLogfile, (unsigned long long)flags, (size_t)buf_size );
#else
    retVal = H5Pset_fapl_log( (hid_t)fapl_id, (const char *)pLogfile, (unsigned int)flags, (size_t)buf_size );
#endif
    if (retVal < 0) {
        h5libraryError(env);
    }

    ENVPTR->ReleaseStringUTFChars(ENVPAR logfile, pLogfile);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return;
}


/**********************************************************************
 *                                                                    *
 *          New functions release 1.6.3 versus release 1.6.2          *
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Premove_filter
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5P1remove_1filter
  (JNIEnv *env, jclass clss, jint obj_id, jint filter)
{
    herr_t status;

    status = H5Premove_filter ((hid_t)obj_id, (H5Z_filter_t)filter);

    if (status < 0) {
        h5libraryError(env);
    }

    return status;
}


/**********************************************************************
 *                                                                    *
    Modified by Peter Cao on July 26, 2006:                            
        Some of the Generic Property APIs have callback function 
        pointers, which Java does not support. Only the Generic 
        Property APIs without function pointers are implemented
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset
  (JNIEnv *env, jclass clss, jint plid, jstring name, jint val)
{
    char    *cstr;
    jboolean isCopy;    
    hid_t    retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Pset: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Pset: name not pinned");
        return -1;
    }

    retVal =  H5Pset((hid_t)plid, cstr, &val);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pexist
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pexist
  (JNIEnv *env, jclass clss, jint plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;    
    hid_t    retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Pexist: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Pexist: name not pinned");
        return -1;
    }

    retVal = H5Pexist((hid_t)plid, cstr);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_size
 * Signature: (ILjava/lang/String;)J 
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1size
  (JNIEnv *env, jclass clss, jint plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;    
    hid_t    retVal = -1;
    size_t   size;

    if (name == NULL) {
        h5nullArgument( env, "H5Pget_size: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError( env, "H5Pget_size: name not pinned");
        return -1;
    }

    retVal = H5Pget_size((hid_t)plid, cstr, &size);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong) size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_nprops
 * Signature: (I)J  
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1nprops
  (JNIEnv *env, jclass clss, jint plid)
{
    hid_t  retVal = -1;
    size_t nprops;

    retVal = H5Pget_nprops((hid_t)plid, &nprops);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong)nprops;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_class_name
 * Signature: (I)Ljava/lang/String; 
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1class_1name
  (JNIEnv *env, jclass clss, jint plid)
{
    char   *c_str;
    jstring j_str;

    c_str = H5Pget_class_name((hid_t)plid);
    if (c_str == NULL) {
        h5libraryError(env);
        return NULL;
    }

    j_str = ENVPTR->NewStringUTF(ENVPAR c_str);
    if (j_str == NULL) {
        h5JNIFatalError(env,"H5Pget_class_name: return string failed");
    }

    return j_str;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_class_parent
 * Signature: (I)I   
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1class_1parent
  (JNIEnv *env, jclass clss, jint plid)
{
    hid_t retVal = -1;

    retVal = H5Pget_class_parent((hid_t)plid);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pisa_class
 * Signature: (II)I    
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pisa_1class
  (JNIEnv *env, jclass clss, jint plid, jint pcls)
{
    htri_t retVal = -1;

    retVal = H5Pisa_class((hid_t)plid, (hid_t)pcls);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget
  (JNIEnv *env, jclass clss, jint plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;
    jint     val;    
    jint     retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Pget: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Pget: name not pinned");
        return -1;
    }

    retVal = H5Pget((hid_t)plid, cstr, &val);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)val;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pequal
 * Signature: (II)I    
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pequal
  (JNIEnv *env, jclass clss, jint plid1, jint plid2)
{
    htri_t retVal = -1;

    retVal = H5Pequal((hid_t)plid1, (hid_t)plid2);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pcopy_prop
 * Signature: (IILjava/lang/String;)I 
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pcopy_1prop
  (JNIEnv *env, jclass clss, jint dst_plid, jint src_plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;
    jint     retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Pcopy_prop: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Pcopy_prop: name not pinned");
        return -1;
    }

    retVal = H5Pcopy_prop((hid_t)dst_plid, (hid_t)src_plid, cstr);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Premove
 * Signature: (ILjava/lang/String;)I 
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Premove
  (JNIEnv *env, jclass clss, jint plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;
    jint     retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Premove: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Premove: name not pinned");
        return -1;
    }

    retVal = H5Premove((hid_t)plid, cstr);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Punregister
 * Signature: (ILjava/lang/String;)I  
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Punregister
  (JNIEnv *env, jclass clss, jint plid, jstring name)
{
    char    *cstr;
    jboolean isCopy;
    jint     retVal = -1;

    if (name == NULL) {
        h5nullArgument(env, "H5Punregister: name is NULL");
        return -1;
    }

    cstr = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (cstr == NULL) {
        h5JNIFatalError(env, "H5Punregister: name not pinned");
        return -1;
    }

    retVal = H5Punregister((hid_t)plid, cstr);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name, cstr);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pclose_list
 * Signature: (I)I   
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pclose_1class
  (JNIEnv *env, jclass clss, jint plid)
{
    hid_t retVal = -1;

    retVal = H5Pclose_class((hid_t)plid);
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter2
 * Signature: (II[I[J[IJ[Ljava/lang/String;[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter2
  (JNIEnv *env, jclass clss, jint plist, jint filter_number, jintArray flags,
  jlongArray cd_nelmts, jintArray cd_values, jlong namelen, jobjectArray name, 
  jintArray filter_config)
{
    herr_t   status;
    jint    *flagsArray;
    jlong   *cd_nelmtsArray;
    jint    *cd_valuesArray;
    jint    *filter_configArray;
    jboolean isCopy;
    char    *filter;
    jstring  str;

    if (namelen <= 0) {
        h5badArgument(env, "H5Pget_filter:  namelen <= 0");
        return -1;
    }
    if (flags == NULL) {
        h5badArgument(env, "H5Pget_filter:  flags is NULL");
        return -1;
    }
    if (cd_nelmts == NULL) {
        h5badArgument(env, "H5Pget_filter:  cd_nelmts is NULL");
        return -1;
    }
    if (cd_values == NULL) {
        h5badArgument(env, "H5Pget_filter:  cd_values is NULL");
        return -1;
    }
    if (filter_config == NULL) {
        h5badArgument(env, "H5Pget_filter:  filter_config is NULL");
        return -1;
    }

    filter = (char *)malloc(sizeof(char)*namelen);
    if (filter == NULL) {
        h5outOfMemory(env, "H5Pget_filter:  namelent malloc failed");
        return -1;
    }
    flagsArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR flags, &isCopy);
    if (flagsArray == NULL) {
        free(filter);
        h5JNIFatalError(env,  "H5Pget_filter:  flags array not pinned");
        return -1;
    }
    cd_nelmtsArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR cd_nelmts, &isCopy);
    if (cd_nelmtsArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5JNIFatalError(env,  "H5Pget_filter:  nelmts array not pinned");
        return -1;
    }
    cd_valuesArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR cd_values, &isCopy);
    if (cd_valuesArray == NULL)  {
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5JNIFatalError(env,  "H5Pget_filter:  elmts array not pinned");
        return -1;
    }
    filter_configArray = (jint *)ENVPTR->GetIntArrayElements(ENVPAR filter_config, &isCopy);
    if (filter_configArray == NULL) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        free(filter);
        h5JNIFatalError(env,  "H5Pget_filter:  filter_config array not pinned");
        return -1;
    }

    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long cd_nelmts_temp = *(cd_nelmtsArray);
        size_t cd_nelmts_t = cd_nelmts_temp;

        status = H5Pget_filter2((hid_t)plist, (int)filter_number, 
            (unsigned int *)flagsArray, &cd_nelmts_t, (unsigned int *)cd_valuesArray,
            (size_t)namelen, filter, (unsigned int *)filter_configArray);
        
        *cd_nelmtsArray = cd_nelmts_t;
    }

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR filter_config, filter_configArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(filter);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseIntArrayElements(ENVPAR filter_config, filter_configArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, 0);
    ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, 0);
    /*  NewStringUTF may throw OutOfMemoryError */
    str = ENVPTR->NewStringUTF(ENVPAR filter);
    if (str == NULL) {
        free(filter);
        h5JNIFatalError(env,  "H5Pget_filter:  return string not pinned");
        return -1;
    }
    /*  SetObjectArrayElement may throw exceptiosn */
    ENVPTR->SetObjectArrayElement(ENVPAR name, 0, (jobject)str);

    free(filter);
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter_by_id2
 * Signature: (II[I[J[IJ[Ljava/lang/String;[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter_1by_1id2
  (JNIEnv *env, jclass clss, jint plist, jint filter, jintArray flags,
  jlongArray cd_nelmts, jintArray cd_values, jlong namelen, jobjectArray name, jintArray filter_config)
{
    herr_t   status;
    int      i = 0;
    jint    *cd_valuesArray;
    jint    *flagsArray;
    jint    *filter_configArray;
    jlong   *cd_nelmtsArray;
    jboolean isCopy;
    long     bs;
    char    *aName;
    jstring  str;

    bs = (long)namelen;
    if (bs <= 0) {
        h5badArgument(env, "H5Pget_filter_by_id:  namelen <= 0");
        return -1;
    }
    if (flags == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  flags is NULL");
        return -1;
    }
    if (cd_nelmts == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  cd_nelms is NULL");
        return -1;
    }
    if (cd_values == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  cd_values is NULL");
        return -1;
    }
    if (name == NULL) {
        h5nullArgument(env, "H5Pget_filter_by_id:  name is NULL");
        return -1;
    }
    if (filter_config == NULL) {
        h5badArgument(env, "H5Pget_filter_by_id:  filter_config is NULL");
        return -1;
    }

    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        h5outOfMemory(env, "H5Pget_filter_by_id:  malloc failed");
        return -1;
    }
    flagsArray = ENVPTR->GetIntArrayElements(ENVPAR flags,&isCopy);
    if (flagsArray == NULL) {
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  flags not pinned");
        return -1;
    }
    cd_nelmtsArray = ENVPTR->GetLongArrayElements(ENVPAR cd_nelmts, &isCopy);
    if (cd_nelmtsArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  cd_nelms not pinned");
        return -1;
    }
    cd_valuesArray = ENVPTR->GetIntArrayElements(ENVPAR cd_values, &isCopy);
    if (cd_valuesArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        free(aName);
        h5JNIFatalError(env, "H5Pget_filter_by_id:  cd_values array not converted to unsigned int.");
        return -1;
    }
    filter_configArray = ENVPTR->GetIntArrayElements(ENVPAR filter_config, &isCopy);
    if (filter_configArray == NULL) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        free(aName);
        h5JNIFatalError(env,  "H5Pget_filter_by_id:  flags not pinned");
        return -1;
    }

    {
        /* direct cast (size_t *)variable fails on 32-bit environment */
        long cd_nelmts_temp = *(cd_nelmtsArray);
        size_t cd_nelmts_t = cd_nelmts_temp;

        status = H5Pget_filter_by_id2((hid_t)plist, (H5Z_filter_t)filter,
            (unsigned int *)flagsArray, &cd_nelmts_t, (unsigned int *)cd_valuesArray,
            (size_t)namelen, (char *)aName, (unsigned int *)filter_configArray);
        
        *cd_nelmtsArray = cd_nelmts_t;
    }

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, JNI_ABORT);
        ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, JNI_ABORT);
        ENVPTR->ReleaseIntArrayElements(ENVPAR filter_config, filter_configArray, JNI_ABORT);
        free(aName);
        h5libraryError(env);
        return -1;
    } 

    str = ENVPTR->NewStringUTF(ENVPAR aName);
    ENVPTR->ReleaseIntArrayElements(ENVPAR flags, flagsArray, 0);
    ENVPTR->ReleaseLongArrayElements(ENVPAR cd_nelmts, cd_nelmtsArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR cd_values, cd_valuesArray, 0);
    ENVPTR->ReleaseIntArrayElements(ENVPAR filter_config, filter_configArray, 0);

    free(aName);

    return (jint)status;
}

/**********************************************************************
 *                                                                    *
 *          New functions release 1.8.0                               *
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_nlinks
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1nlinks
  (JNIEnv *env, jclass clss, jint lapl_id)
{
    herr_t retVal = -1;
    size_t nlinks;

    retVal = H5Pget_nlinks((hid_t)lapl_id, &nlinks); 
    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jlong) nlinks;
}

 /*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_nlinks
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1nlinks
  (JNIEnv *env, jclass clss, jint lapl_id, jlong nlinks)
{
     herr_t retVal = -1;

     if (nlinks <= 0) {
         h5badArgument(env, "H5Pset_1nlinks:  nlinks_l <= 0");
         return -1;
     }

     retVal = H5Pset_nlinks((hid_t)lapl_id, (size_t)nlinks);
     if(retVal < 0) {
         h5libraryError(env);
     }

     return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_libver_bounds
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1libver_1bounds
  (JNIEnv *env, jclass clss, jint fapl_id, jintArray libver)
{
    herr_t        retVal = -1;
    H5F_libver_t *theArray = NULL;
    jboolean      isCopy;

    if (libver == NULL) {
        h5nullArgument(env, "H5Pget_libver_bounds:  libversion bounds is NULL");
        return -1;
    }
    theArray = (H5F_libver_t *)ENVPTR->GetIntArrayElements(ENVPAR libver, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_libver_bounds:  input not pinned");
        return -1;
    }  
    
    retVal = H5Pget_libver_bounds((hid_t)fapl_id, &(theArray[0]), &(theArray[1]));
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR libver, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR libver, (jint *)theArray, 0);

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_libver_bounds
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1libver_1bounds
  (JNIEnv *env, jclass clss, jint fapl_id, jint low, jint high)
{
    herr_t retVal = -1;

    if ((H5F_libver_t)high != H5F_LIBVER_LATEST) {
        h5badArgument(env, "H5Pset_libver_bounds:  invalid high library version bound");
        return -1;
    }

    if(((H5F_libver_t)low !=H5F_LIBVER_EARLIEST) && ((H5F_libver_t)low != H5F_LIBVER_LATEST)) {
        h5badArgument(env, "H5Pset_libver_bounds:  invalid low library version bound");
        return -1;
    }

    retVal = H5Pset_libver_bounds((hid_t)fapl_id, (H5F_libver_t)low, (H5F_libver_t)high);
    if(retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_link_creation_order
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1link_1creation_1order
  (JNIEnv *env, jclass clss, jint gcpl_id)
{
    herr_t   retVal = -1;
    unsigned crt_order_flags;

    retVal = H5Pget_link_creation_order((hid_t)gcpl_id, &crt_order_flags);
    if(retVal < 0) {
        h5libraryError(env);
    }

    return (jint)crt_order_flags;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_link_creation_order
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1link_1creation_1order
  (JNIEnv *env, jclass clss, jint gcpl_id, jint crt_order_flags)
{
    herr_t retVal = -1;

    retVal = H5Pset_link_creation_order((hid_t)gcpl_id, crt_order_flags);
    if(retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_attr_creation_order
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1attr_1creation_1order
  (JNIEnv *env, jclass clss, jint ocpl_id)
{
    herr_t   retVal = -1;
    unsigned crt_order_flags;

    retVal = H5Pget_attr_creation_order((hid_t)ocpl_id, &crt_order_flags);
    if(retVal < 0) {
        h5libraryError(env);
    }

    return (jint)crt_order_flags;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_attr_creation_order
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1attr_1creation_1order
  (JNIEnv *env, jclass clss, jint ocpl_id, jint crt_order_flags)
{
    herr_t retVal = -1;

    retVal = H5Pset_attr_creation_order((hid_t)ocpl_id, crt_order_flags);
    if(retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_copy_object
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1copy_1object
  (JNIEnv *env, jclass clss, jint ocp_plist_id, jint copy_options)
{
    herr_t retVal = -1;

    retVal = H5Pset_copy_object((hid_t)ocp_plist_id, (unsigned)copy_options);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_copy_object
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1copy_1object
  (JNIEnv *env, jclass clss, jint ocp_plist_id)
{
    herr_t   retVal = -1;
    unsigned copy_options;

    retVal = H5Pget_copy_object((hid_t)ocp_plist_id, &copy_options);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)copy_options;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_create_intermediate_group
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1create_1intermediate_1group
  (JNIEnv *env, jclass clss, jint lcpl_id, jboolean crt_intermed_group)
{
    herr_t retVal = -1;

    retVal = H5Pset_create_intermediate_group((hid_t)lcpl_id, (unsigned)crt_intermed_group); 
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_create_intermediate_group
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1create_1intermediate_1group
  (JNIEnv *env, jclass clss, jint lcpl_id)
{
    herr_t retVal = -1;
    unsigned crt_intermed_group;

    retVal = H5Pget_create_intermediate_group((hid_t)lcpl_id, &crt_intermed_group); 
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jboolean)crt_intermed_group;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_data_transform
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1data_1transform
  (JNIEnv *env, jclass clss, jint plist_id, jstring expression)
{
    herr_t    retVal = -1;
    char     *express;
    jboolean  isCopy;

    if (expression == NULL) {
        h5nullArgument(env, "H5Pset_data_transform:  expression is NULL");
        return -1;
    }
    express = (char *)ENVPTR->GetStringUTFChars(ENVPAR expression, &isCopy);
    if (express == NULL) {
        h5JNIFatalError(env, "H5Pset_data_transform: expression is not pinned");
        return -1;
    }
    retVal = H5Pset_data_transform((hid_t)plist_id, (const char*)express);

    ENVPTR->ReleaseStringUTFChars(ENVPAR expression, express);

    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_data_transform
 * Signature: (I[Ljava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1data_1transform
  (JNIEnv *env, jclass clss, jint plist_id, jobjectArray expression, jlong size)
{
    ssize_t  buf_size;
    char    *express;
    jlong    express_size;
    jstring  str = NULL;

    if (size <= 0) {
        h5badArgument(env, "H5Pget_data_transform:  size <= 0");
        return -1;
    }
    express_size = (jlong)H5Pget_data_transform((hid_t)plist_id, (char*)NULL, (size_t)size);
    if(express_size < 0) {
        h5libraryError(env);
        return -1;
    }
    buf_size = (ssize_t)express_size + 1;/* add extra space for the null terminator */
    express = (char*)malloc(sizeof(char)*buf_size);
    if (express == NULL) {
        h5outOfMemory(env, "H5Pget_data_transform:  malloc failed ");
        return -1;
    }

    express_size = (jlong)H5Pget_data_transform((hid_t)plist_id, (char*)express, (size_t)size);
    if (express_size < 0) {
        free(express);
        h5libraryError(env);
        return -1;
    }

    str = ENVPTR->NewStringUTF(ENVPAR express);
    if (str == NULL) {
        /* exception -- fatal JNI error */
        free(express);
        h5JNIFatalError(env, "H5Pget_data_transform:  return string not created");
        return -1;
    }
    ENVPTR->SetObjectArrayElement(ENVPAR expression, 0, str);
    free(express);

    return express_size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_elink_acc_flags
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1elink_1acc_1flags
  (JNIEnv *env, jclass clss, jint lapl_id)
{
    herr_t   status;
    unsigned flags;

    status = H5Pget_elink_acc_flags((hid_t)lapl_id, &flags);
    if(status < 0) {
        h5libraryError(env);
    }
    
    return (jint)flags;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_elink_acc_flags
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1elink_1acc_1flags
  (JNIEnv *env, jclass clss, jint lapl_id, jint flags)
{
    herr_t retVal;

    if (((unsigned) flags != H5F_ACC_RDWR) && 
            ((unsigned) flags != H5F_ACC_RDONLY) && 
            ((unsigned) flags != H5F_ACC_DEFAULT)) {
        h5badArgument(env, "H5Pset_elink_acc_flags: invalid flags value");
        return -1;
    }

    retVal = H5Pset_elink_acc_flags((hid_t)lapl_id, (unsigned)flags);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint) retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_link_phase_change
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1link_1phase_1change
  (JNIEnv *env, jclass clss, jint gcpl_id, jint max_compact, jint min_dense)
{
    herr_t retVal;

    if(max_compact < min_dense) {
        h5badArgument(env, "H5Pset_link_phase_change: max compact value must be >= min dense value");
        return -1;
    }
    if(max_compact > 65535) {
        h5badArgument(env, "H5Pset_link_phase_change: max compact value must be < 65536");
        return -1;
    }
    if(min_dense > 65535) {
        h5badArgument(env, "H5Pset_link_phase_change: min dense value must be < 65536");
        return -1;
    }
    
    retVal = H5Pset_link_phase_change((hid_t)gcpl_id, (unsigned)max_compact, (unsigned)min_dense);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_link_phase_change
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1link_1phase_1change
  (JNIEnv *env, jclass clss, jint gcpl_id, jintArray links)
{
    herr_t    retVal = -1;
    unsigned *theArray = NULL;
    jboolean  isCopy;

    if (links == NULL) {
        h5nullArgument( env, "H5Pget_link_phase_change:  links is NULL");
        return -1;
    }
    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR links, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError( env, "H5Pget_link_phase_change:  input not pinned");
        return -1;
    }  
    
    retVal = H5Pget_link_phase_change((hid_t)gcpl_id, &(theArray[0]), &(theArray[1]));
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR links, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR links, (jint *)theArray, 0);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_attr_phase_change
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1attr_1phase_1change
  (JNIEnv *env, jclass clss, jint ocpl_id, jintArray attributes)
{
    herr_t    retVal = -1;
    unsigned *theArray = NULL;
    jboolean  isCopy;

    if (attributes == NULL) {
        h5nullArgument(env, "H5Pget_attr_phase_change:  attributes is NULL");
        return -1;
    }
    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR attributes, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_attr_phase_change:  input not pinned");
        return -1;
    }  
    
    retVal = H5Pget_attr_phase_change((hid_t)ocpl_id, &(theArray[0]), &(theArray[1]));
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR attributes, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR attributes, (jint *)theArray, 0);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_shared_mesg_phase_change
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1shared_1mesg_1phase_1change
  (JNIEnv *env, jclass clss, jint fcpl_id, jintArray size)
{
    herr_t    retVal = -1;
    unsigned *theArray = NULL;
    jboolean  isCopy;

    if (size == NULL) {
        h5nullArgument(env, "H5Pget_shared_mesg_phase_change:  size is NULL");
        return -1;
    }
    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR size, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_shared_mesg_phase_change:  input not pinned");
        return -1;
    }  
    
    retVal = H5Pget_shared_mesg_phase_change((hid_t)fcpl_id, &(theArray[0]), &(theArray[1]));
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR size, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR size, (jint *)theArray, 0);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_shared_mesg_phase_change
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1shared_1mesg_1phase_1change
  (JNIEnv *env, jclass clss, jint fcpl_id, jint max_list, jint min_btree)
{
    herr_t retVal;

    /* Check that values are sensible.  The min_btree value must be no greater
     * than the max list plus one.
     *
     * Range check to make certain they will fit into encoded form.
     */

    if(max_list + 1 < min_btree) {
        h5badArgument(env, "H5Pset_shared_mesg_phase_change: minimum B-tree value is greater than maximum list value");
        return -1;
    }
    if(max_list > H5O_SHMESG_MAX_LIST_SIZE) {
        h5badArgument(env, "H5Pset_shared_mesg_phase_change: max list value is larger than H5O_SHMESG_MAX_LIST_SIZE");
        return -1;
    }
    if(min_btree > H5O_SHMESG_MAX_LIST_SIZE) {
        h5badArgument(env, "H5Pset_shared_mesg_phase_change: min btree value is larger than H5O_SHMESG_MAX_LIST_SIZE");
        return -1;
    }
    
    retVal = H5Pset_shared_mesg_phase_change((hid_t)fcpl_id, (unsigned)max_list, (unsigned)min_btree);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_shared_mesg_nindexes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1shared_1mesg_1nindexes
  (JNIEnv *env, jclass clss, jint fcpl_id)
{
    herr_t   status;
    unsigned nindexes;

    status = H5Pget_shared_mesg_nindexes((hid_t)fcpl_id, &nindexes); 
    if(status < 0) {
        h5libraryError(env);
    }
    
    return (jint)nindexes;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_shared_mesg_nindexes
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1shared_1mesg_1nindexes
  (JNIEnv *env, jclass clss, jint plist_id, jint nindexes)
{
    herr_t retVal;

    if (nindexes > H5O_SHMESG_MAX_NINDEXES) {
        h5badArgument(env, "H5Pset_shared_mesg_nindexes: number of indexes is greater than H5O_SHMESG_MAX_NINDEXES");
        return -1;
    }  
    
    retVal = H5Pset_shared_mesg_nindexes((hid_t)plist_id, (unsigned)nindexes); 
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_shared_mesg_index
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1shared_1mesg_1index
  (JNIEnv *env, jclass clss, jint fcpl_id, jint index_num, jint mesg_type_flags, jint min_mesg_size)
{  
    herr_t      retVal;
    unsigned    nindexes;/* Number of SOHM indexes */

    /* Check arguments */
    if(mesg_type_flags > H5O_SHMESG_ALL_FLAG) {
        h5badArgument(env, "H5Pset_shared_mesg_index: unrecognized flags in mesg_type_flags");
        return -1;
    }
    /* Read the current number of indexes */
    if(H5Pget_shared_mesg_nindexes((hid_t)fcpl_id, &nindexes) < 0) {
        h5libraryError(env);
    }
    /* Range check */
    if((unsigned)index_num >= nindexes) {
        h5badArgument(env, "H5Pset_shared_mesg_index: index_num is too large; no such index");
        return -1;
    }
    
    retVal = H5Pset_shared_mesg_index((hid_t)fcpl_id, (unsigned)index_num, (unsigned) mesg_type_flags, (unsigned) min_mesg_size);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_shared_mesg_index
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1shared_1mesg_1index
  (JNIEnv *env, jclass clss, jint fcpl_id, jint index_num, jintArray mesg_info)
{
    herr_t    retVal = -1;
    unsigned  nindexes;/* Number of SOHM indexes */
    unsigned *theArray = NULL;
    jboolean  isCopy;

    /* Read the current number of indexes */
    if(H5Pget_shared_mesg_nindexes((hid_t)fcpl_id, &nindexes)<0) {
        h5libraryError(env);
    }
    /* Range check */
    if((unsigned)index_num >= nindexes) {
        h5badArgument(env, "H5Pget_shared_mesg_index: index_num is too large; no such index");
        return -1;
    }
    if (mesg_info == NULL) {
        h5nullArgument(env, "H5Pget_shared_mesg_index:  mesg_info is NULL");
        return -1;
    }
    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR mesg_info, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_shared_mesg_index:  input not pinned");
        return -1;
    }  

    retVal = H5Pget_shared_mesg_index((hid_t)fcpl_id, (unsigned)index_num, &(theArray[0]), &(theArray[1])); 
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR mesg_info, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR mesg_info, (jint *)theArray, 0);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_local_heap_size_hint
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1local_1heap_1size_1hint
  (JNIEnv *env, jclass clss, jint gcpl_id, jlong size_hint)
{
    herr_t retVal = -1;

    retVal = H5Pset_local_heap_size_hint((hid_t)gcpl_id, (size_t)size_hint); 
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_local_heap_size_hint
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1local_1heap_1size_1hint
  (JNIEnv *env, jclass clss, jint gcpl_id)
{ 
    herr_t status;
    size_t size_hint;

    status = H5Pget_local_heap_size_hint((hid_t)gcpl_id, &size_hint); 
    if(status < 0) {
        h5libraryError(env);
    }
    
    return (jlong)size_hint;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_nbit
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1nbit
  (JNIEnv *env, jclass clss, jint plist_id)
{
    herr_t retVal = -1;

    retVal = H5Pset_nbit((hid_t)plist_id);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_scaleoffset
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1scaleoffset
  (JNIEnv *env, jclass clss, jint plist_id, jint scale_type, jint scale_factor)
{
    herr_t retVal = -1;

    /* Check arguments */
    if(scale_factor < 0) {
        h5badArgument(env, "H5Pset_scaleoffset: scale factor must be > 0");
        return -1;
    }
    if(scale_type != H5Z_SO_FLOAT_DSCALE && scale_type != H5Z_SO_FLOAT_ESCALE && scale_type != H5Z_SO_INT){
        h5badArgument(env, "H5Pset_scaleoffset: invalid scale type");
        return -1;
    }
    
    retVal = H5Pset_scaleoffset((hid_t)plist_id, (H5Z_SO_scale_type_t)scale_type, scale_factor);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_est_link_info
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1est_1link_1info
  (JNIEnv *env, jclass clss, jint gcpl_id, jint est_num_entries, jint est_name_len)
{
    herr_t retVal = -1;

    /* Range check values */
    if(est_num_entries > 65535) {
        h5badArgument(env, "H5Pset_est_link_info: est. number of entries must be < 65536");
        return -1;
    }
    if(est_name_len > 65535) {
        h5badArgument(env, "H5Pset_est_link_info: est. name length must be < 65536");
        return -1;
    }
    
    retVal = H5Pset_est_link_info((hid_t)gcpl_id, (unsigned)est_num_entries, (unsigned)est_name_len);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_est_link_info
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1est_1link_1info
  (JNIEnv *env, jclass clss, jint gcpl_id, jintArray link_info)
{
    herr_t    retVal = -1;
    unsigned *theArray = NULL;
    jboolean  isCopy;

    if (link_info == NULL) {
        h5nullArgument(env, "H5Pget_est_link_info:  link_info is NULL");
        return -1;
    }
    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR link_info,&isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_est_link_info:  input not pinned");
        return -1;
    }
    
    retVal= H5Pget_est_link_info((hid_t)gcpl_id, &(theArray[0]), &(theArray[1]));
    if(retVal < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR link_info, (jint *)theArray, JNI_ABORT);
        h5libraryError(env);
        return -1;
    }  
    ENVPTR->ReleaseIntArrayElements(ENVPAR link_info, (jint *)theArray, 0);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_elink_fapl
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1elink_1fapl
  (JNIEnv *env, jclass clss, jint lapl_id, jint fapl_id)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_elink_fapl((hid_t)lapl_id, (hid_t)fapl_id);
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Pget_elink_fapl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Pget_1elink_1fapl
  (JNIEnv *env, jclass clss, jint lapl_id)
{
    hid_t retVal = -1;

    retVal = H5Pget_elink_fapl((hid_t)lapl_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_elink_prefix
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1elink_1prefix
  (JNIEnv *env, jclass clss, jint lapl_id, jstring prefix)
{ 
    herr_t      retVal = -1;
    const char *aName;
    jboolean    isCopy;

    if (prefix == NULL) {
        h5nullArgument(env, "H5Pset_elink_prefix: prefix is NULL");
        return -1;
    }
    aName = (const char*)ENVPTR->GetStringUTFChars(ENVPAR prefix, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError(env, "H5Pset_elink_prefix: prefix not pinned");
        return -1;
    }
    
    retVal = H5Pset_elink_prefix((hid_t)lapl_id, aName);
    if(retVal < 0) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR prefix, aName);
        h5libraryError(env);
        return -1;
    }
    ENVPTR->ReleaseStringUTFChars(ENVPAR prefix, aName);
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_elink_prefix
 * Signature: (I[Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1elink_1prefix
  (JNIEnv *env, jclass clss, jint lapl_id, jobjectArray prefix)
{
    size_t  size = -1;
    char   *pre;
    jlong   prefix_size;
    jstring str = NULL;

    if (prefix == NULL) {
        h5nullArgument(env, "H5Pget_elink_prefix: prefix is NULL");
        return -1;
    }
    prefix_size = (jlong)H5Pget_elink_prefix((hid_t)lapl_id, (char*)NULL, size);
    if(prefix_size < 0) {
        h5libraryError(env);
        return -1;
    }
    size = (size_t)prefix_size + 1;/* add extra space for the null terminator */
    pre = (char*)malloc(sizeof(char)*size);
    if (pre == NULL) {
        h5outOfMemory(env, "H5Pget_elink_prefix:  malloc failed ");
        return -1;
    }
    prefix_size = (jlong)H5Pget_elink_prefix((hid_t)lapl_id, (char*)pre, size);
    if (prefix_size < 0) {
        free(pre);
        h5libraryError(env);
        return -1;
    }

    str = ENVPTR->NewStringUTF(ENVPAR pre);
    if (str == NULL) {
        /* exception -- fatal JNI error */
        free(pre);
        h5JNIFatalError(env, "H5Pget_elink_prefix:  return string not created");
        return -1;
    }
    ENVPTR->SetObjectArrayElement(ENVPAR prefix, 0, str);
    free(pre);

    return prefix_size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_direct
 * Signature: (IJJJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1direct
 (JNIEnv *env, jclass clss, jint fapl_id, jlong alignment, jlong block_size, jlong cbuf_size)
{
    herr_t retVal = -1;

#ifdef H5_HAVE_DIRECT 
    retVal = H5Pset_fapl_direct((hid_t)fapl_id, (size_t)alignment, (size_t)block_size, (size_t)cbuf_size);
#endif 
    if(retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fapl_direct
 * Signature: (I[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fapl_1direct
 (JNIEnv *env, jclass clss, jint fapl_id, jlongArray info)
{
    herr_t   retVal = -1;
    size_t   alignment = 0;
    size_t   block_size = 0;
    size_t   cbuf_size = 0;
    jlong   *theArray;
    jboolean isCopy;

#ifdef H5_HAVE_DIRECT
    if (info == NULL) {
        h5nullArgument(env, "H5Pget_fapl_direct:  info input array is NULL");
        return -1;
    }
    if (ENVPTR->GetArrayLength(ENVPAR info) < 3) {
        h5badArgument( env, "H5Pget_fapl_direct:  info input array < 4");
        return -1;
    }

    theArray = (jlong *)ENVPTR->GetLongArrayElements(ENVPAR info, &isCopy);
    if (theArray == NULL) {
        h5JNIFatalError(env, "H5Pget_fapl_direct:  info not pinned");
        return -1;
    }

    retVal = H5Pget_fapl_direct((hid_t)fapl_id, &alignment, &block_size, &cbuf_size);
    if(retVal <0) {
        ENVPTR->ReleaseLongArrayElements(ENVPAR info, theArray, JNI_ABORT);
        h5libraryError(env);
    } 
    else {
        theArray[0] = alignment;
        theArray[1] = block_size;
        theArray[2] = cbuf_size;
        ENVPTR->ReleaseLongArrayElements(ENVPAR info, theArray, 0);
    }
#else
    if (retVal < 0) {
        h5libraryError(env);
    }
#endif

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_sec2
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1sec2
  (JNIEnv *env, jclass clss, jint fapl_id)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_fapl_sec2((hid_t) fapl_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_stdio
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1stdio
  (JNIEnv *env, jclass clss, jint fapl_id)
{
    herr_t retVal = -1;
    
    retVal = H5Pset_fapl_stdio((hid_t) fapl_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fapl_windows
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fapl_1windows
  (JNIEnv *env, jclass clss, jint fapl_id)
{
    herr_t retVal = -1;
    
#ifdef H5_HAVE_WINDOWS
    retVal = H5Pset_fapl_windows((hid_t) fapl_id);
#endif
    if (retVal < 0) {
        h5libraryError(env);
    }
    
    return (jint)retVal;
}

#ifdef __cplusplus
}
#endif
