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

#include "H5version.h"

#ifndef _Included_h5jni
#define _Included_h5jni

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif

#define GET_ENUM_VALUE(_obj, _val) { \
    jclass enum_cls = ENVPTR->GetObjectClass(ENVPAR _obj); \
    jmethodID enum_getCode = ENVPTR->GetMethodID(ENVPAR enum_cls, "getCode", "()I"); \
    _val = ENVPTR->CallIntMethod(ENVPAR _obj, enum_getCode); \
}

#ifdef __cplusplus
extern "C" {
#endif
extern jboolean h5JNIFatalError(JNIEnv *, char *);
extern jboolean h5nullArgument(JNIEnv *, char *);
extern jboolean h5badArgument (JNIEnv *, char *);
extern jboolean h5outOfMemory (JNIEnv *, char *);
extern jboolean h5libraryError(JNIEnv *env );
extern jboolean h5raiseException(JNIEnv *, char *, char *);
#ifdef __cplusplus
}
#endif

#endif
