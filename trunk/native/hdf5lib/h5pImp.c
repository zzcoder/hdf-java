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
/* missing definitions from hdf5.h */
#define FALSE 0
#define TRUE (!FALSE)
/* delete TRUE and FALSE when fixed in HDF5 */

#include <jni.h>
#include <stdlib.h>

extern jboolean h5outOfMemory( JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pcreate
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pcreate
  (JNIEnv *env, jclass clss, jint type)
{
	herr_t retVal = -1;
	retVal =  H5Pcreate((hid_t)type );
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
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pclose
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retVal = -1;
	retVal =  H5Pclose((hid_t)plist );
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
	retVal =  H5Pget_class((hid_t) plist );
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
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pcopy
  (JNIEnv *env, jclass clss, jint plist)
{
	hid_t retVal = -1;
	retVal =  H5Pcopy((hid_t)plist);
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
	herr_t status;
	jint *theArray;
	jboolean isCopy;

	if (version_info == NULL) {
		h5nullArgument( env, "H5Pget_version:  version_info input array is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(version_info) < 4) {
		h5badArgument( env, "H5Pget_version:  version_info input array < 4");
	}

	theArray = (jint *)env->GetIntArrayElements(version_info,&isCopy);
#else
	if ((*env)->GetArrayLength(env, version_info) < 4) {
		h5badArgument( env, "H5Pget_version:  version_info input array < 4");
	}

	theArray = (jint *)(*env)->GetIntArrayElements(env,version_info,&isCopy);
#endif

	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_version:  version_info not pinned");
		return -1;
	}

	status = H5Pget_version((hid_t)plist, (int *)&(theArray[0]), 
		(int *)&(theArray[1]), (int *)&(theArray[2]), 
		(int *)&(theArray[3]));

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(version_info,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,version_info,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(version_info,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,version_info,theArray,0);
#endif
	}

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
	long sz;
	herr_t retVal = -1;
	sz = (long)size;
	retVal =  H5Pset_userblock((hid_t)plist, (hsize_t)sz );
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
	herr_t status;
	jlong *theArray;
	jboolean isCopy;
	hsize_t s;

	if (size == NULL) {
		/* exception ? */
		h5nullArgument( env, "H5Pget_userblock:  size is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jlong *)env->GetLongArrayElements(size,&isCopy);
#else
	theArray = (jlong *)(*env)->GetLongArrayElements(env,size,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_userblock:  size not pinned");
		return -1;
	}

	status = H5Pget_userblock((hid_t)plist, &s);

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(size,theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,size,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = s;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(size,theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,size,theArray,0);
#endif
	}

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
	retVal =  H5Pset_sizes((hid_t)plist, (size_t)sizeof_addr, (size_t)sizeof_size);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_sizes
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1sizes
  (JNIEnv *env, jclass clss, jint plist, jintArray size)
{
	herr_t status;
	jint *theArray;
	jboolean isCopy;
	size_t ss;
	size_t sa;

	if (size == NULL) {
		h5nullArgument( env, "H5Pget_sizes:  size is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(size) < 2) {
		h5badArgument( env, "H5Pget_sizes:  size input array < 2 elements");
	}
	theArray = (jint *)env->GetIntArrayElements(size,&isCopy);
#else
	if ((*env)->GetArrayLength(env, size) < 2) {
		h5badArgument( env, "H5Pget_sizes:  size input array < 2 elements");
	}
	theArray = (jint *)(*env)->GetIntArrayElements(env,size,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_sizes:  size not pinned");
		return -1;
	}

	status = H5Pget_sizes((hid_t)plist, &sa, &ss);

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,size,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = sa;
		theArray[1] = ss;
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,size,theArray,0);
#endif
	}

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
	retVal =  H5Pset_sym_k((hid_t)plist, (int)ik, (int)lk);
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
	herr_t status;
	jint *theArray;
	jboolean isCopy;

	if (size == NULL) {
		h5nullArgument( env, "H5Pget_sym_k:  size is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(size) < 2) {
		h5badArgument( env, "H5Pget_sym_k:  size < 2 elements");
	}
	theArray = (jint *)env->GetIntArrayElements(size,&isCopy);
#else
	if ((*env)->GetArrayLength(env, size) < 2) {
		h5badArgument( env, "H5Pget_sym_k:  size < 2 elements");
	}
	theArray = (jint *)(*env)->GetIntArrayElements(env,size,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_sym_k:  size not pinned");
		return -1;
	}

	status = H5Pget_sym_k((hid_t)plist, (int *)&(theArray[0]), (int *)&(theArray[1]));

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,size,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,size,theArray,0);
#endif
	}

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
	retVal =  H5Pset_istore_k((hid_t)plist, (int)ik );
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
	herr_t status;
	jint *theArray;
	jboolean isCopy;

	if (ik == NULL) {
		h5nullArgument( env, "H5Pget_store_k:  ik is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jint *)env->GetIntArrayElements(ik,&isCopy);
#else
	theArray = (jint *)(*env)->GetIntArrayElements(env,ik,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_store_k:  size not pinned");
		return -1;
	}

	status = H5Pget_istore_k((hid_t)plist, (int *)&(theArray[0]));

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(ik,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,ik,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(ik,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,ik,theArray,0);
#endif
	}

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
	retVal =  H5Pset_layout((hid_t)plist, (H5D_layout_t)layout );
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
	retVal =  H5Pget_layout((hid_t)plist);
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
	herr_t status;
	jbyte *theArray;
	jboolean isCopy;
	hsize_t *da;
	int i;
	hsize_t *lp;
	jlong *jlp;
	int rank;

	if (dim == NULL) {
		h5nullArgument( env, "H5Pset_chunk:  dim array is NULL");
		return -1;
	}
#ifdef __cplusplus
	i = env->GetArrayLength(dim);
#else
	i = (*env)->GetArrayLength(env, dim);
#endif
	rank = i / sizeof(jlong);
	if (rank < ndims) {
		h5badArgument( env, "H5Pset_chunk:  dims array < ndims");
	}
#ifdef __cplusplus
	theArray = (jbyte *)env->GetByteArrayElements(dim,&isCopy);
#else
	theArray = (jbyte *)(*env)->GetByteArrayElements(env,dim,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pset_chunk:  dim array not pinned");
		return -1;
	}
	da = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (da == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(dim,theArray,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,dim,theArray,JNI_ABORT);
#endif
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

#ifdef __cplusplus
	env->ReleaseByteArrayElements(dim,theArray,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,dim,theArray,JNI_ABORT);
#endif
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
	herr_t status;
	jlong *theArray;
	jboolean isCopy;
	hsize_t *da;
	int i;

	if (dims == NULL) {
		h5nullArgument( env, "H5Pget_chunk:  dims is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(dims) < max_ndims) {
		h5badArgument( env, "H5Pget_chunk:  dims array < max_ndims");
	}
	theArray = (jlong *)env->GetLongArrayElements(dims,&isCopy);
#else
	if ((*env)->GetArrayLength(env, dims) < max_ndims) {
		h5badArgument( env, "H5Pget_chunk:  dims array < max_ndims");
	}
	theArray = (jlong *)(*env)->GetLongArrayElements(env,dims,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_chunk:  input dims not pinned");
		return -1;
	}
	da = (hsize_t *)malloc( max_ndims * sizeof(hsize_t)); 
	if (da == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims, theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,dims, theArray,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Pget_chunk:  dims not converted to hsize_t");
		return -1;
	}

	status = H5Pget_chunk((hid_t)plist, (int)max_ndims, da);

	if (status < 0)  {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims, theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,dims, theArray,JNI_ABORT);
#endif
		free (da);
		h5libraryError(env);
	} else {
		for (i= 0; i < max_ndims; i++) {	
			theArray[i] = da[i];
		}
		free (da); 
#ifdef __cplusplus
		env->ReleaseLongArrayElements(dims, theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,dims, theArray,0);
#endif
	}

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
	long thr;
	long align;
	herr_t retVal = -1;
	thr = (long)threshold;
	align = (long)alignment;
	retVal =  H5Pset_alignment((hid_t)plist, (hsize_t)thr, (hsize_t)align);
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
	herr_t status;
	jlong *theArray;
	jboolean isCopy;
	hsize_t t;
	hsize_t a;

	if (alignment == NULL) {
		h5nullArgument( env, "H5Pget_alignment:  input alignment is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(alignment) < 2) {
		h5badArgument( env, "H5Pget_alignment:  allingment input array < 2");
	}
	theArray = (jlong *)env->GetLongArrayElements(alignment,&isCopy);
#else
	if ((*env)->GetArrayLength(env, alignment) < 2) {
		h5badArgument( env, "H5Pget_alignment:  allingment input array < 2");
	}
	theArray = (jlong *)(*env)->GetLongArrayElements(env,alignment,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_alignment:  input array not pinned");
		return -1;
	}

	status = H5Pget_alignment((hid_t)plist, &t, &a);

	if (status < 0)  {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(alignment, theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,alignment, theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = t;
		theArray[1] = a;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(alignment, theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,alignment, theArray,0);
#endif
	}

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
	herr_t status;
	char* file;
	jboolean isCopy;
	long off;
	long sz;

	off = (long)offset;
	sz = (long)size;
	if (name == NULL) {
		h5nullArgument( env, "H5Pset_external:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		h5JNIFatalError( env, "H5Pset_external:  name not pinned");
		return -1;
	}

	status = H5Pset_external((hid_t)plist, file, (off_t)off, (hsize_t)sz);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
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
	retVal =  H5Pget_external_count((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_external
 * Signature: (III[Ljava/lang/String;[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1external
  (JNIEnv *env, jclass clss, jint plist, jint idx, jint name_size, 
  jobjectArray name, jlongArray size)
{
	herr_t status;
	jlong *theArray;
	jboolean isCopy;
	char *file;
	jstring str;
	off_t o;
	hsize_t s;

	if (name_size < 0) {
		h5badArgument( env, "H5Pget_external:  name_size < 0");
		return -1;
	}
	else if (name_size == 0) {
		file = NULL;
	}
	else {
		file = (char *)malloc(sizeof(char)*name_size);
	}

	if (size != NULL) {
#ifdef __cplusplus
		if (env->GetArrayLength(size) < 2) {
			free(file);
			h5badArgument( env, "H5Pget_external:  size input array < 2");
		}
		theArray = (jlong *)env->GetLongArrayElements(size,&isCopy);
#else
		if ((*env)->GetArrayLength(env, size) < 2) {
			free(file);
			h5badArgument( env, "H5Pget_external:  size input array < 2");
		}
		theArray = (jlong *)(*env)->GetLongArrayElements(env,size,&isCopy);
#endif
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
#ifdef __cplusplus
			env->ReleaseLongArrayElements(size,theArray,JNI_ABORT);
#else
			(*env)->ReleaseLongArrayElements(env,size,theArray,JNI_ABORT);
#endif
		}
		free(file);
		h5libraryError(env);
		return -1;
	}
	
	if (size != NULL) {
		theArray[0] = o;
		theArray[1] = s;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(size,theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,size,theArray,0);
#endif
	}

	if (file != NULL) {
		/*  NewStringUTF may throw OutOfMemoryError */
#ifdef __cplusplus
		str = env->NewStringUTF(file);
#else
		str = (*env)->NewStringUTF(env,file);
#endif
		if (str == NULL) {
			free(file);
			h5JNIFatalError( env, "H5Pget_external:  return array not created");
			return -1;
		}
		/*  SetObjectArrayElement may raise exceptions */
#ifdef __cplusplus
		env->SetObjectArrayElement(name,0,(jobject)str);
#else
		(*env)->SetObjectArrayElement(env,name,0,(jobject)str);
#endif
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
	/*
	unimplemented( env, "H5Pset_fill_value:  not implemented yet");
	return -1;
	*/
	jint status;
	jbyte *byteP;
	jboolean isCopy;

#ifdef __cplusplus
	byteP = env->GetByteArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,value,&isCopy);
#endif
	status = H5Pset_fill_value((hid_t)plist_id, (hid_t)type_id, byteP);
#ifdef __cplusplus
	env->ReleaseByteArrayElements(value,byteP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,value,byteP,JNI_ABORT);
#endif

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
	jint status;
	jbyte *byteP;
	jboolean isCopy;

#ifdef __cplusplus
	byteP = env->GetByteArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,value,&isCopy);
#endif
	status = H5Pget_fill_value((hid_t)plist_id, (hid_t)type_id, byteP);

#ifdef __cplusplus
	if (status < 0)
		env->ReleaseByteArrayElements(value,byteP,JNI_ABORT);
	else 
		env->ReleaseByteArrayElements(value,byteP,0);
#else
	if (status < 0)
		(*env)->ReleaseByteArrayElements(env,value,byteP,JNI_ABORT);
	else 
		(*env)->ReleaseByteArrayElements(env,value,byteP,0);
#endif

	return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_filter
 * Signature: (IIII[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter, jint flags, 
  jint cd_nelmts, jintArray cd_values)
{
	herr_t status;
	jint *theArray;
	jboolean isCopy;

	if (cd_values == NULL)
		status = H5Pset_filter((hid_t)plist, (H5Z_filter_t)filter, 
			(unsigned int)flags, (size_t)cd_nelmts, NULL);
	else
	{
#ifdef __cplusplus
		theArray = (jint *)env->GetIntArrayElements(cd_values,&isCopy);
#else
		theArray = (jint *)(*env)->GetIntArrayElements(env,cd_values,&isCopy);
#endif
		if (theArray == NULL) {
			h5JNIFatalError(env,  "H5Pset_filter:  input array  not pinned");
			return -1;
		}
		status = H5Pset_filter((hid_t)plist, (H5Z_filter_t)filter, 
			(unsigned int)flags, (size_t)cd_nelmts, (const unsigned int *)theArray);
#ifdef __cplusplus
		env->ReleaseIntArrayElements(cd_values,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,cd_values,theArray,JNI_ABORT);
#endif
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
	retVal =  H5Pget_nfilters((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter
 * Signature: (II[I[IILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter_number, jintArray flags,
  jintArray cd_nelmts, jintArray cd_values, jint namelen, jobjectArray name)
{ 
	herr_t status;
	jint *flagsArray, *cd_nelmtsArray, *cd_valuesArray;
	jboolean isCopy;
	char *filter;
	jstring str;

	if (namelen <= 0) {
		h5badArgument( env, "H5Pget_filter:  namelen <= 0");
		return -1;
	}
	if (flags == NULL) {
		h5badArgument( env, "H5Pget_filter:  flags is NULL");
		return -1;
	}
	if (cd_nelmts == NULL) {
		h5badArgument( env, "H5Pget_filter:  cd_nelmts is NULL");
		return -1;
	}
	if (cd_values == NULL) {
		h5badArgument( env, "H5Pget_filter:  cd_values is NULL");
		return -1;
	}
	filter = (char *)malloc(sizeof(char)*namelen);
	if (filter == NULL) {
		h5outOfMemory( env, "H5Pget_filter:  namelent malloc failed");
		return -1;
	}
#ifdef __cplusplus
	flagsArray = (jint *)env->GetIntArrayElements(flags,&isCopy);
#else
	flagsArray = (jint *)(*env)->GetIntArrayElements(env,flags,&isCopy);
#endif
	if (flagsArray == NULL) {
		free(filter);
		h5JNIFatalError(env,  "H5Pget_filter:  flags array not pinned");
		return -1;
	}
#ifdef __cplusplus
	cd_nelmtsArray = (jint *)env->GetIntArrayElements(cd_nelmts,&isCopy);
#else
	cd_nelmtsArray = (jint *)(*env)->GetIntArrayElements(env,cd_nelmts,&isCopy);
#endif
	if (cd_nelmtsArray == NULL) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(flags,flagsArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,flags,flagsArray,JNI_ABORT);
#endif
		free(filter);
		h5JNIFatalError(env,  "H5Pget_filter:  nelmts array not pinned");
		return -1;
	}
#ifdef __cplusplus
	cd_valuesArray = (jint *)env->GetIntArrayElements(cd_values,&isCopy);
#else
	cd_valuesArray = (jint *)(*env)->GetIntArrayElements(env,cd_values,&isCopy);
#endif
	if (cd_valuesArray == NULL)  {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(cd_nelmts,cd_nelmtsArray,JNI_ABORT);
		env->ReleaseIntArrayElements(flags,flagsArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,cd_nelmts,cd_nelmtsArray,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,flags,flagsArray,JNI_ABORT);
#endif
		free(filter);
		h5JNIFatalError(env,  "H5Pget_filter:  elmts array not pinned");
		return -1;
	}

	status = H5Pget_filter((hid_t)plist, (int)filter_number, (unsigned int *)flagsArray,
          (size_t *)cd_nelmtsArray, (unsigned int *)cd_valuesArray, (size_t)namelen, filter);

	if (status < 0)
	{
#ifdef __cplusplus
		env->ReleaseIntArrayElements(cd_values,cd_valuesArray,JNI_ABORT);
		env->ReleaseIntArrayElements(cd_nelmts,cd_nelmtsArray,JNI_ABORT);
		env->ReleaseIntArrayElements(flags,flagsArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,cd_values,cd_valuesArray,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,cd_nelmts,cd_nelmtsArray,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,flags,flagsArray,JNI_ABORT);
#endif
		free(filter);
		h5libraryError(env);
	}
	else
	{
#ifdef __cplusplus
		env->ReleaseIntArrayElements(cd_values,cd_valuesArray,0);
		env->ReleaseIntArrayElements(cd_nelmts,cd_nelmtsArray,0);
		env->ReleaseIntArrayElements(flags,flagsArray,0);
		/*  NewStringUTF may throw OutOfMemoryError */
		str = env->NewStringUTF(filter);
#else
		(*env)->ReleaseIntArrayElements(env,cd_values,cd_valuesArray,0);
		(*env)->ReleaseIntArrayElements(env,cd_nelmts,cd_nelmtsArray,0);
		(*env)->ReleaseIntArrayElements(env,flags,flagsArray,0);
		/*  NewStringUTF may throw OutOfMemoryError */
		str = (*env)->NewStringUTF(env,filter);
#endif
		if (str == NULL) {
			free(filter);
			h5JNIFatalError(env,  "H5Pget_filter:  return string not pinned");
			return -1;
		}
		free(filter);
		/*  SetObjectArrayElement may throw exceptiosn */
#ifdef __cplusplus
		env->SetObjectArrayElement(name,0,(jobject)str);
#else
		(*env)->SetObjectArrayElement(env,name,0,(jobject)str);
#endif
	}

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
	retVal =  H5Pget_driver((hid_t) plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

#ifdef removed
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_stdio
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1stdio
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retVal = -1;
	retVal =  H5Pset_stdio((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_stdio
 * Signature: (I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1stdio
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retValue;
	retValue = H5Pget_stdio((hid_t)plist);

	if (retValue >= 0) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_sec2
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1sec2
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retVal = -1;
	retVal =  H5Pset_sec2((hid_t) plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_sec2
 * Signature: (I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1sec2
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retValue;

	retValue =  H5Pget_sec2((hid_t)plist);

	if (retValue >= 0) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_core
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1core
  (JNIEnv *env, jclass clss, jint plist, jint increment)
{
	herr_t retVal = -1;
	retVal =  H5Pset_core((hid_t)plist, (size_t)increment);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_core
 * Signature: (I[I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1core
  (JNIEnv *env, jclass clss, jint plist, jintArray increment)
{
	jboolean isCopy;
	herr_t status;
	jint *theArray = NULL;

	if (increment != NULL) {
		theArray = (jint *)(*env)->GetIntArrayElements(env,increment,&isCopy);
		if (theArray == NULL) {
			h5JNIFatalError(env,  "H5Pget_core:  input array not pinned");
			return JNI_FALSE;
		}
	}
	status = H5Pget_core((hid_t)plist, (size_t *)&(theArray[0]));

	if (status < 0) {
		(*env)->ReleaseIntArrayElements(env,increment,theArray,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,increment,theArray,0);
		return JNI_TRUE;
	}
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_split
 * Signature: (ILjava/lang/String;ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1split
  (JNIEnv *env, jclass clss, jint plist, jstring meta_ext, jint meta_plist,
  jstring raw_ext, jint raw_plist)
{
	herr_t status;
	char *meta, *raw;
	jboolean isCopy;

	if (meta_ext == NULL) {
		meta = (char *)NULL;
	} else {
		meta = (char *)(*env)->GetStringUTFChars(env,meta_ext,&isCopy);
		if (meta == NULL) {
			h5JNIFatalError(env,  "H5Pset_split:  meta not pinned");
			return -1;
		}
	}

	if (raw_ext == NULL) {
		raw = (char *)NULL;
	} else {
		raw = (char *)(*env)->GetStringUTFChars(env,raw_ext,&isCopy);
		if (raw == NULL) {
			(*env)->ReleaseStringUTFChars(env,meta_ext,meta);
			h5JNIFatalError(env,  "H5Pset_split:  raw not pinned");
			return -1;
		}
	}

	status = H5Pset_split((hid_t)plist, meta, (hid_t)meta_plist, raw, (hid_t)raw_plist);
	(*env)->ReleaseStringUTFChars(env,raw_ext,raw);
	(*env)->ReleaseStringUTFChars(env,meta_ext,meta);
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_split
 * Signature: (II[Ljava/lang/String;[II[Ljava/lang/String;[I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1split
  (JNIEnv *env, jclass clss, jint plist, jint meta_ext_size, jobjectArray meta_ext,
  jintArray meta_properties, jint raw_ext_size, jobjectArray raw_ext, 
  jintArray raw_properties)
{
	jint status;
	jint *metaArray, *rawArray;
	jboolean isCopy;
	char *meta, *raw;
	jstring meta_str, raw_str;

	if (meta_ext == NULL) {
		metaArray = NULL;
	} else {
		if (meta_ext_size <=0 ) {
			h5badArgument( env, "H5Pget_split:  meta_ext_size <=0");
		}
		meta = (char *)malloc(sizeof(char)*meta_ext_size);
		if (meta == NULL) {
			h5JNIFatalError(env,  "H5Pget_split:  meta not pinned");
			return -1;
		}
	}
	if (raw_ext == NULL ) {
		rawArray = NULL;
	} else {
		if (raw_ext_size <=0 ) {
			h5badArgument( env, "H5Pget_split:  raw_ext_size <=0");
			return -1;
		}
		raw = (char *)malloc(sizeof(char)*raw_ext_size);
		if (raw == NULL) {
			free(meta);
			h5JNIFatalError(env,  "H5Pget_split:  raw not pinned");
			return -1;
		}
	}
	metaArray = (jint *)(*env)->GetIntArrayElements(env,meta_properties,&isCopy);
	if (metaArray == NULL) {
		free(raw);
		free(meta);
		h5JNIFatalError(env,  "H5Pget_split:  metaArray not pinned");
		return -1;
	}
	rawArray = (jint *)(*env)->GetIntArrayElements(env,raw_properties,&isCopy);
	if (rawArray == NULL) {
		(*env)->ReleaseIntArrayElements(env,meta_properties,metaArray,JNI_ABORT);
		free(raw);
		free(meta);
		h5JNIFatalError(env,  "H5Pget_split:  rawArray not pinned");
		return -1;
	}

	status = H5Pget_split((hid_t)plist, (size_t)meta_ext_size, meta,
		(hid_t *)metaArray, (size_t)raw_ext_size, raw, (hid_t *)rawArray);

	if (status < 0)
	{
		(*env)->ReleaseIntArrayElements(env,raw_properties,rawArray,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,meta_properties,metaArray,JNI_ABORT);
		free(raw);
		free(meta);
		h5libraryError(env);
	}
	else
	{
		(*env)->ReleaseIntArrayElements(env,raw_properties,rawArray,0);
		(*env)->ReleaseIntArrayElements(env,meta_properties,metaArray,0);
		/*  NewStringUTF may throw OutOfMemoryError */
		meta_str = (*env)->NewStringUTF(env,meta);
		if (meta_str == NULL) {
			free(raw);
			free(meta);
			h5JNIFatalError(env,  "H5Pget_split:  return meta_str not pinned");
			return -1;
		}
		/*  SetObjectArrayElement may throw exceptions */
		(*env)->SetObjectArrayElement(env,meta_ext,0,(jobject)meta_str);
		free(meta);
		/*  NewStringUTF may throw OutOfMemoryError */
		raw_str = (*env)->NewStringUTF(env,raw);
		if (meta_str == NULL) {
			free(raw);
			h5JNIFatalError(env,  "H5Pget_split:  return raw_str not pinned");
			return -1;
		}
		/*  SetObjectArrayElement may throw exceptions */
		(*env)->SetObjectArrayElement(env,raw_ext,0,(jobject)raw_str);
		free(raw);
	}

	return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_family
 * Signature: (IJI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1family
  (JNIEnv *env, jclass clss, jint plist, jlong memb_size, jint memb_plist)
{
	long ms;
	herr_t retVal = -1;
	ms = memb_size;
	retVal =  H5Pset_family((hid_t)plist, (hsize_t)ms, (hid_t)memb_plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_family
 * Signature: (I[JI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1family
  (JNIEnv *env, jclass clss, jint tid, jlongArray memb_size, jintArray memb_plist)
{
	herr_t status;
	jlong *sizeArray;
	jint *plistArray;
	jboolean isCopy;
	hsize_t *sa;
	int i;
	int rank;

	if (memb_size == NULL) {
		h5nullArgument( env, "H5Pget_family:  memb_size is NULL");
		return -1; 
	}
	if (memb_plist == NULL) {
		h5nullArgument( env, "H5Pget_family:  memb_plist is NULL");
		return -1; 
	}
	sizeArray = (jlong *)(*env)->GetLongArrayElements(env,memb_size,&isCopy);
	if (sizeArray == NULL) {
		h5JNIFatalError(env,  "H5Pget_family:  sizeArray not pinned");
		return -1; 
	}
	rank  = (*env)->GetArrayLength(env, memb_size);
	sa = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
	if (sa == NULL) {
		(*env)->ReleaseLongArrayElements(env,memb_size,sizeArray,JNI_ABORT);
		h5JNIFatalError(env,  "H5Screate-simple:  dims not converted to hsize_t");
		return -1;
	}
	plistArray = (jint *)(*env)->GetIntArrayElements(env,memb_plist,&isCopy);
	if (plistArray == NULL) {
		(*env)->ReleaseLongArrayElements(env,memb_size,sizeArray,JNI_ABORT);
		h5JNIFatalError(env,  "H5Pget_family:  plistArray not pinned");
		return -1; 
	}
	status = H5Pget_family((hid_t)tid, sa, (hid_t *)plistArray); 
	
	if (status < 0)
	{
		free(sa);
		(*env)->ReleaseLongArrayElements(env,memb_size,sizeArray,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,memb_plist,plistArray,JNI_ABORT);
		h5libraryError(env);
	}
	else
	{
		for (i= 0; i < rank; i++) {	
			sa[i] = sizeArray[i];
		}
		free(sa); 
		(*env)->ReleaseLongArrayElements(env,memb_size,sizeArray,0);
		(*env)->ReleaseIntArrayElements(env,memb_plist,plistArray,0);
	}

	return (jint)status;
}
#endif

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_cache
 * Signature: (IIID)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1cache
  (JNIEnv *env, jclass clss, jint plist, jint mdc_nelmts, jint rdcc_nelmts,
  jint rdcc_nbytes, jdouble rdcc_w0)
{
	herr_t retVal = -1;
	retVal =  H5Pset_cache((hid_t)plist, (int)mdc_nelmts, (int)rdcc_nelmts,
		(size_t)rdcc_nbytes, (double) rdcc_w0);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_cache
 * Signature: (I[I[I[D)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1cache
  (JNIEnv *env, jclass clss, jint plist, jintArray mdc_nelmts, 
  jintArray rdcc_nelmts, jintArray rdcc_nbytes, jdoubleArray rdcc_w0)
{
	herr_t status;
	jint mode;
	jdouble *w0Array;
	jint *mdc_nelmtsArray, *rdcc_nelmtsArray, *nbytesArray;
	jboolean isCopy;

	if (mdc_nelmts == NULL) {
		h5nullArgument( env, "H5Pget_gache:  mdc_nelmts is NULL");
		return -1;
	}
#ifdef __cplusplus
	mdc_nelmtsArray = (jint *)env->GetIntArrayElements(mdc_nelmts,&isCopy);
#else
	mdc_nelmtsArray = (jint *)(*env)->GetIntArrayElements(env,mdc_nelmts,&isCopy);
#endif
	if (mdc_nelmtsArray == NULL) {
		h5JNIFatalError(env,  "H5Pget_cache:  mdc_nelmts array not pinned");
		return -1;
	}

	if (rdcc_w0 == NULL) {
		w0Array = (jdouble *)NULL;
	} else {
#ifdef __cplusplus
		w0Array = (jdouble *)env->GetDoubleArrayElements(rdcc_w0,&isCopy);
#else
		w0Array = (jdouble *)(*env)->GetDoubleArrayElements(env,rdcc_w0,&isCopy);
#endif
		if (w0Array == NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
#else
			(*env)->ReleaseIntArrayElements(env,mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
#endif
			h5JNIFatalError(env,  "H5Pget_cache:  w0_array array not pinned");
			return -1;
		}
	}

	if (rdcc_nelmts == NULL) {
		rdcc_nelmtsArray = (jint *) NULL;
	} else {
#ifdef __cplusplus
		rdcc_nelmtsArray = (jint *)env->GetIntArrayElements(rdcc_nelmts,&isCopy);
#else
		rdcc_nelmtsArray = (jint *)(*env)->GetIntArrayElements(env,rdcc_nelmts,&isCopy);
#endif
		if (rdcc_nelmtsArray == NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
#else
			(*env)->ReleaseIntArrayElements(env,mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
#endif
			/* exception -- out of memory */
			if (w0Array != NULL) {
#ifdef __cplusplus
				env->ReleaseDoubleArrayElements(rdcc_w0,w0Array,JNI_ABORT);
#else
				(*env)->ReleaseDoubleArrayElements(env,rdcc_w0,w0Array,JNI_ABORT);
#endif
			}
			h5JNIFatalError(env,  "H5Pget_cache:  rdcc_nelmts array not pinned");
			return -1;
		}
	}
	
	if (rdcc_nbytes == NULL) {
		nbytesArray = (jint *) NULL;
	} else {
#ifdef __cplusplus
		nbytesArray = (jint *)env->GetIntArrayElements(rdcc_nbytes,&isCopy);
#else
		nbytesArray = (jint *)(*env)->GetIntArrayElements(env,rdcc_nbytes,&isCopy);
#endif
		if (nbytesArray == NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
			if (w0Array != NULL) {
				env->ReleaseDoubleArrayElements(rdcc_w0,w0Array,JNI_ABORT);
			}
			if (rdcc_nelmtsArray != NULL) {
				env->ReleaseIntArrayElements(rdcc_nelmts,rdcc_nelmtsArray,JNI_ABORT);
			}
#else
			(*env)->ReleaseIntArrayElements(env,mdc_nelmts,mdc_nelmtsArray,JNI_ABORT);
			if (w0Array != NULL) {
				(*env)->ReleaseDoubleArrayElements(env,rdcc_w0,w0Array,JNI_ABORT);
			}
			if (rdcc_nelmtsArray != NULL) {
				(*env)->ReleaseIntArrayElements(env,rdcc_nelmts,rdcc_nelmtsArray,JNI_ABORT);
			}
#endif
			h5JNIFatalError(env,  "H5Pget_cache:  nbytesArray array not pinned");
			return -1;
		}
	}

	status = H5Pget_cache((hid_t)plist, (int *)mdc_nelmtsArray, (int *)rdcc_nelmtsArray, (size_t *)nbytesArray,
		w0Array);
	
	if (status < 0) {
		mode = JNI_ABORT;
	} else {
		mode = 0; /* commit and free */
	}

#ifdef __cplusplus
	env->ReleaseIntArrayElements(mdc_nelmts,mdc_nelmtsArray,mode);
#else
	(*env)->ReleaseIntArrayElements(env,mdc_nelmts,mdc_nelmtsArray,mode);
#endif

	if (rdcc_nelmtsArray != NULL) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(rdcc_nelmts,rdcc_nelmtsArray,mode);
#else
		(*env)->ReleaseIntArrayElements(env,rdcc_nelmts,rdcc_nelmtsArray,mode);
#endif
	}

	if (nbytesArray != NULL) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(rdcc_nbytes,nbytesArray,mode);
#else
		(*env)->ReleaseIntArrayElements(env,rdcc_nbytes,nbytesArray,mode);
#endif
	}

	if (w0Array != NULL) { 
#ifdef __cplusplus
		env->ReleaseDoubleArrayElements(rdcc_w0,w0Array,mode);
#else
		(*env)->ReleaseDoubleArrayElements(env,rdcc_w0,w0Array,mode);
#endif
	}

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

#ifdef notdef

/* DON'T IMPLEMENT THIS!!! */
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_buffer
 * Signature: (II[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1buffer
  (JNIEnv *env, jclass clss, jint plist, jint size, jbyteArray tconv, jbyteArray bkg)
{
	jint status;
	jbyte *tconvP, *bkgP;
	jboolean isCopy;

	if (tconv == NULL)
		tconvP = (jbyte *)NULL;
	else 
		tconvP = (*env)->GetByteArrayElements(env,tconv,&isCopy);
	if (bkg == NULL)
		bkgP = (jbyte *)NULL;
	else
		bkgP = (*env)->GetByteArrayElements(env,bkg,&isCopy);

	status = H5Pset_buffer((hid_t)plist, (size_t)size, tconvP, bkgP);

	if (tconv != NULL)
		(*env)->ReleaseByteArrayElements(env,tconv,tconvP,JNI_ABORT);
	if (bkg != NULL)
		(*env)->ReleaseByteArrayElements(env,bkg,bkgP,JNI_ABORT);

	return status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_buffer
 * Signature: (I[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1buffer
  (JNIEnv *env, jclass clss, jint plist, jbyteArray tconv, jbyteArray bkg)
{
	jint status;
	jbyte *tconvP, *bkgP;
	jboolean isCopy;

	tconvP = (*env)->GetByteArrayElements(env,tconv,&isCopy);
	bkgP = (*env)->GetByteArrayElements(env,bkg,&isCopy);
	status = H5Pget_buffer((hid_t)plist, tconvP, bkgP);
	
	if (status < 0)
	{
		(*env)->ReleaseByteArrayElements(env,tconv,tconvP,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,bkg,bkgP,JNI_ABORT);
	}
	else 
	{
		(*env)->ReleaseByteArrayElements(env,tconv,tconvP,0);
		(*env)->ReleaseByteArrayElements(env,bkg,bkgP,0);
	}

	return status;
}
#endif

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_preserve
 * Signature: (IB)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1preserve
  (JNIEnv *env, jclass clss, jint plist, jboolean status)
{
	hbool_t st;
	herr_t retVal = -1;

	if (status == JNI_TRUE) {
		st = TRUE;
	} else if (status == JNI_FALSE) {
		st = FALSE;
	} else {
		/* exception -- bad argument */
		h5badArgument( env, "H5Pset_preserve:  status not TRUE or FALSE");
		return -1;
	}
	retVal =  H5Pset_preserve((hid_t)plist, (hbool_t)st);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_preserve
 * Signature: (I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1preserve
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retValue;
	retValue = H5Pget_preserve((hid_t)plist);
	if (retValue == TRUE) {
		return JNI_TRUE;
	} else if (retValue == FALSE) {
		return JNI_FALSE;
	} else {
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_deflate
 * Signature: (II)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1deflate
  (JNIEnv *env, jclass clss, jint plist, jint level)
{
	herr_t retValue;
	retValue = H5Pset_deflate((hid_t)plist, (int)level); 
	if (retValue == TRUE) {
		return JNI_TRUE;
	} else if (retValue == FALSE) {
		return JNI_FALSE;
	} else {
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_gc_references
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1gc_1references
  (JNIEnv *env, jclass clss, jint fapl_id, jboolean gc_ref)
  {
	herr_t retVal;
	unsigned gc_ref_val;
	if (gc_ref == JNI_TRUE) {
		gc_ref_val = 1;
	} else {
		gc_ref_val = 0;
	}
	retVal = H5Pset_gc_references((hid_t)fapl_id, gc_ref_val); 

	if (retVal < 0) {
		h5libraryError(env);
		return -1;
	}
	return (jint)retVal;
}

#ifdef remove
#ifdef USE_H5_1_2_1
#define GET_GC H5Pget_gc_reference
#else
#define GET_GC H5Pget_gc_references
#endif
#endif
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_gc_references
 * Signature: (I[Z)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1gc_1references
  (JNIEnv *env, jclass clss, jint fapl_id, jbooleanArray gc_ref)
{
	herr_t status;
	jboolean *theArray;
	jboolean isCopy;
	unsigned gc_ref_val = 0;

	if (gc_ref == NULL) {
		h5nullArgument( env, "H5Pget_gc_references:  gc_ref input array is NULL");
		return -1;
	}

#ifdef __cplusplus
	theArray = (jboolean *)env->GetBooleanArrayElements(gc_ref,&isCopy);
#else
	theArray = (jboolean *)(*env)->GetBooleanArrayElements(env,gc_ref,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_gc_references:  gc_ref not pinned");
		return -1;
	}

	status = H5Pget_gc_references((hid_t)fapl_id, (unsigned *)&gc_ref_val);
#ifdef removed
	status = GET_GC((hid_t)fapl_id, (unsigned *)&gc_ref_val);
#endif

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseBooleanArrayElements(gc_ref,theArray,JNI_ABORT);
#else
		(*env)->ReleaseBooleanArrayElements(env,gc_ref,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		if (gc_ref_val == 1) {
			theArray[0] = JNI_TRUE;
		} else {
			theArray[0] = JNI_FALSE;
		}
#ifdef __cplusplus
		env->ReleaseBooleanArrayElements(gc_ref,theArray,0);
#else
		(*env)->ReleaseBooleanArrayElements(env,gc_ref,theArray,0);
#endif
	}

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
	herr_t status;
	jdouble *leftP, *middleP, *rightP;
	jboolean isCopy;

	if (left == NULL) {
		h5nullArgument( env, "H5Pget_btree_ratios:  left input array is NULL");
		return -1;
	}

	if (middle == NULL) {
		h5nullArgument( env, "H5Pget_btree_ratios:  middle input array is NULL");
		return -1;
	}

	if (right == NULL) {
		h5nullArgument( env, "H5Pget_btree_ratios:  right input array is NULL");
		return -1;
	}

#ifdef __cplusplus
	leftP = (jdouble *)env->GetDoubleArrayElements(left,&isCopy);
#else
	leftP = (jdouble *)(*env)->GetDoubleArrayElements(env,left,&isCopy);
#endif
	if (leftP == NULL) {
		h5JNIFatalError( env, "H5Pget_btree_ratios:  left not pinned");
		return -1;
	}

#ifdef __cplusplus
	middleP = (jdouble *)env->GetDoubleArrayElements(middle,&isCopy);
#else
	middleP = (jdouble *)(*env)->GetDoubleArrayElements(env,middle,&isCopy);
#endif
	if (middleP == NULL) {
#ifdef __cplusplus
		env->ReleaseDoubleArrayElements(left,leftP,JNI_ABORT);
#else
		(*env)->ReleaseDoubleArrayElements(env,left,leftP,JNI_ABORT);
#endif
		h5JNIFatalError( env, "H5Pget_btree_ratios:  middle not pinned");
		return -1;
	}

#ifdef __cplusplus
	rightP = (jdouble *)env->GetDoubleArrayElements(right,&isCopy);
#else
	rightP = (jdouble *)(*env)->GetDoubleArrayElements(env,right,&isCopy);
#endif
	if (rightP == NULL) {
#ifdef __cplusplus
		env->ReleaseDoubleArrayElements(left,leftP,JNI_ABORT);
		env->ReleaseDoubleArrayElements(middle,middleP,JNI_ABORT);
#else
		(*env)->ReleaseDoubleArrayElements(env,left,leftP,JNI_ABORT);
		(*env)->ReleaseDoubleArrayElements(env,middle,middleP,JNI_ABORT);
#endif
		h5JNIFatalError( env, "H5Pget_btree_ratios:  middle not pinned");
		return -1;
	}

	status = H5Pget_btree_ratios((hid_t)plist_id, (double *)leftP,
		(double *)middleP, (double *)rightP);

	if (status < 0)  {
#ifdef __cplusplus
		env->ReleaseDoubleArrayElements(left,leftP,JNI_ABORT);
		env->ReleaseDoubleArrayElements(middle,middleP,JNI_ABORT);
		env->ReleaseDoubleArrayElements(right,rightP,JNI_ABORT);
#else
		(*env)->ReleaseDoubleArrayElements(env,left,leftP,JNI_ABORT);
		(*env)->ReleaseDoubleArrayElements(env,middle,middleP,JNI_ABORT);
		(*env)->ReleaseDoubleArrayElements(env,right,rightP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus
		env->ReleaseDoubleArrayElements(left,leftP,0);
		env->ReleaseDoubleArrayElements(middle,middleP,0);
		env->ReleaseDoubleArrayElements(right,rightP,0);
#else
		(*env)->ReleaseDoubleArrayElements(env,left,leftP,0);
		(*env)->ReleaseDoubleArrayElements(env,middle,middleP,0);
		(*env)->ReleaseDoubleArrayElements(env,right,rightP,0);
#endif
	}

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
	long sz;
	herr_t retVal = -1;
	sz = (long)size;
	retVal =  H5Pset_small_data_block_size((hid_t)plist, (hsize_t)sz );
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
	herr_t status;
	jlong *theArray;
	jboolean isCopy;
	hsize_t s;

	if (size == NULL) {
		/* exception ? */
		h5nullArgument( env, "H5Pget_small_user_block_size:  size is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jlong *)env->GetLongArrayElements(size,&isCopy);
#else
	theArray = (jlong *)(*env)->GetLongArrayElements(env,size,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_userblock:  size not pinned");
		return -1;
	}

	status = H5Pget_small_data_block_size((hid_t)plist, &s);

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(size,theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,size,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = s;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(size,theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,size,theArray,0);
#endif
	}

	return (jint)status;
}


/***************************************************************
 *                   New APIs for HDF5.1.6                     *
 ***************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_alloc_time(hid_t plist_id, H5D_alloc_time_t alloc_time ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1alloc_1time
  (JNIEnv *env, jclass clss, jint plist, jint alloc_time)
{
	herr_t retVal = -1;

	retVal =  H5Pset_alloc_time((hid_t)plist, (H5D_alloc_time_t)alloc_time );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_alloc_time(hid_t plist_id, H5D_alloc_time_t *alloc_time ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1alloc_1time
  (JNIEnv *env, jclass clss, jint plist, jintArray alloc_time)
{
	herr_t retVal = -1;
	jint *theArray;
	jboolean isCopy;
	H5D_alloc_time_t time;


	if (alloc_time == NULL) {
		/* exception ? */
		h5nullArgument( env, "H5Pget_alloc_time:  alloc_time is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jint *)env->GetIntArrayElements(alloc_time,&isCopy);
#else
	theArray = (jint *)(*env)->GetIntArrayElements(env,alloc_time,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_alloc_time:  alloc_time not pinned");
		return -1;
	}

	retVal =  H5Pget_alloc_time((hid_t)plist, &time );

	if (retVal < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(alloc_time,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,alloc_time,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = time;
#ifdef __cplusplus
		env->ReleaseIntArrayElements(alloc_time,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,alloc_time,theArray,0);
#endif
	}

	return (jint)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fill_time(hid_t plist_id, H5D_fill_time_t fill_time ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fill_1time
  (JNIEnv *env, jclass clss, jint plist, jint fill_time)
{
	herr_t retVal = -1;

	retVal =  H5Pset_fill_time((hid_t)plist, (H5D_fill_time_t)fill_time );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_fill_time(hid_t plist_id, H5D_fill_time_t *fill_time ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1fill_1time
  (JNIEnv *env, jclass clss, jint plist, jintArray fill_time)
{
	herr_t retVal = -1;
	jint *theArray;
	jboolean isCopy;
	H5D_fill_time_t time;


	if (fill_time == NULL) {
		/* exception ? */
		h5nullArgument( env, "H5Pget_fill_time:  fill_time is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jint *)env->GetIntArrayElements(fill_time,&isCopy);
#else
	theArray = (jint *)(*env)->GetIntArrayElements(env,fill_time,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_fill_time:  fill_time not pinned");
		return -1;
	}

	retVal =  H5Pget_fill_time((hid_t)plist, &time );


	if (retVal < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(fill_time,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,fill_time,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = time;
#ifdef __cplusplus
		env->ReleaseIntArrayElements(fill_time,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,fill_time,theArray,0);
#endif
	}

	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pfill_value_defined(hid_t plist_id, H5D_fill_value_t *status ) 
 * Signature: (IJ)I
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
		h5nullArgument( env, "H5Pfill_value_defined:  status is NULL");
		return -1;
	}
#ifdef __cplusplus
	theArray = (jint *)env->GetIntArrayElements(status,&isCopy);
#else
	theArray = (jint *)(*env)->GetIntArrayElements(env,status,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pfill_value_defined:  status not pinned");
		return -1;
	}

	retVal =  H5Pfill_value_defined((hid_t)plist, &value );

	if (retVal < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(status,theArray,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,status,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = value;
#ifdef __cplusplus
		env->ReleaseIntArrayElements(status,theArray,0);
#else
		(*env)->ReleaseIntArrayElements(env,status,theArray,0);
#endif
	}

	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_fletcher32(hid_t plist) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1fletcher32
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retVal = -1;

	retVal =  H5Pset_fletcher32((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_edc_check(hid_t plist, H5Z_EDC_t check) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1edc_1check
  (JNIEnv *env, jclass clss, jint plist, jint check)
{
	herr_t retVal = -1;

	retVal =  H5Pset_edc_check((hid_t)plist, (H5Z_EDC_t)check );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_edc_check(hid_t plist) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1edc_1check
  (JNIEnv *env, jclass clss, jint plist)
{
	H5Z_EDC_t retVal = -1;

	retVal =  H5Pget_edc_check((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_shuffle(hid_t plist_id) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1shuffle
  (JNIEnv *env, jclass clss, jint plist)
{
	herr_t retVal = -1;

	retVal =  H5Pset_shuffle((hid_t)plist);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_szip(hid_t plist, unsigned int options_mask, unsigned int pixels_per_block) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1szip
  (JNIEnv *env, jclass clss, jint plist, jint options_mask, jint pixels_per_block)
{
	herr_t retVal = -1;

	retVal =  H5Pset_szip((hid_t)plist, (unsigned int)options_mask, (unsigned int)pixels_per_block);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pset_hyper_vector_size(hid_t dxpl_id, size_t vector_size ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pset_1hyper_1vector_1size
  (JNIEnv *env, jclass clss, jint plist, jlong vector_size)
{
	herr_t retVal = -1;

	retVal =  H5Pset_hyper_vector_size((hid_t)plist, (size_t)vector_size);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_hyper_vector_size(hid_t dxpl_id, size_t *vector_size ) 
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1hyper_1vector_1size
  (JNIEnv *env, jclass clss, jint plist, jlongArray vector_size)
{
	herr_t retVal = -1;
	jlong *theArray;
	size_t size;
	jboolean isCopy;

	if (vector_size == NULL) {
		/* exception ? */
		h5nullArgument( env, "H5Pget_hyper_vector_size:  vector_size is NULL");
		return -1;
	}

#ifdef __cplusplus
	theArray = (jlong *)env->GetLongArrayElements(vector_size,&isCopy);
#else
	theArray = (jlong *)(*env)->GetLongArrayElements(env,vector_size,&isCopy);
#endif
	if (theArray == NULL) {
		h5JNIFatalError( env, "H5Pget_hyper_vector_size:  vector_size not pinned");
		return -1;
	}

	retVal =  H5Pget_hyper_vector_size((hid_t)plist, &size);

	if (retVal < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(vector_size,theArray,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,vector_size,theArray,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		theArray[0] = size;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(vector_size,theArray,0);
#else
		(*env)->ReleaseLongArrayElements(env,vector_size,theArray,0);
#endif
	}

	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pall_filters_avail(hid_t dcpl_id) 
 * Signature: (I)J
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pall_1filters_1avail
  (JNIEnv *env, jclass clss, jint dcpl_id)
{
	htri_t bval;
	bval = H5Pall_filters_avail((hid_t)dcpl_id);
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
 * Method:    H5Pmodify_filter(hid_t plist, H5Z_filter_t filter, 
 *                unsigned int flags, size_t cd_nelmts, const unsigned int cd_values[] ) 
 * Signature: (III[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pmodify_1filter
  (JNIEnv *env, jclass clss, jint plist, jint filter, jint flags, 
  jlong cd_nelmts, jintArray cd_values)
{
	herr_t status;
	jint *cd_valuesP;
	jboolean isCopy;

	if (cd_values == NULL) {
		h5nullArgument( env, "H5Pmodify_filter:  cd_values is NULL");
		return -1;
	}

#ifdef __cplusplus
	cd_valuesP = (*env)->GetIntArrayElements(cd_values,&isCopy);
#else
	cd_valuesP = (*env)->GetIntArrayElements(env,cd_values,&isCopy);
#endif

	if (cd_valuesP == NULL) {
		h5JNIFatalError(env,  "H5Pmodify_filter:  cd_values not pinned");
		return -1;
	}

	status = H5Pmodify_filter((hid_t)plist, (H5Z_filter_t)filter,(const unsigned int)flags, 
		(size_t)cd_nelmts, (unsigned int *)cd_valuesP);

#ifdef __cplusplus
	(*env)->ReleaseIntArrayElements(cd_values, cd_valuesP, JNI_ABORT);
#else
	(*env)->ReleaseIntArrayElements(env, cd_values, cd_valuesP, JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Pget_filter_by_id( hid_t plist_id, H5Z_filter_t filter, 
 *                unsigned int *flags, size_t *cd_nelmts, unsigned int cd_values[], 
 *                size_t namelen, char *name[] ) 
 * Signature: (III[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Pget_1filter_1by_1id
  (JNIEnv *env, jclass clss, jint plist, jint filter, jintArray flags, 
  jlongArray cd_nelmts, jintArray cd_values, jlong namelen, jobjectArray name)
{
	herr_t status;
	int i=0;
	jint *cd_valuesP, *flagsP;
	jlong *cd_nelmsP;
	jboolean isCopy;
	size_t *nelmsP;
	int rank;
	long bs;
	char *aName;
	jstring str;

	bs = (long)namelen;
	if (bs <= 0) {
		h5badArgument( env, "H5Pget_filter_by_id:  namelen <= 0");
		return -1;
	}

	if (flags == NULL) {
		h5nullArgument( env, "H5Pget_filter_by_id:  flags is NULL");
		return -1;
	}

	if (cd_nelmts == NULL) {
		h5nullArgument( env, "H5Pget_filter_by_id:  cd_nelms is NULL");
		return -1;
	}

	if (cd_values == NULL) {
		h5nullArgument( env, "H5Pget_filter_by_id:  cd_values is NULL");
		return -1;
	}

	if (name == NULL) {
		h5nullArgument( env, "H5Pget_filter_by_id:  name is NULL");
		return -1;
	}

	aName = (char*)malloc(sizeof(char)*bs);
	if (aName == NULL) {
		h5outOfMemory( env, "H5Pget_filter_by_id:  malloc failed");
		return -1;
	}

#ifdef __cplusplus
	flagsP = (*env)->GetIntArrayElements(flags,&isCopy);
#else
	flagsP = (*env)->GetIntArrayElements(env,flags,&isCopy);
#endif

	if (flagsP == NULL) {
		free(aName);
		h5JNIFatalError(env,  "H5Pget_filter_by_id:  flags not pinned");
		return -1;
	}

#ifdef __cplusplus
	cd_nelmsP = (*env)->GetLongArrayElements(cd_nelmts,&isCopy);
#else
	cd_nelmsP = (*env)->GetLongArrayElements(env,cd_nelmts,&isCopy);
#endif

	if (cd_nelmsP == NULL) {
		free(aName);
		h5JNIFatalError(env,  "H5Pget_filter_by_id:  cd_nelms not pinned");
		return -1;
	}

	nelmsP = (size_t *)malloc( sizeof(size_t)); 

	if (nelmsP == NULL) {
#ifdef __cplusplus
		(*env)->ReleaseIntArrayElements(flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(cd_nelmts,cd_nelmsP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,cd_nelmts,cd_nelmsP,JNI_ABORT);
#endif
		free(aName);
		h5JNIFatalError(env,  "H5Pget_filter_by_id:  cd_nelmts array not converted to unsigned int.");
		return -1;
	}

#ifdef __cplusplus
	cd_valuesP = (*env)->GetIntArrayElements(cd_values,&isCopy);
	rank  = (*env)->GetArrayLength(env, cd_values);
#else
	cd_valuesP = (*env)->GetIntArrayElements(env,cd_values,&isCopy);
	rank  = (*env)->GetArrayLength(env, cd_values);
#endif

	if (cd_valuesP == NULL) {
#ifdef __cplusplus
		(*env)->ReleaseIntArrayElements(flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(cd_nelmts,cd_nelmsP,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(cd_values,cd_valuesP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,cd_nelmts,cd_nelmsP,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,cd_values,cd_valuesP,JNI_ABORT);
#endif
		free(aName);
		free(nelmsP);
		h5JNIFatalError(env,  "H5Pget_filter_by_id:  cd_values array not converted to unsigned int.");
		return -1;
	}

	status = H5Pget_filter_by_id( (hid_t)plist, (H5Z_filter_t)filter, 
		(unsigned int *)flagsP, (size_t *)nelmsP, (unsigned int *)cd_valuesP, 
		(size_t)namelen, (char *)aName);

	if (status < 0) {
#ifdef __cplusplus
		(*env)->ReleaseIntArrayElements(flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(cd_nelmts,cd_nelmsP,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(cd_values,cd_valuesP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,flags,flagsP,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,cd_nelmts,cd_nelmsP,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,cd_values,cd_valuesP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus

		cd_nelmsP[0] = nelmsP[0];

		str = env->NewStringUTF(aName);
		(*env)->ReleaseIntArrayElements(flags,flagsP,0);
		(*env)->ReleaseLongArrayElements(cd_nelmts,cd_nelmsP,0);
		(*env)->ReleaseIntArrayElements(cd_values,cd_valuesP,0);
#else
		str = (*env)->NewStringUTF(env, aName);
		(*env)->ReleaseIntArrayElements(env,flags,flagsP,0);
		(*env)->ReleaseLongArrayElements(env,cd_nelmts,cd_nelmsP,0);
		(*env)->ReleaseIntArrayElements(env,cd_values,cd_valuesP,0);
#endif

	}

	free(aName);
	free(nelmsP);

	return (jint)status;
}

#ifdef __cplusplus
}
#endif 
