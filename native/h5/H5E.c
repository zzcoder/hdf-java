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

/*
 *  This code is the C-interface called by Java programs to access the
 *  general library functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://www.hdfgroup.org/HDF5/doc/
 *
 */

#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5E.h"

    /*
     * Class:     hdf_h5_H5E
     * Method:    H5Eregister_class
     * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
     */
    JNIEXPORT jint JNICALL Java_hdf_h5_H5E_H5Eregister_1class
      (JNIEnv *env, jclass cls, jstring cls_name, jstring lib_name, jstring version)
    {
        herr_t ret_val = -1;
        char* the_cls_name;
        char* the_lib_name;
        char* the_version;
        jboolean isCopy;

        if(cls_name==NULL) {
            h5nullArgument( env, "H5Eregister_class: error class name is NULL");
            return ret_val;
        }
        the_cls_name = (char *)ENVPTR->GetStringUTFChars(ENVPAR cls_name,&isCopy);
        if (the_cls_name == NULL) {
            h5JNIFatalError( env, "H5Eregister_class: error class name not pinned");
            return ret_val;
        }
        if(lib_name==NULL) {
            h5nullArgument( env, "H5Eregister_class: client library or application name is NULL");
            return ret_val;
        }
        the_lib_name = (char *)ENVPTR->GetStringUTFChars(ENVPAR lib_name,&isCopy);
        if (the_lib_name == NULL) {
            h5JNIFatalError( env, "H5Eregister_class: client name not pinned");
            return ret_val;
        }
        if(version==NULL) {
            h5nullArgument( env, "H5Eregister_class: version of the client library or application is NULL");
            return ret_val;
        }
        the_version = (char *)ENVPTR->GetStringUTFChars(ENVPAR version,&isCopy);
        if (version == NULL) {
            h5JNIFatalError( env, "H5Eregister_class: version not pinned");
            return ret_val;
        }
        ret_val = H5Eregister_class(cls_name, lib_name, version);
        ENVPTR->ReleaseStringUTFChars(ENVPAR cls_name, the_cls_name);
        ENVPTR->ReleaseStringUTFChars(ENVPAR lib_name, the_lib_name);
        ENVPTR->ReleaseStringUTFChars(ENVPAR version, the_version);
        if (ret_val < 0) {
            h5libraryError(env);
        }
        return (jint)ret_val;
    }

    /*
     * Class:     hdf_h5_H5E
     * Method:    H5Eunregister_class
     * Signature: (I)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5E_H5Eunregister_1class
      (JNIEnv *env, jclass cls, jint cls_id){
        herr_t ret_val = -1;

        if (cls_id < 0) {
            h5badArgument(env, "H5Eunregister_class: invalid argument");
            return;
        }
        ret_val = H5Eunregister_class((hid_t)cls_id);
        if (ret_val < 0) {
            h5libraryError(env);
        }
    }

    /*
     * Class:     hdf_h5_H5E
     * Method:    H5Eprint2
     * Signature: (ILjava/lang/Object;)V
     */
    JNIEXPORT void JNICALL Java_hdf_h5_H5E_H5Eprint2
      (JNIEnv *env, jclass cls, jint cls_id, jobject stream_obj)
    {
        herr_t ret_val = -1;

        if (cls_id < 0) {
            h5badArgument(env, "H5Eprint2: invalid argument");
            return;
        }
        ret_val = H5Eprint2((hid_t)cls_id, (FILE*)stream_obj);
        if (ret_val < 0) {
            h5libraryError(env);
        }
    }



#ifdef __cplusplus
}
#endif
