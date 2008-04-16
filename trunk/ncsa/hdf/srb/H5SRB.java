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

package ncsa.hdf.srb;

import java.io.*;

/**
**/
public class H5SRB {

    public final static String H5SRBPATH_PROPERTY_KEY = "ncsa.hdf.srb.h5srblib";
    public final static int H5OBJECT_UNKNOWN = -1;
    public final static int H5OBJECT_LINK=0;
    public final static int H5OBJECT_GROUP=1;
    public final static int H5OBJECT_DATASET=2;
    public final static int H5OBJECT_DATATYPE=3;
    public final static int H5OBJECT_DATASPACE=4;
    public final static int H5OBJECT_ATTRIBUTE=5;
    public final static int H5OBJECT_FILE=6;

    static
    {
        boolean isLoaded = false;
        String filename = null;
        filename = System.getProperty(H5SRBPATH_PROPERTY_KEY,null);

        /* load the hdf5-srb client lib from a given path */
        if ((filename != null) && (filename.length() > 0))
        {
            File h5dll = new File(filename);
            if (h5dll.exists() && h5dll.canRead() && h5dll.isFile()) {
                try {
                   System.load(filename);
                   isLoaded = true;
                } catch (Throwable err) { err.printStackTrace(); isLoaded= false; }
            }
        }

        /* load the hdf5-srb client lib from default path */
        if (!isLoaded)
        {
            try {
                System.loadLibrary("jh5srb");
                isLoaded = true;
            } catch (Throwable err) { isLoaded = false; }
        }
    }

    /**
     **/
    public synchronized static native int h5ObjRequest
        (String srb_info[], Object obj, int obj_type) throws Exception;
}

