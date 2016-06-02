<html>
<head><title>First JSP</title></head>
<body>

String inputfile = request.getParameter("inputfile");
String desiredLocation = request.getParameter("desiredLocation");
String execute = request.getParameter("execute");
String unpack = request.getParameter("unpack");

out.write(inputfile);
out.write(desiredLocation);
out.write(execute);
out.write(unpack);


</body>
</html>