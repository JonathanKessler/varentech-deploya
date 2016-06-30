package com.varentech.deploya;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;


public class Driver {

    static Logger logg = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) throws IOException {

        ResourceBundle resource = ResourceBundle.getBundle("config");
        String port = resource.getString("port_number");
        String context_path=resource.getString("context_path");

        //connect to server
        Server server = new Server(Integer.valueOf(port));
        logg.debug("Successfully connected to Server.");

        WebAppContext ctx = new WebAppContext();
        ctx.setResourceBase("src/main/webapp");
        ctx.setContextPath("/" + context_path);
        //3. Including the JSTL jars for the webapp.
        ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/[^/]*jstl.*\\.jar$");
        //4. Enabling the Annotation based configuration
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
        //5. Setting the handler and starting the Server
        server.setHandler(ctx);
        try {

            server.start();
            server.join();
        }catch (Exception e){
           logg.error("Exception while connecting to the server: ", e);
        }






    }

}
