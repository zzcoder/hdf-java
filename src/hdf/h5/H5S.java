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

package hdf.h5;

import hdf.h5.enums.H5S_CLASS;
import hdf.h5.enums.H5S_SEL;
import hdf.h5.enums.H5S_SELECT_OPER;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//          H5S: Dataspace Interface Functions              //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5S {

  /**
   *  H5Screate creates a new dataspace of a particular type.
   *
   *  @param type IN: The type of dataspace to be created.
   *
   *  @return a dataspace identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Screate(H5S_CLASS type)
  throws HDF5LibraryException;

  /**
   *  H5Screate_simple creates a new simple data space and opens
   *  it for access.
   *
   *  @param rank    IN: Number of dimensions of dataspace.
   *  @param dims    IN: An array of the size of each dimension.
   *  @param maxdims IN: An array of the maximum size of each dimension.
   *
   *  @return a dataspace identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - dims or maxdims is null.
   **/
  public synchronized static native int H5Screate_simple(int rank, long dims[], long maxdims[])
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sset_extent_simple sets or resets the size of an existing
   *  dataspace.
   *
   *  @param space_id     IN: Dataspace identifier.
   *  @param rank         IN: Rank, or dimensionality, of the dataspace.
   *  @param current_size IN: Array containing current size of dataspace.
   *  @param maximum_size IN: Array containing maximum size of dataspace.
   *
   *  @return a dataspace identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Sset_extent_simple(int space_id, int rank, long dims[], long max[])
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Scopy creates a new dataspace which is an exact copy of the
   *  dataspace identified by space_id.
   *
   *  @param space_id  IN: Identifier of dataspace to copy.
   *
   *  @return a dataspace identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Scopy(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sclose releases a dataspace.
   *
   *  @param space_id  IN: Identifier of dataspace to release.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sclose(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sencode converts a data space description into binary form in a buffer.
   *
   *  @param obj_id   IN: Identifier of the object to be encoded.
   *  @param buf     OUT: Buffer for the object to be encoded into. 
   *                      If the provided buffer is NULL, only the 
   *                      size of buffer needed is returned.
   *  @param nalloc   IN: The size of the allocated buffer.
   *
   *  @return the size needed for the allocated buffer.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Sencode(int obj_id, byte[] buf, long[] nalloc)
  throws HDF5LibraryException, NullPointerException;
  /**
   *  H5Sencode converts a data space description into binary form in a buffer.
   *
   *  @param obj_id   IN: Identifier of the object to be encoded.
   *
   *  @return the buffer for the object to be encoded into.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native byte[] H5Sencode(int obj_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sdecode reconstructs the HDF5 data space object and returns a 
   *  new object handle for it.
   *
   *  @param buf   IN: Buffer for the data space object to be decoded.
   *
   *  @return a new object handle
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native int H5Sdecode(byte[] buf)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sget_simple_extent_npoints determines the number of elements
   *  in a dataspace.
   *
   *  @param space_id IN: Identifier of the dataspace object to query
   *
   *  @return the number of elements in the dataspace
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Sget_simple_extent_npoints(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sget_simple_extent_ndims determines the dimensionality
   *  (or rank) of a dataspace.
   *
   *  @param space_id  IN: Identifier of the dataspace
   *
   *  @return the number of dimensions in the dataspace
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Sget_simple_extent_ndims(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sget_simple_extent_dims returns the size and maximum sizes of
   *  each dimension of a dataspace through the dims and maxdims parameters.
   *
   *  @param space_id  IN: Identifier of the dataspace object to query
   *  @param dims     OUT: Pointer to array to store the size of each dimension.
   *  @param maxdims  OUT: Pointer to array to store the maximum size of each dimension.
   *
   *  @return the number of dimensions in the dataspace 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - dims or maxdims is null.
   **/
  public synchronized static native int H5Sget_simple_extent_dims(int space_id, long dims[], long maxdims[])
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sis_simple determines whether a dataspace is a simple dataspace.
   *
   *  @param space_id  IN: Identifier of the dataspace to query
   *
   *  @return true if dataspace is a simple dataspace
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Sis_simple(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sget_select_npoints determines the number of elements in the
   *  current selection of a dataspace.
   *
   *  @param space_id IN: Dataspace identifier.
   *
   *  @return the number of elements in the selection
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Sget_select_npoints(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sselect_hyperslab selects a hyperslab region to add to
   *  the current selected region for the dataspace specified
   *  by space_id.  The start, stride, count, and block arrays
   *  must be the same size as the rank of the dataspace.
   *
   *  @param space_id IN: Identifier of dataspace selection to modify
   *  @param op       IN: Operation to perform on current selection.
   *  @param start    IN: Offset of start of hyperslab
   *  @param count    IN: Number of blocks included in hyperslab.
   *  @param stride   IN: Hyperslab stride.
   *  @param block    IN: Size of block in hyperslab.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - an input array is null.
   *  @exception IllegalArgumentException - an input array is invalid.
   **/
  public synchronized static native void H5Sselect_hyperslab(int space_id, H5S_SELECT_OPER op,
      long start[], long _stride[], long count[], long _block[])
  throws HDF5LibraryException, NullPointerException, IllegalArgumentException;
  /* #define NEW_HYPERSLAB_API */
//  #ifdef NEW_HYPERSLAB_API
//  public synchronized static native int H5Scombine_hyperslab(int space_id, H5S_SELECT_OPER op,
//             const long start[], const long _stride[],
//             const long count[], const long _block[])
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Sselect_select(int space1_id, H5S_SELECT_OPER op,
//                                    int space2_id)
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Scombine_select(int space1_id, H5S_SELECT_OPER op,
//                                    int space2_id)
//  throws HDF5LibraryException, NullPointerException;
//  #endif /* NEW_HYPERSLAB_API */

  /**
   *  H5Sselect_elements selects array elements to be included in the
   *  selection for the space_id dataspace.
   *
   *  @param space_id     IN: Identifier of the dataspace.
   *  @param op           IN: operator specifying how the new selection 
   *                           is combined.
   *  @param num_elem     IN: Number of elements to be selected.
   *  @param coord        IN: A 2-dimensional array specifying the 
   *                           coordinates of the elements.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sselect_elements(int space_id, H5S_SELECT_OPER op,
      long num_elem, long[] coord)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sget_simple_extent_type queries a dataspace to determine the
   *  current class of a dataspace.
   *
   *  @param space_id  IN: Dataspace identifier.
   *
   *  @return a dataspace class name
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5S_CLASS H5Sget_simple_extent_type(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sset_extent_none removes the extent from a dataspace and
   *  sets the type to H5S_NONE.
   *
   *  @param space_id ID: The identifier for the dataspace from
   *                       which the extent is to be removed.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sset_extent_none(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sextent_copy copies the extent from source_space_id to
   *  dest_space_id. This action may change the type of the
   *  dataspace.
   *
   *  @param dest_space_id    IN: The identifier for the dataspace
   *                               from which the extent is copied.
   *  @param source_space_id  IN: The identifier for the
   *                               dataspace to which the extent is copied.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sextent_copy(int dest_space_id, int source_space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sextent_equal determines whether the dataspace extents of two 
   *  dataspaces, space1_id and space2_id, are equal. 
   *
   *  @param space1_id   IN: First dataspace identifier.
   *  @param space2_id   IN: Second dataspace identifier.
   *
   *  @return true if the dataspace extents of two dataspaces are equal.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Sextent_equal(int space1_id, int space2_id)
  throws HDF5LibraryException;

  /**
   *  H5Sselect_all selects the entire extent of the dataspace space_id.
   *
   *  @param space_id  IN: The identifier of the dataspace to be selected.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sselect_all(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sselect_none resets the selection region for the
   *  dataspace space_id to include no elements.
   *
   *  @param space_id  IN: The identifier of the dataspace to be reset.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Sselect_none(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Soffset_simple sets the offset of a simple dataspace space_id. The 
   *  offset array must be the same number of elements as the number of 
   *  dimensions for the dataspace. If the offset array is set to null, the 
   *  offset for the dataspace is reset to 0.
   *
   *  @param space_id  IN: The identifier for the dataspace object to reset.
   *  @param offset    IN: The offset at which to position the selection.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Soffset_simple(int space_id, long[] offset)
  throws HDF5LibraryException;

  /**
   *  H5Sselect_valid verifies that the selection for the dataspace.
   *
   *  @param space_id IN: The identifier for the dataspace being queried.
   *
   *  @return true if the selection is contained within the extent.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Sselect_valid(int space_id)
  throws HDF5LibraryException;

  /**
   *  H5Sget_select_hyper_nblocks returns the number of hyperslab blocks in the current dataspace selection.
   *
   *  @param spaceid  IN: The identifier for the dataspace being queried.
   *
   *  @return the number of hyperslab blocks in the current dataspace selection 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Sget_select_hyper_nblocks(int spaceid)
  throws HDF5LibraryException;

  /**
   *  H5Sget_select_elem_npoints returns the number of element points in the current dataspace selection.
   *
   *  @param spaceid  IN: Identifier of dataspace to release.
   *
   *  @return the number of element points in the current dataspace selection.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Sget_select_elem_npoints(int spaceid)
  throws HDF5LibraryException;

  /**
   *  H5Sget_select_hyper_blocklist returns an array of hyperslab blocks. The 
   *  block coordinates have the same dimensionality (rank) as the dataspace 
   *  they are located within. The list of blocks is formatted as follows:
   *  <ul>
   *    <li>"start" coordinate, immediately followed by</li>
   *    <li>"opposite" corner coordinate, followed by</li>
   *    <li>the next "start" and "opposite" coordinates,</li>
   *    <li>etc.</li>
   *    <li>until all of the selected blocks have been listed.</li>
   * </ul>
   *
   *  @param spaceid     IN: Identifier of dataspace to release.
   *  @param startblock  IN: First block to retrieve
   *  @param numblocks   IN: Number of blocks to retrieve
   *
   *  @return a list of hyperslab blocks selected from blocks startblock to startblock+num-1,
   *  each block is <i>rank</i> * 2 (corners) longs.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long[] H5Sget_select_hyper_blocklist(int spaceid, 
      long startblock, long numblocks)
  throws HDF5LibraryException;

  /**
   *  H5Sget_select_elem_pointlist returns an array of of element points in 
   *  the current dataspace selection. The point coordinates have the same 
   *  dimensionality (rank) as the dataspace they are located within,
   *  one coordinate per point.
   *
   *  @param spaceid     IN: Identifier of dataspace to release.
   *  @param startpoint  IN: First point to retrieve
   *  @param numpoints   IN: Number of points to retrieve
   *
   *  @return points startblock to startblock+num-1,
   *  each points is <i>rank</i> longs.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long[] H5Sget_select_elem_pointlist(int spaceid,
          long startpoint, long numpoints)
  throws HDF5LibraryException ;

  /**
   *  H5Sget_select_bounds retrieves the coordinates of the bounding box 
   *  containing the current selection and places them into user-supplied buffers.
   *  <div>
   *  The start and end buffers must be large enough to
   *  hold the dataspace rank number of coordinates.
   *  </div>
   *
   *  @param spaceid IN: Identifier of dataspace to release.
   *  @param start  OUT: Coordinates of lowest corner of bounding box.
   *  @param end    OUT: Coordinates of highest corner of bounding box.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - start or end is null.
   **/
  public synchronized static native void H5Sget_select_bounds(int spaceid,
          long[] start, long[] end)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Sget_select_type retrieves the type of selection currently defined for the dataspace space_id. 
   *
   *  @param spaceid  IN: Identifier of dataspace.
   *
   *  @return the dataspace selection type.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5S_SEL H5Sget_select_type(int spaceid)
  throws HDF5LibraryException, NullPointerException;
}
