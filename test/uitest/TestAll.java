package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
    TestHDFViewCLMultipleFiles.class,
    TestHDFViewCLRootMultipleFiles.class,
    TestHDFViewMenu.class,
    TestTreeViewNewMenu.class,
    TestTreeViewFiles.class
})

public class TestAll {
}
