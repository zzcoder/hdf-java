
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

jint h5file_request(JNIEnv *env, jobject jobj);
jint h5dataset_request(JNIEnv *env, jobject jobj);
jint j2c_h5file(JNIEnv *env, jobject jobj, H5File *cobj);
jint j2c_h5dataset(JNIEnv *env, jobject jdataset, H5Dataset *cobj);
jint c2j_h5file(JNIEnv *env, jobject jobj, H5File *cobj);
jint c2j_h5dataset(JNIEnv *env, jobject jdataset, H5Dataset *cobj);
jint c2j_h5group(JNIEnv *env, jobject jfile, jobject jgroup, H5Group *cgroup);

void make_connection(JNIEnv *env);
void load_field_method_IDs(JNIEnv *env);

/* for debug purpose */
void print_file(H5File *file);
void print_group(srbConn *conn, const H5Group *pg);
void print_dataset(const H5Dataset *d);
void print_dataset_value(const H5Dataset *d);
void print_datatype(H5Datatype *type);
void print_dataspace(H5Dataspace *space);

/* srb connection: (srbHost, srbPort, srbAuth, userName, domainName, authScheme, serverDn) */
srbConn *connection=NULL;

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
  (JNIEnv *env, jclass cls, jobject jobj, jint obj_type)
{
    int ret_val = 0;
    H5Object_t obj_id = -1;

    if (!connection)
        make_connection(env);

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
    if (h5ObjRequest(connection, &h5file, H5OBJECT_FILE) < 0)
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

    if ( connection && 
        (clStatus(connection) == CLI_CONNECTION_OK) &&
         H5FILE_OP_CLOSE == h5file.opID &&
         file_count <=0 )
    {
        clFinish(connection);
        connection = NULL;
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
    if (h5ObjRequest(connection, &h5dataset, H5OBJECT_DATASET) < 0)
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

            /* get full path */
            jpath = (*env)->NewStringUTF(env,cd->fullpath);

            /* get the oid */
            joid = (*env)->NewLongArray(env, 1);
            jptr = (*env)->GetLongArrayElements(env, joid, 0);
            jptr[0] = (jlong)cd->objID[0];
            (*env)->ReleaseLongArrayElements(env, joid, jptr, 0); 

            /* create a new dataset */
            jd = (*env)->NewObject(env, cls_dataset, mid_dataset_ctr, jfile, NULL, jpath, joid);

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

    cls = (*env)->FindClass(env, "ncsa/hdf/srb/obj/H5SrbDataset");
    if (cls != NULL)
    {
        cls_dataset = cls;

        fid_dataset_opID = (*env)->GetFieldID(env, cls, "opID", "I");
        fid_dataset_fid = (*env)->GetFieldID(env, cls, "fid", "I");
        fid_dataset_fullPath = (*env)->GetFieldID(env, cls, "fullPath", "Ljava/lang/String;");
        fid_dataset_rank = (*env)->GetFieldID(env, cls, "rank", "I");
        fid_dataset_dims = (*env)->GetFieldID(env, cls, "dims", "[J");
        fid_dataset_selectedDims = (*env)->GetFieldID(env, cls, "selectedDims", "[J");
        fid_dataset_startDims = (*env)->GetFieldID(env, cls, "startDims", "[J");
        fid_dataset_selectedIndex = (*env)->GetFieldID(env, cls, "selectedIndex", "[I");
        fid_dataset_selectedStride = (*env)->GetFieldID(env, cls, "selectedStride", "[J");
        fid_dataset_datatype = (*env)->GetFieldID(env, cls, "datatype", "Lncsa/hdf/object/Datatype;");

        mid_dataset_ctr = (*env)->GetMethodID(env, cls, "<init>", 
            "(Lncsa/hdf/object/FileFormat;Ljava/lang/String;Ljava/lang/String;[J)V");
        mid_dataset_setData = (*env)->GetMethodID(env, cls, "setData", "(Ljava/lang/Object;)V");
        mid_dataset_init = (*env)->GetMethodID(env, cls, "init", "(I[JIIII)V");
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

void make_connection(JNIEnv *env) {

#ifdef _WIN32
    FILE *env_file = NULL;
    FILE *auth_file = NULL;
    char *userprofile = getenv ("USERPROFILE");
    char *pars[] = {NULL, NULL, NULL, NULL, NULL, NULL};
    char buf[MAX_PATH], line[MAX_PATH], key[MAX_PATH], val[MAX_PATH],
         srbHost[MAX_PATH], srbPort[MAX_PATH], srbAuth[MAX_PATH], 
         userName[MAX_PATH], domainName[MAX_PATH], authScheme[MAX_PATH];

    if (userprofile) {
        strcpy(buf, userprofile);
        strcat(buf, "/.srb/.");
        strcat(buf, CL_MDAS_ENV_FILENAME);
        env_file = fopen(buf, "r");

        strcpy(buf, userprofile);
        strcat(buf, "/.srb/.");
        strcat(buf, CL_MDAS_AUTH_FILENAME);
        auth_file = fopen(buf, "r");
    }

    /* the get the passwd from authenticatin file */
    if (auth_file) {
        char *eol = NULL;
        line[0] = '\0';
        while( fgets(line, MAX_PATH-1, auth_file) != NULL) {
            if ('#' == line[0] || strlen(line) <=2) /* comment or CR, LF */
                continue;
            else {
                strcpy(srbAuth, line);

                /* get rid of newline, LF or CR charactor */
                if ( (eol=strchr(srbAuth, '\n')) || 
                     (eol=strchr(srbAuth, 10)) || 
                     (eol=strchr(srbAuth, 13)) )
                    *eol = '\0';
                break;
            }
        }
    }


    /* set default port */
    strcpy(srbPort, "5544");
    srbHost[0]=userName[0]=domainName[0]=authScheme[0]='\0';
    /* get env. variables */
    if (env_file) {
        line[0] = '\0';
        while( fgets(line, MAX_PATH-1, env_file) != NULL) {
            if ('#' == line[0] || strlen(line) <=2) /* comment or CR, LF */
                continue;

            key[0] = val[0] = '\0';
            if (sscanf(line, "%s '%[^']'", key, val) <= 0)
                continue;

            if (!strcmp(key, HOST_KEYWORD))
                strcpy(srbHost, val);
            else if (!strcmp(key, PORT_KEYWORD))
                strcpy(srbPort, val);
            else if (!strcmp(key, USER_KEYWORD))
                strcpy(userName, val);
            else if (!strcmp(key, DOMAIN_KEYWORD))
                strcpy(domainName, val);
            else if (!strcmp(key, AUTH_KEYWD))
                strcpy(authScheme, val);
        }
    }

    if ( strlen(srbHost)>0 ) pars[0] = srbHost;
    if ( strlen(srbPort)>0 ) pars[1] = srbPort;
    if ( strlen(srbAuth)>0 ) pars[2] = srbAuth;
    if ( strlen(userName)>0 ) pars[3] = userName;
    if ( strlen(domainName)>0 ) pars[4] = domainName;
    if ( strlen(authScheme)>0 ) pars[5] = authScheme;
        
    connection = srbConnect (pars[0], pars[1], pars[2], pars[3], pars[4], pars[5], NULL);
#else
    /* make srb connection with default setting */
    /* (srbHost, srbPort, srbAuth, userName, domainName, authScheme, serverDn) */
    connection = srbConnect (NULL, NULL, NULL, NULL, NULL, NULL, NULL);
#endif


    if ( (NULL == connection) || (clStatus(connection) != CLI_CONNECTION_OK) ) {
        clFinish(connection);
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeException"), 
            "Cannot make connection to srbMaster.");
        return;
    }
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


