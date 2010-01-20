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

//Information struct for object (for H5Oget_info/H5Oget_info_by_name/H5Oget_info_by_idx)
public class H5O_info_t {
    public long   fileno;     /* File number that object is located in */
    public long   addr;       /* Object address in file   */
    public int    type;       /* Basic object type (group, dataset, etc.) */
    public int    rc;     /* Reference count of object    */
    public long   atime;      /* Access time          */
    public long   mtime;      /* Modification time        */
    public long   ctime;      /* Change time          */
    public long   btime;      /* Birth time           */
    public long   num_attrs;  /* # of attributes attached to object */
    public H5O_hdr_info_t      hdr;            /* Object header information */
    /* Extra metadata storage for obj & attributes */
    public struct meta_size {
        public H5_ih_info_t   obj;             /* v1/v2 B-tree & local/fractal heap for groups, B-tree for chunked datasets */
        public H5_ih_info_t   attr;            /* v2 B-tree & heap for attributes */
    };
}
