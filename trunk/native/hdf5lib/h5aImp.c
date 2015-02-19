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
 *  Attribute API Functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include "h5util.h"
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include "h5jni.h"

#ifdef H5_HAVE_WIN32_API
  #define strtoll(S,R,N)     _strtoi64(S,R,N)
  #define strtoull(S,R,N)    _strtoui64(S,R,N)
  #define strtof(S,R)    atof(S)
#endif /* H5_HAVE_WIN32_API */

herr_t H5AreadVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AreadVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AreadVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);

herr_t H5AwriteVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AwriteVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);
herr_t H5AwriteVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf);

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acreate
 * Signature: (JLjava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong type_id,
          jlong space_id, jlong create_plist)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Acreate:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Acreate: aName is not pinned");
        return -1;
    }

    status = H5Acreate2((hid_t)loc_id, aName, (hid_t)type_id,
        (hid_t)space_id, (hid_t)create_plist, (hid_t)H5P_DEFAULT );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_name
 * Signature: (JLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env,"H5Aopen_name:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env,"H5Aopen_name: name is not pinned");
        return -1;
    }

    status = H5Aopen_name((hid_t)loc_id, aName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_idx
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1idx
  (JNIEnv *env, jclass clss, jlong loc_id, jint idx)
{
    hid_t retVal = -1;
    retVal =  H5Aopen_idx((hid_t)loc_id, (unsigned int) idx );
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Awrite
 * Signature: (JJ[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Awrite
  (JNIEnv *env, jclass clss, jlong attr_id, jlong mem_type_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument( env,"H5Awrite:  buf is NULL");
        return -1;
    }

    byteP = ENVPTR->GetByteArrayElements(ENVPAR buf,&isCopy);

    if (byteP == NULL) {
        h5JNIFatalError( env,"H5Awrite: buf is not pinned");
        return -1;
    }
    status = H5Awrite((hid_t)attr_id, (hid_t)mem_type_id, byteP);

    ENVPTR->ReleaseByteArrayElements(ENVPAR buf, byteP,JNI_ABORT);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5AwriteVL
 * Signature: (JJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5AwriteVL
  (JNIEnv *env, jclass clss, jlong attr_id, jlong mem_type_id, jobjectArray buf)
{
    herr_t status;

    if (buf == NULL) {
        h5nullArgument( env,"H5AwriteVL:  buf is NULL");
        return -1;
    }

    if (H5Tis_variable_str((hid_t)mem_type_id) > 0) {
        status = H5AwriteVL_str (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        status = H5AwriteVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_ARRAY) {
        status = H5AwriteVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else {
        status = H5AwriteVL_num (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }

    return (jint)status;
}

herr_t H5AwriteVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t  status = -1;
    int     n;
    hvl_t  *wdata = NULL;
    jint    i,j;
    unsigned char   tmp_uchar = 0;
    char            tmp_char = 0;
    unsigned short  tmp_ushort = 0;
    short           tmp_short = 0;
    unsigned int    tmp_uint = 0;
    int             tmp_int = 0;
    unsigned long   tmp_ulong = 0;
    long            tmp_long = 0;
    unsigned long long tmp_ullong = 0;
    long long       tmp_llong = 0;
    float           tmp_float = 0.0;
    double          tmp_double = 0.0;
    long double     tmp_ldouble = 0.0;
    H5T_class_t     tclass = H5Tget_class(tid);
    size_t          size = H5Tget_size(tid);
    H5T_sign_t      nsign = H5Tget_sign(tid);
//    hid_t           sid = H5Aget_space(tid);
    hid_t           basetid = -1;
    hid_t           basesid = -1;
    H5T_class_t     basetclass = -1;
    char           *temp;
    char           *token;

    if(tclass == H5T_VLEN) {
        basetid = H5Tget_super(tid);
        size = H5Tget_size(basetid);
        basetclass = H5Tget_class(basetid);
//        basesid = H5Aget_space(basetid);
    }
    n = ENVPTR->GetArrayLength(ENVPAR (jarray)buf);
printf("H5AwriteVL_num: n=%d of len %d\n", n, sizeof(buf));

    wdata = (hvl_t *)calloc(n+1, sizeof(hvl_t));
    if (!wdata) {
        h5JNIFatalError(env, "H5AwriteVL_str:  cannot allocate buffer");
        return -1;
    }
    for (i = 0; i < n; i++) {
        int j;

        jstring obj = (jstring) ENVPTR->GetObjectArrayElement(ENVPAR (jobjectArray) buf, i);
        if (obj != 0) {
            jsize length = ENVPTR->GetStringUTFLength(ENVPAR obj);
            const char *utf8 = ENVPTR->GetStringUTFChars(ENVPAR obj, 0);
printf("utf8=%s\n", utf8);
            temp = malloc(length+1);
            strncpy(temp, utf8, length);
            temp[length] = '\0';
printf("temp=%s\n", temp);
            token = strtok(temp, ",");
printf("token[0]:%s\n", token);
            j = 1;
            while (1) {
                token = strtok (NULL, ",");
printf("token[%d]:%s\n", j, token);
                if (token == NULL)
                    break;
                j++;
            }
printf("H5AwriteVL_num: count=%d obj_len=%d of utf8_len %d\n", j, length, sizeof(utf8));
            wdata[i].p = malloc(j * size);
            wdata[i].len = j;

            strncpy(temp, utf8, length);
            temp[length] = '\0';
            switch (tclass) {
                case H5T_FLOAT:
                    if (sizeof(float) == size) {
printf("float:%s\n", utf8);
                        j = 0;
                        tmp_float = strtof(strtok(temp, ","), NULL);
                        ((float *)wdata[i].p)[j++] = tmp_float;

                        while (1) {
                            token = strtok (NULL, ",");
                            if (token == NULL)
                                break;
                            if (token[0] == ' ')
                                token++;
                            tmp_float = strtof(token, NULL);
                            ((float *)wdata[i].p)[j++] = tmp_float;
                        }
                    }
                    else if (sizeof(double) == size) {
printf("double:%s\n", utf8);
                        j = 0;
                        tmp_double = strtod(strtok(temp, ","), NULL);
                        ((double *)wdata[i].p)[j++] = tmp_double;

                        while (1) {
                            token = strtok (NULL, ",");
                            if (token == NULL)
                                break;
                            if (token[0] == ' ')
                                token++;
                            tmp_double = strtod(token, NULL);
                            ((double *)wdata[i].p)[j++] = tmp_double;
                        }
                    }
#if H5_SIZEOF_LONG_DOUBLE !=0
                    else if (sizeof(long double) == size) {
printf("longdouble:%s\n", utf8);
                        j = 0;
                        tmp_ldouble = strtold(strtok(temp, ","), NULL);
                        ((long double *)wdata[i].p)[j++] = tmp_ldouble;

                        while (1) {
                            token = strtok (NULL, ",");
                            if (token == NULL)
                                break;
                            if (token[0] == ' ')
                                token++;
                            tmp_ldouble = strtold(token, NULL);
                            ((long double *)wdata[i].p)[j++] = tmp_ldouble;
                        }
                   }
#endif
                    break;
                case H5T_INTEGER:
                    if (sizeof(char) == size) {
                        if(H5T_SGN_NONE == nsign) {
printf("uchar:%s\n", utf8);
                            j = 0;
                            tmp_uchar = (unsigned char)strtoul(strtok(temp, ","), NULL, 10);
                            ((unsigned char *)wdata[i].p)[j++] = tmp_uchar;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_uchar = (unsigned char)strtoul(token, NULL, 10);
                                ((unsigned char *)wdata[i].p)[j++] = tmp_uchar;
                            }
                        }
                        else {
printf("char:%s\n", utf8);
                            j = 0;
                            tmp_char = (char)strtoul(strtok(temp, ","), NULL, 10);
                            ((char *)wdata[i].p)[j++] = tmp_char;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_char = (char)strtoul(token, NULL, 10);
                                ((char *)wdata[i].p)[j++] = tmp_char;
                            }
                        }
                    }
                    else if (sizeof(int) == size) {
                        if(H5T_SGN_NONE == nsign) {
printf("uint:%s\n", utf8);
                            j = 0;
                            tmp_uint = (unsigned int)strtoul(strtok(temp, ","), NULL, 10);
                            ((unsigned int *)wdata[i].p)[j++] = tmp_uint;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_uint = (unsigned int)strtoul(token, NULL, 10);
                                ((unsigned int *)wdata[i].p)[j++] = tmp_uint;
                            }
                        }
                        else {
printf("int:%s\n", utf8);
                            j = 0;
                            tmp_int = (int)strtoul(strtok(temp, ","), NULL, 10);
                            ((int *)wdata[i].p)[j++] = tmp_int;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_int = (int)strtoul(token, NULL, 10);
                                ((int *)wdata[i].p)[j++] = tmp_int;
                            }
                        }
                    }
                    else if (sizeof(short) == size) {
                        if(H5T_SGN_NONE == nsign) {
printf("ushort:%s\n", utf8);
                            j = 0;
                            tmp_ushort = (unsigned short)strtoul(strtok(temp, ","), NULL, 10);
                            ((unsigned short *)wdata[i].p)[j++] = tmp_ushort;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_ushort = (unsigned short)strtoul(token, NULL, 10);
                                ((unsigned short *)wdata[i].p)[j++] = tmp_ushort;
                            }
                        }
                        else {
printf("short:%s\n", utf8);
                            j = 0;
                            tmp_short = (short)strtoul(strtok(temp, ","), NULL, 10);
                            ((short *)wdata[i].p)[j++] = tmp_short;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_short = (short)strtoul(token, NULL, 10);
                                ((short *)wdata[i].p)[j++] = tmp_short;
                            }
                        }
                    }
                    else if (sizeof(long) == size) {
                        if(H5T_SGN_NONE == nsign) {
printf("ulong:%s\n", utf8);
                            j = 0;
                            tmp_ulong = strtoul(strtok(temp, ","), NULL, 10);
                            ((unsigned long *)wdata[i].p)[j++] = tmp_ulong;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_ulong = strtoul(token, NULL, 10);
                                ((unsigned long *)wdata[i].p)[j++] = tmp_ulong;
                            }
                        }
                        else {
printf("long:%s\n", utf8);
                            j = 0;
                            tmp_long = strtol(strtok(temp, ","), NULL, 10);
                            ((long *)wdata[i].p)[j++] = tmp_long;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_long = strtol(token, NULL, 10);
                                ((long *)wdata[i].p)[j++] = tmp_long;
                            }
                        }
                    }
                    else if (sizeof(long long) == size) {
                        if(H5T_SGN_NONE == nsign) {
printf("ulonglong:%s\n", utf8);
                            j = 0;
                            tmp_ullong = strtoull(strtok(temp, ","), NULL, 10);
                            ((unsigned long long *)wdata[i].p)[j++] = tmp_ullong;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_ullong = strtoull(token, NULL, 10);
                                ((unsigned long long *)wdata[i].p)[j++] = tmp_ullong;
                            }
                        }
                        else {
printf("longlong:%s\n", utf8);
                            j = 0;
                            tmp_llong = strtoll(strtok(temp, ","), NULL, 10);
                            ((long long *)wdata[i].p)[j++] = tmp_llong;

                            while (1) {
                                token = strtok (NULL, ",");
                                if (token == NULL)
                                    break;
                                if (token[0] == ' ')
                                    token++;
                                tmp_llong = strtoll(token, NULL, 10);
                                ((long long *)wdata[i].p)[j++] = tmp_llong;
                            }
                       }
                    }
                    break;
                case H5T_STRING:
                    {
printf("string:%s\n", utf8);
                    }
                    break;
                case H5T_COMPOUND:
                    {
printf("compound:%s\n", utf8);
                    }
                    break;
                case H5T_ENUM:
                    {
printf("enum:%s\n", utf8);
                    }
                    break;
                case H5T_REFERENCE:
printf("reference:%s\n", utf8);
                    break;
                case H5T_ARRAY:
                    {
printf("array:%s\n", utf8);
                    }
                    break;
                case H5T_VLEN:
                    {
printf("vlen:type size=%d, %s\n", size, utf8);
                        switch (basetclass) {
                        case H5T_FLOAT:
                            if (sizeof(float) == size) {
printf("vlfloat:%s\n", utf8);
                                j = 0;
                                tmp_float = strtof(strtok(temp, ","), NULL);
                                ((float *)wdata[i].p)[j++] = tmp_float;

                                while (1) {
                                    token = strtok (NULL, ",");
                                    if (token == NULL)
                                        break;
                                    if (token[0] == ' ')
                                        token++;
                                    tmp_float = strtof(token, NULL);
                                    ((float *)wdata[i].p)[j++] = tmp_float;
                                }
                            }
                            else if (sizeof(double) == size) {
printf("vldouble:%s\n", utf8);
                                j = 0;
                                tmp_double = strtod(strtok(temp, ","), NULL);
                                ((double *)wdata[i].p)[j++] = tmp_double;

                                while (1) {
                                    token = strtok (NULL, ",");
                                    if (token == NULL)
                                        break;
                                    if (token[0] == ' ')
                                        token++;
                                    tmp_double = strtod(token, NULL);
                                    ((double *)wdata[i].p)[j++] = tmp_double;
                                }
                            }
        #if H5_SIZEOF_LONG_DOUBLE !=0
                            else if (sizeof(long double) == size) {
printf("vllongdouble:%s\n", utf8);
                                j = 0;
                                tmp_ldouble = strtold(strtok(temp, ","), NULL);
                                ((long double *)wdata[i].p)[j++] = tmp_ldouble;

                                while (1) {
                                    token = strtok (NULL, ",");
                                    if (token == NULL)
                                        break;
                                    if (token[0] == ' ')
                                        token++;
                                    tmp_ldouble = strtold(token, NULL);
                                    ((long double *)wdata[i].p)[j++] = tmp_ldouble;
                                }
                           }
        #endif
                            break;
                        case H5T_INTEGER:
                            if (sizeof(char) == size) {
                                if(H5T_SGN_NONE == nsign) {
printf("vluchar:%s\n", utf8);
                                    j = 0;
                                    tmp_uchar = (unsigned char)strtoul(strtok(temp, ","), NULL, 10);
                                    ((unsigned char *)wdata[i].p)[j++] = tmp_uchar;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_uchar = (unsigned char)strtoul(token, NULL, 10);
                                        ((unsigned char *)wdata[i].p)[j++] = tmp_uchar;
                                    }
                                }
                                else {
printf("vlchar:%s\n", utf8);
                                    j = 0;
                                    tmp_char = (char)strtoul(strtok(temp, ","), NULL, 10);
                                    ((char *)wdata[i].p)[j++] = tmp_char;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_char = (char)strtoul(token, NULL, 10);
                                        ((char *)wdata[i].p)[j++] = tmp_char;
                                    }
                                }
                            }
                            else if (sizeof(int) == size) {
                                if(H5T_SGN_NONE == nsign) {
printf("vluint:%s\n", utf8);
                                    j = 0;
                                    tmp_uint = (unsigned int)strtoul(strtok(temp, ","), NULL, 10);
                                    ((unsigned int *)wdata[i].p)[j++] = tmp_uint;

                                    while (1) {
                                        token = strtok (NULL, ",");
printf("token[%d]:%s\n", j, token);
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_uint = (unsigned int)strtoul(token, NULL, 10);
                                        ((unsigned int *)wdata[i].p)[j++] = tmp_uint;
                                    }
                                }
                                else {
printf("vlint:%s\n", utf8);
                                    j = 0;
                                    tmp_int = (int)strtoul(strtok(temp, ","), NULL, 10);
                                    ((int *)wdata[i].p)[j++] = tmp_int;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_int = (int)strtoul(token, NULL, 10);
                                        ((int *)wdata[i].p)[j++] = tmp_int;
                                    }
                                }
                            }
                            else if (sizeof(short) == size) {
                                if(H5T_SGN_NONE == nsign) {
printf("vlushort:%s\n", utf8);
                                    j = 0;
                                    tmp_ushort = (unsigned short)strtoul(strtok(temp, ","), NULL, 10);
                                    ((unsigned short *)wdata[i].p)[j++] = tmp_ushort;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        tmp_ushort = (unsigned short)strtoul(token, NULL, 10);
                                        ((unsigned short *)wdata[i].p)[j++] = tmp_ushort;
                                    }
                                }
                                else {
printf("vlshort:%s\n", utf8);
                                    j = 0;
                                    tmp_short = (short)strtoul(strtok(temp, ","), NULL, 10);
                                    ((short *)wdata[i].p)[j++] = tmp_short;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        tmp_short = (short)strtoul(token, NULL, 10);
                                        ((short *)wdata[i].p)[j++] = tmp_short;
                                    }
                                }
                            }
                            else if (sizeof(long) == size) {
                                if(H5T_SGN_NONE == nsign) {
printf("vlulong:%s\n", utf8);
                                    j = 0;
                                    tmp_ulong = strtoul(strtok(temp, ","), NULL, 10);
                                    ((unsigned long *)wdata[i].p)[j++] = tmp_ulong;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_ulong = strtoul(token, NULL, 10);
                                        ((unsigned long *)wdata[i].p)[j++] = tmp_ulong;
                                    }
                                }
                                else {
printf("vllong:%s\n", utf8);
                                    j = 0;
                                    tmp_long = strtol(strtok(temp, ","), NULL, 10);
                                    ((long *)wdata[i].p)[j++] = tmp_long;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_long = strtol(token, NULL, 10);
                                        ((long *)wdata[i].p)[j++] = tmp_long;
                                    }
                                }
                            }
                            else if (sizeof(long long) == size) {
                                if(H5T_SGN_NONE == nsign) {
printf("vlulonglong:%s\n", utf8);
                                    j = 0;
                                    tmp_ullong = strtoull(strtok(temp, ","), NULL, 10);
                                    ((unsigned long long *)wdata[i].p)[j++] = tmp_ullong;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_ullong = strtoull(token, NULL, 10);
                                        ((unsigned long long *)wdata[i].p)[j++] = tmp_ullong;
                                    }
                                }
                                else {
printf("vllonglong:%s\n", utf8);
                                    j = 0;
                                    tmp_llong = strtoll(strtok(temp, ","), NULL, 10);
                                    ((long long *)wdata[i].p)[j++] = tmp_llong;

                                    while (1) {
                                        token = strtok (NULL, ",");
                                        if (token == NULL)
                                            break;
                                        if (token[0] == ' ')
                                            token++;
                                        tmp_llong = strtoll(token, NULL, 10);
                                        ((long long *)wdata[i].p)[j++] = tmp_llong;
                                    }
                               }
                            }
                            break;
                        }
                    }
                    break;
            } /* end switch */

        }
    }


    status = H5Awrite((hid_t)aid, (hid_t)tid, wdata);

    // now free memory
    for (i = 0; i < n; i++) {
       if(wdata[i].p) {
           free(wdata[i].p);
       }
    }
//    H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, wdata);
    free(wdata);
//    H5Sclose(sid);

    if (status < 0) {
        h5libraryError(env);
    }

    return status;
}

herr_t H5AwriteVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t      status;

    h5unimplemented(env, "H5AwriteVL_comp:  not implemented");
    status = -1;

    return status;
}

herr_t H5AwriteVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t  status = -1;
    char  **wdata;
    jsize   size;
    jint    i;

    size = ENVPTR->GetArrayLength(ENVPAR (jarray) buf);

    wdata = (char**)malloc(size * sizeof (char*));
    if (!wdata) {
        h5JNIFatalError(env, "H5AwriteVL_str:  cannot allocate buffer");
        return -1;
    }

    memset(wdata, 0, size * sizeof(char*));
    for (i = 0; i < size; ++i) {
        jstring obj = (jstring) ENVPTR->GetObjectArrayElement(ENVPAR (jobjectArray) buf, i);
        if (obj != 0) {
            jsize length = ENVPTR->GetStringUTFLength(ENVPAR obj);
            const char *utf8 = ENVPTR->GetStringUTFChars(ENVPAR obj, 0);

            if (utf8) {
                wdata[i] = (char*)malloc(length + 1);
                if (wdata[i]) {
                    memset(wdata[i], 0, (length + 1));
                    strncpy(wdata[i], utf8, length);
                }
            }

            ENVPTR->ReleaseStringUTFChars(ENVPAR obj, utf8);
            ENVPTR->DeleteLocalRef(ENVPAR obj);
        }
    } /*for (i = 0; i < size; ++i) */

    status = H5Awrite((hid_t)aid, (hid_t)tid, wdata);

    // now free memory
    for (i = 0; i < size; i++) {
       if(wdata[i]) {
           free(wdata[i]);
       }
    }
    free(wdata);

    if (status < 0) {
        h5libraryError(env);
    }

    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aread
 * Signature: (JJ[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aread
  (JNIEnv *env, jclass clss, jlong attr_id, jlong mem_type_id, jbyteArray buf)
{
    herr_t status;
    jbyte *byteP;
    jboolean isCopy;

    if (buf == NULL) {
        h5nullArgument( env,"H5Aread:  buf is NULL");
        return -1;
    }

    byteP = ENVPTR->GetByteArrayElements(ENVPAR buf,&isCopy);

    if (byteP == NULL) {
        h5JNIFatalError( env,"H5Aread: buf is not pinned");
        return -1;
    }

    status = H5Aread((hid_t)attr_id, (hid_t)mem_type_id, byteP);

    if (status < 0) {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf,byteP,JNI_ABORT);
        h5libraryError(env);
    }
    else  {
        ENVPTR->ReleaseByteArrayElements(ENVPAR buf,byteP,0);
    }

    return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_space
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aget_1space
  (JNIEnv *env, jclass clss, jlong attr_id)
{
    hid_t retVal = -1;
    retVal =  H5Aget_space((hid_t)attr_id);
    if (retVal < 0) {
        /* throw exception */
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_type
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aget_1type
  (JNIEnv *env, jclass clss, jlong attr_id)
{
    hid_t retVal = -1;
    retVal =  H5Aget_type((hid_t)attr_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_name
 * Signature: (JJ[Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1name
  (JNIEnv *env, jclass clss, jlong attr_id, jlong buf_size, jobjectArray name)
{
    char *aName;
    jstring str;
    hssize_t size;
    long bs;

    if (buf_size==0 && name == NULL)
      return (jlong) H5Aget_name((hid_t)attr_id, 0, NULL);

    bs = (long)buf_size;
    if (bs <= 0) {
        h5badArgument( env, "H5Aget_name:  buf_size <= 0");
        return -1;
    }
    aName = (char*)malloc(sizeof(char)*bs);
    if (aName == NULL) {
        h5outOfMemory( env, "H5Aget_name:  malloc failed");
        return -1;
    }
    size = H5Aget_name((hid_t)attr_id, (size_t)buf_size, aName);
    if (size < 0) {
        free(aName);
        h5libraryError(env);
        return -1;
        /*  exception, returns immediately */
    }
    /* successful return -- save the string; */

    str = ENVPTR->NewStringUTF(ENVPAR aName);

    if (str == NULL) {
        free(aName);
        h5JNIFatalError( env,"H5Aget_name:  return string failed");
        return -1;
    }
    free(aName);
    /*  Note: throws ArrayIndexOutOfBoundsException,
        ArrayStoreException */

    ENVPTR->SetObjectArrayElement(ENVPAR name,0,str);

    return (jlong)size;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_num_attrs
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1num_1attrs
  (JNIEnv *env, jclass clss, jlong loc_id)
{
    int retVal = -1;
    retVal =  H5Aget_num_attrs((hid_t)loc_id);
    if (retVal < 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name)
{
    herr_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env,"H5Adelete:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name,&isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env,"H5Adelete: name is not pinned");
        return -1;
    }

    status = H5Adelete((hid_t)loc_id, aName );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aclose
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aclose
  (JNIEnv *env, jclass clss, jlong attr_id)
{
    herr_t retVal = 0;

    if (attr_id > 0)
        retVal =  H5Aclose((hid_t)attr_id);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5AreadVL
 * Signature: (JJ[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5AreadVL
  (JNIEnv *env, jclass clss, jlong attr_id, jlong mem_type_id, jobjectArray buf)
{
    htri_t isStr;

    if ( buf == NULL ) {
        h5nullArgument( env, "H5AreadVL:  buf is NULL");
        return -1;
    }

    isStr = H5Tis_variable_str((hid_t)mem_type_id);

    if (H5Tis_variable_str((hid_t)mem_type_id) > 0) {
        return (jint) H5AreadVL_str (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_COMPOUND) {
        return (jint) H5AreadVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else if (H5Tget_class((hid_t)mem_type_id) == H5T_ARRAY) {
        return (jint) H5AreadVL_comp (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
    else {
        return (jint) H5AreadVL_num (env, (hid_t)attr_id, (hid_t)mem_type_id, buf);
    }
}

herr_t H5AreadVL_num (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t  status;
    int     i;
    int     n;
    size_t  max_len = 0;
    h5str_t h5str;
    jstring jstr;
    hvl_t  *rdata = NULL;
    size_t  size;
    hid_t   sid;
    hsize_t dims[H5S_MAX_RANK];

    n = ENVPTR->GetArrayLength(ENVPAR buf);
    rdata = (hvl_t *)calloc(n+1, sizeof(hvl_t));
    if (rdata == NULL) {
        h5JNIFatalError( env, "H5AreadVL_num:  failed to allocate buff for read");
        return -1;
    }

    status = H5Aread(aid, tid, rdata);
    dims[0] = n;
    sid = H5Screate_simple(1, dims, NULL);
    if (status < 0) {
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
        H5Sclose(sid);
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL_num: failed to read data");
        return -1;
    }

    for (i = 0; i < n; i++) {
        if ((rdata +i)->len > max_len)
            max_len = (rdata + i)->len;
    }

    size = H5Tget_size(tid);
    memset((void *)&h5str, (int)0, (size_t)sizeof(h5str_t));
    h5str_new(&h5str, 4*size);

    if (h5str.s == NULL) {
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
        H5Sclose(sid);
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL_num:  failed to allocate strng buf");
        return -1;
    }

    for (i = 0; i < n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, aid, tid, rdata + i, 0);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);
    H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, rdata);
    H5Sclose(sid);

    free(rdata);

    return status;
}

herr_t H5AreadVL_comp (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t      status;
    int         i;
    int         n;
    size_t      max_len = 0;
    h5str_t     h5str;
    jstring     jstr;
    char       *rdata;
    size_t      size;
    hid_t       p_type;

    p_type = H5Tget_native_type(tid, H5T_DIR_DEFAULT);
    size = (((H5Tget_size(tid))>(H5Tget_size(p_type))) ? (H5Tget_size(tid)) : (H5Tget_size(p_type)));
    H5Tclose(p_type);
    n = ENVPTR->GetArrayLength(ENVPAR buf);
    rdata = (char *)malloc(n * size);

    if (rdata == NULL) {
        h5JNIFatalError(env, "H5AreadVL_comp:  failed to allocate buff for read");
        return -1;
    }

    status = H5Aread(aid, tid, rdata);

    if (status < 0) {
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL_comp: failed to read data");
        return -1;
    }

    memset(&h5str, 0, sizeof(h5str_t));
    h5str_new(&h5str, 4 * size);

    if (h5str.s == NULL) {
        free(rdata);
        h5JNIFatalError(env, "H5AreadVL_comp:  failed to allocate string buf");
        return -1;
    }

    for (i = 0; i < n; i++) {
        h5str.s[0] = '\0';
        h5str_sprintf(&h5str, aid, tid, rdata + i * size, 0);
        jstr = ENVPTR->NewStringUTF(ENVPAR h5str.s);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
    }

    h5str_free(&h5str);

    free(rdata);

    return status;
}

herr_t H5AreadVL_str (JNIEnv *env, hid_t aid, hid_t tid, jobjectArray buf)
{
    herr_t status=-1;
    jstring jstr;
    char **strs;
    int i, n;
    hid_t sid;
    hsize_t dims[H5S_MAX_RANK];

    n = ENVPTR->GetArrayLength(ENVPAR buf);

    strs =(char **)malloc(n*sizeof(char *));
    if (strs == NULL) {
        h5JNIFatalError( env, "H5AreadVL_str:  failed to allocate buff for read variable length strings");
        return -1;
    }

    status = H5Aread(aid, tid, strs);
    if (status < 0) {
        dims[0] = n;
        sid = H5Screate_simple(1, dims, NULL);
        H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, strs);
        H5Sclose(sid);
        free(strs);
        h5JNIFatalError(env, "H5AreadVL_str: failed to read variable length strings");
        return -1;
    }

    for (i=0; i<n; i++) {
        jstr = ENVPTR->NewStringUTF(ENVPAR strs[i]);
        ENVPTR->SetObjectArrayElement(ENVPAR buf, i, jstr);
        free (strs[i]);
    }

    /*
    for repeatedly reading an attribute with a large number of strs (e.g., 1,000,000 strings,
    H5Dvlen_reclaim() may crash on Windows because the Java GC will not be able to collect
    free space in time. Instead, use "free(strs[i])" to free individual strings
    after it is done.
    H5Dvlen_reclaim(tid, sid, H5P_DEFAULT, strs);
    */

    if (strs)
        free(strs);

    return status;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acopy
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Acopy
  (JNIEnv *env, jclass clss, jlong src_id, jlong dst_id)
{
    jbyte *buf;
    herr_t retVal = -1;
    hid_t src_did = (hid_t)src_id;
    hid_t dst_did = (hid_t)dst_id;
    hid_t tid=-1;
    hid_t sid=-1;
    hsize_t total_size = 0;


    sid = H5Aget_space(src_did);
    if (sid < 0) {
        h5libraryError(env);
        return -1;
    }

    tid = H5Aget_type(src_did);
    if (tid < 0) {
        H5Sclose(sid);
        h5libraryError(env);
        return -1;
    }

    total_size = H5Sget_simple_extent_npoints(sid) * H5Tget_size(tid);

    H5Sclose(sid);

    buf = (jbyte *)malloc( (int) (total_size * sizeof(jbyte)));
    if (buf == NULL) {
    H5Tclose(tid);
        h5outOfMemory( env, "H5Acopy:  malloc failed");
        return -1;
    }

    retVal = H5Aread(src_did, tid, buf);
    H5Tclose(tid);

    if (retVal < 0) {
        free(buf);
        h5libraryError(env);
        return (jint)retVal;
    }

    tid = H5Aget_type(dst_did);
    if (tid < 0) {
        free(buf);
        h5libraryError(env);
        return -1;
    }
    retVal = H5Awrite(dst_did, tid, buf);
    H5Tclose(tid);
    free(buf);

    if (retVal < 0) {
        h5libraryError(env);
    }

    return (jint)retVal;
}

/**********************************************************************
 *                                                                    *
 *          New functions release 1.8.0                               *
 *                                                                    *
 **********************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Acreate2
 * Signature: (JLjava/lang/String;JJJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate2
(JNIEnv *env, jclass clss, jlong loc_id, jstring name, jlong type_id,
        jlong space_id, jlong create_plist, jlong access_plist)
{
    hid_t status;
    char* aName;
    jboolean isCopy;

    if (name == NULL) {
        h5nullArgument( env, "H5Acreate2:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Acreate2: aName is not pinned");
        return -1;
    }

    status = H5Acreate2((hid_t)loc_id, aName, (hid_t)type_id,
        (hid_t)space_id, (hid_t)create_plist, (hid_t)access_plist );

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen
 * Signature: (JLjava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen
  (JNIEnv *env, jclass clss, jlong obj_id, jstring name, jlong access_plist)

{
   hid_t retVal;
   char* aName;
   jboolean isCopy;

   if (name == NULL) {
        h5nullArgument( env, "H5Aopen:  name is NULL");
        return -1;
    }

    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aopen: aName is not pinned");
        return -1;
    }

    retVal = H5Aopen((hid_t)obj_id, aName, (hid_t)access_plist);

    ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

    if (retVal< 0) {
        h5libraryError(env);
    }
    return (jlong)retVal;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen_by_idx
 * Signature: (JLjava/lang/String;IIJJJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1by_1idx
  (JNIEnv *env, jclass clss, jlong loc_id, jstring name, jint idx_type, jint order, jlong n, jlong aapl_id, jlong lapl_id)
{
  hid_t retVal;
  char* aName;
  jboolean isCopy;

  if (name == NULL) {
    h5nullArgument( env, "H5Aopen_by_idx:  name is NULL");
    return -1;
  }

  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR name, &isCopy);

  if (aName == NULL) {
    h5JNIFatalError( env, "H5Aopen_by_idx: aName is not pinned");
    return -1;
  }

  retVal = H5Aopen_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (hid_t)aapl_id, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR name,aName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jlong)retVal;

}

/*
* Class:     ncsa_hdf_hdf5lib_H5
* Method:    _H5Acreate_by_name
* Signature: (JLjava/lang/String;Ljava/lang/String;JJJJJ)J
*/
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Acreate_1by_1name
(JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring attr_name, jlong type_id, jlong space_id, jlong acpl_id, jlong aapl_id, jlong lapl_id)
{
  hid_t retVal;
  char *aName, *attrName;
  jboolean isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Acreate_by_name:  object name is NULL");
    return -1;
  }
  if (attr_name == NULL) {
    h5nullArgument( env, "H5Acreate_by_name:  attribute name is NULL");
    return -1;
  }
  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Acreate_by_name: aName is not pinned");
    return -1;
  }
  attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
  if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    h5JNIFatalError( env, "H5Acreate_by_name: attrName is not pinned");
    return -1;
  }

  retVal = H5Acreate_by_name((hid_t)loc_id, aName, attrName, (hid_t)type_id,
    (hid_t)space_id, (hid_t)acpl_id, (hid_t)aapl_id, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jlong)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aexists_by_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;J)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aexists_1by_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring attr_name, jlong lapl_id)
{
   htri_t retVal;
   char *aName, *attrName;
   jboolean isCopy;

  if (obj_name == NULL) {
        h5nullArgument( env, "H5Aexists_by_name:  object name is NULL");
        return -1;
  }
  if (attr_name == NULL) {
        h5nullArgument( env, "H5Aexists_by_name:  attribute name is NULL");
        return -1;
  }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aexists_by_name: aName is not pinned");
        return -1;
    }
    attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
        h5JNIFatalError( env, "H5Aexists_by_name: attrName is not pinned");
        return -1;
    }

  retVal = H5Aexists_by_name((hid_t)loc_id, aName, attrName, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

  if (retVal< 0) {
        h5libraryError(env);
    }
    return (jboolean)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Arename
 * Signature: (JLjava/lang/String;Ljava/lang/String)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Arename
  (JNIEnv *env, jclass clss, jlong loc_id, jstring old_attr_name, jstring new_attr_name)
{
    herr_t retVal;
    char *oName, *nName;
    jboolean isCopy;

    if (old_attr_name == NULL) {
        h5nullArgument( env, "H5Arename:  old_attr_name is NULL");
        return -1;
    }
    if (new_attr_name == NULL) {
        h5nullArgument( env, "H5Arename:  new_attr_name is NULL");
        return -1;
    }

    oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR old_attr_name,&isCopy);
    if (oName == NULL) {
        h5JNIFatalError( env, "H5Arename:  old_attr_name not pinned");
        return -1;
    }
    nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_attr_name,&isCopy);
    if (nName == NULL) {
        ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
        h5JNIFatalError( env, "H5Arename:  new_attr_name not pinned");
        return -1;
    }

    retVal = H5Arename((hid_t)loc_id, oName, nName);

    ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR new_attr_name,nName);

    if (retVal< 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Arename_by_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Arename_1by_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring old_attr_name, jstring new_attr_name, jlong lapl_id)
{
  herr_t retVal;
  char *aName, *oName, *nName;
  jboolean isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  object name is NULL");
    return -1;
  }
  if (old_attr_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  old_attr_name is NULL");
    return -1;
  }
  if (new_attr_name == NULL) {
    h5nullArgument( env, "H5Arename_by_name:  new_attr_name is NULL");
    return -1;
  }

  aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Arename_by_name: object name is not pinned");
    return -1;
  }
  oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR old_attr_name,&isCopy);
  if (oName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    h5JNIFatalError( env, "H5Arename_by_name:  old_attr_name not pinned");
    return -1;
  }
  nName = (char *)ENVPTR->GetStringUTFChars(ENVPAR new_attr_name,&isCopy);
  if (nName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
    h5JNIFatalError( env, "H5Arename_by_name:  new_attr_name not pinned");
    return -1;
  }

  retVal = H5Arename_by_name((hid_t)loc_id, aName, oName, nName, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR old_attr_name,oName);
  ENVPTR->ReleaseStringUTFChars(ENVPAR new_attr_name,nName);

  if (retVal< 0) {
    h5libraryError(env);
  }
  return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_name_by_idx
 * Signature: (JLjava/lang/String;IIJJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1name_1by_1idx
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jlong lapl_id)
{
  size_t   buf_size;
  char    *aName;
  char    *aValue;
  jboolean isCopy;
  jlong    status_size;
  jstring  str = NULL;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Aget_name_by_idx:  object name is NULL");
    return NULL;
  }
  aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Aget_name_by_idx:  name not pinned");
    return NULL;
  }

  /* get the length of the attribute name */
  status_size = H5Aget_name_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t) order, (hsize_t) n, (char*)NULL, (size_t)0, (hid_t)lapl_id);

  if(status_size < 0) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
    h5libraryError(env);
    return NULL;
  }
  buf_size = (size_t)status_size + 1;/* add extra space for the null terminator */

  aValue = (char*)malloc(sizeof(char) * buf_size);
  if (aValue == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
    h5outOfMemory( env, "H5Aget_name_by_idx:  malloc failed ");
    return NULL;
  }

  status_size = H5Aget_name_by_idx((hid_t)loc_id, aName, (H5_index_t)idx_type,
    (H5_iter_order_t) order, (hsize_t) n, (char*)aValue, (size_t)buf_size, (hid_t)lapl_id);

  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

  if (status_size < 0) {
    free(aValue);
    h5libraryError(env);
    return NULL;
  }
  /* may throw OutOfMemoryError */
  str = ENVPTR->NewStringUTF(ENVPAR aValue);
  if (str == NULL) {
    /* exception -- fatal JNI error */
    free(aValue);
    h5JNIFatalError( env, "H5Aget_name_by_idx:  return string not created");
    return NULL;
  }

  free(aValue);

  return str;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_storage_size
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1storage_1size
  (JNIEnv *env, jclass clss, jlong attr_id)
{
  hsize_t retVal = (hsize_t)-1;

    retVal = H5Aget_storage_size((hid_t)attr_id);
/* probably returns '0' if fails--don't do an exception
    if (retVal < 0) {
        h5libraryError(env);
    }
*/
    return (jlong)retVal;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info
 * Signature: (J)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info
  (JNIEnv *env, jclass clss, jlong attr_id)
{
    herr_t     status;
    H5A_info_t ainfo;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    status = H5Aget_info((hid_t)attr_id, (H5A_info_t*)&ainfo);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
    constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info_by_idx
 * Signature: (JLjava/lang/String;IIJJ)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info_1by_1idx
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jlong lapl_id)
{

    char      *aName;
    herr_t     status;
    H5A_info_t ainfo;
    jboolean   isCopy;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    if (obj_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_idx: obj_name is NULL");
        return NULL;
    }

    aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aget_info_by_idx: object name not pinned");
        return NULL;
    }

  status = H5Aget_info_by_idx((hid_t)loc_id, (const char*)aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (H5A_info_t*)&ainfo, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
  constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_info_by_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;J)Lncsa/hdf/hdf5lib/structs/H5A_info_t;
 */
JNIEXPORT jobject JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1info_1by_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring attr_name, jlong lapl_id)
{
    char      *aName;
    char    *attrName;
    herr_t     status;
    H5A_info_t ainfo;
    jboolean   isCopy;
    jclass     cls;
    jmethodID  constructor;
    jvalue     args[4];
    jobject    ret_info_t = NULL;

    if (obj_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_name: obj_name is NULL");
        return NULL;
    }
    if (attr_name == NULL) {
        h5nullArgument( env, "H5Aget_info_by_name: attr_name is NULL");
        return NULL;
    }
    aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aget_info_by_name: object name not pinned");
        return NULL;
    }
    attrName = (char*)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
        h5JNIFatalError( env, "H5Aget_info_by_name: Attribute name not pinned");
        return NULL;
    }
    status = H5Aget_info_by_name((hid_t)loc_id, (const char*)aName, (const char*)attrName,
                  (H5A_info_t*)&ainfo,(hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name, attrName);

    if (status < 0) {
       h5libraryError(env);
       return NULL;
    }

    // get a reference to your class if you don't have it already
    cls = ENVPTR->FindClass(ENVPAR "ncsa/hdf/hdf5lib/structs/H5A_info_t");
    // get a reference to the constructor; the name is <init>
    constructor = ENVPTR->GetMethodID(ENVPAR cls, "<init>", "(ZJIJ)V");
    args[0].z = ainfo.corder_valid;
    args[1].j = ainfo.corder;
    args[2].i = ainfo.cset;
    args[3].j = ainfo.data_size;
    ret_info_t = ENVPTR->NewObjectA(ENVPAR cls, constructor, args);
    return ret_info_t;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete_by_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;J)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete_1by_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring attr_name, jlong lapl_id)
{
   herr_t retVal;
   char *aName, *attrName;
   jboolean isCopy;

   if (obj_name == NULL) {
        h5nullArgument( env, "H5Adelete_by_name:  object name is NULL");
        return -1;
   }
   if (attr_name == NULL) {
        h5nullArgument( env, "H5Adelete_by_name:  attribute name is NULL");
        return -1;
   }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Adelete_by_name: aName is not pinned");
        return -1;
    }
    attrName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (attrName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
        h5JNIFatalError( env, "H5Adelete_by_name: attrName is not pinned");
        return -1;
    }
    retVal = H5Adelete_by_name((hid_t)loc_id, (const char*)aName, (const char*)attrName, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,aName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,attrName);

    if (retVal< 0) {
        h5libraryError(env);
    }
    return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aexists
 * Signature: (JLjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aexists
  (JNIEnv *env, jclass clss, jlong obj_id, jstring attr_name)
{
    char    *aName;
    jboolean isCopy;
    htri_t   bval = 0;

    if (attr_name == NULL) {
        h5nullArgument( env, "H5Aexists: attr_name is NULL");
        return JNI_FALSE;
    }
    aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR attr_name, &isCopy);
    if (aName == NULL) {
        h5JNIFatalError( env, "H5Aexists: attr_name not pinned");
        return JNI_FALSE;
    }

    bval = H5Aexists((hid_t)obj_id, (const char*)aName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name, aName);

    if (bval > 0) {
        return JNI_TRUE;
    }
    else if (bval == 0) {
        return JNI_FALSE;
    }
    else {
        h5libraryError(env);
        return JNI_FALSE;
    }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete_by_idx
 * Signature: (JLjava/lang/String;IIJJ)V
 */
JNIEXPORT void JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete_1by_1idx
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jint idx_type, jint order, jlong n, jlong lapl_id)
{
  char      *aName;
  herr_t     status;
  jboolean   isCopy;

  if (obj_name == NULL) {
    h5nullArgument( env, "H5Adelete_by_idx: obj_name is NULL");
    return;
  }

  aName = (char*)ENVPTR->GetStringUTFChars(ENVPAR obj_name, &isCopy);
  if (aName == NULL) {
    h5JNIFatalError( env, "H5Adelete_by_idx: obj_name not pinned");
    return;
  }

  status = H5Adelete_by_idx((hid_t)loc_id, (const char*)aName, (H5_index_t)idx_type,
    (H5_iter_order_t)order, (hsize_t)n, (hid_t)lapl_id);
  ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name, aName);

  if (status < 0) {
    h5libraryError(env);
    return;
  }
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    _H5Aopen_by_name
 * Signature: (JLjava/lang/String;Ljava/lang/String;JJ)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5__1H5Aopen_1by_1name
  (JNIEnv *env, jclass clss, jlong loc_id, jstring obj_name, jstring attr_name, jlong aapl_id, jlong lapl_id)

{
    hid_t status;
    char *aName, *oName;
    jboolean isCopy;

    if (obj_name == NULL) {
        h5nullArgument( env,"_H5Aopen_by_name:  obj_name is NULL");
        return -1;
    }
    if (attr_name == NULL) {
        h5nullArgument( env,"_H5Aopen_by_name:  attr_name is NULL");
        return -1;
    }

    oName = (char *)ENVPTR->GetStringUTFChars(ENVPAR obj_name,&isCopy);
    if (oName == NULL) {
        h5JNIFatalError( env,"_H5Aopen_by_name: obj_name is not pinned");
        return -1;
    }
    aName = (char *)ENVPTR->GetStringUTFChars(ENVPAR attr_name,&isCopy);
    if (aName == NULL) {
    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,oName);
        h5JNIFatalError( env,"_H5Aopen_by_name: attr_name is not pinned");
        return -1;
    }

    status = H5Aopen_by_name((hid_t)loc_id, oName, aName, (hid_t)aapl_id, (hid_t)lapl_id);

    ENVPTR->ReleaseStringUTFChars(ENVPAR obj_name,oName);
    ENVPTR->ReleaseStringUTFChars(ENVPAR attr_name,aName);

    if (status < 0) {
        h5libraryError(env);
    }
    return (jlong)status;

}

#ifdef __cplusplus
}
#endif
