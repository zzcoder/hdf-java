
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

public class vgrouptest {

	private int file_id = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public vgrouptest() {};

static public void main( String []args ) 
{
	vgrouptest vgt = new vgrouptest();
	vgt.setup();
	boolean r1 = vgt.test1();
	boolean r2 = vgt.test2();
	vgt.cleanup();
	if (r1 && r2) {
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
		file_id = HDFLibrary.Hopen("testallvgroup.hdf",
			HDFConstants.DFACC_READ);
	} catch (HDFException he1) {
		System.err.println("Setup: open H interface exception: "+he1);
		System.exit( -1);
	}
	if (file_id == -1) {
		System.err.println("Setup: cannot open H interface: "+file_id);
		System.exit( -1);
	}

	boolean status  = false;
	try {
		status = HDFLibrary.Vstart(file_id);
	} catch (HDFException he9) {
		System.err.println("Setup: open V interface exception: "+he9);
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he10) {}
		System.exit( -1);
	}

	if(status == false) {
		System.err.println("Setup: open V interface error");
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he12) {}
		System.exit( -1);
	}

	try {
		h4toh5id= h4toh5.H4toh5open("testallvgroup.hdf",
			"testallvgroup.h5",h4toh5.H425_CLOBBER);
	} catch (H45Exception h45e1) {
		System.err.println("Setup: H4toh5open exception "+h45e1);
		try {
			HDFLibrary.Vend(file_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he13) {}
		System.exit( -1);
	}
	if(h4toh5id  < 0) {
		System.err.println("Setup: error returned by H4toh5open: "+h4toh5id);
		try {
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

private void test2_cleanup() {
	// clean up for test 1 here?
	// vdata_id
	// vgroup_id
	// etc.
}

/*
 *  Test batch I:  test conversion of all lone vgroups
 *  Need to skip special groups such as "CDF0.0"
 */
public boolean test1(  ) 
{

	int num_lonevg = -1;

	try {
		num_lonevg = HDFLibrary.Vlone(file_id,null,0);
	} catch (HDFException he1) {
		System.err.println("Vlone exception "+he1);
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	if (num_lonevg < 0) {
		System.err.println("Vlone error ");
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	if (num_lonevg == 0) {
		System.err.println("Vlone: no groups found?");
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	int [] ref_array = new int[num_lonevg];

	try {
		num_lonevg = HDFLibrary.Vlone(file_id,ref_array,num_lonevg);
	} catch (HDFException he1) {
		System.err.println("Vlone exception "+he1);
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	if (num_lonevg < 0) {
		System.err.println("Vlone error ");
		System.err.println("Test I.1: FAIL");
		test1_cleanup();
		return(false);
	}

	for (int lone_vg_number = 0; lone_vg_number < num_lonevg; 
		lone_vg_number++) {
      		int vgroup_id = -1;
		try {
			vgroup_id = HDFLibrary.Vattach(file_id,
				ref_array[lone_vg_number],"r");
		} catch (HDFException he2) {
			System.err.println("Vattach exception "+he2);
			System.err.println("Test I.1: FAIL");
			test1_cleanup();
			return(false);
		}

		if (vgroup_id < 0) {
			System.err.println("Vattach error ");
			System.err.println("Test I.1: FAIL");
			test1_cleanup();
			return(false);
		}

		String vgroup_class[] = new String[1];
		vgroup_class[0] = new String("");
		try {
			HDFLibrary.Vgetclass(vgroup_id,vgroup_class);
		} catch (HDFException he3) {
			System.err.println("Vgetclass exception "+he3);
			System.err.println("Test I.1: FAIL");
			test1_cleanup();
			return(false);
		}

		if (vgroup_class[0].equals("CDF0.0")) {
			try {
				HDFLibrary.Vdetach(vgroup_id);
			} catch (HDFException he4) {}
			continue;
		}
		if (vgroup_class[0].equals("RIG0.0")) {
			try {
				HDFLibrary.Vdetach(vgroup_id);
			} catch (HDFException he4) {}
			continue;
		}

		try {
			h4toh5.H4toh5advgroup(h4toh5id,vgroup_id,
				"/",null);
		} catch (H45Exception h45e1) {
			System.err.println("h4toh5advgroup exception "+h45e1);
			System.err.println("Test I.4: FAIL");
			test1_cleanup();
			return(false);
		}

		try {
			HDFLibrary.Vdetach(vgroup_id);
		} catch (HDFException he5) {}
	}

	System.out.print("Test conversion of lone Vgroups: ");
 	System.out.println("PASS");
	return(true);
}

/*
 *  Test batch II:  test conversion of all lone vdatas
 *  Need to skip special groups such as "ATTR0.0"
 */
public boolean test2(  ) 
{

   /* II.1. Test  lone vdatas
    *
    *  vgroup itself and vgroup with non-vgroup members will be converted. 
    *
    */
	int num_lonevd = -1;

	try {
		num_lonevd = HDFLibrary.VSlone(file_id,null,0);
	} catch (HDFException he1) {
		System.err.println("VSlone exception "+he1);
		System.err.println("Test II.1: FAIL");
		test2_cleanup();
		return(false);
	}

	if (num_lonevd < 0) {
		System.err.println("VSlone error ");
		System.err.println("Test II.1: FAIL");
		test2_cleanup();
		return(false);
	}

	if (num_lonevd == 0) {
		System.err.println("VSlone: no groups found?");
		System.err.println("Test II.1: FAIL");
		test2_cleanup();
		return(false);
	}

	int [] ref_array = new int[num_lonevd];

	try {
		num_lonevd = HDFLibrary.VSlone(file_id,ref_array,num_lonevd);
	} catch (HDFException he1) {
		System.err.println("VSlone exception "+he1);
		System.err.println("Test II.1: FAIL");
		test2_cleanup();
		return(false);
	}

	if (num_lonevd < 0) {
		System.err.println("VSlone error ");
		System.err.println("Test II.1: FAIL");
		test2_cleanup();
		return(false);
	}

	for (int lone_vd_number = 0; lone_vd_number < num_lonevd; 
		lone_vd_number++) {
      		int vdata_id = -1;
		try {
			vdata_id = HDFLibrary.VSattach(file_id,
				ref_array[lone_vd_number],"r");
		} catch (HDFException he2) {
			System.err.println("VSattach exception "+he2);
			System.err.println("Test II.1: FAIL");
			test2_cleanup();
			return(false);
		}

		if (vdata_id < 0) {
			System.err.println("VSattach error ");
			System.err.println("Test II.1: FAIL");
			test2_cleanup();
			return(false);
		}
		

		boolean isA = false;
		try {
			isA = HDFLibrary.VSisattr(vdata_id);
		} catch (HDFException he3) {
			System.err.println("VSisattr exception "+he3);
			System.err.println("Test II.1: FAIL");
			test2_cleanup();
			return(false);
		}

		if (isA) {
			try {
				HDFLibrary.VSdetach(vdata_id);
			} catch (HDFException he5) {}
			continue;
		}

		String vdata_class[] = new String[1];
		vdata_class[0] = new String("");
		try {
			HDFLibrary.VSgetclass(vdata_id,vdata_class);
		} catch (HDFException he4) {
			System.err.println("VSgetclass exception "+he4);
			System.err.println("Test II.1: FAIL");
			test2_cleanup();
			return(false);
		}

		if (vdata_class[0].equals("_HDF_CHK_TBL_")) {
			try {
				HDFLibrary.VSdetach(vdata_id);
			} catch (HDFException he5) {}
			continue;
		}

		try {
			h4toh5.H4toh5vdata(h4toh5id,vdata_id,
				"/",null,1);
		} catch (H45Exception h45e1) {
			System.err.println("h4toh5vdata exception "+h45e1);
			System.err.println("Test II.4: FAIL");
			test2_cleanup();
			return(false);
		}

		try {
			HDFLibrary.VSdetach(vdata_id);
		} catch (HDFException he6) {}
	}

	System.out.print("Test conversion of lone Vdatas: ");
 	System.out.println("PASS");
	return(true);
}

}
