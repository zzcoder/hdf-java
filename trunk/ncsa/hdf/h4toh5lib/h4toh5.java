/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-h4toh5/COPYING file.                                                        *
 *                                                                          *
 ****************************************************************************/


/**
 *  This is a class that wraps the HDF4 to HDF5 library.
 *  <p>
 */
package ncsa.hdf.h4toh5lib;

import java.io.*;
import ncsa.hdf.h4toh5lib.exceptions.*;


public class h4toh5 {
	public final static String H45PATH_PROPERTY_KEY = "ncsa.hdf.h4toh5lib.h4toh5.h45lib"; 

	public final static String JH4to5vers = "1.0beta";

    static
    {
	
        String filename = null;
        filename = System.getProperty(H45PATH_PROPERTY_KEY,null);
        if ((filename != null) && (filename.length() > 0))
        {
            File h45dll = new File(filename);
            if (h45dll.exists() && h45dll.canRead() && h45dll.isFile()) {
                System.load(filename);
            } else {
                throw (new UnsatisfiedLinkError("Invalid HDF45 library, "+filename));
            }
        }
        else {
            System.loadLibrary("jh4toh5");
        }
    }

public static String JH4toh5version() { return JH4to5vers; };

/**
  *   <p>Get the version of the java-h4toh5 library.
  *
  *   @returns String with the version of the java h4toh5 library.
  *
  */
public static native String H4toh5version();

/**
  *   Initialize the h4 to h5 library. The return value is the h4toh5id
  *   that must be passed to subsequent library calls.
  *
  *   @param hdf4file (IN) The HDF4 file (input). The files is open to read.
  *   @param hdf5file (IN) The HDF5 file (output).  If the file does not
  *   exist, it will be created.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @return The <i>h45id</i>, used in subsequent calls.
  */
public static native int H4toh5open(String hdf4file,String hdf5file) 
	throws H45Exception;

/**
  *   Terminate access and free resources of the h4 to h5 library for the
  *   session identified by <i>h45id</i>. 
  *
  *   <p>the program should close all HDDF4 and HDF5 before calling this.
  *
  *   @param h45id (IN) The h45id returned from H4toh5open.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  */
public static native void H4toh5close(int h45id) throws H45Exception;

/**
  *   Convert a vgroup from HDF4 to a group in HDF5, optionally converting
  *   members of the vgroup that are not vgroups.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vgroup ID, returned by HDF4 Vattach.
  *   @param parent (IN) The HDF5 group in which the converted group should be 
  *          inserted.
  *   @param group (IN) The name of the HDF5 group to create.
  *   @param vg_flag (IN) Flag to indicate whether to convert the members
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all elements of the Vgroup that are not Vgroups
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vgroup
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the Vgroup into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5advgroup
  */
public static native void H4toh5basvgroup(int h45id,int vgid,String parent,
  String group,int vg_flag,int attr_flag) throws H45Exception;

/**
  *   Convert a vgroup from HDF4 to a group in HDF5, optionally converting
  *   members of the vgroup that are not vgroups.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vgroup ID, returned by HDF4 Vattach.
  *   @param parent (IN) The HDF5 group in which the converted group should be 
  *          inserted.
  *   @param group (IN) The name of the HDF5 group to create.
  *   @param vg_flag (IN) Flag to indicate whether to convert the members
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all elements of the Vgroup that are not Vgroups
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vgroup
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the Vgroup into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5advgroup
  */
public static void H4toh5basvgroup(int h45id,int vgid,String parent,
  String group,boolean vg_flag,boolean attr_flag) throws H45Exception {
	int vf = 0;
	int af = 0;
	if (vg_flag) vf = 1;
	if (attr_flag) af = 1;

	H4toh5basvgroup(h45id,vgid,parent, group,vf,af);
}

/**
  *   Convert a vgroup from HDF4 to a group in HDF5, recursively converting
  *   all the members of the vgroup including other vgroups.  
  *   <p>
  *   <b>Caution:</b> This function should be used carefully, especially
  *    when the structure of the HDF4 Vgroup may be complicated.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vgroup ID, returned by HDF4 Vattach.
  *   @param parent (IN) The HDF5 group in which the converted grup should be 
  *          inserted.
  *   @param group (IN) The name of the HDF5 group to create.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5basvgroup
  */
public static native void H4toh5advgroup(int h45id,int vgid,String parent,String group) throws H45Exception;

/**
  *   Convert a vdata from HDF4 to a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param name (IN) The name of the HDF5 dataset to create.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vdata
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5vdata(int h45id,int vgid,String parent,String name,int attr_flag) throws H45Exception;

/**
  *   Convert a vdata from HDF4 to a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param name (IN) The name of the HDF5 dataset to create.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vdata
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static void H4toh5vdata(int h45id,int vgid,String parent,
	String name,boolean attr_flag) throws H45Exception
{
	int af = 0;
	if (attr_flag) af = 1;
	H4toh5vdata(h45id,vgid,parent, name,af);
}

/**
  *   Convert a SDS from HDF4 to a dataset in HDF5, with appropriate data
  *   type.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdsid (IN) The SD ID, returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dataset 
  *  should be inserted.
  *   @param dsetname (IN) The name of the HDF5 dataset to create.
  *   @param dim_flag (IN) Flag to indicate whether to convert the dimension
  *    scales into HDF5 datasets.
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all dimension scales of the SDS
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the SDS
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the SDS into equivalent 
  *     attributes of the HDF5 dataset.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5sds(int h45id,int sdsid,String parent,
	String dsetname,String dimgroupname,int dim_flag,int attr_flag) 
	throws H45Exception;

/**
  *   Convert a SDS from HDF4 to a dataset in HDF5, with appropriate data
  *   type.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdsid (IN) The SD ID, returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dataset should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 dataset to create.
  *   @param dim_flag (IN) Flag to indicate whether to convert the dimension
  *    scales into HDF5 datasets.
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all dimension scales of the SDS
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the SDS
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the SDS into equivalent 
  *     attributes of the HDF5 dataset.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5advgroup
  */
public static void H4toh5sds(int h45id,int sdsid,String parent,
	String dsetname,String dimgroupname,boolean dim_flag,boolean attr_flag) 
	throws H45Exception
{
	int df = 0;
	int af = 0;
	if (dim_flag) df = 1;
	if (attr_flag) af = 1;
	H4toh5sds(h45id,sdsid,parent, dsetname,dimgroupname,df,af);
}

/**
  *   Convert a raster image from HDF4 to an image dataset in HDF5, 
  *   optionally converting associated palettes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param grid (IN) The GR image ID, returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the converted image should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 image dataset to create.
  *   @param palpath (IN) The absolute path of the HDF5 group to store 
  *   the palette dataset.
  *   @param palname (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the image
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param pal_flag (IN) Flag to indicate whether to convert the palette
  *    of the image
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert the palette as well as the image
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5pal
  */
public static native void H4toh5image(int h45id,int riid,String parent,
  String dsetname,String palpath,String palname,int attr_flag,int pal_flag) 
	throws H45Exception; 

/**
  *   Convert a raster image from HDF4 to an image dataset in HDF5, 
  *   optionally converting associated palettes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param riid (IN) The GR image ID, returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the converted image should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 image dataset to create.
  *   @param palpath (IN) The absolute path of the HDF5 group to store 
  *   the palette dataset.
  *   @param palname (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the image
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param pal_flag (IN) Flag to indicate whether to convert the palette
  *    of the image
  *    <ul>
  *	<li>	<b>false</b> = don't convert the palette
  *	<li>	<b>true</b> = convert the palette as well as the image
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5pal
  */
public static void H4toh5image(int h45id,int riid,String parent,
  String dsetname,String palpath,String palname,boolean attr_flag,
	boolean pal_flag) 
	throws H45Exception
{
	int af = 0;
	int pf = 0;
	if (attr_flag) af = 1;
	if (pal_flag) pf = 1;
	H4toh5image(h45id,riid,parent, dsetname,palpath,palname,af, pf);
}

/**
  *   Convert a palette from HDF4 to an palette dataset in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param grid (IN) The GR image ID of an image that refers to the palette,
  *   returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the converted palette should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 image dataset to attach the 
  *   palette (if none, <b>null</b>).
  *   @param palgroup (IN) The absolute path of the HDF5 group to store 
  *   the palette dataset.
  *   @param palname (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the palette
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    to an image
  *    <ul>
  *	<li>	0 = don't attach
  *	<li>	1 = attach to 'dsetname'.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5image
  */
public static native void H4toh5pal(int h45id,int riid,String parent,
  String dsetname,String palgroup,String paldset,int attr_flag,int ref_flag) throws H45Exception;

/**
  *   Convert a palette from HDF4 to an palette dataset in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param riid (IN) The GR image ID of an image that refers to the palette,
  *   returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the converted palette should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 image dataset to attach the 
  *   palette (if none, <b>null</b>).
  *   @param palgroup (IN) The absolute path of the HDF5 group to store 
  *   the palette dataset.
  *   @param palname (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the palette
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    to an image
  *    <ul>
  *	<li>	0 = don't attach
  *	<li>	1 = attach to 'dsetname'.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5image
  */
public static void H4toh5pal(int h45id,int riid,String parent,
  String dsetname,String palgroup,String paldset,boolean attr_flag,
	boolean ref_flag) 
	throws H45Exception 
{
	int af = 0;
	int rf = 0;
	if (attr_flag) af = 1;
	if (ref_flag) rf = 1;
	H4toh5pal(h45id,riid,parent, dsetname,palgroup,paldset,af, rf); 
}

/**
  *   Convert a file annotation to a String attribute of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param labelname (IN) The name of the anotation.
  *   @param label_ind (IN) The index of the annotation.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5annofile_alllabels
  */
public static native void H4toh5annofil_label(int h45id,String labelname,int label_ind) throws H45Exception;

/**
  *   Convert a file description to a String attribute of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param descname (IN) The name of the description.
  *   @param desc_ind (IN) The index of the description.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5annofile_alldescs
  */
public static native void H4toh5annofil_desc(int h45id,String descname,int desc_ind) throws H45Exception;

/**
  *   Convert all file annotations to String attributes of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5annofile_label
  */
public static native void H4toh5annofil_alllabels(int h45id) throws H45Exception;

/**
  *   Convert all file descriptiosn to String attributes of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5annofile_desc
  */
public static native void H4toh5annofil_alldescs(int h45id) throws H45Exception;

/**
  *   Convert a object label annotation from HDF4 to a attribute of
  *   an object in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param parent (IN) The HDF5 group in which the HDF5 object is found.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   annotdaion
  *   @param ref (IN) The HDF4 ref for the object the label is attached to,
  *   @param tag (IN) The HDF4 tag for the object the label is attached to,
  *   @param label (IN) The name of the annotation to convert.
  *   @param label_ind (IN) The index of the label.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5obj_alllabels
  */
public static native void H4toh5annoobj_label(int h45id,String parent,
  String datasetname,int ref, int tag,String label,int label_ind) throws H45Exception;

/**
  *   Convert a object description annotation from HDF4 to a attribute of
  *   an object in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param parent (IN) The HDF5 group in which the HDF5 object is found.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   annotation
  *   @param ref (IN) The HDF4 ref for the object the description is attached to,
  *   @param tag (IN) The HDF4 tag for the object the description is attached to,
  *   @param desc (IN) The name of the annotation to convert.
  *   @param desc_ind (IN) The index of the description.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5obj_alldescs
  */
public static native void H4toh5annoobj_desc(int h45id,String parent,
  String datasetname,int ref, int tag,String desc,int desc_ind) throws H45Exception;

/**
  *   Convert all the object label annotations from an HDF4 object
  *   to attributes of an object in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param parent (IN) The HDF5 group in which the HDF5 object is found.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   annotations
  *   @param ref (IN) The HDF4 ref for the object the labels are attached to,
  *   @param tag (IN) The HDF4 tag for the object the labesl are attached to,
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5obj_desc
  */
public static native int H4toh5annoobj_alllabels(int h45id,String parent,
  String datasetname,int ref, int tag) throws H45Exception;

/**
  *   Convert all the object description annotation from an HDF4 object
  *   to attributes of an object in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param parent (IN) The HDF5 group in which the HDF5 object is found.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   annotations
  *   @param ref (IN) The HDF4 ref for the object the descriptions are attached to,
  *   @param tag (IN) The HDF4 tag for the object the descriptions are attached to,
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5obj_desc
  */
public static native void H4toh5annoobj_alldescs(int h45id,String parent,
  String datasetname,int ref, int tag) throws H45Exception;

/**
  *   Convert all the dimension scales from an HDF4 SDS to a HDF5 dimension
  *   datasets, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdid (IN) An SDS ID,  returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group of the dataset to attach.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   dimensions (if none, <b>null</b>).
  *   @param dimgroup (IN) The absolute path of the HDF5 group to store 
  *   the dimension datasets.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the dimension
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the dimension
  *    to the dataset
  *    <ul>
  *	<li>	0 = don't attach
  *	<li>	1 = attach to 'dsetname'.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5onedimscale
  */
public static native void H4toh5alldimscale(int h45id,int sdid,String parent,
 String dsetname,String dimgroup,int attr_flag,int ref_flag) throws H45Exception;

/**
  *   Convert all the dimension scales from an HDF4 SDS to a HDF5 dimension
  *   datasets, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdid (IN) An SDS ID,  returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group of the dataset to attach.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   dimensions (if none, <b>null</b>).
  *   @param dimgroup (IN) The absolute path of the HDF5 group to store 
  *   the dimension datasets.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the dimension
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the dimension
  *    to the dataset
  *    <ul>
  *	<li>	<b>false</b> = don't attach
  *	<li>	<b>true</b> = attach to 'dsetname'.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5onedimscale
  */
public static void H4toh5alldimscale(int h45id,int sdid,String parent,
 String dsetname,String dimgroup,boolean attr_flag,boolean ref_flag) 
throws H45Exception
{
	int af = 0;
	int rf = 0;
	if (attr_flag) af = 1;
	if (ref_flag) rf = 1;
	H4toh5alldimscale(h45id,sdid,parent,dsetname,dimgroup,af,rf) ;
}

/**
  *   Convert one dimension scale from HDF4 to a dataset in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdid (IN) An SDS ID,  returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dimension should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   dimension (if none, <b>null</b>).
  *   @param dimgroup (IN) The absolute path of the HDF5 group to store 
  *   the dimension dataset.
  *   @param dimdatase (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the palette
  *    <ul>
  *	<li>	0 = don't convert
  *	<li>	1 = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    to an image
  *    <ul>
  *	<li>	0 = don't attach
  *	<li>	1 = attach to 'dsetname'.
  *    </ul>
  *   @param dim_ind (IN) The index of the dimension do convert.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5alldimscales
  */
public static native void H4toh5onedimscale(int h45id,int sdid,String parent,
 String dsetname,String dimgroup,String dimdataset, int attr_flag,
	int ref_flag, int dim_ind) throws H45Exception;
/**
  *   Convert one dimension scale from HDF4 to a dataset in HDF5, 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdid (IN) An SDS ID,  returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dimension should be 
  *          inserted.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach the 
  *   dimension (if none, <b>null</b>).
  *   @param dimgroup (IN) The absolute path of the HDF5 group to store 
  *   the dimension dataset.
  *   @param dimdatase (IN) The name of the HDF5 palette dataset.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the palette
  *    <ul>
  *	<li>	<b>false</b> = don't convert
  *	<li>	<b>true</b> = convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    to an image
  *    <ul>
  *	<li>	<b>false</b> = don't attach
  *	<li>	<b>true</b> = attach to 'dsetname'.
  *    </ul>
  *   @param dim_ind (IN) The index of the dimension do convert.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5alldimscales
  */
public static void H4toh5onedimscale(int h45id,int sdid,String parent,
 String dsetname,String dimgroup,String dimdataset, boolean attr_flag,
	boolean ref_flag, int dim_ind) throws H45Exception
{
	int af = 0;
	int rf = 0;
	if (attr_flag) af = 1;
	if (ref_flag) rf = 1;
	H4toh5onedimscale(h45id,sdid,parent, dsetname,dimgroup,dimdataset, af, 
		rf, dim_ind);
}

/**
  *   Convert all global SDS attributes to attributes of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5_glosdsattr(int h45id) throws H45Exception;

/**
  *   Convert all global GR attributes to attributes of the HDF5 root group.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5_glograttr(int h45id) throws H45Exception;

/*
// This is done by HDF45Exception.printStackTrace().
public static native int H4toh5Eget(int h45id);
*/

}
