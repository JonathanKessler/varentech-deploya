package Varen;

import java.sql.*;





public class InsertToDatabase{

    void addToDatabase(String inputfile, String desiredLocation, String executeCommand, String unpack){

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:formData.db");
            Statement stat = conn.createStatement();
            //stat.executeUpdate("drop table if exists people;");
            //stat.executeUpdate("create table form (inputfile text, desiredLocation text, executeCommand text, unpack text);");
            System.out.println("Opened database successfully");

            stat.execute("INSERT INTO `form`(inputfile,desiredLocation,executeCommand,unpack) VALUES ('"+inputfile+"','"+desiredLocation+"','"+executeCommand+"','"+unpack+"');");

            conn.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

        ExecuteJar ex = new ExecuteJar();
        ex.exJar();
    }
}
