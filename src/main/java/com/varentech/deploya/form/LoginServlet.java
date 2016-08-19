package com.varentech.deploya.form;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a servlet for the form page to gather information from the user.
 */
public class LoginServlet extends HttpServlet {
    private static final Logger logg = LoggerFactory.getLogger(LoginServlet.class);
    /**
     * This method runs when the login submit button is clicked.
     * Adds username as a session attribute.
     * Redirects to form.jsp
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        response.setContentType("text/html");
        logg.debug("Successfully connected to login servlet.");
        response.setStatus(HttpServletResponse.SC_OK);

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        String port = config.getString("port_number");
        String context_path = config.getString("context_path");

        //set the username as a session attribute
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        session.setAttribute("Username", username);

        //redirect to the main form
        logg.debug("Now redirecting to file upload page.");
        try {
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + port + context_path + "/form.jsp");
        } catch (IOException e) {
            logg.error("Error while redirecting to file upload: ", e);
        }
        return;
    }
}
