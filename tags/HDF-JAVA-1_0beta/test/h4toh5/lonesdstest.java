
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

public class lonesdstest {

	private int h4toh5id = -1;
	private boolean initialized = false;

	public lonesdstest() {};

static public void main( String []args ) 
{
	lonesdstest sdt = new lonesdstest();
	sdt.setup();
	boolean r1 = sdt.test1();
	sdt.cleanup();
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
		h4toh5id= h4toh5.H4toh5open("sds_lib_lonetest.hdf",
			"sds_lib_lonetest.h5",h4toh5.H425_CLOBBER);
	} catch (H45Exception h45e1) {
		System.err.println("Setup: H4toh5open exception "+h45e1);
		System.exit( -1);
	}
	if(h4toh5id  < 0) {
		System.err.println("Setup: error returned by H4toh5open: "+h4toh5id);
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
		} catch (H45Exception h45e) {}
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
 *  Test batch I:  test all SDS conversion using the default name generation
 *                 for the HDF5 objects.
 */
public boolean test1(  ) 
{
	
	try {
		h4toh5.H4toh5alllonesds(h4toh5id,"/",null,
			h4toh5.H425_DIMSCALE,h4toh5.H425_ALLATTRS);
	} catch (H45Exception h45e1) {
		System.err.println("H4toh5alllonesds exception "+h45e1);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}

	System.out.print("Test conversion of all SDS: ");
 	System.out.println("PASS");
	return(true);
}

}
