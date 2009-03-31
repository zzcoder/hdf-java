/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF Java Products. The full HDF Java copyright       *
 * notice, including terms governing use, modification, and redistribution,  *
 * is contained in the file, COPYING.  COPYING can be found at the root of   *
 * the source code distribution tree. You can also access it online  at      *
 * http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
 * access to the file, you may request a copy from help@hdfgroup.org.        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


#ifdef __cplusplus
extern "C" {
#endif

#include "hdf.h"
#include <jni.h>
#include "h4jni.h"


jboolean h4buildException( JNIEnv *env, jint HDFerr)
{
jmethodID jm;
jclass jc;
int args[2];
jobject ex;
int rval;


    jc = ENVPTR->FindClass(ENVPAR  "hdf/h4/HDFLibraryException");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = ENVPTR->GetMethodID(ENVPAR  jc, "<init>", "(I)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }
    args[0] = HDFerr;
    args[1] = 0;

    ex = ENVPTR->NewObjectA (ENVPAR jc, jm, (jvalue *)args );

    rval = ENVPTR->Throw(ENVPAR  (jthrowable)ex );

    return JNI_TRUE;
}

jboolean h4NotImplemented( JNIEnv *env, char *functName)
{
jmethodID jm;
jclass jc;
char * args[2];
jobject ex;
jstring str;
int rval;


    jc = ENVPTR->FindClass(ENVPAR  "hdf/h4/HDFNotImplementedException");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = ENVPTR->GetMethodID(ENVPAR  jc, "<init>", "(Ljava/lang/String;)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }

    str = ENVPTR->NewStringUTF(ENVPAR functName);
    args[0] = (char *)str;
    args[1] = 0;
    ex = ENVPTR->NewObjectA (ENVPAR jc, jm, (jvalue *)args );

    rval = ENVPTR->Throw(ENVPAR  (jthrowable)ex );

    return JNI_TRUE;
}

jboolean h4outOfMemory( JNIEnv *env, char *functName)
{
jmethodID jm;
jclass jc;
char * args[2];
jobject ex;
jstring str;
int rval;

    jc = ENVPTR->FindClass(ENVPAR  "java/lang/OutOfMemoryError");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = ENVPTR->GetMethodID(ENVPAR  jc, "<init>", "(Ljava/lang/String;)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }

    str = ENVPTR->NewStringUTF(ENVPAR functName);
    args[0] = (char *)str;
    args[1] = 0;
    ex = ENVPTR->NewObjectA (ENVPAR jc, jm, (jvalue *)args );

    rval = ENVPTR->Throw(ENVPAR  (jthrowable)ex );

    return JNI_TRUE;
}

/*
 *  A fatal error in a JNI call
 */
jboolean h4JNIFatalError( JNIEnv *env, char *functName)
{
jmethodID jm;
jclass jc;
char * args[2];
jobject ex;
jstring str;
int rval;

    jc = ENVPTR->FindClass(ENVPAR  "java/lang/InternalError");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = ENVPTR->GetMethodID(ENVPAR  jc, "<init>", "(Ljava/lang/String;)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }

    str = ENVPTR->NewStringUTF(ENVPAR functName);
    args[0] = (char *)str;
    args[1] = 0;
    ex = ENVPTR->NewObjectA (ENVPAR jc, jm, (jvalue *)args );

    rval = ENVPTR->Throw(ENVPAR  (jthrowable)ex );

    return JNI_TRUE;
}

jboolean h4raiseException( JNIEnv *env, char *message)
{
jmethodID jm;
jclass jc;
char * args[2];
jobject ex;
jstring str;
int rval;

    jc = ENVPTR->FindClass(ENVPAR  "hdf/h4/HDFLibraryException");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = ENVPTR->GetMethodID(ENVPAR  jc, "<init>", "(Ljava/lang/String;)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }

    str = ENVPTR->NewStringUTF(ENVPAR message);
    args[0] = (char *)str;
    args[1] = 0;
    ex = ENVPTR->NewObjectA (ENVPAR jc, jm, (jvalue *)args );

    rval = ENVPTR->Throw(ENVPAR  (jthrowable)ex );

    return JNI_TRUE;
}

#ifdef __cplusplus
}
#endif
