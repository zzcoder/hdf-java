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
 *  Link Object API Functions of the HDF5 library.
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
#include "h5oImp.h"

#ifdef __cplusplus
#define ENVPTR (env)
#define ENVPAR 
#define CBENVPTR (cbenv)
#define CBENVPAR 
#define JVMPTR (jvm)
#define JVMPAR 
#define JVMPAR2 
#else
#define ENVPTR (*env)
#define ENVPAR env,
#define CBENVPTR (*cbenv)
#define CBENVPAR cbenv,
#define JVMPTR (*jvm)
#define JVMPAR jvm
#define JVMPAR2 jvm,
#endif
    
//    JavaVM *jvm;
//    jobject visit_callback;   

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    _H5Oopen
     * Signature: (ILjava/lang/String;I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Oopen
      (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_plist_id)
    {
        hid_t    status;
        char*    oName;
        jboolean isCopy;

        if (name == NULL) {
            h5nullArgument( env, "H5Oopen:  name is NULL");
            return -1;
        }

        oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

        if (oName == NULL) {
            h5JNIFatalError( env, "H5Oopen:  object name not pinned");
            return -1;
        }

        status = H5Oopen((hid_t)loc_id, oName, (hid_t)access_plist_id );

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, oName);
        if (status < 0) {
            h5libraryError(env);
        }
        return (jint)status;
    }

    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    _H5Oclose
     * Signature: (I)I
     */
    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Oclose
      (JNIEnv *env, jclass clss, jint object_id)
    {
        herr_t retVal =  H5Oclose((hid_t)object_id) ;

        if (retVal < 0) {
            h5libraryError(env);
        }

        return (jint)retVal;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Ocopy
     * Signature: (ILjava/lang/String;ILjava/lang/String;II)V
     */
    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ocopy
      (JNIEnv *env, jclass clss, jint cur_loc_id, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
    {
        char    *lCurName;
        char    *lDstName;
        jboolean isCopy;
        herr_t   status = -1;
        
        if (cur_name == NULL) {
            h5nullArgument(env, "H5Ocopy:  cur_name is NULL");
            return;
        }
        
        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
        if (lCurName == NULL) {
            h5JNIFatalError(env, "H5Ocopy:  cur_name not pinned");
            return;
        }
        
        if (dst_name == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5nullArgument(env, "H5Ocopy:  dst_name is NULL");
            return;
        }
        
        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
        if (lDstName == NULL) {
            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
            h5JNIFatalError(env, "H5Ocopy:  dst_name not pinned");
            return;
        }

        status = H5Ocopy((hid_t)cur_loc_id, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
        
        if (status < 0) {
           h5libraryError(env);
           return;
        }
        
        return;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Oget_info
     * Signature: (I)Lncsa/hdf/hdf5lib/structs/H5O_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Oget_1info
    (JNIEnv *env, jclass clss, jint loc_id)
    {
        herr_t      status;
        H5O_info_t  infobuf;
        jboolean    isCopy;
        jclass      cls;
        jmethodID   constructor;
        jvalue      args[12];
        jobject     hdrinfobuf;
        jobject     ihinfobuf1;
        jobject     ihinfobuf2;
        jobject     ret_info_t = NULL;

        status = H5Oget_info((hid_t)loc_id, (H5O_info_t*)&infobuf);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to the H5_hdr_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_hdr_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_hdr_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IIIIJJJJJJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_hdr_info_t failed\n");
           return NULL;
        }
        args[0].i = infobuf.hdr.version;
        args[1].i = infobuf.hdr.nmesgs;
        args[2].i = infobuf.hdr.nchunks;
        args[3].i = infobuf.hdr.flags;
        args[4].j = infobuf.hdr.space.total;
        args[5].j = infobuf.hdr.space.meta;
        args[6].j = infobuf.hdr.space.mesg;
        args[7].j = infobuf.hdr.space.free;
        args[8].j = infobuf.hdr.mesg.present;
        args[9].j = infobuf.hdr.mesg.shared;
        hdrinfobuf = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5_ih_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5_ih_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5_ih_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5_ih_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.meta_size.obj.index_size;
        args[1].j = infobuf.meta_size.obj.heap_size;
        ihinfobuf1 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
        args[0].j = infobuf.meta_size.attr.index_size;
        args[1].j = infobuf.meta_size.attr.heap_size;
        ihinfobuf2 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5O_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJIIJJJJJLncsa/hdf/hdf5lib/structs/H5O_hdr_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.fileno;
        args[1].j = infobuf.addr;
        args[2].i = infobuf.type;
        args[3].i = infobuf.rc;
        args[4].j = infobuf.num_attrs;
        args[5].j = infobuf.atime;
        args[6].j = infobuf.mtime;
        args[7].j = infobuf.ctime;
        args[8].j = infobuf.btime;
        args[9].l = hdrinfobuf;
        args[10].l = ihinfobuf1;
        args[11].l = ihinfobuf2;
        ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Oget_info_by_name
     * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5O_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Oget_1info_1by_1name
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint access_id)
    {
        char       *lName;
        herr_t      status;
        H5O_info_t  infobuf;
        jboolean    isCopy;
        jclass      cls;
        jmethodID   constructor;
        jvalue      args[12];
        jobject     hdrinfobuf;
        jobject     ihinfobuf1;
        jobject     ihinfobuf2;
        jobject     ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument(env, "H5Oget_info_by_name:  name is NULL");
            return NULL;
        }

        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Oget_info_by_name:  name not pinned");
            return NULL;
        }

        status = H5Oget_info_by_name((hid_t)loc_id, (const char*)lName, (H5O_info_t*)&infobuf, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to the H5_hdr_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_hdr_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_hdr_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IIIIJJJJJJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_hdr_info_t failed\n");
           return NULL;
        }
        args[0].i = infobuf.hdr.version;
        args[1].i = infobuf.hdr.nmesgs;
        args[2].i = infobuf.hdr.nchunks;
        args[3].i = infobuf.hdr.flags;
        args[4].j = infobuf.hdr.space.total;
        args[5].j = infobuf.hdr.space.meta;
        args[6].j = infobuf.hdr.space.mesg;
        args[7].j = infobuf.hdr.space.free;
        args[8].j = infobuf.hdr.mesg.present;
        args[9].j = infobuf.hdr.mesg.shared;
        hdrinfobuf = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5_ih_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5_ih_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5_ih_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5_ih_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.meta_size.obj.index_size;
        args[1].j = infobuf.meta_size.obj.heap_size;
        ihinfobuf1 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
        args[0].j = infobuf.meta_size.attr.index_size;
        args[1].j = infobuf.meta_size.attr.heap_size;
        ihinfobuf2 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5O_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJIIJJJJJLncsa/hdf/hdf5lib/structs/H5O_hdr_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.fileno;
        args[1].j = infobuf.addr;
        args[2].i = infobuf.type;
        args[3].i = infobuf.rc;
        args[4].j = infobuf.num_attrs;
        args[5].j = infobuf.atime;
        args[6].j = infobuf.mtime;
        args[7].j = infobuf.ctime;
        args[8].j = infobuf.btime;
        args[9].l = hdrinfobuf;
        args[10].l = ihinfobuf1;
        args[11].l = ihinfobuf2;
        ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        return ret_info_t;
    }
    
    /*
     * Class:     ncsa_hdf_hdf5lib_H5
     * Method:    H5Oget_info_by_idx
     * Signature: (ILjava/lang/String;IIJI)Lncsa/hdf/hdf5lib/structs/H5O_info_t;
     */
    JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Oget_1info_1by_1idx
    (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint index_field, jint order, jlong link_n, jint access_id)
    {
        char       *lName;
        herr_t      status;
        H5O_info_t  infobuf;
        jboolean    isCopy;
        jclass      cls;
        jmethodID   constructor;
        jvalue      args[12];
        jobject     hdrinfobuf;
        jobject     ihinfobuf1;
        jobject     ihinfobuf2;
        jobject     ret_info_t = NULL;

        if (name == NULL) {
            h5nullArgument(env, "H5Oget_info_by_idx:  name is NULL");
            return NULL;
        }
        
        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
        if (lName == NULL) {
            h5JNIFatalError(env, "H5Oget_info_by_idx:  name not pinned");
            return NULL;
        }

        status = H5Oget_info_by_idx((hid_t)loc_id, (const char*)lName, (H5_index_t)index_field, (H5_iter_order_t)order, (hsize_t)link_n, (H5O_info_t*)&infobuf, (hid_t)access_id);

        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);

        if (status < 0) {
           h5libraryError(env);
           return NULL;
        }

        // get a reference to the H5_hdr_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_hdr_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_hdr_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(IIIIJJJJJJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_hdr_info_t failed\n");
           return NULL;
        }
        args[0].i = infobuf.hdr.version;
        args[1].i = infobuf.hdr.nmesgs;
        args[2].i = infobuf.hdr.nchunks;
        args[3].i = infobuf.hdr.flags;
        args[4].j = infobuf.hdr.space.total;
        args[5].j = infobuf.hdr.space.meta;
        args[6].j = infobuf.hdr.space.mesg;
        args[7].j = infobuf.hdr.space.free;
        args[8].j = infobuf.hdr.mesg.present;
        args[9].j = infobuf.hdr.mesg.shared;
        hdrinfobuf = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5_ih_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5_ih_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5_ih_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJ)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5_ih_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.meta_size.obj.index_size;
        args[1].j = infobuf.meta_size.obj.heap_size;
        ihinfobuf1 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
        args[0].j = infobuf.meta_size.attr.index_size;
        args[1].j = infobuf.meta_size.attr.heap_size;
        ihinfobuf2 = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        // get a reference to the H5O_info_t class
        cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5O_info_t");
        if (cls == 0) {
           h5JNIFatalError( env, "JNI error: GetObjectClass H5O_info_t failed\n");
           return NULL;
        }
        // get a reference to the constructor; the name is <init>
        constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(JJIIJJJJJLncsa/hdf/hdf5lib/structs/H5O_hdr_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;Lncsa/hdf/hdf5lib/structs/H5_ih_info_t;)V");
        if (constructor == 0) {
            h5JNIFatalError( env, "JNI error: GetMethodID H5O_info_t failed\n");
           return NULL;
        }
        args[0].j = infobuf.fileno;
        args[1].j = infobuf.addr;
        args[2].i = infobuf.type;
        args[3].i = infobuf.rc;
        args[4].j = infobuf.num_attrs;
        args[5].j = infobuf.atime;
        args[6].j = infobuf.mtime;
        args[7].j = infobuf.ctime;
        args[8].j = infobuf.btime;
        args[9].l = hdrinfobuf;
        args[10].l = ihinfobuf1;
        args[11].l = ihinfobuf2;
        ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);

        return ret_info_t;
    }
//    
//    /*
//     * Class:     ncsa_hdf_hdf5lib_H5
//     * Method:    H5Olink
//     * Signature: (ILjava/lang/String;ILjava/lang/String;II)V
//     */
//    JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Olink
//      (JNIEnv *env, jclass clss, jint cur_loc_id, jstring cur_name, jint dst_loc_id, jstring dst_name, jint create_id, jint access_id)
//    {
//        char    *lCurName;
//        char    *lDstName;
//        jboolean isCopy;
//        herr_t   status = -1;
//        
//        if (cur_name == NULL) {
//            h5nullArgument(env, "H5Ocreate_hard:  cur_name is NULL");
//            return;
//        }
//        
//        lCurName = (char*)ENVPTR->GetStringUTFChars(ENVPAR cur_name, &isCopy);
//        if (lCurName == NULL) {
//            h5JNIFatalError(env, "H5Ocreate_hard:  cur_name not pinned");
//            return;
//        }
//        
//        if (dst_name == NULL) {
//            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
//            h5nullArgument(env, "H5Ocreate_hard:  dst_name is NULL");
//            return;
//        }
//        
//        lDstName = (char*)ENVPTR->GetStringUTFChars(ENVPAR dst_name, &isCopy);
//        if (lDstName == NULL) {
//            ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
//            h5JNIFatalError( env, "H5Ocreate_hard:  dst_name not pinned");
//            return;
//        }
//
//        status = H5Olink((hid_t)cur_loc_id, (const char*)lCurName, (hid_t)dst_loc_id, (const char*)lDstName, (hid_t)create_id, (hid_t)access_id);
//
//        ENVPTR->ReleaseStringUTFChars(ENVPAR cur_name, lCurName);
//        ENVPTR->ReleaseStringUTFChars(ENVPAR dst_name, lDstName);
//        
//        if (status < 0) {
//           h5libraryError(env);
//           return;
//        }
//        
//        return;
//    }
//
//    herr_t H5O_iterate_cb(hid_t g_id, const char *name, const H5O_info_t *info, void *op_data) {
//        JNIEnv    *cbenv;
//        jint       status;
//        jclass     cls;
//        jmethodID  mid;
//        jstring    str;
//        jmethodID  constructor;
//        jvalue     args[5];
//        jobject    cb_info_t = NULL;
//
//        if(JVMPTR->AttachCurrentThread(JVMPAR2 (void**)&cbenv, NULL) != 0) {
//            /* printf("JNI H5O_iterate_cb error: AttachCurrentThread failed\n"); */
//            JVMPTR->DetachCurrentThread(JVMPAR);
//            return -1;
//        }
//        cls = CBENVPTR->GetObjectClass(CBENVPAR visit_callback);
//        if (cls == 0) {
//            /* printf("JNI H5O_iterate_cb error: GetObjectClass failed\n"); */
//           JVMPTR->DetachCurrentThread(JVMPAR);
//           return -1;
//        }
//        mid = CBENVPTR->GetMethodID(CBENVPAR cls, "callback", "(ILjava/lang/String;Lncsa/hdf/hdf5lib/structs/H5O_info_t;Lncsa/hdf/hdf5lib/callbacks/H5O_iterate_t;)I");
//        if (mid == 0) {
//            /* printf("JNI H5O_iterate_cb error: GetMethodID failed\n"); */
//            JVMPTR->DetachCurrentThread(JVMPAR);
//            return -1;
//        }
//        str = CBENVPTR->NewStringUTF(CBENVPAR name);
//
//        // get a reference to your class if you don't have it already
//        cls = CBENVPTR->FindClass(CBENVPAR "ncsa/hdf/hdf5lib/structs/H5O_info_t");
//        if (cls == 0) {
//            /* printf("JNI H5O_iterate_cb error: GetObjectClass info failed\n"); */
//           JVMPTR->DetachCurrentThread(JVMPAR);
//           return -1;
//        }
//        // get a reference to the constructor; the name is <init>
//        constructor = CBENVPTR->GetMethodID(CBENVPAR cls, "<init>", "(IZJIJ)V");
//        if (constructor == 0) {
//            /* printf("JNI H5O_iterate_cb error: GetMethodID constructor failed\n"); */
//            JVMPTR->DetachCurrentThread(JVMPAR);
//            return -1;
//        }
//        args[0].i = info->type;
//        args[1].z = info->corder_valid;
//        args[2].j = info->corder;
//        args[3].i = info->cset;
//        if(info->type==0)
//            args[4].j = info->u.address;
//        else
//            args[4].j = info->u.val_size;
//        cb_info_t = CBENVPTR->NewObjectA(CBENVPAR cls, constructor, args);
//
//        status = CBENVPTR->CallIntMethod(CBENVPAR visit_callback, mid, g_id, str, cb_info_t, op_data);
//
//        JVMPTR->DetachCurrentThread(JVMPAR);
//        return status;
//    }
//    
//    /*
//     * Class:     ncsa_hdf_hdf5lib_H5
//     * Method:    H5Ovisit
//     * Signature: (IIILjava/lang/Object;Ljava/lang/Object;)I
//     */
//    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ovisit
//      (JNIEnv *env, jclass clss, jint grp_id, jint idx_type, jint order,
//              jobject callback_op, jobject op_data)
//    {
//        jboolean      isCopy;
//        herr_t        status = -1;
//        
//        ENVPTR->GetJavaVM(ENVPAR &jvm);
//        visit_callback = callback_op;
//
//        if (op_data == NULL) {
//            h5nullArgument(env, "H5Ovisit:  op_data is NULL");
//            return -1;
//        }
//        if (callback_op == NULL) {
//            h5nullArgument(env, "H5Ovisit:  callback_op is NULL");
//            return -1;
//        }
//        
//        status = H5Ovisit((hid_t)grp_id, (H5_index_t)idx_type, (H5_iter_order_t)order, (H5O_iterate_t)H5O_iterate_cb, (void*)op_data);
//        
//        if (status < 0) {
//           h5libraryError(env);
//           return status;
//        }
//        
//        return status;
//    }
//    
//    /*
//     * Class:     ncsa_hdf_hdf5lib_H5
//     * Method:    H5Ovisit_by_name
//     * Signature: (ILjava/lang/String;IILjava/lang/Object;Ljava/lang/Object;I)I
//     */
//    JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Ovisit_1by_1name
//      (JNIEnv *env, jclass clss, jint grp_id, jstring name, jint idx_type, jint order,
//              jobject callback_op, jobject op_data, jint access_id)
//    {
//        jboolean      isCopy;
//        char         *lName;
//        herr_t        status = -1;
//        
//        ENVPTR->GetJavaVM(ENVPAR &jvm);
//        visit_callback = callback_op;
//        
//        if (name == NULL) {
//            h5nullArgument(env, "H5Ovisit_by_name:  name is NULL");
//            return NULL;
//        }
//        
//        lName = (char*)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);
//        if (lName == NULL) {
//            h5JNIFatalError(env, "H5Ovisit_by_name:  name not pinned");
//            return NULL;
//        }
//
//        if (op_data == NULL) {
//            h5nullArgument(env, "H5Ovisit_by_name:  op_data is NULL");
//            return -1;
//        }
//        if (callback_op == NULL) {
//            h5nullArgument(env, "H5Ovisit_by_name:  callback_op is NULL");
//            return -1;
//        }
//        
//        status = H5Ovisit_by_name((hid_t)grp_id, (const char*)lName, (H5_index_t)idx_type, (H5_iter_order_t)order, (H5O_iterate_t)H5O_iterate_cb, (void*)op_data, (hid_t)access_id);
//
//        ENVPTR->ReleaseStringUTFChars(ENVPAR name, lName);
//        
//        if (status < 0) {
//           h5libraryError(env);
//           return status;
//        }
//        
//        return status;
//    }


#ifdef __cplusplus
}
#endif
