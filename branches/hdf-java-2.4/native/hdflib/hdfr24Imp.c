
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
#include "jni.h"

extern jboolean getOldCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf);


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24getdims
( JNIEnv *env,
jclass class,
jstring filename,
jintArray argv) /* OUT: width, height, il */
{
    intn rval;

    char  *hdf_file;
    jint *theArgs;
    jboolean bb;

    theArgs = (*env)->GetIntArrayElements(env,argv,&bb);
    hdf_file =(char *) (*env)->GetStringUTFChars(env,filename,0);

    /* get image dimension information */
    rval = DF24getdims(hdf_file, (int32 *)&(theArgs[0]),
        (int32 *)&(theArgs[1]), (intn *)&(theArgs[2]));

    (*env)->ReleaseStringUTFChars(env,filename,hdf_file);
    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24getimage
( JNIEnv *env,
jclass class,
jstring filename,
jbyteArray image, /* OUT: image data width X height X 3 */
jint width,
jint height)
{
    char  *hdf_file;
    intn   rval;
    jbyte *dat;
    jboolean bb;

    hdf_file =(char *) (*env)->GetStringUTFChars(env,filename,0);
    dat = (*env)->GetByteArrayElements(env,image,&bb);

    rval =  DF24getimage((char *)hdf_file, (VOIDP) dat, (int32) width, (int32) height);

    (*env)->ReleaseStringUTFChars(env,filename,hdf_file);
    if (rval == FAIL) {
        (*env)->ReleaseByteArrayElements(env,image,dat,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseByteArrayElements(env,image,dat,0);
        return JNI_TRUE;
    }

}

JNIEXPORT jshort JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24lastref
( JNIEnv *env,
jobject obj)
{
    return ((short)DF24lastref());
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24readref
( JNIEnv *env,
jclass class,
jstring filename,
jshort ref)
{
    int  retVal;
    char *filePtr;
    filePtr =(char *) (*env)->GetStringUTFChars(env,filename,0);

    retVal = DF24readref(filePtr, (short)ref);

    (*env)->ReleaseStringUTFChars(env,filename,filePtr);
    if (retVal == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24restart
( JNIEnv *env,
jobject obj)
{
    int retVal;
    retVal = DF24restart();

    if (retVal) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24nimages
( JNIEnv *env,
jclass class,
jstring hdfFile)
{
    char  *hdf_file;

    hdf_file =(char *) (*env)->GetStringUTFChars(env,hdfFile,0);
    return(DF24nimages(hdf_file));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24reqil
( JNIEnv *env,
jclass class,
jint interlace)
{
    return(DF24reqil((intn)interlace));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24addimage
( JNIEnv *env,
jclass class,
jstring filename,
jbyteArray image, /* IN: image data width X height X 3 */
jint width,
jint height)
{
    intn rval;
    char  *f;
    jbyte *dat;
    jboolean bb;

    f =(char *) (*env)->GetStringUTFChars(env,filename,0);
    dat = (*env)->GetByteArrayElements(env,image,&bb);

    rval = DF24addimage((char *)f, (VOIDP) dat, (int32) width, (int32) height);

    (*env)->ReleaseStringUTFChars(env,filename,f);
    (*env)->ReleaseByteArrayElements(env,image,dat,JNI_ABORT);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_1DF24putimage
( JNIEnv *env,
jclass class,
jstring filename,
jbyteArray image, /* IN: image data width X height X 3 */
jint width,
jint height)
{
    intn rval;
    char  *f;
    jbyte *dat;
    jboolean bb;

    f =(char *) (*env)->GetStringUTFChars(env,filename,0);
    dat = (*env)->GetByteArrayElements(env,image,&bb);

    rval = DF24putimage((char *)f, (VOIDP) dat, (int32) width, (int32) height);

    (*env)->ReleaseStringUTFChars(env,filename,f);
    (*env)->ReleaseByteArrayElements(env,image,dat,JNI_ABORT);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24setcompress
( JNIEnv *env,
jclass class,
jint type,
jobject cinfo)
{
    intn rval;
    comp_info cinf;
    jboolean bval;

    bval = getOldCompInfo(env,cinfo,&cinf);

    /* check fo rsuccess... */

    /* fill in cinf depending on the value of 'type' */
    rval = DF24setcompress((int32) type, (comp_info *)&cinf);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24setdims
( JNIEnv *env,
jclass class,
jint width,
jint height)
{
    intn rval;

    rval = DF24setdims((int32) width, (int32) height);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DF24setil
( JNIEnv *env,
jclass class,
jint il)
{
    intn rval;
    rval = DF24setil((intn) il);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}
