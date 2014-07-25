package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
//    TestHDFViewCLGeometry.class,
    TestHDFViewCLMultipleFiles.class,
    TestHDFViewCLRootMultipleFiles.class,
    TestHDFViewDatasetFrameSelection.class,
    TestHDFViewImageConversion.class,
    TestHDFViewLibBounds.class,
    TestHDFViewMenu.class,
    TestHDFViewTAttr2.class,
    TestTreeViewNewMenu.class,
    TestTreeViewGroups.class,
    TestTreeViewFiles.class,
    TestTreeViewFilters.class,
    TestTreeViewExport.class,
    TestTableViewMenu.class,
    TestTreeViewNewVLDatatypes.class
})

public class TestAll {
}
