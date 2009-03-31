/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class hdf_h5_H5A */

#ifndef _Included_hdf_h5_H5A
#define _Included_hdf_h5_H5A
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     hdf_h5_H5A
 * Method:    H5Acreate2
 * Signature: (ILjava/lang/String;IIII)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Acreate2
  (JNIEnv *, jclass, jint, jstring, jint, jint, jint, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Acreate_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;IIIII)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Acreate_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint, jint, jint, jint, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aopen
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aopen
  (JNIEnv *, jclass, jint, jstring, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aopen_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aopen_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aopen_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JII)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aopen_1by_1idx
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jint, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Awrite
 * Signature: (II[B)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Awrite
  (JNIEnv *, jclass, jint, jint, jbyteArray);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aread
 * Signature: (II[B)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Aread
  (JNIEnv *, jclass, jint, jint, jbyteArray);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5AreadVL
 * Signature: (II[Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5AreadVL
  (JNIEnv *, jclass, jint, jint, jobjectArray);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aclose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Aclose
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_space
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aget_1space
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aget_1type
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Aget_1create_1plist
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_name
 * Signature: (IJ[Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_hdf_h5_H5A_H5Aget_1name__IJ_3Ljava_lang_String_2
  (JNIEnv *, jclass, jint, jlong, jobjectArray);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_name
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_hdf_h5_H5A_H5Aget_1name__I
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_name_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;J[Ljava/lang/String;JI)J
 */
JNIEXPORT jlong JNICALL Java_hdf_h5_H5A_H5Aget_1name_1by_1idx__ILjava_lang_String_2Lhdf_h5_enums_H5_1INDEX_2Lhdf_h5_enums_H5_1ITER_2J_3Ljava_lang_String_2JI
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jobjectArray, jlong, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_name_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_hdf_h5_H5A_H5Aget_1name_1by_1idx__ILjava_lang_String_2Lhdf_h5_enums_H5_1INDEX_2Lhdf_h5_enums_H5_1ITER_2JI
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_storage_size
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_hdf_h5_H5A_H5Aget_1storage_1size
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_info
 * Signature: (I)Lhdf/h5/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5A_H5Aget_1info
  (JNIEnv *, jclass, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_info_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)Lhdf/h5/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5A_H5Aget_1info_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aget_info_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JI)Lhdf/h5/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5A_H5Aget_1info_1by_1idx
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Arename
 * Signature: (ILjava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Arename
  (JNIEnv *, jclass, jint, jstring, jstring);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Arename_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Arename_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jstring, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Adelete
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Adelete
  (JNIEnv *, jclass, jint, jstring);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Adelete_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Adelete_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Adelete_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JI)V
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5A_H5Adelete_1by_1idx
  (JNIEnv *, jclass, jint, jstring, jobject, jobject, jlong, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aexists
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_hdf_h5_H5A_H5Aexists
  (JNIEnv *, jclass, jint, jstring);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Aexists_by_name
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_hdf_h5_H5A_H5Aexists_1by_1name
  (JNIEnv *, jclass, jint, jstring, jstring, jint);

/*
 * Class:     hdf_h5_H5A
 * Method:    H5Acopy
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5A_H5Acopy
  (JNIEnv *, jclass, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
