package com.varentech.deploya;

import java.sql.*;

/*
This class now acts as a simple connection
 */

public class Soluton {
    public static void main(String[] args){
        System.out.println("Testing our new Maven project!");
        System.out.println("---------------SQLite "
        + " JDBC Connection Testing ---------------");

        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        }
        catch (Exception exception){
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage() );
            System.exit(0);
        }
        System.out.println("DATABASE OPENED SUCCESS");


        if(args.length != 4){
            System.out.println("Syntax: java Soluton " + "DRIVER URL UID PASSWORD");
            return;
        }
        //Loading the Driver
        try{
            Class.forName(args[0]).newInstance();
        }
        //Problem loading driver. Class does not exist?
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        try{
            connection = DriverManager.getConnection(args[1], args[2], args[3]);
            System.out.println("Connection Successful");
            //TODO: Implement queries or whatever you want your database to do here.
            queryTest(connection);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                try{
                    connection.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void queryTest(Connection connection){
        try {
            System.out.println("Creating a table in the given database...");
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE REGISTRATION " +
                    " (id INTEGER not NULL, " +
                    " first VARCHAR(225), " +
                    " last VARCHAR(225), " +
                    " age INTEGER, " +
                    " PRIMARY KEY ( id))";
            statement.executeUpdate(sql);
            System.out.println("Created table in given database...");
        }
        catch (SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch (Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }
}
