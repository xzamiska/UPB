import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;

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
//		String path = applicationPath + File.separator + "keys" + File.separator + "Key.pem";
		
		
		// exncrypted key
		File file = new File(filePath);

		FileInputStream fin = new FileInputStream(file);
		
		byte[] vstup = new byte[(int) file.length()];
		fin.read(vstup);
		System.out.println(vstup.toString());
		
		byte[] key = Arrays.copyOfRange(vstup, vstup.length - 256, vstup.length);	
		byte[] encrypte_text = Arrays.copyOfRange(vstup, 0, vstup.length - 256);
		System.out.println(key.toString());
		System.out.println(key.length);
		System.out.println(encrypte_text.toString());
		System.out.println(encrypte_text.length);
		try {
			
		 CryptoUtils.decrypt(fileOut, pathKey, key, encrypte_text);
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
			File file_to_delete = new File(fileOut); 
			if(file_to_delete.exists()) 
	        { 
				file_to_delete.delete();
	            System.out.println("File deleted successfully"); 
	        } 
	        else
	        { 
	            System.out.println("Failed to delete the file"); 
	        } 
			outStream.flush();
			if (outStream != null) {
				outStream.close();
			}
		}

	}

}
