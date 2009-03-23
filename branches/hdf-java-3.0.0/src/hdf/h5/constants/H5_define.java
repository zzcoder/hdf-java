package hdf.h5.constants;

public class H5_define {

	// Iteration callback values
	// (Actually, any postive value will cause the iterator to stop and pass back
	//      that positive value to the function that called the iterator)
	public static final int  H5_ITER_ERROR   = -1;
	public static final int  H5_ITER_CONT    = 0;
	public static final int  H5_ITER_STOP    = 1;

}
