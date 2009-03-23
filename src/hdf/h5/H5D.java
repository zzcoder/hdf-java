package hdf.h5;

import hdf.h5.enums.H5D.H5D_space_status_t;
import hdf.h5.enums.H5T.H5T_class_t;
import hdf.h5.exceptions.HDF5Exception;
import hdf.h5.exceptions.HDF5JavaException;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//             H5D: Datasets Interface Functions            //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5D {
  //  // Define the operator function pointer for H5Diterate()
  //  public interface H5D_operator_t extends Callback {
  //    int callback(Pointer elem, int type_id, int ndim,
  //           LongByReference point, Pointer operator_data);
  //  }

  /**
   *  H5Dcreate2 creates a new dataset named name at the 
   *  location specified by loc_id.
   *
   *  @param loc_id   IN: Location identifier 
   *  @param name     IN: Dataset name
   *  @param type_id  IN: Datatype identifier
   *  @param space_id IN: Dataspace identifier 
   *  @param lcpl_id  IN: Identifier of link creation property list.
   *  @param dcpl_id  IN: Identifier of dataset creation property list.
   *  @param dapl_id  IN: Identifier of dataset access property list.
   *
   *  @return a dataset identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Dcreate2(int loc_id, String name, int type_id,
      int space_id, int lcpl_id, int dcpl_id, int dapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dcreate_anon creates a dataset in the file specified by loc_id. 
   *
   *  @param loc_id   IN: Location identifier 
   *  @param type_id  IN: Datatype identifier
   *  @param space_id IN: Dataspace identifier 
   *  @param lcpl_id  IN: Identifier of link creation property list.
   *  @param dcpl_id  IN: Identifier of dataset creation property list.
   *  @param dapl_id  IN: Identifier of dataset access property list.
   *
   *  @return a dataset identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Dcreate_anon(int loc_id, int type_id, int space_id,
      int dcpl_id, int dapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Dopen2 opens the existing dataset specified by a location identifier 
   *  and name, loc_id  and name, respectively. 
   *
   *  @param loc_id   IN: Location identifier 
   *  @param name     IN: Dataset name
   *  @param dapl_id  IN: Identifier of dataset access property list.
   *
   *  @return a dataset identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Dopen2(int loc_id, String name, int dapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dclose ends access to a dataset specified by dset_id and
   *  releases resources used by it.
   *
   *  @param dset_id IN: Identifier of the dataset to finish access to.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Dclose(int dset_id)
  throws HDF5LibraryException;

  /**
   *  H5Dget_space returns an identifier for a copy of the
   *  dataspace for a dataset.
   *
   *  @param dset_id IN: Identifier of the dataset to query.
   *
   *  @return a dataspace identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Dget_space(int dset_id)
  throws HDF5LibraryException;

  /**
   *  H5Dget_space_status determines whether space has been 
   *  allocated for the dataset dset_id. 
   *
   *  @param dset_id IN: Identifier of the dataset to query.
   *
   *  @return the space allocation status
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  //  public synchronized static native int H5Dget_space_status(int dset_id, int[] status )
  //  throws HDF5LibraryException, NullPointerException;
  public synchronized static native H5D_space_status_t H5Dget_space_status(int dset_id)
  throws HDF5LibraryException;
  //  int H5Dget_space_status(int dset_id, H5D_space_status_t allocation);  

  /**
   *  H5Dget_type returns an identifier for a copy of the datatype for a dataset.
   *
   *  @param dset_id IN: Identifier of the dataset to query.
   *
   *  @return a datatype identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Dget_type(int dset_id)
  throws HDF5LibraryException;

  /**
   *  H5Dget_create_plist returns an identifier for a copy of the
   *  dataset creation property list for a dataset.
   *
   *  @param dset_id IN: Identifier of the dataset to query.
   *
   *  @return a dataset creation property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Dget_create_plist(int dset_id)
  throws HDF5LibraryException;

  /**
   *  H5Dget_access_plist returns an identifier for a copy of the
   *  dataset access property list for a dataset.
   *
   *  @param dset_id IN: Identifier of the dataset to query.
   *
   *  @return a dataset access property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Dget_access_plist(int dset_id)
  throws HDF5LibraryException;

  /** H5Dget_storage_size returns the amount of storage that is required for the dataset.
   *
   *  @param dset_id  IN: Identifier of the dataset in question
   *
   *  @return the amount of storage space allocated for the dataset.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Dget_storage_size(int dset_id)
  throws HDF5LibraryException;

  /** H5Dget_offset returns the address in the file of the dataset dset_id.
   *
   *  @param dset_id  IN: Identifier of the dataset in question
   *
   *  @return the offset in bytes.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Dget_offset(int dset_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dread reads a (partial) dataset, specified by its
   *  identifier dset_id, from the file into the application memory buffer buf.
   *
   *  @param dset_id       IN: Identifier of the dataset read from.
   *  @param mem_type_id   IN: Identifier of the memory datatype.
   *  @param mem_space_id  IN: Identifier of the memory dataspace.
   *  @param file_space_id IN: Identifier of the dataset's dataspace in the file.
   *  @param xfer_plist_id IN: Identifier of a transfer property list for this I/O operation.
   *  @param buf          OUT: Buffer to store data read from the file.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data buffer is null.
   **/
  public synchronized static native void H5Dread(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      byte[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      byte[] buf)  throws HDF5LibraryException, NullPointerException 
      {
    H5Dread(dset_id, mem_type_id,mem_space_id, file_space_id, 
        xfer_plist_id, buf, true);
      }

  public synchronized static native void H5DreadVL(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id, Object[] buf)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dread reads a (partial) dataset, specified by its
   *  identifier dset_id, from the file into the application data object.
   *
   *  @param dset_id       IN: Identifier of the dataset read from.
   *  @param mem_type_id   IN: Identifier of the memory datatype.
   *  @param mem_space_id  IN: Identifier of the memory dataspace.
   *  @param file_space_id IN: Identifier of the dataset's dataspace in the file.
   *  @param xfer_plist_id IN: Identifier of a transfer property list for this I/O operation.
   *  @param obj          OUT: Object to store data read from the file.
   *
   *  @return none
   *
   *  @exception HDF5Exception - Failure in the data conversion.
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data object is null.
   **/
  public synchronized static void H5Dread(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id,
      int xfer_plist_id, Object obj, boolean isCriticalPinning)
  throws HDF5Exception, HDF5LibraryException, NullPointerException
  {
    boolean is1D = false;

    Class dataClass = obj.getClass();
    if (!dataClass.isArray()) {
      throw (new HDF5JavaException("H5Dread: data is not an array"));
    }

    String cname = dataClass.getName();
    is1D = (cname.lastIndexOf('[') ==cname.indexOf('['));
    char dname = cname.charAt(cname.lastIndexOf("[")+1);

    if (is1D && (dname == 'B')) {
      H5Dread(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (byte[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'S')) {
      H5Dread_short(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (short[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'I')) {
      H5Dread_int(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (int[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'J')) {
      H5Dread_long(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (long[])obj);
    }
    else if (is1D && (dname == 'F')) {
      H5Dread_float(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (float[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'D')) {
      H5Dread_double(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (double[])obj, isCriticalPinning);
    }
    else if (H5T.H5Tequal(mem_type_id, HDF5Constants.H5T_STD_REF_DSETREG)) {
      H5Dread_reg_ref(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (String[])obj);
    }
    else if (is1D && (dataClass.getComponentType() == String.class)) { 
      // Rosetta Biosoftware - add support for Strings (variable length) 
      if (H5T.H5Tis_variable_str(mem_type_id)) {
        H5DreadVL(dset_id, mem_type_id,mem_space_id, file_space_id, 
            xfer_plist_id, (Object[])obj);
      } 
      else {
        H5Dread_string(dset_id, mem_type_id,mem_space_id, file_space_id,
            xfer_plist_id, (String[])obj);
      }
    } else {
      // Create a data buffer to hold the data into a Java Array
      HDFArray theArray = new HDFArray(obj);
      byte[] buf = theArray.emptyBytes();

      // will raise exception if read fails
      H5Dread(dset_id, mem_type_id, mem_space_id,
          file_space_id, xfer_plist_id, buf, isCriticalPinning);
      // convert the data into a Java Array */
      obj = theArray.arrayify( buf);

      // clean up these:  assign 'null' as hint to gc() */
      buf = null;
      theArray = null;
    }
  }

  public synchronized static void H5Dread(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id, Object obj)
  throws HDF5Exception, HDF5LibraryException, NullPointerException
  {
    H5Dread(dset_id, mem_type_id,mem_space_id, file_space_id,
        xfer_plist_id, obj, true);
  }

  ////////////////////////////////////////////////////////////////////
  //                                                                //
  //         New APIs for read data from library                    //
  //  Using H5Dread(..., Object buf) requires function calls        //
  //  theArray.emptyBytes() and theArray.arrayify(buf), which      //
  //  triples the actual memory needed by the data set.             //
  //  Using the following APIs solves the problem.                  //
  //                                                                //
  ////////////////////////////////////////////////////////////////////

  public synchronized static native void H5Dread_short(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      short[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread_short(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      short[] buf) 
  throws HDF5LibraryException, NullPointerException
  {
    H5Dread_short(dset_id, mem_type_id,mem_space_id, 
        file_space_id, xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dread_int(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      int[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread_int(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      int[] buf) 
  throws HDF5LibraryException, NullPointerException
  {
    H5Dread_int(dset_id, mem_type_id,mem_space_id, 
        file_space_id, xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dread_long(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      long[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread_long(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      long[] buf) 
  throws HDF5LibraryException, NullPointerException
  {
    H5Dread_long(dset_id, mem_type_id,mem_space_id, 
        file_space_id, xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dread_float(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      float[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread_float(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      float[] buf) 
  throws HDF5LibraryException, NullPointerException
  {
    H5Dread_float(dset_id, mem_type_id,mem_space_id, 
        file_space_id, xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dread_double(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      double[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dread_double(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      double[] buf) 
  throws HDF5LibraryException, NullPointerException
  {
    H5Dread_double(dset_id, mem_type_id,mem_space_id, 
        file_space_id, xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dread_string(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      String[] buf)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static native void H5Dread_reg_ref(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      String[] buf)
  throws HDF5LibraryException, NullPointerException;

  //  int H5Dread(int dset_id, int mem_type_id, int mem_space_id,
      //        int file_space_id, int plist_id, Buffer buf/*out*/);

  /** 
   *  H5DwriteString writes a (partial) variable length String dataset, specified by its 
   *  identifier dset_id, from the application memory buffer buf into the file. 
   * 
   *  @param dset_id       IN: Identifier of the dataset read from. 
   *  @param mem_type_id   IN: Identifier of the memory datatype. 
   *  @param mem_space_id  IN: Identifier of the memory dataspace. 
   *  @param file_space_id IN: Identifier of the dataset's dataspace in the file. 
   *  @param xfer_plist_id IN: Identifier of a transfer property list for this I/O operation. 
   *  @param buf           IN: Buffer with data to be written to the file. 
   * 
   *  @return none
   *  
   *  @author contributed by Rosetta Biosoftware
   * 
   *  @exception HDF5LibraryException - Error from the HDF-5 Library. 
   *  @exception NullPointerException - name is null. 
   **/ 


  public synchronized static native void H5DwriteString(int dset_id, 
      int mem_type_id, int mem_space_id, 
      int file_space_id, int xfer_plist_id, String[] buf) 
  throws HDF5LibraryException, NullPointerException; 


  /**
   *  H5Dwrite writes a (partial) dataset, specified by its
   *  identifier dset_id, from the application memory buffer
   *  buf into the file.
   *
   *  @param dset_id       IN: Identifier of the dataset read from.
   *  @param mem_type_id   IN: Identifier of the memory datatype.
   *  @param mem_space_id  IN: Identifier of the memory dataspace.
   *  @param file_space_id IN: Identifier of the dataset's dataspace in the file.
   *  @param xfer_plist_id IN: Identifier of a transfer property list for this I/O operation.
   *  @param buf           IN: Buffer with data to be written to the file.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Dwrite(int dset_id,
      int mem_type_id, int mem_space_id, int file_space_id, 
      int xfer_plist_id, byte[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite(int dset_id,
      int mem_type_id, int mem_space_id, int file_space_id, 
      int xfer_plist_id, byte[] buf) 
  throws HDF5LibraryException, NullPointerException 
  {
    H5Dwrite(dset_id, mem_type_id, mem_space_id, file_space_id,
        xfer_plist_id, buf, true);
  }

  /**
   *  H5Dwrite writes a (partial) dataset, specified by its
   *  identifier dset_id, from the application memory data
   *  object into the file.
   *
   *  @param dset_id       IN: Identifier of the dataset read from.
   *  @param mem_type_id   IN: Identifier of the memory datatype.
   *  @param mem_space_id  IN: Identifier of the memory dataspace.
   *  @param file_space_id IN: Identifier of the dataset's dataspace in the file.
   *  @param xfer_plist_id IN: Identifier of a transfer property list for this I/O operation.
   *  @param obj           IN: Object with data to be written to the file.
   *
   *  @return none
   *
   *  @exception HDF5Exception - Failure in the data conversion.
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data object is null.
   **/
  public synchronized static void H5Dwrite(int dset_id, int mem_type_id, int mem_space_id,
      int file_space_id, int xfer_plist_id, Object obj, boolean isCriticalPinning)
  throws HDF5Exception, HDF5LibraryException, NullPointerException
  {
    boolean is1D = false;

    Class dataClass = obj.getClass();
    if (!dataClass.isArray()) {
      throw (new HDF5JavaException("H5Dread: data is not an array"));
    }

    String cname = dataClass.getName();
    is1D = (cname.lastIndexOf('[') ==cname.indexOf('['));
    char dname = cname.charAt(cname.lastIndexOf("[")+1);

    if (is1D && (dname == 'B')) {
      H5Dwrite(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (byte[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'S')) {
      H5Dwrite_short(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (short[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'I')) {
      H5Dwrite_int(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (int[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'J')) {
      H5Dwrite_long(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (long[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'F')) {
      H5Dwrite_float(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (float[])obj, isCriticalPinning);
    }
    else if (is1D && (dname == 'D')) {
      H5Dwrite_double(dset_id, mem_type_id,mem_space_id, file_space_id,
          xfer_plist_id, (double[])obj, isCriticalPinning);
    }

    // Rosetta Biosoftware - call into H5DwriteString for variable length Strings 
    else if ((H5T.H5Tget_class(mem_type_id) == H5T_class_t.H5T_STRING) && H5T.H5Tis_variable_str(mem_type_id) 
        && dataClass.isArray() && (dataClass.getComponentType() == String.class) && is1D) { 
      H5DwriteString(dset_id, mem_type_id, 
          mem_space_id, file_space_id, xfer_plist_id, (String[])obj); 

    } else {
      HDFArray theArray = new HDFArray(obj);
      byte[] buf = theArray.byteify();

      /* will raise exception on error */
      H5Dwrite(dset_id, mem_type_id, mem_space_id, 
          file_space_id, xfer_plist_id, buf, isCriticalPinning);

      // clean up these:  assign 'null' as hint to gc() */
      buf = null;
      theArray = null;
    }
  }

  public synchronized static void H5Dwrite(int dset_id, int mem_type_id, int mem_space_id,
      int file_space_id, int xfer_plist_id, Object obj)
  throws HDF5Exception, HDF5LibraryException, NullPointerException
  {
    H5Dwrite(dset_id, mem_type_id, mem_space_id,
        file_space_id, xfer_plist_id, obj, true);
  }
  public synchronized static native void H5Dwrite_short(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      short[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite_short(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      short[] buf)
  throws HDF5LibraryException, NullPointerException
  {
    H5Dwrite_short(dset_id, mem_type_id,mem_space_id, 
        file_space_id,  xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dwrite_int(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      int[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite_int(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      int[] buf)
  throws HDF5LibraryException, NullPointerException
  {
    H5Dwrite_int(dset_id, mem_type_id,mem_space_id, 
        file_space_id,  xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dwrite_long(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      long[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite_long(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      long[] buf)
  throws HDF5LibraryException, NullPointerException
  {
    H5Dwrite_long(dset_id, mem_type_id,mem_space_id, 
        file_space_id,  xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dwrite_float(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      float[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite_float(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      float[] buf)
  throws HDF5LibraryException, NullPointerException
  {
    H5Dwrite_float(dset_id, mem_type_id,mem_space_id, 
        file_space_id,  xfer_plist_id, buf, true);
  }

  public synchronized static native void H5Dwrite_double(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      double[] buf, boolean isCriticalPinning)
  throws HDF5LibraryException, NullPointerException;

  public synchronized static void H5Dwrite_double(int dset_id, int mem_type_id,
      int mem_space_id, int file_space_id, int xfer_plist_id,
      double[] buf)
  throws HDF5LibraryException, NullPointerException
  {
    H5Dwrite_double(dset_id, mem_type_id,mem_space_id, 
        file_space_id,  xfer_plist_id, buf, true);
  }
  //  int H5Dwrite(int dset_id, int mem_type_id, int mem_space_id,
  //        int file_space_id, int plist_id, Pointer buf);
  //  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dvlen_reclaim reclaims memory buffers created to store VL datatypes. 
   *
   *  @param type_id  IN: Identifier of the datatype.
   *  @param space_id IN: Identifier of the dataspace.
   *  @param plist_id IN: Identifier of the property list used to create the buffer.
   *  @param buf      IN: Pointer to the buffer to be reclaimed.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native void H5Dvlen_reclaim(int type_id, int space_id,
      int plist_id, byte[] buf)
  throws HDF5LibraryException, NullPointerException;
//  int H5Dvlen_reclaim(int type_id, int space_id, int plist_id, Pointer buf);

  /**
   *  H5Dvlen_get_buf_size determines the number of bytes required to store the VL data from 
   *  the dataset, using the space_id for the selection in the dataset on disk and the 
   *  type_id for the memory representation of the VL data in memory. 
   *
   *  @param dset_id  IN: Identifier of the dataset read from.
   *  @param type_id  IN: Identifier of the datatype.
   *  @param space_id IN: Identifier of the dataspace.
   *
   *  @return the size in bytes of the memory buffer required to store the VL data.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native long H5Dvlen_get_buf_size(int dset_id, int type_id, int space_id)
  throws HDF5LibraryException;
//  int H5Dvlen_get_buf_size(int dset_id, int type_id, int space_id, LongByReference size);

  /**
   *  H5Dfill explicitly fills the dataspace selection in memory, space_id, 
   *  with the fill value specified in fill. 
   *
   *  @param fill      IN: Pointer to the fill value to be used.
   *  @param fill_type IN: Fill value datatype identifier.
   *  @param buf   IN/OUT: Pointer to the memory buffer containing the selection to be filled.
   *  @param buf_type  IN: Datatype of dataspace elements to be filled.
   *  @param space     IN: Dataspace describing memory buffer and containing the selection to be filled.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native void H5Dfill(byte[] fill, int fill_type, byte[] buf, int buf_type, int space)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Dset_extent sets the current dimensions of the chunked dataset dset_id 
   *  to the sizes specified in size. 
   *
   *  @param dset_id  IN: Chunked dataset identifier.
   *  @param size     IN: Array containing the new magnitude of each dimension of the dataset. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - size is null.
   **/
  public synchronized static native void H5Dset_extent(int dset_id, long size[])
  throws HDF5LibraryException, NullPointerException;
  public synchronized static native int H5Ddebug(int dset_id)
  throws HDF5LibraryException, NullPointerException;
}
