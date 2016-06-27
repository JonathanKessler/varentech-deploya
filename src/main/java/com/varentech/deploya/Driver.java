package com.varentech.deploya;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


import java.io.IOException;
import java.util.ResourceBundle;

public class Driver {
    //This string should be final as it should not change. At initial run, the
    //program should be able to create this directory if it is not found.
    final static String pathToLocal = "/opt/deploya";

    public static void main(String[] args) throws IOException {

        ResourceBundle resource = ResourceBundle.getBundle("config");
        String port = resource.getString("port_number");
        String context_path=resource.getString("context_path");

        //connect to server
        Server server = new Server(Integer.valueOf(port));

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
            e.printStackTrace();
        }






    }

}