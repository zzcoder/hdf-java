/****************************************************************************
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
 ****************************************************************************/
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
 *   http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */

#include "hdf5.h"
#include <jni.h>
#include "h5jni.h"
/*
#include <signal.h>
*/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5open
  (JNIEnv *env, jclass clss)
{
    herr_t retVal = -1;
    retVal =  H5open();
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5close
  (JNIEnv *env, jclass clss)
{
    herr_t retVal = -1;
    retVal =  H5close();
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5dont_atexit
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5dont_1atexit
  (JNIEnv *env, jclass clss)
{
    int retVal = H5dont_atexit();
    if (retVal < 0) {
        h5libraryError(env);
    }
    return retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5get_libversion
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5get_1libversion
  (JNIEnv *env, jclass clss, jintArray libversion)
{
    unsigned *theArray = NULL;
    jboolean isCopy;
    int status;

    if (libversion == NULL) {
        h5nullArgument( env, "H5get_version:  libversion is NULL");
        return -1;
    }

    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR libversion,&isCopy);
    if (theArray == NULL) {
        h5JNIFatalError( env, "H5get_libversion:  input not pinned");
        return -1;
    }

    status =  H5get_libversion(&(theArray[0]), &(theArray[1]), &(theArray[2]));

    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR libversion,(jint *)theArray,JNI_ABORT);
        h5libraryError(env);
    }
    else {
        ENVPTR->ReleaseIntArrayElements(ENVPAR libversion,(jint *)theArray,0);
    }
    return (jint)status;
}

#ifdef notdef
/*
 struct sigaction {
   int sa_flags;
     void (*sa_handler)();
     sigset_t sa_mask;
     void (*sa_sigaction)(int, siginfo_t *, void *);
};
int sigaction(int sig, struct sigaction *act, struct sigaction *oact);
*/
void catch_abrt()
{
    /*  Raise Java exception */
    printf("raise exception....\n");
}
#endif
/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5check_version
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5check_1version
  (JNIEnv *env, jclass clss, jint majnum, jint minnum, jint relnum)
{
    int status;
/*
 *   In principle, we want to catch the 'abort' signal, and
 *  do something other than crash.
 *   Look up how to do this portably.
 */
/*
    int res;
    struct sigaction ctchit;
    struct sigaction old;
    ctchit.sa_handler = catch_abrt;
*/

/*
    res = sigaction(SIGABRT, &ctchit, &old);
    if (res != 0) {
        printf("sigaction failed\n");
        return(-1);
    }
*/
    /*  catch the signal? */
    status = H5check_version((unsigned)majnum, (unsigned)minnum, (unsigned)relnum);
/*
    res = sigaction(SIGABRT, &old, 0);
    if (res != 0) {
        printf("sigaction failed\n");
        return(-1);
    }
*/
    return status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5garbage_collect
 * Signature: ()I
 *
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5garbage_1collect
  (JNIEnv *env, jclass clss)
{
    herr_t retVal = -1;
    retVal =  H5garbage_collect();
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5set_free_list_limits
 * Signature: (IIIIII)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5set_1free_1list_1limits
  (JNIEnv *env, jclass clss, jint reg_global_lim, jint reg_list_lim,
  jint arr_global_lim, jint arr_list_lim, jint blk_global_lim, jint blk_list_lim )
{
    int retVal = H5set_free_list_limits((int)reg_global_lim, (int)reg_list_lim,
        (int)arr_global_lim, (int)arr_list_lim, (int)blk_global_lim, (int)blk_list_lim);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return retVal;
}


#ifdef __cplusplus
}
#endif
