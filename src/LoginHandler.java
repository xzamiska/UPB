import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class LoginHandler extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Calendar calendar = Calendar.getInstance();
		String userid = request.getParameter("uname");
		String pwd = request.getParameter("pass");

		LocalDateTime timeBan = (LocalDateTime) request.getSession().getAttribute("timeBan");
		if (timeBan != null) {
			if (timeBan.compareTo(LocalDateTime.now()) >= 0) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/timeBan.jsp");
				dispatcher.forward(request, response);
			}
		} else {
			request.removeAttribute("timeBan");
		}

		userid = userid.replaceAll("[^a-zA-Z0-9]", "");
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
			rs = st.executeQuery("select password from users where login='" + userid + "'");
		} catch (SQLException e) {
			response.sendRedirect("index2.jsp");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs.next()) {
				String passwd = rs.getString("password");
				if (CryptoUtils.verifyUserPassword(pwd, passwd) == true) {

					
					RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
					dispatcher.forward(request, response);

				} else {
					/*
					Integer failedTries = (Integer) request.getSession().getAttribute("failedTries");
					if (failedTries != null) {
						if (failedTries > 3) {
							System.out.println("setujem time ban");
							request.getSession().setAttribute("timeBan", LocalDateTime.now().plusMinutes((long) 30.0));
						}
						request.getSession().setAttribute("failedTries", failedTries + 1);
					} else {
						request.getSession().setAttribute("failedTries", 1);
					}
					*/
					response.sendRedirect("index2.jsp");
				}

				// out.println("welcome " + userid);
				// out.println("<a href='logout.jsp'>Log out</a>");

			} else {
				response.sendRedirect("index2.jsp");

				// out.println("Invalid password <a href='index.jsp'>try again</a>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
