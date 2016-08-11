import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.util.ConnectionConfiguration;
import com.varentech.deploya.util.DatabaseConnectivity;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseConnectivityTest {

    @Test
    public void databaseCheckTest() throws SQLException {
        DatabaseConnectivity db = new DatabaseConnectivity();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        ConnectionConfiguration.setPathToDataBase(config.getString("path_to_database"));

        db.databaseCheck();

        Connection connection = ConnectionConfiguration.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        assertEquals("Entries", resultSet.getString(3));
        resultSet.close();

        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        assertEquals("Entries_Details", resultSet.getString(3));
        resultSet.close();
        connection.close();
    }

    @Test
    public void hasColumnTest() throws SQLException {
        DatabaseConnectivity db = new DatabaseConnectivity();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        ConnectionConfiguration.setPathToDataBase(config.getString("path_to_database"));

        Connection connection = ConnectionConfiguration.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()) {
            connection.close();
            String table = "Entries";
            String columnName = "time_stamp";
            assertTrue(db.hasColumn(table, columnName));
            columnName = "notThere";
            assertFalse(db.hasColumn(table, columnName));
        }

        resultSet.close();
        connection = ConnectionConfiguration.getConnection();
        metaData = connection.getMetaData();
        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if (resultSet.next()) {
            connection.close();
            String table = "Entries_Details";
            String columnName = "error";
            assertTrue(db.hasColumn(table, columnName));
            columnName = "notThere";
            assertFalse(db.hasColumn(table, columnName));
        }
        resultSet.close();
        connection.close();
    }
}