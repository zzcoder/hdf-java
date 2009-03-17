/****************************************************************************
 * NCSA HDF5                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-h4toh5/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.h4toh5lib.exceptions;

import ncsa.hdf.h4toh5lib.h4toh5;

/**
 *  <p>
 *  The class H45LibraryException returns errors raised by the HDF5
 *  library.
 *  <p>
 */


public class H45LibraryException extends H45Exception {

	/**
	 * Constructs an <code>H45LibraryException</code> with 
	 * no specified detail message.
	 */
	 public H45LibraryException() {
		super();

		// this code forces the loading of the HDF-5 library
		// to assure that the native methods are available
		h4toh5.H4toh5version();

		// not sure about error codes yet.
		//detailMessage = getMinorError(getMinorErrorNumber());
	}

	/**
	 * Constructs an <code>HDF5LibraryException</code> with 
	 * the specified detail message.
	 *
	 * @param   s   the detail message.
	 */
	public H45LibraryException(String s) {
		super(s);
		// this code forces the loading of the HDF-5 library
		// to assure that the native methods are available
		h4toh5.H4toh5version();
	}

}
