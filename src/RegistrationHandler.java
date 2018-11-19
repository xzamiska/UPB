import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class RegistrationHandler extends HttpServlet {

	public RegistrationHandler() {
		super();
		// TODOAuto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODOAuto-generated method stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException, ServletException {
		String user = request.getParameter("login");
		String pwd = request.getParameter("pass");
		String fname = request.getParameter("fullName");
		
		user = user.replaceAll("[^a-zA-Z0-9]", "");
		fname = fname.replaceAll("[^a-zA-Z0-9]", "");
		Nbvcxz nbvcxz = new Nbvcxz();
		// Estimate password
		Result result = nbvcxz.estimate(pwd);
		if (!result.isMinimumEntropyMet()) {
			response.sendRedirect("index2.jsp");
		}
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://147.175.121.179:3306/skuska_denis", "root", "");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Statement st = null;
		try {
			st = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String query = "select login from users where login='" + user + "'"; // get username
		System.out.println(query);
		try {
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				String message = "Pouzivatelske prihlasovacie meno uz existuje";
				// request.getSession().setAttribute("message", message);
				// response.sendRedirect("reg.jsp");
				response.sendRedirect("reg.jsp?message=" + URLEncoder.encode(message, "UTF-8"));

			} else {
				String applicationPath = getServletContext().getRealPath("");
				String uploadPath = applicationPath + java.io.File.separator + "files" + java.io.File.separator + user
						+ java.io.File.separator + "keys";
				// String uploadPathkeys = applicationPath + java.io.File.separator + "files"
				// +java.io.File.separator+ user + java.io.File.separator+ "keys";
				System.out.println(uploadPath);
				java.io.File fileUploadDirectory = new java.io.File(uploadPath);
				String[] keys = new String[2];

				if (!fileUploadDirectory.exists()) {
					fileUploadDirectory.mkdirs();
				}
				keys = CryptoUtils.rsa_keys();
				Writer out = new FileWriter(uploadPath + "\\publicKey.pem");
				out.write("-----BEGIN PUBLIC KEY-----\n");
				out.write(keys[0]);
				out.write("\n-----END PUBLIC KEY-----\n");
				out.close();
				out = new FileWriter(uploadPath + "\\privateKey.pem");
				out.write("-----BEGIN PRIVATE KEY-----\n");
				out.write(keys[1]);
				out.write("\n-----END PRIVATE KEY-----\n");
				out.close();
				String salt = CryptoUtils.getSalt(16);
				System.out.println(salt);
				String mySecurePassword = CryptoUtils.generateSecurePassword(pwd, salt);
				System.out.println(mySecurePassword);
				// ResultSet rs;
				int i = st.executeUpdate("insert into users (login, fullName, password) " + "values ('" + user + "','"
						+ fname + "','" + mySecurePassword + "')");
				if (i > 0) {
					request.getSession().setAttribute("userid", user);
					// nacitanie uzivatelov
					ArrayList<String> uzivatelia = new ArrayList<String>();
					rs = st.executeQuery("select login from users");

					while (rs.next()) {
						uzivatelia.add(rs.getString("login"));
					}

					String[] pole_uzivatelov = new String[uzivatelia.size()];

					for (int x = 0; x < uzivatelia.size(); x++) {
						pole_uzivatelov[x] = uzivatelia.get(x);
					}
					System.out.println(pole_uzivatelov.length);
					request.setAttribute("logins", pole_uzivatelov);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
					dispatcher.forward(request, response);
					// out.print("Registration Successfull!"+"<a href='index.jsp'>Go to Login</a>");
				} else {
					response.sendRedirect("index2.jsp");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
