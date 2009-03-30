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
#define ENVPTR (env)
#define ENVPAR
#else
#define ENVPTR (*env)
#define ENVPAR env,
#endif

#define MAX_GR_NAME H4_MAX_GR_NAME

extern jboolean h4outOfMemory( JNIEnv *env, char *functName);
extern jboolean h4NotImplemented( JNIEnv *env, char *functName);
extern jboolean makeChunkInfo( JNIEnv *env, jobject chunkobj, int32 flgs, HDF_CHUNK_DEF *cinf);
extern jboolean getNewCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf);
extern jboolean setNewCompInfo( JNIEnv *env, jobject ciobj, comp_coder_t coder, comp_info *cinf);
extern jboolean getChunkInfo( JNIEnv *env, jobject chunkobj, HDF_CHUNK_DEF *cinf);


