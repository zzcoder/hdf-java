
/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
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
extern jboolean h4NotImplemented( JNIEnv *env, char *functName);

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSattach 
(JNIEnv *env,
jclass clss, 
jint fid,
jint vdata_ref,
jstring accessmode)
{

	int   retVal;
	char  *access;

	access = (char *)(*env)->GetStringUTFChars(env,accessmode,0);

	/* open HDF file specified by ncsa_hdf_HDF_file */
	retVal = VSattach(fid, vdata_ref, (char *)access);

	(*env)->ReleaseStringUTFChars(env,accessmode,access);

	return retVal;
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSdetach 
(JNIEnv *env,
jclass clss, 
jint vdata_id)
{
	VSdetach((int32)vdata_id);
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetid 
(JNIEnv *env,
jclass clss, 
jint fid,
jint vdata_ref)
{
	return(VSgetid((int32)fid, (int32)vdata_ref));
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetclass 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jobjectArray hdfclass) /* OUT: String */
{
	char *className;
	jstring rstring;
	jclass jc;
	jobject o;
	jboolean bb;
	int r;

	if (hdfclass == NULL) {
		/* exception: null argument */
		return;
	}
	r = (*env)->GetArrayLength(env,hdfclass);
	if (r < 1) {
		/* exception: bad argument */
		return;
	}

	className = (char *)malloc(VSNAMELENMAX+1);
	if (className == NULL) {
		h4outOfMemory(env, "VSgetclass");
		return;
	}

	/* get the class class of the vgroup */
	VSgetclass(vdata_id, className);
	className[VSNAMELENMAX] = '\0';

	/* convert it to java string */
	rstring = (*env)->NewStringUTF(env,className);

	o = (*env)->GetObjectArrayElement(env,hdfclass,0);
	if (o == NULL) {
		free(className);
		return;
	}
	jc = (*env)->FindClass(env, "java/lang/String");
	if (jc == NULL) {
		free(className);
		return;
	}
	bb = (*env)->IsInstanceOf(env,o,jc);
	if (bb == JNI_FALSE) {
		free(className);
		return;
	}
	(*env)->SetObjectArrayElement(env,hdfclass,0,(jobject)rstring);

	free(className);
	return;
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetname 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jobjectArray hdfname) /* OUT: String */
{
	char nameName[VSNAMELENMAX+1];
	jstring rstring;
	jclass jc;
	jobject o;
	jboolean bb;

	/* get the name name of the vgroup */
	VSgetname(vdata_id, nameName);

	nameName[VSNAMELENMAX]='\0';

	/* convert it to java string */
	rstring = (*env)->NewStringUTF(env,nameName);

	o = (*env)->GetObjectArrayElement(env,hdfname,0);
	if (o == NULL) {
		return;
	}
	jc = (*env)->FindClass(env, "java/lang/String");
	if (jc == NULL) {
		return;
	}
	bb = (*env)->IsInstanceOf(env,o,jc);
	if (bb == JNI_FALSE) {
		return;
	}
	(*env)->SetObjectArrayElement(env,hdfname,0,(jobject)rstring);

	return;

}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSelts 
(JNIEnv *env,
jclass clss, 
jint vdata_id)
{
	return(VSelts((int32)vdata_id));

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfdefine 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring fieldname,
jint numbertype,
jint order)
{
	int32 retVal;
	char  *fldName;

	fldName = (char *)(*env)->GetStringUTFChars(env,fieldname,0);

	retVal  = VSfdefine((int32)vdata_id,(char *)fldName,(int32)numbertype,(int32)order);

	(*env)->ReleaseStringUTFChars(env,fieldname,fldName);

	if (retVal == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfexist 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring fieldname)
{
	int32 retVal;
	char  *fldName;

	fldName = (char *)(*env)->GetStringUTFChars(env,fieldname,0);

	/* Check the fields */
	retVal  = VSfexist((int32)vdata_id,(char *)fldName);
	(*env)->ReleaseStringUTFChars(env,fieldname,fldName);
	if (retVal != FAIL)
		return JNI_TRUE;
	else
		return JNI_FALSE;

}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfind 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring fieldname)
{
	int32 retVal;
	char  *fldName;

	fldName = (char *)(*env)->GetStringUTFChars(env,fieldname,0);

	/* Check the fields */
	retVal  = VSfind((int32)vdata_id,(char *)fldName);

	(*env)->ReleaseStringUTFChars(env,fieldname,fldName);

	return(retVal);
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetblocksize 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jint blocksize)
{
	int32 retVal;


	/* Check the fields */
	retVal  = VSsetblocksize((int32)vdata_id,(int32)blocksize);

	return(retVal);
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetnumblocks
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jint numblocks)
{
	int32 retVal;


	/* Check the fields */
	retVal  = VSsetnumblocks((int32)vdata_id,(int32)numblocks);

	return(retVal);
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetfields 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jobjectArray fields) /* OUT: String */
{
	int retVal;
	jclass jc;
	char *flds;
	jstring rstring;
	jobject o;
	jboolean bb;

	flds = (char *)malloc(25600); 
	if (flds == NULL) {
		h4outOfMemory(env, "VSgetfields");
		return FAIL;
	}

	/* get the fields name in the vdata */
	retVal = VSgetfields((int32)vdata_id, flds);

	flds[25599] = '\0';

	if (retVal != FAIL) {
		rstring = (*env)->NewStringUTF(env, flds);
		o = (*env)->GetObjectArrayElement(env,fields,0);
		if (o == NULL) {
			free(flds);
			return FAIL;
		}
		jc = (*env)->FindClass(env, "java/lang/String");
		if (jc == NULL) {
			free(flds);
			return FAIL;
		}
		bb = (*env)->IsInstanceOf(env,o,jc);
		if (bb == JNI_FALSE) {
			free(flds);
			return FAIL;
		}
		(*env)->SetObjectArrayElement(env,fields,0,(jobject)rstring);
	}
	free(flds);
	return retVal;
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetinterlace 
(JNIEnv *env,
jclass clss, 
jint vdata_id)
{
	return(VSgetinterlace((int32)vdata_id));
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSinquire 
( JNIEnv *env,
jclass clss, 
jint vdata_id, 
jintArray iargs,  /* OUT: int n_records, interlace, vdata_size */
jobjectArray sargs) /* OUT: String fields, vdata_name */
{
	intn rval;
	char *flds;
	char *name;
	jclass jc;
	jstring rstring;
	jint * theIargs;
	jboolean bb;
	jobject o;


	flds = (char *)malloc(MAX_FIELD_SIZE+1);
	if (flds == NULL) {
		h4outOfMemory(env, "VSinquire");
		return FAIL;
	}

	name = (char *)malloc(MAX_NC_NAME+1);
	if (name == NULL) {
		free(flds);
		return FAIL;
	}

	theIargs = (*env)->GetIntArrayElements(env,iargs,&bb);

	if (theIargs == NULL) {
		free(flds);
		free(name);
		return FAIL;
	}

	rval = VSinquire((int32) vdata_id, (int32 *)&(theIargs[0]), 
		(int32 *)&(theIargs[1]), flds, (int32 *)&(theIargs[2]), name);

	flds[MAX_FIELD_SIZE] = '\0';
	name[MAX_NC_NAME] = '\0';

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,iargs,theIargs,JNI_ABORT);
		free(flds);
		free(name);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,iargs,theIargs,0);

		jc = (*env)->FindClass(env, "java/lang/String");
		if (jc == NULL) {
			free(flds);
			free(name);
			return JNI_FALSE;
		}
		o = (*env)->GetObjectArrayElement(env,sargs,0);
		if (o == NULL) {
			free(flds);
			free(name);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,jc);
		if (bb == JNI_FALSE) {
			/*GB*/ free(flds);
			return JNI_FALSE;
		}
		rstring = (*env)->NewStringUTF(env,flds);
		(*env)->SetObjectArrayElement(env,sargs,0,(jobject)rstring);


		o = (*env)->GetObjectArrayElement(env,sargs,1);
		if (o == NULL) {
			free(flds);
			free(name);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,jc);
		if (bb == JNI_FALSE) {
			free(flds);
			free(name);
			return JNI_FALSE;
		}
		rstring = (*env)->NewStringUTF(env,name);
		(*env)->SetObjectArrayElement(env,sargs,1,(jobject)rstring);

		free(flds);
		free(name);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetblockinfo 
( JNIEnv *env,
jclass clss, 
jint vdata_id, 
jintArray iargs)  /* OUT: int blocksize, num_blocks */
{
	intn rval;
	jint * theIargs;
	jboolean bb;

	theIargs = (*env)->GetIntArrayElements(env,iargs,&bb);

	if (theIargs == NULL) {
		return FAIL;
	}

	rval = VSgetblockinfo((int32) vdata_id, (int32 *)&(theIargs[0]), 
		(int32 *)&(theIargs[1]));

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,iargs,theIargs,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,iargs,theIargs,0);
		return JNI_TRUE;
	}
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSlone 
(JNIEnv *env,
jclass clss, 
jint fid,
jintArray ref_array,  /* int[] */
jint arraysize    
)
{
	int retVal;
	jint * arr;
	jboolean bb;

	if (ref_array == NULL ) {
		arr = NULL;
	} else {
		arr = (*env)->GetIntArrayElements(env,ref_array,&bb);

		if (arr == NULL) {
			return FAIL;
		}
	}

	/* get the lone vdata reference number in the vdata */
	retVal = VSlone((int32)fid, (int32 *)arr, (int32)arraysize);

	if (retVal == FAIL) {
		if (arr != NULL) {
		(*env)->ReleaseIntArrayElements(env,ref_array,arr,JNI_ABORT);
		}
	} else {
		if (arr != NULL) {
		(*env)->ReleaseIntArrayElements(env,ref_array,arr,0);
		}
	}

	return retVal;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSread 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jbyteArray databuf,  /* OUT:  byte[] */
jint nrecords,
jint interlace)
{
	int32   retVal;
	jbyte  * data;
	jboolean bb;

	data = (*env)->GetByteArrayElements(env,databuf,&bb);

	/* retrieve the general info. */
	retVal = VSread((int32)vdata_id, (unsigned char *)data, nrecords, interlace);

	if (retVal == FAIL) {
		(*env)->ReleaseByteArrayElements(env,databuf,data,JNI_ABORT);
	} else {
		(*env)->ReleaseByteArrayElements(env,databuf,data,0);
	}
	return retVal;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSseek 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jint nrecord)
{
	return(VSseek((int32)vdata_id, (int32)nrecord));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetfields 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring fields)
{
	int32 retVal;
	char *fldPtr;

	fldPtr = (char *)(*env)->GetStringUTFChars(env,fields,0);

	/* set the vdata fields to read */
	retVal = VSsetfields((int32)vdata_id, (char *)fldPtr);

	(*env)->ReleaseStringUTFChars(env,fields,fldPtr);

	if (retVal != FAIL) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetinterlace 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jint interlace)
{
	int32 retVal;

	/* set the interlace for Vdata */
	retVal = VSsetinterlace((int32)vdata_id, (int32)interlace);

	if (retVal != FAIL) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsizeof 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring fields)
{
	int32 retVal;
	char *fldPtr;

	fldPtr = (char *)(*env)->GetStringUTFChars(env,fields,0);

	/* get the size of a Vdata */
	retVal = VSsizeof((int32)vdata_id, (char *)fldPtr);

	(*env)->ReleaseStringUTFChars(env,fields,fldPtr);

	return(retVal);
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSappendable 
( JNIEnv *env,
jclass clss, 
jint vkey,
jint block_size)
{
	int32 rval;
	rval = VSappendable((int32) vkey, (int32) block_size);
	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSdump 
( JNIEnv *env,
jclass clss, 
jint vkey)
{
	VSdump((int32) vkey);
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfindclass 
(JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring hdfclassname)
{
	int32 rval;
	char *string;


	string = (char *)(*env)->GetStringUTFChars(env,hdfclassname,0);

	/* get the class name of the vgroup */
	rval = VSfindclass((int32) vdata_id, string);

	(*env)->ReleaseStringUTFChars(env,hdfclassname,string);

	return rval;

}


/* no idea at all how to deal with 2-D arrays.... */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfpack 
( JNIEnv *env,
jclass clss, 
jint vdata_id,jint action,
jstring fields_in_buf,
jarray buf,
jint bufsize,
jstring fields,
jarray bufptrs)
{

	/*
		VSfpack((int32) vdata_id, (intn) action, char
			*fields_in_buf, VOIDP buf, intn buf_size, intn
			n_records, char *fields, VOIDP bufptrs[]);
	*/
	h4NotImplemented(env, "VSfpack");
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetversion 
( JNIEnv *env,
jclass clss, 
jint key)
{
	return (VSgetversion((int32) key));
}

JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetclass 
( JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring vdata_class)
{
	char *string = (char *)(*env)->GetStringUTFChars(env,vdata_class,0);

	VSsetclass((int32) vdata_id, (char *)string);

	(*env)->ReleaseStringUTFChars(env,vdata_class,string);

	return;
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary__1VSsetexternalfile 
( JNIEnv *env,
jclass clss,
jint vkey,
jstring filename, 
jint offset)
{
	intn rval;

	char *string = (char *)(*env)->GetStringUTFChars(env,filename,0);
	rval = VSsetexternalfile((int32) vkey, (char *)string, (int32) offset);
	(*env)->ReleaseStringUTFChars(env,filename,string);
	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT void JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetname 
( JNIEnv *env,
jclass clss, 
jint vdata_id,
jstring vdata_name)
{
	char *string = (char *)(*env)->GetStringUTFChars(env,vdata_name,0);

	VSsetname((int32) vdata_id, (char *)string);

	(*env)->ReleaseStringUTFChars(env,vdata_name,string);

	return;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSwrite 
( JNIEnv *env,
jclass clss, 
jint vdata_id,
jbyteArray databuf,
jint n_records,
jint interlace)
{
	int32 rval;
	jbyte *b;
	jboolean bb;

	b = (*env)->GetByteArrayElements(env,databuf,&bb);

	rval = VSwrite((int32) vdata_id, (unsigned char *)b, (int32) n_records, (int32) interlace);

	if (rval == FAIL) {
		(*env)->ReleaseByteArrayElements(env,databuf,b,JNI_ABORT);
	} else {
		(*env)->ReleaseByteArrayElements(env,databuf,b,0);
	}
	return rval;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSattrinfo 
( JNIEnv *env,
jclass clss, 
jint id,
jint index,
jint attr_index,
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

	retVal = VSattrinfo((int32)id, (int32)index, (int32)attr_index,
		nam, 
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

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfindex
( JNIEnv *env,
jclass clss, 
jint id,
jstring name, 
jintArray findex)  /* OUT: findex */
{
	intn rval;
	char *str;
	jint *arr;
	jboolean bb;

	str =(char *) (*env)->GetStringUTFChars(env,name,0);
	arr = (*env)->GetIntArrayElements(env,findex,&bb);

	rval = VSfindex((int32) id, str, (int32 *)arr);

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,findex,arr,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,findex,arr,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfindattr 
( JNIEnv *env,
jclass clss, 
jint id,
jint index,
jstring name)
{
        int32 retVal;
        char  *cname;

        cname =(char *) (*env)->GetStringUTFChars(env,name,0);

        retVal = VSfindattr((int32)id, (int32)index, cname);

        (*env)->ReleaseStringUTFChars(env,name,cname);

        return retVal;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSfnattrs 
( JNIEnv *env,
jclass clss, 
jint id,
jint attr)
{

	return (VSfnattrs((int32)id, (int32)attr));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSgetattr 
( JNIEnv *env,
jclass clss, 
jint id, 
jint field_index, 
jint attr_index,  
jbyteArray values)  /* OUT: byte[] */
{
	intn rval;
	jbyte *arr;
	jboolean bb;

	arr = (*env)->GetByteArrayElements(env,values,&bb);
	rval = VSgetattr((int32) id, (int32)field_index, 
		(int32) attr_index,  (VOIDP) arr);
	if (rval == FAIL) {
		(*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,values,arr,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSisattr 
( JNIEnv *env,
jclass clss, 
jint id)
{
	intn rval;

	rval = VSisattr((int32) id);
	if (rval == 0) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSnattrs 
( JNIEnv *env,
jclass clss, 
jint id)
{
	return (VSnattrs((int32) id));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetattr__IILjava_lang_String_2IILjava_lang_String_2 
( JNIEnv *env,
jclass clss, 
jint id, 
jint index, 
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

	rval = VSsetattr((int32) id, (int32) index, (char *)str, 
		(int32) data_type, (int32) count, (VOIDP) val);

	(*env)->ReleaseStringUTFChars(env,attr_name,str);
	(*env)->ReleaseStringUTFChars(env,values,val);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_VSsetattr__IILjava_lang_String_2II_3B 
( JNIEnv *env,
jclass clss, 
jint id, 
jint index, 
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

	rval = VSsetattr((int32) id, (int32) index, (char *)str, 
		(int32) data_type, (int32) count, (VOIDP) arr);

	(*env)->ReleaseStringUTFChars(env,attr_name,str);
	(*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}
