import com.varentech.deploya.util.ConfigurationFileManipulation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class ConfigurationFileManipulationTest {

    @Test
    public void exportConfigFileTest(){
        String path = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "application.conf";
        ConfigurationFileManipulation conf = new ConfigurationFileManipulation(path);
        conf.exportConfigFile();

        File file = new File(path);
        assertNotEquals(0,file.length());
    }
}
