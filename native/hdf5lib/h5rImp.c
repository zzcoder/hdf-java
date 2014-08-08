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
 *  Reference API Functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */
#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include <jni.h>
#include <stdlib.h>
#include "h5jni.h"


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rcreate
 * Signature: ([BJLjava/lang/String;IJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rcreate
  (JNIEnv *env, jclass clss, jbyteArray ref, jlong loc_id, jstring name, jint ref_type, jlong space_id)
{
    char* rName;
    jboolean isCopy;
    herr_t status;
    jbyte *refP;

    if (ref == NULL) {
        h5nullArgument( env, "H5Rcreate:  ref is NULL");
        return -1;
    }
    if (name == NULL) {
        h5nullArgument( env, "H5Rcreate:  name is NULL");
        return -1;
    }
    if (ref_type == H5R_OBJECT) {
        if (ENVPTR->GetArrayLength(ENVPAR ref) != H5R_OBJ_REF_BUF_SIZE) {
            h5badArgument( env, "H5Rcreate:  ref input array != H5R_OBJ_REF_BUF_SIZE");
            return -1;
        }
    }
    else if (ref_type == H5R_DATASET_REGION) {
        if (ENVPTR->GetArrayLength(ENVPAR ref) != H5R_DSET_REG_REF_BUF_SIZE) {
            h5badArgument( env, "H5Rcreate:  region ref input array != H5R_DSET_REG_REF_BUF_SIZE");
            return -1;
        }
    }
    else {
        h5badArgument( env, "H5Rcreate:  ref_type unknown type ");
        return -1;
    }

    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rcreate:  ref not pinned");
        return -1;
    }
    rName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
    if (rName == NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR ref,refP,JNI_ABORT);
        h5JNIFatalError(env,  "H5Rcreate:  name not pinned");
        return -1;
    }

    status = H5Rcreate(refP, (hid_t)loc_id, rName, (H5R_type_t)ref_type, (hid_t)space_id);
    ENVPTR->ReleaseStringUTFChars(ENVPAR name, rName);
    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);
        h5libraryError(env);
    }
    else {
        ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, 0);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Rdereference
 * Signature: (JJI[B)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Rdereference
  (JNIEnv *env, jclass clss, jlong dataset, jlong access_list, jint ref_type, jbyteArray ref )
{
    jboolean isCopy;
    jbyte *refP;
    hid_t status;

    if (ref == NULL) {
        h5nullArgument( env, "H5Rdereference:  ref is NULL");
        return -1;
    }
    if ((ref_type == H5R_OBJECT) && ENVPTR->GetArrayLength(ENVPAR ref) != H5R_OBJ_REF_BUF_SIZE) {
        h5badArgument( env, "H5Rdereference:  obj ref input array != H5R_OBJ_REF_BUF_SIZE");
        return -1;
    }
    else if ((ref_type == H5R_DATASET_REGION)
        && ENVPTR->GetArrayLength(ENVPAR ref) != H5R_DSET_REG_REF_BUF_SIZE) {
        h5badArgument( env, "H5Rdereference:  region ref input array != H5R_DSET_REG_REF_BUF_SIZE");
        return -1;
    }
    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rderefernce:  ref not pinned");
        return -1;
    }

    status = H5Rdereference2((hid_t)dataset, (hid_t)access_list, (H5R_type_t)ref_type, refP);

    ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rget_region
 * Signature: (JI[B)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Rget_1region
  (JNIEnv *env, jclass clss, jlong dataset, jint ref_type,
  jbyteArray ref )
{
    hid_t status;
    jboolean isCopy;
    jbyte *refP;

    if (ref_type != H5R_DATASET_REGION)  {
        h5badArgument( env, "H5Rget_region:  bad ref_type ");
        return -1;
    }

    if (ref == NULL) {
        h5nullArgument( env, "H5Rget_region:  ref is NULL");
        return -1;
    }
    if ( ENVPTR->GetArrayLength(ENVPAR ref) != H5R_DSET_REG_REF_BUF_SIZE) {
        h5badArgument( env, "H5Rget_region:  region ref input array != H5R_DSET_REG_REF_BUF_SIZE");
        return -1;
    }
    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rget_region:  ref not pinned");
        return -1;
    }

    status = H5Rget_region((hid_t)dataset, (H5R_type_t)ref_type, refP);

    ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5G_obj_t H5Rget_obj_type
 * Signature: (JI[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1obj_1type
  (JNIEnv *env, jclass clss, jlong loc_id, jint ref_type, jbyteArray ref)
{
    int retVal =-1;
    jboolean isCopy;
    jbyte *refP;
    H5O_type_t object_info;


    if (ref == NULL) {
        h5nullArgument( env, "H5Rget_object_type:  ref is NULL");
        return -1;
    }

    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rget_object_type:  ref not pinned");
        return -1;
    }

    retVal = H5Rget_obj_type2((hid_t)loc_id, (H5R_type_t)ref_type, refP, &object_info);
    if(retVal >= 0)
        retVal = object_info;

    ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);

    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    int H5Rget_obj_type2
 * Signature: (JI[B[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1obj_1type2
  (JNIEnv *env, jclass clss, jlong loc_id, jint ref_type, jbyteArray ref, jintArray ref_obj)
{

    jint status;
    jboolean isCopy;
    jbyte *refP;
    jint *ref_objP;
  int retVal;


    if (ref == NULL) {
        h5nullArgument( env, "H5Rget_object_type:  ref is NULL");
        return -1;
    }
    if (ref_obj == NULL) {
        h5nullArgument( env, "H5Rget_object_type:  ref_obj is NULL");
        return -1;
    }

    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rget_object_type:  ref not pinned");
        return -1;
    }
    ref_objP = (jint *)ENVPTR->GetIntArrayElements(ENVPAR ref_obj, &isCopy);
    if (ref_objP == NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR ref,refP,0);
        h5JNIFatalError(env,  "H5Rget_object_type:  ref_obj not pinned");
        return -1;
    }

    status = H5Rget_obj_type2((hid_t)loc_id, (H5R_type_t)ref_type, refP, (H5O_type_t*)ref_objP);
    retVal = ref_objP[0];

    ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);
    if (status < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR ref_obj,ref_objP, JNI_ABORT);
        h5libraryError(env);
    } 
    else {
        ENVPTR->ReleaseIntArrayElements(ENVPAR ref_obj, ref_objP, 0);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Rget_name
 * Signature: (JI[B[Ljava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Rget_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jint ref_type, jbyteArray ref, jobjectArray name, jlong size)
{
    jlong ret_val = -1;
    jbyte *refP;
    jboolean isCopy;
    char *aName=NULL;
    jstring str;
    size_t bs;

    bs = (size_t)size;
    if (bs <= 0) {
        h5badArgument( env, "H5Rget_name:  size <= 0");
        return -1;
    }

    if (ref == NULL) {
        h5nullArgument( env, "H5Rget_name:  ref is NULL");
        return -1;
    }

    if ((ref_type == H5R_OBJECT) && ENVPTR->GetArrayLength(ENVPAR ref) != H5R_OBJ_REF_BUF_SIZE) {
        h5badArgument( env, "H5Rdereference:  obj ref input array != H5R_OBJ_REF_BUF_SIZE");
        return -1;
    } 
    else if ((ref_type == H5R_DATASET_REGION)
            && ENVPTR->GetArrayLength(ENVPAR ref) != H5R_DSET_REG_REF_BUF_SIZE) {
        h5badArgument( env, "H5Rdereference:  region ref input array != H5R_DSET_REG_REF_BUF_SIZE");
        return -1;
    }

    refP = (jbyte *)ENVPTR->GetByteArrayElements(ENVPAR ref, &isCopy);
    if (refP == NULL) {
        h5JNIFatalError(env,  "H5Rcreate:  ref not pinned");
        return -1;
    }

    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);
        h5outOfMemory( env, "H5Aget_name:  malloc failed");
        return -1;
    }

    ret_val = (jlong) H5Rget_name((hid_t)loc_id, (H5R_type_t) ref_type, refP, aName, bs) ;

    ENVPTR->ReleaseByteArrayElements(ENVPAR ref, refP, JNI_ABORT);
    if (ret_val < 0) {
        free(aName);
        h5libraryError(env);
        return -1;
    }

    str = ENVPTR->NewStringUTF(ENVPAR aName);
    ENVPTR->SetObjectArrayElement(ENVPAR name, 0, str);

    if (aName) free (aName);

    return ret_val;
}


#ifdef __cplusplus
}
#endif
