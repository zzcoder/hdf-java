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
 *  Group Object API Functions of the HDF5 library. 
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
 * Method:    H5Gcreate
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gcreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint size_hint)
{
	hid_t status;
	char* gName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Gcreate:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif

	if (gName == NULL) {
		h5JNIFatalError( env, "H5Gcreate:  file name not pinned");
		return -1; 
	}

	status = H5Gcreate((hid_t)loc_id, gName, (size_t)size_hint );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gopen
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gopen
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* gName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Gopen:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif

	if (gName == NULL) {
		h5JNIFatalError( env, "H5Gopen:  file name not pinned");
		return -1; 
	}

	status = H5Gopen((hid_t)loc_id, gName ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gclose
  (JNIEnv *env, jclass clss, jint group_id)
{
	herr_t retVal = -1;
	retVal =  H5Gclose((hid_t)group_id) ;
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Glink
 * Signature: (IILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Glink
  (JNIEnv *env, jclass clss, jint loc_id, jint link_type, jstring 
	current_name, jstring new_name)
{
	herr_t status;
	char *cName, *nName;
	jboolean isCopy;

	if (current_name == NULL) {
		h5nullArgument( env, "H5Glink:  current_name is NULL");
		return -1;
	} 
	if (new_name == NULL) {
		h5nullArgument( env, "H5Glink:  new_name is NULL");
		return -1;
	} 
#ifdef __cplusplus
	cName = (char *)env->GetStringUTFChars(current_name,&isCopy);
#else
	cName = (char *)(*env)->GetStringUTFChars(env,current_name,&isCopy);
#endif
	if (cName == NULL) {
		h5JNIFatalError( env, "H5Glink:  current_name not pinned");
		return -1; 
	}
#ifdef __cplusplus
	nName = (char *)env->GetStringUTFChars(new_name,&isCopy);
#else
	nName = (char *)(*env)->GetStringUTFChars(env,new_name,&isCopy);
#endif
	if (nName == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(current_name,cName);
#else
		(*env)->ReleaseStringUTFChars(env,current_name,cName);
#endif
		h5JNIFatalError( env, "H5Glink:  new_name not pinned");
		return -1; 
	}

	status = H5Glink((hid_t)loc_id, (H5G_link_t)link_type, cName, nName);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(new_name,nName);
	env->ReleaseStringUTFChars(current_name,cName);
#else
	(*env)->ReleaseStringUTFChars(env,new_name,nName);
	(*env)->ReleaseStringUTFChars(env,current_name,cName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gunlink
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gunlink
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* gName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Gunlink:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif

	if (gName == NULL) {
		h5JNIFatalError( env, "H5Gunlink:  name not pinned");
		return -1; 
	}

	status = H5Gunlink((hid_t)loc_id, gName ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gmove
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gmove
  (JNIEnv *env, jclass clss, jint loc_id, jstring src, jstring dst)
{
	herr_t status;
	char *sName, *dName;
	jboolean isCopy;

	if (src == NULL) {
		h5nullArgument( env, "H5Gmove:  src is NULL");
		return -1;
	} 
	if (dst == NULL) {
		h5nullArgument( env, "H5Gmove:  dst is NULL");
		return -1;
	} 
#ifdef __cplusplus
	sName = (char *)env->GetStringUTFChars(src,&isCopy);
#else
	sName = (char *)(*env)->GetStringUTFChars(env,src,&isCopy);
#endif
	if (sName == NULL) {
		h5JNIFatalError( env, "H5Gmove:  src not pinned");
		return -1; 
	}
#ifdef __cplusplus
	dName = (char *)env->GetStringUTFChars(dst,&isCopy);
#else
	dName = (char *)(*env)->GetStringUTFChars(env,dst,&isCopy);
#endif
	if (dName == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(src,sName);
#else
		(*env)->ReleaseStringUTFChars(env,src,sName);
#endif
		h5JNIFatalError( env, "H5Gmove:  dst not pinned");
		return -1; 
	}

	status = H5Gmove((hid_t)loc_id, sName, dName );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(dst,dName);
	env->ReleaseStringUTFChars(src,sName);
#else
	(*env)->ReleaseStringUTFChars(env,dst,dName);
	(*env)->ReleaseStringUTFChars(env,src,sName);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_objinfo
 * Signature: (ILjava/lang/String;Z[J[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objinfo
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jboolean follow_link, 
  jlongArray fileno, jlongArray objno, jintArray link_info, jlongArray mtime)
{
	char* gName;
	jboolean isCopy;
	herr_t retVal; 
	jint *linkInfo;
	jlong *fileInfo, *objInfo, *timeInfo;
	hbool_t follow;
	H5G_stat_t h5gInfo;

	if (name == NULL) {
		h5nullArgument( env, "H5Gget_objinfo:  name is NULL");
		return -1;
	} 
	if (follow_link == JNI_TRUE) {
		follow = TRUE;  /*  HDF5 'TRUE' */
	} else if (follow_link == JNI_FALSE) {
		follow = FALSE;  /*  HDF5 'FALSE' */
	} else {
		h5badArgument( env, "H5Gget_objinfo:  follow_link is invalid");
	} 
	if (fileno == NULL) {
		h5nullArgument( env, "H5Gget_objinfo:  fileno is NULL");
		return -1;
	} 
#ifdef __cplusplus
	if (env->GetArrayLength(fileno) < 2) {
		h5badArgument( env, "H5Gget_objinfo:  fileno input array < 2");
	}
#else
	if ((*env)->GetArrayLength(env, fileno) < 2) {
		h5badArgument( env, "H5Gget_objinfo:  fileno input array < 2");
	}
#endif
	if (objno == NULL) {
		h5nullArgument( env, "H5Gget_objinfo:  objno is NULL");
		return -1;
	} 
#ifdef __cplusplus
	if (env->GetArrayLength(objno) < 2) {
		h5badArgument( env, "H5Gget_objinfo:  objno input array < 2");
	}
#else
	if ((*env)->GetArrayLength(env, objno) < 2) {
		h5badArgument( env, "H5Gget_objinfo:  objno input array < 2");
	}
#endif
	if (link_info == NULL) {
		h5nullArgument( env, "H5Gget_objinfo:  link_info is NULL");
		return -1;
	} 
#ifdef __cplusplus
	if (env->GetArrayLength(link_info) < 3) {
		h5badArgument( env, "H5Gget_objinfo:  link_info input array < 3");
	}
#else
	if ((*env)->GetArrayLength(env, link_info) < 3) {
		h5badArgument( env, "H5Gget_objinfo:  link_info input array < 3");
	}
#endif
	if (mtime == NULL) {
		h5nullArgument( env, "H5Gget_objinfo:  mtime is NULL");
		return -1;
	} 

#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (gName == NULL) {
		h5JNIFatalError( env, "H5Gget_object:  name not pinned");
		return -1;
	}
#ifdef __cplusplus
	fileInfo = (jlong *)env->GetLongArrayElements(fileno,&isCopy);
#else
	fileInfo = (jlong *)(*env)->GetLongArrayElements(env,fileno,&isCopy);
#endif
	if (fileInfo == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5JNIFatalError( env, "H5Gget_object:  fileno not pinned");
		return -1;
	}
#ifdef __cplusplus
	objInfo = (jlong *)env->GetLongArrayElements(objno,&isCopy);
#else
	objInfo = (jlong *)(*env)->GetLongArrayElements(env,objno,&isCopy);
#endif
	if (objInfo == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(fileno,fileInfo,JNI_ABORT);
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseLongArrayElements(env,fileno,fileInfo,JNI_ABORT);
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5JNIFatalError( env, "H5Gget_object:  objno not pinned");
		return -1;
	}
#ifdef __cplusplus
	linkInfo = (jint *)env->GetIntArrayElements(link_info,&isCopy);
#else
	linkInfo = (jint *)(*env)->GetIntArrayElements(env,link_info,&isCopy);
#endif
	if (linkInfo == NULL) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(objno,objInfo,JNI_ABORT);
		env->ReleaseLongArrayElements(fileno,fileInfo,JNI_ABORT);
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseLongArrayElements(env,objno,objInfo,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,fileno,fileInfo,JNI_ABORT);
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5JNIFatalError( env, "H5Gget_object:  link_info not pinned");
		return -1;
	}
#ifdef __cplusplus
	timeInfo = (jlong *)env->GetLongArrayElements(mtime,&isCopy);
#else
	timeInfo = (jlong *)(*env)->GetLongArrayElements(env,mtime,&isCopy);
#endif
	if (timeInfo == NULL) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(link_info,linkInfo,JNI_ABORT);
		env->ReleaseLongArrayElements(objno,objInfo,JNI_ABORT);
		env->ReleaseLongArrayElements(fileno,fileInfo,JNI_ABORT);
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseIntArrayElements(env,link_info,linkInfo,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,objno,objInfo,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,fileno,fileInfo,JNI_ABORT);
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5JNIFatalError( env, "H5Gget_object:  mtime not pinned");
		return -1;
	}

	retVal = H5Gget_objinfo((hid_t)loc_id, gName, follow, &h5gInfo);

	if (retVal < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(mtime,timeInfo,JNI_ABORT);
		env->ReleaseLongArrayElements(objno,objInfo,JNI_ABORT);
		env->ReleaseLongArrayElements(fileno,fileInfo,JNI_ABORT);
		env->ReleaseIntArrayElements(link_info,linkInfo,JNI_ABORT);
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseLongArrayElements(env,mtime,timeInfo,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,objno,objInfo,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,fileno,fileInfo,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,link_info,linkInfo,JNI_ABORT);
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5libraryError(env);
	} else {
		fileInfo[0] = (jlong)h5gInfo.fileno[0];
		fileInfo[1] = (jlong)h5gInfo.fileno[1];
		objInfo[0] = (jlong)h5gInfo.objno[0];
		objInfo[1] = (jlong)h5gInfo.objno[1];
		timeInfo[0] = (jlong)h5gInfo.mtime;
		linkInfo[0] = (jint)h5gInfo.nlink;
		linkInfo[1] = (jint)h5gInfo.type;
		linkInfo[2] = (jint)h5gInfo.linklen;
#ifdef __cplusplus
		env->ReleaseLongArrayElements(mtime,timeInfo,0);
		env->ReleaseLongArrayElements(objno,objInfo,0);
		env->ReleaseLongArrayElements(fileno,fileInfo,0);
		env->ReleaseIntArrayElements(link_info,linkInfo,0);
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseLongArrayElements(env,mtime,timeInfo,0);
		(*env)->ReleaseLongArrayElements(env,objno,objInfo,0);
		(*env)->ReleaseLongArrayElements(env,fileno,fileInfo,0);
		(*env)->ReleaseIntArrayElements(env,link_info,linkInfo,0);
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_linkval
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1linkval
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint size, jobjectArray value)
{
	char* gName;
	jboolean isCopy;
	char *lValue;
	jstring str;
	herr_t status;

	if (size < 0) {
		h5badArgument( env, "H5Gget_linkval:  size < 0");
		return -1;
	}
	if (name == NULL) {
		h5nullArgument( env, "H5Gget_linkval:  name is NULL");
		return -1;
	}
	lValue = (char *) malloc(sizeof(char)*size);
	if (lValue == NULL) {
		h5outOfMemory( env, "H5Gget_linkval:  malloc failed ");
		return -1;
	}
#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (gName == NULL) {
		free(lValue);
		h5JNIFatalError( env, "H5Gget_linkval:  name not pinned");
		return -1;
	}

	status = H5Gget_linkval((hid_t)loc_id, gName, (size_t)size, lValue); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
	if (status >= 0)
	{
		/* may throw OutOfMemoryError */
#ifdef __cplusplus
		str = env->NewStringUTF(lValue);
#else
		str = (*env)->NewStringUTF(env,lValue);
#endif
		if (str == NULL) {
			/* exception -- fatal JNI error */
			free(lValue);
			h5JNIFatalError( env, "H5Gget_linkval:  return string not created");
			return -1;
		}
		/*  the SetObjectArrayElement may raise exceptions... */
#ifdef __cplusplus
		env->SetObjectArrayElement(value,0,(jobject)str);
#else
		(*env)->SetObjectArrayElement(env,value,0,(jobject)str);
#endif
		free(lValue);
	} else {
		free(lValue);
		h5libraryError(env);
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gset_comment
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gset_1comment
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jstring comment)
{
	herr_t status;
	char *gName, *gComment;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Gset_comment:  name is NULL");
		return -1;
	}
	if (comment == NULL) {
		h5nullArgument( env, "H5Gset_comment:  comment is NULL");
		return -1;
	}
#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (gName == NULL) {
		h5JNIFatalError( env, "H5Gset_comment:  name not pinned");
		return -1;
	}
#ifdef __cplusplus
	gComment = (char *)env->GetStringUTFChars(comment,&isCopy);
#else
	gComment = (char *)(*env)->GetStringUTFChars(env,comment,&isCopy);
#endif
	if (gComment == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(name,gName);
#else
		(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
		h5JNIFatalError( env, "H5Gset_comment:  comment not pinned");
		return -1;
	}

	status = H5Gset_comment((hid_t)loc_id, gName, gComment);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(comment,gComment);
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,comment,gComment);
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_comment
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1comment
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint bufsize, 
  jobjectArray comment)
{
	char* gName;
	jboolean isCopy;
	char *gComment;
	jstring str;
	jint status;

	if (bufsize <= 0) {
		h5badArgument( env, "H5Gget_comment:  bufsize <= 0");
		return -1;
	}
	if (name == NULL) {
		h5nullArgument( env, "H5Gget_comment:  name is NULL");
		return -1;
	}
	if (comment == NULL) {
		h5nullArgument( env, "H5Gget_comment:  comment is NULL");
		return -1;
	}
	gComment = (char *)malloc(sizeof(char)*bufsize);
	if (gComment == NULL) {
		/* exception -- out of memory */
		h5outOfMemory( env, "H5Gget_comment:  malloc failed");
		return -1;
	}
#ifdef __cplusplus
	gName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	gName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (gName == NULL) {
		free(gComment);
		h5JNIFatalError( env, "H5Gget_comment:  name not pinned");
		return -1;
	}
	status = H5Gget_comment((hid_t)loc_id, gName, (size_t)bufsize, gComment); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,gName);
#else
	(*env)->ReleaseStringUTFChars(env,name,gName);
#endif
	if (status >= 0)
	{
		/*  may throw OutOfMemoryError */
#ifdef __cplusplus
		str = env->NewStringUTF(gComment);
#else
		str = (*env)->NewStringUTF(env,gComment);
#endif
		if (str == NULL) {
			free(gComment);
			h5JNIFatalError( env, "H5Gget_comment:  return string not allocated");
			return -1;
		}
		/*  The SetObjectArrayElement may raise exceptions */
#ifdef __cplusplus
		env->SetObjectArrayElement(comment,0,(jobject)str);
#else
		(*env)->SetObjectArrayElement(env,comment,0,(jobject)str);
#endif
		free(gComment);
	} else {
		free(gComment);
		h5libraryError(env);
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_num_objs 
 * Signature: (I[J[J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1num_1objs
  (JNIEnv *env, jclass clss, jint loc_id, jlongArray num_obj)
{
	int status;
	jlong *num_objP;
	jboolean isCopy;
	hsize_t *num_obja;
	int i;
	int rank;

	if (num_obj == NULL) {
		h5nullArgument( env, "H5Gget_num_objs:  num_obj is NULL");
		return -1;
	}

#ifdef __cplusplus
	num_objP = env->GetLongArrayElements(num_obj,&isCopy);
#else
	num_objP = (*env)->GetLongArrayElements(env,num_obj,&isCopy);
#endif
	if (num_objP == NULL) {
		h5JNIFatalError(env,  "H5Gget_num_objs:  num_obj not pinned");
		return -1;
	}
#ifdef __cplusplus
	rank = (int) env->GetArrayLength(num_obj);
#else
	rank = (int) (*env)->GetArrayLength(env,num_obj);
#endif
	num_obja = (hsize_t *)malloc( rank * sizeof(hsize_t)); 
	if (num_obja == NULL)  {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(num_obj,num_objP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,num_obj,num_objP,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Gget_num_objs:  num_obj not converted to hsize_t");
		return -1;
	}

	status = H5Gget_num_objs(loc_id, (hsize_t *)num_obja); 
	
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseLongArrayElements(num_obj,num_objP,JNI_ABORT);
#else
		(*env)->ReleaseLongArrayElements(env,num_obj,num_objP,JNI_ABORT);
#endif
		free(num_obja);
		h5libraryError(env);
	} else {
		for (i = 0; i < rank; i++) {
			num_objP[i] = num_obja[i];
		}
		free(num_obja); 
#ifdef __cplusplus
		env->ReleaseLongArrayElements(num_objs,num_objP,0);
#else
		(*env)->ReleaseLongArrayElements(env,num_obj,num_objP,0);
#endif
	}

	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_objname_by_idx(hid_t group_id, hsize_t idx, char *name, size_t* size ) 
 * Signature: (IJLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objname_1by_1idx
  (JNIEnv *env, jclass clss, jint group_id, jlong idx, jobjectArray name, jlong buf_size)
{
	char *aName;
	jstring str;
	hssize_t size;
	long bs;

	bs = (long)buf_size;
	if (bs <= 0) {
		h5badArgument( env, "H5Gget_objname_by_idx:  buf_size <= 0");
		return -1;
	}
	aName = (char*)malloc(sizeof(char)*bs);
	if (aName == NULL) {
		h5outOfMemory( env, "H5Gget_objname_by_idx:  malloc failed");
		return -1;
	}
	size = H5Gget_objname_by_idx((hid_t)group_id, (hsize_t)idx, aName, (size_t)buf_size);
	if (size < 0) {
		free(aName);
		h5libraryError(env);
		/*  exception, returns immediately */
	}
	/* successful return -- save the string; */
#ifdef __cplusplus
	str = env->NewStringUTF(aName);
#else
	str = (*env)->NewStringUTF(env,aName);
#endif
	if (str == NULL) {
		free(aName);
		h5JNIFatalError( env,"H5Gget_objname_by_idx:  return string failed");
		return -1;
	}
	free(aName);
	/*  Note: throws ArrayIndexOutOfBoundsException, 
		ArrayStoreException */
#ifdef __cplusplus
	env->SetObjectArrayElement(name,0,str);
#else
	(*env)->SetObjectArrayElement(env,name,0,str);
#endif

	return (jlong)size;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_objtype_by_idx(hid_t group_id, hsize_t idx ) 
 * Signature: (IJLjava/lang/String;)J
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Gget_1objtype_1by_1idx
  (JNIEnv *env, jclass clss, jint group_id, jlong idx)
{
	int type;

	type = H5Gget_objtype_by_idx((hid_t)group_id, (hsize_t)idx );
	if (type < 0) {
		h5libraryError(env);
		/*  exception, returns immediately */
	}

	return (jint)type;
}



#ifdef __cplusplus
}
#endif 
