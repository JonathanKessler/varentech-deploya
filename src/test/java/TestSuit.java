import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        ConfigurationFileManipulationTest.class,
        DatabaseConnectivityTest.class,
        EntriesDetailsDaoImplTest.class,
        FormServletTest.class,
        ProcessFileTest.class,
        SaveTempDirectoryTest.class,
        SendFileTest.class
})

public class TestSuit {

}