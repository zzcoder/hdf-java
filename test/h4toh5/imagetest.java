
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

public class imagetest {

	private int file_id = -1;
	private int gr_id = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public imagetest() {};

static public void main( String []args ) 
{
	imagetest it = new imagetest();
	it.setup();
	boolean r1 = it.test1();
	boolean r2 = it.test2();
	it.cleanup();
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
		file_id = HDFLibrary.Hopen("image_lib_test.hdf",
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
		gr_id = HDFLibrary.GRstart(file_id);
	} catch (HDFException he5) {
		System.err.println("Setup: open GR interface exception: "+he5);
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he6) {}
		System.exit( -1);
	}
	if(gr_id == -1) {   
		System.err.println("Setup: open GR interface error: "+gr_id);
		try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he8) {}
		System.exit( -1);
	}

	try {
		h4toh5id= h4toh5.H4toh5open("image_lib_test.hdf",
			"image_lib_test.h5");
	} catch (H45Exception h45e1) {
		System.err.println("Setup: H4toh5open exception "+h45e1);
		try {
			HDFLibrary.GRend(gr_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he13) {}
		System.exit( -1);
	}
	if(h4toh5id  < 0) {
		System.err.println("Setup: error returned by H4toh5open: "+h4toh5id);
		try {
			HDFLibrary.GRend(gr_id);
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
 *  Test batch I:  test all API using the default name generation
 *                 for the HDF5 objects.
 */
public boolean test1(  ) 
{

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
		h4toh5.H4toh5image(h4toh5id,image_id,"/group1",
			null,null,null,1,1);
	} catch (H45Exception h45e2) {
		System.err.println("h4toh5image exception "+h45e2);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}


	try {
		HDFLibrary.GRendaccess(image_id);
	} catch (HDFException he7_1) {
		System.err.println("Test 1: FAIL after test");
		return(false);
	}


	System.out.print("Test image conversion using default name generation: ");
 	System.out.println("PASS");
	return(true);
}

/*
 *  Test batch II:  Test API with user-defined name for HDF5 objects
 */
public boolean test2(  ) 
{

	int image_id = -1;
	try {
		image_id = HDFLibrary.GRselect(gr_id,1);
	} catch (HDFException he2) {
		System.err.println("GRselect exception "+he2);
		System.err.println("Test 2: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5image(h4toh5id,image_id,"/group1",
			"myimage","/mypalg","mypal",0,1);
	} catch (H45Exception h45e2_1) {
		System.err.println("h4toh5image exception "+h45e2_1);
		System.err.println("Test 2: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		HDFLibrary.GRendaccess(image_id);
	} catch (HDFException he7_1) {
		System.err.println("Test 2: FAIL after test");
		return(false);
	}

	System.out.print("Test image conversion using user name generation: ");
 	System.out.println("PASS");
	return(true);
}

}
