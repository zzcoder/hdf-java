import ncsa.hdf.h4toh5lib.*;
import ncsa.hdf.h4toh5lib.exceptions.*;
import ncsa.hdf.hdflib.*;
import ncsa.hdf.hdf5lib.*;

public class sdstest {

	private int sd_id   = -1;
	private int h4toh5id = -1;
	private boolean initialized = false;

	public sdstest() {};

static public void main( String []args ) 
{
	sdstest sdt = new sdstest();
	sdt.setup();
	boolean r1 = sdt.test1();
	boolean r2 = sdt.test2();
	sdt.cleanup();
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
		sd_id = HDFLibrary.SDstart("sds_lib_test.hdf",
			HDFConstants.DFACC_READ);
	} catch (HDFException he2) {
		System.err.println("Setup: open SD interface exception: "+he2);
		System.exit( -1);
	}
	if(sd_id == -1) {
		System.err.println("Setup: cannot start SD interface: "+sd_id);
		System.exit( -1);
	}

	try {
		h4toh5id= h4toh5.H4toh5open("sds_lib_test.hdf",
			"sds_lib_test.h5");
	} catch (H45Exception h45e1) {
		System.err.println("Setup: H4toh5open exception "+h45e1);
		try {
			HDFLibrary.SDend(sd_id);
		} catch (HDFException he13) {}
		System.exit( -1);
	}
	if(h4toh5id  < 0) {
		System.err.println("Setup: error returned by H4toh5open: "+h4toh5id);
		try {
			HDFLibrary.SDend(sd_id);
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
			HDFLibrary.SDend(sd_id);
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
 *  Test batch I:  test all SDS conversion using the default name generation
 *                 for the HDF5 objects.
 */
public boolean test1(  ) 
{

	
	int sds_id = -1;
	try {
		sds_id = HDFLibrary.SDselect(sd_id,0);
	} catch (HDFException he1) {
		System.err.println("SDselect failed");
		System.err.println("Test 1: FAIL");
		return(false);
	}

	try {
		h4toh5.H4toh5sds(h4toh5id,sds_id,"/group1",null,null,1,1);
	} catch (H45Exception h45e1) {
		System.err.println("H4toh5sds exception "+h45e1);
		System.err.println("Test 1: FAIL");
		test1_cleanup();
		return(false);
	}

	try {
		HDFLibrary.SDendaccess(sds_id);
	} catch (HDFException he8_1) {
		System.err.println("Test I.8: FAIL after test");
		return(false);
	}

	System.out.print("Test SDS conversion using the default name generation: ");
 	System.out.println("PASS");
	return(true);
}

/*
 *  Test batch II:  Test SDS conversion with user-defined name for HDF5 objects
 */
public boolean test2(  ) 
{

	int sds_id = -1;
	try {
		sds_id = HDFLibrary.SDselect(sd_id,3);
	} catch (HDFException he1) {
		System.err.println("SDselect failed");
		System.err.println("Test 2: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		h4toh5.H4toh5sds(h4toh5id,sds_id,"/group1",
			"mysds","/mydimg",1,0);
	} catch (H45Exception h45e1) {
		System.err.println("H4toh5sds exception "+h45e1);
		System.err.println("Test 2: FAIL");
		test2_cleanup();
		return(false);
	}

	try {
		HDFLibrary.SDendaccess(sds_id);
	} catch (HDFException he8_1) {
		System.err.println("Test II.8: FAIL after test");
		return(false);
	}

	System.out.print("Test SDS conversion using user names: ");
 	System.out.println("PASS");
	return(true);
}

}
