
#ifndef _WIN32
#define _WIN32
#endif

#include <assert.h>
#include "jni.h"
#include "h5Object.h"
#include "h5File.h"
#include "h5Dataset.h"
#include "clConnectExtern.h"


#define NODEBUG

#ifdef _WIN32
#pragma comment(lib,"ws2_32")
#endif

#define THROW_JNI_ERROR(_ex, _msg) { \
    (*env)->ThrowNew(env, (*env)->FindClass(env, _ex), _msg); \
     ret_val = -1; \
     goto done; \
}

#define GOTO_JNI_ERROR() { \
     ret_val = -1; \
     goto done; \
}

/* count of open files */
static int file_count = 0;

typedef struct srb_connection {
    srbConn *connection;
    char *host;
    char *port;
    char *password;
    char *user;
    char *domain;
} srb_connection;

typedef struct srb_connection_list {
    srb_connection connections[50];
    int count;      /* count of total connectins */
} srb_connection_list;

/* srb connection: (srbHost, srbPort, srbAuth, userName, domainName, authScheme, serverDn) */
srbConn *current_connection=NULL;
srb_connection_list connection_list;

srbConn *make_connection(JNIEnv *env, jobjectArray jsrb_info);
srbConn *find_connection(char *host);
void close_srb_connections();

jint h5file_request(JNIEnv *env, jobject jobj);
jint h5dataset_request(JNIEnv *env, jobject jobj);
jint j2c_h5file(JNIEnv *env, jobject jobj, H5File *cobj);
jint j2c_h5dataset(JNIEnv *env, jobject jdataset, H5Dataset *cobj);
jint c2j_h5file(JNIEnv *env, jobject jobj, H5File *cobj);
jint c2j_h5dataset(JNIEnv *env, jobject jdataset, H5Dataset *cobj);
jint c2j_h5group(JNIEnv *env, jobject jfile, jobject jgroup, H5Group *cgroup);

void load_field_method_IDs(JNIEnv *env);
void set_field_method_IDs_scalar();
void set_field_method_IDs_compound();

/* for debug purpose */
void print_file(H5File *file);
void print_group(srbConn *conn, const H5Group *pg);
void print_dataset(const H5Dataset *d);
void print_dataset_value(const H5Dataset *d);
void print_datatype(H5Datatype *type);
void print_dataspace(H5Dataspace *space);

/**
 * Caching in the Defining Class's Initializer
 * For details, read http://java.sun.com/docs/books/jni/html/fldmeth.html#26855
 */

jclass    cls_file=NULL;
jfieldID  fid_file_opID=NULL;
jfieldID  fid_file_fid=NULL;
jfieldID  fid_file_rootGroup=NULL;
jfieldID  fid_file_fullFileName=NULL;

jclass    cls_group=NULL;
jfieldID  fid_group_fid=NULL;
jfieldID  fid_group_fullPath=NULL;
jmethodID mid_group_ctr=NULL;
jmethodID mid_group_addToMemberList=NULL;

jclass    cls_dataset=NULL;
jfieldID  fid_dataset_opID=NULL;
jfieldID  fid_dataset_fid=NULL;
jfieldID  fid_dataset_fullPath=NULL;
jfieldID  fid_dataset_rank=NULL;
jfieldID  fid_dataset_dims=NULL;
jfieldID  fid_dataset_selectedDims=NULL;
jfieldID  fid_dataset_startDims=NULL;
jfieldID  fid_dataset_selectedIndex=NULL;
jfieldID  fid_dataset_selectedStride=NULL;
jfieldID  fid_dataset_datatype=NULL;
jmethodID mid_dataset_ctr=NULL;
jmethodID mid_dataset_setData=NULL;
jmethodID mid_dataset_init=NULL;

jclass    cls_dataset_scalar=NULL;
jfieldID  fid_dataset_scalar_opID=NULL;
jfieldID  fid_dataset_scalar_fid=NULL;
jfieldID  fid_dataset_scalar_fullPath=NULL;
jfieldID  fid_dataset_scalar_rank=NULL;
jfieldID  fid_dataset_scalar_dims=NULL;
jfieldID  fid_dataset_scalar_selectedDims=NULL;
jfieldID  fid_dataset_scalar_startDims=NULL;
jfieldID  fid_dataset_scalar_selectedIndex=NULL;
jfieldID  fid_dataset_scalar_selectedStride=NULL;
jfieldID  fid_dataset_scalar_datatype=NULL;
jmethodID mid_dataset_scalar_ctr=NULL;
jmethodID mid_dataset_scalar_setData=NULL;
jmethodID mid_dataset_scalar_init=NULL;

/* unique for scalar dataset */
jmethodID mid_dataset_scalar_setPalette=NULL;

jclass    cls_dataset_compound=NULL;
jfieldID  fid_dataset_compound_opID=NULL;
jfieldID  fid_dataset_compound_fid=NULL;
jfieldID  fid_dataset_compound_fullPath=NULL;
jfieldID  fid_dataset_compound_rank=NULL;
jfieldID  fid_dataset_compound_dims=NULL;
jfieldID  fid_dataset_compound_selectedDims=NULL;
jfieldID  fid_dataset_compound_startDims=NULL;
jfieldID  fid_dataset_compound_selectedIndex=NULL;
jfieldID  fid_dataset_compound_selectedStride=NULL;
jfieldID  fid_dataset_compound_datatype=NULL;
jmethodID mid_dataset_compound_ctr=NULL;
jmethodID mid_dataset_compound_setData=NULL;
jmethodID mid_dataset_compound_init=NULL;

/* unique for compound */
jfieldID  fid_dataset_compound_memberNames=NULL;
jfieldID  fid_dataset_compound_memberTypes=NULL;
jmethodID mid_dataset_compound_setMemberCount=NULL;


jclass    cls_datatype=NULL;
jfieldID  fid_datatype_class=NULL;
jfieldID  fid_datatype_size=NULL;
jfieldID  fid_datatype_order=NULL;
jfieldID  fid_datatype_sign=NULL;
 

/*
 * Class:     ncsa_hdf_srb_h5srb_H5SRB
 * Method:    h5ObjRequest
 * Signature: (Ledu/sdsc/grid/io/srb/SRBAccount;Ljava/lang/Object;I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_srb_h5srb_H5SRB_h5ObjRequest
  (JNIEnv *env, jclass cls, jobjectArray jsrb_info, jobject jobj, jint obj_type)
{
    int ret_val=0, i=0;
    H5Object_t obj_id = -1;

    if ( (current_connection = make_connection(env, jsrb_info)) == NULL )
        THROW_JNI_ERROR("java/lang/RuntimeException", "Cannot make connection to the SrbMaster server");

    load_field_method_IDs(env);

    switch (obj_type) {
        case H5OBJECT_FILE:
            h5file_request(env, jobj);
            break;
        case H5OBJECT_DATASET:
            h5dataset_request(env, jobj);
            break;
        default:
            THROW_JNI_ERROR("java/lang/UnsupportedOperationException",
                     "Unsupported HDF5-SRB client request");
            break;
    } /* end of switch */

done:
    return ret_val;
}

/* process HDF5-SRB file request: 
 * 1) decode Java message to C, 
 * 2) send request to the server
 * 3) encode server result to Java
 */
jint h5file_request(JNIEnv *env, jobject jobj)
{
    jint ret_val = 0;
    H5File h5file;

    assert(connection);

    H5File_ctor(&h5file);

    if ( (ret_val = j2c_h5file (env, jobj, &h5file)) < 0)
        goto done;

    /* send the request to the server to process */
    if (h5ObjRequest(current_connection, &h5file, H5OBJECT_FILE) < 0)
        THROW_JNI_ERROR("java/lang/RuntimeException", "file h5ObjRequest() failed");

#ifdef DEBUG
    print_group(connection, h5file.root);fflush(stdout);
#endif

    if ( (ret_val = c2j_h5file (env, jobj, &h5file)) < 0)
        goto done;

    if (H5FILE_OP_OPEN == h5file.opID)
        file_count++;
    else
        file_count--;

done:

    if (file_count <=0 ) {
        close_srb_connections();
    }

    H5File_dtor(&h5file);

    return ret_val;
}

/* process HDF5-SRB file request: 
 * 1) decode Java message to C, 
 * 2) send request to the server
 * 3) encode server result to Java
 */
jint h5dataset_request(JNIEnv *env, jobject jobj)
{
    jint ret_val=0, i=0;
    unsigned int npoints=1;
    H5Dataset h5dataset;
    H5Dataset *d = &h5dataset;

    assert(connection);

    H5Dataset_ctor(&h5dataset);

    if ( (ret_val = j2c_h5dataset(env, jobj, &h5dataset)) < 0)
        goto done;

    /* send the request to the server to process */
    if (h5ObjRequest(current_connection, &h5dataset, H5OBJECT_DATASET) < 0)
        THROW_JNI_ERROR("java/lang/RuntimeException", "dataset h5ObjRequest() failed");

    if (h5dataset.value && h5dataset.space.npoints==0) {
        h5dataset.space.npoints = 1;
        for (i=0; i<h5dataset.space.rank; i++)
            h5dataset.space.npoints *= h5dataset.space.count[i];
    }
    

    if ( (ret_val = c2j_h5dataset (env, jobj, &h5dataset)) < 0)
        goto done;

done:

    H5Dataset_dtor(&h5dataset);

    return ret_val;
}

/* construct C H5File structure from Java H5SrbFile object */
jint j2c_h5file(JNIEnv *env, jobject jobj, H5File *cobj)
{
    jint ret_val = 0;
    jstring jstr;
    const char *cstr;
    char jni_name[] = "j2c_h5file";

    assert(cobj);

    cobj->opID = (*env)->GetIntField(env, jobj, fid_file_opID);
    cobj->fid = (*env)->GetIntField(env, jobj, fid_file_fid);

    if (H5FILE_OP_OPEN == cobj->opID) {
         jstr = (*env)->GetObjectField(env, jobj, fid_file_fullFileName);
         if (NULL == (cstr = (char *)(*env)->GetStringUTFChars(env, jstr, NULL)) )
            THROW_JNI_ERROR("java/lang/OutOfMemoryError", jni_name);

         cobj->filename = (char *)malloc(strlen(cstr)+1);
         strcpy(cobj->filename, cstr);
        (*env)->ReleaseStringUTFChars(env, jstr, cstr);
    }


done:
    return ret_val;
}

/* construct C H5Dataset structure from Java Dataset object */
jint j2c_h5dataset(JNIEnv *env, jobject jobj, H5Dataset *cobj)
{
    jint ret_val = 0;
    jstring jstr;
    const char *cstr;
    char jni_name[] = "j2c_h5dataset";
    jobject jtype;
    jlongArray ja;
    jlong *jptr;
    int i=0;

    assert(cobj);

    cobj->opID = (*env)->GetIntField(env, jobj, fid_dataset_opID);
    cobj->fid = (*env)->GetIntField(env, jobj, fid_dataset_fid);

    if (H5DATASET_OP_READ == cobj->opID)
    {
        /* set the full path */
        jstr = (*env)->GetObjectField(env, jobj, fid_dataset_fullPath);
        if (NULL == (cstr = (char *)(*env)->GetStringUTFChars(env, jstr, NULL)) )
            THROW_JNI_ERROR("java/lang/OutOfMemoryError", jni_name);
        cobj->fullpath = (char *)malloc(strlen(cstr)+1);
        strcpy(cobj->fullpath, cstr);
        (*env)->ReleaseStringUTFChars(env, jstr, cstr);

        /* set datatype information */
        jtype = (*env)->GetObjectField(env, jobj, fid_dataset_datatype);
        cobj->type.class = (H5Datatype_class_t) (*env)->GetIntField(env, jtype, fid_datatype_class);
        cobj->type.size = (unsigned int) (*env)->GetIntField(env, jtype, fid_datatype_size);
        /*cobj->type.order = (H5Datatype_order_t) (*env)->GetIntField(env, jtype, fid_datatype_order);*/
        cobj->type.sign = (H5Datatype_sign_t) (*env)->GetIntField(env, jtype, fid_datatype_sign);
        cobj->type.order =get_machine_endian();

        /* set rank */
        cobj->space.rank = (int)(*env)->GetIntField(env, jobj, fid_dataset_rank);

        /* set dim information */
        ja = (*env)->GetObjectField(env, jobj, fid_dataset_dims);
        jptr = (*env)->GetLongArrayElements(env, ja, 0);
        if (jptr != NULL) {
            for (i=0; i<cobj->space.rank; i++) {
                cobj->space.dims[i] = (unsigned int)jptr[i];
               }
            (*env)->ReleaseLongArrayElements(env, ja, jptr, 0); 
        }

        /* set start information */
        ja = (*env)->GetObjectField(env, jobj, fid_dataset_startDims);
        jptr = (*env)->GetLongArrayElements(env, ja, 0);
        if (jptr != NULL) {
            for (i=0; i<cobj->space.rank; i++) {
                cobj->space.start[i] = (unsigned int)jptr[i];
            }
            (*env)->ReleaseLongArrayElements(env, ja, jptr, 0); 
        }


        /* set stride information */
        ja = (*env)->GetObjectField(env, jobj, fid_dataset_selectedStride);
        jptr = (*env)->GetLongArrayElements(env, ja, 0);
        if (jptr != NULL) {
            for (i=0; i<cobj->space.rank; i++)  {
                cobj->space.stride[i] = (unsigned int)jptr[i];
            }
            (*env)->ReleaseLongArrayElements(env, ja, jptr, 0); 
        }

        /* set stride information */
        ja = (*env)->GetObjectField(env, jobj, fid_dataset_selectedDims);
        jptr = (*env)->GetLongArrayElements(env, ja, 0);
        if (jptr != NULL) {
            for (i=0; i<cobj->space.rank; i++) {
                cobj->space.count[i] = (unsigned int)jptr[i];
            }
            (*env)->ReleaseLongArrayElements(env, ja, jptr, 0);
        }
    }

done:
    return ret_val;
}

/* construct Java H5SrbFile object from C H5File structure */
jint c2j_h5file(JNIEnv *env, jobject jobj, H5File *cobj)
{
    jint ret_val = 0;
    jobject jroot;
    char jni_name[] = "j2c_h5file";

    assert(cobj);

    if (H5FILE_OP_CLOSE == cobj->opID)
        goto  done;

    /* set file id */
    (*env)->SetIntField(env, jobj, fid_file_fid, (jint)cobj->fid);

    /* retrieve the root group */
    if (NULL == (jroot = (*env)->GetObjectField(env, jobj, fid_file_rootGroup)) )
        THROW_JNI_ERROR("java/lang/NoSuchFieldException", jni_name);

    if ( c2j_h5group(env, jobj, jroot, cobj->root) < 0)
      THROW_JNI_ERROR("java/lang/RuntimeException", jni_name);

done:
    return ret_val;
}

/* construct Java group object from C group structure */
jint c2j_h5group(JNIEnv *env, jobject jfile, jobject jgroup, H5Group *cgroup)
{
    jint ret_val = 0;
    char jni_name[] = "c2j_h5group";
    int i=0,j=0;
    jstring jpath;
    jlongArray joid;
    jlongArray jdims;
    H5Group *cg;
    H5Dataset *cd;
    jobject jg;
    jobject jd;
    jlong *jptr;

    if (NULL==jfile || NULL == jgroup || NULL == cgroup)
        THROW_JNI_ERROR("java/lang/NullPointerException", jni_name);

    if (cgroup->groups && cgroup->ngroups>0) {
        for (i=0; i<cgroup->ngroups; i++) {
            cg = &cgroup->groups[i];
            if (NULL == cg) continue;

            /* get full path */
            jpath = (*env)->NewStringUTF(env,cg->fullpath);

            /* get the oid */
            joid = (*env)->NewLongArray(env, 1);
            jptr = (*env)->GetLongArrayElements(env, joid, 0);
            jptr[0] = (jlong)cg->objID[0];
            (*env)->ReleaseLongArrayElements(env, joid, jptr, 0); 

            /* create a new group */
            jg = (*env)->NewObject(env, cls_group, mid_group_ctr, jfile, NULL, jpath, jgroup, joid);

            /* add the new group into its parant */
            (*env)->CallVoidMethod(env, jgroup, mid_group_addToMemberList, jg);

            /* recursively call c2j_h5group to contruct the subtree */
            c2j_h5group(env, jfile, jg, cg);
        }
    }

    if (cgroup->datasets && cgroup->ndatasets>0) {
        for (i=0; i<cgroup->ndatasets; i++) {
            cd = &cgroup->datasets[i];
            if (NULL == cd) continue;

            if (H5DATATYPE_COMPOUND == cd->type.class)
                set_field_method_IDs_compound();
            else
                set_field_method_IDs_scalar();            

            /* get full path */
            jpath = (*env)->NewStringUTF(env,cd->fullpath);

            /* get the oid */
            joid = (*env)->NewLongArray(env, 1);
            jptr = (*env)->GetLongArrayElements(env, joid, 0);
            jptr[0] = (jlong)cd->objID[0];
            (*env)->ReleaseLongArrayElements(env, joid, jptr, 0); 

            /* create a new dataset */
            jd = (*env)->NewObject(env, cls_dataset, mid_dataset_ctr, jfile, NULL, jpath, joid);

            /* for compound only */
            if (H5DATATYPE_COMPOUND == cd->type.class && cd->type.nmembers>0 && cd->type.mnames) {
                jobjectArray jmnames;
                jstring jname;
                jint *jiptr;
                jintArray jints;

                /* set the names of the compound fields (the order of the calls are very important */
                (*env)->CallVoidMethod(env, jd, mid_dataset_compound_setMemberCount, (jint)cd->type.nmembers);
                jmnames = (jobjectArray)(*env)->GetObjectField(env, jd, fid_dataset_compound_memberNames);
                jints = (jintArray)(*env)->GetObjectField(env, jd, fid_dataset_compound_memberTypes);
                jiptr = (*env)->GetIntArrayElements(env, jints, 0);
	            for (j=0; j<cd->type.nmembers; j++) {
                    jiptr[j] = cd->type.mtypes[j];
		            jname = (*env)->NewStringUTF(env, cd->type.mnames[j]);
		            (*env)->SetObjectArrayElement(env, jmnames, j, jname);
	            }
                (*env)->ReleaseIntArrayElements(env, jints, jiptr, 0); 
            }
            else if (cd->attributes && 
                strcmp(PALETTE_VALUE, (cd->attributes)[0].name)==0 &&
                (cd->attributes)[0].value)
            {
                jbyte *value;             
                jbyte *jbptr;
                jbyteArray jbytes;

                // setup palette
                value = (jbyte *)(cd->attributes)[0].value;
                jbytes = (*env)->NewByteArray(env, 768);
                jbptr = (*env)->GetByteArrayElements(env, jbytes, 0);
                for (j=0; j<768; j++) jbptr[j] = value[j];
                (*env)->CallVoidMethod(env, jd, mid_dataset_scalar_setPalette, jbytes);
                (*env)->ReleaseByteArrayElements(env, jbytes, jbptr, 0); 
            }

            /* get the dims */
            jdims = (*env)->NewLongArray(env, cd->space.rank);
            jptr = (*env)->GetLongArrayElements(env, jdims, 0);
            for (j=0; j<cd->space.rank; j++) jptr[j] = (jlong)cd->space.dims[j];
            (*env)->ReleaseLongArrayElements(env, jdims, jptr, 0);

            /* initialize the dataset */
            (*env)->CallVoidMethod(env, jd, mid_dataset_init, cd->space.rank, jdims, 
                cd->type.class, cd->type.size, cd->type.order, cd->type.sign);

            /* add the dataset into its parant */
            (*env)->CallVoidMethod(env, jgroup, mid_group_addToMemberList, jd);
        }
    }

done:

    return ret_val;
}

/* construct  Java Dataset object from C H5Dataset structure*/
jint c2j_h5dataset(JNIEnv *env, jobject jobj, H5Dataset *cobj)
{
    jint ret_val = 0;
    unsigned int i=0;
    char jni_name[] = "j2c_h5dataset";
    jstring jstr;
    char **strs;
    jobjectArray jobj_a;


    assert(cobj);
    
    if (NULL == cobj->value)
        GOTO_JNI_ERROR();

    switch (cobj->type.class) {
        case H5DATATYPE_INTEGER:
        case H5DATATYPE_REFERENCE:
            if (1 == cobj->type.size) {
                jbyte *ca = (jbyte *)cobj->value;
                jbyteArray ja = (*env)->NewByteArray(env, cobj->space.npoints);
                jbyte *jptr = (*env)->GetByteArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++) jptr[i] = ca[i];
                (*env)->ReleaseByteArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            else if (2 == cobj->type.size) {
                jshort *ca = (jshort *)cobj->value;
                jshortArray ja = (*env)->NewShortArray(env, cobj->space.npoints);
                jshort *jptr = (*env)->GetShortArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++) jptr[i] = ca[i];
                (*env)->ReleaseShortArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            else if (4 == cobj->type.size) {
                jint *ca = (jint *)cobj->value;
                jintArray ja = (*env)->NewIntArray(env, cobj->space.npoints);
                jint *jptr = (*env)->GetIntArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++)
                {
                    jptr[i] = ca[i];
                }
                (*env)->ReleaseIntArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            else {
                jlong *ca = (jlong *)cobj->value;
                jlongArray ja = (*env)->NewLongArray(env, cobj->space.npoints);
                jlong *jptr = (*env)->GetLongArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++) jptr[i] = ca[i];
                (*env)->ReleaseLongArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            break;
        case H5DATATYPE_FLOAT:
            if (4 == cobj->type.size) {
                jfloat *ca = (jfloat *)cobj->value;
                jfloatArray ja = (*env)->NewFloatArray(env, cobj->space.npoints);
                jfloat *jptr = (*env)->GetFloatArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++) jptr[i] = ca[i];
                (*env)->ReleaseFloatArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            else {
                jdouble *ca = (jdouble *)cobj->value;
                jdoubleArray ja = (*env)->NewDoubleArray(env, cobj->space.npoints);
                jdouble *jptr = (*env)->GetDoubleArrayElements(env, ja, 0);
                for (i=0; i<cobj->space.npoints; i++) jptr[i] = ca[i];
                (*env)->ReleaseDoubleArrayElements(env, ja, jptr, 0); 
                (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, ja);
            }
            break;
        case H5DATATYPE_STRING:
        case H5DATATYPE_VLEN:
        case H5DATATYPE_COMPOUND:
            strs = (char **)cobj->value;
            jobj_a = (*env)->NewObjectArray(env, cobj->space.npoints, 
                (*env)->FindClass(env, "java/lang/String"), (*env)->NewStringUTF(env, ""));
	        for (i=0; i<cobj->space.npoints; i++) {
		        jstr = (*env)->NewStringUTF(env, strs[i]);
		        (*env)->SetObjectArrayElement(env, jobj_a, i, jstr);
	        }; 
            (*env)->CallVoidMethod(env, jobj, mid_dataset_setData, jobj_a);
            break;
        default:
            break;
    }


done:
    return ret_val;
}

void load_field_method_IDs(JNIEnv *env) {
    jclass cls;

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbFile");
    if (cls != NULL)
    {
        cls_file = cls;
        fid_file_opID = (*env)->GetFieldID(env, cls, "opID", "I");
        fid_file_fid = (*env)->GetFieldID(env, cls, "fid", "I");
        fid_file_fullFileName = (*env)->GetFieldID(env, cls, "fullFileName", "Ljava/lang/String;");
        fid_file_rootGroup = (*env)->GetFieldID(env, cls, "rootGroup", "Lncsa/hdf/srb/obj/H5SrbGroup;");
    }

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbGroup");
    if (cls != NULL)
    {
        cls_group = cls;
        fid_group_fid = (*env)->GetFieldID(env, cls, "fid", "I");
        fid_group_fullPath = (*env)->GetFieldID(env, cls, "fullPath", "Ljava/lang/String;");
        mid_group_ctr = (*env)->GetMethodID(env, cls, "<init>", 
            "(Lncsa/hdf/object/FileFormat;Ljava/lang/String;Ljava/lang/String;Lncsa/hdf/object/Group;[J)V");
        mid_group_addToMemberList = (*env)->GetMethodID(env, cls, "addToMemberList", "(Lncsa/hdf/object/HObject;)V");
    }

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbScalarDS");
    if (cls != NULL)
    {
        cls_dataset_scalar = cls;

        fid_dataset_scalar_opID = (*env)->GetFieldID(env, cls, "opID", "I");
        fid_dataset_scalar_fid = (*env)->GetFieldID(env, cls, "fid", "I");
        fid_dataset_scalar_fullPath = (*env)->GetFieldID(env, cls, "fullPath", "Ljava/lang/String;");
        fid_dataset_scalar_rank = (*env)->GetFieldID(env, cls, "rank", "I");
        fid_dataset_scalar_dims = (*env)->GetFieldID(env, cls, "dims", "[J");
        fid_dataset_scalar_selectedDims = (*env)->GetFieldID(env, cls, "selectedDims", "[J");
        fid_dataset_scalar_startDims = (*env)->GetFieldID(env, cls, "startDims", "[J");
        fid_dataset_scalar_selectedIndex = (*env)->GetFieldID(env, cls, "selectedIndex", "[I");
        fid_dataset_scalar_selectedStride = (*env)->GetFieldID(env, cls, "selectedStride", "[J");
        fid_dataset_scalar_datatype = (*env)->GetFieldID(env, cls, "datatype", "Lncsa/hdf/object/Datatype;");

        mid_dataset_scalar_ctr = (*env)->GetMethodID(env, cls, "<init>", 
            "(Lncsa/hdf/object/FileFormat;Ljava/lang/String;Ljava/lang/String;[J)V");
        mid_dataset_scalar_setData = (*env)->GetMethodID(env, cls, "setData", "(Ljava/lang/Object;)V");
        mid_dataset_scalar_init = (*env)->GetMethodID(env, cls, "init", "(I[JIIII)V");

        /* unique for scalar dataset */
        mid_dataset_scalar_setPalette = (*env)->GetMethodID(env, cls, "setPalette", "([B)V"); 

    }

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbCompoundDS");
    if (cls != NULL)
    {
        cls_dataset_compound = cls;

        fid_dataset_compound_opID = (*env)->GetFieldID(env, cls, "opID", "I");
        fid_dataset_compound_fid = (*env)->GetFieldID(env, cls, "fid", "I");
        fid_dataset_compound_fullPath = (*env)->GetFieldID(env, cls, "fullPath", "Ljava/lang/String;");
        fid_dataset_compound_rank = (*env)->GetFieldID(env, cls, "rank", "I");
        fid_dataset_compound_dims = (*env)->GetFieldID(env, cls, "dims", "[J");
        fid_dataset_compound_selectedDims = (*env)->GetFieldID(env, cls, "selectedDims", "[J");
        fid_dataset_compound_startDims = (*env)->GetFieldID(env, cls, "startDims", "[J");
        fid_dataset_compound_selectedIndex = (*env)->GetFieldID(env, cls, "selectedIndex", "[I");
        fid_dataset_compound_selectedStride = (*env)->GetFieldID(env, cls, "selectedStride", "[J");
        fid_dataset_compound_datatype = (*env)->GetFieldID(env, cls, "datatype", "Lncsa/hdf/object/Datatype;");

        mid_dataset_compound_ctr = (*env)->GetMethodID(env, cls, "<init>", 
            "(Lncsa/hdf/object/FileFormat;Ljava/lang/String;Ljava/lang/String;[J)V");
        mid_dataset_compound_setData = (*env)->GetMethodID(env, cls, "setData", "(Ljava/lang/Object;)V");
        mid_dataset_compound_init = (*env)->GetMethodID(env, cls, "init", "(I[JIIII)V");

        /* unique for compound */
        fid_dataset_compound_memberNames = (*env)->GetFieldID(env, cls_dataset_compound, "memberNames", "[Ljava/lang/String;");
        fid_dataset_compound_memberTypes = (*env)->GetFieldID(env, cls_dataset_compound, "memberTypes", "[I");
        mid_dataset_compound_setMemberCount = (*env)->GetMethodID(env, cls_dataset_compound,"setMemberCount", "(I)V");

    }

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbDatatype");
    if (cls != NULL)
    {
        cls_datatype = cls;

        fid_datatype_class = (*env)->GetFieldID(env, cls, "datatypeClass", "I");
        fid_datatype_size = (*env)->GetFieldID(env, cls, "datatypeSize", "I");
        fid_datatype_order = (*env)->GetFieldID(env, cls, "datatypeOrder", "I");
        fid_datatype_sign = (*env)->GetFieldID(env, cls, "datatypeSign", "I");
    }
}

void set_field_method_IDs_scalar()
{
    cls_dataset = cls_dataset_scalar;
    fid_dataset_opID = fid_dataset_scalar_opID;
    fid_dataset_fid= fid_dataset_scalar_fid;
    fid_dataset_fullPath = fid_dataset_scalar_fullPath;
    fid_dataset_rank = fid_dataset_scalar_rank;
    fid_dataset_dims = fid_dataset_scalar_dims;
    fid_dataset_selectedDims = fid_dataset_scalar_selectedDims;
    fid_dataset_startDims = fid_dataset_scalar_startDims;
    fid_dataset_selectedIndex = fid_dataset_scalar_selectedIndex;
    fid_dataset_selectedStride = fid_dataset_scalar_selectedStride;
    fid_dataset_datatype = fid_dataset_scalar_datatype;
    mid_dataset_ctr = mid_dataset_scalar_ctr;
    mid_dataset_setData = mid_dataset_scalar_setData;
    mid_dataset_init = mid_dataset_scalar_init;
}

void set_field_method_IDs_compound()
{
    cls_dataset = cls_dataset_compound;
    fid_dataset_opID = fid_dataset_compound_opID;
    fid_dataset_fid= fid_dataset_compound_fid;
    fid_dataset_fullPath = fid_dataset_compound_fullPath;
    fid_dataset_rank = fid_dataset_compound_rank;
    fid_dataset_dims = fid_dataset_compound_dims;
    fid_dataset_selectedDims = fid_dataset_compound_selectedDims;
    fid_dataset_startDims = fid_dataset_compound_startDims;
    fid_dataset_selectedIndex = fid_dataset_compound_selectedIndex;
    fid_dataset_selectedStride = fid_dataset_compound_selectedStride;
    fid_dataset_datatype = fid_dataset_compound_datatype;
    mid_dataset_ctr = mid_dataset_compound_ctr;
    mid_dataset_setData = mid_dataset_compound_setData;
    mid_dataset_init = mid_dataset_compound_init;
}

srbConn *make_connection(JNIEnv *env, jobjectArray jsrb_info) {
    jstring jstr;
    char *cstr;
    srbConn *conn;
    srb_connection the_connection;

    memset(&the_connection, 0, sizeof(srb_connection));

    jstr = (jstring)(*env)->GetObjectArrayElement(env, jsrb_info, 0);
    cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    if ( (conn=find_connection(cstr)) ) {
        (*env)->ReleaseStringUTFChars(env,jstr,cstr);
        return conn;
    } 
    the_connection.host = (char *)malloc(strlen(cstr)+1);
    strcpy(the_connection.host, cstr);
    (*env)->ReleaseStringUTFChars(env,jstr,cstr);

    jstr = (jstring)(*env)->GetObjectArrayElement(env, jsrb_info, 1);
    cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    the_connection.port = (char *)malloc(strlen(cstr)+1);
    strcpy(the_connection.port, cstr);
    (*env)->ReleaseStringUTFChars(env,jstr,cstr);               
        
    jstr = (jstring)(*env)->GetObjectArrayElement(env, jsrb_info, 2);
    cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    the_connection.password = (char *)malloc(strlen(cstr)+1);
    strcpy(the_connection.password, cstr);
    (*env)->ReleaseStringUTFChars(env,jstr,cstr);               

    jstr = (jstring)(*env)->GetObjectArrayElement(env, jsrb_info, 3);
    cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    the_connection.user = (char *)malloc(strlen(cstr)+1);
    strcpy(the_connection.user, cstr);
    (*env)->ReleaseStringUTFChars(env,jstr,cstr); 
    
    jstr = (jstring)(*env)->GetObjectArrayElement(env, jsrb_info, 4);
    cstr = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    the_connection.domain = (char *)malloc(strlen(cstr)+1);
    strcpy(the_connection.domain, cstr);
    (*env)->ReleaseStringUTFChars(env,jstr,cstr); 

    conn = srbConnect (the_connection.host, the_connection.port, 
        the_connection.password, the_connection.user, the_connection.domain,
        NULL, NULL);

    if ( (NULL == conn) || (clStatus(conn) != CLI_CONNECTION_OK) ) {
        clFinish(conn);
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), 
            "Cannot make connection to srbMaster.");
        return NULL;
    }

    the_connection.connection = conn;
    connection_list.connections[connection_list.count++] = the_connection;
    
    return conn;
}

srbConn *find_connection(char *host) {
    int i=0;
    srbConn *conn;

    conn = NULL;
    for (i=0; i<connection_list.count; i++) {
        if (strcmp(host, connection_list.connections[i].host) == 0) {
            conn = connection_list.connections[i].connection;
            break;
        }
    }

    return conn;
}

void close_srb_connections() {
    int i=0;
    srb_connection the_connection;
    

    for (i=0; i<connection_list.count; i++) {
        the_connection = connection_list.connections[i];
        if (the_connection.host) free(the_connection.host);
        if (the_connection.port) free(the_connection.port);
        if (the_connection.password) free(the_connection.password);
        if (the_connection.user) free(the_connection.user);
        if (the_connection.domain) free(the_connection.domain);
        clFinish(the_connection.connection);
        memset(&the_connection, 0, sizeof(srb_connection));
    }
    connection_list.count = 0;
}


/**********************************************************************
 *                                                                    *
 *                       Debug Rountines                              *
 *                                                                    *
 **********************************************************************/

void print_file(H5File *file)
{
    assert(file);
    
    printf("\nFile name = %s\n", file->filename);
    printf("opID = %d\n", file->opID);
}

void print_group(srbConn *conn, const H5Group *pg)
{
    int i=0;
    H5Group *g=0;
    H5Dataset *d=0;

    assert(pg);

    for (i=0; i<pg->ngroups; i++)
    {
        g = (H5Group *) &pg->groups[i];
        printf("%s\n", g->fullpath);
        print_group(conn, g);
    }

    for (i=0; i<pg->ndatasets; i++)
    {
        d = (H5Dataset *) &pg->datasets[i];
        d->opID = H5DATASET_OP_READ;
        printf("%s\n", d->fullpath);

        if (h5ObjRequest(conn, d, H5OBJECT_DATASET) < 0) {
            fprintf (stderr, "H5DATASET_OP_READ failed\n");
            goto done;
        }

        print_dataset(d); /* print_dataset_value(d); */
    }

done:
    return;
}

void print_datatype(H5Datatype *type)
{
    int i = 0;

    assert(type);

    printf("\ntype class = %d\n", type->class);
    printf(  "type order = %d\n", type->order);
    printf(  "type sign  = %d\n", type->sign);
    printf(  "type size  = %d\n", type->size);

    printf(  "nmembers   = %d\n", type->nmembers);
    if (type->nmembers > 0 && type->mnames != NULL)
    {
        for (i=0; i<type->nmembers; i++)
            printf("member[%d] = %s\n", i, type->mnames[i]);
    }

}

void print_dataspace(H5Dataspace *space)
{
    int i = 0;

    assert (space);

    printf("\nspace rank = %d\n", space->rank);
    printf(  "space npoints = %d\n", space->npoints);

    if (space->rank > 0 && space->dims != NULL)
    {
        for (i=0; i<space->rank; i++) {
            printf("dims[%d] = %d\n", i, space->dims[i]);
	    }
    }

    if (space->rank > 0 && space->start != NULL)
    {
        for (i=0; i<space->rank; i++) {
            printf("start[%d] = %d\n", i, space->start[i]);
	    }
    }


    if (space->rank > 0 && space->stride != NULL)
    {
        for (i=0; i<space->rank; i++) {
            printf("stride[%d] = %d\n", i, space->stride[i]);
	    }
    }


    if (space->rank > 0 && space->count != NULL)
    {
        for (i=0; i<space->rank; i++) {
            printf("count[%d] = %d\n", i, space->count[i]);
	    }
    }
}

void print_dataset(const H5Dataset *d)
{
    assert(d);

    printf("\nDataset fullpath = %s\n", d->fullpath);
    printf("fid = %d\n", d->fid);
    print_datatype(&(d->type));
    print_dataspace(&(d->space));
    print_dataset_value(d);
}


void print_dataset_value(const H5Dataset *d)
{
    int size=0, i=0;
    char* pv;
    char **strs;

    assert(d);

    printf("\nThe total size of the value buffer = %d", d->nvalue);
    printf("\nPrinting the first 10 values of %s\n", d->fullpath);
    if (d->value)
    {
        size = 1;
        pv = (char*)d->value;
        for (i=0; i<d->space.rank; i++) size *= d->space.count[i];
        if (size > 10 ) size = 10; /* print only the first 10 values */
        if (d->type.class == H5DATATYPE_VLEN
            || d->type.class == H5DATATYPE_COMPOUND
            || d->type.class == H5DATATYPE_STRING)
            strs = (char **)d->value;

        for (i=0; i<size; i++)
        {
            if (d->type.class == H5DATATYPE_INTEGER)
            {
                if (d->type.sign == H5DATATYPE_SGN_NONE)
                {
                    if (d->type.size == 1)
                        printf("%u\t", *((unsigned char*)(pv+i)));
                    else if (d->type.size == 2)
                        printf("%u\t", *((unsigned short*)(pv+i*2)));
                    else if (d->type.size == 4)
                        printf("%u\t", *((unsigned int*)(pv+i*4)));
                    else if (d->type.size == 8)
                        printf("%u\t", *((unsigned long*)(pv+i*8)));
                }
                else
                {
                    if (d->type.size == 1)
                        printf("%d\t", *((char*)(pv+i)));
                    else if (d->type.size == 2)
                        printf("%d\t", *((short*)(pv+i*2)));
                    else if (d->type.size == 4)
                        printf("%d\t", *((int*)(pv+i*4)));
                    else if (d->type.size == 8)
                        printf("%d\t", *((long*)(pv+i*8)));
                }
            } else if (d->type.class == H5DATATYPE_FLOAT)
            {
                if (d->type.size == 4)
                    printf("%f\t", *((float *)(pv+i*4)));
                else if (d->type.size == 8)
                    printf("%f\t", *((double *)(pv+i*8)));
            } else if (d->type.class == H5DATATYPE_VLEN
                    || d->type.class == H5DATATYPE_COMPOUND)
            {
                if (strs[i])
                    printf("%s\t", strs[i]);
            } else if (d->type.class == H5DATATYPE_STRING)
            {
                if (strs[i])
                    printf("%s\t", strs[i]);
            }
        }
        printf("\n\n");
    }

    return;
}

