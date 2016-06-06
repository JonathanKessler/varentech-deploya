package com.varentech.deploya.Form;

import java.sql.*;

public class InsertToDatabase{

    void addToDatabase(String file_name, String path_to_destination, String execute_args, String unpack_args){

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:formData.db");
            Statement stat = conn.createStatement();
            //stat.executeUpdate("drop table if exists people;");
            //stat.executeUpdate("create table form (inputfile text, desiredLocation text, executeCommand text, unpack text);");
            System.out.println("Opened database successfully");

            stat.execute("INSERT INTO `form`(inputfile,desiredLocation,executeCommand,unpack) VALUES ('"+file_name+"','"+path_to_destination+"','"+execute_args+"','"+unpack_args+"');");

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
