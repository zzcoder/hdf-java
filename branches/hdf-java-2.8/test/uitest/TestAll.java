package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
    TestHDFViewMenu.class,
    TestTreeViewNewMenu.class
})

public class TestAll {
}
