package decrypt;

import java.io.*;
import java.security.SecureRandom;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;


/**
 * @author hjh
 * @since 2019/3/11
 */

public class AES_file {
	Key key;   
	public  AES_file(String str) {   
	    getKey(str);
	  }   

  public void getKey(String strKey) {   
    try {   
        KeyGenerator _generator = KeyGenerator.getInstance("AES");   
        _generator.init(new SecureRandom(strKey.getBytes()));   
        this.key = _generator.generateKey();   
        _generator = null;    
    } catch (Exception e) {   
        throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);   
    }   
  } 
	
	public void encryptFile(String sourcepath, String destpath) throws Exception {
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		
		File sourceFile = new File(sourcepath);
		File destFile = new File(destpath);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream =  new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(destFile);
			
			
			
			//encrypt flow
			CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
			byte[] 	cache = new byte[1024];
			int nRead = 0;
			while((nRead = cipherInputStream.read(cache))!= -1){
				outputStream.write(cache, 0, nRead);
				outputStream.flush();
			}
			cipherInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				inputStream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			try{
				outputStream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		System.out.println("Encrypt successfully!");
	}
	
	//decrypt file
	public void decryFile(String sourcePath, String destPath) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		
		File sourceFile = new File(sourcePath);
		File destFile = new File(destPath);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try{
			
			
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(destFile);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while((r = inputStream.read(buffer)) >= 0){
				cipherOutputStream.write(buffer, 0, r);
			}
			cipherOutputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				inputStream.close();
				outputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		System.out.println("Decrypt successfully!");
	}
	
	
	
	public static void main(String[] args) throws Exception{
		String sKey = "00b09e37363e596e1f25b23c78e49939238b";
		AES_file af = new AES_file(sKey);

		String sourcePath = "/tmp/SGXindex";
		String destPath = "/tmp/SGXindex_encrypt";
		String decryptPath = "/tmp/SGXindex_decrypt";
		
		af.encryptFile(sourcePath, destPath);
		af.decryFile(destPath, decryptPath);
		
	}
}
