package decrypt;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.omg.PortableServer.AdapterActivator;

import decrypt.AES_file_DH;

/**
 * socket server
 * @author hjh
 *
 */
public class AES_file_DH_server {
	public AES_file_DH_server() throws Exception{   
	}
	static byte[] serverPublicKey = null;
	static byte[] clientPublicKey = new byte[230];   
	static byte[] serverPrivateKey = null;
	static byte[] localKey = null;
	SecretKey key;   

//	try{
//		AES_file_DH ad = new AES_file_DH();
//	}catch(Exception e){
//		e.printStackTrace();
//	}
	
	
	public static void main(String[] args) throws Exception{
		String destPath = "/tmp/SGXindex_encrypt";
		String decryptPath = "/tmp/SGXindex_decrypt";
		
		//initial bind listen
		ServerSocket serverSocket = new ServerSocket(8888);
		//accept
		System.out.println("******server is going to begin!******");
		Socket socket = serverSocket.accept();
		//judge connection accessful ???
		
		//always listen
		
		
		
		//input
		InputStream in = socket.getInputStream();
//		InputStreamReader inputStreamReader = new InputStreamReader(in);
		DataInputStream dis = new DataInputStream(in);
//		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		int flagLength = dis.readInt();
		System.out.println("flagLength: " + flagLength);
		if(flagLength == 0)
			dis.readFully(clientPublicKey, 0, 226);
		else {
			dis.readFully(clientPublicKey, 0, 227);
		}
		
		
//		dis.readFully(clientPublicKey,0,226);
//		try{
//			dis.readFully(clientPublicKey, 0, 226);
//		}catch(EOFException e){
//			e.printStackTrace();
//		}finally{
//			dis.readFully(clientPublicKey, 0, 227);
//		}
		
		while(clientPublicKey != null){     //(clientPublicKey = bufferedReader.readLine()) != null
			System.out.println("Cilent public key:\n" + Base64.encodeBase64String(clientPublicKey));    //???
			//decrypt
			initKey();

			
//			decryFile(destPath, decryptPath);
			break;
		}
		socket.shutdownInput();
		
		//write
		OutputStream outputStream = socket.getOutputStream();
//		PrintWriter printWriter = new PrintWriter(outputStream);
		DataOutputStream dosDataOutputStream = new DataOutputStream(outputStream);
		
		//getserverPublicKey length
		int spkLength = serverPublicKey.length;
		System.out.println("spkLength is: " + spkLength);
		
		if(spkLength == 226){
			dosDataOutputStream.writeInt(0);
		}
		else {
			dosDataOutputStream.writeInt(1);
		}
		
		dosDataOutputStream.write(serverPublicKey, 0, serverPublicKey.length);
		
		dosDataOutputStream.flush();
		socket.shutdownOutput();
		
		decryFile(destPath, decryptPath);
		//close
		dosDataOutputStream.close();
		outputStream.close();
//		bufferedReader.close();
//		inputStreamReader.close();
		dis.close();
		socket.close();
		serverSocket.close();
	}

	



//initial local key
public static  void initKey() throws Exception{
	//gengerate A key pair
	Map<String, Object> keyMap1 = DHCoder.initKey();
	serverPublicKey = DHCoder.getPublicKey(keyMap1);
	serverPrivateKey = DHCoder.getPrivateKey(keyMap1);
	
	System.out.println("serverPublicKey:\n" + Base64.encodeBase64String(serverPublicKey));
	System.out.println("serverPrivateKey:\n" + Base64.encodeBase64String(serverPrivateKey));
	
	//Building Server's local key pair from Client's public key   !!!
	
	
	localKey = DHCoder.getSecretKey(clientPublicKey, serverPrivateKey);    
	System.out.println("server's local key:\n" + Base64.encodeBase64String(localKey));
}


//decrypt file
public static void decryFile(String sourcePath, String destPath) throws Exception{
//			Cipher cipher = Cipher.getInstance("AES");
//			cipher.init(Cipher.DECRYPT_MODE, key);
	SecretKey secretKey = new SecretKeySpec(localKey, "AES");
	Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
	cipher.init(Cipher.DECRYPT_MODE, secretKey);
	
	File sourceFile0 = new File(sourcePath);
	while(!sourceFile0.exists()){
		Thread.currentThread();
		Thread.sleep(10000);  //write transmit
	}
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
}
	
