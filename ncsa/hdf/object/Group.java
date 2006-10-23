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

import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Group is an abstract class. Current implementing classes are the H4Group and
 * H5Group. This class includes general information of a group object such as
 * members of a group and common operations on groups.
 * <p>
 * Members of a group may include other groups, datasets or links.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class Group extends HObject
{
    /**
     * The list of members (in memory) of this group.
     */
    private List memberList;

    /** The parent group where this group is loacted. */
    protected Group parent;

    /**
     * Total number of members in this group.
     */
    private int nMembersInFile;

    /**
     * Creates a group object with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     */
    public Group(FileFormat fileFormat, String name, String path, Group parent)
    {
        this(fileFormat, name, path, parent, null);
    }

    /**
     * Creates a group object with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public Group(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        this.parent = parent;
    }

    /**
     * Adds an HObject to the member list of this group.
     * <p>
     * @param object the HObject to be added to the member list.
     */
    public void addToMemberList(HObject object)
    {
        if (memberList == null)
        {
            int size = Math.min(getNumberOfMembersInFile(), this.getFileFormat().getMaxMembers());
            memberList = new Vector(size+5);
        }

        if (object != null)
            memberList.add(object);
    }

    /**
     * Removes an HObject from the member list of this group.
     * <p>
     * @param object the HObject to be removed from the member list.
     */
    public void removeFromMemberList(HObject object)
    {
        if (memberList != null)
        {
            memberList.remove(object);
        }
    }

    /**
     * Returns the list of members of this group.
     */
    public List getMemberList()
    {
        FileFormat theFile = this.getFileFormat();
        String thePath = this.getPath();
        String theName = this.getName();

        if (memberList == null && theFile != null)
        {
            memberList = new Vector(); // avoid infinite loop search for groups without member

            // find the memberList from the file by check the group path and name
            // group may be created out of the structure tree (H4/5File.loadTree()).
            try { theFile.open(); } // load the file structure;
            catch (Exception ex) {;}

            DefaultMutableTreeNode root = (DefaultMutableTreeNode)theFile.getRootNode();

            if (root == null)
                return memberList;

            Enumeration emu = root.depthFirstEnumeration();

            Group g = null;
            Object uObj = null;
            while(emu.hasMoreElements())
            {
                uObj = (HObject)((DefaultMutableTreeNode)emu.nextElement()).getUserObject();
                if (uObj instanceof Group)
                {
                    g = (Group)uObj;
                    if( g.getPath() != null ) // add this check to get rid of null exception
                    {
                      if ( ( this.isRoot() && g.isRoot() ) ||
                           ( thePath.equals(g.getPath()) && g.getName().endsWith( theName ) ) )
                      {
                        memberList = g.getMemberList();
                        break;
                      }
                    }
                }
            }
        }

        return memberList;
    }

    /** Returns the parent group */
    public final Group getParent() { return parent; }

    /**
     * Checks if it is a root group.
     */
    public final boolean isRoot()
    {
        return (parent==null);
    }

    /**
     *
     * @return total number of members of this group in file.
     */
    public int getNumberOfMembersInFile() { return nMembersInFile; }

    /**
     * sets total number of members of this group in file.
     * @param n total number of members of this group in file.
     */
    public void setNumberOfMembersInFile(int n) { nMembersInFile = n; }
}
