import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import com.varentech.deploya.form.ProcessFile;
import com.varentech.deploya.form.Resource;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ProcessFileTest {

    @Test
    public void executeArgumentsTest(){
        Resource res = new Resource();
        ProcessFile processFile = new ProcessFile();

        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());

        res.entry.setExecuteArguments("java -jar " + testFile.getAbsolutePath());

        processFile.executeArguments();

        assertEquals(res.entriesDetail.getOutput(), "Hello, world. I'm changing this!more stuff hsfdgfgsjkdf");
        assertEquals(res.entriesDetail.getError(), "");
    }

   /* @Test
    public void unpackTest() throws IOException {
        ProcessFile processFile = new ProcessFile();
        URL url = this.getClass().getResource("/HelloWorldJar.jar");
        File testFile = new File(url.getFile());

        File tempDir = File.createTempFile("temp", "");
        tempDir.delete();
        tempDir.mkdir();

        InputStream inputStream = new FileInputStream(testFile.getPath());
        OutputStream outputStream;
        outputStream = new FileOutputStream(tempDir + File.separator + testFile.getName());
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();

        File helloJar = tempDir.listFiles()[0];

        processFile.unpack(helloJar);

        assertEquals(3, tempDir.list().length);

    }

    @Test
    public void hashFilesTest() throws SQLException {
        ProcessFile processFile = new ProcessFile();
        Resource res = new Resource();
        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //format the date to have no spaces
        String formatted_time = formatter.format(date);

        res.entry.setPathToDestination("path");
        res.entry.setExecuteArguments("execute");
        res.entry.setUnpackArguments("yes");
        res.entry.setTime(formatted_time);
        res.entry.setFileName("file");
        res.entry.setUserName("katie");
        res.entry.setArchive("no");

        impl.insertIntoEntries();

        URL url1 = this.getClass().getResource("/HelloWorldJar.jar");
        File helloWorldFile = new File(url1.getFile());
        URL url2 = this.getClass().getResource("/AnotherJar.jar");
        File anotherJarFile = new File(url2.getFile());

        File[] fileList = {helloWorldFile,anotherJarFile};

        processFile.hashFiles(fileList,anotherJarFile);

        Connection connection = ConnectionConfiguration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT id FROM Entries WHERE time_stamp = " + "'" + res.entry.getTime() + "'"
        );
        int id = resultSet.getInt("id");

        resultSet.close();
        statement.close();

        statement = connection.createStatement();
        resultSet = statement.executeQuery(
                "SELECT hash_value FROM Entries_Details WHERE entries_id = " + "'" + id + "'"
        );

        assertEquals("63fc37d0b2e737cb245a22fa29e17cb8", resultSet.getString(1));

        resultSet.close();
        statement.close();

        statement = connection.createStatement();
        resultSet = statement.executeQuery(
                "SELECT COUNT(hash_value) FROM Entries_Details WHERE entries_id = " + "'" + id + "'"
        );

        assertEquals(1, resultSet.getInt(1));

        resultSet.close();
        statement.close();
        connection.close();

    }*/

}
