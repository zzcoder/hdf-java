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

package hdf.h4;

/**
 * <p>
 *  This class is a container for the parameters to the HDF
 *  ``Image compression'' compression class.
 * <p>
 * In this case, no auxilliary information is needed.
 * <p>
 *  For details of the HDF libraries, see the HDF Documentation at:
 *     <a href="http://hdf.ncsa.uiuc.edu">http://hdf.ncsa.uiuc.edu</a>
 */
public class HDFIMCOMPCompInfo extends HDFOldCompInfo {

    public HDFIMCOMPCompInfo () {
        ctype = HDFConstants.COMP_IMCOMP;
    }

}