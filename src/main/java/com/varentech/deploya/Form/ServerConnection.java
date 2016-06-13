package com.varentech.deploya.Form;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a ServerConnection between servlets.
 * Specifically, the LoginServlet and the FormServlet
 * @author VarenTech
 * @see org.eclipse.jetty.server.Server
 * @see org.eclipse.jetty.servlet.ServletContextHandler
 * @see org.eclipse.jetty.servlet.ServletHolder
 * @see org.glassfish.jersey.server.ResourceConfig
 */

public class ServerConnection {

    /**
     * This method connects servlets together.
     */
     
  private Logger logger= LoggerFactory.getLogger(ServerConnection.class);
    public void connect() {
        ResourceConfig config = new ResourceConfig();
        config.packages("com/varentech/deploya");

        //create a server object
        Server server = new Server(8080);
        ServletContextHandler context;
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        //set home path
        context.setContextPath("/home");
        server.setHandler(context);

        //add servlets and paths
        context.addServlet(new ServletHolder(new LoginServlet.loginServlet()), "/login");
        context.addServlet(new ServletHolder(new FormServlet.formServlet()), "/upload");

        //connect to server
        try {
            server.start();
            logger.info("Successfully connected to server.");
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }

    }
}
