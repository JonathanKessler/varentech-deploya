package com.varentech.deploya.form;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        logg.debug("Successfully connected to login servlet.");
        response.setStatus(HttpServletResponse.SC_OK);
        ResourceBundle resource = ResourceBundle.getBundle("config");
        String port = resource.getString("port_number");
        String context_path = resource.getString("context_path");

        //set the username as a session attribute
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        session.setAttribute("Username", username);

        //redirect to the main form
        logg.debug("Now redirecting to file upload page.");
        response.sendRedirect("http://" + request.getServerName() + ":" + port + context_path + "/pages/form.jsp");
        return;

    }
}

