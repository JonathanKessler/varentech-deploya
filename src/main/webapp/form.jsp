<!DOCTYPE html>

<%@ page import ="java.util.ResourceBundle"%>
<% ResourceBundle resource = ResourceBundle.getBundle("config");
    String tab_name_form=resource.getString("tab_name_form");
    String page_title = resource.getString("page_title");
    String context_path = resource.getString("context_path");
    String port = resource.getString("port_number");


    if (session.getAttribute("Username") == null) {
        response.sendRedirect("http://localhost:" + port +  "/"+ context_path +"/login.jsp");
        return;
    }

%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%=tab_name_form%></title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">


</head>
<body>


<!-- creates top menu-->
<div class="container">

    <nav class="navbar navbar-inverse">

        <div class="navbar-header">
            <a class="navbar-brand" href="#"><%=page_title%></a>
        </div>
    </nav>
</div>


<!-- creates form-->
<form class="form-horizontal" id="form_members" role="form" method="POST" action="http://localhost:<%=port%>/<%=context_path%>/upload" enctype="multipart/form-data">


    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <label for="file_name">File input</label>
            <input type="file" id="file_name" name="file_name" required>
            <p class="help-block">Upload a file</p>
        </div>
    </div>


    <!--text input-->
    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <label for="path_to_destination">Desired Location</label>
            <input type="text" id="path_to_destination" name="path_to_destination" class="form-control" placeholder="Enter save location for file" required>
        </div>
    </div>


    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <label for="execute_args">Execute Command</label>
            <input type="text" id="execute_args" name="execute_args" class="form-control" placeholder="Enter execute command" required>
        </div>
    </div>

    <div class="form-group">
        <div class="col-md-4 col-md-offset-4">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="unpack_args"> Unpack Required
                </label>
            </div>
        </div>
    </div>

    <div class="form-group">
        <div class="col-md-4 col-md-offset-4">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="archive"> Archive file
                </label>
            </div>
        </div>
    </div>


    <div class="col-md-4 col-md-offset-4">
        <div class="form-group">
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
    </div>


</form>
<div class="navbar-inner navbar-fixed-bottom">
    <center> <a href="http://localhost:<%=port%>/<%=context_path%>/history.jsp">Click to see history</a> </center>
</div>
</body>
</html>