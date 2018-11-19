import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.oreilly.servlet.multipart.Part;

/**
 * Servlet to handle File upload request from Client
 * 
 * @author Javin Paul
 */


public class FileUploadHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private final String UPLOAD_DIRECTORY = "C://upload";

	public FileUploadHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = null;
		
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<FileItem> iter = items.iterator();
		File temp = null; // file nacitany
		File encrypted = null; // file zakodovany
		String UPLOAD_DIRECTORY = getServletContext().getRealPath("");
		String value = null;
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (item.isFormField()) {
			     value = item.getString();
			}			
		}
		Iterator<FileItem> iter2 = items.iterator();
		String name = null;
		while (iter2.hasNext()) {
			FileItem item = iter2.next();
			if (!item.isFormField()) {
			
			 name = new File(item.getName()).getName();
			try {
				item.write(new File(UPLOAD_DIRECTORY + java.io.File.separator + "files" + java.io.File.separator + value + java.io.File.separator + name));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			temp = new File(UPLOAD_DIRECTORY + java.io.File.separator + "files" + java.io.File.separator + value + java.io.File.separator + name);
			encrypted = new File(UPLOAD_DIRECTORY + java.io.File.separator + "files" + java.io.File.separator + value + java.io.File.separator + name);
			}
		}
		try {
			String path = UPLOAD_DIRECTORY + java.io.File.separator + "files" + java.io.File.separator + value
					+ java.io.File.separator + "keys";			
			CryptoUtils.encrypt(temp.getPath(), encrypted.getPath(), path);			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*response.setContentType("text/plain");
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(encrypted);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.flush();*/
		String a = request.getSession().getAttribute("userid").toString();
		Path path = Paths.get(UPLOAD_DIRECTORY + java.io.File.separator + "files" + java.io.File.separator + value + java.io.File.separator + name);
		byte[] obj =a.getBytes(); 
		Files.setAttribute(path,"user:kodovanie", obj);
		
		// databaza
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
		ArrayList<String> uzivatelia = new ArrayList<String>();
		try {
			rs = st.executeQuery("select login from users");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				uzivatelia.add(rs.getString("login"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] pole_uzivatelov = new String[uzivatelia.size()];

		for (int x = 0; x < uzivatelia.size(); x++) {
			pole_uzivatelov[x] = uzivatelia.get(x);
		}
		request.setAttribute("logins", pole_uzivatelov);
		
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

}
