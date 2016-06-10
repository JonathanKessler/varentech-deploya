package com.varentech.deploya.Form;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LoginServlet{

public static class loginServlet extends HttpServlet {

    //this method runs when login submit button is clicked
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        //set the username as a session attribute
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        session.setAttribute("Username", username);

        //redirect to the main form
        //NOTE: THIS IS DIFFERENT FOR EVERYONE!!!!!
        //NEED TO CHANGE ".../TryAgain/VarenProject/..." to where my bootstrapPage.html file is.
        response.sendRedirect("http://localhost:63342/TryAgain/VarenProject/bootstrapPage.html");
        return;

    }
}
}
