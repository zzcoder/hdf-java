package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
//    TestHDFViewCLGeometry.class,
    TestHDFViewCLMultipleFiles.class,
    TestHDFViewCLRootMultipleFiles.class,
    TestHDFViewMenu.class,
    TestTreeViewNewMenu.class,
    TestTreeViewGroups.class,
    TestTreeViewFiles.class,
    TestTreeViewExport.class,
    TestTableViewMenu.class
})

public class TestAll {
}
