
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF5.  The full HDF5 copyright notice, including     *
 * terms governing use, modification, and redistribution, is contained in    *
 * the files COPYING and Copyright.html.  COPYING can be found at the root   *
 * of the source code distribution tree; Copyright.html can be found at the  *
 * root level of an installed copy of the electronic HDF5 document set and   *
 * is linked from the top-level documents page.  It can also be found at     *
 * http://hdf.ncsa.uiuc.edu/HDF5/doc/Copyright.html.  If you do not have     *
 * access to either file, you may request a copy from hdfhelp@ncsa.uiuc.edu. *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

#ifndef _H5HANDLER_H
#define _H5HANDLER_H

#include "rodsClient.h"

struct openedH5File {
    int fid;	/* the fid of the opened H5File */
    struct hostElement  *hostTabPtr;	/* host table where the file is 
					 * located */
    struct openedH5File *next;    /* link list pointer */
};

#ifdef __cplusplus
extern "C" {
#endif

/* Client side API:
 *     send client request to the server and process the response from server 
 */
int h5ObjRequest(rcComm_t *conn, void *obj, int objID);

/* Server side API:
 *     process request from the client and send result to the client
 */
#if 0	/* XXXX rm for irods */
int h5ObjProcess(genFunctType_t functType, int objID, int intInput2,
char *strInput1, char *strInput2, bytesBuf_t *bytesBufInp,
bytesBuf_t *bytesBufOut, int maxOutSz);
int
addOpenedHdf5File (int fid, struct hostElement *hostTabPtr);
struct hostElement *getHostByFid (int fid);
#endif
#ifdef __cplusplus
}
#endif

#endif


