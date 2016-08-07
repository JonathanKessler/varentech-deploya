package com.varentech.deploya.form;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a servlet for the form page to gather information from the user.
 */
public class LoginServlet extends HttpServlet {
    private Logger logg = LoggerFactory.getLogger(LoginServlet.class);
    /**
     * This method runs when the login submit button is clicked.
     * Adds username as a session attribute.
     * Redirects to form.jsp
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        logg.debug("Successfully connected to login servlet.");
        response.setStatus(HttpServletResponse.SC_OK);
        Config config = ConfigFactory.load();
        String port = config.getString("varentech.project.port_number");
        String context_path = config.getString("varentech.project.context_path");

        //set the username as a session attribute
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        session.setAttribute("Username", username);

        //redirect to the main form
        logg.debug("Now redirecting to file upload page.");
        try {
            response.sendRedirect("http://" + request.getServerName() + ":" + port + context_path + "/form.jsp");
        } catch (IOException e) {
            logg.error("Error while redirecting to file upload: ", e);
        }
        return;
    }
}
