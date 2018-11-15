<%
session.setAttribute("userid", null);
session.invalidate();
response.sendRedirect("index2.jsp");
%>