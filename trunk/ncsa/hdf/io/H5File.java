/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf  COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.io;

import java.io.File;
import java.util.*;
import javax.swing.tree.*;
import ncsa.hdf.object.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * This class provides file level APIs. File access APIs include retrieving the
 * file hierarchy, opening and closing file, and writing file content to disk.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5File extends DefaultFileFormat
{
    /**
     * file identifier for the open file.
     */
    private int fid;

    /**
     * the file access flag.
     */
    private int flag;

    /**
     * The full name of this file; full path plus name
     */
    private final String fullFileName;

    /**
     * The root node of the file hierearchy.
     */
    private MutableTreeNode rootNode;

    /**
     * Creates an H5File object with specific file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     * <DL><DL>
     * <DT> H5F_ACC_RDWR <DD> Allow read and write access to file.</DT>
     * <DT> H5F_ACC_RDONLY <DD> Allow read-only access to file.</DT>
     * </DL></DL>
     */
    public H5File(String pathname, int flag)
    {
        super(pathname);

        this.fid = -1;
        this.flag = flag;
        this.fullFileName = pathname;
    }

    /**
     * Checks if a given file is an HDF5 file.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public static boolean isThisType(String filename)
    {
        boolean isH5 = false;

        try {
            isH5 = H5.H5Fis_hdf5(filename);
        } catch (HDF5Exception ex)
        {
            isH5 = false;
        }

        return isH5;
    }

    /**
     * Opens access to the file resource.
     * <p>
     * @return true if the file open is succsessful; otherwise returns false.
     */
    public boolean open() throws Exception
    {
        // check for valid file access permission
        if (!exists())
            throw new HDF5Exception("File does not exist -- "+fullFileName);
        else if ( flag < 0)
            throw new HDF5Exception("Invalid access identifer -- "+flag);
        else if ((flag == HDF5Constants.H5F_ACC_RDONLY) && !canRead())
            throw new HDF5Exception("Cannot read file -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDWR) && !canWrite())
            throw new HDF5Exception("Cannot write file -- "+fullFileName);

        fid = H5.H5Fopen( fullFileName, flag, HDF5Constants.H5P_DEFAULT);

        boolean b = (fid>=0);

        if (b)
        {
            // load the hierearchy of the file
            rootNode = loadTree();
        }

        return b;
    }

    /**
     * Closes access to the file resource.
     * <p>
     * @return true if file close is succsessful; otherwise returns false.
     */
    public boolean close()
    {
        boolean b = true;

        try
        {
            H5.H5Fclose(fid);
            fid = -1;
        } catch (HDF5Exception ex)
        {
            b = false;
        }

        return b;
    }

    /**
     * Returns the root node of the file
     */
    public MutableTreeNode getRootNode()
    {
        return rootNode;
    }

    /**
     * Retrieves and returns the tree structure from the file
     */
    private MutableTreeNode loadTree()
    {
        if (fid <0 ) return null;

        long[] oid = {0};
        H5Group rootGroup = new H5Group(
            fid,
            fullFileName,
            getName(), // set the name of the root node to the name of the file
            null, // root node does not have a parent path
            null, // root node does not have a parent node
            oid);

        MutableTreeNode root = new DefaultMutableTreeNode(rootGroup);

        depth_first(root);

        return root;
    }

    /**
     * Retrieves the file structure by depth-first order, recursively.
     * The current implementation retrieves group and dataset only. It does
     * not include named datatype and soft links.
     * <p>
     * It also detects and stops loops. A loop is detected if there exists
     * object with the same object ID by tracing path back up to the root.
     * <p>
     * @param parentNode the parent node.
     */
    private void depth_first(MutableTreeNode parentNode)
    {
        //System.out.println("H5File.depth_first() pnode = "+parentNode);
        int nelems;
        MutableTreeNode node = null;
        String fullPath = null;
        String ppath = null;
        DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)parentNode;

        H5Group pgroup = (H5Group)(pnode.getUserObject());
        ppath = pgroup.getPath();

        if (ppath == null)
        {
            fullPath = HObject.separator;
        }
        else
        {
            fullPath = ppath+pgroup.getName()+HObject.separator;
        }

        nelems = 0;
        try {
            nelems = H5.H5Gn_members(fid, fullPath);
        } catch (HDF5Exception ex) {
            nelems = -1;
        }

        if (nelems < 0 ) {
            return;
        }

        int[] oType = new int[1];
        long[] oid = null;
        String [] oName = new String[1];
        //Iterate through the file to see members of the group
        for ( int i = 0; i < nelems; i++)
        {
            oName[0] = "";
            oType[0] = -1;
            oid = null;
            try {
                H5.H5Gget_obj_info_idx(fid, fullPath, i, oName, oType );
            } catch (HDF5Exception ex) {
                // do not stop if accessing one member fails
                continue;
            }
            try
            {
                // retrieve the object ID.
                byte[] ref_buf = H5.H5Rcreate(
                    fid,
                    fullPath+oName[0],
                    HDF5Constants.H5R_OBJECT,
                    -1);
                long l = HDFNativeData.byteToLong(ref_buf, 0);
                oid = new long[1];
                oid[0] = l; // save the object ID
            } catch (HDF5Exception ex) {}

            if (oid == null)
                continue; // do the next one, if the object is not identified.

            switch (oType[0])
            {
                // create a new group
                case HDF5Constants.H5G_GROUP:
                    H5Group g = new H5Group(
                        fid,
                        fullFileName,
                        oName[0],
                        fullPath,
                        pgroup,
                        oid);
                    node = new DefaultMutableTreeNode(g);
                    pnode.add( node );

                    // detect and stop loops
                    // a loop is detected if there exists object with the same
                    // object ID by tracing path back up to the root.
                    boolean hasLoop = false;
                    HObject tmpObj = null;
                    long[] tmpOID = null;
                    DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode)pnode;
                    while (tmpNode != null)
                    {
                        tmpObj = (HObject)tmpNode.getUserObject();
                        tmpOID = tmpObj.getOID();
                        if (tmpOID!= null && oid[0] == tmpOID[0])
                        {
                            hasLoop = true;
                            break;
                        }
                        else
                            tmpNode = (DefaultMutableTreeNode)tmpNode.getParent();
                    }

                    // recursively go through the next group
                    // stops if it has loop.
                    if (!hasLoop)
                        depth_first(node);
                    break;
                case HDF5Constants.H5G_DATASET:
                    int did=-1, tid=-1, tclass=-1;
                    try {
                        did = H5.H5Dopen(fid, fullPath+oName[0]);
                        tid = H5.H5Dget_type(did);
                        tclass = H5.H5Tget_class(tid);
                    } catch (HDF5Exception ex) {}
                    finally {
                        try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
                        try { H5.H5Dclose(did); } catch (HDF5Exception ex) {}
                    }
                    Dataset d = null;

                    // Todo: what about ARRAY of scalar or compound data
                    if (tclass == HDF5Constants.H5T_COMPOUND)
                    {
                        // create a new compound dataset
                        d = new H5CompoundDS(
                            fid,
                            fullFileName,
                            oName[0],
                            fullPath,
                            oid);
                    }
                    else
                    {
                        // create a new scalar dataset
                        d = new H5ScalarDS(
                            fid,
                            fullFileName,
                            oName[0],
                            fullPath,
                            oid);
                    }

                    node = new DefaultMutableTreeNode(d);
                    pnode.add( node );
                    break;
                default:
                    break;
            } // switch (oType[0]) {
        } // for ( i = 0; i < nelems; i++) {
    } // private depth_first()

}