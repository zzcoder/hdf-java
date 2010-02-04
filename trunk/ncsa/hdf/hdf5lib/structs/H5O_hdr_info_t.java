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

package ncsa.hdf.hdf5lib.structs;

// Information struct for object header metadata (for H5Oget_info/H5Oget_info_by_name/H5Oget_info_by_idx)
public class H5O_hdr_info_t {
    public int version;       /* Version number of header format in file */
    public int nmesgs;        /* Number of object header messages */
    public int nchunks;       /* Number of object header chunks */
    public int flags;             /* Object header status flags */
    public struct space{
        public long total;      /* Total space for storing object header in file */
        public long meta;       /* Space within header for object header metadata information */
        public long mesg;       /* Space within header for actual message information */
        public long free;       /* Free space within object header */
    };
    public class mesg {
        public long present;   /* Flags to indicate presence of message type in header */
        public long shared;    /* Flags to indicate message type is shared in header */
    } ;
}
