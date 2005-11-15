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
 *  This is a utility program used by the HDF Java-C wrapper layer to
 *  generate exceptions.  This may be called from any part of the
 *  Java-C interface.
 *
 */
#ifdef __cplusplus
extern "C" {
#endif

#include "jni.h"
char *defineH45LibraryException(int maj_num);

/*
 *  Create and throw an 'outOfMemoryException'
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45outOfMemory( JNIEnv *env, char *functName)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass("java/lang/OutOfMemoryError");
#else
	jc = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = (env)->NewStringUTF(functName);
#else
	str = (*env)->NewStringUTF(env,functName);
#endif
	args[0] = (char *)str;
	args[1] = 0;

#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw( (jthrowable ) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  OutOfMemoryError: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

/*
 *  A fatal error in a JNI call
 *  Create and throw an 'InternalError'
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45JNIFatalError( JNIEnv *env, char *functName)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass("java/lang/InternalError");
#else
	jc = (*env)->FindClass(env, "java/lang/InternalError");
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = env->NewStringUTF(functName);
#else
	str = (*env)->NewStringUTF(env,functName);
#endif
	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw( (jthrowable) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  JNIFatal: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

/*
 *  A NULL argument in an HDF5 call
 *  Create and throw an 'NullPointerException'
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45nullArgument( JNIEnv *env, char *functName)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass("java/lang/NullPointerException");
#else
	jc = (*env)->FindClass(env, "java/lang/NullPointerException");
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = env->NewStringUTF(functName);
#else
	str = (*env)->NewStringUTF(env,functName);
#endif
	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw((jthrowable) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif

	if (rval < 0) {
		printf("FATAL ERROR:  NullPoitner: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

/*
 *  A bad argument in an HDF5 call
 *  Create and throw an 'IllegalArgumentException'
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45badArgument( JNIEnv *env, char *functName)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass("java/lang/IllegalArgumentException");
#else
	jc = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = env->NewStringUTF(functName);
#else
	str = (*env)->NewStringUTF(env,functName);
#endif
	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw((jthrowable) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  BadArgument: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

/*
 *  Some feature Not implemented yet
 *  Create and throw an 'UnsupportedOperationException'
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45unimplemented( JNIEnv *env, char *functName)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass("java/lang/UnsupportedOperationException");
#else
	jc = (*env)->FindClass(env, "java/lang/UnsupportedOperationException");
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = env->NewStringUTF(functName);
#else
	str = (*env)->NewStringUTF(env,functName);
#endif
	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw((jthrowable) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  Unsupported: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

jboolean h45buildException( JNIEnv *env, char *exception, jint HDFerr)
{
	jmethodID jm;
	jclass jc;
	int args[2];
	jobject ex;
	int rval;


	jc = (*env)->FindClass(env, exception);
	if (jc == NULL) {
		return JNI_FALSE;
	}
	jm = (*env)->GetMethodID(env, jc, "<init>", "(I)V");
	if (jm == NULL) {
		return JNI_FALSE;
	}
	args[0] = HDFerr;
	args[1] = 0;

	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
	if (rval < 0) {
		printf("FATAL ERROR:  raiseException: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

#ifdef __cplusplus
}
#endif





/*
 *  libraryError()   determines the HDF-5 major error code
 *  and creates and throws the appropriate sub-class of
 *  HDF5LibraryException().  This routine should be called
 *  whenever a call to the HDF-5 library fails, i.e., when
 *  the return is -1.
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45libraryError( JNIEnv *env )
{
	jmethodID jm;
	jclass jc;
	char *args[2];
	char *exception;
	jobject ex;
	jstring str;
	char *msg;
	int rval;

	exception = (char *)defineH45LibraryException(0);

#ifdef __cplusplus
	jc = env->FindClass(exception);
#else
	jc = (*env)->FindClass(env, exception);
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

	msg = "*"; /* need to put a message in here.... */
#ifdef __cplusplus
	str = env->NewStringUTF(msg);
#else
	str = (*env)->NewStringUTF(env,msg);
#endif

	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA ( jc, jm, (jvalue *)args );

	rval = env->Throw((jthrowable) ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  libraryError: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}


/*  raiseException().  This routine is called to generate
 *  an arbitrary Java exception with a particular message.
 *  
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h45raiseException( JNIEnv *env, char *exception, char *message)
{
	jmethodID jm;
	jclass jc;
	char * args[2];
	jobject ex;
	jstring str;
	int rval;

#ifdef __cplusplus
	jc = env->FindClass(exception);
#else
	jc = (*env)->FindClass(env, exception);
#endif
	if (jc == NULL) {
		return JNI_FALSE;
	}
#ifdef __cplusplus
	jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
	jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
	if (jm == NULL) {
		return JNI_FALSE;
	}

#ifdef __cplusplus
	str = env->NewStringUTF(message);
#else
	str = (*env)->NewStringUTF(env,message);
#endif
	args[0] = (char *)str;
	args[1] = 0;
#ifdef __cplusplus
	ex = env->NewObjectA (  jc, jm, (jvalue *)args );

	rval = env->Throw( (jthrowable)ex );
#else
	ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

	rval = (*env)->Throw(env, ex );
#endif
	if (rval < 0) {
		printf("FATAL ERROR:  raiseException: Throw failed\n");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

/*
 *  defineH45LibraryException()  returns the name of the sub-class
 *  which goes with an HDF-5 error code.
 */
char *defineH45LibraryException(int maj_num)
{
/* to do:  figure out the error and create a specific exception for each */
	 return "ncsa/hdf/libh4toh5/exceptions/H45LibraryException";
}

#ifdef __cplusplus
}
#endif
