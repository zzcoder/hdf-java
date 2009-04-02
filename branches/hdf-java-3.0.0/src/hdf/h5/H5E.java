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

import hdf.h5.enums.H5E_TYPE;
import hdf.h5.exceptions.HDF5LibraryException;

import java.io.File;

public class H5E {
//  // Error stack traversal callback function pointers 
//  public interface H5E_walk2_t extends Callback {
//    int callback(int n, H5E_error2_t err_desc, Pointer client_data);
//  }
//  public interface H5E_auto2_t extends Callback {
//    int callback(int estack, Pointer client_data);
//  }
  
  /**
   *  H5Eregister_class registers a client library or application program to 
   *  the HDF5 error API so that the client library or application program 
   *  can report errors together with HDF5 library.
   *
   *  @param cls_name IN: Name of the error class.
   *  @param lib_name IN: Name of the client library or application to which the error class belongs.
   *  @param version  IN: Version of the client library or application to which the error class belongs.
   *
   *  @return a class identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Eregister_class(String cls_name, String lib_name, String version)
  throws HDF5LibraryException, NullPointerException;
  
  /**
   *  H5Eunregister_class removes the error class specified by class_id.
   *
   *  @param class_id IN: Error class identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Eunregister_class(int class_id)
  throws HDF5LibraryException;
  
  /**
   *  H5Eclose_msg closes an error message identifier, which can be either a major or minor message. 
   *
   *  @param err_id  IN: Error message identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Eclose_msg(int err_id)
  throws HDF5LibraryException;
  
  /**
   *  H5Ecreate_msg adds an error message to an error class defined by client 
   *  library or application program.
   *
   *  @param cls_id   IN: Error class identifier.
   *  @param msg_type IN: The type of the error message. 
   *  @param msg      IN: The error message.
   *
   *  @return a message identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - msg is null.
   **/
  public synchronized static native int H5Ecreate_msg(int cls_id, H5E_TYPE msg_type, String msg)
  throws HDF5LibraryException, NullPointerException;
  
  /**
   *  H5Ecreate_stack creates a new empty error stack and returns the 
   *  new stack’s identifier. 
   *
   *  @param none
   *
   *  @return an error stack identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Ecreate_stack()
  throws HDF5LibraryException;
  
  /**
   *  H5Eget_current_stack copies the current error stack and returns an 
   *  error stack identifier for the new copy. 
   *
   *  @param none
   *
   *  @return an error stack identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Eget_current_stack()
  throws HDF5LibraryException;
  
  /**
   *  H5Eclose_stack closes the object handle for an error stack and 
   *  releases its resources.
   *
   *  @param stack_id IN: Error stack identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Eclose_stack(int stack_id)
  throws HDF5LibraryException;
  
  /**
   *  H5Eget_class_name retrieves the name of the error class 
   *  specified by the class identifier.
   *
   *  @param class_id IN: Error class identifier.
   *
   *  @return the name of the error class
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Eget_class_name(int class_id)
  throws HDF5LibraryException, NullPointerException;
//long H5Eget_class_name(int class_id, String name, IntegerType size);  
  
  /**
   *  H5Eset_current_stack replaces the content of the current error stack 
   *  with a copy of the content of the error stack specified by estack_id. 
   *
   *  @param stack_id IN: Error stack identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Eset_current_stack(int stack_id)
  throws HDF5LibraryException;
//  public static int H5Epush(int err_stack, String file, String func, int line,
//    int cls_id, int maj_id, int min_id, String msg, ...)
//  {
//    H5Epush2(err_stack, file, func, line, cls_id, maj_id, min_id, msg, ...);
//  }
//  public synchronized static native int H5Epush2(int err_stack, String file, String func, int line,
//    int cls_id, int maj_id, int min_id, String msg, ...);
  
  /**
   *  H5Epop deletes the number of error records specified in count from 
   *  the top of the error stack specified by estack_id 
   *  (including major, minor messages and description).
   *
   *  @param stack_id IN: Error stack identifier.
   *  @param count    IN: Version of the client library or application to which the error class belongs.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Epop(int stack_id, long count)
  throws HDF5LibraryException;
  
  /**
   *  H5Eprint prints the error stack specified by estack_id on the 
   *  specified stream, stream.
   *
   *  @param stack_id IN: Error stack identifier.If the identifier is H5E_DEFAULT, the current error stack will be printed.
   *  @param stream   IN: File pointer, or stderr if null.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public static void H5Eprint(int err_stack, File stream)
  throws HDF5LibraryException
  {
    H5Eprint2(err_stack, stream);
  }
  /**
   *  H5Eprint2 prints the error stack specified by estack_id on the 
   *  specified stream, stream.
   *
   *  @see public static void H5Eprint(int err_stack, File stream)
   **/
  public synchronized static native void H5Eprint2(int err_stack, File stream)
  throws HDF5LibraryException;
  
//  int H5Ewalk(int err_stack, H5E_direction_t direction, H5E_walk2_t func,
//        Pointer client_data)
//  {
//    return H5Ewalk2(err_stack, direction, func, client_data);
//  }
//  int H5Ewalk2(int err_stack, H5E_direction_t direction, H5E_walk2_t func,
//        Pointer client_data);
//  int H5Eget_auto(int estack_id, H5E_auto2_t func, PointerByReference client_data);
//  {
//    return H5Eget_auto2(estack_id,  func, client_data);
//  }
//  int H5Eget_auto2(int estack_id, H5E_auto2_t func, PointerByReference client_data);
//  int H5Eset_auto(int estack_id, H5E_auto2_t func, Pointer client_data);
//  {
//    return H5Eset_auto2(estack_id, func, client_data);
//  }
//  int H5Eset_auto2(int estack_id, H5E_auto2_t func, Pointer client_data);
  
  /**
   *  H5Eclear clears the error stack specified by estack_id, or, if 
   *  estack_id is set to H5E_DEFAULT, the error stack for the current thread. 
   *
   *  @param stack_id IN: Error stack identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public static void H5Eclear(int err_stack)
  throws HDF5LibraryException
  {
    H5Eclear2(err_stack);
  }
  /**
   *  H5Eclear2 clears the error stack specified by estack_id, or, if 
   *  estack_id is set to H5E_DEFAULT, the error stack for the current thread. 
   *
   *  @see public static void H5Eclear(int err_stack)
   **/
  public synchronized static native void H5Eclear2(int err_stack)
  throws HDF5LibraryException;
  
  /**
   *  H5Eauto_is_v2 determines whether the error auto reporting function 
   *  for an error stack conforms to the H5E_auto2_t typedef 
   *  or the H5E_auto1_t typedef. 
   *
   *  @param stack_id IN: Error stack identifier.
   *
   *  @return boolean true if the error stack conforms to H5E_auto2_t  
   *          and false if it conforms to H5E_auto1_t. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Eauto_is_v2(int err_stack)
  throws HDF5LibraryException;
  
  /**
   *  H5Eget_msg retrieves the error message including its length and type.
   *
   *  @param msg_id  IN: Name of the error class.
   *  @param type   OUT: The type of the error message.
   *                     Valid values are H5E_MAJOR and H5E_MINOR.
   *
   *  @return the error message
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Eget_msg(int msg_id, H5E_TYPE[] type)
  throws HDF5LibraryException;
//long H5Eget_msg(int msg_id, H5E_TYPE type, String msg, IntegerType size);  
  
  /**
   *  H5Eget_num retrieves the number of error records in the error 
   *  stack specified by estack_id  
   *  (including major, minor messages and description). 
   *
   *  @param stack_id IN: Error stack identifier.
   *
   *  @return the number of error messages
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Eget_num(int stack_id)
  throws HDF5LibraryException, NullPointerException;
  
  /**
   *  H5Eprint prints the error stack specified by estack_id on the 
   *  specified stream, stream.
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Eprint(int, File)}
   *
   *  @param stream   IN: File pointer, or stderr if null.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public static void H5Eprint(File stream)
  throws HDF5LibraryException
  {
    H5Eprint1(stream);
  }
  /**
   *  H5Eprint1 prints the error stack specified by estack_id on the 
   *  specified stream, stream.
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Eprint2(int, File)}
   *
   *  @see public static void H5Eprint(File stream)
   **/
  private synchronized static native void H5Eprint1(File stream)
  throws HDF5LibraryException;
  
  /**
   *  H5Eclear clears the error stack specified by estack_id, or, if 
   *  estack_id is set to H5E_DEFAULT, the error stack for the current thread. 
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Eclear(int)}
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public static void H5Eclear()
  throws HDF5LibraryException
  {
    H5Eclear1();
  }
  /**
   *  H5Eclear1 clears the error stack specified by estack_id, or, if 
   *  estack_id is set to H5E_DEFAULT, the error stack for the current thread. 
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Eclear2(int)}
   *
   *  @see public static void H5Eclear()
   **/
  private synchronized static native void H5Eclear1()
  throws HDF5LibraryException;
//  public static int H5Epush(String file, String func, int line,
//    int maj_id, int min_id, String msg)
//  {
//    H5Epush1(file, func, line, maj_id, min_id, msg);
//  }
//  private synchronized static native int H5Epush1(String file, String func, int line,
//    int maj_id, int min_id, String msg);
//  
//  int H5Ewalk(H5E_direction_t direction, H5E_walk1_t func,
//        Pointer client_data)
//  {
//    return H5Ewalk1(direction, func, client_data);
//  }
//  int H5Ewalk1(H5E_direction_t direction, H5E_walk1_t func,
//        Pointer client_data);
//  int H5Eget_auto(H5E_auto1_t func, PointerByReference client_data);
//  {
//    return H5Eget_auto1(func, client_data);
//  }
//  int H5Eget_auto1(H5E_auto1_t func, PointerByReference client_data);
//  int H5Eset_auto(H5E_auto1_t func, Pointer client_data);
//  {
//    return H5Eset_auto1(func, client_data);
//  }
//  int H5Eset_auto1(H5E_auto1_t func, Pointer client_data);

}
