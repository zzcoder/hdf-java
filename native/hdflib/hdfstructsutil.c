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
 *  This module contains code to translate between the HDF C union 'comp_info'
 *  and a subclass of the Java class CompInfo.
 *
 *  This is nasty and ugly and probably buggy.
 *
 */
#include "hdf.h"

#include "jni.h"

extern jboolean h4outOfMemory( JNIEnv *env, char *functName);

/*
 *  Get information from a Java HDFNewCompInfo object in to a C comp_info
 *  struct.
 *
 *  Extract information for the different types of compression.
 */

jboolean getNewCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf)
{
jfieldID jf;
jclass jc;
jint ctype;

    jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFNewCompInfo");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jf = (*env)->GetFieldID(env, jc, "ctype", "I");
    if (jf == NULL) {
        return JNI_FALSE;
    }
    ctype = (*env)->GetIntField(env, ciobj, jf);

    switch(ctype) {
    case COMP_CODE_NONE:
    case COMP_CODE_RLE:
    default:
        break;

    case COMP_CODE_SKPHUFF:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFSKPHUFFCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "skp_size", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->skphuff.skp_size = (*env)->GetIntField(env, ciobj, jf);
        break;

    case COMP_CODE_DEFLATE:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFDeflateCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "level", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->deflate.level = (*env)->GetIntField(env, ciobj, jf);
        break;
    case COMP_CODE_SZIP:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFSZIPCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "bits_per_pixel", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }

        cinf->szip.bits_per_pixel = (*env)->GetIntField(env, ciobj, jf);
        jf = (*env)->GetFieldID(env, jc, "options_mask", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->szip.options_mask = (*env)->GetIntField(env, ciobj, jf);

/*      changes from hdf-42r0 to hdf-42r1
        jf = (*env)->GetFieldID(env, jc, "compression_mode", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->szip.compression_mode = (*env)->GetIntField(env, ciobj, jf);
*/

        jf = (*env)->GetFieldID(env, jc, "pixels", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->szip.pixels = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "pixels_per_block", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->szip.pixels_per_block = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "pixels_per_scanline", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->szip.pixels_per_scanline = (*env)->GetIntField(env, ciobj, jf);
        break;
    case COMP_CODE_NBIT:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFNBITCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "nt", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->nbit.nt = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "sign_ext", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->nbit.sign_ext = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "fill_one", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->nbit.fill_one = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "start_bit", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->nbit.start_bit = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "bit_len", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->nbit.bit_len = (*env)->GetIntField(env, ciobj, jf);
        break;
    }

    return JNI_TRUE;
}

/*
 *   Extract info from C comp_info struct, put in Java HDFCompInfo object.
 *
 *   Put in the fields for each compression method.
 */
jboolean setNewCompInfo( JNIEnv *env, jobject ciobj, comp_coder_t coder,
comp_info *cinf)
{
jfieldID jf;
jclass jc;

    jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFCompInfo");

    if (jc == NULL) {
        return JNI_FALSE;
    } else {
        jf = (*env)->GetFieldID(env, jc, "ctype", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, coder);
    }


    switch(coder) {
    case COMP_CODE_NONE:
    case COMP_CODE_RLE:
    default:
        break;
    case COMP_CODE_SKPHUFF:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFSKPHUFFCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "ctype", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, COMP_CODE_SKPHUFF);
        jf = (*env)->GetFieldID(env, jc, "skp_size", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->skphuff.skp_size );
        break;

    case COMP_CODE_DEFLATE:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFDeflateCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "ctype", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, COMP_CODE_DEFLATE);
        jf = (*env)->GetFieldID(env, jc, "level", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->deflate.level );
        break;
    case COMP_CODE_SZIP:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFSZIPCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "ctype", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, COMP_CODE_SZIP);

        jf = (*env)->GetFieldID(env, jc, "bits_per_pixel", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.bits_per_pixel);

        jf = (*env)->GetFieldID(env, jc, "options_mask", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.options_mask);

/*   changes from hdf-42r0 to hdf-42r1
        jf = (*env)->GetFieldID(env, jc, "compression_mode", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.compression_mode);
*/

        jf = (*env)->GetFieldID(env, jc, "pixels", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.pixels);

        jf = (*env)->GetFieldID(env, jc, "pixels_per_block", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.pixels_per_block);

        jf = (*env)->GetFieldID(env, jc, "pixels_per_scanline", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->szip.pixels_per_scanline);
        break;
    case COMP_CODE_NBIT:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFNBITCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }

        jf = (*env)->GetFieldID(env, jc, "ctype", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, COMP_CODE_NBIT);
        jf = (*env)->GetFieldID(env, jc, "nt", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->nbit.nt);

        jf = (*env)->GetFieldID(env, jc, "sign_ext", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->nbit.sign_ext );

        jf = (*env)->GetFieldID(env, jc, "fill_one", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->nbit.fill_one);

        jf = (*env)->GetFieldID(env, jc, "start_bit", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->nbit.start_bit );

        jf = (*env)->GetFieldID(env, jc, "bit_len", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        (*env)->SetIntField(env, ciobj, jf, cinf->nbit.bit_len);
        break;
    }

    return JNI_TRUE;
}


/*
 *     Get info from old style C comp_info struct, put in HDFCompInfo object.
 */
jboolean getOldCompInfo( JNIEnv *env, jobject ciobj, comp_info *cinf)
{
jfieldID jf;
jclass jc;
jint ctype;

    jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFOldCompInfo");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jf = (*env)->GetFieldID(env, jc, "ctype", "I");
    if (jf == NULL) {
        return JNI_FALSE;
    }
    ctype = (*env)->GetIntField(env, ciobj, jf);

    switch(ctype) {
    case COMP_NONE:
    case COMP_RLE:
    case COMP_IMCOMP:
    default:
        break;

    case COMP_JPEG:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFJPEGCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "quality", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->jpeg.quality = (*env)->GetIntField(env, ciobj, jf);

        jf = (*env)->GetFieldID(env, jc, "force_baseline", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        cinf->jpeg.force_baseline = (*env)->GetIntField(env, ciobj, jf);
        break;
    }

    return JNI_TRUE;
}

/*
 *  Get the Chunk info from C HDF_CHUNK_DEF struct, put in
 *  Java HDFChunkInfo object.
 */
jboolean getChunkInfo( JNIEnv *env, jobject chunkobj, HDF_CHUNK_DEF *cinf)
{
jfieldID jf;
jclass jc;
jint ctype;
jobject larr;
jint * lens;
int i;
jboolean bval;
jboolean bb;

    jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFChunkInfo");
    if (jc == NULL) {
        return JNI_FALSE;
    }
    jf = (*env)->GetFieldID(env, jc, "ctype", "I");
    if (jf == NULL) {
        return JNI_FALSE;
    }
    ctype = (*env)->GetIntField(env, chunkobj, jf);

    jf = (*env)->GetFieldID(env, jc, "chunk_lengths", "[I");
    if (jf == NULL) {
        return JNI_FALSE;
    }
    larr = (*env)->GetObjectField(env,chunkobj,jf);
    if (larr == NULL) {
        return JNI_FALSE;
    }

    lens = (jint *)(*env)->GetIntArrayElements(env,(jintArray)larr,&bb);

    for (i = 0; i < MAX_VAR_DIMS; i++) {
        cinf->comp.chunk_lengths[i] = (int32)lens[i];
    }

    (*env)->ReleaseIntArrayElements(env,(jintArray)larr,(jint *)lens,JNI_ABORT);

    if (ctype == (HDF_CHUNK | HDF_COMP)) {
        jf = (*env)->GetFieldID(env, jc, "comp_type", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->comp.comp_type = (*env)->GetIntField(env, chunkobj, jf);

        jf = (*env)->GetFieldID(env, jc, "model_type", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->comp.model_type = (*env)->GetIntField(env, chunkobj, jf);

        jf = (*env)->GetFieldID(env, jc, "cinfo", "Lncsa/hdf/hdflib/HDFCompInfo;");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        larr = (*env)->GetObjectField(env,chunkobj,jf);
        if (larr == NULL) {
            return JNI_FALSE;
        }
        bval = getNewCompInfo(env,(jobject)larr,&(cinf->comp.cinfo));
     } else if (ctype == (HDF_CHUNK | HDF_NBIT)) {
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFNBITChunkInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jf = (*env)->GetFieldID(env, jc, "chunk_lengths", "[I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
        larr = (*env)->GetObjectField(env,chunkobj,jf);
        if (larr == NULL) {
            return JNI_FALSE;
        }

        lens = (jint *)(*env)->GetIntArrayElements(env,(jintArray)larr,&bb);

        for (i = 0; i < MAX_VAR_DIMS; i++) {
            cinf->nbit.chunk_lengths[i] = (int32)lens[i];
        }

        (*env)->ReleaseIntArrayElements(env,(jintArray)larr,(jint *)lens,JNI_ABORT);

        jf = (*env)->GetFieldID(env, jc, "start_bit", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->nbit.start_bit = (*env)->GetIntField(env, chunkobj, jf);

        jf = (*env)->GetFieldID(env, jc, "bit_len", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->nbit.bit_len = (*env)->GetIntField(env, chunkobj, jf);

        jf = (*env)->GetFieldID(env, jc, "sign_ext", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->nbit.sign_ext = (*env)->GetIntField(env, chunkobj, jf);

        jf = (*env)->GetFieldID(env, jc, "fill_one", "I");
        if (jf == NULL) {
            return JNI_FALSE;
        }
                cinf->nbit.fill_one = (*env)->GetIntField(env, chunkobj, jf);
    }
    return JNI_TRUE;
}

/*
 *  Create C HDF_CHUNK_DEF struct from Java HDFChunkInfo object.
 *
 *  Determine the compression method, and create an appropriate subclass
 *  of HDFCompInfo.  Then call the constructor for HDFChunkInfo.
 */
jboolean makeChunkInfo( JNIEnv *env, jobject chunkobj, int32 flgs, HDF_CHUNK_DEF *cinf)
{
jclass jc;
jclass jci;
jmethodID jmi;
jintArray rarray;
jobject compinfo;

    rarray = (*env)->NewIntArray(env,MAX_VAR_DIMS);
    if (rarray == NULL) {
        return JNI_FALSE;
    }
    (*env)->SetIntArrayRegion(env,rarray,0,MAX_VAR_DIMS,(jint *)cinf->chunk_lengths);

        /* release rarray? */

    jci = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFChunkInfo");
    if (jci == NULL) {
        return JNI_FALSE;
    }

    switch (flgs) {
        case HDF_CHUNK:
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFCompInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jmi = (*env)->GetMethodID(env, jc, "<init>", "()V");
        if (jmi == NULL) {
            return JNI_FALSE;
        }
        compinfo = (*env)->NewObject(env,jc,jmi);
        break;
        case (HDF_CHUNK | HDF_COMP):
        switch (cinf->comp.comp_type) {
            case COMP_CODE_NONE:
            default:
                jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFCompInfo");
                if (jc == NULL) {
                    return JNI_FALSE;
                }
                jmi = (*env)->GetMethodID(env, jc, "<init>", "()V");
                if (jmi == NULL) {
                    return JNI_FALSE;
                }
                compinfo = (*env)->NewObject(env,jc,jmi);
                break;
            case COMP_CODE_JPEG:
                /* new HDFJPEGCompInfo() */
                jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFJPEGCompInfo");
                if (jc == NULL) {
                    return JNI_FALSE;
                }
                jmi = (*env)->GetMethodID(env, jc, "<init>", "(II)V");
                if (jmi == NULL) {
                    return JNI_FALSE;
                }
                compinfo = (*env)->NewObject(env,jc,jmi,
                    cinf->comp.cinfo.jpeg.quality,
                    cinf->comp.cinfo.jpeg.force_baseline);
                break;
            case COMP_CODE_DEFLATE:
                jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFDeflateCompInfo");
                if (jc == NULL) {
                    return JNI_FALSE;
                }
                jmi = (*env)->GetMethodID(env, jc, "<init>", "(I)V");
                if (jmi == NULL) {
                    return JNI_FALSE;
                }
                compinfo = (*env)->NewObject(env,jc,jmi, cinf->comp.cinfo.deflate.level);
                break;
            case COMP_CODE_SZIP:
                jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFSZIPCompInfo");
                if (jc == NULL) {
                    return JNI_FALSE;
                }
                jmi = (*env)->GetMethodID(env, jc, "<init>", "(IIIIII)V");
                if (jmi == NULL) {
                    return JNI_FALSE;
                }
                compinfo = (*env)->NewObject(env,jc,jmi,
                        cinf->comp.cinfo.szip.bits_per_pixel,
                        cinf->comp.cinfo.szip.options_mask,
                        cinf->comp.cinfo.szip.pixels,
                        cinf->comp.cinfo.szip.pixels_per_block,
                        cinf->comp.cinfo.szip.pixels_per_scanline);
                break;
        }
        break;
        case (HDF_CHUNK | HDF_NBIT):
        /* new HDFCompInfo() */
        jc = (*env)->FindClass(env, "ncsa/hdf/hdflib/HDFNBITChunkInfo");
        if (jc == NULL) {
            return JNI_FALSE;
        }
        jmi = (*env)->GetMethodID(env, jc, "<init>",
            "([IIIII;)V");
        if (jmi == NULL) {
            return JNI_FALSE;
        }
        (*env)->CallVoidMethod(env,chunkobj,jmi, rarray,
            cinf->nbit.start_bit,
            cinf->nbit.bit_len,
            cinf->nbit.sign_ext,
            cinf->nbit.fill_one);
        return JNI_TRUE;
        break;
        }

    jmi = (*env)->GetMethodID(env, jci, "<init>",
        "([IILncsa/hdf/hdflib/HDFCompInfo;)V");
    if (jmi == NULL) {
        return JNI_FALSE;
    }
    (*env)->CallVoidMethod(env,chunkobj,jmi, rarray, cinf->comp.comp_type,
            compinfo);

    return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_ncsa_hdf_hdflib_HDFLibrary_HCget_1config_1info
( JNIEnv *env,
jclass class,
jint coder_type) /* out: CompInfo */
{
	intn rval;
	uint32 compression_config_info=0;
	
	/* check for success... */
	rval = HCget_config_info( (comp_coder_t) coder_type, (uint32*)&compression_config_info);

	if (rval == FAIL)
		return -1;
	
	return compression_config_info;
}



