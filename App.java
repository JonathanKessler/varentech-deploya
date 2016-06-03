package jettyjerseytutorial;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

public class App 
{
    public static void main( String[] args )
    {
        App app = new App();



        ResourceConfig config = new ResourceConfig();
        config.packages("jettyjerseytutorial");
        ServletHolder servlet;
        servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(2222);
        ServletContextHandler context;
        context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet,"/*");
//TODO try to make a todo
        try{
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            server.destroy();
        }

    }


}
