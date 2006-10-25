
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
#include "mfhdf.h"
#include "jni.h"

extern jboolean h4outOfMemory( JNIEnv *env, char *functName);
extern jboolean makeChunkInfo( JNIEnv *env, jobject chunkobj, int32 flgs, HDF_CHUNK_DEF *cinf);
extern jboolean getNewCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf);
extern jboolean setNewCompInfo( JNIEnv *env, jobject ciobj, comp_coder_t coder, comp_info *cinf);
extern jboolean getChunkInfo( JNIEnv *env, jobject chunkobj, HDF_CHUNK_DEF *cinf);



JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRstart
( JNIEnv *env,
jclass cls,
jint file_id)
{
    return (GRstart((int32) file_id));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRend
( JNIEnv *env,
jclass cls,
jint gr_id)
{
    intn rval;

    rval = GRend((int32) gr_id);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRendaccess
( JNIEnv *env,
jclass cls,
jint gr_id)
{
    intn rval;
    rval =  GRendaccess((int32) gr_id);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRfileinfo
( JNIEnv *env,
jclass cls,
jint gr_id,
jintArray argv)  /* OUT:  n_datasets, n_fileattrs */
{
    intn rval;
    jint *theArgs;
    jboolean bb; /*  dummy */

    theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

    rval = GRfileinfo((int32) gr_id, (int32 *)&(theArgs[0]),
        (int32 *)&(theArgs[1]));

    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRselect
( JNIEnv *env,
jclass cls,
jint gr_id,
jint index)
{
    return (GRselect((int32) gr_id, (int32) index));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRnametoindex
( JNIEnv *env,
jclass cls,
jint gr_id,
jstring gr_name)
{
    int32 rval;
    char *str;

    str =(char *) (*env)->GetStringUTFChars(env,gr_name,0);

    rval = GRnametoindex((int32) gr_id, (char *)str);

    (*env)->ReleaseStringUTFChars(env,gr_name,str);

    return rval;
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetchunkinfo
( JNIEnv *env,
jclass cls,
jint grsid,
jobject chunk_def, /* Out: HDFChunkInfo */
jintArray cflags)              /* OUT: Integer */
{
int32 rval;
HDF_CHUNK_DEF cdef;
jboolean stat;
jint *flgs;
jboolean bb;

/*
    bzero((char *)&cdef, sizeof(cdef));
*/
    flgs = (*env)->GetIntArrayElements(env,cflags,&bb);
    rval = GRgetchunkinfo( (int32)grsid, &cdef, (int32 *)&(flgs[0]));

    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,cflags,(jint *)flgs,JNI_ABORT);
        return JNI_FALSE;
    } else {
        stat = JNI_TRUE;
        if (*flgs != 0) {
        /* convert cdef to HDFchunkinfo */
            stat = makeChunkInfo( env, chunk_def, *flgs, &cdef);
        }
        (*env)->ReleaseIntArrayElements(env,cflags,(jint *)flgs,0);
        return stat/*JNI_TRUE*/;
    }

}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetiminfo
( JNIEnv *env,
jclass cls,
jint ri_id,
jarray gr_name,  /* OUT: String[0] */
jintArray argv,   /* OUT: int ncomp, data_type, interlace, num_attrs */
jintArray dim_sizes) /* OUT: int[2] */
{
    intn rval;
    jint * dims;
    jint * theArgs;
    jclass Sjc;
    char *str;
    jstring rstring;
    jboolean bb;
    jobject o;

    str = HDmalloc(MAX_GR_NAME+1);
    if (str == NULL) {
        h4outOfMemory(env,"GRgetiminfo");
        return JNI_FALSE;
    }
    /* check for out of memory error ... */

    dims = (*env)->GetIntArrayElements(env,dim_sizes,&bb);
    theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

    rval = GRgetiminfo((int32) ri_id, (char *)str, (int32 *)&(theArgs[0]),
        (int32 *)&(theArgs[1]), (int32 *)&(theArgs[2]), (int32 *)dims,
        (int32 *)&(theArgs[3]));

    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,dim_sizes,dims,JNI_ABORT);
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
        if (str != NULL) HDfree(str);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseIntArrayElements(env,dim_sizes,dims,0);
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,0);
        if (str != NULL) {
            str[MAX_GR_NAME] = '\0';
        rstring = (*env)->NewStringUTF(env, str);
        o = (*env)->GetObjectArrayElement(env,gr_name,0);
        if (o == NULL) {
            return JNI_FALSE;
        }
        Sjc = (*env)->FindClass(env, "java/lang/String");
        if (Sjc == NULL) {
            return JNI_FALSE;
        }
        bb = (*env)->IsInstanceOf(env,o,Sjc);
        if (bb == JNI_FALSE) {
            return JNI_FALSE;
        }
        (*env)->SetObjectArrayElement(env,gr_name,0,(jobject)rstring);

        HDfree(str);
        }
        return JNI_TRUE;
    }
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreadimage
( JNIEnv *env,
jclass cls,
jint ri_id,
jintArray start, /* IN: int[2] */
jintArray stride, /* IN: int[2] */
jintArray edge, /* IN: int[2] */
jbyteArray data)  /* OUT: byte[] */
{
    intn rval;
    jbyte *arr;
    jint *strt;
    jint *strd;
    jint *edg;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,data,&bb);
    strt = (*env)->GetIntArrayElements(env,start,&bb);
    if (stride == NULL) {
        strd = NULL;
    } else {
        strd = (*env)->GetIntArrayElements(env,stride,&bb);
    }
    edg = (*env)->GetIntArrayElements(env,edge,&bb);

    if (strd == NULL) {
        rval = GRreadimage((int32) ri_id, (int32 *)strt, (int32 *)NULL,
            (int32 *)edg, (VOIDP) arr);
    } else {
        rval = GRreadimage((int32) ri_id, (int32 *)strt, (int32 *)strd,
            (int32 *)edg, (VOIDP) arr);
    }

    (*env)->ReleaseIntArrayElements(env,start,strt,JNI_ABORT);
    if (stride != NULL) {
        (*env)->ReleaseIntArrayElements(env,stride,strd,JNI_ABORT);
    }
    (*env)->ReleaseIntArrayElements(env,edge,edg,JNI_ABORT);

    if (rval == FAIL) {
        (*env)->ReleaseByteArrayElements(env,data,arr,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseByteArrayElements(env,data,arr,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jshort JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRidtoref
( JNIEnv *env,
jclass cls,
jint gr_id)
{
    return (GRidtoref((int32) gr_id));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreftoindex
( JNIEnv *env,
jclass cls,
jint gr_id,
jshort ref)
{
    return (GRreftoindex((int32) gr_id, (uint16)ref));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreqlutil
( JNIEnv *env,
jclass cls,
jint gr_id,
jint interlace_mode)
{
    intn rval;
    rval =  GRreqlutil((int32) gr_id, (intn)interlace_mode);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreqimageil
( JNIEnv *env,
jclass cls,
jint gr_id,
jint interlace_mode)
{
    intn rval;
    rval =  GRreqimageil((int32) gr_id, (intn)interlace_mode);
    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetlutid
( JNIEnv *env,
jclass cls,
jint gr_id,
jint index)
{
    return (GRgetlutid((int32) gr_id, (int32)index));
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetnluts
( JNIEnv *env,
jclass cls,
jint gr_id)
{
    return (GRgetnluts((int32) gr_id));
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetlutinfo
( JNIEnv *env,
jclass cls,
jint ri_id,
jintArray argv) /* OUT:  ncomp, data_type, interlace, num_entries */
{
    intn rval;
    jint * theArgs;
    jboolean bb; /* dummy */

    theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

    rval = GRgetlutinfo((int32) ri_id, (int32 *)&(theArgs[0]),
        (int32 *)&(theArgs[1]), (int32 *)&(theArgs[2]),
        (int32 *)&(theArgs[3]));


    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,0);
        return JNI_TRUE;
    }
}



JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreadlut
( JNIEnv *env,
jclass cls,
jint pal_id,
jbyteArray pal_data)  /* OUT: byte[] */
{
    intn rval;
    jbyte *arr;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,pal_data,&bb);

    rval = GRreadlut((int32) pal_id, (VOIDP) arr);

    if (rval == FAIL) {
        (*env)->ReleaseByteArrayElements(env,pal_data,arr,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseByteArrayElements(env,pal_data,arr,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRattrinfo
( JNIEnv *env,
jclass cls,
int gr_id,
jint attr_index,
jarray name,   /* OUT: String[] */
jintArray argv) /* OUT:  data_type, length */
{
    int32 rval;
    char *str;
    jclass jc;
    jstring rstring;
    jint *theArgs;
    jboolean bb;
    jobject o;


    /* check for out of memory error ... */
    str = HDmalloc(MAX_GR_NAME+1);
    if (str == NULL) {
        /* exception */
        h4outOfMemory(env,"GRattrinfo");
        return JNI_FALSE;
    }

    theArgs = (*env)->GetIntArrayElements(env,argv,&bb);

    rval = GRattrinfo((int32) gr_id, (int32) attr_index,
        (char *)str, (int32 *)&(theArgs[0]), (int32 *)&(theArgs[1]));


    if (rval == FAIL) {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseIntArrayElements(env,argv,theArgs,0);
        if (str != NULL) {
        str[MAX_GR_NAME] = '\0';
        rstring = (*env)->NewStringUTF(env, str);
        o = (*env)->GetObjectArrayElement(env,name,0);
        if (o == NULL) {
            return JNI_FALSE;
        }
        jc = (*env)->FindClass(env, "java/lang/String");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        bb = (*env)->IsInstanceOf(env,o,jc);
        if (bb == JNI_FALSE) {
            return JNI_FALSE;
        }
        (*env)->SetObjectArrayElement(env,name,0,(jobject)rstring);
        HDfree(str);
        }
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetattr
( JNIEnv *env,
jclass cls,
jint gr_id,
jint attr_index,
jbyteArray values)  /* OUT: byte[] */
{
    intn rval;
    jbyte *arr;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,values,&bb);
    rval = GRgetattr((int32) gr_id, (int32) attr_index,  (VOIDP) arr);
    if (rval == FAIL) {
        (*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseByteArrayElements(env,values,arr,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRfindattr
( JNIEnv *env,
jclass cls,
jint gr_id,
jstring attr_name) /* IN */
{
    int32 rval;
    char *str;

    str =(char *) (*env)->GetStringUTFChars(env,attr_name,0);

    rval = GRfindattr((int32) gr_id, (char *)str);

    (*env)->ReleaseStringUTFChars(env,attr_name,str);

    return rval;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRcreate
( JNIEnv *env,
jclass cls,
jint gr_id,
jstring name,
jint ncomp,
jint data_type,
jint interlace_mode,
jbyteArray dim_sizes)  /* IN: int[2] */
{
    int32 rval;
    jint *dims;
    char *str;
    jboolean bb;

    dims = (*env)->GetIntArrayElements(env,dim_sizes,&bb);
    str =(char *) (*env)->GetStringUTFChars(env,name,0);

    rval = GRcreate( (int32) gr_id, (char *) str, (int32) ncomp,
        (int32) data_type, (int32) interlace_mode, (int32 *)dims);

    (*env)->ReleaseStringUTFChars(env,name,str);
    (*env)->ReleaseIntArrayElements(env,dim_sizes,dims,JNI_ABORT);

    return rval;
}


JNIEXPORT jshort JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRluttoref
( JNIEnv *env,
jclass cls,
jint pal_id)
{
    return (GRluttoref((int32) pal_id));
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetattr__ILjava_lang_String_2IILjava_lang_String_2
( JNIEnv *env,
jclass cls,
jint gr_id,
jstring attr_name,
jint data_type,
jint count,
jstring values)  /* IN: String */
{
    intn rval;
    char *str;
    char *val;

    str =(char *) (*env)->GetStringUTFChars(env,attr_name,0);
    val =(char *) (*env)->GetStringUTFChars(env,values,0);

    rval = GRsetattr((int32) gr_id, (char *)str, (int32) data_type,
        (int32) count, (VOIDP) val);

    (*env)->ReleaseStringUTFChars(env,attr_name,str);
    (*env)->ReleaseStringUTFChars(env,values,val);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetattr__ILjava_lang_String_2II_3B
( JNIEnv *env,
jclass cls,
jint gr_id,
jstring attr_name,
jint data_type,
jint count,
jbyteArray values)  /* IN: byte[] */
{
    intn rval;
    jbyte *arr;
    char *str;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,values,&bb);
    str =(char *) (*env)->GetStringUTFChars(env,attr_name,0);

    rval = GRsetattr((int32) gr_id, (char *)str, (int32) data_type,
        (int32) count, (VOIDP) arr);

    (*env)->ReleaseStringUTFChars(env,attr_name,str);
    (*env)->ReleaseByteArrayElements(env,values,arr,JNI_ABORT);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}


JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetcompress
( JNIEnv *env,
jclass cls,
jint ri_id,
jint comp_type,
jobject c_info) /* IN:  CompInfo */
{
    intn rval;
    comp_info cinf;
    jboolean bval;

    bval = getNewCompInfo(env,c_info,&cinf);

    /* check for success... */

    rval = GRsetcompress((int32) ri_id, (int32) comp_type, (comp_info *)&cinf);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRgetcompress
( JNIEnv *env,
jclass cls,
jint ri_id,
jobject c_info) /* out:  CompInfo */
{
    intn rval;
    comp_coder_t coder;
    comp_info cinf;
    jboolean bval;


    /* check for success... */

    rval = GRgetcompress((int32) ri_id, (comp_coder_t *) &coder, (comp_info *)&cinf);

    bval = setNewCompInfo(env,c_info,coder,&cinf);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetchunk
( JNIEnv *env,
jclass cls,
jint sdsid,
jobject chunk_def, /* IN: HDFChunkInfo */
jint flags)
{
intn rval;
HDF_CHUNK_DEF c_def;
jboolean bval;

/*
    bzero((char *)&c_def, sizeof(c_def));
*/

    bval = getChunkInfo(env,chunk_def,&c_def);

    /* check results */

    rval = SDsetchunk ((int32) sdsid, c_def, (int32) flags);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetchunkcache
( JNIEnv *env,
jclass cls,
jint sdsid,
jint maxcache,
jint flags)
{
    return ( SDsetchunkcache((int32)sdsid, (int32)maxcache, (int32)flags) );
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRsetexternalfile
( JNIEnv *env,
jclass cls,
jint ri_id,
jstring filename,
jint offset)
{
    int32 rval;
    char *str;

    str =(char *) (*env)->GetStringUTFChars(env,filename,0);

    rval = GRsetexternalfile((int32) ri_id, (char *)str, (int32) offset);

    (*env)->ReleaseStringUTFChars(env,filename,str);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRwriteimage
( JNIEnv *env,
jclass cls,
jint ri_id,
jintArray start,  /* IN: int[2] */
jintArray stride,  /* IN: int[2] */
jintArray edge,  /* IN: int[2] */
jbyteArray data)  /* IN: byte[] */
{
    intn rval;
    jbyte *arr;
    jint *strt;
    jint *strd;
    jint *edg;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,data,&bb);
    strt = (*env)->GetIntArrayElements(env,start,&bb);
    if (stride == NULL) {
        strd = NULL;
    } else {
        strd = (*env)->GetIntArrayElements(env,stride,&bb);
    }
    edg = (*env)->GetIntArrayElements(env,edge,&bb);

    if (strd == NULL) {
        rval = GRwriteimage((int32) ri_id, (int32 *)strt, (int32  *)NULL,
            (int32 *)edg, (VOIDP) arr);
    } else {
        rval = GRwriteimage((int32) ri_id, (int32 *)strt, (int32  *)strd,
            (int32 *)edg, (VOIDP) arr);
    }

    (*env)->ReleaseIntArrayElements(env,start,strt,JNI_ABORT);
    if (stride != NULL) {
        (*env)->ReleaseIntArrayElements(env,stride,strd,JNI_ABORT);
    }
    (*env)->ReleaseIntArrayElements(env,edge,edg,JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env,data,arr,JNI_ABORT);


    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRwritelut
( JNIEnv *env,
jclass cls,
jint pal_id,
jint ncomp,
jint data_type,
jint interlace,
jint num_entries,
jbyteArray pal_data)  /* IN:  byte[] */
{
    intn rval;
    jbyte *arr;
    jboolean bb;

    arr = (*env)->GetByteArrayElements(env,pal_data,&bb);

    rval = GRwritelut((int32) pal_id, (int32) ncomp, (int32) data_type,
        (int32) interlace, (int32) num_entries, (VOIDP) arr);

    (*env)->ReleaseByteArrayElements(env,pal_data,arr,JNI_ABORT);

    if (rval == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRreadchunk
( JNIEnv *env,
jclass cls,
jint grid,
jintArray origin, /* IN: int[] */
jbyteArray dat)  /* OUT: byte[] */
{
int32 retVal;
jbyte * s;
jint *arr;
    jboolean bb;

    arr = (*env)->GetIntArrayElements(env,origin,&bb);
    s = (*env)->GetByteArrayElements(env,dat,&bb);

    retVal = GRreadchunk((int32)grid,(int32 *)arr,s);

    (*env)->ReleaseIntArrayElements(env,origin,arr,JNI_ABORT);
    if (retVal == FAIL) {
        (*env)->ReleaseByteArrayElements(env,dat,s,JNI_ABORT);
        return JNI_FALSE;
    } else {
        (*env)->ReleaseByteArrayElements(env,dat,s,0);
        return JNI_TRUE;
    }
}

JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_GRwritechunk
( JNIEnv *env,
jclass cls,
jint grid,
jintArray origin, /* IN: int[] */
jbyteArray dat)  /* IN: byte[] */
{
int32 retVal;
jbyte * s;
jint * arr;
    jboolean bb;

    s = (*env)->GetByteArrayElements(env,dat,&bb);
    arr = (*env)->GetIntArrayElements(env,origin,&bb);

    retVal = GRwritechunk((int32)grid,(int32 *)arr,(char *)s);

    (*env)->ReleaseByteArrayElements(env,dat,s,JNI_ABORT);
    (*env)->ReleaseIntArrayElements(env,origin,arr,JNI_ABORT);

    if (retVal == FAIL) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}
