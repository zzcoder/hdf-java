
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

public class loneimagetest {

	private int h4toh5id = -1;
	private boolean initialized = false;

	public loneimagetest() {};

static public void main( String []args ) 
{
	loneimagetest sdt = new loneimagetest();
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
		h4toh5id= h4toh5.H4toh5open("image_lib_lonetest.hdf",
			"image_lib_lonetest.h5",h4toh5.H425_CLOBBER);
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
	// image_id
	// vgroup_id
	// etc.
	//   VSdetach(image_id);
	//   Vdetach(vgroup_id);
	//   SDendaccess(sds_id);
	//   GRendaccess(image_id);
}

/*
 *  Test batch I:  test conversion of all images 
 */
public boolean test1(  ) 
{
	
	try {
		h4toh5.H4toh5allloneimage(h4toh5id,"/",null,
			h4toh5.H425_ALLATTRS,h4toh5.H425_PAL);
	} catch (H45Exception h45e1) {
		System.err.println("H4toh5allloneimage exception "+h45e1);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}

	System.out.print("Test conversion of all images: ");
 	System.out.println("PASS");
	return(true);
}

}
