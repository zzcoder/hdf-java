
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
 *  HDF 4.1 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *     http://hdf.ncsa.uiuc.edu
 *
 */
#include "hdf.h"

#include <jni.h>

extern jboolean h4outOfMemory( JNIEnv *env, char *functName);
/* exceptions??... */

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vstart 
(JNIEnv *env,
jclass class, 
jint fid)
{
	intn rval;
	rval = Vstart((int32)fid);
	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vattach 
(JNIEnv *env,
jclass class, 
jint fid,
jint vgroup_ref,
jstring accessmode)
{
	int   retVal;
	char  *access;

	access = (char *)(*env)->GetStringUTFChars(env,accessmode,0);

	/* open HDF file specified by ncsa_hdf_HDF_file */
	retVal = Vattach(fid, vgroup_ref, (char *)access);

	(*env)->ReleaseStringUTFChars(env,accessmode,access);
	return retVal;
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vdetach 
(JNIEnv *env,
jclass class, 
jint vgroup_id)
{
	Vdetach((int32)vgroup_id);
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vend 
(JNIEnv *env,
jclass class, 
jint fid)
{
	Vend(fid);
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetid 
(JNIEnv *env,
jclass class, 
jint fid,
jint vgroup_ref)
{
	return(Vgetid((int32)fid, (int32)vgroup_ref));
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetclass 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jobjectArray hdfclassname)
{
	char *className;
	jstring rstring;
	jclass jc;
	jobject o;
	jboolean bb;
	int r;

	if (hdfclassname == NULL) {
		/* nullpointer exception */
		return;
	}
	r = (*env)->GetArrayLength(env,hdfclassname);
	if (r == 0) {
		/* invalid arg exception */
		return;
	}

	className = (char *)malloc(VGNAMELENMAX+1);
	if (className == NULL) {
		h4outOfMemory(env, "Vgetclass");
		return;
	}
	/* get the class name of the vgroup */
	r = Vgetclass(vgroup_id, className);

	if (r < 0) {
		/* exception -- failed */
		return;
	}

	className[VGNAMELENMAX] = '\0';
	/* convert it to java string */
	rstring = (*env)->NewStringUTF(env,className);

	/*  create a Java String object in the calling environment... */
	jc = (*env)->FindClass(env, "java/lang/String");
	if (jc == NULL) {
		free(className);
		return ; /* exception is raised */
	}

	o = (*env)->GetObjectArrayElement(env,hdfclassname,0);
	if (o == NULL) {
		free(className);
		return ;
	}
	bb = (*env)->IsInstanceOf(env,o,jc);
	if (bb == JNI_FALSE) {
		free(className);
		return ;
	}
	(*env)->SetObjectArrayElement(env,hdfclassname,0,(jobject)rstring);

	free(className);
	return;
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetname 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jobject hdfname)
{
	char *name;
	jstring rstring;
	jclass jc;
	jobject o;
	jboolean bb;

	name = (char *) malloc(VGNAMELENMAX+1);
	if (name == NULL) {
		h4outOfMemory(env, "Vgetname");
		return;
	}
	Vgetname(vgroup_id, name);

	name[VGNAMELENMAX] = '\0';

	rstring = (*env)->NewStringUTF(env,name);

	jc = (*env)->FindClass(env, "java/lang/String");
	if (jc == NULL) {
		free(name);
		return ; /* exception is raised */
	}
	o = (*env)->GetObjectArrayElement(env,hdfname,0);
	if (o == NULL) {
		free(name);
		return ;
	}
	bb = (*env)->IsInstanceOf(env,o,jc);
	if (bb == JNI_FALSE) {
		free(name);
		return ;
	}
	(*env)->SetObjectArrayElement(env,hdfname,0,(jobject)rstring);

	free(name);
	return;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Visvg 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jint vgroup_ref)
{

	int rval;
	rval = Visvg(vgroup_id, vgroup_ref);

	if (rval == TRUE || rval == 1) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Visvs 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jint vdata_ref)
{

	int rval;
	rval = Visvs(vgroup_id, vdata_ref);

	if (rval == TRUE || rval == 1) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}

}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgettagrefs 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jintArray tags,  /* OUT:  this should be allocated by caller ? */
jintArray refs,  /* OUT:  this should be allocated by caller ? */
jint size)
{

	jint *tagVal;
	jint *refVal;
	int retVal;

	jboolean iscopy;

	tagVal = (*env)->GetIntArrayElements(env,tags,&iscopy);
	refVal = (*env)->GetIntArrayElements(env,refs,&iscopy);

	if (tagVal == NULL || refVal == NULL) {
		/* exception */
		return FAIL;
	}

	/* get the tag/ref pairs number in the vgroup */
	retVal = Vgettagrefs(vgroup_id, (int32 *)tagVal, (int32 *)refVal, size);

	if ( retVal == FAIL ) {
		(*env)->ReleaseIntArrayElements(env,tags,tagVal,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,refs,refVal,JNI_ABORT);
	} else {
		(*env)->ReleaseIntArrayElements(env,tags,tagVal,0);
		(*env)->ReleaseIntArrayElements(env,refs,refVal,0);
	}

	return retVal;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgettagref 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jint index,
jintArray tagref)  /* OUT:  int tag, ref */
{
	int retVal;
	jint * theArgs;
	jboolean bb;

	theArgs = (*env)->GetIntArrayElements(env,tagref,&bb);

	/* get the tag/ref pairs number in the vgroup */
	retVal = Vgettagref(vgroup_id, index, (int32 *)&(theArgs[0]), 
			(int32 *)&(theArgs[1]));

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,tagref,theArgs,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,tagref,theArgs,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vntagrefs 
(JNIEnv *env,
jclass class, 
jint vgroup_id)
{
	return (Vntagrefs(vgroup_id));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vinqtagref 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jint tag,
jint ref
)
{
	return(Vinqtagref((int32)vgroup_id, (int32)tag, (int32)ref));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vlone 
(JNIEnv *env,
jclass class, 
jint fid,
jintArray ref_array,
jint arraysize)
{
	int retVal;
	jint * arr;
	jboolean bb;

	if (ref_array == NULL) {
		arr = NULL;
	} else {
		arr = (*env)->GetIntArrayElements(env,ref_array,&bb);
	}

	/* get the lone group reference number in the vgroup */
	retVal = Vlone(fid, (int32 *)arr, arraysize);

	if (arr != NULL) {
		(*env)->ReleaseIntArrayElements(env,ref_array,arr, 0);
	}

	return retVal;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vaddtagref 
( JNIEnv *env,
jclass class, 
jint vgroup_id,
jint tag,
jint ref)
{
	return (Vaddtagref((int32) vgroup_id, (int32) tag, (int32) ref));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vdeletetagref 
( JNIEnv *env,
jclass class, 
jint vgroup_id,
jint tag,
jint ref)
{
	return (Vdeletetagref((int32) vgroup_id, (int32) tag, (int32) ref));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vclose_I 
( JNIEnv *env,
jclass class, 
jint file_id)
{
	intn rval;
	rval =  Vclose((int32) file_id);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vfind 
( JNIEnv *env,
jclass class, 
jint file_id,
jstring vgname)
{
	int32 rval;
	char * vgn;
	vgn = (char *)(*env)->GetStringUTFChars(env,vgname,0);

	rval = Vfind((int32)file_id, (char *)vgn);

	(*env)->ReleaseStringUTFChars(env,vgname,vgn);

	return rval;

}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vfindclass 
( JNIEnv *env,
jclass class, 
jint file_id,
jstring vgclassname)
{
	int32 rval;
	char * vgcn;
	vgcn = (char *)(*env)->GetStringUTFChars(env,vgclassname,0);

	rval = Vfindclass((int32)file_id, (char *)vgcn);

	(*env)->ReleaseStringUTFChars(env,vgclassname,vgcn);

	return rval;

}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vflocate 
( JNIEnv *env,
jclass class, 
jint key,
jstring field)
{
	int32 rval;
	char * fld;
	fld = (char *)(*env)->GetStringUTFChars(env,field,0);

	rval = Vflocate((int32)key, (char *)fld);

	(*env)->ReleaseStringUTFChars(env,field,fld);

	return rval;

}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetnext 
( JNIEnv *env,
jclass class, 
jint vkey,
jint elem_ref)
{
	return(Vgetnext((int32) vkey, (int32) elem_ref));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vinquire 
( JNIEnv *env,
jclass class, 
jint vgroup_id, 
jintArray n_entries,   /* OUT: int */
jobjectArray vgroup_name) /* OUT: String */
{
	intn rval;
	jclass jc;
	jstring rstring;
	char *name;
	jint * theArg;
	jobject o;
	jboolean bb;

	name = (char *)malloc(VGNAMELENMAX+1);
	if (name == NULL) {
		h4outOfMemory(env, "Vinquire");
		return JNI_FALSE;
	}
	theArg = (*env)->GetIntArrayElements(env,n_entries,&bb);

	rval = Vinquire((int32) vgroup_id, (int32 *)&(theArg[0]), name);

	name[VGNAMELENMAX] = '\0';

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,n_entries,theArg, JNI_ABORT);
		free(name);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,n_entries,theArg, 0);
		jc = (*env)->FindClass(env, "java/lang/String");
		if (jc == NULL) {
			free(name);
			return JNI_FALSE;
		}
		o = (*env)->GetObjectArrayElement(env,vgroup_name,0);
		if (o == NULL) {
			free(name);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,jc);
		if (bb == JNI_FALSE) {
			free(name);
			return JNI_FALSE;
		}
		rstring = (*env)->NewStringUTF(env,name);
		(*env)->SetObjectArrayElement(env,vgroup_name,0,(jobject)rstring);
		free(name);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vinsert 
( JNIEnv *env,
jclass class, 
jint vgroup_id,
jint v_id)
{
	return (Vinsert((int32) vgroup_id, (int32) v_id));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vnrefs 
( JNIEnv *env,
jclass class, 
jint vkey,
jint tag)
{
	return(Vnrefs((int32) vkey, (int32) tag));
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vopen 
( JNIEnv *env,
jclass class, 
jstring filename,
jint access,
jshort ndds)
{
	intn rval;
	char * str;
	str = (char *)(*env)->GetStringUTFChars(env,filename,0);


	rval = Vopen((char *)str, (intn) access, (int16) ndds);

	(*env)->ReleaseStringUTFChars(env,filename,str);

	return rval;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vsetclass 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jstring hdfclassname)
{
	intn rval;
	char * str;
	str = (char *)(*env)->GetStringUTFChars(env,hdfclassname,0);

	rval = Vsetclass((int32)vgroup_id, (char *)str);

	(*env)->ReleaseStringUTFChars(env,hdfclassname,str);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vsetname 
(JNIEnv *env,
jclass class, 
jint vgroup_id,
jstring name)
{
	intn rval;
	char *str;
	str =  (char *)(*env)->GetStringUTFChars(env,name,0);

	rval = Vsetname((int32)vgroup_id, (char *)str);

	(*env)->ReleaseStringUTFChars(env,name,str);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vattrinfo 
( JNIEnv *env,
jclass class, 
jint id,
jint index,
jarray name,  /* OUT:  String */
jintArray argv)  /* OUT:  NT, count, size */
{
	int32 retVal;
	jint *theArgs;
	jboolean bb;
	jclass Sjc;
	jstring str;
	jobject o;
	char  nam[256];  /* what is the correct constant??? */

	theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

	retVal = Vattrinfo((int32)id, (int32)index, nam, 
		(int32 *)&(theArgs[0]), (int32 *)&(theArgs[1]),
		(int32 *)&(theArgs[2]));

	nam[255] = '\0';

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
		return JNI_FALSE;
	} else {

		(*env)->ReleaseIntArrayElements(env,argv,theArgs,0);

		str = (*env)->NewStringUTF(env,nam);
		o = (*env)->GetObjectArrayElement(env,name,0);
		if (o == NULL) {
			return JNI_FALSE;
		}
		Sjc = (*env)->FindClass(env, "java/lang/String");
		if (Sjc == NULL) {
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,name,0,(jobject)str);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vfindattr 
( JNIEnv *env,
jclass class, 
jint id,
jstring name)
{
	int32 retVal;
	char  *cname;

	cname =(char *) (*env)->GetStringUTFChars(env,name,0);

	retVal = Vfindattr((int32)id, cname);

	(*env)->ReleaseStringUTFChars(env,name,cname);

	return retVal;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetattr 
( JNIEnv *env,
jclass class, 
jint gr_id, 
jint attr_index,  
jbyteArray values)  /* OUT: byte[] */
{
	intn rval;
	jbyte *arr;
	jboolean bb;

	arr = (*env)->GetByteArrayElements(env,values,&bb);
	rval = Vgetattr((int32) gr_id, (int32) attr_index,  (VOIDP) arr);
	if (rval == FAIL) {
		(*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,values,arr,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vgetversion 
( JNIEnv *env,
jclass class, 
jint id)
{
	return (Vgetversion((int32) id));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vnattrs 
( JNIEnv *env,
jclass class, 
jint id)
{
	return (Vnattrs((int32) id));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vsetattr__ILjava_lang_String_2IILjava_lang_String_2 
( JNIEnv *env,
jclass class, 
jint gr_id, 
jstring attr_name,
jint data_type, 
jint count, 
jstring values)  /* IN: String */
{
	intn rval;
	char *str;
	char *val;

	str =(char *) (*env)->GetStringUTFChars(env,attr_name,0);
	val =(char *) (*env)->GetStringUTFChars(env,values,0);

	rval = Vsetattr((int32) gr_id, (char *)str, (int32) data_type, 
	    (int32) count, (VOIDP) val);

	(*env)->ReleaseStringUTFChars(env,attr_name,str);
	(*env)->ReleaseStringUTFChars(env,values,val);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_Vsetattr__ILjava_lang_String_2II_3B 
( JNIEnv *env,
jclass class, 
jint id, 
jstring attr_name,
jint data_type, 
jint count, 
jbyteArray values)  /* IN: byte[] */
{
	intn rval;
	jbyte *arr;
	char *str;
	jboolean bb;

	arr = (*env)->GetByteArrayElements(env,values,&bb);
	str =(char *) (*env)->GetStringUTFChars(env,attr_name,0);

	rval = Vsetattr((int32) id, (char *)str, (int32) data_type, 
	    (int32) count, (VOIDP) arr);

	(*env)->ReleaseStringUTFChars(env,attr_name,str);
	(*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}
