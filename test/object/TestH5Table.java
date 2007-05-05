package test.object;

import java.util.Vector;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;

public class TestH5Table {
    
    public static void main(String[] args) {
        long dim1=1000, dim2=50;
        
        if (args.length > 1) 
        {
            try {
                dim1 = Long.parseLong(args[0]);
                dim2 = Long.parseLong(args[1]);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
        
        try { 
            testTable(dim1, dim2); 
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    /**
     * Test the performance of reading a small table data.
     */
    public static final void testTable(long dim1, long dim2) throws Exception 
    {
        int nObjs = 0; // number of object left open
        Dataset dset =null;
        long t0=0, t1=0, time=0, nbytes=0, readKBS=0, writeKBS=0;
        String dname = "/table";
        
        String[] COMPOUND_MEMBER_NAMES = {"int32", "float32"};
        H5Datatype[] COMPOUND_MEMBER_DATATYPES = {
            new H5Datatype(Datatype.CLASS_INTEGER, 4, -1, -1), 
            new H5Datatype(Datatype.CLASS_FLOAT, 4, -1, -1)};
        
        long DIM1 = dim1;
        long DIM2 = dim2;
        long[] DIMs = {DIM1, DIM2};
        int DIM_SIZE = (int)(DIM1*DIM2);
        
        int[] DATA_INT = new int[DIM_SIZE];
        float[] DATA_FLOAT = new float[DIM_SIZE];
        Vector DATA_COMP = new Vector(2);
        
        for (int i=0; i<DIM_SIZE; i++) {
            DATA_INT[i] = i;
            DATA_FLOAT[i] = i+i/100.0f;
        }
        DATA_COMP.add(0, DATA_INT);
        DATA_COMP.add(1, DATA_FLOAT);
        
        H5File file = new H5File("testH5Table.h5", FileFormat.CREATE);
        file.open();
        
        try {
            dset = file.createCompoundDS(dname, null, DIMs, null, null, -1, 
                    COMPOUND_MEMBER_NAMES, COMPOUND_MEMBER_DATATYPES, null, DATA_COMP);
        } catch (Exception ex) { 
            System.out.println("file.createCompoundDS() failed. "+ ex);
        }
        
        try {
            time = 0;
            nbytes = (DIM_SIZE*8L*1000000L);            
            dset = (Dataset)file.get(dname);
            dset.clearData();
            collectGarbage();
            
            // test reading
            t0 = System.nanoTime();
            try {
                dset.getData();
            } catch (Exception ex) { 
                 System.out.println("dset.getData() failed. "+ ex);
            }
            t1 = System.nanoTime();
            time = (t1-t0);
            readKBS = (nbytes)/time;

            // test writing
            t0 = System.nanoTime();
            try {
                dset.write();
            } catch (Exception ex) { 
                 System.out.println("dset.write() failed. "+ ex);
            }
            t1 = System.nanoTime();
            time = (t1-t0);
            writeKBS = (nbytes)/time;
            System.out.println("\nReading/writing a "+DIM1+"x"+DIM2+" table [KB/S]: \t"+ readKBS+"\t"+writeKBS);
            
            try {
                nObjs = H5.H5Fget_obj_count(file.getFID(), HDF5Constants.H5F_OBJ_LOCAL);
            } catch (Exception ex) { 
                 System.out.println("H5.H5Fget_obj_count() failed. "+ ex);
            }
            
            try {            
                file.close();
            } catch (Exception ex) { 
                System.out.println("file.close() failed. "+ ex);
            }
        } finally {
            // delete the testing file
            file.deleteOnExit();
        }
    }

    private static void collectGarbage() {
        try {
            System.gc();
            Thread.sleep(100);
            System.runFinalization();
            Thread.sleep(100);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
   