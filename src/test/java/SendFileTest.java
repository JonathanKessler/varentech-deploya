import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.form.Resource;
import com.varentech.deploya.form.SendFile;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class SendFileTest {

    // @Rule
    // public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void sendToDestinationTest() throws Exception {

        Resource res = new Resource();
        SendFile sendFile = new SendFile();

        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());
        long actualLength = testFile.length();

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        res.entry.setPathToDestination(tempDir.getPath());

        InputStream inputStream = new FileInputStream(testFile.getPath());
        sendFile.sendToDestination(inputStream,testFile.getName());

        File destination_file = new File(tempDir.getPath() + File.separator + testFile.getName());
        assertEquals(actualLength, destination_file.length());

    }

    @Test
    public void sendToArchiveTest() throws Exception {

        Resource res = new Resource();
        SendFile sendFile = new SendFile();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());
        long actualLength = testFile.length();

        res.entry.setFileName(testFile.getName());

        InputStream inputStream = new FileInputStream(testFile.getPath());
        sendFile.sendToArchive(inputStream);

        File archive_file = new File(config.getString("varentech.project.default_directory") + File.separator + testFile.getName());
        assertEquals(actualLength, archive_file.length());

    }

}