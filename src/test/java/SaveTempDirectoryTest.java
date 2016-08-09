import com.varentech.deploya.form.Resource;
import com.varentech.deploya.form.SaveTempDirectory;
import com.varentech.deploya.form.SendFile;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class SaveTempDirectoryTest {

   @Test
    public void directoryTest() throws IOException {
        Resource res = new Resource();
        SaveTempDirectory saveTempDirectory = new SaveTempDirectory();
        SendFile sendFile = new SendFile();

        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());
        long actualLength = testFile.length();

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        res.entry.setPathToDestination(tempDir.getPath());
        res.entry.setUnpackArguments("on");
        res.entry.setExecuteArguments("java -jar " + tempDir.getPath() + File.separator + testFile.getName());

        InputStream inputStream = new FileInputStream(testFile.getPath());
        sendFile.sendToDestination(inputStream,testFile.getName());

        saveTempDirectory.directory(testFile.getName());

        File destination_file = new File(tempDir.getPath() + File.separator + testFile.getName());

        assertEquals(actualLength, destination_file.length());
        assertEquals(3, tempDir.list().length);

    }

    @Test
    public void moveFilesTest() throws IOException {
        SaveTempDirectory saveTempDirectory = new SaveTempDirectory();
        Resource res = new Resource();

        URL url1 = this.getClass().getResource("/HelloWorldJar.jar");
        File helloWorldFile = new File(url1.getFile());
        URL url2 = this.getClass().getResource("/AnotherJar.jar");
        File anotherJarFile = new File(url2.getFile());

        File[] fileList = {helloWorldFile,anotherJarFile};

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        res.entry.setPathToDestination(tempDir.getPath());
        res.entry.setUnpackArguments("on");

        saveTempDirectory.moveFiles(fileList,helloWorldFile);

        assertEquals(tempDir.list().length, 1);
        assertEquals(tempDir.list()[0].toString(), anotherJarFile.getName());
    }
}
