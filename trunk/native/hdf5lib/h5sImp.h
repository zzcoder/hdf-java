/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ncsa_hdf_hdf5lib_H5_H5F */

#ifndef _Included_ncsa_hdf_hdf5lib_H5
#define _Included_ncsa_hdf_hdf5lib_H5
#ifdef __cplusplus
extern "C" {
#endif


    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Screate
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Screate
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Screate_simple
     * Signature: (I[J[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Screate_1simple
      (JNIEnv *, jclass, jint, jlongArray, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Scopy
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Scopy
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sselect_elements
     * Signature: (III[B)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1elements
      (JNIEnv *, jclass, jint, jint, jint, jbyteArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sselect_all
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1all
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sselect_none
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1none
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sselect_valid
     * Signature: (I)Z
     */
    JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1valid
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_simple_extent_npoints
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1npoints
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_npoints
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1npoints
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_simple_extent_ndims
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1ndims
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_simple_extent_dims
     * Signature: (I[J[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1dims
      (JNIEnv *, jclass, jint, jlongArray, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_simple_extent_type
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1type
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sset_extent_simple
     * Signature: (II[J[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1simple
      (JNIEnv *, jclass, jint, jint, jlongArray, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sis_simple
     * Signature: (I)Z
     */
    JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sis_1simple
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Soffset_simple
     * Signature: (I[B)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Soffset_1simple
      (JNIEnv *, jclass, jint, jbyteArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sextent_copy
     * Signature: (II)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sextent_1copy
      (JNIEnv *, jclass, jint, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sset_extent_none
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1none
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sselect_hyperslab
     * Signature: (II[J[J[J[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1hyperslab
      (JNIEnv *, jclass, jint, jint, jlongArray, jlongArray, jlongArray, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sclose
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Sclose
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_hyper_nblocks
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1nblocks
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_elem_npoints
     * Signature: (I)J
     */
    JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1npoints
      (JNIEnv *, jclass, jint);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_hyper_blocklist
     * Signature: (IJJ[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1blocklist
      (JNIEnv *, jclass, jint, jlong, jlong, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_elem_pointlist
     * Signature: (IJJ[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1pointlist
      (JNIEnv *, jclass, jint, jlong, jlong, jlongArray);

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Sget_select_bounds
     * Signature: (I[J[J)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1bounds
      (JNIEnv *, jclass, jint, jlongArray, jlongArray);

    /*
     * Class:     hdf_h5_H5S
     * Method:    H5Sencode
     * Signature: (I[B[J)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sencode__I_3B_3J
      (JNIEnv *env, jclass cls, jint obj_id, jbyteArray buf, jlongArray nalloc);

    /*
     * Class:     hdf_h5_H5S
     * Method:    H5Sencode
     * Signature: (I)[B
     */
    JNIEXPORT jbyteArray JNICALL Java_hdf_h5_H5S_H5Sencode__I
      (JNIEnv *env, jclass cls, jint obj_id);

    /*
     * Class:     hdf_h5_H5S
     * Method:    H5Sdecode
     * Signature: ([B)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sdecode
      (JNIEnv *env, jclass cls, jbyteArray buf);

#ifdef __cplusplus
}
#endif
#endif
