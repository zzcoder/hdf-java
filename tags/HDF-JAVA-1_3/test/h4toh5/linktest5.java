
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
import ncsa.hdf.hdf5lib.exceptions.*;

public class linktest5 {

	private int file_id = -1;
	private int gr_id = -1;
	int image_id = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public linktest5() {};

static public void main( String []args ) 
{
	linktest5 it = new linktest5();
	it.setup();
	boolean r1 = it.test5();
	it.cleanup();
	//if (r1 && r2) {
		System.exit(0);
	//} else {
	//	System.exit(-1);
	//}
}

/*
 * Set up the testfiles and library interfaces.
 *
 * These are used by all the tests.
 */
public void setup(  ) 
{
	try {
		H5.H5open();
	} catch (HDF5Exception h45e1) {
		System.err.println("Setup: H5open exception "+h45e1);
	//	try {
	//		HDFLibrary.GRend(gr_id);
	//		HDFLibrary.Hclose(file_id);
//		} catch (HDFException he13) {}
	//	return(false);
		System.exit( -1);
	}
	try {
		h4toh5id= h4toh5.H4toh5open("image_lib_test.hdf",
			"image_lib_test.h5",h4toh5.H425_CLOBBER);
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
		image_id = HDFLibrary.GRselect(gr_id,0);
	} catch (HDFException he2) {
		System.err.println("GRselect exception "+he2);
		System.err.println("Test I.2: FAIL");
		System.exit( -1);
	}
	initialized = true;
}

private boolean test5() {

	try {
		HDFLibrary.GRgetnluts( image_id);

	} catch (HDFException he2) {
		System.err.println("GRselect exception "+he2);
		System.err.println("Test I.2: FAIL after");
		return(false);
	}
	try {
		HDFLibrary.GRendaccess(image_id);
	} catch (HDFException he7_1) {
		System.err.println("Test 4: FAIL after test");
		return(false);
	}
	try {
		h4toh5.H4toh5close(h4toh5id);
	} catch (H45Exception he) {
		System.err.println("Test 4: close FAIL after test");
		return(false);
	}
	System.out.println("Test 5 OK; 5-4-45-4");
	return(true);
}


/*
 * Clean up of global objects.
 */
public void cleanup()
{
	if (initialized) {
		try {
			HDFLibrary.GRend(gr_id);
			HDFLibrary.Hclose(file_id);
		} catch (HDFException he) {}
		initialized = false;
	}
}
}

