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
 *  Identifier API Functions of the HDF5 library. 
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

extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5Header
 * Method:    H5Gget_linkval
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Iget_1type
  (JNIEnv *env, jclass clss, jint obj_id)
{
	H5I_type_t retVal = H5I_BADID;
	retVal =  H5Iget_type((hid_t)obj_id);
	if (retVal == H5I_BADID) {
		h5libraryError(env);
	}
	return (jint)retVal;
}
#ifdef __cplusplus
}
#endif 
