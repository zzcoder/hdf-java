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

/*
 *  This code is the C-interface called by Java programs to access the
 *  Group Object API Functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *    http://hdfdfgroup.org/HDF5/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>
#include <stdlib.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5lImp.h"

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR 
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Lget_val
     * Signature: (ILjava/lang/String;I)Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Lget_1val
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char* lName;
        jboolean isCopy;
        char *lValue;
        size_t buf_size;
        jstring str;
        herr_t status;
        H5L_info_t infobuf;

        if (name == NULL) {
            h5nullArgument( env, "H5Lget_val:  name is NULL");
            return NULL;
        }
        
        lName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);
        if (lName == NULL) {
            h5JNIFatalError( env, "H5Lget_val:  name not pinned");
            return NULL;
        }

        /* get the length of the link val */
        status = H5Lget_info(loc_id, lName, &infobuf, H5P_DEFAULT);
        if(status < 0) {
            h5libraryError(env);
            return NULL;
        }
        buf_size = infobuf.u.val_size + 1;/* add extra space for the null terminator */
        
        if(infobuf.type != H5L_TYPE_SOFT) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5JNIFatalError( env, "H5Lget_val:  link not symbolic");
            return NULL;
        }
        
        lValue = (char *) malloc(sizeof(char)*buf_size);
        if (lValue == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
            h5outOfMemory( env, "H5Lget_val:  malloc failed ");
            return NULL;
        }

        status = H5Lget_val((hid_t)loc_id, lName, lValue, (size_t)buf_size, access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name,lName);
        if (status < 0) {
            free(lValue);
           h5libraryError(env);
           return NULL;
        }
        /* may throw OutOfMemoryError */
        str = ENVPTR->NewStringUTF(ENVPAR lValue);
        if (str == NULL) {
            /* exception -- fatal JNI error */
            free(lValue);
            h5JNIFatalError( env, "H5Lget_val:  return string not created");
            return NULL;
        }

        free(lValue);

        return str;
    }


#ifdef __cplusplus
}
#endif
