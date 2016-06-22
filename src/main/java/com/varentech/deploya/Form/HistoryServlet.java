package com.varentech.deploya.Form;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class HistoryServlet {

    public static class historyServlet extends HttpServlet {
        Resource res = new Resource();
        private final Logger logg = LoggerFactory.getLogger(FormServlet.class);


        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            logg.info("Successfully connected to history servlet");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>All Employees</title></head>");
            out.println("<body>");
            out.println("<center><h1>All Employees</h1>");
            Connection conn = null;
            Statement statement = null;
            try {
                // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                //conn = DriverManager.getConnection("jdbc:odbc:Employees");
                conn = ConnectionConfiguration.getConnection();
                statement = conn.createStatement();
                /*String orderBy = request.getParameter("sort");
                if ((orderBy == null) || orderBy.equals("")) {
                    orderBy = "SSN";
                }
                String orderByDir = request.getParameter("sortdir");
                if ((orderByDir == null) || orderByDir.equals("")) {
                    orderByDir = "asc";
                }*/
                ResultSet idMax = statement.executeQuery("select max(id) max_id from entries");
                int id2 = -1;
                if (idMax.next()) {
                    id2 = idMax.getInt("max_id");
                }

                String time_stamp;
                String username;
                String file_name = null;
                String path_to_local_file = null;
                String path_to_destination = null;
                String unpack_args = null;
                String execute_args = null;
                String archive = null;


                //for (int i=1; i<id2+1; i++){
                ResultSet line = statement.executeQuery("select * from entries where id=" + 1);

                time_stamp = line.getObject("time_stamp").toString();
                username = line.getObject("username").toString();

                if (line.getObject("file_name") != null) {
                    file_name = line.getObject("file_name").toString();
                }

                if (line.getObject("path_to_local_file") != null) {
                    path_to_local_file = line.getObject("path_to_local_file").toString();
                }

                if (line.getObject("path_to_destination") != null) {
                    path_to_destination = line.getObject("path_to_destination").toString();
                }
                if (line.getObject("unpack_args") != null) {
                    unpack_args = line.getObject("unpack_args").toString();
                }

                if (line.getObject("execute_args") != null) {
                    execute_args = line.getObject("execute_args").toString();
                }
                if (line.getObject("archive") != null) {
                    archive = line.getObject("archive").toString();
                }

                out.print(time_stamp + "::");
                out.print(username + "::");
                out.print(file_name + "::");
                out.print(path_to_local_file + "::");
                out.print(path_to_destination + "::");
                out.print(unpack_args + "::");
                out.print(execute_args + "::");
                out.print(archive + "::");
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                }
            }
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

/*
            //JSONObject json = retrieveData();

            response.setContentType("application/json");
            logg.info("Successfully connected to history servlet");

            Connection connection = ConnectionConfiguration.getConnection();

            try {
               Statement statement = connection.createStatement();

                ResultSet idMax = statement.executeQuery("select max(id) max_id from entries");
                int id2 = -1;
                if (idMax.next()) {
                    id2 = idMax.getInt("max_id");
                }

                String time_stamp;
                String username;
                String file_name=null;
                String path_to_local_file=null;
                String path_to_destination=null;
                String unpack_args=null;
                String execute_args=null;
                String archive=null;


                //for (int i=1; i<id2+1; i++){
                    ResultSet line = statement.executeQuery("select * from entries where id=" + 1);

                    time_stamp = line.getObject("time_stamp").toString();
                    username = line.getObject("username").toString();

                if(line.getObject("file_name")!=null) {
                    file_name = line.getObject("file_name").toString();
                }

                if(line.getObject("path_to_local_file")!=null) {
                    path_to_local_file = line.getObject("path_to_local_file").toString();
                }

                if(line. getObject("path_to_destination")!=null) {
                    path_to_destination = line.getObject("path_to_destination").toString();
                }
                if(line.getObject("unpack_args")!=null) {
                    unpack_args = line.getObject("unpack_args").toString();
                }

                if(line.getObject("execute_args")!=null) {
                    execute_args = line.getObject("execute_args").toString();
                }
                if(line.getObject("archive")!=null) {
                    archive = line.getObject("archive").toString();
                }


                JSONObject obj = new JSONObject();

                obj.put("time_stamp", time_stamp);
                obj.put("file_name", file_name);
                obj.put("username", username);
                obj.put("path_to_local_file", path_to_local_file);
                obj.put("path_to_destination", path_to_destination);
                obj.put("unpack", unpack_args);
                obj.put("execute_args", execute_args);
                obj.put("archive", archive);


                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(obj.toString());
                String output = gson.toJson(je);


                //String output = obj.toString();
                PrintWriter writer = response.getWriter();
                writer.write(output);
                writer.close();
                //}


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }*/
}
