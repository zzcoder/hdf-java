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
 * A CompoundDS is dataset with compound datatypes.
 * <p>
 * A compound datatype is a collection of one or more atomic types or small
 * arrays of such types. Each member of a compound type has a name which is
 * unique within that type, and a datatype of that member in a compound datum.
 * Compound datatype can be nested, i.e. members of compound datatype can be
 * some other compound datatype.
 * <p>
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for read, write and subet a dataset. No function is defined
 * to select a subset of a data array. The selection is done in an implicit way.
 * Function calls to dimension information such as getSelectedDims() return an array
 * of dimension values, which is a reference to the array in the dataset object.
 * Changes of the array outside the dataset object directly change the values of
 * the array in the dataset object. It is like pointers in C.
 * <p>
 *
 * The following is an example of how to make a subset. In the example, the dataset
 * is a 4-dimension with size of [200][100][50][10], i.e.
 * dims[0]=200; dims[1]=100; dims[2]=50; dims[3]=10; <br>
 * We want to select every other data points in dims[1] and dims[2]
 * <pre>
     int rank = dataset.getRank();   // number of dimension of the dataset
     long[] dims = dataset.getDims(); // the dimension sizes of the dataset
     long[] selected = dataset.getSelectedDims(); // the selected size of the dataet
     long[] start = dataset.getStartDims(); // the off set of the selection
     long[] stride = dataset.getStride(); // the stride of the dataset
     int[]  selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display

     // select dim1 and dim2 as 2D data for display,and slice through dim0
     selectedIndex[0] = 1;
     selectedIndex[1] = 2;
     selectedIndex[1] = 0;

     // reset the selection arrays
     for (int i=0; i<rank; i++) {
         start[i] = 0;
         selected[i] = 1;
         stride[i] = 1;
    }

    // set stride to 2 on dim1 and dim2 so that every other data points are selected.
    stride[1] = 2;
    stride[2] = 2;

    // set the selection size of dim1 and dim2
    selected[1] = dims[1]/stride[1];
    selected[2] = dims[1]/stride[2];

    // when dataset.read() is called, the slection above will be used since
    // the dimension arrays is passed by reference. Changes of these arrays
    // outside the dataset object directly change the values of these array
    // in the dataset object.

 * </pre>
 *
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class CompoundDS extends Dataset
{
    /** A single character to separate the names of nested compound fields. 
     * Use EXTENDED ASCII CHARACTER 0x95 */
    public static final String separator = "\u0095"; 
    
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
     * is the total number of points of the array. For scalar data, the member order is one.
     */
    protected int[] memberOrders;

    /**
     * The dimension sizes of each memeber.
     */
    protected Object[] memberDims;

    /**
     * The array to store flags to indicate if a member is selected for read/write.
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
     * This object is usually constructed at FileFormat.open(), which loads the
     * file structure and object informatoin into tree structure (TreeNode). It
     * is rarely used elsewhere.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of this CompoundDS, e.g. "compDS".
     * @param path the full path of this CompoundDS, e.g. "/g1".
     */
    public CompoundDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constructs a CompoundDS object with given file and dataset name and path.
     * This object is usually constructed at FileFormat.open(), which loads the
     * file structure and object informatoin into tree structure (TreeNode). It
     * is rarely used elsewhere.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of this CompoundDS, e.g. "compDS".
     * @param path the full path of this CompoundDS, e.g. "/g1".
     * @param oid the unique identifier of this dataset object.
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
     * Selelcted members are the compound fields which are selected to read
     * or write.  
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
     * Returns the names of the members of the compound dataset
     */
    public final String[] getMemberNames()
    {
        return memberNames;
    }

    /**
     * Returns the datatype identifiers of the compound members.
     * Each member of a compound dataset has its datatype. The datatype of a
     * member can be atomic or other compound datatype.
     */
    public final int[] getMemberTypes()
    {
        return memberTypes;
    }

    /**
     * Returns true if the i-th memeber is selected; otherwise returns false.
     */
    public final boolean isMemberSelected(int i)
    {
        if (isMemberSelected != null && isMemberSelected.length>i)
            return isMemberSelected[i];
        else
            return false;
    }

    /**
     * Selects the i-th member for read/write.
     */
    public final void selectMember(int i)
    {
        if (isMemberSelected != null && isMemberSelected.length>i)
            isMemberSelected[i] = true;
    }

    /**
     * Selects/deselects all members.
     * @param isSelected The indicator to select or deselect all members.
     *     If isSelected is true, all members are selected for read/write. 
     *     If isSelected is false, no member is selected for read/write.
     */
    public final void setMemberSelection(boolean isSelected)
    {
        if (isMemberSelected == null)
            return;

        for (int i=0; i<isMemberSelected.length; i++)
            isMemberSelected[i] = isSelected;
    }

    /**
     * Returns the dimension sizes of the compound members.
     * <p>For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * getMemberOrders() will return an integer array of {1, 5, 6} to indicate that 
     * member A has one element, member B had 5 elements and member C has 6 elements.
     */
    public final int[] getMemberOrders()
    {
        return memberOrders;
    }

    /**
     * Returns the dimension sizes of the selected members of this compound dataset.
     * <p>For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * If A and B are selected, getSelectedMemberOrders() returns an array of {1, 5}
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

    /** Returns the datatype IDs of the selected members */
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
     * <p>For example, a compound dataset COMP has members of A, B and C as
     * <pre>
     *     COMP {
     *         int A;
     *         float B[5];
     *         double C[2][3];
     *     }
     * </pre>
     * getMemeberDims(2) returns an array of {2, 3}
     */
    public final int[] getMemeberDims(int i) {
        if (memberDims == null)
            return null;
        return (int[])memberDims[i];
    }

    /**
     * Copy a subset of this dataset to a new dataset.
     * <p>
     * <b>NOTE: This function is not implemented for compound dataset. It
     * may be added in future implementation</b>
     * <p>
     * However, copying the whole dataset is implemented by its sub-classes.
     * For example, to copy a H5CompoundDS, one can use
     * H5File.copy(HObject srcObj, Group dstGroup, String dstName)
     *
     * @param pgroup the group which the dataset is copied to.
     * @param name the name of the new dataset.
     * @param dims the dimension sizes of the the new dataset.
     * @param data the data values of the subset to be copied.
     * @return the new dataset.
     */
    public Dataset copy(Group pgroup, String name, long[] dims, Object data) throws Exception
    {
        throw new UnsupportedOperationException(
            "Writing a subset of a compound dataset to a new dataset is not implemented.");
    }

}
