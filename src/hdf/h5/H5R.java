package hdf.h5;

import hdf.h5.enums.H5O.H5O_type_t;
import hdf.h5.enums.H5R.H5R_type_t;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//          H5R: Reference Interface Functions              //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5R {

  /**
   *  H5Rcreate creates the reference, ref, of the type specified in
   *  ref_type, pointing to the object name located at loc_id.
   *
   *  @param loc_id    IN: Location identifier used to locate the object being pointed to.
   *  @param name      IN: Name of object at location loc_id.
   *  @param ref_type  IN: Type of reference.
   *  @param space_id  IN: Dataspace identifier with selection.
   *
   *  @return the reference (byte[]) created.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native byte[] H5Rcreate(int loc_id, String name,
          H5R_type_t ref_type, int space_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Rdereference opens a referenced object and returns an identifier.
   *
   *  @param dataset   IN: Dataset containing reference object.
   *  @param ref_type  IN: The reference type of ref.
   *  @param ref       IN: reference to open
   *
   *  @return valid identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Rdereference(int dataset, H5R_type_t ref_type, byte[] ref)
      throws HDF5LibraryException;

  /**
   *  H5Rget_region creates a copy of the dataspace of the dataset 
   *  pointed to and defines a selection in the copy which is the region pointed to. 
   *
   *  @param loc_id    IN: Dataset containing the reference object.
   *  @param ref_type  IN: The reference type.
   *  @param ref      OUT: The reference to open.
   *
   *  @return a valid identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - ref array is null.
   **/
  public synchronized static native int H5Rget_region(int dataset, H5R_type_t ref_type, byte[] ref)
      throws HDF5LibraryException, NullPointerException;
//  int H5Rget_region(int dataset, H5R_type_t ref_type, Pointer ref);

  /**
   *  H5Rget_obj_type2 retrieves the type of the referenced object.
   *
   *  @param loc_id   IN: The dataset containing the reference object or the group containing that dataset.
   *  @param ref_type IN: Type of reference to query.
   *  @param ref      IN: Reference to query.
   *
   *  @return type (H5O_type_t) of the referenced object
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - ref is null.
   **/
  public synchronized static native H5O_type_t H5Rget_obj_type2(int loc_id, H5R_type_t ref_type, byte[] ref)
      throws HDF5LibraryException, NullPointerException;
//  int H5Rget_obj_type2(int loc_id, H5R_type_t ref_type, Pointer ref, H5O_type_t obj_type);

  /**
   *  H5Rget_name retrieves a name for the referenced object.
   *
   *  @param loc_id   IN: The dataset containing the reference object or the group containing that dataset.
   *  @param ref_type IN: Type of reference to query.
   *  @param ref      IN: Reference to query.
   *
   *  @return the name associated with the referenced object or dataset region.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - ref is null.
   **/
  public synchronized static native String H5Rget_name(int loc_id, H5R_type_t ref_type, byte[] ref)
      throws HDF5LibraryException, NullPointerException;
//  long H5Rget_name(int loc_id, H5R_type_t ref_type, Pointer ref,
//      String name/*out*/, long size);

}
