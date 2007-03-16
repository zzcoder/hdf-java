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

/**
 * A CompoundDS is a dataset with compound datatype.
 * <p>
 * A compound datatype is an aggregation of one or more datatypes. Each member 
 * of a compound type has a name which is unique within that type, and a datatype 
 * of that member in a compound datum. Compound datatype can be nested, i.e. members 
 * of compound datatype can be some other compound datatype.
 * <p>
 * For more details on compound datatype, see <b>
 * {@link <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/UG/index.html">HDF5 User's Guide</a>}
 * <p>
 * Since Java cannot handle C-structured compound data, data in compound dataset
 * is loaded in to an Java List. Each element of the list is a data array that corresponds 
 * to a compound field. The data is read/written by compound field.
 * <p>
 * For example, if compound dataset "comp" has the following nested structure,
 * and memeber datatypes
 * <pre>
 * comp --> m01 (int)
 * comp --> m02 (float)
 * comp --> nest1 --> m11 (char)
 * comp --> nest1 --> m12 (String)
 * comp --> nest1 --> nest2 --> m21 (long)
 * comp --> nest1 --> nest2 --> m22 (double)
 * </pre>
 * The data object is an Java list of six arrays: {int[], float[], char[], Stirng[], long[] and double[]}.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class CompoundDS extends Dataset
{
    /** 
     * A single character to separate the names of nested compound fields. 
     * An extended ASCII character, 0x95, is used to avoid common characters in compound names. 
     */
    public static final String separator = "\u0095"; 
    
    /**
     * The number of members of the compound dataset.
     */
    protected int numberOfMembers;

    /**
     * The names of members of the compound dataset.
     */
    protected String[] memberNames;

    /**
     * The data types of the members of the compound dataset.
     */
    protected int[] memberTypes;

    /**
     * Returns array containing the total number of elements of the members of compound.
     * <p>
     * For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * memberOrders is an integer array of {1, 5, 6} to indicate that 
     * member A has one element, member B has 5 elements, and member C has 6 elements.
     */
    protected int[] memberOrders;

    /**
     * The dimension sizes of each member.
     */
    protected Object[] memberDims;

    /**
     * The array to store flags to indicate if a member of compound dataset is selected for read/write.
     * <p>
     * If a member is selected, the read/write will perform on the member. Applications such
     * as HDFView will only display the selected members of the compound dataset.
     * <pre>
     * For example, if a compound dataset has four members
     *     String[] memberNames = {"X", "Y", "Z", "TIME"};
     * and
     *     boolean[] isMemberSelected = {true, false, false, true};
     * members "X" and "TIME" are selected for read and write.
     * </pre>
     */
    protected boolean[] isMemberSelected;

    /**
     * Constructs a CompoundDS object with given file, dataset name and path.
     * 
     * This object is usually constructed at FileFormat.open(), which loads the
     * file structure and object informatoin into tree structure (TreeNode). It
     * is rarely used elsewhere.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of this CompoundDS, e.g. "compDS".
     * @param path the path of this CompoundDS, e.g. "/g1".
     */
    public CompoundDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * @deprecated  Not for public use in the future.
     * Using {@link #CompoundDS(FileFormat, String, String)}
     * <p>
     * Constructs a CompoundDS object with given file and dataset name and path.
     * 
     * This object is usually constructed at FileFormat.open(), which loads the
     * file structure and object informatoin into tree structure (TreeNode). It
     * is rarely used elsewhere.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of this CompoundDS, e.g. "compDS".
     * @param path the path of this CompoundDS, e.g. "/g1".
     * @param oid the unique identifier of this dataset object.
     *    HDF4 objects are uniquely identified by the (tag_id, ref_id) pairs,
     *    i.e. oid[0]=tag, oid[1]=ref.<br>
     *    HDF5 objects are uniquely identified by the reference identifier.
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
     * Returns the number of members of the compound dataset.
     * 
     * @return the number of members of the compound dataset.
     */
    public final int getMemberCount()
    {
        return numberOfMembers;
    }

    /**
     * Returns the number of selected members of the compound dataset.
     * 
     * Selected members are the compound fields which are selected for read/write.
     * <p>
     * For example, in a compound datatype of {int A, float B, char[] C},
     * users can choose to retrieve only {A, C} from dataset. In this case, 
     * getSelectedMemberCount() returns two. 
     * 
     * @return the number of selected members.
     */
    public final int getSelectedMemberCount()
    {
        int count = 0;

        if (isMemberSelected != null)
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
     * Returns the names of the members of the compound dataset.
     * The names of compound members are stored in an array of Strings.
     * <p>
     * For example, for a compound datatype of {int A, float B, char[] C}
     * getMemberNames() returns ["A", "B", "C"}.
     * 
     * @return the names of compound members. 
     */
    public final String[] getMemberNames()
    {
        return memberNames;
    }

    /**
     * Returns the array of datatype identifiers of the compound members.
     * <p>
     * Each member of a compound dataset has its own datatype. The datatype of a
     * member can be atomic or other compound datatype (nested compound). Sub-classes
     * set up the datatype identifiers at init(). For example, at H5CompoundDS.init()
     * H5.H5Tget_member_type(tid, i) is called to obtained the datatype identifiers
     * of members of compound.
     * <p>
     * <strong>Applications should never change the values of the array (member datatype identifiers). </strong>
     * 
     * @return the array of datatype identifiers of the compound members.
     */
    public final int[] getMemberTypes()
    {
        return memberTypes;
    }

    /**
     * Checks if a member of compound is selected for read/write.
     * 
     * @param idx the index of compound member.
     * 
     * @return true if the i-th memeber is selected; otherwise returns false.
     */
    public final boolean isMemberSelected(int idx)
    {
        if (isMemberSelected != null && isMemberSelected.length>idx)
            return isMemberSelected[idx];
        else
            return false;
    }

    /**
     * Selects the i-th member for read/write.
     * 
     * @param idx the index of compound member.
     */
    public final void selectMember(int idx)
    {
        if (isMemberSelected != null && isMemberSelected.length>idx)
            isMemberSelected[idx] = true;
    }

    /**
     * Selects/deselects all members.
     * 
     * @param isSelected The indicator to select or deselect all members.
     *     If true, all members are selected for read/write. 
     *     If false, no member is selected for read/write.
     */
    public final void setMemberSelection(boolean isSelected)
    {
        if (isMemberSelected == null)
            return;

        for (int i=0; i<isMemberSelected.length; i++)
            isMemberSelected[i] = isSelected;
    }

    /**
     * Returns array containing the total number of elements of the members of compound.
     * <p>
     * For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * getMemberOrders() will return an integer array of {1, 5, 6} to indicate that 
     * member A has one element, member B has 5 elements, and member C has 6 elements.
     * 
     * @return the array containing the total number of elements of the members of compound.
     */
    public final int[] getMemberOrders()
    {
        return memberOrders;
    }

    /**
     * Returns array containing the total number of elements of the elected members of compound.
     * 
     * <p>
     * For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * If A and B are selected, getSelectedMemberOrders() returns an array of {1, 5}
     * 
     * @return array containing the total number of elements of the selected members of compound.
    */
    public final int[] getSelectedMemberOrders()
    {
        if (isMemberSelected == null)
            return memberOrders;

        int idx = 0;
        int[] orders = new int[getSelectedMemberCount()];
        for (int i=0; i<isMemberSelected.length; i++)
        {
            if (isMemberSelected[i])
                orders[idx++] = memberOrders[i];
        }

        return orders;
    }

    /** 
     * Returns the datatype identifiers of the selected members.
     *  
     * <strong>Applications should never change the values of member datatype identifiers. </strong>
     * 
     * @return the datatype identifiers of the selected members.
     */
    public final int[] getSelectedMemberTypes()
    {
        if (isMemberSelected == null)
            return memberTypes;

        int idx = 0;
        int[] types = new int[getSelectedMemberCount()];
        for (int i=0; i<isMemberSelected.length; i++)
        {
            if (isMemberSelected[i])
                types[idx++] = memberTypes[i];
        }

        return types;
    }

    /**
     * Returns the dimension sizes of of the i-th member.
     * <p>
     * For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * getMemeberDims(2) returns an array of {2, 3}, while getMemeberDims(1)
     * returns an array of {5}.
     * 
     * @return the dimension sizes of of the i-th member.
     */
    public final int[] getMemeberDims(int i) {
        if (memberDims == null)
            return null;
        return (int[])memberDims[i];
    }

    /**
     * @deprecated  Not implemented for compound dataset.
     * 
     * <p>
     * Copy a selected part of a compound dataset to a new compound dataset.
     * <p>
     * <b>NOTE: This function is not implemented for compound dataset. 
     *          It may be added in future</b>
     * <p>
     * However, copying the whole dataset is implemented by all sub-classes.
     * For example, to copy a H5CompoundDS, one can use
     * H5File.copy(HObject srcObj, Group dstGroup, String dstName)
     *
     * @param pgroup the group which the dataset is copied to.
     * @param name the name of the new dataset.
     * @param dims the dimension sizes of the the new dataset.
     * @param data the data values of the subset to be copied.
     * @return the new dataset if sucessful; otherwise returns null.
     */
    public Dataset copy(Group pgroup, String name, long[] dims, Object data) throws Exception
    {
        throw new UnsupportedOperationException(
            "Writing a subset of a compound dataset to a new dataset is not implemented.");
    }

}
