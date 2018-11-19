<%@ page import = "java.io.File "%>
<%@ page import = "java.io.IOException"%>
<%@ page import = "java.nio.file.Files"%>
<%@ page import = "java.nio.file.Path"%>
<%@ page import = "java.nio.file.Paths"%>
<%@ page import = "java.util.ArrayList"%>

<%@ page import = "javax.servlet.RequestDispatcher"%>
<%@ page import = "javax.servlet.ServletException"%>
<%@ page import = "javax.servlet.annotation.WebServlet"%>
<%@ page import = "javax.servlet.http.HttpServlet"%>
<%@ page import = "javax.servlet.http.HttpServletRequest"%>
<%@ page import = "javax.servlet.http.HttpServletResponse"%>
<%@ page import =  "skuska.Fileinfo"%>

<%
String uzivatel = request.getSession().getAttribute("userid").toString();
String applicationPath = getServletContext().getRealPath("") + java.io.File.separator + "files" + java.io.File.separator + uzivatel;

File fileUploadDirectory = new File(applicationPath);

File[] subory_priecninka = fileUploadDirectory.listFiles();
Fileinfo fileInfo = null;
ArrayList<Fileinfo> list_informacii = new ArrayList<>(); /// treida listov		

for (File file : subory_priecninka) {

	if (!file.getName().equals("keys")) {
		 Path pathX = Paths.get(file.getAbsolutePath());
		 System.out.println(pathX.toString());
			byte[] pole=(byte[]) Files.getAttribute(pathX, "user:kodovanie");
		      String owner=new String(pole);
		fileInfo = new Fileinfo();     
		fileInfo.setNazov_authora(owner);
		fileInfo.setNazov_suboru(file.getName());
		list_informacii.add(fileInfo);
	}
	
}

%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="download">
<table>

    <% for(Fileinfo File: list_informacii){
    	%>
    	<tr>
    	<td>Author:<%=File.getNazov_authora()%></td>
    	<td>Subor:<%=File.getNazov_suboru()%></td>
    	<td><a id="downloadLink" class="hyperLink" href="<%=request.getContextPath()%>/download?fileName=<%=File.getNazov_suboru()%>">Decrypt And Download</a></td> 
    	</tr>
    <%
   	}
    %>
</table>
</form>
</body>
</html>