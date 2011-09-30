
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
#ifdef __cplusplus
extern "C" {
#endif


#include "hdf.h"
#include "jni.h"

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR
#define ENVONLY
#else
#define ENVPTR (*env)
#define ENVPAR env,
#define ENVONLY env
#endif

extern jboolean h4NotImplemented( JNIEnv *env, char *functName);

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_DFUfptoimage
( JNIEnv *env,
jclass clss,
jint hdim,
jint vdim,
jfloat max,
jfloat min,
jfloatArray hscale, /* IN: float [] */
jfloatArray vscale, /* IN: float [] */
jfloatArray data,  /* IN:  float[][] -- > bytes? */
jbyteArray palette,  /* IN: byte[] */
jstring outfile,   /* IN */
jint ct_method,  /* IN */
jint hres, /* IN */
jint vres,  /* IN */
jint compress) /* IN */
{
#ifndef WIN32
    int rval;
    float *hs;
    float *vs;
    char * arr;
    char * pal;
    char *file;
    jboolean bb;

    if (hscale == NULL) {
        hs = NULL;
    } else {
        hs = (float *)ENVPTR->GetFloatArrayElements(ENVPAR hscale,&bb);
    }
    if (vscale == NULL) {
        vs = NULL;
    } else {
        vs = (float *)ENVPTR->GetFloatArrayElements(ENVPAR vscale,&bb);
    }
    arr = (char *)ENVPTR->GetByteArrayElements(ENVPAR data,&bb);
    if (palette == NULL) {
        pal = NULL;
    } else {
        pal = (char *)ENVPTR->GetByteArrayElements(ENVPAR palette,&bb);
    }
    file =(char *) ENVPTR->GetStringUTFChars(ENVPAR outfile,0);

    rval = DFUfptoimage((int32) hdim, (int32) vdim,
        (float32) max, (float32) min, (float32 *)hs, (float32 *)vs,
        (float32 *)arr, (uint8 *)pal,
        (char *)file, (int) ct_method,
        (int32) hres, (int32) vres,
        (int) compress);

    if (hs != NULL) {
        ENVPTR->ReleaseFloatArrayElements(ENVPAR hscale,hs,JNI_ABORT);
    }
    if (vs != NULL) {
        ENVPTR->ReleaseFloatArrayElements(ENVPAR vscale,vs,JNI_ABORT);
    }
    ENVPTR->ReleaseByteArrayElements(ENVPAR data,(jbyte *)arr,JNI_ABORT);
    if (pal != NULL) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR palette,(jbyte *)pal,JNI_ABORT);
    }
    ENVPTR->ReleaseStringUTFChars(ENVPAR outfile,arr);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
#else
    h4NotImplemented(ENVPAR  "DFUfptoimage (windows)");
    return JNI_TRUE;
#endif
}


#ifdef __cplusplus
}
#endif
