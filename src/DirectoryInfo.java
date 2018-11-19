

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DirectoryInfo
 */

public class DirectoryInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectoryInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
		//request.setAttribute("loggedUser", cookies.getCookie(request));
		request.setAttribute("list_info", list_informacii);
		System.out.println("Bol som tu");
		//RequestDispatcher dispatcher = request.getRequestDispatcher("/directory_info.jsp");
		//dispatcher.forward(request, response);
		request.getRequestDispatcher("/directory_info.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
