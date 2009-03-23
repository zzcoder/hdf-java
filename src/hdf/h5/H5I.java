package hdf.h5;

import hdf.h5.enums.H5I.H5I_type_t;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//           H5I: Identifier Interface Functions            //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5I {
//  public interface H5I_free_t extends Callback {
//    int callback(Pointer obj);
//  }
//  public interface H5I_search_func_t extends Callback {
//    int callback(Pointer obj, int id, Pointer key);
//  }

  /**
   *  H5Iregister allocates space for a new ID and returns an identifier for it. 
   *
   *  @param type    IN: Identifier of the type to which the new ID will belong
   *  @param object  IN: Pointer to memory for the library to store
   *
   *  @return the new ID.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Iregister(H5I_type_t type, byte[] object)
      throws HDF5LibraryException;

   /**
    *  H5Iobject_verify returns a pointer to the memory referenced by id after 
    *  verifying that id is of type id_type.
    *
    *  @param id      IN: Identifier to be dereferenced
    *  @param id_type IN: Identifier type to which id should belong
    *
    *  @return the pointer to memory to the object referenced by id.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native byte[] H5Iobject_verify(int id, H5I_type_t id_type)
   throws HDF5LibraryException;

   /**
    *  H5Iremove_verify first ensures that id belongs to id_type.
    *
    *  @param id      IN: Identifier to be removed from internal storage
    *  @param id_type IN: Identifier type whose reference count is to be retrieved
    *
    *  @return the pointer to memory to the object referenced by id.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native byte[] H5Iremove_verify(int id, H5I_type_t id_type)
   throws HDF5LibraryException;

   /**
    *  H5Iget_type retrieves the type of the object identified by obj_id.
    *
    *  @param obj_id  IN: Object identifier whose type is to be determined.
    *
    *  @return the object type if successful; otherwise H5I_BADID.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iget_type(int obj_id)
   throws HDF5LibraryException;

   /**
    *  H5Iget_file_id returns the identifier of the file associated with the object referenced by obj_id. 
    *
    *  @param obj_id  IN: Identifier of the object whose associated file identifier will be returned.
    *
    *  @return a file identifier.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iget_file_id(int obj_id)
   throws HDF5LibraryException;

//   /**
//    *  H5Iget_name retrieves a name for the object identified by obj_id. 
//    *
//    *  @param obj_id  IN: Identifier of the object.
//    *  @param name   OUT: Buffer to store name in.
//    *  @param size    IN: The size of the buffer to store the name in.
//    *
//    *  @return the length of the name with the identifier.
//    *
//    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//    *  @exception NullPointerException - name is null.
//    **/
//   public synchronized static native long H5Iget_name(int obj_id, String[] name, long size )
//   throws HDF5LibraryException, NullPointerException;

   /**
    *  H5Iget_name retrieves a name for the object identified by obj_id. 
    *
    *  @param obj_id  IN: Identifier of the object.
    *
    *  @return the name associated with the identifier.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native String H5Iget_name(int obj_id)
   throws HDF5LibraryException;
//   long H5Iget_name(int id, String name/*out*/, long size);

   /**
    *  H5Iinc_ref increments the reference count of the object identified by obj_id. 
    *
    *  @param obj_id  IN: Object identifier whose reference count will be modified.
    *
    *  @return a non-negative reference count of the object ID after incrementing
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iinc_ref(int obj_id)
   throws HDF5LibraryException;

   /**
    *  H5Idec_ref decrements the reference count of the object identified by obj_id. 
    *
    *  @param obj_id  IN: Object identifier whose reference count will be modified.
    *
    *  @return a non-negative reference count of the object ID after decrementing it.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Idec_ref(int obj_id)
   throws HDF5LibraryException;

   /**
    *  H5Iget_ref retrieves the reference count of the object identified by obj_id. 
    *
    *  @param obj_id  IN: Object identifier whose treference count will be retrieved.
    *
    *  @return a non-negative current reference count of the object ID.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iget_ref(int obj_id)
   throws HDF5LibraryException;

   //   H5I_type_t H5Iregister_type(long hash_size, int reserved, H5I_free_t free_func);

   /**
    *  H5Iclear_type deletes all IDs of the type identified by the argument type. 
    *
    *  @param type  IN: Identifier of ID type which is to be cleared of IDs
    *  @param force IN: Whether or not to force deletion of all IDs
    *
    *  @return none
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native void H5Iclear_type(H5I_type_t type, boolean force)
   throws HDF5LibraryException;

   /**
    *  H5Idestroy_type deletes an entire ID type. All IDs of this type are destroyed 
    *  and no new IDs of this type can be registered. 
    *
    *  @param type  IN: Identifier of ID type which is to be destroyed
    *
    *  @return none
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native void H5Idestroy_type(H5I_type_t type)
   throws HDF5LibraryException;

   /**
    *  H5Iinc_type_ref increments the reference count on an ID type.
    *
    *  @param type  IN: The identifier of the type whose reference count is to be incremented
    *
    *  @return the current reference count.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iinc_type_ref(H5I_type_t type)
   throws HDF5LibraryException;

   /**
    *  H5Idec_type_ref decrements the reference count on an ID type.
    *
    *  @param type  IN: The identifier of the type whose reference count is to be decremented
    *
    *  @return the current reference count.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Idec_type_ref(H5I_type_t type)
   throws HDF5LibraryException;

   /**
    *  H5Iget_type_ref retrieves the reference count on an ID type.
    *
    *  @param type  IN: The identifier of the type whose reference count is to be retrieved
    *
    *  @return the current reference count.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native int H5Iget_type_ref(H5I_type_t type)
   throws HDF5LibraryException;

//   Pointer H5Isearch(H5I_type_t type, H5I_search_func_t func, Pointer key);

   /**
    *  H5Inmembers returns the number of identifiers of the identifier type specified in type. 
    *
    *  @param type  IN: Identifier for the identifier type whose member count will be retrieved
    *
    *  @return the number of identifiers of the specified identifier type. 
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native long H5Inmembers(H5I_type_t type)
   throws HDF5LibraryException;

   /**
    *  H5Itype_exists determines whether the given identifier type, type, is registered with the library. 
    *
    *  @param type  IN: Identifier for the identifier type
    *
    *  @return boolean true if the type is registered.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native boolean H5Itype_exists(H5I_type_t type)
   throws HDF5LibraryException;

   /**
    *  H5Iis_valid checks if the given obj_id is valid.  And obj_id is valid if it is in
    *  use and has an application reference count of at least 1.
    *
    *  @param obj_id  IN: Object identifier to be checked.
    *
    *  @return boolean true if an obj_id is valid.
    *
    *  @exception HDF5LibraryException - Error from the HDF-5 Library.
    **/
   public synchronized static native boolean H5Iis_valid(int obj_id)
   throws HDF5LibraryException;
}
