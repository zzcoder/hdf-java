/****************************************************************************
 * NCSA HDF5                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.h4toh5lib.exceptions;

/**
 *  <p>
 *  The class H45Exception returns errors from the 
 *  Java HDF4 to 5 Interface.
 *  <p>
 *  Two sub-classes of H45Exception are defined:
 *  <p>
 *  <ol>
 *  <li>
 *   H45LibraryException -- errors raised the libh4toh5 library code
 *  <li>
 *   H45JavaException -- errors raised the java-h4toh5 Java wrapper code
 *  </ol>
 * 
 */
public class H45Exception extends Exception {

	
    protected String detailMessage;

    /**
     * Constructs an <code>H45Exception</code> with no specified 
     * detail message.
     */
     public H45Exception() {
		super();
     }

    /**
     * Constructs an <code>HDF5Exception</code> with the specified 
     * detail message.
     *
     * @param   message   the detail message.
     */
    public H45Exception(String message) {
		super();
		detailMessage = message;
    }

    /**
     * Returns the detail message of this exception
     *
     * @return  the detail message 
     *          or <code>null</code> if this object does not
     *          have a detail message.
     */
    public String getMessage() {
	return detailMessage;
    }
}
