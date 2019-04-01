package server;

//import server.DES_crypt;	
import java.io.*;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService

public class Judge {
//	DES_crypt.main();	
	static File file = new File("/tmp/SGXindex_decrypt"); 	
	static String[] strMatrix = new String[88];
	public static void inputTuple() {                         //��ʽ��ʱ����static
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bufr = new BufferedReader(fr);
			String i = "";
			int j = 0;
			while((i = bufr.readLine()) != null) {
//				System.out.println(i);
				strMatrix[j++] = i;
			}
//			for(int k = 0; strMatrix[k] != null; k++) {
//				System.out.println(strMatrix[k]);
//			}
			
			bufr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//the judgment function
	public  boolean commitReply(long counter, int[] intArray, int intTail, double[] doubleArray, int doubleTail,float[] floatArray, int floatTail, long[] longArray, int longTail, char[] charArray,int charTail, byte[] byteArray,int byteTail) {
		inputTuple();
		boolean result = true;          
		int type = 0;
		String lValue = "", rValue = "";
		String typeIndex = "", lIndex = "", rIndex = "", fIndex = "";
		typeIndex = strMatrix[(new Long(counter).intValue())*4];    //long -> int
		lIndex =  strMatrix[(new Long(counter).intValue())*4+1];
		rIndex =  strMatrix[(new Long(counter).intValue())*4+2];
		fIndex =  strMatrix[(new Long(counter).intValue())*4+3];
		
		//NullPointerException
//		if(lIndex == null)
//			System.out.println("is null");
		if(lIndex.contains("_")) {
			String[] strIndex = lIndex.split("_");          //ÿ�ζ����µ�strIndex?
			lValue = strIndex[1];
			lIndex = null;
		}
		if(rIndex.contains("_")) {
			String[] strIndex = rIndex.split("_");
			rValue = strIndex[1];
			rIndex = null;
		}
		//��������ֵ
		int lIndex_int = 0;
		int rIndex_int = 0;
		if(lIndex != null) {
			 lIndex_int = Integer.parseInt(lIndex);         //string -> int
		}
		if(rIndex != null) {
			 rIndex_int = Integer.parseInt(rIndex);  
		}
		//�ж������
//		String fIndex = null;
//
//		if(fIndex.equals("1"))
//			fIndex = "==";
//		if(fIndex.equals("2"))
//			fIndex = "!=";
//		if(fIndex.equals("3"))
//			fIndex = ">";
//		if(fIndex.equals("4"))
//			fIndex = "<";
//		if(fIndex.equals("5"))
//			fIndex = ">=";
//		if(fIndex.equals("6"))
//			fIndex = "<=";
		
		//����Ӧ����ȷ�����Ҳ�����
		switch(typeIndex) {
		case "0":
			type = 0;   
			if(lIndex != null) {
				
				lValue = Integer.toString(intArray[lIndex_int]);       //valueֵ����String����
			}
			if(rIndex != null) {
				      
				rValue = Integer.toString(intArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		case "1":
			type = 1; 
			if(lIndex != null) {       
				lValue = Double.toString(doubleArray[lIndex_int]);      
			}
			if(rIndex != null) {       
				rValue = Double.toString(doubleArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		case "2":
			type = 2; 
			if(lIndex != null) {      
				lValue = Float.toString(floatArray[lIndex_int]);      
			}
			if(rIndex != null) {       
				rValue = Float.toString(floatArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		case "3":
			type = 3; 
			if(lIndex != null) {      
				lValue = Character.toString(charArray[lIndex_int]);      
			}
			if(rIndex != null) {
				rValue = Character.toString(charArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		case "4":
			type = 4; 
			if(lIndex != null) {        
				lValue = Long.toString(longArray[lIndex_int]);      
			}
			if(rIndex != null) {       
				rValue = Long.toString(longArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		case "5":
			type = 5; 
			if(lIndex != null) {        
				lValue = Byte.toString(byteArray[lIndex_int]);      
			}
			if(rIndex != null) {       
				rValue = Byte.toString(byteArray[rIndex_int]);      
			}
			result = compare(type, lValue, rValue, fIndex);
			break;
		default:
			System.out.println("This typeIndex is wrong!");
		}
		return result;

//		if(typeStr.equals("int"))
//			typeIndex = 0;
//		else if(typeStr.equals("double"))
//			typeIndex = 1;			
//		else if(typeStr.equals("float"))
//			typeIndex = 2;
//		else if(typeStr.equals("char"))
//			typeIndex = 3;
//		else if(typeStr.equals("long"))
//			typeIndex = 4;
//		else if(typeStr.equals("byte"))
//			typeIndex = 0;
//		else //TODO: contains type object , boolean , short
//			typeIndex = 0;
	}
	
	public static boolean compare(int type, String lValue, String rValue, String fIndex) {
		//��type�����ж�
//		if(symbol.equals(" == "))                //���������
//			indexwriter("1");
//		if(symbol.equals(" != "))
//			indexwriter("2");
//		if(symbol.equals(" > "))
//			indexwriter("3");
//		if(symbol.equals(" < "))	
//			indexwriter("4");
//		if(symbol.equals(" >= "))	
//			indexwriter("5");	
//		if(symbol.equals(" <= "))
//			indexwriter("6");
		
		
		//����/������/�ַ���/���������ֱȽϷ���
		boolean result = true;
		if((type == 0)) {                                               //int�ͱȽ�
			int lValue_int = Integer.parseInt(lValue);
			int rValue_int = Integer.parseInt(rValue);
			if(fIndex.equals("1"))
				result =  lValue_int == rValue_int;
			else if(fIndex.equals("2"))
				result = lValue_int != rValue_int;
			else if(fIndex.equals("3"))
				result = lValue_int > rValue_int;
			else if(fIndex.equals("4"))
				result = lValue_int < rValue_int;
			else if(fIndex.equals("5"))
				result = lValue_int >= rValue_int;
			else if(fIndex.equals("6"))
				result = lValue_int <= rValue_int;
			
		}

		else if(type == 4) {                                      //long��
			long lValue_long = Long.parseLong(lValue);
			long rValue_long = Long.parseLong(rValue);
			if(fIndex.equals("1"))
				result = lValue_long == rValue_long;
			else if(fIndex.equals("2"))
				result = lValue_long != rValue_long;
			else if(fIndex.equals("3"))
				result = lValue_long > rValue_long;
			else if(fIndex.equals("4"))
				result = lValue_long < rValue_long;
			else if(fIndex.equals("5"))
				result = lValue_long >= rValue_long;
			else if(fIndex.equals("6"))
				result = lValue_long <= rValue_long;
		}
		else if(type == 5) {                                        //byte��
			byte lValue_byte = Byte.parseByte(lValue);
			byte rValue_byte = Byte.parseByte(rValue);
			if(fIndex.equals("1"))
				result = lValue_byte == rValue_byte;
			else if(fIndex.equals("2"))
				result = lValue_byte != rValue_byte;
			else if(fIndex.equals("3"))
				result = lValue_byte > rValue_byte;
			else if(fIndex.equals("4"))
				result = lValue_byte < rValue_byte;
			else if(fIndex.equals("5"))
				result = lValue_byte >= rValue_byte;
			else if(fIndex.equals("6"))
				result = lValue_byte <= rValue_byte;
		}
		else if(type == 1) {                                        //double��
			double lValue_double = Double.parseDouble(lValue);
			double rValue_double = Double.parseDouble(rValue);
			if(fIndex.equals("1"))
				result = Math.abs(lValue_double - rValue_double) <= 0;
			else if(fIndex.equals("2"))
				result = Math.abs(lValue_double - rValue_double) > 0;
			else if(fIndex.equals("3"))
				result = lValue_double > rValue_double;
			else if(fIndex.equals("4"))
				result = lValue_double < rValue_double;
			else if(fIndex.equals("5"))
				result = lValue_double >= rValue_double;
			else if(fIndex.equals("6"))
				result = lValue_double <= rValue_double;
		}
		else if(type == 2) {                                        //float��
			float lValue_float = Float.parseFloat(lValue);
			float rValue_float = Float.parseFloat(rValue);
			if(fIndex.equals("1"))
				result = Math.abs(lValue_float - rValue_float) <= 0;
			else if(fIndex.equals("2"))
				result = Math.abs(lValue_float - rValue_float) > 0;
			else if(fIndex.equals("3"))
				result = lValue_float > rValue_float;
			else if(fIndex.equals("4"))
				result = lValue_float < rValue_float;
			else if(fIndex.equals("5"))
				result = lValue_float >= rValue_float;
			else if(fIndex.equals("6"))
				result = lValue_float <= rValue_float;
		}
		else if(type == 3) {                                        //double��
			char lValue_char = lValue.charAt(0);
			char rValue_char = rValue.charAt(0);
			if(fIndex.equals("1"))
				result = lValue == rValue;
			else if(fIndex.equals("2"))
				result =  lValue != rValue;
			else if(fIndex.equals("3"))
				result = lValue_char > rValue_char;
			else if(fIndex.equals("4"))
				result = lValue_char < rValue_char;
			else if(fIndex.equals("5"))
				result = lValue_char >= rValue_char;
			else if(fIndex.equals("6"))
				result = lValue_char <= rValue_char;
		}
					
		return result;
		
	}
	
	public static void main(String[] args) {

//		int[] intArray = new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
//		double[] doubleArray = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
//		float[] floatArray = new float[] {1f,2f,3f,4f,5f,6f,7f,8f,9f,0f};
//		long[] longArray = new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
//		char[] charArray = new char[																												] {'A','B','C','D','E'};
//		byte[] byteArray = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
		inputTuple();
//		System.out.println(commitReply(0,intArray,10,doubleArray,10,floatArray,10,longArray,10,charArray,5,byteArray,10));   //static?
        Endpoint.publish("http://192.168.15.1:9091/service/Judge", new Judge());
        System.out.println("service success !");
		
		
    }
}
