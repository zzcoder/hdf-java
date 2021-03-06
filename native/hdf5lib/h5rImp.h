/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ncsa_hdf_hdf5lib_H5_H5R */

#ifndef _Included_ncsa_hdf_hdf5lib_H5_H5R
#define _Included_ncsa_hdf_hdf5lib_H5_H5R
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rcreate
 * Signature: ([BJLjava/lang/String;IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rcreate
  (JNIEnv *, jclass, jbyteArray, jlong, jstring, jint, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Rdereference
 * Signature: (JJI[B)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Rdereference
  (JNIEnv *, jclass, jlong, jlong, jint, jbyteArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rget_region
 * Signature: (JI[B)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Rget_1region
  (JNIEnv *, jclass, jlong, jint, jbyteArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5G_obj_t H5Rget_obj_type
 * Signature: (JI[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1obj_1type
  (JNIEnv *, jclass, jlong, jint, jbyteArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    int H5Rget_obj_type2
 * Signature: (JI[B[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1obj_1type2
  (JNIEnv *, jclass, jlong, jint, jbyteArray, jintArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rget_name
 * Signature: (JI[B[Ljava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1name
  (JNIEnv *, jclass, jlong, jint, jbyteArray, jobjectArray, jlong);

#ifdef __cplusplus
}
#endif
#endif
