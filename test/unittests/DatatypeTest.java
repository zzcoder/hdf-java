/**
 * 
 */
package test.unittests;

import junit.framework.TestCase;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.h5.H5Datatype;

/**
 * @author rsinha
 *
 */
public class DatatypeTest extends TestCase {
	
	private Datatype[] baseTypes = null;
	private int[] classes = {Datatype.CLASS_BITFIELD, Datatype.CLASS_CHAR, Datatype.CLASS_COMPOUND, 
			Datatype.CLASS_ENUM, Datatype.CLASS_FLOAT, Datatype.CLASS_INTEGER, Datatype.CLASS_NO_CLASS,
			Datatype.CLASS_OPAQUE, Datatype.CLASS_REFERENCE, Datatype.CLASS_STRING, 
			Datatype.CLASS_VLEN};
	private int[] signs = {Datatype.SIGN_2, Datatype.SIGN_NONE, Datatype.NSGN};
	private int[] orders = {Datatype.ORDER_BE, Datatype.ORDER_LE, Datatype.ORDER_NONE, Datatype.ORDER_VAX};
	private int n_classes = 11;
	private int n_signs = 3;
	private int n_orders = 4;
	private int[] sizes = {32, 64, 8, 16};
	private String[] descriptions = {"Unknown",	"Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown", "Unknown",	"Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown", "8-bit character",	"8-bit unsigned character",
	"8-bit character", "8-bit character", "8-bit unsigned character", "8-bit character",
	"8-bit character", "8-bit unsigned character", "8-bit character", "8-bit character",
	"8-bit unsigned character", "8-bit character", "Unknown", "Unknown", "Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown",
	"Unknown", "enum (0=1  1=2  )", "enum (0=1  1=2  )", "enum (0=1  1=2  )",
	"enum (0=1  1=2  )", "enum (0=1  1=2  )", "enum (0=1  1=2  )", 
	"enum (0=1  1=2  )", "enum (0=1  1=2  )", "enum (0=1  1=2  )", "enum (0=1  1=2  )", 
	"enum (0=1  1=2  )", "enum (0=1  1=2  )", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit floating-point",
	"32-bit floating-point", "32-bit floating-point", "32-bit integer",
	"32-bit integer", "64-bit integer", "32-bit integer", "32-bit unsigned integer",
	"32-bit unsigned integer", "64-bit unsigned integer", "32-bit unsigned integer",
	"32-bit integer", "32-bit integer", "64-bit integer", "32-bit integer", "32-bit integer",
	"32-bit integer", "64-bit integer", "32-bit integer", "32-bit unsigned integer",
	"32-bit unsigned integer", "64-bit unsigned integer", "32-bit unsigned integer",
	"32-bit integer", "32-bit integer", "64-bit integer", "32-bit integer", "32-bit integer",
	"32-bit integer", "64-bit integer", "32-bit integer", "32-bit unsigned integer",
	"32-bit unsigned integer", "64-bit unsigned integer", "32-bit unsigned integer",
	"32-bit integer", "32-bit integer", "64-bit integer", "32-bit integer", "32-bit integer",
	"32-bit integer", "64-bit integer", "32-bit integer", "32-bit unsigned integer",
	"32-bit unsigned integer", "64-bit unsigned integer", "32-bit unsigned integer",
	"32-bit integer", "32-bit integer", "64-bit integer", "32-bit integer",
	"Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Object reference", "Object reference",
	"Object reference", "Object reference", "Object reference", "Object reference", "Object reference",
	"Object reference", "Object reference", "Object reference", "Object reference", "Object reference",
	"String, length = 32", "String, length = 32", "String, length = 32", "String, length = 32",
	"String, length = 32", "String, length = 32", "String, length = 32", "String, length = 32",
	"String, length = 32", "String, length = 32", "String, length = 32", "String, length = 32",
	"Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown",
	"Unknown", "Unknown", "Unknown"};
	
	/**
	 * @param arg0
	 */
	public DatatypeTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		baseTypes = new Datatype[n_orders*n_signs*(n_classes+4)];
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						baseTypes[counter++] = new H5Datatype(classes[i], sizes[l], 
								orders[j], signs[k]);
						assertNotNull(baseTypes[i]);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getDatatypeClass()}.
	 */
	public final void testGetDatatypeClass() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						if (baseTypes[counter++].getDatatypeClass() != classes[i])
							fail("getDatatypeClass() failed for Class" + classes[i]);
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getDatatypeSize()}.
	 */
	public final void testGetDatatypeSize() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						if (baseTypes[counter++].getDatatypeSize() != sizes[l])
							fail("getDatatypeClass() failed for size" + sizes[l]);
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getDatatypeOrder()}.
	 */
	public final void testGetDatatypeOrder() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						if (baseTypes[counter++].getDatatypeOrder() != orders[j])
							fail("getDatatypeOrder() failed for Order" + orders[j]);
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getDatatypeSign()}.
	 */
	public final void testGetDatatypeSign() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						if (baseTypes[counter++].getDatatypeSign() != signs[k])
							fail("getDatatypeSigns() failed for Order" + signs[k]);
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#setEnumMembers(java.lang.String)}.
	 */
	public final void testSetEnumMembers() {
		Datatype ed = new H5Datatype(Datatype.CLASS_ENUM, 2, Datatype.ORDER_NONE, Datatype.NSGN);
		ed.setEnumMembers("low=20, high=40");
		if (!ed.getEnumMembers().equals("low=20, high=40"))
			fail("setEnumMembers() or getEnumMembers() failed\n");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getEnumMembers()}.
	 */
	public final void testGetEnumMembers() {
		testSetEnumMembers();
	}

/*	*//** ABSTRACT METHOD.
	 * Test method for {@link ncsa.hdf.object.Datatype#toNative()}.
	 *//*
	public final void testToNative() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						int dt = baseTypes[counter++].toNative();
						if (dt != )
					}
				}
			}
		}
		
		fail("Not yet implemented"); // TODO
	}*/

	/** ABSTRACT METHOD.
	 * Test method for {@link ncsa.hdf.object.Datatype#fromNative(int)}.
	 *//*
	public final void testFromNative() {
		fail("Not yet implemented"); // TODO
	}
*/
	/**
	 * Test method for {@link ncsa.hdf.object.Datatype#getDatatypeDescription()}.
	 * RISHI SINHA - THE METHOD CALLED IS ONE FOR H5 WHICH OVERRIDES THE BASE CALL.
	 */
	public final void testGetDatatypeDescription() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						if (!baseTypes[counter].getDatatypeDescription().equals(descriptions[counter]))
							fail("getDatatypeDescription() fails on " + descriptions[counter] + " + " + baseTypes[counter].getDatatypeDescription() + " + " + counter);
						counter++;
					}
				}
			}
		}
	}
	/** ABSTRACT METHOD
	 * Test method for {@link ncsa.hdf.object.Datatype#isUnsigned()}.
	 */
	public final void testIsUnsigned() {
		int counter = 0;
		for (int i = 0; i < n_classes; i++) {
			for (int j = 0; j < n_orders; j++) {
				for (int k = 0; k < n_signs; k++) {
					int n_sizes;
					switch(classes[i]) {
					case Datatype.CLASS_INTEGER:
						n_sizes = 4;
						break;
					case Datatype.CLASS_FLOAT:
						n_sizes = 2;
						break;
					default:
						n_sizes = 1;
						break;
					}
					for (int l = 0; l < n_sizes; l++) {
						 boolean isSigned = baseTypes[counter++].isUnsigned();
						 if (isSigned && (signs[k] != Datatype.SIGN_NONE))
							 fail("isUnsigned Failed.");
					}
				}
			}
		}
	}
}
