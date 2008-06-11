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
            } catch (Throwable err) { err.printStackTrace(); isLoaded = false; }
        }
    }

    /**
     *  Make a server connection 
     */
     public synchronized static native void callServerInit(String passwd) throws Exception;
 
    /**
     *  Returns server information.
     *  <p>
        srvInfo[0] = rodsUserName
        srvInfo[1] = rodsHost
        srvInfo[2] = rodsPort
        srvInfo[3] = xmsgHost
        srvInfo[4] = xmsgPort
        srvInfo[5] = rodsHome
        srvInfo[6] = rodsCwd
        srvInfo[7] = rodsAuthScheme
        srvInfo[8] = rodsDefResource
        srvInfo[9] = rodsZone
        srvInfo[10] = rodsServerDn 
        srvInfo[11] = rodsLogLevel 
        srvInfo[12] = rodsAuthFileName 
        srvInfo[13] = rodsDebug 
     */
    public static String[] getServerInfo() throws Exception
    {
        String srvInfo[] = new String[14];

        _getServerInfo(srvInfo);

        return srvInfo;
    }
    
    private synchronized static native void _getServerInfo(String srvInfo[]) throws Exception;
  
    
    /**
     * Get seperator for individual fied of file list 
     */
    public synchronized static native String getFileFieldSeparator() throws Exception;


    /**
     *  Get file/directory names from server
     *  @param fileList the list of all files in the server
     *  @return the number of file in the server
     */
    public synchronized static native int getFileList(java.util.Vector fileList) throws Exception;

    /**
     *   Process a data request
     **/
    public synchronized static native int h5ObjRequest (Object obj, int obj_type) throws Exception;
}

