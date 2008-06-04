import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import ncsa.hdf.srb.*;
import ncsa.hdf.object.*;

/**
**/
public class H5SRBTest {
    public H5SRBTest(String filename) {
        try {
            testFileList();
 	} catch (Exception ex) {
            ex.printStackTrace();
        } 

        try {
            testFileContent("/tempZone/home/rods/hdf5_test.h5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void testFileList() throws Exception
    {
        Vector flist = new Vector(50);

        System.out.println(H5SRB.getFileFieldSeparator());

        System.out.println("Server information:");
        String srvInfo[] = H5SRB.getServerInfo();
        for (int i=0; i<srvInfo.length; i++) {
            System.out.println("\t"+srvInfo[i]);
        }
        System.out.println();

        H5SRB.getFileList(flist);
        int n = flist.size();

        for (int i=0; i<n; i++)
            System.out.println(flist.elementAt(i));
    }

    public void testFileContent(String filename) throws Exception
    {
       int fid = 0;
       H5SrbFile srbFile = new H5SrbFile(filename);

        try {
            fid = srbFile.open();
        } catch (Throwable err) {
            err.printStackTrace();
        }

        if (fid <=0) {
            System.out.println("Failed to open file from server: fid="+fid);
            return;
        }

        TreeNode root = srbFile.getRootNode();
        if (root == null)  {
            System.out.println("Failed to open file from server: root=nul: root=nulll");
            return;
        }

        printGroup(root, "");

    }

    private void printGroup(TreeNode pNode, String indent) {
        if (pNode == null)
            return;

        HObject obj = null;
        DefaultMutableTreeNode node = null;
        Enumeration objs = pNode.children() ;
        while(objs.hasMoreElements())
        {
            node = (DefaultMutableTreeNode) objs.nextElement();
            obj = (HObject)node.getUserObject();
            System.out.println(indent+obj);

            if (obj instanceof ScalarDS) {
                ScalarDS sds = (ScalarDS)obj;
                if ( sds.isImage())
                    System.out.println(indent+"(***This is an image***)");
                if ( sds.isTrueColor())
                    System.out.println(indent+"(***This is a true color image***)");
            }

            if (obj instanceof Group)
                 printGroup(node, indent+"\t");
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


