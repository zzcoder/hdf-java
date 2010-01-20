package test.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { TestH5.class, TestH5Eregister.class,
        TestH5Edefault.class, TestH5E.class, TestH5Fparams.class,
        TestH5Fbasic.class, TestH5F.class, 
        TestH5Gbasic.class, TestH5G.class, TestH5Giterate.class,
        TestH5Sbasic.class, TestH5S.class, 
        TestH5Tparams.class, TestH5Tbasic.class, TestH5T.class, 
        TestH5Dparams.class, TestH5D.class
})
public class TestAll {
}
