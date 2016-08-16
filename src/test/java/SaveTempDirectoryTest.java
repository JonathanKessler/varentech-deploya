import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import com.varentech.deploya.form.Resource;
import com.varentech.deploya.form.SaveTempDirectory;
import com.varentech.deploya.form.SendFile;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class SaveTempDirectoryTest {

    @Test
    public void directoryTest() throws IOException, SQLException {
        Resource res = new Resource();
        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        SaveTempDirectory saveTempDirectory = new SaveTempDirectory();
        SendFile sendFile = new SendFile();
        FormServlet formServlet = new FormServlet();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        ConnectionConfiguration.setPathToDataBase(formServlet.homeDirectory(config.getString("path_to_database")));

        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());
        long actualLength = testFile.length();

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        Connection connection = ConnectionConfiguration.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if (resultSet.next()) {
            resultSet.close();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //format the date to have no spaces
            String formatted_time = formatter.format(date);

            res.entry.setPathToDestination(tempDir.getPath());
            res.entry.setExecuteArguments("java -jar " + tempDir.getPath() + File.separator + testFile.getName());
            res.entry.setUnpackArguments("on");
            res.entry.setTime(formatted_time);
            res.entry.setFileName("file");
            res.entry.setUserName("katie");
            res.entry.setArchive("archive");
            res.entry.setPathToLocalFile("local");

            connection.close();
            impl.insertIntoEntries();

            InputStream inputStream = new FileInputStream(testFile.getPath());
            sendFile.sendToDestination(inputStream, testFile.getName());

            saveTempDirectory.directory(testFile.getName());

            File destination_file = new File(tempDir.getPath() + File.separator + testFile.getName());

            assertEquals(actualLength, destination_file.length());
            assertEquals(3, tempDir.list().length);

        }
    }

    @Test
    public void moveFilesTest () throws IOException {
        SaveTempDirectory saveTempDirectory = new SaveTempDirectory();
        Resource res = new Resource();

        URL url1 = this.getClass().getResource("/HelloWorldJar.jar");
        File helloWorldFile = new File(url1.getFile());
        URL url2 = this.getClass().getResource("/AnotherJar.jar");
        File anotherJarFile = new File(url2.getFile());

        File[] fileList = {helloWorldFile, anotherJarFile};

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        res.entry.setPathToDestination(tempDir.getPath());
        res.entry.setUnpackArguments("on");

        saveTempDirectory.moveFiles(fileList, helloWorldFile);

        assertEquals(tempDir.list().length, 1);
        assertEquals(tempDir.list()[0].toString(), anotherJarFile.getName());
    }
}
