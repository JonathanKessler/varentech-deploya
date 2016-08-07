<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.varentech.deploya.util.ConnectionConfiguration" %>

<!DOCTYPE html>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.typesafe.config.Config" %>
<%@ page import="com.typesafe.config.ConfigFactory" %>

<% Config config1 = ConfigFactory.load();
    String tab_name_history = config1.getString("varentech.project.tab_name_history");
    String page_title = config1.getString("varentech.project.page_title");
    String context_path = config1.getString("varentech.project.context_path");
    String port_number = config1.getString("varentech.project.port_number");
    //ResourceBundle resource = ResourceBundle.getBundle("config");
    //String tab_name_history = resource.getString("tab_name_history");
    //String page_title = resource.getString("page_title");
    //String context_path = resource.getString("context_path");
    //String port_number = resource.getString("port_number");
    if (session.getAttribute("Username") == null) {
        response.sendRedirect("http://" + request.getServerName() + ":" + port_number + context_path + "/login.jsp");
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
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

    <script src="http://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script src="http://cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
    <link rel="stylesheet" href="http://cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css">


</head>
<body>

<div class="container">
    <nav class="navbar navbar-inverse">
        <div class="navbar-header">
            <p class="navbar-brand" href="#"><%=page_title%>
            </p>
        </div>
    </nav>
</div>

<script>
    var refid = -1;//stands from reference ID
    var table2;
    var table3;
    var table4;
    var table5;
    var unchange = [];
</script>

<div class="container">
    <ul class="nav nav-tabs" id="myTab">
        <li class="active"><a id="entry" data-toggle="tab" href="#entries" onclick="entriesTabClick();">Entries</a></li>
        <li><a id="detail" data-toggle="tab" href="#entriesDetails"
               onclick="entriesDetailsTabClick();">EntriesDetails</a></li>
        <li><a id="compareByOutput" data-toggle="tab" href="#compareOutput" class="hidden">CompareOutput</a></li>
        <li><a id="compareByFile" data-toggle="tab" href="#compareFile" class="hidden">CompareFile</a></li>
    </ul>

    <script>
        //adds a parameter to the url if it does not exist or replaces it if it does exist. If called with null paramValue it will delete the parameter from the url
        function replaceUrlParam(url, paramName, paramValue) {
            var pattern = new RegExp('\\b(' + paramName + '=).*?(&|$)');
            if (url.search(pattern) >= 0) {
                if (paramValue == null) {
                    var urlparts = url.split('?');
                    if (urlparts.length >= 2) {
                        var prefix = encodeURIComponent(paramName) + '=';
                        var pars = urlparts[1].split(/[&;]/g);
                        //reverse iteration as may be destructive
                        for (var i = pars.length; i-- > 0;) {
                            //idiom for string.startsWith
                            if (pars[i].lastIndexOf(prefix, 0) !== -1) {
                                pars.splice(i, 1);
                            }
                        }
                        url = urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : "");
                        window.history.pushState("", "", url.toString());
                        return url;
                    } else {
                        window.history.pushState("", "", url.toString());
                        return url;
                    }
                } else {
                    window.history.pushState("", "", (url.replace(pattern, '$1' + paramValue + '$2')).toString());
                    return url.replace(pattern, '$1' + paramValue + '$2');
                }
            }
            if (paramValue != null) {
                window.history.pushState("", "", (url + (url.indexOf('?') > 0 ? '&' : '?') + paramName + '=' + paramValue).toString());
                return url + (url.indexOf('?') > 0 ? '&' : '?') + paramName + '=' + paramValue;
            }
        }
        //clears all parameters from the url
        function clearParams(url) {
            var index = 0;
            var newURL = url;
            index = url.indexOf('?');
            if (index == -1) {
                index = url.indexOf('#');
            }
            if (index != -1) {
                newURL = url.substring(0, index);
            }
            window.history.pushState("", "", newURL);
        }
        //clears unnecessary parameters from the url when the entries tab is clicked. Adds the appropriate parameters
        function entriesTabClick() {
            table2.search("").draw();
            table3.search("").draw();
            table4.search("").draw();
            table5.search("").draw();
            for (var i = 0; i < 5; i++) {
                table2.columns(i).search("", true).draw();
                document.getElementById("t2" + i).value = "";
                if (i < 3) {
                    table5.columns(i).search("", true).draw();
                    document.getElementById("t5" + i).value = "";
                }
            }
            $('#toggle-event').bootstrapToggle('off');
            replaceUrlParam(window.location.toString(), 'tab', 'entries');
            replaceUrlParam(window.location.toString(), 'page', (table.page.info().page + 1).toString());
            replaceUrlParam(window.location.toString(), 'size', (table.page.len()).toString());
            replaceUrlParam(window.location.toString(), 'colOrder', table.order());
            replaceUrlParam(window.location.toString(), 'entryClick');
            replaceUrlParam(window.location.toString(), 'compare1');
            replaceUrlParam(window.location.toString(), 'compare2');
            replaceUrlParam(window.location.toString(), 'page3');
            replaceUrlParam(window.location.toString(), 'page4');
            replaceUrlParam(window.location.toString(), 'size3');
            replaceUrlParam(window.location.toString(), 'size4');
            replaceUrlParam(window.location.toString(), 'colOrder3');
            replaceUrlParam(window.location.toString(), 'colOrder4');
            replaceUrlParam(window.location.toString(), 'hide');
            table.columns().every(function () {
                if (this.search() != "") {
                    replaceUrlParam(window.location.toString(), 'col' + this.index(), this.search());
                } else {
                    replaceUrlParam(window.location.toString(), 'col' + this.index());
                }
            });
            if ($('#table1_filter input').val() != "") {
                replaceUrlParam(window.location.toString(), 'search', $('#table1_filter input').val());
            } else {
                replaceUrlParam(window.location.toString(), 'search');
            }
        }
        //clears unecessary parameters from the url when the entries details tab is clicked. Adds the appropriate parameters
        function entriesDetailsTabClick() {
            table2.columns(1).search("", true).draw();
            replaceUrlParam(window.location.toString(), 'tab', 'entriesDetails');
            replaceUrlParam(window.location.toString(), 'page', (table2.page.info().page + 1).toString());
            replaceUrlParam(window.location.toString(), 'size', (table2.page.len()).toString());
            replaceUrlParam(window.location.toString(), 'colOrder', table2.order());
            replaceUrlParam(window.location.toString(), 'entryClick');
            replaceUrlParam(window.location.toString(), 'compare1');
            replaceUrlParam(window.location.toString(), 'compare2');
            replaceUrlParam(window.location.toString(), 'page3');
            replaceUrlParam(window.location.toString(), 'page4');
            replaceUrlParam(window.location.toString(), 'size3');
            replaceUrlParam(window.location.toString(), 'size4');
            replaceUrlParam(window.location.toString(), 'colOrder3');
            replaceUrlParam(window.location.toString(), 'colOrder4');
            replaceUrlParam(window.location.toString(), 'hide');
            table2.columns().every(function () {
                if (this.search() != "") {
                    replaceUrlParam(window.location.toString(), 'col' + this.index(), this.search());
                } else {
                    replaceUrlParam(window.location.toString(), 'col' + this.index());
                }
            });
            if ($('#table2_filter input').val() != "") {
                replaceUrlParam(window.location.toString(), 'search', $('#table2_filter input').val());
            } else {
                replaceUrlParam(window.location.toString(), 'search');
            }
        }
        //occurs when an entry id is clicked on the entries tab. Takes you to entries details tab and filters data. Adds the appropriate parameters to the url
        function entryClick(href) {
            $('#myTab a[href="#entriesDetails"]').tab('show');
            replaceUrlParam(window.location.toString(), 'tab', 'entriesDetails');
            replaceUrlParam(window.location.toString(), 'page', (table2.page.info().page + 1).toString());
            replaceUrlParam(window.location.toString(), 'size', (table2.page.len()).toString());
            replaceUrlParam(window.location.toString(), 'colOrder', table2.order());
            refid = href;
            if (!isNaN(refid)) {
                table2.columns(1).search("^" + refid + "$", true).draw();
                replaceUrlParam(window.location.toString(), 'entryClick', refid);
            }
            if ($('#table2_filter input').val() != "") {
                replaceUrlParam(window.location.toString(), 'search', $('#table2_filter input').val());
            }
        }
    </script>

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

                <%
                    Class.forName("org.sqlite.JDBC");
                    Connection connection = ConnectionConfiguration.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Entries ORDER BY id DESC");
                    while (resultSet.next()) {
                        String ref = resultSet.getString(1);
                %>

                <tr id="<%=ref%>">
                    <td><a onclick="entryClick(<%=ref%>);"><%=resultSet.getString(1)%>
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
                //occurs when two runs are compared by output
                function compareOutput(compare1, compare2) {
                    $('#myTab a[href="#compareOutput"]').tab('show');
                    table3.columns(1).search("^" + compare1.toString() + "$", true).draw();
                    table4.columns(1).search("^" + compare2.toString() + "$", true).draw();
                    table3.rows({search: 'applied'}).data().each(function (value1, index) {
                        var underscore = value1[2].lastIndexOf("_");
                        var fileName1 = value1[2].substring(0, underscore) + value1[2].substring(underscore + 20);
                        table4.rows({search: 'applied'}).data().each(function (value2, index) {
                            var underscore = value2[2].lastIndexOf("_");
                            var fileName2 = value2[2].substring(0, underscore) + value2[2].substring(underscore + 20);
                            if (fileName1 == fileName2) {
                                if (value1[5] != value2[5]) {
                                    $('#c1' + value1[0][0] + value1[0][1] + value1[0][2]).css('background-color', '#ff8c66');//change in error in red
                                    $('#c2' + value2[0][0] + value2[0][1] + value2[0][2]).css('background-color', '#ff8c66');
                                } else if (value1[4] != value2[4]) {
                                    $('#c1' + value1[0][0] + value1[0][1] + value1[0][2]).css('background-color', '#ffe680');//change in output in yello
                                    $('#c2' + value2[0][0] + value2[0][1] + value2[0][2]).css('background-color', '#ffe680');
                                }
                            }
                        });
                    });
                }
                //occurs when two runs are compared by file
                function compareFile(compare1, compare2) {
                    unchange = [];
                    $('#myTab a[href="#compareFile"]').tab('show');
                    var run1UserName = table.row('#' + compare1).data()[2];
                    var run1FileName = table.row('#' + compare1).data()[3];
                    var run2UserName = table.row('#' + compare2).data()[2];
                    var run2FileName = table.row('#' + compare2).data()[3];
                    //$("#here").replaceWith('<h4 id="here"> Comparing run <a onclick="compare();">' + run1FileName + '</a>, ' + run1UserName + ' to run <a href="#" onclick="compare();">' + run2FileName + '</a>, ' + run2UserName + '.</h4>');
                    $("#here").replaceWith('<h4 id="here"> Comparing run ' + run1FileName + ', ' + run1UserName + ' to run ' + run2FileName + ', ' + run2UserName + '.</h4>');
                    table5.clear();
                    table2.columns(1).search("^" + compare1.toString() + "$", true);
                    var run1Array = [];
                    var run2Array = [];
                    table2.rows({search: 'applied'}).data().each(function (value, index) {
                        var underscore = value[2].lastIndexOf("_");
                        var fileName = value[2].substring(0, underscore) + value[2].substring(underscore + 20);
                        run1Array.push([value[0][0] + value[0][1] + value[0][2], fileName, value[3], value[4], value[5]]);
                    });
                    table2.columns(1).search("^" + compare2.toString() + "$", true);
                    table2.rows({search: 'applied'}).data().each(function (value, index) {
                        var underscore = value[2].lastIndexOf("_");
                        var fileName1 = value[2].substring(0, underscore) + value[2].substring(underscore + 20);
                        run2Array.push([value[0][0] + value[0][1] + value[0][2], fileName1, value[3], value[4], value[5], false]);
                    });
                    var rownode;
                    for (var i = 0; i < run1Array.length; i++) {
                        var flag = false;
                        for (var j = 0; j < run2Array.length; j++) {
                            if (run1Array[i][1] == run2Array[j][1]) {
                                run2Array[j][5] = true;
                                flag = true;
                                if (run1Array[i][2] != run2Array[j][2]) {
                                    rownode = table5.row.add([run1Array[i][1], run1Array[i][3], run1Array[i][4]]).draw().node();
                                    $(rownode).css('background-color', '#cc99ff'); //purple changed file
                                } else {
                                    rownode = table5.row.add([run1Array[i][1], run1Array[i][3], run1Array[i][4]]).draw().node();
                                    $(rownode).css('background-color', '#dadcd6'); //grey unchanged file
                                    unchange.push(rownode);
                                }
                            }
                        }
                        if (flag == false) {
                            rownode = table5.row.add([run1Array[i][1], run1Array[i][3], run1Array[i][4]]).draw().node();
                            $(rownode).css('background-color', '#99ff99'); //green new file
                        }
                    }
                    for (var k = 0; k < run2Array.length; k++) {
                        if (run2Array[k][5] == false) {
                            rownode = table5.row.add([run2Array[k][1], run2Array[k][3], run2Array[k][4]]).draw().node();
                            $(rownode).css('background-color', '#ff8c66'); //red deleted file
                        }
                    }
                }
                $(document).ready(function () {
                    table = $('#table1').DataTable({
                        "order": [[0, "desc"]],
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                        select: 'multi',
                        dom: 'lBfrtip',
                        buttons: [
                            {
                                //occurs when the compare output button is clicked
                                extend: 'selected',
                                text: 'Compare Output',
                                action: function (e, dt, node, config) {
                                    if (table.rows('.selected').data().length != 2) {
                                        alert('Please select 2 runs');
                                    } else {
                                        replaceUrlParam(window.location.toString(), 'tab', 'compareOutput');
                                        replaceUrlParam(window.location.toString(), 'page3', (table3.page.info().page + 1).toString());
                                        replaceUrlParam(window.location.toString(), 'size3', (table3.page.len()).toString());
                                        replaceUrlParam(window.location.toString(), 'page4', (table4.page.info().page + 1).toString());
                                        replaceUrlParam(window.location.toString(), 'size4', (table4.page.len()).toString());
                                        replaceUrlParam(window.location.toString(), 'colOrder3', table3.order());
                                        replaceUrlParam(window.location.toString(), 'colOrder4', table4.order());
                                        if ($('#table3_filter input').val() != "") {
                                            replaceUrlParam(window.location.toString(), 'search3', $('#table3_filter input').val());
                                        } else {
                                            replaceUrlParam(window.location.toString(), 'search3');
                                        }
                                        if ($('#table4_filter input').val() != "") {
                                            replaceUrlParam(window.location.toString(), 'search4', $('#table4_filter input').val());
                                        } else {
                                            replaceUrlParam(window.location.toString(), 'search4');
                                        }
                                        replaceUrlParam(window.location.toString(), 'size');
                                        replaceUrlParam(window.location.toString(), 'page');
                                        replaceUrlParam(window.location.toString(), 'colOrder');
                                        replaceUrlParam(window.location.toString(), 'search');
                                        for (var i = 0; i < 9; i++) {
                                            replaceUrlParam(window.location.toString(), 'col' + i);
                                        }
                                        var compare1 = table.rows('.selected').ids().toArray()[0];
                                        var compare2 = table.rows('.selected').ids().toArray()[1];
                                        replaceUrlParam(window.location.toString(), 'compare1', compare1);
                                        replaceUrlParam(window.location.toString(), 'compare2', compare2);
                                        compareOutput(compare1, compare2);
                                    }
                                }
                            },
                            {
                                //orrurs when the compare files button is clicked
                                extend: 'selected',
                                text: 'Compare Files',
                                action: function (e, dt, node, config) {
                                    if (table.rows('.selected').data().length != 2) {
                                        alert('Please select 2 runs');
                                    } else {
                                        replaceUrlParam(window.location.toString(), 'tab', 'compareFile');
                                        replaceUrlParam(window.location.toString(), 'page', (table5.page.info().page + 1).toString());
                                        replaceUrlParam(window.location.toString(), 'size', (table5.page.len()).toString());
                                        replaceUrlParam(window.location.toString(), 'colOrder', table5.order());
                                        replaceUrlParam(window.location.toString(), 'hide', $('#toggle-event').prop('checked'));
                                        if ($('#table5_filter input').val() != "") {
                                            replaceUrlParam(window.location.toString(), 'search', $('#table5_filter input').val());
                                        } else {
                                            replaceUrlParam(window.location.toString(), 'search');
                                        }
                                        for (var i = 0; i < 9; i++) {
                                            replaceUrlParam(window.location.toString(), 'col' + i);
                                        }
                                        var compare1 = table.rows('.selected').ids().toArray()[0];
                                        var compare2 = table.rows('.selected').ids().toArray()[1];
                                        replaceUrlParam(window.location.toString(), 'compare1', compare1);
                                        replaceUrlParam(window.location.toString(), 'compare2', compare2);
                                        compareFile(compare1, compare2);
                                    }
                                }
                            }
                        ]
                    });
                    //detects a change in page number
                    table.on('page', function () {
                        replaceUrlParam(window.location.toString(), 'page', (table.page.info().page + 1).toString());
                    });
                    //detects a change in table length
                    table.on('length.dt', function (e, settings, len) {
                        replaceUrlParam(window.location.toString(), 'size', (len).toString());
                        replaceUrlParam(window.location.toString(), 'page', (table.page.info().page + 1).toString());
                    });
                    //detects a change in column order
                    table.on('order.dt', function () {
                        replaceUrlParam(window.location.toString(), 'colOrder', table.order());
                        replaceUrlParam(window.location.toString(), 'page', (table.page.info().page + 1).toString());
                    });
                    //detects a change in table search value
                    $('#table1_filter input').unbind().keyup(function () {
                        var value = $(this).val();
                        table.search(value).draw();
                        if (value != "") {
                            replaceUrlParam(window.location.toString(), 'search', value.toString());
                        } else {
                            replaceUrlParam(window.location.toString(), 'search');
                        }
                    });
                    // Setup - add a text input to each footer cell
                    $('#table1 tfoot th').each(function () {
                        var title = $(this).text();
                        if ($(this).index() != 1) {
                            $(this).html('<input type="text" placeholder="Search ' + title + '" id="t1' + $(this).index() + '" />');
                        } else {
                            //$(this).html('<div class="col-md-1"><input type="text" placeholder="To" id="to"/></div><div class="col-md-1"><input type="text" placeholder="From" id="from"/></div> ');
                            //$(this).html('<input type="text" placeholder="Start Date" id="start"/><input type="text" placeholder="End Date" id="end"/> ');
                            $(this).html('<input type="text" placeholder="Select Dates" id="date_range" />');
                        }
                    });

                    $("#date_range").daterangepicker({
                        autoUpdateInput: false,
                        locale: {
                            "cancelLabel": "Clear"
                        }
                    });

                    $("#date_range").on('apply.daterangepicker', function(ev, picker) {
                        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' to ' + picker.endDate.format('YYYY-MM-DD'));
                        table.draw();
                    });

                    $("#date_range").on('cancel.daterangepicker', function(ev, picker) {
                        $(this).val('');
                        table.draw();
                    });
                    // Date range script - END of the script

                    $.fn.dataTableExt.afnFiltering.push(
                            function( oSettings, aData, dataIndex ) {

                                var grab_daterange = $("#date_range").val();
                                var give_results_daterange = grab_daterange.split(" to ");
                                var filterstart = give_results_daterange[0]; //where we want results to begin
                                var filterend = give_results_daterange[1]; //where we want results to end

                                var iStartDateCol = 1;
                                var iEndDateCol = 1;
                                var tabledatestart = aData[iStartDateCol];
                                var tabledateend= aData[iEndDateCol];

                                if ( !filterstart && !filterend ) {

                                    return true;
                                } else if ((moment(filterstart).isSame(tabledatestart) || moment(filterstart).isBefore(tabledatestart)) && filterend === "") {
                                    return true;
                                } else if ((moment(filterstart).isSame(tabledatestart) || moment(filterstart).isAfter(tabledatestart)) && filterstart === "") {
                                    return true;
                                } else if ((moment(filterstart).isSame(tabledatestart) || moment(filterstart).isBefore(tabledatestart)) && (moment(filterend).isSame(tabledateend) || moment(filterend).isAfter(tabledateend))) {
                                    return true;
                                }
                                return false;
                            }
                    );


                    /*
                     var start = "";
                     var end = "";
                     $("#start").datepicker({dateFormat: 'yy-mm-dd'}).on("change", function () {
                     start = $(this).val();
                     dateRange(start, end);
                     });
                     $("#end").datepicker({dateFormat: 'yy-mm-dd'}).on("change", function () {
                     end = $(this).val();
                     dateRange(start, end);
                     });
                     function dateRange(start, end) {
                     var startInt = start.substring(0, 4) + start.substring(5, 7) + start.substring(8);
                     var endInt = end.substring(0, 4) + end.substring(5, 7) + end.substring(8);

                     if (startInt > endInt && start != "" && end != "") {
                     alert("Please select start date before end date.");
                     } else {
                     table.search(start);
                     var startSearch = table.rows({search:"applied"}).data();
                     alert(startSearch[startSearch.length-1].);

                     table.rows().each(function () {
                     if(this.data[0]>
                     });
                     }

                     }*/

                    // Apply the column search
                    table.columns().every(function () {
                        var that = this;
                        if (that.index() != 1) {
                            $('input', this.footer()).on('keyup change', function () {
                                if (that.search() !== this.value) {
                                    replaceUrlParam(window.location.toString(), 'col' + that.index(), this.value);
                                    that.search(this.value).draw();
                                    if (that.search() == "") {
                                        replaceUrlParam(window.location.toString(), 'col' + that.index());
                                    }
                                }
                            });
                        }
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

                <tfoot>
                    <th>ID:</th>
                    <th>Entries Table ID:</th>
                    <th>File Name:</th>
                    <th>Hash Value:</th>
                    <th>Output:</th>
                    <th>Error:</th>
                </tfoot>
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
                    //detects a change in page number
                    table2.on('page', function () {
                        replaceUrlParam(window.location.toString(), 'page', (table2.page.info().page + 1).toString());
                    });
                    //detects a change in table length
                    table2.on('length.dt', function (e, settings, len) {
                        replaceUrlParam(window.location.toString(), 'size', (len).toString());
                        replaceUrlParam(window.location.toString(), 'page', (table2.page.info().page + 1).toString());
                    });
                    //detects a change in column order
                    table2.on('order.dt', function () {
                        replaceUrlParam(window.location.toString(), 'colOrder', table2.order());
                        replaceUrlParam(window.location.toString(), 'page', (table2.page.info().page + 1).toString());
                    });
                    //detects a change in table search
                    $('#table2_filter input').unbind().keyup(function () {
                        var value = $(this).val();
                        table2.search(value).draw();
                        if (value != "") {
                            replaceUrlParam(window.location.toString(), 'search', value.toString());
                        } else {
                            replaceUrlParam(window.location.toString(), 'search');
                        }
                    });
                });
            </script>
            <script>
                $(document).ready(function () {
                    // Setup - add a text input to each footer cell
                    $('#table2 tfoot th').each(function () {
                        var title = $(this).text();
                        $(this).html('<input type="text" placeholder="Search ' + title + '" id="t2' + $(this).index() + '" />');
                    });
                    // Apply the column search
                    table2.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                replaceUrlParam(window.location.toString(), 'col' + that.index(), this.value);
                                that.search(this.value).draw();
                                if (that.search() == "") {
                                    replaceUrlParam(window.location.toString(), 'col' + that.index());
                                }
                            }
                        });
                    });
                });
            </script>

        </div>
        <div id="compareOutput" class="tab-pane fade">
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
                        //detects a change in page number
                        table3.on('page', function () {
                            replaceUrlParam(window.location.toString(), 'page3', (table3.page.info().page + 1).toString());
                        });
                        //detects a change in table length
                        table3.on('length.dt', function (e, settings, len) {
                            replaceUrlParam(window.location.toString(), 'size3', (len).toString());
                            replaceUrlParam(window.location.toString(), 'page3', (table3.page.info().page + 1).toString());
                        });
                        //detects a change in column order
                        table3.on('order.dt', function () {
                            replaceUrlParam(window.location.toString(), 'colOrder3', table3.order());
                            replaceUrlParam(window.location.toString(), 'page3', (table3.page.info().page + 1).toString());
                        });
                        //detects a change in table search value
                        $('#table3_filter input').unbind().keyup(function () {
                            var value = $(this).val();
                            table3.search(value).draw();
                            if (value != "") {
                                replaceUrlParam(window.location.toString(), 'search3', value.toString());
                            } else {
                                replaceUrlParam(window.location.toString(), 'search3');
                            }
                        });
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
                        //detects a change in page number
                        table4.on('page', function () {
                            replaceUrlParam(window.location.toString(), 'page4', (table4.page.info().page + 1).toString());
                        });
                        //detects a change in page length
                        table4.on('length.dt', function (e, settings, len) {
                            replaceUrlParam(window.location.toString(), 'size4', (len).toString());
                            replaceUrlParam(window.location.toString(), 'page4', (table4.page.info().page + 1).toString());
                        });
                        //detects a change in column order
                        table4.on('order.dt', function () {
                            replaceUrlParam(window.location.toString(), 'colOrder4', table4.order());
                            replaceUrlParam(window.location.toString(), 'page4', (table4.page.info().page + 1).toString());
                        });
                        //detects a change in search value
                        $('#table4_filter input').unbind().keyup(function () {
                            var value = $(this).val();
                            table4.search(value).draw();
                            if (value != "") {
                                replaceUrlParam(window.location.toString(), 'search4', value.toString());
                            } else {
                                replaceUrlParam(window.location.toString(), 'search4');
                            }
                        });
                    });
                </script>
            </div>
        </div>
        <div id="compareFile" class="tab-pane fade">
            <div class="row">
                <div class="col-sm-10">
                    <!-- this is where the header with the two compare links will be -->
                    <h4 id="here"></h4>

                </div>
                <div class="col-sm-2" id="hide">
                    <!--creates the hide unchanged files toggle button-->
                    <input id="toggle-event" type="checkbox" data-toggle="toggle" data-on="Show Unchanged Files"
                           data-off="Hide Unchanged Files" data-width="175">
                </div>
            </div>
            <div class="row">
                <table id="table5" class="table table-striped">
                    <thead>
                    <tr>
                        <th>File Name:</th>
                        <th>Output:</th>
                        <th>Error:</th>
                    </tr>
                    </thead>
                    <tfoot>
                        <th>File Name:</th>
                        <th>Output:</th>
                        <th>Error:</th>
                    </tfoot>

                    <tbody>
                    </tbody>
                </table>
                <script>
                    $(document).ready(function () {
                        table5 = $('#table5').DataTable();
                        //detects a change in page number
                        table5.on('page', function () {
                            replaceUrlParam(window.location.toString(), 'page', (table5.page.info().page + 1).toString());
                        });
                        //detects a change in table length
                        table5.on('length.dt', function (e, settings, len) {
                            replaceUrlParam(window.location.toString(), 'size', (len).toString());
                            replaceUrlParam(window.location.toString(), 'page', (table5.page.info().page + 1).toString());
                        });
                        //detects a change in column order
                        table5.on('order.dt', function () {
                            replaceUrlParam(window.location.toString(), 'colOrder', table5.order());
                            replaceUrlParam(window.location.toString(), 'page', (table5.page.info().page + 1).toString());
                        });
                        //detects a change in table search value
                        $('#table5_filter input').unbind().keyup(function () {
                            var value = $(this).val();
                            table5.search(value).draw();
                            if (value != "") {
                                replaceUrlParam(window.location.toString(), 'search', value.toString());
                            } else {
                                replaceUrlParam(window.location.toString(), 'search');
                            }
                        });
                        // Setup - add a text input to each footer cell
                        $('#table5 tfoot th').each(function () {
                            var title = $(this).text();
                            $(this).html('<input type="text" placeholder="Search ' + title + '" id="t5' + $(this).index() + '"  />');
                        });
                        // Apply the column search
                        table5.columns().every(function () {
                            var that = this;
                            $('input', this.footer()).on('keyup change', function () {
                                if (that.search() !== this.value) {
                                    that.search(this.value).draw();
                                    replaceUrlParam(window.location.toString(), 'col' + that.index(), this.value);
                                    if (that.search() == "") {
                                        replaceUrlParam(window.location.toString(), 'col' + that.index());
                                    }
                                }
                            });
                        });
                    });
                </script>

                <script>
                    <!--function to hide unchanged files based on toggle bar-->
                    $('#toggle-event').change(function () {
                        replaceUrlParam(window.location.toString(), 'hide', $('#toggle-event').prop('checked'));
                        hideUnchanged();
                    });
                    function hideUnchanged() {
                        var state = $('#toggle-event').prop('checked');
                        for (var i = 0; i < unchange.length; i++) {
                            if (state == true) {
                                unchange[i].style.display = 'none';
                            } else {
                                unchange[i].style.display = '';
                            }
                        }
                    }
                </script>
            </div>
        </div>

        <script>
            //parses through search parameters in url and creates two arrays. (names holds the names of the parameters and values holds the vaules of the parameters
            function parseSearchParameters() {
                var search = window.location.search.toString();
                var index;
                var current;
                var name;
                var value;
                var names = [];
                var values = [];
                if (search.length == 0) {
                    entriesTabClick();
                }
                while (search.length != 0) {
                    search = search.substring(1);
                    index = search.indexOf('&');
                    if (search.indexOf('&') != -1) {
                        current = search.substring(0, index);
                        search = search.substring(index);
                    } else {
                        current = search;
                        search = "";
                    }
                    var split = current.indexOf('=');
                    name = current.substring(0, split);
                    names.push(name);
                    value = current.substring(split + 1);
                    values.push(value);
                }
                var tab = values[names.indexOf('tab')];
                if (tab == 'entries') {
                    entriesOnLoad(names, values);
                } else if (tab == 'entriesDetails') {
                    entriesDetailsOnLoad(names, values);
                } else if (tab == 'compareFile') {
                    compareFileOnLoad(names, values);
                } else if (tab == 'compareOutput') {
                    compareOutputOnLoad(names, values);
                }
            }
            //occurs if the tab being loaded is the entries tab
            function entriesOnLoad(names, values) {
                $('#myTab a[href="#entries"]').tab('show');
                if (names.indexOf('size') != -1) {
                    var size = values[names.indexOf('size')];
                }
                var searchBar = "";
                var colOrder = "";
                var page = "";
                if (names.indexOf('page') != -1) {
                    page = values[names.indexOf('page')] - 1;
                }
                if (names.indexOf('search') != -1) {
                    searchBar = values[names.indexOf('search')];
                }
                if (names.indexOf('colOrder') != -1) {
                    colOrder = values[names.indexOf('colOrder')].toString();
                    split = colOrder.indexOf(',');
                    var colNumber = colOrder.substring(0, split);
                    var col = colOrder.substring(split + 1).toString();
                }
                if (size != "") {
                    table.page.len(size).draw();
                }
                if (searchBar != "") {
                    table.search(searchBar).draw();
                }
                if (colOrder != "") {
                    table.order([colNumber, col]).draw();
                }
                for (var i = 0; i < 8; i++) {
                    if (names.indexOf('col' + i) != -1) {
                        document.getElementById("t1" + i).value = values[names.indexOf('col' + i)];
                        table.column(i).search(values[names.indexOf('col' + i)]).draw();
                    }
                }
                if (page != "") {
                    table.page(page).draw('page');
                }
            }
            //occurs when the EntriesDetails tab is loaded
            function entriesDetailsOnLoad(names, values) {
                $('#myTab a[href="#entriesDetails"]').tab('show');
                //entry click
                if (names.indexOf('entryClick') != -1) {
                    entryClick(values[names.indexOf('entryClick')]);
                }
                if (names.indexOf('size') != -1) {
                    var size = values[names.indexOf('size')];
                }
                var searchBar = "";
                var colOrder = "";
                var page = "";
                if (names.indexOf('page') != -1) {
                    page = values[names.indexOf('page')] - 1;
                }
                if (names.indexOf('search') != -1) {
                    searchBar = values[names.indexOf('search')];
                }
                if (names.indexOf('colOrder') != -1) {
                    colOrder = values[names.indexOf('colOrder')].toString();
                    split = colOrder.indexOf(',');
                    var colNumber = colOrder.substring(0, split);
                    var col = colOrder.substring(split + 1).toString();
                }
                if (size != "") {
                    table2.page.len(size).draw();
                }
                if (searchBar != "") {
                    table2.search(searchBar).draw();
                }
                if (colOrder != "") {
                    table2.order([colNumber, col]).draw();
                }
                for (var i = 0; i < 8; i++) {
                    if (names.indexOf('col' + i) != -1) {
                        document.getElementById("t2" + i).value = values[names.indexOf('col' + i)];
                        table2.column(i).search(values[names.indexOf('col' + i)]).draw();
                    }
                }
                if (page != "") {
                    table2.page(page).draw('page');
                }
            }
            //occurs when the compare file tab is loaded
            function compareFileOnLoad(names, values) {
                var compare1 = values[names.indexOf('compare1')];
                var compare2 = values[names.indexOf('compare2')];
                compareFile(compare1, compare2);
                var hide = values[names.indexOf('hide')];
                if (names.indexOf('size') != -1) {
                    var size = values[names.indexOf('size')];
                }
                var searchBar = "";
                var colOrder = "";
                var page = "";
                if (names.indexOf('page') != -1) {
                    page = values[names.indexOf('page')] - 1;
                }
                if (names.indexOf('search') != -1) {
                    searchBar = values[names.indexOf('search')];
                }
                if (names.indexOf('colOrder') != -1) {
                    colOrder = values[names.indexOf('colOrder')].toString();
                    split = colOrder.indexOf(',');
                    var colNumber = colOrder.substring(0, split);
                    var col = colOrder.substring(split + 1).toString();
                }
                if (hide == 'true') {
                    $('#toggle-event').bootstrapToggle('on');
                } else {
                    $('#toggle-event').bootstrapToggle('off');
                }
                hideUnchanged();
                if (size != "") {
                    table5.page.len(size).draw();
                }
                if (searchBar != "") {
                    table5.search(searchBar).draw();
                }
                if (colOrder != "") {
                    table5.order([colNumber, col]).draw();
                }
                for (var i = 0; i < 8; i++) {
                    if (names.indexOf('col' + i) != -1) {
                        document.getElementById("t5" + i).value = values[names.indexOf('col' + i)];
                        table5.column(i).search(values[names.indexOf('col' + i)]).draw();
                    }
                }
                if (page != "") {
                    table5.page(page).draw('page');
                }
            }
            //occurs when the compare output tab is loaded
            function compareOutputOnLoad(names, values) {
                var compare1 = values[names.indexOf('compare1')];
                var compare2 = values[names.indexOf('compare2')];
                compareOutput(compare1, compare2);
                var page3 = values[names.indexOf('page3')] - 1;
                var page4 = values[names.indexOf('page4')] - 1;
                var size3 = values[names.indexOf('size3')];
                var size4 = values[names.indexOf('size4')];
                var search3 = "";
                var search4 = "";
                var colOrder3 = "";
                var colOrder4 = "";
                if (names.indexOf('colOrder3') != -1) {
                    colOrder3 = values[names.indexOf('colOrder3')].toString();
                    var split3 = colOrder3.indexOf(',');
                    var colNumber3 = colOrder3.substring(0, split3);
                    var col3 = colOrder3.substring(split3 + 1).toString();
                }
                if (names.indexOf('colOrder4') != -1) {
                    colOrder4 = values[names.indexOf('colOrder4')].toString();
                    var split4 = colOrder4.indexOf(',');
                    var colNumber4 = colOrder4.substring(0, split4);
                    var col4 = colOrder4.substring(split4 + 1).toString();
                }
                if (names.indexOf('search3') != -1) {
                    search3 = values[names.indexOf('search3')];
                }
                if (names.indexOf('search4') != -1) {
                    search4 = values[names.indexOf('search4')];
                }
                if (size3 != "") {
                    table3.page.len(size3);
                }
                if (size4 != "") {
                    table4.page.len(size4).draw();
                }
                if (search3 != "") {
                    table3.search(search3).draw();
                }
                if (search4 != "") {
                    table4.search(search4).draw();
                }
                if (colOrder3 != "") {
                    table3.order([colNumber3, col3]).draw();
                }
                if (colOrder4 != "") {
                    table4.order([colNumber4, col4]).draw();
                }
                if (page3 != "") {
                    table3.page(page3).draw('page');
                }
                if (page4 != "") {
                    table4.page(page4).draw('page');
                }
            }
            //give the parameters in the url functionality. Go to appropriate page on load
            $(document).ready(function () {
                parseSearchParameters();
            });
        </script>
    </div>
</div>
</body>
</html>