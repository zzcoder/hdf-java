/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf5/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

/*
 *  This code is the C-interface called by Java programs to access the
 *  Datatype Object API Functions of the HDF5 library. 
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
 * Method:    H5Topen
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Topen
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* tname;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Topen:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	tname = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	tname = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (tname == NULL) {
		h5JNIFatalError(env,  "H5Topen:  name not pinned");
		return -1;
	}

	status = H5Topen(loc_id, tname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,tname);
#else
	(*env)->ReleaseStringUTFChars(env,name,tname);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tcommit
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tcommit
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type)
{
	herr_t status;
	char* tname;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Tcommit:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	tname = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	tname = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (tname == NULL) {
		h5JNIFatalError(env,  "H5Tcommit:  name not pinned");
		return -1;
	}

	status = H5Tcommit(loc_id, tname, type );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,tname);
#else
	(*env)->ReleaseStringUTFChars(env,name,tname);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tcommitted
 * Signature: (I)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tcommitted
  (JNIEnv *env, jclass clss, jint type)
{
	htri_t bval;
	bval = H5Tcommitted(type);
	if (bval > 0) {
		return JNI_TRUE;
	} else if (bval == 0) {
		return JNI_FALSE;
	} else {
		/* raise exception -- return value is irrelevant */
		h5libraryError(env);
		return JNI_FALSE;
	}
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tcreate
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tcreate
  (JNIEnv *env, jclass clss, jint dclass, jint size)
{
	hid_t retVal = -1;
	retVal =  H5Tcreate((H5T_class_t )dclass, size ); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tcopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tcopy
  (JNIEnv *env, jclass clss, jint type_id)
{
	hid_t retVal = -1;
	retVal =  H5Tcopy(type_id); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tequal
 * Signature: (II)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tequal
  (JNIEnv *env, jclass clss, jint type_id1, jint type_id2)
{
	htri_t bval;
	bval = H5Tequal(type_id1, type_id2 ); 
	if (bval > 0) {
		return JNI_TRUE;
	} else if (bval == 0) {
		return JNI_FALSE;
	} else {
		/* raise exception -- return value is irrelevant */
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tlock
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tlock
  (JNIEnv *env, jclass clss, jint type_id)
{
	herr_t retVal = -1;
	retVal =  H5Tlock(type_id );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_class
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1class
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_class_t retVal = H5T_NO_CLASS;
	retVal =  H5Tget_class(type_id );
	if (retVal == H5T_NO_CLASS) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_size
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1size
  (JNIEnv *env, jclass clss, jint type_id)
{
	size_t retVal = 0;
	retVal =  H5Tget_size(type_id );
	if (retVal == 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_size
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1size
  (JNIEnv *env, jclass clss, jint type_id, jint size)
{
	herr_t retVal = -1;
	retVal =  H5Tset_size(type_id, size );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_order
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1order
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_order_t retVal = H5T_ORDER_ERROR;
	retVal =  H5Tget_order(type_id );
	if (retVal == H5T_ORDER_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_order
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1order
  (JNIEnv *env, jclass clss, jint type_id, jint order)
{
	herr_t retVal = -1;
	retVal =  H5Tset_order(type_id, (H5T_order_t)order);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_precision
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1precision
  (JNIEnv *env, jclass clss, jint type_id)
{
	size_t retVal = 0;
	retVal =  H5Tget_precision(type_id);
	if (retVal == 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_precision
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1precision
  (JNIEnv *env, jclass clss, jint type_id, jint precision)
{
	herr_t retVal = -1;
	retVal =  H5Tset_precision(type_id, precision);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_offset
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1offset
  (JNIEnv *env, jclass clss, jint type_id)
{
	size_t retVal = 0;
	retVal =  H5Tget_offset(type_id);
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_offset
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1offset
  (JNIEnv *env, jclass clss, jint type_id, jint offset)
{
	herr_t retVal = -1;
	retVal =  H5Tset_offset(type_id, offset);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_pad
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1pad
  (JNIEnv *env, jclass clss, jint type_id, jintArray pad)
{
	herr_t status;
	jboolean isCopy;
	jint *P;
	
	if (pad == NULL) {
		h5nullArgument( env, "H5Tget_pad:  pad is NULL");
		return -1;
	}
#ifdef __cplusplus
	P = env->GetIntArrayElements(pad,&isCopy);
#else
	P = (*env)->GetIntArrayElements(env,pad,&isCopy);
#endif
	if (P == NULL) {
		h5JNIFatalError(env,  "H5Tget_pad:  pad not pinned");
		return -1;
	}
	status = H5Tget_pad(type_id, (H5T_pad_t *)&(P[0]), (H5T_pad_t *)&(P[1]));
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(pad,P,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,pad,P,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(pad,P,0);
#else
		(*env)->ReleaseIntArrayElements(env,pad,P,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_pad
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1pad
  (JNIEnv *env, jclass clss, jint type_id, jint lsb, jint msb)
{
	herr_t retVal = -1;
	retVal =  H5Tset_pad(type_id, (H5T_pad_t)lsb, (H5T_pad_t)msb);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_sign
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1sign
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_sign_t retVal = H5T_SGN_ERROR;
	retVal =  H5Tget_sign(type_id);
	if (retVal == H5T_SGN_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_sign
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1sign
  (JNIEnv *env, jclass clss, jint type_id, jint sign)
{
	herr_t retVal = -1;
	retVal =  H5Tset_sign(type_id, (H5T_sign_t)sign); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_fields
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1fields
  (JNIEnv *env, jclass clss, jint type_id, jintArray fields)
{
	herr_t status;
	jboolean isCopy;
	jint *P;
	
	if (fields == NULL) {
		h5nullArgument( env, "H5Tget_fields:  fields is NULL");
		return -1;
	}
#ifdef __cplusplus
	if (env->GetArrayLength(fields) < 5) {
		h5badArgument( env, "H5Tget_fields:  fields input array < order 5");
	}
	P = env->GetIntArrayElements(fields,&isCopy);
#else
	if ((*env)->GetArrayLength(env, fields) < 5) {
		h5badArgument( env, "H5Tget_fields:  fields input array < order 5");
	}
	P = (*env)->GetIntArrayElements(env,fields,&isCopy);
#endif
	if (P == NULL) {
		h5JNIFatalError(env,  "H5Tget_fields:  fields not pinned");
		return -1;
	}

	status = H5Tget_fields(type_id, (size_t *)&(P[0]), (size_t *)&(P[1]), (size_t *)&(P[2]), (size_t *)&(P[3]), (size_t *)&(P[4]));

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(fields,P,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,fields,P,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(fields,P,0);
#else
		(*env)->ReleaseIntArrayElements(env,fields,P,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_fields
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1fields
  (JNIEnv *env, jclass clss, jint type_id, jint spos, jint epos, 
  jint esize, jint mpos, jint msiz)
{
	herr_t retVal = -1;
	retVal =  H5Tset_fields(type_id, spos, epos, esize, mpos, msiz);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_ebias
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1ebias
  (JNIEnv *env, jclass clss, jint type_id)
{
	size_t retVal = 0;
	retVal =  H5Tget_ebias(type_id );
	if (retVal == 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_ebias
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1ebias
  (JNIEnv *env, jclass clss, jint type_id, jint ebias)
{
	herr_t retVal = -1;
	retVal =  H5Tset_ebias(type_id, ebias);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_norm
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1norm
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_norm_t retVal = H5T_NORM_ERROR;
	retVal =  H5Tget_norm(type_id);
	if (retVal == H5T_NORM_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_norm
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1norm
  (JNIEnv *env, jclass clss, jint type_id, jint norm)
{
	herr_t retVal = -1;
	retVal =  H5Tset_norm(type_id, (H5T_norm_t )norm);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_inpad
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1inpad
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_pad_t retVal = H5T_PAD_ERROR;
	retVal =  H5Tget_inpad(type_id );
	if (retVal == H5T_PAD_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_inpad
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1inpad
  (JNIEnv *env, jclass clss, jint type_id, jint inpad)
{
	herr_t retVal = -1;
	retVal = H5Tset_inpad(type_id, (H5T_pad_t) inpad);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_cset
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1cset
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_cset_t retVal = H5T_CSET_ERROR;
	retVal =  H5Tget_cset(type_id);
	if (retVal == H5T_CSET_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_cset
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1cset
  (JNIEnv *env, jclass clss, jint type_id, jint cset)
{
	herr_t retVal = -1;
	retVal =  H5Tset_cset(type_id, (H5T_cset_t)cset);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_strpad
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1strpad
  (JNIEnv *env, jclass clss, jint type_id)
{
	H5T_str_t retVal = H5T_STR_ERROR;
	retVal =  H5Tget_strpad(type_id);
	if (retVal == H5T_STR_ERROR) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_strpad
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1strpad
  (JNIEnv *env, jclass clss, jint type_id, jint strpad)
{
	herr_t retVal = -1;
	retVal =  H5Tset_strpad(type_id, (H5T_str_t)strpad);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_nmembers
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1nmembers
  (JNIEnv *env, jclass clss, jint type_id)
{
	int retVal = -1;
	retVal =  H5Tget_nmembers(type_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_member_name
 * Signature: (II)Ljava/lang/String
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1member_1name
  (JNIEnv *env, jclass clss, jint type_id, jint field_idx)
{
	char *name;
	jstring str;

	name = H5Tget_member_name(type_id, field_idx);

	if (name == NULL) {
		return NULL;
	} else {
		/* may throw OutOfMemoryError */
#ifdef __cplusplus
		str = env->NewStringUTF(name);
#else
		str = (*env)->NewStringUTF(env,name);
#endif
		if (str == NULL)  {
			free(name);
			h5JNIFatalError(env,  "H5Tget_member_name:  returned string not created");
			return NULL;
		}
		free(name);
		return str;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_member_type
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1member_1type
  (JNIEnv *env, jclass clss, jint type_id, jint field_idx)
{
	hid_t retVal = -1;
	retVal =  H5Tget_member_type(type_id, field_idx);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_member_offset
 * Signature: (II)I
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1member_1offset
  (JNIEnv *env, jclass clss, jint type_id, jint memno)
{
	size_t retVal = 0;
	retVal =  H5Tget_member_offset((hid_t)type_id, memno);
	return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tinsert
 * Signature: (ILjava/lang/String;JI)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tinsert
  (JNIEnv *env, jclass clss, jint type_id, jstring name, jlong offset, jint field_id)
{
	herr_t status;
	char* tname;
	jboolean isCopy;
	long off;

	off = (long)offset;
	if (name == NULL) {
		h5nullArgument( env, "H5Tinsert:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	tname =(char *)env->GetStringUTFChars(name,&isCopy);
#else
	tname =(char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (tname == NULL) {
		h5JNIFatalError(env,  "H5Tinsert:  name not pinned");
		return -1;
	}

	status = H5Tinsert(type_id, tname, (size_t)off, field_id);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,tname);
#else
	(*env)->ReleaseStringUTFChars(env,name,tname);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tpack
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tpack
  (JNIEnv *env, jclass clss, jint type_id)
{
	herr_t retVal = -1;
	retVal =  H5Tpack(type_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tclose
  (JNIEnv *env, jclass clss, jint type_id)
{
	herr_t retVal = -1;
	retVal =  H5Tclose(type_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tenum_create
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tenum_1create
  (JNIEnv *env, jclass clss, jint base_id)
{
	hid_t status;

	status =  H5Tenum_create((hid_t)base_id);
	if (status < 0)
		h5libraryError(env);

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tenum_insert
 * Signature: (ILjava/lang/String;[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tenum_1insert
  (JNIEnv *env, jclass clss, jint type, jstring name, jintArray value)
{
	herr_t status;
	jint *byteP;
	char *nameP;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Tenum_insert:  name is NULL");
		return -1;
	}

#ifdef __cplusplus
	nameP = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	nameP = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (nameP == NULL) {
		h5JNIFatalError( env, "H5Tenum_insert:  name not pinned");
		return -1;
	}

	if ( value == NULL ) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,nameP);
#else
		(*env)->ReleaseStringUTFChars(env,name,nameP);
#endif
		h5nullArgument( env, "H5Tenum_insert:  value is NULL");
		return -1;
	}

#ifdef __cplusplus
	byteP = env->GetIntArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetIntArrayElements(env,value,&isCopy);
#endif
	if (byteP == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,nameP);
#else
		(*env)->ReleaseStringUTFChars(env,name,nameP);
#endif
		h5JNIFatalError( env, "H5Tenum_insert:  value not pinned");
		return -1;
	}

	status = H5Tenum_insert((hid_t)type, nameP, byteP);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,nameP);
	env->ReleaseIntArrayElements(value,byteP,JNI_ABORT);
#else
	(*env)->ReleaseStringUTFChars(env,name,nameP);
	(*env)->ReleaseIntArrayElements(env,value,byteP,JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tenum_nameof
 * Signature: (I[B[Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tenum_1nameof
  (JNIEnv *env, jclass clss, jint type, jintArray value, jobjectArray name, jint size)
{
	hid_t status;
	jint *byteP;
	char *nameP;
	jboolean isCopy;
	jstring str;

	if (size <= 0) {
		h5badArgument( env, "H5Tenum_nameof:  name size < 0");
		return -1;
	}

	nameP = (char *)malloc(sizeof(char)*size);
	if (nameP == NULL) {
		/* exception -- out of memory */
		h5outOfMemory( env, "H5Tenum_nameof:  malloc name size");
		return -1;
	}

	if ( value == NULL ) {
		free(nameP);
		h5nullArgument( env, "H5Tenum_nameof:  value is NULL");
		return -1;
	}

#ifdef __cplusplus
	byteP = env->GetIntArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetIntArrayElements(env,value,&isCopy);
#endif
	if (byteP == NULL) {
		free(nameP);
		h5JNIFatalError( env, "H5Tenum_nameof:  value not pinned");
		return -1;
	}

	status = H5Tenum_nameof((hid_t)type, byteP, nameP, (size_t)size);

#ifdef __cplusplus
	env->ReleaseIntArrayElements(value,byteP,JNI_ABORT);
#else
	(*env)->ReleaseIntArrayElements(env,value,byteP,JNI_ABORT);
#endif

	if (status < 0) {
		free(nameP);
		h5libraryError(env);
	} 
	else {
#ifdef __cplusplus
		str = env->NewStringUTF(nameP);
#else
		str = (*env)->NewStringUTF(env,nameP);
#endif
		if (str == NULL) {
			free(nameP);
			h5JNIFatalError( env, "H5Tenum_nameof:  return array not created");
			return -1;
		}
		/*  SetObjectArrayElement may raise exceptions */
#ifdef __cplusplus
		env->SetObjectArrayElement(name,0,(jobject)str);
#else
		(*env)->SetObjectArrayElement(env,name,0,(jobject)str);
#endif
	}

	free(nameP);
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tenum_valueof
 * Signature: (ILjava/lang/String;[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tenum_1valueof
  (JNIEnv *env, jclass clss, jint type, jstring name, jintArray value)
{
	hid_t status;
	jint *byteP;
	char *nameP;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Tenum_valueof:  name is NULL");
		return -1;
	}

#ifdef __cplusplus
	nameP = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	nameP = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (nameP == NULL) {
		h5JNIFatalError( env, "H5Tenum_valueof:  name not pinned");
		return -1;
	}

	if ( value == NULL ) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,nameP);
#else
		(*env)->ReleaseStringUTFChars(env,name,nameP);
#endif
		h5nullArgument( env, "H5Tenum_valueof:  value is NULL");
		return -1;
	}

#ifdef __cplusplus
	byteP = env->GetIntArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetIntArrayElements(env,value,&isCopy);
#endif
	if (byteP == NULL)  {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,nameP);
#else
		(*env)->ReleaseStringUTFChars(env,name,nameP);
#endif
		h5JNIFatalError( env, "H5Tenum_valueof:  value not pinned");
		return -1;
	}

	status = H5Tenum_valueof((hid_t)type, nameP, byteP);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,nameP);
#else
	(*env)->ReleaseStringUTFChars(env,name,nameP);
#endif

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(value,byteP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,value,byteP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(value,byteP,0);
#else
		(*env)->ReleaseIntArrayElements(env,value,byteP,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tvlen_create
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tvlen_1create
  (JNIEnv *env, jclass clss, jint base_id)
{
	hid_t status;

	status = H5Tvlen_create((hid_t)base_id);
	if (status < 0)
		h5libraryError(env);

	return status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tset_tag
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tset_1tag
  (JNIEnv *env, jclass clss, jint type, jstring tag)
{
	herr_t status;
	char *tagP;
	jboolean isCopy;

	if (tag == NULL) {
		h5nullArgument( env, "H5Tset_tag:  tag is NULL");
		return -1;
	}

#ifdef __cplusplus
	tagP = (char *)env->GetStringUTFChars(tag,&isCopy);
#else
	tagP = (char *)(*env)->GetStringUTFChars(env,tag,&isCopy);
#endif
	if (tagP == NULL) {
		h5JNIFatalError( env, "H5Tset_tag:  tag not pinned");
		return -1;
	}

	status = H5Tset_tag((hid_t)type, tagP);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(tag,tagP);
#else
	(*env)->ReleaseStringUTFChars(env,tag,tagP);
#endif

	if (status < 0)
		h5libraryError(env);

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_tag
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1tag
  (JNIEnv *env, jclass clss, jint type)
{
	jstring str;
	char *tag;

	tag = H5Tget_tag((hid_t)type);

	if (tag == NULL)
		return NULL;

	/* may throw OutOfMemoryError */
#ifdef __cplusplus
	str = env->NewStringUTF(tag);
#else
	str = (*env)->NewStringUTF(env,tag);
#endif
	if (str == NULL)  {
		free(tag);
		h5JNIFatalError(env,  "H5Tget_tag:  returned string not created");
		return NULL;
	}

	free(tag);
	return str;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_super
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1super
  (JNIEnv *env, jclass clss, jint type)
{
	hid_t status;

	status = H5Tget_super((hid_t)type);
	if (status < 0)
		h5libraryError(env);

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_member_value
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1member_1value
  (JNIEnv *env, jclass clss, jint type, jint membno, jintArray value)
{
	hid_t status;
	jint *byteP;
	jboolean isCopy;

	if ( value == NULL ) {
		h5nullArgument( env, "H5Tget_member_value:  value is NULL");
		return -1;
	}

#ifdef __cplusplus
	byteP = env->GetIntArrayElements(value,&isCopy);
#else
	byteP = (*env)->GetIntArrayElements(env,value,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env, "H5Tget_member_value:  value not pinned");
		return -1;
	}

	status = H5Tget_member_value((hid_t)type, (int)membno, byteP);

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(value,byteP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,value,byteP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(value,byteP,0);
#else
		(*env)->ReleaseIntArrayElements(env,value,byteP,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tarray_create
 * Signature: (II[B[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tarray_1create
  (JNIEnv *env, jclass clss, jint base, jint rank, jintArray dims, jintArray perms)
{
	hid_t status;
	jint *dimsP;
	jint *permP;
	int dlen;
	hsize_t *cdims;
	jboolean isCopy;
	int i;

	if (rank <= 0) {
		h5nullArgument( env, "H5Tarray_create:  rank is < 1");
		return -1;
	}
	if ( dims == NULL ) {
		h5nullArgument( env, "H5Tarray_create:  dims is NULL");
		return -1;
	}

#ifdef __cplusplus
	dimsP = env->GetIntArrayElements(dims,&isCopy);
#else
	dimsP = (*env)->GetIntArrayElements(env,dims,&isCopy);
#endif
	if (dimsP == NULL) {
		h5JNIFatalError( env, "H5Tarray_create:  dimsP not pinned");
		return -1;
	}

#ifdef __cplusplus
	dlen = env->GetArrayLength(dims);
#else
	dlen = (*env)->GetArrayLength(env,dims);
#endif
	if (dlen != rank) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
		return -1;
	}
	cdims = (hsize_t *)malloc(dlen * sizeof(hsize_t));

	if (perms == NULL) {
		permP = NULL;
	} else {
#ifdef __cplusplus
		permP = env->GetIntArrayElements(perms,&isCopy);
#else
		permP = (*env)->GetIntArrayElements(env,perms,&isCopy);
#endif
		if (permP == NULL) {
			h5JNIFatalError( env, "H5Tarray_create:  permP not pinned");
#ifdef __cplusplus
			env->ReleaseIntArrayElements(dims,dimsP,JNI_ABORT);
#else
			(*env)->ReleaseIntArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
			return -1;
		}
	}
	
	for (i = 0; i < dlen; i++) {
		cdims[i] = (hsize_t)dimsP[i];
	}

	status = H5Tarray_create((hid_t)base, (int)rank, (const hsize_t *)cdims, (const int *)permP);

#ifdef __cplusplus
	env->ReleaseIntArrayElements(dims,dimsP,JNI_ABORT);
#else
	(*env)->ReleaseIntArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
	if (permP != NULL) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(perms,permP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,perms,permP,JNI_ABORT);
#endif
	}
	if (status < 0) {
		h5libraryError(env);
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tget_array_dims
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1array_1ndims
  (JNIEnv *env, jclass clss, jint dt)
{
	hid_t status;

	status = H5Tget_array_ndims((hid_t)dt);
	if (status < 0)
		h5libraryError(env);

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Tarray_get_dims
 * Signature: (I[I[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Tget_1array_1dims
  (JNIEnv *env, jclass clss, jint dt, jintArray dims, jintArray perms)
{
	hid_t status;
	jint *dimsP;
	jint *permP;
	int dlen;
	int i;
	hsize_t *cdims;
	jboolean isCopy;

	if ( dims == NULL ) {
		h5nullArgument( env, "H5Tget_array_dims:  value is NULL");
		return -1;
	}

#ifdef __cplusplus
	dimsP = env->GetIntArrayElements(dims,&isCopy);
#else
	dimsP = (*env)->GetIntArrayElements(env,dims,&isCopy);
#endif
	if (dimsP == NULL) {
		h5JNIFatalError( env, "H5Tget_array_dims:  dimsP not pinned");
		return -1;
	}

#ifdef __cplusplus
	dlen = env->GetArrayLength(dims);
#else
	dlen = (*env)->GetArrayLength(env,dims);
#endif
	cdims = (hsize_t *)malloc(dlen * sizeof(hsize_t));

	if (perms == NULL) {
		permP = NULL;
	} else {
#ifdef __cplusplus
		permP = env->GetIntArrayElements(perms,&isCopy);
#else
		permP = (*env)->GetIntArrayElements(env,perms,&isCopy);
#endif
		if (permP == NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(dims,dimsP,JNI_ABORT);
#else
			(*env)->ReleaseIntArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
			h5JNIFatalError( env, "H5Tget_array_dims:  permP not pinned");
			return -1;
		}
	}

	status = H5Tget_array_dims((hid_t)dt, (hsize_t *)cdims, (int *)permP);

	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(dims,dimsP,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,dims,dimsP,JNI_ABORT);
#endif
		if (permP != NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(perms,permP,JNI_ABORT);
#else
			(*env)->ReleaseIntArrayElements(env,perms,permP,JNI_ABORT);
#endif
		}
		h5libraryError(env);
	} else {
		for (i = 0; i < dlen; i++) {
			dimsP[i] = (jint) cdims[i];
		}
#ifdef __cplusplus
		env->ReleaseIntArrayElements(dims,dimsP,0);
#else
		(*env)->ReleaseIntArrayElements(env,dims,dimsP,0);
#endif
		if (permP != NULL) {
#ifdef __cplusplus
			env->ReleaseIntArrayElements(perms,permP,0);
#else
			(*env)->ReleaseIntArrayElements(env,perms,permP,0);
#endif
		}
	}

	return (jint)status;
}
#ifdef __cplusplus
}
#endif 
