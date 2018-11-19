import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

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

		String userid = request.getParameter("uname");
		String pwd = request.getParameter("pass");
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

					// nacitanie uzivatelov
					ArrayList<String> uzivatelia = new ArrayList<String>();
					rs = st.executeQuery("select login from users");

					while (rs.next()) {
						uzivatelia.add(rs.getString("login"));
					}

					String[] pole_uzivatelov = new String[uzivatelia.size()];

					for (int i = 0; i < uzivatelia.size(); i++) {
						pole_uzivatelov[i] = uzivatelia.get(i);
					}
					System.out.println(pole_uzivatelov.length);
					request.setAttribute("logins", pole_uzivatelov);
					request.getSession().setAttribute("userid", userid);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
					dispatcher.forward(request, response);

				} else {
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
