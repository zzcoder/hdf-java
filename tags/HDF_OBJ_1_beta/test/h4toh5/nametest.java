
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

import ncsa.hdf.h4toh5lib.*;
import ncsa.hdf.h4toh5lib.exceptions.*;
import ncsa.hdf.hdflib.*;
import ncsa.hdf.hdf5lib.*;

public class nametest {

	private int file_id = -1;
	private int sd_id   = -1;
	private int gr_id = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public nametest() {};

static public void main( String []args ) 
{
	nametest nt = new nametest();
	nt.setup();
	boolean r1 = nt.test1();
	nt.cleanup();
	if (r1) {
		System.exit(0);
	} else {
		System.exit(-1);
	}
}

/*
 * Set up the testfiles and library interfaces.
 *
 * These are used by all the tests.
 */
public void setup(  ) 
{
	try {
		file_id = HDFLibrary.Hopen("vg_all_testname.hdf",
			HDFConstants.DFACC_READ);
	} catch (HDFException he1) {
		System.err.println("Setup: open H interface exception: "+he1);
		System.exit( -1);
	}
	if (file_id == -1) {
		System.err.println("Setup: cannot open H interface: "+file_id);
		System.exit( -1);
	}

	try {
		sd_id = HDFLibrary.SDstart("vg_all_testname.hdf",
			HDFConstants.DFACC_READ);
	} catch (HDFException he2) {
		System.err.println("Setup: open SD interface exception: "+he2);
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he3) {}
		System.exit( -1);
	}
	if(sd_id == -1) {
		System.err.println("Setup: cannot start SD interface: "+sd_id);
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he4) {}
		System.exit( -1);
	}

	try {
		gr_id = HDFLibrary.GRstart(file_id);
	} catch (HDFException he5) {
		System.err.println("Setup: open GR interface exception: "+he5);
		try {
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he6) {}
		System.exit( -1);
	}
	if(gr_id == -1) {   
		System.err.println("Setup: open GR interface error: "+gr_id);
		try {
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he8) {}
		System.exit( -1);
	}

	boolean status  = false;
	try {
		status = HDFLibrary.Vstart(file_id);
	} catch (HDFException he9) {
		System.err.println("Setup: open V interface exception: "+he9);
		try {
			HDFLibrary.GRend(gr_id);
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he10) {}
		System.exit( -1);
	}

	if(status == false) {
		System.err.println("Setup: open V interface error");
		try {
			HDFLibrary.GRend(gr_id);
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he12) {}
		System.exit( -1);
	}

	try {
		h4toh5id= h4toh5.H4toh5open("vg_all_testname.hdf",
			"vg_all_testname.h5",h4toh5.H425_CLOBBER);
	} catch (H45Exception h45e1) {
		System.err.println("Setup: H4toh5open exception "+h45e1);
		try {
			HDFLibrary.Vend(file_id);
			HDFLibrary.GRend(gr_id);
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he13) {}
		System.exit( -1);
	}
	if(h4toh5id  < 0) {
		System.err.println("Setup: error returned by H4toh5open: "+h4toh5id);
		try {
			HDFLibrary.SDend(sd_id);
			HDFLibrary.GRend(gr_id);
			HDFLibrary.Vend(file_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he14) {}
		System.exit( -1);
	}
	initialized = true;
}

/*
 * Clean up of global objects.
 */
public void cleanup()
{
	if (initialized) {
		try {
			h4toh5.H4toh5close(h4toh5id);
			HDFLibrary.GRend(gr_id);
			HDFLibrary.SDend(sd_id);
			HDFLibrary.Vend(file_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he) {}
		catch (H45Exception h45e) {}
		initialized = false;
	}
}

private void test1_cleanup() {
	// clean up for test 1 here?
	// vdata_id
	// vgroup_id
	// etc.
	//   VSdetach(vdata_id);
	//   Vdetach(vgroup_id);
	//   SDendaccess(sds_id);
	//   GRendaccess(image_id);
}

/*
 *  Test batch I:  test conversion using the user name generation
 *                 for the HDF5 objects.
 */
public boolean test1(  ) 
{

	
   /* 1. Test SDS conversion; choose the SDS object with index 0;
         all attributes and dimensional scales of this SDS will
         be converted. */

	int sds_id = -1;
	try {
		sds_id = HDFLibrary.SDselect(sd_id,0);
	} catch (HDFException he1) {
		System.err.println("SDselect failed");
		System.err.println("Test I.1: FAIL");
		return(false);
	}

	try {
		h4toh5.H4toh5sds(h4toh5id,sds_id,"/group1u",
			"mysds","/mydimgroup",1,1);
	} catch (H45Exception h45e1) {
		System.err.println("H4toh5sds exception "+h45e1);
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	/* 2. Testing GR conversion. */
	int image_id = -1;
	try {
		image_id = HDFLibrary.GRselect(gr_id,0);
	} catch (HDFException he2) {
		System.err.println("GRselect exception "+he2);
		System.err.println("Test I.2: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5image(h4toh5id,image_id,
			"/group1u","myimage","/mypalgroup",
			"mypal",1,1);
	} catch (H45Exception h45e2) {
		System.err.println("h4toh5image exception "+h45e2);
		System.err.println("Test I.2: FAIL");
		test1_cleanup();
		return(false);
	}

   /* 
    *	3. Test independent vdata conversion API.
    *
    *   Since Vdata may be attributes, in order to avoid converting attributes;
    *   we use hdp to find the reference number of an independent vdata in this 
    *   file and obtain vdata id later. 
    */

	int vdata_ref = 9;
	int vdata_id = -1;
	try {
		vdata_id = HDFLibrary.VSattach(file_id,vdata_ref,"r");
	} catch (HDFException he3) {
		System.err.println("VSgetid exception "+he3);
		System.err.println("Test I.3: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5vdata(h4toh5id,vdata_id,
			"/group1u","myvdata",1);
	} catch (H45Exception h45e3) {
		System.err.println("h4toh5vdata exception "+h45e3);
		System.err.println("Test I.3: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.VSdetach(vdata_id);
	} catch (HDFException he3_4) {
		System.err.println("VSdetach error ");
		System.err.println("Test I.3: FAIL after test");
		return(false);
	}


   /* I.4. Test partial vgroup API. 
    *
    *  vgroup itself and vgroup with non-vgroup members will be converted. 
    *
    */
	int vgroup_ref = -1;
	int vgroup_id = -1;

	try {
		vgroup_ref = HDFLibrary.Vgetid(file_id,-1);
		vgroup_id = HDFLibrary.Vattach(file_id,vgroup_ref,"r");
	} catch (HDFException he4) {
		System.err.println("Vget or Vattach exception "+he4);
		System.err.println("Test I.4: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5basvgroup(h4toh5id,vgroup_id,
			"/gpartial","myvgroup",1,1);
	} catch (H45Exception h45e4) {
		System.err.println("h4toh5vgroup exception "+h45e4);
		System.err.println("Test I.4: FAIL");
		test1_cleanup();
		return(false);
	}

   /*  Test I.5. Test the whole vgroup API. 
    *  Everything under this vgroup is converted.
    */
	try {
		h4toh5.H4toh5advgroup(h4toh5id,vgroup_id,
			"/gwhole","myallvgroup");
	} catch (H45Exception h45e5) {
		System.err.println("h4toh5advgroup exception "+h45e5);
		System.err.println("Test I.5: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.Vdetach(vgroup_id);
	} catch (HDFException he5_1) {
		System.err.println("Vdetach error ");
		System.err.println("Test I.5: FAIL after test");
		return(false);
	}

   /*  Test I.6. Test annotation API. */

	try {
		h4toh5.H4toh5annofil_alllabels(h4toh5id);
	} catch (H45Exception h45e6) {
		System.err.println("h4toh5annofil_alllabels exception "+h45e6);
		System.err.println("Test I.6: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5annofil_alldescs(h4toh5id);
	} catch (H45Exception h45e6_1) {
		System.err.println("h4toh5annofilr_alldescs exception "+h45e6_1);
		System.err.println("Test I.6: FAIL");
		test1_cleanup();
		return(false);
	}

	/***** object annotations testing, we have to provide the object reference
	 number and object tag that have annotations associate with it.
	 we will use hdp to check the reference of such object. In this 
	 example, vgroup is used. vgroup tag is 1965. vgroup reference is 4.
	 *****/

	int anno_objref = 4;
	int anno_objtag = 1965;

	try {
		h4toh5.H4toh5annoobj_label(h4toh5id,"/","/group1u",
				anno_objref, anno_objtag,null,0);
	} catch (H45Exception h45e6_2) {
		System.err.println("h4toh5annoobj_label exception "+h45e6_2);
		System.err.println("Test I.6: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5annoobj_desc(h4toh5id,"/","/group1u",
			anno_objref,anno_objtag,null,0);
	} catch (H45Exception h45e6_3) {
		System.err.println("h4toh5annoobj_desc exception "+h45e6_3);
		System.err.println("Test I.6: FAIL");
		test1_cleanup();
		return(false);
	}

   /*  Test I.7. Test palette conversion.
        Note: We only tested default parameters; 
              since we already used a default palette group 
              "/HDF4_PALGROUP" when converting the image; so 
              we made another palette group for this test.*/

	try {
		h4toh5.H4toh5pal(h4toh5id,image_id,null,null,
			"/paltestgroup",null,1,0);
	} catch (H45Exception h45e7) {
		System.err.println("h4toh5pal exception "+h45e7);
		System.err.println("Test I.7: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.GRendaccess(image_id);
	} catch (HDFException he7_1) {
		System.err.println("Test I.7: FAIL after test");
		return(false);
	}


   /* Test I.8. Test dimensional scale dataset API. */

	try {
		h4toh5.H4toh5alldimscale(h4toh5id,sds_id,"/group1",
			"sds","/dimtestgroup",1,0);
	} catch (H45Exception h45e8) {
		System.err.println("h4toh5alldimscale exception "+h45e8);
		System.err.println("Test I.8: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.SDendaccess(sds_id);
	} catch (HDFException he8_1) {
		System.err.println("Test I.8: FAIL after test");
		return(false);
	}

	try {
		h4toh5.H4toh5_glosdsattr(h4toh5id);
	} catch (H45Exception h45e38) {
		System.err.println("h4toh5allglosdsattr exception "+h45e38);
		System.err.println("Test I.8: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5_glograttr(h4toh5id);
	} catch (H45Exception h45e41) {
		System.err.println("h4toh5glograttr exception "+h45e41);
		System.err.println("Test I.8: FAIL");
		test1_cleanup();
		return(false);
	}

	System.out.print("Test conversion using names: ");
 	System.out.println("PASS");
	return(true);
}


}
