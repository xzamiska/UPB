import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.OutputStream ;
import java.io.FileInputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet to handle File upload request from Client
 * @author Javin Paul
 */
public class FileUploadHandler extends HttpServlet {
    private final String UPLOAD_DIRECTORY = "C://upload";
  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        //process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                String checkbox = multiparts.get(2).getFieldName();
               
                File temp = null; //file nacitany
                File encrypted = null; // file zakodovany
                for(FileItem item : multiparts){
                    if(!item.isFormField())
                    {
                    	if(checkbox.equals("encrypt")){
                        String name = new File(item.getName()).getName();
                        item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                        temp=new File (UPLOAD_DIRECTORY + File.separator + name );
                        name = "encrypted_" + name;
                        encrypted=new File(UPLOAD_DIRECTORY + File.separator + name);
                    	}
                        else {
                        String name = new File(item.getName()).getName();
                        item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                        temp=new File (UPLOAD_DIRECTORY + File.separator + name );
                        name = "decrypted_" + name;	
                        // tu treba prisposobit este funkciu na decrypt 
                       // decrypted=new File(UPLOAD_DIRECTORY + File.separator + name);	
                        	
                        }
                        
                    }
                }
                
                CryptoUtils.encrypt(temp, encrypted );
                response.setContentType("text/plain");
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(encrypted);
                byte [] buffer = new byte [1024] ;
                int length;
                while ( (length = in.read (buffer)) > 0 )
                {
                out.write(buffer,0,length) ;
                }
                in.close();
                out.flush();
                request.setAttribute( "message " , " F i l e Uploaded S u c c e s s f u l l y " ) ;
           
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }          
         
        }else{
            request.setAttribute("message",
                                 "Sorry this Servlet only handles file upload request");
        }
    
       // request.getRequestDispatcher("/result.jsp").forward(request, response);
     
    }
  
}
