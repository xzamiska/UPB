<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("login");    
    String pwd = request.getParameter("pass");
    String fname = request.getParameter("fullName");
    String radio = request.getParameter("encrypt");
    Class.forName("com.mysql.jdbc.Driver");  
    Connection con = DriverManager.getConnection("jdbc:mysql://147.175.121.179:3306/skuska_denis",
            "root", "");
    Statement st = con.createStatement();
    //ResultSet rs;
    int i = st.executeUpdate("insert into users (login, fullName, password) " + "values ('" + user + "','" + fname + "','" + pwd + "')");
    if (i > 0) {
        //session.setAttribute("userid", user);
        response.sendRedirect("welcome.jsp");
       // out.print("Registration Successfull!"+"<a href='index.jsp'>Go to Login</a>");
    } else {
        response.sendRedirect("index2.jsp");
    }
%>