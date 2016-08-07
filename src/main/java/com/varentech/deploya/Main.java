package com.varentech.deploya;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import com.varentech.deploya.form.FormServlet;
import com.varentech.deploya.form.LoginServlet;
import com.varentech.deploya.util.ConfigurationFileManipulation;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * This class contains the main method that will connect to the server.
 */

public class Main {
    // Resource path pointing to where the WEBROOT is.
    private static final String WEBROOT_INDEX = "/webroot/";
    private static Logger logg = LoggerFactory.getLogger(Main.class);



    public static void main(String[] args) {
        Config config = ConfigFactory.load();
        String path_to_DB = config.getString("varentech.project.path_to_database");

        if (args.length == 0) {
            ResourceBundle resource = ResourceBundle.getBundle("config");
            int port = config.getInt("varentech.project.port_number");
            //int port = Integer.valueOf(resource.getString("port_number"));
            String context_path = config.getString("varentech.project.context_path");
            //String context_path = resource.getString("context_path");
            Main main = new Main(port,context_path);
            main.start();
            main.waitForInterrupt();
            printHelp();
        } else if (args[0].equals("-h") || args[0].equals("--help")) {
            printHelp();
        } else if (args[0].equals("--export-config") && args.length == 2 && args[1].contains(".properties")) {
            ConfigurationFileManipulation fileManip = new ConfigurationFileManipulation(args[1]);
            fileManip.exportConfigFile();
        } else if (args[0].equals("-c") && args.length == 2 && args[1].contains(".properties")) {
            ConfigurationFileManipulation fileManip = new ConfigurationFileManipulation(args[1]);
            try {
                fileManip.importConfigFile();
            } catch (IOException e) {
                logg.error("Exception while importing config file: ", e);
            }
        } else {
            System.out.println("Illegal Expression(s) given.");
            printHelp();
        }
    }

    private int port;
    private Server server;
    private URI serverURI;
    private String context_path;

    public Main(int port, String context_path) {
        this.port = port;
        this.context_path = context_path;
    }

    public void start() {
        server = new Server();
        ServerConnector connector = connector();
        server.addConnector(connector);

        URI baseUri = getWebRootResourceUri();

        // Set JSP to use Standard JavaC always
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        WebAppContext webAppContext = getWebAppContext(baseUri, getScratchDir());

        server.setHandler(webAppContext);

        // Start Server
        try {
            server.start();
        } catch (Exception e) {
            logg.error("Exception while starting server: ", e);
        }
        logg.debug("Successfully connected to the server");

        this.serverURI = getServerUri(connector);
    }

    private ServerConnector connector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        return connector;
    }

    private URI getWebRootResourceUri() {
        URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
        if (indexUri == null) {
            logg.error("Unable to find resource ", WEBROOT_INDEX);
        }
        // Points to wherever /webroot/ (the resource) is
        try {
            return indexUri.toURI();
        } catch (URISyntaxException e) {
            logg.error("URI Syntax Error: ", e);
            return null;
        }
    }

    /**
     * Establish Scratch directory for the servlet context (used by JSP compilation).
     */
    private File getScratchDir() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                logg.error("Unable to create scratch directory: ", scratchDir);
            }
        }
        return scratchDir;
    }

    /**
     * Setup the basic application "context" for this application at "/".
     * This is also known as the handler tree (in jetty speak).
     */
    private WebAppContext getWebAppContext(URI baseUri, File scratchDir) {
        WebAppContext context = new WebAppContext();
        context.setContextPath(context_path);
        context.setAttribute("javax.servlet.context.tempdir", scratchDir);
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
          ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        context.setResourceBase(baseUri.toASCIIString());
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
        context.setClassLoader(getUrlClassLoader());

        context.addServlet(jspServletHolder(), "*.jsp");
        context.addServlet(defaultServletHolder(baseUri), context_path);
        context.addServlet(FormServlet.class, "/upload");
        context.addServlet(LoginServlet.class, "/login");

        context.addServlet(defaultServletHolder(baseUri), "/");
        return context;
    }

    /**
     * Ensure the jsp engine is initialized correctly.
     */
    private List<ContainerInitializer> jspInitializers() {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }

    /**
     * Set Classloader of Context to be sane (needed for JSTL).
     * JSP requires a non-System classloader, this simply wraps the
     * embedded System classloader in a way that makes it suitable
     * for JSP to use.
     */
    private ClassLoader getUrlClassLoader() {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        return jspClassLoader;
    }

    /**
     * Create JSP Servlet (must be named "jsp").
     */
    private ServletHolder jspServletHolder() {
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.7");
        holderJsp.setInitParameter("compilerSourceVM", "1.7");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }

    /**
     * Create Default Servlet (must be named "default").
     */
    private ServletHolder defaultServletHolder(URI baseUri) {
        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
        holderDefault.setInitParameter("dirAllowed", "true");
        return holderDefault;
    }

    /**
     * Establish the Server URI.
     */
    private URI getServerUri(ServerConnector connector) {
        String scheme = "http";
        for (ConnectionFactory connectFactory : connector.getConnectionFactories()) {
            if (connectFactory.getProtocol().equals("SSL-http")) {
                scheme = "https";
            }
        }
        String host = connector.getHost();
        if (host == null) {
            host = "localhost";
        }
        int port = connector.getLocalPort();
        try {
            serverURI = new URI(String.format("%s://%s:%d/", scheme, host, port));
        } catch (URISyntaxException e) {
            logg.error("URI Syntax Exception: ", e);
        }
        return serverURI;
    }

    /**
     * Cause server to keep running until it receives a Interrupt.
     * <p>
     * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of a kill -TERM {pid} or Ctrl+C
     */
    public void waitForInterrupt() {
        try {
            server.join();
        } catch (InterruptedException e) {
            logg.error("Interrupted signal: ", e);
        }
    }

    public static void printHelp() {
        System.out.println("Options: ");
        System.out.println("\t --export-config {path/to/export/config.properties} \t to export the config file in the jar.");
        System.out.println("\t -c {path/to/config/to/use/config.properties} \t to use a config other than the default config with the jar.");
        System.out.println("Note:\t the external properties must be moved to the same working directory as the jar file and must be called \"config.properties\"");
        System.out.println("\t -h or --help \t for help");
        System.out.println("Example: ");
        System.out.println("\t java -jar path/to/deploya.jar \t opens on port 8080 with context path of \"ProjectThunder\"");
        System.out.println("\t java -jar path/to/deploya.jar --export-config path/to/export/config.conf");
        System.out.println("\t java -jar path/to/deploya.jar -c path/to/config/config.properties");
        System.out.println("\t java -jar path/to/deploya.jar -h or java -jar path/to/deploya.jar --help");
    }
}
