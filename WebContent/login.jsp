<%@ page import ="java.sql.*" %>
<%
    String userid = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    Class.forName("com.mysql.jdbc.Driver");  
    //lala
    Connection con=DriverManager.getConnection("jdbc:mysql://147.175.121.179:3306/skuska_denis","root","");
    Statement st = con.createStatement();
    ResultSet rs;
    rs = st.executeQuery("select * from users where login='" + userid + "' and password='" + pwd + "'");
    if (rs.next()) {
        session.setAttribute("userid", userid);
        //out.println("welcome " + userid);
        //out.println("<a href='logout.jsp'>Log out</a>");
        response.sendRedirect("success.jsp");
    } else {
        out.println("Invalid password <a href='index.jsp'>try again</a>");
    }
%>