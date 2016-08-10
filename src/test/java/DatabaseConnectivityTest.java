import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.util.ConnectionConfiguration;
import com.varentech.deploya.util.DatabaseConnectivity;
import org.junit.Test;

import java.io.File;
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
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));

        db.databaseCheck();

        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        assertTrue(resultSet.next());
        resultSet.close();

        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        assertTrue(resultSet.next());
        resultSet.close();
    }

    @Test
    public void hasColumnTest() throws SQLException {
        DatabaseConnectivity db = new DatabaseConnectivity();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        ConnectionConfiguration.setPathToDataBase(config.getString("varentech.project.path_to_database"));

        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()) {

            String table = "Entries";
            String columnName = "time_stamp";
            assertTrue(db.hasColumn(table, columnName));
            columnName = "notThere";
            assertFalse(db.hasColumn(table, columnName));
        }

        resultSet.close();
        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if (resultSet.next()) {
            String table = "Entries_Details";
            String columnName = "error";
            assertTrue(db.hasColumn(table, columnName));
            columnName = "notThere";
            assertFalse(db.hasColumn(table, columnName));
        }
    }
}
