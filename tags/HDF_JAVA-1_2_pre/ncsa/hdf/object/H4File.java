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
import java.lang.reflect.Array;
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

    private boolean isReadOnly;

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
        this(pathname, READ);
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
    public H4File(String pathname, int access)
    {
        super(pathname);
        isReadOnly = (access == READ);

        this.fid = -1;
        this.fullFileName = pathname;

        if (access == READ)
            flag = HDFConstants.DFACC_READ;
        else if (access == WRITE)
            flag = HDFConstants.DFACC_WRITE;
        else if (access == CREATE)
            flag = HDFConstants.DFACC_CREATE;
        else
            flag = access;
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
                throw new HDFException("Cannot write file, try open as read-only -- "+fullFileName);
        }

        fid = HDFLibrary.Hopen( fullFileName, flag);

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
        // clean unused objects
        if (rootNode != null)
        {
            DefaultMutableTreeNode theNode = null;
            HObject theObj = null;
            Enumeration enum = ((DefaultMutableTreeNode)rootNode).breadthFirstEnumeration();
            while(enum.hasMoreElements())
            {
                theNode = (DefaultMutableTreeNode)enum.nextElement();
                theObj = (HObject)theNode.getUserObject();
                if (theObj instanceof Dataset) ((Dataset)theObj).clearData();
                theObj = null;
                theNode = null;
            }
        }

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

    // Implementing FileFormat
    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    /**
     * Creates a new HDF4 file.
     * @param fileName the name of the file to create.
     */
    public static void create(String fileName) throws Exception
    {
        int fileid = HDFLibrary.Hopen(fileName, HDFConstants.DFACC_CREATE);
        try { HDFLibrary.Hclose(fileid); } catch (HDFException ex) {}
    }

    /**
     * Delete an object from the file.
     * @param obj the data object to delete.
     */
    public void delete(HObject obj) throws Exception
    {
        throw (new UnsupportedOperationException(
            "Cannot delete HDF4 object."));
    }

    /**
     * Copy an object to a group.
     * @param srcObj   the object to copy.
     * @param dstGroup the destination group.
     * @return the new node containing the new object.
     */
    public TreeNode copy(HObject srcObj,
        H4Group dstGroup) throws Exception
    {
        TreeNode newNode = null;

        if (srcObj == null ||
            dstGroup == null)
            return null;

        if (srcObj instanceof H4SDS)
        {
            newNode = new DefaultMutableTreeNode(
            copy((H4SDS)srcObj, dstGroup, srcObj.getName(), null, null));
        }
        else if (srcObj instanceof H4GRImage)
        {
            newNode = new DefaultMutableTreeNode(
            copy((H4GRImage)srcObj, dstGroup, srcObj.getName(), null, null));
        }
        else if (srcObj instanceof H4Vdata)
        {
           newNode = new DefaultMutableTreeNode(copy((H4Vdata)srcObj, dstGroup));
        }
        else if (srcObj instanceof H4Group)
        {
            newNode = copy((H4Group)srcObj, dstGroup);
        }

        return newNode;
    }

    /**
     * Creates a new attribute and attached to the object if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param obj the object which the attribute is to be attached to.
     * @param attr the atribute to attach.
     * @return true if successful and false otherwise.
     */
    public static void writeAttribute(HObject obj, Attribute attr)
    throws HDFException
    {
        String attrName = attr.getName();
        int attrType = attr.getType();
        long[] dims = attr.getDataDims();
        int count = 1;
        if (dims != null)
        {
            for (int i=0; i<dims.length; i++)
                count *= (int)dims[i];
        }

        Object attrValue = attr.getValue();
         if (Array.get(attrValue, 0) instanceof String)
        {
            String strValue = (String)Array.get(attrValue, 0);

            if (strValue.length() > count)
            {
                // truncate the extra characters
                strValue = strValue.substring(0, count);
                Array.set(attrValue, 0, strValue);
            }
            else
            {
                // pad space to the unused space
                for (int i=strValue.length(); i<count; i++)
                    strValue += " ";
            }

            byte[] bval = strValue.getBytes();
            // add null to the end to get rid of the junks
            bval[(strValue.length() - 1)] = 0;
            attrValue = bval;
        }

        int id = obj.open();
        if (obj instanceof H4Group)
            HDFLibrary.Vsetattr(id, attrName, attrType, count, attrValue);
        else if (obj instanceof H4SDS)
            HDFLibrary.SDsetattr(id, attrName, attrType, count, attrValue);
        else if (obj instanceof H4GRImage)
            HDFLibrary.GRsetattr(id, attrName, attrType, count, attrValue);
        else if (obj instanceof H4Vdata)
            HDFLibrary.VSsetattr(id, -1, attrName, attrType, count, attrValue);
        obj.close(id);
    }

    /**
     * copy attributes from one SDS to another SDS
     */
    private void copyAttributeSDS(int srcdid, int dstdid)
    {
        try {
            String[] objName = {""};
            int[] sdInfo = {0, 0, 0};
            int[] tmpDim = new int[HDFConstants.MAX_VAR_DIMS];
            HDFLibrary.SDgetinfo(srcdid, objName, tmpDim, sdInfo);
            int numberOfAttributes = sdInfo[2];

            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = {0, 0};
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.SDattrinfo(srcdid, i, attrName, attrInfo);
                } catch (HDFException ex) { b = false; }

                if (!b) continue;

                // read attribute data from source dataset
                byte[] attrBuff = new byte[attrInfo[1] * HDFLibrary.DFKNTsize(attrInfo[0])];
                try { HDFLibrary.SDreadattr(srcdid, i, attrBuff);
                } catch (HDFException ex) { attrBuff = null; }

                if (attrBuff == null)  continue;

                // attach attribute to the destination dataset
                HDFLibrary.SDsetattr(dstdid, attrName[0], attrInfo[0], attrInfo[1], attrBuff);
            } // for (int i=0; i<numberOfAttributes; i++)
        } catch (Exception ex) {}
    }

    /**
     * copy attributes from one GR image to another GR image
     */
    private void copyAttributeGR(int srcdid, int dstdid, int numberOfAttributes)
    {
        if (numberOfAttributes <=0 )
            return;

        try {
            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = {0, 0};
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.GRattrinfo(srcdid, i, attrName, attrInfo);
                } catch (HDFException ex) { b = false; }

                if (!b) continue;

                // read attribute data from source dataset
                byte[] attrBuff = new byte[attrInfo[1] * HDFLibrary.DFKNTsize(attrInfo[0])];
                try { HDFLibrary.GRgetattr(srcdid, i, attrBuff);
                } catch (Exception ex) { attrBuff = null; }

                if (attrBuff == null)  continue;

                // attach attribute to the destination dataset
                HDFLibrary.GRsetattr(dstdid, attrName[0], attrInfo[0], attrInfo[1], attrBuff);
            } // for (int i=0; i<numberOfAttributes; i++)
        } catch (Exception ex) {}
    }

    public Dataset copy(H4SDS sds, H4Group pgroup,
        String dname, int[] count, Object buff) throws Exception
    {
        Dataset dataset = null;
        int srcdid=-1, dstdid=-1, tid=-1, size=1, rank;
        String path=null;

        if (sds == null || pgroup == null)
            return null;

        if (pgroup.isRoot()) path = HObject.separator;
        else path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        srcdid = sds.open();
        if (srcdid < 0)
            return null;

        int[] start = {0, 0};
        if (count == null)
        {
            rank = sds.getRank();
            if (rank <=0) sds.init();
            rank = sds.getRank();

            long[] dims = sds.getDims();
            start = new int[rank];
            count = new int[rank];
            for (int i=0; i<rank; i++)
            {
                start[i] = 0;
                count[i] = (int)dims[i];
                size *= count[i];
            }
        }
        else
        {
            rank = 2;
        }

        // create the new dataset and attached it to the parent group
        tid = sds.getDataType();
        dstdid = HDFLibrary.SDcreate(sdid, dname, tid, rank, count);
        if (dstdid < 0) return null;

        int ref = HDFLibrary.SDidtoref(dstdid);
        if (!pgroup.isRoot())
        {
            int vgid = pgroup.open();
            HDFLibrary.Vaddtagref(vgid, HDFConstants.DFTAG_NDG, ref);
            pgroup.close(vgid);
        }

        // copy attributes from one object to the new object
        copyAttributeSDS(srcdid, dstdid);

        // read data from the source dataset
        if (buff == null)
        {
            buff = new byte[size * HDFLibrary.DFKNTsize(tid)];
            HDFLibrary.SDreaddata(srcdid, start, null, count, buff);
        }

        // write the data into the destination dataset
        HDFLibrary.SDwritedata(dstdid, start, null, count, buff);

        long[] oid = {HDFConstants.DFTAG_NDG, ref};
        dataset = new H4SDS(this, dname, path, oid);

        pgroup.addToMemberList(dataset);

        sds.close(srcdid);
        try { HDFLibrary.SDendaccess(dstdid); }
        catch (HDFException ex) { ; }

        return dataset;
    }

    /** copy an image. */
    public Dataset copy(H4GRImage img, H4Group pgroup,
        String dname, int[] count, Object buff) throws Exception
    {
        Dataset dataset = null;
        int srcdid=-1, dstdid=-1, size=1;
        String path=null;

        if (img == null || pgroup == null)
            return null;

        if (pgroup.isRoot()) path = HObject.separator;
        else path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        srcdid = img.open();
        if (srcdid < 0)
            return null;

        int rank = 2;
        int[] start = new int[rank];
        int[] grInfo = new int[4]; //ncomp, data_type, interlace and num_attrs
        try {
            String[] tmpName = {""};
            int[] tmpDims = new int[2];
            HDFLibrary.GRgetiminfo(srcdid, tmpName, grInfo, tmpDims);
            if (count == null) count = tmpDims;
        } catch (HDFException ex) {}

        int ncomp = grInfo[0];
        int tid = grInfo[1];
        int interlace = grInfo[2];
        int numberOfAttributes = grInfo[3];
        dstdid = HDFLibrary.GRcreate(grid, dname, ncomp, tid, interlace, count);
        if (dstdid < 0) return null;

        int ref = HDFLibrary.GRidtoref(dstdid);
        if (!pgroup.isRoot())
        {
            int vgid = pgroup.open();
            HDFLibrary.Vaddtagref(vgid, HDFConstants.DFTAG_RIG, ref);
            pgroup.close(vgid);
        }

        // copy palette
        int pid = HDFLibrary.GRgetlutid(srcdid, 0);
        int[] palInfo = new int[4];
        HDFLibrary.GRgetlutinfo(pid, palInfo);
        int palSize = palInfo[0]*HDFLibrary.DFKNTsize(palInfo[1])*palInfo[3];
        byte[] palBuff = new byte[palSize];
        HDFLibrary.GRreadlut(pid, palBuff);
        pid = HDFLibrary.GRgetlutid(dstdid, 0);
        HDFLibrary.GRwritelut(pid, palInfo[0], palInfo[1], palInfo[2], palInfo[3], palBuff);

        // copy attributes from one object to the new object
        copyAttributeGR(srcdid, dstdid, numberOfAttributes);

        for (int i=0; i<rank; i++)
        {
            start[i] = 0;
            size *= count[i];
        }

        // read data from the source dataset
        if (buff == null)
        {
            buff = new byte[size * HDFLibrary.DFKNTsize(tid)];
            HDFLibrary.GRreadimage(srcdid, start, null, count, buff);
        }

        // write the data into the destination dataset
        HDFLibrary.GRwriteimage(dstdid, start, null, count, buff);

        long[] oid = {HDFConstants.DFTAG_RIG, ref};
        dataset = new H4GRImage(this, dname, path, oid);

        pgroup.addToMemberList(dataset);

        img.close(srcdid);
        try { HDFLibrary.GRendaccess(dstdid); }
        catch (HDFException ex) {;}

        return dataset;
    }

    private Dataset copy(H4Vdata vdata, H4Group pgroup) throws Exception
    {
        Dataset dataset = null;
        int srcdid=-1, dstdid=-1;
        String dname=null, path=null;

        if (vdata == null || pgroup == null)
            return null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        dname = vdata.getName();

        srcdid = vdata.open();
        if (srcdid < 0) return null;

        // create a new vdata
        dstdid = HDFLibrary.VSattach(fid, -1, "w");
        HDFLibrary.VSsetname(dstdid, dname);
        String[] vclass = {""};
        HDFLibrary.VSgetclass(srcdid, vclass);
        HDFLibrary.VSsetclass(dstdid, vclass[0]);

        // set fields in the new vdata
        int size=1, fieldType=-1, fieldOrder=1;
        String fieldName = null;
        String nameList = "";
        int numberOfFields = HDFLibrary.VFnfields(srcdid);
        int numberOfRecords = HDFLibrary.VSelts(srcdid);
        for (int i=0; i<numberOfFields; i++)
        {
            fieldName = HDFLibrary.VFfieldname(srcdid, i);
            fieldType = HDFLibrary.VFfieldtype(srcdid, i);
            fieldOrder = HDFLibrary.VFfieldorder(srcdid, i);
            HDFLibrary.VSfdefine(dstdid, fieldName, fieldType, fieldOrder);

            nameList += fieldName;
            if (i<numberOfFields-1) nameList += ",";
        }
        HDFLibrary.VSsetfields(dstdid, nameList);
        HDFLibrary.VSsetinterlace(dstdid, HDFConstants.FULL_INTERLACE);

        int ref = HDFLibrary.VSfind(fid, dname);
        if (!pgroup.isRoot())
        {
            int vgid = pgroup.open();
            HDFLibrary.Vaddtagref(vgid, HDFConstants.DFTAG_VS, ref);
            pgroup.close(vgid);
        }

        // copy attributes
        int numberOfAttributes = 0;
        try {
            numberOfAttributes = HDFLibrary.VSfnattrs(srcdid, -1);
        } catch (Exception ex) { numberOfAttributes = 0; }


        String[] attrName = new String[1];
        byte[] attrBuff = null;
        int[] attrInfo = new int[3]; //data_type,  count, size
        for (int i=0; i< numberOfAttributes; i++)
        {
            try {
                attrName[0] = "";
                HDFLibrary.VSattrinfo(srcdid, -1, i, attrName, attrInfo);
                attrBuff = new byte[attrInfo[2]];
                HDFLibrary.VSgetattr(srcdid, -1, i, attrBuff);
                HDFLibrary.VSsetattr(dstdid, -1, attrName[0], attrInfo[0], attrInfo[2], attrBuff);
            } catch (Exception ex) { continue; }
        }

        // write vdata values once by the whole table
        byte[] buff = null;
        try {
            size = HDFLibrary.VSsizeof(srcdid, nameList)*numberOfRecords;
            buff = new byte[size];

            // read field data
            HDFLibrary.VSseek(srcdid, 0); //moves the access pointer to the start position
            HDFLibrary.VSsetfields(srcdid, nameList); //// Specify the fields to be accessed
            HDFLibrary.VSread(srcdid, buff, numberOfRecords, HDFConstants.FULL_INTERLACE);

            HDFLibrary.VSsetfields(dstdid, nameList); //// Specify the fields to be accessed
            HDFLibrary.VSwrite(dstdid, buff, numberOfRecords, HDFConstants.FULL_INTERLACE);
        } catch (Exception ex) { ; }

        long[] oid = {HDFConstants.DFTAG_VS, ref};
        dataset = new H4Vdata(this, dname, path, oid);

        pgroup.addToMemberList(dataset);

        vdata.close(srcdid);
        try { HDFLibrary.VSdetach(dstdid); }
        catch (Exception ex) { ; }

        return dataset;
    }

    private TreeNode copy(H4Group srcGroup, H4Group pgroup) throws Exception
    {
        H4Group group = null;
        int srcgid, dstgid;
        String gname=null, path=null;

        dstgid = HDFLibrary.Vattach(fid, -1, "w");
        if (dstgid < 0) return null;

        gname = srcGroup.getName();
        srcgid = srcGroup.open();

        HDFLibrary.Vsetname(dstgid, gname);
        int ref = HDFLibrary.VQueryref(dstgid);
        int tag = HDFLibrary.VQuerytag(dstgid);

        if (pgroup.isRoot())
        {
           path = HObject.separator;
        }
        else
        {
            // add the dataset to the parent group
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
            int pid = pgroup.open();
            HDFLibrary.Vinsert(pid, dstgid);
            pgroup.close(pid);
        }

        // copy attributes
        int numberOfAttributes = 0;
        try {
            numberOfAttributes = HDFLibrary.Vnattrs(srcgid);
        } catch (Exception ex) { numberOfAttributes = 0; }

        String[] attrName = new String[1];
        byte[] attrBuff = null;
        int[] attrInfo = new int[3]; //data_type,  count, size
        for (int i=0; i< numberOfAttributes; i++)
        {
            try {
                attrName[0] = "";
                HDFLibrary.Vattrinfo(srcgid, i, attrName, attrInfo);
                attrBuff = new byte[attrInfo[2]];
                HDFLibrary.Vgetattr(srcgid, i, attrBuff);
                HDFLibrary.Vsetattr(dstgid, attrName[0], attrInfo[0], attrInfo[2], attrBuff);
            } catch (Exception ex) { continue; }
        }

        long[] oid = {tag, ref};
        group = new H4Group(this, gname, path, pgroup, oid);

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(group)
        {
            public boolean isLeaf() { return false; }
        };
        pgroup.addToMemberList(group);

        // copy members of the source group to the new group
        List members = srcGroup.getMemberList();
        if (members != null && members.size()>0)
        {
            Iterator iterator = members.iterator();
            while (iterator.hasNext())
            {
                HObject mObj = (HObject)iterator.next();
                try {
                    newNode.add((MutableTreeNode)copy(mObj, group));
                } catch (Exception ex) {}
            }
        }

        srcGroup.close(srcgid);
        try { HDFLibrary.Vdetach(dstgid); }
        catch (Exception ex) { ; }

        return newNode;
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
            b = HDFLibrary.GRfileinfo(grid, argv);
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

        if (rootGroup != null)
        {
            // retrieve file annotation, GR and SDS globle attributes
            List attributeList = null;
            try { attributeList = rootGroup.getMetadata(); }
            catch (HDFException ex) {}

            if (attributeList != null)
            {
                try { getFileAnnotation(fid, attributeList); }
                catch (HDFException ex) {}
                try { getGRglobleAttribute(grid, attributeList); }
                catch (HDFException ex) {}
                try { getSDSglobleAttribute(sdid, attributeList); }
                catch (HDFException ex) {}
            }
        }

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
        int[] imgInfo = new int[4];
        int[] dim_sizes = {0, 0};
        int tag = HDFConstants.DFTAG_RIG;

        try
        {
            id = HDFLibrary.GRselect(grid, index);
            ref = HDFLibrary.GRidtoref(id);
            HDFLibrary.GRgetiminfo(id, objName, imgInfo, dim_sizes);
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
        int[] tmpInfo = new int[HDFConstants.MAX_VAR_DIMS];
        int[] sdInfo = {0, 0, 0};
        int tag = HDFConstants.DFTAG_NDG;

        boolean isCoordvar = false;
        try
        {
            id = HDFLibrary.SDselect(sdid, index);
            ref = HDFLibrary.SDidtoref(id);
            HDFLibrary.SDgetinfo(id, objName, tmpInfo, sdInfo);
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
            // do not display internal Vdata, "_HDF_CHK_TBL_"
            !vClass[0].startsWith(HDFConstants.HDF_CHK_TBL) &&
            // do not display internal vdata for CDF, "CDF0.0"
            !vClass[0].equalsIgnoreCase(HDFConstants.HDF_CDF))
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

    /**
     *  Reads HDF file annontation (file labels and descriptions) into memory.
     *  The file annotation is stroed as attribute of the root group.
     *  <p>
     *  @param fid the file identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    private List getFileAnnotation(int fid, List attrList)
    throws HDFException
    {
        if (fid < 0 )
            return attrList;

        int anid = HDFConstants.FAIL;
        try
        {
            anid = HDFLibrary.ANstart(fid);
            // fileInfo[0] = n_file_label, fileInfo[1] = n_file_desc,
            // fileInfo[2] = n_data_label, fileInfo[3] = n_data_desc
            int[] fileInfo = new int[4];
            HDFLibrary.ANfileinfo(anid, fileInfo);

            if (fileInfo[0]+fileInfo[1] <= 0)
            {
                try { HDFLibrary.ANend(anid); } catch (HDFException ex) {}
                return attrList;
            }

            if (attrList == null)
                attrList = new Vector(fileInfo[0]+fileInfo[1], 5);

            // load file labels and descriptions
            int id = -1;
            int[] annTypes = {HDFConstants.AN_FILE_LABEL, HDFConstants.AN_FILE_DESC};
            for (int j=0; j<2; j++)
            {
                String annName = null;
                if (j == 0)
                    annName = "File Label";
                else
                    annName = "File Description";

                for (int i=0; i < fileInfo[j]; i++)
                {
                    try {
                        id = HDFLibrary.ANselect(anid, i, annTypes[j]);
                    } catch (HDFException ex)
                    {
                        id = HDFConstants.FAIL;
                    }

                    if (id == HDFConstants.FAIL)
                    {
                        try { HDFLibrary.ANendaccess(id); } catch (HDFException ex) {}
                        continue;
                    }

                    int length = 0;
                    try {
                        length = HDFLibrary.ANannlen(id)+1;
                    } catch (HDFException ex)
                    {
                        length = 0;
                    }

                    if (length > 0)
                    {
                        boolean b = false;
                        String str[] = {""};
                        try { b = HDFLibrary.ANreadann(id, str, length);
                        } catch ( HDFException ex) { b = false; }

                        if (b && str[0].length()>0)
                        {
                            long attrDims[] = {str[0].length()};
                            Attribute newAttr = new Attribute(
                                annName +" #"+i,
                                HDFConstants.DFNT_CHAR,
                                attrDims);
                            attrList.add(newAttr);
                            newAttr.setValue(str[0]);
                        }
                    }

                    try { HDFLibrary.ANendaccess(id); } catch (HDFException ex) {}
                } // for (int i=0; i < fileInfo[annTYpe]; i++)
            } // for (int annType=0; annType<2; annType++)
        } finally
        {
            try { HDFLibrary.ANend(anid); } catch (HDFException ex) {}
        }

        return attrList;
    }

    /**
     *  Reads GR globle attributes into memory.
     *  The attributes sre stroed as attributes of the root group.
     *  <p>
     *  @param grid the GR identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    private List getGRglobleAttribute(int grid, List attrList)
    throws HDFException
    {
        if (grid == HDFConstants.FAIL)
            return attrList;

        int[] attrInfo = {0, 0};
        HDFLibrary.GRfileinfo(grid, attrInfo);
        int numberOfAttributes = attrInfo[1];

        if (numberOfAttributes>0)
        {
            if (attrList == null)
                attrList = new Vector(numberOfAttributes, 5);

            String[] attrName = new String[1];
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                boolean b = false;
                try {
                    b =  HDFLibrary.GRattrinfo(grid, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b)
                    continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attrList.add(attr);

                Object buf = H4Datatype.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.GRgetattr(grid, i, buf);
                } catch (HDFException ex)
                {
                    buf = null;
                }

                if (buf != null)
                {
                    if (attrInfo[0] == HDFConstants.DFNT_CHAR ||
                        attrInfo[0] ==  HDFConstants.DFNT_UCHAR8)
                    {
                        buf = Dataset.byteToString((byte[])buf, attrInfo[1]);
                    }

                    attr.setValue(buf);
                }

            } // for (int i=0; i<numberOfAttributes; i++)
        } // if (b && numberOfAttributes>0)

        return attrList;
    }

    /**
     *  Reads SDS globle attributes into memory.
     *  The attributes sre stroed as attributes of the root group.
     *  <p>
     *  @param sdid the SD identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    private List getSDSglobleAttribute(int sdid, List attrList)
    throws HDFException
    {
        if (sdid == HDFConstants.FAIL)
            return attrList;

        int[] attrInfo = {0, 0};
        HDFLibrary.SDfileinfo(sdid, attrInfo);

        int numberOfAttributes = attrInfo[1];
        if (numberOfAttributes>0)
        {
            if (attrList == null)
                attrList = new Vector(numberOfAttributes, 5);

            String[] attrName = new String[1];
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                boolean b = false;
                try {
                    b =  HDFLibrary.SDattrinfo(sdid, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b)
                    continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attrList.add(attr);

                Object buf = H4Datatype.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.SDreadattr(sdid, i, buf);
                } catch (HDFException ex)
                {
                    buf = null;
                }

                if (buf != null)
                {
                    if (attrInfo[0] == HDFConstants.DFNT_CHAR ||
                        attrInfo[0] ==  HDFConstants.DFNT_UCHAR8)
                    {
                        buf = Dataset.byteToString((byte[])buf, attrInfo[1]);
                    }

                    attr.setValue(buf);
                }

            } // for (int i=0; i<numberOfAttributes; i++)
        } // if (b && numberOfAttributes>0)

        return attrList;
    }

    /**
     *  Returns the version of the HDF4 library.
     */
    public static final String getLibversion()
    {
        int[] vers = new int[3];
        String ver = "NCSA HDF ";
        String[] verStr = {""};

        try { HDFLibrary.Hgetlibversion(vers, verStr); }
        catch (HDFException ex) {}

        ver += vers[0] + "." + vers[1] +"."+vers[2];

        return ver;
    }

}
