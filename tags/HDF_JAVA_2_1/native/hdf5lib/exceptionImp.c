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
 *  This is a utility program used by the HDF Java-C wrapper layer to
 *  generate exceptions.  This may be called from any part of the
 *  Java-C interface.
 *
 */
#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include <stdio.h>
#include "jni.h"
/*
#include "H5Eprivate.h"
*/
/*  These types are copied from H5Eprivate.h
 *  They should be moved to a public include file, and deleted from
 *  here.
 */
#define H5E_NSLOTS      32      /*number of slots in an error stack */
/*
* The list of error messages in the system is kept as an array of
* error_code/message pairs, one for major error numbers and another for
* minor error numbers.
*/
typedef struct H5E_major_mesg_t {
    H5E_major_t error_code;
    const char  *str;
} H5E_major_mesg_t;

typedef struct H5E_minor_mesg_t {
    H5E_minor_t error_code;
    const char  *str;
} H5E_minor_mesg_t;

/* An error stack */
typedef struct H5E_t {
    /*intn*/int        nused;         /*num slots currently used in stack  */
    H5E_error_t slot[H5E_NSLOTS];       /*array of error records             */
} H5E_t;


/*
 * The error stack.  Eventually we'll have some sort of global table so each
 * thread has it's own stack.  The stacks will be created on demand when the
 * thread first calls H5E_push().
 */
#ifdef WIN32
/* windows does not like the 'extern' here */
H5E_t H5E_stack_g[1];
#else
extern H5E_t H5E_stack_g[1];
#endif
#define H5E_get_my_stack()	(H5E_stack_g+0)

char *defineHDF5LibraryException(int maj_num);


/*
 * Class:     ncsa_hdf_hdf5lib_exceptions_HDF5Library
 * Method:    H5error_off
 * Signature: ()I
 *
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5error_1off
  (JNIEnv *env, jclass clss )
{
    return H5Eset_auto(NULL, NULL);
}


/*
 * Class:     ncsa_hdf_hdf5lib_exceptions_HDFLibraryException
 * Method:    printStackTrace0
 * Signature: (Ljava/lang/Object;)V
 *
 *  Call the HDF-5 library to print the HDF-5 error stack to 'file_name'.
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_exceptions_HDF5LibraryException_printStackTrace0
  (JNIEnv *env, jobject obj, jstring file_name)
{
    FILE *stream;
    char *file;

    if (file_name == NULL)
        H5Eprint(stderr);
    else
    {
#ifdef __cplusplus
        file = (char *)env->GetStringUTFChars(file_name,0);
#else
        file = (char *)(*env)->GetStringUTFChars(env,file_name,0);
#endif
        stream = fopen(file, "a+");
        H5Eprint(stream);
#ifdef __cplusplus
        env->ReleaseStringUTFChars(file_name, file);
#else
        (*env)->ReleaseStringUTFChars(env, file_name, file);
#endif
        if (stream) fclose(stream);
    }
}

int getMajorErrorNumber();
int getMinorErrorNumber();
/*
 * Class:     ncsa_hdf_hdf5lib_exceptions_HDFLibraryException
 * Method:    getMajorErrorNumber
 * Signature: ()I
 *
 *  Extract the HDF-5 major error number from the HDF-5 error stack.
 *
 *  Note:  This relies on undocumented, 'private' code in the HDF-5
 *  library.  Later releases will have a public interface for this
 *  purpose.
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_exceptions_HDF5LibraryException_getMajorErrorNumber
  (JNIEnv *env, jobject obj)
{
    return (jint) getMajorErrorNumber();
}

int getMajorErrorNumber()
{
    H5E_t	*estack = H5E_get_my_stack ();
    H5E_error_t *err_desc;
    H5E_major_t maj_num = H5E_NONE_MAJOR;


    if (estack && estack->nused>0)
    {
        err_desc = estack->slot+0;
        maj_num = err_desc->maj_num;
    }

    return (int) maj_num;
}

/*
 * Class:     ncsa_hdf_hdf5lib_exceptions_HDFLibraryException
 * Method:    getMinorErrorNumber
 * Signature: ()I
 *
 *  Extract the HDF-5 minor error number from the HDF-5 error stack.
 *
 *  Note:  This relies on undocumented, 'private' code in the HDF-5
 *  library.  Later releases will have a public interface for this
 *  purpose.
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_exceptions_HDF5LibraryException_getMinorErrorNumber
  (JNIEnv *env, jobject obj)
{
    return (jint) getMinorErrorNumber();
}

int getMinorErrorNumber()
{
    H5E_t	*estack = H5E_get_my_stack ();
    H5E_error_t *err_desc;
    H5E_minor_t min_num = H5E_NONE_MINOR;


    if (estack && estack->nused>0)
    {
        err_desc = estack->slot+0;
        min_num = err_desc->min_num;
    }

    return (int) min_num;
}

/*
 *  Routine to raise particular Java exceptions from C
 */

/*
 *  Create and throw an 'outOfMemoryException'
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5outOfMemory( JNIEnv *env, char *functName)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass("java/lang/OutOfMemoryError");
#else
    jc = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = (env)->NewStringUTF(functName);
#else
    str = (*env)->NewStringUTF(env,functName);
#endif
    args[0] = (char *)str;
    args[1] = 0;

#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw( (jthrowable ) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  OutOfMemoryError: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/*
 *  A fatal error in a JNI call
 *  Create and throw an 'InternalError'
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5JNIFatalError( JNIEnv *env, char *functName)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass("java/lang/InternalError");
#else
    jc = (*env)->FindClass(env, "java/lang/InternalError");
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = env->NewStringUTF(functName);
#else
    str = (*env)->NewStringUTF(env,functName);
#endif
    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw( (jthrowable) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  JNIFatal: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 *  A NULL argument in an HDF5 call
 *  Create and throw an 'NullPointerException'
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5nullArgument( JNIEnv *env, char *functName)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass("java/lang/NullPointerException");
#else
    jc = (*env)->FindClass(env, "java/lang/NullPointerException");
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = env->NewStringUTF(functName);
#else
    str = (*env)->NewStringUTF(env,functName);
#endif
    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw((jthrowable) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif

    if (rval < 0) {
        printf("FATAL ERROR:  NullPoitner: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 *  A bad argument in an HDF5 call
 *  Create and throw an 'IllegalArgumentException'
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5badArgument( JNIEnv *env, char *functName)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass("java/lang/IllegalArgumentException");
#else
    jc = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = env->NewStringUTF(functName);
#else
    str = (*env)->NewStringUTF(env,functName);
#endif
    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw((jthrowable) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  BadArgument: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 *  Some feature Not implemented yet
 *  Create and throw an 'UnsupportedOperationException'
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5unimplemented( JNIEnv *env, char *functName)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass("java/lang/UnsupportedOperationException");
#else
    jc = (*env)->FindClass(env, "java/lang/UnsupportedOperationException");
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = env->NewStringUTF(functName);
#else
    str = (*env)->NewStringUTF(env,functName);
#endif
    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw((jthrowable) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  Unsupported: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 *  h5libraryError()   determines the HDF-5 major error code
 *  and creates and throws the appropriate sub-class of
 *  HDF5LibraryException().  This routine should be called
 *  whenever a call to the HDF-5 library fails, i.e., when
 *  the return is -1.
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5libraryError( JNIEnv *env )
{
    jmethodID jm;
    jclass jc;
    char *args[2];
    char *exception;
    jobject ex;
    jstring str;
    char *msg;
    int rval, min_num, maj_num;

    maj_num = (int)getMajorErrorNumber();
    exception = (char *)defineHDF5LibraryException(maj_num);

#ifdef __cplusplus
    jc = env->FindClass(exception);
#else
    jc = (*env)->FindClass(env, exception);
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

    min_num = (int)getMinorErrorNumber();
    msg = (char *)H5Eget_minor((H5E_minor_t)min_num);
#ifdef __cplusplus
    str = env->NewStringUTF(msg);
#else
    str = (*env)->NewStringUTF(env,msg);
#endif

    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA ( jc, jm, (jvalue *)args );

    rval = env->Throw((jthrowable) ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  h5libraryError: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/*  raiseException().  This routine is called to generate
 *  an arbitrary Java exception with a particular message.
 *
 *  Note:  This routine never returns from the 'throw',
 *  and the Java native method immediately raises the
 *  exception.
 */
jboolean h5raiseException( JNIEnv *env, char *exception, char *message)
{
    jmethodID jm;
    jclass jc;
    char * args[2];
    jobject ex;
    jstring str;
    int rval;

#ifdef __cplusplus
    jc = env->FindClass(exception);
#else
    jc = (*env)->FindClass(env, exception);
#endif
    if (jc == NULL) {
        return JNI_FALSE;
    }
#ifdef __cplusplus
    jm = env->GetMethodID(jc, "<init>", "(Ljava/lang/String;)V");
#else
    jm = (*env)->GetMethodID(env, jc, "<init>", "(Ljava/lang/String;)V");
#endif
    if (jm == NULL) {
        return JNI_FALSE;
    }

#ifdef __cplusplus
    str = env->NewStringUTF(message);
#else
    str = (*env)->NewStringUTF(env,message);
#endif
    args[0] = (char *)str;
    args[1] = 0;
#ifdef __cplusplus
    ex = env->NewObjectA (  jc, jm, (jvalue *)args );

    rval = env->Throw( (jthrowable)ex );
#else
    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
#endif
    if (rval < 0) {
        printf("FATAL ERROR:  raiseException: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
jboolean buildException( JNIEnv *env, char *exception, jint HDFerr)
{
    jmethodID jm;
    jclass jc;
    int args[2];
    jobject ex;
    int rval;


    jc = (*env)->FindClass(env, exception);
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jm = (*env)->GetMethodID(env, jc, "<init>", "(I)V");
    if (jm == NULL) {
        return JNI_FALSE;
    }
    args[0] = HDFerr;
    args[1] = 0;

    ex = (*env)->NewObjectA ( env, jc, jm, (jvalue *)args );

    rval = (*env)->Throw(env, ex );
    if (rval < 0) {
        printf("FATAL ERROR:  raiseException: Throw failed\n");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}
*/

/*
 *  defineHDF5LibraryException()  returns the name of the sub-class
 *  which goes with an HDF-5 error code.
 */
char *defineHDF5LibraryException(int maj_num)
{
    H5E_major_t err_num = (H5E_major_t) maj_num;

    switch (err_num)
    {
        case H5E_ARGS:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5FunctionArgumentException";
        case H5E_RESOURCE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5ResourceUnavailableException";
        case H5E_INTERNAL:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5InternalErrorException";
        case H5E_FILE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5FileInterfaceException";
        case H5E_IO:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5LowLevelIOException";
        case H5E_FUNC:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5FunctionEntryExitException";
        case H5E_ATOM:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5AtomException";
        case H5E_CACHE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5MetaDataCacheException";
        case H5E_BTREE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5BtreeException";
        case H5E_SYM:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5SymbolTableException";
        case H5E_HEAP:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5HeapException";
        case H5E_OHDR:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5ObjectHeaderException";
        case H5E_DATATYPE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5DatatypeInterfaceException";
        case H5E_DATASPACE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5DataspaceInterfaceException";
        case H5E_DATASET:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5DatasetInterfaceException";
        case H5E_STORAGE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5DataStorageException";
        case H5E_PLIST:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5PropertyListInterfaceException";
        case H5E_ATTR:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5AttributeException";
        case H5E_PLINE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5DataFiltersException";
        case H5E_EFL:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5ExternalFileListException";
        case H5E_REFERENCE:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5ReferenceException";
        default:
             return "ncsa/hdf/hdf5lib/exceptions/HDF5LibraryException";
    }

}

#ifdef __cplusplus
}
#endif
