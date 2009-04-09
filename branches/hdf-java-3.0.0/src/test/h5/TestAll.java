package test.h5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { TestH5.class,
  TestH5Eregister.class,
  TestH5Edefault.class,
  TestH5E.class,
  TestH5F.class,
  TestH5G.class,
  TestH5S.class })
public class TestAll {
}
