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
 *  Dataspace Object API Functions of the HDF5 library. 
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

extern jboolean h5outOfMemory( JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Screate
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Screate
  (JNIEnv *env, jclass clss, jint type)
{
	hid_t retVal = -1;
	retVal =  H5Screate((H5S_class_t)type);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Screate_simple
 * Signature: (I[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Screate_1simple
  (JNIEnv *env, jclass clss, jint rank, jbyteArray dims, jbyteArray maxdims)
{
	hid_t status;
	jbyte *dimsP, *maxdimsP;
	jboolean isCopy;
	hsize_t *sa;
	hsize_t *msa;
	int i;
	hsize_t *lp;
	jlong *jlp;

	if (dims == NULL) {
		h5nullArgument( env, "H5Screate_simple:  dims is NULL");
		return -1;
	}
#ifdef __cplusplus
	dimsP = env->GetByteArrayElements(dims,&isCopy);
#else
	dimsP = (*env)->GetByteArrayElements(env,dims,&isCopy);
#endif

	if (dimsP == NULL) {
		h5JNIFatalError(env,  "H5Screate-simple:  dims not pinned");
		return -1;
	}
	sa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (sa == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif

		h5JNIFatalError(env,  "H5Screate-simple:  dims not converted to hsize_t");
		return -1;
	}
	jlp = (jlong *)dimsP;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}

	if (maxdims == NULL) {
		maxdimsP = NULL;
		msa = (hsize_t *)maxdimsP;
	} else
	{
#ifdef __cplusplus
		maxdimsP = env->GetByteArrayElements(maxdims,&isCopy);
#else
		maxdimsP = (*env)->GetByteArrayElements(env,maxdims,&isCopy);
#endif
		if (maxdimsP == NULL)  {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
			h5JNIFatalError(env,  "H5Screate-simple:  maxdims not pinned");
			return -1;
		}
	msa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
		if (msa == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
			env->ReleaseByteArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
			free (sa);
			h5JNIFatalError(env,  "H5Screate-simple:  dims not converted to hsize_t");
			return -1;
		}
	jlp = (jlong *)maxdimsP;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}
	}

	status = H5Screate_simple(rank, (const hsize_t *)sa, (const hsize_t *)msa); 
	if (maxdimsP != NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
		free (msa);
	}

#ifdef __cplusplus
	env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
	free (sa);

	if (status < 0) {
//		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Scopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Scopy
  (JNIEnv *env, jclass clss, jint space_id)
{
	herr_t retVal = -1;
	retVal =  H5Scopy (space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_elements
 * Signature: (III[J)I
 */

#ifdef notdef
// 10/28/99 -- added code to copy the array -- this is not used,
// but serves as a reminder in case we try to implement this in
// the future....
/*
 *  Note:  the argument coord is actually long coord[][], which has been
 *         flattened by the caller.
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1elements
  (JNIEnv *env, jclass clss, jint space_id, jint op, jint num_elemn, jlongArray coord)
{
	herr_t status;
	jint i;
	jlong *P;
	jboolean isCopy;
	hssize_t *sa;
	int rank;

	if (coord == NULL) {
		h5nullArgument( env, "H5Sselect_elements:  coord is NULL");
		return -1;
	}

	P = (*env)->GetLongArrayElements(env,coord,&isCopy);
	if (P == NULL) {
		h5JNIFatalError(env,  "H5Sselect_elements:  coord not pinned");
		return -1;
	}
	sa = (hssize_t *)malloc( num_elems * 2 * sizeof(hssize_t)); 
	if (sa == NULL) {
		(*env)->ReleaseLongArrayElements(env,coord,P,JNI_ABORT);
		h5JNIFatalError(env,  "H5Sselect_elements:  coord array not converted to hssize_t");
		return -1;
	}
	for (i= 0; i < (num_elsms * 2); i++) {
		sa[i] = P[i];
	}

	status = H5Sselect_elements (space_id, (H5S_seloper_t)op, num_elemn, (const hssize_t **)&sa);
	(*env)->ReleaseLongArrayElements(env, coord, P, JNI_ABORT);
	free(sa);

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}
#endif

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1elements
  (JNIEnv *env, jclass clss, jint space_id, jint op, jint num_elemn, jbyteArray coord)
{
	int ii;
	hssize_t *lp;
	hssize_t *llp;
	jlong *jlp;
	herr_t status;
	jbyte *P;
	jboolean isCopy;
	jsize size;
	int nlongs;

	if (coord == NULL) {
		h5nullArgument( env, "H5Sselect_elements:  coord is NULL");
		return -1;
	}

#ifdef __cplusplus
	P = env->GetByteArrayElements(coord,&isCopy);
#else
	P = (*env)->GetByteArrayElements(env,coord,&isCopy);
#endif
	if (P == NULL) {
		h5JNIFatalError(env,  "H5Sselect_elements:  coord not pinned");
		return -1;
	}
#ifdef __cplusplus
	size = (int) env->GetArrayLength(coord);
#else
	size = (int) (*env)->GetArrayLength(env,coord);
#endif
	nlongs = size / sizeof(jlong);
	lp = (hssize_t *)malloc(nlongs * sizeof(hssize_t));
	jlp = (jlong *)P;
	llp = lp;
	for (ii = 0; ii < nlongs; ii++) {
		*lp = (hssize_t)*jlp;
		lp++;
		jlp++;
	}

	status = H5Sselect_elements (space_id, (H5S_seloper_t)op, num_elemn, (const hssize_t **)llp);

#ifdef __cplusplus
	env->ReleaseByteArrayElements(coord, P, JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env, coord, P, JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_all
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1all
  (JNIEnv *env, jclass clss, jint space_id)
{
	herr_t retVal = -1;
	retVal =  H5Sselect_all(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_none
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1none
  (JNIEnv *env, jclass clss, jint space_id)
{
	herr_t retVal = -1;
	retVal =  H5Sselect_none(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_valid
 * Signature: (I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1valid
  (JNIEnv *env, jclass clss, jint space_id)
{
	htri_t bval;
	bval = H5Sselect_valid(space_id);
	if (bval > 0) {
		return JNI_TRUE;
	} else if (bval == 0) {
		return JNI_FALSE;
	} else {
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_npoints
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1npoints
  (JNIEnv *env, jclass clss, jint space_id)
{
	hssize_t retVal = -1;
	retVal =  H5Sget_simple_extent_npoints(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_npoints
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1npoints
  (JNIEnv *env, jclass clss, jint space_id)
{
	hssize_t retVal = -1;
	retVal =  H5Sget_select_npoints(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_ndims
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1ndims
  (JNIEnv *env, jclass clss, jint space_id)
{
	int retVal = -1;
	retVal =  H5Sget_simple_extent_ndims(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_dims
 * Signature: (I[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1dims
  (JNIEnv *env, jclass clss, jint space_id, jlongArray dims, jlongArray maxdims)
{
	int status;
	jlong *dimsP, *maxdimsP;
	jboolean isCopy;
	hsize_t *sa;
	hsize_t *msa;
	int i;
	int rank;

	if (dims == NULL) {
		h5nullArgument( env, "H5Sget_simple_extent:  dims is NULL");
		return -1;
	}
#ifdef __cplusplus
	dimsP = env->GetLongArrayElements(dims,&isCopy);
#else
	dimsP = (*env)->GetLongArrayElements(env,dims,&isCopy);
#endif
	if (dimsP == NULL) {
		h5JNIFatalError(env,  "H5Pget_simple_extent:  dims not pinned");
		return -1;
	}
#ifdef __cplusplus
	rank = (int) env->GetArrayLength(dims);
#else
	rank = (int) (*env)->GetArrayLength(env,dims);
#endif
	sa = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
	if (sa == NULL)  {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Sget_simple_extent:  dims not converted to hsize_t");
		return -1;
	}
	if (maxdims == NULL) {
		maxdimsP = NULL;
		msa = (hsize_t *)maxdimsP;
	} else {
#ifdef __cplusplus
		maxdimsP = env->GetLongArrayElements(maxdims,&isCopy);
#else
		maxdimsP = (*env)->GetLongArrayElements(env,maxdims,&isCopy);
#endif
		if (maxdimsP == NULL) {
#ifdef __cplusplus
			env->ReleaseLongArrayElements(dims,dimsP,JNI_ABORT);
#else
			(*env)->ReleaseLongArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
			h5JNIFatalError(env,  "H5Pget_simple_extent:  maxdims not pinned");
			return -1;
		}
		msa = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
		if (msa == NULL)  {
#ifdef __cplusplus
			env->ReleaseLongArrayElements(dims,dimsP,JNI_ABORT);
			env->ReleaseLongArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
			(*env)->ReleaseLongArrayElements(env,dims,dimsP,JNI_ABORT);
			(*env)->ReleaseLongArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
			free(sa);
			h5JNIFatalError(env,  "H5Sget_simple_extent:  maxdims not converted to hsize_t");
			return -1;
		}
	}

	status = H5Sget_simple_extent_dims(space_id, (hsize_t *)sa, (hsize_t *)msa); 
	
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
		free(sa);
		if (maxdimsP != NULL)  {
#ifdef __cplusplus
			env->ReleaseLongArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
			(*env)->ReleaseLongArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
			free(msa);
		}
		h5libraryError(env);
	} else {
		for (i = 0; i < rank; i++) {
			dimsP[i] = sa[i];
		}
		free(sa); 
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims,dimsP,0);
#else
		(*env)->ReleaseLongArrayElements(env,dims,dimsP,0);
#endif
		if (maxdimsP != NULL) {
			for (i = 0; i < rank; i++) {
				maxdimsP[i] = msa[i];
			}
			free(msa); 
#ifdef __cplusplus
			env->ReleaseLongArrayElements(maxdims,maxdimsP,0);
#else
			(*env)->ReleaseLongArrayElements(env,maxdims,maxdimsP,0);
#endif
		}
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_simple_extent_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1simple_1extent_1type
  (JNIEnv *env, jclass clss, jint space_id)
{
	H5S_class_t retVal = H5S_NO_CLASS;
	retVal =  H5Sget_simple_extent_type(space_id);
	if (retVal == H5S_NO_CLASS) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sset_extent_simple
 * Signature: (II[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1simple
  (JNIEnv *env, jclass clss, jint space_id, jint rank, jbyteArray dims, jbyteArray maxdims)
{
	herr_t status;
	jbyte *dimsP, *maxdimsP;
	jboolean isCopy;
	hsize_t *sa;
	hsize_t *msa;
	int i;
	hsize_t *lp;
	jlong *jlp;

	if (dims == NULL) {
		h5nullArgument( env, "H5Sset_simple_extent:  dims is NULL");
		return -1;
	}
#ifdef __cplusplus
	dimsP = env->GetByteArrayElements(dims,&isCopy);
#else
	dimsP = (*env)->GetByteArrayElements(env,dims,&isCopy);
#endif
	if (dimsP == NULL) {
		h5JNIFatalError(env,  "H5Pset_simple_extent:  dims not pinned");
		return -1;
	}
	sa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (sa == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Sset_simple_extent:  dims not converted to hsize_t");
		return -1;
	}
	jlp = (jlong *)dimsP;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}
	if (maxdims == NULL) {
		maxdimsP = NULL;
		msa = (hsize_t *)maxdimsP;
	} else {
#ifdef __cplusplus
		maxdimsP = env->GetByteArrayElements(maxdims,&isCopy);
#else
		maxdimsP = (*env)->GetByteArrayElements(env,maxdims,&isCopy);
#endif
		if (maxdimsP == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
			h5JNIFatalError(env,  "H5Pset_simple_extent:  maxdims not pinned");
			return -1;
		}
		msa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
		if (msa == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
			env->ReleaseByteArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
			free (sa);
			h5JNIFatalError(env,  "H5Sset_simple_extent:  maxdims not converted to hsize_t");
			return -1;
		}
		jlp = (jlong *)maxdimsP;
		for (i = 0; i < rank; i++) {
			*lp = (hsize_t)*jlp;
			lp++;
			jlp++;
		}
	}

	status = H5Sset_extent_simple(space_id, rank, (hsize_t *)sa, (hsize_t *)msa); 

#ifdef __cplusplus
	env->ReleaseByteArrayElements(dims,dimsP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
	free (sa);
	if (maxdimsP != NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(maxdims,maxdimsP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,maxdims,maxdimsP,JNI_ABORT);
#endif
		free (msa);
	}

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sis_simple
 * Signature: (I)J
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sis_1simple
  (JNIEnv *env, jclass clss, jint space_id)
{
	htri_t bval;
	bval = H5Sis_simple(space_id);
	if (bval > 0) {
		return JNI_TRUE;
	} else if (bval == 0) {
		return JNI_FALSE;
	} else {
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Soffset_simple
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Soffset_1simple
  (JNIEnv *env, jclass clss, jint space_id, jbyteArray offset)
{
	herr_t status;
	jbyte *P = NULL;
	jboolean isCopy;
	hssize_t *sa;
	int rank;
	int i;
	hssize_t *lp;
	jlong *jlp;

	if (offset != NULL) {
#ifdef __cplusplus
		P = env->GetByteArrayElements(offset,&isCopy);
#else
		P = (*env)->GetByteArrayElements(env,offset,&isCopy);
#endif
		if (P == NULL) {
			h5JNIFatalError(env,  "H5Soffset_simple:  offset not pinned");
			return -1;
		}
#ifdef __cplusplus
		i = (int) env->GetArrayLength(offset);
#else
		i = (int) (*env)->GetArrayLength(env,offset);
#endif
		rank = i / sizeof(jlong);
		sa = lp = (hssize_t *)malloc(rank * sizeof(hssize_t));
		if (sa == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(offset,P,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,offset,P,JNI_ABORT);
#endif
			h5JNIFatalError(env,  "H5Soffset_simple:  offset not converted to hssize_t");
			return -1;
		}
		jlp = (jlong *)P;
		for (i = 0; i < rank; i++) {
			*lp = (hssize_t)*jlp;
			lp++;
			jlp++;
		}
	} else {
		P = NULL;
		sa = (hssize_t *)P;
	}

	status = H5Soffset_simple(space_id, sa);
	if (P != NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(offset,P,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,offset,P,JNI_ABORT);
#endif
		free(sa);
	}

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sextent_copy
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sextent_1copy
  (JNIEnv *env, jclass clss, jint space_id, jint src_id)
{
	herr_t retVal = -1;
	retVal =  H5Sextent_copy(space_id, src_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sset_extent_none
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sset_1extent_1none
  (JNIEnv *env, jclass clss, jint space_id)
{
	herr_t retVal = -1;
	retVal =  H5Sset_extent_none(space_id);
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sselect_hyperslab
 * Signature: (II[B[B[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sselect_1hyperslab
  (JNIEnv *env, jclass clss, jint space_id, jint op, 
  jbyteArray start, jbyteArray stride, jbyteArray count, jbyteArray block)
{
	herr_t status;
	jbyte *startP, *strideP, *countP, *blockP;
	jboolean isCopy;
	hsize_t *strt;
	hsize_t *strd;
	hsize_t *cnt;
	hsize_t *blk;
	int rank;
	int i;
	hsize_t *lp;
	jlong *jlp;

	if (start == NULL) {
		h5nullArgument( env, "H5Sselect_hyperslab:  start is NULL");
		return -1;
	}
	if (count == NULL) {
		h5nullArgument( env, "H5Sselect_hyperslab:  count is NULL");
		return -1;
	}
#ifdef __cplusplus
	i = (int)env->GetArrayLength(start);
	if (i != env->GetArrayLength(count)) {
		h5badArgument( env, "H5Sselect_hyperslab:  count and start have different rank!");
		
	}
#else
	i = (int)(*env)->GetArrayLength(env,start);
	if (i != (*env)->GetArrayLength(env,count)) {
		h5badArgument( env, "H5Sselect_hyperslab:  count and start have different rank!");
		
	}
#endif
	rank = i / sizeof(jlong);
		
#ifdef __cplusplus
	startP = env->GetByteArrayElements(start,&isCopy);
#else
	startP = (*env)->GetByteArrayElements(env,start,&isCopy);
#endif
	if (startP == NULL) {
		h5JNIFatalError(env,  "H5Sselect_hyperslab:  start not pinned");
		return -1;
	}
	strt = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (strt == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(start,startP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,start,startP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Sselect_hyperslab:  start not converted to hsize_t");
		return -1;
	}
	jlp = (jlong *)startP;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}
#ifdef __cplusplus
	countP = env->GetByteArrayElements(count,&isCopy);
#else
	countP = (*env)->GetByteArrayElements(env,count,&isCopy);
#endif
	if (countP == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
#endif
		free(strt);
		h5JNIFatalError(env,  "H5Sselect_hyperslab:  count not pinned");
		return -1;
	}
	cnt = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (cnt == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
		env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
#endif
		free(strt);
		h5JNIFatalError(env,  "H5Sselect_hyperslab:  count not converted to hsize_t");
		return -1;
	}
	jlp = (jlong *)countP;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}
	if (stride == NULL) {
		strideP = NULL;
		strd = (hsize_t *)strideP;
	} else {
#ifdef __cplusplus
		strideP = env->GetByteArrayElements(stride,&isCopy);
#else
		strideP = (*env)->GetByteArrayElements(env,stride,&isCopy);
#endif
		if (strideP == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
			env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
#endif
			free(cnt); free(strt);
			h5badArgument( env, "H5Sselect_hyperslab:  stride not pinned");
			return -1;
		}
		strd = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
		if (strd == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
			env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
			env->ReleaseByteArrayElements(stride, strideP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,stride, strideP,JNI_ABORT);
#endif
			free(cnt); free(strt);
			h5JNIFatalError(env,  "H5Sselect_hyperslab:  stride not converted to hsize_t");
			return -1;
		}
		jlp = (jlong *)strideP;
		for (i = 0; i < rank; i++) {
			*lp = (hsize_t)*jlp;
			lp++;
			jlp++;
		}
	}
	if (block == NULL) {
		blockP = NULL;
		blk = (hsize_t *)blockP;
	} else {
#ifdef __cplusplus
		blockP = env->GetByteArrayElements(block,&isCopy);
#else
		blockP = (*env)->GetByteArrayElements(env,block,&isCopy);
#endif
		if (blockP == NULL)  {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(stride, strideP,JNI_ABORT);
			env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
			env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,stride, strideP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
#endif
			free(cnt); free(strt);
			if (strd != NULL) { free(strd); }
			h5JNIFatalError(env,  "H5Sselect_hyperslab:  block not pinned");
			return -1;
		}
		blk = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
		if (blk == NULL) {
#ifdef __cplusplus
			env->ReleaseByteArrayElements(stride, strideP,JNI_ABORT);
			env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
			env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
			env->ReleaseByteArrayElements(block, blockP,JNI_ABORT);
#else
			(*env)->ReleaseByteArrayElements(env,stride, strideP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
			(*env)->ReleaseByteArrayElements(env,block, blockP,JNI_ABORT);
#endif
			free(cnt); free(strt);
			if (strd != NULL) { free(strd); }
			h5JNIFatalError(env,  "H5Sget_simple_extent:  block not converted to hsize_t");
			return -1;
		}
		jlp = (jlong *)blockP;
		for (i = 0; i < rank; i++) {
			*lp = (hsize_t)*jlp;
			lp++;
			jlp++;
		}
	}

	status = H5Sselect_hyperslab (space_id, (H5S_seloper_t)op, (const hssize_t *)strt, (const hsize_t *)strd, (const hsize_t *)cnt, (const hsize_t *)blk);

#ifdef __cplusplus
	env->ReleaseByteArrayElements(start, startP,JNI_ABORT);
	env->ReleaseByteArrayElements(count, countP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,start, startP,JNI_ABORT);
	(*env)->ReleaseByteArrayElements(env,count, countP,JNI_ABORT);
#endif
	free(strt);
	free(cnt);
	if (strideP != NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(stride, strideP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,stride, strideP,JNI_ABORT);
#endif
		free(strd);
	}
	if (blockP != NULL)  {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(block, blockP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,block, blockP,JNI_ABORT);
#endif
		free(blk);
	}

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sclose
  (JNIEnv *env, jclass clss, jint space_id)
{
	herr_t retVal = -1;
	retVal =  H5Sclose(space_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_hyper_nblocks
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1nblocks
  (JNIEnv *env, jclass clss, jint spaceid)
{
	hssize_t status;

	status = H5Sget_select_hyper_nblocks((hid_t)spaceid);
	if (status < 0)
		h5libraryError(env);

	return (jlong)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_elem_npoints
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1npoints
  (JNIEnv *env, jclass clss, jint spaceid)
{
	hssize_t status;

	status = H5Sget_select_elem_npoints((hid_t)spaceid);
	if (status < 0)
		h5libraryError(env);

	return (jlong)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_hyper_blocklist
 * Signature: (IJJ[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1hyper_1blocklist
  (JNIEnv *env, jclass clss, jint spaceid, jlong startblock, jlong numblocks, jlongArray buf)
{
	herr_t status;
	jlong *bufP;
	jboolean isCopy;
	hsize_t *ba;
	int i;
	long st;
	long nb;

	st = (long)startblock;
	nb = (long)numblocks;

	if ( buf == NULL ) {
		h5nullArgument( env, "H5Sget_select_hyper_blocklist:  buf is NULL");
		return -1;
	}
#ifdef __cplusplus
	bufP = env->GetLongArrayElements(buf,&isCopy);
#else
	bufP = (*env)->GetLongArrayElements(env,buf,&isCopy);
#endif
	if (bufP == NULL) {
		h5JNIFatalError( env, "H5Sget_select_hyper_blocklist:  buf not pinned");
		return -1;
	}
	ba = (hsize_t *)malloc( nb * 2 * sizeof(hsize_t)); 
	if (ba == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf, bufP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Screate-simple:  buffer not converted to hsize_t");
		return -1;
	}

	status = H5Sget_select_hyper_blocklist((hid_t)spaceid, (hsize_t)st,
		(hsize_t)nb, (hsize_t *)ba);
	
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf,bufP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,JNI_ABORT);
#endif
		free (ba);
		h5libraryError(env);
	} else  {
		for (i = 0; i < (numblocks*2); i++) {
			bufP[i] = ba[i];
		}
		free (ba); 
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf,bufP,0);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,0);
#endif
	}

	return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_elem_pointlist
 * Signature: (IJJ[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1elem_1pointlist
  (JNIEnv *env, jclass clss, jint spaceid, jlong startpoint, jlong numpoints, jlongArray buf)
{
	herr_t status;
	jlong *bufP;
	jboolean isCopy;
	hsize_t *ba;
	int i;

	if ( buf == NULL ) {
		h5nullArgument( env, "H5Sget_select_elem_pointlist:  buf is NULL");
		return -1;
	}
#ifdef __cplusplus
	bufP = env->GetLongArrayElements(buf,&isCopy);
#else
	bufP = (*env)->GetLongArrayElements(env,buf,&isCopy);
#endif
	if (bufP == NULL) {
		h5JNIFatalError( env, "H5Sget_select_elem_pointlist:  buf not pinned");
		return -1;
	}
	ba = (hsize_t *)malloc( ((long)numpoints) * sizeof(hsize_t)); 
	if (ba == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf,bufP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Sget_select_elem_pointlist:  buf not converted to hsize_t");
		return -1;
	}

	status = H5Sget_select_elem_pointlist((hid_t)spaceid, (hsize_t)startpoint, 
		(hsize_t)numpoints, (hsize_t *)ba);
	
	if (status < 0) {
		free (ba);
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf,bufP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
		for (i = 0; i < numpoints; i++) {
			bufP[i] = ba[i];
		}
		free (ba) ;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(buf,bufP,0);
#else
		(*env)->ReleaseLongArrayElements(env,buf,bufP,0);
#endif
	}

	return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Sget_select_bounds
 * Signature: (I[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Sget_1select_1bounds
  (JNIEnv *env, jclass clss, jint spaceid, jlongArray start, jlongArray end)
{
	herr_t status;
	jlong *startP, *endP;
	jboolean isCopy;
	hsize_t *strt;
	hsize_t *en;
	int rank;
	int i;

	if ( start == NULL ) {
		h5nullArgument( env, "H5Sget_select_bounds:  start is NULL");
		return -1;
	}

	if ( end == NULL ) {
		h5nullArgument( env, "H5Sget_select_bounds:  end is NULL");
		return -1;
	}

#ifdef __cplusplus
	startP = env->GetLongArrayElements(start,&isCopy);
#else
	startP = (*env)->GetLongArrayElements(env,start,&isCopy);
#endif
	if (startP == NULL) {
		h5JNIFatalError( env, "H5Sget_select_bounds:  start not pinned");
		return -1;
	}
#ifdef __cplusplus
	rank = (int)env->GetArrayLength(start);
#else
	rank = (int)(*env)->GetArrayLength(env,start);
#endif
	strt = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
	if (strt == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(start,startP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,start,startP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Sget_select_bounds:  start not converted to hsize_t");
		return -1;
	}

#ifdef __cplusplus
	endP = env->GetLongArrayElements(end,&isCopy);
#else
	endP = (*env)->GetLongArrayElements(env,end,&isCopy);
#endif
	if (endP == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(start,startP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,start,startP,JNI_ABORT);
#endif
		free(strt);
		h5JNIFatalError( env, "H5Sget_select_bounds:  end not pinned");
		return -1;
	}
	en = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
	if (en == NULL)  {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(end,endP,JNI_ABORT);
		env->ReleaseLongArrayElements(start,startP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,end,endP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,start,startP,JNI_ABORT);
#endif
		free(strt);
		h5JNIFatalError(env,  "H5Sget_simple_extent:  dims not converted to hsize_t");
		return -1;
	}

	status = H5Sget_select_bounds((hid_t) spaceid, (hsize_t *)strt, (hsize_t *)en);
	
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(start,startP,JNI_ABORT);
		env->ReleaseLongArrayElements(end,endP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,start,startP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,end,endP,JNI_ABORT);
#endif
		free(strt); free(en);
		h5libraryError(env);
	} else  {
		for (i = 0; i < rank; i++) {
			startP[i] = strt[i];
			endP[i] = en[i];
		}
		free(strt); free(en);
#ifdef __cplusplus
		env->ReleaseLongArrayElements(start,startP,0);
		env->ReleaseLongArrayElements(end,endP,0);
#else
		(*env)->ReleaseLongArrayElements(env,start,startP,0);
		(*env)->ReleaseLongArrayElements(env,end,endP,0);
#endif
	}

	return (jint)status;
}


#ifdef __cplusplus
}
#endif 
