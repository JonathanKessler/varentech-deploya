<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.varentech.deploya.util.ConnectionConfiguration" %>

<!DOCTYPE html>
<%@ page import="java.util.ResourceBundle" %>

<% ResourceBundle resource = ResourceBundle.getBundle("config");
    String tab_name_history = resource.getString("tab_name_history");
    String page_title = resource.getString("page_title");
    String context_path = resource.getString("context_path");
    String port_number = resource.getString("port_number");

    if (session.getAttribute("Username") == null) {
        response.sendRedirect("http://" + request.getServerName() + ":" + port_number + "/" + context_path + "/login.jsp");
        return;
    }

%>
<html lang="en">
<head>
    <title><%=tab_name_history%>
    </title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
    <script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>


</head>
<body>

<div class="container">

    <nav class="navbar navbar-inverse">

        <div class="navbar-header">
            <a class="navbar-brand" href="#"><%=page_title%>
            </a>
        </div>
    </nav>
</div>

<script>var refid = -1;
var table2;</script>

<div class="container">
    <ul class="nav nav-tabs" id="myTab">
        <li class="active"><a data-toggle="tab" href="#entries">Entries</a></li>
        <li><a id="detail" data-toggle="tab" href="#entriesDetails">EntriesDetails</a></li>
    </ul>


    <div class="tab-content">
        <div id="entries" class="tab-pane fade in active">
            <table id="table1" class="table table-striped">
                <thead>
                <tr>
                    <th>ID:</th>
                    <th>Time Stamp:</th>
                    <th>User Name:</th>
                    <th>File Name:</th>
                    <th>Path to Local File:</th>
                    <th>Path to Destination:</th>
                    <th>Unpack Arguments:</th>
                    <th>Execute Arguments:</th>
                    <th>Archive:</th>
                </tr>
                </thead>

                <!--<tfoot>
                <th>ID:</th>
                <th>Time Stamp:</th>
                <th>User Name:</th>
                <th>File Name:</th>
                <th>Path to Local File:</th>
                <th>Path to Destination:</th>
                <th>Unpack Arguments:</th>
                <th>Execute Arguments:</th>
                <th>Archive:</th>
                </tfoot>
-->
                <tbody>
                <script>
                    $(document).ready(function () {
                        $("a").on('click', function (event) {
                            $('#myTab a[href="#entriesDetails"]').tab('show');
                            refid = $(this).attr("href").substring(1);

                            if (!isNaN(refid)) {
                            table2.fnFilter("^"+refid+"$", 1, true);
                            }
                        });
                    });

                </script>

                <%
                    Class.forName("org.sqlite.JDBC");
                    Connection connection = ConnectionConfiguration.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Entries order by id DESC");

                    while (resultSet.next()) {
                        String ref = resultSet.getString(1);
                %>


                <tr>
                    <td><a href="#<%=ref%>"><%=resultSet.getString(1)%>
                    </a></td>
                    <td><%= resultSet.getString(2)%>
                    </td>
                    <td><%= resultSet.getString(3)%>
                    </td>
                    <td><%= resultSet.getString(4)%>
                    </td>
                    <td><%= resultSet.getString(5)%>
                    </td>
                    <td><%= resultSet.getString(6)%>
                    </td>
                    <td><%= resultSet.getString(7)%>
                    </td>
                    <td><%= resultSet.getString(8)%>
                    </td>
                    <td><%= resultSet.getString(9)%>
                    </td>
                </tr>
                <% }
                    resultSet.close();
                    statement.close();
                    connection.close();
                %>
                </tbody>
            </table>

            <script>
                $(document).ready(function () {
                    var table = $('#table1').dataTable({
                        "order": [[0, "desc"]],
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
                    });


                });
            </script>
        </div>
        <div id="entriesDetails" class="tab-pane fade">
            <table id="table2" class="table table-striped">
                <thead>
                <tr>
                    <th>ID:</th>
                    <th>Entries Table ID:</th>
                    <th>File Name:</th>
                    <th>Hash Value:</th>
                    <th>Output:</th>
                    <th>Error:</th>
                </tr>
                </thead>
                <tbody>

                <%
                    Class.forName("org.sqlite.JDBC");
                    Connection entriesDetailsConnection = ConnectionConfiguration.getConnection();
                    Statement entriesDetailsStatement = entriesDetailsConnection.createStatement();
                    ResultSet entriesDetailsResultSet = entriesDetailsStatement.executeQuery("SELECT * FROM Entries_Details order by id DESC");
                    while (entriesDetailsResultSet.next()) {
                        String ref = entriesDetailsResultSet.getString(2);
                %>
                <tr>
                    <td><%= entriesDetailsResultSet.getString(1) %>
                        <div id=<%=ref%>></div>
                    </td>
                    <td><%= entriesDetailsResultSet.getString(2) %>
                    </td>
                    <td><%= entriesDetailsResultSet.getString(3) %>
                    </td>
                    <td><%= entriesDetailsResultSet.getString(4) %>
                    </td>
                    <td><%= entriesDetailsResultSet.getString(5) %>
                    </td>
                    <td><%= entriesDetailsResultSet.getString(6) %>
                    </td>
                </tr>
                <% }
                    entriesDetailsResultSet.close();
                    entriesDetailsStatement.close();
                    entriesDetailsConnection.close();
                %>
                </tbody>
            </table>

            <script>
                $(document).ready(function () {

                    table2 = $('#table2').dataTable({
                        "order": [[0, "desc"]],
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
                    });

                    $('#detail').on('click', function (event) {
                     table2.fnFilter("",1);
                     });


                });

            </script>
        </div>
    </div>
</div>

</body>
</html>


