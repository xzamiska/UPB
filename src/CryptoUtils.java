import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.FileUtils;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.org.apache.xerces.internal.util.URI;

public class CryptoUtils {

	// private static SecretKeySpec secretKey ; // Objekt, ktorý obsahuje tajný kľuč sychronnej sifry AES
	    
	    // funkcia ktora vytvori zahashovany secretKey pre AES sifru 
	   /* public static void setKey(SecretKey myKey){
	        MessageDigest sha = null;
	        byte[] key; // pomocna premenna
	        try {
	            sha = MessageDigest.getInstance("SHA-256"); 
	            key = sha.digest(myKey.getEncoded()); // zhashovany key pomocou SHA-256
	            key = Arrays.copyOf(key, 16); // naša AES sifra sifruje bloky dlžky 16bytov preto zobereme len prve 16 byti zahashovaneho kluca	
	            secretKey = new SecretKeySpec(key, "AES"); // vytvoreny interface pre nas zahashovany kluc    
	        }
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            }	 
	    }*/
	    
	    // Funkcia, ktorá vracia načitany publickey zo suboru publickey.pem 
	  /*  public static PublicKey loadPublicKey() throws Exception {
	        // načitanie suboru ako string
	        String publicKeyPEM = FileUtils.readFileToString(new File("publickey.pem"), StandardCharsets.UTF_8);
	        // vymazanie headru a footeru a bielych znakov z klucu
	        publicKeyPEM = publicKeyPEM
	            .replace("-----BEGIN PUBLIC KEY-----", "")
	            .replace("-----END PUBLIC KEY-----", "")
	            .replaceAll("\\s", "");

	        //dekodaovanie dat do binarnej podoby, ktore boli zakodovane cez base64 ... ocakavame ze privatny kluc ktory nacitavame je v takejto podobe
	        byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // vytvorenie objektu keyFactory
	        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER)); // vytvorenie objektu publickKey a teda nas nacitany publicKey
	        
	    return publicKey;
	    }*/
	    
	    // Funkcia, ktorá vracia načitaný private key zo suboru privatekey-pkcs8.pem
	  /*  public static PrivateKey loadPrivateKey() throws Exception {
	        // načiatanie suboru ako string
	        String privateKeyPEM = FileUtils.readFileToString(new File("privatekey-pkcs8.pem"), StandardCharsets.UTF_8);
	        // vymazanie headru a footeru a bielych znakov z klucu 
	        privateKeyPEM = privateKeyPEM
	            .replace("-----BEGIN PRIVATE KEY-----", "")
	            .replace("-----END PRIVATE KEY-----", "")
	            .replaceAll("\\s", "");
	        // dekodaovanie dat do binarnej podoby, ktore boli zakodovane cez base64 ... ocakavame ze privatny kluc ktory nacitavame je v takejto podobe
	        byte[] privateKeyDER = Base64.getDecoder().decode(privateKeyPEM);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // vytvorenie objektu keyFactory
	        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDER)); // vytvorenie objektu privateKey a teda nas nacitany privateKey
	    
	    return privateKey;
	    }*/
	    
	    // Funkcia, ktorá načítava údaje z fileName a šifruje ich do fileOut
	    // Načitáva a zapisáva postupne po 1024 bytov pretože by došlo k preťaženiu
	    public static void encrypt (File fileName, File fileOut) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, URISyntaxException, IOException
	    {
	    	KeyGenerator kgen = KeyGenerator.getInstance("AES"); // vytvorenie instancie kgen
	    	SecretKey skey = kgen.generateKey(); // vytvorenie náhodneho 16 bytoveho sychronného kĽúča
	    	MessageDigest sha = null;
	    	//SecretKeySpec secretKey;
		    byte[] key; // pomocna premenna
		    sha = MessageDigest.getInstance("SHA-256"); 
		    key = sha.digest(skey.getEncoded()); // zhashovany key pomocou SHA-256
		    key = Arrays.copyOf(key, 16); // naša AES sifra sifruje bloky dlžky 16bytov preto zobereme len prve 16 byti zahashovaneho kluca	
		      // vytvoreny interface pre nas zahashovany kluc    
		    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // vytvorenie instancie AES sifry s ECB/PKCS5PADDING = sifrovanie blokov
		    cipher.init(Cipher.ENCRYPT_MODE, secretKey); // inicializacia sifry na encrypt mode
		    //String aha = fileName.getPath();
		   
		    Path file_path = Paths.get(fileName.getPath()).toAbsolutePath();
		    Files.setAttribute(file_path, "key:hidden", secretKey);
		    try
	        {    
	        	
	            try (FileInputStream in = new FileInputStream(fileName);
	            FileOutputStream out = new FileOutputStream(fileOut))
	            {
	                byte[] ibuf = new byte[1024];
	                int len;
	                while ((len = in.read(ibuf)) != -1)
	                {
	                    byte[] obuf = cipher.update(ibuf, 0, len);
	                    if ( obuf != null ) 
	                        out.write(obuf);
	                }
	                byte[] obuf = cipher.doFinal();
	                if ( obuf != null ) 
	                    out.write(obuf);
	            } 
	        }
	        catch (Exception e)
	        {
	            System.out.println("Error while encrypting: "+e.toString());
	        }
	    }

	    // Funkcia, ktorá načítava údaje z fileName a dešifruje ich do fileOut
	    // Načitáva a zapisáva postupne po 1024 bytov pretože by došlo k preťaženiu 
	   /* public static void decrypt(Cipher cipher, String fileName, String fileOut)
	    {
	        try
	        {
	            try (FileInputStream in = new FileInputStream(fileName);
	            FileOutputStream out = new FileOutputStream(fileOut)) 
	            {
	                byte[] ibuf = new byte[1024];
	                int len;
	                while ((len = in.read(ibuf)) != -1)
	                {
	                    byte[] obuf = cipher.update(ibuf, 0, len);
	                    if ( obuf != null ) 
	                        out.write(obuf);
	                }
	                byte[] obuf = cipher.doFinal();
	                if ( obuf != null ) 
	                    out.write(obuf);
	            }    
	        }
	        catch (Exception e)
	        {
	            System.out.println("Error while encrypting: "+e.toString());
	        }
	    }*/
	    
	   /* public static void main(String args[]) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, Exception
	    {
	        long startTime = System.nanoTime(); // časovač
	        //rsa_keys();
	        String fileName = "test.txt";
	        String fileOut = "encrypted.txt";
	        KeyGenerator kgen = KeyGenerator.getInstance("AES"); // vytvorenie instancie kgen
	        SecretKey skey = kgen.generateKey(); // vytvorenie náhodneho 16 bytoveho sychronného kĽúča
	        CryptoUtils.setKey(skey); // zahashovany AES kľúč
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // vytvorenie instancie AES sifry s ECB/PKCS5PADDING = sifrovanie blokov
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // inicializacia sifry na encrypt mode
	        CryptoUtils.encrypt(cipher, fileName, fileOut); // sifrovanie suboru pomocou AES algoritmu
	        
	        //******* Malo by to byt tak, že uživateľ vloží subor a ak stlačí button sifrovanie tak by ho malo popytat PUBLIC KEY,
	        //        ktorý vloží tak začne šifrovanie s normalny symetrickym kľučom cez AES a ked sa dokončí šifrovanie tak by malo začať
	        //        šifrovanie symetrickeho kľúča asymetrickou sifrou RSA pomocou PUBLIC KEY, a ten kluč ma byt pripojeny citujem zo zadania
	        //        " (napr. v rámci hlavičky súboru s metadatami potrebnými k dešifrovaniu)"
	                
	        Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        PublicKey publicKey = loadPublicKey();
	        cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] encrypted = cipher2.doFinal(secretKey.getEncoded()); // zašifrovany kľúč ... neviem ako to je s requestom ale ak by sa posielal ako hlavicka tak by mal byt encodnuty cez Base64
	        FileUtils.writeByteArrayToFile(new File("kluc.txt"), encrypted);
	        // no a ulozit ho niekam 
	        //The SecretKey interface was extended to include Destroyable in Java 8 but no implementation was provided for destroy to the concrete SecretKeySpec class.
	        // secretKey.destroy();
	        // kludne by sme mohli dat ze secretKey.destroy() kvoli bezpecnosti a uz len pri requeste ze Dekodovanie načitať Private key a nejako ten kluc co sme zasifrovali a teda desifrovat kluč ale destroy nefunguje
	        PrivateKey privateKey = loadPrivateKey();
	        cipher2.init(Cipher.DECRYPT_MODE, privateKey);
	        // teraz ho nacitat z nieoho a vlozit do cipher2.doFinal(encrypted)
	        File file = new File("kluc.txt");
	        byte[] decrypted = cipher2.doFinal(FileUtils.readFileToByteArray(file)); // desifrovany kluč a tento vložime do instancie secretKey
	        secretKey = new SecretKeySpec(decrypted, "AES");
	       // a uz nasleduje desifrovanie suboru pomocou AES
	        fileName = "encrypted.txt";
	        fileOut = "decrypted.txt";
	        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        CryptoUtils.decrypt(cipher, fileName, fileOut);
	  
	        // System.out.println("ClearText: " + new String(secretKey.getEncoded(), StandardCharsets.UTF_8));
	        // System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
	        // System.out.println("ClearText length: " + key.length);
	        // System.out.println("Encrypted length: " + encrypted.length);
	        // System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
	                
	        long endTime   = System.nanoTime();
	        long totalTime = endTime - startTime;
	        double seconds = (double)totalTime / 1_000_000_000.0;
	        System.out.println(seconds);
	    }*/
}
