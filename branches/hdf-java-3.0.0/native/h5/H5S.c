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

#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5S.h"


/*
 * Class:     hdf_h5_H5S
 * Method:    H5Screate
 * Signature: (Lhdf/h5/enums/H5S_CLASS;)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Screate
  (JNIEnv *env, jclass cls, jobject)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Screate_simple
 * Signature: (I[J[J)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Screate_1simple
  (JNIEnv *env, jclass cls, jint, jlongArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sset_extent_simple
 * Signature: (II[J[J)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sset_1extent_1simple
  (JNIEnv *env, jclass cls, jint, jint, jlongArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Scopy
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Scopy
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sclose
 * Signature: (I)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sclose
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sencode
 * Signature: (I[B[J)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sencode__I_3B_3J
  (JNIEnv *env, jclass cls, jint, jbyteArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sencode
 * Signature: (I)[B
 */
/*
JNIEXPORT jbyteArray JNICALL Java_hdf_h5_H5S_H5Sencode__I
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sdecode
 * Signature: ([B)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sdecode
  (JNIEnv *env, jclass cls, jbyteArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_simple_extent_npoints
 * Signature: (I)J
 */
/*
JNIEXPORT jlong JNICALL Java_hdf_h5_H5S_H5Sget_1simple_1extent_1npoints
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_simple_extent_ndims
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sget_1simple_1extent_1ndims
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_simple_extent_dims
 * Signature: (I[J[J)I
 */
/*
JNIEXPORT jint JNICALL Java_hdf_h5_H5S_H5Sget_1simple_1extent_1dims
  (JNIEnv *env, jclass cls, jint, jlongArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sis_simple
 * Signature: (I)Z
 */
/*
JNIEXPORT jboolean JNICALL Java_hdf_h5_H5S_H5Sis_1simple
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_npoints
 * Signature: (I)J
 */
/*
JNIEXPORT jlong JNICALL Java_hdf_h5_H5S_H5Sget_1select_1npoints
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sselect_hyperslab
 * Signature: (ILhdf/h5/enums/H5S_SELECT_OPER;[J[J[J[J)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sselect_1hyperslab
  (JNIEnv *env, jclass cls, jint, jobject, jlongArray, jlongArray, jlongArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sselect_elements
 * Signature: (ILhdf/h5/enums/H5S_SELECT_OPER;J[J)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sselect_1elements
  (JNIEnv *env, jclass cls, jint, jobject, jlong, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_simple_extent_type
 * Signature: (I)Lhdf/h5/enums/H5S_CLASS;
 */
/*
JNIEXPORT jobject JNICALL Java_hdf_h5_H5S_H5Sget_1simple_1extent_1type
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sset_extent_none
 * Signature: (I)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sset_1extent_1none
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sextent_copy
 * Signature: (II)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sextent_1copy
  (JNIEnv *env, jclass cls, jint, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sextent_equal
 * Signature: (II)Z
 */
/*
JNIEXPORT jboolean JNICALL Java_hdf_h5_H5S_H5Sextent_1equal
  (JNIEnv *env, jclass cls, jint, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sselect_all
 * Signature: (I)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sselect_1all
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sselect_none
 * Signature: (I)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sselect_1none
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Soffset_simple
 * Signature: (I[J)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Soffset_1simple
  (JNIEnv *env, jclass cls, jint, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sselect_valid
 * Signature: (I)Z
 */
/*
JNIEXPORT jboolean JNICALL Java_hdf_h5_H5S_H5Sselect_1valid
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_hyper_nblocks
 * Signature: (I)J
 */
/*
JNIEXPORT jlong JNICALL Java_hdf_h5_H5S_H5Sget_1select_1hyper_1nblocks
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_elem_npoints
 * Signature: (I)J
 */
/*
JNIEXPORT jlong JNICALL Java_hdf_h5_H5S_H5Sget_1select_1elem_1npoints
  (JNIEnv *env, jclass cls, jint)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_hyper_blocklist
 * Signature: (IJJ)[J
 */
/*
JNIEXPORT jlongArray JNICALL Java_hdf_h5_H5S_H5Sget_1select_1hyper_1blocklist
  (JNIEnv *env, jclass cls, jint, jlong, jlong)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_elem_pointlist
 * Signature: (IJJ)[J
 */
/*
JNIEXPORT jlongArray JNICALL Java_hdf_h5_H5S_H5Sget_1select_1elem_1pointlist
  (JNIEnv *env, jclass cls, jint, jlong, jlong)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_bounds
 * Signature: (I[J[J)V
 */
/*
JNIEXPORT void JNICALL Java_hdf_h5_H5S_H5Sget_1select_1bounds
  (JNIEnv *env, jclass cls, jint, jlongArray, jlongArray)
{

}
*/

/*
 * Class:     hdf_h5_H5S
 * Method:    H5Sget_select_type
 * Signature: (I)Lhdf/h5/enums/H5S_SEL;
 */
/*
JNIEXPORT jobject JNICALL Java_hdf_h5_H5S_H5Sget_1select_1type
  (JNIEnv *env, jclass cls, jint)
{

}
*/

#ifdef __cplusplus
}
#endif

