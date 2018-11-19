<%@page import="java.io.IOException"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.ArrayList"%>
 <%
 
 if (session.getAttribute("userid") == null){
	 response.sendRedirect("index2.jsp");
 } 
 String[] pole_uzivatelov = new String[1];
 
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
	ResultSet rs = null;
	
	try {

			// nacitanie uzivatelov
			ArrayList<String> uzivatelia = new ArrayList<String>();
			rs = st.executeQuery("select login from users");
	
			while (rs.next()) {
				uzivatelia.add(rs.getString("login"));
			}
			
			pole_uzivatelov = new String[uzivatelia.size()];
			for (int i = 0; i < uzivatelia.size(); i++) {
				pole_uzivatelov[i] = uzivatelia.get(i);
			}
		

		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
  %>
 
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload Example in JSP and Servlet - Java web application</title>
        <style>
        a {
        text-decoration: none;
        color: black;
        }
        </style>
    </head>
 
    <body> 
    <form id="fileUploadForm2" method="get" action="directory_info.jsp">
    <div>
    <button id="uploadBtn2" type="submit" class="btn btn_primary">MojeSubory</button>
	</div>
    </form>
    
    <button id="backToUpload" class="btn btn_primary"><a href="logout.jsp">Logout</a></button>
    
    <%
    	if(session.getAttribute("userid") != null){
    		%>
    		 <h3> Welcome <%=
     		session.getAttribute("userid").toString()
     		
     		%></h3>
    		<%
    	}
    %>
    
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
               
	               String[] namess=pole_uzivatelov;
	                   
	                     for(int i=0; i<namess.length; i++) {
	                  %>
	                 
	                    <option value="<%=namess[i] %>"><%=namess[i] %></option>
	                
	                  <% }
                    
                    %>
                  
                   
                
                 
               </select>
			</div>
			<button id="uploadBtn1" type="submit" class="btn btn_primary">Encrypt
				and send</button>
		</form>
                            
        </div>     
    </body>
</html>