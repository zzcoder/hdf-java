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

public class vdatatest {

	private int file_id = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public vdatatest() {};

static public void main( String []args ) 
{
	vdatatest vdt = new vdatatest();
	vdt.setup();
	boolean r1 = vdt.test1();
	boolean r2 = vdt.test2();
	vdt.cleanup();
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
		file_id = HDFLibrary.Hopen("vdata_lib_test.hdf",
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
		System.err.println("Setup: open VS interface exception: "+he9);
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
		h4toh5id= h4toh5.H4toh5open("vdata_lib_test.hdf",
			"vdata_lib_test.h5",h4toh5.H425_CLOBBER);
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
private void test2_cleanup() {}

/*
 *  Test batch I:  test Vdata conversion
 *                 for the HDF5 objects.
 */
public boolean test1(  ) 
{

	int vdata_ref = 2;
	int vdata_id = -1;
	try {
		vdata_id = HDFLibrary.VSattach(file_id,vdata_ref,"r");
	} catch (HDFException he3) {
		System.err.println("VSgetid exception "+he3);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5vdata(h4toh5id,vdata_id,"/group1",null,0);
	} catch (H45Exception h45e3) {
		System.err.println("h4toh5vdata exception "+h45e3);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.VSdetach(vdata_id);
	} catch (HDFException he3_4) {
		System.err.println("VSdetach error ");
		System.err.println("Test 1: FAIL after test");
		return(false);
	}

	System.out.print("Test Vdata conversion: ");
 	System.out.println("PASS");
	return(true);
}

/*
 *  Test batch II:  test Vdata conversion including attrs
 *                 for the HDF5 objects.
 */
public boolean test2(  ) 
{

	int vdata_ref = 2;
	int vdata_id = -1;
	try {
		vdata_id = HDFLibrary.VSattach(file_id,vdata_ref,"r");
	} catch (HDFException he3) {
		System.err.println("VSgetid exception "+he3);
		System.err.println("Test 1: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5vdataattrindex(h4toh5id,vdata_id,"/group1",
			null,1);
	} catch (H45Exception h45e3) {
		System.err.println("h4toh5vdataattrindex exception "+h45e3);
		System.err.println("Test 1: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5vdatafieldattrindex(h4toh5id,vdata_id,"/group1",
			null,2,2);
	} catch (H45Exception h45e32) {
		System.err.println("h4toh5vdatafieldattrindex exception "+h45e32);
		System.err.println("Test 1: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		HDFLibrary.VSdetach(vdata_id);
	} catch (HDFException he3_4) {
		System.err.println("VSdetach error ");
		System.err.println("Test 1: FAIL after test");
		return(false);
	}

	System.out.print("Test Vdata conversion of attrs: ");
 	System.out.println("PASS");
	return(true);
}

}
