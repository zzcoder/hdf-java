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

package ncsa.hdf.object;

import java.io.File;
import java.util.*;
import javax.swing.tree.*;
import ncsa.hdf.object.*;
import ncsa.hdf.hdflib.*;

/**
 * This class provides file level APIs. File access APIs include retrieving the
 * file hierarchy, opening and closing file, and writing file content to disk.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H4File extends File implements FileFormat
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
     * The full name of this file, path+name
     */
    private final String fullFileName;

    /**
     * The root node of the tree structure of this file.
     */
    private MutableTreeNode rootNode;

    /**
     * The list of unique (tag, ref) pairs.
     * It is used to avoid duplicate objects in memory.
     */
    private List objList;

    /**
     * The GR interface identifier.
     * The identifier is returned by GRstart(fid), which initializes the GR
     * interface for the file specified by the parameter. GRstart(fid) is an
     * expensive call. It should be called only once. Calling GRstart(fid) in
     * a loop should be avoided.
     */
    private int grid;

    /**
     * The SDS interface identifier.
     * The identifier is returned by SDstart(fname, flag), which initializes the
     * SD interface for the file specified by the parameter. SDstart(fname, flag)
     * is an expensive call. It should be called only once. Calling
     * SDstart(fname, flag) in a loop should be avoided.
     */
    private int sdid;

    /**
     * Creates an H4File with read only access.
     */
    public H4File(String pathname)
    {
        this(pathname, HDFConstants.DFACC_READ);
    }

    /**
     * Creates an H4File with specific full file name and access flag.
     * <p>
     * @param pathname the full path of the file name.
     * @param flag the file access flag, HDF provides several file access
     * code definitions:
     * <DL><DL>
     * <DT> DFACC_READ <DD> Open for read only. If file does not exist,
     *      an error condition results.</DT>
     * <DT> DFACC_CREATE <DD> If file exists, delete it, then open a new file
     *      for read/write.</DT>
     * <DT> DFACC_WRITE <DD> Open for read/write. If file does not exist,
     *      create it.</DT>
     * </DL></DL>
     */
    public H4File(String pathname, int flag)
    {
        super(pathname);

        this.fid = -1;
        this.flag = flag;
        this.fullFileName = pathname;
    }

    /**
     * Checks if the given file is an HDF4 file.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is an HDF4 file; otherwise returns false.
     */
    public static boolean isThisType(String filename)
    {
        boolean isH4 = false;

        try {
            isH4 = HDFLibrary.Hishdf(filename);
        } catch (HDFException ex)
        {
            isH4 = false;
        }

        return isH4;
    }

    // Implementing FileFormat
    public int open() throws Exception
    {
        if ( fid >=0 )
            return fid; // file is openned already

        // check for valid file access permission
        if (flag < 0) // invalid access id
        {
            throw new HDFException("Invalid access identifer -- "+flag);
        }
        else if (flag == HDFConstants.DFACC_READ)
        {
           if (!exists())
                throw new HDFException("File does not exist -- "+fullFileName);
            else if (exists() && !canRead())
                throw new HDFException("Cannot read file -- "+fullFileName);
        }
        else if (flag == HDFConstants.DFACC_WRITE ||
            flag == HDFConstants.DFACC_CREATE)
        {
            if (exists() && !canWrite())
                throw new HDFException("Cannot write file -- "+fullFileName);
        }

        try
        {
            fid = HDFLibrary.Hopen( fullFileName, flag);
        }
        catch (HDFException ex)
        {
            fid = -1;
        }

        if ( fid>=0 )
        {
            try
            {
                HDFLibrary.Vstart(fid);
                grid = HDFLibrary.GRstart(fid);
                sdid = HDFLibrary.SDstart(fullFileName, flag);
            } catch (HDFException ex) {}

            // load the file hierarchy
            rootNode = loadTree();
        }

        return fid;
    }

    // Implementing FileFormat
    public void close() throws HDFException
    {
        try { HDFLibrary.GRend(grid); } catch (HDFException ex) {}
        try { HDFLibrary.SDend(sdid); } catch (HDFException ex) {}
        try { HDFLibrary.Vend(fid); } catch (HDFException ex) {}

        HDFLibrary.Hclose(fid);
        fid = -1;
        objList = null;
    }

    // Implementing FileFormat
    public MutableTreeNode getRootNode()
    {
        return rootNode;
    }

    // Implementing FileFormat
    public String getFilePath()
    {
        return fullFileName;
    }

    /**
     * Retrieves and returns the file structure from disk.
     * <p>
     * First gets the top level objects or objects that do not belong to any groups.
     * If a top level object is a group, call the depth_first() to retrieve
     * the sub-tree of that group, recursively.
     *
     */
    private MutableTreeNode loadTree()
    {
        if (fid <0 ) return null;

        long[] oid = {0, 0};
        int n=0, tag=-1, ref=-1;
        int[] argv = null;
        MutableTreeNode node = null;

        H4Group rootGroup = new H4Group(
            this,
            getName(), // set the node name to the file name
            null, // root node does not have a parent path
            null, // root node does not have a parent node
            oid);

        if (objList == null)
            objList = new Vector(100);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootGroup)
        {
            public boolean isLeaf() { return false; }
        };

        // get top level VGroup
        int[] tmpN = new int[1];
        int[] refs = null;
        String[] vClass = new String[1];
        try {
            // first call to get the number of lone Vgroup
            n = HDFLibrary.Vlone(fid, tmpN, 0);
            refs = new int[n];
            // second call to get the references of all lone Vgroup
            n = HDFLibrary.Vlone(fid, refs, n);
        } catch (HDFException ex) { n = 0; }

        for (int i=0; i<n; i++)
        {
            ref = refs[i];
            H4Group g = getVGroup(ref, HObject.separator, rootGroup, false);
            if (g != null)
            {
                node = new DefaultMutableTreeNode(g)
                {
                    public boolean isLeaf() { return false; }
                };
                root.add( node );
                rootGroup.addToMemberList(g);

                // recursively get the sub-tree
                depth_first(node);
            }
        } // for (int i=0; i<n; i++)

        // get the top level GR images
        argv = new int[2];
        boolean b = false;
        try {
            b = HDFLibrary.GRfileinfo(grid,argv);
        } catch (HDFException ex)
        {
            b = false;
        }

        if ( b )
        {
            n = argv[0];
            for (int i=0; i<n; i++)
            {
                // no duplicate object at top level
                H4GRImage gr = getGRImage(i, HObject.separator, false);
                if (gr != null)
                {
                    node = new DefaultMutableTreeNode(gr);
                    root.add( node );
                    rootGroup.addToMemberList(gr);
                }
            } // for (int i=0; i<n; i++)
        } // if ( grid!=HDFConstants.FAIL && HDFLibrary.GRfileinfo(grid,argv) )

        // get top level SDS
        try {
            b = HDFLibrary.SDfileinfo(sdid, argv);
        } catch (HDFException ex)
        {
            b = false;
        }

        if (b)
        {
            n = argv[0];
            for (int i=0; i<n; i++)
            {
                // no duplicate object at top level
                H4SDS sds = getSDS(i, HObject.separator, false);
                if (sds != null)
                {
                    node = new DefaultMutableTreeNode(sds);
                    root.add( node );
                    rootGroup.addToMemberList(sds);
                }
            } // for (int i=0; i<n; i++)
        } // if (sdid != HDFConstants.FAIL && HDFLibrary.SDfileinfo(sdid, argv))

        // get top level VData
        try {
            n = HDFLibrary.VSlone(fid, tmpN, 0);
            refs = new int[n];
            n = HDFLibrary.VSlone(fid, refs, n);
        } catch (HDFException ex)
        {
            n = 0;
        }
        for (int i=0; i<n; i++)
        {
            ref = refs[i];

            // no duplicate object at top level
            H4Vdata vdata = getVdata(ref, HObject.separator, false);

            if (vdata != null)
            {
                node = new DefaultMutableTreeNode(vdata);
                root.add( node );
                rootGroup.addToMemberList(vdata);
            }
        } // for (int i=0; i<n; i++)

        return root;
    }

    /**
     * Retrieves the tree structure of the file by depth-first order.
     * The current implementation only retrieves group and dataset. It does
     * not include named datatype and soft links.
     * <p>
     * @param parentNode the parent node.
     */
    private void depth_first(MutableTreeNode parentNode)
    {
        //System.out.println("H4File.depth_first() pnode = "+parentNode);
        int nelems=0, ref=-1, tag=-1, id=-1, index=-1;
        int[] tags = null;
        int[] refs = null;
        MutableTreeNode node = null;

        DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)parentNode;
        H4Group pgroup = (H4Group)(pnode.getUserObject());
        String fullPath = pgroup.getPath()+pgroup.getName()+HObject.separator;

        int gid = pgroup.open();
        if (gid == HDFConstants.FAIL)
        {
            return;
        }

        try
        {
            nelems = HDFLibrary.Vntagrefs(gid);
            tags = new int[nelems];
            refs = new int[nelems];
            nelems = HDFLibrary.Vgettagrefs(gid, tags, refs, nelems);
        } catch (HDFException ex)
        {
            nelems = 0;
        } finally
        {
            pgroup.close(gid);
        }

        // interate through all the objects under this group
        for (int i=0; i<nelems; i++)
        {
            tag = tags[i];
            ref = refs[i];

            switch (tag)
            {
                case HDFConstants.DFTAG_RIG:
                case HDFConstants.DFTAG_RI:
                case HDFConstants.DFTAG_RI8:
                    try {
                        index = HDFLibrary.GRreftoindex(grid, (short)ref);
                    } catch (HDFException ex)
                    {
                        index = HDFConstants.FAIL;
                    }
                    if (index != HDFConstants.FAIL)
                    {
                        H4GRImage gr = getGRImage(index, fullPath, true);
                        if (gr != null)
                        {
                            node = new DefaultMutableTreeNode(gr);
                            pnode.add( node );
                            pgroup.addToMemberList(gr);
                        }
                    }
                    break;
                case HDFConstants.DFTAG_SD:
                case HDFConstants.DFTAG_SDG:
                case HDFConstants.DFTAG_NDG:
                    try {
                        index = HDFLibrary.SDreftoindex(sdid, ref);
                    } catch (HDFException ex)
                    {
                        index = HDFConstants.FAIL;
                    }
                    if (index != HDFConstants.FAIL)
                    {
                        H4SDS sds = getSDS(index, fullPath, true);
                        if (sds != null)
                        {
                            node = new DefaultMutableTreeNode(sds);
                            pnode.add( node );
                            pgroup.addToMemberList(sds);
                        }
                    }
                    break;
                case HDFConstants.DFTAG_VH:
                case HDFConstants.DFTAG_VS:
                    H4Vdata vdata = getVdata(ref, fullPath, true);
                    if (vdata != null)
                    {
                        node = new DefaultMutableTreeNode(vdata);
                        pnode.add( node );
                        pgroup.addToMemberList(vdata);
                    }
                    break;
                case HDFConstants.DFTAG_VG:
                    H4Group vgroup = getVGroup(ref, fullPath, pgroup, true);

                    if (vgroup != null)
                    {
                        node = new DefaultMutableTreeNode(vgroup)
                        {
                            public boolean isLeaf() { return false; }
                        };

                        pnode.add( node );
                        pgroup.addToMemberList(vgroup);

                        // check for loops
                        boolean looped = false;
                        DefaultMutableTreeNode theNode = (DefaultMutableTreeNode)pnode;
                        while (theNode != null && !looped)
                        {
                            H4Group theGroup = (H4Group)theNode.getUserObject();
                            long[] oid = {tag, ref};
                            if (theGroup.equalsOID(oid))
                                looped = true;
                            else
                                theNode = (DefaultMutableTreeNode)theNode.getParent();
                        }
                        if (!looped)
                            depth_first(node);
                    }
                    break;
                default:
                    break;
            } //switch (tag)

        } //for (int i=0; i<nelms; i++)

    } // private depth_first()

    /**
     * Retrieve an GR image for the given GR image identifier and index.
     * <p>
     * @param index the index of the image.
     * @param path the path of the image.
     * @param copyAllowed The indicator if multiple copies of an object is allowed.
     * @return the new H5GRImage if successful; otherwise returns null.
     */
    private final H4GRImage getGRImage(int index, String path, boolean copyAllowed)
    {
        int id=-1, ref=-1;
        H4GRImage gr = null;
        String[] objName = {""};
        int[]  tmpInfo = new int[4];
        int tag = HDFConstants.DFTAG_RIG;

        try
        {
            id = HDFLibrary.GRselect(grid, index);
            ref = HDFLibrary.GRidtoref(id);
            HDFLibrary.GRgetiminfo(id, objName, tmpInfo, tmpInfo);
        } catch (HDFException ex)
        {
            id = HDFConstants.FAIL;
        }
        finally
        {
            try { HDFLibrary.GRendaccess(id); }
            catch (HDFException ex ) {}
        }

        if (id != HDFConstants.FAIL)
        {
            long oid[] = {tag, ref};

            if (copyAllowed)
            {
                objList.add(oid);
            } else if (find(oid))
            {
                return null;
            }

            gr = new H4GRImage(
                this,
                objName[0],
                path,
                oid);
        }

        return gr;
    }

    /**
     * Retrieve a SDS for the given sds identifier and index.
     * <p>
     * @param sdid the SDS idendifier.
     * @param index the index of the SDS.
     * @param path the path of the SDS.
     * @param copyAllowed The indicator if multiple copies of an object is allowed.
     * @return the new H4SDS if successful; otherwise returns null.
     */
    private final H4SDS getSDS(int index, String path, boolean copyAllowed)
    {
        int id=-1, ref=-1;
        H4SDS sds = null;
        String[] objName = {""};
        int[]  tmpInfo = new int[4];
        int tag = HDFConstants.DFTAG_NDG;

        boolean isCoordvar = false;
        try
        {
            id = HDFLibrary.SDselect(sdid, index);
            ref = HDFLibrary.SDidtoref(id);
            HDFLibrary.SDgetinfo(id, objName, tmpInfo, tmpInfo);
            isCoordvar = HDFLibrary.SDiscoordvar(id);
        } catch (HDFException ex)
        {
            id = HDFConstants.FAIL;
        }
        finally
        {
            try { HDFLibrary.SDendaccess(id); }
            catch (HDFException ex) {}
        }

        // check if the given SDS has dimension metadata
        // Coordinate variables are not displayed. They are created to store
        // metadata associated with dimensions. To ensure compatibility with
        // netCDF, coordinate variables are implemented as data sets
        if (id != HDFConstants.FAIL && !isCoordvar)
        {
            long oid[] = {tag, ref};

            if (copyAllowed)
            {
                objList.add(oid);
            } else if (find(oid))
            {
                return null;
            }

            sds = new H4SDS(
                this,
                objName[0],
                path,
                oid);
        }

        return sds;
    }

    /**
     * Retrieve a Vdata for the given Vdata identifier and index.
     * <p>
     * @param ref the reference idendifier of the Vdata.
     * @param path the path of the Vdata.
     * @param copyAllowed The indicator if multiple copies of an object is allowed.
     * @return the new H4Vdata if successful; otherwise returns null.
     */
    private final H4Vdata getVdata(int ref, String path, boolean copyAllowed)
    {
        int id=-1;
        H4Vdata vdata = null;
        String[] objName = {""};
        String[] vClass = {""};
        int tag = HDFConstants.DFTAG_VS;
        long oid[] = {tag, ref};

        if (copyAllowed)
        {
            objList.add(oid);
        } else if (find(oid))
        {
            return null;
        }

        try
        {
            id = HDFLibrary.VSattach(fid, ref, "r");
            HDFLibrary.VSgetclass(id, vClass);
            vClass[0] = vClass[0].trim();
            HDFLibrary.VSgetname(id, objName);
        } catch (HDFException ex)
        {
            id = HDFConstants.FAIL;
        }
        finally
        {
            try { HDFLibrary.VSdetach(id); }
            catch (HDFException ex) {}
        }

        if (id != HDFConstants.FAIL &&
            // do not display Vdata named "Attr0.0"
            !vClass[0].equalsIgnoreCase(HDFConstants.HDF_ATTRIBUTE) &&
            // do not display internal Vdata
            !vClass[0].equalsIgnoreCase(HDFConstants.HDF_CHK_TBL))
        {
            vdata = new H4Vdata(
                this,
                objName[0],
                path,
                oid);
        }

        return vdata;
    }

    /**
     * Retrieve a VGroup for the given VGroup identifier and index.
     * <p>
     * @param ref the reference idendifier of the VGroup.
     * @param path the path of the VGroup.
     * @param pgroup the parent group.
     * @param copyAllowed The indicator if multiple copies of an object is allowed.
     * @return the new H4VGroup if successful; otherwise returns null.
     */
    private final H4Group getVGroup(int ref, String path, H4Group pgroup, boolean copyAllowed)
    {
        int id=-1;
        H4Group vgroup  = null;
        String[] objName = {""};
        String[] vClass = {""};
        int tag = HDFConstants.DFTAG_VG;
        long oid[] = {tag, ref};

        if (copyAllowed)
        {
            objList.add(oid);
        } else if (find(oid))
        {
            return null;
        }

        try
        {
            id = HDFLibrary.Vattach(fid, ref, "r");
            HDFLibrary.Vgetclass(id, vClass);
            vClass[0] = vClass[0].trim();
            HDFLibrary.Vgetname(id, objName);
        } catch (HDFException ex)
        {
            id = HDFConstants.FAIL;
        }
        finally
        {
            try { HDFLibrary.Vdetach(id); }
            catch (HDFException ex) {}
        }

        // ignore the Vgroups created by the GR interface
        if (id != HDFConstants.FAIL &&
            // do not display Vdata named "Attr0.0"
            !vClass[0].equalsIgnoreCase(HDFConstants.GR_NAME) &&
            !vClass[0].equalsIgnoreCase(HDFConstants.RI_NAME) &&
            !vClass[0].equalsIgnoreCase(HDFConstants.RIGATTRNAME) &&
            !vClass[0].equalsIgnoreCase(HDFConstants.RIGATTRCLASS) &&
            !vClass[0].equalsIgnoreCase(HDFConstants.HDF_CDF))
        {
                vgroup = new H4Group(
                    this,
                    objName[0],
                    path,
                    pgroup,
                    oid);
        }

        return vgroup;
    }

    /**
     * Check if object already exists in memory by match the (tag, ref) pairs.
     */
    private final boolean find(long[] oid)
    {
        boolean existed = false;

        if (objList == null)
            return false;

        int n = objList.size();
        long[] theOID = null;

        for (int i=0; i<n; i++)
        {
            theOID = (long[])objList.get(i);
            if (theOID[0]==oid[0] && theOID[1]==oid[1])
            {
                existed = true;
                break;
            }
        }

        if (!existed)
        {
            objList.add(oid);
        }

        return existed;
    }

    /**
     * Returns the GR identifier, which is returned from GRstart(fid).
     */
    int getGRAccessID()
    {
        return grid;
    }

    /**
     * Returns the SDS identifier, which is returned from SDstart(fname, flag).
     */
    int getSDAccessID()
    {
        return sdid;
    }
}
