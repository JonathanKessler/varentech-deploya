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
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
    <script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/select/1.2.0/js/dataTables.select.min.js"></script>
    <link rel="stylesheet" href="http://cdn.datatables.net/1.2.0/css/select.dataTables.min.css">
    <script type="text/javascript" src="https://cdn.datatables.net/buttons/1.2.1/js/dataTables.buttons.min.js"></script>
    <link rel="stylesheet" href="http://cdn.datatables.net/buttons/1.2.1/css/buttons.dataTables.min.css">

</head>
<body>

<div class="container">

    <nav class="navbar navbar-inverse">

        <div class="navbar-header">
            <a class="navbar-brand"><%=page_title%>
            </a>
        </div>
    </nav>
</div>

<script>var refid = -1;
var table2;
var table3;
var table4;
var table5;</script>

<div class="container">
    <ul class="nav nav-tabs" id="myTab">
        <li class="active"><a id="entry" data-toggle="tab" href="#entries">Entries</a></li>
        <li><a id="detail" data-toggle="tab" href="#entriesDetails">EntriesDetails</a></li>
        <li><a id="comparing" data-toggle="tab" href="#compare" class="hidden">Compare</a></li>
        <li><a id="compareFile" data-toggle="tab" href="#compareByFile" class="hidden">CompareFile</a></li>
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

                <tfoot>
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

                <tbody>

                <script>
                    $(document).ready(function () {
                        $("a").on('click', function (event) {
                            //$('#myTab a[href="#entries"]').tab('hide');
                            $('#myTab a[href="#entriesDetails"]').tab('show');
                            refid = $(this).attr("href").substring(1);
                            if (!isNaN(refid)) {
                                table2.fnFilter("^" + refid + "$", 1, true);
                            }
                        });

                    });
                </script>

                <%
                    Class.forName("org.sqlite.JDBC");
                    Connection connection = ConnectionConfiguration.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Entries ORDER BY id DESC");
                    while (resultSet.next()) {
                        String ref = resultSet.getString(1);
                %>

                <tr id="<%=ref%>">
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
                    table = $('#table1').DataTable({
                        "order": [[0, "desc"]],
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                        select: 'multi',
                        dom: 'Bfrtip',
                        buttons: [
                            {
                                extend: 'selected',
                                text: 'Compare Output',
                                action: function (e, dt, node, config) {
                                    var rowData = dt.rows({selected: true}).data();
                                    if (table.rows('.selected').data().length != 2) {
                                        alert('Please select 2 runs');
                                    } else {
                                        //I WANT TO SHOW TAB HEADER WHEN CLicK ON

                                        $('#myTab a[href="#compare"]').tab('show');

                                        var compare1 = table.rows('.selected').ids().toArray()[0];
                                        var compare2 = table.rows('.selected').ids().toArray()[1];

                                        table3.columns(1).search("^" + compare1.toString() + "$", true).draw();
                                        table4.columns(1).search("^" + compare2.toString() + "$", true).draw();

                                        table3.rows({search: 'applied'}).data().each(function (value1, index) {
                                            var underscore = value1[2].lastIndexOf("_");
                                            var fileName1 = value1[2].substring(0, underscore) + value1[2].substring(underscore + 20);

                                            table4.rows({search: 'applied'}).data().each(function (value2, index) {
                                                var underscore = value2[2].lastIndexOf("_");
                                                var fileName2 = value2[2].substring(0, underscore) + value2[2].substring(underscore + 20);

                                                if (fileName1 == fileName2) {
                                                    if (value1[4] != value2[4]) {
                                                        $('#c1' + value1[0][0] + value1[0][1] + value1[0][2]).css('background-color', '#ffe680');
                                                        $('#c2' + value2[0][0] + value2[0][1] + value2[0][2]).css('background-color', '#ffe680');
                                                    } else if (value1[5] != value2[5]) {
                                                        $('#c1' + value1[0][0] + value1[0][1] + value1[0][2]).css('background-color', '#ff8c66');
                                                        $('#c2' + value2[0][0] + value2[0][1] + value2[0][2]).css('background-color', '#ff8c66');

                                                    }
                                                }
                                            });

                                        });
                                    }

                                }
                            },
                            {
                                extend: 'selected',
                                text: 'Compare Files',
                                action: function (e, dt, node, config) {
                                    if (table.rows('.selected').data().length != 2) {
                                        alert('Please select 2 runs');
                                    } else {

                                        $('#myTab a[href="#compareByFile"]').tab('show');

                                        var compare1 = table.rows('.selected').ids().toArray()[0];
                                        var compare2 = table.rows('.selected').ids().toArray()[1];


                                        table3.columns(1).search("^" + compare1.toString() + "$", true);
                                        table4.columns(1).search("^" + compare2.toString() + "$", true);

                                        var fileName1Array=[];

                                        table3.rows({search: 'applied'}).data().each(function (value1, index) {
                                            var underscore = value1[2].lastIndexOf("_");
                                            var fileName1 = value1[2].substring(0, underscore) + value1[2].substring(underscore + 20);
                                            var rownode;

                                            fileName1Array.push(fileName1);

                                            var fileName2Array = [];

                                            table4.rows({search: 'applied'}).data().each(function (value2, index) {
                                                var underscore = value2[2].lastIndexOf("_");
                                                var fileName2 = value2[2].substring(0, underscore) + value2[2].substring(underscore + 20);
                                                var flag = false;

                                                if (fileName1 == fileName2) {
                                                    flag = true;
                                                    if (value1[3] != value2[3]) {

                                                        rownode = table5.row.add([value1[2], value1[4], value1[5]]).draw().node();
                                                        $(rownode).css('background-color', '#e0ccff');
                                                    }
                                                }

                                                fileName2Array.push([fileName2,flag]);

                                            });

                                            alert(fileName1);

                                            for(var i=0;i<fileName2Array.length;i++){
                                                var checkForFile1;
                                                var file1Flag=true;

                                                checkForFile1 = $.inArray(fileName1,fileName2Array[i]);

                                                if(checkForFile1==-1){
                                                    file1Flag=false;
                                                }
                                                if(file1Flag==false){
                                                    rownode = table5.row.add([value1[2], value1[4], value1[5]]).draw().node();
                                                    $(rownode).css('background-color', '#99ff99');
                                                }
                                            }


                                        });


                                    }

                                }
                            }
                        ]
                    });
                });
            </script>
            <script>
                $(document).ready(function () {
                    // Setup - add a text input to each footer cell
                    $('#table1 tfoot th').each(function () {
                        var title = $(this).text();
                        $(this).html('<input type="text" placeholder="Search ' + title + '" />');
                    });

                    // Apply the search
                    table.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                that.search(this.value).draw();
                            }
                        });
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
                    ResultSet entriesDetailsResultSet = entriesDetailsStatement.executeQuery("SELECT * FROM Entries_Details ORDER BY id DESC");
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
                    table2 = $('#table2').DataTable({
                        "order": [[0, "desc"]],
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
                    });
                    $('#detail').on('click', function (event) {
                        table2.fnFilter("", 1);
                    });
                });
            </script>

        </div>
        <div id="compare" class="tab-pane fade">
            <div class="col-md-6">
                <table id="table3" class="table table-striped">
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
                        entriesDetailsConnection = ConnectionConfiguration.getConnection();
                        entriesDetailsStatement = entriesDetailsConnection.createStatement();
                        entriesDetailsResultSet = entriesDetailsStatement.executeQuery("SELECT * FROM Entries_Details ORDER BY id DESC");
                        while (entriesDetailsResultSet.next()) {
                            String ref = "c1" + entriesDetailsResultSet.getString(1);
                    %>
                    <tr id="<%=ref%>">
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
                        table3 = $('#table3').DataTable();
                        table3.columns([0, 3]).visible(false);
                    });


                </script>
            </div>

            <div class="col-md-6">
                <table id="table4" class="table table-striped">
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
                        entriesDetailsConnection = ConnectionConfiguration.getConnection();
                        entriesDetailsStatement = entriesDetailsConnection.createStatement();
                        entriesDetailsResultSet = entriesDetailsStatement.executeQuery("SELECT * FROM Entries_Details ORDER BY id DESC");
                        while (entriesDetailsResultSet.next()) {
                            String ref = "c2" + entriesDetailsResultSet.getString(1);
                    %>
                    <tr id="<%=ref%>">
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
                        table4 = $('#table4').DataTable();
                        table4.columns([0, 3]).visible(false);

                    });

                </script>
            </div>
        </div>
        <div id="compareByFile" class="tab-pane fade">
            <table id="table5" class="table table-striped">
                <thead>
                <tr>
                    <th>File Name:</th>
                    <th>Output:</th>
                    <th>Error:</th>
                </tr>
                </thead>
                <tbody>


                </tbody>
            </table>
            <script>
                $(document).ready(function () {
                    table5 = $('#table5').DataTable();

                });

            </script>
        </div>
    </div>
</div>

</body>
</html>
