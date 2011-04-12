/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class hdf_h5_H5O */

#ifndef _Included_hdf_h5_H5O
#define _Included_hdf_h5_H5O
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oopen
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5O_H5Oopen
  (JNIEnv *, jclass, jint, jstring, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oopen_by_addr
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5O_H5Oopen_1by_1addr
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oopen_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JI)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5O_H5Oopen_1by_1idx
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Olink
 * Signature: (IILjava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Olink
  (JNIEnv *, jclass, jint, jint, jstring, jint, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oincr_refcount
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Oincr_1refcount
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Odecr_refcount
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Odecr_1refcount
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Ocopy
 * Signature: (ILjava/lang/String;ILjava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Ocopy
  (JNIEnv *, jclass, jint, jstring, jint, jstring, jint, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oset_comment
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Oset_1comment
  (JNIEnv *, jclass, jint, jstring);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oset_comment_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Oset_1comment_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oget_comment
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_hdf_h5_H5O_H5Oget_1comment
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oget_comment_by_name
 * Signature: (ILjava/lang/String;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_hdf_h5_H5O_H5Oget_1comment_1by_1name
  (JNIEnv *, jclass, jint, jstring, jint);

/*
 * Class:     hdf_h5_H5O
 * Method:    H5Oclose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5O_H5Oclose
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif