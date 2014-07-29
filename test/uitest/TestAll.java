package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
//    TestHDFViewCLGeometry.class,
    TestHDFViewCLMultipleFiles.class,
    TestHDFViewCLRootMultipleFiles.class,
//    TestHDFViewCopyPaste.class,
//    TestHDFViewCutPaste.class,
//    TestHDFViewDatasetFrameSelection.class,
//    TestHDFViewImageFrameSelection.class,
    TestHDFViewImageConversion.class,
    TestHDFViewLibBounds.class,
//    TestHDFViewLinks.class,
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
