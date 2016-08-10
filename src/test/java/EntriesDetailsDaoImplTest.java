import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import com.varentech.deploya.form.Resource;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class EntriesDetailsDaoImplTest {

    public EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
    public static Resource res = new Resource();

    public  static  Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
    public static Config config = ConfigFactory.load(fileConf);
    public static Connection connection = ConnectionConfiguration.getConnection();


    @BeforeClass
    public static void setUp() {
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));
    }

    @AfterClass
    public static void breakDown() throws SQLException {
        connection.close();
    }

    @Test
    public void createEntriesTableTest() throws SQLException {
        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (!resultSet.next()) {
            resultSet.close();
            impl.createEntriesTable();

            metaData = ConnectionConfiguration.getConnection().getMetaData();
            //check to see if Entries table exists.
            resultSet = metaData.getTables(null, null, "Entries", null);
            assertTrue(resultSet.next());
            resultSet.close();
        }
    }

    @Test
    public void createEntriesDetailsTableTest() throws SQLException {
        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries_Details table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if (!resultSet.next()) {
            impl.createEntriesDetailsTable();
            resultSet.close();

            metaData = ConnectionConfiguration.getConnection().getMetaData();
            //check to see if Entries Details table exists.
            resultSet = metaData.getTables(null, null, "Entries_Details", null);
            assertTrue(resultSet.next());
            resultSet.close();
        }
    }

   /* @Test
    public void insertColumnsIntoEntriesTest() throws SQLException {
        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));

        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()) {
            impl.insertColumnsIntoEntries();
        }

    }*/

    @Test
    public void insertIntoEntriesTest() throws SQLException {
        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        Connection connection = ConnectionConfiguration.getConnection();
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));


        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()) {
            resultSet.close();
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //format the date to have no spaces
            String formatted_time = formatter.format(date);

            //add to entry object
            res.entry.setPathToDestination("path");
            res.entry.setExecuteArguments("execute");
            res.entry.setUnpackArguments("unpack");
            res.entry.setTime(formatted_time);
            res.entry.setFileName("file");
            res.entry.setUserName("katie");
            res.entry.setArchive("archive");
            res.entry.setPathToLocalFile("local");

            impl.insertIntoEntries();

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM Entries WHERE time_stamp = " + "'" + res.entry.getTime() + "'"
            );

            assertNotEquals("", resultSet.getString(1));
            assertEquals(formatted_time, resultSet.getString(2));
            assertEquals("katie", resultSet.getString(3));
            assertEquals("file", resultSet.getString(4));
            assertEquals("local", resultSet.getString(5));
            assertEquals("path", resultSet.getString(6));
            assertEquals("unpack", resultSet.getString(7));
            assertEquals("execute", resultSet.getString(8));
            assertEquals("archive", resultSet.getString(9));

            resultSet.close();
            statement.close();
        }
    }

    @Test
    public void insertIntoEntriesDetailsTest() throws SQLException {
        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        Connection connection = ConnectionConfiguration.getConnection();
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));

        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if (resultSet.next()) {
            resultSet.close();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //format the date to have no spaces
            String formatted_time = formatter.format(date);

            res.entry.setPathToDestination("path");
            res.entry.setExecuteArguments("execute");
            res.entry.setUnpackArguments("unpack");
            res.entry.setTime(formatted_time);
            res.entry.setFileName("file");
            res.entry.setUserName("katie");
            res.entry.setArchive("archive");
            res.entry.setPathToLocalFile("local");

            impl.insertIntoEntries();

            res.entriesDetail.setError("error");
            res.entriesDetail.setFileName("file");
            res.entriesDetail.setHashValue("hash");
            res.entriesDetail.setOutput("output");

            impl.insertIntoEntriesDetail(res.entriesDetail);

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT id FROM Entries WHERE time_stamp = " + "'" + res.entry.getTime() + "'"
            );
            int id = resultSet.getInt("id");

            resultSet.close();
            statement.close();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM Entries_Details WHERE entries_id = " + "'" + id + "'"
            );

            assertNotEquals("", resultSet.getString(1));
            assertEquals(String.valueOf(id), resultSet.getString(2));
            assertEquals("file", resultSet.getString(3));
            assertEquals("hash", resultSet.getString(4));
            assertEquals("output", resultSet.getString(5));
            assertEquals("error", resultSet.getString(6));

            resultSet.close();
            statement.close();
        }
    }
}
