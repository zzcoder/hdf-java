
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
#include "hfile.h"

#include "jni.h"

extern jboolean h4outOfMemory(JNIEnv *env, char *functName);

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDadddata 
( JNIEnv *env,
jclass class, 
jstring filename, 
jint rank, 
jintArray dimsizes, /* IN: int[] */
jbyteArray data)  /* IN: byte[] */
{
	intn rval;
	jchar * name;
	jbyte * dat;
	jint * dims;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	dims = (jint *)(*env)->GetIntArrayElements(env,dimsizes,&bb);
	dat = (jbyte *)(*env)->GetByteArrayElements(env,data,&bb);

	rval = DFSDadddata((char *)name, (intn) rank, (int32 *) dims, (VOIDP) dat);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	(*env)->ReleaseByteArrayElements(env,data,dat,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,dimsizes,dims,JNI_ABORT); /* no write back */
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDclear 
( JNIEnv *env,
jobject obj) 
{
	if (DFSDclear( ) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDendslab 
( JNIEnv *env,
jobject obj) 
{
	if (DFSDendslab( ) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDendslice 
( JNIEnv *env,
jobject obj) 
{
	if (DFSDendslice( ) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetcal 
( JNIEnv *env,
jclass class, 
jdoubleArray calInfo, /* OUT: double cal, cal_err, offset, offset_err */
jintArray data_type)  /* OUT: */
{
	int32 rval;
	jdouble *theCal;
	jint *theNT;
	jboolean bb;

	theCal = (jdouble *)(*env)->GetDoubleArrayElements(env,calInfo,&bb);
	theNT = (jint *)(*env)->GetIntArrayElements(env,data_type,&bb);

	rval = DFSDgetcal((float64 *)&(theCal[0]), (float64 *)&(theCal[1]), 
		(float64 *)&(theCal[2]), (float64 *)&(theCal[3]), 
		(int32 *)&(theNT[0]));

	if (rval == FAIL) {
		(*env)->ReleaseDoubleArrayElements(env,calInfo,theCal,JNI_ABORT); 
		(*env)->ReleaseIntArrayElements(env,data_type,theNT,JNI_ABORT); 
		return JNI_FALSE;
	} else {
		(*env)->ReleaseDoubleArrayElements(env,calInfo,theCal,0); 
		(*env)->ReleaseIntArrayElements(env,data_type,theNT,0); 
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdata 
( JNIEnv *env,
jclass class, 
jstring filename, 
jint rank, 
jintArray dimsizes, /* IN: int[] */
jbyteArray data)    /* OUT: byte[] */
{
	intn rval;
	jchar * name;
	jbyte * dat;
	jint * dims;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	dims = (jint *)(*env)->GetIntArrayElements(env,dimsizes,&bb);

	/* assume that data is big enough */
	dat = (jbyte *)(*env)->GetByteArrayElements(env,data,&bb);

	rval = DFSDgetdata((char *)name, (intn) rank, (int32 *) dims, (VOIDP) dat);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	(*env)->ReleaseIntArrayElements(env,dimsizes,dims,JNI_ABORT); /* no write back */
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,data,dat,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,data,dat,0); /* write back */
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdatalen 
( JNIEnv *env,
jclass class, 
jintArray info) /* label_len, unit_len, format_len coords_len */
{
	intn rval;
	jint *theInfo;
	jboolean bb;

	theInfo = (jint *)(*env)->GetIntArrayElements(env,info,&bb);

	rval = DFSDgetdatalen((intn *)&(theInfo[0]), (intn *)&(theInfo[1]), 
		(intn *)&(theInfo[2]), (intn *)&(theInfo[3]));

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,info,theInfo,JNI_ABORT); 
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,info,theInfo,0); 
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdatastrs 
( JNIEnv *env,
jclass class, 
jobjectArray datastrs) /* OUT: label, unit, format, coordsys */
{
	intn rval;
	int ll;
	int ul;
	int fl;
	int cl;
	char * l;
	char * u;
	char * f;
	char * c;
	jclass Sjc;
	jstring rstring;
	jobject o;
	jboolean bb;


	rval = DFSDgetdatalen((intn *)&ll, (intn *)&(ul), (intn *)&fl, (intn *)&cl);
	if (rval == FAIL) {
		return JNI_FALSE;
	}

	l = (char *)HDmalloc(ll+1);
	if (l == NULL) {
		h4outOfMemory(env,"DFSDgetdatastrs");
		return JNI_FALSE;
        }
	u = (char *)HDmalloc(ul+1);
	if (u == NULL) {
		HDfree(l);
		h4outOfMemory(env,"DFSDgetdatastrs");
		return JNI_FALSE;
        }
	f = (char *)HDmalloc(fl+1);
	if (f == NULL) {
		HDfree(u);
		HDfree(l);
		h4outOfMemory(env,"DFSDgetdatastrs");
		return JNI_FALSE;
        }
	c = (char *)HDmalloc(cl+1);
	if (c == NULL) {
		HDfree(u);
		HDfree(f);
		HDfree(l);
		h4outOfMemory(env,"DFSDgetdatastrs");
		return JNI_FALSE;
        }

	rval = DFSDgetdatastrs((char *)l, (char *)u, (char *)f, (char *)c);
	l[ll] = '\0';
	u[ul] = '\0';
	f[fl] = '\0';
	c[cl] = '\0';

	if (rval == FAIL) {
		if (l != NULL)
			HDfree((char *)l);
		if (u != NULL)
			HDfree((char *)u);
		if (f != NULL)
			HDfree((char *)f);
		if (c != NULL)
			HDfree((char *)c);
		return JNI_FALSE;
	} else {

		Sjc = (*env)->FindClass(env, "java/lang/String");
		if (Sjc == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE; /* exception is raised */
		}

		rstring = (*env)->NewStringUTF(env, l);

		o = (*env)->GetObjectArrayElement(env,datastrs,0);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,datastrs,0,(jobject)rstring);

		rstring = (*env)->NewStringUTF(env, u);

		o = (*env)->GetObjectArrayElement(env,datastrs,1);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,datastrs,1,(jobject)rstring);

		rstring = (*env)->NewStringUTF(env, f);

		o = (*env)->GetObjectArrayElement(env,datastrs,2);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,datastrs,2,(jobject)rstring);

		rstring = (*env)->NewStringUTF(env, c);

		o = (*env)->GetObjectArrayElement(env,datastrs,3);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			if (c != NULL)
				HDfree((char *)c);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,datastrs,3,(jobject)rstring);

		if (l != NULL)
			HDfree((char *)l);
		if (u != NULL)
			HDfree((char *)u);
		if (f != NULL)
			HDfree((char *)f);
		if (c != NULL)
			HDfree((char *)c);

		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdimlen 
( JNIEnv *env,
jclass class, 
jint dim, 
jintArray dimInfo) /* OUT: int label_len, unit_len, format_len */
{
	intn rval;
	jint *theArgs;
	jboolean bb;

	theArgs = (jint *)(*env)->GetIntArrayElements(env,dimInfo,&bb);

	rval = DFSDgetdimlen((intn) dim, (intn *)&(theArgs[0]), 
		(intn *)&(theArgs[1]), (intn *)&(theArgs[2]));

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,dimInfo,theArgs,JNI_ABORT); 
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,dimInfo,theArgs,0); 
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdims 
( JNIEnv *env,
jclass class, 
jstring filename, 
jintArray rank,   /* OUT: int */
jintArray dimsizes, /* OUT: int[]  should be at least 'maxrank' long */
jint maxrank)
{
	intn rval;
	jchar * name;
	jint * dims;
	jint * rnk;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	dims = (jint *)(*env)->GetIntArrayElements(env,dimsizes,&bb);
	rnk = (jint *)(*env)->GetIntArrayElements(env,rank,&bb);

	/* should check lenght of dims.... */

	rval = DFSDgetdims((char *)name, (intn *)&(rnk[0]), (int32 *)dims, (intn) maxrank);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	if (rval==FAIL) {
		(*env)->ReleaseIntArrayElements(env,dimsizes,dims,JNI_ABORT);
		(*env)->ReleaseIntArrayElements(env,rank,rnk,JNI_ABORT); 
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,dimsizes,dims,0); 
		(*env)->ReleaseIntArrayElements(env,rank,rnk,0);
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdimscale 
( JNIEnv *env,
jclass class, 
jint dim, 
jint size, 
jbyteArray scale) /* OUT: byte[] assumed to be long enough */
{
	intn rval;
	jbyte *s;
	jboolean bb;

	s = (jbyte *)(*env)->GetByteArrayElements(env,scale,&bb);

	rval = DFSDgetdimscale((intn) dim, (int32) size, (VOIDP) s);

	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,scale,s,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,scale,s,0); /* write back */
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetdimstrs 
( JNIEnv *env,
jclass class, 
jint dim, 
jobjectArray dimstrs) /* OUT: jstring label, jstring unit, jstring format */
{
	intn rval;
	int ll;
	int ul;
	int fl;
	char *l;
	char *u;
	char *f;
	jclass Sjc;
	jstring rstring;
	jobject o;
	jboolean bb;

	rval = DFSDgetdimlen((intn) dim, (intn *)&ll, (intn *)&ul, (intn *)&fl);

	if (rval == FAIL) {
		return JNI_FALSE;
	}

	l = (char *)HDmalloc(ll+1);
	if (l == NULL) {
		h4outOfMemory(env,"DFSDgetdimstrs");
		return JNI_FALSE;
        }
	u = (char *)HDmalloc(ul+1);
	if (u == NULL) {
		HDfree(l);
		h4outOfMemory(env,"DFSDgetdimstrs");
		return JNI_FALSE;
        }
	f = (char *)HDmalloc(fl+1);
	if (f == NULL) {
		HDfree(u);
		HDfree(l);
		h4outOfMemory(env,"DFSDgetdimstrs");
		return JNI_FALSE;
        }

	rval = DFSDgetdimstrs((intn) dim, (char *)l, (char *)u, (char *)f);

	l[ll] = '\0';
	u[ul] = '\0';
	f[fl] = '\0';

	if (rval == FAIL) {
		if (l != NULL)
			HDfree((char *)l);
		if (u != NULL)
			HDfree((char *)u);
		if (f != NULL)
			HDfree((char *)f);
		return JNI_FALSE;
	} else {

		Sjc = (*env)->FindClass(env, "java/lang/String");
		if (Sjc == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE; /* exception is raised */
		}

		rstring = (*env)->NewStringUTF(env, l);

		o = (*env)->GetObjectArrayElement(env,dimstrs,0);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,dimstrs,0,(jobject)rstring);

		rstring = (*env)->NewStringUTF(env, u);

		o = (*env)->GetObjectArrayElement(env,dimstrs,1);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,dimstrs,1,(jobject)rstring);

		rstring = (*env)->NewStringUTF(env, f);

		o = (*env)->GetObjectArrayElement(env,dimstrs,2);
		if (o == NULL) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		bb = (*env)->IsInstanceOf(env,o,Sjc);
		if (bb == JNI_FALSE) {
			if (l != NULL)
				HDfree((char *)l);
			if (u != NULL)
				HDfree((char *)u);
			if (f != NULL)
				HDfree((char *)f);
			return JNI_FALSE;
		}
		(*env)->SetObjectArrayElement(env,dimstrs,2,(jobject)rstring);

		if (l != NULL)
			HDfree((char *)l);
		if (u != NULL)
			HDfree((char *)u);
		if (f != NULL)
			HDfree((char *)f);

		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetfillvalue 
( JNIEnv *env,
jclass class, 
jbyteArray fill_value)  /* OUT: some kind of number? */
{
	intn rval;
	jbyte *dat;
	jboolean bb;

	dat = (jbyte *)(*env)->GetByteArrayElements(env,fill_value,&bb);
	if (dat == NULL) {
		/* exception */
		return(JNI_FALSE);
	}

	rval = DFSDgetfillvalue((int32 *)dat);
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,fill_value,dat,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,fill_value,dat,0); /* write back */
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetNT 
( JNIEnv *env,
jclass class, 
jintArray data_type) /* OUT: Integer */
{
	intn rval;
	jint *dt;
	jboolean bb;

	dt = (*env)->GetIntArrayElements(env,data_type,&bb);
	if (dt == NULL) {
		/* exception */
		return(JNI_FALSE);
	}
	
	rval = DFSDgetNT((int32 *)&(dt[0]));

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,data_type,dt,JNI_ABORT); 
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,data_type,dt,0); 
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetrange 
( JNIEnv *env,
jclass class, 
jbyteArray max,  /* OUT:  byte[]? */
jbyteArray min)  /* OUT:  byte[] ? */
{
        int32 retVal;
        jbyte *minp, *maxp;
        jboolean bb;

        maxp = (*env)->GetByteArrayElements(env,max,&bb);
        minp = (*env)->GetByteArrayElements(env,min,&bb);

        retVal = DFSDgetrange(maxp, minp);

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

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDgetslice 
( JNIEnv *env,
jclass class, 
jstring filename, 
jintArray winst, /* IN: int[] */
jintArray windims, /* IN: int[] */
jbyteArray data, /* OUT: byte[] */
jintArray dims) /* OUT: int [] */
{
	intn rval;
	jchar * name;
	jbyte * dat;
	jint * wi;
	jint * wd;
	jint * d;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	wi = (jint *)(*env)->GetIntArrayElements(env,winst,&bb);
	wd = (jint *)(*env)->GetIntArrayElements(env,windims,&bb);
	d = (jint *)(*env)->GetIntArrayElements(env,dims,&bb);
	dat = (jbyte *)(*env)->GetByteArrayElements(env,data,&bb);

	rval = DFSDgetslice((char *)name, (int32 *) wi, (int32 *) wd, (VOIDP) dat, (int32 *) d);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	(*env)->ReleaseIntArrayElements(env,winst,wi,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,windims,wd,JNI_ABORT); /* no write back */
	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,data,dat,JNI_ABORT); /* no write back */
		(*env)->ReleaseIntArrayElements(env,dims,d,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,data,dat,0); /* write back */
		(*env)->ReleaseIntArrayElements(env,dims,d,0); /* write back */
		return JNI_TRUE;
	}
}


JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDlastref 
( JNIEnv *env,
jobject obj) 
{
	return DFSDlastref( );
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDndatasets 
( JNIEnv *env,
jclass class, 
jstring filename)
{
	intn rval;
	jchar * name;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDndatasets((char *)name);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);

	return rval;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDpre32sdg 
( JNIEnv *env,
jclass class, 
jstring filename, 
jshort ref, 
jintArray ispre32) /* OUT: int[] */
{
	intn rval;
	jchar * name;
	jint *d;
	jboolean bb;

	d = (jint *)(*env)->GetIntArrayElements(env,ispre32,&bb);
	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDpre32sdg((char *)name, (uint16) ref, (intn *)&(d[0]));

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,ispre32,d,JNI_ABORT);
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,ispre32,d,0);
		return JNI_TRUE;
	}
}



JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDputdata 
( JNIEnv *env,
jclass class, 
jstring filename, 
jint rank, 
jintArray dimsizes, /* IN: int[] */
jbyteArray data)  /* IN: byte[] */
{
	intn rval;
	jchar * name;
	jbyte * dat;
	jint * dims;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	dims = (jint *)(*env)->GetIntArrayElements(env,dimsizes,&bb);
	dat = (jbyte *)(*env)->GetByteArrayElements(env,data,&bb);

	rval = DFSDputdata((char *)name, (intn) rank, (int32 *) dims, (VOIDP) dat);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	(*env)->ReleaseByteArrayElements(env,data,dat,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,dimsizes,dims,JNI_ABORT); /* no write back */
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDputslice 
( JNIEnv *env,
jclass class, 
jintArray windims,  /* IN: int[] */
jbyteArray source,  /* IN: byte[] */
jintArray  dims) /* IN: int[] */
{
	intn rval;
	jbyte * dat;
	jint * wd;
	jint * d;
	jboolean bb;

	d = (jint *)(*env)->GetIntArrayElements(env,dims,&bb);
	wd = (jint *)(*env)->GetIntArrayElements(env,windims,&bb);
	dat = (jbyte *)(*env)->GetByteArrayElements(env,source,&bb);


	rval = DFSDputslice((int32 *)windims, (VOIDP)source, (int32 *)dims);

	(*env)->ReleaseByteArrayElements(env,source,dat,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,dims,d,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,windims,wd,JNI_ABORT); /* no write back */

	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDreadref 
( JNIEnv *env,
jclass class, 
jstring filename, 
jshort ref)
{
	intn rval;
	jchar * name;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDreadref((char *)name, (uint16) ref);
	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	if (rval==FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}

}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDreadslab 
( JNIEnv *env,
jclass class, 
jstring filename,
jintArray start, 	/* IN: int[] */
jintArray slab_size, 	/* IN: int[] */
jintArray stride, 	/* IN: int[] */
jbyteArray buffer,	/* OUT: byte[] */
jintArray buffer_size)	/* OUT: int[] */
{
	intn rval;
	jbyte * dat;
	jint * strt;
	jint * siz;
	jint * strd;
	jint * bsize;
	jchar * name;
	jboolean bb;

	name =(jchar *) (*env)->GetStringUTFChars(env,filename,0);
	strt = (jint *)(*env)->GetIntArrayElements(env,start,&bb);
	siz = (jint *)(*env)->GetIntArrayElements(env,slab_size,&bb);
	strd = (jint *)(*env)->GetIntArrayElements(env,stride,&bb);
	bsize = (jint *)(*env)->GetIntArrayElements(env,buffer_size,&bb);
	dat = (jbyte *)(*env)->GetByteArrayElements(env,buffer,&bb);


	rval = DFSDreadslab((char *)name, (int32 *)strt, (int32 *)siz, (int32 *)strd, 
	    (VOIDP) dat, (int32 *) bsize);
	(*env)->ReleaseStringUTFChars(env,filename,(char *)name);
	(*env)->ReleaseIntArrayElements(env,start,strt,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,slab_size,siz,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,stride,strd,JNI_ABORT); /* no write back */

	if (rval==FAIL) {
		(*env)->ReleaseByteArrayElements(env,buffer,dat,JNI_ABORT); /* no write back */
		(*env)->ReleaseIntArrayElements(env,buffer_size,bsize,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,buffer,dat,0); /* write back */
		(*env)->ReleaseIntArrayElements(env,buffer_size,bsize,0); /* write back */
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDrestart 
( JNIEnv *env,
jobject obj) 
{
	if (DFSDrestart( ) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetcal 
( JNIEnv *env,
jclass class, 
jdouble cal, 
jdouble cal_err, 
jdouble offset, 
jdouble offset_err, 
jint data_type)
{
	if (DFSDsetcal((float64) cal, (float64) cal_err, (float64) offset, 
	    (float64) offset_err, (int32) data_type) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetdatastrs 
( JNIEnv *env,
jclass class, 
jstring label, 
jstring unit, 
jstring format, 
jstring coordsys)
{
	intn rval;
	jchar * l;
	jchar * u;
	jchar * f;
	jchar * c;

	l =(jchar *) (*env)->GetStringUTFChars(env,label,0);
	u =(jchar *) (*env)->GetStringUTFChars(env,unit,0);
	f =(jchar *) (*env)->GetStringUTFChars(env,format,0);
	c =(jchar *) (*env)->GetStringUTFChars(env,coordsys,0);

	rval = DFSDsetdatastrs((char *)l, (char *)u, (char *)f, (char *)c);

	(*env)->ReleaseStringUTFChars(env,label,(char *)l);
	(*env)->ReleaseStringUTFChars(env,unit,(char *)u);
	(*env)->ReleaseStringUTFChars(env,format,(char *)f);
	(*env)->ReleaseStringUTFChars(env,coordsys,(char *)c);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetdims 
( JNIEnv *env,
jclass class, 
jint rank, 
jintArray dimsizes)  /* IN:  int[] */
{
	intn rval;
	jint *d;
	jboolean bb;

	d = (jint *)(*env)->GetIntArrayElements(env,dimsizes,&bb);

	rval = DFSDsetdims ((intn) rank, (int32 *) d);

	if (rval == FAIL) {
		(*env)->ReleaseIntArrayElements(env,dimsizes,d,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseIntArrayElements(env,dimsizes,d,0); /* write back */
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetdimscale 
( JNIEnv *env,
jclass class, 
jint dim, 
jint dimsize, 
jintArray scale) /* IN: byte[] */
{
	intn rval;
	jbyte *d;
	jboolean bb;

	d = (jbyte *)(*env)->GetByteArrayElements(env,scale,&bb);

	rval = DFSDsetdimscale ((intn) dim, (int32) dimsize, (VOIDP) d);
	if (rval == FAIL) {
		(*env)->ReleaseByteArrayElements(env,scale,d,JNI_ABORT); /* no write back */
		return JNI_FALSE;
	} else {
		(*env)->ReleaseByteArrayElements(env,scale,d,0); /* write back */
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetdimstrs 
( JNIEnv *env,
jclass class, 
jint dim, 
jstring label, 
jstring unit,
jstring format)
{
	intn rval;
	jchar * l;
	jchar * u;
	jchar * f;

	l =(jchar *) (*env)->GetStringUTFChars(env,label,0);
	u =(jchar *) (*env)->GetStringUTFChars(env,unit,0);
	f =(jchar *) (*env)->GetStringUTFChars(env,format,0);

	rval = DFSDsetdimstrs((intn) dim, (char *)l, (char *)u, (char *)f);

	(*env)->ReleaseStringUTFChars(env,label,(char *)l);
	(*env)->ReleaseStringUTFChars(env,unit,(char *)u);
	(*env)->ReleaseStringUTFChars(env,format,(char *)f);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetfillvalue 
( JNIEnv *env,
jclass class, 
jbyteArray fill_value)  /* IN: ?? */
{
	intn rval;
	jbyte * fv;
	jboolean bb;

	fv = (jbyte *)(*env)->GetByteArrayElements(env,fill_value,&bb);
	rval = DFSDsetfillvalue((VOIDP) fv);
	(*env)->ReleaseByteArrayElements(env,fill_value,fv,JNI_ABORT); /* no write back */

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetlengths 
( JNIEnv *env,
jclass class, 
jint label_len, 
jint unit_len, 
jint format_len, 
jint coords_len)
{
	if (DFSDsetlengths((intn) label_len, (intn) unit_len, (intn) format_len, 
	    (intn) coords_len) == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetNT 
( JNIEnv *env,
jclass class, 
jint data_type)
{
	if (DFSDsetNT((int32) data_type) == FAIL ) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDsetrange 
( JNIEnv *env,
jclass class, 
jbyteArray max, /* IN: byte[] */
jbyteArray min)/* IN: byte[] */
{
	intn rval;
	jbyte * mx;
	jbyte * mn;
	jboolean bb;

	mx = (jbyte *)(*env)->GetByteArrayElements(env,max,&bb);
	mn = (jbyte *)(*env)->GetByteArrayElements(env,min,&bb);
	rval  = DFSDsetrange((VOIDP) mx, (VOIDP) mn);
	(*env)->ReleaseByteArrayElements(env,max,mx,JNI_ABORT); /* no write back */
	(*env)->ReleaseByteArrayElements(env,min,mn,JNI_ABORT); /* no write back */

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDstartslab 
( JNIEnv *env,
jclass class, 
jstring filename)
{
	intn rval;
	jchar * f;

	f =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDstartslab((char *)f);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)f);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDstartslice 
( JNIEnv *env,
jclass class, 
jstring filename)
{
	intn rval;
	jchar * f;

	f =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDstartslice((char *)f);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)f);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDwriteref 
( JNIEnv *env,
jclass class, 
jstring filename,
jshort ref)
{
	intn rval;
	jchar * f;

	f =(jchar *) (*env)->GetStringUTFChars(env,filename,0);

	rval = DFSDwriteref((char *)f,(uint16)ref);

	(*env)->ReleaseStringUTFChars(env,filename,(char *)f);

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFDeprecated_DFSDwriteslab 
( JNIEnv *env,
jclass class, 
jintArray  start,  /* IN: int[] */
jintArray stride,  /* IN: int[] */
jintArray count, /* IN: int[] */
jbyteArray data) /* IN: byte[] */
{
	intn rval;
	jint *strt;
	jint *strd;
	jint *cnt;
	jbyte *d;
	jboolean bb;

	strt = (jint *)(*env)->GetIntArrayElements(env,start,&bb);
	strd = (jint *)(*env)->GetIntArrayElements(env,stride,&bb);
	cnt = (jint *)(*env)->GetIntArrayElements(env,count,&bb);
	d = (jbyte *)(*env)->GetByteArrayElements(env,data,&bb);

	rval = DFSDwriteslab((int32 *)strt, (int32 *) strd, (int32 *) cnt,
	    (VOIDP)d);

	(*env)->ReleaseIntArrayElements(env,start,strt,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,stride,strd,JNI_ABORT); /* no write back */
	(*env)->ReleaseIntArrayElements(env,count,cnt,JNI_ABORT); /* no write back */
	(*env)->ReleaseByteArrayElements(env,data,d,JNI_ABORT); /* no write back */

	if (rval == FAIL) {
		return JNI_FALSE;
	} else {
		return JNI_TRUE;
	}
}

