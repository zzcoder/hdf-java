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

/**
 * CompoundDS is the superclass for HDF4 and HDF5 Compound Dataset.
 * <p>
 * A compound datatype is a collection of one or more atomic types or small
 * arrays of such types. Each member of a compound type has a name which is
 * unique within that type, and a datatype of that member in a compound datum
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class CompoundDS extends Dataset
{
    /**
     * The number of members of this compound dataset.
     */
    protected int numberOfMembers;

    /**
     * The names of members of this compound dataset.
     */
    protected String[] memberNames;

    /**
     * The data types of the members of this compound dataset.
     */
    protected int[] memberTypes;

    /**
     * The number of data points of each member of this compound dataset.
     * A member can be an array of atomic or compound data. The member order
     * is the total size of the array. For scalar data, the member order is one.
     */
    protected int[] memberOrders;

    /**
     * The array to store flags to indicate if a member is selected. If a member
     * is selected, the read/wirte will perform on the member. Applications such
     * as HDFView will only display the selected members of the compound dataset.
     * <pre>
     * For example, if a compound dataset has four members
     *     String[] memberNames = {"X", "Y", "Z", "TIME"};
     * and
     *     boolean[] isMemberSelected = {true, false, false, true};
     * only members "X" and "TIME" are selected for read and write.
     * </pre>
     */
    protected boolean[] isMemberSelected;

    /**
     * Constructs a CompoundDS object with given file and dataset name and path.
     * This object is usually constructed at FileFormat.open(), which loads the
     * file structure and object informatoin into tree structure (TreeNode). It
     * is rarely used elsewhere.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this CompoundDS.
     * @param path the full path of this CompoundDS.
     * @param oid the unique identifier of this data object.
     *    HDF4 objects are uniquely identified by the (tag_id, ref_id) pairs,
     *    i.e. oid[0]=tag, oid[1]=ref.<br>
     *    HDF5 objects uniquely identified by the reference identifier,
     *    i.e. oid[0]=object_reference<br>
     */
    public CompoundDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        numberOfMembers = 0;
        memberNames = null;
        memberTypes = null;
        isMemberSelected = null;
    }

    /**
     * Returns the number of members of this compound dataset.
     */
    public final int getMemberCount()
    {
        return numberOfMembers;
    }

    /**
     * Returns the number of selected members of this compound dataset.
     */
    public final int getSelectedMemberCount()
    {
        int count = 0;

        if (isMemberSelected == null)
        {
            count = 0;
        }
        else
        {
            for (int i=0; i<isMemberSelected.length; i++)
            {
                if (isMemberSelected[i])
                    count++;
            }
        }

        return count;
    }

    /**
     * Returns the names of the members of the datasets
     */
    public final String[] getMemberNames()
    {
        return memberNames;
    }

    /**
     * Returns the data types of the members of this compound dataset.
     * Each member of a compound dataset has its datatype. The datatype of a
     * member can be atomic or other compound datatype.
     */
    public final int[] getMemberTypes()
    {
        return memberTypes;
    }

    /**
     * Returns true if the i-th memeber is selected.
     */
    public final boolean isMemberSelected(int i)
    {
        if (isMemberSelected != null && isMemberSelected.length>i)
            return isMemberSelected[i];
        else
            return false;
    }

    /**
     * select the i-th member.
     */
    public final void selectMember(int i)
    {
        if (isMemberSelected != null && isMemberSelected.length>i)
            isMemberSelected[i] = true;
    }

    /**
     * select/deselect all members.
     * @param isSelected The indicator to select or deselect all members.
     *     isSelected=true to select all members. isSelected=false to
     *     deselect all members.
     */
    public final void setMemberSelection(boolean isSelected)
    {
        if (isMemberSelected == null)
            return;

        for (int i=0; i<isMemberSelected.length; i++)
            isMemberSelected[i] = isSelected;
    }

    /**
     * Returns the data orders (total array size) of all the members
     * of this compound dataset.
     */
    public int[] getMemberOrders()
    {
        return memberOrders;
    }

    /**
     * Returns the data orders (total array size) of the selected members
     * of this compound dataset.
     */
    public int[] getSelectedMemberOrders()
    {
        if (isMemberSelected == null)
            return memberOrders;

        int idx = 0;
        int[] order = new int[getSelectedMemberCount()];
        for (int i=0; i<isMemberSelected.length; i++)
        {
            if (isMemberSelected[i])
                order[idx++] = memberOrders[i];
        }

        return order;
    }

}
