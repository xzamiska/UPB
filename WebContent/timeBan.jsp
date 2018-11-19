<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>You have BAN!</title>
</head>
<body>
<h1>
	You have ban bcs of too many failed loggins. 
	Time is until : <%= session.getAttribute("timeBan").toString()%>
</h1>
</body>
</html>