 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload Example in JSP and Servlet - Java web application</title>
    </head>
 
    <body> 
        <div>
            <h3> Choose File to Upload in Server </h3>
            <form action="upload" method="post" enctype="multipart/form-data">
             <label for="subor1">Súbor na náhratie</label>
                <input type="file" name="file" value="subor1"/><br>
                <label for="subor2">Kľúč na náhratie </label>
                  <input type="file" name="file2" value="subor2"/><br>
                  <input type="radio" name="encrypt" value="1">Encrypt<br>
  				<input type="radio" name="encrypt" value="2">Decrypt<br>
                <input type="submit" value="upload" />
            </form>          
        </div>
      
    </body>
</html>