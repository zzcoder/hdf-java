import java.io.*;
import java.util.*;
import ncsa.hdf.srb.*;

/**
**/
public class H5SRBTest {
    private final String accountInfo[] = new String[7];

    public H5SRBTest(String filename) {
        accountInfo[0] = "kagiso.hdfgroup.uiuc.edu";
        accountInfo[1] = "1247";
        accountInfo[2] = "rods";
        accountInfo[3] = "hdf5irodsdemo2008";
        accountInfo[4] = "/tempZone/home/rods";
        accountInfo[5] = "tempZone";
        accountInfo[6] = "demoResc";

        try {
            testFileList();
 	} catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

    public void testFileList() throws Exception
    {
        Vector flist = new Vector(50);

        System.out.println(H5SRB.getFileFieldSeparator());

        H5SRB.getFileList(flist);
        int n = flist.size();

        for (int i=0; i<n; i++)
            System.out.println(flist.elementAt(i));
    }

    public void testFileContent(String filename) throws Exception
    {
       H5SrbFile srbFile = new H5SrbFile(filename);

        try {
            int fid = srbFile.open();
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("\nH5SRBTest filename\n");
            System.exit(0);
         }

         H5SRBTest test = new H5SRBTest(args[0]);
    }

}


