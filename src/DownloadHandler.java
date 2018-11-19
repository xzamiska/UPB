import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadHandler
 */

public class DownloadHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @throws IOException 
	 * @throws Exception 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
			 {
		
		String uzivatel = request.getSession().getAttribute("userid").toString();
		String applicationPath = getServletContext().getRealPath("") + java.io.File.separator + "files" + java.io.File.separator + uzivatel;
		/***** Get The Absolute Path Of The File To Be Downloaded *****/
		String fileName = request.getParameter("fileName"),				
		filePath = applicationPath + File.separator + fileName;
		String fileOut = applicationPath + File.separator + "dec" + fileName;  

		String pathKey = applicationPath + File.separator + "keys" + File.separator + "privateKey.pem";
		String path = applicationPath + File.separator + "keys" + File.separator + "Key.pem";
	/*	File file = new File(filePath);

		FileInputStream fin = new FileInputStream(file);
		
		byte[] vstup = new byte[(int) file.length()];
		fin.read(vstup);
	

		
		int zaciatok = vstup.length - 256;
			
		byte[] k = new byte[256]; // kluc

		for (int i = 0; i < k.length; i++) {
			k[i] = vstup[i + zaciatok];
		}
		
		*/
		try {
		 CryptoUtils.decrypt(filePath, fileOut, pathKey, path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File desifrovany_subor = new File(fileOut);
		byte[] fileContent = Files.readAllBytes(desifrovany_subor.toPath());
	    OutputStream outStream = null;

		/**** Setting The Content Attributes For The Response Object ****/
		String mimeType = "application/octet-stream";
		response.setContentType(mimeType);

		/**** Setting The Headers For The Response Object ****/
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", desifrovany_subor.getName());
		response.setHeader(headerKey, headerValue);

		try {
			outStream = response.getOutputStream();
			outStream.write(fileContent);

		} catch (IOException ioExObj) {
		} finally {

			outStream.flush();
			if (outStream != null) {
				outStream.close();
			}
		}

	}

}
