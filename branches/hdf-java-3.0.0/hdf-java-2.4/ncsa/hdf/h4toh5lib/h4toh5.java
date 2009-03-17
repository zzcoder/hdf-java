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


/**
 *  This is a class that wraps the HDF4 to HDF5 library.
 *  <p>
 */
package ncsa.hdf.h4toh5lib;

import java.io.*;
import ncsa.hdf.hdflib.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.h4toh5lib.exceptions.*;

import java.lang.Class;

public class h4toh5 {
	final static String H45PATH_PROPERTY_KEY = "ncsa.hdf.h4toh5lib.h4toh5.h45lib"; 

	final static String JH4to5vers = "1.0beta";

    static
    {
	Class c1 = null;
	try {
		c1 = Class.forName("ncsa.hdf.h4toh5lib.h4toh5");
	} catch (ClassNotFoundException cnf) {
// what to do here?
		System.out.println("h4toh5 is not found.");
	}
	ClassLoader cl1  = null;
	if (c1 != null) {
		try {
			cl1 = c1.getClassLoader();
		} catch (SecurityException se) {
// what to do here?
			System.out.println("class loader not accessible: ");
		}
	}

	/* special:  Want to force loading of classes, but not
	 *   call the static initializer because this will zap
         *   the library if already open.
        */
	if (cl1 != null) {
	Class c3 = null;
	try {
		c3 = Class.forName("ncsa.hdf.hdf5lib.H5",false,cl1);
	} catch (ClassNotFoundException cnf) {
// what to do here?
		System.out.println("HDF5 is not found.");
	}

	Class c4 = null;
	try {
		c4 = Class.forName("ncsa.hdf.hdflib.HDFLibrary");
	} catch (ClassNotFoundException cnf) {
// what to do here?
		System.out.println("HDF is not found.");
	}
	}

	// Actually load the jh4toh5lib here.  May already be loaded.
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

	// To do:  do the HDF5 'atexit call'?
    }

/** CREATE if not exists */
public static final int  H425_CREATE = 1;  
/** OPEN if exists */
public static final int  H425_OPEN = 2;  
/** CREATE, overwrite if exists */
public static final int  H425_CLOBBER = 3;

/** do not copy members of a Vgroup */
public static final int  H425_NOMEMBERS = 0;  
/** copy all members of a Vgroup (except groups) */
public static final int  H425_ALLMEMBERS = 1;

/** do not copy attributes of an HDF4 object */
public static final int  H425_NOATTRS = 0;  
/** copy all attributes of an HDF4 object */
public static final int  H425_ALLATTRS = 1;

/** do not copy dimension scales of an HDF4 SDS */
public static final int  H425_NODIMSCALE = 0;  
/** copy all dimension scales of an HDF4 SDS */
public static final int  H425_DIMSCALE = 1;

/** do not copy palette of an HDF4 GR image */
public static final int  H425_NOPAL = 0;  
/** copy the palette of an HDF4 GR image */
public static final int  H425_PAL = 1;

/** do not attach palette to an HDF5 image */
public static final int  H425_NOREF = 0;  
/** attach palette to an HDF5 image */
public static final int  H425_REF = 1;

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
  *   @param hdf5file (IN) The HDF5 file (input).  If the file does not
  *   @param access (IN) (input).  Controls the overwrite of the output
  *       file.
  *     <ul>
  *     <dl>
  *     <dt><b>H425_CREATE</b> </dt>
  *      <dd> Create the file if it does not exist, fail otherwise. </dd>
  *     <dt><b>H425_OPEN</b> </dt>
  *      <dd> Create the file if it does exist, fail otherwise. </dd>
  *     <dt><b>H425_CLOBBER</b> </dt>
  *      <dd> Create the file. If it does exist, delete and create a new
  *      file. </dd>
  *     </dl>
  *     </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @return The <i>h45id</i>, used in subsequent calls.
  */
public static native int H4toh5open(String hdf4file,String hdf5file,int access) 
	throws H45Exception;

/**
  *   Initialize the h4 to h5 library. The return value is the h4toh5id
  *   that must be passed to subsequent library calls.
  *
  *   @param hdf4file (IN) The HDF4 file (input). The files is open to read.
  *   @param h5fid (IN) The HDF5 descriptor for an open HDF5 file. (input).  
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @return The <i>h45id</i>, used in subsequent calls.
  */
public static native int H4toh5openid(String hdf4file, int h5fid) 
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
  *    <dl>
  *	<dt><b>H425_NOMEMBERS</b></dt>
  *     <dd> don't convert </dd>
  *	<dt><b>H425_ALLMEMBERS</b></dt>
  *	<dd>convert all elements of the Vgroup that are not Vgroups </dd>
  *    </dl>
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vgroup
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the Vgroup into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
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
	int vf = H425_NOMEMBERS;
	int af = H425_NOATTRS;
	if (vg_flag) vf = H425_ALLMEMBERS;
	if (attr_flag) af = H425_ALLATTRS;

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
  *   Convert a named attribute of a vgroup from HDF4 to an attribute 
  *   of a group in HDF5.
  *   <p>
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vgroup ID, returned by HDF4 Vattach.
  *   @param parent (IN) The HDF5 group in which the converted grup should be 
  *          inserted.
  *   @param group (IN) The name of the HDF5 group to create.
  *   @param attrname (IN) The name of the HDF5 attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vgroupattrindex
  */
public static native void H4toh5vgroupattrname(int h45id,int vgid,
    String parent,String group,String attrname) throws H45Exception;


/**
  *   Convert an indexed attribute of a vgroup from HDF4 to an attribute 
  *   of a group in HDF5.
  *   <p>
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vgid (IN) The vgroup ID, returned by HDF4 Vattach.
  *   @param parent (IN) The HDF5 group in which the converted grup should be 
  *          inserted.
  *   @param group (IN) The name of the HDF5 group to create.
  *   @param index (IN) The HDF4 index of the attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vgroupattrname
  */
public static native void H4toh5vgroupattrindex(int h45id,int vgid,
    String parent,String group,int index) throws H45Exception;

/**
  *   Convert a vdata from HDF4 to a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to create the
  *   dataset.
  *   @param name (IN) The name of the HDF5 dataset to create.
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the vdata
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the vdata into equivalent 
  *     attributes of the HDF5 dataset.</dd>
  *    </dl>
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5vdata(int h45id,int vdid,
    String parent,String name,int attr_flag) throws H45Exception;

/**
  *   Convert a vdata from HDF4 to a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to create the
  *   dataset.
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
public static void H4toh5vdata(int h45id,int vdid,String parent,
	String name,boolean attr_flag) throws H45Exception
{
	int af = H425_NOATTRS;
	if (attr_flag) af = H425_ALLATTRS;
	H4toh5vdata(h45id,vdid,parent,name,af);
}

/**
  *   Convert a named attribute of a vdata from HDF4 to an attribute of a 
  *   dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to find the
  *   dataset.
  *   @param name (IN) The name of the HDF5 dataset.
  *   @param attrname (IN) The HDF4 attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vdataattrindex
  */
public static native void H4toh5vdataattrname(int h45id,int vdid,
     String parent,String name,String attrname) throws H45Exception;

/**
  *   Convert an indexed attribute of a vdata from HDF4 to an attribute of a 
  *   dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to find the
  *   dataset.
  *   @param name (IN) The name of the HDF5 dataset.
  *   @param attrname (IN) The index of the HDF4 attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vdataattrname
  */
public static native void H4toh5vdataattrindex(int h45id,int vdid,
     String parent,String name,int attrindex) throws H45Exception;


/**
  *   Convert a named attribute of a vdata field from HDF4 to an attribute 
  *   of a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to find the
  *   dataset.
  *   @param name (IN) The name of the HDF5 dataset.
  *   @param fieldname (IN) The name of the field.
  *   @param attrname (IN) The HDF4 attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vdatafieldattrindex
  */
public static native void H4toh5vdatafieldattrname(int h45id,int vdid,
     String parent,String name,String fieldname,String attrname) 
     throws H45Exception;

/**
  *   Convert an indexed attribute of a vdata field from HDF4 to an 
  *   attribute of a dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param vdid (IN) The vdata ID, returned by HDF4 VSattach.
  *   @param parent (IN) The name of the HDF5 group in which to find the
  *   dataset.
  *   @param name (IN) The name of the HDF5 dataset.
  *   @param fieldindex (IN) The index of the field.
  *   @param attrindex (IN) The index of the HDF4 attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5vdatafieldattrname
  */
public static native void H4toh5vdatafieldattrindex(int h45id,int vdid,
     String parent,String name,int fieldindex, int attrindex) 
     throws H45Exception;


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
  *    <dl>
  *	<dt><b>H425_NODIMSCALE</b></dt>
  *     <dd>don't convert</dd>
  *	<dt>H425_ALLDIMSCALE</dt>
  *	<dd>convert all dimension scales of the SDS</dd>
  *    </dl>
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to convert the attributes
  *    of the SDS
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the SDS into equivalent 
  *     attributes of the HDF5 dataset.</dd>
  *    </dl>
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
  *   @see H4toh5sds
  */
public static void H4toh5sds(int h45id,int sdsid,String parent,
	String dsetname,String dimgroupname,boolean dim_flag,boolean attr_flag) 
	throws H45Exception
{
	int df = H425_NODIMSCALE;
	int af = H425_NOATTRS;
	if (dim_flag) df = H425_DIMSCALE;
	if (attr_flag) af = H425_ALLATTRS;
	H4toh5sds(h45id,sdsid,parent, dsetname,dimgroupname,df,af);
}

/**
  *   Convert a named attribute of an SDS from HDF4 to an attribute of a 
  *   dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdsid (IN) The SD ID, returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dataset 
  *     is located.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach 
  *     the attribute.
  *   @param attrname (IN) The HDF4 attribute.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5sdsattrindex
  */
public native static void H4toh5sdsattrname(int h45id,int sdsid,String parent,
	String dsetname,String attrname)
	throws H45Exception;

/**
  *   Convert a indexed attribute of an SDS from HDF4 to an attribute of a 
  *   dataset in HDF5.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param sdsid (IN) The SD ID, returned by HDF4 SDselect.
  *   @param parent (IN) The HDF5 group in which the converted dataset 
  *     is located.
  *   @param dsetname (IN) The name of the HDF5 dataset to attach 
  *     the attribute.
  *   @param attrname (IN) The index of the HDF4 attribute.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5sdsattrname
  */
public native static void H4toh5sdsattrindex(int h45id,int sdsid,
    String parent,String dsetname,int index)
	throws H45Exception;

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
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
  *    </ul>
  *   @param pal_flag (IN) Flag to indicate whether to convert the palette
  *    of the image
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOPAL</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_PAL</b></dt>
  *	<dd>convert the palette as well as the image </dd>
  *    </dl>
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
	int af = H425_NOATTRS;
	int pf = H425_NOPAL;
	if (attr_flag) af = H425_ALLATTRS;
	if (pal_flag) pf = H425_PAL;
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
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOREF</b></dt>
  *	<dd>don't convert</dd>
  *	<dt>H425_ALLREF</dt>
  *	<dd>attach to 'dsetname'. </dd>
  *    </dl>
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
	int af = H425_NOATTRS;
	int rf = H425_NOREF;
	if (attr_flag) af = H425_ALLATTRS;
	if (ref_flag) rf = H425_REF;
	H4toh5pal(h45id,riid,parent, dsetname,palgroup,paldset,af, rf); 
}

/**
  *   Convert a named attribute of a raster image from HDF4 to an attribute
  *   of an image dataset in HDF5. 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param grid (IN) The GR image ID, returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the image is found.
  *   @param dsetname (IN) The name of the HDF5 image dataset.
  *   @param attrname (IN) The attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5imageattrindex
  */
public static native void H4toh5imageattrname(int h45id,int riid,
  String parent,String dsetname,String attrname) 
	throws H45Exception; 

/**
  *   Convert a named attribute of a raster image from HDF4 to an attribute
  *   of an image dataset in HDF5. 
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param grid (IN) The GR image ID, returned by HDF4 GRselect.
  *   @param parent (IN) The HDF5 group in which the image is found.
  *   @param dsetname (IN) The name of the HDF5 image dataset.
  *   @param index (IN) The index of the attribute to copy.
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  *   @see H4toh5imageattrname
  */
public static native void H4toh5imageattrindex(int h45id,int riid,
  String parent,String dsetname,int index) 
	throws H45Exception; 

/**
  *   Convert an annotation to a String attribute of the HDF5 root group.
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
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the dimension
  *    to the dataset
  *    <dl>
  *	<dt><b>H425_NOREF</b></dt>
  *	<dd>don't convert</dd>
  *	<dt>H425_ALLREF</dt>
  *	<dd>attach to 'dsetname'. </dd>
  *    </dl>
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
	int af = H425_NOATTRS;
	int rf = H425_NOREF;
	if (attr_flag) af = H425_ALLATTRS;
	if (ref_flag) rf = H425_REF;
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
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the image into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
  *    </ul>
  *   @param ref_flag (IN) Flag to indicate whether to attach the palette
  *    to an image
  *    <dl>
  *	<dt><b>H425_NOREF</b></dt>
  *	<dd>don't convert</dd>
  *	<dt>H425_ALLREF</dt>
  *	<dd>attach to 'dsetname'. </dd>
  *    </dl>
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
	int af = H425_NOATTRS;
	int rf = H425_NOREF;
	if (attr_flag) af = H425_ALLATTRS;
	if (ref_flag) rf = H425_REF;
	H4toh5onedimscale(h45id,sdid,parent,dsetname,dimgroup,dimdataset, 
            af,rf,dim_ind);
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

/**
  *   Convert all lone vdatas to datasets under a specified HDF5 group,
  *   optionally copying the attributes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the Vgroup into equivalent 
  *     attributes of the HDF5 group.</dd>
  *    </dl>
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5alllonevdata(int h45id, String groupname,
   int attr_flag) throws H45Exception;

/**
  *   Convert all lone vdatas to datasets to a specified HDF5 group,
  *   optionally copying the attributes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *	<li><b>false</b> don't convert
  *	<li><b>true</b> convert all attributes of the 
  *     Vgroup into equivalent 
  *     attributes of the HDF5 group.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static void H4toh5alllonevdata(int h45id, String groupname,
   boolean attr_flag) throws H45Exception
  {
    int af = H425_NOATTRS;
    if (attr_flag) af = H425_ALLATTRS;
    H4toh5alllonevdata(h45id, groupname, af);
  }

/**
  *   Convert all lone SDSs to datasets under a specified HDF5 group,
  *   optionally copying the attributes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param dimgroupname (IN) The HDF5 group to store the dim scales.
  *   @param dim_flag (IN) Flag to indicate whether to copy dimensions:
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NODIMSCALE</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_DIMSCALE</b></dt>
  *	<dd>convert all dimensions of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.</dd>
  *    </dl>
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.</dd>
  *    </dl>
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5alllonesds(int h45id, String groupname,
   String dimgroupname, int dim_flag, int attr_flag) throws H45Exception;

/**
  *   Convert all lone SDSs to datasets to a specified HDF5 group,
  *   optionally copying the attributes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param dimgroupname (IN) The HDF5 group to store the dim scales.
  *   @param dim_flag (IN) Flag to indicate whether to copy dimensions:
  *    <ul>
  *	<li><b>true</b> don't convert
  *	<li><b>false</b> convert all dimensions.
  *    </ul>
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *	<li><b>false</b>don't convert
  *	<li><b>true</b>convert all attributes of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static void H4toh5alllonesds(int h45id, String groupname,
   String dimgroupname, boolean dim_flag, boolean attr_flag) throws H45Exception
  {
    int df = H425_NODIMSCALE;
    if (dim_flag) df = H425_DIMSCALE;
    int af = H425_NOATTRS;
    if (attr_flag) af = H425_ALLATTRS;
    H4toh5alllonesds(h45id, groupname, dimgroupname, df, af);
  }

/**
  *   Convert all GR images to datasets under a specified HDF5 group,
  *   optionally copying the attributes and palettes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param palgroupname (IN) The HDF5 group to store the palette.
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *    <dl>
  *	<dt><b>H425_NOATTRS</b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b>H425_ALLATTRS</b></dt>
  *	<dd>convert all attributes of the Vdata into equivalent 
  *     attributes of the HDF5 dataset.</dd>
  *    </dl>
  *    </ul>
  *   @param pal_flag (IN) Flag to indicate whether to copy dimensions:
  *    <ul>
  *    <dl>
  *	<dt><b><b>H425_NOPAL</b></b></dt>
  *	<dd>don't convert</dd>
  *	<dt><b><b>H425_PAL</b></b></dt>
  *	<dd>convert palette.</dd>
  *    </dl>
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static native void H4toh5allloneimage(int h45id, String groupname,
   String palgroupname, int attr_flag, int pal_flag) throws H45Exception;

/**
  *   Convert all lone SDSs to datasets to a specified HDF5 group,
  *   optionally copying the attributes.
  *
  *   @param h45id (IN) The h45id, returned from H4toh5open.
  *   @param groupname (IN) The HDF5 group in which to place the vdatas.
  *   @param palgroupname (IN) The HDF5 group to store the palette.
  *   @param attr_flag (IN) Flag to indicate whether to copy attributes:
  *    <ul>
  *	<li><b>false</b> don't convert
  *	<li><b>true</b> convert all dimensions.
  *    </ul>
  *   @param pal_flag (IN) Flag to indicate whether to copy dimensions:
  *    <ul>
  *	<li><b>false</b> don't convert
  *	<li><b>true</b> convert palette.
  *    </ul>
  *
  *   @exception HDF45Exception Error in the java-h4toh5 or the libh4toh5.
  *
  */
public static void H4toh5allloneimage(int h45id, String groupname,
   String palgroupname, boolean attr_flag, boolean pal_flag) 
   throws H45Exception
  {
    int pf = H425_NOPAL;
    if (pal_flag) pf = H425_PAL;
    int af = H425_NOATTRS;
    if (attr_flag) af = H425_ALLATTRS;
    H4toh5allloneimage(h45id, groupname, palgroupname, pf, af);
  }

/*
// This is done by HDF45Exception.printStackTrace().
public static native int H4toh5Eget(int h45id);
*/

/*
// not sure how to implement this one.
public static native int  H4toh5_datatypeconv(hid_t   h4toh5id,
const   int32 h4type,                                                         
hid_t*  h5typeptr,                                                            
hid_t*  h5memtypeptr,                                                         
size_t* h4sizeptr,                                                            
size_t* h4memsizeptr)

*/
}
