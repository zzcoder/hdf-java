
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
#include "mfhdf.h"

#include "jni.h"

extern jboolean h4outOfMemory( JNIEnv *env, char *functName);

extern jboolean makeChunkInfo( JNIEnv *env, jobject chunkobj, int32 flgs, HDF_CHUNK_DEF *cinf);
extern jboolean getNewCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf);
extern jboolean setNewCompInfo( JNIEnv *env, jobject ciobj, comp_coder_t coder, comp_info *cinf);
extern jboolean getChunkInfo( JNIEnv *env, jobject chunkobj, HDF_CHUNK_DEF *cinf);



JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary__1SDstart 
( JNIEnv *env,
jclass class,
jstring filename,
jint access)
{
	int32 sdid;
	char  *fname;

	fname =(char *) (*env)->GetStringUTFChars(env,filename,0);

	sdid = SDstart(fname, (int32)access);

	(*env)->ReleaseStringUTFChars(env,filename,fname);

	return sdid;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDend 
( JNIEnv *env,
jclass class, 
jint sdid)
{
	int32 retVal;

	retVal = SDend((int32)sdid);

	if (retVal == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDfileinfo 
( JNIEnv *env,
jclass class, 
jint sdid,
jintArray argv)  /* OUT: ndatasets, nglobalattr */
{
	int32 retVal;
	int32 * theArray;
	jboolean bb;

	theArray = (int32 *)(*env)->GetIntArrayElements(env,argv,&bb);

	retVal = SDfileinfo((int32)sdid, &(theArray[0]), &(theArray[1]));

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,argv,(jint *)theArray,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,argv,(jint *)theArray,0);
		return JNI_TRUE;
	}
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDselect 
( JNIEnv *env,
jclass class, 
jint sdid,
jint index)
{
	return (SDselect((int32)sdid, (int32)index));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDnametoindex 
( JNIEnv *env,
jclass class, 
jint sdid,
jstring name)
{
	int32 retVal;
	char  *cname;

	cname =(char *) (*env)->GetStringUTFChars(env,name,0);

	/* select the dataset */
	retVal = SDnametoindex((int32)sdid, cname);

	(*env)->ReleaseStringUTFChars(env,name,cname);

	return retVal;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetinfo 
( JNIEnv *env,
jclass class, 
jint sdsid,
jarray name,  /* OUT: String */
jintArray dimsizes, /* OUT: int[] */
jintArray argv)  /* OUT: rank, NT, nattr */
{
	int32 retVal;
	jclass jc;
	jstring str;
	jboolean bb;
	jobject o;
	char  *cname;

	/* variables of  infomation */
	int32 *dims;
	int32 *theArgs;

	cname = (char *)malloc(MAX_NC_NAME+1);
	if (cname == NULL) {
		h4outOfMemory(env, "SDgetinfo");
		return FAIL;
	}

	dims = (int32 *)(*env)->GetIntArrayElements(env,dimsizes,&bb);
	theArgs = (int32 *)(*env)->GetIntArrayElements(env,argv,&bb);

	retVal = SDgetinfo((int32)sdsid, cname, &(theArgs[0]), dims, 
			&(theArgs[1]), &(theArgs[2]));
	cname[MAX_NC_NAME] = '\0';

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,dimsizes,(jint *)dims,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,argv,(jint *)theArgs,JNI_ABORT);
		free(cname);
		return JNI_FALSE;
	} else {

		(*env)->ReleaseIntArrayElements(env,dimsizes,(jint *)dims,0);
		(*env)->ReleaseIntArrayElements(env,argv,(jint *)theArgs,0);

		str = (*env)->NewStringUTF(env,cname);
		o = (*env)->GetObjectArrayElement(env,name,0);
		if (o == NULL) {
			free(cname);
			return JNI_FALSE;
		}
		jc = (*env)->FindClass(env, "java/lang/String");
		if (jc == NULL) {
			free(cname);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,jc);
		if (bb == JNI_FALSE) {
			free(cname);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,name,0,(jobject)str);
		free(cname);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata 
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jbyteArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jbyte *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetByteArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseByteArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDendaccess
( JNIEnv *env,
jclass class, 
jint sdsid)
{
	int32 retVal;

	retVal = SDendaccess((int32)sdsid);

	if (retVal == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetdimid 
( JNIEnv *env,
jclass class, 
jint sdsid,
jint index)
{
	return (SDgetdimid((int32)sdsid, (int32)index));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDdiminfo 
( JNIEnv *env,
jclass class, 
jint dimid,
jarray dimname, /* OUT: String */
jintArray argv)  /* OUT: count, NT, nattrs */
{
	int32 retVal;
	jint *theArgs;
	jclass Sjc;
	jstring str;
	char  name[256];  /* what is the correct constant??? */
	jboolean bb;
	jobject o;


	theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

	retVal = SDdiminfo((int32)dimid, name, (int32 *)&(theArgs[0]), 
		(int32 *)&(theArgs[1]),(int32 *)&(theArgs[2]));

	name[255] = '\0';

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
		return JNI_FALSE;
	} else {

		(*env)->ReleaseIntArrayElements(env,argv,theArgs,0);

		str = (*env)->NewStringUTF(env,name);
		o = (*env)->GetObjectArrayElement(env,dimname,0);
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
		(*env)->SetObjectArrayElement(env,dimname,0,(jobject)str);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDidtoref 
( JNIEnv *env,
jclass class, 
jint sdsid)
{
	return(SDidtoref((int32)sdsid));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreftoindex 
( JNIEnv *env,
jclass class, 
jint sdid,
jint ref)
{
	return(SDreftoindex((int32)sdid, (int32)ref));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDattrinfo 
( JNIEnv *env,
jclass class, 
jint id,
jint index,
jarray name,  /* OUT:  String */
jintArray argv)  /* OUT:  NT, count */
{
	int32 retVal;
	jint *theArgs;
	jboolean bb;
	jclass Sjc;
	jstring str;
	jobject o;
	char  nam[256];  /* what is the correct constant??? */

	theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

	retVal = SDattrinfo((int32)id, (int32)index, nam, 
		(int32 *)&(theArgs[0]), (int32 *)&(theArgs[1]));

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

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreadattr 
( JNIEnv *env,
jclass class, 
jint id,
jint index,
jbyteArray dat)
{
	int32 retVal;
	jbyte * s;
	jboolean bb;

	s = (*env)->GetByteArrayElements(env,dat,&bb);

	retVal = SDreadattr((int32)id,(int32)index,s);

	if (retVal == FAIL) {
		(*env)->ReleaseByteArrayElements(env,dat,s,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,dat,s,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDfindattr 
( JNIEnv *env,
jclass class, 
jint id,
jstring name)
{
	int32 retVal;
	char  *cname;

	cname =(char *) (*env)->GetStringUTFChars(env,name,0);

	retVal = SDfindattr((int32)id, cname);

	(*env)->ReleaseStringUTFChars(env,name,cname);

	return retVal;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDiscoordvar 
( JNIEnv *env,
jclass class, 
jint sdsid)
{
	int32 retVal;

	retVal = SDiscoordvar((int32)sdsid);

	if (retVal == FALSE) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetcal 
( JNIEnv *env,
jclass class, 
jint sdsid,
jdoubleArray argv, /* OUT:  Cal, Cal_err, Offset, Offset_err */
jintArray nt)  /* OUT:  NT */
{
	int32 retVal;
	jdouble *theArgs;
	jint *theNT;
	jboolean bb;

	theArgs = (*env)->GetDoubleArrayElements(env,argv,&bb);
	theNT = (*env)->GetIntArrayElements(env,nt,&bb);

	retVal = SDgetcal((int32)sdsid, (float64 *)&(theArgs[0]),
		(float64 *)&(theArgs[1]), (float64 *)&(theArgs[2]), 
		(float64 *)&(theArgs[3]), (int32 *)&(theNT[0]));

	if (retVal==FAIL) {
		(*env)->ReleaseDoubleArrayElements(env,argv,theArgs,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,nt,theNT,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseDoubleArrayElements(env,argv,theArgs,0);
		(*env)->ReleaseIntArrayElements(env,nt,theNT,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetdatastrs 
( JNIEnv *env,
jclass class, 
jint sdsid,
jobjectArray strings, /* OUT: label, unit, format, coordsys */
jint len)  /* IN */
{
	int32 retVal;
	jclass Sjc;
	jstring str;
	jobject o;
	jboolean bb;
	jstring label,unit,format,coordsys;
	char *labVal;
	char *unitVal;
	char *fmtVal;
	char *coordsysVal;
	int i;

	Sjc = (*env)->FindClass(env, "java/lang/String");
	if (Sjc == NULL) {
		return JNI_FALSE;
	}
	for (i = 0; i < 4; i++) {
		o = (*env)->GetObjectArrayElement(env,strings,i);
		if (o == NULL) {
			continue;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			/* exception */
			return JNI_FALSE;
		}
	}

	label = (jstring)(*env)->GetObjectArrayElement(env,strings,0);
	/* allocate space */
	if (label == NULL) {
		labVal = NULL; /* don't read label */
	} else {
		labVal =  (char *)HDmalloc(len+1);
		if (labVal == NULL) {
			h4outOfMemory(env, "SDgetdatastrs");
			return JNI_FALSE;
		}
	}
	unit = (jstring)(*env)->GetObjectArrayElement(env,strings,1);
	if (unit == NULL) {
		unitVal = NULL;
	} else {
		unitVal =  (char *)HDmalloc(len+1);
		if (unitVal == NULL) {
			if (labVal != NULL) HDfree(labVal);
			h4outOfMemory(env, "SDgetdatastrs");
			return JNI_FALSE;
		}
	}

	format = (jstring)(*env)->GetObjectArrayElement(env,strings,2);
	if (format == NULL) {
		fmtVal = NULL;
	} else {
		fmtVal =  (char *)HDmalloc(len+1);
		if (fmtVal == NULL) {
			if (labVal != NULL) HDfree(labVal);
			if (unitVal != NULL) HDfree(unitVal);
			h4outOfMemory(env, "SDgetdatastrs");
			return JNI_FALSE;
		}
	}
	coordsys = (jstring)(*env)->GetObjectArrayElement(env,strings,3);
	if (coordsys == NULL) {
		coordsysVal = NULL;
	} else {
		coordsysVal =  (char *)HDmalloc(len+1);
		if ( coordsysVal == NULL) {
			if (labVal != NULL) HDfree(labVal);
			if (unitVal != NULL) HDfree(unitVal);
			if (fmtVal != NULL) HDfree(fmtVal);
			h4outOfMemory(env, "SDgetdatastrs");
			return JNI_FALSE;
		}
	}

	retVal = SDgetdatastrs((int32)sdsid, labVal, unitVal, fmtVal, coordsysVal,(int32)len);

	if (retVal==FAIL) {
		if (labVal != NULL) HDfree(labVal);
		if (unitVal != NULL) HDfree(unitVal);
		if (fmtVal != NULL) HDfree(fmtVal);
		if (coordsysVal != NULL) HDfree(coordsysVal);
		return JNI_FALSE;
	} else {
		Sjc = (*env)->FindClass(env, "java/lang/String");
		if (Sjc == NULL) {
			if (labVal != NULL) HDfree(labVal);
			if (unitVal != NULL) HDfree(unitVal);
			if (fmtVal != NULL) HDfree(fmtVal);
			if (coordsysVal != NULL) HDfree(coordsysVal);
			h4outOfMemory(env, "SDgetdatastrs");
			return JNI_FALSE;
		}
		if (labVal != NULL) {
			labVal[len] = '\0';
			str = (*env)->NewStringUTF(env,labVal);
			if (str != NULL) {
				(*env)->SetObjectArrayElement(env,strings,0,(jobject)str);
			}
			HDfree(labVal);
		}
		if (unitVal != NULL) {
			unitVal[len] = '\0';
			str = (*env)->NewStringUTF(env,unitVal);
			if (str != NULL) {
			(*env)->SetObjectArrayElement(env,strings,1,(jobject)str);
			}
			HDfree(unitVal);
		}
		if (fmtVal != NULL) {
			fmtVal[len] = '\0';
			str = (*env)->NewStringUTF(env,fmtVal);
			if (str != NULL) {
			(*env)->SetObjectArrayElement(env,strings,2,(jobject)str);
			}
			HDfree(fmtVal);
		}
		if (coordsysVal != NULL) {
			coordsysVal[len] = '\0';
			str = (*env)->NewStringUTF(env,coordsysVal);
			if (str != NULL) {
			(*env)->SetObjectArrayElement(env,strings,3,(jobject)str);
			}
			HDfree(coordsysVal);
		}

		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetdimstrs 
( JNIEnv *env,
jclass class, 
jint dimid,
jobjectArray argv, /* OUT: String label, unit, format, */
jint len)
{
	int32 retVal;
	char *labVal, *unitVal, *fmtVal;
	jclass Sjc;
	jstring str;
	jobject o;
	jboolean bb;

	o = (*env)->GetObjectArrayElement(env,argv,0);
	if (o == NULL) {
		labVal = NULL; /* don't read label */
	} else {
		labVal =  (char *)HDmalloc(len+1);
		if (labVal == NULL) {
			h4outOfMemory(env, "SDgetdimstrs");
			return JNI_FALSE;
		}
	}
	o = (*env)->GetObjectArrayElement(env,argv,1);
	if (o == NULL) {
		unitVal = NULL;
	} else {
		unitVal =  (char *)HDmalloc(len+1);
		if (unitVal == NULL) {
			if (labVal != NULL) HDfree(labVal);
			h4outOfMemory(env, "SDgetdimstrs");
			return JNI_FALSE;
		}
	}

	o = (*env)->GetObjectArrayElement(env,argv,2);
	if (o == NULL) {
		fmtVal = NULL;
	} else {
		fmtVal =  (char *)HDmalloc(len+1);
		if (fmtVal == NULL) {
			if (labVal != NULL) HDfree(labVal);
			if (unitVal != NULL) HDfree(unitVal);
			h4outOfMemory(env, "SDgetdimstrs");
			return JNI_FALSE;
		}
	}

	retVal = SDgetdimstrs((int32)dimid, labVal, unitVal, fmtVal, (int32)len);

	if (retVal==FAIL) {
		if (labVal != NULL) HDfree(labVal);
		if (unitVal != NULL) HDfree(unitVal);
		if (fmtVal != NULL) HDfree(fmtVal);
		return JNI_FALSE;
	} else {
		Sjc = (*env)->FindClass(env, "java/lang/String");
		if (Sjc == NULL) {
			if (labVal != NULL) HDfree(labVal);
			if (unitVal != NULL) HDfree(unitVal);
			if (fmtVal != NULL) HDfree(fmtVal);
			return JNI_FALSE;
		}

		if (labVal != NULL) {
			o = (*env)->GetObjectArrayElement(env,argv,0);
			if (o == NULL) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			bb = (*env)->IsInstanceOf(env,o,Sjc);
			if (bb == JNI_FALSE) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			labVal[len] = '\0';
			str = (*env)->NewStringUTF(env,labVal);
			(*env)->SetObjectArrayElement(env,argv,0,(jobject)str);
		}
		if (unitVal != NULL) {
			o = (*env)->GetObjectArrayElement(env,argv,1);
			if (o == NULL) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			bb = (*env)->IsInstanceOf(env,o,Sjc);
			if (bb == JNI_FALSE) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			unitVal[len] = '\0';
			str = (*env)->NewStringUTF(env,unitVal);
			(*env)->SetObjectArrayElement(env,argv,1,(jobject)str);
		}
		if (fmtVal != NULL) {
			o = (*env)->GetObjectArrayElement(env,argv,2);
			if (o == NULL) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			bb = (*env)->IsInstanceOf(env,o,Sjc);
			if (bb == JNI_FALSE) {
				if (labVal != NULL) HDfree(labVal);
				if (unitVal != NULL) HDfree(unitVal);
				if (fmtVal != NULL) HDfree(fmtVal);
				return JNI_FALSE;
			}
			fmtVal[len] = '\0';
			str = (*env)->NewStringUTF(env,fmtVal);
			(*env)->SetObjectArrayElement(env,argv,2,(jobject)str);
		}
		if (labVal != NULL) HDfree(labVal); 
		if (unitVal != NULL) HDfree(unitVal);
		if (fmtVal != NULL) HDfree(fmtVal);
		return JNI_TRUE;
	}
}

/*** note this is returning data of many types in an array of bytes.... */
/* not sure how well this will work for java programs .... */

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetdimscale 
( JNIEnv *env,
jclass class, 
jint dimid,
jbyteArray data) /* OUT: byte? */
{
	int32 retVal;
	jbyte *datVal;
	jboolean bb;

	datVal = (*env)->GetByteArrayElements(env,data,&bb);

	retVal = SDgetdimscale((int32)dimid, (char *)datVal);
	if (retVal==FAIL) {
		(*env)->ReleaseByteArrayElements(env,data,datVal,JNI_ABORT);
		return JNI_FALSE;
	}
	else {
		(*env)->ReleaseByteArrayElements(env,data,datVal,0);
		return JNI_TRUE;
	}

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetfillvalue 
( JNIEnv *env,
jclass class, 
jint sdsid,
jbyteArray data)  /* OUT: byte? */
{
	int32 retVal;
	jbyte *datVal;
	jboolean bb;

	datVal = (*env)->GetByteArrayElements(env,data,&bb);

	retVal = SDgetfillvalue((int32)sdsid, (char *)datVal);
	if (retVal==FAIL) {
		(*env)->ReleaseByteArrayElements(env,data,datVal,JNI_ABORT);
		return JNI_FALSE;
	}
	else {
		(*env)->ReleaseByteArrayElements(env,data,datVal,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetrange 
( JNIEnv *env,
jclass class, 
jint sdsid,
jbyteArray max,  /* OUT:  byte[]? */
jbyteArray min)  /* OUT:  byte[] ? */
{
	int32 retVal;
	jbyte *minp, *maxp;
	jboolean bb;

	maxp = (*env)->GetByteArrayElements(env,max,&bb);
	minp = (*env)->GetByteArrayElements(env,min,&bb);

	retVal = SDgetrange((int32)sdsid, maxp, minp);
	if (retVal==FAIL) {
		(*env)->ReleaseByteArrayElements(env,max,maxp,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,min,minp,JNI_ABORT);
		return JNI_FALSE;
	}
	else {
		(*env)->ReleaseByteArrayElements(env,max,maxp,0);
		(*env)->ReleaseByteArrayElements(env,min,minp,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDcreate 
( JNIEnv *env,
jclass class, 
jint sd_id,
jstring name,
jint number_type, 
jint rank, 
jintArray dimsizes) /* IN: int[] */
{
	int32 rval;
	char * s;
	jint * dims;
	jboolean bb;

	s =(char *) (*env)->GetStringUTFChars(env,name,0);
	dims = (*env)->GetIntArrayElements(env,dimsizes,&bb);


	rval = SDcreate((int32) sd_id, (char *)s, (int32) number_type, (int32) rank, (int32 *)dims);

	(*env)->ReleaseStringUTFChars(env,name,s);
	if (rval==FAIL) {
		(*env)->ReleaseIntArrayElements(env,dimsizes,dims,JNI_ABORT); /* no write back */
	} else {
		(*env)->ReleaseIntArrayElements(env,dimsizes,dims,0); /* do write back */
	}
	return rval;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDisrecord 
( JNIEnv *env,
jclass class, 
jint sdsid)
{
	int32 rval;

	rval = SDisrecord((int32)sdsid);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetattr 
(JNIEnv *env,
jclass class, 
jint s_id,
jstring attr_name, 
jint num_type,
jint count, 
jbyteArray values) /* IN:  byte[]??? */
{
	intn rval;
	jboolean bb;

	char * s;
	jbyte * v;

	s =(char *) (*env)->GetStringUTFChars(env,attr_name,0);
	v = (*env)->GetByteArrayElements(env,values,&bb);

	rval = SDsetattr((int32) s_id, (char *)s, (int32) num_type,
	    (int32) count, (VOIDP) v);

	(*env)->ReleaseStringUTFChars(env,attr_name,s);
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,values,v,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,values,v,0); /* do write back */
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetcal 
( JNIEnv *env,
jclass class, 
jint sds_id,
jdouble cal, 
jdouble cal_err,
jdouble offset, 
jdouble offset_err, 
jint number_type)
{
	intn rval;

	rval = SDsetcal((int32) sds_id, (float64) cal, (float64) cal_err,
	    (float64) offset, (float64) offset_err, (int32) number_type) ;

	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetdatastrs 
( JNIEnv *env,
jclass class, 
jint sds_id,
jstring label, 
jstring unit, 
jstring format, 
jstring coordsys)
{
	intn rval;
	char * labstr;
	char * unstr;
	char * formstr;
	char * csstr;

	if (label == NULL) { 
		labstr = NULL; 
	} else {
		labstr =(char *) (*env)->GetStringUTFChars(env,label,0);
	}
	if (unit == NULL) { 
		unstr = NULL; 
	} else {
		unstr =(char *) (*env)->GetStringUTFChars(env,unit,0);
	}
	if (format == NULL) { 
		formstr = NULL; 
	} else {
		formstr =(char *) (*env)->GetStringUTFChars(env,format,0);
	}
	if (coordsys == NULL) { 
		csstr = NULL; 
	} else {
		csstr =(char *) (*env)->GetStringUTFChars(env,coordsys,0);
	}

	rval = SDsetdatastrs((int32) sds_id, labstr, unstr, formstr, csstr);

	if (labstr != NULL) (*env)->ReleaseStringUTFChars(env,label,labstr);
	if (unstr != NULL) (*env)->ReleaseStringUTFChars(env,unit,unstr);
	if (formstr != NULL) (*env)->ReleaseStringUTFChars(env,format,formstr);
	if (csstr != NULL) (*env)->ReleaseStringUTFChars(env,coordsys,csstr);
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetdimname 
( JNIEnv *env,
jclass class, 
jint dim_id,
jstring dim_name)
{
	intn rval;
	char * str;

	str =(char *) (*env)->GetStringUTFChars(env,dim_name,0);
	rval = SDsetdimname((int32) dim_id, str) ;
	(*env)->ReleaseStringUTFChars(env,dim_name,str);
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetdimscale 
( JNIEnv *env,
jclass class, 
jint dim_id,
jint count, 
jint number_type,
jbyteArray data) /*  IN:  byte[]???*/
{
	intn rval;
	jbyte *d;
	jboolean bb;

	d = (*env)->GetByteArrayElements(env,data,&bb);

	rval = SDsetdimscale((int32) dim_id, (int32) count, (int32) number_type, d) ;
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,data,d,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,data,d,0); /* do write back */
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetdimstrs 
( JNIEnv *env,
jclass class, 
jint dim_id,
jstring label, 
jstring unit, 
jstring format)
{
	intn rval;
	char * l;
	char * u;
	char * f;


	l =(char *) (*env)->GetStringUTFChars(env,label,0);
	u =(char *) (*env)->GetStringUTFChars(env,unit,0);
	f =(char *) (*env)->GetStringUTFChars(env,format,0);
	rval = SDsetdimstrs((int32) dim_id, l, u, f);
	(*env)->ReleaseStringUTFChars(env,label,l);
	(*env)->ReleaseStringUTFChars(env,unit,u);
	(*env)->ReleaseStringUTFChars(env,format,f);
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary__1SDsetexternalfile 
( JNIEnv *env,
jclass class,
jint sds_id,
jstring filename, 
jint offset)
{
	intn rval;
	char * f;

	f =(char *) (*env)->GetStringUTFChars(env,filename,0);

	rval =  SDsetexternalfile((int32) sds_id, f, (int32) offset);
	(*env)->ReleaseStringUTFChars(env,filename,f);
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetfillvalue 
( JNIEnv *env,
jclass class, 
jint sds_id,
jbyteArray fill_val)  /* IN:  ?? byte[] */
{
	intn rval;
	jboolean bb;
	jbyte * d;

	d = (*env)->GetByteArrayElements(env,fill_val,&bb);
	rval = SDsetfillvalue((int32) sds_id, (VOIDP) d) ;
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,fill_val,d,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,fill_val,d,0); /* do write back */
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetrange 
( JNIEnv *env,
jclass class, 
jint sdsid,
jbyteArray max,  /* IN:  byte[]? */
jbyteArray min)  /* IN:  byte[] ? */
{
	int32 retVal;
	jboolean bb;
	jbyte *minp, *maxp;

	maxp = (*env)->GetByteArrayElements(env,max,&bb);
	minp = (*env)->GetByteArrayElements(env,min,&bb);

	retVal = SDsetrange((int32)sdsid, maxp, minp);
	if (retVal==FAIL) {
		(*env)->ReleaseByteArrayElements(env,max,maxp,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,min,minp,JNI_ABORT);
		return JNI_FALSE;
	}
	else {
		(*env)->ReleaseByteArrayElements(env,max,maxp,0);
		(*env)->ReleaseByteArrayElements(env,min,minp,0);
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDwritedata 
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray edge,
jbyteArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *e;
	jbyte *d;
	jboolean bb;


	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL ) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	e = (int32 *)(*env)->GetIntArrayElements(env,edge,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetByteArrayElements(env,data,&bb);

	retVal = SDwritedata((int32)sdsid, strt, strd, e, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL ) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,edge,(jint *)e,JNI_ABORT);
		(*env)->ReleaseByteArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL ) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,edge,(jint *)e,0);
		(*env)->ReleaseByteArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

/* new stuff for chunking */

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetnbitdataset 
( JNIEnv *env,
jclass class, 
jint id, 
jint start_bit, 
jint bit_len,
jint sign_ext, 
jint fill_one)
{
intn rval;

	rval = SDsetnbitdataset((int32)id, (intn) start_bit, (intn) bit_len,
        	(intn) sign_ext, (intn) fill_one);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetcompress 
( JNIEnv *env,
jclass class, 
jint id,
jint type,
jobject cinfo) /* IN: CompInfo */
{
        intn rval;
        comp_info cinf;
        jboolean bval;

/*
	bzero((char *)&cinf, sizeof(cinf));
*/

        bval = getNewCompInfo(env,cinfo,&cinf); /* or is it New ? */

        /* check for success... */

        rval = SDsetcompress((int32) id, (comp_coder_t) type, (comp_info *)&cinf);

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetcompress 
( JNIEnv *env,
jclass class, 
jint id,
jobject cinfo) /* out: CompInfo */
{
        intn rval;
	comp_coder_t coder;
        comp_info cinf;
        jboolean bval;

        /* check for success... */

        rval = SDgetcompress((int32) id, (comp_coder_t *) &coder, 
		(comp_info *)&cinf);


        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
		bval = setNewCompInfo(env,cinfo, coder, &cinf);
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetaccesstype 
( JNIEnv *env,
jclass class, 
jint id, 
jint accesstype )
{
intn rval;

	rval = SDsetaccesstype( (int32) id, (uintn) accesstype );

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetblocksize 
( JNIEnv *env,
jclass class, 
jint sdsid, 
jint block_size)
{
intn rval;

	rval = SDsetblocksize( (int32) sdsid, (int32) block_size );

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetfillmode 
( JNIEnv *env,
jclass class, 
jint sdsid, 
jint fillmode )
{
intn rval;

	rval = SDsetfillmode( (int32) sdsid, (intn) fillmode );

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetdimval_comp 
( JNIEnv *env,
jclass class, 
jint sdsid, 
jint comp_mode )
{
intn rval;

	rval = SDsetdimval_comp( (int32) sdsid, (intn) comp_mode );

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDisdimval_bwcomp 
( JNIEnv *env,
jclass class, 
jint dimid )
{
intn rval;

	rval = SDisdimval_bwcomp( (int32) dimid );
	if (rval == SD_DIMVAL_BW_COMP) {
		return JNI_TRUE;
	} else if (rval == SD_DIMVAL_BW_INCOMP) {
		return JNI_FALSE;
	} else {
		/* exception? */
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetchunk 
( JNIEnv *env,
jclass class, 
jint sdsid,
jobject chunk_def, /* IN: HDFChunkInfo */
jint flags)              
{
intn rval;
HDF_CHUNK_DEF c_def;
jboolean bval;

/*
	bzero((char *)&c_def, sizeof(c_def));
*/

	bval = getChunkInfo(env,chunk_def,&c_def);

	/* check results */
	
	rval = SDsetchunk ((int32) sdsid, c_def, (int32) flags);

        if (rval == FAIL) {
                return JNI_FALSE;
        } else {
                return JNI_TRUE;
        }
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDgetchunkinfo 
( JNIEnv *env,
jclass class, 
jint sdsid,
jobject chunk_def, /* Out: HDFChunkInfo */
jintArray cflags)              /* OUT: Integer */
{
int32 rval;
HDF_CHUNK_DEF cdef;
jboolean stat;
jint *flgs;
jboolean bb;

/*
	bzero((char *)&cdef, sizeof(cdef));
*/
	flgs = (*env)->GetIntArrayElements(env,cflags,&bb);
	rval = SDgetchunkinfo( (int32)sdsid, &cdef, (int32 *)&(flgs[0]));

	/* convert cdef to HDFchunkinfo */
	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,cflags,(jint *)flgs,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,cflags,(jint *)flgs,0);
		stat = makeChunkInfo( env, chunk_def, (int32)flgs, &cdef);

		return stat/*JNI_TRUE*/;
	}

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreadchunk 
( JNIEnv *env,
jclass class, 
jint sdid,
jintArray origin, /* IN: int[] */
jbyteArray dat)  /* OUT: byte[] */
{
int32 retVal;
jbyte * s;
jint *arr;
	jboolean bb;

	arr = (*env)->GetIntArrayElements(env,origin,&bb);
	s = (*env)->GetByteArrayElements(env,dat,&bb);

	retVal = SDreadchunk((int32)sdid,(int32 *)arr,s);

	(*env)->ReleaseIntArrayElements(env,origin,arr,JNI_ABORT);
	if (retVal == FAIL) {
		(*env)->ReleaseByteArrayElements(env,dat,s,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,dat,s,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDsetchunkcache 
( JNIEnv *env,
jclass class, 
jint sdsid,
jint maxcache,
jint flags)              
{
	return ( SDsetchunkcache((int32)sdsid, (int32)maxcache, (int32)flags) );
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDwritechunk 
( JNIEnv *env,
jclass class, 
jint sdid,
jintArray index, /* IN: int[] */
jbyteArray dat)  /* IN: byte[] */
{
int32 retVal;
jbyte * s;
jint * arr;
	jboolean bb;

	s = (*env)->GetByteArrayElements(env,dat,&bb);
	arr = (*env)->GetIntArrayElements(env,index,&bb);

	retVal = SDwritechunk((int32)sdid,(int32 *)arr,(char *)s);

	(*env)->ReleaseByteArrayElements(env,dat,s,JNI_ABORT);
	(*env)->ReleaseIntArrayElements(env,index,arr,JNI_ABORT);

	if (retVal == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDcheckempty 
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray emptySDS)  /* OUT: 1 if sds is empty */
{
	int32 retVal;
	jboolean bb;

	/* variables of  infomation */
	intn *isempty;

	isempty = (intn *)(*env)->GetIntArrayElements(env,emptySDS,&bb);

	retVal = SDcheckempty((int32)sdsid, (intn *)isempty );

	(*env)->ReleaseIntArrayElements(env,emptySDS,(jint *)isempty,0);
	if (retVal == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


/*
    ////////////////////////////////////////////////////////////////////
    //                                                                //
    //         New APIs for read data from library                    //
    //  Using SDreaddata(..., Object buf) requires function calls        //
    //  theArray.emptyBytes() and theArray.arrayify( buf), which      //
    //  triples the actual memory needed by the data set.             //
    //  Using the following APIs solves the problem.                  //
    //                                                                //
    ////////////////////////////////////////////////////////////////////
*/

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata_1short
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jshortArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jshort *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetShortArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseShortArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseShortArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata_1int
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jintArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jint *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetIntArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseIntArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata_1long
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jlongArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jlong *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetLongArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseLongArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseLongArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata_1float
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jfloatArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jfloat *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetFloatArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseFloatArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseFloatArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_SDreaddata_1double
( JNIEnv *env,
jclass class, 
jint sdsid,
jintArray start, 
jintArray stride,
jintArray count,
jdoubleArray data)
{
	int32 retVal;
	int32 *strt;
	int32 *strd;
	int32 *cnt;
	jdouble *d;
	jboolean bb;

	strt = (int32 *)(*env)->GetIntArrayElements(env,start,&bb);
	if (stride != NULL) {
		strd = (int32 *)(*env)->GetIntArrayElements(env,stride,&bb);
	} else {
		strd = (int32 *)NULL;
	}
	cnt = (int32 *)(*env)->GetIntArrayElements(env,count,&bb);

	/* assume that 'data' is big enough */
	d = (*env)->GetDoubleArrayElements(env,data,&bb);

	retVal = SDreaddata((int32)sdsid, strt, strd, cnt, d);

	if (retVal == FAIL) {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,JNI_ABORT);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,JNI_ABORT);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,JNI_ABORT);
		(*env)->ReleaseDoubleArrayElements(env,data,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,start,(jint *)strt,0);
		if (stride != NULL) {
			(*env)->ReleaseIntArrayElements(env,stride,(jint *)strd,0);
		}
		(*env)->ReleaseIntArrayElements(env,count,(jint *)cnt,0);
		(*env)->ReleaseDoubleArrayElements(env,data,d,0);
		return JNI_TRUE;
	}
}
