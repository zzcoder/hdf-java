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

#include <stdlib.h>
#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5G.h"
#include "h5util.h"

#ifdef __cplusplus
herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata);
herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type );
#else
static herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata);
static herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type );
#endif

typedef struct info_all
{
    char **objname;
    int *type;
    int count;
} info_all_t;

/*
 * Create a java object of hdf.h5.structs.H5G_info_t
 * public class H5G_info_t {
 *   public H5G_STORAGE_TYPE  storage_type; // Type of storage for links in group
 *   public long     nlinks;       // Number of links in group
 *   public long     max_corder;   // Current max. creation order value for group
 *   public int      mounted;      // Whether group has a file mounted on it
 * }
 *
 */
jobject create_H5G_info_t(JNIEnv *env, H5G_info_t group_info)
{
	jclass cls;
	jboolean jmounted;
	jobject storage_type, obj;
	jfieldID fid_storage_type, fid_nlinks, fid_max_corder, fid_mounted;

	cls = ENVPTR->FindClass(ENVPAR "hdf/h5/structs/H5G_info_t");
	if (cls == NULL) return NULL;

	obj = ENVPTR->AllocObject(ENVPAR cls);
	if (obj == NULL) return NULL;

	fid_storage_type = ENVPTR->GetFieldID(ENVPAR cls, "storage_type",
			"Lhdf/h5/enums/H5G_STORAGE_TYPE;");
	fid_nlinks = ENVPTR->GetFieldID(ENVPAR cls, "nlinks", "J");
	fid_max_corder = ENVPTR->GetFieldID(ENVPAR cls, "max_corder", "J");
	fid_mounted = ENVPTR->GetFieldID(ENVPAR cls, "mounted", "Z");

	if (fid_storage_type==NULL || fid_nlinks==NULL || fid_max_corder==NULL ||
			fid_mounted == NULL)
		return NULL;

	jmounted = (group_info.mounted==0) ? JNI_FALSE : JNI_TRUE;
	storage_type = get_enum_object(env,
			"hdf/h5/enums/H5G_STORAGE_TYPE",
			(jint)group_info.storage_type,
			"(I)Lhdf/h5/enums/H5G_STORAGE_TYPE;");
	if (storage_type == NULL) return NULL;

	ENVPTR->SetObjectField(ENVPAR obj, fid_storage_type, storage_type);
	ENVPTR->SetLongField(ENVPAR obj, fid_nlinks, (jlong)group_info.nlinks);
	ENVPTR->SetLongField(ENVPAR obj, fid_max_corder, (jlong)group_info.max_corder);
	ENVPTR->SetBooleanField(ENVPAR obj, fid_mounted, jmounted);

	return obj;
}

/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gcreate2
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gcreate2
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint lcpl_id, jint gcpl_id,
		  jint gapl_id)
{
    hid_t ret_val;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gcreate:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gcreate:  file name not pinned");
        return -1;
    }

    ret_val = H5Gcreate2((hid_t)loc_id, gName, (hid_t)lcpl_id, (hid_t)gcpl_id,
    		(hid_t)gapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jint)ret_val;
}



/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gcreate1
 * Signature: (ILjava/lang/String;J)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gcreate1
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jlong size_hint)
{
    hid_t ret_val;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gcreate:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gcreate:  file name not pinned");
        return -1;
    }

    ret_val = H5Gcreate1((hid_t)loc_id, gName, (size_t)size_hint );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jint)ret_val;
}

/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gcreate_anon
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gcreate_1anon
  (JNIEnv *env, jclass cls, jint loc_id, jint gcpl_id, jint gapl_id)
{
    hid_t ret_val;

    ret_val = H5Gcreate_anon((hid_t)loc_id, (hid_t)gcpl_id, (hid_t)gapl_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }
    return (jint)ret_val;
}


/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gopen2
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gopen2
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint gapl_id)
{
    hid_t ret_val;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gopen:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gopen:  file name not pinned");
        return -1;
    }

    ret_val = H5Gopen2((hid_t)loc_id, gName, (hid_t) gapl_id );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jint)ret_val;
}


/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gopen1
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gopen1
  (JNIEnv *env, jclass cls, jint loc_id, jstring name)
{
    hid_t ret_val;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gopen:  name is NULL");
        return -1;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gopen:  file name not pinned");
        return -1;
    }

    ret_val = H5Gopen1((hid_t)loc_id, gName );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);
    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jint)ret_val;
}


/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_H5G_H5Gget_1create_1plist
  (JNIEnv *env, jclass cls, jint loc_id)
{
    hid_t ret_val;

    ret_val = H5Gget_create_plist((hid_t)loc_id);

    if (ret_val < 0) {
        h5libraryError(env);
    }

    return (jint)ret_val;
}


/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gget_info
 * Signature: (I)Lhdf/h5/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5G_H5Gget_1info
  (JNIEnv *env, jclass cls, jint loc_id)
{
	H5G_info_t group_info;
	herr_t ret_val = -1;

	ret_val = H5Gget_info( (hid_t) loc_id, &group_info);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

	return create_H5G_info_t(env, group_info);
}


/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gget_info_by_name
 * Signature: (ILjava/lang/String;I)Lhdf/h5/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5G_H5Gget_1info_1by_1name
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jint lapl_id)
{
	H5G_info_t group_info;
	herr_t ret_val = -1;
    char* gName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Gget_info_by_name:  name is NULL");
        return NULL;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gget_info_by_name:  file name not pinned");
        return NULL;
    }

    ret_val = H5Gget_info_by_name((hid_t)loc_id, gName, &group_info, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

	return create_H5G_info_t(env, group_info);
}

/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gget_info_by_idx
 * Signature: (ILjava/lang/String;Lhdf/h5/enums/H5_INDEX;Lhdf/h5/enums/H5_ITER;JI)Lhdf/h5/structs/H5G_info_t;
 */
JNIEXPORT jobject JNICALL Java_hdf_h5_H5G_H5Gget_1info_1by_1idx
  (JNIEnv *env, jclass cls, jint loc_id, jstring name, jobject index_type,
		  jobject order, jlong n, jint lapl_id)
{
	H5G_info_t group_info;
	herr_t ret_val = -1;
    char* gName;
    jboolean isCopy;
    H5_index_t cindex_type = (H5_index_t)get_enum_value(env, index_type);
    H5_iter_order_t corder = (H5_iter_order_t)get_enum_value(env, order);

    if (name == NULL) {
        h5nullArgument( env, "H5Gget_info_by_idx:  name is NULL");
        return NULL;
    }

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (gName == NULL) {
        h5JNIFatalError( env, "H5Gget_info_by_idx:  file name not pinned");
        return NULL;
    }

    ret_val = H5Gget_info_by_idx((hid_t)loc_id, gName, cindex_type,
    		corder, (hsize_t)n, &group_info, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,gName);

    if (ret_val < 0) {
        h5libraryError(env);
        return NULL;
    }

	return create_H5G_info_t(env, group_info);
}

/*
 * Class:     hdf_h5_H5G
 * Method:    H5Gclose
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5G_H5Gclose
  (JNIEnv *env, jclass cls, jint loc_id)
{
    herr_t ret_val = 0;

    if (loc_id > 0)
        ret_val =  H5Gclose((hid_t)loc_id) ;

    if (ret_val < 0) {
        h5libraryError(env);
    }
}

/*
/////////////////////////////////////////////////////////////////////////////////
//
//
// Add these methods so that we don't need to call H5Gget_objtype_by_idx
// in a loop to get information for all the object in a group, which takes
// a lot of time to finish if the number of objects is more than 10,000
//
/////////////////////////////////////////////////////////////////////////////////
*/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Gget_obj_info_idx
 * Signature: (ILjava/lang/String;I[I[Ljava/lang/String;)I
 */
JNIEXPORT void JNICALL Java_hdf_h5_H5G_H5Gget_1obj_1info_1all
  (JNIEnv *env, jclass clss, jint loc_id, jstring group_name,
    jobjectArray objName, jintArray oType, jint n)
{
    herr_t ret_val;
    char *gName=NULL;
    char **oName=NULL;
    jboolean isCopy;
    jstring str;
    jint *tarr;
    int i;

    gName = (char *)ENVPTR->GetStringUTFChars(ENVPAR group_name,&isCopy);
    tarr = ENVPTR->GetIntArrayElements(ENVPAR oType,&isCopy);
    oName = (char **)malloc(n * sizeof (*oName));

    ret_val = H5Gget_obj_info_all( (hid_t) loc_id, gName,  oName, (int *)tarr );

    ENVPTR->ReleaseStringUTFChars(ENVPAR group_name,gName);

    if (ret_val < 0) {
        ENVPTR->ReleaseIntArrayElements(ENVPAR oType,tarr,JNI_ABORT);
        h5str_array_free(oName, n);
        h5libraryError(env);
        return;
    }

    ENVPTR->ReleaseIntArrayElements(ENVPAR oType,tarr,0);

    if (oName) {
        for (i=0; i<n; i++) {
            if (*(oName+i)) {
                str = ENVPTR->NewStringUTF(ENVPAR *(oName+i));
                ENVPTR->SetObjectArrayElement(ENVPAR objName,i,(jobject)str);
            }
        } /* for (i=0; i<n; i++)*/
    }

    h5str_array_free(oName, n);
}

herr_t H5Gget_obj_info_all( hid_t loc_id, char *group_name, char **objname, int *type )
{
    info_all_t info;
    info.objname = objname;
    info.type = type;
    info.count = 0;

    if(H5Giterate(loc_id, group_name, NULL, obj_info_all, (void *)&info)<0)
        return -1;

    return 0;
}

herr_t obj_info_all(hid_t loc_id, const char *name, void *opdata)
{
    int type = -1;
    info_all_t* info = (info_all_t*)opdata;

    if ( (type=H5Gget_objtype_by_idx(loc_id, info->count)) <0)
    {
        *(info->type+info->count) = -1;
        *(info->objname+info->count) = NULL;
    } else {
        *(info->type+info->count) = type;
        /* this will be freed by h5str_array_free(oName, n)*/
        *(info->objname+info->count) = (char *) malloc(strlen(name)+1);
        strcpy(*(info->objname+info->count), name);
    }
    info->count++;

    return 0;
}


#ifdef __cplusplus
}
#endif
