<!DOCTYPE html>
<%@ page import="com.typesafe.config.Config" %>
<%@ page import="com.typesafe.config.ConfigFactory" %>
<%@ page import="java.io.File" %>

<% Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
    Config config1 = ConfigFactory.load(fileConf);
    String tab_name_login = config1.getString("tab_name_login");
    String page_title = config1.getString("page_title");
    String context_path = config1.getString("context_path");
    String port = config1.getString("port_number");
    String default_header = config1.getString("default_header");
    String userName = request.getHeader(default_header);
    if(userName!=null){
        session.setAttribute("Username", userName);
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + port + context_path + "/form.jsp");
    }
%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%=tab_name_login%></title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

</head>
<BODY>

<div class="container">
    <nav class="navbar navbar-inverse">

        <div class="navbar-header">
            <p class="navbar-brand" href="#"><%=page_title%></p>
        </div>
    </nav>
</div>

<form class="form-horizontal" id="form_members" role="form" action="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=port%><%=context_path%>/login" method="get">

    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="Username" required>
        </div>
    </div>

    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
    </div>

</FORM>
</BODY>
</html>