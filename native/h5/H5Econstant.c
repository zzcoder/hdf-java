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

#include "hdf5.h"
#include "H5Econstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Econstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Econstant_JH5E_ALIGNMENT : return H5E_ALIGNMENT;
    case hdf_h5_constants_H5Econstant_JH5E_ALREADYEXISTS : return H5E_ALREADYEXISTS;
    case hdf_h5_constants_H5Econstant_JH5E_ALREADYINIT : return H5E_ALREADYINIT;
    case hdf_h5_constants_H5Econstant_JH5E_ARGS : return H5E_ARGS;
    case hdf_h5_constants_H5Econstant_JH5E_ATOM : return H5E_ATOM;
    case hdf_h5_constants_H5Econstant_JH5E_ATTR : return H5E_ATTR;
    case hdf_h5_constants_H5Econstant_JH5E_BADATOM : return H5E_BADATOM;
    case hdf_h5_constants_H5Econstant_JH5E_BADFILE : return H5E_BADFILE;
    case hdf_h5_constants_H5Econstant_JH5E_BADGROUP : return H5E_BADGROUP;
    case hdf_h5_constants_H5Econstant_JH5E_BADMESG : return H5E_BADMESG;
    case hdf_h5_constants_H5Econstant_JH5E_BADRANGE : return H5E_BADRANGE;
    case hdf_h5_constants_H5Econstant_JH5E_BADSELECT : return H5E_BADSELECT;
    case hdf_h5_constants_H5Econstant_JH5E_BADSIZE : return H5E_BADSIZE;
    case hdf_h5_constants_H5Econstant_JH5E_BADTYPE : return H5E_BADTYPE;
    case hdf_h5_constants_H5Econstant_JH5E_BADVALUE : return H5E_BADVALUE;
    case hdf_h5_constants_H5Econstant_JH5E_BTREE : return H5E_BTREE;
    case hdf_h5_constants_H5Econstant_JH5E_CACHE : return H5E_CACHE;
    case hdf_h5_constants_H5Econstant_JH5E_CALLBACK : return H5E_CALLBACK;
    case hdf_h5_constants_H5Econstant_JH5E_CANAPPLY : return H5E_CANAPPLY;
    case hdf_h5_constants_H5Econstant_JH5E_CANTALLOC : return H5E_CANTALLOC;
    /*case hdf_h5_constants_H5Econstant_JH5E_CANTCHANGE : return H5E_CANTCHANGE;*/
    case hdf_h5_constants_H5Econstant_JH5E_CANTCLIP : return H5E_CANTCLIP;
    case hdf_h5_constants_H5Econstant_JH5E_CANTCLOSEFILE : return H5E_CANTCLOSEFILE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTCONVERT : return H5E_CANTCONVERT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTCOPY : return H5E_CANTCOPY;
    case hdf_h5_constants_H5Econstant_JH5E_CANTCOUNT : return H5E_CANTCOUNT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTCREATE : return H5E_CANTCREATE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTDEC : return H5E_CANTDEC;
    case hdf_h5_constants_H5Econstant_JH5E_CANTDECODE : return H5E_CANTDECODE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTDELETE : return H5E_CANTDELETE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTENCODE : return H5E_CANTENCODE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTFLUSH : return H5E_CANTFLUSH;
    case hdf_h5_constants_H5Econstant_JH5E_CANTFREE : return H5E_CANTFREE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTGET : return H5E_CANTGET;
    case hdf_h5_constants_H5Econstant_JH5E_CANTINC : return H5E_CANTINC;
    case hdf_h5_constants_H5Econstant_JH5E_CANTINIT : return H5E_CANTINIT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTINSERT : return H5E_CANTINSERT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTLIST : return H5E_CANTLIST;
    case hdf_h5_constants_H5Econstant_JH5E_CANTLOAD : return H5E_CANTLOAD;
    case hdf_h5_constants_H5Econstant_JH5E_CANTLOCK : return H5E_CANTLOCK;
    /*case hdf_h5_constants_H5Econstant_JH5E_CANTMAKETREE : return H5E_CANTMAKETREE; */
    case hdf_h5_constants_H5Econstant_JH5E_CANTNEXT : return H5E_CANTNEXT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTOPENFILE : return H5E_CANTOPENFILE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTOPENOBJ : return H5E_CANTOPENOBJ;
    case hdf_h5_constants_H5Econstant_JH5E_CANTRECV : return H5E_CANTRECV;
    case hdf_h5_constants_H5Econstant_JH5E_CANTREGISTER : return H5E_CANTREGISTER;
    case hdf_h5_constants_H5Econstant_JH5E_CANTRELEASE : return H5E_CANTRELEASE;
    case hdf_h5_constants_H5Econstant_JH5E_CANTSELECT : return H5E_CANTSELECT;
    /*case hdf_h5_constants_H5Econstant_JH5E_CANTSENDMDATA : return H5E_CANTSENDMDATA; */
    case hdf_h5_constants_H5Econstant_JH5E_CANTSET : return H5E_CANTSET;
    case hdf_h5_constants_H5Econstant_JH5E_CANTSPLIT : return H5E_CANTSPLIT;
    case hdf_h5_constants_H5Econstant_JH5E_CANTUNLOCK : return H5E_CANTUNLOCK;
    case hdf_h5_constants_H5Econstant_JH5E_CLOSEERROR : return H5E_CLOSEERROR;
    case hdf_h5_constants_H5Econstant_JH5E_COMPLEN : return H5E_COMPLEN;
    /*case hdf_h5_constants_H5Econstant_JH5E_CWG : return H5E_CWG; */
    case hdf_h5_constants_H5Econstant_JH5E_DATASET : return H5E_DATASET;
    case hdf_h5_constants_H5Econstant_JH5E_DATASPACE : return H5E_DATASPACE;
    case hdf_h5_constants_H5Econstant_JH5E_DATATYPE : return H5E_DATATYPE;
    case hdf_h5_constants_H5Econstant_JH5E_DEFAULT : return H5E_DEFAULT;
    case hdf_h5_constants_H5Econstant_JH5E_DUPCLASS : return H5E_DUPCLASS;
    case hdf_h5_constants_H5Econstant_JH5E_EFL : return H5E_EFL;
    case hdf_h5_constants_H5Econstant_JH5E_EXISTS : return H5E_EXISTS;
    case hdf_h5_constants_H5Econstant_JH5E_FCNTL : return H5E_FCNTL;
    case hdf_h5_constants_H5Econstant_JH5E_FILE : return H5E_FILE;
    case hdf_h5_constants_H5Econstant_JH5E_FILEEXISTS : return H5E_FILEEXISTS;
    case hdf_h5_constants_H5Econstant_JH5E_FILEOPEN : return H5E_FILEOPEN;
    /*case hdf_h5_constants_H5Econstant_JH5E_FPHDF5 : return H5E_FPHDF5;*/
    case hdf_h5_constants_H5Econstant_JH5E_FUNC : return H5E_FUNC;
    case hdf_h5_constants_H5Econstant_JH5E_HEAP : return H5E_HEAP;
    case hdf_h5_constants_H5Econstant_JH5E_INTERNAL : return H5E_INTERNAL;
    case hdf_h5_constants_H5Econstant_JH5E_IO : return H5E_IO;
    case hdf_h5_constants_H5Econstant_JH5E_LINK : return H5E_LINK;
    case hdf_h5_constants_H5Econstant_JH5E_LINKCOUNT : return H5E_LINKCOUNT;
    case hdf_h5_constants_H5Econstant_JH5E_MOUNT : return H5E_MOUNT;
    case hdf_h5_constants_H5Econstant_JH5E_MPI : return H5E_MPI;
    case hdf_h5_constants_H5Econstant_JH5E_MPIERRSTR : return H5E_MPIERRSTR;
    case hdf_h5_constants_H5Econstant_JH5E_NOFILTER : return H5E_NOFILTER;
    case hdf_h5_constants_H5Econstant_JH5E_NOIDS : return H5E_NOIDS;
    case hdf_h5_constants_H5Econstant_JH5E_NONE_MAJOR : return H5E_NONE_MAJOR;
    case hdf_h5_constants_H5Econstant_JH5E_NONE_MINOR : return H5E_NONE_MINOR;
    case hdf_h5_constants_H5Econstant_JH5E_NOSPACE : return H5E_NOSPACE;
    case hdf_h5_constants_H5Econstant_JH5E_NOTCACHED : return H5E_NOTCACHED;
    case hdf_h5_constants_H5Econstant_JH5E_NOTFOUND : return H5E_NOTFOUND;
    case hdf_h5_constants_H5Econstant_JH5E_NOTHDF5 : return H5E_NOTHDF5;
    case hdf_h5_constants_H5Econstant_JH5E_OHDR : return H5E_OHDR;
    case hdf_h5_constants_H5Econstant_JH5E_OVERFLOW : return H5E_OVERFLOW;
    case hdf_h5_constants_H5Econstant_JH5E_PLINE : return H5E_PLINE;
    case hdf_h5_constants_H5Econstant_JH5E_PLIST : return H5E_PLIST;
    case hdf_h5_constants_H5Econstant_JH5E_PROTECT : return H5E_PROTECT;
    case hdf_h5_constants_H5Econstant_JH5E_READERROR : return H5E_READERROR;
    case hdf_h5_constants_H5Econstant_JH5E_REFERENCE : return H5E_REFERENCE;
    case hdf_h5_constants_H5Econstant_JH5E_RESOURCE : return H5E_RESOURCE;
    case hdf_h5_constants_H5Econstant_JH5E_RS : return H5E_RS;
    case hdf_h5_constants_H5Econstant_JH5E_SEEKERROR : return H5E_SEEKERROR;
    case hdf_h5_constants_H5Econstant_JH5E_SETLOCAL : return H5E_SETLOCAL;
    /*case hdf_h5_constants_H5Econstant_JH5E_SLINK : return H5E_SLINK;*/
    case hdf_h5_constants_H5Econstant_JH5E_STORAGE : return H5E_STORAGE;
    case hdf_h5_constants_H5Econstant_JH5E_SYM : return H5E_SYM;
    /*case hdf_h5_constants_H5Econstant_JH5E_TBBT : return H5E_TBBT */;
    case hdf_h5_constants_H5Econstant_JH5E_TRUNCATED : return H5E_TRUNCATED;
    case hdf_h5_constants_H5Econstant_JH5E_TST : return H5E_TST;
    case hdf_h5_constants_H5Econstant_JH5E_UNINITIALIZED : return H5E_UNINITIALIZED;
    case hdf_h5_constants_H5Econstant_JH5E_UNSUPPORTED : return H5E_UNSUPPORTED;
    case hdf_h5_constants_H5Econstant_JH5E_VERSION : return H5E_VERSION;
    case hdf_h5_constants_H5Econstant_JH5E_VFL : return H5E_VFL;
    case hdf_h5_constants_H5Econstant_JH5E_WRITEERROR : return H5E_WRITEERROR;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
