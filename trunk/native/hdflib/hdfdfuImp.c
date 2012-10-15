
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
    h4NotImplemented(env, "DFUfptoimage (windows)");
    return JNI_TRUE;
}


#ifdef __cplusplus
}
#endif
