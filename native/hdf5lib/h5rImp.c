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
 *  Reference API Functions of the HDF5 library. 
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

extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rcreate
 * Signature: ([BILjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rcreate
  (JNIEnv *env, jclass clss, 
  jbyteArray ref, jint loc_id, jstring name, jint ref_type, jint space_id)
{
	char* rName;
	jboolean isCopy;
	herr_t status;
	jbyte *refP;

	if (ref == NULL) {
		h5nullArgument( env, "H5Rcreate:  ref is NULL");
		return -1;
	}
	if (name == NULL) {
		h5nullArgument( env, "H5Rcreate:  name is NULL");
		return -1;
	}
	if (ref_type == H5R_OBJECT) {
#ifdef __cplusplus
		if (env->GetArrayLength(ref) < 8) {
			h5badArgument( env, "H5Rcreate:  ref input array < 8");
			return -1;
		}
#else
		if ((*env)->GetArrayLength(env, ref) < 8) {
			h5badArgument( env, "H5Rcreate:  ref input array < 8");
			return -1;
		}
#endif
	} else if (ref_type == H5R_DATASET_REGION) {
#ifdef __cplusplus
		if (env->GetArrayLength( ref) < 12) {
			h5badArgument( env, "H5Rcreate:  region ref input array < 12");
			return -1;
		}
#else
		if ((*env)->GetArrayLength(env, ref) < 12) {
			h5badArgument( env, "H5Rcreate:  region ref input array < 12");
			return -1;
		}
#endif
	} else {
		h5badArgument( env, "H5Rcreate:  ref_type unknown type ");
		return -1;
	}

#ifdef __cplusplus
	refP = (jbyte *)env->GetByteArrayElements(ref,&isCopy);
#else
	refP = (jbyte *)(*env)->GetByteArrayElements(env,ref,&isCopy);
#endif
	if (refP == NULL) {
		h5JNIFatalError(env,  "H5Rcreate:  ref not pinned");
		return -1;
	}
#ifdef __cplusplus
	rName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	rName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (rName == NULL) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(ref,refP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,ref,refP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Rcreate:  name not pinned");
		return -1;
	}

	status = H5Rcreate(refP, loc_id, rName, (H5R_type_t)ref_type, space_id);
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,rName);
		env->ReleaseByteArrayElements(ref,refP,JNI_ABORT);
#else
		(*env)->ReleaseStringUTFChars(env,name,rName);
		(*env)->ReleaseByteArrayElements(env,ref,refP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,rName);
		env->ReleaseByteArrayElements(ref,refP,0);
#else
		(*env)->ReleaseStringUTFChars(env,name,rName);
		(*env)->ReleaseByteArrayElements(env,ref,refP,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rdereference
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rdereference
  (JNIEnv *env, jclass clss, jint dataset, jint ref_type,
  jbyteArray ref )
{
	jboolean isCopy;
	jbyte *refP;
	herr_t status;

	if (ref == NULL) {
		h5nullArgument( env, "H5Rdereference:  ref is NULL");
		return -1;
	}
#ifdef __cplusplus
	if ((ref_type == H5R_OBJECT) && env->GetArrayLength(ref) < 8) {
		h5badArgument( env, "H5Rdereference:  obj ref input array < 8");
	} else if ((ref_type == H5R_DATASET_REGION)  
		&& env->GetArrayLength(ref) < 12) {
		h5badArgument( env, "H5Rdereference:  region ref input array < 12");
	}
	refP = (jbyte *)env->GetByteArrayElements(ref,&isCopy);
#else
	if ((ref_type == H5R_OBJECT) && (*env)->GetArrayLength(env, ref) < 8) {
		h5badArgument( env, "H5Rdereference:  obj ref input array < 8");
	} else if ((ref_type == H5R_DATASET_REGION)  
		&& (*env)->GetArrayLength(env, ref) < 12) {
		h5badArgument( env, "H5Rdereference:  region ref input array < 12");
	}
	refP = (jbyte *)(*env)->GetByteArrayElements(env,ref,&isCopy);
#endif
	if (refP == NULL) {
		h5JNIFatalError(env,  "H5Rderefernce:  ref not pinned");
		return -1;
	}

	status = H5Rdereference((hid_t)dataset, (H5R_type_t)ref_type, refP);

#ifdef __cplusplus
	env->ReleaseByteArrayElements(ref,refP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,ref,refP,JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rget_region
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1region
  (JNIEnv *env, jclass clss, jint dataset, jint ref_type,
  jbyteArray ref )
{
	hid_t status;
	jboolean isCopy;
	jbyte *refP;

	if (ref_type != H5R_DATASET_REGION)  {
		h5badArgument( env, "H5Rget_region:  bad ref_type ");
		return -1;
	}

	if (ref == NULL) {
		h5nullArgument( env, "H5Rget_region:  ref is NULL");
		return -1;
	}
#ifdef __cplusplus
	if ( env->GetArrayLength(ref) < 12) {
		h5badArgument( env, "H5Rget_region:  region ref input array < 12");
	}
	refP = (jbyte *)env->GetByteArrayElements(ref,&isCopy);
#else
	if ( (*env)->GetArrayLength(env, ref) < 12) {
		h5badArgument( env, "H5Rget_region:  region ref input array < 12");
	}
	refP = (jbyte *)(*env)->GetByteArrayElements(env,ref,&isCopy);
#endif
	if (refP == NULL) {
		h5JNIFatalError(env,  "H5Rget_region:  ref not pinned");
		return -1;
	}

	status = H5Rget_region((hid_t)dataset, (H5R_type_t)ref_type, refP);

#ifdef __cplusplus
	env->ReleaseByteArrayElements(ref,refP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,ref,refP,JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}


#ifdef __cplusplus
}
#endif 
