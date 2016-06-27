package com.varentech.deploya.Form;

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
 * This class holds the Login Servlet
 * @author VarenTech
 * @see javax.servlet.ServletException
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.http.HttpServletRequest
 * @see javax.servlet.http.HttpServletResponse
 * @see javax.servlet.http.HttpSession
 */


public class LoginServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    /**
     * This method runs when the login submit button is clicked.
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        logger.info("Successfully connected to login servlet.");
        response.setStatus(HttpServletResponse.SC_OK);
        ResourceBundle resource = ResourceBundle.getBundle("config");
        String port = resource.getString("port_number");
        String context_path = resource.getString("context_path");

        //set the username as a session attribute
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        session.setAttribute("Username", username);

        //redirect to the main form

        logger.info("Now redirecting to file upload page.");
        response.sendRedirect("http://localhost:" + port + "/" + context_path + "/form.jsp");
        return;

    }
}