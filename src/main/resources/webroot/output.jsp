<!DOCTYPE html>

<%@ page import="com.typesafe.config.Config" %>
<%@ page import="com.typesafe.config.ConfigFactory" %>
<%@ page import="java.io.File" %>
<%@ page import="com.varentech.deploya.form.Resource" %>
<% Resource res = new Resource();
    Config fileConf1 = ConfigFactory.parseFile(new File("application.conf"));
    Config config1 = ConfigFactory.load(fileConf1);
    String tab_name_form = config1.getString("tab_name_form");
    String page_title = config1.getString("page_title");
    String context_path = config1.getString("context_path");
    String port = config1.getString("port_number");
    if (session.getAttribute("Username") == null) {
        response.sendRedirect(request.getScheme() +"://" + request.getServerName() + ":" + port + context_path + "/login.jsp");
        return;
    }
%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%=tab_name_form%>
    </title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

</head>
<body>

<!-- creates top menu-->
<div class="container">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
                <p class="navbar-brand" href="#"><%=page_title%>
                </p>
            </div>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=port%><%=context_path%>/form.jsp">Form</a></li>
                <li><a href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=port%><%=context_path%>/history.jsp">History</a>
                </li>
            </ul>
        </div>
    </nav>

    <%if (res.entriesDetail.getOutput() != null) {%>
    <FONT COLOR="#000000"><%=res.entriesDetail.getOutput()%>
    </FONT>
    <br>
    <%
        }
        if (res.entriesDetail.getError() != null) {
    %>
    <FONT COLOR="#ff0000"><%=res.entriesDetail.getError()%>
    </FONT>
    <%}%>
</div>
</body>
</html>