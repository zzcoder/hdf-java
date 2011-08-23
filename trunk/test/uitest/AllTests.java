package test.uitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { TestHDFView.class,
    TestHDFViewMenu.class
})

public class AllTests {
}
