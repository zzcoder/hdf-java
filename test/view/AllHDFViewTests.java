/**
 * 
 */
package test.view;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author xcao
 *
 */
public class AllHDFViewTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.view");
        //$JUnit-BEGIN$
        suite.addTestSuite(ToolsTest.class);
        //$JUnit-END$
        return suite;
    }
    
    public static void main(final String[] args) {
        
        try { TestFile.createH5File(null); } 
        catch (final Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        junit.textui.TestRunner.run(suite());
    }
    

}
