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

#ifdef __cplusplus
extern "C" {
#endif
#include "hdf5.h"
#include "hdf.h"
#include "mfhdf.h"
#include "h4toh5.h"
#include <jni.h>
#include <stdlib.h>

extern jboolean h45nullArgument( JNIEnv *env, char *functName);
extern jboolean h45JNIFatalError( JNIEnv *env, char *mess);
extern jboolean h45libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5version
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5version
  (JNIEnv *env, jclass clss)
{
jstring str;
char * vers = "1.0a";

#ifdef __cplusplus
	str = env->NewStringUTF(vers);
#else
	str = (*env)->NewStringUTF(env,vers);
#endif
	return str;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5open
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5open
  (JNIEnv *env, jclass clss, jstring h4filename, jstring h5filename, 
  jint access)
{
	hid_t status;
	char* h4file;
	char* h5file;
	jboolean isCopy = JNI_FALSE;

	if ((access != H425_OPEN) &&
	    (access != H425_CREATE) &&
	    (access != H425_CLOBBER)) {
		h45badArgument( env, "h4toh5open:  access is invalid");
		return -1;
	}
	if (h4filename == NULL) {
		h45nullArgument( env, "h4toh5open:  h4filename is NULL");
		return -1;
	}

	if (h5filename == NULL) {
		h5file = NULL;
	} else {
#ifdef __cplusplus
		h5file = (char *)env->GetStringUTFChars(h5filename,&isCopy);
#else
		h5file = (char *)(*env)->GetStringUTFChars(env,h5filename,&isCopy);
#endif
		if (h5file == NULL) {
			h45JNIFatalError( env, "h4toh5open:  h4file name not pinned");
			return -1;
		}
	}
#ifdef __cplusplus
	h4file = (char *)env->GetStringUTFChars(h4filename,&isCopy);
#else
	h4file = (char *)(*env)->GetStringUTFChars(env,h4filename,&isCopy);
#endif
	if (h4file == NULL) {
		if (h5file != NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(h5filename,h5file);
#else
		(*env)->ReleaseStringUTFChars(env,h5filename,h5file);
#endif
		}
		h45JNIFatalError( env, "h4toh5open:  h4file name not pinned");
		return -1;
	}

	status = H4toh5open(h4file, h5file, access ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(h4filename,h4file);
#else
	(*env)->ReleaseStringUTFChars(env,h4filename,h4file);
#endif
	if (h5file != NULL) {
#ifdef __cplusplus
	env->ReleaseStringUTFChars(h5filename,h5file);
#else
	(*env)->ReleaseStringUTFChars(env,h5filename,h5file);
#endif
	}

	if (status < 0) {
		h45libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5openid
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5openid
  (JNIEnv *env, jclass clss, jstring h4filename, jint h5id ) 
{
	hid_t status;
	char* h4file;
	jboolean isCopy = JNI_FALSE;

	if (h4filename == NULL) {
		h45nullArgument( env, "h4toh5openid:  h4filename is NULL");
		return -1;
	}

#ifdef __cplusplus
	h4file = (char *)env->GetStringUTFChars(h4filename,&isCopy);
#else
	h4file = (char *)(*env)->GetStringUTFChars(env,h4filename,&isCopy);
#endif
	if (h4file == NULL) {
		h45JNIFatalError( env, "h4toh5openid:  h4file name not pinned");
		return -1;
	}

	status = H4toh5openid(h4file, (hid_t)h5id ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(h4filename,h4file);
#else
	(*env)->ReleaseStringUTFChars(env,h4filename,h4file);
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5close
  (JNIEnv *env, jclass cls, jint h45id)
{
	hid_t status;
	jboolean isCopy = JNI_FALSE;

	status = H4toh5close((hid_t)h45id);

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}


/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5basvgroup
 * Signature: (IILjava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5basvgroup
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
    jstring child, jint vg_flag, jint attr_flag)
{
	hid_t status;
	char* pname;
	char* cname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5basvgroup:  parent is NULL");
		return;
	}

	if (child == NULL) {
		cname = NULL;
	} else {
#ifdef __cplusplus
		cname = (char *)env->GetStringUTFChars(child,&isCopy);
#else
		cname = (char *)(*env)->GetStringUTFChars(env,child,&isCopy);
#endif
		if (cname == NULL) {
			h45JNIFatalError( env, "h4toh5basvgroup:  child name not pinned");
			return ;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (cname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(child,cname);
#else
			(*env)->ReleaseStringUTFChars(env,child,cname);
#endif
		}
		h45JNIFatalError( env, "h4toh5basvgroup:  parent name not pinned");
		return;
	}

	status = H4toh5basvgroup((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) cname,
                    (int) vg_flag,
                    (int) attr_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (cname != NULL) {
		env->ReleaseStringUTFChars(child,cname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (cname != NULL) {
		(*env)->ReleaseStringUTFChars(env,child,cname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5advgroup
 * Signature: (IILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5advgroup
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring child)
{
	hid_t status;
	char* pname;
	char* cname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5advgroup:  parent is NULL");
		return ;
	}

	if (child == NULL) {
		cname = NULL;		
	} else {
#ifdef __cplusplus
		cname = (char *)env->GetStringUTFChars(child,&isCopy);
#else
		cname = (char *)(*env)->GetStringUTFChars(env,child,&isCopy);
#endif
		if (cname == NULL) {
			h45JNIFatalError( env, "h4toh5advgroup:  child name not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (cname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(child,cname);
#else
			(*env)->ReleaseStringUTFChars(env,child,cname);
#endif
		}
		h45JNIFatalError( env, "h4toh5advgroup:  parent name not pinned");
		return ;
	}

	status = H4toh5advgroup((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) cname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (cname != NULL) {
		env->ReleaseStringUTFChars(child,cname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (cname != NULL) {
		(*env)->ReleaseStringUTFChars(env,child,cname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}


/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vgroupattrname
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vgroupattrname
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring groupname, jstring attrname)
{
	hid_t status;
	char* pname;
	char* gname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vgroupattrname:  parent is NULL");
		return ;
	}

	if (attrname == NULL) {
		h45nullArgument( env, "h4toh5vgroupattrname:  attrname is NULL");
		return ;
	}

	if (groupname == NULL) {
		gname = NULL;		
	} else {
#ifdef __cplusplus
		gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
		gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
		if (gname == NULL) {
			h45JNIFatalError( env, "h4toh5vgroupattrname:  groupname not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (gname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vgroupattrname:  parent name not pinned");
		return ;
	}

#ifdef __cplusplus
	aname = (char *)env->GetStringUTFChars(attrname,&isCopy);
#else
	aname = (char *)(*env)->GetStringUTFChars(env,attrname,&isCopy);
#endif
	if (aname == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(parent,pname);
#else
		(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
		if (gname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vgroupattrname:  attribute name not pinned");
		return ;
	}

	status = H4toh5vgroupattrname((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) gname,
		    (char*) aname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(attrname,aname);
	env->ReleaseStringUTFChars(parent,pname);
	if (gname != NULL) {
		env->ReleaseStringUTFChars(groupname,gname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,attrname,aname);
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (gname != NULL) {
		(*env)->ReleaseStringUTFChars(env,groupname,gname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vgroupattrindex
 * Signature: (IILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vgroupattrindex
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring groupname, jint index)
{
	hid_t status;
	char* pname;
	char* gname;
	jboolean isCopy = JNI_FALSE;

	/* check range of index */

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vgroupattrindex:  parent is NULL");
		return ;
	}

	if (groupname == NULL) {
		gname = NULL;		
	} else {
#ifdef __cplusplus
		gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
		gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
		if (gname == NULL) {
			h45JNIFatalError( env, "h4toh5vgroupattrindex:  groupname name not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (gname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vgroupattrindex:  parent name not pinned");
		return ;
	}

	status = H4toh5vgroupattrindex((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) gname,
		    (int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (gname != NULL) {
		env->ReleaseStringUTFChars(groupname,gname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (gname != NULL) {
		(*env)->ReleaseStringUTFChars(env,groupname,gname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vdata
 * Signature: (IILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vdata
  (JNIEnv * env, jclass cls, jint h45id, jint vdid, jstring groupname, 
jstring vdataname, jint attr_flag)
{
	hid_t status;
	char* gname;
	char* vname;
	jboolean isCopy = JNI_FALSE;

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5vdata:  groupname is NULL");
		return ;
	}

	if (vdataname == NULL) {
		vname = NULL;
	} else {
#ifdef __cplusplus
		vname = (char *)env->GetStringUTFChars(vdataname,&isCopy);
#else
		vname = (char *)(*env)->GetStringUTFChars(env,vdataname,&isCopy);
#endif
		if (vname == NULL) {
			h45JNIFatalError( env, "h4toh5vdata:  vdataname name not pinned");
			return ;
		}
	}
#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		if (vname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vdata:  vdata name not pinned");
		return ;
	}

	status = H4toh5vdata((hid_t) h45id,
                    (int32) vdid,
                    (char*) gname,
                    (char*) vname,
		(int)	attr_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (vname != NULL) {
		env->ReleaseStringUTFChars(vdataname,vname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (vname != NULL) {
		(*env)->ReleaseStringUTFChars(env,vdataname,vname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vdataattrname
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vdataattrname
  (JNIEnv * env, jclass cls, jint h45id, jint vdid, jstring parent, 
jstring dsetname, jstring attrname)
{
	hid_t status;
	char* pname;
	char* dname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vdataattrname:  parent is NULL");
		return ;
	}

	if (attrname == NULL) {
		h45nullArgument( env, "h4toh5vdataattrname:  attrname is NULL");
		return ;
	}


	if (dsetname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dsetname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dsetname,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5vdataattrname:  dset name not pinned");
			return;
		}
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dsetname,cname);
#else
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vdataattrname:  parent name not pinned");
		return ;
	}

#ifdef __cplusplus
	aname = (char *)env->GetStringUTFChars(attrname,&isCopy);
#else
	aname = (char *)(*env)->GetStringUTFChars(env,attrname,&isCopy);
#endif
	if (aname == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(parent,pname);
		if (dname != NULL) {
			env->ReleaseStringUTFChars(dasetname,dname);
		}
#else
		(*env)->ReleaseStringUTFChars(env,parent,pname);
		if (dname != NULL) {
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
		}
#endif
		h45JNIFatalError( env, "h4toh5vdataattrname:  attr name not pinned");
		return ;
	}

	status = H4toh5vdataattrname((hid_t) h45id,
                    (int32) vdid,
                    (char*) pname,
                    (char*) dname,
		    (char*) aname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(attrname,aname);
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dsetname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,attrname,aname);
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dsetname,dname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vdataattrindex
 * Signature: (IILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vdataattrindex
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring dsetname, jint index)
{
	hid_t status;
	char* pname;
	char* dname;
	jboolean isCopy = JNI_FALSE;

	/* check range of index */

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vdataattrindex:  parent is NULL");
		return ;
	}

	if (dsetname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dsetname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dsetname,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5vdataattrindex:  dataset name not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dsetname,dname);
#else
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vdataattrindex:  parent name not pinned");
		return ;
	}

	status = H4toh5imageattrindex((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) dname,
		    (int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dsetname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dsetname,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vdatafieldattrname 
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vdatafieldattrname
  (JNIEnv * env, jclass cls, jint h45id, jint vdid, jstring parent, 
jstring dsetname, jstring fieldname, jstring attrname)
{
	hid_t status;
	char* pname;
	char* dname;
	char* fname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vdatafieldattrname:  parent is NULL");
		return ;
	}

	if (attrname == NULL) {
		h45nullArgument( env, "h4toh5vdatafieldattrname:  attrname is NULL");
		return ;
	}


	if (fieldname == NULL) {
		h45nullArgument( env, "h4toh5vdatafieldattrname:  fieldname is NULL");
		return ;
	}

	if (dsetname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dsetname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dsetname,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5vdataattrname:  dset name not pinned");
			return;
		}
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dsetname,cname);
#else
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vdataattrname:  parent name not pinned");
		return ;
	}

#ifdef __cplusplus
	aname = (char *)env->GetStringUTFChars(attrname,&isCopy);
#else
	aname = (char *)(*env)->GetStringUTFChars(env,attrname,&isCopy);
#endif
	if (aname == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(parent,pname);
		if (dname != NULL) {
			env->ReleaseStringUTFChars(dasetname,dname);
		}
#else
		(*env)->ReleaseStringUTFChars(env,parent,pname);
		if (dname != NULL) {
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
		}
#endif
		h45JNIFatalError( env, "h4toh5vdataattrname:  attribute name not pinned");
		return ;
	}

#ifdef __cplusplus
	fname = (char *)env->GetStringUTFChars(fieldname,&isCopy);
#else
	fname = (char *)(*env)->GetStringUTFChars(env,fieldname,&isCopy);
#endif
	if (fname == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(parent,pname);
		if (dname != NULL) {
			env->ReleaseStringUTFChars(dasetname,dname);
		}
		env->ReleaseStringUTFChars(attrname,aname);
#else
		(*env)->ReleaseStringUTFChars(env,parent,pname);
		if (dname != NULL) {
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
		}
		(*env)->ReleaseStringUTFChars(env,attrname,aname);
#endif
		h45JNIFatalError( env, "h4toh5vdataattrname:  field name not pinned");
		return ;
	}

	status = H4toh5vdatafieldattrname((hid_t) h45id,
                    (int32) vdid,
                    (char*) pname,
                    (char*) dname,
                    (char*) fname,
		    (char*) aname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(fieldname,fname);
	env->ReleaseStringUTFChars(attrname,aname);
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dsetname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,fieldname,fname);
	(*env)->ReleaseStringUTFChars(env,attrname,aname);
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dsetname,dname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5vdatafieldattrindex
 * Signature: (IILjava/lang/String;Ljava/lang/String;II)V
 */                                                                           
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5vdatafieldattrindex
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring dsetname, jint fieldindex, jint index)
{
	hid_t status;
	char* pname;
	char* dname;
	jboolean isCopy = JNI_FALSE;

	/* check range of index */

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5vdatafieldattrindex:  parent is NULL");
		return ;
	}

	if (dsetname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dsetname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dsetname,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5vdatafieldattrindex:  child name not pinned");
			return;
		}
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dsetname,dname);
#else
			(*env)->ReleaseStringUTFChars(env,dsetname,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5vdatafieldattrindex:  parent name not pinned");
		return ;
	}

	status = H4toh5vdatafieldattrindex((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) dname,
		    (int)fieldindex,
		    (int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dsetname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dsetname,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5sds
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5sds
  (JNIEnv * env, jclass cls, jint h45id, jint sdsid,
jstring groupname, jstring h5dsetname, jstring dimgrouppath,  
jint dim_flag, jint attr_flag)
{
	hid_t status;
	char* gname;
	char* dsname;
	char* dgpath;
	jboolean isCopy = JNI_FALSE;

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5sds:  groupname is NULL");
		return ;
	}

	if (h5dsetname == NULL) {
		dsname = NULL;
	} else {
#ifdef __cplusplus
		dsname = (char *)env->GetStringUTFChars(h5dsetname,&isCopy);
#else
		dsname = (char *)(*env)->GetStringUTFChars(env,h5dsetname,&isCopy);
#endif
		if (dsname == NULL) {
			h45JNIFatalError( env, "h4toh5sds:  sds name not pinned");
			return ;
		}
	}

	if (dimgrouppath == NULL) {
		dgpath = NULL;
	} else {
#ifdef __cplusplus
		dgpath = (char *)env->GetStringUTFChars(dimgrouppath,&isCopy);
#else
		dgpath = (char *)(*env)->GetStringUTFChars(env,dimgrouppath,&isCopy);
#endif
		if (dgpath == NULL) {
			if (dsname != NULL) {
#ifdef __cplusplus
				env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
				(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
			}
			h45JNIFatalError( env, "h4toh5sds:  dimgroup name not pinned");
			return ;
		}
	}


#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		if (dsname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
		}
		if (dgpath != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dimgrouppath,dgpath);
#else
			(*env)->ReleaseStringUTFChars(env,dimgrouppath,dgpath);
#endif
		}
		h45JNIFatalError( env, "h4toh5sds:  group name not pinned");
		return ;
	}

	status = H4toh5sds((hid_t) h45id,
                    (int32) sdsid,
                    (char*) gname,
                    (char*) dsname,
                    (char*) dgpath,
		(int)	dim_flag,
		(int)	attr_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (dsname != NULL) {
		env->ReleaseStringUTFChars(h5dsetname,dsname);
	}
	if (dgpath != NULL) {
		env->ReleaseStringUTFChars(dimgrouppath,dgpath);
	}
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (dsname != NULL) {
		(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
	}
	if (dgpath != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimgrouppath,dgpath);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5sdsattrname
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5sdsattrname
  (JNIEnv * env, jclass cls, jint h45id, jint sdsid,
jstring groupname, jstring h5dsetname, jstring attrname)
{
	hid_t status;
	char* gname;
	char* dsname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (attrname == NULL) {
		h45nullArgument( env, "h4toh5sdsattrname:  attrname is NULL");
		return ;
	}

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5sdsattrname:  groupname is NULL");
		return ;
	}

	if (h5dsetname == NULL) {
		dsname = NULL;
	} else {
#ifdef __cplusplus
		dsname = (char *)env->GetStringUTFChars(h5dsetname,&isCopy);
#else
		dsname = (char *)(*env)->GetStringUTFChars(env,h5dsetname,&isCopy);
#endif
		if (dsname == NULL) {
			h45JNIFatalError( env, "h4toh5sdsattrname:  sds name not pinned");
			return ;
		}
	}

#ifdef __cplusplus
	aname = (char *)env->GetStringUTFChars(attrname,&isCopy);
#else
	aname = (char *)(*env)->GetStringUTFChars(env,attrname,&isCopy);
#endif
	if (aname == NULL) {
		if (dsname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
		}
		h45JNIFatalError( env, "h4toh5sdsattrname:  attr name not pinned");
		return ;
	}

#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		if (dsname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
		}
#ifdef __cplusplus
		env->ReleaseStringUTFChars(attrname,aname);
#else
		(*env)->ReleaseStringUTFChars(env,attrname,aname);
#endif
		h45JNIFatalError( env, "h4toh5sdsattrname:  group name not pinned");
		return ;
	}

	status = H4toh5sdsattrname((hid_t) h45id,
                    (int32) sdsid,
                    (char*) gname,
                    (char*) dsname,
                    (char*) aname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (dsname != NULL) {
		env->ReleaseStringUTFChars(h5dsetname,dsname);
	}
	env->ReleaseStringUTFChars(attrname,aname);
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (dsname != NULL) {
		(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
	}
	(*env)->ReleaseStringUTFChars(env,attrname,aname);
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5sdsattrindex
 * Signature: (IILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5sdsattrindex
  (JNIEnv * env, jclass cls, jint h45id, jint sdsid,
jstring groupname, jstring h5dsetname, jint index)
{
	hid_t status;
	char* gname;
	char* dsname;
	jboolean isCopy = JNI_FALSE;

	/* check range of index? */

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5sdsattrindex:  groupname is NULL");
		return ;
	}

	if (h5dsetname == NULL) {
		dsname = NULL;
	} else {
#ifdef __cplusplus
		dsname = (char *)env->GetStringUTFChars(h5dsetname,&isCopy);
#else
		dsname = (char *)(*env)->GetStringUTFChars(env,h5dsetname,&isCopy);
#endif
		if (dsname == NULL) {
			h45JNIFatalError( env, "h4toh5sdsattrindex:  sds name not pinned");
			return ;
		}
	}

#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		if (dsname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
		}
		h45JNIFatalError( env, "h4toh5sdsattrindex:  group name not pinned");
		return ;
	}

	status = H4toh5sdsattrindex((hid_t) h45id,
                    (int32) sdsid,
                    (char*) gname,
                    (char*) dsname,
                    (int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (dsname != NULL) {
		env->ReleaseStringUTFChars(h5dsetname,dsname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (dsname != NULL) {
		(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5image
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5image
  (JNIEnv *env, jclass cls, jint h45id, jint riid, 
jstring groupname, 
jstring dsetname, 
jstring palgroupname, 
jstring palname, 
jint attr_flag, 
jint pal_flag)
{
	hid_t status;
	char* pgname = NULL;
	char* gname = NULL;
	char* dsname = NULL;
	char* pname = NULL;
	jboolean isCopy = JNI_FALSE;

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5image:  groupname is NULL");
		return ;
	}

	if (dsetname != NULL) {
#ifdef __cplusplus
		dsname = (char *)env->GetStringUTFChars(dsetname,&isCopy);
#else
		dsname = (char *)(*env)->GetStringUTFChars(env,dsetname,&isCopy);
#endif
		if (dsname == NULL) {
			h45JNIFatalError( env, "h4toh5image:  dataset name not pinned");
			return ;
		}
	}

#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		if (dsetname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(dsetname,dsname);
#else
			(*env)->ReleaseStringUTFChars(env,dsetname,dsname);
#endif
			h45JNIFatalError( env, "h4toh5image:  group name not pinned");
			return ;
		}
	}

	if (palgroupname != NULL) {
#ifdef __cplusplus
		pgname = (char *)env->GetStringUTFChars(palgroupname,&isCopy);
#else
		pgname = (char *)(*env)->GetStringUTFChars(env,palgroupname,&isCopy);
#endif
		if (pgname == NULL) {
#ifdef __cplusplus
			if (dsetname != NULL) {
				env->ReleaseStringUTFChars(dsetname,dsname);
			}
			env->ReleaseStringUTFChars(groupname,gname);
#else
			if (dsetname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dsetname,dsname);
			}
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
			h45JNIFatalError( env, "h4toh5image:  file name not pinned");
			return ;
		}
	}
	if (palname != NULL) {
#ifdef __cplusplus
		palname = (char *)env->GetStringUTFChars(palname,&isCopy);
#else
		pname = (char *)(*env)->GetStringUTFChars(env,palname,&isCopy);
#endif
		if (pname == NULL) {
#ifdef __cplusplus
			if (dsetname != NULL) {
				env->ReleaseStringUTFChars(dsetname,dsname);
			}
			env->ReleaseStringUTFChars(groupname,gname);
			if (palgroupname != NULL) {
				env->ReleaseStringUTFChars(palgroupname,pgname);
			}
#else
			if (dsetname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dsetname,dsname);
			}
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
			if (palgroupname != NULL) {
				(*env)->ReleaseStringUTFChars(env,palgroupname,pgname);
			}
#endif
			h45JNIFatalError( env, "h4toh5image:  pal name not pinned");
			return ;
		}
	}

	status = H4toh5image((hid_t) h45id,
                    (int32) riid,
                    (char*) gname,
                    (char*) dsname,
                    (char*) pgname,
                    (char*) pname,
		(int)	attr_flag,
		(int)	pal_flag);

#ifdef __cplusplus
	if (dsetname != NULL) {
		env->ReleaseStringUTFChars(dsetname,dsname);
	}
	env->ReleaseStringUTFChars(groupname,gname);
	if (palgroupname != NULL) {
		env->ReleaseStringUTFChars(palgroupname,ptname);
	}
	if (palname != NULL) {
		env->ReleaseStringUTFChars(palname,pname);
	}
#else
	if (dsetname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dsetname,dsname);
	}
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (palgroupname != NULL) {
		(*env)->ReleaseStringUTFChars(env,palgroupname,pgname);
	}
	if (palname != NULL) {
		(*env)->ReleaseStringUTFChars(env,palname,pname);
	}
#endif

	if ((int)status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5imageattrname
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5imageattrname
  (JNIEnv * env, jclass cls, jint h45id, jint grid, jstring parent, 
jstring h5dataset, jstring attrname)
{
	hid_t status;
	char* pname;
	char* dname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5imageattrname:  parent is NULL");
		return ;
	}

	if (attrname == NULL) {
		h45nullArgument( env, "h4toh5imageattrname:  attrname is NULL");
		return ;
	}

	if (h5dataset == NULL) {
		dname = NULL;		
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(h5dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,h5dataset,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5imageattrname:  h5dataset name not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dataset,dname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dataset,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5imageattrname:  parent name not pinned");
		return ;
	}

#ifdef __cplusplus
	aname = (char *)env->GetStringUTFChars(attrname,&isCopy);
#else
	aname = (char *)(*env)->GetStringUTFChars(env,attrname,&isCopy);
#endif
	if (aname == NULL) {
#ifdef __cplusplus
		env->ReleaseStringUTFChars(parent,pname);
#else
		(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dataset,dname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dataset,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5imageattrname:  attr name not pinned");
		return ;
	}

	status = H4toh5imageattrname((hid_t) h45id,
                    (int32) grid,
                    (char*) pname,
                    (char*) dname,
		    (char*) aname);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(attrname,aname);
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(h5dataset,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,attrname,aname);
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,h5dataset,dname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5imageattrindex
 * Signature: (IILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5imageattrindex
  (JNIEnv * env, jclass cls, jint h45id, jint vgid, jstring parent, 
jstring h5dataset, jint index)
{
	hid_t status;
	char* pname;
	char* dname;
	jboolean isCopy = JNI_FALSE;

	/* check range of index */

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5imageattrindex:  parent is NULL");
		return ;
	}

	if (h5dataset == NULL) {
		dname = NULL;		
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(h5dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,h5dataset,&isCopy);
#endif
		if (dname == NULL) {
			h45JNIFatalError( env, "h4toh5imageattrindex:  h5dataset name not pinned");
			return;
		}
	}
#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		if (dname != NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(h5dataset,dname);
#else
			(*env)->ReleaseStringUTFChars(env,h5dataset,dname);
#endif
		}
		h45JNIFatalError( env, "h4toh5imageattrindex:  parent name not pinned");
		return ;
	}

	status = H4toh5imageattrindex((hid_t) h45id,
                    (int32) vgid,
                    (char*) pname,
                    (char*) dname,
		    (int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(h5dataset,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,h5dataset,dname);
	}
#endif
	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5pal
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5pal
  (JNIEnv *env, jclass cls, jint h45id, jint palid, jstring h5path, 
jstring groupname, jstring h5dsetname, jstring paldsetname, 
jint attr_flag, jint ref_flag)
{
	hid_t status;
	char* pname;
	char* gname;
	char* dsname;
	char* palname;
	jboolean isCopy = JNI_FALSE;

	if (h5dsetname == NULL) {
		dsname = NULL;
	} else {
#ifdef __cplusplus
		dsname = (char *)env->GetStringUTFChars(h5dsetname,&isCopy);
#else
		dsname = (char *)(*env)->GetStringUTFChars(env,h5dsetname,&isCopy);
#endif
		if (dsname == NULL) {
			h45JNIFatalError( env, "h4toh5pal:  dataset name not pinned");
			return ;
		}
	}

	if (groupname == NULL) {
		gname = NULL;
	} else {
#ifdef __cplusplus
		gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
		gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
		if (gname == NULL) {
			if (dsname != NULL) {
#ifdef __cplusplus
				env->ReleaseStringUTFChars(h5dsetname,dsname);
#else
				(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
#endif
			}
			h45JNIFatalError( env, "h4toh5pal:  group name not pinned");
			return ;
		}
	}

	if (h5path == NULL) {
		pname = NULL;
	} else {
#ifdef __cplusplus
		pname = (char *)env->GetStringUTFChars(h5path,&isCopy);
#else
		pname = (char *)(*env)->GetStringUTFChars(env,h5path,&isCopy);
#endif
		if (pname == NULL) {
#ifdef __cplusplus
			if (dsname != NULL) {
				env->ReleaseStringUTFChars(h5dsetname,dsname);
			}
			if (gname != NULL) {
				env->ReleaseStringUTFChars(groupname,gname);
			}
#else
			if (dsname != NULL) {
				(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
			}
			if (gname != NULL) {
				(*env)->ReleaseStringUTFChars(env,groupname,gname);
			}
#endif
			h45JNIFatalError( env, "h4toh5pal:  file name not pinned");
			return ;
		}
	}

	if (paldsetname == NULL) {
		palname = NULL;
	} else {
#ifdef __cplusplus
		palname = (char *)env->GetStringUTFChars(paldsetname,&isCopy);
#else
		palname = (char *)(*env)->GetStringUTFChars(env,paldsetname,&isCopy);
#endif
		if (palname == NULL) {
#ifdef __cplusplus
			if (dsname != NULL) {
				env->ReleaseStringUTFChars(h5dsetname,dsname);
			}
			if (gname != NULL) {
				env->ReleaseStringUTFChars(groupname,gname);
			}
			if (pname != NULL) {
				env->ReleaseStringUTFChars(h5path,pname);
			}
#else
			if (dsname != NULL) {
				(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
			}
			if (gname != NULL) {
				(*env)->ReleaseStringUTFChars(env,groupname,gname);
			}
			if (pname != NULL) {
				(*env)->ReleaseStringUTFChars(env,h5path,pname);
			}
#endif
			h45JNIFatalError( env, "h4toh5pal:  pal name not pinned");
			return ;
		}
	}

	status = H4toh5pal((hid_t) h45id,
                    (int32) palid,
                    (char*) pname,
                    (char*) gname,
                    (char*) dsname,
                    (char*) palname,
		(int)	attr_flag,
		(int)	ref_flag);

#ifdef __cplusplus
		if (dsname != NULL) {
			env->ReleaseStringUTFChars(h5dsetname,dsname);
		}
		if (gname != NULL) {
			env->ReleaseStringUTFChars(groupname,gname);
		}
		if (pname != NULL) {
			env->ReleaseStringUTFChars(h5path,pname);
		}
		if (palname != NULL) {
			env->ReleaseStringUTFChars(paldsetname,palname);
		}
#else
		if (dsname != NULL) {
			(*env)->ReleaseStringUTFChars(env,h5dsetname,dsname);
		}
		if (gname != NULL) {
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
		}
		if (pname != NULL) {
			(*env)->ReleaseStringUTFChars(env,h5path,pname);
		}
		if (palname != NULL) {
			(*env)->ReleaseStringUTFChars(env,paldsetname,palname);
		}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annofil_label
 * Signature: (ILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annofil_1label
  (JNIEnv * env, jclass cls, jint h45id, jstring label, jint index)
{
	hid_t status;
	char* alabel;
	jboolean isCopy = JNI_FALSE;

	if (label == NULL) {
		h45nullArgument( env, "h4toh5annofil_label:  label is NULL");
		return ;
	}

#ifdef __cplusplus
	alabel = (char *)env->GetStringUTFChars(label,&isCopy);
#else
	alabel = (char *)(*env)->GetStringUTFChars(env,label,&isCopy);
#endif
	if (alabel == NULL) {
		h45JNIFatalError( env, "h4toh5annofil_label:  label name not pinned");
		return ;
	}

	status = H4toh5annofil_label((hid_t) h45id,
                    (char*) alabel, (int)index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(label,alabel);
#else
	(*env)->ReleaseStringUTFChars(env,label,alabel);
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annofil_desc
 * Signature: (ILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annofil_1desc
  (JNIEnv *env, jclass cls, jint h45id, jstring descname, jint index)
{
	hid_t status;
	char* dname;
	jboolean isCopy = JNI_FALSE;

	if (descname == NULL) {
		h45nullArgument( env, "h4toh5annofile_desc:  descname is NULL");
		return ;
	}

#ifdef __cplusplus
	dname = (char *)env->GetStringUTFChars(descname,&isCopy);
#else
	dname = (char *)(*env)->GetStringUTFChars(env,descname,&isCopy);
#endif
	if (dname == NULL) {
		h45JNIFatalError( env, "h4toh5annofile_desc:  descname name not pinned");
		return ;
	}

	status = H4toh5annofil_desc((hid_t) h45id,
                    (char*)dname,(int)index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(descname,dname);
#else
	(*env)->ReleaseStringUTFChars(env,descname,dname);
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annofil_alllabels
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annofil_1alllabels
  (JNIEnv *env, jclass clss, jint h45id)
{
	hid_t status;

	status = H4toh5annofil_alllabels((hid_t)h45id); 

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annofil_alldescs
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annofil_1alldescs
  (JNIEnv *env, jclass clss, jint h45id)
{
	hid_t status;

	status = H4toh5annofil_alldescs((hid_t)h45id); 

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annoobj_label
 * Signature: (ILjava/lang/String;Ljava/lang/String;IILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annoobj_1label
  (JNIEnv *env, jclass clss, jint h45id, jstring parent, jstring dataset, 
jint objectref, jint objtag, jstring annoname, jint annoindex)
{
	hid_t status;
	char* pname;
	char* dname;
	char* aname;
	jboolean isCopy = JNI_FALSE;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5annoobj_label:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5annoobj_label:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5annoobj_label:  parent name not pinned");
			return ;
		}
	}

	if (annoname == NULL) {
		aname = NULL;
	} else {
#ifdef __cplusplus
		aname = (char *)env->GetStringUTFChars(annoname,&isCopy);
#else
		aname = (char *)(*env)->GetStringUTFChars(env,annoname,&isCopy);
#endif
		if (aname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
			if (dname != NULL) {
				env->ReleaseStringUTFChars(dataset,dname);
			}
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
			if (dname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dataset,dname);
			}
#endif
			h45JNIFatalError( env, "h4toh5annoobj_label:  parent name not pinned");
			return ;
		}
	}

	status = H4toh5annoobj_label((hid_t) h45id,
                    (char*) pname,
                    (char*) dname,
		    (uint16) objectref,
		    (int32) objtag,
                    (char*) aname,
		(int)	annoindex);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
	if (aname != NULL) {
		env->ReleaseStringUTFChars(annoname,aname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
	if (aname != NULL) {
		(*env)->ReleaseStringUTFChars(env,annoname,aname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annoobj_desc
 * Signature: (ILjava/lang/String;Ljava/lang/String;IILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annoobj_1desc
  (JNIEnv *env, jclass clss, jint h45id, 
jstring parent, jstring dataset, jint ref, jint tag, 
jstring description, jint index)
{
	hid_t status;
	char* pname;
	char* dname;
	char* desc;
	jboolean isCopy;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5annoobj_desc:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5annoobj_desc:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5annoobj_desc:  parent name not pinned");
			return;
		}
	}

	if (description == NULL) {
		desc = NULL;
	} else {
#ifdef __cplusplus
		desc = (char *)env->GetStringUTFChars(description,&isCopy);
#else
		desc = (char *)(*env)->GetStringUTFChars(env,description,&isCopy);
#endif
		if (desc == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
			if (dname != NULL) {
				env->ReleaseStringUTFChars(dataset,dname);
			}
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
			if (dname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dataset,dname);
			}
#endif
			h45JNIFatalError( env, "h4toh5annoobj_desc:  description name not pinned");
			return ;
		}
	}

	status = H4toh5annoobj_desc((hid_t) h45id,
                    (char*) pname,
                    (char*) dname,
		    (uint16) ref,
		    (int32) tag,
                    (char*) desc,
		(int)	index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
	if (desc != NULL) {
		env->ReleaseStringUTFChars(description,desc);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
	if (desc != NULL) {
		(*env)->ReleaseStringUTFChars(env,description,desc);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annoobj_alllabels
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annoobj_1alllabels
  (JNIEnv *env, jclass clss, jint h45id, 
jstring parent, jstring dataset, jint ref, jint tag)
{
	hid_t status;
	char* pname;
	char* dname;
	jboolean isCopy;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5annoobjalllabels:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5annoobjalllabels:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5annoobjalllabels:  parent name not pinned");
			return;
		}
	}

	status = H4toh5annoobj_alllabels((hid_t) h45id,
                    (char*) pname,
                    (char*) dname,
		    (uint16) ref,
		    (int32) tag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}


/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5annoobj_alldescs
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5annoobj_1alldescs
  (JNIEnv *env, jclass clss, jint h45id, 
jstring parent, jstring dataset, jint ref, jint tag)
{
	hid_t status;
	char* pname;
	char* dname;
	jboolean isCopy;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5annoobj_alldescs:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5annoobj_alldescs:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5annoobj_alldescs:  parent name not pinned");
			return ;
		}
	}

	status = H4toh5annoobj_alldescs((hid_t) h45id,
                    (char*) pname,
                    (char*) dname,
		    (uint16) ref,
		    (int32) tag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5alldimscale
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5alldimscale
  (JNIEnv *env, jclass clss, jint h45id, jint sdsid, 
jstring parent, jstring dataset, jstring dimgroup, jint attr_flag, 
jint ref_flag)
{
	hid_t status;
	char* pname;
	char* dname;
	char* dimg;
	jboolean isCopy;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5alldimscale:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5alldimscale:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5alldimscale:  parent name not pinned");
			return ;
		}
	}

	if (dimgroup == NULL) {
		dimg = NULL;
	} else {
#ifdef __cplusplus
		dimg = (char *)env->GetStringUTFChars(dimgroup,&isCopy);
#else
		dimg = (char *)(*env)->GetStringUTFChars(env,dimgroup,&isCopy);
#endif
		if (dimg == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
			if (dname != NULL) {
				env->ReleaseStringUTFChars(dataset,dname);
			}
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
			if (dname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dataset,dname);
			}
#endif
			h45JNIFatalError( env, "h4toh5alldimscale:  parent name not pinned");
			return ;
		}
	}

	status = H4toh5alldimscale((hid_t) h45id,
			(int32) sdsid,
                    (char*) pname,
                    (char*) dname,
                    (char*) dimg,
		    (int) attr_flag,
		    (int) ref_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
	if (dimg != NULL) {
		env->ReleaseStringUTFChars(dimgroup,dimg);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
		if (dname != NULL) {
	(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
	if (dimg != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimgroup,dimg);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5onedimscale
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;III)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5onedimscale
  (JNIEnv *env, jclass clss, jint h45id, jint sdsid, 
jstring parent, jstring dataset, jstring dimgroup, 
jstring dimdataset,
jint attr_flag, 
jint ref_flag,
jint index)
{
	hid_t status;
	char* pname;
	char* dname;
	char* dimg;
	char* dimds;
	jboolean isCopy;

	if (parent == NULL) {
		h45nullArgument( env, "h4toh5onedimscale:  group path is NULL");
		return ;
	}

#ifdef __cplusplus
	pname = (char *)env->GetStringUTFChars(parent,&isCopy);
#else
	pname = (char *)(*env)->GetStringUTFChars(env,parent,&isCopy);
#endif
	if (pname == NULL) {
		h45JNIFatalError( env, "h4toh5onedimscale:  parent name not pinned");
		return ;
	}

	if (dataset == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dataset,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dataset,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
#endif
			h45JNIFatalError( env, "h4toh5onedimscale:  parent name not pinned");
			return ;
		}
	}

	if (dimgroup == NULL) {
		dimg = NULL;
	} else {
#ifdef __cplusplus
		dimg = (char *)env->GetStringUTFChars(dimgroup,&isCopy);
#else
		dimg = (char *)(*env)->GetStringUTFChars(env,dimgroup,&isCopy);
#endif
		if (dimg == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
			if (dname != NULL) {
				env->ReleaseStringUTFChars(dataset,dname);
			}
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
			if (dname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dataset,dname);
			}
#endif
			h45JNIFatalError( env, "h4toh5onedimscale:  parent name not pinned");
			return ;
		}
	}

	if (dimdataset == NULL) {
		dimds = NULL;
	} else {
#ifdef __cplusplus
		dimds = (char *)env->GetStringUTFChars(dimdataset,&isCopy);
#else
		dimds = (char *)(*env)->GetStringUTFChars(env,dimdataset,&isCopy);
#endif
		if (dimds == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(parent,pname);
			if (dname != NULL) {
				env->ReleaseStringUTFChars(dataset,dname);
			}
			if (dimg != NULL) {
				env->ReleaseStringUTFChars(dimgroup,dimg);
			}
#else
			(*env)->ReleaseStringUTFChars(env,parent,pname);
			if (dname != NULL) {
				(*env)->ReleaseStringUTFChars(env,dataset,dname);
			}
			if (dimg != NULL) {
				(*env)->ReleaseStringUTFChars(env,dimgroup,dimg);
			}
#endif
			h45JNIFatalError( env, "h4toh5onedimscale:  dimg name not pinned");
			return ;
		}
	}

	status = H4toh5onedimscale((hid_t) h45id, (int32) sdsid,
                    (char*) pname,
                    (char*) dname,
                    (char*) dimg,
                    (char*) dimds,
		    (int) attr_flag,
		    (int) ref_flag,
			(int) index);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(parent,pname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(dataset,dname);
	}
	if (dimg != NULL) {
		env->ReleaseStringUTFChars(dimgroup,dimg);
	}
	if (dimds != NULL) {
		env->ReleaseStringUTFChars(dimdataset,dimds);
	}
#else
	(*env)->ReleaseStringUTFChars(env,parent,pname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dataset,dname);
	}
	if (dimg != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimgroup,dimg);
	}
	if (dimds != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimdataset,dimds);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5_glosdsattr
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5_1glosdsattr
  (JNIEnv *env, jclass clss, jint h45id)
{
	hid_t status;
	jboolean isCopy = JNI_FALSE;

	status = H4toh5_glosdsattr((hid_t)h45id);

	if (status < 0) {
		h45libraryError(env);
	}
	return ;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5_glograttr
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5_1glograttr
  (JNIEnv *env, jclass cls, jint h45id)
{
	hid_t status;
	jboolean isCopy = JNI_FALSE;

	status = H4toh5_glograttr((hid_t)h45id);

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5alllonevdata
 * Signature: (ILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5alllonevdata
  (JNIEnv *env, jclass cls, jint h45id, jstring dirname, jint attr_flag)
{
	hid_t status;
	char * dname;
	jboolean isCopy = JNI_FALSE;

	if (dirname == NULL) {
		h45nullArgument( env, "h4toh5alllonevdata:  dirname is null pinned");
		return;
	} 
#ifdef __cplusplus
	dname = (char *)env->GetStringUTFChars(dirname,&isCopy);
#else
	dname = (char *)(*env)->GetStringUTFChars(env,dirname,&isCopy);
#endif
	if (dname == NULL) {
		h45JNIFatalError( env, "h4toh5alllonevdata:  dirname name not pinned");
		return;
	}

	status = H4toh5alllonevdata((hid_t)h45id,dname,(int)attr_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(dirname,dname);
#else
	(*env)->ReleaseStringUTFChars(env,dirname,dname);
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5alllonesds
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5alllonesds
  (JNIEnv *env, jclass cls, jint h45id, jstring groupname, 
   jstring dimgroupname, jint dim_flag, jint attr_flag)
{
	hid_t status;
	char * gname;
	char * dname;
	jboolean isCopy = JNI_FALSE;

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5alllonesds:  groupname name is null");
		return;
	} 

#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		h45JNIFatalError( env, "h4toh5alllonesds:  groupname name not pinned");
		return;
	}

	if (dimgroupname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dimgroupname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dimgroupname,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
			h45JNIFatalError( env, "h4toh5alllonesds:  dimgroupname name not pinned");
			return;
		}
	}

	status = H4toh5alllonesds((hid_t)h45id,gname,dname,(int)dim_flag,(int)attr_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(groupdirname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimgroupname,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

/*
 * Class:     ncsa_hdf_h4toh5lib_h4toh5
 * Method:    H4toh5allloneimage
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_h4toh5lib_h4toh5_H4toh5allloneimage
  (JNIEnv *env, jclass cls, jint h45id, jstring groupname, 
   jstring dimgroupname, jint attr_flag, jint pal_flag)
{
	hid_t status;
	char * gname;
	char * dname;
	jboolean isCopy = JNI_FALSE;

	if (groupname == NULL) {
		h45nullArgument( env, "h4toh5allloneimage:  groupname is null");
		return;
	} 
#ifdef __cplusplus
	gname = (char *)env->GetStringUTFChars(groupname,&isCopy);
#else
	gname = (char *)(*env)->GetStringUTFChars(env,groupname,&isCopy);
#endif
	if (gname == NULL) {
		h45JNIFatalError( env, "h4toh5allloneimage:  groupname name not pinned");
		return;
	}

	if (dimgroupname == NULL) {
		dname = NULL;
	} else {
#ifdef __cplusplus
		dname = (char *)env->GetStringUTFChars(dimgroupname,&isCopy);
#else
		dname = (char *)(*env)->GetStringUTFChars(env,dimgroupname,&isCopy);
#endif
		if (dname == NULL) {
#ifdef __cplusplus
			env->ReleaseStringUTFChars(groupname,gname);
#else
			(*env)->ReleaseStringUTFChars(env,groupname,gname);
#endif
			h45JNIFatalError( env, "h4toh5allloneimage:  dimgroupname name not pinned");
			return;
		}
	}

	status = H4toh5allloneimage((hid_t)h45id,gname,dname,(int)attr_flag,
		(int)pal_flag);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(groupname,gname);
	if (dname != NULL) {
		env->ReleaseStringUTFChars(groupdirname,dname);
	}
#else
	(*env)->ReleaseStringUTFChars(env,groupname,gname);
	if (dname != NULL) {
		(*env)->ReleaseStringUTFChars(env,dimgroupname,dname);
	}
#endif

	if (status < 0) {
		h45libraryError(env);
	}
	return;
}

#ifdef __cplusplus
}
#endif
