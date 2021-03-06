package decrypt;
import decrypt.DHCoder;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
public class DH_test {
	private static byte[] publicKey1;
	private static byte[] privateKey1;
	private static byte[] key1;
	private static byte[] publicKey2;
	private static byte[] privateKey2;
	private static byte[] key2;
	
	/**
	 * Initialize key
	 * @throws Exception
	 */
	public static final void initKey() throws Exception{
		//gengerate A key pair
		Map<String, Object> keyMap1 = DHCoder.initKey();
		publicKey1 = DHCoder.getPublicKey(keyMap1);
		privateKey1 = DHCoder.getPrivateKey(keyMap1);
		System.out.println("A's public key:\n" + Base64.encodeBase64String(publicKey1));
		System.out.println("A's private key:\n" + Base64.encodeBase64String(privateKey1));
		
		//Building B's local key pair from A' public key   !!!
		Map<String, Object> keyMap2 = DHCoder.initKey(publicKey1);
		publicKey2 = DHCoder.getPublicKey(keyMap2);
		privateKey2 = DHCoder.getPrivateKey(keyMap2);
		System.out.println("B's public key:\n" + Base64.encodeBase64String(publicKey2));
		System.out.println("B's private key:\n" + Base64.encodeBase64String(privateKey2));
		
		key1 = DHCoder.getSecretKey(publicKey2, privateKey1);
		System.out.println("A's local key:\n" + Base64.encodeBase64String(key1));
		key2 = DHCoder.getSecretKey(publicKey1, privateKey2);
		System.out.println("B's local key:\n" + Base64.encodeBase64String(key2));
	}
	/**
	 * main method
	 * @param args
	 * @throws Exception
	 */
	public static void  main(String[] args) throws Exception {
		initKey();
		System.out.println();
		System.out.println("---A to B---");
		String input1 = "Hello HD";
		System.out.println("Original text:\n" + input1);
		System.out.println("---encrypt data with A's local key---");
		byte[] encode1 = DHCoder.encrypt(input1.getBytes(), key1);
		System.out.println("encryption:\n" + Base64.encodeBase64String(encode1));
		
		System.out.println("---decrypt data with B's local key---");
		byte[] decode1 = DHCoder.decrypt(encode1, key2);
		
//		if(decode1.length == 0)
//			System.out.println("error");       //decode1 is empty
//		System.out.println(decode1[0]);
		
		String output1 = new String(decode1);
		
		System.out.println("decrypt:\n" + output1);
		
	}
}
