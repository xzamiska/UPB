 <%
 if (session.getAttribute("userid") == null){
	 response.sendRedirect("index2.jsp");
 } else {
 
 try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Connection con = null;
	try {
		con = DriverManager.getConnection("jdbc:mysql://147.175.121.179:3306/skuska_denis", "root", "");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Statement st = null;
	try {
		st = con.createStatement();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
 }
 
  %>
 
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload Example in JSP and Servlet - Java web application</title>
    </head>
 
    <body> 
    <form id="fileUploadForm2" method="get" action="directory_info.jsp">
    <div>
    <button id="uploadBtn2" type="submit" class="btn btn_primary">MojeSubory</button>
	</div>
    </form>
    <button id="backToUpload" class="btn btn_primary"><a href="logout.jsp">Logout</a></button>
        <div>
            <h3> Choose File to Upload in Server </h3>
            <form id="fileUploadForm1" method="post" action="upload"
			enctype="multipart/form-data">
			<div class="form_group">
				<label>Upload File</label><span id="colon">: </span><input
					id="fileAttachment" type="file" name="fileUpload"
					multiple="multiple" /> 
					</div>
					<div class="form_group">
					<label>Choose user</label><span id="colon">: </span>
				 <select id="userName" multiple="multiple" name="userName">
               <% 
               if(request.getAttribute("logins") != null){
	               String[] namess=(String[])request.getAttribute("logins");
	                   
	                     for(int i=0; i<namess.length; i++) {
	                  %>
	                 
	                    <option value="<%=namess[i] %>"><%=namess[i] %></option>
	                
	                  <% }
                    }
                    %>
                  
                   
                
                 
               </select>
			</div>
			<button id="uploadBtn1" type="submit" class="btn btn_primary">Encrypt
				and send</button>
		</form>
                            
        </div>     
    </body>
</html>