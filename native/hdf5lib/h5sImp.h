/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ncsa_hdf_hdf5lib_H5_H5S */

#ifndef _Included_ncsa_hdf_hdf5lib_H5_H5S
#define _Included_ncsa_hdf_hdf5lib_H5_H5S
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Screate
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Screate(
        JNIEnv *, jclass, jint);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Screate_simple
 * Signature: (I[J[J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Screate_1simple(
        JNIEnv *, jclass, jint, jlongArray, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Scopy
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Scopy(
        JNIEnv *, jclass, jlong);

#ifdef notdef
// 10/28/99 -- added code to copy the array -- this is not used,
// but serves as a reminder in case we try to implement this in
// the future....
/*
 *  Note:  the argument coord is actually long coord[][], which has been
 *         flattened by the caller.
 */

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_elements
 * Signature: (JII[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1elements
(JNIEnv *, jclass, jlong, jint, jint, jlongArray);
#endif

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_elements
 * Signature: (JII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1elements(
        JNIEnv *, jclass, jlong, jint, jint, jbyteArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_all
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1all(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_none
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1none(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_valid
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1valid(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_npoints
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1npoints(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_npoints
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1npoints(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_type
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1type(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_ndims
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1ndims(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_dims
 * Signature: (J[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1dims(
        JNIEnv *, jclass, jlong, jlongArray, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_type
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1type(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sset_extent_simple
 * Signature: (JI[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1simple(
        JNIEnv *, jclass, jlong, jint, jlongArray, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sis_simple
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sis_1simple(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Soffset_simple
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Soffset_1simple(
        JNIEnv *, jclass, jlong, jbyteArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sextent_copy
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sextent_1copy(
        JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sextent_equal
 * Signature: (JJ)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sextent_1equal
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sset_extent_none
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1none(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_hyperslab
 * Signature: (JI[J[J[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1hyperslab(
        JNIEnv *, jclass, jlong, jint, jlongArray, jlongArray, jlongArray, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sclose
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Sclose(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_hyper_nblocks
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1nblocks(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_elem_npoints
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1npoints(
        JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_hyper_blocklist
 * Signature: (JJJ[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1blocklist(
        JNIEnv *, jclass, jlong, jlong, jlong, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_elem_pointlist
 * Signature: (JJJ[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1pointlist(
        JNIEnv *, jclass, jlong, jlong, jlong, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_bounds
 * Signature: (J[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1bounds(
        JNIEnv *, jclass, jlong, jlongArray, jlongArray);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sencode
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sencode
  (JNIEnv *, jclass, jlong);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sdecode
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sdecode
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
