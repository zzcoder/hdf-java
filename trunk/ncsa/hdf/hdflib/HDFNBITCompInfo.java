/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.hdflib;

/**
 * <p>
 *  This class is a container for the parameters to the HDF
 *  ``NBIT'' compression class.
 * <p>
 * In this case, the information needed is the number type,
 * the sign extension, the fill bit, the start bit, and the
 * number of bits to store.
 * <p>
 *  For details of the HDF libraries, see the HDF Documentation at:
 *     <a href="http://hdf.ncsa.uiuc.edu">http://hdf.ncsa.uiuc.edu</a>
 */

public class HDFNBITCompInfo extends HDFNewCompInfo {

    int   nt;     /* number type of the data to encode */
    int    sign_ext;   /* whether to sign extend or not */
    int    fill_one;   /* whether to fill with 1's or 0's */
    int    start_bit;  /* offset of the start bit in the data */
    int    bit_len;    /* number of bits to store */

    public HDFNBITCompInfo() {
                ctype = HDFConstants.COMP_CODE_NBIT;
    }

    public HDFNBITCompInfo(
    int   Nt,
    int    Sign_ext,
    int    Fill_one,
    int    Start_bit,
    int    Bit_len) {
                ctype = HDFConstants.COMP_CODE_NBIT;

        int   nt = Nt;
        int    sign_ext = Sign_ext;
        int    fill_one = Fill_one;
        int    start_bit = Start_bit;
        int    bit_len = Bit_len;
    }

}
