package jettyjerseytutorial;
import java.sql.*;


public class Add_to_Table {

    public void alterTable(String inputfile, String desiredLocation, String execute, String unpack) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:T1.db");
            statement=connection.createStatement();
            System.out.println("Database opened successfully");
            System.out.println("You chose to upload: " + inputfile + ". " +
                    "You would like this file to be stored in: " + desiredLocation +
                    ". You would like the file to be executed as follows: " + execute +
                    ". You would like the file to unpacked as follows: " + unpack +".");

            statement.execute("INSERT INTO 'T1' (File_name,Path_destination,Unpack_arg,Exe_arg)" +
                    " VALUES ('"+ inputfile +"','"+desiredLocation+"','"+unpack+"', '"+execute+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
