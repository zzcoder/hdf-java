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

package ncsa.hdf.object;

import java.util.*;

/**
 * Group is the superclass for HDF4/5 group, inheriting the HObject.
 * <p>
 * Group is an abstract class. Its implementing sub-classes are the H4Group and
 * H5Group. This class includes general information of a group object such as
 * members of a group and common operation on groups for both HDF4 and HDF5.
 * <p>
 * Members of a group may include other groups, datasets or links.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class Group extends HObject
{
    /**
     * The parent of this group.
     */
    private final Group parent;

    /**
     * The list of members of this group.
     */
    private List memberList;

    /**
     * Creates a group object with specific name, path, and parent.
     * <p>
     * @param fid the file identifier.
     * @param filename the full path of the file that contains this data object.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public Group(
        int fid,
        String filename,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fid, filename, name, path, oid);

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
            memberList = new Vector(5, 5);

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
        return memberList;
    }

    /**
     * Checks if the group is a root group.
     */
    public final boolean isRoot()
    {
        return (parent==null);
    }
}