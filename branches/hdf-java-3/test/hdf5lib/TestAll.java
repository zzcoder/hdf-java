package test.hdf5lib;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { TestH5.class, 
        TestH5Eregister.class, 
        TestH5Edefault.class, 
        TestH5E.class, 
        TestH5Fparams.class, TestH5Fbasic.class, TestH5F.class, 
        TestH5Gbasic.class, TestH5G.class, TestH5Giterate.class,
        TestH5Sbasic.class, TestH5S.class, 
        TestH5Tparams.class, TestH5Tbasic.class, TestH5T.class, 
        TestH5Dparams.class, TestH5D.class, TestH5Dplist.class,
        TestH5Lparams.class, TestH5Lbasic.class, TestH5Lcreate.class,
        TestH5R.class, 
        TestH5P.class, TestH5PData.class, TestH5Pfapl.class,
        TestH5A.class, 
        TestH5Oparams.class, TestH5Obasic.class, TestH5Ocopy.class, TestH5Ocreate.class,
        TestH5Z.class
})

public class TestAll {
}
