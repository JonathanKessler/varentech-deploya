import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import com.varentech.deploya.form.FormServlet;
import com.varentech.deploya.form.Resource;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.junit.Test;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class EntriesDetailsDaoImplTest {

    @Test
    public void createEntriesTableTest() {

        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        FormServlet formServlet = new FormServlet();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        ConnectionConfiguration.setPathToDataBase(formServlet.homeDirectory(config.getString("path_to_database")));
        Connection connection = ConnectionConfiguration.getConnection();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "Entries", null);

            if (resultSet.getString(3).equals("Entries")) {
                System.out.println("Entries exists");
                resultSet.close();
                connection.close();
            }
        } catch (SQLException e) {
            try {
                connection.close();

                impl.createEntriesTable();
                connection = ConnectionConfiguration.getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "Entries", null);

                assertEquals("Entries", resultSet.getString(3));
                resultSet.close();
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Test
    public void createEntriesDetailsTableTest() throws SQLException {
        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        FormServlet formServlet = new FormServlet();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        ConnectionConfiguration.setPathToDataBase(formServlet.homeDirectory(config.getString("path_to_database")));
        Connection connection = ConnectionConfiguration.getConnection();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "Entries_Details", null);

            if (resultSet.getString(3).equals("Entries_Details")) {
                System.out.println("Entries_Details exists");
                resultSet.close();
                connection.close();
            }
        } catch (SQLException e) {
            try {
                connection.close();

                impl.createEntriesDetailsTable();
                connection = ConnectionConfiguration.getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "Entries_Details", null);

                assertEquals("Entries_Details", resultSet.getString(3));
                resultSet.close();
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Test
    public void insertIntoEntriesTest() throws SQLException {
        Resource res = new Resource();
        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        FormServlet formServlet = new FormServlet();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        ConnectionConfiguration.setPathToDataBase(formServlet.homeDirectory(config.getString("path_to_database")));
        Connection connection = ConnectionConfiguration.getConnection();

        DatabaseMetaData metaData = connection.getMetaData();
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

            assertThat("",not(resultSet.getString(1)));
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
            connection.close();

        }
    }

    @Test
    public void insertIntoEntriesDetailsTest() throws SQLException {
        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        Resource res = new Resource();
        FormServlet formServlet = new FormServlet();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        ConnectionConfiguration.setPathToDataBase(formServlet.homeDirectory(config.getString("path_to_database")));
        Connection connection = ConnectionConfiguration.getConnection();

        DatabaseMetaData metaData = connection.getMetaData();
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

            assertThat("",not(resultSet.getString(1)));
            assertEquals(String.valueOf(id), resultSet.getString(2));
            assertEquals("file", resultSet.getString(3));
            assertEquals("hash", resultSet.getString(4));
            assertEquals("output", resultSet.getString(5));
            assertEquals("error", resultSet.getString(6));

            resultSet.close();
            statement.close();
            connection.close();
        }
    }

}
