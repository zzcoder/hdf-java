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

import ncsa.hdf.hdf5lib.callbacks.H5L_copy_func_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_create_func_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_delete_func_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_move_func_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_query_func_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_traverse_func_cb;


//Information struct for object (for H5Oget_info/H5Oget_info_by_name/H5Oget_info_by_idx)
public class H5L_class_t {
    public int version;                     // Version number of this struct
    public int id;                          // Link type ID 
    public String comment;                  // Comment for debugging 
    public H5L_create_func_cb create_func;  // Callback during link creation 
    public H5L_move_func_cb move_func;      // Callback after moving link 
    public H5L_copy_func_cb copy_func;      // Callback after copying link 
    public H5L_traverse_func_cb trav_func;  // Callback during link traversal 
    public H5L_delete_func_cb del_func;     // Callback for link deletion 
    public H5L_query_func_cb query_func;    // Callback for queries 
}
